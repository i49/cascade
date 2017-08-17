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

import org.w3c.dom.Element;

import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.AllOfMatcher;

/**
 * A skeletal implementation of {@link Sequence}.
 */
abstract class AbstractSequence implements Sequence {

    protected final Matcher matcher;
    protected final Matcher optimum;
    private Sequence previous;

    protected AbstractSequence(Matcher matcher) {
        this.matcher = matcher;
        this.optimum = matcher.optimum();
    }

    @Override
    public boolean hasPrevious() {
        return previous != null;
    }

    @Override
    public Sequence getPrevious() {
        return previous;
    }

    @Override
    public Sequence prepend(Matcher matcher, Combinator combinator) {
        Sequence previous = null;
        switch (combinator) {
        case DESCENDANT:
            previous = new DescendantSequence(matcher);
            break;
        case CHILD:
            previous = new ChildSequence(matcher);
            break;
        case ADJACENT:
            previous = new AdjacentSequence(matcher);
            break;
        case SIBLING:
            previous = new SiblingSequence(matcher);
            break;
        default:
            break;
        }
        this.previous = previous;
        return previous;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (hasPrevious()) {
            b.append(getPrevious().toString());
        }
        if (matcher instanceof AllOfMatcher) {
            AllOfMatcher aggregation = (AllOfMatcher)matcher;
            Iterator<Matcher> it = aggregation.iterator();
            if (!it.hasNext() || !it.next().getType().representsType()) {
                b.append("*");
            }
        } else if (!matcher.getType().representsType()) {
            b.append("*");
        }
        b.append(matcher.toString());
        return b.toString();
    }

    protected boolean test(Element element) {
        return optimum.matches(element);
    }

    protected boolean testPrevious(Element element, Element root) {
        if (hasPrevious()) {
            return getPrevious().test(element, root);
        } else {
            return true;
        }
    }
}
