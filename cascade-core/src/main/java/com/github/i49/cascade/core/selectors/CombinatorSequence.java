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

import java.util.List;

import com.github.i49.cascade.core.matchers.Matcher;

/**
 * A sequence preceded by combinator.
 */
public abstract class CombinatorSequence extends AbstractSequence {

    private final Combinator combinator;
  
    protected CombinatorSequence(Combinator combinator, List<Matcher> matchers) {
        super(matchers);
        this.combinator = combinator;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getCombinator().getSymbol());
        b.append(super.toString());
        return b.toString();
    }

    /**
     * Returns the combinator type of this sequence.
     * 
     * @return the combinator type of this sequence.
     */
    public Combinator getCombinator() {
        return combinator;
    }
}
