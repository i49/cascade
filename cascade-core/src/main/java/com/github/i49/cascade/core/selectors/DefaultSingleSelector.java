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
import java.util.logging.Logger;

import org.w3c.dom.Element;

import com.github.i49.cascade.api.SingleSelector;

/**
 *
 */
public class DefaultSingleSelector implements SingleSelector {
    
    private static final Logger log = Logger.getLogger(DefaultSingleSelector.class.getName());
    
    private final Sequence head;
    
    public DefaultSingleSelector(Sequence head) {
        this.head = head;
    }

    @Override
    public Set<Element> select(Element root) {
        if (root == null) {
            throw new NullPointerException("root must not be null");
        }
        SequenceResult result  = head.processAll(root);
        log.fine(
            "Visited: " + result.getNumberOfVisitedElements() +
            " Selected: " + result.getNumberOfSelectedElements()); 
        return result.getSelected();
    }
    
    @Override
    public String toString() {
        return head.toString();
    }
}
