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

package com.github.i49.cascade.api;

import java.util.ServiceLoader;

/**
 *
 */
public abstract class SelectorCompilerFactory {

    private static final ThreadLocal<SelectorCompilerFactory> providers =
            ThreadLocal.withInitial(SelectorCompilerFactory::supply);

    private static SelectorCompilerFactory supply() {
        ServiceLoader<SelectorCompilerFactory> loader = ServiceLoader.load(SelectorCompilerFactory.class);
        return loader.iterator().next();
    }    

    static SelectorCompilerFactory get() {
        return providers.get();
    }
    
    public abstract SelectorCompiler createCompiler();
}
