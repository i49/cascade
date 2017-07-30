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

import java.util.AbstractList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

import com.github.i49.cascade.api.SelectorGroup;
import com.github.i49.cascade.api.SingleSelector;

/**
 *
 */
public class DefaultSelectorGroup extends AbstractList<SingleSelector> implements SelectorGroup {
    
    private final List<SingleSelector> selectors;
    
    public DefaultSelectorGroup(List<SingleSelector> selectors) {
        this.selectors = selectors;
    }

    @Override
    public Set<Element> select(Element root) {
        if (root == null) {
            throw new NullPointerException("root must not be null");
        }
        Set<Element> found  = new LinkedHashSet<>();
        for (SingleSelector selector: this.selectors) {
            found.addAll(selector.select(root));
        }
        return found;
    }
    
    @Override
    public String toString() {
        return selectors.stream()
                .map(SingleSelector::toString)
                .collect(Collectors.joining(", "));
    }
    
    @Override
    public SingleSelector get(int index) {
        return selectors.get(index);
    }

    @Override
    public int size() {
        return selectors.size();
    }
}
