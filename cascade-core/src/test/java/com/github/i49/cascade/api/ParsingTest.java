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

package com.github.i49.cascade.api;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParsingTest {
    
    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            /* universal */
            { "*", "*" },
            /* type */
            { "h1", "h1" },
            /* class */
            { ".pastoral", "*.pastoral" },
            { "h1.pastoral", "h1.pastoral" },
            { "p.pastoral.marine", "p.pastoral.marine" },
            /* id */
            { "#chapter1", "*#chapter1" },
            { "h1#chapter1", "h1#chapter1" },
            { "*#z98y", "*#z98y" },
            /* attribute */
            { "[title]", "*[title]" },
            { "[class=example]", "*[class=\"example\"]" },
            { "[class=\"example\"]", "*[class=\"example\"]" },
            { "[class=\'example\']", "*[class=\"example\"]" },
            { "span[hello=\"Cleveland\"][goodbye=\"Columbus\"]", "span[hello=\"Cleveland\"][goodbye=\"Columbus\"]" },
            { "[rel~=\"copyright\"]", "*[rel~=\"copyright\"]" },
            { "[hreflang|=\"en\"]", "*[hreflang|=\"en\"]" },
            { "[type^=\"image/\"]", "*[type^=\"image/\"]" },
            { "[href$=\".html\"]", "*[href$=\".html\"]" },
            { "[title*=\"hello\"]", "*[title*=\"hello\"]" },
            /* combinator */
            { "h1 em", "h1 em" },
            { "div * p", "div * p" },
            { "div p *[href]", "div p *[href]" },
            { "body > p", "body > p" },
            { "div ol>li p", "div ol > li p" },
            { "math + p", "math + p" },
            { "h1.opener + h2", "h1.opener + h2" },
            { "h1 ~ pre", "h1 ~ pre" },
            /* group */
            { "li, p", "li, p" },
            { "h1, h2, h3", "h1, h2, h3" },
            /* leading or trailing spaces */
            { " p ", "p" },
            { " figure > img ", "figure > img" },
            { " h1, h2, h3 ", "h1, h2, h3" }
        });
    }

    private final String expression;
    private final String result;

    public ParsingTest(String expression, String result) {
        this.expression = expression;
        this.result = result;
    }
    
    @Test
    public void compile_shouldParseExpression() {
        SelectorCompiler compiler = SelectorCompiler.create();
        Selector s = compiler.compile(this.expression);
        assertThat(s).hasToString(this.result);
    }
}
