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

    public SelectorTokenizer(String text) {
        this.input = new TextInput(text);
    }

    @Override
    public Token nextToken() {
        int index = currentIndex = nextIndex;
        currentToken = Token.UNKNOWN;

        if (!input.hasChar(index)) {
            return Token.EOI;
        }

        int c = input.charAt(index);
        switch (c) {
        case '.':
            newToken(Token.PERIOD);
            return currentToken;
        case '[':
            newToken(Token.OPENING_BRACKET);
            return currentToken;
        case ']':
            newToken(Token.CLOSING_BRACKET);
            return currentToken;
        }

        if (string(index) ||
            equalityOperator(index) ||
            separator(index) ||
            hash(index) ||
            identity(index)
            ) {
            return currentToken;
        }

        if (c == '*') {
            newToken(Token.ASTERISK);
        }

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

    // tokens

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
            if (c < 0) {
                return false;
            } else if (c == '\\') {
                String matched = input.match(STRING_ESCAPE_PATTERN, index);
                if (matched != null) {
                    b.append(matched);
                    index += matched.length();
                } else {
                    return false;
                }
            } else if ("\n\r\f".indexOf(c) >= 0) {
                return false;
            } else {
                b.append((char)c);
                index++;
                if (c == q) {
                    break;
                }
            }
        }
        return newToken(Token.of(TokenCategory.STRING, b.toString()));
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
        } else if ("+>~,".indexOf(c) < 0) {
            return false;
        }

        if ("+>~,".indexOf(c) >= 0) {
            while (isWhitespace(input.charAt(++index))) {
                ++length;
            }
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
        case -1:
            return newToken(Token.EOI, --length);
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
        String rawText = "#" + name(index);
        return newToken(Token.of(TokenCategory.HASH, rawText));
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
        return newToken(Token.of(TokenCategory.IDENTITY, name(index)));
    }

    // helper

    private String name(int index) {
        StringBuilder b = new StringBuilder();
        int c;
        while ((c = input.charAt(index)) != -1) {
            if (c == '\\') {
                String escape = escaped(index);
                if (escape == null) {
                    break;
                }
                b.append(escape);
                index += escape.length();
            } else if (isNameLetter(c)) {
                b.append((char)c);
                ++index;
            } else {
                break;
            }
        }
        return b.toString();
    }

    private String escaped(int index) {
        return input.match(ESCAPE_PATTERN, index);
    }

    private boolean newToken(Token token) {
        int length = token.getRawText().length();
        return newToken(token, length);
    }

    private boolean newToken(Token token, int length) {
        this.currentToken = token;
        this.nextIndex = this.currentIndex + length;
        return true;
    }
}
