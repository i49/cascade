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

package com.github.i49.cascade.core.matchers.pseudo;

import static com.github.i49.cascade.core.dom.Elements.hasParent;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Matcher for :empty pseudo-class selector.
 */
public class OnlyChildMatcher extends PseudoClassMatcher {

    public static final OnlyChildMatcher SINGLETON = new OnlyChildMatcher();

    private OnlyChildMatcher() {
    }

    @Override
    public boolean matches(Element element) {
        if (!hasParent(element)) {
            return false;
        }

        // No sibling before the given element.
        Node sibling = element.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
            sibling = sibling.getPreviousSibling();
        }

        // No sibling after the given element.
        sibling = element.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
            sibling = sibling.getNextSibling();
        }

       return true;
    }

    @Override
    public String getClassName() {
        return "only-child";
    }
}
