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
import com.github.i49.cascade.core.matchers.AllOfMatcher;
import com.github.i49.cascade.core.matchers.PrefixMatcher;
import com.github.i49.cascade.core.matchers.SubstringMatcher;
import com.github.i49.cascade.core.matchers.SuffixMatcher;
import com.github.i49.cascade.core.matchers.AttributeMatcher;
import com.github.i49.cascade.core.matchers.TypeMatcher;
import com.github.i49.cascade.core.matchers.UniversalMatcher;
import com.github.i49.cascade.core.matchers.pseudo.NegationMatcher;
import com.github.i49.cascade.core.matchers.pseudo.Parity;
import com.github.i49.cascade.core.matchers.pseudo.PseudoClass;
import com.github.i49.cascade.core.matchers.pseudo.PseudoClassMatcherFactory;
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

    private final PseudoClassMatcherFactory pseudoClassMatcherFactory;

    public DefaultSelectorCompiler() {
        this.pseudoClassMatcherFactory = PseudoClassMatcherFactory.create();
    }

    @Override
    public Selector compile(String expression) {
        if (expression == null) {
            throw new NullPointerException("expression must not be null.");
        }
        this.tokenizer = new SelectorTokenizer(expression);
        return parseGroupOfSelectors();
    }

    private Selector parseGroupOfSelectors() {
        List<SingleSelector> selectors = new ArrayList<>();
        do {
            SingleSelector selector = parseAllSequencesInSelector();
            selectors.add(selector);
        } while (currentToken.getCategory() != TokenCategory.EOI);

        if (selectors.size() == 1) {
            return selectors.get(0);
        } else {
            return new DefaultSelectorGroup(selectors);
        }
    }

    private SingleSelector parseAllSequencesInSelector() {
        Sequence first = null;
        Sequence last = null;
        Combinator combinator = null;
        do {
            Matcher matcher = parseSequence();
            if (matcher == null) {
                if (combinator == Combinator.DESCENDANT) {
                    break;
                } else {
                    throw newException(Message.UNEXPECTED_END_OF_INPUT);
                }
            }
            if (first == null) {
                first = new HeadSequence(matcher);
                last = first;
            } else {
                CombinatorSequence sequence = CombinatorSequence.create(combinator, matcher);
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

    private Matcher parseSequence() {
        List<Matcher> matchers = new ArrayList<>();
        int index = 0;
        Token token = nextNonSpaceToken();
        Matcher matcher;
        while ((matcher = parseSimpleSelector(token, index++, false)) != null) {
            matchers.add(matcher);
            token = nextToken();
            if (token.isEndOfSequence()) {
                break;
            }
        }
        if (matchers.isEmpty()) {
            return null;
        }
        return AllOfMatcher.allOf(matchers);
    }

    private Matcher parseSimpleSelector(Token token, int index, boolean nested) {
        switch (token.getCategory()) {
        case ASTERISK:
        case IDENTITY:
            return parseTypeSelector(token, index);
        case HASH:
            return parseIdSelector(token);
        case PERIOD:
            return parseClassSelector();
        case COLON:
            return parsePseudoClass(nested);
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

    /* simple selector parsers */

    private Matcher parseTypeSelector(Token token, int index) {
        TokenCategory type = token.getCategory();
        if (type == TokenCategory.ASTERISK) {
            if (index > 0) {
                throw newException(Message.UNIVERSAL_SELECTOR_POSITION_IS_INVALID);
            }
            return newUniversalSelector();
        } else if (type == TokenCategory.IDENTITY) {
            if (index > 0) {
                throw newException(Message.TYPE_SELECTOR_POSITION_IS_INVALID.with(token.getText()));
            }
            return newTypeSelector(token.getText());
        }
        throw internalError();
    }

    private Matcher parseIdSelector(Token token) {
        return newIdSelector(token.getText());
    }

    private Matcher parseClassSelector() {
        Token token = nextToken();
        if (token.getCategory() == TokenCategory.IDENTITY) {
            return newClassSelector(token.getText());
        }
        throw newException(Message.CLASS_NAME_IS_MISSING);
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

    private Matcher parsePseudoClass(boolean nested) {
        Token token = nextToken();
        if (token == Token.COLON) {
            return parsePseudoElement();
        }
        TokenCategory category = token.getCategory();
        if (category != TokenCategory.IDENTITY && category != TokenCategory.FUNCTION) {
            throw newException(Message.PSEUDO_CLASS_NAME_IS_MISSING);
        }
        String className = token.getText().split("\\(")[0];
        PseudoClass pseudoClass = PseudoClass.byName(className);
        if (pseudoClass == null) {
            throw newException(Message.UNSUPPORTED_PSEUDO_CLASS.with(className));
        }
        if (nested && pseudoClass == PseudoClass.NOT) {
            throw newException(Message.NEGATION_IS_NESTED);
        }
        if (pseudoClass.isFunctional()) {
            if (category == TokenCategory.FUNCTION) {
                return parseFunctionalPseudoClass(pseudoClass);
            } else {
                throw newException(Message.PSEUDO_CLASS_ARGUMENT_IS_MISSING.with(pseudoClass));
            }
        } else {
            return newPseudoClassSelector(pseudoClass);
        }
    }

    private Matcher parsePseudoElement() {
        throw newException(Message.PSEUDO_ELEMENTS_ARE_NOT_SUPPORTED);
    }

    private Matcher parseFunctionalPseudoClass(PseudoClass pseudoClass) {
        switch (pseudoClass) {
        case NTH_CHILD:
        case NTH_LAST_CHILD:
        case NTH_OF_TYPE:
        case NTH_LAST_OF_TYPE:
            return parsePositionalPseudoClass(pseudoClass);
        case NOT:
            return parseNegationPseudoClass();
        default:
            throw newException(Message.UNSUPPORTED_PSEUDO_CLASS.with(pseudoClass));
        }
    }

    private Token getFirstTokenInFunction(PseudoClass pseudoClass) {
        Token token = nextNonSpaceToken();
        if (token == Token.CLOSING_PARENTHESIS) {
            throw newException(Message.PSEUDO_CLASS_ARGUMENT_IS_MISSING.with(pseudoClass));
        }
        return token;
    }

    private Matcher parsePositionalPseudoClass(PseudoClass pseudoClass) {
        Token token = getFirstTokenInFunction(pseudoClass);
        if (token.getCategory() == TokenCategory.IDENTITY) {
            // "odd" or "even"
            Parity parity = Parity.byName(token.getText());
            if (parity != null) {
                return parseParityFunctionalPseudoClass(pseudoClass, parity);
            }
            // for "n"
        }
        return parseOrdinalPseudoClass(pseudoClass, token);
    }

    private Matcher parseParityFunctionalPseudoClass(PseudoClass pseudoClass, Parity parity) {
        Token token = nextNonSpaceToken();
        if (token == Token.CLOSING_PARENTHESIS) {
            return newFunctionalPseudoClassSelector(pseudoClass, parity);
        } else {
            throw unexpectedTokenInFunction(token);
        }
    }

    private Matcher parseOrdinalPseudoClass(PseudoClass pseudoClass, Token token) {
        OrdinalPositionParser parser = new OrdinalPositionParser();
        parser.parse(token);
        return newFunctionalPseudoClassSelector(pseudoClass, parser.a, parser.b);
    }

    private Matcher parseNegationPseudoClass() {
        Token token = getFirstTokenInFunction(PseudoClass.NOT);
        Matcher selector = parseSimpleSelectorInNegation(token);
        if (selector == null) {
            throw unexpectedToken(token);
        }
        token = nextNonSpaceToken();
        if (token != Token.CLOSING_PARENTHESIS) {
            throw unexpectedToken(token);
        }
        return NegationMatcher.negate(selector);
    }

    private Matcher parseSimpleSelectorInNegation(Token token) {
        switch (token.getCategory()) {
        case SPACE:
        case GREATER:
        case PLUS:
        case TILDE:
        case COMMA:
            throw unexpectedToken(token);
        default:
            return parseSimpleSelector(token, 0, true);
        }
    }

    /* selector creators */

    private Matcher newUniversalSelector() {
        return UniversalMatcher.getInstance();
    }

    private Matcher newTypeSelector(String selector) {
        return new TypeMatcher(selector);
    }

    private Matcher newIdSelector(String selector) {
        String identifier = selector.substring(1);
        return new IdentifierMatcher(identifier);
    }

    private Matcher newClassSelector(String className) {
        return new ClassMatcher(className);
    }

    private Matcher newPseudoClassSelector(PseudoClass pseudoClass) {
        Matcher matcher = pseudoClassMatcherFactory.createMatcher(pseudoClass);
        if (matcher == null) {
            throw newException(Message.UNSUPPORTED_PSEUDO_CLASS.with(pseudoClass));
        }
        return matcher;
    }

    private Matcher newFunctionalPseudoClassSelector(PseudoClass pseudoClass, Parity parity) {
        Matcher matcher = pseudoClassMatcherFactory.createMatcher(pseudoClass, parity);
        if (matcher == null) {
            internalError();
        }
        return matcher;
    }

    private Matcher newFunctionalPseudoClassSelector(PseudoClass pseudoClass, int a, int b) {
        Matcher matcher = pseudoClassMatcherFactory.createMatcher(pseudoClass, a, b);
        if (matcher == null) {
            internalError();
        }
        return matcher;
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

    private InvalidSelectorException unexpectedTokenInFunction(Token token) {
        if (token == Token.EOI) {
            return newException(Message.FUNCTION_IS_NOT_CLOSED);
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

    private class OrdinalPositionParser {

        private int a;
        private int b;

        public void parse(Token token) {
            a = b = 0;
            int sign = 1;

            // a or b

            if (token == Token.PLUS) {
                token = nextToken();
            } else if (token == Token.MINUS) {
                sign = -1;
                token = nextToken();
            }

            switch (token.getCategory()) {
            case IDENTITY:
            case DIMENSION:
                if (parseOrdinalPosition(sign, token)) {
                    expectClosingParenthesis();
                    return;
                }
                break;
            case NUMBER:
                b = parseInteger(token.getRawText()) * sign;
                expectClosingParenthesis();
                return;
            default:
                throw unexpectedTokenInFunction(token);
            }

            // after "n"

            token = nextNonSpaceToken();
            if (token == Token.CLOSING_PARENTHESIS) {
                b = 0;
            } else {
                if (token == Token.PLUS) {
                    sign = 1;
                } else if (token == Token.MINUS) {
                    sign = -1;
                } else {
                    throw unexpectedTokenInFunction(token);
                }
                token = nextNonSpaceToken();
                if (token.getCategory() == TokenCategory.NUMBER) {
                    b = parseInteger(token.getRawText()) * sign;
                    expectClosingParenthesis();
                } else {
                    throw unexpectedTokenInFunction(token);
                }
            }
        }

        private boolean parseOrdinalPosition(int sign, Token token) {
            String[] parts = token.getRawText().split("-", 2);
            java.util.regex.Matcher m = Letters.NUMBER_PATTERN.matcher(parts[0]);
            String variable = null;
            if (m.lookingAt()) {
                String numeric = m.group();
                variable = parts[0].substring(numeric.length());
                a = parseInteger(numeric) * sign;
            } else {
                variable = parts[0];
                a = sign;
            }

            if (!variable.equalsIgnoreCase("n")) {
                throw unexpectedToken(token);
            }

            if (parts.length == 1) {
                return false;
            }

            m = Letters.NUMBER_PATTERN.matcher(parts[1]);
            if (m.matches()) {
                b = -parseInteger(parts[1]);
                return true;
            } else {
                throw unexpectedToken(token);
            }
        }

        private void expectClosingParenthesis() {
            Token token = nextNonSpaceToken();
            if (token != Token.CLOSING_PARENTHESIS) {
                throw unexpectedToken(token);
            }
        }

        private int parseInteger(String value) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw newException(Message.NUMBER_IS_NOT_INTEGER.with(value));
            }
        }
    }
}

