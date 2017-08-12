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

/**
 * All pseudo-classes.
 */
public enum PseudoClass {
    // Dynamic pseudo-classes

    LINK("link"),
    VISITED("visited"),
    HOVER("hover"),
    ACTIVE("active"),
    FOCUS("focus"),

    // Target pseudo-classes

    TARGET("target"),

    // Language pseudo-classes

    LANG("lang"),

    // UI element states pseudo-classes

    ENABLED("enabled"),
    DISABLED("disabled"),
    CHECKED("checked"),

    // Structual pseudo-classes

    ROOT("root"),
    NTH_CHILD("nth-child"),
    NTH_LAST_CHILD("nth-last-child"),
    NTH_OF_TYPE("nth-of-type"),
    NTH_LAST_OF_TYPE("nth-last-of-type"),
    FIRST_CHILD("first-child"),
    LAST_CHILD("last-child"),
    FIRST_OF_TYPE("first-of-type"),
    LAST_OF_TYPE("last-of-type"),
    ONLY_CHILD("only-child"),
    ONLY_OF_TYPE("only-of-type"),
    EMPTY("empty"),

    // Negation pseudo-classes

    NOT("not")
    ;

    private static final Map<String, PseudoClass> values = new HashMap<>();

    static {
        for (PseudoClass value: values()) {
            values.put(value.getClassName(), value);
        }
    };

    private String className;

    private PseudoClass(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    /**
     * Checks if this pseudo-class is functional and requires arguments enclosed by parentheses.
     *
     * @return {@code true} if this pseudo-class is functional.
     */
    public boolean isFunctional() {
        return this == LANG ||
               this == NTH_CHILD ||
               this == NTH_LAST_CHILD ||
               this == NTH_OF_TYPE ||
               this == NTH_LAST_OF_TYPE ||
               this == NOT
               ;
    }

    @Override
    public String toString() {
        return ":" + getClassName();
    }

    public static PseudoClass byName(String className) {
        return values.get(className);
    }
}
