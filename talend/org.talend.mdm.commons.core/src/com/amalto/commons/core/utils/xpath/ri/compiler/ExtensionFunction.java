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

import com.amalto.commons.core.utils.xpath.ri.QName;

/**
 * Represents an element of the parse tree representing an extension function
 * call.
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public class ExtensionFunction extends Operation {

    private QName functionName;

    protected ExtensionFunction() {
    	super();
    }

    /**
     * Create a new ExtensionFunction.
     * @param functionName name of the function
     * @param args Expression[] of function args
     */
    public ExtensionFunction(QName functionName, Expression[] args) {
        super(args);
        this.functionName = functionName;
    }

    /**
     * Get the function name
     * @return QName
     */
    public QName getFunctionName() {
        return functionName;
    }

    /**
     * An extension function gets the current context, therefore it MAY be
     * context dependent.
     * @return true
     */
    public boolean computeContextDependent() {
        return true;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(functionName);
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


    public ExtensionFunction clone(boolean deep) {
    	if (! deep) return new ExtensionFunction(this.functionName, this.args);

    	ExtensionFunction ef = new ExtensionFunction();
    	ef.functionName = new QName(
    		this.getFunctionName().getPrefix() == null ? null : new String(this.getFunctionName().getPrefix()),
    		this.getFunctionName().getName() == null ? null : new String(this.getFunctionName().getName())
    	);

    	//args
    	ArrayList<Expression> ps = new ArrayList<Expression>();
    	if (this.args != null) {
    		for (int i = 0; i < this.args.length; i++) {
    			ps.add(this.args[i].clone(true));
            }
    	}
    	ef.args = ps.toArray(new Expression[ps.size()]);

    	return ef;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return clone(true);
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof ExtensionFunction)) return false;
        ExtensionFunction ep = (ExtensionFunction)obj;

    	if (! ep.functionName.equals(this.functionName)) return false;

    	//args
    	if (this.args != null) {
    		if (ep.args.length != this.args.length) return false;
    		for (int i = 0; i < this.args.length; i++) {
    			if (! ep.args[i].equals(this.args[i])) return false;
            }
    	}

    	return true;
    }


}
