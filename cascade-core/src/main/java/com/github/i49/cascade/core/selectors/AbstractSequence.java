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

import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.github.i49.cascade.core.matchers.Matcher;

/**
 * A skeletal implementation of {@link Sequence}.
 */
abstract class AbstractSequence implements Sequence {
    
    private List<Matcher> matchers;
    private CombinatorSequence nextSequence;
    
    protected AbstractSequence(List<Matcher> matchers) {
        this.matchers = matchers;
    }
    
    @Override
    public SequenceResult processAll(Element element) {
        SequenceResult result = processCurrent(element);
        processSubsequent(result);
        return result;
    }

    @Override
    public void process(SequenceResult result) {
        Set<Element> selected = result.resetSelected();
        processCurrent(selected, result);
    }

    @Override
    public boolean hasNext() {
        return getNext() != null;
    }
    
    @Override
    public CombinatorSequence getNext() {
        return nextSequence;
    }

    @Override
    public void setNext(CombinatorSequence next) {
        this.nextSequence = next;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < this.matchers.size(); i++) {
            Matcher m = this.matchers.get(i);
            if (i == 0 && !m.getType().representsType()) {
                b.append("*");
            }
            b.append(m.toString());
        }
        if (hasNext()) {
            b.append(this.nextSequence.toString());
        }
        return b.toString();
    }
    
    private SequenceResult processCurrent(Element element) {
        SequenceResult result = new SequenceResult();
        traverse(element, result);
        return result;
    }
    
    private void processCurrent(Set<Element> selected, SequenceResult result) {
        for (Element element: selected) {
            traverse(element, result);
        }
    }
    
    private Set<Element> processSubsequent(SequenceResult result) {
        Sequence current = getNext();
        Set<Element> selected = result.getSelected();
        while (current != null && !selected.isEmpty()) {
            current.process(result);
            current = current.getNext();
        }
        return selected;
    }
    
    /**
     * Traverses the document tree.
     * This method should be overridden by the combinator sequences.
     * 
     * @param e the element as the starting point.
     * @param found the elements found matched during the traversal.
     */
    protected void traverse(Element e, SequenceResult result) {
        visitElemntAndItsDescendants(e, result);
    }
    
    private void visitElemntAndItsDescendants(Element e, SequenceResult result) {
        match(e, result);
        visitDescendantsOf(e, result);
    }
    
    protected void match(Element e, SequenceResult result) {
        if (test(e)) {
            result.select(e);
        }
        result.addVisited();
    }
    
    protected void visitDescendantsOf(Element e, SequenceResult result) {
        for (Node child = e.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                visitElemntAndItsDescendants((Element)child, result);
            }
        }
    }
    
    private boolean test(Element e) {
        for (Matcher m: this.matchers) {
            if (!m.matches(e)) {
                return false;
            }
        }
        return true;
    }
}
