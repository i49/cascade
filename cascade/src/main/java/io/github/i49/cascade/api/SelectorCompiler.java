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

package io.github.i49.cascade.api;

/**
 * Selector expression compiler.
 *
 * <p>
 * Each instance of this class is not thread-safe.
 * </p>
 */
public interface SelectorCompiler {

    /**
     * Creates an instance of this type.
     *
     * @return newly created instance of this type.
     */
    static SelectorCompiler create() {
        SelectorCompilerFactory factory = SelectorCompilerFactory.get();
        return factory.createCompiler();
    }

    /**
     * Declares a namespace.
     *
     * @param prefix the prefix which represents the namespace.
     * @param namespace the namespace to declare.
     * @return configured compiler.
     * @throws NullPointerException if one or more arguments are {@code null}.
     * @throws IllegalArgumentException if given {@code prefix} is blank.
     */
    SelectorCompiler withNamespace(String prefix, String namespace);

    /**
     * Declares the default namespace.
     * No default namespace is declared by default.
     * If default namespace is already declared, it will be replaced by new one.
     *
     * @param namespace the default namespace.
     * @return configured compiler.
     * @throws NullPointerException if given argument is {@code null}.
     */
    SelectorCompiler withDefaultNamespace(String namespace);

    /**
     * Compiles the given expression to generate a selector.
     *
     * @param expression the expression representing a selector.
     * @return created selector.
     * @throws NullPointerException if given {@code expression} is {@code null}.
     * @throws InvalidSelectorException if given {@code expression} has any syntax errors.
     */
    Selector compile(String expression);
}
