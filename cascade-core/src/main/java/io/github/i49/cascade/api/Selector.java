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

package io.github.i49.cascade.api;

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
 * <pre><code> SelectorCompiler compiler = SelectorCompiler.create()
 *         .withNamespace("ns", "http://www.w3.org/2000/svg");
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
     * @param start the starting point of all elements to search.
     *              All descendants of this element including this element will be searched.
     *              This element must exist in the document tree.
     * @return the list of all elements found, may be empty but never be {@code null}.
     * @throws NullPointerException if the given {@code start} is {@code null}.
     * @throws IllegalArgumentException if the given {@code start} does not exist in the document tree.
     */
    List<Element> select(Element start);

    /**
     * Returns the string representation of this selector.
     * <p>
     * The string to be returned is almost same as the text initially passed to the compiler,
     * but will be in normalized form.
     * e.g., for "[title=hello]" this method returns "*[title="hello"]".
     * </p>
     *
     * @return the string representation of this selector.
     */
    @Override
    String toString();
}
