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
 * A sequence preceded by adjacent sibling combinator.
 */
public class AdjacentCombinatorSequence extends AbstractCombinatorSequence {

    public AdjacentCombinatorSequence(List<Matcher> matchers) {
        super(Combinator.ADJACENT, matchers);
    }

    @Override
    protected void traverse(Element e, SequenceResult result) {
        visitOneSiblingAfter(e, result);
    }
    
    private void visitOneSiblingAfter(Element e, SequenceResult result) {
        Node sibling = e.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                match((Element)sibling, result);
                break;
            }
            sibling = sibling.getNextSibling();
        }
    }
}
