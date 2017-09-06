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

package io.github.i49.cascade.core.compiler;

import java.util.ArrayList;
import java.util.List;

import io.github.i49.cascade.api.InvalidSelectorException;
import io.github.i49.cascade.api.Selector;
import io.github.i49.cascade.core.matchers.Matcher;
import io.github.i49.cascade.core.matchers.pseudo.Parity;
import io.github.i49.cascade.core.matchers.pseudo.PseudoClass;
import io.github.i49.cascade.core.matchers.pseudo.PseudoClassMatcherFactory;
import io.github.i49.cascade.core.matchers.simple.AttributeNameMatcher;
import io.github.i49.cascade.core.matchers.simple.ClassMatcher;
import io.github.i49.cascade.core.matchers.simple.DashSeparatedValueMatcher;
import io.github.i49.cascade.core.matchers.simple.ExactMatcher;
import io.github.i49.cascade.core.matchers.simple.IdentifierMatcher;
import io.github.i49.cascade.core.matchers.simple.PrefixMatcher;
import io.github.i49.cascade.core.matchers.simple.SpaceSeparatedValueMatcher;
import io.github.i49.cascade.core.matchers.simple.SubstringMatcher;
import io.github.i49.cascade.core.matchers.simple.SuffixMatcher;
import io.github.i49.cascade.core.matchers.simple.TypeMatcher;
import io.github.i49.cascade.core.matchers.simple.UniversalMatcher;
import io.github.i49.cascade.core.matchers.util.Matchers;
import io.github.i49.cascade.core.message.Message;
import io.github.i49.cascade.core.selectors.Combinator;
import io.github.i49.cascade.core.selectors.DefaultSelectorGroup;
import io.github.i49.cascade.core.selectors.DefaultSingleSelector;
import io.github.i49.cascade.core.selectors.Sequence;
import io.github.i49.cascade.core.selectors.TailSequence;

/**
 * Selector expression parser.
 */
public class SelectorParser {

    private final Tokenizer tokenizer;
    private Token currentToken;
    private Token nextToken;

    private final NamespaceRegistry namespaceRegistry;
    private final PseudoClassMatcherFactory pseudoClassMatcherFactory;

    public SelectorParser(
            String expression,
            NamespaceRegistry namespaceRegistry,
            PseudoClassMatcherFactory pseudoClassMatcherFactory) {

        this.tokenizer = new SelectorTokenizer(expression);
        this.namespaceRegistry = namespaceRegistry;
        this.pseudoClassMatcherFactory = pseudoClassMatcherFactory;
    }

    public Selector parse() {
        List<DefaultSingleSelector> selectors = new ArrayList<>();

        do {
            DefaultSingleSelector selector = parseAllSequencesInSelector();
            selectors.add(selector);
        } while (peekToken().getCategory() != TokenCategory.EOI);

        return DefaultSelectorGroup.of(selectors);
    }

    private DefaultSingleSelector parseAllSequencesInSelector() {
        List<Matcher> matchers = new ArrayList<>();
        List<Combinator> combinators = new ArrayList<>();
        Combinator combinator = null;
        for (;;) {
            Matcher matcher = parseSequence();
            if (matcher == null) {
                if (combinator == Combinator.DESCENDANT) {
                    break;
                } else {
                    throw newException(Message.UNEXPECTED_END_OF_INPUT);
                }
            }
            matchers.add(matcher);
            combinator = parseCombinatorType(nextToken());
            if (combinator != null) {
                combinators.add(combinator);
            } else {
                break;
            }
        }
        return buildSelector(matchers, combinators);
    }

