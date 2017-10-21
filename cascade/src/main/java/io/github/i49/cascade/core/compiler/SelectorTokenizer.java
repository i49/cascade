/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.i49.cascade.core.compiler;

import static io.github.i49.cascade.core.compiler.Letters.*;

/**
 * The tokenizer for CSS selector expression.
 */
class SelectorTokenizer implements Tokenizer {

    private final TextInput input;

    private int currentIndex;
    private Token currentToken;
    private int currentTokenLength;

    public SelectorTokenizer(String text) {
        this.input = new TextInput(text);
    }

    @Override
    public String getInput() {
        return input.getText();
    }

    @Override
    public Token nextToken() {
        currentToken = null;
        
        skipComments();
        if (currentToken == null) {
            fetchToken();
        }
        
        if (currentToken == null) {
            newToken(TokenCategory.UNKNOWN, "");
        }
        currentIndex += currentTokenLength;
        currentTokenLength = 0;
        return currentToken;
    }
    
    /**
     * Skips comments.
     */
    private void skipComments() {
        int index = currentIndex;
        while (comment(index) && currentToken.getCategory() == TokenCategory.COMMENT) {
            currentIndex += currentTokenLength;
            currentTokenLength = 0;
            currentToken = null;
            index = currentIndex;
        }
    }

    private boolean fetchToken() {

        int index = currentIndex;

        if (!input.hasChar(index)) {
            return newToken(TokenCategory.EOI, "");
        }

        if (number(index)) {
            dimension(index, currentToken);
            return true;
        }

        int c = input.charAt(index);
        switch (c) {
        case '.':
            return newToken(TokenCategory.PERIOD, ".");
        case '[':
            return newToken(TokenCategory.OPENING_BRACKET, "[");
        case ']':
            return newToken(TokenCategory.CLOSING_BRACKET, "]");
        case ':':
            return newToken(TokenCategory.COLON, ":");
        case '-':
            return newToken(TokenCategory.MINUS, "-");
        }

        if (string(index) ||
            equalityOperator(index) ||
            separator(index) ||
            hash(index) ||
            identity(index)
            ) {

            if (currentToken.getCategory() == TokenCategory.IDENTITY) {
                function(index, currentToken);
            }
            return true;
        }

        if (c == '*') {
            newToken(TokenCategory.ASTERISK, "*");
        } else if (c == '|') {
            newToken(TokenCategory.VERTICAL_BAR, "|");
        }

        return true;
    }
    
    // token generators

    private boolean comment(int index) {
        if (input.charAt(index) == '/' && input.charAt(index + 1) == '*') {
            index += 2;
        } else {
            return false;
        }
        StringBuilder b = new StringBuilder("/*");
        int c = input.charAt(index);
        while (c >= 0) {
            if (c == '*' && input.charAt(index + 1) == '/') {
                return newToken(TokenCategory.COMMENT, b.append("*/").toString());
            }
            b.append((char)c);
            c = input.charAt(++index);
        }
        return newToken(TokenCategory.INVALID_COMMENT, b.toString());
    }
    
    private boolean number(int index) {
        int c = input.charAt(index);
        if (c == '.') {
            if (!isDigit(input.charAt(index + 1))) {
                return false;
            }
        } else if (!isDigit(c)) {
            return false;
        }
        String text = input.match(index, NUMBER_PATTERN);
        return newToken(TokenCategory.NUMBER, text);
    }

    private boolean dimension(int index, Token number) {
        index += number.getRawText().length();
        if (!identity(index)) {
            return false;
        }
        String text = number.getRawText() + currentToken.getRawText();
        return newToken(TokenCategory.DIMENSION, text);
    }

