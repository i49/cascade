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

package com.github.i49.cascade.core.matchers;

import java.util.regex.Pattern;

import org.w3c.dom.Element;

/**
 * Matcher which returns true if one of attribute values
 * separated by whitespaces equals the specified value.
 */
public class IncludeMatcher extends AttributeValueMatcher {
    
    private static final Pattern SEPARATOR = Pattern.compile("[ \\t\\r\\n\\f]+");
            
    public IncludeMatcher(String name, String value) {
        super(name, value);
    }

    @Override
    public boolean matches(Element element) {
        if (!super.matches(element)) {
            return false;
        }
        String expected = getExpectedValue();
        String values = element.getAttribute(getName()); 
        for (String value: SEPARATOR.split(values)) {
            if (value.equals(expected)) {
                return true;
            }
        }
        return false;
    }
  
    @Override
    protected String getSymbol() {
        return "~=";
    }
}
