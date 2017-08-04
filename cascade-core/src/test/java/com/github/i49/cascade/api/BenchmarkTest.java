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

import java.util.Set;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

    private long stop() {
        long elapsed = (System.nanoTime() - this.startTime) / (1000 * 1000);
        return elapsed;
    }

    @Test
    public void benchmark_idSelector() {
       String id = "forms__action";
       String expression = "#" + id;

       long elapsed1 = benchmark(()->doc.getElementById(id));

       Selector s = Selector.compile(expression);
       Element root = doc.getDocumentElement();
       long elapsed2 = benchmark(()->{
           s.select(root).iterator().next();
       });

       log.info("\"" + expression + "\": " + elapsed1 + " : " + elapsed2);
    }

    @Test
    public void benchmark_typeSelector() {
        String expression = "article";

        long elapsed1 = benchmark(()->{
            NodeList nodeList = doc.getElementsByTagName(expression);
            nodeList.getLength();
        });

        Selector s = Selector.compile(expression);
        Element root = doc.getDocumentElement();
        long elapsed2 = benchmark(()->{
            Set<Element> selected = s.select(root);
            selected.size();
        });

        log.info("\"" + expression + "\": " + elapsed1 + " : " + elapsed2);
    }

    @Test
    public void benchmark_descendantCombinator() {
        String expression = "* article";

        Selector s = Selector.compile(expression);
        Element root = doc.getDocumentElement();
        long elapsed = benchmark(()->{
            Set<Element> selected = s.select(root);
            selected.size();
        });

        log.info("\"" + expression + "\": " + elapsed);
    }

    @Test
    public void benchmark_doubleDescendantCombinator() {
        String expression = "* * article";

        Selector s = Selector.compile(expression);
        Element root = doc.getDocumentElement();
        long elapsed = benchmark(()->{
            Set<Element> selected = s.select(root);
            selected.size();
        });

        log.info("\"" + expression + "\": " + elapsed);
    }

    private long benchmark(Runnable runnable) {
        start();
        int i = 10000;
        while (i-- > 0) {
            runnable.run();
        }
        return stop();
    }
}
