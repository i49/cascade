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

package com.github.i49.cascade.core.matchers.pseudo;

import static com.github.i49.cascade.core.dom.Elements.hasParent;

import org.w3c.dom.Element;

public abstract class OrdinalPositionMatcher extends FunctionalPseudoClassMatcher {

    private final String expression;
    protected final int a;
    protected final int b;

    protected OrdinalPositionMatcher(int a, int b) {
        this.expression = buildExpression(a, b);
        this.a = a;
        this.b = (b >= 0) ? b : (a - (-b % a));
    }

    protected OrdinalPositionMatcher(Parity parity) {
        this.a = 2;
        this.b = (parity == Parity.EVEN) ? 0 : 1;
        this.expression = parity.name().toLowerCase();
    }

    @Override
    public boolean matches(Element element) {
        if (!hasParent(element)) {
            return false;
        }
        if (a == 0) {
            if (b == 0) {
                return false;
            }
            return countSiblingsAround(element) == b - 1;
        }

        final int count = countSiblingsAround(element);
        if (a > 0) {
            int step = a;
            for (int i = b - 1; i <= count; i += step) {
                if (i == count) {
                    return true;
                }
            }
        } else {
            int step = -a;
            for (int i = b - 1; i >= 0; i -= step) {
                if (i == count) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isNever() {
        return a == 0 && b == 0;
    }

    @Override
    protected String getExpression() {
        return expression;
    }

    /**
     * Counts siblings before or after given element.
     *
     * @param element the element of current interest.
     * @return the number of siblings.
     */
    protected abstract int countSiblingsAround(Element element);

    private static String buildExpression(int a, int b) {
         StringBuilder builder = new StringBuilder();
         if (a == 1) {
             builder.append("n");
         } else if (a == -1) {
             builder.append("-n");
         } else if (a != 0) {
             builder.append(a).append("n");
         }

         if (a == 0) {
             builder.append(b);
         } else if (b > 0) {
             builder.append(" + ").append(b);
         } else if (b < 0) {
             builder.append(" - ").append(-b);
         }
        return builder.toString();
    }
}
