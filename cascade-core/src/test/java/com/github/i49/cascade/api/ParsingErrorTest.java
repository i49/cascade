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

import org.junit.BeforeClass;
import org.junit.Test;

public class ParsingErrorTest {

    private static SelectorCompiler compiler;
    
    @BeforeClass
    public static void setUpOnce() {
        compiler = SelectorCompiler.create(); 
    }
    
    @Test
    public void compile_shouldThrowExceptionIfExpressionIsNull() {
        Throwable thrown = catchThrowable(()->{
           compiler.compile(null); 
        });
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void compile_shouldThrowExceptionIfExpressionIsBlank() {
        Throwable thrown = catchThrowable(()->{
           compiler.compile(""); 
        });
        assertThat(thrown).isInstanceOf(InvalidSelectorExeption.class);
    }

    @Test
    public void compile_shouldThrowExceptionIfUnexpectedEndOfInputOcurred() {
        Throwable thrown = catchThrowable(()->{
           compiler.compile("p, "); 
        });
        assertThat(thrown).isInstanceOf(InvalidSelectorExeption.class);
    }
    
    @Test
    public void compile_shoundThrowExceptionIfClassNameIsInvalid() {
        Throwable thrown = catchThrowable(()->{
            compiler.compile(".5cm"); 
         });
         assertThat(thrown).isInstanceOf(InvalidSelectorExeption.class);
    }
}
