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
import com.github.i49.cascade.api.SingleSelector;
import com.github.i49.cascade.core.matchers.ClassMatcher;
import com.github.i49.cascade.core.matchers.DashMatcher;
import com.github.i49.cascade.core.matchers.ExactMatcher;
import com.github.i49.cascade.core.matchers.IdentifierMatcher;
import com.github.i49.cascade.core.matchers.IncludeMatcher;
import com.github.i49.cascade.core.matchers.Matcher;
import com.github.i49.cascade.core.matchers.MatcherList;
import com.github.i49.cascade.core.matchers.PrefixMatcher;
import com.github.i49.cascade.core.matchers.SubstringMatcher;
import com.github.i49.cascade.core.matchers.SuffixMatcher;
import com.github.i49.cascade.core.matchers.AttributeMatcher;
import com.github.i49.cascade.core.matchers.TypeMatcher;
import com.github.i49.cascade.core.matchers.UniversalMatcher;
import com.github.i49.cascade.core.matchers.pseudo.PseudoClassMatcher;
import com.github.i49.cascade.core.selectors.Combinator;
import com.github.i49.cascade.core.selectors.CombinatorSequence;
import com.github.i49.cascade.core.selectors.DefaultSelectorGroup;
import com.github.i49.cascade.core.selectors.DefaultSingleSelector;
import com.github.i49.cascade.core.selectors.Sequence;
import com.github.i49.cascade.core.selectors.HeadSequence;

/**
 * Default implementation of {@link SelectorCompiler}.
 */
public class DefaultSelectorCompiler implements SelectorCompiler {

    private Tokenizer tokenizer;
    private Token currentToken;

    @Override
    public Selector compile(String expression) {
        if (expression == null) {
            throw new NullPointerException("expression must not be null.");
        }
        this.tokenizer = new SelectorTokenizer(expression);
        return selectorGroup();
    }

    private Selector selectorGroup() {
        List<SingleSelector> selectors = new ArrayList<>();

        do {
            SingleSelector selector = singleSelector();
            selectors.add(selector);
        } while (currentToken.getCategory() != TokenCategory.EOI);

        if (selectors.size() == 1) {
            return selectors.get(0);
        } else {
            return new DefaultSelectorGroup(selectors);
        }
    }

    private SingleSelector singleSelector() {
        Sequence first = null;
        Sequence last = null;
        Combinator combinator = null;
        do {
            MatcherList matchers = sequence();
            if (matchers.isEmpty()) {
                throw newException(Message.UNEXPECTED_END_OF_INPUT);
            }
            if (first == null) {
                first = new HeadSequence(matchers);
                last = first;
            } else {
                CombinatorSequence sequence = CombinatorSequence.create(combinator, matchers);
                last.combine(sequence);
                last = sequence;
            }
            combinator = parseCombinatorType(currentToken);
        } while (combinator != null);

        return new DefaultSingleSelector(first);
    }

    /**
     * Returns the combinator type of the given token.
     *
     * @param token the token to check.
     * @return {@code null} if given token is not a combinator.
     */
    private Combinator parseCombinatorType(Token token) {
        switch (token.getCategory()) {
        case SPACE:
            return Combinator.DESCENDANT;
        case GREATER:
            return Combinator.CHILD;
        case PLUS:
            return Combinator.ADJACENT;
        case TILDE:
            return Combinator.SIBLING;
        default:
            return null;
        }
    }

    private MatcherList sequence() {
        MatcherList matchers = new MatcherList();
        int index = 0;
        Token token = nextNonSpaceToken();
        for (;;) {
            Matcher matcher = parseSimpleSelector(token, index++);
            if (matcher == null) {
                break;
            }
            matchers.add(matcher);
            token = nextToken();
            if (isEndOfSequence(token)) {
                break;
            }
        }
        return matchers;
    }

    private static boolean isEndOfSequence(Token token) {
        return (token == Token.SPACE ||
                token == Token.GREATER ||
                token == Token.PLUS ||
                token == Token.TILDE ||
                token == Token.COMMA ||
                token == Token.EOI);
    }

