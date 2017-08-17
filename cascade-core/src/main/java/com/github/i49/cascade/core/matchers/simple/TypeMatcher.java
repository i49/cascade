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

package com.github.i49.cascade.core.matchers.simple;

import org.w3c.dom.Element;

import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.MatcherType;

/**
 *
 */
public class TypeMatcher implements Matcher {

    public static TypeMatcher anyNamespace(String localName) {
        return new TypeMatcher(localName);
    }

    public static TypeMatcher withNamespace(String prefix, String namespace, String localName) {
        return new NamespacedTypeMatcher(prefix, namespace, localName);
    }

    public static TypeMatcher withoutNamespace(String localName) {
        return new NoNamespaceTypeMatcher(localName);
    }

    private final String elementName;

    protected TypeMatcher(String elementName) {
        this.elementName = elementName;
    }

    @Override
    public MatcherType getType() {
        return MatcherType.TYPE;
    }

    @Override
    public boolean matches(Element element) {
        return elementName.equals(element.getLocalName());
    }

    @Override
    public String toString() {
        return elementName;
    }

    public String getElementName() {
        return elementName;
    }

    private static class NoNamespaceTypeMatcher extends TypeMatcher {

        public NoNamespaceTypeMatcher(String elementName) {
            super(elementName);
        }

        @Override
        public boolean matches(Element element) {
            if (!super.matches(element)) {
                return false;
            }
            return element.getNamespaceURI() == null;
        }

        @Override
        public String toString() {
            return "|" + super.toString();
        }
    }

    private static class NamespacedTypeMatcher extends TypeMatcher {

        private final String prefix;
        private final String namespace;

        public NamespacedTypeMatcher(String prefix, String namespace, String localName) {
            super(localName);
            this.prefix = prefix;
            this.namespace = namespace;
        }

        @Override
        public boolean matches(Element element) {
            if (!super.matches(element)) {
                return false;
            }
            return namespace.equals(element.getNamespaceURI());
        }

        @Override
        public String toString() {
            if (prefix != null) {
                return prefix + "|" + super.toString();
            } else {
                return super.toString();
            }
        }
    }
}
