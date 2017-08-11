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
import com.github.i49.cascade.core.matchers.pseudo.PseudoClass;
import com.github.i49.cascade.core.matchers.pseudo.PseudoClassMatcher;
import com.github.i49.cascade.core.traversers.DepthFirstTraverser;
import com.github.i49.cascade.core.traversers.FastIdentifierTraverser;
import com.github.i49.cascade.core.traversers.RootTraverser;
import com.github.i49.cascade.core.traversers.Traverser;

/**
 * A skeletal implementation of {@link Sequence}.
 */
abstract class AbstractSequence implements Sequence {

    private final Matcher originalMatcher;
    private final Matcher matcherToApply;
    private Traverser traverser;
    private CombinatorSequence nextSequence;

    protected AbstractSequence(MatcherList matchers) {
        this.originalMatcher = matchers;
        this.matcherToApply = matchers.optimum();
        this.traverser = createTraverserFor(matchers);
    }

    protected AbstractSequence(MatcherList matchers, Traverser traverser) {
        this.originalMatcher = matchers;
        this.matcherToApply = matchers.optimum();
        this.traverser = traverser;
    }

    @Override
    public SequenceResult process(Element element) {
        MatchingVisitor visitor = new MatchingVisitor(matcherToApply);
        traverser.traverse(element, visitor);
        return visitor;
    }

    @Override
    public SequenceResult process(Set<Element> elements) {
        MatchingVisitor visitor = new MatchingVisitor(matcherToApply);
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
    public void combine(CombinatorSequence next) {
        this.nextSequence = next;
        Combinator combinator = next.getCombinator();
        this.traverser = combinator.convertPreviousTraverser(this.traverser);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(originalMatcher.toString());
        if (hasNext()) {
            b.append(nextSequence.toString());
        }
        return b.toString();
    }

    private static Traverser createTraverserFor(MatcherList matchers) {
        for (Matcher matcher: matchers) {
            MatcherType type = matcher.getType();
            if (type == MatcherType.IDENTIFIER) {
                String identifier = ((IdentifierMatcher)matcher).getIdentifier();
                return new FastIdentifierTraverser(identifier);
            } else if (type == MatcherType.PSEUDO_CLASS) {
                if (((PseudoClassMatcher)matcher).getPseudoClass() == PseudoClass.ROOT) {
                    return RootTraverser.SINGLETON;
                }
            }
        }
        return DepthFirstTraverser.SINGLETON;
    }
}
