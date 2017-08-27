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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.api.SelectorGroup;
import io.github.i49.cascade.api.SingleSelector;
import io.github.i49.cascade.core.walkers.DepthFirstWalker;

/**
 *
 */
public class DefaultSelectorGroup extends AbstractSelector implements SelectorGroup {

    private final List<DefaultSingleSelector> selectors;

    /**
     * Creates a selector containing all given selectors.
     *
     * @param selectors the selectors to contain.
     * @return created selector.
     */
    public static Selector of(List<DefaultSingleSelector> selectors) {
        if (selectors.size() == 1) {
            return selectors.get(0);
        } else {
            return new DefaultSelectorGroup(selectors);
        }
    }

    private DefaultSelectorGroup(List<DefaultSingleSelector> selectors) {
        super(DepthFirstWalker.create());
        this.selectors = selectors;
    }

    @Override
    public Iterator<SingleSelector> iterator() {
        List<SingleSelector> list = Collections.unmodifiableList(selectors);
        return list.iterator();
    }

    @Override
    public String toString() {
        return selectors.stream()
                .map(SingleSelector::toString)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean test(Element element, Element root) {
        for (DefaultSingleSelector selector: selectors) {
            if (selector.test(element, root)) {
                return true;
            }
        }
        return false;
    }
}
