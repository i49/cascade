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
    public static final Token MINUS = new Token(TokenCategory.MINUS, "-");
    public static final Token GREATER = new Token(TokenCategory.GREATER, ">");
    public static final Token COMMA = new Token(TokenCategory.COMMA, ",");
    public static final Token TILDE = new Token(TokenCategory.TILDE, "~");
    public static final Token ASTERISK = new Token(TokenCategory.ASTERISK, "*");
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
    public static final Token COLON = new Token(TokenCategory.COLON, ":");
    public static final Token CLOSING_PARENTHESIS = new Token(TokenCategory.CLOSING_PARENTHESIS, ")");

    private final TokenCategory category;
    private final String rawText;

    public static Token create(TokenCategory category, String rawText) {
        switch (category) {
        case NUMBER:
            return new NumberToken(rawText);
        case STRING:
            return new StringToken(rawText);
        case INVALID_STRING:
            return new InvalidStringToken(rawText);
        case IDENTITY:
        case HASH:
        case FUNCTION:
            return new EscapedToken(category, rawText);
        default:
            return new Token(category, rawText);
        }
    }

    protected Token(TokenCategory category, String rawValue) {
        this.category = category;
        this.rawText = rawValue;
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
     * @return the original lexeme of this token.
     */
    public String getRawText() {
        return rawText;
    }

    /**
     * Returns the decoded lexeme of this token.
     *
     * @return the decoded lexeme of this token.
     */
    public String getText() {
        return getRawText();
    }

    public String getValue() {
        return getText();
    }

    public boolean isEndOfSequence() {
        switch (getCategory()) {
        case SPACE:
        case GREATER:
        case PLUS:
        case TILDE:
        case COMMA:
        case EOI:
            return true;
        default:
            return false;
        }
    }

    @Override
    public String toString() {
        return getRawText() + "@" + getCategory().name();
    }

    private static class EscapedToken extends Token {

        public EscapedToken(TokenCategory category, String rawText) {
            super(category, rawText);
        }

        @Override
        public String getText() {
            return Escaper.unescape(getRawText());
        }
    }

    /**
     * Specialized token which represents strings.
     */
    private static class StringToken extends EscapedToken {

        public StringToken(String rawValue) {
            this(TokenCategory.STRING, rawValue);
        }

        public StringToken(TokenCategory category, String rawValue) {
            super(category, rawValue);
        }

        @Override
        public String getText() {
            return Escaper.unescapeQuotedString(getRawText());
        }

        @Override
        public String getValue() {
            return unquote(getText());
        }

        private static String unquote(String s) {
            return s.substring(1, s.length() - 1);
        }
    }

    /**
     * Specialized token which represents invalid strings.
     */
    private static class InvalidStringToken extends StringToken {

        public InvalidStringToken(String rawValue) {
            super(TokenCategory.INVALID_STRING, rawValue);
        }

        @Override
        public String getValue() {
            throw new UnsupportedOperationException();
        }
    }
}
