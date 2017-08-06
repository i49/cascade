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

import java.util.regex.Matcher;

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

    private final TokenCategory category;
    private final String rawText;

    public static Token of(TokenCategory category, String rawText) {
        switch (category) {
        case STRING:
            return new StringToken(rawText);
        case IDENTITY:
        case HASH:
            return new EncodedToken(category, rawText);
        default:
            return new Token(category, rawText);
        }
    }

    private Token(TokenCategory category, String rawValue) {
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
        return getRawText();
    }

    @Override
    public String toString() {
        return getRawText() + "@" + getCategory().name();
    }

    private static class EncodedToken extends Token {

        private final String decodedText;

        public EncodedToken(TokenCategory category, String rawText) {
            super(category, rawText);
            this.decodedText = decode(rawText);
        }

        @Override
        public String getText() {
            return decodedText;
        }

        private String decode(String rawValue) {
            Matcher m = ESCAPE_PATTERN.matcher(rawValue);
            StringBuffer buffer = new StringBuffer();
            while (m.find()) {
                String matched = m.group();
                String replacement = isUnicode(matched)
                        ? unescapeUnicode(matched) : unescape(matched);
                m.appendReplacement(buffer, replacement);
            }
            m.appendTail(buffer);
            return buffer.toString();
        }

        private boolean isUnicode(String rawValue) {
            return isHexDigit(rawValue.charAt(1));
        }

        private String unescapeUnicode(String rawValue) {
            rawValue = rawValue.toLowerCase();
            String[] parts = rawValue.split("\\s");
            String hex = parts[0].substring(1);
            char c = (char)Integer.parseInt(hex, 16);
            return String.valueOf(c);
        }

        private String unescape(String rawValue) {
            return rawValue.substring(1);
        }
    }

    /**
     * Specialized token which represents strings.
     *
     */
    private static class StringToken extends EncodedToken {

        public StringToken(String rawValue) {
            super(TokenCategory.STRING, rawValue);
        }

        @Override
        public String getValue() {
            return unquote(getText());
        }

        private static String unquote(String s) {
            return s.substring(1, s.length() - 1);
        }
    }
}
