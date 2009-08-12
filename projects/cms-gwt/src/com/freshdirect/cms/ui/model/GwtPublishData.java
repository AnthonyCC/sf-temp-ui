package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * @author zsombor
 *
 */
public class GwtPublishData extends BaseModelData implements Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setId(String id) {
		set("id", id);
	}
	
	public String getId() {
	    return get("id");
	}

	public void setStatus(String status) {
		set("status", status);
	}

	public void setCreated(Date created) {
		set("created", created);
	}

	public void setPublisher(String publisher) {
		set("publisher", publisher);
	}

	public void setComment(String comment) {
		set("comment", comment);
	}

}
