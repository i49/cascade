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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

final class Documents {
    
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
    
    public static Element findOne(Document doc, String tagname) {
        NodeList nodes = doc.getElementsByTagName(tagname);
        if (nodes.getLength() > 0) {
            return (Element)nodes.item(0);
        } else {
            return null;
        }
    }
    
    public static Collection<Element> findAll(Document doc) {
        NodeList nodes = doc.getElementsByTagName("*");
        List<Element> found = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            found.add((Element)nodes.item(i));
        }
        return found;
    }
    
    private static Document loadFromResource(String resourceName) {
        Document doc = null;
        DocumentBuilder b = builders.get();
        try (InputStream in = Documents.class.getResourceAsStream(resourceName)) {
            doc = b.parse(in);
            activateIdentifiers(doc);
        } catch (Exception e) {
            throw new RuntimeException("Document not found: " + resourceName);
        }
        return doc;
    }
    
    private static void activateIdentifiers(Document doc) {
        visitElement(doc.getDocumentElement(), e->{
            if (e.hasAttribute("id")) {
                e.setIdAttribute("id", true);
            }
        });
    }
    
    private static void visitElement(Element e, Consumer<Element> consumer) {
        consumer.accept(e);
        Node child = e.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                visitElement((Element)child, consumer);
            }
            child = child.getNextSibling();
        }
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
