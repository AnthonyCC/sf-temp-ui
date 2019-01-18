package com.freshdirect.cms.ui.editor.reports.repository;

import static com.freshdirect.cms.ui.editor.reports.ReportColumnType.CLASS_COL;
import static com.freshdirect.cms.ui.editor.reports.ReportColumnType.GROUP_COL;
import static com.freshdirect.cms.ui.editor.reports.ReportColumnType.SUFFIX_ATTR;
import static com.freshdirect.cms.ui.editor.reports.ReportColumnType.SUFFIX_KEY;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.builder.AttributeBuilder;
import com.freshdirect.cms.ui.editor.reports.service.ContentKeyRowMapper;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;

@Repository
public class ReportsRepository {

    // TODO : remove remaining navtree references

    private static final String QUERY_UNREACHABLE = "select id from cms_contentnode cn "
            + "where contenttype_id not in ('Html','Image','Store','ErpCharacteristic') "
            + "and id not in ('FDFolder:recipes') "
            + "minus "
            + "select child_contentnode_id from cms.navtree "
            + "start with parent_contentnode_id = 'Store:FreshDirect' "
            + "connect by prior child_contentnode_id = parent_contentnode_id";

    private static final String QUERY_RECENTLY_MODIFIED = "select distinct id from "
            + "( "
            + "  select contenttype||':'||contentnode_id id "
            + "  from cms_changeset cs, cms_contentnodechange cnc "
            + "  where cs.timestamp>sysdate-7 "
            + "  and cnc.changeset_id=cs.id "
            + "  order by timestamp desc "
            + ") "
            + "where rownum<100";

    private static final String QUERY_MULTI_HOME_PRODUCTS = "select child_contentnode_id as content_key$, count(*) as homes "
            + "from cms.navtree where def_contenttype='Product' "
            + "group by child_contentnode_id "
            + "having count(*) > 1 "
            + "order by homes desc";

    private static final String QUERY_RECENT_CHANGES = "select * from ( "
            + "  select cnc.contenttype as group$, cnc.changetype as class$, contenttype||'':''||contentnode_id as content_key$, user_id, timestamp "
            + "  from cms_changeset cs, cms_contentnodechange cnc "
            + "  where cs.timestamp>sysdate-{0} "
            + "  and cnc.changeset_id=cs.id "
            + "  order by timestamp desc "
            + ") where rownum<{1} order by group$ asc ";

    private static final String QUERY_INVISIBLE_PRODUCTS = "select cn.id as content_key$ from cms.contentnode cn, cms.attribute a where "
            + "cn.contenttype_id='Product' and a.def_name = 'INVISIBLE' and a.value='true' and cn.id=a.contentnode_id";

    private static final String QUERY_CIRCULAR_REFERENCES = "select parent_contentnode_id as parent_key$, def_name, child_contentnode_id as child_key$ from ( "
            + "  select connect_by_iscycle cyc,level,r.parent_contentnode_id, r.def_name, r.child_contentnode_id "
            + "  from cms.relationship r "
            + "  start with r.parent_contentnode_id like 'Category:%' "
            + "  connect by nocycle prior r.child_contentnode_id = r.parent_contentnode_id and "
            + "  (r.def_name = 'categories' or r.def_name = 'subcategories' or r.def_name = 'VIRTUAL_GROUP') ) where cyc <> 0 "
            + "group by "
            + "parent_contentnode_id, def_name, child_contentnode_id";

    private static final String QUERY_MULTIPLE_REFERENCES = "select r1.parent_contentnode_id as parent_key$, r1.child_contentnode_id as child_key$, r2.child_contentnode_id as grandchild_key$ from cms.relationship r1, cms.relationship r2 "
            + "  where "
            + "  r1.def_name = 'VIRTUAL_GROUP' and "
            + "  r2.def_name = 'VIRTUAL_GROUP' and "
            + "  r1.child_contentnode_id = r2.parent_contentnode_id";

    // TODO NOT USED YET
    public static final String QUERY_HIERARCHY_REPORT = "select level, parent_contentnode_id as parent_key$, child_contentnode_id as child_key$, "
            + "child_contentnode_id||':{Attribute_1}' as {Attribute_1}_1_attribute$, "
            + "child_contentnode_id||':{Attribute_2}' as {Attribute_2}_2_attribute$, "
            + "child_contentnode_id||':{Attribute_3}' as {Attribute_3}_3_attribute$, "
            + "child_contentnode_id||':{Attribute_4}' as {Attribute_4}_4_attribute$, "
            + "child_contentnode_id||':{Attribute_5}' as {Attribute_5}_5_attribute$ "
            + "from cms.navtree "
            + "where def_contenttype in ({ContentTypes}) "
            + "start with parent_contentnode_id = '{RootNode}' "
            + "connect by prior child_contentnode_id = parent_contentnode_id "
            + "order siblings by def_name, ordinal";

