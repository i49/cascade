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

package com.github.i49.cascade.core.selectors;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.github.i49.cascade.api.Selector;
import com.github.i49.cascade.core.traversers.Traverser;

/**
 * A skeletal implementation of {@link Selector}.
 */
abstract class AbstractSelector implements Selector {

    private final Traverser traverser;

    protected AbstractSelector(Traverser traverser) {
        this.traverser = traverser;
    }

    @Override
    public List<Element> select(Element root) {
        if (root == null) {
            throw new NullPointerException("root must not be null.");
        }
        List<Element> selected  = new ArrayList<>();
        this.traverser.traverse(root, element->{
            if (test(element, root)) {
                selected.add(element);
            }
        });
        return selected;
    }

    public boolean matches(Element element) {
        if (element == null) {
            throw new NullPointerException("element must not be null.");
        }
        return test(element, element);
    }

    public abstract boolean test(Element element, Element root);
}
