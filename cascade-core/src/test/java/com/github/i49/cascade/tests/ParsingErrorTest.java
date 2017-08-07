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

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.i49.cascade.api.InvalidSelectorException;
import com.github.i49.cascade.api.SelectorCompiler;

@RunWith(Parameterized.class)
public class ParsingErrorTest {

    private static final Logger log = Logger.getLogger(ParsingErrorTest.class.getName());

    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { null, 0 },
            { "", 0 },
            { "p, ", 3 },
            { ".5cm, ", 1 },
            { "> p", 0 },
            { "+ p", 0 },
            { "~ p", 0 },
            { ", p", 0 },
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
            // pseudo-class
            { ":", 1 },
            { ":foo", 1 },
            // pseudo-element
            { "::first-line", 1 }
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
        SelectorCompiler compiler = SelectorCompiler.create();
        Throwable thrown = catchThrowable(()->{
            compiler.compile(this.expression);
        });
        if (this.expression == null) {
            assertThat(thrown).isInstanceOf(NullPointerException.class);
        } else {
            assertThat(thrown).isInstanceOf(InvalidSelectorException.class);
            InvalidSelectorException e = (InvalidSelectorException)thrown;
            assertThat(e.getPosition()).isEqualTo(this.position);
        }
        log.fine(thrown.getMessage());
    }
}
