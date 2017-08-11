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

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Compiler message.
 */
public enum Message {
    UNIVERSAL_SELECTOR_POSITION_IS_INVALID,
    TYPE_SELECTOR_POSITION_IS_INVALID,
    CLASS_NAME_IS_MISSING,
    ATTRIBUTE_NAME_IS_MISSING,
    ATTRIBUTE_VALUE_IS_MISSING,
    UNKNOWN_TOKEN,
    UNEXPECTED_TOKEN,
    COMBINATOR_WITHOUT_PRECEDING_SEQUENCE,
    UNEXPECTED_END_OF_SELECTOR,
    UNEXPECTED_END_OF_INPUT,
    STRING_IS_NOT_CLOSED,

    UNSUPPORTED_PSEUDO_CLASS,
    PSEUDO_CLASS_NAME_IS_MISSING,
    PSEUDO_CLASS_ARGUMENT_IS_MISSING,
    PSEUDO_ELEMENTS_ARE_NOT_SUPPORTED,
    FUNCTION_IS_NOT_CLOSED,
    NUMBER_IS_NOT_INTEGER,
    ;

    private static final String BUNDLE_NAME = Message.class.getPackage().getName() + ".messages";
    private static final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);

    @Override
    public String toString() {
        return bundle.getString(name());
    }

    public String with(Object... arguments) {
        String pattern = toString();
        return MessageFormat.format(pattern, arguments);
    }
}
