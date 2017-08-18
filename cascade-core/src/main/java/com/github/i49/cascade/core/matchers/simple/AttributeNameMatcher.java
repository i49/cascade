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

/**
 * The matcher which will check the presence of the specified attribute.
 */
public class AttributeNameMatcher implements AttributeMatcher, QualifiedMatcherProvider<AttributeNameMatcher> {

    private final String localName;

    /**
     * Constructs this matcher.
     *
     * @param name the name of the attribute.
     */
    public AttributeNameMatcher(String name) {
        this.localName = name;
    }

    @Override
    public boolean matches(Element element) {
        return element.hasAttributeNS(null, localName);
    }

    @Override
    public AttributeNameMatcher anyNamespace() {
        return new AnyNamespaceMatcher(getLocalName());
    }

    @Override
    public AttributeNameMatcher withNamespace(String prefix, String namespace) {
        return new NamespacedMatcher(prefix, namespace, getLocalName());
    }

    @Override
    public AttributeNameMatcher withoutNamespace() {
        return new NoNamespaceMatcher(getLocalName());
    }

    @Override
    public String toString() {
        return "[" + getDisplayName() + "]";
    }

    public final String getLocalName() {
        return localName;
    }

    public String getDisplayName() {
        return getLocalName();
    }

    public String getAttributeValue(Element element) {
        return element.getAttributeNS(null, getLocalName());
    }

    private static class AnyNamespaceMatcher extends AttributeNameMatcher {

        public AnyNamespaceMatcher(String name) {
            super(name);
        }

        @Override
        public boolean matches(Element element) {
            return element.hasAttribute(getLocalName());
        }

        @Override
        public String getDisplayName() {
            return "*|" + getLocalName();
        }

        @Override
        public String getAttributeValue(Element element) {
            return element.getAttribute(getLocalName());
        }
    }

    private static class NoNamespaceMatcher extends AttributeNameMatcher {

        public NoNamespaceMatcher(String name) {
            super(name);
        }

        @Override
        public String getDisplayName() {
            return "|" + getLocalName();
        }
    }

    private static class NamespacedMatcher extends AttributeNameMatcher {

        private final String prefix;
        private final String namespace;

        public NamespacedMatcher(String prefix, String namespace, String name) {
            super(name);
            assert(prefix != null);
            assert(namespace != null);
            this.prefix = prefix;
            this.namespace = namespace;
        }

        @Override
        public boolean matches(Element element) {
            return element.hasAttributeNS(this.namespace, getLocalName());
        }

        @Override
        public String getAttributeValue(Element element) {
            return element.getAttributeNS(this.namespace, getLocalName());
        }

        @Override
        public String getDisplayName() {
            StringBuilder b = new StringBuilder();
            return b.append(prefix).append("|").append(getLocalName()).toString();
        }
    }
}