    private static final String QUERY_STACKED_SKUS = "select parent_contentnode_id as parent_key$, child_contentnode_id as child_key$ from cms.relationship "
            + "  where def_name = 'skus' "
            + "  and parent_contentnode_id in ( "
            + "  select parent_contentnode_id "
            + "  from cms.relationship where def_name = 'skus' "
            + "  group by parent_contentnode_id "
            + "  having count(*) > 1 "
            + "  ) "
            + "  order by parent_contentnode_id";

    private static final String QUERY_RECIPES_SUMMARY = "select cn.id as content_key$, "
            + "      name.value as name, "
            + "      productionStatus.value as productionStatus, "
            + "      source.CHILD_CONTENTNODE_ID as source_key$, "
            + "      photoPath.uri as photo, "
            + "      ingredientsMediaPath.uri as ingredientsMedia, "
            + "      copyrightMediaPath.uri as copyrightMedia, "
            + "      titleImagePath.uri as titleImage "
            + "from cms.contentnode cn, "
            + "       cms.attribute name, "
            + "       cms.attribute productionStatus, "
            + "       cms.relationship source, "
            + "       cms.relationship photo, cms.media photoPath, "
            + "       cms.relationship ingredientsMedia, cms.media ingredientsMediaPath, "
            + "       cms.relationship copyrightMedia, cms.media copyrightMediaPath, "
            + "       cms.relationship titleImage, cms.media titleImagePath "
            + "where cn.contenttype_id = 'Recipe' "
            + "    and name.contentnode_id(+)=cn.id "
            + "        and name.def_name(+)='name' "
            + "    and productionStatus.contentnode_id(+)=cn.id "
            + "        and productionStatus.def_name(+)='productionStatus' "
            + "    and source.parent_contentnode_id(+)=cn.id "
            + "        and source.def_name(+)='source' "
            + "    and photo.parent_contentnode_id(+)=cn.id "
            + "        and photo.def_name(+)='photo' "
            + "        and photoPath.id(+)=substr(photo.child_contentnode_id, instr(photo.child_contentnode_id, ':')+1) "
            + "    and ingredientsMedia.parent_contentnode_id(+)=cn.id "
            + "        and ingredientsMedia.def_name(+)='ingredientsMedia' "
            + "        and ingredientsMediaPath.id(+)=substr(ingredientsMedia.child_contentnode_id, instr(ingredientsMedia.child_contentnode_id, ':')+1) "
            + "    and copyrightMedia.parent_contentnode_id(+)=cn.id "
            + "        and copyrightMedia.def_name(+)='copyrightMedia' "
            + "        and copyrightMediaPath.id(+)=substr(copyrightMedia.child_contentnode_id, instr(copyrightMedia.child_contentnode_id, ':')+1) "
            + "    and titleImage.parent_contentnode_id(+)=cn.id "
            + "        and titleImage.def_name(+)='titleImage' "
            + "        and titleImagePath.id(+)=substr(titleImage.child_contentnode_id, instr(titleImage.child_contentnode_id, ':')+1) "
            + "order by cn.id";

    private static final String QUERY_PRIMARY_HOMES = "select product as product_key$, r.child_contentnode_id as primary_home_key$, orphan "
            + " "
            + "from "
            + "( "
            + "  (select id as product, NVL2(product, 0, 1) as orphan from CMS.contentnode "
            + " "
            + "  left join "
            + " "
            + "    (select distinct product, root "
            + "    from "
            + "        (select child_contentnode_id as product, parent_contentnode_id, connect_by_root parent_contentnode_id as root "
            + "        from "
            + "          (select * from CMS.relationship "
            + "          where def_name = ''departments'' or (def_name = ''categories'' and def_contenttype = ''Category'') "
            + "          or (def_name = ''subcategories'' and def_contenttype = ''Category'') or def_name = ''products'' "
            + "          ) "
            + " "
            + "        start with parent_contentnode_id = ''Store:FreshDirect'' "
            + "        connect by parent_contentnode_id = prior child_contentnode_id "
            + "        ) "
            + " "
            + "    where product like ''Product:%'' and root = ''Store:FreshDirect'' "
            + "    ) "
            + "  on id = product "
            + " "
            + "  where contenttype_id = ''Product'' "
            + "  ) "
            + "  q "
            + " "
            + "left join CMS.relationship r "
            + "on q.product = r.parent_contentnode_id and r.def_name = ''PRIMARY_HOME'' and r.def_contenttype = ''Category'' "
            + ") "
            + " "
            + "join "
            + " "
            + "    (select distinct product_scope, root "
            + "    from "
            + "(select child_contentnode_id as product_scope, parent_contentnode_id, connect_by_root parent_contentnode_id as root "
            + "        from "
            + "          (select * from CMS.relationship "
            + "          where def_name = ''departments'' or (def_name = ''categories'' and def_contenttype = ''Category'') "
            + "          or (def_name = ''subcategories'' and def_contenttype = ''Category'') or def_name = ''products'' "
            + "          ) "
            + " "
            + "        start with parent_contentnode_id = ''{0}'' "
            + "        connect by parent_contentnode_id = prior child_contentnode_id "
            + "        ) "
            + " "
            + "    where product_scope like ''Product:%'' "
            + ") "
            + " "
            + "on product=product_scope";

