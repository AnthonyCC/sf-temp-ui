package com.freshdirect.cms.publish.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.publish.config.DatabaseConfig;
import com.freshdirect.cms.publish.util.TaskTimer;

public final class ERPSDataRepository extends JdbcRepository {
    
    private static final Logger LOGGER = Logger.getLogger(ERPSDataRepository.class);

    /**
     * Query to fetch SKU->Material mapping
     */
    private static final String FETCH_SKU_MAT_SQL =
            "select m.SKUCODE, m.sap_id as material_id " +
            "from erps.material m, " +
            "( " +
            "  select sap_id s, max(version) max_ver " +
            "  from erps.material " +
            "  group by sap_id " +
            ") m_latest " +
            "where m.sap_id=m_latest.s and m.version=m_latest.max_ver";
    
    /**
     * Query to fetch material configurations
     */
    private static final String FETCH_MAT_CONFIG_SQL =
            "select m.sap_id as material_id, c.sap_id as cname, ch.name as chname, cv.name as chvalue " +
            "from erps.class c, " +
            "  erps.characteristic ch, " +
            "  erps.charvalue cv, " +
            "  erps.material_class mc, " +
            "  erps.material m " +
            "where c.version= " +
            "  ( " +
            "    select max(version) from erps.class " +
            "    where sap_id=c.sap_id " +
            "  ) " +
            "  and c.id=ch.class_id " +
            "  and ch.id=cv.char_id " +
            "  and mc.CLASS_ID=c.ID " +
            "  and m.id=mc.MAT_ID " +
            "  and m.version=( " +
            "    select max(version) " +
            "    from erps.material " +
            "    where sap_id=m.sap_id " +
            "  ) " +
            "order by m.sap_id, c.sap_id, ch.name, cv.name ";
    /**
     * Query to fetch sales units for each materials
     */
    private static final String FETCH_SALES_UNITS_SQL =
            "SELECT m.sap_id, SU.ALTERNATIVE_UNIT " +
            "FROM ( " +
            "  select sap_id s, max(version) v " +
            "  from erps.material m " +
            "  group by sap_id " +
            ") T, " +
            "erps.material m, erps.salesunit su " +
            "where T.s=M.sap_id and T.v=m.version " +
            "and m.id=SU.MAT_ID and M.VERSION=su.version ";    

    /**
     * Fetch SKU material associations
     * 
     * @return
     */
    public Map<String,String> fetchSkuMaterialAssociations() {
        
        Map<String,String> result = new HashMap<String,String>(50000);
        
        TaskTimer timer = null;
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            
            stmt = createFastReaderStatement(conn);

            timer = new TaskTimer("SKU Material ID associations");
            rs = stmt.executeQuery(FETCH_SKU_MAT_SQL);
            while (rs.next()) {
                final String skuCode = rs.getString(1);
                final String materialId = rs.getString(2);
                result.put(skuCode, materialId);
            }
            rs.close();
            rs = null;
            timer.logTimeSpent(LOGGER, "Loaded " + result.size() + " entries");

        } catch (SQLException exc) {
            LOGGER.error("DB error raised during building media nodes", exc);
        } finally {
            closeResources(rs, stmt, conn);
        }

        return result;
    }



    /**
     * Returns the material configuration options
     * in compact form
     * 
     *   "Char1=CharVal11|CharVal12|...,Char2=..."
     *   
     * where CharN denotes an ERPS characteristics
     * and the list of CharValN1
     * 
     * @return
     */
    public Map<String,Map<String,Set<String>>> fetchMaterialConfigurationMap() {
        
        Map<String,Map<String,Set<String>>> result = new HashMap<String,Map<String,Set<String>>>(700);
        
        TaskTimer timer = null;
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            
            stmt = createFastReaderStatement(conn);

            timer = new TaskTimer("Material Configuration Options mapping");
            rs = stmt.executeQuery(FETCH_MAT_CONFIG_SQL);
            
            String currentMaterial = null;
            Map<String,Set<String>> currentCharValues = null;
            
            while (rs.next()) {
                final String matId = rs.getString(1);
                final String charName = rs.getString(3);
                final String charValue = rs.getString(4);


                if (currentMaterial == null || !currentMaterial.equals(matId)) {
                    if (currentMaterial != null) {
                        result.put(currentMaterial, currentCharValues);
                    }

                    // set the new material to work on
                    currentMaterial = matId;
                    currentCharValues = new HashMap<String,Set<String>>();
                    
                }

                Set<String> characteristicValueSet = currentCharValues.get(charName);
                if (characteristicValueSet == null) {
                    characteristicValueSet = new HashSet<String>();
                    currentCharValues.put(charName, characteristicValueSet);
                }
                characteristicValueSet.add(charValue);

            }

            // append last value
            if (currentMaterial != null) {
                result.put(currentMaterial, currentCharValues);
            }
            
            rs.close();
            rs = null;
            timer.logTimeSpent(LOGGER, "Loaded " + result.size() + " entries");

        } catch (SQLException exc) {
            LOGGER.error("DB error raised during building media nodes", exc);
        } finally {
            closeResources(rs, stmt, conn);
        }

        return result;
    }
    

    /**
     * Retrieves sales units for materials
     * 
     * @return material ID -> Sales Unit mapping
     */
    public Map<String,Set<String>> fetchSalesUnits() {
        Map<String,Set<String>> result = new HashMap<String,Set<String>>(51000);
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            
            stmt = createFastReaderStatement(conn);

            final TaskTimer timer = new TaskTimer("Material Sales-Unit assignment");
            rs = stmt.executeQuery(FETCH_SALES_UNITS_SQL);
            
            while (rs.next()) {
                String matId = rs.getString(1);
                String salesUnit = rs.getString(2);
                
                Set<String> suSet = result.get(matId);
                if (suSet == null) {
                    suSet = new HashSet<String>();
                    result.put(matId, suSet);
                }
                
                suSet.add(salesUnit);
            }

            rs.close();
            rs = null;

            timer.logTimeSpent(LOGGER, "Loaded " + result.size() + " entries");

        } catch (SQLException exc) {
            LOGGER.error("DB error raised during building media nodes", exc);
        } finally {
            closeResources(rs, stmt, conn);
        }

        return result;
    }
}
