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

package com.github.i49.cascade.core.traversers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A basic depth-first traverser.
 */
public class DepthFirstTraverser implements Traverser {

    // The one and only instance of this traverser.
    public static final DepthFirstTraverser SINGLETON = new DepthFirstTraverser();

    // The traverser which skips descendants of matched elements.
    private static final DepthFirstTraverser DESCENDANT_SKIPPABLE = new DepthFirstTraverser() {
        @Override
        public void traverse(Element start, Visitor visitor) {
            if (!visitor.visit(start)) {
                visitExcludingDescendantsOfMatched(start, visitor);
            }
        }
    };

    // The traverser which skips siblings of matched elements.
    private static final DepthFirstTraverser SIBLING_SKIPPABLE = new DepthFirstTraverser() {
        @Override
        public void traverse(Element start, Visitor visitor) {
            visitor.visit(start);
            visitExcludingSiblingsOfMatched(start, visitor);
        }
    };

    protected DepthFirstTraverser() {
    }

    @Override
    public void traverse(Element start, Visitor visitor) {
        visitor.visit(start);
        visitDescendants(start, visitor);
    }

    @Override
    public Traverser skippingDescendantsOfMatched() {
        return DESCENDANT_SKIPPABLE;
    }

    @Override
    public Traverser skippingSiblingsOfMatched() {
        return SIBLING_SKIPPABLE;
    }

    protected static void visitDescendants(Element element, Visitor visitor) {
        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element)node;
                visitor.visit(child);
                visitDescendants((Element)node, visitor);
            }
        }
    }

    protected static void visitExcludingDescendantsOfMatched(Element element, Visitor visitor) {
        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element)node;
                if (!visitor.visit(child)) {
                    visitExcludingDescendantsOfMatched(child, visitor);
                }
            }
        }
    }

    protected static void visitExcludingSiblingsOfMatched(Element element, Visitor visitor) {
        boolean skipSibling = false;
        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element)node;
                if (!skipSibling) {
                    skipSibling = visitor.visit(child);
                }
                visitExcludingSiblingsOfMatched(child, visitor);
            }
        }
    }
}
