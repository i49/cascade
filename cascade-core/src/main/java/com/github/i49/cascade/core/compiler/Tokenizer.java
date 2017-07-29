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
 *
 */
public class Tokenizer {
    
    private static final int HISTORY_SIZE = 16;
    
    private final String input;
    private int currentIndex;
    private int nextIndex;
    
    private final int[] history = new int[HISTORY_SIZE];
    private int nextHistoryIndex;
    private int historyEntryCount;
    
    public Tokenizer(String input) {
        this.input = input;
        this.nextIndex = 0;
        this.currentIndex = 0;
    }
    
    public boolean hasMoreTokens() {
        return nextIndex < input.length();
    }
    
    public Token nextToken() {
        this.currentIndex = this.nextIndex;
        int c = nextChar();
        if (c < 0) {
            return Token.EOI;
        } if (c == '*') {
            return Token.WILDCARD;
        } else if (c == '.') {
            return Token.PERIOD;
        } else if (isDelimiter(c)) {
            return delimiterToken(c);
        } else if (isHash(c)) {
            return new Token(TokenCategory.HASH, hash());
        } else if (isIdent(c)) {
            rewind(1);
            return new Token(TokenCategory.IDENT, ident());
        }
        return Token.UNKNOWN;
    }
    
    public String getInput() {
        return input;
    }
    
    public int getLastPosition() {
        return currentIndex;
    }
    
    private boolean isIdent(int c) {
        if (c == '-') {
            c = nextChar();
            rewind(1);
        }
        return isFirstLetter(c);
    }
    
    private String ident() {
        StringBuilder b = new StringBuilder();
        for (int c = nextChar(); c >= 0; c = nextChar()) {
            if (isNameLetter(c)) {
                b.append((char)c);
            } else {
                rewind(1);
                break;
            }
        }
        return b.toString();
    }
    
    private String name() {
        StringBuilder b = new StringBuilder();
        for (int c = nextChar(); c >= 0; c = nextChar()) {
            if (isNameLetter(c)) {
                b.append((char)c);
            } else {
                rewind(1);
                break;
            }
        }
        return b.toString();
    }
    
    private boolean isHash(int c) {
        if (c != '#') {
            return false;
        }
        c = nextChar();
        if (c >= 0) {
            rewind(1);
        }
        return isNameLetter(c); 
    }
    
    private String hash() {
        StringBuilder b = new StringBuilder();
        b.append("#");
        b.append(name());
        return b.toString();
    }
    
    private Token delimiterToken(int c) {
        if (isWhitespace(c)) {
            skipSpaces();
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
        default:
            return new Token(TokenCategory.SPACE, " ");
        }
    }
    
    private static boolean isFirstLetter(int c) {
        return c == '_' ||
               isAlphabetic(c) ||
               isNoAscii(c);
    }
    
    private static boolean isNameLetter(int c) {
        return c == '_' || 
               c == '-' ||
               isAlphabetic(c) ||
               isDigit(c) ||
               isNoAscii(c);
    }
    
    private static boolean isNoAscii(int c) {
        return c >= 128;
    }
    
    private static boolean isWhitespace(int c) {
        return c == 0x20 || c == '\t' || c == '\r' || c == '\n' || c == '\f';
    }
    
    private static boolean isDelimiter(int c) {
        return isWhitespace(c) || c == '+' || c == '>' || c == ',' || c == '~';
    }
    
    private void skipSpaces() {
        int c = nextChar();
        while (isWhitespace(c)) {
            c = nextChar();
        }
        if (c >= 0) {
            rewind(1);
        }
    }

    private int nextChar() {
        if (nextIndex >= input.length()) {
            return -1;
        }
        history[nextHistoryIndex++] = nextIndex;
        if (nextHistoryIndex >= HISTORY_SIZE) {
            nextHistoryIndex = 0; 
        }
        if (historyEntryCount < HISTORY_SIZE) {
            historyEntryCount++;
        }
        return input.charAt(nextIndex++);
    }
    
    private void rewind(int count) {
        if (count <= 0) {
            return;
        } if (count > historyEntryCount) {
            throw new IllegalStateException();
        }
        int newHistoryIndex = nextHistoryIndex - count;
        if (newHistoryIndex < 0) {
            newHistoryIndex += HISTORY_SIZE;
        }
        historyEntryCount -= count;
        nextHistoryIndex = newHistoryIndex;
        nextIndex = history[newHistoryIndex];
    }
}
