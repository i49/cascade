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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.i49.cascade.core.dom.Elements;

/**
 * Fast walker which is accelerated by {@link Document#getElementById(String)} method.
 */
public class IdentifierWalker implements Walker {

    private final String identifier;

    public static IdentifierWalker create(String identifier) {
        return new IdentifierWalker(identifier);
    }

    /**
     * Constructs this object.
     *
     * @param identifier the identifier of the element to visit.
     */
    private IdentifierWalker(String identifier) {
        this.identifier = identifier;
    }

    /**
     * {@inheritDoc}
     * This method only visits the element which has the specified identifier.
     */
    @Override
    public void walkTree(Element start, Visitor visitor) {
        Document doc = start.getOwnerDocument();
        Element found = doc.getElementById(this.identifier);
        if (found == null) {
            return;
        }
        if (Elements.isRoot(start) || Elements.hasDescendant(start, found)) {
            visitor.visit(found);
        }
    }
}
