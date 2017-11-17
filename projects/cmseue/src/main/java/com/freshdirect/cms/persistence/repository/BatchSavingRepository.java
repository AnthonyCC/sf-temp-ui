package com.freshdirect.cms.persistence.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.freshdirect.cms.persistence.entity.AttributeEntity;
import com.freshdirect.cms.persistence.entity.ContentNodeEntity;
import com.freshdirect.cms.persistence.entity.RelationshipEntity;

@Profile("database")
@Repository
@Transactional
public class BatchSavingRepository {

    private final static String SAVE_RELATIONSHIP = "insert into relationship(id, parent_contentnode_id, ordinal, def_name, def_contenttype, child_contentnode_id) "
            + "values(cms_system_seq.nextVal, ?, ?, ?, ?, ?)";
    private final static String DELETE_RELATIONSHIP = "delete from relationship where parent_contentnode_id = ? and def_name = ?";
    private final static String INSERT_NODE = "insert into contentnode(id, contenttype_id) select ?, ? from dual where not exists (select id from contentnode where id=?)";
    private final static String DELETE_ATTRIBUTE = "delete from attribute where contentnode_id = ? and def_name = ?";
    private final static String INSERT_ATTRIBUTE = "insert into attribute(id, contentnode_id, def_name, def_contenttype, value, ordinal) values (cms_system_seq.nextVal, ?, ?, ?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void batchSaveContentNodes(final List<ContentNodeEntity> contentNodeEntities) {
        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, contentNodeEntities.get(i).getContentKey());
                ps.setString(2, contentNodeEntities.get(i).getContentType());
                ps.setString(3, contentNodeEntities.get(i).getContentKey());
            }

            @Override
            public int getBatchSize() {
                return contentNodeEntities.size();
            }
        };

        jdbcTemplate.batchUpdate(INSERT_NODE, batchPreparedStatementSetter);
    }

    public void saveScalarAttribute(final AttributeEntity attributeEntity) {
        PreparedStatementSetter batchPreparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, attributeEntity.getContentKey());
                ps.setString(2, attributeEntity.getName());
                ps.setString(3, attributeEntity.getContentType());
                ps.setString(4, attributeEntity.getValue());
                ps.setInt(5, attributeEntity.getOrdinal());
            }
        };
        deleteScalarAttribute(attributeEntity);
        jdbcTemplate.update(INSERT_ATTRIBUTE, batchPreparedStatementSetter);
    }

    public void deleteScalarAttribute(final AttributeEntity attributeEntity) {
        deleteScalarAttribute(attributeEntity.getContentKey(), attributeEntity.getName());
    }
    
    public void deleteScalarAttribute(final String contentKey, final String name) {
        PreparedStatementSetter batchPreparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, contentKey);
                ps.setString(2, name);
            }
        };

        jdbcTemplate.update(DELETE_ATTRIBUTE, batchPreparedStatementSetter);

    }

    public void saveSingleRelationship(final RelationshipEntity relationshipEntity) {
        PreparedStatementSetter batchPreparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, relationshipEntity.getRelationshipSource());
                ps.setLong(2, relationshipEntity.getOrdinal());
                ps.setString(3, relationshipEntity.getRelationshipName());
                ps.setString(4, relationshipEntity.getRelationshipDestinationType());
                ps.setString(5, relationshipEntity.getRelationshipDestination());
            }
        };

        jdbcTemplate.update(SAVE_RELATIONSHIP, batchPreparedStatementSetter);
    }

    public void deleteRelationship(final String relationshipSource, final String relationshipName) {
        PreparedStatementSetter batchPreparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, relationshipSource);
                ps.setString(2, relationshipName);
            }
        };

        jdbcTemplate.update(DELETE_RELATIONSHIP, batchPreparedStatementSetter);

    }

    public void batchSaveRelationships(final List<RelationshipEntity> relationshipEntities) {
        BatchPreparedStatementSetter batchPreparedStatementSetter = new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, relationshipEntities.get(i).getRelationshipSource());
                ps.setLong(2, relationshipEntities.get(i).getOrdinal());
                ps.setString(3, relationshipEntities.get(i).getRelationshipName());
                ps.setString(4, relationshipEntities.get(i).getRelationshipDestinationType());
                ps.setString(5, relationshipEntities.get(i).getRelationshipDestination());
            }

            @Override
            public int getBatchSize() {
                return relationshipEntities.size();
            }
        };

        jdbcTemplate.batchUpdate(SAVE_RELATIONSHIP, batchPreparedStatementSetter);
    }
    
}
