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
import static io.github.i49.cascade.tests.Namespaces.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.api.SelectorCompiler;
import io.github.i49.cascade.tests.Documents;
import io.github.i49.cascade.tests.Fixture;
import io.github.i49.cascade.tests.Fixture.ElementMatcher;

@RunWith(Parameterized.class)
public class NamespaceTest {

    @Parameters(name = "{index}: {2}")
    public static Iterable<Object[]> parameters() {
        return Arrays.asList(new Object[][] {

            // prefixed element
            { "#prefix-test", null, "text", contains(2) },
            { "#prefix-test", SVG, "text", contains(2) },
            { "#prefix-test", NONEXISTENT, "text", contains() },
            { "#prefix-test", null, "*", contains(0, 1, 2, 3) },
            { "#prefix-test", SVG, "*", contains(1, 2, 3) },
            { "#prefix-test", NONEXISTENT, "*", contains() },
            { "#prefix-test", null, "ns1|text", contains(2) },
            { "#prefix-test", null, "ns1|*", contains(1, 2, 3) },
            { "#prefix-test", null, "ns3|text", contains() },
            { "#prefix-test", null, "ns3|*", contains() },

            // element in default namespace
            { "#default-ns-test", null, "text", contains(2) },
            { "#default-ns-test", SVG, "text", contains(2) },
            { "#default-ns-test", NONEXISTENT, "text", contains() },
            { "#default-ns-test", null, "*", contains(0, 1, 2, 3) },
            { "#default-ns-test", SVG, "*", contains(1, 2, 3) },
            { "#default-ns-test", NONEXISTENT, "*", contains() },
            { "#default-ns-test", null, "ns1|text", contains(2) },
            { "#default-ns-test", null, "ns1|*", contains(1, 2, 3) },
            { "#default-ns-test", null, "ns3|text", contains() },
            { "#default-ns-test", null, "ns3|*", contains() },

            // element without namespace
            { "#no-ns-test", null, "text", contains(2) },
            { "#no-ns-test", null, "|text", contains(2) },
            { "#no-ns-test", SVG, "text", contains() },
            { "#no-ns-test", null, "*", contains(0, 1, 2, 3) },
            { "#no-ns-test", null, "|*", contains(0, 1, 2, 3) },
            { "#no-ns-test", SVG, "*", contains() },
            { "#no-ns-test", null, "ns1|text", contains() },
            { "#no-ns-test", null, "ns1|*", contains() },

            // prefixed attribute
            { "#attribute-prefix-test", null, "[type]", contains() },
            { "#attribute-prefix-test", XLINK, "[type]", contains() },
            { "#attribute-prefix-test", NONEXISTENT, "[type]", contains() },
            { "#attribute-prefix-test", null, "[ns2|type]", contains(2, 3) },
            { "#attribute-prefix-test", null, "[ns2|type=extended]", contains(3) },
            { "#attribute-prefix-test", null, "[ns3|type]", contains() },
            { "#attribute-prefix-test", null, "[ns3|type=extended]", contains() },
            { "#attribute-prefix-test", null, "[|type]", contains() },
            { "#attribute-prefix-test", null, "[*|type]", contains(2, 3) },
            { "#attribute-prefix-test", null, "[*|type=extended]", contains(3) },

            // unprefixed attribute with default namespace
            { "#attribute-default-ns-test", null, "[type]", contains(2, 3) },
            { "#attribute-default-ns-test", XLINK, "*|*[type]", contains(2, 3) },
            { "#attribute-default-ns-test", XLINK, "*|*[type=extended]", contains(3) },
            { "#attribute-default-ns-test", NONEXISTENT, "*|*[type]", contains(2, 3) },
            { "#attribute-default-ns-test", NONEXISTENT, "*|*[type=extended]", contains(3) },
            { "#attribute-default-ns-test", null, "[ns2|type]", contains() },
            { "#attribute-default-ns-test", null, "[ns3|type]", contains() },
            { "#attribute-default-ns-test", null, "[|type]", contains(2, 3) },
            { "#attribute-default-ns-test", null, "[|type=extended]", contains(3) },
            { "#attribute-default-ns-test", null, "[*|type]", contains(2, 3) },
            { "#attribute-default-ns-test", null, "[*|type=extended]", contains(3) },

            // unprefixed attribute without default namespace
            { "#attribute-no-ns-test", null, "[type]", contains(2, 3) },
            { "#attribute-no-ns-test", XLINK, "*|*[type]", contains(2, 3) },
            { "#attribute-no-ns-test", XLINK, "*|*[type=extended]", contains(3) },
            { "#attribute-no-ns-test", NONEXISTENT, "*|*[type]", contains(2, 3) },
            { "#attribute-no-ns-test", NONEXISTENT, "*|*[type=extended]", contains(3) },
            { "#attribute-no-ns-test", null, "[ns2|type]", contains() },
            { "#attribute-no-ns-test", null, "[ns3|type]", contains() },
            { "#attribute-no-ns-test", null, "[|type]", contains(2, 3) },
            { "#attribute-no-ns-test", null, "[|type=extended]", contains(3) },
            { "#attribute-no-ns-test", null, "[*|type]", contains(2, 3) },
            { "#attribute-no-ns-test", null, "[*|type=extended]", contains(3) },

            // exact value test
            { "#exact-value-test", null, "[a|title=hello]", contains(2) },
            { "#exact-value-test", null, "[|title=hello]", contains(1) },
            { "#exact-value-test", null, "[*|title=hello]", contains(1, 2, 4) },
            { "#exact-value-test", null, "[a|title=helloworld]", contains(5) },
            { "#exact-value-test", null, "[|title=helloworld]", contains() },
            { "#exact-value-test", null, "[*|title=helloworld]", contains(5) },

            // space-separated value test
            { "#space-separated-value-test", null, "[a|title~=hello]", contains(2, 3) },
            { "#space-separated-value-test", null, "[|title~=hello]", contains(1) },
            { "#space-separated-value-test", null, "[*|title~=hello]", contains(1, 2, 3, 4) },
            { "#space-separated-value-test", null, "[a|title~=world]", contains(2) },
            { "#space-separated-value-test", null, "[|title~=world]", contains(1) },
            { "#space-separated-value-test", null, "[*|title~=world]", contains(1, 2, 4) },

            // dash-separated value test
            { "#dash-separated-value-test", null, "[a|title|=hello]", contains(2, 3) },
            { "#dash-separated-value-test", null, "[|title|=hello]", contains(1) },
            { "#dash-separated-value-test", null, "[*|title|=hello]", contains(1, 2, 3, 4) },
            { "#dash-separated-value-test", null, "[a|title|=world]", contains() },
            { "#dash-separated-value-test", null, "[|title|=world]", contains() },
            { "#dash-separated-value-test", null, "[*|title|=world]", contains() },

            // beginning value test
            { "#substring-value-test", null, "[a|title^=\"pen\"]", contains(2, 3, 5) },
            { "#substring-value-test", null, "[|title^=\"pen\"]", contains(1) },
            { "#substring-value-test", null, "[*|title^=\"pen\"]", contains(1, 2, 3, 4, 5) },
            { "#substring-value-test", null, "[a|title^=\"pen pineapple\"]", contains(2, 3) },
            { "#substring-value-test", null, "[|title^=\"pen pineapple\"]", contains(1) },
            { "#substring-value-test", null, "[*|title^=\"pen pineapple\"]", contains(1, 2, 3, 4) },

            // ending value test
            { "#substring-value-test", null, "[a|title$=\"apple\"]", contains(2, 3, 5) },
            { "#substring-value-test", null, "[|title$=\"apple\"]", contains(1) },
            { "#substring-value-test", null, "[*|title$=\"apple\"]", contains(1, 2, 3, 4, 5) },
            { "#substring-value-test", null, "[a|title$=\"pineapple apple\"]", contains(2) },
            { "#substring-value-test", null, "[|title$=\"pineapple apple\"]", contains(1) },
            { "#substring-value-test", null, "[*|title$=\"pineapple apple\"]", contains(1, 2, 4) },

            // middle value test
            { "#substring-value-test", null, "[a|title*=\"pineapple\"]", contains(2, 3, 5) },
            { "#substring-value-test", null, "[|title*=\"pineapple\"]", contains(1) },
            { "#substring-value-test", null, "[*|title*=\"pineapple\"]", contains(1, 2, 3, 4, 5) },
            { "#substring-value-test", null, "[a|title*=\"pineapple apple\"]", contains(2) },
            { "#substring-value-test", null, "[|title*=\"pineapple apple\"]", contains(1) },
            { "#substring-value-test", null, "[*|title*=\"pineapple apple\"]", contains(1, 2, 4) },
            { "#substring-value-test", null, "[a|title*=\"apple pineapple\"]", contains(5) },
            { "#substring-value-test", null, "[|title*=\"apple pineapple\"]", contains() },
            { "#substring-value-test", null, "[*|title*=\"apple pineapple\"]", contains(5) },

            // multiple attributes test
            { "#multiple-attributes-test", null, "a[*|attribute=\"pass\"]", contains(1) },
            { "#multiple-attributes-test", null, "b[*|attribute=\"pass\"]", contains(2) },
        });
    }

