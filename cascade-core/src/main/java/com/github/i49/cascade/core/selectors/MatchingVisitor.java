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

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.traversers.Visitor;

/**
 *
 */
class MatchingVisitor implements Visitor, SequenceResult {

    private final Matcher matcher;
    private final Set<Element> selected;
    private int visited;

    public MatchingVisitor(Matcher matcher) {
        this.matcher = matcher;
        this.selected = new LinkedHashSet<>();
        this.visited = 0;
    }

    @Override
    public boolean visit(Element element) {
        this.visited++;
        boolean result = matcher.matches(element);
        if (result) {
            selected.add(element);
        }
        return result;
    }

    @Override
    public Set<Element> getSelected() {
        return selected;
    }

    @Override
    public int getNumberOfVisitedElements() {
        return visited;
    }
}
