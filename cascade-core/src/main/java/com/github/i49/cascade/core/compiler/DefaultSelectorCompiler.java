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

package com.github.i49.cascade.core.compiler;

import java.util.ArrayList;
import java.util.List;

import com.github.i49.cascade.api.InvalidSelectorException;
import com.github.i49.cascade.api.Selector;
import com.github.i49.cascade.api.SelectorCompiler;
import com.github.i49.cascade.core.matchers.ClassMatcher;
import com.github.i49.cascade.core.matchers.IdentifierMatcher;
import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.TypeMatcher;
import com.github.i49.cascade.core.matchers.UniversalMatcher;
import com.github.i49.cascade.core.selectors.Collector;
import com.github.i49.cascade.core.selectors.DefaultSelectorGroup;
import com.github.i49.cascade.core.selectors.DefaultSingleSelector;
import com.github.i49.cascade.core.selectors.SequenceCollector;

/**
 * Default implementation of {@link SelectorCompiler}.
 */
public class DefaultSelectorCompiler implements SelectorCompiler {
   
    private Tokenizer tokenizer;
    private Token currentToken;
    
    @Override
    public Selector compile(String expression) {
        if (expression == null) {
            throw new NullPointerException("expression must no be null");
        }
        this.tokenizer = new Tokenizer(expression);
        return selectorGroup();
    }
    
    private Selector selectorGroup() {
        List<Collector> collectors = new ArrayList<>();
        
        do {
            Collector collector = sequenceOfSimpleSelectors();
            collectors.add(collector);
        } while (currentToken.getCategory() != TokenCategory.EOI);

        if (collectors.size() == 1) {
            return new DefaultSingleSelector(collectors.get(0));
        } else {
            return new DefaultSelectorGroup(collectors);
        }
    }

    private Token nextToken() {
        this.currentToken = this.tokenizer.nextToken();
        return this.currentToken;
    }
    
    private Collector sequenceOfSimpleSelectors() {
        Matcher composed = null;
        Token token = null;
        int index = 0;
        while ((token = nextToken()) != null) {
            Matcher matcher = parseSimpleSelector(token, index++);
            if (matcher == null) {
                break;
            }
            if (composed == null) {
                composed = matcher;
            } else {
                composed = composed.and(matcher);
            }
        }
        return new SequenceCollector(composed);
    }
    
    private Matcher parseSimpleSelector(Token token, int index) {
        switch (token.getCategory()) {
        case WILDCARD:
        case IDENT:
        case HASH:
            return simpleSelector(token, index++);
        case PERIOD:
            token = nextToken();
            if (token.getCategory() == TokenCategory.IDENT) {
                return classSelector(token.getLexeme());
            }
            throwException("Class name is missing."); 
            break;
        case EOI:
        case COMMA:
        case SPACE:
            break;
        case UNKNOWN:
            throwException("Unknown token found.");
            break;
        default:
            throwException("Unexpected token.");
            break;
        }
        return null;
    }
    
    private Matcher simpleSelector(Token token, int index) {
        TokenCategory type = token.getCategory();
        if (type == TokenCategory.WILDCARD) {
            if (index > 0) {
                throwException("Invalid ordering of selectors");
            }
            return universalSelector();
        } else if (type == TokenCategory.IDENT) {
            if (index > 0) {
                throwException("Invalid ordering of selectors");
            }
            return typeSelector(token.getLexeme());
        } else if (type == TokenCategory.HASH) {
            return idSelector(token.getLexeme().substring(1));
        }
        return null;
    }
    
    private Matcher universalSelector() {
        return UniversalMatcher.getInstance();
    }
    
    private Matcher typeSelector(String selector) {
        return new TypeMatcher(selector);
    }
    
    private Matcher idSelector(String identifier) {
        return new IdentifierMatcher(identifier);
    }
    
    private Matcher classSelector(String className) {
        return new ClassMatcher(className);
    }
    
    private void throwException(String message) {
        throw new InvalidSelectorException(
                message, tokenizer.getInput(), tokenizer.getLastPosition());  
    }
}

