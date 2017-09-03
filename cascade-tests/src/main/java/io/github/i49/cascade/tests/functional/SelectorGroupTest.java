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

import io.github.i49.cascade.tests.BasicSelectorTest;
import io.github.i49.cascade.tests.Documents;
import io.github.i49.cascade.tests.Fixture;
import io.github.i49.cascade.tests.Fixture.ElementMatcher;

/**
 * Tests for groups of selectors.
 */
@RunWith(Parameterized.class)
public class SelectorGroupTest extends BasicSelectorTest {

    @Parameters(name = "{index}: {1}")
    public static Iterable<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { "#selector-group-test", "li, p", contains(1, 3, 4, 5) },
            { "#selector-group-test", "li, nonexistent", contains(3, 4) },
            { "#selector-group-test", "nonexistent, p", contains(1, 5) },
            { "#selector-group-test", "nonexistent1, nonexistent2", contains() },
            { "#selector-group-test", "li, .example", contains(1, 3, 4, 5) },
        });
    }

    private static Document doc;
    
    public SelectorGroupTest(String startId, String expression, Function<Fixture, ElementMatcher> matcherFactory) {
        super(new Fixture(doc, startId, expression, matcherFactory));
    }

    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/selector-group-test.html");
    }
    
    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }
}
