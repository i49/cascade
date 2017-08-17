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

package com.github.i49.cascade.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;

public class Expectation {

    private final boolean inclusive;
    private final int[] entries;

    public static Expectation include(int[] entries) {
        return new Expectation(entries, true);
    }

    public static Expectation exclude(int[] entries) {
        return new Expectation(entries, false);
    }

    private Expectation(int[] entries, boolean inclusive) {
        this.entries = entries;
        this.inclusive = inclusive;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public boolean isEmpty() {
        return entries.length == 0;
    }

    public int[] getEntries() {
        return entries;
    }

    public List<Element> getExpected(Element root) {
        if (isEmpty() && isInclusive()) {
            return Collections.emptyList();
        }
        List<Element> all = Documents.descentandsOf(root);
        if (isEmpty()) {
            return all;
        }
        List<Element> entries = new ArrayList<>();
        for (int index: getEntries()) {
            entries.add(all.get(index));
        }
        if (isInclusive()) {
            return entries;
        } else {
            List<Element> subtracted = new ArrayList<>(all);
            subtracted.removeAll(entries);
            return subtracted;
        }
    }
}
