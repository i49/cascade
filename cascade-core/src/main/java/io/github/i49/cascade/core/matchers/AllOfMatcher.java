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

package io.github.i49.cascade.core.matchers;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

/**
 * Matcher which tests all of entry matchers
 */
public class AllOfMatcher implements Matcher, Iterable<Matcher> {

    private final List<Matcher> matchers;

    public AllOfMatcher(List<Matcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public MatcherType getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean matches(Element element) {
        for (Matcher m: this) {
            if (!m.matches(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean matchesAlways() {
        for (Matcher m: this) {
            if (!m.matchesAlways()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean matchesNever() {
        for (Matcher m: this) {
            if (m.matchesNever()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Matcher m: this) {
            b.append(m.toString());
        }
        return b.toString();
    }

    @Override
    public Matcher optimum() {
        if (matchesAlways()) {
            return AlwaysMatcher.get();
        } else if (matchesNever()) {
            return NeverMatcher.get();
        }

        List<Matcher> matchers = this.matchers.stream()
                .filter(m->!m.matchesAlways())
                .collect(Collectors.toList());
        if (this.matchers.size() == matchers.size()) {
            return this;
        } else if (matchers.isEmpty()) {
            return AlwaysMatcher.get();
        } else {
            return new AllOfMatcher(matchers);
        }
    }

    @Override
    public Iterator<Matcher> iterator() {
        return matchers.iterator();
    }

    public Matcher find(Predicate<Matcher> predicate) {
        for (Matcher matcher: this) {
            if (predicate.test(matcher)) {
                return matcher;
            }
        }
        return null;
    }
}
