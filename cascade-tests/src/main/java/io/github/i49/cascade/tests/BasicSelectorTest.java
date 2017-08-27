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

package io.github.i49.cascade.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.api.SelectorCompiler;

public abstract class BasicSelectorTest extends AbstractSelectorTest {

    public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
    public static final String NONEXISTENT_NS = "http://www.example.org/nonexistent";

    private static String defaultNamespace = XHTML_NS;

    protected BasicSelectorTest(String rootId, String expression, Expectation expected) {
        super(rootId, expression, expected);
    }

    public static void setDefaultNamespace(String namespace) {
        defaultNamespace = namespace;
    }

    @Test
    public void testWithoutDefaultNamespace() {
        SelectorCompiler compiler = SelectorCompiler.create();
        Selector selector = compiler.compile(getExpression());
        List<Element> actual  = selector.select(getRoot());
        assertThat(actual).containsExactlyElementsOf(getExpected());
    }

    @Test
    public void testWithDefaultNamespace() {
        SelectorCompiler compiler = SelectorCompiler.create()
                .withDefaultNamespace(defaultNamespace);
        Selector selector = compiler.compile(getExpression());
        List<Element> actual  = selector.select(getRoot());
        assertThat(actual).containsExactlyElementsOf(getExpected());
    }

    @Test
    public void testWithUnknownDefaultNamespace() {
        SelectorCompiler compiler = SelectorCompiler.create()
                .withDefaultNamespace(NONEXISTENT_NS);
        Selector selector = compiler.compile(getExpression());
        List<Element> actual  = selector.select(getRoot());
        assertThat(actual).isEmpty();
    }
}
