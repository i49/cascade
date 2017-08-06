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

import java.util.regex.Pattern;

/**
 * Letters in selector expression.
 */
class Letters {

    public static final String UNICODE_ESCAPE =
            "\\\\[0-9a-fA-F]{1,6}(\r\n|[ \n\r\t\f])?";

    public static final Pattern ESCAPE_PATTERN = Pattern.compile(
            UNICODE_ESCAPE + "|\\\\[^\\n\\r\\f0-9a-fA-F]"
            );

    public static boolean isWhitespace(int c) {
        return c == 0x20 || c == '\t' || c == '\r' || c == '\n' || c == '\f';
    }

    public static boolean isAscii(int c) {
        return 0 <= c && c <= 127;
    }

    public static boolean isHexDigit(int c) {
        return isDigit(c) || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
    }

    public static boolean isIdentifierStart(int c) {
        return c == '_' || isAlphabetic(c) || !isAscii(c);
    }

    public static boolean isNameLetter(int c) {
        return isIdentifierStart(c) || c == '-' || isDigit(c);
    }

     private Letters() {
    }
}
