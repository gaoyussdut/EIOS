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
 * @version $Revision: 681111 $ $Date: 2008-07-30 11:30:29 -0500 (Wed, 30 Jul 2008) $
 */
public abstract class Path extends Expression {

    protected Step[] steps;
    protected boolean basicKnown = false;
    protected boolean basic;

    protected Path() {
    }

    /**
     * Create a new Path.
     * @param steps that compose the Path
     */
    public Path(Step[] steps) {
        this.steps = steps;
    }


    /**
     * Add a step at then end of this path
     * @param index
     * 		Position of the step; a negative value means at the end
     * @param step
     * @return
     * 		This path
     */
    public Path addStep(Step step) {
    	return addStep(-1, step);
    }

    /**
     * Add a step to this path
     * @param index
     * 		Position of the step; a negative value means at the end
     * @param step
     * @return
     * 		This path
     */
    public Path addStep(int index, Step step) {
    	ArrayList<Step> stepsList = new ArrayList<Step>();
    	if (steps != null) {
    		stepsList.addAll(Arrays.asList(steps));
    	}
    	if (index <0 )
    		stepsList.add(step);
    	else
    		stepsList.add(index, step);
    	this.steps = stepsList.toArray(new Step[stepsList.size()]);
    	return this;
    }


    /**
     * Get the steps.
     * @return Step[]
     */
    public Step[] getSteps() {
        return steps;
    }

    public boolean computeContextDependent() {
        if (steps != null) {
            for (int i = 0; i < steps.length; i++) {
                if (steps[i].isContextDependent()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recognizes paths formatted as <code>foo/bar[3]/baz[@name = 'biz']</code>.
     * The evaluation of such "simple" paths is optimized and
     * streamlined.
     * @return <code>true</code> if this path is simple
     */
    public synchronized boolean isSimplePath() {
        if (!basicKnown) {
            basicKnown = true;
            basic = true;
            Step[] steps = getSteps();
            for (int i = 0; i < steps.length; i++) {
                if (!isSimpleStep(steps[i])) {
                    basic = false;
                    break;
                }
            }
        }
        return basic;
    }

    /**
     * A Step is "simple" if it takes one of these forms: ".", "/foo",
     * "@bar", "/foo[3]". If there are predicates, they should be
     * context independent for the step to still be considered simple.
     * @param step the step to check
     * @return boolean
     */
    protected boolean isSimpleStep(Step step) {
        if (step.getAxis() == Compiler.AXIS_SELF) {
            NodeTest nodeTest = step.getNodeTest();
            if (!(nodeTest instanceof NodeTypeTest)) {
                return false;
            }
            int nodeType = ((NodeTypeTest) nodeTest).getNodeType();
            if (nodeType != Compiler.NODE_TYPE_NODE) {
                return false;
            }
            return areBasicPredicates(step.getPredicates());
        }
        if (step.getAxis() == Compiler.AXIS_CHILD
                || step.getAxis() == Compiler.AXIS_ATTRIBUTE) {
            NodeTest nodeTest = step.getNodeTest();
            if (!(nodeTest instanceof NodeNameTest)) {
                return false;
            }
            if (((NodeNameTest) nodeTest).isWildcard()) {
                return false;
            }
            return areBasicPredicates(step.getPredicates());
        }
        return false;
    }

    /**
     * Learn whether the elements of the specified array are "basic" predicates.
     * @param predicates the Expression[] to check
     * @return boolean
     */
    protected boolean areBasicPredicates(Expression[] predicates) {
        if (predicates != null && predicates.length != 0) {
            boolean firstIndex = true;
            for (int i = 0; i < predicates.length; i++) {
                if (predicates[i] instanceof NameAttributeTest) {
                    if (((NameAttributeTest) predicates[i])
                        .getNameTestExpression()
                        .isContextDependent()) {
                        return false;
                    }
                }
                else if (predicates[i].isContextDependent()) {
                    return false;
                }
                else {
                    if (!firstIndex) {
                        return false;
                    }
                    firstIndex = false;
                }
            }
        }
        return true;
    }

}
