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

package com.github.i49.cascade.core.selectors;

import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.github.i49.cascade.core.matchers.Matcher;

/**
 * A collector which collects elements matching a sequence of simple selectors.  
 */
public class SequenceCollector implements Collector {

    private final Matcher matcher;
    
    public SequenceCollector(Matcher matcher) {
        this.matcher = matcher;
    }
    
    @Override
    public void collect(Element element, Set<Element> found) {
        visitElement(element, found);
    }
    
    @Override
    public String toString() {
        return matcher.toString();
    }
    
    private void visitElement(Element current, Set<Element> found) {
        if (this.matcher.matches(current)) {
            found.add(current);
        }
        visitChildren(current, found);
    }
    
    private void visitChildren(Element current, Set<Element> found) {
        Node child = current.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                visitElement((Element)child, found);
            }
            child = child.getNextSibling();
        }
    }
}
