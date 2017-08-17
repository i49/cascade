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
public class UniversalMatcher implements Matcher {

    private static final UniversalMatcher ANY_NAMESPACE = new UniversalMatcher();
    private static final UniversalMatcher NO_NAMESPACE = new NoNamespaceUniversalMatcher();

    public static UniversalMatcher get() {
        return ANY_NAMESPACE;
    }

    protected UniversalMatcher() {
    }

    @Override
    public MatcherType getType() {
        return MatcherType.UNIVERSAL;
    }

    @Override
    public boolean matches(Element element) {
        return true;
    }

    @Override
    public boolean matchesAlways() {
        return true;
    }

    @Override
    public String toString() {
        return "*";
    }

    public UniversalMatcher withNamespace(String namespace) {
        return new NamespacedUnversalMatcher(namespace);
    }

    public UniversalMatcher withNamespace(String prefix, String namespace) {
        return new NamespacedUnversalMatcher(prefix, namespace);
    }

    public UniversalMatcher withoutNamespace() {
        return NO_NAMESPACE;
    }

    protected static class NoNamespaceUniversalMatcher extends UniversalMatcher {

        @Override
        public boolean matches(Element element) {
            return element.getNamespaceURI() == null;
        }

        @Override
        public boolean matchesAlways() {
            return false;
        }

        @Override
        public String toString() {
            return "|" + super.toString();
        }
    }

    protected static class NamespacedUnversalMatcher extends UniversalMatcher {

        private final String prefix;
        private final String namespace;

        public NamespacedUnversalMatcher(String namespace) {
            this.prefix = null;
            this.namespace = namespace;
        }

        public NamespacedUnversalMatcher(String prefix, String namespace) {
            this.prefix = prefix;
            this.namespace = namespace;
        }

        @Override
        public boolean matches(Element element) {
            return namespace.equals(element.getNamespaceURI());
        }

        @Override
        public boolean matchesAlways() {
            return false;
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
