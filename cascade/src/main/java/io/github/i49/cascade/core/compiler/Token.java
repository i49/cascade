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

/**
 * The token extracted from the selector expression.
 */
public class Token {

    private final TokenCategory category;
    private final String rawText;
    private final int position;

    /**
     * Creates a new token.
     *
     * @param category the category of the token, cannot be {@code null}.
     * @param rawText the raw lexeme of the token, cannot be {@code null}.
     * @param position the position in the original text which is zero-indexed.
     * @return newly created token.
     */
    public static Token create(TokenCategory category, String rawText, int position) {
        switch (category) {
        case NUMBER:
            return new NumberToken(rawText, position);
        case STRING:
            return new StringToken(rawText, position);
        case INVALID_STRING:
            return new InvalidStringToken(rawText, position);
        case IDENTITY:
        case HASH:
        case FUNCTION:
            return new EscapedToken(category, rawText, position);
        default:
            return new Token(category, rawText, position);
        }
    }

    protected Token(TokenCategory category, String rawValue, int position) {
        this.category = category;
        this.rawText = rawValue;
        this.position = position;
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
     * Checks if this token belongs to the given category.
     *
     * @param category the token category to check.
     * @return {@code true} if this token belongs to the given category, {@code false} otherwise..
     */
    public boolean is(TokenCategory category) {
        return getCategory() == category;
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

    /**
     * Returns the position of this token in the original text,
     * which is zero-indexed..
     *
     * @return the position of this token.
     */
    public int getPosition() {
        return position;
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

        public EscapedToken(TokenCategory category, String rawText, int position) {
            super(category, rawText, position);
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

        public StringToken(String rawValue, int position) {
            this(TokenCategory.STRING, rawValue, position);
        }

        public StringToken(TokenCategory category, String rawValue, int position) {
            super(category, rawValue, position);
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

        public InvalidStringToken(String rawValue, int position) {
            super(TokenCategory.INVALID_STRING, rawValue, position);
        }

        @Override
        public String getValue() {
            throw new UnsupportedOperationException();
        }
    }
}
