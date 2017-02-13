/**
 * @author ekracoff
 * Created on Jan 24, 2005*/
package com.freshdirect.cms.changecontrol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;

/**
 * DAO implementation for {@link com.freshdirect.cms.changecontrol.DbChangeLogService}.
 */
class ChangeSetDao {

	private static String SELECT = "SELECT cs.ID AS changeset_id, cs.TIMESTAMP, cs.user_id, cs.note, cnc.changetype, cnc.contenttype, cnc.contentnode_id, cd.ATTRIBUTENAME, cd.oldvalue, cd.newvalue, cnc.ID as CHANGENODE_ID "
		+ "FROM cms_changeset cs, cms_contentnodechange cnc, cms_changedetail cd "
		+ "WHERE cs.ID=cnc.changeset_id "
		+ "AND cnc.ID = cd.CONTENTNODECHANGE_ID(+) ";

	private static String ORDER_BY = "ORDER BY cs.ID, contentnode_id";

	public ChangeSet retrieve(Connection conn, String id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(SELECT + "AND cs.id = ? " + ORDER_BY);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();

		ChangeSet result = buildChangeSets(rs).get(0);
		ps.close();
		return result;
	}

	public List<ChangeSet> getChangeHistory(Connection conn, ContentKey key) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(SELECT + "AND cnc.contenttype = ? AND cnc.contentnode_id = ? " + ORDER_BY);
		ps.setString(1, key.getType().getName());
		ps.setString(2, key.getId());
		ResultSet rs = ps.executeQuery();
		
