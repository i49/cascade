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

package io.github.i49.cascade.core.matchers.pseudo;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Matcher for :empty pseudo-class selector.
 */
public class EmptyMatcher extends PseudoClassMatcher {

    public static final EmptyMatcher SINGLETON = new EmptyMatcher();

    private EmptyMatcher() {
    }

    @Override
    public boolean matches(Element element) {
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            short type = child.getNodeType();
            if (type == Node.ELEMENT_NODE) {
                return false;
            } else if (type == Node.TEXT_NODE ||
                       type == Node.CDATA_SECTION_NODE ||
                       type == Node.ENTITY_REFERENCE_NODE) {
                String content = child.getTextContent();
                if (content != null && !content.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public PseudoClass getPseudoClass() {
        return PseudoClass.EMPTY;
    }
}
