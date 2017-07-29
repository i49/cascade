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
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

final class Documents {
    
    private static final ThreadLocal<DocumentBuilder> builders = 
            ThreadLocal.withInitial(Documents::builder);

    public static Document load(String resourceName) {
        Document doc = null;
        DocumentBuilder b = builders.get();
        ClassLoader loader = Documents.class.getClassLoader();
        try (InputStream in = loader.getResourceAsStream(resourceName)) {
            doc = b.parse(in);
            activateIdentifiers(doc);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
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
