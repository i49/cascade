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

/**
 * Document tree walker.
 * This type is the base type of all other walkers.
 */
@FunctionalInterface
public interface Walker {

    /**
     * Walks a document tree rooted at a given starting element.
     *
     * @param start the starting point element to walk.
     * @param visitor the visitor of each element.
     */
    void walkTree(Element start, Visitor visitor);
}
