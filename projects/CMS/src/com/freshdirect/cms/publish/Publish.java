/**
 * @author ekracoff
 * Created on Feb 3, 2005*/

package com.freshdirect.cms.publish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

/**
 * Record of a content publish event.
 */
public class Publish implements Serializable, Cloneable {

	private static final long serialVersionUID = 1807983326641626884L;

	private String id;
	private Date timestamp;
	private String userId;
	private String description;
	private Date lastModified;
	private EnumPublishStatus status;
	private ArrayList<PublishMessage> messages = new ArrayList<PublishMessage>();
	
	/**
	 * Base path represents the path to
	 * the common folder of publish materials of each store.
	 * 
	 * Basically, it should point to "${cms.publish.basePath}/<publish ID>"
	 */
	@Transient
	private String basePath;
	
	/**
	 * It holds the ID of store key 
	 */
	@Transient
	private String storeId;

	/**
	 * Path points to a particular store material under base path
	 */
	@Transient
	private String path;

	/**
	 * Clone the publish object.
	 * 
	 * @return a new Publish object, with the same properties.
	 */

	@Override
    @SuppressWarnings("unchecked")
	public Object clone() {
		Publish clone = new Publish();
		clone.id = id;
		clone.timestamp = (Date) timestamp.clone();
		clone.userId = userId;
		clone.description = description;
		clone.lastModified = (Date) lastModified.clone();
		clone.status = status;
		clone.messages = (ArrayList<PublishMessage>) messages.clone();
		clone.basePath = basePath;
		clone.path = path;
		clone.storeId = storeId;
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Publish)) {
			return false;
		}
		Publish other = (Publish) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (lastModified == null) {
			if (other.lastModified != null) {
				return false;
			}
		} else if (!lastModified.equals(other.lastModified)) {
			return false;
		}
		if (messages == null) {
			if (other.messages != null) {
				return false;
			}
		} else if (!messages.equals(other.messages)) {
			return false;
		}
		if (status == null) {
			if (other.status != null) {
				return false;
			}
		} else if (!status.equals(other.status)) {
			return false;
		}
		if (storeId == null) {
			if (other.storeId != null) {
				return false;
			}
		} else if (!storeId.equals(other.storeId)) {
			return false;
		}
		if (timestamp == null) {
			if (other.timestamp != null) {
				return false;
			}
		} else if (!timestamp.equals(other.timestamp)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * Get the messages for the publish object.
	 * 
	 * @return Returns a set of PublishMessage objects.
	 */
	public List<PublishMessage> getMessages() {
		return messages;
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return Returns the status.
	 */
	public EnumPublishStatus getStatus() {
		return status;
	}

	public String getBasePath() {
		return basePath;
	}
	
	public String getStoreId() {
		return storeId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getUserId() {
		return userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((messages == null) ? 0 : messages.hashCode());
		result = prime * result + ((basePath == null) ? 0 : basePath.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((storeId == null) ? 0 : storeId.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Set the messages for the publish object.
	 * 
	 * @param messages
	 *            a set of PublishMessage objects, the new messages for the
	 *            publish.
	 */
	public void setMessages(List<PublishMessage> messages) {
		this.messages = new ArrayList<PublishMessage>(messages);
	}

	/**
	 * @param path
	 *            The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(EnumPublishStatus status) {
		this.status = status;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setUserId(String user) {
		this.userId = user;
	}
}
