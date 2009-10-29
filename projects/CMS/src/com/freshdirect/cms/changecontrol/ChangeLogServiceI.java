package com.freshdirect.cms.changecontrol;

import java.util.Date;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * Service interface responsible for storing and retrieving
 * {@link com.freshdirect.cms.changecontrol.ChangeSet} instances.
 */
public interface ChangeLogServiceI {

	/**
	 * Record a {@link ChangeSet}.
	 * 
	 * @param changeSet change set to store (never null)
	 * 
	 * @return primary key of stored change set (never null)
	 */
	public PrimaryKey storeChangeSet(ChangeSet changeSet);

	/**
	 * Get change sets affecting a given content object.
	 * Note, that the retrieved change sets will only have the
	 * {@link ContentNodeChange} records affecting the given content node. 
	 * 
	 * @param key content key (never null)
	 * 
	 * @return List of {@link ChangeSet} (never null)
	 */
	public List<ChangeSet> getChangeHistory(ContentKey key);
	
	/**
	 * Get a change set by primary key.
	 * 
	 * @param pk primary key of the change set
	 * 
	 * @return the {@link ChangeSet}, or null if not found
	 */
	public ChangeSet getChangeSet(PrimaryKey pk);
	
	/**
	 * Get changes by date range.
	 * 
	 * @param startDate start date (never null)
	 * @param endDate end date (never null)
	 * 
	 * @return List of {@link ChangeSet} (never null)
	 */
	public List<ChangeSet> getChangesBetween(Date startDate, Date endDate);

}