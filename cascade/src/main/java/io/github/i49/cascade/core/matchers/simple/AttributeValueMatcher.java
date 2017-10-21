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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * The abstract base matcher which tests attribute values.
 */
public abstract class AttributeValueMatcher implements AttributeMatcher {

    private final AttributeNameMatcher nameMatcher;
    private final String expectedValue;

    protected AttributeValueMatcher(AttributeNameMatcher nameMatcher, String expectedValue) {
        this.nameMatcher = nameMatcher;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean matches(Element element) {
        for (Attr attribute: nameMatcher.findAttributes(element)) {
            if (testValue(attribute.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[")
         .append(nameMatcher.getDisplayName())
         .append(getSymbol())
         .append("\"")
         .append(getExpectedValue())
         .append("\"]");
        return b.toString();
    }

    /**
     * Returns the expected value.
     *
     * @return the expected value.
     */
    public String getExpectedValue() {
        return expectedValue;
    }

    /**
     * Returns the symbol which represents the comparison.
     *
     * @return the symbol of the comparison.
     */
    protected abstract String getSymbol();

    /**
     * Tests the actual attribute value.
     *
     * @param actualValue the attribute value, never be {@code null}.
     * @return {@code true} if the value is expected one.
     */
    protected abstract boolean testValue(String actualValue);
}
