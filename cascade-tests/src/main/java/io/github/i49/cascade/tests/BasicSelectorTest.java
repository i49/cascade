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

    /**
     * Tests the selector compiled without any default namespace.
     */
    @Test
    public void testWithoutDefaultNamespace() {
        // given
        SelectorCompiler compiler = SelectorCompiler.create();
        Selector selector = compiler.compile(getFixture().getExpression());
        
        // when
        List<Element> actual = selector.select(getFixture().getStartElement());

        // then
        assertThat(actual, getFixture().getMatcher());
    }

    /**
     * Tests the selector compiled with proper default namespace.
     */
    @Test
    public void testWithDefaultNamespace() {
        // given
        SelectorCompiler compiler = SelectorCompiler.create()
                .withDefaultNamespace(getDefaultNamespace());
        Selector selector = compiler.compile(getFixture().getExpression());
        
        // when
        List<Element> actual = selector.select(getFixture().getStartElement());

        // then
        assertThat(actual, getFixture().getMatcher());
    }

    /**
     * Tests the selector compiled with nonexistent default namespace.
     */
    @Test
    public void testWithUnknownDefaultNamespace() {
        // given
        SelectorCompiler compiler = SelectorCompiler.create()
                .withDefaultNamespace(Namespaces.NONEXISTENT);
        Selector selector = compiler.compile(getFixture().getExpression());
 
        // when
        List<Element> actual = selector.select(getFixture().getStartElement());
        
        // then
        assertThat(actual.size(), is(equalTo(0)));
    }
    
    public String getDefaultNamespace() {
        return Namespaces.XHTML;
    }

    public abstract Fixture getFixture();
}
