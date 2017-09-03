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
import io.github.i49.cascade.tests.Fixture.ElementMatcher;

/**
 * Test with HTML5 file.
 */
@RunWith(Parameterized.class)
public class Html5Test extends BasicSelectorTest {

    @Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // simple selectors
            { "#forms__action", contains(435) },
            { ":not(#forms__action)", doesNotContain(435) },
            { "article", contains(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { ":not(article)", doesNotContain(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },

            // combinator
            { "* article", contains(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "* * article", contains(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "* ~ article", contains(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "* * ~ article", contains(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "* ~ * ~ article", contains(84, 92, 104, 125, 133, 180, 198, 269, 277, 285, 293, 301, 309, 318) },

            // pseudo class
            { ":root", contains(0) },
            { ":not(:root)", doesNotContain(0) },
            { "meta:empty", contains(2, 3) },

            { "figcaption", contains(265) },
            { "[src]", contains(254, 259, 264, 322) },
            { "[type=password]", contains(337) },
            { "input[type|=datetime]", contains(429, 432) },
            { "[width$=px]", contains(313) },
            { "[href]:not([href^=\"#\"])", contains(451, 452) },
            { "main ~ footer a:nth-child(2)", contains(452) },
        });
    }

    private static Document doc;
    private final Fixture fixture;
   
    public Html5Test(String expression, Function<Element, ElementMatcher> mapper) {
        this.fixture = new Fixture(doc, expression, mapper);
    }

    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/html5-test.html");
    }
    
    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }

    @Override
    public Fixture getFixture() {
        return fixture;
    }
}
