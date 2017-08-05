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

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

/**
 * Tokenizer for parsing CSS selector expression into classified tokens.
 */
public class Tokenizer {

    private final String input;
    private int currentIndex;
    private int nextIndex;

    public Tokenizer(String input) {
        this.input = input;
        this.nextIndex = 0;
        this.currentIndex = 0;
        // Skips leading spaces.
        skipSpaces();
    }

    public Token nextToken() {
        this.currentIndex = this.nextIndex;
        int c = nextChar();
        if (c < 0) {
            return Token.EOI;
        } else if (c == '.') {
            return Token.PERIOD;
        } else if (c == '[') {
            return Token.OPENING_BRACKET;
        } else if (c == ']') {
            return Token.CLOSING_BRACKET;
        } else if (c == '\"' || c == '\'') {
            return string(c);
        } else if (isEqualSign(c)) {
            return equalSign(c);
        } else if (isSeparator(c)) {
            return separator(c);
        } else if (isHash(c)) {
            return Token.of(TokenCategory.HASH, hash());
        } else if (isIdent(c)) {
            return Token.of(TokenCategory.IDENTITY, ident(c));
        } if (c == '*') {
            return Token.ASTERISK;
        }
        return Token.UNKNOWN;
    }

    public String getInput() {
        return input;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    private boolean isIdent(int c) {
        if (c == '-') {
            int pos = current();
            c = nextChar();
            rewind(pos);
        }
        return isFirstLetter(c);
    }

    private String ident(int c) {
        StringBuilder b = new StringBuilder();
        b.append((char)c);
        b.append(name());
        return b.toString();
    }

    private String name() {
        StringBuilder b = new StringBuilder();
        for (;;) {
            int pos = current();
            int c = nextChar();
            if (isNameLetter(c)) {
                b.append((char)c);
            } else {
                rewind(pos);
                break;
            }
        }
        return b.toString();
    }

    private boolean isHash(int c) {
        if (c != '#') {
            return false;
        }
        int pos = current();
        c = nextChar();
        rewind(pos);
        return isNameLetter(c);
    }

    private String hash() {
        StringBuilder b = new StringBuilder();
        b.append("#");
        b.append(name());
        return b.toString();
    }

    private Token string(int quote) {
        StringBuilder b = new StringBuilder();
        b.append((char)quote);
        int c = 0;
        while ((c = nextChar()) != quote) {
            if (c < 0) {
                return Token.EOI;
            }
            b.append((char)c);
        }
        String value = b.append((char)quote).toString();
        return Token.of(TokenCategory.STRING, value);
    }

    /**
     * Creates a token of combinators or comma.
     *
     * @param c the first character.
     * @return created token.
     */
    private Token separator(int c) {
        int pos = current();
        if (isWhitespace(c)) {
            skipSpaces();
            pos = current();
            c = nextChar();
        }
        switch (c) {
        case '+':
            skipSpaces();
            return Token.PLUS;
        case '>':
            skipSpaces();
            return Token.GREATER;
        case ',':
            skipSpaces();
            return Token.COMMA;
        case '~':
            skipSpaces();
            return Token.TILDE;
        case -1:
            // End of input immediate after whitespaces.
            return Token.EOI;
        default:
            rewind(pos);
            return Token.SPACE;
        }
    }

    private static boolean isFirstLetter(int c) {
        return c == '_' ||
               isAlphabetic(c) ||
               isNoAscii(c) ||
               c == '\\'
               ;
    }

    private static boolean isNameLetter(int c) {
        return c == '_' ||
               c == '-' ||
               isAlphabetic(c) ||
               isDigit(c) ||
               isNoAscii(c) ||
               c == '\\'
               ;
    }

    private static boolean isNoAscii(int c) {
        return c >= 128;
    }

    private static boolean isWhitespace(int c) {
        return c == 0x20 || c == '\t' || c == '\r' || c == '\n' || c == '\f';
    }

    private static boolean isSeparator(int c) {
        return isWhitespace(c) || c == '+' || c == '>' || c == ',' || c == '~';
    }

    private boolean isEqualSign(int c) {
        if (c == '=') {
            return true;
        } else if (c == '~' || c == '|' || c == '^' || c == '$' ||  c == '*') {
            int pos = current();
            c = nextChar();
            rewind(pos);
            return c == '=';
        } else {
            return false;
        }
    }

    private Token equalSign(int c) {
        if (c == '=') {
            return Token.EXACT_MATCH;
        }
        nextChar();
        switch (c) {
        case '~':
            return Token.INCLUDES;
        case '|':
            return Token.DASH_MATCH;
        case '^':
            return Token.PREFIX_MATCH;
        case '$':
            return Token.SUFFIX_MATCH;
        case '*':
            return Token.SUBSTRING_MATCH;
        default:
            throw new IllegalStateException();
        }
    }

    private int current() {
        return nextIndex;
    }

    private void rewind(int mark) {
        this.nextIndex = mark;
    }

    private void skipSpaces() {
        while (nextIndex < input.length()) {
            char c = input.charAt(nextIndex);
            if (!isWhitespace(c)) {
                break;
            }
            nextIndex++;
        }
    }

    private int nextChar() {
        if (nextIndex >= input.length()) {
            return -1;
        }
        return input.charAt(nextIndex++);
    }
}
