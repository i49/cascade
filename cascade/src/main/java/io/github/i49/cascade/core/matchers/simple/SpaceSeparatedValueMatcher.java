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

package io.github.i49.cascade.core.matchers.simple;

import java.util.regex.Pattern;

/**
 * The matcher which tests if any part of attribute value
 * separated by spaces equals to the specified value.
 */
public class SpaceSeparatedValueMatcher extends AttributeValueMatcher {

    private static final Pattern SEPARATOR = Pattern.compile("[ \\t\\r\\n\\f]+");

    public SpaceSeparatedValueMatcher(AttributeNameMatcher nameMatcher, String value) {
        super(nameMatcher, value);
    }

    @Override
    public boolean matchesNever() {
        return getExpectedValue().isEmpty();
    }

    @Override
    protected String getSymbol() {
        return "~=";
    }

    @Override
    public boolean testValue(String actualValue) {
        final String expectedValue = getExpectedValue();
        if (expectedValue.isEmpty()) {
            return false;
        }
        for (String value: SEPARATOR.split(actualValue)) {
            if (value.equals(expectedValue)) {
                return true;
            }
        }
        return false;
    }
}
