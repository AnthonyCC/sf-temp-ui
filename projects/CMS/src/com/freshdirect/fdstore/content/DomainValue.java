package com.freshdirect.fdstore.content;

import java.util.Comparator;

import com.freshdirect.cms.ContentKey;


public class DomainValue extends ContentNodeModelImpl implements WineFilterValue {
	public static Comparator<DomainValue> SORT_BY_LABEL = new Comparator<DomainValue>() {
		@Override
		public int compare(DomainValue o1, DomainValue o2) {
			return o1.getLabel().compareTo(o2.getLabel());
		}
	};

	public DomainValue(ContentKey cKey) {
		super(cKey);
	}

	public String getLabel() {
		return getAttribute("Label", this.getCMSNode().getKey().getId());
	}

	/**
	 *  This is not getting the value [-batchley 20090710]
	 */
	public String getValue() { /*do this so we dont have to do it on the jsp level */
		return getAttribute("Label", this.getCMSNode().getKey().getId());
	}
	
	/**
	 *  Actually get the value 
	 */
	public String getTheValue() {
		return getAttribute("VALUE", this.getCMSNode().getKey().getId());
	}
	
	public String getID() {
		return this.getCMSNode().getKey().getId();
	}

	public Domain getDomain() {
		return (Domain) this.getParentNode();
	}
	
	public ContentKey getDomainContentKey() {
	    return getParentNode().getContentKey();
	}

	public String toString() {
		return "DomainValue[" + getDomain().toString() + ", " + getLabel() + ", " + getValue() + ", " + getPriority() + "]";
	}
	
	public boolean equals(Object obj){
		if(obj!= null && obj instanceof DomainValue){
			DomainValue dv = (DomainValue)obj;
			return (this.getContentName().equals(dv.getContentName()) && this.getValue().equals(dv.getValue())); 
		}
		return false;
	}

	@Override
	public String getEncoded() {
		return getContentKey().getEncoded();
	}
	
	@Override
	public String getDomainName() {
		Domain domain = getDomain();
		return domain == null ? null : domain.getLabel();
	}
	
	@Override
	public String getDomainEncoded() {
		String cachedValue = ContentFactory.getInstance().getDomainEncodedForWineDomainValue(this);
		return cachedValue != null ? cachedValue : getDomain().getContentKey().getEncoded();
	}
	
	@Override
	public EnumWineFilterValueType getWineFilterValueType() {
		return EnumWineFilterValueType.CMS;
	}
	
	@Override
	public String getFilterRepresentation() {
		return getLabel();
	}
}
