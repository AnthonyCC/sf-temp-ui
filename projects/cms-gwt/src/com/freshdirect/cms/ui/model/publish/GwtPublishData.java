package com.freshdirect.cms.ui.model.publish;

import java.io.Serializable;
import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class GwtPublishData extends BaseModelData implements Serializable {

	private static final long	serialVersionUID	= 1L;
	
	public static final String	COMPLETE			= "COMPLETE";
	public static final String	FAILED				= "FAILED";
	public static final String	PROGRESS			= "PROGRESS";

	public void setId( String id ) {
		set( "id", id );
	}

	public String getId() {
		return get( "id" );
	}

	public void setStatus( String statusCode, String statusMessage ) {
		set( "statuscode", statusCode );
		set( "status", statusMessage );
	}

	public String getStatusMessage() {
		return get( "status" );
	}
	
	public String getStatusCode() {
		return get( "statuscode" );
	}

	public void setCreated( Date created ) {
		set( "created", created );
	}
	
	public final Date getCreated() {
		return get( "created" ); 
	}

	public void setPublisher( String publisher ) {
		set( "publisher", publisher );
	}
	
	public String getPublisher() {
		return get( "publisher" );
	}

	public void setComment( String comment ) {
		set( "comment", comment );
	}
	
	public String getComment() {
		return get( "comment" );
	}

}
