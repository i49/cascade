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

package com.github.i49.cascade.core.matchers;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 * List of matchers.
 */
public class MatcherList extends ArrayList<Matcher> implements Matcher {

    private static final long serialVersionUID = 1L;

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
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            Matcher m = get(i);
            if (i == 0 && !m.getType().representsType()) {
                b.append("*");
            }
            b.append(m.toString());
        }
        return b.toString();
    }

    public Matcher findFirst(MatcherType type) {
        for (Matcher m: this) {
            if (m.getType() == type) {
                return m;
            }
        }
        return null;
    }

    public boolean contains(MatcherType type) {
        for (Matcher m: this) {
            if (m.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public MatcherList without(Matcher matcher) {
        MatcherList newList = new MatcherList();
        newList.addAll(this);
        newList.remove(matcher);
        return newList;
    }

    @Override
    public Matcher optimum() {
        if (size() == 1) {
            return get(0);
        }
        return this;
    }
}
