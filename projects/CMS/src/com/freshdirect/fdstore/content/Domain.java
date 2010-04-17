package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;


public final class Domain extends ContentNodeModelImpl {
    
	private static final long	serialVersionUID	= -8561526219039903925L;
	
	private List<DomainValue> valueModels = new ArrayList<DomainValue>();

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
    
    public List<DomainValue> getDomainValues() {
    	ContentNodeModelUtil.refreshModels(this, "domainValues", valueModels, true);
        return new ArrayList<DomainValue>(valueModels);
    }
    
    public List<ContentKey> getDomainValueKeys() {
        return (List<ContentKey>) getCmsAttributeValue("domainValues");
    }
    
    public DomainValue getDomainValue(String value) {
    	for ( DomainValue v  : valueModels ) {
            if (v.getValue().equalsIgnoreCase(value))
                return v;
        }
        return null;
    }

    public String toString() {
    	return "Domain["+ getName() +"]";
    }
    
}
