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

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Base test class.
 */
public abstract class AbstractSelectorTest {

    private static Document doc;

    private final Element root;
    private final String expression;
    private final List<Element> expected;

    public static void loadDocument(String resourceName) {
        doc = Documents.load(resourceName);
    }

    public static Document getDocument() {
        return doc;
    }

    protected AbstractSelectorTest(String rootId, String expression, Expectation expected) {
        if (rootId == null) {
            this.root = doc.getDocumentElement();
        } else {
            this.root = doc.getElementById(rootId.substring(1));
        }
        this.expression = expression;
        this.expected = expected.getExpected(this.root);
    }

    public Element getRoot() {
        return root;
    }

    public String getExpression() {
        return expression;
    }

    public List<Element> getExpected() {
        return expected;
    }

    protected static Expectation contains(int... indices) {
        return Expectation.include(indices);
    }

    protected static Expectation doesNotContain(int... indices) {
        return Expectation.exclude(indices);
    }
}
