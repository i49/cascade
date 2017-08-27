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

package io.github.i49.cascade.core.message;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Compiler message.
 */
public enum Message {
    // common

    ARGUMENT_IS_NULL,
    ARGUMENT_IS_BLANK,
    ELEMENT_HAS_NOT_PARENT,

    // parsing errors

    UNKNOWN_TOKEN,
    UNEXPECTED_TOKEN,
    UNEXPECTED_WHITESPACE,
    STRING_IS_NOT_CLOSED,
    NAMESPACE_PREFIX_IS_NOT_DECLARED,
    UNEXPECTED_END_OF_INPUT,

    // simple selectors

    UNIVERSAL_SELECTOR_POSITION_IS_INVALID,
    TYPE_SELECTOR_POSITION_IS_INVALID,
    CLASS_NAME_IS_MISSING,
    ATTRIBUTE_NAME_IS_MISSING,
    ATTRIBUTE_VALUE_IS_MISSING,
    NAMESPACE_SEPARATOR_IS_MISSING,
    COMBINATOR_WITHOUT_PRECEDING_SEQUENCE,
    UNEXPECTED_END_OF_SELECTOR,

    // pseudo-classes

    UNSUPPORTED_PSEUDO_CLASS,
    PSEUDO_CLASS_NAME_IS_MISSING,
    PSEUDO_CLASS_ARGUMENT_IS_MISSING,
    FUNCTION_IS_NOT_CLOSED,
    NUMBER_IS_NOT_INTEGER,

    // negation

    NEGATION_IS_NESTED,

    // pseudo-elements

    PSEUDO_ELEMENTS_ARE_NOT_SUPPORTED,
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
