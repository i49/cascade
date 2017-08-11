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

import static com.github.i49.cascade.core.dom.Elements.countSiblingsAfter;

import org.w3c.dom.Element;

/**
 * Matcher for :nth-child pseudo-class selector.
 */
public class NthLastChildMatcher extends OrdinalPositionMatcher {

    public static final NthLastChildMatcher ODD = new NthLastChildMatcher(Parity.ODD);
    public static final NthLastChildMatcher EVEN = new NthLastChildMatcher(Parity.EVEN);

    private static final NthLastChildMatcher NEVER = new NthLastChildMatcher(0, 0) {
        @Override
        public boolean matches(Element element) {
            return false;
        }
    };

    public static NthLastChildMatcher of(int a, int b) {
        if (a == 0 && b == 0) {
            return NEVER;
        }
        return new NthLastChildMatcher(a, b);
    }

    private NthLastChildMatcher(int a, int b) {
        super(a, b);
    }

    private NthLastChildMatcher(Parity parity) {
        super(parity);
    }

    @Override
    protected int getOrdinal(Element element) {
        return countSiblingsAfter(element) + 1;
    }

    @Override
    public PseudoClass getPseudoClass() {
        return PseudoClass.NTH_LAST_CHILD;
    }
}
