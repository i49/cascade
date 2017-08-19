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

package com.github.i49.cascade.tests.functional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Element;

import com.github.i49.cascade.api.Selector;
import com.github.i49.cascade.api.SelectorCompiler;
import com.github.i49.cascade.tests.AbstractSelectorTest;
import com.github.i49.cascade.tests.Expectation;

@RunWith(Parameterized.class)
public class NamespaceTest extends AbstractSelectorTest {

    private static final String SVG_NS = "http://www.w3.org/2000/svg";
    private static final String XLINK_NS = "http://www.w3.org/1999/xlink";
    private static final String NONEXISTENT_NS = "http://www.example.org/nonexistent";

    @Parameters(name = "{index}: {2}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][] {

            // prefixed element
            { "#prefix-test", null, "text", contains(2) },
            { "#prefix-test", SVG_NS, "text", contains(2) },
            { "#prefix-test", NONEXISTENT_NS, "text", contains() },
            { "#prefix-test", null, "*", contains(0, 1, 2, 3) },
            { "#prefix-test", SVG_NS, "*", contains(1, 2, 3) },
            { "#prefix-test", NONEXISTENT_NS, "*", contains() },
            { "#prefix-test", null, "ns1|text", contains(2) },
            { "#prefix-test", null, "ns1|*", contains(1, 2, 3) },
            { "#prefix-test", null, "ns3|text", contains() },
            { "#prefix-test", null, "ns3|*", contains() },

            // element in default namespace
            { "#default-ns-test", null, "text", contains(2) },
            { "#default-ns-test", SVG_NS, "text", contains(2) },
            { "#default-ns-test", NONEXISTENT_NS, "text", contains() },
            { "#default-ns-test", null, "*", contains(0, 1, 2, 3) },
            { "#default-ns-test", SVG_NS, "*", contains(1, 2, 3) },
            { "#default-ns-test", NONEXISTENT_NS, "*", contains() },
            { "#default-ns-test", null, "ns1|text", contains(2) },
            { "#default-ns-test", null, "ns1|*", contains(1, 2, 3) },
            { "#default-ns-test", null, "ns3|text", contains() },
            { "#default-ns-test", null, "ns3|*", contains() },

            // element without namespace
            { "#no-ns-test", null, "text", contains(2) },
            { "#no-ns-test", null, "|text", contains(2) },
            { "#no-ns-test", SVG_NS, "text", contains() },
            { "#no-ns-test", null, "*", contains(0, 1, 2, 3) },
            { "#no-ns-test", null, "|*", contains(0, 1, 2, 3) },
            { "#no-ns-test", SVG_NS, "*", contains() },
            { "#no-ns-test", null, "ns1|text", contains() },
            { "#no-ns-test", null, "ns1|*", contains() },

            // prefixed attribute
            { "#attribute-prefix-test", null, "[type]", contains() },
            { "#attribute-prefix-test", XLINK_NS, "[type]", contains() },
            { "#attribute-prefix-test", NONEXISTENT_NS, "[type]", contains() },
            { "#attribute-prefix-test", null, "[ns2|type]", contains(2, 3) },
            { "#attribute-prefix-test", null, "[ns2|type=extended]", contains(3) },
            { "#attribute-prefix-test", null, "[ns3|type]", contains() },
            { "#attribute-prefix-test", null, "[ns3|type=extended]", contains() },
            { "#attribute-prefix-test", null, "[|type]", contains() },
            { "#attribute-prefix-test", null, "[*|type]", contains(2, 3) },
            { "#attribute-prefix-test", null, "[*|type=extended]", contains(3) },

            // unprefixed attribute with default namespace
            { "#attribute-default-ns-test", null, "[type]", contains(2, 3) },
            { "#attribute-default-ns-test", XLINK_NS, "*|*[type]", contains(2, 3) },
            { "#attribute-default-ns-test", XLINK_NS, "*|*[type=extended]", contains(3) },
            { "#attribute-default-ns-test", NONEXISTENT_NS, "*|*[type]", contains(2, 3) },
            { "#attribute-default-ns-test", NONEXISTENT_NS, "*|*[type=extended]", contains(3) },
            { "#attribute-default-ns-test", null, "[ns2|type]", contains() },
            { "#attribute-default-ns-test", null, "[ns3|type]", contains() },
            { "#attribute-default-ns-test", null, "[|type]", contains(2, 3) },
            { "#attribute-default-ns-test", null, "[|type=extended]", contains(3) },
            { "#attribute-default-ns-test", null, "[*|type]", contains(2, 3) },
            { "#attribute-default-ns-test", null, "[*|type=extended]", contains(3) },

            // unprefixed attribute without default namespace
            { "#attribute-no-ns-test", null, "[type]", contains(2, 3) },
            { "#attribute-no-ns-test", XLINK_NS, "*|*[type]", contains(2, 3) },
            { "#attribute-no-ns-test", XLINK_NS, "*|*[type=extended]", contains(3) },
            { "#attribute-no-ns-test", NONEXISTENT_NS, "*|*[type]", contains(2, 3) },
            { "#attribute-no-ns-test", NONEXISTENT_NS, "*|*[type=extended]", contains(3) },
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

    private final String defaultNamespace;

    public NamespaceTest(String rootId, String defaultNamespace, String expression, Expectation expected) {
        super(rootId, expression, expected);
        this.defaultNamespace = defaultNamespace;
    }

    @Test
    public void test() {
        SelectorCompiler compiler = SelectorCompiler.create();
        configureCompiler(compiler);
        Selector selector = compiler.compile(getExpression());
        List<Element> actual  = selector.select(getRoot());
        assertThat(actual).containsExactlyElementsOf(getExpected());
    }

    private void configureCompiler(SelectorCompiler compiler) {
        compiler.declare("ns1", SVG_NS);
        compiler.declare("ns2", XLINK_NS);
        compiler.declare("ns3", NONEXISTENT_NS);
        compiler.declare("a", "http://www.example.org/a");
        compiler.declare("b", "http://www.example.org/b");
        if (defaultNamespace != null) {
            compiler.declareDefault(defaultNamespace);
        }
    }

    @BeforeClass
    public static void setUpOnce() {
        loadDocument("/namespace-test.xml");
    }
}
