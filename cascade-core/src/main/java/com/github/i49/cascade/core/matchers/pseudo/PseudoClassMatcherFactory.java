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

import java.util.EnumMap;
import java.util.Map;

public class PseudoClassMatcherFactory {

    private static final PseudoClassMatcherFactory SINGLETON = new PseudoClassMatcherFactory();

    private final Map<PseudoClass, PseudoClassMatcher> registry = new EnumMap<>(PseudoClass.class);

    public static PseudoClassMatcherFactory create() {
        return SINGLETON;
    }

    public PseudoClassMatcher createMatcher(PseudoClass pseudoClass) {
        return registry.get(pseudoClass);
    }

    public PseudoClassMatcher createMatcher(PseudoClass pseudoClass, Parity parity) {
        switch (pseudoClass) {
        case NTH_CHILD:
            return (parity == Parity.ODD) ? NthChildMatcher.ODD : NthChildMatcher.EVEN;
        case NTH_LAST_CHILD:
            return (parity == Parity.ODD) ? NthLastChildMatcher.ODD : NthLastChildMatcher.EVEN;
        default:
            return null;
        }
    }

    public PseudoClassMatcher createMatcher(PseudoClass pseudoClass, int a, int b) {
        switch (pseudoClass) {
        case NTH_CHILD:
            return NthChildMatcher.of(a, b);
        case NTH_LAST_CHILD:
            return NthLastChildMatcher.of(a, b);
        default:
            return null;
        }
    }

    private PseudoClassMatcherFactory() {
        register(RootMatcher.SINGLETON);
        register(EmptyMatcher.SINGLETON);
        register(FirstChildMatcher.SINGLETON);
        register(LastChildMatcher.SINGLETON);
        register(OnlyChildMatcher.SINGLETON);
    }

    private void register(PseudoClassMatcher matcher) {
        registry.put(matcher.getPseudoClass(), matcher);
    }
}