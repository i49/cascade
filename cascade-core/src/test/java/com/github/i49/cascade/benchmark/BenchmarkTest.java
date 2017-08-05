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

package com.github.i49.cascade.benchmark;

import java.util.Set;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.i49.cascade.api.Selector;
import com.github.i49.cascade.tests.Documents;

@RunWith(Parameterized.class)
public class BenchmarkTest {

    private static final Logger log = Logger.getLogger(BenchmarkTest.class.getName());

    private static Document doc;

    private final String expression;
    private long startTime;

    public BenchmarkTest(String expression) {
        this.expression = expression;
    }

    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/html5-test.html");
    }

    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }

    @Parameters
    public static Object[] parameters() {
        return new Object[] {
                "#forms__action",
                "article",
                "* article",
                "* * article",
                "* ~ article",
                "* * ~ article",
                "* ~ * ~ article",
        };
    }

    @Test
    public void benchmark() {
        Selector s = Selector.compile(expression);
        Element root = doc.getDocumentElement();
        long elapsed = profile(()->{
            s.select(root);
        });

        log.info("\"" + expression + "\": " + elapsed + " [ms]");
    }

    private long profile(Runnable runnable) {
        start();
        int i = 10000;
        while (i-- > 0) {
            runnable.run();
        }
        return stop();
    }

    private void start() {
        this.startTime = System.nanoTime();
    }

    private long stop() {
        long elapsed = (System.nanoTime() - this.startTime) / (1000 * 1000);
        return elapsed;
    }

    // Not used.
    public void compareWithGetElementById() {
        String id = "forms__action";
        String expression = "#" + id;

        long elapsed1 = profile(()->doc.getElementById(id));

        Selector s = Selector.compile(expression);
        Element root = doc.getDocumentElement();
        long elapsed2 = profile(()->{
            s.select(root).iterator().next();
        });

        log.info("\"" + expression + "\": " + elapsed1 + " : " + elapsed2);
     }

    // Not used.
     public void compareWithGetElementsByTagName() {
         String expression = "article";

         long elapsed1 = profile(()->{
             NodeList nodeList = doc.getElementsByTagName(expression);
             nodeList.getLength();
         });

         Selector s = Selector.compile(expression);
         Element root = doc.getDocumentElement();
         long elapsed2 = profile(()->{
             Set<Element> selected = s.select(root);
             selected.size();
         });

         log.info("\"" + expression + "\": " + elapsed1 + " : " + elapsed2);
     }
}
