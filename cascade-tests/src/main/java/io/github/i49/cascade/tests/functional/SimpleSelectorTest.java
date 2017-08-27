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

import java.util.Arrays;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.github.i49.cascade.tests.BasicSelectorTest;
import io.github.i49.cascade.tests.Expectation;

/**
 * Tests for universal selector, type selector, identifier selector and class selector.
 */
@RunWith(Parameterized.class)
public class SimpleSelectorTest extends BasicSelectorTest {

    @Parameters(name = "{index}: {1}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // universal
            { "#universal-selector-test", "*", contains(0) },
            { "#universal-selector-test-2", "*", contains(0, 1, 2, 3, 4, 5) },
            // negated
            { "#universal-selector-test", ":not(*)", contains() },
            { "#universal-selector-test-2", ":not(*)", contains() },

            // type
            { "#type-selector-test", "section", contains(0) },
            { "#type-selector-test", "h1", contains(1) },
            { "#type-selector-test", "p", contains(2, 3) },
            { "#type-selector-test", "nonexistent", contains() },
            // negated
            { "#type-selector-test", ":not(section)", contains(1, 2, 3) },
            { "#type-selector-test", ":not(h1)", contains(0, 2, 3) },
            { "#type-selector-test", ":not(p)", contains(0, 1) },
            { "#type-selector-test", ":not(nonexistent)", contains(0, 1, 2, 3) },

            // id
            { "#id-selector-test", "#content", contains(2) },
            { "#id-selector-test", "div#content", contains(2) },
            { "#id-selector-test", "section#content", contains() },
            { "#id-selector-test", "#nonexistent", contains() },
            // negated
            { "#id-selector-test", ":not(#content)", contains(0, 1, 3, 4, 5) },
            { "#id-selector-test", ":not(#nonexistent)", contains(0, 1, 2, 3, 4, 5) },

            // class
            { "#class-selector-test", ".hello", contains(2, 4, 6) },
            { "#class-selector-test", ".hello.java", contains(4) },
            { "#class-selector-test", ".java.hello", contains(4) },
            { "#class-selector-test", "div.hello", contains(2, 4, 6) },
            { "#class-selector-test", ".nonexistent", contains() },
            { "#class-selector-test", ".hello\\ python", contains() },
            // negated
            { "#class-selector-test", ":not(.hello)", contains(0, 1, 3, 5, 7) },
            { "#class-selector-test", ":not(.hello):not(.java)", contains(0, 1, 3, 5, 7) },
            { "#class-selector-test", ":not(.java):not(.hello)", contains(0, 1, 3, 5, 7) },
            { "#class-selector-test", ":not(.nonexistent)", contains(0, 1, 2, 3, 4, 5, 6, 7) },

            // mix
            { "#class-and-id-test", "p:not(#other).class:not(.fail).test#id#id", contains(1) },
            { "#class-and-id-test", "div:not(#theid).class:not(.fail).test#theid#theid", contains() },
            { "#class-and-id-test", "div:not(#other).notclass:not(.fail).test#theid#theid", contains() },
            { "#class-and-id-test", "div:not(#other).class:not(.test).test#theid#theid", contains() },
            { "#class-and-id-test", "div:not(#other).class:not(.fail).nottest#theid#theid", contains() },
            { "#class-and-id-test", "div:not(#other).class:not(.fail).nottest#theid#other", contains() },

        });
    }

    public SimpleSelectorTest(String rootId, String expression, Expectation expected) {
        super(rootId, expression, expected);
    }

    @BeforeClass
    public static void setUpOnce() {
        loadDocument("/simple-selector-test.html");
    }
}
