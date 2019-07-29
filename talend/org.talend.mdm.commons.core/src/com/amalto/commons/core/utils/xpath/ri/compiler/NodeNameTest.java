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

import com.amalto.commons.core.utils.xpath.ri.QName;

/**
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public class NodeNameTest extends NodeTest {
    private QName qname;
    private String namespaceURI;

    protected NodeNameTest() {
    	super();
    }

    /**
     * Create a new NodeNameTest.
     * @param qname name to match
     */
    public NodeNameTest(QName qname) {
        this.qname = qname;
    }

    /**
     * Create a new NodeNameTest.
     * @param qname name to match
     * @param namespaceURI uri to match
     */
    public NodeNameTest(QName qname, String namespaceURI) {
        this.qname = qname;
        this.namespaceURI = namespaceURI;
    }

    /**
     * Get the node name.
     * @return QName
     */
    public QName getNodeName() {
        return qname;
    }

    /**
     * Get the ns URI.
     * @return String
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Learn whether this is a wildcard test.
     * @return <code>true</code> if the node name is "*".
     */
    public boolean isWildcard() {
        return qname.getName().equals("*");
    }

    public String toString() {
        return qname.toString();
    }

    public NodeNameTest clone(boolean deep) {
    	if (! deep) return new NodeNameTest(this.qname, this.namespaceURI);

    	NodeNameTest nnt = new NodeNameTest();
    	if (this.qname != null) {
        	nnt.qname = new QName(
        		this.qname.getPrefix() == null? null : new String(this.qname.getPrefix()),
        		this.qname.getName() == null ? null : new String(this.qname.getName())
        	);
    	}
    	nnt.namespaceURI = this.namespaceURI == null ? null : new String(this.namespaceURI);

    	return nnt;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return clone(true);
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof NodeNameTest)) return false;
        NodeNameTest ep = (NodeNameTest)obj;

    	if (! ep.qname.equals(this.qname)) return false;
    	if (ep.namespaceURI==null) {
    		return (this.namespaceURI==null);
    	}
    	if (! ep.namespaceURI.equals(this.namespaceURI)) return false;

    	return true;
    }
}