    private static final String QUERY_BROKEN_MEDIA_LINKS = "select r.PARENT_CONTENTNODE_ID  as parent_key$, r.DEF_NAME as attribute, r.CHILD_CONTENTNODE_ID as media from RELATIONSHIP r "
            + " left join (select concat(type,concat(':',id)) as mediaids from MEDIA) m on r.CHILD_CONTENTNODE_ID = m.mediaids "
            + " where r.DEF_CONTENTTYPE in ('Html', 'Image', 'Template', 'MediaFolder') and m.mediaids is null";

    // -- SMART STORE REPORTS --

    private static final String QUERY_SCARAB_MERCHANDISING_RULES = "select parent_contentnode_id as parent_key$, def_name as group$, child_contentnode_id as child_key$ "
            + "from cms.relationship "
            + "where def_name in ('SCARAB_YMAL_INCLUDE', 'SCARAB_YMAL_EXCLUDE', 'SCARAB_YMAL_PROMOTE', 'SCARAB_YMAL_DEMOTE') "
            + "order by def_name, parent_contentnode_id, ordinal";

    private static final String QUERY_YMAL_SETS = "select parent_contentnode_id as node_key$, child_contentnode_id as ymalset_key$ "
            + "from cms.relationship "
            + "where def_name = 'ymalSets' "
            + "order by parent_contentnode_id, ordinal";

    private static final String QUERY_SMART_CATEGORY_RECOMMENDERS = "select parent_contentnode_id as category_key$, child_contentnode_id as recommender_key$  "
            + "from cms.relationship "
            + "where def_name = 'recommender' "
            + "order by parent_contentnode_id, ordinal";

    private static final String QUERY_PAIR_IT_AND_COMPLETED_BY_MEAL = "select key.productKey as product_key$, hd.pairItHeading as PAIR_IT_HEADING_ATTRIBUTE$, txt.pairItText as PAIR_IT_TEXT_ATTRIBUTE$, ctm.mealProductKey as completeTheMeal_key$ " +
            "from ( " +
            "    select distinct(productKey) from ( " +
            "        select contentnode_id as productKey from cms.attribute where def_name='PAIR_IT_HEADING' or def_name='PAIR_IT_TEXT' " +
            "        union " +
            "        select parent_contentnode_id as productKey from CMS.relationship where def_name='completeTheMeal' " +
            "    ) " +
            ") key " +
            "join ( " +
            "    select contentnode_id as productKey, value as pairItHeading " +
            "    from CMS.attribute " +
            "    where def_name='PAIR_IT_HEADING' " +
            ") hd on(key.productKey = hd.productKey) " +
            "join ( " +
            "    select contentnode_id as productKey, value as pairItText " +
            "    from CMS.attribute " +
            "    where def_name='PAIR_IT_TEXT' " +
            ") txt on (key.productKey = txt.productKey) " +
            "join ( " +
            "    select parent_contentnode_id as productKey, child_contentnode_id as mealProductKey " +
            "    from CMS.relationship " +
            "    where def_name='completeTheMeal' " +
            ") ctm on (key.productKey = ctm.productKey) " +
            "order by hd.productKey";

    private static final String QUERY_RECIPES = "select parent_contentnode_id as node_key$, DEF_NAME as relationship, child_contentnode_id as recipe_key$ "
            + "from cms.relationship "
            + "where def_contenttype='Recipe' "
            + "order by parent_contentnode_id, DEF_NAME";

    private static final class ReportMapper implements RowMapper<Map<Attribute,Object>> {

        private final Attribute[] attributes;

        public ReportMapper(Attribute[] attributes) {
            this.attributes = attributes;
        }

        @Override
        public Map<Attribute,Object> mapRow(ResultSet resultSet, int index) throws SQLException {
            Map<Attribute, Object> object = new LinkedHashMap<Attribute, Object>();

            for (int col=0; col<attributes.length; col++) {
                object.put(attributes[col], resultSet.getString(col+1));

            }

            return object;
        }
    };

    @Autowired
    private ContentKeyRowMapper contentKeyRowMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<ContentKey> fetchUnreachableStoreObjects() {
        List<ContentKey> result = jdbcTemplate.query(QUERY_UNREACHABLE, contentKeyRowMapper);

        return result;
    }

