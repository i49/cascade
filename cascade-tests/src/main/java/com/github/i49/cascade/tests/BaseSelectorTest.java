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

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.github.i49.cascade.api.Selector;

/**
 * Base test class.
 */
public abstract class BaseSelectorTest {

    protected static Document doc;

    protected final Element root;
    protected final String expression;
    private final List<Element> expected;

    protected BaseSelectorTest(String expression, Expected expected) {
        this(null, expression, expected);
    }

    protected BaseSelectorTest(String rootId, String expression, Expected expected) {
        if (rootId == null) {
            this.root = doc.getDocumentElement();
        } else {
            this.root = doc.getElementById(rootId.substring(1));
        }
        this.expression = expression;
        List<Element> all = Documents.descentandsOf(this.root);
        this.expected = filtertExpected(all, expected);
    }

    public static void loadDocument(String resourceName) {
        doc = Documents.load(resourceName);
    }

    @Test
    public void test() {
        Selector selector = Selector.compile(this.expression);
        List<Element> actual  = selector.select(this.root);
        assertThat(actual).containsExactlyElementsOf(this.expected);
    }

    protected static Expected contains(int... indices) {
        return Expected.include(indices);
    }

    protected static Expected doesNotContain(int... indices) {
        return Expected.exclude(indices);
    }

    private static List<Element> filtertExpected(List<Element> all, Expected expected) {
        List<Element> entries = new ArrayList<>();
        for (int index: expected.getEntries()) {
            entries.add(all.get(index));
        }
        if (expected.isInclusive()) {
            return entries;
        } else {
            List<Element> subtracted = new ArrayList<>(all);
            subtracted.removeAll(entries);
            return subtracted;
        }
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
