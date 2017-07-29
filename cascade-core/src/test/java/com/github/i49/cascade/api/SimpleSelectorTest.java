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

package com.github.i49.cascade.api;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit tests for simple selectors.
 */
public class SimpleSelectorTest {

    private static Document doc;
    private SelectorCompiler compiler;
    
    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("simple-selector-test.html");
    }
    
    @Before
    public void setUp() {
        this.compiler = SelectorCompiler.create();
    }

    @Test
    public void typeSelector_shouldSelectElement() {
        Selector s = compiler.compile("address");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next();
        assertThat(e.getTagName()).isEqualTo("address");
    }

    @Test
    public void typeSelector_shouldSelectRootElement() {
        Selector s = compiler.compile("html");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next();
        assertThat(e.getTagName()).isEqualTo("html");
    }
   
    @Test
    public void typeSelector_shouldSelectMultipleElements() {
        Selector s = compiler.compile("p");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(3);
    }

    @Test
    public void typeSelector_shouldSelectNoElements() {
        Selector s = compiler.compile("object");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).isNotNull().isEmpty();
    }

    @Test
    public void idSelector_shouldSelectElement() {
        Selector s = compiler.compile("#i2");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next();
        assertThat(e.getTagName()).isEqualTo("li");
        assertThat(e.getTextContent()).isEqualTo("list item 2");
    }
    
    @Test
    public void classSelector_shouldSelectElement() {
        Selector s = compiler.compile(".c2");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next();
        assertThat(e.getTagName()).isEqualTo("dt");
        assertThat(e.getTextContent()).isEqualTo("term2");
    }
 
    @Test
    public void selector_shouldThrowExceptionIfRootIsNull() {
        Selector s = compiler.compile("p");
        Throwable thrown = catchThrowable(()->{
            s.select(null);
        });
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
