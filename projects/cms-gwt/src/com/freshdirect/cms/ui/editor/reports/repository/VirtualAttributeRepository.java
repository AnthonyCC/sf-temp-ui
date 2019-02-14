package com.freshdirect.cms.ui.editor.reports.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final String QUERY_CONSUMED_BY_SECTION = "select distinct(parent_contentnode_id) as contentKey "
            + "from cms.relationship "
            + "where def_name in ('category', 'linkTarget') "
            + "and parent_contentnode_id like 'Section:%' "
            + "and child_contentnode_id=?";

    private static final String QUERY_CONSUMED_BY_IMAGEBANNER = "select distinct(parent_contentnode_id) as contentKey "
            + "from cms.relationship "
            + "where def_name in ('Target', 'linkOneTarget', 'linkTwoTarget') "
            + "and parent_contentnode_id like 'ImageBanner:%' "
            + "and child_contentnode_id=?";

    private static final String QUERY_CONSUMED_BY_MODULE = "select distinct(parent_contentnode_id) as contentKey "
            + "from cms.relationship "
            + "where def_name='sourceNode' "
            + "and parent_contentnode_id like 'Module:%' "
            + "and child_contentnode_id=?";

    private static final String QUERY_CONSUMED_BY_BANNER = "select distinct(parent_contentnode_id) as contentKey "
            + "from cms.relationship "
            + "where def_name='link' "
            + "and parent_contentnode_id like 'Banner:%' "
            + "and child_contentnode_id=?";

    public static final String QUERY_CONSUMED_BY_TABLET_FEATURE_CATEGORIES = "select distinct(parent_contentnode_id) as contentKey "
            + "from cms.relationship "
            + "where def_name='tabletFeaturedCategories' "
            + "and parent_contentnode_id like 'Store:%' "
            + "and child_contentnode_id=?";

    @Autowired
    private ContentKeyRowMapper contentKeyRowMapper;

    @Autowired
    @Qualifier("cmsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

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

    public List<ContentKey> queryConsumedBySection(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(QUERY_CONSUMED_BY_SECTION, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }

    public List<ContentKey> queryConsumedByImageBanner(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(QUERY_CONSUMED_BY_IMAGEBANNER, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }

    public List<ContentKey> queryConsumedByTabletFeaturedCategories(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(QUERY_CONSUMED_BY_TABLET_FEATURE_CATEGORIES, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }

    public List<ContentKey> queryConsumedByModule(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(QUERY_CONSUMED_BY_MODULE, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }

    public List<ContentKey> queryConsumedByBanner(final ContentKey categoryKey) {
        PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, categoryKey.toString());
            }
        };
        List<ContentKey> result = jdbcTemplate.query(QUERY_CONSUMED_BY_BANNER, preparedStatementSetter, contentKeyRowMapper);
        return result;
    }
}
