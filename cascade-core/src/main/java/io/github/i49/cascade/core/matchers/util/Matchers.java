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

package io.github.i49.cascade.core.matchers.util;

import java.util.List;
import java.util.function.Predicate;

import io.github.i49.cascade.core.matchers.AllOfMatcher;
import io.github.i49.cascade.core.matchers.Matcher;
import io.github.i49.cascade.core.matchers.MatcherType;
import io.github.i49.cascade.core.matchers.pseudo.NegationMatcher;
import io.github.i49.cascade.core.matchers.pseudo.PseudoClass;
import io.github.i49.cascade.core.matchers.pseudo.PseudoClassMatcher;

public final class Matchers {

    public static Matcher allOf(List<Matcher> matchers) {
        assert(!matchers.isEmpty());
        if (matchers.size() == 1) {
            return matchers.iterator().next();
        } else {
            return new AllOfMatcher(matchers);
        }
    }

    /**
     * Creates a new matcher which negates the given matcher.
     *
     * @param matcher the matcher to be negated.
     * @return newly created matcher.
     */
    public static Matcher negate(Matcher matcher) {
        assert(matcher != null);
        return (matcher != null) ? new NegationMatcher(matcher) : null;
    }

    public static Matcher extractByType(Matcher matcher, MatcherType type) {
        return findMatcher(matcher, m->m.getType() == type);
    }

    public static Matcher extractByPseudoClass(Matcher matcher, PseudoClass pseudoClass) {
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
