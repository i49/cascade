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
 * Matcher for universal selector represented by "*".
 */
public class UniversalMatcher implements Matcher, QualifiedMatcherProvider<UniversalMatcher> {

    private static final UniversalMatcher DEFAULT_INSTANCE = new UniversalMatcher();
    private static final UniversalMatcher ANY_NAMESPACE = new AnyNamespaceMatcher();
    private static final UniversalMatcher NO_NAMESPACE = new NoNamespaceMatcher();

    public static UniversalMatcher get() {
        return DEFAULT_INSTANCE;
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

    @Override
    public UniversalMatcher anyNamespace() {
        return ANY_NAMESPACE;
    }

    @Override
    public UniversalMatcher withNamespace(String namespace) {
        return new NamespacedMatcher(namespace);
    }

    @Override
    public UniversalMatcher withNamespace(String prefix, String namespace) {
        return new NamespacedMatcher(prefix, namespace);
    }

    @Override
    public UniversalMatcher withoutNamespace() {
        return NO_NAMESPACE;
    }

    private static class AnyNamespaceMatcher extends UniversalMatcher {

        @Override
        public String toString() {
            return "*|" + super.toString();
        }
    }

    private static class NoNamespaceMatcher extends UniversalMatcher {

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

    private static class NamespacedMatcher extends UniversalMatcher {

        private final String prefix;
        private final String namespace;

        public NamespacedMatcher(String namespace) {
            this.prefix = null;
            this.namespace = namespace;
        }

        public NamespacedMatcher(String prefix, String namespace) {
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