    public List<ContentKey> fetchRecentlyModifiedObjects() {
        List<ContentKey> result = jdbcTemplate.query(QUERY_RECENTLY_MODIFIED, contentKeyRowMapper);

        return result;
    }

    private Attribute[] processMetadata(String querySql) {
        Assert.hasText(querySql);

        DataSource ds = jdbcTemplate.getDataSource();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DataSourceUtils.getConnection(ds);
            ps = con.prepareStatement(querySql);
            ResultSetMetaData metaData = ps.getMetaData();

            Attribute attributes[] = new Attribute[metaData.getColumnCount()];
            for (int col=0; col<metaData.getColumnCount(); col++ ) {
                String colName = metaData.getColumnName(col+1);

                final String name = colName.toUpperCase();
                String attrName = null;

                if (name.endsWith(SUFFIX_ATTR.fragment)) {
                    attrName = TableAttribute.ColumnType.ATTRIB.name() + "|" + colName.substring(0, colName.length() - SUFFIX_ATTR.fragment.length());
                } else if (name.endsWith(SUFFIX_KEY.fragment)) {
                    attrName = TableAttribute.ColumnType.KEY.name() + "|" + colName.substring(0, colName.length() - SUFFIX_KEY.fragment.length());
                } else if (CLASS_COL.fragment.equals(name)) {
                    attrName = TableAttribute.ColumnType.CLASS.name() + "|" + colName;
                } else if (GROUP_COL.fragment.equals(name)) {
                    attrName = TableAttribute.ColumnType.GROUPING.name() + "|" + colName;
                } else {
                    attrName = TableAttribute.ColumnType.NORMAL.name() + "|" + colName;
                }

                attributes[col] = AttributeBuilder.attribute().name(attrName).type(String.class).build();
            }

            return attributes;
        } catch (SQLException exc) {
            throw jdbcTemplate.getExceptionTranslator().translate("processMetadata", null, exc);
        } finally {
            JdbcUtils.closeStatement(ps);
            DataSourceUtils.releaseConnection(con, ds);
        }
    }

    private List<Map<Attribute,Object>> fetchReport(String query) {
        ReportMapper mapper = new ReportMapper(processMetadata(query));

        List<Map<Attribute,Object>> result = jdbcTemplate.query(query, mapper);
        return result;
    }

    public List<Map<Attribute,Object>> fetchMultiHomeProducts() {
        return fetchReport(QUERY_MULTI_HOME_PRODUCTS);
    }

    public List<Map<Attribute,Object>> fetchRecentChanges(int days, int maxRows) {
        String query = MessageFormat.format(QUERY_RECENT_CHANGES, days, String.valueOf(maxRows));
        return fetchReport(query);
    }

    public List<Map<Attribute,Object>> fetchInvisibleProducts() {
        return fetchReport(QUERY_INVISIBLE_PRODUCTS);
    }

    public List<Map<Attribute,Object>> fetchCircularReferences() {
        return fetchReport(QUERY_CIRCULAR_REFERENCES);
    }

    public List<Map<Attribute,Object>> fetchMultipleReferences() {
        return fetchReport(QUERY_MULTIPLE_REFERENCES);
    }

    // FIXME
    public List<Map<Attribute, Object>> fetchHierarchyReport() {
        return Collections.emptyList();
    }

    public List<Map<Attribute,Object>> fetchStackedSKUs() {
        return fetchReport(QUERY_STACKED_SKUS);
    }

    public List<Map<Attribute,Object>> fetchRecipesSummary() {
        return fetchReport(QUERY_RECIPES_SUMMARY);
    }

    public List<Map<Attribute,Object>> fetchPrimaryHomes(String storeObject) {
        String query = MessageFormat.format(QUERY_PRIMARY_HOMES, (storeObject != null ? storeObject : "Store:FreshDirect"));
        return fetchReport(query);
    }

    public List<Map<Attribute,Object>> fetchScarabMerchandisingRules() {
        return fetchReport(QUERY_SCARAB_MERCHANDISING_RULES);
    }

    public List<Map<Attribute,Object>> fetchYmalSets() {
        return fetchReport(QUERY_YMAL_SETS);
    }

    public List<Map<Attribute,Object>> fetchSmartCategoryRecommenders() {
        return fetchReport(QUERY_SMART_CATEGORY_RECOMMENDERS);
    }

    public List<Map<Attribute, Object>> fetchBrokenMediaLinks() {
        return fetchReport(QUERY_BROKEN_MEDIA_LINKS);
    }

    public List<Map<Attribute, Object>> fetchPairItAndCompletedByMealProducts() {
        return fetchReport(QUERY_PAIR_IT_AND_COMPLETED_BY_MEAL);
    }

    public List<Map<Attribute, Object>> fetchRecipes() {
        return fetchReport(QUERY_RECIPES);
    }
}
