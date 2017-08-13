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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.github.i49.cascade.core.matchers.Matcher;

/**
 */
public class DescendantSequence extends PrecedingSequence {

    public DescendantSequence(Matcher matcher) {
        super(matcher, Combinator.DESCENDANT);
    }

    @Override
    public boolean test(Element start, Element root) {
        if (start == root) {
            return false;
        }
        Node parent = start.getParentNode();
        while (parent != root) {
            Element element = (Element)parent;
            if (test(element) && testPrevious(element, root)) {
                return true;
            }
            parent = parent.getParentNode();
        }
        return false;
    }
}
