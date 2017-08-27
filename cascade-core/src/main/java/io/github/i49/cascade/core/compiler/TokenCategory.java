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
 * Categories of tokens.
 */
public enum TokenCategory {
    /** Unknown category. */
    UNKNOWN,
    /** End of input. */
    EOI,
    /** Sequence of whitespaces. */
    SPACE,
    IDENTITY,
    HASH,
    PLUS,
    MINUS,
    GREATER,
    COMMA,
    TILDE,
    ASTERISK,
    PERIOD,
    OPENING_BRACKET,
    CLOSING_BRACKET,
    STRING,
    INVALID_STRING,
    EXACT_MATCH,
    INCLUDES,
    DASH_MATCH,
    PREFIX_MATCH,
    SUFFIX_MATCH,
    SUBSTRING_MATCH,
    COLON,
    FUNCTION,
    CLOSING_PARENTHESIS,
    NUMBER,
    DIMENSION,
    VERTICAL_BAR
}
