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
public class ParsingErrorTest {

    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { null, 0 },
            { "", 0 },
            { "p, ", 3 },
            { ".5cm, ", 1 },
            { "> p", 0 },
            { ", p", 0 }
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
    }
}
