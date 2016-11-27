package com.freshdirect.cms.ui.model.publish;

import java.util.Date;

import com.freshdirect.cms.ui.model.ContentNodeModel;

public class GwtPublishMessage extends ContentNodeModel {
	private static final long serialVersionUID = 8040793784936726437L;

	// the ordinals are important, check the constants in PublishMessage. 
    public enum Level {
        FAILURE, ERROR, WARNING, INFO, DEBUG
    }

    
	public GwtPublishMessage() {
		super();
	}
    
    public GwtPublishMessage( String type, String key ) {
    	super( type, "", key );
    }

    public void setSeverity( int severity ) {
    	set( "severity", Level.values()[severity] );
    }
    
    public Level getSeverity() {
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

}
