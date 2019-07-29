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


/**
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public class LocationPath extends Path {

    private boolean absolute;

    protected LocationPath() {
    }

    /**
     * Create a new LocationPath.
     * @param absolute whether this is an absolute path
     * @param steps to evaluate
     */
    public LocationPath(boolean absolute, Step[] steps) {
        super(steps);
        this.absolute = absolute;
    }

    /**
     * Learn whether this LocationPath is absolute.
     * @return boolean
     */
    public boolean isAbsolute() {
        return absolute;
    }

    public LocationPath clone(boolean deep) {
    	if (! deep) return new LocationPath(this.absolute, this.getSteps());

    	LocationPath lp = new LocationPath();
    	lp.absolute = this.absolute;

    	//Steps
    	ArrayList<Step> ss = new ArrayList<Step>();
    	if (this.steps != null) {
    		for (int i = 0; i < this.steps.length; i++) {
    			ss.add(this.steps[i].clone(true));
            }
    	}
    	lp.steps = ss.toArray(new Step[ss.size()]);

    	return lp;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return clone(true);
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof LocationPath)) return false;
        LocationPath ep = (LocationPath)obj;

    	if (ep.absolute != this.absolute) return false;

    	//Steps
    	if (this.steps != null) {
    		if (ep.steps.length != this.steps.length) return false;
    		for (int i = 0; i < this.steps.length; i++) {
    			if (! ep.steps[i].equals(this.steps[i])) return false;
            }
    	}

    	return true;
    }


    public boolean computeContextDependent() {
        return !absolute || super.computeContextDependent();
    }




    public String toString() {
        StringBuffer buffer = new StringBuffer();
        Step[] steps = getSteps();
        if (steps != null) {
            for (int i = 0; i < steps.length; i++) {
                if (i > 0 || absolute) {
                    buffer.append('/');
                }
                buffer.append(steps[i]);
            }
        }
        return buffer.toString();
    }

}
