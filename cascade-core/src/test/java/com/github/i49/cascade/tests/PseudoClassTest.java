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
 * Tests for pseudo-class selectors.
 */
@RunWith(Parameterized.class)
public class PseudoClassTest extends BaseSelectorTest {

    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // root
            { "/empty-pseudo-class-test.html", 0, ":root", 1 },
            { "/empty-pseudo-class-test.html", 4, ":root", 0 },
            // empty
            { "/empty-pseudo-class-test.html", 0, "p:empty", 1 },
        });
    }

    public PseudoClassTest(String resourceName, int startIndex, String expression, int expectedCount) {
        super(resourceName, startIndex, expression, expectedCount);
    }
}
