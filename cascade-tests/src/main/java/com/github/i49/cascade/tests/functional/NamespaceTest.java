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
            { "#prefix-test", null, "text", contains(1) },
            { "#prefix-test", SVG_NS, "text", contains(1) },
            { "#prefix-test", NONEXISTENT_NS, "text", contains() },
            { "#prefix-test", null, "*", contains(0, 1, 2) },
            { "#prefix-test", SVG_NS, "*", contains(0, 1, 2) },
            { "#prefix-test", NONEXISTENT_NS, "*", contains() },
            { "#prefix-test", null, "ns1|text", contains(1) },
            { "#prefix-test", null, "ns1|*", contains(0, 1, 2) },
            { "#prefix-test", null, "ns3|text", contains() },
            { "#prefix-test", null, "ns3|*", contains() },

            // element in default namespace
            { "#default-ns-test", null, "text", contains(1) },
            { "#default-ns-test", SVG_NS, "text", contains(1) },
            { "#default-ns-test", NONEXISTENT_NS, "text", contains() },
            { "#default-ns-test", null, "*", contains(0, 1, 2) },
            { "#default-ns-test", SVG_NS, "*", contains(0, 1, 2) },
            { "#default-ns-test", NONEXISTENT_NS, "*", contains() },
            { "#default-ns-test", null, "ns1|text", contains(1) },
            { "#default-ns-test", null, "ns1|*", contains(0, 1, 2) },
            { "#default-ns-test", null, "ns3|text", contains() },
            { "#default-ns-test", null, "ns3|*", contains() },

            // element without namespace
            { "#no-ns-test", null, "text", contains(1) },
            { "#no-ns-test", null, "|text", contains(1) },
            { "#no-ns-test", SVG_NS, "text", contains() },
            { "#no-ns-test", null, "*", contains(0, 1, 2) },
            { "#no-ns-test", null, "|*", contains(0, 1, 2) },
            { "#no-ns-test", SVG_NS, "*", contains() },
            { "#no-ns-test", null, "ns1|text", contains() },
            { "#no-ns-test", null, "ns1|*", contains() },

            // prefixed attribute
            { "#attribute-prefix-test", null, "[type]", contains() },
            { "#attribute-prefix-test", XLINK_NS, "[type]", contains() },
            { "#attribute-prefix-test", NONEXISTENT_NS, "[type]", contains() },
            { "#attribute-prefix-test", null, "[ns2|type]", contains(1, 2) },
            { "#attribute-prefix-test", null, "[ns2|type=extended]", contains(2) },
            { "#attribute-prefix-test", null, "[ns3|type]", contains() },
            { "#attribute-prefix-test", null, "[ns3|type=extended]", contains() },
            { "#attribute-prefix-test", null, "[|type]", contains() },
            { "#attribute-prefix-test", null, "[*|type]", contains(1, 2) },
            { "#attribute-prefix-test", null, "[*|type=extended]", contains(2) },

            // unprefixed attribute with default namespace
            { "#attribute-default-ns-test", null, "[type]", contains(1, 2) },
            { "#attribute-default-ns-test", XLINK_NS, "*|*[type]", contains(1, 2) },
            { "#attribute-default-ns-test", XLINK_NS, "*|*[type=extended]", contains(2) },
            { "#attribute-default-ns-test", NONEXISTENT_NS, "*|*[type]", contains(1, 2) },
            { "#attribute-default-ns-test", NONEXISTENT_NS, "*|*[type=extended]", contains(2) },
            { "#attribute-default-ns-test", null, "[ns2|type]", contains() },
            { "#attribute-default-ns-test", null, "[ns3|type]", contains() },
            { "#attribute-default-ns-test", null, "[|type]", contains(1, 2) },
            { "#attribute-default-ns-test", null, "[|type=extended]", contains(2) },
            { "#attribute-default-ns-test", null, "[*|type]", contains(1, 2) },
            { "#attribute-default-ns-test", null, "[*|type=extended]", contains(2) },

            // unprefixed attribute without default namespace
            { "#attribute-no-ns-test", null, "[type]", contains(1, 2) },
            { "#attribute-no-ns-test", XLINK_NS, "*|*[type]", contains(1, 2) },
            { "#attribute-no-ns-test", XLINK_NS, "*|*[type=extended]", contains(2) },
            { "#attribute-no-ns-test", NONEXISTENT_NS, "*|*[type]", contains(1, 2) },
            { "#attribute-no-ns-test", NONEXISTENT_NS, "*|*[type=extended]", contains(2) },
            { "#attribute-no-ns-test", null, "[ns2|type]", contains() },
            { "#attribute-no-ns-test", null, "[ns3|type]", contains() },
            { "#attribute-no-ns-test", null, "[|type]", contains(1, 2) },
            { "#attribute-no-ns-test", null, "[|type=extended]", contains(2) },
            { "#attribute-no-ns-test", null, "[*|type]", contains(1, 2) },
            { "#attribute-no-ns-test", null, "[*|type=extended]", contains(2) },

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
        if (defaultNamespace != null) {
            compiler.declareDefault(defaultNamespace);
        }
    }

    @BeforeClass
    public static void setUpOnce() {
        loadDocument("/namespace-test.xml");
    }
}
