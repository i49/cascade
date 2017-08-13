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
            { "/smallest-xml.xml", null, "*", contains(0) },
            { "/universal-selector-test.html", null, "*", contains(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) },
            // negated
            { "/smallest-xml.xml", null, ":not(*)", doesNotContain(0) },
            { "/universal-selector-test.html", null, ":not(*)", doesNotContain(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) },
            // type
            { "/type-selector-test.html", null, "html", contains(0) },
            { "/type-selector-test.html", null, "h1", contains(5) },
            { "/type-selector-test.html", null, "p", contains(6, 7) },
            { "/type-selector-test.html", null, "nonexistent", contains() },
            // negated
            { "/type-selector-test.html", null, ":not(html)", doesNotContain(0) },
            { "/type-selector-test.html", null, ":not(h1)", doesNotContain(5) },
            { "/type-selector-test.html", null, ":not(p)", doesNotContain(6, 7) },
            { "/type-selector-test.html", null, ":not(nonexistent)", doesNotContain() },
            // identifier
            { "/id-selector-test.html", null, "#content", contains(6) },
            { "/id-selector-test.html", "body", "#content", contains(6) },
            { "/id-selector-test.html", null, "section#content", contains(6) },
            { "/id-selector-test.html", null, "#nonexistent", contains() },
            // negated
            { "/id-selector-test.html", null, ":not(#content)", doesNotContain(6) },
            { "/id-selector-test.html", "body", ":not(#content)", contains(4, 5, 7, 8, 9) },
            { "/id-selector-test.html", null, ":not(#nonexistent)", doesNotContain() },
            // class
            { "/class-selector-test.html", null, ".hello", contains(6, 8, 10) },
            { "/class-selector-test.html", null, ".hello.java", contains(8) },
            { "/class-selector-test.html", null, ".java.hello", contains(8) },
            { "/class-selector-test.html", null, "div.hello", contains(6, 8, 10) },
            { "/class-selector-test.html", null, ".nonexistent", contains() },
            // negated
            { "/class-selector-test.html", null, ":not(.hello)", doesNotContain(6, 8, 10) },
            { "/class-selector-test.html", null, ":not(.hello):not(.java)", contains(0, 1, 2, 3, 4, 5, 7, 9, 11) },
            { "/class-selector-test.html", null, ":not(.java):not(.hello)", contains(0, 1, 2, 3, 4, 5, 7, 9, 11) },
            { "/class-selector-test.html", null, ":not(.nonexistent)", doesNotContain() },
        });
    }

    public SimpleSelectorTest(String resourceName, String startElement, String expression, Expected expected) {
        super(resourceName, startElement, expression, expected);
    }
}
