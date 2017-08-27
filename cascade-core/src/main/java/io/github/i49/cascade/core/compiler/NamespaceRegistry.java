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

package io.github.i49.cascade.core.compiler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class NamespaceRegistry {

    private final Map<String, String> prefixes;
    private String defaultNamespace;

    public NamespaceRegistry() {
        this.prefixes = new HashMap<>();
        predefineNamespaces();
    }

    public boolean hasDefault() {
        return defaultNamespace != null;
    }

    public String getDefault() {
        return defaultNamespace;
    }

    public boolean hasPrefix(String prefix) {
        return this.prefixes.containsKey(prefix);
    }

    public String lookUp(String prefix) {
        return this.prefixes.get(prefix);
    }

    public void register(String prefix, String namespace) {
        assert(prefix != null);
        this.prefixes.put(prefix, namespace);
    }

    public void registerDefault(String namespace) {
        this.defaultNamespace = namespace;
    }

    private void predefineNamespaces() {
        register("", null);
    }
}
