package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentTypes;


public final class Domain extends ContentNodeModelImpl {

	private static final long	serialVersionUID	= -8561526219039903925L;

	private List<DomainValue> valueModels = new ArrayList<DomainValue>();

    public Domain(ContentKey key) {
        super(key);
    }

    public String getName(){
        return getAttribute(ContentTypes.Domain.NAME.getName(), this.key.id);
    }

    public String getLabel(){
        return getAttribute(ContentTypes.Domain.Label.getName(), this.key.id);
    }

    public List<DomainValue> getDomainValues() {
    	ContentNodeModelUtil.refreshModels(this, "domainValues", valueModels, true);
        return new ArrayList<DomainValue>(valueModels);
    }

    @SuppressWarnings( "unchecked" )
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

    @Override
    public String toString() {
    	return "Domain["+ getName() +"]";
    }

}
