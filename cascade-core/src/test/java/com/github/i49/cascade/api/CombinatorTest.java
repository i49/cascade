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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CombinatorTest {

    private static Document doc;
    private SelectorCompiler compiler;
    
    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/combinator-test.html");
    }
    
    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }
    
    @Before
    public void setUp() {
        this.compiler = SelectorCompiler.create();
    }
    
    /* descendant combinator */
    
    @Test
    public void select_shouldSelectDescendantElements() {
        Selector s = compiler.compile("div.content p");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found)
            .hasSize(3)
            .extracting(Element::getTextContent)
            .containsExactly("paragraph 1", "paragraph 2", "paragraph 3");
    }

    /* child combinator */

    @Test
    public void select_shouldSelectChildElements() {
        Selector s = compiler.compile("div > p");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found)
            .hasSize(2)
            .extracting(Element::getTextContent)
            .containsExactly("paragraph 1", "paragraph 3");
    }
    
    /* adjacent combinator */

    @Test
    public void select_shouldSelectAdjacentElement() {
        Selector s = compiler.compile("h1.opener + h2");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found)
            .hasSize(1)
            .extracting(Element::getTextContent)
            .containsExactly("heading level 2");
    }

    @Test
    public void select_shouldSelectMultipleAdjacentElements() {
        Selector s = compiler.compile("article > p + p");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found)
            .hasSize(2)
            .extracting(Element::getTextContent)
            .containsExactly("paragraph b", "paragraph c");
    }

    /* sibling combinator */

    @Test
    public void select_shouldSelectSiblingElement() {
        Selector s = compiler.compile("h2 ~ pre");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(1);
        Element e = found.iterator().next();
        assertThat(e.getTagName()).isEqualTo("pre");
    }

    @Test
    public void select_shouldSelectMultipleSiblingElements() {
        Selector s = compiler.compile("article > p ~ p");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found)
            .hasSize(3)
            .extracting(Element::getTextContent)
            .containsExactly("paragraph b", "paragraph c", "paragraph d");
    }
}
