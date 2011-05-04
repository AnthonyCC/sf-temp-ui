/**
 * @author ekracoff
 * Created on Jan 21, 2005*/

package com.freshdirect.cms.changecontrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;

/**
 * Record of a change affecting a given content node. Has a
 * list of {@link com.freshdirect.cms.changecontrol.ChangeDetail}s.
 */
public class ContentNodeChange implements Serializable {

	private ChangeSet changeSet;
	
	private ContentKey contentKey;
	
	/** List of {@link com.freshdirect.cms.changecontrol.ChangeDetail} */
	private List<ChangeDetail> changeDetails = new ArrayList<ChangeDetail>();
	
	private EnumContentNodeChangeType changeType;

	public ContentNodeChange() {
	}

	/**
	 * @return List of {@link ChangeDetail}
	 */
	public List<ChangeDetail> getChangeDetails() {
		return changeDetails;
	}

	public ContentKey getContentKey() {
		return contentKey;
	}

	public void setContentKey(ContentKey contentKey) {
		this.contentKey = contentKey;
	}

	public void setChangeDetails(List<ChangeDetail> changeDetails) {
		this.changeDetails = changeDetails;
	}

	public EnumContentNodeChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(EnumContentNodeChangeType changeType) {
		this.changeType = changeType;
	}

	public void addDetail(ChangeDetail detail) {
		this.changeDetails.add(detail);
	}

	public void setChangeSet(ChangeSet changeSet) {
		this.changeSet = changeSet;
	}

	public ChangeSet getChangeSet() {
		return this.changeSet;
	}
	
	@Override
	public String toString() {
            return "ContentNodeChange[" + contentKey + ',' + changeType.getName() + ',' + changeDetails + ']';
	}

}
