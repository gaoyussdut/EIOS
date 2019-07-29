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
import java.util.Arrays;

import com.amalto.commons.core.utils.xpath.ri.Compiler;

/**
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public class Step {
    private int axis;
    private NodeTest nodeTest;
    private Expression[] predicates;

    protected Step() {
    }

    /**
     * Create a new Step.
     * @param axis axis code
     * @param nodeTest step test
     * @param predicates predicate expressions
     */
    public Step(int axis, NodeTest nodeTest, Expression[] predicates) {
        this.axis = axis;
        this.nodeTest = nodeTest;
        this.predicates = predicates;
    }

    /**
     * Get the axis code.
     * @return int
     */
    public int getAxis() {
        return axis;
    }

    /**
     * Sets the axis code
     * @param axis
     */
    public void setAxis(int axis) {
    	this.axis = axis;
    }

    /**
     * Get the step test.
     * @return NodeTest
     */
    public NodeTest getNodeTest() {
        return nodeTest;
    }

    /**
     * Set the step Test
     * @param nodeTest
     */
    public void setNodeTest(NodeTest nodeTest) {
    	this.nodeTest = nodeTest;
    }

    /**
     * Get the predicates.
     * @return Expression[]
     */
    public Expression[] getPredicates() {
        return predicates == null ? new Expression[0]: predicates;
    }

    /**
     * Set the predicates
     * @param predicates
     */
    public void setPredicates(Expression[] predicates) {
    	this.predicates = predicates;
    }

	/**
     * Add a predicate to this step at the end
     * @param predicate
     * @return
     */
    public Step addPredicate(Expression predicate) {
    	return addPredicate(-1, predicate);
    }

    /**
     * Add a predicate to this Step
     * @param index
     * 		Position of the predicate; a negative value means at the end
     * @param step
     * @return
     * 		This path
     */
    public Step addPredicate(int index, Expression predicate) {
    	ArrayList<Expression> predicatesList = new ArrayList<Expression>();
    	if (predicates != null) {
    		predicatesList.addAll(Arrays.asList(predicates));
    	}
    	if (index <0 )
    		predicatesList.add(predicate);
    	else
    		predicatesList.add(index, predicate);
    	this.predicates = predicatesList.toArray(new Expression[predicatesList.size()]);
    	return this;
    }

    public Step clone(boolean deep) {
    	if (! deep) return new Step(this.axis, this.nodeTest, this.predicates);
    	Step step = new Step();
    	step.axis = this.axis;
    	step.nodeTest = this.nodeTest.clone(true);
    	//predicates
    	ArrayList<Expression> ps = new ArrayList<Expression>();
    	if (this.predicates != null) {
    		for (int i = 0; i < this.predicates.length; i++) {
    			ps.add(this.predicates[i].clone(true));
            }
    	}
    	step.predicates = ps.toArray(new Expression[ps.size()]);
    	return step;
    }

    @Override
    public boolean equals(Object obj) {
    	if (! (obj instanceof Step)) return false;
    	Step s = (Step)obj;

    	if (s.axis != this.axis) return false;
    	if (! s.nodeTest.equals(this.nodeTest)) return false;

    	//args
    	if (this.predicates != null) {
    		if (s.predicates.length != this.predicates.length) return false;
    		for (int i = 0; i < this.predicates.length; i++) {
    			if (! s.predicates[i].equals(this.predicates[i])) return false;
            }
    	}

    	return true;
    }

    /**
     * Learn whether this step contains any predicate that is context dependent.
     * @return boolean
     */
    public boolean isContextDependent() {
        if (predicates != null) {
            for (int i = 0; i < predicates.length; i++) {
                if (predicates[i].isContextDependent()) {
                    return true;
                }
            }
        }
        return false;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        int axis = getAxis();
        if (axis == Compiler.AXIS_CHILD) {
            buffer.append(nodeTest);
        }
        else if (axis == Compiler.AXIS_ATTRIBUTE) {
            buffer.append('@');
            buffer.append(nodeTest);
        }
        else if (axis == Compiler.AXIS_SELF
                && nodeTest instanceof NodeTypeTest
                && ((NodeTypeTest) nodeTest).getNodeType()
                    == Compiler.NODE_TYPE_NODE) {
            buffer.append(".");
        }
        else if (axis == Compiler.AXIS_PARENT
                && nodeTest instanceof NodeTypeTest
                && ((NodeTypeTest) nodeTest).getNodeType()
                    == Compiler.NODE_TYPE_NODE) {
            buffer.append("..");
        }
        else if (axis == Compiler.AXIS_DESCENDANT_OR_SELF
                && nodeTest instanceof NodeTypeTest
                && ((NodeTypeTest) nodeTest).getNodeType()
                    == Compiler.NODE_TYPE_NODE
                && (predicates == null || predicates.length == 0)) {
            buffer.append("");
        }
        else {
            buffer.append(axisToString(axis));
            buffer.append("::");
            buffer.append(nodeTest);
        }
        Expression[] predicates = getPredicates();
        if (predicates != null) {
            for (int i = 0; i < predicates.length; i++) {
                buffer.append('[');
                buffer.append(predicates[i]);
                buffer.append(']');
            }
        }
        return buffer.toString();
    }

    /**
     * Decode an axis code to its name.
     * @param axis int code
     * @return String name.
     * @see Compiler
     * @see http://www.w3.org/TR/xpath#axes
     */
    public static String axisToString(int axis) {
        switch (axis) {
            case Compiler.AXIS_SELF :
                return "self";
            case Compiler.AXIS_CHILD :
                return "child";
            case Compiler.AXIS_PARENT :
                return "parent";
            case Compiler.AXIS_ANCESTOR :
                return "ancestor";
            case Compiler.AXIS_ATTRIBUTE :
                return "attribute";
            case Compiler.AXIS_NAMESPACE :
                return "namespace";
            case Compiler.AXIS_PRECEDING :
                return "preceding";
            case Compiler.AXIS_FOLLOWING :
                return "following";
            case Compiler.AXIS_DESCENDANT :
                return "descendant";
            case Compiler.AXIS_ANCESTOR_OR_SELF :
                return "ancestor-or-self";
            case Compiler.AXIS_FOLLOWING_SIBLING :
                return "following-sibling";
            case Compiler.AXIS_PRECEDING_SIBLING :
                return "preceding-sibling";
            case Compiler.AXIS_DESCENDANT_OR_SELF :
                return "descendant-or-self";
            default:
                return "UNKNOWN";
        }
    }
}
