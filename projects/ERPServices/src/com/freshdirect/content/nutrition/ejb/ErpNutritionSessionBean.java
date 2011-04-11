package com.freshdirect.content.nutrition.ejb;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.freshdirect.content.nutrition.EnumAllergenValue;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumKosherSymbolValue;
import com.freshdirect.content.nutrition.EnumKosherTypeValue;
import com.freshdirect.content.nutrition.EnumOrganicValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.NutritionInfoAttribute;
import com.freshdirect.content.nutrition.NutritionValueEnum;
import com.freshdirect.erp.model.ActivityLog;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;


/** This class have different methods for MappingLoader and KwikeeLoader to
 * call in order to persist Nutrition data in database.
 */
public class ErpNutritionSessionBean extends SessionBeanSupport {
    
	private static final long	serialVersionUID	= 270497771665382812L;
	
	private static Category LOGGER = LoggerFactory.getInstance( ErpNutritionSessionBean.class );
    
    /** Constructor
     */
    public ErpNutritionSessionBean() {
        super();
    }
    
    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    protected String getResourceCacheKey() {
        return "com.freshdirect.content.nutrition.ejb.ErpNutritionHome";
    }
    
    /** Remove method for container to call
     * @throws EJBException throws EJBException if there is any problem.
     */
    public void ejbRemove() {
    }
    
    /** Gets the nutrition information about the given SKU from the database and
     * populates a ErpNutritionModel Object.
     * @param skuCode skuCode for which nutrition info is needed
     * @return ErpNutritionModel filled with nutrition info
     * for given skuCode
     */
    public ErpNutritionModel getNutrition(String skuCode) {
        
        Connection con = null;
        
        try {
            con = getConnection();
            
            PreparedStatement ps = con.prepareStatement("select nutrition_type, value, uom from ERPS.NUTRITION where sku_code = ?");
            ps.setString(1, skuCode);
            ResultSet rs = ps.executeQuery();
            
            ErpNutritionModel nutritionModel = new ErpNutritionModel();
            nutritionModel.setSkuCode(skuCode);
            
            while (rs.next()){
                String nutritionType = rs.getString("NUTRITION_TYPE");
                double value = rs.getDouble("VALUE");
                String uom = rs.getString("UOM");
                nutritionModel.setUomFor(nutritionType, uom);
                nutritionModel.setValueFor(nutritionType, value);
            }
            rs.close();
            ps.close();
            
            ps = con.prepareStatement("select type, priority, info from erps.nutrition_info where skucode=? order by type, priority");
            ps.setString(1, skuCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString(1);
                int priority = rs.getInt(2);
                String info = rs.getString(3);
                ErpNutritionInfoType infoType = ErpNutritionInfoType.getInfoType(type);
                if (infoType == null) continue;
                Object value = null;
                if (infoType.equals(ErpNutritionInfoType.CLAIM)) {
                    value = EnumClaimValue.getValueForCode(info);
                } else if (infoType.equals(ErpNutritionInfoType.KOSHER_SYMBOL)) {
                    value = EnumKosherSymbolValue.getValueForCode(info);
                } else if (infoType.equals(ErpNutritionInfoType.KOSHER_TYPE)) {
                    value = EnumKosherTypeValue.getValueForCode(info);
                } else if (infoType.equals(ErpNutritionInfoType.ALLERGEN)) {
                    value = EnumAllergenValue.getValueForCode(info);
                } else if (infoType.equals(ErpNutritionInfoType.ORGANIC)) {
                    value = EnumOrganicValue.getValueForCode(info);
                } else {
                    value = info;
                }
                if (value == null) continue;
                nutritionModel.addNutritionAttribute(new NutritionInfoAttribute(infoType, priority, value));
            }
            rs.close();
            ps.close();
            
            return nutritionModel;
            
        } catch (SQLException se) {
            LOGGER.error("Following SQLException occurred " +se.getMessage());
            throw new EJBException(se.getMessage());
        } finally {
            close(con);
        }
        
    }
    