    private Matcher parseSimpleSelector(Token token, int index) {
        switch (token.getCategory()) {
        case ASTERISK:
        case IDENTITY:
        case HASH:
            return simpleSelector(token, index++);
        case PERIOD:
            token = nextToken();
            if (token.getCategory() == TokenCategory.IDENTITY) {
                return classSelector(token.getText());
            }
            throw newException(Message.CLASS_NAME_IS_MISSING);
        case COLON:
            token = nextToken();
            if (token == Token.COLON) {
                throw newException(Message.PSEUDO_ELEMENTS_ARE_NOT_SUPPORTED);
            } else if (token.getCategory() == TokenCategory.IDENTITY) {
                return pseudoClassSelector(token.getText());
            }
            throw newException(Message.PSEUDO_CLASS_NAME_IS_MISSING);
        case OPENING_BRACKET:
            return parseAttributeSelector();
        case SPACE:
        case GREATER:
        case PLUS:
        case TILDE:
            throw newException(Message.COMBINATOR_WITHOUT_PRECEDING_SEQUENCE.with(token.getRawText()));
        case COMMA:
            throw newException(Message.UNEXPECTED_END_OF_SELECTOR);
        case EOI:
            break;
        case UNKNOWN:
        default:
            throw unexpectedToken(token);
        }
        return null;
    }

    private Matcher simpleSelector(Token token, int index) {
        TokenCategory type = token.getCategory();
        if (type == TokenCategory.ASTERISK) {
            if (index > 0) {
                throw newException(Message.UNIVERSAL_SELECTOR_POSITION_IS_INVALID);
            }
            return universalSelector();
        } else if (type == TokenCategory.IDENTITY) {
            if (index > 0) {
                throw newException(Message.TYPE_SELECTOR_POSITION_IS_INVALID.with(token.getText()));
            }
            return typeSelector(token.getText());
        } else if (type == TokenCategory.HASH) {
            return idSelector(token.getText());
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

    private Matcher classSelector(String className) {
        return new ClassMatcher(className);
    }

    private Matcher pseudoClassSelector(String className) {
        Matcher matcher = PseudoClassMatcher.byName(className);
        if (matcher == null) {
            String selector = ":" + className;
            throw newException(Message.UNSUPPORTED_PSEUDO_CLASS.with(selector));
        }
        return matcher;
    }

    private Matcher parseAttributeSelector() {
        Token token = nextNonSpaceToken();
        if (token.getCategory() != TokenCategory.IDENTITY) {
            throw newException(Message.ATTRIBUTE_NAME_IS_MISSING);
        }
        String name = token.getRawText();
        token = nextNonSpaceToken();
        if (token == Token.CLOSING_BRACKET) {
            return new AttributeMatcher(name);
        } else {
            switch (token.getCategory()) {
            case EXACT_MATCH:
            case INCLUDES:
            case DASH_MATCH:
            case PREFIX_MATCH:
            case SUFFIX_MATCH:
            case SUBSTRING_MATCH:
                return parseAttributeValueSelector(name, token);
            default:
                throw unexpectedToken(token);
            }
        }
    }

    private Matcher parseAttributeValueSelector(String name, Token operator) {
        Token token = nextNonSpaceToken();
        switch (token.getCategory()) {
        case IDENTITY:
        case STRING:
            break;
        case INVALID_STRING:
            throw newException(Message.STRING_IS_NOT_CLOSED);
        default:
            throw newException(Message.ATTRIBUTE_VALUE_IS_MISSING);
        }
        String value = token.getValue();
        token = nextNonSpaceToken();
        if (token != Token.CLOSING_BRACKET) {
            throw unexpectedToken(token);
        }
        switch (operator.getCategory()) {
        case EXACT_MATCH:
            return new ExactMatcher(name, value);
        case INCLUDES:
            return new IncludeMatcher(name, value);
        case DASH_MATCH:
            return new DashMatcher(name, value);
        case PREFIX_MATCH:
            return new PrefixMatcher(name, value);
        case SUFFIX_MATCH:
            return new SuffixMatcher(name, value);
        case SUBSTRING_MATCH:
            return new SubstringMatcher(name, value);
        default:
            throw internalError();
        }
    }

    private Token nextToken() {
        this.currentToken = this.tokenizer.nextToken();
        return this.currentToken;
    }

    private Token nextNonSpaceToken() {
        Token token = nextToken();
        if (token == Token.SPACE) {
            token = nextToken();
        }
        return token;
    }

    private InvalidSelectorException unexpectedToken(Token token) {
        if (token == Token.EOI) {
            return newException(Message.UNEXPECTED_END_OF_INPUT);
        } else if (token == Token.UNKNOWN) {
            return newException(Message.UNKNOWN_TOKEN);
        } else {
            return newException(Message.UNEXPECTED_TOKEN.with(token.getRawText()));
        }
    }

    private InvalidSelectorException newException(Object message) {
        return new InvalidSelectorException(
                message.toString(),
                tokenizer.getInput(),
                tokenizer.getCurrentIndex());
    }

    private RuntimeException internalError() {
        return new RuntimeException("Internal error.");
    }
}

