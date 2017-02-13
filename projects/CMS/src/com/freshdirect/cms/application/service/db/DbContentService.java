/*
 * Created on Jan 12, 2005
 *
 */
package com.freshdirect.cms.application.service.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.BidirectionalRelationshipDefI;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponse;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.AbstractContentService;
import com.freshdirect.cms.classgenerator.ContentNodeGenerator;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.reverse.BidirectionalReferenceHandler;
import com.freshdirect.cms.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * {@link com.freshdirect.cms.application.ContentServiceI} implementation that persists nodes to/from an Oracle RDBMS in a generic schema.
 * 
 * Uses the following tables:
 * 
 * <pre>
 * cms_contentnode
 * cms_attribute
 * cms_relationship
 * cms_all_nodes
 * </pre>
 * 
 * Uses the <code>cms_system_seq</code> sequence.
 */
public class DbContentService extends AbstractContentService implements ContentServiceI {

    private final static boolean DEBUG_MISSING = false;
    private final static Logger LOG = LoggerFactory.getInstance(DbContentService.class);

    private ContentTypeServiceI typeService;

    private DataSource dataSource;

    private ContentNodeGenerator generator;

    private class AddNodeBatch {
        boolean bulkInsertMode;
        
        // batches
        
        PreparedStatement delAttrStmt;
        PreparedStatement delRelshipStmt;
        PreparedStatement insertNodeStmt;
        PreparedStatement insertAttrStmt;
        PreparedStatement insertRelshipStmt;
        PreparedStatement delPrevOtherRelshipStmt;

        public AddNodeBatch(Connection conn, boolean bulkInsertMode) throws SQLException {
            // this.conn = conn;
            this.bulkInsertMode = bulkInsertMode;

            // Create batches
            /* if (!bulkInsertMode) {
                this.delAttrStmt = conn.prepareStatement(DELETE_ATTRIBUTE);
                this.delRelshipStmt = conn.prepareStatement(DELETE_RELATIONSHIP);
                this.delPrevOtherRelshipStmt = conn.prepareStatement(DELETE_PREV_OTHER_REL);
            } */
            this.insertNodeStmt = conn.prepareStatement(INSERT_NODE);

            this.insertAttrStmt = conn.prepareStatement(INSERT_ATTRIBUTE);
            this.insertRelshipStmt = conn.prepareStatement(INSERT_RELATIONSHIP);
        }
        
        
        public void addNode(ContentNodeI node) throws SQLException {
            /* if (!bulkInsertMode) {
                delAttrStmt.setString(1, node.getKey().getEncoded());
                delAttrStmt.addBatch();
    
                delRelshipStmt.setString(1, node.getKey().getEncoded());
                delRelshipStmt.addBatch();
            } */

            addNodeToBatch(node,
                    insertNodeStmt, insertAttrStmt,
                    insertRelshipStmt, delPrevOtherRelshipStmt,
                    bulkInsertMode);
        }


        public void execute() throws SQLException {
            /* if (!bulkInsertMode) {
                delAttrStmt.executeBatch();
                delAttrStmt.close();
                
                delRelshipStmt.executeBatch();
                delRelshipStmt.close();
            } */

            insertNodeStmt.executeBatch();
            insertNodeStmt.close();

            insertAttrStmt.executeBatch();
            insertAttrStmt.close();

            if (!bulkInsertMode) {
                delPrevOtherRelshipStmt.executeBatch();
                delPrevOtherRelshipStmt.close();
            }
            
            insertRelshipStmt.executeBatch();
            insertRelshipStmt.close();
        }
    }
    
    
    public DbContentService() {
        super();
    }

    public void setContentTypeService(ContentTypeServiceI typeService) {
        this.typeService = typeService;
        this.generator = new ContentNodeGenerator(typeService);
    }

