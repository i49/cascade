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

package io.github.i49.cascade.tests.functional;

import static io.github.i49.cascade.tests.Fixture.*;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.i49.cascade.tests.BasicSelectorTest;
import io.github.i49.cascade.tests.Documents;
import io.github.i49.cascade.tests.Fixture;

/**
 * Tests for attribute selectors.
 */
@RunWith(Parameterized.class)
public class AttributeSelectorTest extends BasicSelectorTest {

    @Parameters(name = "{index}: {1}")
    public static Iterable<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // presence
            { "#presence-test", "[title]", contains(1) },
            // presence negation
            { "#presence-test", ":not([title])", contains(0) },

            // value
            { "#extact-value-test", "[href=\"http://www.w3.org/\"]", contains(3) },
            // value negation
            { "#extact-value-test", ":not([href=\"http://www.w3.org/\"])", doesNotContain(3) },

            // space-separated
            { "#space-separated-value-test", "[rel~=\"copyright\"]", contains(3) },
            { "#space-separated-value-test", "[rel~=\"copyright copyleft\"]", contains() },
            { "#space-separated-value-test", "[rel~=\"\"]", contains() },
            // space-separated negation
            { "#space-separated-value-test", ":not([rel~=\"copyright\"])", doesNotContain(3) },
            { "#space-separated-value-test", ":not([rel~=\"copyright copyleft\"])", doesNotContain() },

            // dash-separated
            { "#dash-separated-value-test", "[hreflang|=\"en\"]", contains(3, 5) },
            { "#dash-separated-value-test", "[hreflang|=\"US\"]", contains() },
            // dash-separated negation
            { "#dash-separated-value-test", ":not([hreflang|=\"en\"])", doesNotContain(3, 5) },
            { "#dash-separated-value-test", ":not([hreflang|=\"US\"])", doesNotContain() },

            // prefix
            { "#prefix-test", "[type^=\"image/\"]", contains(3) },
            { "#prefix-test", "[type^=\"image/png\"]", contains(3) },
            { "#prefix-test", "[type^=\"\"]", contains() },
            // prefix negation
            { "#prefix-test", ":not([type^=\"image/\"])", doesNotContain(3) },
            { "#prefix-test", ":not([type^=\"image/png\"])", doesNotContain(3) },

            // suffix
            { "#suffix-test", "[href$=\".pdf\"]", contains(3) },
            { "#suffix-test", "[href$=\"userguide.pdf\"]", contains(3) },
            { "#suffix-test", "[href$=\"\"]", contains() },
            // suffix negation
            { "#suffix-test", ":not([href$=\".pdf\"])", doesNotContain(3) },
            { "#suffix-test", ":not([href$=\"userguide.pdf\"])", doesNotContain(3) },

            // substring
            { "#substring-test", "[href*=\"example\"]", contains(3) },
            { "#substring-test", "[href*=\"http://example.com\"]", contains(3) },
            { "#substring-test", "[href*=\"\"]", contains() },
            // substring negation
            { "#substring-test", ":not([href*=\"example\"])", doesNotContain(3) },
            { "#substring-test", ":not([href*=\"http://example.com\"])", doesNotContain(3) },
       });
    }

    private static Document doc;
    private final Fixture fixture;
    
    public AttributeSelectorTest(String startId, String expression, Function<Element, ElementMatcher> mapper) {
        this.fixture = new Fixture(doc, startId, expression, mapper);
    }

    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/attribute-selector-test.html");
    }
    
    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }
    
    public Fixture getFixture() {
        return fixture;
    }
}
