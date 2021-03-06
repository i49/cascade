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
import java.util.List;
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
 * Tests for selectors with combinators.
 */
@RunWith(Parameterized.class)
public class CombinatorTest extends BasicSelectorTest {

    @Parameters(name = "{index}: {1}")
    public static Iterable<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // descendant combinator
            { "#descendant-combinator-test", "div.main p", contains(3, 8, 10) },

            // child combinator
            { "#child-combinator-test", "div > p", contains(3, 10) },

            // adjacent combinator
            { "#adjacent-combinator-test-1", "h1.opener + h2", contains(2) },
            { "#adjacent-combinator-test-2", "article > p + p", contains(3, 4) },

            // general sibling combinator
            { "#sibling-combinator-test-1", "h2 ~ pre", contains(4) },
            { "#sibling-combinator-test-2", "article > p ~ p", contains(4, 5, 7) },

            // element order
            { "#element-order-test-1", "div ~ p", contains(5, 6) },
            { "#element-order-test-2", "div > p", contains(4, 5) },

            // multiple combinators
            { "#descendant-and-child-test", "blockquote div > p", contains(4) },
            { "#descendant-and-child-test", "blockquote > div p", contains(4) },
            { "#sibling-and-adjacent-test", "blockquote ~ div + p", contains(5) },
            { "#sibling-and-adjacent-test", "blockquote + div ~ p", contains(5) },
            { "#adjacent-and-descendant-test", "blockquote + div p", contains(5) },
        });
    }

    private static Document doc;
    private final Fixture fixture;
    
    public CombinatorTest(String startId, String expression, Function<Element, List<Element>> teacher) {
        this.fixture = new Fixture(doc, startId, expression, teacher);
    }
    
    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/combinator-test.html");
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
