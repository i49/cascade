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
    private Sequence nextSequence;
    
    protected AbstractSequence(List<Matcher> matchers) {
        this.matchers = matchers;
    }
    
    @Override
    public Set<Element> process(Element element) {
        Set<Element> found = processCurrent(element);
        return processNext(found);
    }

    @Override
    public Set<Element> process(Set<Element> elements) {
        Set<Element> found = processCurrent(elements);
        return processNext(found);
    }

    @Override
    public boolean hasNext() {
        return this.nextSequence != null;
    }

    @Override
    public Sequence chain(Sequence next) {
        this.nextSequence = next;
        return this;
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
    
    protected Set<Element> processCurrent(Element element) {
        Set<Element> found = new LinkedHashSet<>();
        traverse(element, found);
        return found;
    }
    
    protected Set<Element> processCurrent(Set<Element> elements) {
        Set<Element> found = new LinkedHashSet<>();
        for (Element e: elements) {
            traverse(e, found);
        }
        return found;
    }
    
    protected Set<Element> processNext(Set<Element> found) {
        if (hasNext()) {
            return nextSequence.process(found);
        } else {
            return found;
        }
    }
    
    /**
     * Traverses the document tree.
     * This method should be overridden by the combinator sequences.
     * 
     * @param e the element as the starting point.
     * @param found the elements found matched during the traversal.
     */
    protected void traverse(Element e, Set<Element> found) {
        visitElemntAndItsDescendants(e, found);
    }
    
    private void visitElemntAndItsDescendants(Element e, Set<Element> found) {
        match(e, found);
        visitDescendantsOf(e, found);
    }
    
    protected void match(Element e, Set<Element> found) {
        if (test(e)) {
            found.add(e);
        }
    }
    
    protected void visitDescendantsOf(Element e, Set<Element> found) {
        for (Node child = e.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                visitElemntAndItsDescendants((Element)child, found);
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
