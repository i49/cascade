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
 * Traverser which traverses the siblings after given element.
 */
public class SiblingTraverser implements Traverser {

    // The one and only instance of this traverser.
    public static final SiblingTraverser SINGLETON = new SiblingTraverser();

    // The traverser which skips siblings after matched elements.
    public static final SiblingTraverser SIBLING_SKIPPABLE = new SiblingTraverser() {
        @Override
        public void traverse(Element start, Visitor visitor) {
            for (Node node = start.getNextSibling(); node != null; node = node.getNextSibling()) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (visitor.visit((Element)node)) {
                        break;
                    }
                }
            }
        }
    };

    private SiblingTraverser() {
    }

    @Override
    public void traverse(Element start, Visitor visitor) {
        for (Node node = start.getNextSibling(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                visitor.visit((Element)node);
            }
        }
    }

    @Override
    public Traverser skippingSiblingsOfMatched() {
        return SIBLING_SKIPPABLE;
    }
}
