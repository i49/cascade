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

import static com.github.i49.cascade.core.compiler.Letters.isHexDigit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for unescaping text.
 */
final class Escaper {

    /**
     * Unescapes text of name or identifier.
     *
     * @param text the text to unescape.
     * @return unescaped text.
     */
    public static String unescape(String text) {
        return unescape(text, Letters.ESCAPE_PATTERN);
    }

    /**
     * Unescapes quoted string.
     *
     * @param text the text to unescape.
     * @return unescaped text, which stays quoted.
     */
    public static String unescapeQuotedString(String text) {
        return unescape(text, Letters.STRING_ESCAPE_PATTERN);
    }

    private static String unescape(String text, Pattern escapePattern) {
        Matcher m = escapePattern.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (m.find()) {
            String matched = m.group();
            String replacement = isUncodeEscape(matched)
                    ? unescapeUnicode(matched) : unescapeCharacter(matched);
            m.appendReplacement(buffer, replacement);
        }
        m.appendTail(buffer);
        return buffer.toString();
    }

    private static boolean isUncodeEscape(String rawValue) {
        return isHexDigit(rawValue.charAt(1));
    }

    private static String unescapeUnicode(String rawValue) {
        rawValue = rawValue.toLowerCase();
        String[] parts = rawValue.split("\\s");
        String hex = parts[0].substring(1);
        char c = (char)Integer.parseInt(hex, 16);
        return String.valueOf(c);
    }

    private static String unescapeCharacter(String rawValue) {
        return rawValue.substring(1);
    }

    private Escaper() {
    }
}
