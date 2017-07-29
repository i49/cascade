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

public class AttributeSelectorTest {

    private static Document doc;
    private SelectorCompiler compiler;
    
    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("attribute-selector-test.html");
    }
    
    @Before
    public void setUp() {
        this.compiler = SelectorCompiler.create();
    }
    
    @Test
    public void select_shouldSelectElementsByPresence() {
        Selector s = compiler.compile("[title]");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next(); 
        assertThat(e.getTagName()).isEqualTo("h1");
    }

    @Test
    public void select_shouldSelectElementsByValue() {
        Selector s = compiler.compile("[href=\"http://www.w3.org/\"]");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next(); 
        assertThat(e.getTextContent()).isEqualTo("link 2");
    }

    @Test
    public void select_shouldSelectElementsBySeparatedValue() {
        Selector s = compiler.compile("[rel~=\"copyright\"]");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next(); 
        assertThat(e.getTextContent()).isEqualTo("link 3");
    }

    @Test
    public void select_shouldSelectElementsByDashValue() {
        Selector s = compiler.compile("[hreflang|=\"en\"]");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Element::getTextContent).contains("link 4", "link 5");
    }

    @Test
    public void select_shouldSelectElementsByPrefix() {
        Selector s = compiler.compile("[type^=\"image/\"]");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next(); 
        assertThat(e.getTextContent()).isEqualTo("link 6");
    }

    @Test
    public void select_shouldSelectElementsBySuffix() {
        Selector s = compiler.compile("[href$=\".html\"]");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(4);
    }

    @Test
    public void select_shouldSelectElementsBySubstring() {
        Selector s = compiler.compile("[href*=\"example\"]");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next(); 
        assertThat(e.getTextContent()).isEqualTo("link 7");
    }
}
