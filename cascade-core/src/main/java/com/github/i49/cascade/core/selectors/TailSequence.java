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

import org.w3c.dom.Element;

import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.MatcherType;
import com.github.i49.cascade.core.matchers.pseudo.PseudoClass;
import com.github.i49.cascade.core.matchers.simple.IdentifierMatcher;
import com.github.i49.cascade.core.matchers.util.Matchers;
import com.github.i49.cascade.core.traversers.DepthFirstTraverser;
import com.github.i49.cascade.core.traversers.FastIdentifierTraverser;
import com.github.i49.cascade.core.traversers.RootTraverser;
import com.github.i49.cascade.core.traversers.Traverser;

/**
 * The last sequence of sequences combined by combinators.
 */
public class TailSequence extends AbstractSequence {

    public TailSequence(Matcher matcher) {
        super(matcher);
    }

    @Override
    public boolean test(Element element, Element root) {
        return test(element) && testPrevious(element, root);
    }

    public Traverser createTraverser() {
        Matcher found = Matchers.findOneOfPseudoClass(matcher, PseudoClass.ROOT);
        if (found != null) {
            return RootTraverser.SINGLETON;
        }
        found = Matchers.findOneOfType(matcher, MatcherType.IDENTIFIER);
        if (found != null) {
            String identifier = ((IdentifierMatcher)found).getIdentifier();
            return new FastIdentifierTraverser(identifier);
        }
        return DepthFirstTraverser.SINGLETON;
    }
}