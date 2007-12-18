/**
 * @author ekracoff
 * Created on Feb 3, 2005*/

package com.freshdirect.cms.publish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


/**
 * Record of a content publish event.
 */
public class Publish implements Serializable, Cloneable {

	private String             id;
	private Date               timestamp;
	private String             userId;
	private String             description;
	private Date               lastModified;
	private EnumPublishStatus  status;
	private ArrayList          messages = new ArrayList();
	
	// this field is not persistent
	private String path;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String user) {
		this.userId = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Get the messages for the publish object.
	 * 
	 * @return Returns a set of PublishMessage objects.
	 */
	public List getMessages() {
		return messages;
	}
	
	/**
	 * Set the messages for the publish object.
	 * 
	 * @param messages a set of PublishMessage objects, the new messages for the publish.
	 */
	public void setMessages(List messages) {
		this.messages = new ArrayList(messages);
	}
	
	/**
	 * @return Returns the status.
	 */
	public EnumPublishStatus getStatus() {
		return status;
	}
	
	/**
	 * @param status The status to set.
	 */
	public void setStatus(EnumPublishStatus status) {
		this.status = status;
	}
	
	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 *  Clone the publish object.
	 * 
	 *  @return a new Publish object, with the same properties.
	 */
	public Object clone() {
		Publish clone = new Publish();
		
		clone.id           = id;
		clone.timestamp    = (Date) timestamp.clone();
		clone.userId       = userId;
		clone.description  = description;
		clone.lastModified = (Date) lastModified.clone();
		clone.status       = status;
		clone.messages     = (ArrayList) messages.clone();
		
		return clone;
	}
	
	/**
	 *  Compare the Publish object to another one.
	 * 
	 *  @return true if the two objects are equal Publish objects.
	 */
	public boolean equals(Object other) {
		if (other instanceof Publish) {
			Publish publish = (Publish) other;
			
			return id.equals(publish.id)
			    && timestamp.equals(publish.timestamp)
				&& userId.equals(publish.userId)
				&& description.equals(publish.description)
				&& lastModified.equals(publish.lastModified)
				&& status.equals(publish.status)
				&& messages.equals(publish.messages);
		}
		
		return false;
	}
}
