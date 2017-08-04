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

import java.util.Set;

import org.w3c.dom.Element;

import com.github.i49.cascade.core.matchers.IdentifierMatcher;
import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.MatcherList;
import com.github.i49.cascade.core.matchers.MatcherType;
import com.github.i49.cascade.core.traversers.DepthFirstTraverser;
import com.github.i49.cascade.core.traversers.FastIdentifierTraverser;
import com.github.i49.cascade.core.traversers.Traverser;

/**
 * A skeletal implementation of {@link Sequence}.
 */
abstract class AbstractSequence implements Sequence {

    private MatcherList matchers;
    private final MatcherList originalMatchers;
    private CombinatorSequence nextSequence;
    private Traverser traverser;

    protected AbstractSequence(MatcherList matchers) {
        this.matchers = this.originalMatchers = matchers;

        Matcher found = matchers.findFirst(MatcherType.IDENTIFIER);
        if (found != null) {
            String identifier = ((IdentifierMatcher)found).getIdentifier();
            this.traverser = new FastIdentifierTraverser(identifier);
            this.matchers = matchers.without(found);
        } else {
            this.traverser = DepthFirstTraverser.SINGLETON;
            this.matchers = matchers;
        }
    }

    protected AbstractSequence(MatcherList matchers, Traverser traverser) {
        this.matchers = this.originalMatchers = matchers;
        this.traverser = traverser;
    }

    @Override
    public SequenceResult process(Element element) {
        MatchingVisitor visitor = new MatchingVisitor(matchers);
        traverser.traverse(element, visitor);
        return visitor;
    }

    @Override
    public SequenceResult process(Set<Element> elements) {
        MatchingVisitor visitor = new MatchingVisitor(matchers);
        for (Element element: elements) {
            traverser.traverse(element, visitor);
        }
        return visitor;
    }

    @Override
    public boolean hasNext() {
        return getNext() != null;
    }

    @Override
    public CombinatorSequence getNext() {
        return nextSequence;
    }

    @Override
    public void setNext(CombinatorSequence next) {
        Combinator combinator = next.getCombinator();
        this.nextSequence = next;
        this.traverser = combinator.optimizePreviousTraverser(this.traverser);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(originalMatchers.toString());
        if (hasNext()) {
            b.append(nextSequence.toString());
        }
        return b.toString();
    }
}
