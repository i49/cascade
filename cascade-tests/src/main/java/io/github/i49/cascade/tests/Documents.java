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

package io.github.i49.cascade.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class Documents {

    private static final ThreadLocal<DocumentBuilder> builders =
            ThreadLocal.withInitial(Documents::builder);

    /**
     * Loads a document from resource specified by name.
     *
     * @param resourceName the name of the resource.
     * @return loaded document.
     */
    public static Document load(String resourceName) {
        return loadFromResource(resourceName);
    }

    public static void save(String fileName, Document doc) {
        saveToFile(fileName, doc);
    }

    public static List<Element> descentandsOf(Element element) {
        List<Element> descendants = new ArrayList<>();
        walkTree(element, e->descendants.add(e));
        return descendants;
    }

    public static void walkTree(Element start, Consumer<Element> consumer) {
        consumer.accept(start);
        Node child = start.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                walkTree((Element)child, consumer);
            }
            child = child.getNextSibling();
        }
    }
    private static Document loadFromResource(String resourceName) {
        Document doc = null;
        DocumentBuilder b = builders.get();
        try (InputStream in = Documents.class.getResourceAsStream(resourceName)) {
            doc = b.parse(in);
            activateIdentifiers(doc);
        } catch (IOException e) {
            throw new RuntimeException("Document not found: " + resourceName);
        } catch (Exception e) {
            throw new RuntimeException("Problem found in: " + resourceName);
        }
        return doc;
    }

    private static void saveToFile(String fileName, Document doc) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(fileName));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void activateIdentifiers(Document doc) {
        walkTree(doc.getDocumentElement(), e->{
            if (e.hasAttribute("id")) {
                e.setIdAttribute("id", true);
            }
        });
    }

    private static DocumentBuilder builder() {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setNamespaceAware(true);
        try {
            return f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    private Documents() {
    }
}
