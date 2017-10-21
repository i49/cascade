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

package io.github.i49.cascade.core.dom;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 */
public final class Elements {

    public static boolean isRoot(Element element) {
        return element.getOwnerDocument().getDocumentElement() == element;
    }

    /**
     * Checks if the given element has the specified another element as a descendant.
     *
     * @param element the element to check.
     * @param descendant the element to be a descendant of {@code element}.
     * @return {@code true} if given {@code descendant} is actually a descendant of the {@code element}.
     */
    public static boolean hasDescendant(Element element, Element descendant) {
        Node current = descendant;
        while (current.getNodeType() != Node.DOCUMENT_NODE) {
            if (current == element) {
                return true;
            }
            current = current.getParentNode();
        }
        return false;
    }

    public static boolean hasParent(Element element) {
        return element.getParentNode() != null;
    }
    
    public static boolean isOrphan(Element element) {
        return !hasParent(element);
    }

    public static boolean hasSiblingBefore(Element element) {
        Node sibling = element.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
            sibling = sibling.getPreviousSibling();
        }
        return false;
    }

    public static boolean hasSiblingAfter(Element element) {
        Node sibling = element.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
            sibling = sibling.getNextSibling();
        }
        return false;
    }

    public static boolean hasSameTypeBefore(Element element) {
        Node sibling = element.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (isSameType(element, (Element)sibling)) {
                    return true;
                }
            }
            sibling = sibling.getPreviousSibling();
        }
        return false;
    }

    public static boolean hasSameTypeAfter(Element element) {
        Node sibling = element.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (isSameType(element, (Element)sibling)) {
                    return true;
                }
            }
            sibling = sibling.getNextSibling();
        }
        return false;
    }

    public static int countSiblingsBefore(Element element) {
        int count = 0;
        Node sibling = element.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                ++count;
            }
            sibling = sibling.getPreviousSibling();
        }
        return count;
    }

    public static int countSiblingsAfter(Element element) {
        int count = 0;
        Node sibling = element.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                ++count;
            }
            sibling = sibling.getNextSibling();
        }
        return count;
    }

    public static int countSameTypeBefore(Element element) {
        int count = 0;
        Node sibling = element.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (isSameType(element, (Element)sibling)) {
                    ++count;
                }
            }
            sibling = sibling.getPreviousSibling();
        }
        return count;
    }

    public static int countSameTypeAfter(Element element) {
        int count = 0;
        Node sibling = element.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (isSameType(element, (Element)sibling)) {
                    ++count;
                }
            }
            sibling = sibling.getNextSibling();
        }
        return count;
    }

    public static boolean isSameType(Element a, Element b) {
        String namespace = a.getNamespaceURI();
        if (namespace == null) {
            if (b.getNamespaceURI() != null) {
                return false;
            }
        } else {
            if (!namespace.equals(b.getNamespaceURI())) {
                return false;
            }
        }
        return a.getLocalName().equals(b.getLocalName());
    }

    private Elements() {
    }
}
