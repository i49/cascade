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
 * Tokens to be extracted from selector expression.
 */
public class Token {
    
    public static final Token UNKNOWN = new Token(TokenCategory.UNKNOWN, "");
    public static final Token EOI = new Token(TokenCategory.EOI, ""); 
    public static final Token PLUS = new Token(TokenCategory.PLUS, "+"); 
    public static final Token GREATER = new Token(TokenCategory.GREATER, ">"); 
    public static final Token COMMA = new Token(TokenCategory.COMMA, ","); 
    public static final Token TILDE = new Token(TokenCategory.TILDE, "~"); 
    public static final Token WILDCARD = new Token(TokenCategory.WILDCARD, "*"); 
    public static final Token PERIOD = new Token(TokenCategory.PERIOD, "."); 
    public static final Token SPACE = new Token(TokenCategory.SPACE, " ");
    public static final Token OPENING_BRACKET = new Token(TokenCategory.OPENING_BRACKET, "["); 
    public static final Token CLOSING_BRACKET = new Token(TokenCategory.OPENING_BRACKET, "]"); 
    public static final Token EXACT_MATCH = new Token(TokenCategory.EXACT_MATCH, "=");
    public static final Token INCLUDES = new Token(TokenCategory.INCLUDES, "~=");
    public static final Token DASH_MATCH = new Token(TokenCategory.DASH_MATCH, "|=");
    public static final Token PREFIX_MATCH = new Token(TokenCategory.PREFIX_MATCH, "^="); 
    public static final Token SUFFIX_MATCH = new Token(TokenCategory.SUFFIX_MATCH, "$="); 
    public static final Token SUBSTRING_MATCH = new Token(TokenCategory.SUBSTRING_MATCH, "*="); 
    
    private final TokenCategory category;
    private final String lexeme;
    
    public static Token of(TokenCategory category, String lexeme) {
        if (category == TokenCategory.STRING) {
            return new StringToken(lexeme);
        } else {
            return new Token(category, lexeme);
        }
    }
    
    private Token(TokenCategory category, String lexeme) {
        this.category = category;
        this.lexeme = lexeme;
    }
    
    /**
     * Returns the category of this token.
     * 
     * @return the category of this token.
     */
    public TokenCategory getCategory() {
        return category;
    }
    
    /**
     * Returns the original lexeme of this token.
     * 
     * @return the lexeme of this token.
     */
    public String getLexeme() {
        return lexeme;
    }
    
    public String getValue() {
        return getLexeme();
    }
    
    @Override
    public String toString() {
        return getLexeme() + "@" + getCategory().name();
    }
    
    /**
     * Specialized token which represents strings.
     *
     */
    private static final class StringToken extends Token {
        
        public StringToken(String lexeme) {
            super(TokenCategory.STRING, lexeme);
        }

        @Override
        public String getValue() {
            return unquote(getLexeme());
        }
        
        private static String unquote(String s) {
            return s.substring(1, s.length() - 1);
        }
    }
}
