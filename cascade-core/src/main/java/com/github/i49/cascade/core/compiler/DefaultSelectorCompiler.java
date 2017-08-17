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

package com.github.i49.cascade.core.compiler;

import com.github.i49.cascade.api.Selector;
import com.github.i49.cascade.api.SelectorCompiler;
import com.github.i49.cascade.core.matchers.pseudo.PseudoClassMatcherFactory;
import com.github.i49.cascade.core.message.Message;

/**
 * Default implementation of {@link SelectorCompiler}.
 */
public class DefaultSelectorCompiler implements SelectorCompiler {

    private final NamespaceRegistry namespaceRegistry;
    private final PseudoClassMatcherFactory pseudoClassMatcherFactory;

    public DefaultSelectorCompiler() {
        this.namespaceRegistry = new NamespaceRegistry();
        this.pseudoClassMatcherFactory = PseudoClassMatcherFactory.create();
    }

    @Override
    public void declare(String prefix, String namespace) {
        if (prefix == null) {
            throw new NullPointerException(Message.ARGUMENT_IS_NULL.with("prefix"));
        } else if (prefix.isEmpty()) {
            throw new IllegalArgumentException(Message.ARGUMENT_IS_BLANK.with("prefix"));
        }
        if (namespace == null) {
            throw new NullPointerException(Message.ARGUMENT_IS_NULL.with("namespace"));
        }
        namespaceRegistry.register(prefix, namespace);
    }

    @Override
    public void declareDefault(String namespace) {
        if (namespace == null) {
            throw new NullPointerException(Message.ARGUMENT_IS_NULL.with("namespace"));
        }
        namespaceRegistry.registerDefault(namespace);
    }

    @Override
    public Selector compile(String expression) {
        if (expression == null) {
            throw new NullPointerException(Message.ARGUMENT_IS_NULL.with("expression"));
        }
        SelectorParser parser = new SelectorParser(expression, namespaceRegistry, pseudoClassMatcherFactory);
        return parser.parse();
    }
}

