package com.freshdirect.cms.ui.editor.reports.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.ui.editor.reports.service.ContentKeyRowMapper;

@Repository
public class VirtualAttributeRepository {

    private static final String CATEGORY_CONSUMED_BY_VIRTUAL_CATEGORY = "select distinct(PARENT_CONTENTNODE_ID) from cms.relationship where CHILD_CONTENTNODE_ID = ? AND "
            + "DEF_NAME = 'VIRTUAL_GROUP'";

    private static final String CATEGORY_CONSUMED_BY_DEPARTMENT_CAROUSEL = "select distinct(PARENT_CONTENTNODE_ID) from cms.relationship where CHILD_CONTENTNODE_ID = ? AND "
            + "DEF_NAME in ('SCARAB_YMAL_PROMOTE', 'SCARAB_YMAL_DEMOTE', 'SCARAB_YMAL_EXCLUDE', 'featuredRecommenderSourceCategory') "
            + "AND PARENT_CONTENTNODE_ID like 'Department:%'";

    private static final String CATEGORY_CONSUMED_BY_CATEGORY_CAROUSEL = "select distinct(PARENT_CONTENTNODE_ID) from cms.relationship where CHILD_CONTENTNODE_ID = ? AND "
            + "DEF_NAME in ( 'CANDIDATE_LIST', 'SCARAB_YMAL_PROMOTE', 'SCARAB_YMAL_DEMOTE', 'SCARAB_YMAL_EXCLUDE', 'featuredRecommenderSourceCategory') "
            + "AND PARENT_CONTENTNODE_ID like 'Category:%'";

    private static final String CATEGORY_CONSUMED_BY_STORE_CAROUSEL = "select distinct(PARENT_CONTENTNODE_ID) from cms.relationship where CHILD_CONTENTNODE_ID = ? AND "
            + "DEF_NAME in ( 'CANDIDATE_LIST', 'SCARAB_YMAL_PROMOTE', 'SCARAB_YMAL_DEMOTE', 'SCARAB_YMAL_EXCLUDE', 'featuredRecommenderSourceCategory') "
            + "AND PARENT_CONTENTNODE_ID like 'Store:%'";

    @Autowired
    private ContentKeyRowMapper contentKeyRowMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<ContentKey> queryConsumedByVirtualCategory(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(CATEGORY_CONSUMED_BY_VIRTUAL_CATEGORY, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }

    public List<ContentKey> queryConsumedByDepartmentCarousel(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(CATEGORY_CONSUMED_BY_DEPARTMENT_CAROUSEL, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }

    public List<ContentKey> queryConsumedByCategoryCarousel(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(CATEGORY_CONSUMED_BY_CATEGORY_CAROUSEL, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }

    public List<ContentKey> queryConsumedByStoreCarousel(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(CATEGORY_CONSUMED_BY_STORE_CAROUSEL, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }
}
