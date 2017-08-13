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
 * Tests for selectors with combinators.
 */
@RunWith(Parameterized.class)
public class CombinatorTest extends BaseSelectorTest {

    @Parameters(name = "{index}: {1}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { "/descendant-combinator-test.html", "div.main p", expect(7, 12, 14) },
            { "/child-combinator-test.html", "div > p", expect(7, 14) },
            { "/adjacent-combinator-test-1.html", "h1.opener + h2", expect(6) },
            { "/adjacent-combinator-test-2.html", "article > p + p", expect(7, 8) },
            { "/sibling-combinator-test-1.html", "h2 ~ pre", expect(8) },
            { "/sibling-combinator-test-2.html", "article > p ~ p", expect(8, 9, 11) }
        });
    }

    public CombinatorTest(String resourceName, String expression, Expected expected) {
        super(resourceName, null, expression, expected);
    }
}
