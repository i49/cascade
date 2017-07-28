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

/**
 *
 */
public class Token {
    
    private final TokenType type;
    private final String value;
    
    public static final Token PLUS = new Token(TokenType.PLUS, "+"); 
    public static final Token GREATER = new Token(TokenType.PLUS, ">"); 
    public static final Token COMMA = new Token(TokenType.COMMA, ","); 
    public static final Token TILDE = new Token(TokenType.TILDE, "~"); 
    public static final Token WILDCARD = new Token(TokenType.WILDCARD, "*"); 
    
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    
    public TokenType getType() {
        return type;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return getValue() + "@" + getType().name();
    }
}
