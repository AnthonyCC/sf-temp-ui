/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Category;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpMaterialInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpPlantMaterialInfo;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpInfoSessionBean extends SessionBeanSupport {

	/** logger for messages */
	private static Category LOGGER = LoggerFactory.getInstance(ErpInfoSessionBean.class);

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.erp.ejb.ErpInfoHome";
	}

	public Collection<ErpMaterialInfoModel> findMaterialsByBatch(int batchNumber) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps =
				conn.prepareStatement(
					"select id, version, sap_id, description from erps.material where version = ? order by description");
			ps.setInt(1, batchNumber);
			rs = ps.executeQuery();

			ArrayList<ErpMaterialInfoModel> materials = new ArrayList<ErpMaterialInfoModel>();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

//			rs.close();
//			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for batch " + batchNumber, sqle);
			throw new EJBException(sqle);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}
	}

	private final static String QUERY_MATERIALS_BY_SAPID =
		"select max(to_number(id)), max(version), sap_id, description"
			+ " from (select id, version, sap_id, description from erps.material where sap_id like ?)"
			+ " group by sap_id, description order by description";

	public Collection<ErpMaterialInfoModel> findMaterialsBySapId(String sapId) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();

			ps = conn.prepareStatement(QUERY_MATERIALS_BY_SAPID);
			ps.setString(1, "%" + sapId);
			rs = ps.executeQuery();

			ArrayList<ErpMaterialInfoModel> materials = new ArrayList<ErpMaterialInfoModel>();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

//			rs.close();
//			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for SapId " + sapId, sqle);
			throw new EJBException(sqle);
		} finally {
			close(rs);
			close(ps);
            close(conn);
		}
	}

	private final static String QUERY_MATERIALS_BY_SKU =
		/*"select max(to_number(id)), max(version), sap_id, description"
			+ " from (select m.ID as id, m.VERSION as version, m.sap_id as sap_id, m.description as description"
			+ " from erps.material m, erps.materialproxy mpx, erps.product p where p.sku_code = ? and p.id = mpx.product_id and mpx.mat_id=m.id)"
			+ " group by sap_id, description order by description";*/
			
			"select max(to_number(id)), max(version), sap_id, description"
			+ " from (select m.ID as id, m.VERSION as version, m.sap_id as sap_id, m.description as description"
			+ " from erps.material m where m.skucode = ?)"
			+ " group by sap_id, description order by description";

	public Collection<ErpMaterialInfoModel> findMaterialsBySku(String skuCode) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();

			ps = conn.prepareStatement(QUERY_MATERIALS_BY_SKU);
			ps.setString(1, skuCode);
			rs = ps.executeQuery();

			ArrayList<ErpMaterialInfoModel> materials = new ArrayList<ErpMaterialInfoModel>();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

//			rs.close();
//			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for SKU " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}
	}

	private final static String QUERY_MATERIALS_BY_DESCRIPTION =
		"select max(to_number(id)), max(version), sap_id, description"
			+ " from (select id, version, sap_id, description from erps.material where description like ?)"
			+ " group by sap_id, description order by description";

	public Collection<ErpMaterialInfoModel> findMaterialsByDescription(String description) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();

			ps = conn.prepareStatement(QUERY_MATERIALS_BY_DESCRIPTION);
			ps.setString(1, "%" + description.toUpperCase() + "%");
			rs = ps.executeQuery();

			ArrayList<ErpMaterialInfoModel> materials = new ArrayList<ErpMaterialInfoModel>();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

//			rs.close();
//			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for description \"" + description + "\"", sqle);
			throw new EJBException(sqle);
		} finally {
			close(rs);
			close(ps);
            close(conn);
		}
	}

	private final static String QUERY_PRODUCTS_BY_SKU =
