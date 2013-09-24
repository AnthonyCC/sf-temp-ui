package com.freshdirect.fdstore.coremetrics.tagmodel;

import java.util.List;


public class ConversionEventTagModel extends AbstractTagModel  {

	private static final long	serialVersionUID	= -7677360510359719732L;
	
	private String eventId; 
	private String actionType;
	private String eventCategoryId; 
	private String points;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getEventCategoryId() {
		return eventCategoryId;
	}
	public void setEventCategoryId(String eventCategoryId) {
		this.eventCategoryId = eventCategoryId;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	
	@Override
	public String getFunctionName() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<String> toStringList() {
		throw new UnsupportedOperationException();
	} 
	
}