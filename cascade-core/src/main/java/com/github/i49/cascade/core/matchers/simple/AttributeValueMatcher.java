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
 * The abstract base matcher which tests attribute values.
 */
public abstract class AttributeValueMatcher implements AttributeMatcher {

    private final AttributeNameMatcher nameMatcher;
    private final String value;

    protected AttributeValueMatcher(AttributeNameMatcher nameMatcher, String value) {
        this.nameMatcher = nameMatcher;
        this.value = value;
    }

    @Override
    public boolean matches(Element element) {
        return nameMatcher.matches(element);
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
        return value;
    }

    /**
     * Returns the actual attribute value of the specified element.
     *
     * @param element the element which has the attribute.
     * @return the attribute value.
     */
    public String getActualValue(Element element) {
        return nameMatcher.getAttributeValue(element);
    }

    /**
     * Returns the symbol which represents the comparison.
     *
     * @return the symbol of the comparison.
     */
    protected abstract String getSymbol();
}
