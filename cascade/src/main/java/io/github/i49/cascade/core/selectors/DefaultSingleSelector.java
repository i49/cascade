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

package io.github.i49.cascade.core.selectors;

import org.w3c.dom.Element;

import io.github.i49.cascade.api.SingleSelector;

/**
 * Default implementation of {@link SingleSelector} interface.
 */
public class DefaultSingleSelector extends AbstractSelector implements SingleSelector {

    private final TailSequence tail;

    public DefaultSingleSelector(TailSequence tail) {
        super(tail.createWalker());
        this.tail = tail;
    }

    @Override
    public String toString() {
        return tail.toString();
    }

    @Override
    public boolean test(Element element, Element root) {
        return tail.test(element, root);
    }
}
