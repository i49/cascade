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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The text used as an input for the {@link Tokenizer}.
 */
class TextInput {

    private final String text;
    private final int length;

    public TextInput(String text) {
        this.text = text;
        this.length = text.length();
    }

    public String getText() {
        return text;
    }

    public boolean hasChar(int index) {
        return index < length;
    }

    public int charAt(int index) {
        if (hasChar(index)) {
            return text.charAt(index);
        }
        return -1;
    }

    public String match(int index, Pattern pattern) {
        Matcher m = pattern.matcher(text);
        m = m.region(index, length);
        if (m.lookingAt()) {
            return m.group();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return text;
    }
}
