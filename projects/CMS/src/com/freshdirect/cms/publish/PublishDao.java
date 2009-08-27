/**
 * @author ekracoff
 * Created on Apr 22, 2005*/

package com.freshdirect.cms.publish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.hibernate.HibernateDaoFactory;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;


public class PublishDao extends HibernateDaoSupport {
	
	/**
	 *  The hibernate dao factory used to create this DAO object.
	 */
	private HibernateDaoFactory daoFactory;
	
	/**
	 *  Constructor.
	 * 
	 *  @param daoFactory the hibernate dao factory used to create this dao object.
	 */
	public PublishDao(HibernateDaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	/**
	 *  Return a hibernate session factory to use for accessing persistent storage.
	 * 
	 *  @return a hibernate session factory
	 *  @see ResourceUtil#getResource(String)
	 */
	protected SessionFactory getSessionFactory() {
		return daoFactory.getSessionFactory();
	}

	/**
	 * Return a HibernateDaoFactory object that is specific to the package.
	 * The main reason for this object is to provide a thread-local, package-specific
	 * hibernate session and transaction.
	 */
	protected HibernateDaoFactory getDaoFactory() {
		return daoFactory;
	}
	
	/**
	 *  Return a list of all Publish objects.
	 *  
	 *  @return a list containing all Publish objects.
	 */
	public List getAllPublishes() {
		return query("from Publish");
	}
	
	/**
	 * Fetch publish objects without getting their children too.
	 * Basically it is a hack as hibernate force eager load causing a tremendous amount of subselects.
	 * We only need the publish objects.
	 * 
	 * @param qualifiers SQL qualifiers following WHERE keyword
	 * @param orderBy
	 * @return (List<Publish>) publishes
	 */
	public List fetchPublishes(String qualifiers, String orderBy) {
		List results = new ArrayList();

		Connection conn = currentSession().connection();
		
		StringBuffer cmd = new StringBuffer("SELECT ID, TIMESTAMP, USER_ID, DESCRIPTION, LAST_MODIFIED, STATUS FROM CMS.PUBLISH");
		if (qualifiers != null) {
			cmd.append(" WHERE " + qualifiers);
		}
		if (orderBy != null) {
			cmd.append(" ORDER BY " + orderBy);
		}
		
		try {
			PreparedStatement stmt = conn.prepareStatement(cmd.toString());
			
			ResultSet records = stmt.executeQuery();
			
			while(records.next()) {
				Publish p = new Publish();
				
				// fill in publish object manually
				p.setId(records.getString(1));
				p.setTimestamp(records.getTimestamp(2));
				p.setUserId(records.getString(3));
				p.setDescription(records.getString(4));
				p.setLastModified(records.getDate(5));
				p.setStatus(EnumPublishStatus.getEnum(records.getString(6)));
				
				
				p.setMessages(Collections.EMPTY_LIST);
				// add to list
				results.add(p);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}

		return results;
	}
	
	/**
	 *  Return all Publish objects, orderred by the specified column.
	 * 
	 * @param orderBy the order by clause
	 * @return all Publish objects, ordered by the specified column
	 */
	public List getAllPublishesOrdered(String orderBy) {
		return query("from Publish order by " + orderBy);
	}
	
	public Publish getPublish(String publishId) {
		return (Publish) load(Publish.class, publishId);
	}
	
	/**
	 *  Return the most recent Publish object, with state COMPLETE.
	 * 
	 *  @return the most recent Publish object.
	 */
	public Publish getMostRecentPublish() {
		try {
			List		list = query("from Publish "
			               + " where timestamp = (select max(publish.timestamp) from Publish publish "
	                       + " where status = '" + EnumPublishStatus.COMPLETE.getName() + "')");
	
			return list.size() > 0 ? (Publish) list.get(0) : null;
		} catch (ConstraintViolationException e) {
			// this happens if there are no complete publishes in the database
			// this the above nested query fails
			return null;
		}
	}

	
        /**
         *  Return the most recent Publish object.
         * 
         *  @return the most recent Publish object.
         */
        public Publish getMostRecentNotCompletedPublish() {
            try {
                    List            list = query("from Publish "
                                   + " where timestamp = (select max(publish.timestamp) from Publish publish) ");
    
                    return list.size() > 0 ? (Publish) list.get(0) : null;
            } catch (ConstraintViolationException e) {
                    // this happens if there are no complete publishes in the database
                    // this the above nested query fails
                    return null;
            }
    }
	
	
	public void deletePublish(Publish publish) {
		delete(publish);
	}
	
	/**
	 * Get a publish made before a specified publish.
	 * 
	 * @param publish get the publish that was made before this one.
	 * @return the publish made before the supplied publish object,
	 *         or null if there is no previous publish.
	 */
	public Publish getPreviousPublish(Publish publish) {
		List list = fetchPublishes("timestamp < (select p.timestamp from CMS.PUBLISH p where p.id = " + publish.getId() + ") ", "timestamp desc");
		
		return list.size() > 0 ? (Publish) list.get(0) : null;
	}
	
	public void savePublish(Publish publish) {
		saveOrUpdate(publish);
	}

}
