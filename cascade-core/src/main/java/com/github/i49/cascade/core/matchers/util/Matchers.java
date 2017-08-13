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

package com.github.i49.cascade.core.matchers.util;

import java.util.function.Predicate;

import com.github.i49.cascade.core.matchers.AllOfMatcher;
import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.MatcherType;
import com.github.i49.cascade.core.matchers.pseudo.PseudoClass;
import com.github.i49.cascade.core.matchers.pseudo.PseudoClassMatcher;

public final class Matchers {

    public static Matcher findOneOfType(Matcher matcher, MatcherType type) {
        return findMatcher(matcher, m->m.getType() == type);
    }

    public static Matcher findOneOfPseudoClass(Matcher matcher, PseudoClass pseudoClass) {
        return findMatcher(matcher, m->{
           return m.getType() == MatcherType.PSEUDO_CLASS &&
                  ((PseudoClassMatcher)m).getPseudoClass() == pseudoClass;
        });
    }

    private static Matcher findMatcher(Matcher matcher, Predicate<Matcher> predicate) {
        if (matcher instanceof AllOfMatcher) {
            for (Matcher entry: (AllOfMatcher)matcher) {
                if (predicate.test(entry)) {
                    return entry;
                }
            }
        } else if (predicate.test(matcher)) {
            return matcher;
        }
        return null;
    }

    private Matchers() {
    }
}
