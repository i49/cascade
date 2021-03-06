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

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.api.SelectorCompiler;

@RunWith(Parameterized.class)
public class ParsingTest {

    private static final Logger log = Logger.getLogger(ParsingTest.class.getName());

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            // universal selector
            { "*", "*" },
            { "*|*", "*|*" },
            { "ns|*", "ns|*" },
            { "|*", "|*" },
            // type selector
            { "h1", "h1" },
            { "*|h1", "*|h1" },
            { "ns|h1", "ns|h1" },
            { "|h1", "|h1" },
            // class
            { ".pastoral", "*.pastoral" },
            { "h1.pastoral", "h1.pastoral" },
            { "p.pastoral.marine", "p.pastoral.marine" },
            { ".two\\ words",  "*.two words" },
            { ".one\\.word", "*.one.word" },
            // id
            { "#chapter1", "*#chapter1" },
            { "h1#chapter1", "h1#chapter1" },
            { "*#z98y", "*#z98y" },
            { "#-a-b-c-", "*#-a-b-c-" },
            { "#\u00a9", "*#\u00a9" },
            // attribute
            { "[title]", "*[title]" },
            { "[ns|title]", "*[ns|title]" },
            { "[*|title]", "*[*|title]" },
            { "[|title]", "*[|title]" },
            { "[class=example]", "*[class=\"example\"]" },
            { "[ns|class=example]", "*[ns|class=\"example\"]" },
            { "[*|class=example]", "*[*|class=\"example\"]" },
            { "[|class=example]", "*[|class=\"example\"]" },
            { "[class=\"example\"]", "*[class=\"example\"]" },
            { "[class=\'example\']", "*[class=\"example\"]" },
            { "span[hello=\"Cleveland\"][goodbye=\"Columbus\"]", "span[hello=\"Cleveland\"][goodbye=\"Columbus\"]" },
            { "[rel~=\"copyright\"]", "*[rel~=\"copyright\"]" },
            { "[hreflang|=\"en\"]", "*[hreflang|=\"en\"]" },
            { "[type^=\"image/\"]", "*[type^=\"image/\"]" },
            { "[href$=\".html\"]", "*[href$=\".html\"]" },
            { "[title*=\"hello\"]", "*[title*=\"hello\"]" },
            { "[title=two\\ words]", "*[title=\"two words\"]" },
            { "[title=\"hop \\\"step\\\" jump\"]", "*[title=\"hop \"step\" jump\"]" },
            { "[title=\"two\\\nlines\"]", "*[title=\"two\nlines\"]" },
            // combinator
            { "h1 em", "h1 em" },
            { "div * p", "div * p" },
            { "div p *[href]", "div p *[href]" },
            { "body > p", "body > p" },
            { "div ol>li p", "div ol > li p" },
            { "math + p", "math + p" },
            { "h1.opener + h2", "h1.opener + h2" },
            { "h1 ~ pre", "h1 ~ pre" },
            // group
            { "li, p", "li, p" },
            { "h1, h2, h3", "h1, h2, h3" },
            // leading or trailing spaces
            { " p ", "p" },
            { " figure > img ", "figure > img" },
            { " h1, h2, h3 ", "h1, h2, h3" },
            // escape
            { "\\0000a9", "\u00a9" },
            { "\\0000a912", "\u00a912" },
            { "\\00a9 B", "\u00a9B" },
            { ".\\3A \\`\\(", "*.:`(" },
            { ".\\31 a2b3c", "*.1a2b3c" },
            { "#\\#fake-id", "*##fake-id" },
            { "one\\ two", "one two" },
            { "[rel=\"\\00a9  2017\"]", "*[rel=\"\u00a9 2017\"]" },
            { "[title=\" !#$%&'()*+,-./:;<=>?@[]^_`{|}~\"]", "*[title=\" !#$%&'()*+,-./:;<=>?@[]^_`{|}~\"]" },
            // pseudo-class
            { ":root", "*:root" },
            { ":ROOT", "*:root" },
            { "style:empty", "style:empty" },
            { "style:EMPTY", "style:empty" },
            { "tr:nth-child(2n+1)", "tr:nth-child(2n + 1)" },
            { "tr:nth-child(2n+0)", "tr:nth-child(2n)" },
            { "tr:nth-child(10n-1)", "tr:nth-child(10n - 1)" },
            { "tr:nth-child(10n+9)", "tr:nth-child(10n + 9)" },
            { "tr:nth-child(0n+5)", "tr:nth-child(5)" },
            { "tr:nth-child(5)", "tr:nth-child(5)" },
            { "tr:nth-child(1n+0)", "tr:nth-child(n)" },
            { "tr:nth-child(n+0)", "tr:nth-child(n)" },
            { "tr:nth-child(n)", "tr:nth-child(n)" },
            { "tr:nth-child(2n+0)", "tr:nth-child(2n)" },
            { "tr:nth-child(2n)", "tr:nth-child(2n)" },
            { "tr:nth-child( 3n + 1 )", "tr:nth-child(3n + 1)" },
            { "tr:nth-child( +3n - 2 )", "tr:nth-child(3n - 2)" },
            { "tr:nth-child( -n+ 6)", "tr:nth-child(-n + 6)" },
            { "tr:nth-child( +6 )", "tr:nth-child(6)" },
            { "tr:nth-child(-n+6)", "tr:nth-child(-n + 6)" },
            { "tr:nth-child(n-1)", "tr:nth-child(n - 1)" },

