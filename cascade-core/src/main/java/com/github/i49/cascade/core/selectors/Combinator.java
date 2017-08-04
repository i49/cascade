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
    DESCENDANT {
        @Override
        public String getSymbol() {
            return " ";
        }

        @Override
        public Traverser getTraverser() {
            return DescendantTraverser.SINGLETON;
        }

        @Override
        public Traverser optimizePreviousTraverser(Traverser traverser) {
            return traverser.skippingDescendantsOfMatched();
        }
    },
    /** Child combinators. */
    CHILD {
        @Override
        public String getSymbol() {
            return " > ";
        }

        @Override
        public Traverser getTraverser() {
            return ChildTraverser.SINGLETON;
        }
    },
    /** Adjacent sibling combinator. */
    ADJACENT {
        @Override
        public String getSymbol() {
            return " + ";
        }

        @Override
        public Traverser getTraverser() {
            return AdjacentTraverser.SINGLETON;
        }
    },
    /** General sibling combinator. */
    SIBLING {
        @Override
        public String getSymbol() {
            return " ~ ";
        }

        @Override
        public Traverser getTraverser() {
            return SiblingTraverser.SINGLETON;
        }
    }
    ;

    /**
     * Returns the symbol representing this combinator.
     *
     * @return the symbol.
     */
    public abstract String getSymbol();

    /**
     * Returns the traverser for this combinator.
     *
     * @return the traverser.
     */
    public abstract Traverser getTraverser();

    /**
     * Optimizes the traverser used by the previous sequence.
     *
     * @param traverser the traverser to optimize.
     * @return optimized traverser.
     */
    public Traverser optimizePreviousTraverser(Traverser traverser) {
        return traverser;
    }
}
