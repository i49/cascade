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

package io.github.i49.cascade.core.walkers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A basic depth-first walker.
 */
public class DepthFirstWalker implements Walker {

    // The one and only instance of this class.
    private static final DepthFirstWalker SINGLETON = new DepthFirstWalker();

    public static DepthFirstWalker create() {
        return SINGLETON;
    }

    private DepthFirstWalker() {
    }

    @Override
    public void walkTree(Element start, Visitor visitor) {
        visitor.visit(start);
        visitDescendants(start, visitor);
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
}