/*		"select p.version, m.sap_id, p.unavailability_status, p.unavailability_date,"
			+ " p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
			+ " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, "
			+ " p.days_fresh, p.days_in_house "
			+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
			+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id and p.sku_code = ?"
			+ " and p.version = (select max(version) from erps.product where sku_code = ?)";

		" select version, sap_id, unavailability_status, unavailability_date,"
		+ " unavailability_reason, description, atp_rule, rating, price,"
		+ " pricing_unit, promo_price, scale_unit, scale_quantity, sap_zone_id,"
		+ " days_fresh, days_in_house "		
		+ " from ("
		+ " select rank () over (order by p.version desc) rank, p.version, m.sap_id, p.unavailability_status, p.unavailability_date,"
		+ " p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
		+ " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
		+ " p.days_fresh, p.days_in_house "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id"
		+ " and mpx.mat_id = m.id"
		+ " and mp.mat_id = m.id"
		+ " and p.sku_code = ?)" 
		+ " where rank = 1";*/
		  /* "SELECT p.version, m.sap_id, unavailability_status, unavailability_date, "+
		   "unavailability_reason, description, atp_rule, rating, price, "+
		   "pricing_unit, promo_price, scale_unit, scale_quantity, sap_zone_id, "+
		   "days_fresh, days_in_house, sustainability_rating, upc "+
		   "FROM erps.materialprice mp, erps. material m,erps.materialproxy mpx, erps.product p, "+
		   "(SELECT MAX(p1.version) AS V FROM erps.product p1 WHERE p1.sku_code =?) t "+
		   "WHERE  m.id=mpx.mat_id AND m.id= mp.mat_id  AND p.id=mpx.product_id AND  p.version=t.V AND p.sku_code =?";*/
			
			 "SELECT m.version, m.sap_id, p.plant_id,ms.sales_org,ms.distribution_channel, ms.unavailability_status, ms.unavailability_date, "+
			   "ms.unavailability_reason, m.description, p.atp_rule, p.rating, price, "+
			   "pricing_unit, promo_price, scale_unit, scale_quantity, sap_zone_id, mp.sales_org as mp_sales_org,mp.distribution_channel as mp_distribution_channel,  "+
			   "p.daysfresh, p.days_in_house, p.sustainability_rating, m.upc,p.KOSHER_PRODUCTION,p.platter,p.blocked_days,p.HIDE_OOS,ms.DAYPART_VALUE, m.alcoholic_content, ms.picking_plant_id "+
			   "FROM erps.materialprice mp, erps.material m,erps.plant_material p,erps.material_sales_area ms, "+
			   "(SELECT MAX(m1.version) AS V FROM erps.material m1 WHERE m1.skucode =?) t "+
			   "WHERE  m.id=p.mat_id AND m.id= mp.mat_id and m.id=ms.mat_id AND m.version=t.V AND m.skucode =?";	
			
			
		
	public ErpProductInfoModel findProductBySku(String skuCode) throws ObjectNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();

			ps = conn.prepareStatement(QUERY_PRODUCTS_BY_SKU);
			ps.setString(1, skuCode);
			ps.setString(2, skuCode);

			rs = ps.executeQuery();

			if (!rs.next()) {
				throw new ObjectNotFoundException("SKU " + skuCode + " not found");
			}
			
			return fetchErpProductInfoModel(rs, skuCode);
			

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product for SKU " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}

	private static final String skuVersionQuery =
		        /*"select m.sap_id, p.unavailability_status, p.unavailability_date,"
			+ " p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
		        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"         
   			+ " p.days_fresh, p.days_in_house, p.sustainability_rating, m.upc "
			+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
			+ " where p.id=mpx.product_id and mpx.mat_id=m.id and mp.mat_id = m.id and p.sku_code = ? and p.version = ?";*/
			"select m.sap_id, ms.unavailability_status, ms.unavailability_date,"
			+ " ms.unavailability_reason, m.description, p.atp_rule, p.rating, mp.price,"
		        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"         
   			+ " p.daysfresh, p.days_in_house, p.sustainability_rating, m.upc,ms.sales_org,ms.distribution_channel,p.KOSHER_PRODUCTION,p.platter,p.blocked_days,p.HIDE_OOS,ms.DAYPART_VALUE,M.ALCOHOLIC_CONTENT, ms.picking_plant_id "
			+ " from erps.plant_material p, erps.material m, erps.materialprice mp, erps.MATERIAL_SALES_AREA ms"
			+ " where p.mat_id=m.id and mp.mat_id = m.id and ms.mat_id=m.id and m.skucode = ? and m.version = ?";
	
	public ErpProductInfoModel findProductBySku(String skuCode, int version) throws ObjectNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();

			ps = conn.prepareStatement(skuVersionQuery);
			ps.setString(1, skuCode);
			ps.setInt(2, version);

			rs = ps.executeQuery();

			if (rs.next()) {
				List<String> matNos = new ArrayList<String>(5);
				Set<ErpMaterialPrice> matPrices = new HashSet<ErpMaterialPrice>(5);
				Set<ErpPlantMaterialInfo> matPlants = new HashSet<ErpPlantMaterialInfo>(5);
				Set<ErpMaterialSalesAreaInfo> matSalesAreas = new HashSet<ErpMaterialSalesAreaInfo>(5);
				String descr = rs.getString(5);				
				String days_fresh = rs.getString(14);				
				String upc=rs.getString(17);				

				matNos.add(rs.getString(1));
				matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(8), rs.getString(9), rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13), null, null));

				while (rs.next()) {
					matNos.add(rs.getString(1));
					matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(8), rs.getString(9), rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13), null, null));
					
					String days_in_house = rs.getString(15);
					String sustainabilityRating=rs.getString(16);
					//String freshness = getFreshnessValue(days_fresh, days_in_house);
					matPlants.add(new ErpPlantMaterialInfo("X".equalsIgnoreCase(rs.getString("KOSHER_PRODUCTION")), "X".equalsIgnoreCase(rs.getString("PLATTER")), DayOfWeekSet.decode(rs.getString("BLOCKED_DAYS")), EnumATPRule.getEnum(rs.getInt("ATP_RULE")), rs.getString("RATING"), days_fresh, sustainabilityRating,rs.getString("plant_id"),"X".equalsIgnoreCase(rs.getString("HIDE_OOS"))));
					matSalesAreas.add((new ErpMaterialSalesAreaInfo(new SalesAreaInfo(rs.getString("sales_org"),rs.getString("distribution_channel")), rs.getString("unavailability_status"), rs.getDate("unavailability_date"), rs.getString("unavailability_reason"),rs.getString("DAYPART_VALUE"),rs.getString("PICKING_PLANT_ID"))));
					
				}
				//boolean isAlcohol=StringUtil.isEmpty(rs.getString("ALCOHOLIC_CONTENT"))?false:true;
				EnumAlcoholicContent alcoholType = StringUtil.isEmpty(rs.getString("ALCOHOLIC_CONTENT"))? EnumAlcoholicContent.NONE:EnumAlcoholicContent.getAlcoholicContent(rs.getString("ALCOHOLIC_CONTENT"));
				 return new ErpProductInfoModel(
				        	skuCode,version,
				        	matNos.toArray(new String[0]),
				        	descr,
				        	matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),       
				        	upc, matPlants.toArray(new ErpProductInfoModel.ErpPlantMaterialInfo[0]),matSalesAreas.toArray(new ErpProductInfoModel.ErpMaterialSalesAreaInfo[0]),alcoholType); 
			}
			throw new ObjectNotFoundException("SKU " + skuCode + ", version " + version + " not found");

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product for SKU " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}

	public Collection<ErpProductInfoModel> findProductsBySku(String[] skuCodes) {
		Connection conn = null;
		PreparedStatement ps = null;
		List<ErpProductInfoModel> products = new ArrayList<ErpProductInfoModel>();
		if (skuCodes != null && skuCodes.length > 0) {
			try {
				conn = getConnection();
				ps = conn.prepareStatement(QUERY_PRODUCTS_BY_SKU);
				for (int i = 0; i < skuCodes.length; i++) {
					ps.setString(1, skuCodes[i]);
					ps.setString(2, skuCodes[i]);
					ResultSet rs = null;
					try {
						rs = ps.executeQuery();
						if (rs.next()) {
							ErpProductInfoModel m = fetchErpProductInfoModel(rs, skuCodes[i]);
							products.add(m);
						}
					} finally {
						close(rs);						
					}
				}
			} catch (SQLException sqle) {
				LOGGER.error("Error finding SKUs ", sqle);
				throw new EJBException(sqle);
			} catch (Exception e) {
				LOGGER.error("Unhandled exception in findProductsBySku : " + e.getMessage());
				throw new EJBException(e);
			} finally {
				close(ps); 
				close(conn);
			}
		}
		return products;
	}

    /**
     * @param rs
     * @param skuCode
     * @return
     * @throws SQLException
     */
    private ErpProductInfoModel fetchErpProductInfoModel(ResultSet rs, String skuCode) throws SQLException {
        
        List<String> matNos = new ArrayList<String>(5);
        Set<ErpMaterialPrice> matPrices = new HashSet<ErpMaterialPrice>(5);
        Set<ErpPlantMaterialInfo> matPlants = new HashSet<ErpPlantMaterialInfo>(5);
        Set<ErpMaterialSalesAreaInfo> matSalesAreas = new HashSet<ErpMaterialSalesAreaInfo>(5);
        int version = rs.getInt("version");
        String descr = rs.getString("description");
        String days_fresh = rs.getString("daysfresh"); 
        String upc=rs.getString("upc");     
        //boolean isAlcohol=StringUtil.isEmpty(rs.getString("ALCOHOLIC_CONTENT"))?false:true;
		EnumAlcoholicContent alcoholType =getEnumAlcoholicContent(rs.getString("ALCOHOLIC_CONTENT"));
        fetchErpProductInfoModel(rs, matNos, matPrices, matPlants, matSalesAreas, days_fresh);
        while (rs.next()) {
        	fetchErpProductInfoModel(rs, matNos, matPrices, matPlants,	matSalesAreas, rs.getString("daysfresh"));
        }

        return new ErpProductInfoModel(
        	skuCode,version,
        	matNos.toArray(new String[0]),
        	descr,
        	matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),       
        	upc, matPlants.toArray(new ErpProductInfoModel.ErpPlantMaterialInfo[0]),matSalesAreas.toArray(new ErpProductInfoModel.ErpMaterialSalesAreaInfo[0]),alcoholType);        
    }
	private static EnumAlcoholicContent getEnumAlcoholicContent(String alcoholicContentType) {
    	return StringUtil.isEmpty(alcoholicContentType)? EnumAlcoholicContent.NONE:EnumAlcoholicContent.getAlcoholicContent(alcoholicContentType);
    }

	/**
	 * @param rs
	 * @param matNos
	 * @param matPrices
	 * @param matPlants
	 * @param matSalesAreas
	 * @param days_fresh
	 * @throws SQLException
	 */
	private void fetchErpProductInfoModel(ResultSet rs, List<String> matNos,
			Set<ErpMaterialPrice> matPrices,
			Set<ErpPlantMaterialInfo> matPlants,
			Set<ErpMaterialSalesAreaInfo> matSalesAreas, String days_fresh)
			throws SQLException {
		matNos.add(rs.getString("sap_id"));
        matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble("price"), rs.getString("pricing_unit"), rs.getDouble("promo_price"), rs.getString("scale_unit"), rs.getDouble("scale_quantity"), rs.getString("sap_zone_id") , rs.getString("mp_sales_org"),rs.getString("mp_distribution_channel")));
        String sustainabilityRating=rs.getString("sustainability_rating");
        String days_in_house = rs.getString("days_in_house");
        //String freshness = getFreshnessValue(days_fresh, days_in_house);                            
        matPlants.add(new ErpPlantMaterialInfo("X".equalsIgnoreCase(rs.getString("KOSHER_PRODUCTION")), "X".equalsIgnoreCase(rs.getString("PLATTER")), DayOfWeekSet.decode(rs.getString("BLOCKED_DAYS")), EnumATPRule.getEnum(rs.getInt("ATP_RULE")), rs.getString("RATING"), days_fresh, sustainabilityRating,rs.getString("plant_id"),"X".equalsIgnoreCase(rs.getString("HIDE_OOS"))));
        matSalesAreas.add((new ErpMaterialSalesAreaInfo(new SalesAreaInfo(rs.getString("sales_org"),rs.getString("distribution_channel")), rs.getString("unavailability_status"), rs.getDate("unavailability_date"), rs.getString("unavailability_reason"),rs.getString("DAYPART_VALUE"),rs.getString("PICKING_PLANT_ID"))));
	}

	public Collection<ErpMaterialInfoModel> findMaterialsByCharacteristic(String classAndCharName) {
		int sep = classAndCharName.indexOf(":");
		if (sep == -1)
			throw new EJBException("Couldn't find \":\" separator character between class name and characteristic name");
		String className = classAndCharName.substring(0, sep);
		String charName = classAndCharName.substring(sep + 1, classAndCharName.length());
		return findMaterialsByCharacteristic(className, charName);
	}

	private final static String QUERY_MATERIALS_BY_CHAR =
		"select mtl.id, mtl.version, mtl.sap_id, mtl.description"
			+ " from erps.characteristic chr, erps.class cls, erps.material_class mcl, erps.material mtl"
			+ " where cls.id = mcl.class_id and mcl.mat_id = mtl.id and chr.class_id = cls.id"
			+ " and chr.name = ? and cls.sap_id = ?"
			+ " and cls.version=(select max(version) from erps.characteristic where name = ?)";

	public Collection<ErpMaterialInfoModel> findMaterialsByCharacteristic(String className, String charName) {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(QUERY_MATERIALS_BY_CHAR);
			ps.setString(1, charName);
			ps.setString(2, className);
			ps.setString(3, charName);
			ResultSet rs = ps.executeQuery();

			ArrayList<ErpMaterialInfoModel> materials = new ArrayList<ErpMaterialInfoModel>();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

			rs.close();
			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for Characteristic \"" + charName + "\" in Class \"" + className + "\"", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}

	private final static String QUERY_MATERIALS_BY_CLASS =
		"select mtl.id, mtl.version, mtl.sap_id, mtl.description"
			+ " from erps.class cls, erps.material_class mcl, erps.material mtl"
			+ " where cls.id = mcl.class_id and mcl.mat_id = mtl.id"
			+ " and cls.sap_id = ? and cls.version = (select max(version) from erps.class where sap_id = ?)";

	public Collection<ErpMaterialInfoModel> findMaterialsByClass(String className) {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(QUERY_MATERIALS_BY_CLASS);
			ps.setString(1, className);
			ps.setString(2, className);
			ResultSet rs = ps.executeQuery();

			ArrayList<ErpMaterialInfoModel> materials = new ArrayList<ErpMaterialInfoModel>();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

			rs.close();
			ps.close();

			return materials;
		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials in Class \"" + className + "\"", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}
	
    private final static String QUERY_SKUS_BY_SAP_ID =
        /*"select p.sku_code "+
        "from erps.product p, erps.materialproxy mpx, erps.material m "+
        "where p.id = mpx.product_id and mpx.mat_id = m.id "+
        "and p.version = (select max(version) from erps.product p2 where p2.sku_code = p.sku_code) "+
        "and m.sap_id like ? order by p.id";*/
    		
    		"select m.skucode "+
            "from erps.material m "+
            "where m.version = (select max(version) from erps.material m2 where m2.skucode = m.skucode) "+
            "and m.sap_id like ? order by m.id";

    public Collection<String> findSkusBySapId(String sapId) {
    	Connection conn = null;
    	PreparedStatement ps =null;
    	ResultSet rs =null;
    	List<String> skuCodes = new ArrayList<String>();
    	try {

    		conn = getConnection();

    		ps = conn.prepareStatement(QUERY_SKUS_BY_SAP_ID);
    		ps.setString(1, "%" + sapId);
    		rs = ps.executeQuery();

    		while (rs.next()) {

    			String skuCode = rs.getString(1);
    			skuCodes.add(skuCode);
    		}              
    		return skuCodes;
    	} catch (SQLException sqle) {
    		LOGGER.error("Error finding SKUs ", sqle);
    		throw new EJBException(sqle);
    	} finally {
    		
    		close(rs);
    		close(ps);
    		close(conn);
    	}
    }


	private final static String QUERY_PRODUCTS_BY_SAP_ID =
		/*"select p.sku_code, p.version, m.sap_id, p.unavailability_status,"
		+ " p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
        	+ " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
		+ " p.days_fresh, p.days_in_house, p.sustainability_rating, m.upc  "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id"
		+ " and p.version = (select max(version) from erps.product p2 where p2.sku_code = p.sku_code)"
		+ " and m.sap_id like ? order by m.description";*/
			
			"select m.skucode, m.version, m.sap_id, ms.unavailability_status,"
			+ " ms.unavailability_date, ms.unavailability_reason, m.description, p.atp_rule, p.rating, mp.price,"
	        	+ " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,mp.sales_org as mp_sales_org,mp.distribution_channel as mp_distribution_channel,"
			+ " p.daysfresh, p.days_in_house, p.sustainability_rating, m.upc,p.KOSHER_PRODUCTION,p.platter,p.blocked_days, p.plant_id,p.HIDE_OOS, ms.DAYPART_VALUE,M.ALCOHOLIC_CONTENT, ms.picking_plant_id "
			+ " from erps.plant_material p, erps.material m, erps.materialprice mp, erps.material_sales_area ms"
			+ " where p.mat_id = m.id and mp.mat_id = m.id and ms.mat_id = m.id"
			+ " and m.version = (select max(version) from erps.material m2 where m2.skucode = m.skucode)"
			+ " and m.sap_id like ? order by m.sap_id";

	public Collection<ErpProductInfoModel> findProductsBySapId(String sapId) {
		Connection conn = null;
		PreparedStatement ps =null;
		try {

			conn = getConnection();

			ps= conn.prepareStatement(QUERY_PRODUCTS_BY_SAP_ID);
			ps.setString(1, "%" + sapId);
			List<ErpProductInfoModel> products = fetchErpProductInfoModel(ps);
			return products;
		} catch (SQLException sqle) {
			LOGGER.error("Error finding SKUs ", sqle);
			throw new EJBException(sqle);
		} finally {
					close(ps);
                    close(conn);
		}
	}

	private final static String QUERY_PRODUCTS_BY_DESCRIPTION =
		/*"select p.sku_code, p.version, m.sap_id, p.unavailability_status,"
		+ " p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
	        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, " 
		+ " p.days_fresh, p.days_in_house, p.sustainability_rating, m.upc "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id"
		+ " and p.version = (select max(version) from erps.product p2 where p2.sku_code = p.sku_code)"
		+ " and m.description like ?"
		+ " order by m.description";*/
	
	"select m.skucode, m.version, m.sap_id, ms.unavailability_status,"
	+ " ms.unavailability_date, ms.unavailability_reason, m.description, p.atp_rule, p.rating, mp.price,"
        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, " 
	+ " p.daysfresh, p.days_in_house, p.sustainability_rating, m.upc, p.KOSHER_PRODUCTION,p.platter,p.blocked_days, ms.sales_org,ms.distribution_channel,mp.sales_org as mp_sales_org,mp.distribution_channel as mp_distribution_channel, p.plant_id,p.HIDE_OOS,ms.DAYPART_VALUE,M.ALCOHOLIC_CONTENT, ms.picking_plant_id  "
	+ " from erps.plant_material p, erps.material m, erps.materialprice mp, erps.material_sales_area ms"
	+ " where p.mat_id = m.id and mp.mat_id = m.id and m.id=ms.mat_id "
	+ " and m.version = (select max(version) from erps.material m2 where m2.skucode = m.skucode)"
	+ " and m.description like ?"
	+ " order by m.sap_id";

	public Collection<ErpProductInfoModel> findProductsByDescription(String description) {
		Connection conn = null;
		try {

			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_BY_DESCRIPTION);
			ps.setString(1, "%" + description.toUpperCase() + "%");
			List<ErpProductInfoModel> products = fetchErpProductInfoModel(ps);
			ps.close();

			return products;
		} catch (SQLException sqle) {
			LOGGER.error("Error finding SKUs ", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}

    /**
     * @param ps
     * @return
     * @throws SQLException
     */
    private List<ErpProductInfoModel> fetchErpProductInfoModel(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();

        List<ErpProductInfoModel> products = new ArrayList<ErpProductInfoModel>();
        String sapId = null;
        boolean newSapId= false;
        while (rs.next()) {
        	String curSapId = rs.getString("sap_id");
        	Set<String> matNos = null;
        	List<ErpMaterialPrice> matPrices = null;
        	Set<ErpPlantMaterialInfo> matPlants = null;
        	Set<ErpMaterialSalesAreaInfo> matSalesAreas = null;
        	if(null ==sapId || !sapId.equals(curSapId)){
        		newSapId = true;
        		sapId = curSapId;
            	matNos = new HashSet<String>(2);
            	matPrices = new ArrayList<ErpMaterialPrice>(5);
            	matPlants = new HashSet<ErpPlantMaterialInfo>(5);
            	matSalesAreas = new HashSet<ErpMaterialSalesAreaInfo>(5);
        	} else {
        		System.out.println("continue... "+rs.getString("sap_id"));
        		continue;
        	}

        	String skuCode = rs.getString("skucode");
        	int version = rs.getInt("version");
        	String descr = rs.getString("description");
        	matNos.add(rs.getString("sap_id"));

        	String days_fresh = rs.getString("daysfresh");
        	String days_in_house = rs.getString("days_in_house");
        	//String freshness = getFreshnessValue(days_fresh, days_in_house);
        	String sustainabilityRating=rs.getString("sustainability_rating");
        	String upc=rs.getString("upc");
        	//boolean isAlcohol=StringUtil.isEmpty(rs.getString("ALCOHOLIC_CONTENT"))?false:true;
			EnumAlcoholicContent alcoholType = StringUtil.isEmpty(rs.getString("ALCOHOLIC_CONTENT"))? EnumAlcoholicContent.NONE:EnumAlcoholicContent.getAlcoholicContent(rs.getString("ALCOHOLIC_CONTENT"));
        	
			matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble("price"), rs.getString("pricing_unit"), rs.getDouble("promo_price"), rs.getString("scale_unit"), 
        	        rs.getDouble("scale_quantity"), rs.getString("sap_zone_id"), rs.getString("mp_sales_org"),rs.getString("mp_distribution_channel")));
        	
        	matPlants.add(new ErpPlantMaterialInfo("X".equalsIgnoreCase(rs.getString("KOSHER_PRODUCTION")), "X".equalsIgnoreCase(rs.getString("PLATTER")), DayOfWeekSet.decode(rs.getString("BLOCKED_DAYS")), EnumATPRule.getEnum(rs.getInt("ATP_RULE")), rs.getString("RATING"), days_fresh, sustainabilityRating,  rs.getString("plant_id"),"X".equalsIgnoreCase(rs.getString("HIDE_OOS"))));
        	matSalesAreas.add((new ErpMaterialSalesAreaInfo(new SalesAreaInfo(rs.getString("mp_sales_org"),rs.getString("mp_distribution_channel")), rs.getString("unavailability_status"), rs.getDate("unavailability_date"), rs.getString("unavailability_reason"),rs.getString("DAYPART_VALUE"),rs.getString("PICKING_PLANT_ID"))));
        	
        	if(newSapId){
        		products.add(
        				new ErpProductInfoModel(
		                	skuCode,
		                	version,
		                	matNos.toArray(new String[0]),
		                	descr,
		                	matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),       
		                	upc,
		                	matPlants.toArray(new ErpProductInfoModel.ErpPlantMaterialInfo[0]),
		                	matSalesAreas.toArray(new ErpProductInfoModel.ErpMaterialSalesAreaInfo[0]),
		                	alcoholType
		                )
        		);
        		newSapId = false;
        	}
        }

        close(rs);
        return products;
    }

	private final static String QUERY_PRODUCTS_LIKE_SKU =
		/*"select p.sku_code, p.version, m.sap_id, p.unavailability_status, p.unavailability_date,"
		+ " p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
	        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
		+ " p.days_fresh, p.days_in_house, p.sustainability_rating, m.upc "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id and p.sku_code like ?"
		+ " and p.version = (select max(version) from erps.product where sku_code = p.sku_code)";*/
	
	"select m.skucode, m.version, m.sap_id, p.unavailability_status, p.unavailability_date,"
	+ " p.unavailability_reason, m.description, p.atp_rule, p.rating, mp.price,"
        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
	+ " p.daysfresh, p.days_in_house, p.sustainability_rating, m.upc,p.KOSHER_PRODUCTION,p.platter,p.blocked_days,p.HIDE_OOS,M.ALCOHOLIC_CONTENT "
	+ " from erps.plant_material p, erps.material m, erps.materialprice mp"
	+ " where p.mat_id = m.id and mp.mat_id = m.id and m.skucode like ?"
	+ " and m.version = (select max(version) from erps.material m1 where m1.skucode = m.skucode)";

	public Collection<ErpProductInfoModel> findProductsLikeSku(String skuCode) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_LIKE_SKU);
			ps.setString(1, skuCode.toUpperCase() + "%");
			return queryProductInfoModel(ps);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product for SKU " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}

	private final static String QUERY_PRODUCTS_BY_UPC =
		/*"select p.sku_code, p.version, m.sap_id, p.unavailability_status, p.unavailability_date, "
		+ " p.unavailability_reason,m.description, m.atp_rule,p.rating, mp.price,"
	        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, "
		+ " p.days_fresh, p.days_in_house,p.sustainability_rating, m.upc  "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id " +
				" and (m.upc = ? or '0' || M.UPC = ?)"
	    + " and m.version = (select max(version) from erps.material where upc = ? or '0' || upc = ?)";*/
			
			"select m.skucode, m.version, m.sap_id, ms.unavailability_status, ms.unavailability_date, "
			+ " ms.unavailability_reason,m.description, p.atp_rule,p.rating, mp.price,"
		        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, "
			+ " p.daysfresh, p.days_in_house,p.sustainability_rating, m.upc, p.KOSHER_PRODUCTION, p.platter,p.blocked_days, "
	        + " ms.sales_org,ms.distribution_channel,mp.sales_org as mp_sales_org,mp.distribution_channel as mp_distribution_channel , p.plant_id,p.HIDE_OOS,ms.DAYPART_VALUE,M.ALCOHOLIC_CONTENT, ms.picking_plant_id "
			+ " from erps.plant_material p, erps.material m, erps.materialprice mp, erps.material_sales_area ms "
			+ " where p.mat_id = m.id and mp.mat_id = m.id and ms.mat_id = m.id" +
					" and (m.upc = ? or '0' || M.UPC = ?)"
		    + " and m.version = (select max(version) from erps.material where upc = ? or '0' || upc = ?)";

	public Collection<ErpProductInfoModel> findProductsByUPC(String upc) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_BY_UPC);
			ps.setString(1, upc);
			ps.setString(2, upc);
			ps.setString(3, upc);
			ps.setString(4, upc);
			
			return queryProductInfoModel(ps);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product by UPC " + upc, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}
	
	private final static String QUERY_PRODUCTS_BY_CUSTOMERUPC =
		"select T.\"sku\" from CUST.CARTON_DETAIL cd, (select s.id as \"s\", OL.SKU_CODE as \"sku\", OL.ORDERLINE_NUMBER as \"ol\" " +
				"from cust.orderline ol, cust.salesaction sa, cust.sale s where  s.type='REG' and sa.id=OL.SALESACTION_ID " +
				"and s.customer_id=? and s.customer_id=sa.customer_id and  s.id=sa.sale_id  and S.CROMOD_DATE=SA.ACTION_DATE " +
				"and SA.ACTION_TYPE in ('CRO','MOD')  and sa.requested_Date>(sysdate-60)) T " +
				"where CD.ORDERLINE_NUMBER=T.\"ol\" and cd.sale_id=T.\"s\" and (CD.BARCODE=? or '0' || CD.BARCODE = ?) ";

	public Collection<String> findProductsByCustomerUPC(String erpCustomerPK, String upc) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();

			ps = conn.prepareStatement(QUERY_PRODUCTS_BY_CUSTOMERUPC);
			ps.setString(1, erpCustomerPK);
			ps.setString(2, upc);
			ps.setString(3, upc);
			
			rs = ps.executeQuery();

	        ArrayList<String> results = new ArrayList<String>();
	        while (rs.next()) {
	        	results.add(rs.getString(1));
	        }
			return results;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product by Customer:" + erpCustomerPK + ", UPC:"+ upc, sqle);
			throw new EJBException(sqle);
		} finally {
			close(rs);
		    close(ps);
            close(conn);
		}
	}

	private final static String QUERY_PRODUCTS_LIKE_UPC =
      		/*"select p.sku_code, p.version, m.sap_id, p.unavailability_status, p.unavailability_date,"
		+ " p.unavailability_reason, m.description, m.atp_rule,p.rating, mp.price,"
	        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
		+ " p.days_fresh, p.days_in_house,p.sustainability_rating, m.upc  "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id and m.upc like ?"
		+ " and p.version = (select max(version) from erps.product where sku_code = p.sku_code)";*/
	
	"select m.skucode, m.version, m.sap_id, p.unavailability_status, p.unavailability_date,"
	+ " p.unavailability_reason, m.description, p.atp_rule,p.rating, mp.price,"
        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
	+ " p.daysfresh, p.days_in_house,p.sustainability_rating, m.upc,p.HIDE_OOS,M.ALCOHOLIC_CONTENT  "
	+ " from erps.plant_material p, erps.material m, erps.materialprice mp"
	+ " where p.mat_id = m.id and mp.mat_id = m.id and m.upc like ?"
	+ " and m.version = (select max(version) from erps.material m1 where m1.skucode = m.skucode)";

	public Collection<ErpProductInfoModel> findProductsLikeUPC(String upc) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_LIKE_UPC);
			ps.setString(1, upc + "%");
			return queryProductInfoModel(ps);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product like UPC " + upc, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}

    /**
     * @param ps
     * @return
     * @throws SQLException
     */
    protected Collection<ErpProductInfoModel> queryProductInfoModel(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();

        ArrayList<ErpProductInfoModel> results = new ArrayList<ErpProductInfoModel>();
        while (rs.next()) {
        	List<String> matNos = new ArrayList<String>(5);
        	Set<ErpMaterialPrice> matPrices = new HashSet<ErpMaterialPrice>(5);
        	Set<ErpPlantMaterialInfo> matPlants = new HashSet<ErpPlantMaterialInfo>(5);
        	Set<ErpMaterialSalesAreaInfo> matSalesAreas = new HashSet<ErpMaterialSalesAreaInfo>(5);
        	
        	String sc = rs.getString(1);
        	int version = rs.getInt(2);

        	//double defPrice = rs.getDouble(3);
        	//String defPriceUnit = rs.getString(4);

//        	String unavStatus = rs.getString(4);
//        	java.util.Date unavDate = rs.getDate(5);
//        	String unavReason = rs.getString(6);
        	String descr = rs.getString(7);
//        	EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(8));
//        	String rating=rs.getString(9);
        	//double basePrice = rs.getDouble(12);
        	//String basePriceUnit = rs.getString(13);

        	String days_fresh = rs.getString(16);
        	String days_in_house = rs.getString(17);
        	//String freshness = getFreshnessValue(days_fresh, days_in_house);
        	String sustainabilityRating= rs.getString(18);
        	String upc=rs.getString(19);

        	matNos.add(rs.getString(3));
        	//boolean isAlcohol=StringUtil.isEmpty(rs.getString("ALCOHOLIC_CONTENT"))?false:true;
			EnumAlcoholicContent alcoholType = StringUtil.isEmpty(rs.getString("ALCOHOLIC_CONTENT"))? EnumAlcoholicContent.NONE:EnumAlcoholicContent.getAlcoholicContent(rs.getString("ALCOHOLIC_CONTENT"));
        	matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13), rs.getDouble(14), rs.getString(15), null, null));        	
        	matPlants.add(new ErpPlantMaterialInfo("X".equalsIgnoreCase(rs.getString("KOSHER_PRODUCTION")), "X".equalsIgnoreCase(rs.getString("PLATTER")), DayOfWeekSet.decode(rs.getString("BLOCKED_DAYS")), EnumATPRule.getEnum(rs.getInt("ATP_RULE")), rs.getString("RATING"), days_fresh, sustainabilityRating, rs.getString("plant_id"),"X".equalsIgnoreCase(rs.getString("HIDE_OOS"))));
        	matSalesAreas.add((new ErpMaterialSalesAreaInfo(new SalesAreaInfo(rs.getString("sales_org"),rs.getString("distribution_channel")), rs.getString("unavailability_status"), rs.getDate("unavailability_date"), rs.getString("unavailability_reason"),rs.getString("DAYPART_VALUE"),rs.getString("PICKING_PLANT_ID"))));

        	results.add(new ErpProductInfoModel(
        			sc,version,
        		        	matNos.toArray(new String[0]),
        		        	descr,
        		        	matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),       
        		        	upc, matPlants.toArray(new ErpProductInfoModel.ErpPlantMaterialInfo[0]),matSalesAreas.toArray(new ErpProductInfoModel.ErpMaterialSalesAreaInfo[0]),alcoholType)      );
        }

        close(rs);
        close(ps);

        return results;
    }

	/** @return null if there's no inventory for material */
	public ErpInventoryModel getInventoryInfo(String materialNo) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			conn = getConnection();

			ps = conn.prepareStatement(
					"SELECT DATE_MODIFIED, START_DATE, QUANTITY, PLANT_ID FROM ERPS.INVENTORY i, ERPS.INVENTORY_ENTRY ie WHERE i.MATERIAL_SAP_ID=? AND i.MATERIAL_SAP_ID=ie.MATERIAL_SAP_ID ORDER BY START_DATE");
			ps.setString(1, materialNo);
			rs = ps.executeQuery();

			if (!rs.next()) {
				// no such inventory
				return null;
			}

			java.util.Date dateModified = rs.getDate("DATE_MODIFIED");

			List<ErpInventoryEntryModel> entryList = new ArrayList<ErpInventoryEntryModel>();
			do {
				entryList.add(new ErpInventoryEntryModel(rs.getDate("START_DATE"), rs.getDouble("QUANTITY"), rs.getString("PLANT_ID")));
			} while (rs.next());

			return new ErpInventoryModel(materialNo, dateModified, entryList);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to load inventory for material " + materialNo, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}
	
	private static final String LOAD_INVENTORY = "SELECT i.MATERIAL_SAP_ID, DATE_MODIFIED, START_DATE, QUANTITY, PLANT_ID " 
		+ "FROM ERPS.INVENTORY i, ERPS.INVENTORY_ENTRY ie " 
		+ "WHERE i.MATERIAL_SAP_ID=ie.MATERIAL_SAP_ID "
		+ "AND DATE_MODIFIED > ? "
		+ "ORDER BY i.material_sap_id, date_modified ";
	
	public Map<String, ErpInventoryModel> loadInventoryInfo(Date lastModified) {
		Connection conn = null;
		try{
			conn = this.getConnection();
			Map<String, ErpInventoryModel> m = new HashMap<String, ErpInventoryModel>();
			
			PreparedStatement ps = conn.prepareStatement(LOAD_INVENTORY);
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
			ResultSet rs = ps.executeQuery();
			String lastMatId = "";
			List<ErpInventoryEntryModel> entryList = new ArrayList<ErpInventoryEntryModel>();
			Timestamp t = null;
			while(rs.next()){
				t = rs.getTimestamp("DATE_MODIFIED");
				String matId = rs.getString("MATERIAL_SAP_ID");
				if("".equals(lastMatId)){
					lastMatId = matId;
				}
				if(!lastMatId.equals(matId)){
					m.put(lastMatId, new ErpInventoryModel(lastMatId, t, entryList));
					entryList = new ArrayList<ErpInventoryEntryModel>();
					lastMatId = matId;
				}
				entryList.add(new ErpInventoryEntryModel(rs.getDate("START_DATE"), rs.getDouble("QUANTITY"), rs.getString("PLANT_ID")));
			}
			rs.close();
			ps.close();
			//add the last one to the map
			if(!"".equals(lastMatId)){
				m.put(lastMatId, new ErpInventoryModel(lastMatId, t, entryList));
			}
			return m;
		}catch(SQLException e) {
			LOGGER.error("Unable to load INVENTORY INFO", e);
			throw new EJBException(e);
		}finally{
                    close(conn);
		}
	}

	private final static String QUERY_NEW_SKUCODES =
		"select prd.sku_code from erps.history hst, erps.product prd"
			+ " where hst.version=prd.version"
			+ " and hst.version = (select min(version) from erps.product where sku_code=prd.sku_code and NVL(unavailability_status,' ') <> 'TEST')"
			+ " and prd.unavailability_status is null"
			+ " and hst.date_created >= (SYSDATE-?)";

	public Collection<String> findNewSkuCodes(int days) {
		return this.querySkus(QUERY_NEW_SKUCODES, new int[] { days });
	}
	
	private final static String QUERY_SKU_NEWNESS = 
		"select prd.sku_code, trunc(sysdate - hst.date_created) as diff from erps.history hst, erps.product prd"
			+ " where hst.version=prd.version"
			+ " and hst.version = (select min(version) from erps.product where sku_code=prd.sku_code and NVL(unavailability_status,' ') <> 'TEST')"
			+ "and prd.unavailability_status is null";
	
	public Map<String, Integer> getSkusOldness() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(QUERY_SKU_NEWNESS);
			rs = ps.executeQuery();

			Map<String, Integer> skus = new HashMap<String, Integer>();
			while (rs.next()) {
				skus.put(rs.getString(1), new Integer(rs.getInt(2)));
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find skus", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
		
	}

	private final static String QUERY_REINTRODUCED_SKUCODES =
		"select prd.sku_code"
			+ " from erps.history hst, erps.product prd,"
			+ "	(select prd3.sku_code, prd3.version"
			+ "	from erps.product prd3"
			+ "	where prd3.version = (select max(version) from erps.product where sku_code=prd3.sku_code and unavailability_status is not null)"
			+ "	) unav"
			+ " where hst.version=prd.version"
			+ " and hst.version=(select min(version) from erps.product where sku_code=prd.sku_code and unavailability_status is null and version > unav.version)"
			+ " and prd.sku_code=unav.sku_code"
			+ " and hst.date_created >= (SYSDATE-?)"
			+ " minus "
			+ QUERY_NEW_SKUCODES;

	public Collection<String> findReintroducedSkuCodes(int days) {
		return this.querySkus(QUERY_REINTRODUCED_SKUCODES, new int[] { days, days });
	}

	/**
	 * Get SKUs that
	 *  - are available based on status (in their most recent version)
	 *  - have zero inventory on any day
	 *  - have any entries after tomorrow, or the last entry is zero (therefore end is unlimited) 
	 */	
	private final static String QUERY_OUTOFSTOCK_SKUCODES =
		"select distinct p.sku_code"
			+ " from erps.inventory_entry ie, erps.material m, erps.materialproxy mp, erps.product p"
			+ " where ie.quantity=0"
			+ " and m.sap_id=ie.material_sap_id"
			+ " and mp.mat_id=m.id"
			+ " and mp.product_id=p.id"
			+ " and p.unavailability_status is null"
			+ " and m.version=(select max(version) from erps.material em where em.sap_id=m.sap_id)"
			+ " and ("
			+ "  exists (select material_sap_id from erps.inventory_entry"
			+ "   where material_sap_id=ie.material_sap_id and start_date>trunc(sysdate)+1)"
			+ "  or (ie.start_date=(select max(start_date) from erps.inventory_entry"
			+ "   where material_sap_id=ie.material_sap_id)) )";

	public Collection<String> findOutOfStockSkuCodes() {
		return this.querySkus(QUERY_OUTOFSTOCK_SKUCODES, new int[] {});
	}

	private Collection<String> querySkus(String query, int[] days) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(query);
			for (int i = 0; i < days.length; i++) {
				ps.setInt(i + 1, days[i]);
			}
			rs = ps.executeQuery();

			List<String> skus = new ArrayList<String>();
			while (rs.next()) {
				skus.add(rs.getString(1));
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find skus", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}
	
	public Collection<String> findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes) {
		
		
		StringBuffer statement=new StringBuffer("SELECT p1.sku_code FROM erps.PRODUCT p1,erps.material m, erps.materialprice mp, erps.materialproxy mpr ") 
		.append(" WHERE p1.VERSION=(SELECT MAX(VERSION) FROM erps.PRODUCT ") 
         .append(" WHERE sku_code=p1.sku_code) ") 
         .append(" and MPR.PRODUCT_ID=p1.id ")
         .append(" and MPR.MAT_ID=m.id ")
         .append(" and M.ID=MP.MAT_ID ")
         .append(" and MP.PROMO_PRICE>0 ")                  
         .append(" and ((MP.PRICE-MP.PROMO_PRICE)/mp.price)*100 between ? and ? ");
		
		/*
		StringBuffer statement=new StringBuffer("SELECT p1.sku_code FROM erps.PRODUCT p1 ")
		        .append(" WHERE p1.VERSION=(SELECT MAX(VERSION) FROM erps.PRODUCT ")
		        .append(" WHERE sku_code=p1.sku_code) AND p1.base_price<>0  AND ")
		        .append("((p1.BASE_PRICE-p1.DEFAULT_PRICE)/p1.BASE_PRICE)*100 BETWEEN ? AND ?" );
		        */
		
		Connection conn = null;
		try {
			
			if(hasValue(skuPrefixes)) {
				statement.append(" AND (");
				for(int i=0;i<skuPrefixes.size();i++) {
					
					statement.append(" ( sku_code LIKE ?) ");
					if(i<(skuPrefixes.size()-1)) {
						statement.append(" OR ");
					}
				}
				statement.append(" )");
			}
			//System.out.println("Statement is : "+statement.toString());
			conn = this.getConnection();
	
			PreparedStatement ps = conn.prepareStatement(statement.toString());
			//ps.setDouble(1, lowerLimit);
			ps.setBigDecimal(1, new java.math.BigDecimal(lowerLimit));
			//ps.setDouble(2, upperLimit);
			ps.setBigDecimal(2, new java.math.BigDecimal(upperLimit));
			
			if(hasValue(skuPrefixes)) {
				for(int i=0;i<skuPrefixes.size();i++) {
					ps.setString(i+3, skuPrefixes.get(i).toString());
				}
			}
			ResultSet rs = ps.executeQuery();
	
			List<String> lst = new ArrayList<String>();
			while (rs.next()) {
				lst.add(rs.getString(1));
			}
	
			rs.close();
			ps.close();
	
			return lst;
	
		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}
	
	
	public List<String> findPeakProduceSKUsByDepartment(List skuPrefixes){
		
		StringBuffer statement=new StringBuffer("SELECT distinct p1.sku_code, rating "+ 
		"FROM erps.PRODUCT p1 "+
		"where p1.unavailability_date > sysdate+1 and "+
		"p1.unavailability_status is null " + 
		"and p1.VERSION=(SELECT MAX(VERSION) FROM erps.PRODUCT "+
		"WHERE sku_code=p1.sku_code "+
		") and rating is not null and rating like 'P%' " );
				Connection conn = null;
				try {
					
					if(hasValue(skuPrefixes)) {
						statement.append(" AND (");
						for(int i=0;i<skuPrefixes.size();i++) {
							
							statement.append(" ( p1.sku_code LIKE UPPER(?)) ");
							if(i<(skuPrefixes.size()-1)) {
								statement.append(" OR ");
							}
						}
						statement.append(" )");
					}
					//System.out.println("Statement is : "+statement.toString());
					conn = this.getConnection();
				
					PreparedStatement ps = conn.prepareStatement(statement.toString());
					
					
					if(hasValue(skuPrefixes)) {
						for(int i=0;i<skuPrefixes.size();i++) {
							//System.out.println(skuPrefixes.get(i).toString()+"%");
							ps.setString(i+1, skuPrefixes.get(i).toString()+"%");
						}
					}
					
					ResultSet rs = ps.executeQuery();
				
					List<String> lst = new ArrayList<String>();
					while (rs.next()) {
						lst.add(rs.getString(1));
						//System.out.println(rs.getString(1));
					}
				
					rs.close();
					ps.close();
				
					return lst;
				
				} catch (SQLException sqle) {
					throw new EJBException(sqle);
				} finally {
				    close(conn);
				}		
	}
	
	
	private boolean hasValue(List skuPrefixes) {
		
		if(skuPrefixes!=null && skuPrefixes.size()!=0) {
			return true;
		} else {
			return false;
		}
	}

	/*private String getFreshnessValue(String days_fresh, String days_in_house) {
		if(null != days_fresh && days_fresh.trim().length() > 0 && StringUtils.isNumeric(days_fresh)) {
			int df = Integer.parseInt(days_fresh);
			if(null != days_in_house && days_in_house.trim().length() > 0 && StringUtils.isNumeric(days_in_house)) {
				int dih = Integer.parseInt(days_in_house);
				int difference = df - dih;
				// make sure we don't pass a negative number for days fresh in case of data error.
				if(difference < 0) {
					difference = 0;
				}
				return String.valueOf(difference);
			} else {
				// in the case where the value was set for days_fresh but no value
				// or an invalid value was set for days_in_house, return null
				return null;
			}
		}
		return null;
	}*/

	private static final String QUERY_NEW_SKUS =
		"SELECT * FROM erps.new_products_v1";

	public Map<String, Map<String,Date>> getNewSkus(/*String salesOrg, String distributionChannel*/) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(QUERY_NEW_SKUS);
			/*ps.setString(1, salesOrg);
			ps.setString(2, distributionChannel);
			*/
			rs = ps.executeQuery();

			Map<String, Map<String,Date>> skus = new TreeMap<String, Map<String,Date>>();
			String sku="";
			Map<String,Date> value=null;
			while (rs.next()) {
				sku=rs.getString(1);
				if(skus.containsKey(sku)) {
					value=skus.get(sku);
					value.put(new StringBuilder(5).append(rs.getString(2)).append(rs.getString(3)).toString(), rs.getTimestamp(4));
				} else {
					value=new HashMap<String,Date>();
					value.put(new StringBuilder(5).append(rs.getString(2)).append(rs.getString(3)).toString(), rs.getTimestamp(4));
				}
				skus.put(sku, value);
			}
			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find new skus dates", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}
	
	private static final String QUERY_BACK_IN_STOCK_SKUS = 
		"SELECT * FROM erps.back_in_stock_products_v1";

	public Map<String, Map<String,Date>> getBackInStockSkus() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(QUERY_BACK_IN_STOCK_SKUS);
			rs = ps.executeQuery();
			Map<String, Map<String,Date>> skus = new TreeMap<String, Map<String,Date>>();
			String sku="";
			Map<String,Date> value=null;
			while (rs.next()) {
				sku=rs.getString(1);
				if(skus.containsKey(sku)) {
					value=skus.get(sku);
					value.put(new StringBuilder(5).append(rs.getString(2)).append(rs.getString(3)).toString(), rs.getTimestamp(4));
				} else {
					value=new HashMap<String,Date>();
					value.put(new StringBuilder(5).append(rs.getString(2)).append(rs.getString(3)).toString(), rs.getTimestamp(4));
				}
				skus.put(sku, value);
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find back in stock SKU dates", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}
	
	private static DateFormat DATE_FORMAT1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");

	private static DateFormat DATE_FORMAT2 = new SimpleDateFormat("MM/dd/yyyy");

	private static DateFormat DATE_FORMAT3 = new SimpleDateFormat("MM/dd/yy");
	
	public Map<String, Map<String,Date>> getOverriddenNewSkus() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT skucode,SALES_ORG,DISTRIBUTION_CHANNEL, atr_value FROM erps.ATTRIBUTES_SALES_AREA WHERE atr_name = 'new_prod_date'");
			rs = ps.executeQuery();

			return extractOverridenValues(rs);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find overridden new skus dates", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}

    /**
     * @param rs
     * @return
     * @throws SQLException
     */
    protected Map<String, Map<String,Date>> extractOverridenValues(ResultSet rs) throws SQLException {
    	Map<String, Map<String,Date>> skus = new TreeMap<String, Map<String,Date>>();
    	Map<String,Date> value=null;
    	String sku="";
    	String salesOrg="";
    	String distributionChannel="";
        while (rs.next()) {
        	 sku = rs.getString(1);
        	 salesOrg=rs.getString(2);
        	 distributionChannel=rs.getString(3);
        	String ds = rs.getString(4);
        	//To fix NumberFormatException
        	if(null == ds || "".equals(ds.trim())){
        		continue;
        	}
        	Date date;
        	try {
				try {
					date = DATE_FORMAT1.parse(ds);
				} catch (ParseException e) {
					try {
						date = DATE_FORMAT3.parse(ds);
					} catch (ParseException e1) {
						try {
							date = DATE_FORMAT2.parse(ds);
						} catch (ParseException e2) {
							// skip this bad date
							continue;
						}
					}
				}
			} catch (Exception e) {
				continue;
			}
    		if(skus.containsKey(sku)) {
				value=skus.get(sku);
				value.put(new StringBuilder(5).append(salesOrg).append(distributionChannel).toString(), date);
			} else {
				value=new HashMap<String,Date>();
				value.put(new StringBuilder(5).append(salesOrg).append(distributionChannel).toString(), date);
			}
//        	}
        	skus.put(sku, value);
        }

        return skus;
    }

	public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT skucode,SALES_ORG,DISTRIBUTION_CHANNEL, atr_value FROM erps.ATTRIBUTES_SALES_AREA WHERE atr_name = 'back_in_stock'");
			rs = ps.executeQuery();

			return extractOverridenValues(rs);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find overridden back in stock SKU dates", sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}
	
	public Map<String,String> getOverriddenNewness(String sku) {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1=null;
		ResultSet rs = null;
		Map<String,String> result=new HashMap<String,String>(2);
		try {
			conn = getConnection();
			ps=conn.prepareStatement("SELECT sales_org, distribution_channel, atr_value FROM erps.ATTRIBUTES_SALES_AREA where skucode=? and atr_name='new_prod_date'");
			ps.setString(1,sku);
			rs=ps.executeQuery();
			while(rs.next()) {
				result.put(rs.getString(1)+"-"+rs.getString(2), rs.getString(3));
			}
		}catch (SQLException sqle) {
			LOGGER.error("Unable to setOverriddenBackInStock for SKU: " + sku, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps1);
                    close(ps);
                    close(conn);
		}
		return result;
	}
	
	public Map<String,String> getOverriddenBackInStock(String sku) {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1=null;
		ResultSet rs = null;
		Map<String,String> result=new HashMap<String,String>(2);
		try {
			conn = getConnection();
			ps=conn.prepareStatement("SELECT sales_org, distribution_channel, atr_value FROM erps.ATTRIBUTES_SALES_AREA where skucode=? and atr_name='back_in_stock'");
			ps.setString(1,sku);
			rs=ps.executeQuery();
			while(rs.next()) {
				result.put(rs.getString(1)+"-"+rs.getString(2), rs.getString(3));
			}
		}catch (SQLException sqle) {
			LOGGER.error("Unable to setOverriddenBackInStock for SKU: " + sku, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps1);
                    close(ps);
                    close(conn);
		}
		return result;
	}
	public void setOverriddenBackInStock(String sku, Map<String,String> salesAreaOverrides) {
		//get timestamp to replace sysdate
		Timestamp ts = new Timestamp(new Date().getTime());
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1=null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps1=conn.prepareStatement("DELETE FROM erps.ATTRIBUTES_SALES_AREA where skucode=? and atr_name='back_in_stock'");
			ps1.setString(1,sku);
			ps1.execute();
			
			ps = conn
					.prepareStatement("Insert into ERPS.ATTRIBUTES_SALES_AREA   (id, skucode, sales_org, distribution_channel, atr_type, atr_name, atr_value, date_modified) Values (?,?,?,?,?,?,?,?)");
			for (Map.Entry<String,String> entry : salesAreaOverrides.entrySet()) {
				
			    String[] val=entry.getKey().split("-");
			    
				ps.setString(1, SequenceGenerator.getNextId(conn, "ERPS","ATTRIBUTE_MSA_SEQ"));
				ps.setString(2,sku);
				ps.setString(3, val[0]);
				ps.setString(4, val[1]);
				ps.setString(5, "S");
				ps.setString(6, "back_in_stock");
				
				ps.setString(7, entry.getValue());
				ps.setTimestamp(8, ts);
				ps.addBatch();
			}
			if(ps!=null) {
				ps.executeBatch();
				ps.close();
			}
			

		} catch (SQLException sqle) {
			LOGGER.error("Unable to setOverriddenBackInStock for SKU: " + sku, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps1);
                    close(ps);
                    close(conn);
		}
	}
	
	public void setOverriddenNewness(String sku, Map<String,String> salesAreaOverrides) {
		//get timestamp to replace sysdate
		Timestamp ts = new Timestamp(new Date().getTime());
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1=null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			ps1=conn.prepareStatement("DELETE FROM erps.ATTRIBUTES_SALES_AREA where skucode=? and atr_name='new_prod_date'");
			ps1.setString(1,sku);
			ps1.execute();
			
			ps = conn
					.prepareStatement("Insert into ERPS.ATTRIBUTES_SALES_AREA   (id, skucode, sales_org, distribution_channel, atr_type, atr_name, atr_value, date_modified) Values (?,?,?,?,?,?,?,?)");
			for (Map.Entry<String,String> entry : salesAreaOverrides.entrySet()) {
				
			    String[] val=entry.getKey().split("-");
			    
				ps.setString(1, SequenceGenerator.getNextId(conn, "ERPS","ATTRIBUTE_MSA_SEQ"));
				ps.setString(2,sku);
				ps.setString(3, val[0]);
				ps.setString(4, val[1]);
				ps.setString(5, "S");
				ps.setString(6, "new_prod_date");
				ps.setString(7, entry.getValue());
				ps.setTimestamp(8, ts);
				ps.addBatch();
			}
			if(ps!=null) {
				ps.executeBatch();
				ps.close();
			}
			

		} catch (SQLException sqle) {
			LOGGER.error("Unable to setOverriddenBackInStock for SKU: " + sku, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps1);
                    close(ps);
                    close(conn);
		}
	}
	private static final String QUERY_SKU_AVAILABILITY_HISTORY = 
			"SELECT p.sku_code, h.version, p.unavailability_status, h.date_created FROM erps.product p" +
			" INNER JOIN erps.history h ON p.version     = h.version WHERE p.sku_code = ? ORDER BY version";
	
	public List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(QUERY_SKU_AVAILABILITY_HISTORY);
			ps.setString(1, skuCode);
			rs = ps.executeQuery();

			List<SkuAvailabilityHistory> history = new ArrayList<SkuAvailabilityHistory>();
			while (rs.next()) {
				history.add(new SkuAvailabilityHistory(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4)));
			}

			return history;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find availability history for SKU: " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
	}
	
	public void refreshNewAndBackViews() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareCall("CALL erps.refresh_new_and_back()");
			ps.execute();
		} catch (SQLException sqle) {
			LOGGER.error("Unable to update materialized views (NEW_PRODUCTS and BACK_IN_STOCK_PRODUCTS)", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}
	
	private static final String QUERY_AVAILABILITY_DATES = 
		"SELECT DATE_AVAILABLE FROM ERPS.AVAILABILITY_DELIVERY_DATES WHERE MATERIAL_SAP_ID = ? AND DATE_AVAILABLE >= (SYSDATE - ?)";
	
	public List<Date> getAvailableDeliveryDates(String materialNumber, int daysInPast){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(QUERY_AVAILABILITY_DATES);
			ps.setString(1, materialNumber);
			ps.setInt(2, daysInPast);
			rs = ps.executeQuery();

			List<Date> restrictedDates = new ArrayList<Date>();
			while (rs.next()) {
				restrictedDates.add(rs.getDate("DATE_AVAILABLE"));
			}

			return restrictedDates;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find availability restriction for materialNumber : " + materialNumber, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(rs);
                    close(ps);
                    close(conn);
		}
		
	}
	
	private final static String QUERY_PRODUCTS_BY_LAST_MODIFIED =
			/*"select p.sku_code, p.version, m.sap_id, p.unavailability_status, p.unavailability_date, "
			+ " p.unavailability_reason,m.description, m.atp_rule,p.rating, mp.price,"
		        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, "
			+ " p.days_fresh, p.days_in_house,p.sustainability_rating, m.upc  "
			+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
			+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id " +
					" and (m.upc = ? or '0' || M.UPC = ?)"
		    + " and m.version = (select max(version) from erps.material where upc = ? or '0' || upc = ?)";*/
				
				"select m.skucode, m.version, m.sap_id, ms.unavailability_status, ms.unavailability_date, "
				+ " ms.unavailability_reason,m.description, p.atp_rule,p.rating, mp.price,"
			        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, "
				+ " p.daysfresh, p.days_in_house,p.sustainability_rating, m.upc, p.KOSHER_PRODUCTION, p.platter,p.blocked_days, "
		        + " ms.sales_org,ms.distribution_channel,mp.sales_org as mp_sales_org,mp.distribution_channel as mp_distribution_channel , p.plant_id,p.HIDE_OOS,ms.DAYPART_VALUE,M.ALCOHOLIC_CONTENT, ms.picking_plant_id "
				+ " from erps.plant_material p, erps.material m, erps.materialprice mp, erps.material_sales_area ms,erps.history h "
				+ " where p.mat_id = m.id and mp.mat_id = m.id and ms.mat_id = m.id"						
			    + " and m.version = (select max(m1.version) from erps.material m1 where m1.version=h.version and m1.sap_id=m.sap_id)"
				+ " h.date_created > ?";
	
	public Collection<ErpProductInfoModel> findProductsByLastModified(Date lastModifiedTime) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_BY_LAST_MODIFIED);
			ps.setTimestamp(1, new Timestamp(lastModifiedTime.getTime()));		
			
			return queryProductInfoModel(ps);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product by last modified: " + lastModifiedTime, sqle);
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}
}
