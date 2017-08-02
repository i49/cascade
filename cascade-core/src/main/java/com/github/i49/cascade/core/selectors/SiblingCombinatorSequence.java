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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.github.i49.cascade.core.matchers.Matcher;

/**
 * A sequence preceded by general sibling combinator.
 */
public class SiblingCombinatorSequence extends AbstractCombinatorSequence {

    public SiblingCombinatorSequence(List<Matcher> matchers) {
        super(Combinator.SIBLING, matchers);
    }

    @Override
    protected void traverse(Element e, SequenceResult result) {
        visitAllSiblingsAfter(e, result);
    }

    private void visitAllSiblingsAfter(Element e, SequenceResult result) {
        Node sibling = e.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                match((Element)sibling, result);
            }
            sibling = sibling.getNextSibling();
        }
    }
}