    private static Document doc;
    
    private final Fixture fixture;
    private final String defaultNamespace;

    public NamespaceTest(String startId, String defaultNamespace, String expression, Function<Element, ElementMatcher> mapper) {
        this.fixture = new Fixture(doc, startId, expression, mapper);
        this.defaultNamespace = defaultNamespace;
    }
    
    @BeforeClass
    public static void setUpOnce() {
        doc = Documents.load("/namespace-test.xml");
    }
    
    @AfterClass
    public static void tearDownOnce() {
        doc = null;
    }

    @Test
    public void test() {
        // given
        SelectorCompiler compiler = SelectorCompiler.create();
        compiler = configureCompiler(compiler);
        Selector selector = compiler.compile(fixture.getExpression());

        // when
        List<Element> actual  = selector.select(fixture.getStartElement());
        
        // then
        assertThat(actual, fixture.getMatcher());
    }

    private SelectorCompiler configureCompiler(SelectorCompiler compiler) {
        compiler = compiler
            .withNamespace("ns1", SVG)
            .withNamespace("ns2", XLINK)
            .withNamespace("ns3", NONEXISTENT)
            .withNamespace("a", "http://www.example.org/a")
            .withNamespace("b", "http://www.example.org/b");
        if (defaultNamespace != null) {
            compiler = compiler.withDefaultNamespace(defaultNamespace);
        }
        return compiler;
    }
}
