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

import static io.github.i49.cascade.tests.Fixture.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.i49.cascade.tests.BasicSelectorTest;
import io.github.i49.cascade.tests.Documents;
import io.github.i49.cascade.tests.Fixture;

@RunWith(Parameterized.class)
public class SmallXmlTest extends BasicSelectorTest {

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { "*", contains(0) },
            { "_ _", contains() },
            { "_ > _", contains() },
            { "_ ~ _", contains() },
            { "_ + _", contains() },
        });
    }
    
    private static Document doc;
    private final Fixture fixture;

    public SmallXmlTest(String expression, Function<Element, ElementMatcher> mapper) {
        this.fixture = new Fixture(doc, expression, mapper);
    }
    
    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/smallest-xml.xml");
    }
    
    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }
    
    @Override
    public void testWithDefaultNamespace() {
        // nothing to do
    }

    @Override
    public Fixture getFixture() {
        return fixture;
    }
}
