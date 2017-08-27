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

import org.w3c.dom.Element;

/**
 * The matcher which never match any element
 * used for matcher optimization.
 */
public class NeverMatcher implements Matcher {

    private static final NeverMatcher SINGLETON = new NeverMatcher();

    public static Matcher get() {
        return SINGLETON;
    }

    private NeverMatcher() {
    }

    @Override
    public MatcherType getType() {
        return MatcherType.NEVER;
    }

    @Override
    public boolean matches(Element element) {
        return false;
    }

    @Override
    public boolean matchesNever() {
        return true;
    }
}
