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
            { "/attribute-presence-selector-test.html", "[title]", 1 },
            /* value */
            { "/attribute-value-selector-test.html", "[href=\"http://www.w3.org/\"]", 1 },
            { "/attribute-value-selector-test.html", "[rel~=\"copyright\"]", 1 },
            { "/attribute-value-selector-test.html", "[rel~=\"copyright copyleft\"]", 0 },
            /* dash */
            { "/attribute-value-selector-test.html", "[hreflang|=\"en\"]", 2 },
            { "/attribute-value-selector-test.html", "[hreflang|=\"US\"]", 0 },
            /* prefix */
            { "/attribute-value-selector-test.html", "[type^=\"image/\"]", 1 },
            { "/attribute-value-selector-test.html", "[type^=\"image/png\"]", 1 },
            /* suffix */
            { "/attribute-value-selector-test.html", "[href$=\".pdf\"]", 1 },
            { "/attribute-value-selector-test.html", "[href$=\"userguide.pdf\"]", 1 },
            /* substring */
            { "/attribute-value-selector-test.html", "[href*=\"example\"]", 1 },
            { "/attribute-value-selector-test.html", "[href*=\"http://example.com\"]", 1 },
        });
    }
   
    public AttributeSelectorTest(String resourceName, String expression, int expectedCount) {
        super(resourceName, expression, expectedCount);
    }
}
