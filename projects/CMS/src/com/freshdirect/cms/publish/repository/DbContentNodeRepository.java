package com.freshdirect.cms.publish.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.db.DbContentService;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.publish.config.DatabaseConfig;
import com.freshdirect.cms.publish.service.ContentNodeGeneratorService;
import com.freshdirect.cms.publish.util.TaskTimer;

/**
 * This repository supports loading all CMS content nodes at once.
 * 
 * @author segabor
 *
 */
public final class DbContentNodeRepository extends JdbcRepository {

    private static final Logger LOGGER = Logger.getLogger(DbContentNodeRepository.class);

    /**
     * The amount of expected content nodes
     */
    private static final int NODES_SIZE = 427989;

    private static final String FETCH_NODES_SIZE_SQL =
            "select count(*) from cms_contentnode";
    
    private static final String FETCH_ALL_KEYS_SQL =
            "select ID as key from cms_contentnode";

    private static final String FETCH_ALL_ATTRIBUTES_SQL =
            "select atr.contentnode_id key, atr.def_name, atr.value " +
            "from cms_attribute atr " +
            "order by atr.contentnode_id, atr.def_name";

    private final static String FETCH_ALL_RELS_SQL = "select r.parent_contentnode_id, r.def_name, r.child_contentnode_id " +
            "from cms_relationship r " +
            "order by r.parent_contentnode_id, r.def_name, r.ordinal";

    private final ContentTypeServiceI typeService;
    
    private final ContentNodeGeneratorService generatorService;

    public DbContentNodeRepository(ContentTypeServiceI typeService, ContentNodeGeneratorService generatorService) {
        this.typeService = typeService;
        
        this.generatorService = generatorService;
    }

    public Collection<ContentNodeI> findAll() {
        return loadAllNodes();
    }

    private Collection<ContentNodeI> loadAllNodes() {

        Map<ContentKey,ContentNodeI> nodeMap = null;
        
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
            nodeMap = new HashMap<ContentKey, ContentNodeI>(nodesSize);

            // build empty nodes
            timer = new TaskTimer("Node construction");
            rs = stmt.executeQuery(FETCH_ALL_KEYS_SQL);
            processNodesResultSet(rs, nodeMap);
            timer.logTimeSpent(LOGGER, "Created " + nodeMap.size() + " nodes");
            rs.close();

            // populate attributes
            timer = new TaskTimer("Populating attributes");
            rs = stmt.executeQuery(FETCH_ALL_ATTRIBUTES_SQL);
            processAttributesResultSet(rs, nodeMap);
            timer.logTimeSpent(LOGGER, null);
            rs.close();

            // populate relationship values
            timer = new TaskTimer("Populating relationship values");
            rs = stmt.executeQuery(FETCH_ALL_RELS_SQL);
            processAttributesResultSet(rs, nodeMap);
            timer.logTimeSpent(LOGGER, null);
            rs.close();

            stmt.close();

            rs = null;
            stmt = null;
        } catch (SQLException exc) {
            LOGGER.error("DB error raised during building content node map", exc);
        } finally {
            closeResources(rs, stmt, conn);
        }

        return nodeMap.values();
    }

    /**
     * Build content node out of DB result set
     * 
     * @param rs Result Set
     * @param nodeMap result map that collects nodes
     * @throws SQLException
     * 
     * @see DbContentService#processNodesResultSet()
     */
    private void processNodesResultSet(ResultSet rs, Map<ContentKey, ContentNodeI> nodeMap) throws SQLException {
        while (rs.next()) {
            ContentKey key = ContentKey.getContentKey(rs.getString("key"));
            ContentNodeI node = generatorService.createContentNode(key);
            
            if (node != null) {
                nodeMap.put(key, node);
            }
        }
    }

    /**
     * Fills out node attributes
     * 
     * @param rs Result Set
     * @param nodeMap
     * @throws SQLException
     * 
     * @see DbContentService#processAttributesResultSet()
     */
    private void processAttributesResultSet(ResultSet rs, Map<ContentKey, ContentNodeI> nodeMap) throws SQLException {
        ContentKey currKey = null;
        String currAttrName = null;
        List<String> currValue = new ArrayList<String>();

        while (rs.next()) {
            ContentKey key = ContentKey.getContentKey(rs.getString(1));
            String attrName = rs.getString(2);

            if (!key.equals(currKey) || !attrName.equals(currAttrName)) {
                if (currKey != null) {
                    recordAttribute(nodeMap, currKey, currAttrName, currValue);
                }
                currKey = key;
                currAttrName = attrName;
                currValue = new ArrayList<String>();
            }

            currValue.add(rs.getString(3));
        }
        if (currKey != null) {
            recordAttribute(nodeMap, currKey, currAttrName, currValue);
        }
    }

    /**
     * Set a particular attribute value on the given node
     * 
     * @param nodeMap node lookup map
     * @param key content key of node
     * @param attrName attribute name
     * @param value attriubte value
     * 
     * @see DbContentService#recordAttribute()
     */
    private void recordAttribute(Map<ContentKey, ContentNodeI> nodeMap, ContentKey key, String attrName, List<String> value) {
        ContentNodeI node = nodeMap.get(key);

        AttributeDefI aDef = typeService.getContentTypeDefinition(node.getKey().getType()).getAttributeDef(attrName);
        if (aDef == null) {
            return;
        }

        node.setAttributeValue(attrName, ContentTypeUtil.convertAttributeValues(aDef, value));
    }
}
