package com.freshdirect.delivery.restriction;

import com.freshdirect.framework.core.ModelSupport;

abstract class AbstractRestriction extends ModelSupport implements RestrictionI {

	private final EnumDlvRestrictionCriterion criterion;
	private final EnumDlvRestrictionReason reason;
	private final String name;
	private final String message;
	private final String path;	

	protected AbstractRestriction(
		String id,	
		EnumDlvRestrictionCriterion criterion,
		EnumDlvRestrictionReason reason,
		String name,
		String message,String path) {
		if(id!=null && id.trim().length()>0){
		    this.setId(id);
		}
		this.criterion = criterion;
		this.reason = reason;
		this.name = name;
		this.message = message;
		this.path= path;
	}

	public String getName() {
		return this.name;
	}

	public String getMessage() {
		return this.message;
	}
	
	public String getPath() {
		return path;
	}
	
	public EnumDlvRestrictionCriterion getCriterion() {
		return criterion;
	}

	public EnumDlvRestrictionReason getReason() {
		return this.reason;
	}

	public boolean isMatching(EnumDlvRestrictionCriterion criterion, EnumDlvRestrictionReason reason, EnumDlvRestrictionType type) {
		return (criterion == null || criterion.equals(this.getCriterion()))
			&& (reason == null || reason.equals(this.getReason()))
			&& (type == null || type.equals(this.getType()));
	}

	public String toString() {
		return "DlvRestriction["
			+ this.getCriterion()
			+ ", "
			+ this.getType()
			+ ", "
			+ this.getReason().getName()
			+ ", "
			+ this.getName()
			+ ", "
			+ this.getDisplayDate()
			+ "]";
	}

}
