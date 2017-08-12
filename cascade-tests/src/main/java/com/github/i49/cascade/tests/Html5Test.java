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
 * Test with HTML5 file.
 */
@RunWith(Parameterized.class)
public class Html5Test extends BaseSelectorTest {

    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // simple selectors
            { "/html5-test.html", "#forms__action", expect(435) },
            { "/html5-test.html", ":not(#forms__action)", expectAllBut(435) },
            { "/html5-test.html", "article", expect(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "/html5-test.html", ":not(article)", expectAllBut(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },

            // combinator
            { "/html5-test.html", "* article", expect(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "/html5-test.html", "* * article", expect(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "/html5-test.html", "* ~ article", expect(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "/html5-test.html", "* * ~ article", expect(71, 84, 92, 104, 125, 133, 180, 198, 247, 269, 277, 285, 293, 301, 309, 318) },
            { "/html5-test.html", "* ~ * ~ article", expect(84, 92, 104, 125, 133, 180, 198, 269, 277, 285, 293, 301, 309, 318) },

            // pseudo class
            { "/html5-test.html", ":root", expect(0) },
            { "/html5-test.html", ":not(:root)", expectAllBut(0) },
            { "/html5-test.html", "meta:empty", expect(2, 3) },
        });
    }

    public Html5Test(String resourceName, String expression, Expected expected) {
        super(resourceName, null, expression, expected);
    }
}
