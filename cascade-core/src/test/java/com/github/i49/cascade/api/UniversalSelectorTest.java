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

public class UniversalSelectorTest {

    private static Document doc;
    private SelectorCompiler compiler;
    
    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/universal-selector-test.html");
    }
    
    @Before
    public void setUp() {
        this.compiler = SelectorCompiler.create();
    }

    @Test
    public void select_shouldSelectAllElementsInDocument() {
        Selector s = compiler.compile("*");
        Set<Element> found = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(10).containsAll(Documents.findAll(doc));
    }
    
    @Test
    public void select_shouldSelectElementsInBody() {
        Selector s = compiler.compile("*");
        Element body = Documents.findOne(doc, "body");
        Set<Element> found = s.select(body);
        assertThat(found).hasSize(6);
    }
}
