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

import io.github.i49.cascade.core.dom.Elements;

/**
 * The walker which will visit only the root element of the document.
 */
public class RootOnlyWalker implements Walker {

    // The one and only instance of this class.
    private static final RootOnlyWalker SINGLETON = new RootOnlyWalker();

    public static RootOnlyWalker create() {
        return SINGLETON;
    }

    private RootOnlyWalker() {
    }

    /**
     * {@inheritDoc}
     * This method only visits the element at the root of the document.
     */
    @Override
    public void walkTree(Element start, Visitor visitor) {
        if (Elements.isRoot(start)) {
            visitor.visit(start);
        }
    }
}
