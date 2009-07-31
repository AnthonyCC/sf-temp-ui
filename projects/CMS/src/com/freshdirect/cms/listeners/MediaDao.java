/**
 * @author ekracoff
 * Created on Dec 17, 2004*/

package com.freshdirect.cms.listeners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentType;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;

public class MediaDao {
	private static final Logger LOGGER = Logger.getLogger(MediaDao.class);

	public Media insert(Connection conn, Media media) throws SQLException {
    	LOGGER.debug("-->insert()");
		PrimaryKey key = new PrimaryKey(getNextId(conn));
		Media m = new Media(key, media.getUri(), media.getType(), media.getWidth(), media.getHeight(), media.getMimeType(), media.getLastModified());

		PreparedStatement ps = conn
			.prepareStatement(
					"MERGE INTO CMS_MEDIA A " +
						"USING (SELECT ? ID, ? URI, ? WIDTH, ? HEIGHT, ? TYPE, ? MIME_TYPE, ? LAST_MODIFIED FROM DUAL) B " +
						"ON (A.URI = B.URI) " +
						"WHEN MATCHED THEN " +
							"UPDATE SET A.WIDTH = B.WIDTH, A.HEIGHT = B.HEIGHT, A.TYPE = B.TYPE, A.MIME_TYPE = B.MIME_TYPE, " +
								"A.LAST_MODIFIED = B.LAST_MODIFIED " +
						"WHEN NOT MATCHED THEN " +
							"INSERT (A.ID, A.URI, A.WIDTH, A.HEIGHT, A.TYPE, A.MIME_TYPE, A.LAST_MODIFIED) " +
							"VALUES (B.ID, B.URI, B.WIDTH, B.HEIGHT, B.TYPE, B.MIME_TYPE, B.LAST_MODIFIED)");

		ps.setString(1, m.getPK().getId());
		ps.setString(2, m.getUri());

		if(m.getWidth() == null){
			ps.setNull(3, Types.INTEGER);
		} else {
			ps.setInt(3, m.getWidth().intValue());
		}
		
		if(m.getHeight() == null){
			ps.setNull(4, Types.INTEGER);
		} else {
			ps.setInt(4, m.getHeight().intValue());
		}
		
		ps.setString(5, m.getType().getName());
		ps.setString(6, m.getMimeType());
		ps.setTimestamp(7, new Timestamp(m.getLastModified().getTime()));
		
		ps.execute();
		ps.close();
    	LOGGER.debug("<--insert()");
		return m;
	}

	public void update(Connection conn, Media media) throws SQLException {
    	LOGGER.debug("-->update()");
		PreparedStatement ps = conn.prepareStatement("Update CMS_Media set Uri=?,width=?,height=?,type=?,mime_type=?,last_modified=? Where uri = ?");
		ps.setString(1, media.getUri());
		
		if(media.getWidth() == null){
			ps.setNull(2, Types.INTEGER);
		} else {
			ps.setInt(2, media.getWidth().intValue());
		}
		
		if(media.getHeight() == null){
			ps.setNull(3, Types.INTEGER);
		} else {
			ps.setInt(3, media.getHeight().intValue());
		}
		
		ps.setString(4, media.getType().getName());
		ps.setString(5, media.getMimeType());
		ps.setTimestamp(6, new Timestamp(media.getLastModified().getTime()));
		ps.setString(7, media.getUri());
		
		ps.executeUpdate();
		ps.close();
    	LOGGER.debug("<--update()");
	}

	public Media lookup(Connection conn, String id) throws SQLException {
    	LOGGER.debug("-->lookup()");
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CMS_MEDIA WHERE id = ?");

		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();

		Media media = null;
		if (rs.next()) {
			media = new Media(
				new PrimaryKey(rs.getString("ID")),
				rs.getString("URI"),
				ContentType.get(rs.getString("TYPE")),
				new Integer(rs.getInt("WIDTH")),
				new Integer(rs.getInt("HEIGHT")),
				rs.getString("mime_type"),
				rs.getTimestamp("last_modified"));
		}
		rs.close();
		ps.close();
    	LOGGER.debug("<--lookup()");
		return media;
	}
	
	public Media lookupByUri(Connection conn, String uri) throws SQLException {
    	LOGGER.debug("-->lookupByUri()");
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CMS_MEDIA WHERE uri = ?");

		ps.setString(1, uri);
		ResultSet rs = ps.executeQuery();

		Media media = null;
		if (rs.next()) {
			media = new Media(
				new PrimaryKey(rs.getString("ID")),
				rs.getString("URI"),
				ContentType.get(rs.getString("TYPE")),
				new Integer(rs.getInt("WIDTH")),
				new Integer(rs.getInt("HEIGHT")),
				rs.getString("mime_type"),
				rs.getTimestamp("last_modified"));
		}
		rs.close();
		ps.close();
    	LOGGER.debug("<--lookupByUri()");
		return media;
	}
	

	public void delete(Connection conn, String uri) throws SQLException {
    	LOGGER.debug("-->delete()");
		PreparedStatement ps = conn.prepareStatement("Delete from CMS_media where uri = ? or uri like ?");
		ps.setString(1, uri);
		ps.setString(2, uri + "/%");
		ps.execute();
		ps.close();
		conn.commit();
    	LOGGER.debug("<--delete()");
	}
	
	public void move(Connection conn, String sourceUri, String targetUri) throws SQLException{
    	LOGGER.debug("-->move()");
		PreparedStatement ps = conn.prepareStatement("Update CMS_media set uri = REPLACE(URI,?,?), last_modified = sysdate WHERE URI = ? or URI LIKE ?");
		ps.setString(1, sourceUri);
		ps.setString(2, targetUri);
		ps.setString(3, sourceUri);
		ps.setString(4, sourceUri + "/%");
		
		ps.execute();
		conn.commit();
		ps.close();
    	LOGGER.debug("<--move()");
	}
	
	public void copy(Connection conn, String sourceUri, String targetUri) throws SQLException{
    	LOGGER.debug("-->copy()");
		PreparedStatement ps = conn.prepareStatement(
			"INSERT INTO CMS_MEDIA (ID, URI, width, height, TYPE, mime_type, last_modified) " + 
			"SELECT CMS_system_seq.NEXTVAL, REPLACE(URI,?,?), width, height, TYPE, mime_type, sysdate FROM cms_MEDIA WHERE URI = ? or URI LIKE ?");
		
		ps.setString(1, sourceUri);
		ps.setString(2, targetUri);
		ps.setString(3, sourceUri);
		ps.setString(4, sourceUri + "/%");
		
		ps.execute();
		conn.commit();
		ps.close();
    	LOGGER.debug("<--copy()");
	}
	
	private static String getNextId(Connection conn) throws SQLException {
    	LOGGER.debug("-->getNextId()");
		String nextIdFromSequence = SequenceGenerator.getNextIdFromSequence(conn, "CMS_SYSTEM_SEQ");
    	LOGGER.debug("<--getNextId()");
		return nextIdFromSequence;
	}


}