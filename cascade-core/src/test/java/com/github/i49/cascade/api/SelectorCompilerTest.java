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

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class SelectorCompilerTest {

    private SelectorCompiler compiler;
    
    @Before
    public void setUp() {
        compiler = SelectorCompiler.create(); 
    }
    
    @Test
    public void compile_shouldParseUniversalSelector() {
       Selector s = compiler.compile("*"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldParseTypeSelector() {
       Selector s = compiler.compile("address"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldParseIdSelector() {
       Selector s = compiler.compile("#t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldParseTypeWithId() {
       Selector s = compiler.compile("li#t2"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldParseUnversalWithId() {
       Selector s = compiler.compile("*#t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldParseClassSelector() {
       Selector s = compiler.compile(".t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldParseTypeWithClass() {
       Selector s = compiler.compile("li.t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldParseIdWithClass() {
       Selector s = compiler.compile("*.t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }
    
    @Test
    public void compile_shouldParseSelectorsGroup() {
        Selector s = compiler.compile("li, p"); 
        assertThat(s).isNotNull().isInstanceOf(SelectorGroup.class);
        SelectorGroup g = (SelectorGroup)s;
        assertThat(g).hasSize(2);
    }
}
