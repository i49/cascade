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

package io.github.i49.cascade.core.selectors;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import io.github.i49.cascade.core.matchers.Matcher;

/**
 */
public class SiblingSequence extends PrecedingSequence {

    public SiblingSequence(Matcher matcher) {
        super(matcher, Combinator.SIBLING);
    }

    @Override
    public boolean test(Element start, Element root) {
        Node sibling = start.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)sibling;
                if (test(element) && testPrevious(element, root)) {
                    return true;
                }
            }
            sibling = sibling.getPreviousSibling();
        }
        return false;
    }
}