    private boolean string(int index) {
        int q = input.charAt(index);
        if (!isQuote(q)) {
            return false;
        }
        StringBuilder b = new StringBuilder();
        b.append((char)q);
        index++;
        for (;;) {
            int c = input.charAt(index);
            if (c < 0 || c == '\n' || c == '\r' || c == '\f') {
                return newToken(TokenCategory.INVALID_STRING, b.toString());
            } else if (c == '\\') {
                String matched = input.match(index, STRING_ESCAPE_PATTERN);
                if (matched != null) {
                    b.append(matched);
                    index += matched.length();
                } else {
                    return false;
                }
            } else {
                b.append((char)c);
                index++;
                if (c == q) {
                    break;
                }
            }
        }
        return newToken(TokenCategory.STRING, b.toString());
    }

    private boolean equalityOperator(int index) {
        int c = input.charAt(index);
        if (c == '=') {
            return newToken(TokenCategory.EXACT_MATCH, "=");
        } else if ("~|^$*".indexOf(c) >= 0) {
            if (input.charAt(index + 1) != '=') {
                return false;
            }
            switch (c) {
            case '~':
                return newToken(TokenCategory.INCLUDES, "~=");
            case '|':
                return newToken(TokenCategory.DASH_MATCH, "|=");
            case '^':
                return newToken(TokenCategory.PREFIX_MATCH, "^=");
            case '$':
                return newToken(TokenCategory.SUFFIX_MATCH, "$=");
            case '*':
                return newToken(TokenCategory.SUBSTRING_MATCH, "*=");
            }
            return false;
        }
        return false;
    }

    private boolean separator(int index) {
        int c = input.charAt(index);
        int length = 1;
        if (isWhitespace(c)) {
            do {
                c = input.charAt(++index);
                length++;
            } while (isWhitespace(c));
        } else if ("+>~,)".indexOf(c) < 0) {
            return false;
        }

        switch (c) {
        case '+':
            return newToken(TokenCategory.PLUS, "+", length);
        case '>':
            return newToken(TokenCategory.GREATER, ">", length);
        case '~':
            return newToken(TokenCategory.TILDE, "~", length);
        case ',':
            return newToken(TokenCategory.COMMA, ",", length);
        case ')':
            return newToken(TokenCategory.CLOSING_PARENTHESIS, ")", length);
        default:
            return newToken(TokenCategory.SPACE, " ", --length);
        }
    }

    private boolean hash(int index) {
        int c = input.charAt(index);
        if (c != '#') {
            return false;
        }
        c = input.charAt(++index);
        if (c == '\\') {
            if (escaped(index) == null) {
                return false;
            }
        } else if (!isNameLetter(c)) {
            return false;
        }
        StringBuilder b = new StringBuilder("#");
        name(index, b);
        return newToken(TokenCategory.HASH, b.toString());
    }

    private boolean identity(int index) {
        int c = input.charAt(index);
        if (c == '\\') {
            if (escaped(index) == null) {
                return false;
            }
        } else if (!isIdentifierStart(c)) {
            return false;
        }
        StringBuilder b = new StringBuilder();
        name(index, b);
        return newToken(TokenCategory.IDENTITY, b.toString());
    }

    private boolean function(int index, Token identity) {
        index += identity.getRawText().length();
        int c = input.charAt(index);
        if (c != '(') {
            return false;
        }
        String text = identity.getRawText() + "(";
        return newToken(TokenCategory.FUNCTION, text);
    }

    // helper methods

    private int name(int index, StringBuilder builder) {
        int c;
        while ((c = input.charAt(index)) != -1) {
            if (c == '\\') {
                String escape = escaped(index);
                if (escape == null) {
                    break;
                }
                builder.append(escape);
                index += escape.length();
            } else if (isNameLetter(c)) {
                builder.append((char)c);
                ++index;
            } else {
                break;
            }
        }
        return index;
    }

    private String escaped(int index) {
        return input.match(index, ESCAPE_PATTERN);
    }

    private boolean newToken(TokenCategory category, String text) {
        return newToken(category, text, text.length());
    }

    private boolean newToken(TokenCategory category, String text, int length) {
        this.currentToken = Token.create(category, text, this.currentIndex);
        this.currentTokenLength = length;
        return true;
    }
}
