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
public class ParsingTest {

    private SelectorCompiler compiler;
    
    @Before
    public void setUp() {
        compiler = SelectorCompiler.create(); 
    }
    
    @Test
    public void compile_shouldCompileUniversalSelector() {
       Selector s = compiler.compile("*"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileTypeSelector() {
       Selector s = compiler.compile("address"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileIdSelector() {
       Selector s = compiler.compile("#t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileTypeWithId() {
       Selector s = compiler.compile("li#t2"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileUnversalWithId() {
       Selector s = compiler.compile("*#t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileClassSelector() {
       Selector s = compiler.compile(".t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileTypeWithClass() {
       Selector s = compiler.compile("li.t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileIdWithClass() {
       Selector s = compiler.compile("*.t1"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }
    
    @Test
    public void compile_shouldCompileAttributePresenseSelector() {
       Selector s = compiler.compile("[title]"); 
       assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }
    
    @Test
    public void compile_shouldCompileAttributeValueSelector() {
        Selector s = compiler.compile("[class=example]"); 
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileDoubleQuotedAttributeValueSelector() {
        Selector s = compiler.compile("[class=\"example\"]"); 
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileSingleQuotedAttributeValueSelector() {
        Selector s = compiler.compile("[class=\'example\']"); 
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }
    
    @Test
    public void compile_shouldCompleMultipleValueSelector() {
        Selector s = compiler.compile("span[hello=\"Cleveland\"][goodbye=\"Columbus\"]");
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileIncludesSelector() {
        Selector s = compiler.compile("[rel~=\"copyright\"]"); 
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileDashMatchSelector() {
        Selector s = compiler.compile("[hreflang|=\"en\"]"); 
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompilePrefixMatchSelector() {
        Selector s = compiler.compile("[type^=\"image/\"]"); 
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileSuffixMatchSelector() {
        Selector s = compiler.compile("[href$=\".html\"]"); 
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileSubstringMatchSelector() {
        Selector s = compiler.compile("[title*=\"hello\"]"); 
        assertThat(s).isNotNull().isInstanceOf(SingleSelector.class);
    }

    @Test
    public void compile_shouldCompileSelectorsGroup() {
        Selector s = compiler.compile("li, p"); 
        assertThat(s).isNotNull().isInstanceOf(SelectorGroup.class);
        SelectorGroup g = (SelectorGroup)s;
        assertThat(g).hasSize(2);
    }
}
