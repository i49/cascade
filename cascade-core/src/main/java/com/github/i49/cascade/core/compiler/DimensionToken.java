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

import java.util.regex.Matcher;

/**
 *
 */
public class DimensionToken extends NumberToken {

    private final String numericPart;
    private final String identityPart;

    protected DimensionToken(String rawValue) {
        super(TokenCategory.DIMENSION, rawValue);
        Matcher m = Letters.NUMBER_PATTERN.matcher(getRawText());
        if (m.lookingAt()) {
            this.numericPart = m.group();
        } else {
            this.numericPart =  "";
        }
        this.identityPart = rawValue.substring(this.numericPart.length());
    }

    public String getIdentityPart() {
        return identityPart;
    }

    @Override
    public boolean isIntegral() {
        return getNumericPart().indexOf('.') < 0;
    }

    @Override
    public int intValue() {
        return Integer.parseInt(getNumericPart());
    }

    @Override
    public String getNumericPart() {
        return numericPart;
    }
}
