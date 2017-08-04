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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.github.i49.cascade.core.dom.Elements;

/**
 *
 */
public class FastIdentifierTraverser extends DepthFirstTraverser {

    private final String identifier;

    public FastIdentifierTraverser(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void traverse(Element start, Visitor visitor) {
        if (!Elements.isRoot(start)) {
            super.traverse(start, visitor);
            return;
        }
        Document doc = start.getOwnerDocument();
        Element found = doc.getElementById(identifier);
        if (found != null) {
            visitor.visit(found);
        }
    }
}
