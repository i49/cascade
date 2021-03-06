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
 * The base type for performing various kinds of matching against given element.
 */
public interface Matcher {

    /**
     * Returns the type of this matcher.
     *
     * @return the type of this matcher.
     */
    MatcherType getType();

    /**
     * Performs matching for given element.
     *
     * @param element the element to check, cannot be {@code null}.
     * @return {@code true} if given element satisfied the condition, {@code false} otherwise.
     */
    boolean matches(Element element);

    /**
     * Checks if this matcher matches any element.
     *
     * @return {@code true} if this matcher matches an element, {@code false} otherwise.
     */
    default boolean matchesAlways() {
        return false;
    }

    /**
     * Checks if this matcher never match any element.
     *
     * @return {@code true} if this matcher never match any element, {@code false} otherwise.
     */
    default boolean matchesNever() {
        return false;
    }

    /**
     * Returns the optimum matcher of this one.
     *
     * @return the optimum matcher.
     */
    default Matcher optimum() {
        return this;
    }
}
