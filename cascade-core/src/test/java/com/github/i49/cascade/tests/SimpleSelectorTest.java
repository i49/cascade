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

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for universal selector, type selector, identifier selector and class selector.
 */
@RunWith(Parameterized.class)
public class SimpleSelectorTest extends BaseSelectorTest {
    
    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            /* universal */
            { "/smallest-xml.xml", "*", 1 },
            { "/universal-selector-test.html", "*", 10 },
            /* type */
            { "/type-selector-test.html", "html", 1 },
            { "/type-selector-test.html", "h1", 1 },
            { "/type-selector-test.html", "p", 2 },
            { "/type-selector-test.html", "nonexistent", 0 },
            /* identifier */
            { "/id-selector-test.html", "#content", 1 },
            { "/id-selector-test.html", "section#content", 1 },
            { "/id-selector-test.html", "#nonexistent", 0 },
            /* class */
            { "/class-selector-test.html", ".hello", 3 },
            { "/class-selector-test.html", ".hello.java", 1 },
            { "/class-selector-test.html", ".java.hello", 1 },
            { "/class-selector-test.html", "div.hello", 3 },
            { "/class-selector-test.html", ".nonexistent", 0 },
        });
    }
    
    public SimpleSelectorTest(String resourceName, String expression, int expectedCount) {
        super(resourceName, expression, expectedCount);
    }
}
