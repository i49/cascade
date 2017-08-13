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
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.i49.cascade.api.Selector;

/**
 * Base class of all selector tests.
 */
public abstract class BaseSelectorTest {

    private final String resourceName;
    private final String startElement;
    private final String expression;
    private final Expected expected;

    private static Map<String, Document> documentCache;

    protected BaseSelectorTest(String resourceName, String startElement, String expression, Expected expected) {
        this.resourceName = resourceName;
        this.startElement = startElement;
        this.expression = expression;
        this.expected = expected;
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
        Selector selector = Selector.compile(expression);
        List<Element> selected  = selector.select(getStartingElement(doc));
        if (expected == null) {
            return;
        }

        int[] entries = expected.getEntries();
        if (expected.isInclusive()) {
            assertThat(selected).hasSize(entries.length);
            NodeList nodes = doc.getElementsByTagName("*");
            int i = 0;
            for (Element actual: selected) {
                Element expected = (Element)nodes.item(entries[i++]);
                assertThat(actual).isSameAs(expected);
            }
        } else {
            NodeList nodes = doc.getElementsByTagName("*");
            assertThat(selected).hasSize(nodes.getLength() - entries.length);
            for (int index: entries) {
                Element excluded = (Element)nodes.item(index);
                assertThat(selected).doesNotContain(excluded);
            }
        }
    }

    protected static Expected contains(int... indices) {
        return Expected.include(indices);
    }

    protected static Expected doesNotContain(int... indices) {
        return Expected.exclude(indices);
    }

    private Document loadDocument() {
        Document doc = documentCache.get(this.resourceName);
        if (doc == null) {
            doc = Documents.load(this.resourceName);
            documentCache.put(this.resourceName, doc);
        }
        return doc;
    }

    private Element getStartingElement(Document doc) {
        if (startElement == null) {
            return doc.getDocumentElement();
        }
        NodeList nodes = doc.getElementsByTagName(startElement);
        Element element = (Element)nodes.item(0);
        if (element == null) {
            throw new IllegalArgumentException("Starting element " + startElement + " was not found.");
        }
        return element;
    }

    public static class Expected {

        private final boolean inclusive;
        private final int[] entries;

        public static Expected include(int[] entries) {
            return new Expected(entries, true);
        }

        public static Expected exclude(int[] entries) {
            return new Expected(entries, false);
        }

        private Expected(int[] entries, boolean inclusive) {
            this.entries = entries;
            this.inclusive = inclusive;
        }

        public boolean isInclusive() {
            return inclusive;
        }

        public int[] getEntries() {
            return entries;
        }
    }
}
