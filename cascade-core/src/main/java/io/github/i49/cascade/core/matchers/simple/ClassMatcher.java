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

import org.w3c.dom.Element;

import io.github.i49.cascade.core.matchers.Matcher;
import io.github.i49.cascade.core.matchers.MatcherType;

/**
 *
 */
public class ClassMatcher implements Matcher {
    
    private final String className;
    
    public ClassMatcher(String className) {
        this.className = className;
    }

    @Override
    public MatcherType getType() {
        return MatcherType.CLASS;
    }
    
    @Override
    public boolean matches(Element element) {
        String valueList = element.getAttribute("class");
        for (String value: valueList.split("\\s+")) {
            if (this.className.equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "." + this.className;
    }
}