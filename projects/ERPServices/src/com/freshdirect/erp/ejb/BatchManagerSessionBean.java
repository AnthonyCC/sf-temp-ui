/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.ejb;

import javax.ejb.*;
import java.sql.*;
import java.util.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.erp.*;
import com.freshdirect.erp.model.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class BatchManagerSessionBean extends SessionBeanSupport {
    
    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( BatchManagerSessionBean.class );
    
    /** Creates new SAPLoaderSessionBean */
    public BatchManagerSessionBean() {
        super();
    }
    
    /*
    public Collection getBatches(EnumBatchStatus status, java.util.Date since)
        throws LoaderException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList batches = new ArrayList();
        try {
            String query =  "select b.version, b.description, bh.status, bh.status_date, bh.initials, bh.notes from batch b, batch_history bh where b.version=bh.version ";
            String orderBy = " order by bh.version desc, bh.status_date, bh.status";
            conn = getConnection();
            if ((status == null) && (since == null)) {
                //
                // find all batches of any status since the beginning of time
                //
                ps = conn.prepareStatement(query + orderBy);
            } else if (status == null) {
                //
                // find all batches of any status since a specific date
                //
                //ps = conn.prepareStatement(query + "and b.version in (select distinct(version) from batch_history where status_date>?)" + orderBy);
                //ps.setTimestamp(1, new java.sql.Timestamp(since.getTime()));
            } else if (since == null) {
                //
                // find all batches of a specific status since the beginning of time
                //
                //ps = conn.prepareStatement(query + "and b.version in (select distinct(version) from batch_history where status=?)" + orderBy);
                //ps.setString(1, status.getCode());
            } else {
                //
                // find all batches of a specific status since a specific date
                //
                //ps = conn.prepareStatement(query + "and b.version in (select distinct(version) from batch_history where status=? and status_date>?)" + orderBy);
                //ps.setString(1, status.getCode());
                //ps.setTimestamp(2, new java.sql.Timestamp(since.getTime()));
            }
            int lastVersion = -1;
            BatchModel bm = null;
            rs = ps.executeQuery();
            while (rs.next()) {
                int version = rs.getInt(1);
                if (version != lastVersion) {
                    bm = new BatchModel();
                    bm.setBatchNumber(version);
                    bm.setDescription(rs.getString(2));
                    batches.add(bm);
                    lastVersion = version;
                }
                BatchHistoryModel bhm = new BatchHistoryModel();
                bhm.setStatus(EnumBatchStatus.getStatus(rs.getString(3)));
                bhm.setStatusDate(rs.getTimestamp(4));
                bhm.setUser(rs.getString(5));
                bhm.setNotes(rs.getString(6));
                bm.updateStatus(bhm);
            }
        } catch (SQLException sqle) {
            LOGGER.error("Unable to find batches", sqle);
            throw new LoaderException(sqle);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException sqle) {
                LOGGER.error("Unable to close db connection", sqle);
                throw new LoaderException(sqle);
            }
            return batches;
        }
    }
     */
    
    /*
    public void updateStatus(int batchNumber, String user, EnumBatchStatus status, String notes)
        throws LoaderException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("insert into erps.batch_history (version,user,status,notes,status_date) values (?,?,?,?,SYSDATE)");
            ps.setInt(1, batchNumber);
            ps.setString(2, user);
            ps.setString(3, status.getCode());
            ps.setString(4, notes);
            int rows = ps.executeUpdate();
            if (rows != 1)
                throw new LoaderException("Unable to insert a row in BATCH_HISTORY");
        } catch (SQLException sqle) {
            LOGGER.error("Unable to insert a row in BATCH_HISTORY", sqle);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException sqle) {
                LOGGER.error("Unable to close db connection", sqle);
            }
        }
    }
     */
    
    public BatchModel getBatch(int batchNumber)
    throws EJBException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("select version, date_created, approval_status from erps.history where version=?");
            ps.setInt(1, batchNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                BatchModel bm = new BatchModel();
                bm.setBatchNumber(rs.getInt(1));
                bm.setDescription("");
                BatchHistoryModel bhm = new BatchHistoryModel();
                bhm.setStatusDate(rs.getTimestamp(2));
                String status = rs.getString(3);
                if ("L".equalsIgnoreCase(status))
                    bhm.setStatus(EnumBatchStatus.EMS_LOADING);
                else if ("N".equalsIgnoreCase(status))
                    bhm.setStatus(EnumBatchStatus.EMS_NEW);
                else
                    bhm.setStatus(EnumBatchStatus.EMS_REJ);
                bhm.setUser("");
                bhm.setNotes("");
                bm.updateStatus(bhm);
                return bm;
            } else {
                return null;
            }
        } catch (SQLException sqle) {
            LOGGER.error("Unable to find batch " + batchNumber, sqle);
            throw new EJBException(sqle);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException sqle) {
                LOGGER.error("Unable to close db connection", sqle);
                throw new EJBException(sqle);
            }
        }
    }
    
    public Collection getBatches()
    throws EJBException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("select version, date_created, approval_status from erps.history order by version desc");
            rs = ps.executeQuery();
            Collection retval = processResultSet(rs);
            rs.close();
            ps.close();
            return retval;
        } catch (SQLException sqle) {
            LOGGER.error("Unable to find batches", sqle);
            throw new EJBException(sqle);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException sqle) {
                LOGGER.error("Unable to close db connection", sqle);
                throw new EJBException(sqle);
            }
        }
    }
    
    private Collection processResultSet(ResultSet rs) throws SQLException {
        
        ArrayList batches = new ArrayList();
        while (rs.next()) {
            BatchModel bm = new BatchModel();
            bm.setBatchNumber(rs.getInt(1));
            bm.setDescription("");
            BatchHistoryModel bhm = new BatchHistoryModel();
            bhm.setStatusDate(rs.getTimestamp(2));
            String status = rs.getString(3);
            if ("N".equalsIgnoreCase(status))
                bhm.setStatus(EnumBatchStatus.EMS_NEW);
            else
                bhm.setStatus(EnumBatchStatus.EMS_REJ);
            bhm.setUser("");
            bhm.setNotes("");
            bm.updateStatus(bhm);
            batches.add(bm);
        }
        return batches;
        
    }
    
    
    public Collection getRecentBatches()
    throws EJBException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("select * from (select version, date_created, approval_status from erps.history order by version desc) where rownum < 100");
            rs = ps.executeQuery();
            Collection retval = processResultSet(rs);
            rs.close();
            ps.close();
            return retval;
        } catch (SQLException sqle) {
            LOGGER.error("Unable to find batches", sqle);
            throw new EJBException(sqle);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException sqle) {
                LOGGER.error("Unable to close db connection", sqle);
                throw new EJBException(sqle);
            }
        }
    }
    
}
