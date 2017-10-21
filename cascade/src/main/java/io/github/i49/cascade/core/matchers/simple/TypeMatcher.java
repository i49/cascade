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

package io.github.i49.cascade.core.matchers.simple;

import org.w3c.dom.Element;

import io.github.i49.cascade.core.matchers.Matcher;
import io.github.i49.cascade.core.matchers.MatcherType;

/**
 * Matcher for type selector.
 */
public class TypeMatcher implements Matcher, QualifiedMatcherProvider<TypeMatcher> {

    private final String localName;

    public TypeMatcher(String localName) {
        this.localName = localName;
    }

    @Override
    public MatcherType getType() {
        return MatcherType.TYPE;
    }

    @Override
    public boolean matches(Element element) {
        return localName.equals(element.getLocalName());
    }

    @Override
    public String toString() {
        return localName;
    }

    public String getLocalName() {
        return localName;
    }

    @Override
    public TypeMatcher anyNamespace() {
        return new AnyNamespaceMatcher(getLocalName());
    }

    @Override
    public TypeMatcher withNamespace(String namespace) {
        return new NamespacedMatcher(namespace, getLocalName());
    }

    @Override
    public TypeMatcher withNamespace(String prefix, String namespace) {
        return new NamespacedMatcher(prefix, namespace, getLocalName());
    }

    @Override
    public TypeMatcher withoutNamespace() {
        return new NoNamespaceMatcher(getLocalName());
    }

    private static class AnyNamespaceMatcher extends TypeMatcher {

        public AnyNamespaceMatcher(String elementName) {
            super(elementName);
        }

        @Override
        public String toString() {
            return "*|" + super.toString();
        }
    }

    private static class NoNamespaceMatcher extends TypeMatcher {

        public NoNamespaceMatcher(String elementName) {
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

    private static class NamespacedMatcher extends TypeMatcher {

        private final String prefix;
        private final String namespace;

        public NamespacedMatcher(String namespace, String localName) {
            super(localName);
            this.prefix = null;
            this.namespace = namespace;
        }

        public NamespacedMatcher(String prefix, String namespace, String localName) {
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
