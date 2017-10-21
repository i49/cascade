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

import static io.github.i49.cascade.core.dom.Elements.countSameTypeAfter;

import org.w3c.dom.Element;

/**
 * Matcher for :nth-child pseudo-class selector.
 */
public class NthLastOfTypeMatcher extends OrdinalPositionMatcher {

    private static final NthLastOfTypeMatcher ODD = new NthLastOfTypeMatcher(Parity.ODD);
    private static final NthLastOfTypeMatcher EVEN = new NthLastOfTypeMatcher(Parity.EVEN);

    public static NthLastOfTypeMatcher of(int a, int b) {
        return new NthLastOfTypeMatcher(a, b);
    }

    public static NthLastOfTypeMatcher of(Parity parity) {
        if (parity == Parity.ODD) {
            return ODD;
        } else {
            return EVEN;
        }
    }

    private NthLastOfTypeMatcher(int a, int b) {
        super(a, b);
    }

    private NthLastOfTypeMatcher(Parity parity) {
        super(parity);
    }

    @Override
    protected int countSiblingsAround(Element element) {
        return countSameTypeAfter(element);
    }

    @Override
    public PseudoClass getPseudoClass() {
        return PseudoClass.NTH_LAST_OF_TYPE;
    }
}
