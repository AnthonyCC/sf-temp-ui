package com.freshdirect.cms.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.freshdirect.cms.persistence.erps.data.MaterialConfig;
import com.freshdirect.cms.persistence.erps.data.MaterialData;
import com.freshdirect.cms.persistence.erps.data.MaterialSalesUnit;
import com.freshdirect.cms.persistence.erps.data.SkuMaterial;

@Profile("database")
@Repository
public class ERPSDataRepository {

    private static final String SKU_MATERIAL_ASSOCIATION_QUERY = "select m.skucode, m.sap_id from erps.material m where m.version=(select max(version) from erps.material where skucode=m.skucode)";

    private static final String ONE_SKU_MATERIAL_ASSOCIATION_QUERY = "select m.skucode, m.sap_id from erps.material m where m.version=(select max(version) from erps.material where skucode=m.skucode) and skucode=?";

    private static final String MATERIAL_CONFIG_ASSOCIATION_QUERY = "select m.sap_id, ch.name as ch_name, cv.name as cv_name, cv.description from erps.class c0, erps.class c, erps.characteristic ch, erps.charvalue cv, "
            + "erps.material_class mc, erps.material m "
            + "where c0.id=mc.class_id "
            + "and c.sap_id=c0.sap_id "
            + "and c.version=(select max(version) from erps.class where sap_id=c0.sap_id) "
            + "and c.id=ch.class_id "
            + "and ch.id=cv.char_id "
            + "and m.id=mc.MAT_ID and m.version=(select max(version) from erps.material where sap_id=m.sap_id) "
            + "order by m.sap_id, c.sap_id, ch.name, cv.name ";

    private static final String SALES_UNIT_QUERY = "SELECT m.sap_id, SU.ALTERNATIVE_UNIT, SU.DESCRIPTION FROM (select sap_id s, max(version) v from erps.material m "
            + "group by sap_id) T, erps.material m, erps.salesunit su where T.s=M.sap_id and T.v=m.version and m.id=SU.MAT_ID and M.VERSION=su.version";

    private static final String ERP_MATERIAL_LABEL_QUERY = "select m.sap_id, ltrim(m.sap_id, '0') as material_name, m.description, m.upc, msa.UNAVAILABILITY_STATUS, pm.atp_rule, pm.lead_time, upc, alcoholic_content, taxable, pm.kosher_production, pm.platter, pm.blocked_days, DECODE(MP.PROMO_PRICE, 0, MP.PRICE, MP.PROMO_PRICE) AS price, m.version "
            + "from erps.material m, (select sap_id s, max(version) v from erps.material group  by sap_id) t, ERPS.PLANT_MATERIAL pm, erps.material_sales_area msa, erps.materialprice mp "
            + "where pm.mat_id=m.id and PM.PLANT_ID='1000' and m.sap_id=T.s and m.version=T.v "
            + "and m.version=mp.version and mp.sap_zone_id = '0000100000' AND mp.scale_quantity<=1 and m.id=MP.MAT_ID "
            + "and msa.MAT_ID=m.id";

    private static final String ERP_CHARVAL_QUERY = "select cv.name, cv.description from CHARVALUE cv "
            + "join characteristic c on (c.id = cv.CHAR_ID) "
            + "join class cl on (cl.id = c.CLASS_ID) "
            + "where cl.version = (select max(version) from class where sap_id=?) "
            + "and cl.sap_id=? "
            + "and c.name=?";

    private static final String ERP_CLASSES_QUERY = "select c.sap_id as class_id from class c " +
            "join material_class mc on (mc.class_id = c.id) " +
            "join material m on(m.id = mc.mat_id) " +
            "where m.sap_id=? and m.version = (select max(version) from material where sap_id=?) " +
            "and c.version = (select max(version) from class where sap_id=c.sap_id)";

    private static final String ERP_CHARS_QUERY = "select ch.name " +
            "from class c, characteristic ch " +
            "where c.version=(select max(version) from class where sap_id=c.sap_id) " +
            "and c.id=ch.class_id " +
            "and c.sap_id=?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("erpsDataSource")
    private void setDataSource(DataSource erpsDataSource) {
        jdbcTemplate = new JdbcTemplate(erpsDataSource);
    }

    public List<SkuMaterial> findAllSkuMaterialAssociations() {
        return jdbcTemplate.query(SKU_MATERIAL_ASSOCIATION_QUERY, new RowMapper<SkuMaterial>() {

            @Override
            public SkuMaterial mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SkuMaterial(rs.getString("skucode"), rs.getString("sap_id"));
            }
        });
    }

    public List<SkuMaterial> findSkuMaterialAssociation(String skuCode) {
        return jdbcTemplate.query(ONE_SKU_MATERIAL_ASSOCIATION_QUERY, new Object[]{skuCode}, new RowMapper<SkuMaterial>() {

            @Override
            public SkuMaterial mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SkuMaterial(rs.getString("skucode"), rs.getString("sap_id"));
            }
        });
    }

    public List<MaterialConfig> findAllMaterialConfigurations() {
        return jdbcTemplate.query(MATERIAL_CONFIG_ASSOCIATION_QUERY, new RowMapper<MaterialConfig>() {

            @Override
            public MaterialConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MaterialConfig(rs.getString("sap_id"), rs.getString("ch_name"), rs.getString("cv_name"), rs.getString("description"));
            }
        });
    }

    public List<MaterialSalesUnit> findAllSalesUnits() {
        return jdbcTemplate.query(SALES_UNIT_QUERY, new RowMapper<MaterialSalesUnit>() {

            @Override
            public MaterialSalesUnit mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MaterialSalesUnit(rs.getString("sap_id"), rs.getString("alternative_unit"), rs.getString("description"));
            }
        });
    }

    public List<MaterialData> findAllMaterialData() {
        return jdbcTemplate.query(ERP_MATERIAL_LABEL_QUERY, new RowMapper<MaterialData>() {

            @Override
            public MaterialData mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MaterialData(rs.getString("sap_id"), rs.getString("material_name"), rs.getString("description"), rs.getString("upc"), rs.getString("UNAVAILABILITY_STATUS"), rs.getString("atp_rule"), rs.getString("lead_time"), rs.getString("alcoholic_content"), rs.getString("taxable"), rs.getString("kosher_production"), rs.getString("platter"), rs.getString("blocked_days"), rs.getDouble("price"), rs.getInt("version"));

            }
        });
    }

    public Map<String, String> findCharacteristicValues(String sapId, String characteristicName) {
        final String params[] = {sapId, sapId, characteristicName};
        final Map<String, String> result = new LinkedHashMap<String, String>();

        jdbcTemplate.query(ERP_CHARVAL_QUERY, params, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String charName = rs.getString(1);
                String charDesc = rs.getString(2);
                result.put(charName, charDesc);
            }
        });

        return result;
    }

    public List<String> findClassesForMaterial(String materialId) {
        return jdbcTemplate.query(ERP_CLASSES_QUERY, new String[] {materialId, materialId}, new RowMapper<String>() {
           @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
    }

    public List<String> findCharacteristicsForClass(String classId) {
        return jdbcTemplate.query(ERP_CHARS_QUERY, new String[] {classId}, new RowMapper<String>() {
           @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
    }
}
