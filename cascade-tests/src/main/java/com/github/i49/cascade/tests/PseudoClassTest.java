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
            // :root
            { "/root-test.html", null, ":root", expect(0) },
            { "/root-test.html", "body", ":root", expect() },
            // :empty
            { "/empty-test.html", null, "p:empty", expect(6) },
            // :nth-child
            { "/nth-child-test.html", null, "li:nth-child(2n)", expect(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:nth-child(2n+1)", expect(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:nth-child(2n+2)", expect(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:nth-child(3n)", expect(9, 12, 16, 19) },
            { "/nth-child-test.html", null, "li:nth-child(3n+1)", expect(7, 10, 14, 17) },
            { "/nth-child-test.html", null, "li:nth-child(3n+2)", expect(8, 11, 15, 18) },
            { "/nth-child-test.html", null, "li:nth-child(3n+3)", expect(9, 12, 16, 19) },
            { "/nth-child-test.html", null, "li:nth-child(3n-1)", expect(8, 11, 15, 18) },
            { "/nth-child-test.html", null, "li:nth-child(3n-2)", expect(7, 10, 14, 17) },
            { "/nth-child-test.html", null, "li:nth-child(-n+4)", expect(7, 8, 9, 10, 14, 15, 16, 17) },
            { "/nth-child-test.html", null, "li:nth-child(1)", expect(7, 14) },
            { "/nth-child-test.html", null, "li:nth-child(6)", expect(12, 19) },
            { "/nth-child-test.html", null, "li:nth-child(0)", expect() },
            { "/nth-child-test.html", null, "li:nth-child(even)", expect(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:nth-child(odd)", expect(7, 9, 11, 14, 16, 18) },
            // :nth-last-child
            { "/nth-child-test.html", null, "li:nth-last-child(2n)", expect(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(2n+1)", expect(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(2n+2)", expect(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n)", expect(7, 10, 14, 17) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n+1)", expect(9, 12, 16, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n+2)", expect(8, 11, 15, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n+3)", expect(7, 10, 14, 17) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n-1)", expect(8, 11, 15, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n-2)", expect(9, 12, 16, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(-n+4)", expect(9, 10, 11, 12, 16, 17, 18, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(1)", expect(12, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(6)", expect(7, 14) },
            { "/nth-child-test.html", null, "li:nth-last-child(0)", expect() },
            { "/nth-child-test.html", null, "li:nth-last-child(even)", expect(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(odd)", expect(8, 10, 12, 15, 17, 19) },
            // :first-child
            { "/nth-child-test.html", null, "li:first-child", expect(7, 14) },
            // :last-child
            { "/nth-child-test.html", null, "li:last-child", expect(12, 19) },
            // :only-child
            { "/only-child-test.html", null, "p:only-child", expect(8) },
        });
    }

    public PseudoClassTest(String resourceName, String startElement, String expression, int[] indices) {
        super(resourceName, startElement, expression, indices);
    }
}
