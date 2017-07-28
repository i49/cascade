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

import com.github.i49.cascade.api.InvalidSelectorException;
import com.github.i49.cascade.api.Selector;
import com.github.i49.cascade.api.SelectorCompiler;
import com.github.i49.cascade.core.matchers.ClassMatcher;
import com.github.i49.cascade.core.matchers.IdentifierMatcher;
import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.TypeMatcher;
import com.github.i49.cascade.core.matchers.UniversalMatcher;
import com.github.i49.cascade.core.selectors.Collector;
import com.github.i49.cascade.core.selectors.SelectorGroup;
import com.github.i49.cascade.core.selectors.SequenceCollector;

/**
 * Default implementation of {@link SelectorCompiler}.
 */
public class DefaultSelectorCompiler implements SelectorCompiler {
   
    private Tokenizer tokenizer;
    private Token lastToken;
    
    @Override
    public Selector compile(String expression) {
        if (expression == null) {
            throw new NullPointerException("expression must no be null");
        }
        this.tokenizer = new Tokenizer(expression);
        return selectorGroup();
    }
    
    private SelectorGroup selectorGroup() {
        SelectorGroup group = new SelectorGroup();
        Collector collector = sequenceOfSimpleSelectors();
        group.addCollector(collector);
        return group;
    }

    private Token nextToken() {
        Token token = null;
        if (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
        }
        this.lastToken = token;
        return token;
    }
    
    private Collector sequenceOfSimpleSelectors() {
        Matcher composed = null;
        Token token = null;
        int index = 0;
        while ((token = nextToken()) != null) {
            Matcher matcher = null;
            switch (token.getType()) {
            case WILDCARD:
            case IDENT:
            case HASH:
            case CLASS:
                matcher = simpleSelector(token, index++);
                break;
            default:
                lastToken = token;
                break;
            }
            if (matcher == null) {
                break;
            } else {
                if (composed == null) {
                    composed = matcher;
                } else {
                    composed = composed.and(matcher);
                }
           }
        }
        return new SequenceCollector(composed);
    }
    
    private Matcher simpleSelector(Token token, int index) {
        TokenType type = token.getType();
        if (type == TokenType.WILDCARD) {
            if (index > 0) {
                throwException("Invalid ordering of selectors");
            }
            return universalSelector();
        } else if (type == TokenType.IDENT) {
            if (index > 0) {
                throwException("Invalid ordering of selectors");
            }
            return typeSelector(token.getValue());
        } else if (type == TokenType.HASH) {
            return idSelector(token.getValue());
        } else if (type == TokenType.CLASS) {
            return classSelector(token.getValue());
        }
        return null;
    }
    
    private Matcher universalSelector() {
        return UniversalMatcher.getInstance();
    }
    
    private Matcher typeSelector(String selector) {
        return new TypeMatcher(selector);
    }
    
    private Matcher idSelector(String selector) {
        String identifier = selector.substring(1);
        return new IdentifierMatcher(identifier);
    }
    
    private Matcher classSelector(String selector) {
        String className = selector.substring(1);
        return new ClassMatcher(className);
    }
    
    private void throwException(String message) {
        throw new InvalidSelectorException(
                message, tokenizer.getInput(), tokenizer.getLastPosition());  
    }
}

