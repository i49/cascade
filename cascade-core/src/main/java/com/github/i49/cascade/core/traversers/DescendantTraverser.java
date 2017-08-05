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

/**
 * Depth-first traverser ignoring the starting element.
 */
public class DescendantTraverser extends DepthFirstTraverser {

    // The one and only instance of this traverser.
    public static final DescendantTraverser SINGLETON = new DescendantTraverser();

    // The traverser which skips descendants of matched elements.
    private static final DescendantTraverser DESCENDANT_SKIPPABLE = new DescendantTraverser() {
        @Override
        public void traverse(Element start, Visitor visitor) {
            visitExcludingDescendantsOfMatched(start, visitor);
        }
    };

    // The traverser which skips siblings of matched elements.
       private static final DescendantTraverser SIBLING_SKIPPABLE = new DescendantTraverser() {
        @Override
        public void traverse(Element start, Visitor visitor) {
            visitExcludingSiblingsOfMatched(start, visitor);
        }
    };

    private DescendantTraverser() {
    }

    @Override
    public void traverse(Element start, Visitor visitor) {
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
}
