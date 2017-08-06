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
import static com.github.i49.cascade.core.dom.Elements.hasSiblingAfter;

import org.w3c.dom.Element;

/**
 * Matcher for :last-child pseudo-class selector.
 */
public class LastChildMatcher extends PseudoClassMatcher {

    public static final LastChildMatcher SINGLETON = new LastChildMatcher();

    private LastChildMatcher() {
    }

    @Override
    public boolean matches(Element element) {
        return hasParent(element) && !hasSiblingAfter(element);
    }

    @Override
    public String getClassName() {
        return "last-child";
    }
}