    private DefaultSingleSelector buildSelector(List<Matcher> matchers, List<Combinator> combinators) {
        int index = matchers.size() - 1;
        TailSequence tail = new TailSequence(matchers.get(index));
        Sequence current = tail;
        while (--index >= 0) {
            current = current.prepend(matchers.get(index), combinators.get(index));
        }
        return new DefaultSingleSelector(tail);
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
            token = peekToken();
            if (token.isEndOfSequence()) {
                break;
            } else {
                token = nextToken();
            }
        }
        if (matchers.isEmpty()) {
            return null;
        }
        if (!matchers.get(0).getType().representsType()) {
            matchers.add(0, newUniversalSelector());
        }
        return Matchers.allOf(matchers);
    }

    private Matcher parseSimpleSelector(Token token, int index, boolean nested) {
        switch (token.getCategory()) {
        case ASTERISK:
        case IDENTITY:
        case VERTICAL_BAR:
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
        String prefix = null;
        if (token.is(TokenCategory.VERTICAL_BAR)) {
            prefix = "";
            token = nextToken();
        } else if (peekToken().is(TokenCategory.VERTICAL_BAR)) {
            prefix = token.getText();
            validateNamespacePrefix(prefix);
            // skips vertical bar
            nextToken();
            token = nextToken();
        }
        TokenCategory category = token.getCategory();
        if (category == TokenCategory.ASTERISK) {
            if (index > 0) {
                throw newException(Message.UNIVERSAL_SELECTOR_POSITION_IS_INVALID);
            }
            if (prefix == null) {
                return newUniversalSelector();
            } else {
                return newUniversalSelector(prefix);
            }
        } else if (category == TokenCategory.IDENTITY) {
            if (index > 0) {
                throw newException(Message.TYPE_SELECTOR_POSITION_IS_INVALID.with(token.getText()));
            }
            if (prefix == null) {
                return newTypeSelector(token.getText());
            } else {
                return newTypeSelector(prefix, token.getText());
            }
        } else {
            throw unexpectedToken(token);
        }
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
        String prefix = null;
        Token token = nextNonSpaceToken();
        if (token.is(TokenCategory.VERTICAL_BAR)) {
            prefix = "";
            token = nextToken();
        } else if (token.is(TokenCategory.ASTERISK)) {
            prefix = token.getText(); // *
            token = nextToken();
            if (token.is(TokenCategory.VERTICAL_BAR)) {
                token = nextToken();
            } else {
                throw newException(Message.NAMESPACE_SEPARATOR_IS_MISSING);
            }
        } else if (token.is(TokenCategory.IDENTITY)) {
            if (peekToken().is(TokenCategory.VERTICAL_BAR)) {
                prefix = token.getText();
                validateNamespacePrefix(prefix);
                // skips vertical bar
                nextToken();
                token = nextToken();
            }
        }
        if (token.getCategory() != TokenCategory.IDENTITY) {
            throw newException(Message.ATTRIBUTE_NAME_IS_MISSING);
        }
        String localName = token.getRawText();
        AttributeNameMatcher nameMatcher = (prefix == null) ?
                        newAttributeSelector(localName) :
                        newAttributeSelector(prefix, localName);
        token = nextNonSpaceToken();
        if (token.is(TokenCategory.CLOSING_BRACKET)) {
            return nameMatcher;
        } else {
            switch (token.getCategory()) {
            case EXACT_MATCH:
            case INCLUDES:
            case DASH_MATCH:
            case PREFIX_MATCH:
            case SUFFIX_MATCH:
            case SUBSTRING_MATCH:
                return parseAttributeValueSelector(nameMatcher, token);
            default:
                throw unexpectedToken(token);
            }
        }
    }

    private Matcher parseAttributeValueSelector(AttributeNameMatcher nameMatcher, Token operator) {
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
        if (!token.is(TokenCategory.CLOSING_BRACKET)) {
            throw unexpectedToken(token);
        }
        switch (operator.getCategory()) {
        case EXACT_MATCH:
            return new ExactMatcher(nameMatcher, value);
        case INCLUDES:
            return new SpaceSeparatedValueMatcher(nameMatcher, value);
        case DASH_MATCH:
            return new DashSeparatedValueMatcher(nameMatcher, value);
        case PREFIX_MATCH:
            return new PrefixMatcher(nameMatcher, value);
        case SUFFIX_MATCH:
            return new SuffixMatcher(nameMatcher, value);
        case SUBSTRING_MATCH:
            return new SubstringMatcher(nameMatcher, value);
        default:
            throw internalError();
        }
    }

    private Matcher parsePseudoClass(boolean nested) {
        Token token = nextToken();
        if (token.is(TokenCategory.COLON)) {
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
        if (token.is(TokenCategory.CLOSING_PARENTHESIS)) {
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
        if (token.is(TokenCategory.CLOSING_PARENTHESIS)) {
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
        if (!token.is(TokenCategory.CLOSING_PARENTHESIS)) {
            throw unexpectedToken(token);
        }
        return Matchers.negate(selector);
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
        UniversalMatcher matcher = UniversalMatcher.get();
        if (namespaceRegistry.hasDefault()) {
            matcher = matcher.withNamespace(namespaceRegistry.getDefault());
        }
        return matcher;
    }

    /**
     * Creates a new universal selector matcher.
     *
     * @param prefix the prefix representing a namespace.
     * @return newly created matcher.
     */
    private Matcher newUniversalSelector(String prefix) {
        UniversalMatcher matcher = UniversalMatcher.get();
        if (prefix.isEmpty()) {
            matcher = matcher.withoutNamespace();
        } else if (prefix.equals("*")) {
            matcher = matcher.anyNamespace();
        } else {
            matcher = matcher.withNamespace(prefix, namespaceRegistry.lookUp(prefix));
        }
        return matcher;
    }

    private Matcher newTypeSelector(String selector) {
        TypeMatcher matcher = new TypeMatcher(selector);
        if (namespaceRegistry.hasDefault()) {
            matcher = matcher.withNamespace(namespaceRegistry.getDefault());
        }
        return matcher;
    }

    private Matcher newTypeSelector(String prefix, String selector) {
        TypeMatcher matcher = new TypeMatcher(selector);
        if (prefix.isEmpty()) {
            matcher = matcher.withoutNamespace();
        } else if (prefix.equals("*")) {
            matcher = matcher.anyNamespace();
        } else {
            matcher = matcher.withNamespace(prefix, namespaceRegistry.lookUp(prefix));
        }
        return matcher;
    }

    private Matcher newIdSelector(String selector) {
        String identifier = selector.substring(1);
        return new IdentifierMatcher(identifier);
    }

    private Matcher newClassSelector(String className) {
        return new ClassMatcher(className);
    }

    private AttributeNameMatcher newAttributeSelector(String localName) {
        return new AttributeNameMatcher(localName);
    }

    private AttributeNameMatcher newAttributeSelector(String prefix, String localName) {
        AttributeNameMatcher matcher = new AttributeNameMatcher(localName);
        if (prefix.isEmpty()) {
            matcher = matcher.withoutNamespace();
        } else if (prefix.equals("*")) {
            matcher = matcher.anyNamespace();
        } else {
            matcher = matcher.withNamespace(prefix, namespaceRegistry.lookUp(prefix));
        }
        return matcher;
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

    private void validateNamespacePrefix(String prefix) {
        if (prefix.equals("*") || namespaceRegistry.hasPrefix(prefix)) {
            return;
        }
        throw newException(Message.NAMESPACE_PREFIX_IS_NOT_DECLARED.with(prefix));
    }

    private Token peekToken() {
        if (this.nextToken == null) {
            this.nextToken = this.tokenizer.nextToken();
        }
        return this.nextToken;
    }

    private Token nextToken() {
        if (this.nextToken == null) {
            peekToken();
        }
        Token token = this.nextToken;
        this.currentToken = token;
        this.nextToken = null;
        return token;
    }

    private Token nextNonSpaceToken() {
        Token token = nextToken();
        if (token.is(TokenCategory.SPACE)) {
            token = nextToken();
        }
        return token;
    }

    private InvalidSelectorException unexpectedToken(Token token) {
        // This is an instance of Message or String.
        Object message = null; 
        switch (token.getCategory()) {
        case EOI:
            message = Message.UNEXPECTED_END_OF_INPUT;
            break;
        case UNKNOWN:
            message = Message.UNKNOWN_TOKEN;
            break;
        case SPACE:
            message = Message.UNEXPECTED_WHITESPACE;
            break;
        case INVALID_COMMENT:
            message = Message.COMMENT_IS_NOT_CLOSED;
            break;
        default:
            message = Message.UNEXPECTED_TOKEN.with(token.getRawText());
            break;
        }
        return newException(message);
    }

    private InvalidSelectorException unexpectedTokenInFunction(Token token) {
        if (token.is(TokenCategory.EOI)) {
            return newException(Message.FUNCTION_IS_NOT_CLOSED);
        }
        return unexpectedToken(token);
    }

    private InvalidSelectorException newException(Object message) {
        return new InvalidSelectorException(
                message.toString(),
                tokenizer.getInput(),
                this.currentToken.getPosition());
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

            if (token.is(TokenCategory.PLUS)) {
                token = nextToken();
            } else if (token.is(TokenCategory.MINUS)) {
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
            if (token.is(TokenCategory.CLOSING_PARENTHESIS)) {
                b = 0;
            } else {
                if (token.is(TokenCategory.PLUS)) {
                    sign = 1;
                } else if (token.is(TokenCategory.MINUS)) {
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
            if (!token.is(TokenCategory.CLOSING_PARENTHESIS)) {
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
