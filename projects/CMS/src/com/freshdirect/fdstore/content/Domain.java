/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public final class Domain extends ContentNodeModelImpl {
    
    //private List valueKeys = new ArrayList();
    private List valueModels = new ArrayList();

    public Domain(ContentKey cKey) {
    	super(cKey);
	}
    
    public EnumDomainType getDomainType() {
        return EnumDomainType.getDomainType(getAttribute("DOMAIN_TYPE", "V"));
    }
       
    public String getName(){
        return getAttribute("NAME", this.getCMSNode().getKey().getId());
    }

    public String getLabel(){
        return getAttribute("Label", this.getCMSNode().getKey().getId());
    }
    
    public List getDomainValues() {
		ContentNodeModelUtil.refreshModels(this, "domainValues", valueModels, true);

        return new ArrayList(valueModels);
    }
    
    public DomainValue getDomainValue(String value) {
        Iterator vIter = valueModels.iterator();
        while (vIter.hasNext()) {
            DomainValue v = (DomainValue) vIter.next();
            if (v.getValue().equalsIgnoreCase(value))
                return v;
        }
        return null;
    }

    /*
    private final static Comparator VALUE_COMPARATOR = new Comparator() {
        public int compare(Object obj1, Object obj2) {
            DomainValue v1 = (DomainValue) obj1;
            DomainValue v2 = (DomainValue) obj2;
            if (v1.getPriority() > v2.getPriority())
                return 1;
            else if (v1.getPriority() < v2.getPriority())
                return -1;
            else
                return 0;
        }
    };
    */
    
    public String toString() {
    	return "Domain["+ getName() +"]";
    }
    
    
}
