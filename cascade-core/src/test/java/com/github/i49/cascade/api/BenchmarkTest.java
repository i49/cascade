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

import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Ignore
public class BenchmarkTest {

    private static final Logger log = Logger.getLogger(BenchmarkTest.class.getName());

    private static Document doc;

    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/index.html");
    }

    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }

    private long startTime;

    private void start() {
        this.startTime = System.nanoTime();
    }

    private void stop() {
        long elapsed = (System.nanoTime() - this.startTime) / (1000 * 1000);
        log.info("elapsed: " + elapsed + " [ms]");
    }

    @Test
    public void test_idSelector() {
       String id = "forms__action";

       log.info("getElementById");
       benchmark(()->doc.getElementById(id));

       Selector s = Selector.compile("#" + id);
       Element root = doc.getDocumentElement();
       log.info("Selector::select(#id)");
       benchmark(()->s.select(root));
    }

    @Test
    public void test_typeSelector() {
        String tagName = "article";

        log.info("getElementsByTagName()");
        benchmark(()->doc.getElementsByTagName(tagName));

        Selector s = Selector.compile(tagName);
        Element root = doc.getDocumentElement();
        log.info("Selector::select(type)");
        benchmark(()->s.select(root));
    }

    private void benchmark(Runnable runnable) {
        start();
        int i = 10000;
        while (i-- > 0) {
            runnable.run();
        }
        stop();
    }
}
