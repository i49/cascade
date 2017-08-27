package io.github.i49.cascade.tests.functional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.tests.Documents;

/**
 * Tests for elements which do not exist in the document.
 */
@RunWith(Parameterized.class)
public class OrphanElementTest {
    
    private final String expression;
    private Element startingElement;
	
    public OrphanElementTest(String expression) {
        this.expression = expression;
    }
    
    @Before
    public void setUp() {
        Document doc = Documents.empty();
        this.startingElement = doc.createElement("orphan");
    }
    
    @Parameters(name = "{index}: {0}")
    public static String[] parameters() {
        return new String[] {
                ":root",
                "* *",
                "* > *",
                "* ~ *",
                "* + *",
        };
    }
    
    @Test
    public void shouldNotSelectOrphan() {
        // given
        Selector selector = Selector.compile(this.expression);
        
        // when
        List<Element> selected = selector.select(this.startingElement);

        // then
        assertThat(selected).isNotNull().isEmpty();
    }
}
