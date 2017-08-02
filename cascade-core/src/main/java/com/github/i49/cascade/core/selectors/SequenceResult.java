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

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Element;

/**
 * Result of a sequence of simple selectors.
 */
public class SequenceResult {
    
    private Set<Element> selected;
    private int visited;
    
    public SequenceResult() {
        this.visited = 0;
        resetSelected();
    }
    
    public Set<Element> resetSelected() {
        Set<Element> selected = getSelected();
        this.selected = new LinkedHashSet<>();
        return selected;
    }
    
    public void select(Element e) {
        this.selected.add(e);
    }
    
    public Set<Element> getSelected() {
        return selected;
    }
    
    public void addVisited() {
        this.visited++;
    }
    
    public int getNumberOfSelectedElements() {
        return selected.size();
    }
    
    public int getNumberOfVisitedElements() {
        return visited;
    }
}
