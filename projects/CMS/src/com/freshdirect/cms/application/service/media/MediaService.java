/**
 * @author ekracoff
 * Created on Jan 20, 2005*/

package com.freshdirect.cms.application.service.media;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.MediaServiceI;
import com.freshdirect.cms.application.service.AbstractContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNode;
import com.freshdirect.cms.util.DaoUtil;

public class MediaService extends AbstractContentService implements MediaServiceI {

    private final DataSource dataSource;
    private final ContentTypeServiceI typeService;

    public MediaService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.typeService = new XmlTypeService("classpath:/com/freshdirect/cms/resource/MediaDef.xml");
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
        Connection conn = null;
        try {
            conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id from cms_media where type = ?");
            ps.setString(1, type.getName());
            ResultSet rs = ps.executeQuery();
            Set<ContentKey> keys = new HashSet<ContentKey>();
            while (rs.next()) {
                ContentKey key = ContentKey.getContentKey(type, rs.getString("ID"));
                keys.add(key);
            }
            rs.close();
            ps.close();
            return keys;
        } catch (SQLException e) {
            throw new CmsRuntimeException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e1) {
                throw new CmsRuntimeException(e1);
            }
        }
    }

    @Override
    public Set<ContentKey> getContentKeys(DraftContext draftContext) {
        Connection conn = null;
        try {
            conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, type from cms_media");
            ResultSet rs = ps.executeQuery();
            Set<ContentKey> keys = new HashSet<ContentKey>();
            while (rs.next()) {
                ContentKey key = ContentKey.getContentKey(ContentType.get(rs.getString("TYPE")), rs.getString("ID"));
                keys.add(key);
            }
            rs.close();
            ps.close();
            return keys;
        } catch (SQLException e) {
            throw new CmsRuntimeException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e1) {
                throw new CmsRuntimeException(e1);
            }
        }
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
        // TODO implement
        return Collections.EMPTY_SET;
    }

    @Override
    public ContentNodeI getContentNode(ContentKey cKey, DraftContext draftContext) {
        if (typeService.getContentTypeDefinition(cKey.getType()) == null) {
            return null;
        }

        Connection conn = null;
        try {
            conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, uri, type, width, height, last_modified from cms_media where id = ?");
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
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e1) {
                throw new CmsRuntimeException(e1);
            }
        }
    }

	private static String QUERY_MEDIA_MANY = "select /*+ FIRST_ROWS */ id, uri, type, width, height, last_modified from cms_media where id in (?)";

    /** @return Set of keys with known content types */
    private Set<ContentKey> filterKeys(Set<ContentKey> keys) {
        Set<ContentKey> k = new HashSet<ContentKey>(keys.size());
        Set<ContentType> types = typeService.getContentTypes();

        for (Iterator i = keys.iterator(); i.hasNext();) {
            ContentKey key = (ContentKey) i.next();
            if (types.contains(key.getType())) {
                k.add(key);
            }
        }
        return k;
    }

    @Override
    public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
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
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new CmsRuntimeException(e);
                }
            }
        }
    }

    @Override
    public ContentNodeI createPrototypeContentNode(ContentKey cKey, DraftContext draftContext) {
        if (getTypeService().getContentTypeDefinition(cKey.getType()) == null) {
            return null;
        }
        ContentNodeI node = new ContentNode(this, cKey);
        ContentType type = cKey.getType();

        node.getAttribute("path").setValue("");
        if (FDContentTypes.IMAGE.equals(type)) {
            node.getAttribute("width").setValue(new Integer(0));
            node.getAttribute("height").setValue(new Integer(0));
        }
        return node;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private ContentNodeI processNode(Connection conn, ResultSet rs) throws SQLException {
        ContentType type = ContentType.get(rs.getString("TYPE"));
        ContentKey key = ContentKey.getContentKey(type, rs.getString("ID"));

        ContentNodeI node = new ContentNode(this, key);

        String uri = rs.getString("URI");
        node.getAttribute("path").setValue(uri);
        String lastModDate = new Date().toString();
        if (FDContentTypes.IMAGE.equals(type)) {
            node.getAttribute("width").setValue(new Integer(rs.getInt("WIDTH")));
            node.getAttribute("height").setValue(new Integer(rs.getInt("HEIGHT")));
            if (rs.getDate("last_modified") != null) {
                lastModDate = rs.getDate("last_modified").toString();
            }
            node.getAttribute("lastmodified").setValue(lastModDate);

        } else if (FDContentTypes.MEDIAFOLDER.equals(type)) {

            Set<ContentKey> children = queryChildren(conn, uri);

            List<ContentKey> folders = new ArrayList<ContentKey>();
            List<ContentKey> files = new ArrayList<ContentKey>();
            for (Iterator<ContentKey> i = children.iterator(); i.hasNext();) {
                ContentKey child = (ContentKey) i.next();
                if (FDContentTypes.MEDIAFOLDER.equals(child.getType())) {
                    folders.add(child);
                } else {
                    files.add(child);
                }
            }
            node.getAttribute("subFolders").setValue(folders);
            node.getAttribute("files").setValue(files);
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
            ContentKey key = ContentKey.getContentKey(ContentType.get(rs.getString("TYPE")), rs.getString("ID"));
            children.add(key);
        }

        rs.close();
        ps.close();
        return children;
    }

    @Override
    public CmsResponseI handle(CmsRequestI request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
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
			PreparedStatement ps = conn.prepareStatement("select id, uri, type, width, height, last_modified from cms_media where uri = ? order by uri");
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
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e1) {
                throw new CmsRuntimeException(e1);
            }
        }
    }

    @Override
    public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
        return getContentNode(key, draftContext);
    }
}
