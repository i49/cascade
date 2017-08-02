# Cascade
[![Apache 2.0 License](https://img.shields.io/:license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0
)

Cascade is a pure Java implementation of CSS Selector operating on W3C DOM.

## How to use API

```java
Selector selector = Selector.compile("div p");
Document doc = ... /* doc is an instance of org.w3c.dom.Document */
Set<Element> selected = selector.select(doc.getDocumentElement());
```
