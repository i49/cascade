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

package com.github.i49.cascade.core.selectors;

import com.github.i49.cascade.core.traversers.AdjacentTraverser;
import com.github.i49.cascade.core.traversers.ChildTraverser;
import com.github.i49.cascade.core.traversers.DescendantTraverser;
import com.github.i49.cascade.core.traversers.SiblingTraverser;
import com.github.i49.cascade.core.traversers.Traverser;

/**
 * Combinators.
 */
public enum Combinator {
    /** Descendant combinator. */
    DESCENDANT(" "),
    /** Child combinators. */
    CHILD(" > "),
    /** Adjacent sibling combinator. */
    ADJACENT(" + "),
    /** General sibling combinator. */
    SIBLING(" ~ ")
    ;

    private final String symbol;
    
    private Combinator(String symbol) {
        this.symbol = symbol;
    }
    
    /**
     * Returns the symbol representing this combinator.
     * 
     * @return the symbol.
     */
    public String getSymbol() {
        return symbol;
    }
    
    public Traverser getTraverser() {
        switch (this) {
        case DESCENDANT:
            return DescendantTraverser.SINGLETON;
        case CHILD:
            return ChildTraverser.SINGLETON;
        case ADJACENT:
            return AdjacentTraverser.SINGLETON;
        case SIBLING:
            return SiblingTraverser.SINGLETON;
        default:
            return null;
        }
    }
}
