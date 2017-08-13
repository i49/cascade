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

import com.github.i49.cascade.core.matchers.Matcher;

/**
 *
 */
public abstract class PrecedingSequence extends AbstractSequence {

    public final Combinator combinator;

    protected PrecedingSequence(Matcher matcher, Combinator combinator) {
        super(matcher);
        this.combinator = combinator;
    }

    public Combinator getCombinator() {
        return combinator;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(super.toString());
        b.append(getCombinator().getSymbol());
        return b.toString();
    }
}
