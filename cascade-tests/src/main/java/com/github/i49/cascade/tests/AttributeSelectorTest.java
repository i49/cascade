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
 * Tests for attribute selectors.
 */
@RunWith(Parameterized.class)
public class AttributeSelectorTest extends BaseSelectorTest {

    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // presence
            { "/attribute-presence-selector-test.html", "[title]", expect(5) },
            // negated
            { "/attribute-presence-selector-test.html", ":not([title])", expectAllBut(5) },
            // value
            { "/attribute-value-selector-test.html", "[href=\"http://www.w3.org/\"]", expect(8) },
            // negated
            { "/attribute-value-selector-test.html", ":not([href=\"http://www.w3.org/\"])", expectAllBut(8) },
            // space-separated
            { "/attribute-value-selector-test.html", "[rel~=\"copyright\"]", expect(10) },
            { "/attribute-value-selector-test.html", "[rel~=\"copyright copyleft\"]", expect() },
            // negated
            { "/attribute-value-selector-test.html", ":not([rel~=\"copyright\"])", expectAllBut(10) },
            { "/attribute-value-selector-test.html", ":not([rel~=\"copyright copyleft\"])", expectAllBut() },
            // dash-separated
            { "/attribute-value-selector-test.html", "[hreflang|=\"en\"]", expect(12, 14) },
            { "/attribute-value-selector-test.html", "[hreflang|=\"US\"]", expect() },
            // negated
            { "/attribute-value-selector-test.html", ":not([hreflang|=\"en\"])", expectAllBut(12, 14) },
            { "/attribute-value-selector-test.html", ":not([hreflang|=\"US\"])", expectAllBut() },
            // prefix
            { "/attribute-value-selector-test.html", "[type^=\"image/\"]", expect(16) },
            { "/attribute-value-selector-test.html", "[type^=\"image/png\"]", expect(16) },
            // negated
            { "/attribute-value-selector-test.html", ":not([type^=\"image/\"])", expectAllBut(16) },
            { "/attribute-value-selector-test.html", ":not([type^=\"image/png\"])", expectAllBut(16) },
            // suffix
            { "/attribute-value-selector-test.html", "[href$=\".pdf\"]", expect(18) },
            { "/attribute-value-selector-test.html", "[href$=\"userguide.pdf\"]", expect(18) },
            // negated
            { "/attribute-value-selector-test.html", ":not([href$=\".pdf\"])", expectAllBut(18) },
            { "/attribute-value-selector-test.html", ":not([href$=\"userguide.pdf\"])", expectAllBut(18) },
            // substring
            { "/attribute-value-selector-test.html", "[href*=\"example\"]", expect(20) },
            { "/attribute-value-selector-test.html", "[href*=\"http://example.com\"]", expect(20) },
            // negated
            { "/attribute-value-selector-test.html", ":not([href*=\"example\"])", expectAllBut(20) },
            { "/attribute-value-selector-test.html", ":not([href*=\"http://example.com\"])", expectAllBut(20) },
        });
    }

    public AttributeSelectorTest(String resourceName, String expression, Expected expected) {
        super(resourceName, null, expression, expected);
    }
}
