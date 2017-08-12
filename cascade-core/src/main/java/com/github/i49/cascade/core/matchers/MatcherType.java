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

/**
 * Matcher types categorized by simple selector types.
 */
public enum MatcherType {
    /** Matcher for type selector. */
    TYPE,
    /** Matcher for universal selector. */
    UNIVERSAL,
    /** Matcher for attribute selector. */
    ATTRIBUTE,
    /** Matcher for class selector. */
    CLASS,
    /** Matcher for ID selector. */
    IDENTIFIER,
    /** Matcher for pseudo-class. */
    PSEUDO_CLASS,
    /** Matcher which matches any element. */
    ALWAYS,
    /** Matcher which never match any element. */
    NEVER
    ;

    /**
     * Checks if this type represents a element type.
     *
     * @return {@code true} if current instance is {@link #TYPE} or {@link #UNIVERSAL}.
     */
    public boolean representsType() {
        return this == TYPE || this == UNIVERSAL;
    }
}
