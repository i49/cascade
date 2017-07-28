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
 */
public class InvalidSelectorException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private final String expression;
    private final int position;
    
    public InvalidSelectorException(String message, String expression, int position) {
        super(message);
        this.expression = expression;
        this.position = position;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public int getPosition() {
        return position;
    }
}
