package com.freshdirect.cms.ui.model.publish;

import java.util.Date;

import com.freshdirect.cms.ui.model.ContentNodeModel;

public class GwtPublishMessage extends ContentNodeModel {
	private static final long serialVersionUID = 8040793784936726437L;

	public GwtPublishMessage() {
		super();
	}

    public GwtPublishMessage( String type, String key ) {
    	super( type, "", key );
    }

    public void setSeverity( String severity ) {
    	set( "severity", severity );
    }

    public String getSeverity() {
		return get( "severity" );
    }

    public void setMessage( String message ) {
    	set( "message", message );
    }

    public String getMessage() {
		return get( "message" );
    }

    public void setTimestamp( Date timestamp ) {
    	set( "timestamp", timestamp );
    }

    public Date getTimestamp() {
		return get( "timestamp" );
    }

    public String getTask(){
        return get("task");
    }
    
    public void setTask(String task){
        set("task", task);
    }

    public String getStoreId(){
        return get("storeId");
    }
    
    public void setStoreId(String storeId){
        set("storeId", storeId);
    }
}
