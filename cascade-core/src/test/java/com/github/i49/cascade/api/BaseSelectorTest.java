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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class BaseSelectorTest {

    private final String resourceName;
    private final String expression;
    private final int expectedCount;
    
    protected BaseSelectorTest(String resourceName, String expression, int expectedCount) {
        this.resourceName = resourceName;
        this.expression = expression;
        this.expectedCount = expectedCount;
    }

    @Test
    public void test() {
        Document doc = loadDocument();
        Selector s = Selector.compile(expression);
        Set<Element> found  = s.select(doc.getDocumentElement());
        assertThat(found).hasSize(expectedCount);
    }
    
    private Document loadDocument() {
        return Documents.load(this.resourceName);
    }
}