            { "tr:NTH-CHILD(2n+1)", "tr:nth-child(2n + 1)" },
            { "tr:nth-child(2N+1)", "tr:nth-child(2n + 1)" },

            { "tr:nth-child(odd)", "tr:nth-child(odd)" },
            { "tr:nth-child(ODD)", "tr:nth-child(odd)" },
            { "tr:nth-child(even)", "tr:nth-child(even)" },
            { "tr:nth-child(EVEN)", "tr:nth-child(even)" },

            { "tr:nth-last-child(-n+2)", "tr:nth-last-child(-n + 2)" },
            { "tr:nth-last-child(odd)", "tr:nth-last-child(odd)" },
            { "tr:nth-last-child(even)", "tr:nth-last-child(even)" },
            { "img:nth-of-type(2n+1)", "img:nth-of-type(2n + 1)" },
            { "img:nth-of-type(2n)", "img:nth-of-type(2n)" },
            { "body > h2:nth-of-type(n+2):nth-last-of-type(n+2)", "body > h2:nth-of-type(n + 2):nth-last-of-type(n + 2)" },
            { "tr:first-child", "tr:first-child" },
            { "tr:last-child", "tr:last-child" },
            { "dl dt:first-of-type", "dl dt:first-of-type" },
            { "tr > td:last-of-type", "tr > td:last-of-type" },
            { "tr:only-child", "tr:only-child" },
            // negation
            { ":not([disabled])", "*:not([disabled])" },
            { "button:not([disabled])", "button:not([disabled])" },
            { ":not(p)", "*:not(p)" },
            { ":not(*)", "*:not(*)" },
            { "tr:not(:nth-child(3n+2))", "tr:not(:nth-child(3n + 2))" },
            
            // comment
            { "/*comment*/h1", "h1" },
            { "h1/*comment*/", "h1" },
            { "p/*comment*/.abstract", "p.abstract" },
            { "figure /*comment*/img", "figure img" },
            { "ol,/*comment*/ul", "ol, ul" },
        });
    }

    private final String expression;
    private final String result;

    public ParsingTest(String expression, String result) {
        this.expression = expression;
        this.result = result;
    }

    @Test
    public void shouldCompileSuccessfully() {
        // given
        SelectorCompiler compiler = SelectorCompiler.create();
        declareNamespaces(compiler);
        
        // when
        Selector s = compiler.compile(this.expression);
        
        // then
        assertThat(s).hasToString(this.result);
        log.fine(this.expression + " -> " + this.result);
    }

    private SelectorCompiler declareNamespaces(SelectorCompiler compiler) {
        return compiler.withNamespace("ns", "http://www.example.com");
    }
}
