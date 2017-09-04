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

package io.github.i49.cascade.tests.performance;

import java.util.List;
import java.util.function.Function;
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

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.api.SelectorCompiler;
import io.github.i49.cascade.tests.Documents;
import io.github.i49.cascade.tests.Fixture;
import io.github.i49.cascade.tests.Namespaces;
import io.github.i49.cascade.tests.functional.Html5Test;

@RunWith(Parameterized.class)
public class PerformanceTest {

    private static final Logger log = Logger.getLogger(PerformanceTest.class.getName());
    private static int REPEAT_COUNT = 10000;
    
    private static Document doc;
    private final Fixture fixture;

    @Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> parameters() {
        return Html5Test.parameters();
    }

    public PerformanceTest(String expression, Function<Element, List<Element>> teacher) {
        this.fixture = new Fixture(doc, expression, teacher);
    }
    
    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/html5-test.html");
    }
    
    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }

    @Test
    public void testPerformance() {
        SelectorCompiler compiler = SelectorCompiler.create();
        Selector selector = compiler.compile(fixture.getExpression());
        profileSelector(selector, fixture.getStartElement(), fixture.getExpression());
    }

    @Test
    public void testPerformanceWithDefaultNamespace() {
        SelectorCompiler compiler = SelectorCompiler.create();
        compiler = compiler.withDefaultNamespace(Namespaces.XHTML);
        Selector selector = compiler.compile(fixture.getExpression());
        log.info("with default namespace");
        profileSelector(selector, fixture.getStartElement(), fixture.getExpression());
    }

    private void profileSelector(Selector selector, Element start, String expression) {
        long elapsed = profile(()->{ selector.select(start); });
        log.info("selector = \"" + expression + "\", elapsed = " + elapsed + " [ms]");
    }

    private static long profile(Runnable runnable) {
        long startTime = System.nanoTime();
        int i = REPEAT_COUNT;
        while (i-- > 0) {
            runnable.run();
        }
        long endTime = System.nanoTime();
        long elapsed = (endTime - startTime) / (1000 * 1000);
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
             List<Element> selected = s.select(root);
             selected.size();
         });

         log.info("\"" + expression + "\": " + elapsed1 + " : " + elapsed2);
     }
}
