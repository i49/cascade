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

package io.github.i49.cascade.core.matchers.pseudo;

import static io.github.i49.cascade.core.dom.Elements.countSameTypeBefore;

import org.w3c.dom.Element;

/**
 * Matcher for :nth-child pseudo-class selector.
 */
public class NthOfTypeMatcher extends OrdinalPositionMatcher {

    private static final NthOfTypeMatcher ODD = new NthOfTypeMatcher(Parity.ODD);
    private static final NthOfTypeMatcher EVEN = new NthOfTypeMatcher(Parity.EVEN);

    public static NthOfTypeMatcher of(int a, int b) {
        return new NthOfTypeMatcher(a, b);
    }

    public static NthOfTypeMatcher of(Parity parity) {
        if (parity == Parity.ODD) {
            return ODD;
        } else {
            return EVEN;
        }
    }

    private NthOfTypeMatcher(int a, int b) {
        super(a, b);
    }

    private NthOfTypeMatcher(Parity parity) {
        super(parity);
    }

    @Override
    protected int countSiblingsAround(Element element) {
        return countSameTypeBefore(element);
    }

    @Override
    public PseudoClass getPseudoClass() {
        return PseudoClass.NTH_OF_TYPE;
    }
}
