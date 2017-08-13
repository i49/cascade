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

import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Element;

import com.github.i49.cascade.core.matchers.IdentifierMatcher;
import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.AllOfMatcher;
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

    protected AbstractSequence(Matcher matcher) {
        this.originalMatcher = matcher;
        this.matcherToApply = matcher.optimum();
        this.traverser = createTraverserFor(matcher);
    }

    protected AbstractSequence(Matcher matcher, Traverser traverser) {
        this.originalMatcher = matcher;
        this.matcherToApply = matcher.optimum();
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

        if (originalMatcher instanceof AllOfMatcher) {
            AllOfMatcher aggregation = (AllOfMatcher)originalMatcher;
            Iterator<Matcher> it = aggregation.iterator();
            if (!it.hasNext() || !it.next().getType().representsType()) {
                b.append("*");
            }
        } else if (!originalMatcher.getType().representsType()) {
            b.append("*");
        }
        b.append(originalMatcher.toString());

        if (hasNext()) {
            b.append(nextSequence.toString());
        }

        return b.toString();
    }

    private static Traverser createTraverserFor(Matcher matcher) {
        if (matcher instanceof AllOfMatcher) {
            AllOfMatcher aggregation = (AllOfMatcher)matcher;
            Matcher found = aggregation.find(AbstractSequence::isRootMatcher);
            if (found != null) {
                return RootTraverser.SINGLETON;
            }
            found = aggregation.find(AbstractSequence::isIdMatcher);
            if (found != null) {
                String identifier = ((IdentifierMatcher)found).getIdentifier();
                return new FastIdentifierTraverser(identifier);
            }
        } else {
            if (isRootMatcher(matcher)) {
                return RootTraverser.SINGLETON;
            } else if (isIdMatcher(matcher)) {
                String identifier = ((IdentifierMatcher)matcher).getIdentifier();
                return new FastIdentifierTraverser(identifier);
            }
        }
        return DepthFirstTraverser.SINGLETON;
    }

    private static boolean isRootMatcher(Matcher matcher) {
        return matcher.getType() == MatcherType.PSEUDO_CLASS &&
               ((PseudoClassMatcher)matcher).getPseudoClass() == PseudoClass.ROOT;
    }

    private static boolean isIdMatcher(Matcher matcher) {
        return matcher.getType() == MatcherType.IDENTIFIER;
    }
}
