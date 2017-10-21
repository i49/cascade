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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * The matcher which will check the presence of the specified attribute.
 */
public class AttributeNameMatcher implements AttributeMatcher, QualifiedMatcherProvider<AttributeNameMatcher> {

    private final String localName;

    /**
     * Constructs this matcher.
     *
     * @param localName the unqualified name of the attribute.
     */
    public AttributeNameMatcher(String localName) {
        this.localName = localName;
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

    public List<Attr> findAttributes(Element element) {
        Attr found = element.getAttributeNodeNS(null, getLocalName());
        if (found == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(found);
    }

    /**
     * Matcher for attributes in all namespaces including no namespace.
     */
    private static class AnyNamespaceMatcher extends AttributeNameMatcher {

        public AnyNamespaceMatcher(String localName) {
            super(localName);
        }

        @Override
        public boolean matches(Element element) {
            final String expectedName = getLocalName();
            NamedNodeMap attributes = element.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                if (attribute.getLocalName().equals(expectedName)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDisplayName() {
            return "*|" + getLocalName();
        }

        @Override
        public List<Attr> findAttributes(Element element) {
            List<Attr> found = null;
            final String expectedName = getLocalName();
            NamedNodeMap attributes = element.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                if (attribute.getLocalName().equals(expectedName)) {
                    if (found == null) {
                        found = new ArrayList<>();
                    }
                    found.add((Attr)attribute);
                }
            }
            return (found != null) ? found : Collections.emptyList();
        }
    }

    /**
     * Matcher for attributes without namespace.
     */
    private static class NoNamespaceMatcher extends AttributeNameMatcher {

        public NoNamespaceMatcher(String localName) {
            super(localName);
        }

        @Override
        public String getDisplayName() {
            return "|" + getLocalName();
        }
    }

    /**
     * Matcher for attributes in specific namespace.
     */
    private static class NamespacedMatcher extends AttributeNameMatcher {

        private final String prefix;
        private final String namespace;

        public NamespacedMatcher(String prefix, String namespace, String localName) {
            super(localName);
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
        public String getDisplayName() {
            StringBuilder b = new StringBuilder();
            return b.append(prefix).append("|").append(getLocalName()).toString();
        }

        @Override
        public List<Attr> findAttributes(Element element) {
            Attr found = element.getAttributeNodeNS(this.namespace, getLocalName());
            if (found == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(found);
        }
    }
}
