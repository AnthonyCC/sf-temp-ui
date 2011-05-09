/**
 * @author ekracoff
 * Created on Jan 20, 2005*/

package com.freshdirect.cms.application.service.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponse;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.MediaServiceI;
import com.freshdirect.cms.application.service.AbstractContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.listeners.Media;
import com.freshdirect.cms.node.ContentNode;
import com.freshdirect.cms.util.DaoUtil;

public class MediaService extends AbstractContentService implements MediaServiceI {

	private final DataSource dataSource;
	private final ContentTypeServiceI typeService;

	public MediaService(DataSource dataSource) {
		this.dataSource = dataSource;
		this.typeService = new XmlTypeService("classpath:/com/freshdirect/cms/resource/MediaDef.xml");
	}

	public Set<ContentKey> getContentKeysByType(ContentType type) {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id from cms_media where type = ?");
			ps.setString(1, type.getName());
			ResultSet rs = ps.executeQuery();
			Set<ContentKey> keys = new HashSet<ContentKey>();
			while (rs.next()) {
				ContentKey key = new ContentKey(type, rs.getString("ID"));
				keys.add(key);
			}
			rs.close();
			ps.close();
			return keys;
		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
			close(conn);
		}
	}

	public Set<ContentKey> getContentKeys() {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, type from cms_media");
			ResultSet rs = ps.executeQuery();
			Set<ContentKey> keys = new HashSet<ContentKey>();
			while (rs.next()) {
				ContentKey key = new ContentKey(ContentType.get(rs.getString("TYPE")), rs.getString("ID"));
				keys.add(key);
			}
			rs.close();
			ps.close();
			return keys;
		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
			close(conn);
		}
	}

	public Set getParentKeys(ContentKey key) {
		// TODO implement
		return Collections.EMPTY_SET;
	}

	public ContentNodeI getContentNode(ContentKey cKey) {
		if (typeService.getContentTypeDefinition(cKey.getType()) == null) {
			return null;
		}

		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, uri, type, width, height, mime_type, last_modified from cms_media where id = ?");
			ps.setString(1, cKey.getId());
			ResultSet rs = ps.executeQuery();
			ContentNodeI node = null;
			if (rs.next()) {
				node = processNode(conn, rs);
			}
			rs.close();
			ps.close();
			return node;
		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
			close(conn);
		}
	}

	private static String QUERY_MEDIA_MANY = "select /*+ FIRST_ROWS */ id, uri, type, width, height, mime_type, last_modified from cms_media where id in (?)";

	/** @return Set of keys with known content types */
	private Set<ContentKey> filterKeys(Set<ContentKey> keys) {
		Set<ContentKey> k = new HashSet<ContentKey>(keys.size());
		Set<ContentType> types = typeService.getContentTypes();

		for (ContentKey key : keys) {
			if (types.contains(key.getType())) {
				k.add(key);
			}
		}
		return k;
	}

	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys) {
		keys = filterKeys(keys);
		if (keys.isEmpty()) {
			return Collections.EMPTY_MAP;
		}

		Connection conn = null;
		try {
			conn = getConnection();

			String[] idChunks = DaoUtil.chunkContentKeys(keys, false);

			Map<ContentKey, ContentNodeI> nodeMap = new HashMap<ContentKey, ContentNodeI>(keys.size());

			for (int i = 0; i < idChunks.length; i++) {
				String query = StringUtils.replace(QUERY_MEDIA_MANY, "?", idChunks[i]);
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					ContentNodeI node = processNode(conn, rs);
					nodeMap.put(node.getKey(), node);
				}

				rs.close();
				ps.close();
			}

			return nodeMap;

		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
		    close(conn);
		}
	}

	public ContentNodeI createPrototypeContentNode(ContentKey cKey) {
		if (getTypeService().getContentTypeDefinition(cKey.getType()) == null) {
			return null;
		}
		ContentNodeI node = new ContentNode(this, cKey);
		ContentType type = cKey.getType();

		node.setAttributeValue("path", "");
		if (FDContentTypes.IMAGE.equals(type)) {
			node.setAttributeValue("width", 0);
			node.setAttributeValue("height", 0);
		}
		return node;
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	private ContentNodeI processNode(Connection conn, ResultSet rs) throws SQLException {
		ContentType type = ContentType.get(rs.getString("TYPE"));
		ContentKey key = new ContentKey(type, rs.getString("ID"));

		ContentNodeI node = new ContentNode(this, key);

		String uri = rs.getString("URI");
		node.setAttributeValue("path", uri);
		
		node.setAttributeValue("mimeType", rs.getString("MIME_TYPE"));
                node.setAttributeValue("lastModified", rs.getDate("LAST_MODIFIED"));

		if (FDContentTypes.IMAGE.equals(type)) {
			node.setAttributeValue("width", rs.getInt("WIDTH"));
			node.setAttributeValue("height", rs.getInt("HEIGHT"));

		} else if (FDContentTypes.MEDIAFOLDER.equals(type)) {

			Set<ContentKey> children = queryChildren(conn, uri);

			List<ContentKey> folders = new ArrayList<ContentKey>();
			List<ContentKey> files = new ArrayList<ContentKey>();
			for (Iterator<ContentKey> i = children.iterator(); i.hasNext();) {
				ContentKey child = i.next();
				if (FDContentTypes.MEDIAFOLDER.equals(child.getType())) {
					folders.add(child);
				} else {
					files.add(child);
				}
			}
			node.setAttributeValue("subFolders", folders);
			node.setAttributeValue("files", files);
		}
		return node;
	}

	/** @return Set of ContentKey */
	private Set<ContentKey> queryChildren(Connection conn, String uri) throws SQLException {
		// FIXME a parent column would be handy for querying immediate children
		PreparedStatement ps = conn
			.prepareStatement("select id, type from cms_media where uri like ? and instr(uri, '/', ?) = 0 and uri != '/'");
		String basePath = uri.endsWith("/") ? uri + "%" : uri + "/%";
		ps.setString(1, basePath);
		ps.setInt(2, basePath.length());

		ResultSet rs = ps.executeQuery();

		Set<ContentKey> children = new HashSet<ContentKey>();
		while (rs.next()) {
			ContentKey key = new ContentKey(ContentType.get(rs.getString("TYPE")), rs.getString("ID"));
			children.add(key);
		}

		rs.close();
		ps.close();
		return children;
	}

	public CmsResponseI handle(CmsRequestI request) {
	    Connection conn = null;
	    try {
	        conn = getConnection();
	        for (Iterator i = request.getNodes().iterator(); i.hasNext();) {
	            ContentNodeI node = (ContentNodeI) i.next();
	            storeContentNode(conn, node);
	        }
	        return new CmsResponse();
	        
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                throw new CmsRuntimeException(e);
            } finally {
                close(conn);
            }

	}

	private void storeContentNode(Connection conn, ContentNodeI node) throws SQLException {
	    persist(conn, node.getKey().getType(), node.getKey().getId(), (String) node.getAttributeValue("path"), (Integer) node.getAttributeValue("width"), (Integer) node.getAttributeValue("height"), (String) node.getAttributeValue("mimeType"), (Date) node.getAttributeValue("lastModified"));
	}

    private void persist(Connection conn, ContentType type, String id, String path, Integer width, Integer height, String mimeType, Date lastModified)
            throws SQLException {
        if (!FDContentTypes.IMAGE.equals(type)) {
            width = null;
            height = null;
        }
        if (FDContentTypes.MEDIAFOLDER.equals(type)) {
            mimeType = null;
        }
        if (lastModified == null) {
            lastModified = new Date();
        }
        if (mimeType == null) {
            mimeType = Media.getDefaultMimeType(type, path);
        }
        
        PreparedStatement ps = conn.prepareStatement("UPDATE CMS.Media SET uri=?,width=?,height=?,type=?,mime_type=?,last_modified=? WHERE id = ?");
        
        setupStatement(type, id, path, width, height, mimeType, lastModified, ps);
        
        int result = ps.executeUpdate();
        ps.close();
        if (result == 0) {
            ps = conn.prepareStatement("INSERT INTO CMS.Media(uri,width,height,type,mime_type,last_modified,id) values(?,?,?,?,?,?,?)");
            setupStatement(type, id, path, width, height, mimeType, lastModified, ps);
            ps.executeUpdate();
            ps.close();
        }
    }

    /**
     * @param type
     * @param id
     * @param path
     * @param width
     * @param height
     * @param mimeType
     * @param lastModified
     * @param ps
     * @throws SQLException
     */
    protected void setupStatement(ContentType type, String id, String path, Integer width, Integer height, String mimeType, Date lastModified,
            PreparedStatement ps) throws SQLException {
        ps.setString(1, path);
        if (width != null) {
            ps.setInt(2, width.intValue());
        } else {
            ps.setNull(2, Types.INTEGER);
        }
        if (height != null) {
            ps.setInt(3, height.intValue());
        } else {
            ps.setNull(3, Types.INTEGER);
        }
        ps.setString(4, type.getName());
        ps.setString(5, mimeType);
        ps.setDate(6, new java.sql.Date(lastModified.getTime()));
        ps.setString(7, id);
    }

    public ContentTypeServiceI getTypeService() {
		return typeService;
	}

	@Override
	public ContentNodeI getContentNode(String uri) {
		if (uri == null) {
			return null;
		}

		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, uri, type, width, height, mime_type, last_modified from cms_media where uri = ? order by uri");
			ps.setString(1, uri);
			ResultSet rs = ps.executeQuery();
			ContentNodeI node = null;
			if (rs.next()) {
				node = processNode(conn, rs);
			}
			rs.close();
			ps.close();
			return node;
		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
			close(conn);
		}
	}

    /**
     * @param conn
     */
    protected void close(Connection conn) {
        try {
        	if (conn != null) {
        		conn.close();
        	}
        } catch (SQLException e1) {
        	throw new CmsRuntimeException(e1);
        }
    }
}