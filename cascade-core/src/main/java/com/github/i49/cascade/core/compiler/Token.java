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
    
    private final TokenCategory category;
    private final String lexeme;
    
    public static final Token UNKNOWN = new Token(TokenCategory.UNKNOWN, "");
    public static final Token EOI = new Token(TokenCategory.EOI, ""); 
    public static final Token PLUS = new Token(TokenCategory.PLUS, "+"); 
    public static final Token GREATER = new Token(TokenCategory.PLUS, ">"); 
    public static final Token COMMA = new Token(TokenCategory.COMMA, ","); 
    public static final Token TILDE = new Token(TokenCategory.TILDE, "~"); 
    public static final Token WILDCARD = new Token(TokenCategory.WILDCARD, "*"); 
    public static final Token PERIOD = new Token(TokenCategory.PERIOD, "."); 
    
    public Token(TokenCategory category, String lexeme) {
        this.category = category;
        this.lexeme = lexeme;
    }
    
    public TokenCategory getCategory() {
        return category;
    }
    
    public String getLexeme() {
        return lexeme;
    }
    
    @Override
    public String toString() {
        return getLexeme() + "@" + getCategory().name();
    }
}
