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

import java.util.HashMap;
import java.util.Map;

import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.MatcherType;

/**
 * Base type of all matchers for pseudo class selectors.
 */
public abstract class PseudoClassMatcher implements Matcher {

    @SuppressWarnings("serial")
    private static final Map<String, PseudoClassMatcher> classes = new HashMap<String, PseudoClassMatcher>() {{
        put("root", RootMatcher.SINGLETON);
        put("empty", EmptyMatcher.SINGLETON);
    }};

    public static PseudoClassMatcher of(String className) {
        return classes.get(className);
    }

    @Override
    public MatcherType getType() {
        return MatcherType.PSEUDO_CLASS;
    }
}
