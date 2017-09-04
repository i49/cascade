package io.github.i49.cascade.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Selector test fixture.
 */
public class Fixture {
    
    private final Element startElement;
    private final String expression;
    private final List<Element> expected;
   
    public Fixture(Document doc, String expression, Function<Element, List<Element>> teacher) {
        this(doc, null, expression, teacher);
    }
    
    /**
     * Constructs this object.
     * 
     * @param doc the XML document.
     * @param startId the starting element to search.
     * @param expression the selector expression.
     * @param teacher the object to supply expected values.
     */
    public Fixture(Document doc, String startId, String expression, Function<Element, List<Element>> teacher) {
        if (startId != null) {
            this.startElement = doc.getElementById(startId.substring(1));
        } else {
            this.startElement = doc.getDocumentElement();
        }
        this.expression = expression;
        this.expected = teacher.apply(this.startElement);
    }
    
    /**
     * Returns the starting element from which to search for elements.
     * 
     * @return the starting element.
     */
    public Element getStartElement() {
        return startElement;
    }
    
    /**
     * Returns the selector expression to test.
     * 
     * @return the selector expression.
     */
    public String getExpression() {
        return expression;
    }
    
    public List<Element> getExpected() {
        return expected;
    }

    public static Function<Element, List<Element>> contains(int... indices) {
        return (Element startElement)->{
            List<Element> expected = null;
            if (indices.length == 0) {
                expected = Collections.emptyList();
            } else {
                List<Element> all = Documents.descentandsOf(startElement);
                expected = new ArrayList<>();
                for (int index: indices) {
                    expected.add(all.get(index));
                }
            }
            return expected;
        };
    }
    
    public static Function<Element, List<Element>> doesNotContain(int... indices) {
        return (Element startElement)->{
            List<Element> expected = null;
            if (indices.length == 0) {
                expected = Documents.descentandsOf(startElement);
            } else {
                List<Element> all = Documents.descentandsOf(startElement);
                List<Element> negation = new ArrayList<>();
                for (int index: indices) {
                    negation.add(all.get(index));
                }
                expected = new ArrayList<>(all);
                expected.removeAll(negation);
            }
            return expected;
        };
    }
    
    public static Function<Fixture, List<Element>> empty() {
        return (Fixture f)->Collections.emptyList();
    }
}
