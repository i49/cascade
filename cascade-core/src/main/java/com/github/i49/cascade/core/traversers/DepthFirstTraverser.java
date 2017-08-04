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

    public static final DepthFirstTraverser SINGLETON = new DepthFirstTraverser();

    @Override
    public void traverse(Element start, Visitor visitor) {
        visit(start, visitor);
    }

    private final void visit(Element element, Visitor visitor) {
        visitor.visit(element);
        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                traverse((Element)node, visitor);
            }
        }
    }
}