    @Override
    public ContentTypeServiceI getTypeService() {
        return typeService;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void initialize() {
        Collection<BidirectionalReferenceHandler> handlers = this.typeService.getAllReferenceHandler();
        for (BidirectionalReferenceHandler handler : handlers) {
            preloadHandler(handler);
        }
    }

    private void preloadHandler(BidirectionalReferenceHandler handler) {
        Connection conn = null;
        try {
            conn = getConnection();
            for (RelationshipDefI destinationType : handler.getDestinationTypes()) {
                PreparedStatement ps = conn
                        .prepareStatement("select parent_contentnode_id, child_contentnode_id from cms_relationship where parent_contentnode_id in (select id from cms_contentnode where contenttype_id = ?) and def_contenttype=? and def_name=?");
                ps.setString(1, handler.getSourceType().getName());
                ps.setString(2, destinationType.getName());
                ps.setString(3, handler.getRelationName());
                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    ContentKey sourceKey = ContentKey.getContentKey(resultSet.getString("parent_contentnode_id"));
                    ContentKey destKey = ContentKey.getContentKey(resultSet.getString("child_contentnode_id"));
                    handler.addRelation(sourceKey, destKey);
                }
                resultSet.close();
                ps.close();
            }
        } catch (SQLException e) {
            throw new CmsRuntimeException(e);
        } finally {
            close(conn);
        }
        return;
    }

