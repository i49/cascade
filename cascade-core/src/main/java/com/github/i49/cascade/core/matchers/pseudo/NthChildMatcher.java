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

package com.github.i49.cascade.core.matchers.pseudo;

import static com.github.i49.cascade.core.dom.Elements.countSiblingsBefore;

import org.w3c.dom.Element;

/**
 * Matcher for :nth-child pseudo-class selector.
 */
public class NthChildMatcher extends OrdinalPositionMatcher {

    public static final NthChildMatcher ODD = new NthChildMatcher(Parity.ODD);
    public static final NthChildMatcher EVEN = new NthChildMatcher(Parity.EVEN);

    public static NthChildMatcher of(int a, int b) {
        return new NthChildMatcher(a, b);
    }

    private NthChildMatcher(int a, int b) {
        super(a, b);
    }

    private NthChildMatcher(Parity parity) {
        super(parity);
    }

    @Override
    protected int countSiblings(Element element) {
        return countSiblingsBefore(element);
    }

    @Override
    public PseudoClass getPseudoClass() {
        return PseudoClass.NTH_CHILD;
    }
}
