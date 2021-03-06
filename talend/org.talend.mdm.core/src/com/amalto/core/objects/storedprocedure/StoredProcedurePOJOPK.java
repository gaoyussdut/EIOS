/*
 * Copyright (C) 2006-2018 Talend Inc. - www.talend.com
 * 
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 * 
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */
package com.amalto.core.objects.storedprocedure;

import com.amalto.core.objects.ObjectPOJOPK;


public class StoredProcedurePOJOPK extends ObjectPOJOPK{
	
	public StoredProcedurePOJOPK(String name) {
		super(name);
	}
	
	public StoredProcedurePOJOPK(ObjectPOJOPK pk) {
		super(pk.getIds());
	}
	

}
