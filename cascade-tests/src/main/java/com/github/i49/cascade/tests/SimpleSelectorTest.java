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
            // universal
            { "/smallest-xml.xml", null, "*", expect(0) },
            { "/universal-selector-test.html", null, "*", expect(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) },
            // negated
            { "/smallest-xml.xml", null, ":not(*)", expectAllBut(0) },
            { "/universal-selector-test.html", null, ":not(*)", expectAllBut(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) },
            // type
            { "/type-selector-test.html", null, "html", expect(0) },
            { "/type-selector-test.html", null, "h1", expect(5) },
            { "/type-selector-test.html", null, "p", expect(6, 7) },
            { "/type-selector-test.html", null, "nonexistent", expect() },
            // negated
            { "/type-selector-test.html", null, ":not(html)", expectAllBut(0) },
            { "/type-selector-test.html", null, ":not(h1)", expectAllBut(5) },
            { "/type-selector-test.html", null, ":not(p)", expectAllBut(6, 7) },
            { "/type-selector-test.html", null, ":not(nonexistent)", expectAllBut() },
            // identifier
            { "/id-selector-test.html", null, "#content", expect(6) },
            { "/id-selector-test.html", "body", "#content", expect(6) },
            { "/id-selector-test.html", null, "section#content", expect(6) },
            { "/id-selector-test.html", null, "#nonexistent", expect() },
            // negated
            { "/id-selector-test.html", null, ":not(#content)", expectAllBut(6) },
            { "/id-selector-test.html", "body", ":not(#content)", expect(4, 5, 7, 8, 9) },
            { "/id-selector-test.html", null, ":not(#nonexistent)", expectAllBut() },
            // class
            { "/class-selector-test.html", null, ".hello", expect(6, 8, 10) },
            { "/class-selector-test.html", null, ".hello.java", expect(8) },
            { "/class-selector-test.html", null, ".java.hello", expect(8) },
            { "/class-selector-test.html", null, "div.hello", expect(6, 8, 10) },
            { "/class-selector-test.html", null, ".nonexistent", expect() },
            // negated
            { "/class-selector-test.html", null, ":not(.hello)", expectAllBut(6, 8, 10) },
            { "/class-selector-test.html", null, ":not(.hello):not(.java)", expect(0, 1, 2, 3, 4, 5, 7, 9, 11) },
            { "/class-selector-test.html", null, ":not(.java):not(.hello)", expect(0, 1, 2, 3, 4, 5, 7, 9, 11) },
            { "/class-selector-test.html", null, ":not(.nonexistent)", expectAllBut() },
        });
    }

    public SimpleSelectorTest(String resourceName, String startElement, String expression, Expected expected) {
        super(resourceName, startElement, expression, expected);
    }
}