    /**
     * @param conn
     */
    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new CmsRuntimeException(e);
            }
        }
    }

    private final static String QUERY_ALL_KEYS = "select id, contenttype_id from cms_contentnode";

    private final static String QUERY_KEYS_BY_TYPE = QUERY_ALL_KEYS + " where contenttype_id = ?";

    private Set<ContentKey> getContentKeys(String query, String[] args) {
        Connection conn = null;

        Set<ContentKey> returnSet = new HashSet<ContentKey>();

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            for (int i = 0; i < args.length; i++) {
                ps.setString(i + 1, args[i]);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                returnSet.add(ContentKey.getContentKey(rs.getString("id")));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new CmsRuntimeException(e);
        } finally {
            close(conn);
        }
        return returnSet;
    }

    @Override
    public Set<ContentKey> getContentKeys(DraftContext draftContext) {
        return getContentKeys(QUERY_ALL_KEYS, new String[] {});
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
        return getContentKeys(QUERY_KEYS_BY_TYPE, new String[] { type.getName() });
    }

    @Override
    public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
        Set<ContentKey> p = new HashSet<ContentKey>();
        p.add(key);
        Map<ContentKey, ContentNodeI> map = getContentNodes(p, draftContext);
        ContentNodeI c = map.get(key);
        return c;
    }

    private final static String SELECT_MANY_NODES = "select /*+ FIRST_ROWS */ id from cms_contentnode where id in (?)";

    private final static String SELECT_MANY_ATTR = "select /*+ FIRST_ROWS */ atr.contentnode_id, atr.def_name, atr.value" + " from cms_attribute atr"
            + " where atr.contentnode_id in (?)" + " order by atr.contentnode_id, atr.def_name";

    private final static String SELECT_MANY_REL = "select /*+ FIRST_ROWS */ r.parent_contentnode_id, r.def_name, r.child_contentnode_id" + " from cms_relationship r"
            + " where r.parent_contentnode_id in (?)" + " order by r.parent_contentnode_id, r.def_name, r.ordinal";

    // [APPDEV-3423] fixed parent key getter
    private final static String SELECT_PARENT_KEYS = "select parent_contentnode_id " + "from  cms_navtree " + "where child_contentnode_id=?";

    @Override
    public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {
        Connection conn = null;
        try {
            conn = getConnection();

            String[] idChunks = DaoUtil.chunkContentKeys(keys);

            Map<ContentKey, ContentNodeI> nodeMap = new HashMap<ContentKey, ContentNodeI>(keys.size());
            for (int i = 0; i < idChunks.length; i++) {
                process(conn, nodeMap, idChunks[i], draftContext);
            }

            if (DEBUG_MISSING && nodeMap.size() < keys.size()) {
                Set<ContentKey> s = new HashSet<ContentKey>(keys);
                s.removeAll(nodeMap.keySet());
                System.err.println("DbContentService.getContentNodes() NULL for " + s);
            }

            return nodeMap;

        } catch (SQLException e) {
            throw new CmsRuntimeException(e);
        } finally {
            close(conn);
        }
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
        Set<ContentKey> set = new HashSet<ContentKey>();
        Connection conn = null;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_PARENT_KEYS);
            ps.setString(1, key.getEncoded());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ContentKey parentKey = ContentKey.getContentKey(rs.getString(1));
                set.add(parentKey);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new CmsRuntimeException(e);
        } finally {
            close(conn);
        }

        return set;
    }

    private final static int FETCH_SIZE = 1000;

    private void process(Connection conn, Map<ContentKey, ContentNodeI> nodeMap, String ids, DraftContext draftContext) throws SQLException {

        Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(FETCH_SIZE);

        String query = StringUtils.replace(SELECT_MANY_NODES, "?", ids);
        ResultSet rs = stmt.executeQuery(query);
        processNodesResultSet(rs, nodeMap, draftContext);
        rs.close();

        query = StringUtils.replace(SELECT_MANY_ATTR, "?", ids);
        rs = stmt.executeQuery(query);
        processAttributesResultSet(rs, nodeMap);
        rs.close();

        query = StringUtils.replace(SELECT_MANY_REL, "?", ids);
        rs = stmt.executeQuery(query);
        processAttributesResultSet(rs, nodeMap);
        rs.close();

        stmt.close();

    }

    private final static String DELETE_ATTRIBUTE = "delete from cms_attribute where contentnode_id = ?";

    private final static String DELETE_RELATIONSHIP = "delete from cms_relationship where parent_contentnode_id = ?";

    /** conditional insert */
    private final static String INSERT_NODE = "insert into cms_contentnode(id, contenttype_id) select ?, ? from dual where not exists (select id from cms_contentnode where id=?)";

    private final static String INSERT_ATTRIBUTE = "insert into cms_attribute(id, contentnode_id, def_name, def_contenttype, value, ordinal) "
            + "values (cms_system_seq.nextVal, ?, ?, ?, ?, ?)";

    private final static String INSERT_RELATIONSHIP = "insert into cms_relationship(id, parent_contentnode_id, def_name, def_contenttype, child_contentnode_id, ordinal) "
            + "values (cms_system_seq.nextVal, ?, ?, ?, ?, ?)";

    private final static String DELETE_PREV_OTHER_REL = "delete from cms_relationship where def_name = ? and def_contenttype = ? and child_contentnode_id = ? and ordinal = ?";

    private void storeContentNode(ContentNodeI node) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(DELETE_ATTRIBUTE);
            ps.setString(1, node.getKey().getEncoded());
            ps.executeQuery();
            ps.close();

            ps = conn.prepareStatement(DELETE_RELATIONSHIP);
            ps.setString(1, node.getKey().getEncoded());
            ps.executeQuery();
            ps.close();

            if (node.getKey().getType() == ContentType.NULL_TYPE) {
                LOG.warn("trying to insert null content node:" + node + " key : " + node.getKey());
                conn.commit();
                return;
            }

            PreparedStatement insPs = conn.prepareStatement(INSERT_NODE);
            addInsBatch(insPs, node.getKey());

            PreparedStatement atrPs = conn.prepareStatement(INSERT_ATTRIBUTE);
            PreparedStatement relPs = conn.prepareStatement(INSERT_RELATIONSHIP);
            PreparedStatement delInvPs = conn.prepareStatement(DELETE_PREV_OTHER_REL);

            // Map attrMap = node.getAttributes();
            Set<String> names = node.getDefinition().getAttributeNames();
            for (String name : names) {
                Object value = node.getAttributeValue(name);
                // System.out.println("Saving attribute - " + node.getKey() +
                // ":"+ attr.getName());
                if (value == null) {
                    continue;
                }
                AttributeDefI def = node.getDefinition().getAttributeDef(name);
                if (def instanceof RelationshipDefI) {
                    if (((RelationshipDefI) def).isCalculated()) {
                        continue;
                    }
                    if (EnumCardinality.ONE.equals(def.getCardinality())) {
                        ContentKey k = (ContentKey) value;
                        addInsBatch(insPs, k);
                        addRelBatch(relPs, node.getKey(), name, k, 0);
                        if (def instanceof BidirectionalRelationshipDefI) {
                            addInverseDelBatch(delInvPs, name, k, 0);
                        }
                    } else {
                        List l = (List) value;
                        for (int m = 0; m < l.size(); m++) {
                            ContentKey k = (ContentKey) l.get(m);
                            addInsBatch(insPs, k);
                            addRelBatch(relPs, node.getKey(), name, k, m);
                        }
                    }
                } else {
                    atrPs.setString(1, node.getKey().getEncoded());
                    atrPs.setString(2, name);
                    atrPs.setString(3, node.getKey().getType().getName());
                    atrPs.setString(4, ContentTypeUtil.attributeToString(def, value));
                    atrPs.setInt(5, 0);
                    atrPs.addBatch();
                }
            }

            insPs.executeBatch();
            insPs.close();

            atrPs.executeBatch();
            atrPs.close();

            delInvPs.executeBatch();
            delInvPs.close();

            relPs.executeBatch();
            relPs.close();

            conn.commit();
        } catch (Exception sqle1) {
            throw new CmsRuntimeException(sqle1);
        } finally {
            close(conn);
        }

    }

    // BULK-INSERT
    protected void addNodeToBatch(ContentNodeI node,
            PreparedStatement insertNodeStmt, PreparedStatement insertAttrStmt,
            PreparedStatement insertRelshipStmt,
            PreparedStatement delPrevOtherRelshipStmt,
            boolean bulkInsertMode) throws SQLException {
        if (node.getKey().getType()==ContentType.NULL_TYPE) {
            // LOGGER.warn("trying to insert null content node:" + node + " key : " + node.getKey());
        } else {
            // [1] Add content node
            addInsertNodeToBatch(insertNodeStmt, node.getKey());

            // [2] Add attributes and relationships
            
            //Map attrMap = node.getAttributes();
            Set<String> names = node.getDefinition().getAttributeNames();
            for (String name : names) {
                Object value = node.getAttributeValue(name);
                // System.out.println("Saving attribute - " + node.getKey() +
                // ":"+ attr.getName());
                if (value == null) {
                    continue;
                }
                AttributeDefI def = node.getDefinition().getAttributeDef(name);
                if (def instanceof RelationshipDefI) {
                    if (((RelationshipDefI) def).isCalculated()) {
                        continue;
                    }
                    if (EnumCardinality.ONE.equals(def.getCardinality())) {
                        ContentKey k = (ContentKey) value;
                        addInsertNodeToBatch(insertNodeStmt, k);
                        addRelBatch(insertRelshipStmt, node.getKey(), name, k, 0);
                        if (!bulkInsertMode && def instanceof BidirectionalRelationshipDefI) {
                            addInverseDelBatch(delPrevOtherRelshipStmt, name, k, 0);
                        }
                    } else {
                        List<ContentKey> l = (List<ContentKey>) value;
                        for (int m = 0; m < l.size(); m++) {
                            ContentKey k = l.get(m);
                            addInsertNodeToBatch(insertNodeStmt, k);
                            addRelBatch(insertRelshipStmt, node.getKey(), name, k, m);
                        }
                    }
                } else {
                    insertAttrStmt.setString(1, node.getKey().getEncoded());
                    insertAttrStmt.setString(2, name);
                    insertAttrStmt.setString(3, node.getKey().getType().getName());
                    insertAttrStmt.setString(4, ContentTypeUtil.attributeToString(def, value));
                    insertAttrStmt.setInt(5, 0);
                    insertAttrStmt.addBatch();
                }
            }
        }
    }

    private void addInverseDelBatch(PreparedStatement delInvPs, String relationshipName, ContentKey destKey, int ordinal) throws SQLException {
        delInvPs.setString(1, relationshipName);
        delInvPs.setString(2, destKey.getType().getName());
        delInvPs.setString(3, destKey.getEncoded());
        delInvPs.setInt(4, ordinal);
        delInvPs.addBatch();
    }

    private void addInsBatch(PreparedStatement insPs, ContentKey key) throws SQLException {
        if (key.getType() == ContentType.NULL_TYPE) {
            return;
        }
        String id = key.getEncoded();
        insPs.setString(1, id);
        insPs.setString(2, key.getType().getName());
        insPs.setString(3, id);
        insPs.addBatch();
    }

    private void addRelBatch(PreparedStatement relPs, ContentKey parent, String relationshipName, ContentKey destKey, int ordinal) throws SQLException {
        relPs.setString(1, parent.getEncoded());
        relPs.setString(2, relationshipName);
        relPs.setString(3, destKey.getType().getName());
        if (ContentType.NULL_TYPE == destKey.getType()) {
            relPs.setNull(4, Types.VARCHAR);
        } else {
            relPs.setString(4, destKey.getEncoded());
        }
        relPs.setInt(5, ordinal);
        relPs.addBatch();
    }

    // BULK-INSERT MODE
    private void addInsertNodeToBatch(PreparedStatement insPs, ContentKey key) throws SQLException {
        if (key.getType() == ContentType.NULL_TYPE) {
            return;
        }
        String id = key.getEncoded();
        insPs.setString(1, id);
        insPs.setString(2, key.getType().getName());
        insPs.setString(3, id);
        insPs.addBatch();
    }

    @Override
    public ContentNodeI createPrototypeContentNode(ContentKey key, DraftContext draftContext) {
        if (!typeService.getContentTypes().contains(key.getType())) {
            return null;
        }
        return createContentNode(key, draftContext);
    }

    private void processNodesResultSet(ResultSet rs, Map<ContentKey, ContentNodeI> nodeMap, DraftContext draftContext) throws SQLException {
        while (rs.next()) {
            ContentKey key = ContentKey.getContentKey(rs.getString("id"));
            ContentNodeI node = createContentNode(key, draftContext);
            
            if (node != null) {
                nodeMap.put(key, node);
            }
        }
    }

    protected ContentNodeI createContentNode(ContentKey key, DraftContext draftContext) {
        return
            ! ContentKey.NULL_KEY.equals(key)
                ? this.generator.createNode(key, draftContext)
                : null
            ;
    }

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

    private void recordAttribute(Map<ContentKey, ContentNodeI> nodeMap, ContentKey key, String attrName, List<String> value) {
        ContentNodeI node = nodeMap.get(key);

        AttributeDefI aDef = typeService.getContentTypeDefinition(node.getKey().getType()).getAttributeDef(attrName);
        if (aDef == null) {
            return;
        }

        node.setAttributeValue(attrName, ContentTypeUtil.convertAttributeValues(aDef, value));
    }


    // BULK-INSERT MODE
    private void storeContentNodes(Collection<ContentNodeI> nodes) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            conn.setReadOnly(true);
            
            // CREATE BATCHES
            AddNodeBatch anb = new AddNodeBatch(conn, true);
            
            for (ContentNodeI node : nodes) {
                // FILL BATCHES
                anb.addNode(node);
            }

            // EXECUTE BATCHES
            anb.execute();

            conn.commit();
        } catch (Exception exc) {
            throw new CmsRuntimeException(exc);
        } finally {
            close(conn);
        }
    }
    
    
    @Override
    public CmsResponseI handle(CmsRequestI request) {
        
        if (request != null) {
            if (CmsRequestI.Source.STORE_IMPORT == request.getSource()) {
                // bulk import operation
                storeContentNodes( request.getNodes() );

            } else {
                // Basic save / update operation
                
                for (ContentNodeI node : request.getNodes()) {
                    storeContentNode(node);
                }
            
            }
        }
        return new CmsResponse();
    }

    @Override
    public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
        return getContentNode(key, draftContext);
    }

}
