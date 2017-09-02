# Cascade
[![Apache 2.0 License](https://img.shields.io/:license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0
) [![Build Status](https://travis-ci.org/i49/cascade.svg?branch=master)](https://travis-ci.org/i49/cascade) [![Maven Central](https://img.shields.io/maven-central/v/io.github.i49/cascade-api.svg)](https://search.maven.org/#artifactdetails%7Cio.github.i49%7Ccascade-api%7C3.1.0%7Cjar)  [![Javadocs](https://www.javadoc.io/badge/io.github.i49/cascade-api.svg?color=blue)](https://www.javadoc.io/doc/io.github.i49/cascade-api)

Cascade is a pure Java implementation of CSS Selectors operating on W3C DOM.

## How to Use the API

```java
Selector selector = Selector.compile("div p");
Document doc = ... /* doc is an instance of org.w3c.dom.Document */
List<Element> selected = selector.select(doc.getDocumentElement());
```

## Features

* Compliant with [W3C Selectors Level 3](http://www.w3.org/TR/css3-selectors/).
* Operating on standard W3C DOM interface defined in `org.w3c.dom` package.
* Simple and intuitive API.
* Optimization is performed when selectors are compiled.
* Supports namespace.
* Compatible with documents provided by `javafx.scene.web.WebEngine`.
* Passed 700+ test cases.

For more detail, please see [API documentation](https://www.javadoc.io/doc/io.github.i49/cascade-api)

## Prerequisites

* Java 8 or higher.

The library is available in the Maven central repository.
Add 2 dependencies to your pom.xml before using it as follows:
```xml
<dependency>
  <groupId>io.github.i49</groupId>
  <artifactId>cascade-api</artifactId>
  <version>3.1.0</version>
</dependency>
<dependency>
  <groupId>io.github.i49</groupId>
  <artifactId>cascade-core</artifactId>
  <version>3.1.0</version>
  <scope>runtime</scope>
</dependency>
```

## Supported Selectors
### Simple Selectors
Selector                             | Example
-------------------------------------|--------------------
Universal selector                   | \*       
Type selector                        | h1
ID selector                          | \#notice
Class selector                       | .example
Attribute selector (presence)        | [title]
Attribute selector (exact match)     | [class="example"]
Attribute selector (space-separated) | [class~="example"]
Attribute selector (dash-separated)  | [hreflang&#124;="en"]
Attribute selector (prefix)          | [type^="image/"]
Attribute selector (suffix)          | [href$=".html"]
Attribute selector (substring)       | [title*="hello"]

### Pseudo-classes
Selector                             | Example
-------------------------------------|--------------------
:root                                |:root
:nth-child                           |:nth-child(2n + 1) <br> :nth-child(odd)
:nth-last-child                      |:nth-last-child(2n) <br> :nth-last-child(even)
:nth-of-type                         |:nth-of-type(2n + 1) <br> :nth-of-type(odd)
:nth-last-of-type                    |:nth-last-of-type(2n) <br> :nth-last-of-type(even)
:first-child                         |:first-child
:last-child                          |:last-child
:first-of-type                       |:first-of-type
:last-of-type                        |:last-of-type
:only-child                          |:only-child
:only-of-type                        |:only-of-type
:empty                               |:empty
:not                                 |:not(.example)

### Combinators
Selector                             | Example
-------------------------------------|--------------------
Descendant combinator                | h1 em       
Child combinator                     | body > p       
Adjacent sibling combinator          | h1 + h2       
General sibling combinator           | h1 ~ pre

Pseudo-elements are not supported.

## Namespace Support
Element and attribute names can be qualified with namespaces.

```java
SelectorCompiler compiler = SelectorCompiler.create()
        .withNamespace("ns", "http://www.w3.org/2000/svg");
Selector selector = compiler.compile("ns|circle");
Document doc = ... /* doc is an instance of org.w3c.dom.Document */
List<Element> selected = selector.select(doc.getDocumentElement());
```
