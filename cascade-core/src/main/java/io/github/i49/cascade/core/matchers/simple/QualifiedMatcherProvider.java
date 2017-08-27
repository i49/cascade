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

package io.github.i49.cascade.core.matchers.simple;

import io.github.i49.cascade.core.matchers.Matcher;

/**
 * Provider of matchers which test namespace qualified names.
 *
 * @param <T> the type of the matcher to be qualified with namespace.
 */
public interface QualifiedMatcherProvider<T extends Matcher> {

    T anyNamespace();

    /**
     * Returns the matcher qualified with the given namespace
     * defined as the default namespace.
     *
     * @param namespace the namespace of the matcher.
     * @return the matcher with namespace.
     */
    default T withNamespace(String namespace) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the matcher qualified with the given namespace.
     *
     * @param prefix the prefix representing the namespace.
     * @param namespace the namespace of the matcher.
     * @return the matcher with namespace.
     */
    T withNamespace(String prefix, String namespace);

    /**
     * Returns the matcher without any namespace.
     *
     * @return the matcher without any namespace.
     */
    T withoutNamespace();
}
