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
 * Token representing numbers.
 */
class NumberToken extends Token {

    public NumberToken(String rawText) {
        super(TokenCategory.NUMBER, rawText);
    }

    protected NumberToken(TokenCategory category, String rawText) {
        super(category, rawText);
    }

    public boolean isIntegral() {
        return getRawText().indexOf('.') < 0;
    }

    public int intValue() {
        return Integer.parseInt(getRawText());
    }
}