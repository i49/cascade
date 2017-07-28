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

import com.github.i49.cascade.api.InvalidSelectorException;

/**
 *
 */
public class Tokenizer {
    
    private final String input;
    private int currentPos;
    private int lastPos;
    
    public Tokenizer(String input) {
        this.input = input;
        this.currentPos = 0;
        this.lastPos = 0;
    }
    
    public boolean hasMoreTokens() {
        return currentPos < input.length();
    }
    
    public Token nextToken() {
        this.lastPos = this.currentPos;
        int c = peekChar();
        if (c < 0) {
            return null;
        } if (c == '*') {
            nextChar();
            return Token.WILDCARD;
        } if (isIdent(c)) {
            return new Token(TokenType.IDENT, ident());
        } else if (c == '#') {
            return new Token(TokenType.HASH, hash());
        } else if (c == '.') {
            return new Token(TokenType.CLASS, clazz());
        } else if (isDelimiter(c)) {
            return delimiterToken();
        }
        throw new InvalidSelectorException("Unrecognized token", getInput(), this.lastPos);
    }
    
    public String getInput() {
        return input;
    }
    
    public int getLastPosition() {
        return lastPos;
    }
    
    private boolean isIdent(int c) {
        return c == '-' || isFirstLetter(c);
    }
    
    private String ident() {
        StringBuilder b = new StringBuilder();
        while (isNameLetter(peekChar())) {
            b.append((char)nextChar());
        }
        return b.toString();
    }
    
    private String name() {
        return ident();
    }
    
    private String hash() {
        StringBuilder b = new StringBuilder();
        b.append((char)nextChar());
        if (isNameLetter(peekChar())) {
            b.append(name());
        }
        return b.toString();
    }
    
    private String clazz() {
        StringBuilder b = new StringBuilder();
        b.append((char)nextChar());
        if (isIdent(peekChar())) {
            b.append(ident());
        }
        return b.toString();
    }
    
    private Token delimiterToken() {
        String spaces = whitespaces();
        switch (peekChar()) {
        case '+':
            nextChar();
            whitespaces();
            return Token.PLUS;
        case '>':
            nextChar();
            whitespaces();
            return Token.GREATER;
        case ',':
            nextChar();
            whitespaces();
            return Token.COMMA;
        case '~':
            nextChar();
            whitespaces();
            return Token.TILDE;
        default:
            return new Token(TokenType.SPACE, spaces);
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
    
    private int peekChar() {
        if (currentPos >= input.length()) {
            return -1;
        }
        return input.charAt(currentPos);
    }
    
    private int nextChar() {
        if (currentPos >= input.length()) {
            return -1;
        }
        return input.charAt(currentPos++);
    }

    private String whitespaces() {
        StringBuilder b = new StringBuilder();
        int c = peekChar();
        while(isWhitespace(c)) {
            b.append(nextChar());
            c = peekChar();
        }
        return b.toString();
    }
}
