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
 * Tests for groups of selectors.
 */
@RunWith(Parameterized.class)
public class SelectorGroupTest extends BaseSelectorTest {

    @Parameters(name = "{index}: {1}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { "/selector-group-test.html", "li, p", expect(5, 7, 8, 9) },
            { "/selector-group-test.html", "li, nonexistent", expect(7, 8) },
            { "/selector-group-test.html", "nonexistent, p", expect(5, 9) },
            { "/selector-group-test.html", "nonexistent1, nonexistent2", expect() },
            { "/selector-group-test.html", "li, .example", expect(5, 7, 8, 9) },
        });
    }

    public SelectorGroupTest(String resourceName, String expression, Expected expected) {
        super(resourceName, null, expression, expected);
   }
}
