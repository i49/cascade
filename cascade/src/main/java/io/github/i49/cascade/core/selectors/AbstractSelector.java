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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.core.dom.Elements;
import io.github.i49.cascade.core.message.Message;
import io.github.i49.cascade.core.walkers.Walker;

/**
 * A skeletal implementation of {@link Selector}.
 */
abstract class AbstractSelector implements Selector {

    private final Walker walker;

    protected AbstractSelector(Walker walker) {
        this.walker = walker;
    }

    @Override
    public List<Element> select(Element start) {
        if (start == null) {
            throw new NullPointerException("start must not be null.");
        } else if (Elements.isOrphan(start)) {
            throw new IllegalArgumentException(Message.ELEMENT_HAS_NOT_PARENT.toString());
        }
        List<Element> selected  = new ArrayList<>();
        this.walker.walkTree(start, element->{
            if (test(element, start)) {
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
