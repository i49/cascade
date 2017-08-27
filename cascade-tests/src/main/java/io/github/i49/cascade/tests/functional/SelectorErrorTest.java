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

package io.github.i49.cascade.tests.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.Test;

import io.github.i49.cascade.api.Selector;

public class SelectorErrorTest {

    @Test
    public void selector_shouldThrowExceptionIfRootIsNull() {
        Selector s = Selector.compile("p");
        Throwable thrown = catchThrowable(()->{
            s.select(null);
        });
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
