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
            { "/root-test.html", null, ":root", contains(0) },
            { "/root-test.html", "body", ":root", contains() },
            // :root negation
            { "/root-test.html", null, ":not(:root)", doesNotContain(0) },
            { "/root-test.html", "body", ":not(:root)", contains(4, 5) },
            // :empty
            { "/empty-test.html", null, "p:empty", contains(6) },
            // :empty negation
            { "/empty-test.html", null, "p:not(:empty)", contains(7, 8, 10) },
            // :nth-child
            { "/nth-child-test.html", null, "li:nth-child(2n)", contains(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:nth-child(2n+1)", contains(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:nth-child(2n+2)", contains(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:nth-child(3n)", contains(9, 12, 16, 19) },
            { "/nth-child-test.html", null, "li:nth-child(3n+1)", contains(7, 10, 14, 17) },
            { "/nth-child-test.html", null, "li:nth-child(3n+2)", contains(8, 11, 15, 18) },
            { "/nth-child-test.html", null, "li:nth-child(3n+3)", contains(9, 12, 16, 19) },
            { "/nth-child-test.html", null, "li:nth-child(3n-1)", contains(8, 11, 15, 18) },
            { "/nth-child-test.html", null, "li:nth-child(3n-2)", contains(7, 10, 14, 17) },
            { "/nth-child-test.html", null, "li:nth-child(-n+4)", contains(7, 8, 9, 10, 14, 15, 16, 17) },
            { "/nth-child-test.html", null, "li:nth-child(1)", contains(7, 14) },
            { "/nth-child-test.html", null, "li:nth-child(6)", contains(12, 19) },
            { "/nth-child-test.html", null, "li:nth-child(0)", contains() },
            { "/nth-child-test.html", null, "li:nth-child(even)", contains(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:nth-child(odd)", contains(7, 9, 11, 14, 16, 18) },
            // :nth-child negation
            { "/nth-child-test.html", null, "li:not(:nth-child(2n))", contains(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:not(:nth-child(2n+1))", contains(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:not(:nth-child(2n+2))", contains(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:not(:nth-child(1))", contains(8, 9, 10, 11, 12, 15, 16, 17, 18, 19) },
            { "/nth-child-test.html", null, "li:not(:nth-child(6))", contains(7, 8, 9, 10, 11, 14, 15, 16, 17, 18) },
            { "/nth-child-test.html", null, "li:not(:nth-child(0))", contains(7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19) },
            { "/nth-child-test.html", null, "li:not(:nth-child(even))", contains(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:not(:nth-child(odd))", contains(8, 10, 12, 15, 17, 19) },
            // :nth-last-child
            { "/nth-child-test.html", null, "li:nth-last-child(2n)", contains(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(2n+1)", contains(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(2n+2)", contains(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n)", contains(7, 10, 14, 17) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n+1)", contains(9, 12, 16, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n+2)", contains(8, 11, 15, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n+3)", contains(7, 10, 14, 17) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n-1)", contains(8, 11, 15, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(3n-2)", contains(9, 12, 16, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(-n+4)", contains(9, 10, 11, 12, 16, 17, 18, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(1)", contains(12, 19) },
            { "/nth-child-test.html", null, "li:nth-last-child(6)", contains(7, 14) },
            { "/nth-child-test.html", null, "li:nth-last-child(0)", contains() },
            { "/nth-child-test.html", null, "li:nth-last-child(even)", contains(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:nth-last-child(odd)", contains(8, 10, 12, 15, 17, 19) },
            // :nth-last-child negation
            { "/nth-child-test.html", null, "li:not(:nth-last-child(2n))", contains(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:not(:nth-last-child(2n+1))",  contains(7, 9, 11, 14, 16, 18) },
            { "/nth-child-test.html", null, "li:not(:nth-last-child(2n+2))", contains(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:not(:nth-last-child(1))", contains(7, 8, 9, 10, 11, 14, 15, 16, 17, 18) },
            { "/nth-child-test.html", null, "li:not(:nth-last-child(6))", contains(8, 9, 10, 11, 12, 15, 16, 17, 18, 19) },
            { "/nth-child-test.html", null, "li:not(:nth-last-child(0))", contains(7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19) },
            { "/nth-child-test.html", null, "li:not(:nth-last-child(even))", contains(8, 10, 12, 15, 17, 19) },
            { "/nth-child-test.html", null, "li:not(:nth-last-child(odd))", contains(7, 9, 11, 14, 16, 18) },
            // :nth-of-type
            { "/nth-of-type-test.html", null, "p:nth-of-type(3)", contains(9) },
            { "/nth-of-type-test.html", null, "dl > :nth-of-type(3n+1)", contains(11, 12, 17, 18) },
            // :nth-of-type negation
            { "/nth-of-type-test.html", null, "p:not(:nth-of-type(3))", contains(6, 8) },
            { "/nth-of-type-test.html", null, "dl > :not(:nth-of-type(3n+1))", contains(13, 14, 15, 16, 19, 20, 21, 22) },
            // :nth-last-of-type
            { "/nth-of-type-test.html", null, "p:nth-last-of-type(3)", contains(6) },
            { "/nth-of-type-test.html", null, "dl > :nth-last-of-type(3n+1)", contains(15, 16, 21, 22) },
            // :nth-last-of-type negation
            { "/nth-of-type-test.html", null, "p:not(:nth-last-of-type(3))", contains(8, 9) },
            { "/nth-of-type-test.html", null, "dl > :not(:nth-last-of-type(3n+1))", contains(11, 12, 13, 14, 17, 18, 19, 20) },
            // :first-child
            { "/first-child-test.html", null, "div > p:first-child", contains(8) },
            { "/nth-child-test.html", null, "li:first-child", contains(7, 14) },
            // :first-child negation
            { "/first-child-test.html", null, "div > p:not(:first-child)", contains(12) },
            { "/nth-child-test.html", null, "li:not(:first-child)", contains(8, 9, 10, 11, 12, 15, 16, 17, 18, 19) },
            // :last-child
            { "/nth-child-test.html", null, "li:last-child", contains(12, 19) },
            // :last-child negation
            { "/nth-child-test.html", null, "li:not(:last-child)", contains(7, 8, 9, 10, 11, 14, 15, 16, 17, 18) },
            // :first-of-type
            { "/first-of-type-test.html", null, "dl dt:first-of-type", contains(7, 10) },
            // :first-of-type negation
            { "/first-of-type-test.html", null, "dl dt:not(:first-of-type)", contains(12) },
            // :last-of-type
            { "/last-of-type-test.html", null, "address:last-of-type", contains(9) },
            // :last-of-type negation
            { "/last-of-type-test.html", null, "address:not(:last-of-type)", contains(7, 8) },
            // :only-child
            { "/only-child-test.html", null, "p:only-child", contains(8) },
            // :only-child negation
            { "/only-child-test.html", null, "p:not(:only-child)", contains(6) },
            // :only-of-type
            { "/only-of-type-test.html", null, ".example :only-of-type", contains(8) },
            // :only-of-type negation
            { "/only-of-type-test.html", null, ".example :not(:only-of-type)", contains(7, 9) },
        });
    }

    public PseudoClassTest(String resourceName, String startElement, String expression, Expected expected) {
        super(resourceName, startElement, expression, expected);
    }
}
