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

    @Parameters(name = "{index}: {1}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // presence
            { "/attribute-presence-selector-test.html", "[title]", contains(5) },
            // presence negation
            { "/attribute-presence-selector-test.html", ":not([title])", doesNotContain(5) },
            // value
            { "/attribute-value-selector-test.html", "[href=\"http://www.w3.org/\"]", contains(8) },
            // value negation
            { "/attribute-value-selector-test.html", ":not([href=\"http://www.w3.org/\"])", doesNotContain(8) },
            // space-separated
            { "/attribute-value-selector-test.html", "[rel~=\"copyright\"]", contains(10) },
            { "/attribute-value-selector-test.html", "[rel~=\"copyright copyleft\"]", contains() },
            { "/attribute-value-selector-test.html", "[rel~=\"\"]", contains() },
            // space-separated negation
            { "/attribute-value-selector-test.html", ":not([rel~=\"copyright\"])", doesNotContain(10) },
            { "/attribute-value-selector-test.html", ":not([rel~=\"copyright copyleft\"])", doesNotContain() },
            // dash-separated
            { "/attribute-value-selector-test.html", "[hreflang|=\"en\"]", contains(14, 16) },
            { "/attribute-value-selector-test.html", "[hreflang|=\"US\"]", contains() },
            // dash-separated negation
            { "/attribute-value-selector-test.html", ":not([hreflang|=\"en\"])", doesNotContain(14, 16) },
            { "/attribute-value-selector-test.html", ":not([hreflang|=\"US\"])", doesNotContain() },
            // prefix
            { "/attribute-value-selector-test.html", "[type^=\"image/\"]", contains(18) },
            { "/attribute-value-selector-test.html", "[type^=\"image/png\"]", contains(18) },
            { "/attribute-value-selector-test.html", "[type^=\"\"]", contains() },
            // prefix negation
            { "/attribute-value-selector-test.html", ":not([type^=\"image/\"])", doesNotContain(18) },
            { "/attribute-value-selector-test.html", ":not([type^=\"image/png\"])", doesNotContain(18) },
            // suffix
            { "/attribute-value-selector-test.html", "[href$=\".pdf\"]", contains(22) },
            { "/attribute-value-selector-test.html", "[href$=\"userguide.pdf\"]", contains(22) },
            { "/attribute-value-selector-test.html", "[href$=\"\"]", contains() },
            // suffix negation
            { "/attribute-value-selector-test.html", ":not([href$=\".pdf\"])", doesNotContain(22) },
            { "/attribute-value-selector-test.html", ":not([href$=\"userguide.pdf\"])", doesNotContain(22) },
            // substring
            { "/attribute-value-selector-test.html", "[href*=\"example\"]", contains(26) },
            { "/attribute-value-selector-test.html", "[href*=\"http://example.com\"]", contains(26) },
            { "/attribute-value-selector-test.html", "[href*=\"\"]", contains() },
            // substring negation
            { "/attribute-value-selector-test.html", ":not([href*=\"example\"])", doesNotContain(26) },
            { "/attribute-value-selector-test.html", ":not([href*=\"http://example.com\"])", doesNotContain(26) },
        });
    }

    public AttributeSelectorTest(String resourceName, String expression, Expected expected) {
        super(resourceName, null, expression, expected);
    }
}
