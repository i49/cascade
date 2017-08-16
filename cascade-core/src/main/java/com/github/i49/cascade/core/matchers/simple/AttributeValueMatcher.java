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

/**
 *
 */
public abstract class AttributeValueMatcher extends AttributeMatcher {

    private final String value;
    
    public AttributeValueMatcher(String attribute, String value) {
        super(attribute);
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[")
         .append(getName())
         .append(getSymbol())
         .append("\"")
         .append(getExpectedValue())
         .append("\"]");
        return b.toString();
    }
    
    public String getExpectedValue() {
        return value;
    }

    protected abstract String getSymbol();
}