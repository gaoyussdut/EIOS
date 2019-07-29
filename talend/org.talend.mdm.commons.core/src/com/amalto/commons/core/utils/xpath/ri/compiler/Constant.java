/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amalto.commons.core.utils.xpath.ri.compiler;

import com.amalto.commons.core.utils.xpath.ri.InfoSetUtil;

/**
 * A compile tree element containing a constant number or string.
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public class Constant extends Expression {

    private Object value;

    protected Constant() {
    }

    protected Constant(Object object) {
        this.value = object;
    }

    /**
     * Create a new Constant.
     * @param number constant
     */
    public Constant(Number number) {
        this.value = number;
    }

    /**
     * Create a new Constant.
     * @param string constant
     */
    public Constant(String string) {
        this.value = string;
    }


//    public Object compute(EvalContext context) {
//        return value;
//    }
//
//    public Object computeValue(EvalContext context) {
//        return value;
//    }

    public Object getValue() {
    	return value;
    }

    public Constant clone(boolean deep) {
    	if (deep) return new Constant(this.value);
    	if (this.value instanceof Number) return new Constant(
    		new Double(((Number)this.value).doubleValue())
    	);
    	if (this.value instanceof Number) return new Constant(
    		new String(((String)this.value))
    	);
    	return null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return clone(true);
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Constant)) return false;
        Constant c = (Constant) obj;
        if (! this.value.getClass().equals(c.value.getClass())) return false;
        if (this.value instanceof Number) {
        	return ((Number)this.value).doubleValue() ==  ((Number)c.value).doubleValue();
        }
        if (this.value instanceof String) {
        	return ((String)this.value).equals((String)c.value);
        }
        return false;
    }

	/**
     * Returns false
     * @return false
     */
    public boolean isContextDependent() {
        return false;
    }

    /**
     * Returns false
     * @return false
     */
    public boolean computeContextDependent() {
        return false;
    }

    public String toString() {
        if (value instanceof Number) {
            return InfoSetUtil.stringValue(value);
        }
        return "'" + value + "'";
    }
}
