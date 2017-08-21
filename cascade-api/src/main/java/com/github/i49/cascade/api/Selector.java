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

import java.util.List;

import org.w3c.dom.Element;

/**
 * Cascading Style Sheets selector.
 *
 * <p>The following code snippet shows the most basic usage of this class.</p>
 * <pre><code> Selector selector = Selector.compile("div p");
 * Document doc = ... // doc is an instance of org.w3c.dom.Document
 * List&lt;Element&gt; selected = selector.select(doc.getDocumentElement());
 * </code></pre>
 *
 * <p>If you would like to configure the compiler, you can create the compiler explicitly.</p>
 * <pre><code> SelectorCompiler compiler = SelectorCompiler.create();
 * compiler.declare("ns", "http://www.w3.org/2000/svg");
 * Selector selector = compiler.compile("ns|circle");
 * Document doc = ... // doc is an instance of org.w3c.dom.Document
 * List&lt;Element&gt; selected = selector.select(doc.getDocumentElement());
 * </code></pre>
 *
 * @see SelectorCompiler
 */
public interface Selector {

    /**
     * Compiles the given expression to generate a selector.
     *
     * @param expression the expression representing a selector.
     * @return created selector.
     * @throws NullPointerException if given {@code expression} is {@code null}.
     * @throws InvalidSelectorException if given {@code expression} has any syntax errors.
     */
    static Selector compile(String expression) {
        return SelectorCompiler.create().compile(expression);
    }

    /**
     * Searches the document tree for elements matching this selector.
     * The list returned by this method does not contain any duplicates
     * and retains the order of the elements in the original document.
     *
     * @param root the root of all elements to search.
     *             All descendants of this element and given element itself will be searched.
     * @return the list of all elements found, may be empty but never be {@code null}.
     * @throws NullPointerException if the given {@code root} is {@code null}.
     */
    List<Element> select(Element root);
}
