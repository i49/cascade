package io.github.i49.cascade.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Selector test fixture.
 */
public class Fixture {
    
    private final Element startElement;
    private final String expression;
    private final ElementMatcher matcher;
   
    public Fixture(Document doc, String expression, Function<Fixture, ElementMatcher> matcherFactory) {
        this(doc, null, expression, matcherFactory);
    }
    
    public Fixture(Document doc, String startId, String expression, Function<Fixture, ElementMatcher> matcherFactory) {
        if (startId != null) {
            this.startElement = doc.getElementById(startId.substring(1));
        } else {
            this.startElement = doc.getDocumentElement();
        }
        this.expression = expression;
        this.matcher = matcherFactory.apply(this);
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
    
    /**
     * Returns the custom matcher for asserting on the selected elements.
     * 
     * @return the custom matcher.
     */
    public ElementMatcher getMatcher() {
        return matcher;
    }

    public static Function<Fixture, ElementMatcher> contains(int... indices) {
        return (Fixture f)->{
            List<Element> expected = null;
            if (indices.length == 0) {
                expected = Collections.emptyList();
            } else {
                List<Element> all = Documents.descentandsOf(f.startElement);
                expected = new ArrayList<>();
                for (int index: indices) {
                    expected.add(all.get(index));
                }
            }
            return new ElementMatcher(expected);
        };
    }
    
    public static Function<Fixture, ElementMatcher> doesNotContain(int... indices) {
        return (Fixture f)->{
            List<Element> expected = null;
            if (indices.length == 0) {
                expected = Documents.descentandsOf(f.startElement);
            } else {
                List<Element> all = Documents.descentandsOf(f.startElement);
                List<Element> negation = new ArrayList<>();
                for (int index: indices) {
                    negation.add(all.get(index));
                }
                expected = new ArrayList<>(all);
                expected.removeAll(negation);
            }
            return new ElementMatcher(expected);
        };
    }
    
    public static Function<Fixture, ElementMatcher> empty() {
        return (Fixture f)->new ElementMatcher(Collections.emptyList());
    }
    
    public static class ElementMatcher extends BaseMatcher<List<Element>> {
        
        private final List<Element> expected;
        
        public ElementMatcher(List<Element> expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object actual) {
            return expected.equals(actual);
        }

        @Override
        public void describeTo(Description desc) {
            desc.appendValue(expected);
        }
    }
}
