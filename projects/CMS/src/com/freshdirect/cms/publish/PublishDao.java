package com.freshdirect.cms.publish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.hibernate.HibernateDaoFactory;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PublishDao extends HibernateDaoSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(PublishDao.class);

    /**
     * The hibernate dao factory used to create this DAO object.
     */
    private HibernateDaoFactory daoFactory;

    /**
     * Constructor.
     * 
     * @param daoFactory
     *            the hibernate dao factory used to create this dao object.
     */
    public PublishDao(HibernateDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Return a hibernate session factory to use for accessing persistent storage.
     * 
     * @return a hibernate session factory
     * @see ResourceUtil#getResource(String)
     */
    @Override
    protected SessionFactory getSessionFactory() {
        return daoFactory.getSessionFactory();
    }

    /**
     * Return a HibernateDaoFactory object that is specific to the package. The main reason for this object is to provide a thread-local, package-specific hibernate session and
     * transaction.
     */
    @Override
    protected HibernateDaoFactory getDaoFactory() {
        return daoFactory;
    }

    /**
     * Return a list of all Publish objects.
     * 
     * @return a list containing all Publish objects.
     */
    public List<Publish> getAllPublishes() {
        return query("from Publish");
    }

    /**
     * Fetch publish objects without getting their children too. Basically it is a hack as hibernate force eager load causing a tremendous amount of subselects. We only need the
     * publish objects.
     * 
     * @param qualifiers
     *            SQL qualifiers following WHERE keyword
     * @param orderBy
     * @return (List<Publish>) publishes
     */
    public List<Publish> fetchPublishes(String qualifiers, String orderBy) {
        List<Publish> results = new ArrayList<Publish>();

        Connection conn = currentSession().connection();

        StringBuffer cmd = new StringBuffer("SELECT ID, TIMESTAMP, USER_ID, DESCRIPTION, LAST_MODIFIED, STATUS FROM CMS.PUBLISH");
        if (qualifiers != null) {
            cmd.append(" WHERE " + qualifiers);
        }
        if (orderBy != null) {
            cmd.append(" ORDER BY " + orderBy);
        }

        String query = cmd.toString();

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet records = stmt.executeQuery();

            while (records.next()) {
                Publish p = new Publish();

                // fill in publish object manually
                p.setId(records.getString(1));
                p.setTimestamp(records.getTimestamp(2));
                p.setUserId(records.getString(3));
                p.setDescription(records.getString(4));
                p.setLastModified(records.getDate(5));
                p.setStatus(EnumPublishStatus.getEnum(records.getString(6)));

                p.setMessages(Collections.<PublishMessage> emptyList());

                // add to list
                results.add(p);
            }

        } catch (SQLException e) {
            LOGGER.error("SQL error during fetching publishes, query = " + query, e);
        }

        return results;
    }

    /**
     * Fetch publish objects without getting their children too. Basically it is a hack as hibernate force eager load causing a tremendous amount of subselects. We only need the
     * publish objects.
     * 
     * @param qualifiers
     *            SQL qualifiers following WHERE keyword
     * @param orderBy
     * @return (List<Publish>) publishes
     */
    public List<Publish> fetchPublishesX(String qualifiers, String orderBy) {
        List<Publish> results = new ArrayList<Publish>();

        Connection conn = currentSession().connection();

        StringBuffer cmd = new StringBuffer("SELECT ID, CRO_MOD_DATE, USER_ID, DESCRIPTION, LAST_MODIFIED, STATUS FROM CMS.PUBLISHX");
        if (qualifiers != null) {
            cmd.append(" WHERE " + qualifiers);
        }
        if (orderBy != null) {
            cmd.append(" ORDER BY " + orderBy);
        }

        String query = cmd.toString();

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet records = stmt.executeQuery();

            while (records.next()) {
                PublishX p = new PublishX();
                p.setId(records.getString(1));
                p.setTimestamp(records.getTimestamp(2));
                p.setUserId(records.getString(3));
                p.setDescription(records.getString(4));
                p.setLastModified(records.getDate(5));
                p.setStatus(EnumPublishStatus.getEnum(records.getString(6)));
                p.setMessages(Collections.<PublishMessage> emptyList());
                results.add(p);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL error during fetching feed publishes, query = " + query, e);
        }

        return results;
    }

    /**
     * Return all Publish objects, ordered by the specified column.
     * 
     * @param orderBy
     *            the order by clause
     * @return all Publish objects, ordered by the specified column
     */
    public List<Publish> getAllPublishesOrdered(String orderBy) {
        return query("from Publish order by " + orderBy);
    }

    public Publish getPublish(String publishId, Class clazz) {
        return (Publish) load(clazz, publishId);
    }

    /**
     * Return the most recent Publish object, with state COMPLETE.
     * 
     * @return the most recent Publish object.
     */
    public Publish getMostRecentPublish() {
        String query = "from Publish where timestamp = (select max(publish.timestamp) from Publish publish " + " where status = '" + EnumPublishStatus.COMPLETE.getName() + "')";
        try {
            List<Publish> list = query(query);
            return list.size() > 0 ? list.get(0) : null;
        } catch (ConstraintViolationException e) {
            // this happens if there are no complete publishes in the database
            // this the above nested query fails
            LOGGER.error("Constraint violation error during fetching most recent publishes, query = " + query, e);
            return null;
        }
    }

    public PublishX getMostRecentPublishX() {
        String query = "from PublishX where timestamp = (select max(publishX.timestamp) from PublishX publishX " + " where status = '" + EnumPublishStatus.COMPLETE.getName()
                + "')";
        try {
            List<PublishX> list = query(query);
            return list.size() > 0 ? list.get(0) : null;
        } catch (ConstraintViolationException e) {
            LOGGER.error("Constraint violation error during fetching most recent feed publishes, query = " + query, e);
            return null;
        }
    }

    /**
     * Return the most recent Publish object.
     * 
     * @return the most recent Publish object.
     */
    public Publish getMostRecentNotCompletedPublish() {
        String query = "from Publish where timestamp = (select max(publish.timestamp) from Publish publish) ";
        try {
            List<Publish> list = query(query);
            return list.size() > 0 ? list.get(0) : null;
        } catch (ConstraintViolationException e) {
            // this happens if there are no complete publishes in the database
            // this the above nested query fails
            LOGGER.error("Constraint violation error during fetching most recent not completed publishes, query = " + query, e);
            return null;
        }
    }

    public void deletePublish(Publish publish) {
        delete(publish);
    }

    /**
     * Get a publish made before a specified publish.
     * 
     * @param publish
     *            get the publish that was made before this one.
     * @return the publish made before the supplied publish object, or null if there is no previous publish.
     */
    public Publish getPreviousPublish(Publish publish) {
        List<Publish> list = fetchPublishes("timestamp < (select p.timestamp from CMS.PUBLISH p where p.id = " + publish.getId() + ") ", "timestamp desc");

        return list.size() > 0 ? list.get(0) : null;
    }

    public Publish getPreviousFeedPublish(Publish publish) {
        List<Publish> list = fetchPublishesX("cro_mod_date < (select p.cro_mod_date from CMS.PUBLISHX p where p.id = " + publish.getId() + ") ", "cro_mod_date desc");
        return list.size() > 0 ? list.get(0) : null;
    }

    public void savePublish(Publish publish) {
        saveOrUpdate(publish);
    }

    public PublishX getMostRecentNotCompletedPublishX() {
        String query = "from PublishX where timestamp = (select max(publishX.timestamp) from PublishX publishX) ";
        try {
            List<PublishX> list = query(query);
            return list.size() > 0 ? list.get(0) : null;
        } catch (ConstraintViolationException e) {
            LOGGER.error("Constraint violation error during fetching most recent not completed feed publishes, query = " + query, e);
            return null;
        }
    }
}
