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

package com.github.i49.cascade.api;

/**
 * Exception thrown if the given expression was invalid.
 */
public class InvalidSelectorExeption extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private final String expression;
    private final int position;
    
    /**
     * Constructs this exception.
     * 
     * @param message the messages for the exception.
     * @param expression the input expression.
     * @param position the position in the expression.
     */
    public InvalidSelectorExeption(String message, String expression, int position) {
        super(message);
        this.expression = expression;
        this.position = position;
    }
    
    /**
     * Returns the expression which caused a problem.
     * 
     * @return the expression. 
     */
    public String getExpression() {
        return expression;
    }
    
    /**
     * Returns the position in the expression where the problem was found.
     * 
     * @return the position in the expression.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Returns the detail message string of this exception.
     * 
     * @return the detail message string of this exception instance, which never be {@code null}. 
     */
    @Override
    public String getMessage() {
        StringBuilder b = new StringBuilder();
        b.append(super.getMessage()).append("\n");
        b.append(getExpression()).append("\n");
        int spaces = getPosition();
        while (--spaces > 0) {
            b.append(" ");
        }
        b.append("^");
        return b.toString();
    }
}
