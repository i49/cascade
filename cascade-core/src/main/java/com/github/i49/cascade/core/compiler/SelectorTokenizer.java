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

package com.github.i49.cascade.core.compiler;

import static com.github.i49.cascade.core.compiler.Letters.*;

/**
 * Toeknizer for CSS selector expression.
 */
class SelectorTokenizer implements Tokenizer {

    private final TextInput input;

    private int currentIndex;
    private int nextIndex;
    private Token currentToken;
    private int currentTokenLength;

    public SelectorTokenizer(String text) {
        this.input = new TextInput(text);
    }

    @Override
    public Token nextToken() {
        fetchToken();
        nextIndex = currentIndex + currentTokenLength;
        currentTokenLength = 0;
        return currentToken;
    }

    @Override
    public String getInput() {
        return input.getText();
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    private boolean fetchToken() {
        // default token
        currentToken = Token.UNKNOWN;

        int index = currentIndex = nextIndex;

        if (!input.hasChar(index)) {
            return newToken(Token.EOI);
        }

        if (number(index)) {
            dimension(index, currentToken);
            return true;
        }

        int c = input.charAt(index);
        switch (c) {
        case '.':
            return newToken(Token.PERIOD);
        case '[':
            return newToken(Token.OPENING_BRACKET);
        case ']':
            return newToken(Token.CLOSING_BRACKET);
        case ':':
            return newToken(Token.COLON);
        case '-':
            return newToken(Token.MINUS);
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
            newToken(Token.ASTERISK);
        } else if (c == '|') {
            newToken(Token.VERTICAL_BAR);
        }

        return true;
    }

    // token generators

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
        return newToken(Token.create(TokenCategory.NUMBER, text));
    }

    private boolean dimension(int index, Token number) {
        index += number.getRawText().length();
        if (!identity(index)) {
            return false;
        }
        String text = number.getRawText() + currentToken.getRawText();
        return newToken(Token.create(TokenCategory.DIMENSION, text));
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
                return newToken(Token.create(TokenCategory.INVALID_STRING, b.toString()));
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
        return newToken(Token.create(TokenCategory.STRING, b.toString()));
    }

    private boolean equalityOperator(int index) {
        int c = input.charAt(index);
        if (c == '=') {
            return newToken(Token.EXACT_MATCH);
        } else if ("~|^$*".indexOf(c) >= 0) {
            if (input.charAt(index + 1) != '=') {
                return false;
            }
            switch (c) {
            case '~':
                return newToken(Token.INCLUDES);
            case '|':
                return newToken(Token.DASH_MATCH);
            case '^':
                return newToken(Token.PREFIX_MATCH);
            case '$':
                return newToken(Token.SUFFIX_MATCH);
            case '*':
                return newToken(Token.SUBSTRING_MATCH);
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
            return newToken(Token.PLUS, length);
        case '>':
            return newToken(Token.GREATER, length);
        case '~':
            return newToken(Token.TILDE, length);
        case ',':
            return newToken(Token.COMMA, length);
        case ')':
            return newToken(Token.CLOSING_PARENTHESIS, length);
        default:
            return newToken(Token.SPACE, --length);
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
        return newToken(Token.create(TokenCategory.HASH, b.toString()));
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
        return newToken(Token.create(TokenCategory.IDENTITY, b.toString()));
    }

    private boolean function(int index, Token identity) {
        index += identity.getRawText().length();
        int c = input.charAt(index);
        if (c != '(') {
            return false;
        }
        String text = identity.getRawText() + "(";
        return newToken(Token.create(TokenCategory.FUNCTION, text));
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

    private boolean newToken(Token token) {
        int length = token.getRawText().length();
        return newToken(token, length);
    }

    private boolean newToken(Token token, int length) {
        this.currentToken = token;
        this.currentTokenLength = length;
        return true;
    }
}
