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

import java.util.ArrayList;

import com.amalto.commons.core.utils.xpath.ri.Compiler;

/**
 * An element of the compile tree representing one of built-in functions
 * like "position()" or "number()".
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 668329 $ $Date: 2008-06-16 16:59:48 -0500 (Mon, 16 Jun 2008) $
 */
public class CoreFunction extends Operation {

//    private static final Double ZERO = new Double(0);
    private int functionCode;

    protected CoreFunction() {
    }

    /**
     * Create a new CoreFunction.
     * @param functionCode int function code
     * @param args argument Expressions
     */
    public CoreFunction(int functionCode, Expression[] args) {
        super(args);
        this.functionCode = functionCode;
    }

    /**
     * Get the function code.
     * @return int function code
     */
    public int getFunctionCode() {
        return functionCode;
    }

    /**
     * Get the name of this function.
     * @return String function name
     */
    public String getFunctionName() {
        switch (functionCode) {
            case Compiler.FUNCTION_LAST :
                return "last";
            case Compiler.FUNCTION_POSITION :
                return "position";
            case Compiler.FUNCTION_COUNT :
                return "count";
            case Compiler.FUNCTION_ID :
                return "id";
            case Compiler.FUNCTION_LOCAL_NAME :
                return "local-name";
            case Compiler.FUNCTION_NAMESPACE_URI :
                return "namespace-uri";
            case Compiler.FUNCTION_NAME :
                return "name";
            case Compiler.FUNCTION_STRING :
                return "string";
            case Compiler.FUNCTION_CONCAT :
                return "concat";
            case Compiler.FUNCTION_STARTS_WITH :
                return "starts-with";
            case Compiler.FUNCTION_CONTAINS :
                return "contains";
            case Compiler.FUNCTION_SUBSTRING_BEFORE :
                return "substring-before";
            case Compiler.FUNCTION_SUBSTRING_AFTER :
                return "substring-after";
            case Compiler.FUNCTION_SUBSTRING :
                return "substring";
            case Compiler.FUNCTION_STRING_LENGTH :
                return "string-length";
            case Compiler.FUNCTION_NORMALIZE_SPACE :
                return "normalize-space";
            case Compiler.FUNCTION_TRANSLATE :
                return "translate";
            case Compiler.FUNCTION_BOOLEAN :
                return "boolean";
            case Compiler.FUNCTION_NOT :
                return "not";
            case Compiler.FUNCTION_TRUE :
                return "true";
            case Compiler.FUNCTION_FALSE :
                return "false";
            case Compiler.FUNCTION_LANG :
                return "lang";
            case Compiler.FUNCTION_NUMBER :
                return "number";
            case Compiler.FUNCTION_SUM :
                return "sum";
            case Compiler.FUNCTION_FLOOR :
                return "floor";
            case Compiler.FUNCTION_CEILING :
                return "ceiling";
            case Compiler.FUNCTION_ROUND :
                return "round";
            case Compiler.FUNCTION_KEY :
                return "key";
            case Compiler.FUNCTION_FORMAT_NUMBER:
                return "format-number";
            default:
                return "unknownFunction" + functionCode + "()";
        }
    }

    /**
     * Convenience method to return the first argument.
     * @return Expression
     */
    public Expression getArg1() {
        return args[0];
    }

    /**
     * Convenience method to return the second argument.
     * @return Expression
     */
    public Expression getArg2() {
        return args[1];
    }

    /**
     * Convenience method to return the third argument.
     * @return Expression
     */
    public Expression getArg3() {
        return args[2];
    }

    /**
     * Return the number of argument Expressions.
     * @return int count
     */
    public int getArgumentCount() {
        if (args == null) {
            return 0;
        }
        return args.length;
    }

    /**
     * Returns true if any argument is context dependent or if
     * the function is last(), position(), boolean(), local-name(),
     * name(), string(), lang(), number().
     * @return boolean
     */
    public boolean computeContextDependent() {
        if (super.computeContextDependent()) {
            return true;
        }

        switch (functionCode) {
            case Compiler.FUNCTION_LAST:
            case Compiler.FUNCTION_POSITION:
                return true;

            case Compiler.FUNCTION_BOOLEAN:
            case Compiler.FUNCTION_LOCAL_NAME:
            case Compiler.FUNCTION_NAME:
            case Compiler.FUNCTION_NAMESPACE_URI:
            case Compiler.FUNCTION_STRING:
            case Compiler.FUNCTION_LANG:
            case Compiler.FUNCTION_NUMBER:
                return args == null || args.length == 0;

            case Compiler.FUNCTION_FORMAT_NUMBER:
                return args != null && args.length == 2;

            case Compiler.FUNCTION_COUNT:
            case Compiler.FUNCTION_ID:
            case Compiler.FUNCTION_CONCAT:
            case Compiler.FUNCTION_STARTS_WITH:
            case Compiler.FUNCTION_CONTAINS:
            case Compiler.FUNCTION_SUBSTRING_BEFORE:
            case Compiler.FUNCTION_SUBSTRING_AFTER:
            case Compiler.FUNCTION_SUBSTRING:
            case Compiler.FUNCTION_STRING_LENGTH:
            case Compiler.FUNCTION_NORMALIZE_SPACE:
            case Compiler.FUNCTION_TRANSLATE:
            case Compiler.FUNCTION_NOT:
            case Compiler.FUNCTION_TRUE:
            case Compiler.FUNCTION_FALSE:
            case Compiler.FUNCTION_SUM:
            case Compiler.FUNCTION_FLOOR:
            case Compiler.FUNCTION_CEILING:
            case Compiler.FUNCTION_ROUND:
            default:
                return false;
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getFunctionName());
        buffer.append('(');
        Expression[] args = getArguments();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    buffer.append(", ");
                }
                buffer.append(args[i]);
            }
        }
        buffer.append(')');
        return buffer.toString();
    }

    public CoreFunction clone(boolean deep) {
    	if (! deep) return new CoreFunction(this.functionCode, this.args);

    	CoreFunction cf = new CoreFunction();
    	cf.functionCode = this.functionCode;

    	//args
    	ArrayList<Expression> ps = new ArrayList<Expression>();
    	if (this.args != null) {
    		for (int i = 0; i < this.args.length; i++) {
    			ps.add(this.args[i].clone(true));
            }
    	}
    	cf.args = ps.toArray(new Expression[ps.size()]);

    	return cf;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return clone(true);
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof CoreFunction)) return false;
        CoreFunction cf = (CoreFunction)obj;

    	if (cf.functionCode != this.functionCode) return false;

    	//args
    	if (this.args != null) {
    		if (cf.args.length != this.args.length) return false;
    		for (int i = 0; i < this.args.length; i++) {
    			if (! cf.args[i].equals(this.args[i])) return false;
            }
    	}

    	return true;
    }

// W
//    /**
//     * Assert <code>count</code> args.
//     * @param count int
//     */
//    private void assertArgCount(int count) {
//        assertArgRange(count, count);
//    }
//
//    /**
//     * Assert at least <code>min</code>/at most <code>max</code> args.
//     * @param min int
//     * @param max int
//     */
//    private void assertArgRange(int min, int max) {
//        int ct = getArgumentCount();
//        if (ct < min || ct > max) {
//            throw new JXPathInvalidSyntaxException(
//                    "Incorrect number of arguments: " + this);
//        }
//    }
}
