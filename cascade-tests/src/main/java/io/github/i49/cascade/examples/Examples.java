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

package io.github.i49.cascade.examples;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.api.SelectorCompiler;

public class Examples {

    @SuppressWarnings("unused")
    public static void selectorExample() {
        Selector selector = Selector.compile("div p");
        Document doc = getDocument(); /* doc is of org.w3c.dom.Document */
        List<Element> selected = selector.select(doc.getDocumentElement());
    }

    @SuppressWarnings("unused")
    public static void namespaceExample() {
        SelectorCompiler compiler = SelectorCompiler.create()
                .withNamespace("ns", "http://www.w3.org/2000/svg");
        Selector selector = compiler.compile("ns|circle");
        Document doc = getDocument(); /* doc is of org.w3c.dom.Document */
        List<Element> selected = selector.select(doc.getDocumentElement());
    }

    private static Document getDocument() {
        return null;
    }
}
