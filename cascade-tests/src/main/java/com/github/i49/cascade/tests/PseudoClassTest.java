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

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for pseudo-class selectors.
 */
@RunWith(Parameterized.class)
public class PseudoClassTest extends BaseSelectorTest {

    @Parameters(name = "{index}: {1}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // :root
            { null, ":root", contains(0) },
            { "#root-test", ":root", contains() },
            // :root negation
            { null, ":not(:root)", doesNotContain(0) },
            { "#root-test", ":not(:root)", contains(0, 1) },

            // :empty
            { "#empty-test", "p:empty", contains(2) },
            // :empty negation
            { "#empty-test", "p:not(:empty)", contains(3, 4, 6) },

            // :nth-child
            { "#nth-child-test", "li:nth-child(2n)", contains(4, 6, 8, 11, 13, 15) },
            { "#nth-child-test", "li:nth-child(2n+1)", contains(3, 5, 7, 10, 12, 14) },
            { "#nth-child-test", "li:nth-child(2n+2)", contains(4, 6, 8, 11, 13, 15) },
            { "#nth-child-test", "li:nth-child(3n)", contains(5, 8, 12, 15) },
            { "#nth-child-test", "li:nth-child(3n+1)", contains(3, 6, 10, 13) },
            { "#nth-child-test", "li:nth-child(3n+2)", contains(4, 7, 11, 14) },
            { "#nth-child-test", "li:nth-child(3n+3)", contains(5, 8, 12, 15) },
            { "#nth-child-test", "li:nth-child(3n-1)", contains(4, 7, 11, 14) },
            { "#nth-child-test", "li:nth-child(3n-2)", contains(3, 6, 10, 13) },
            { "#nth-child-test", "li:nth-child(-n+4)", contains(3, 4, 5, 6, 10, 11, 12, 13) },
            { "#nth-child-test", "li:nth-child(1)", contains(3, 10) },
            { "#nth-child-test", "li:nth-child(6)", contains(8, 15) },
            { "#nth-child-test", "li:nth-child(0)", contains() },
            { "#nth-child-test", "li:nth-child(even)", contains(4, 6, 8, 11, 13, 15) },
            { "#nth-child-test", "li:nth-child(odd)", contains(3, 5, 7, 10, 12, 14) },
            // :nth-child negation
            { "#nth-child-test", "li:not(:nth-child(2n))", contains(3, 5, 7, 10, 12, 14) },
            { "#nth-child-test", "li:not(:nth-child(2n+1))", contains(4, 6, 8, 11, 13, 15) },
            { "#nth-child-test", "li:not(:nth-child(2n+2))", contains(3, 5, 7, 10, 12, 14) },
            { "#nth-child-test", "li:not(:nth-child(1))", contains(4, 5, 6, 7, 8, 11, 12, 13, 14, 15) },
            { "#nth-child-test", "li:not(:nth-child(6))", contains(3, 4, 5, 6, 7, 10, 11, 12, 13, 14) },
            { "#nth-child-test", "li:not(:nth-child(0))", contains(3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15) },
            { "#nth-child-test", "li:not(:nth-child(even))", contains(3, 5, 7, 10, 12, 14) },
            { "#nth-child-test", "li:not(:nth-child(odd))", contains(4, 6, 8, 11, 13, 15) },

            // :nth-last-child
            { "#nth-child-test", "li:nth-last-child(2n)", contains(3, 5, 7, 10, 12, 14) },
            { "#nth-child-test", "li:nth-last-child(2n+1)", contains(4, 6, 8, 11, 13, 15) },
            { "#nth-child-test", "li:nth-last-child(2n+2)", contains(3, 5, 7, 10, 12, 14) },
            { "#nth-child-test", "li:nth-last-child(3n)", contains(3, 6, 10, 13) },
            { "#nth-child-test", "li:nth-last-child(3n+1)", contains(5, 8, 12, 15) },
            { "#nth-child-test", "li:nth-last-child(3n+2)", contains(4, 7, 11, 14) },
            { "#nth-child-test", "li:nth-last-child(3n+3)", contains(3, 6, 10, 13) },
            { "#nth-child-test", "li:nth-last-child(3n-1)", contains(4, 7, 11, 14) },
            { "#nth-child-test", "li:nth-last-child(3n-2)", contains(5, 8, 12, 15) },
            { "#nth-child-test", "li:nth-last-child(-n+4)", contains(5, 6, 7, 8, 12, 13, 14, 15) },
            { "#nth-child-test", "li:nth-last-child(1)", contains(8, 15) },
            { "#nth-child-test", "li:nth-last-child(6)", contains(3, 10) },
            { "#nth-child-test", "li:nth-last-child(0)", contains() },
            { "#nth-child-test", "li:nth-last-child(even)", contains(3, 5, 7, 10, 12, 14) },
            { "#nth-child-test", "li:nth-last-child(odd)", contains(4, 6, 8, 11, 13, 15) },
            // :nth-last-child negation
            { "#nth-child-test", "li:not(:nth-last-child(2n))", contains(4, 6, 8, 11, 13, 15) },
            { "#nth-child-test", "li:not(:nth-last-child(2n+1))",  contains(3, 5, 7, 10, 12, 14) },
            { "#nth-child-test", "li:not(:nth-last-child(2n+2))", contains(4, 6, 8, 11, 13, 15) },
            { "#nth-child-test", "li:not(:nth-last-child(1))", contains(3, 4, 5, 6, 7, 10, 11, 12, 13, 14) },
            { "#nth-child-test", "li:not(:nth-last-child(6))", contains(4, 5, 6, 7, 8, 11, 12, 13, 14, 15) },
            { "#nth-child-test", "li:not(:nth-last-child(0))", contains(3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15) },
            { "#nth-child-test", "li:not(:nth-last-child(even))", contains(4, 6, 8, 11, 13, 15) },
            { "#nth-child-test", "li:not(:nth-last-child(odd))", contains(3, 5, 7, 10, 12, 14) },

            // :nth-of-type
            { "#nth-of-type-test", "p:nth-of-type(3)", contains(5) },
            { "#nth-of-type-test", "dl > :nth-of-type(3n+1)", contains(7, 8, 13, 14) },
            // :nth-of-type negation
            { "#nth-of-type-test", "p:not(:nth-of-type(3))", contains(2, 4) },
            { "#nth-of-type-test", "dl > :not(:nth-of-type(3n+1))", contains(9, 10, 11, 12, 15, 16, 17, 18) },

            // :nth-last-of-type
            { "#nth-of-type-test", "p:nth-last-of-type(3)", contains(2) },
            { "#nth-of-type-test", "dl > :nth-last-of-type(3n+1)", contains(11, 12, 17, 18) },
            // :nth-last-of-type negation
            { "#nth-of-type-test", "p:not(:nth-last-of-type(3))", contains(4, 5) },
            { "#nth-of-type-test", "dl > :not(:nth-last-of-type(3n+1))", contains(7, 8, 9, 10, 13, 14, 15, 16) },

            // :first-child
            { "#first-child-test", "div > p:first-child", contains(4) },
            { "#nth-child-test", "li:first-child", contains(3, 10) },
            // :first-child negation
            { "#first-child-test", "div > p:not(:first-child)", contains(8) },
            { "#nth-child-test", "li:not(:first-child)", contains(4, 5, 6, 7, 8, 11, 12, 13, 14, 15) },

            // :last-child
            { "#nth-child-test", "li:last-child", contains(8, 15) },
            // :last-child negation
            { "#nth-child-test", "li:not(:last-child)", contains(3, 4, 5, 6, 7, 10, 11, 12, 13, 14) },

            // :first-of-type
            { "#first-of-type-test", "dl dt:first-of-type", contains(3, 6) },
            // :first-of-type negation
            { "#first-of-type-test", "dl dt:not(:first-of-type)", contains(8) },

            // :last-of-type
            { "#last-of-type-test", "address:last-of-type", contains(5) },
            // :last-of-type negation
            { "#last-of-type-test", "address:not(:last-of-type)", contains(3, 4) },

            // :only-child
            { "#only-child-test", "p:only-child", contains(4) },
            // :only-child negation
            { "#only-child-test", "p:not(:only-child)", contains(2) },

            // :only-of-type
            { "#only-of-type-test", ".example :only-of-type", contains(4) },
            // :only-of-type negation
            { "#only-of-type-test", ".example :not(:only-of-type)", contains(3, 5) },
        });
    }

    public PseudoClassTest(String rootId, String expression, Expected expected) {
        super(rootId, expression, expected);
    }

    @BeforeClass
    public static void setUpOnce() {
        loadDocument("/pseudo-class-test.html");
    }
}
