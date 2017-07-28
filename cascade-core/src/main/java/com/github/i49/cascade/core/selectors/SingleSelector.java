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

package com.github.i49.cascade.core.selectors;

import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.github.i49.cascade.api.Selector;

/**
 *
 */
public class SingleSelector implements Selector {
    
    private final Collector collector;
    
    public SingleSelector(Collector collector) {
        this.collector = collector;
    }

    @Override
    public Set<Element> select(Element root) {
        Set<Element> found = new LinkedHashSet<>();
        collector.collect(root, found);
        return found;
    }
}
