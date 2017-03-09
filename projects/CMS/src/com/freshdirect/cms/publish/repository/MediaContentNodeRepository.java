package com.freshdirect.cms.publish.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.service.media.MediaService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.publish.config.DatabaseConfig;
import com.freshdirect.cms.publish.service.ContentNodeGeneratorService;
import com.freshdirect.cms.publish.util.TaskTimer;


public final class MediaContentNodeRepository extends JdbcRepository {

    private static final Logger LOGGER = Logger.getLogger(MediaContentNodeRepository.class);

    private static final String MEDIA_ATTR_PATH_NAME = "path"; 

    private static final String MEDIA_ATTR_FOLDERS_NAME = "subFolders"; 

    private static final String MEDIA_ATTR_FILES_NAME = "files"; 

    /**
     * The amount of expected content nodes
     */
    private static final int NODES_SIZE = 263919;

    private static final String FETCH_NODES_SIZE_SQL =
            "select count(*) from cms_media";
    
    private static final String FETCH_ALL_MEDIA_SQL = 
            "select id, uri, type, width, height, last_modified from cms_media";
    
    private static final String FETCH_MEDIA_URIS_SQL =
            "select uri " +
            "from cms_media " +
            "where last_modified > ( " +
            "  select max(timestamp) " +
            "  from cms_publish " +
            "  where STATUS='COMPLETE' " +
            ") and type != 'MediaFolder'";

    private final ContentNodeGeneratorService generatorService;

    public MediaContentNodeRepository(ContentNodeGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public Collection<ContentNodeI> findAll() {
        return loadAllNodes();
    }


    /**
     * Fetch URIs of media items changed since last successful publish
     * 
     * @return list of media URIs
     */
    public List<String> fetchMediaURIs() {
        List<String> result = new ArrayList<String>();
        
        TaskTimer timer = null;
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            
            stmt = createFastReaderStatement(conn);
            
            timer = new TaskTimer("Media URIs");
            rs = stmt.executeQuery(FETCH_MEDIA_URIS_SQL);
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            rs.close();
            rs = null;
            timer.logTimeSpent(LOGGER, "Loaded " + result.size() + " media URIs");

        } catch (SQLException exc) {
            LOGGER.error("DB error raised during building media nodes", exc);
        } finally {
            closeResources(rs, stmt, conn);
        }

        return result;
    }
    
    private Collection<ContentNodeI> loadAllNodes() {
        Map<ContentKey, ContentNodeI> nodeMap = null;
        Map<String,ContentNodeI> folderMap = new HashMap<String,ContentNodeI>();

        TaskTimer timer = null;
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            
            stmt = createFastReaderStatement(conn);

            // Init round: calculate node size
            int nodesSize = NODES_SIZE;
            rs = stmt.executeQuery(FETCH_NODES_SIZE_SQL);
            if (rs.next()) {
                nodesSize = rs.getInt(1);
            }
            rs.close();
            nodeMap = new HashMap<ContentKey, ContentNodeI>(nodesSize);


            // First round: load all media nodes
            timer = new TaskTimer("Media nodes");
            rs = stmt.executeQuery(FETCH_ALL_MEDIA_SQL);
            while (rs.next()) {
                ContentNodeI node = processNode(conn, rs);
                if (FDContentTypes.MEDIAFOLDER.equals(node.getKey().getType())) {
                    folderMap.put((String) node.getAttributeValue(MEDIA_ATTR_PATH_NAME), node);
                }
                nodeMap.put(node.getKey(), node);
            }
            rs.close();
            rs = null;
            timer.logTimeSpent(LOGGER, "Loaded " + nodeMap.size() + " media nodes");

            // Second round: build 'filesystem structure'
            timer = new TaskTimer("Build parent-child relationships");
            buildFolderStructure(nodeMap, folderMap);
            timer.logTimeSpent(LOGGER, null);

            stmt.close();
            stmt = null;
        } catch (SQLException exc) {
            LOGGER.error("DB error raised during building media nodes", exc);
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return nodeMap.values();
    }

    @SuppressWarnings("unchecked")
    private void buildFolderStructure(Map<ContentKey, ContentNodeI> nodeMap, Map<String, ContentNodeI> folderMap) {
        for (final ContentNodeI node : nodeMap.values()) {
            final String path = (String) node.getAttributeValue(MEDIA_ATTR_PATH_NAME);
            
            // root path is skipped 
            if (path.length() <= 1) {
                continue;
            }

            final int parentSepIndex = path.lastIndexOf("/");
            final String parentPath = parentSepIndex == 0 ? "/" : path.substring(0, parentSepIndex);
            final ContentNodeI parentFolder = folderMap.get(parentPath);

            if (parentFolder == null) {
                LOGGER.warn("Parent folder not found for item: " + path);
                continue;
            }

            if (FDContentTypes.MEDIAFOLDER.equals(node.getKey().getType())) {
                ((List<ContentKey>)parentFolder.getAttributeValue(MEDIA_ATTR_FOLDERS_NAME)).add(node.getKey());
            } else {
                ((List<ContentKey>)parentFolder.getAttributeValue(MEDIA_ATTR_FILES_NAME)).add(node.getKey());
            }
        }
    }

    /**
     * 
     * @param conn SQL Connection
     * @param rs ResultSet of media query
     * @return Media node constructed from DB
     * @throws SQLException
     * 
     * @see MediaService#processNode()
     */
    private ContentNodeI processNode(Connection conn, ResultSet rs) throws SQLException {
        final ContentType type = ContentType.get(rs.getString("TYPE"));
        final ContentKey key = ContentKey.getContentKey(type, rs.getString("ID"));

        final ContentNodeI node = generatorService.createContentNode(key);

        String uri = rs.getString("URI");
        node.setAttributeValue(MEDIA_ATTR_PATH_NAME, uri);

        if (FDContentTypes.IMAGE.equals(type)) {
            node.setAttributeValue("width", Integer.valueOf(rs.getInt("WIDTH")));
            node.setAttributeValue("height", Integer.valueOf(rs.getInt("HEIGHT")));

            String lastModDate = new Date().toString();
            if (rs.getDate("last_modified") != null) {
                lastModDate = rs.getDate("last_modified").toString();
            }
            node.setAttributeValue("lastmodified", lastModDate);

        } else if (FDContentTypes.MEDIAFOLDER.equals(type)) {
            node.setAttributeValue(MEDIA_ATTR_FOLDERS_NAME, new ArrayList<ContentKey>());
            node.setAttributeValue(MEDIA_ATTR_FILES_NAME, new ArrayList<ContentKey>());
        }
        return node;
    }
}
