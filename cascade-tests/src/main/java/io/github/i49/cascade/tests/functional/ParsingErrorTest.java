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

import io.github.i49.cascade.api.InvalidSelectorException;
import io.github.i49.cascade.api.SelectorCompiler;

@RunWith(Parameterized.class)
public class ParsingErrorTest {

    private static final Logger log = Logger.getLogger(ParsingErrorTest.class.getName());

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { null, 0 },
            // type
            { "", 0 },
            { "undeclared|p", 0 },
            { "undeclared|*", 0 },
            { "ns|123", 3 },
            // selector group
            { "p, ", 3 },
            // class
            { ".5cm, ", 0 },
            // combinators
            { "> p", 0 },
            { "+ p", 0 },
            { "~ p", 0 },
            { ", p", 0 },
            // attributes
            { "[class=\"example\"]*", 17 },
            { "[class=\"example\"]div", 17 },
            { "[]", 1 },
            { "[=\"content\"]", 1 },
            { "[~=\"content\"]", 1 },
            { "[class=]", 7 },
            { "[class~=]", 8 },
            { "[class=", 7 },
            { "[class=\"", 7 },
            { "[title=\"example\n", 7 },
            { "[*=test]", 1 },
            { "##", 0 },
            { "[title=two words]", 11 },
            { "[undeclared|title]", 1 },
            { "[ns|123]", 4 },
            { "[*title]", 2 },
            // pseudo-class
            { ":", 1 },
            { ":foo", 1 },
            // functional pseudo-class
            { ":nth-child", 1 },
            { ":nth-child()", 11 },
            { ":nth-child(odd", 14 },
            { ":nth-child(foo)", 11 },
            { ":nth-child(10n+-1)", 15 },
            { ":nth-child(3 n)", 13 },
            { ":nth-child(+ 2n)", 12 },
            { ":nth-child(+ 2)", 12 },
            { ":nth-child(1.2n)", 11 },
            { ":nth-child(.1n)", 11 },
            { ":nth-child(2n+1.2)", 14 },
            { ":nth-child(2n-1.2)", 15 },
            { ":nth-child(2n+.1)", 14 },
            // pseudo-element
            { "::first-line", 1 },
            // negation
            { ":not", 1 },
            { ":not()", 5 },
            { ":not(", 5 },
            { ":not(:not(div))", 6 },
            { ":not(h1 em)", 8 },
            { ":not(div > p)", 8 },
            { ":not(h1 + h2)", 7 },
            { ":not(h1 ~ pre)", 7 },
            { ":not(li, p)", 7 },
            { ":not(h1.opener)", 7 },
            { ":not(42)", 5 },
        });
    }

    private final String expression;
    private final int position;

    public ParsingErrorTest(String expression, int position) {
        this.expression = expression;
        this.position = position;
    }

    @Test
    public void compile_shouldThrowExceptionIfSyntaxErrorFound() {
        // given
        SelectorCompiler compiler = SelectorCompiler.create();
        declareNamespaces(compiler);
        
        Throwable thrown = catchThrowable(()->{
            // when
            compiler.compile(this.expression);
        });
        
        // then
        if (this.expression == null) {
            assertThat(thrown).isInstanceOf(NullPointerException.class);
        } else {
            assertThat(thrown).isInstanceOf(InvalidSelectorException.class);
            InvalidSelectorException e = (InvalidSelectorException)thrown;
            assertThat(e.getPosition()).isEqualTo(this.position);
        }
        log.fine(thrown.getMessage());
    }

    private SelectorCompiler declareNamespaces(SelectorCompiler compiler) {
        return compiler.withNamespace("ns", "http://www.example.com");
    }
}
