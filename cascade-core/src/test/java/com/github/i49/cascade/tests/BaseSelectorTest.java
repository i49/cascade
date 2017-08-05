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

package com.github.i49.cascade.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.github.i49.cascade.api.Selector;

public abstract class BaseSelectorTest {

    private final String resourceName;
    private final String expression;
    private final int expectedCount;

    private static Map<String, Document> documentCache;
    
    protected BaseSelectorTest(String resourceName, String expression, int expectedCount) {
        this.resourceName = resourceName;
        this.expression = expression;
        this.expectedCount = expectedCount;
    }
    
    @BeforeClass
    public static void setUpOnce() {
        documentCache = new HashMap<>();
    }
    
    @AfterClass
    public static void tearDownOnce() {
        documentCache.clear();
        documentCache = null;
    }

    @Test
    public void test() {
        Document doc = loadDocument();
        Selector s = Selector.compile(expression);
        Set<Element> found  = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(expectedCount);
    }
    
    private Document loadDocument() {
        Document doc = documentCache.get(this.resourceName);
        if (doc == null) {
            doc = Documents.load(this.resourceName);
            documentCache.put(this.resourceName, doc);
        }
        return doc;
    }
} 