    private static final String LOAD_NUTRITION = "select sku_code, nutrition_type, value, uom, date_modified "
    	+ "from ERPS.NUTRITION "
    	+ "where date_modified > ? "
    	+ "order by sku_code ";
    private static final String LOAD_NUTRITION_INFO = "select skucode, type, priority, info, date_modified,  DBMS_LOB.SUBSTR (info, 4000)info_clob, DBMS_LOB.GETLENGTH(info) length " 
    	+ "from erps.nutrition_info "
    	+ "where date_modified > ? "
    	+ "order by skucode, type, priority ";
    
    public Map<String, ErpNutritionModel> loadNutrition(Date lastModified) {
    	Connection conn = null;
    	try{
    		conn = this.getConnection();    		 
    		long time = System.currentTimeMillis();    		
    		PreparedStatement ps = conn.prepareStatement(LOAD_NUTRITION);
    		ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
    		ResultSet rs = ps.executeQuery();
    		Map<String, ErpNutritionModel> m = new HashMap<String, ErpNutritionModel>();
    		
    		while(rs.next()){
    			Timestamp t = rs.getTimestamp("DATE_MODIFIED");
    			String skuCode = rs.getString("SKU_CODE").intern();
    			ErpNutritionModel model = m.get(skuCode);
    			if(model == null){
    				model = new ErpNutritionModel();
    				model.setSkuCode(skuCode);
    				model.setLastModifiedDate(t);
    				m.put(skuCode, model);
    			}
    			
    			String nutritionType = rs.getString("NUTRITION_TYPE").intern();
                double value = rs.getDouble("VALUE");
                String uom = rs.getString("UOM");
                model.setUomFor(nutritionType, uom);
                model.setValueFor(nutritionType, value);
    		}
    		ps.close();
    		rs.close();
    		
    		ps = conn.prepareStatement(LOAD_NUTRITION_INFO);
    		ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
    		rs = ps.executeQuery();
    		ResultSetMetaData rsmd = rs.getMetaData();
    		String[] columnTypes = new String[rsmd.getColumnCount()];
    		for (int col = 0; col < rsmd.getColumnCount(); col++) {
    			columnTypes[col] = rsmd.getColumnTypeName(col + 1);
    		}
    		

    		 while (rs.next()) {
    			 String skuCode = rs.getString("SKUCODE").intern();
    			 Timestamp t = rs.getTimestamp("DATE_MODIFIED");
    			 ErpNutritionModel model = m.get(skuCode);
    			 if(model == null) {
    				 model = new ErpNutritionModel();
    				 model.setSkuCode(skuCode);
    				 model.setLastModifiedDate(t);
    				 m.put(skuCode, model);
    			 }
                 String type = rs.getString("TYPE").intern();
                 int priority = rs.getInt("PRIORITY");
                 
                 String info = "";
                 
                 //determine how to get the value based on a property
                 if (columnTypes != null && columnTypes.length >=4 && columnTypes[3] == "CLOB") {
                	//works if type is CLOB but not if VARCHAR2
                	 try {
//                		 Clob infoClob = rs.getClob("INFO");

    	                 /*if (infoClob != null) {
    	                	 try {
    	                		 info = infoClob.getSubString(1, (int) (infoClob.length()));
    	                	 }catch (Exception ex) {
    	                		 LOGGER.warn("An error occured while reading CLOB: "+ex.getMessage());
    	                	 }
    	                 }*/
                		 /*if(null!=infoClob){
	                		 java.io.Reader reader0 = infoClob.getCharacterStream();
	                		 BufferedReader reader = new BufferedReader(reader0);
	                		 StringBuilder sb = new StringBuilder();
                		    String line = null;
                		    while ((line = reader.readLine()) != null) {
                		      sb.append(line + "\n");
                		    }
                		    reader0.close();
                		    reader.close();
                		    info = sb.toString();
                		 }*/
                		 info = rs.getString("INFO_CLOB");
                    	 StringBuffer buff = new StringBuffer();
                    	 int size = rs.getInt("LENGTH");
                    	 PreparedStatement ps1 = null;
                    	 ResultSet rs1 = null;
                    	 if(size>4000){
                    		 //Retrieving the clob data in chunks of 4000 chars.
    	                	 for(int i=1; i<=size;i=i+4000){
    	                		try {
    								ps1 = conn.prepareStatement("select DBMS_LOB.SUBSTR (info,"+4000 +","+i 
    								    	+ ") from erps.nutrition_info "
    								    	+ " where skucode =? and type=? and priority=?");
//    								ps1.setTimestamp(1, t);//new Timestamp(lastModified.getTime()));
    								ps1.setString(1, skuCode);
    								ps1.setString(2, type);
    								ps1.setInt(3, priority);
    								rs1 = ps1.executeQuery();
    								if(rs1.next()) {
    									buff.append(rs1.getString(1));
    								}
    							} finally{
    								if(ps1!= null){
    									ps1.close();
    								}if(rs1!=null){
    									rs1.close();
    								}
    							}
    	                	 }
    	                	 info = buff.toString();
                    	 }
	                 } catch (SQLException sqle) {
	                     LOGGER.error("An error occured while reading CLOB, check if column is CLOB type. "+sqle.getMessage());
	                     throw sqle;
	                 } 
                	 
                 }else {
                	//works if column type is VARCHAR2
                	 info = rs.getString("INFO");
                	
                 }
                                  
                 ErpNutritionInfoType infoType = ErpNutritionInfoType.getInfoType(type);
                 if (infoType == null) {
                	 continue;
                 }
                 Object value = null;
                 if (infoType.equals(ErpNutritionInfoType.CLAIM)) {
                     value = EnumClaimValue.getValueForCode(info);
                 } else if (infoType.equals(ErpNutritionInfoType.KOSHER_SYMBOL)) {
                     value = EnumKosherSymbolValue.getValueForCode(info);
                 } else if (infoType.equals(ErpNutritionInfoType.KOSHER_TYPE)) {
                     value = EnumKosherTypeValue.getValueForCode(info);
                 } else if (infoType.equals(ErpNutritionInfoType.ALLERGEN)) {
                     value = EnumAllergenValue.getValueForCode(info);
                 } else if (infoType.equals(ErpNutritionInfoType.ORGANIC)) {
                     value = EnumOrganicValue.getValueForCode(info);
                 } else {
                     value = info;
                 }
                 if (value == null){
                	 continue;
                 }
                 model.addNutritionAttribute(new NutritionInfoAttribute(infoType, priority, value));
             }
    		 ps.close();
    		 rs.close();
    		 time = (System.currentTimeMillis()-time)/1000;
    		 LOGGER.info("Time in loadNutrition():"+time+" secs");
    		 return m;
    	}catch(SQLException e){
    		throw new EJBException(e);
    	}finally {
            close(conn);
        }
    }
    
