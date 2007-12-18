/**
 * @author ekracoff
 * Created on Jan 21, 2005*/

package com.freshdirect.cms.changecontrol;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.core.ModelSupport;

/**
 * Record of a change set affecting multiple content objects. Has a
 * list of {@link com.freshdirect.cms.changecontrol.ContentNodeChange}s.
 */
public class ChangeSet extends ModelSupport {

	private String userId;

	private Date modifiedDate;

	/** List of {@link ContentNodeChange} */
	private List nodeChanges = new ArrayList();

	private String note;

	/**
	 * @return List of {@link ContentNodeChange}
	 */
	public List getNodeChanges() {
		return nodeChanges;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setNodeChanges(List nodeChanges) {
		this.nodeChanges = nodeChanges;
	}

	public void setModifiedDate(Date modified) {
		this.modifiedDate = modified;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void addChange(ContentNodeChange nodeChange) {
		this.nodeChanges.add(nodeChange);
	}

	/**
	 * Get a filtered list of changes for a given content object.
	 * 
	 * @param key content node key (never null)
	 * @return List of {@link ContentNodeChange} (never null)
	 */
	public List getNodeChangesById(ContentKey key) {
		List nodeChanges = new ArrayList();
		for (Iterator i = this.nodeChanges.iterator(); i.hasNext();) {
			ContentNodeChange cnc = (ContentNodeChange) i.next();
			if (key.equals(cnc.getContentKey())) {
				nodeChanges.add(cnc);
			}
		}
		return nodeChanges;
	}

}
