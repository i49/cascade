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

package com.github.i49.cascade.core.matchers.simple;

import org.w3c.dom.Element;

/**
 * Matcher which tests if attribute value starts with the specified prefix.
 */
public class PrefixMatcher extends AttributeValueMatcher {

    public PrefixMatcher(AttributeNameMatcher nameMatcher, String prefix) {
        super(nameMatcher, prefix);
    }

    @Override
    public boolean matches(Element element) {
        String expected = getExpectedValue();
        if (expected.isEmpty()) {
            return false;
        }
        if (!super.matches(element)) {
            return false;
        }
        String actual = getActualValue(element);
        return actual.startsWith(expected);
    }

    @Override
    public boolean matchesNever() {
        return getExpectedValue().isEmpty();
    }

    @Override
    protected String getSymbol() {
        return "^=";
    }
}
