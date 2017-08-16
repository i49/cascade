# Cascade
[![Apache 2.0 License](https://img.shields.io/:license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0
)

Cascade is a pure Java implementation of CSS Selectors operating on W3C DOM.

## How to Use the API

```java
Selector selector = Selector.compile("div p");
Document doc = ... /* doc is an instance of org.w3c.dom.Document */
List<Element> selected = selector.select(doc.getDocumentElement());
```
## Features

* Compliant with [W3C Selectors Level 3](http://www.w3.org/TR/css3-selectors/).
* Operating on standard W3C DOM interface in _org.w3c.dom_ package.
* Intuitive and clean API.
* Optimization is performed when selectors are compiled.
* Passed 300+ test cases.

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