		List<ChangeSet> result = buildChangeSets(rs);
		ps.close();
		return result;
	}

	public List<ChangeSet> getChangesBetween(Connection conn, Date startDate, Date endDate) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(SELECT + "AND cs.timestamp between ? and ? " + ORDER_BY);
		ps.setTimestamp(1, new Timestamp(startDate.getTime()));
		ps.setTimestamp(2, new Timestamp(endDate.getTime()));
		ResultSet rs = ps.executeQuery();

		List<ChangeSet> result = buildChangeSets(rs);
		ps.close();
		return result;
	}

	public List<ChangeSet> getChangesByUser(Connection conn, String userId ) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(SELECT + "AND user_id = ? " + ORDER_BY);
		ps.setString(1, userId);
		ResultSet rs = ps.executeQuery();

		List<ChangeSet> result = buildChangeSets(rs);
		ps.close();
		return result;
	}
	
	public List<ChangeSet> getChangeSets( Connection conn, ContentKey contentKey, String userId, Date startDate, Date endDate ) throws SQLException {

		StringBuilder query = new StringBuilder( SELECT );
		if ( contentKey != null ) {
			query.append( "AND cnc.contenttype = ? AND cnc.contentnode_id = ? " );
		}
		if ( userId != null ) {
			query.append( "AND user_id = ? " );
		}
		if ( startDate != null && endDate != null ) {
			// both dates are set
			query.append( "AND cs.timestamp between ? and ? " );
		} else if ( startDate != null ) {
			// only startDate is set
			query.append( "AND cs.timestamp > ? " );			
		} else if ( endDate != null ) {
			// only endDate is set
			query.append( "AND cs.timestamp < ? " );			
		}
		query.append( ORDER_BY );
		
		Logger.getLogger( this.getClass() ).info( "QUERY = " + query.toString() );

		PreparedStatement ps = conn.prepareStatement( query.toString() );
		int paramCounter = 1;

		if ( contentKey != null ) {
			ps.setString( paramCounter++, contentKey.getType().getName() );
			ps.setString( paramCounter++, contentKey.getId() );
		}
		if ( userId != null ) {
			ps.setString( paramCounter++, userId );
		}
		if ( startDate != null && endDate != null ) {
			ps.setTimestamp( paramCounter++, new Timestamp( startDate.getTime() ) );
			ps.setTimestamp( paramCounter++, new Timestamp( endDate.getTime() ) );
		} else if ( startDate != null ) {
			ps.setTimestamp( paramCounter++, new Timestamp( startDate.getTime() ) );
		} else if ( endDate != null ) {
			ps.setTimestamp( paramCounter++, new Timestamp( endDate.getTime() ) );
		}

		ResultSet rs = ps.executeQuery();
		List<ChangeSet> result = buildChangeSets(rs);
		
		ps.close();
		return result;
	}	
	
	private List<ChangeSet> buildChangeSets(ResultSet rs) throws SQLException {
		List<ChangeSet> sets = new ArrayList<ChangeSet>();
		String prevNodeId = null;
		String prevSetId = null;

		ChangeSet cs = null;
		ContentNodeChange cnc = null;
		while (rs.next()) {
			if ( !rs.getObject("CHANGESET_ID").equals(prevSetId) ) {
				cs = new ChangeSet();
				cs.setPK(new PrimaryKey(rs.getString("CHANGESET_ID")));
				cs.setUserId(rs.getString("USER_ID"));
				cs.setModifiedDate(new Date(rs.getTimestamp("TIMESTAMP").getTime()));
				cs.setNote(rs.getString("NOTE"));
				sets.add(cs);
				prevSetId = rs.getString("CHANGESET_ID");
			}

			if ( !rs.getString("CHANGENODE_ID").equals(prevNodeId) && cs != null ) {
				cnc = new ContentNodeChange();
				cnc.setContentKey(ContentKey.getContentKey(ContentType.get(rs.getString("CONTENTTYPE")), rs.getString("CONTENTNODE_ID")));
				cnc.setChangeType(EnumContentNodeChangeType.getType(rs.getString("CHANGETYPE")));
				cnc.setChangeSet(cs);
				cs.addChange(cnc);
				prevNodeId = rs.getString("CHANGENODE_ID");
			}

			if ( rs.getString("ATTRIBUTENAME") != null && cnc != null ) {
				ChangeDetail cd = new ChangeDetail();
				cd.setAttributeName(rs.getString("ATTRIBUTENAME"));
				cd.setOldValue(rs.getString("OLDVALUE"));
				cd.setNewValue(rs.getString("NEWVALUE"));
				cnc.addDetail(cd);
			}
		}
		return sets;
	}

	public String store(Connection conn, ChangeSet changeSet) throws SQLException {
		conn.setAutoCommit(false);
		String key = getNextId(conn);
		PreparedStatement ps = conn.prepareStatement("Insert into cms_changeset(ID,TIMESTAMP,USER_ID,NOTE) values (?,?,?,?)");
		ps.setString(1, key);
		ps.setTimestamp(2, new java.sql.Timestamp(changeSet.getModifiedDate().getTime()));
		ps.setString(3, changeSet.getUserId());
		ps.setString(4, changeSet.getNote());
		ps.execute();
		ps.close();

		insertNodeChanges(conn, changeSet.getNodeChanges(), key);
		conn.commit();
		return key;
	}

	private void insertNodeChanges(Connection conn, List<ContentNodeChange> contentNodeChanges, String changeSetId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("Insert into cms_contentnodechange(id,changeset_id,contentnode_id,changetype,contenttype) values(?,?,?,?,?)");
		for ( ContentNodeChange nc : contentNodeChanges ) {
			String key = getNextId(conn);
			ps.setString(1, key);
			ps.setString(2, changeSetId);
			ps.setString(3, nc.getContentKey().getId());
			ps.setString(4, nc.getChangeType().getCode());
			ps.setString(5, nc.getContentKey().getType().getName());
			ps.execute();
			insertBatchDetails(conn, nc.getChangeDetails(), key);
		}
		ps.close();
	}

	private void insertBatchDetails(Connection conn, List<ChangeDetail> changeDetails, String nodeChangeId) throws SQLException {
		PreparedStatement ps = conn
			.prepareStatement("Insert into cms_changedetail(contentnodechange_id,attributename,changetype,oldvalue,newvalue) values (?,?,?,?,?)");

		for (ChangeDetail cd : changeDetails) {
			ps.setString(1, nodeChangeId);
			ps.setString(2, cd.getAttributeName());
			ps.setString(3, "MOD");
			ps.setString(4, StringUtils.left(cd.getOldValue(), 2000));
			ps.setString(5, StringUtils.left(cd.getNewValue(), 2000));
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}

	protected String getNextId(Connection conn) throws SQLException {
		return SequenceGenerator.getNextIdFromSequence(conn, "CMS_SYSTEM_SEQ");
	}

}