    /** creates a new nutrition data in the database for given sku
     * using the ErpNutritionModel passed in as parameter.
     * @param nutrition Nutrition model to persist in the database.
     * @throws EJBException throws EJBException if there is any problem accessing
     * database resources.
     */
    public void createNutrition(ErpNutritionModel nutrition) {
        updateNutrition(nutrition, "");
    }
    
    /** Updates the nutrition information by first deleting all the old information for given
     * NutrionModel and then recreating it from the updated model.
     * @param nutrition Updated Nutrion model
     * @throws EJBException throws EJBException if there is any exception in accessing the database
     */
    public void updateNutrition(ErpNutritionModel nutrition, String user) {
        
        Connection con = null;
        try {
            con = getConnection();
            String skuCode = nutrition.getSkuCode();
            
            PreparedStatement pstmt = con.prepareStatement("select nutrition_type, uom, value from erps.nutrition where SKU_CODE = ?");
            pstmt.setString(1, skuCode);
            ResultSet rset = pstmt.executeQuery();
            Hashtable<String, String> oldHash = new Hashtable<String, String>();
            while(rset.next()) {
            	if(rset.getString(2) == null)
            		oldHash.put(rset.getString(1) + "|UOM", "");
            	else
            		oldHash.put(rset.getString(1) + "|UOM", rset.getString(2));
            	if(rset.getString(3) == null)
            		oldHash.put(rset.getString(1), "");
            	else
            		oldHash.put(rset.getString(1), rset.getString(3));
            }
            if(pstmt != null)
            	pstmt.close();
            if(rset != null)
            	rset.close();
            
            PreparedStatement ps = con.prepareStatement("delete from erps.nutrition where SKU_CODE = ?");
            ps.setString(1, skuCode);
            ps.executeUpdate();
            ps.close();
            
            ps = con.prepareStatement("insert into erps.nutrition (SKU_CODE, NUTRITION_TYPE, VALUE, UOM, DATE_MODIFIED) values (?, ?, ?, ?, ? )");
            Iterator<String> it = nutrition.getKeyIterator();

			//get timestamp to replace sysdate
			Timestamp ts = new Timestamp(new Date().getTime());
			List<ActivityLog> aLog = new ArrayList<ActivityLog>();
			
            while (it.hasNext()) {
                String nutritionType = it.next();
                double value = nutrition.getValueFor(nutritionType);
                String uom = nutrition.getUomFor(nutritionType);
                if (uom == null) {
                    uom = "";
                }
                ps.setString(1, skuCode);
                ps.setString(2, nutritionType);
                ps.setDouble(3, value);
                ps.setString(4, uom);
				//use timestamp instead of sysdate
				ps.setTimestamp(5, ts);
                
				//newHash.put(nutritionType + "|UOM", uom);
				String oldValue = "";
	            if(oldHash.containsKey(nutritionType + "|UOM")) {
	            	oldValue = (String) oldHash.get(nutritionType + "|UOM");
	            }
	            if(!oldValue.equalsIgnoreCase(uom)) {
	            	ActivityLog al = new ActivityLog(null, skuCode, "UOM", oldValue, uom, ts, user);
	            	al.setNutritionType(nutritionType);
	            	aLog.add(al);
	            }
	            
	            String val_string = value>0?Double.toString(value):"0";
	            oldValue = "";
	            if(oldHash.containsKey(nutritionType)) {
	            	oldValue = (String) oldHash.get(nutritionType);
	            }
	            if(!oldValue.equalsIgnoreCase(val_string)) {
	            	ActivityLog al = new ActivityLog(null, skuCode, "VALUE", oldValue, val_string, ts, user);
	            	al.setNutritionType(nutritionType);
	            	aLog.add(al);
	            	LOGGER.debug(al.toString());
	            }
				
                
                try {
                    ps.executeUpdate();
                } catch (SQLException sqle) {
                    LOGGER.error("insert into erps.nutrition ("+ skuCode + ", " + nutritionType + ", " + value + ", " + uom + ")");
                    throw sqle;
                }
            }
            ps.close();
            
            pstmt = con.prepareStatement("select type, info, priority from erps.nutrition_info where skucode = ?");
            pstmt.setString(1, skuCode);
            rset = pstmt.executeQuery();
            while (rset.next()) {
            	oldHash.put(ErpNutritionInfoType.getInfoType(rset.getString(1)).getName() +"|" + rset.getString(1) + "|" + rset.getString(3), rset.getString(2));
            }
            
            ps = con.prepareStatement("delete from erps.nutrition_info where skucode=?");
            ps.setString(1, skuCode);
            ps.executeUpdate();
            ps.close();
            
            ps = con.prepareStatement("insert into erps.nutrition_info (skucode, type, priority, info, DATE_MODIFIED) values (?,?,?,?,?)");
            for (Iterator iIter = nutrition.getNutritionAttributes().iterator(); iIter.hasNext(); ) {
                NutritionInfoAttribute attr = (NutritionInfoAttribute) iIter.next();
                if ((attr != null) && attr.getValue() != null) {
                    ps.clearParameters();
                    ps.setString(1, skuCode);
                    ps.setString(2, attr.getNutritionInfoType().getCode());
                    ps.setInt(3, attr.getPriority());
                    String info_value = "";
                    if (attr.getValue() instanceof NutritionValueEnum) {
                    	//newHash.put(attr.getNutritionInfoType().getName()+ "|"+attr.getNutritionInfoType().getCode(), ((NutritionValueEnum)attr.getValue()).getCode());
                    	info_value = ((NutritionValueEnum)attr.getValue()).getCode();
                        ps.setString(4, ((NutritionValueEnum)attr.getValue()).getCode());
                    } else {
                    	//newHash.put(attr.getNutritionInfoType().getName()+ "|"+attr.getNutritionInfoType().getCode(), (String) attr.getValue());
                    	info_value = (String) attr.getValue();
                        ps.setString(4, (String) attr.getValue());
                    }
    				//use timestamp instead of sysdate
    				ps.setTimestamp(5, ts);
                    ps.executeUpdate();
                    
                    String oldValue = "";
                    String key = attr.getNutritionInfoType().getName()+ "|"+attr.getNutritionInfoType().getCode() + "|" + attr.getPriority();
    	            if(oldHash.containsKey(key)) {
    	            	oldValue = (String) oldHash.get(key);
                }
    	            if(!oldValue.equalsIgnoreCase(info_value)) {
    	            	ActivityLog al = new ActivityLog(null, skuCode, "INFO", oldValue, info_value, ts, user);
    	            	al.setNutInfoType(attr.getNutritionInfoType().getCode());
    	            	al.setNutritionPriority(attr.getPriority());
    	            	aLog.add(al);
    	            	LOGGER.debug(al.toString());
            }
                }
            }
            ps.close();
            
            //call activity log method
			com.freshdirect.erp.ejb.ErpActivityLogDAO.logActivity(con, aLog);
            
        } catch (SQLException se) {
            LOGGER.error("Following SQLException occurred " , se);
            throw new EJBException(se.getMessage());
        } finally {
            close(con);
        }
    }
    
