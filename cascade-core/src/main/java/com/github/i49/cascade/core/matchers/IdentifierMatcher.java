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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 *
 */
public class IdentifierMatcher implements Matcher {
    
    private final String identifier;
    
    public IdentifierMatcher(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public MatcherType getType() {
        return MatcherType.IDENTIFIER;
    }
   
    @Override
    public boolean matches(Element element) {
        NamedNodeMap map = element.getAttributes();
        for (int i = 0; i < map.getLength(); i++) {
            Attr a = (Attr)map.item(i);
            if (a.isId() && this.identifier.equals(a.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "#" + this.identifier;
    }
}
