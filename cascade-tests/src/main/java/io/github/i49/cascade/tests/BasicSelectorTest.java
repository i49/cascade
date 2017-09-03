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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Test;
import org.w3c.dom.Element;

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.api.SelectorCompiler;

public abstract class BasicSelectorTest {

    public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
    public static final String NONEXISTENT_NS = "http://www.example.org/nonexistent";

    private final Fixture fixture;
    private String defaultNamespace = XHTML_NS;
    
    protected BasicSelectorTest(Fixture fixture) {
        this.fixture = fixture;
    }

    public void setDefaultNamespace(String namespace) {
        defaultNamespace = namespace;
    }

    @Test
    public void testWithoutDefaultNamespace() {
        SelectorCompiler compiler = SelectorCompiler.create();
        Selector selector = compiler.compile(fixture.getExpression());
        List<Element> actual = selector.select(fixture.getStartElement());
        assertThat(actual, fixture.getMatcher());
    }

    @Test
    public void testWithDefaultNamespace() {
        SelectorCompiler compiler = SelectorCompiler.create()
                .withDefaultNamespace(defaultNamespace);
        Selector selector = compiler.compile(fixture.getExpression());
        List<Element> actual = selector.select(fixture.getStartElement());
        assertThat(actual, fixture.getMatcher());
    }

    @Test
    public void testWithUnknownDefaultNamespace() {
        SelectorCompiler compiler = SelectorCompiler.create()
                .withDefaultNamespace(NONEXISTENT_NS);
        Selector selector = compiler.compile(fixture.getExpression());
        List<Element> actual = selector.select(fixture.getStartElement());
        assertThat(actual.size(), is(equalTo(0)));
    }
}