    /** deletes nutrition information from database for a given skuCode
     * @param skuCode skuCode of Nutrition info to be removed
     * @throws EJBException throws EJBException if there is any problem in accessing database resources
     */
    public void removeNutrition(String skuCode) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = getConnection();
            
            ps = con.prepareStatement("delete from erps.nutrition where SKU_CODE = ?");
            
            ps.setString(1, skuCode);
            ps.executeUpdate();
            
            ps.close();
            
            ps = con.prepareStatement("delete from erps.nutrition_info where skucode=?");
            ps.setString(1, skuCode);
            ps.executeUpdate();
            ps.close();
        } catch(SQLException se) {
            LOGGER.error("Following SQLException occurred " +se.getMessage());
            throw new EJBException(se.getMessage());
        } finally {
            close(con);
        }
    }
    
    /** Creates an entry in UPC_SKU table in the database
     * for the given UPC SKU pair that is used to map FD
     * SKUs to their UPC.
     * @param skuCode skuCode
     * @param upc UPC
     * @throws EJBException throws EJBException if there is an exception accessing database resources.
     */
    public void createUpcSkuMapping(String skuCode, String upc) {
        
        Connection con = null;
        
        try {
            con = getConnection();
            
            PreparedStatement ps = con.prepareStatement("delete from erps.UPC_SKU where upc=?");
            ps.setString(1, upc);
            ps.executeUpdate();
            ps.close();
            
            ps = con.prepareStatement("insert into erps.UPC_SKU(SKU_CODE, UPC) values(?, ?)");
            ps.setString(1, skuCode);
            ps.setString(2, upc);
            ps.executeUpdate();
            ps.close();
            
        } catch(SQLException se) {
            
            LOGGER.error("Following SQLException occurred " +se.getMessage());
            throw new EJBException(se.getMessage());
            
        } finally {
            close(con);
        }
    }
    /** returns a skuCode from the mapping table for a given upc,
     * if there is a entry in UPC_SKU table otherwise throws FinderException.
     * @param upc UPC to return skuCode for
     * @throws FinderException throws FinderException if there is no entry in the table for given UPC
     * @throws EJBException throws EJBException if there is an exception accessing database resources
     * @return returns a SKU code for given UPC
     */
    public String getSkuCodeForUpc(String upc) throws FinderException{
        Connection con = null;
        
        try {
            con = getConnection();
            
            PreparedStatement ps = con.prepareStatement("select SKU_CODE from erps.UPC_SKU where UPC = ?");
            
            ps.setString(1, upc);
            ResultSet rs = ps.executeQuery();
            String ret = null;
            while(rs.next()) {
                ret = rs.getString("SKU_CODE");
            }
            if ((ret == null) || ret.equals("")) {
                throw new FinderException("No Sku Found for upc: " + upc);
            }
            
            rs.close();
            ps.close();
            
            return ret;
            
        } catch(SQLException se) {
            LOGGER.error("Following SQLException occurred " +se.getMessage());
            throw new EJBException(se.getMessage());
        } finally {
            close(con);
        }
    }
    
    public List<Map<String, String>> generateNutritionReport(){
    	Connection con = null;
    	
    	try{
    		StringBuffer sql = new StringBuffer();
			 sql.append("SELECT s.skucode, s.material_number, s.description, s.status, source,")
				.append("DECODE(SUBSTR(nutrition_type,1,1),'T','YES','NO') AS has_nutrition,") 
				.append("DECODE((SELECT COUNT(*) FROM erps.nutrition WHERE sku_code=s.skucode AND nutrition_type='IGNORE'),0,'NO','YES') AS is_hidden,")
				.append("DECODE((SELECT COUNT(*) FROM erps.nutrition_info WHERE skucode=s.skucode AND TYPE='CLAM'),0,'NO DATA',1,(SELECT DECODE(info,'NONE','NONE','YES') FROM erps.nutrition_info WHERE skucode=s.skucode AND TYPE='CLAM'),'YES') AS has_claims,")
				.append("DECODE((SELECT COUNT(*) FROM erps.nutrition_info WHERE skucode=s.skucode AND TYPE='ALRG'),0,'NO DATA',1,(SELECT DECODE(info,'NONE','NONE','YES') FROM erps.nutrition_info WHERE skucode=s.skucode AND TYPE='ALRG'),'YES') AS has_allergens,")
				.append("DECODE((SELECT COUNT(*) FROM erps.nutrition_info WHERE skucode=s.skucode AND TYPE='ORGN'),0,'NO DATA',1,(SELECT DECODE(info,'NONE','NONE','YES') FROM erps.nutrition_info WHERE skucode=s.skucode AND TYPE='ORGN'),'YES') AS has_organic,")
				.append("(SELECT info FROM erps.nutrition_info WHERE TYPE='KSYM' AND skucode=s.skucode) AS kosher_symbol,")
				.append("(SELECT info FROM erps.nutrition_info WHERE TYPE='KTYP' AND skucode=s.skucode) AS kosher_type,")
				.append("(SELECT TRANSLATE(TRANSLATE(info,CHR(13),' '),CHR(10),' ') FROM erps.nutrition_info WHERE TYPE='HNGR' AND skucode=s.skucode) AS notes,")
				.append("(SELECT TRANSLATE(TRANSLATE(info,CHR(13),' '),CHR(10),' ') FROM erps.nutrition_info WHERE TYPE='HEAT' AND skucode=s.skucode) AS heating,")
				.append("TRANSLATE(TRANSLATE(info,CHR(13),' '),CHR(10),' ') AS ingredients ")
				.append("FROM erps.nutrition, erps.nutrition_info,")
				.append("(SELECT skus.sku_code AS skucode, skus.material_number, skus.description AS description, skus.status AS status, uom AS source FROM erps.nutrition n,")
				.append("(SELECT prd.sku_code AS sku_code, mtl.sap_id AS material_number, mtl.description AS description, NVL(prd.unavailability_reason, 'Available') AS status FROM erps.product prd, erps.materialproxy mpx, erps.material mtl ")
				.append("WHERE prd.version=(SELECT MAX(version) FROM erps.product WHERE sku_code=prd.sku_code)")
				.append("AND prd.id=mpx.product_id AND mpx.mat_id=mtl.id) skus ")
				.append("WHERE skus.sku_code=n.sku_code(+) AND 'SOURCE'=n.nutrition_type(+)) s ")
				.append("WHERE s.skucode=nutrition.sku_code(+) AND 'TOTAL_CALORIES'=nutrition.nutrition_type(+) ")
				.append("AND s.skucode=nutrition_info.skucode(+) AND 'INGR'=nutrition_info.TYPE(+) ")
				.append("ORDER BY s.skucode ");
				
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			
			List<Map<String, String>> report = new ArrayList<Map<String, String>>();
			while(rs.next()){
				Map<String, String> m = new HashMap<String, String>();
				m.put("SKUCODE", rs.getString("SKUCODE"));
				m.put("MATERIAL_NUMBER", rs.getString("MATERIAL_NUMBER"));
				m.put("DESCRIPTION", rs.getString("DESCRIPTION"));
				m.put("STATUS", rs.getString("STATUS"));
				m.put("SOURCE", rs.getString("SOURCE"));
				m.put("HAS_NUTRITION", rs.getString("HAS_NUTRITION"));
				m.put("IS_HIDDEN", rs.getString("IS_HIDDEN"));
				m.put("HAS_CLAIMS", rs.getString("HAS_CLAIMS"));
				m.put("HAS_ALLERGENS", rs.getString("HAS_ALLERGENS"));
				m.put("HAS_ORGANIC", rs.getString("HAS_ORGANIC"));
				m.put("KOSHER_SYMBOL", rs.getString("KOSHER_SYMBOL"));
				m.put("KOSHER_TYPE", rs.getString("KOSHER_TYPE"));
				m.put("NOTES", rs.getString("NOTES"));
				m.put("HEATING", rs.getString("HEATING"));
				m.put("INGREDIENTS", rs.getString("INGREDIENTS"));
				
				for(Iterator<String> i = m.values().iterator();i.hasNext();){
					String value = i.next();
					if(value != null){
						value.trim();
					}
				}
				
				report.add(m);
			}
			
			return report;
			
		} catch(SQLException se) {
			LOGGER.error("Following SQLException occurred " +se.getMessage());
			throw new EJBException(se.getMessage());
		} finally {
                    close(con);
		}
    }
    
    public List<Map<String, String>> generateClaimsReport(){
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		conn = this.getConnection();
    		ps = conn.prepareStatement(
				"SELECT cn.content_name AS skucode, cn.full_name, p.unavailability_status, cni.flag AS has_claim, oni.flag AS has_organic " +
				"FROM CONTENTNODE cn, erps.PRODUCT p, " +
				"(SELECT ni.skucode, 'X' AS flag " +
				"FROM erps.nutrition_info ni " +
				"WHERE ni.TYPE='CLAM' AND ni.info IS NOT NULL AND ni.info<>'NONE' " +
				"GROUP BY ni.skucode) cni, " +
				"(SELECT ni.skucode, 'X' AS flag " +
				"FROM erps.nutrition_info ni " +
				"WHERE ni.TYPE='ORGN' AND ni.info IS NOT NULL AND ni.info<>'NONE' " +
				"GROUP BY ni.skucode) oni " +
				"WHERE cn.CONTENT_NAME=p.sku_code " +
				"AND cni.SKUCODE(+)=cn.CONTENT_NAME " +
				"AND oni.SKUCODE(+)=cn.CONTENT_NAME " +
				"AND p.version=(SELECT MAX(version) FROM erps.PRODUCT WHERE sku_code=p.sku_code) " +
				"ORDER BY cn.full_name ");
				
			rs = ps.executeQuery();
			
			List<Map<String, String>> report = new ArrayList<Map<String, String>>();
			while(rs.next()){
				Map<String, String> m = new HashMap<String, String>();
				m.put("SKUCODE", rs.getString("SKUCODE"));
				m.put("FULL_NAME", rs.getString("FULL_NAME"));
				m.put("UNAVAILABILITY_STATUS", rs.getString("UNAVAILABILITY_STATUS"));
				m.put("HAS_CLAIM", rs.getString("HAS_CLAIM"));
				m.put("HAS_ORGANIC", rs.getString("HAS_ORGANIC"));
				report.add(m);
			}
			
			return report;
			
		} catch(SQLException se) {
			LOGGER.error("Following SQLException occurred " +se.getMessage());
			throw new EJBException(se.getMessage());
		} finally {
			try {
				if (ps != null) ps.close();
				if (rs != null) rs.close();
			} catch (SQLException sqle2) {
				LOGGER.warn("Unable to close connection", sqle2);
			}
                    close(conn);
    	}
    }
    
}
