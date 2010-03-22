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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpMaterialInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.core.VersionedPrimaryKey;
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

	public Collection findMaterialsByBatch(int batchNumber) {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps =
				conn.prepareStatement(
					"select id, version, sap_id, description from erps.material where version = ? order by description");
			ps.setInt(1, batchNumber);
			ResultSet rs = ps.executeQuery();

			ArrayList materials = new ArrayList();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

			rs.close();
			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for batch " + batchNumber, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
	}

	private final static String QUERY_MATERIALS_BY_SAPID =
		"select max(to_number(id)), max(version), sap_id, description"
			+ " from (select id, version, sap_id, description from erps.material where sap_id like ?)"
			+ " group by sap_id, description order by description";

	public Collection findMaterialsBySapId(String sapId) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_MATERIALS_BY_SAPID);
			ps.setString(1, "%" + sapId);
			ResultSet rs = ps.executeQuery();

			ArrayList materials = new ArrayList();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

			rs.close();
			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for SapId " + sapId, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
	}

	private final static String QUERY_MATERIALS_BY_SKU =
		"select max(to_number(id)), max(version), sap_id, description"
			+ " from (select m.ID as id, m.VERSION as version, m.sap_id as sap_id, m.description as description"
			+ " from erps.material m, erps.materialproxy mpx, erps.product p where p.sku_code = ? and p.id = mpx.product_id and mpx.mat_id=m.id)"
			+ " group by sap_id, description order by description";

	public Collection findMaterialsBySku(String skuCode) {
		Connection conn = null;

		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_MATERIALS_BY_SKU);
			ps.setString(1, skuCode);
			ResultSet rs = ps.executeQuery();

			ArrayList materials = new ArrayList();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

			rs.close();
			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for SKU " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
	}

	private final static String QUERY_MATERIALS_BY_DESCRIPTION =
		"select max(to_number(id)), max(version), sap_id, description"
			+ " from (select id, version, sap_id, description from erps.material where description like ?)"
			+ " group by sap_id, description order by description";

	public Collection findMaterialsByDescription(String description) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_MATERIALS_BY_DESCRIPTION);
			ps.setString(1, "%" + description.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			ArrayList materials = new ArrayList();
			while (rs.next()) {
				VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2));
				ErpMaterialInfoModel matlInfo = new ErpMaterialInfoModel(vpk, rs.getString(3), rs.getString(4));
				materials.add(matlInfo);
			}

			rs.close();
			ps.close();

			return materials;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find materials for description \"" + description + "\"", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
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
*/
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
		+ " where rank = 1";
		
	public ErpProductInfoModel findProductBySku(String skuCode) throws ObjectNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();

			ps = conn.prepareStatement(QUERY_PRODUCTS_BY_SKU);
			ps.setString(1, skuCode);
			//ps.setString(2, skuCode);

			rs = ps.executeQuery();

			if (!rs.next()) {
				throw new ObjectNotFoundException("SKU " + skuCode + " not found");
			}

			List matNos = new ArrayList(5);
			List matPrices = new ArrayList(5);

			int version = rs.getInt(1);
			//double defPrice = rs.getDouble(2);
			//String defPriceUnit = rs.getString(3);
			String sapId = rs.getString(2);
			String unavStatus = rs.getString(3);
			java.util.Date unavDate = rs.getDate(4);
			String unavReason = rs.getString(5);
			
			String descr = rs.getString(6);
			EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(7));
			String rating=rs.getString(8);

			String days_fresh = rs.getString(15);
			String days_in_house = rs.getString(16);
			String freshness = getFreshnessValue(days_fresh, days_in_house);

			//double basePrice = rs.getDouble(11);
			//String basePriceUnit = rs.getString(12);
			
			matNos.add(rs.getString(2));
			matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(9), rs.getString(10), rs.getDouble(11), rs.getString(12), rs.getDouble(13), rs.getString(14)));
			while (rs.next()) {
				matNos.add(rs.getString(2));
				matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(9), rs.getString(10), rs.getDouble(11), rs.getString(12), rs.getDouble(13), rs.getString(14)));
			}
			

			return new ErpProductInfoModel(
				skuCode,
				version,
				(String[]) matNos.toArray(new String[0]),
				(ErpProductInfoModel.ErpMaterialPrice[]) matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),
				atpRule,
				unavStatus,
				unavDate,
				unavReason,
				descr,
				rating, 
				freshness);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product for SKU " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	private static final String skuVersionQuery =
		        "select m.sap_id, p.unavailability_status, p.unavailability_date,"
			+ " p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
		        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"         
   			+ " p.days_fresh, p.days_in_house "
			+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
			+ " where p.id=mpx.product_id and mpx.mat_id=m.id and mp.mat_id = m.id and p.sku_code = ? and p.version = ?"; 
	
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
				List matNos = new ArrayList(5);
				List matPrices = new ArrayList(5);

				//double defPrice = rs.getDouble(1);
				//String defPriceUnit = rs.getString(2);

				String unavStatus = rs.getString(2);
				java.util.Date unavDate = rs.getDate(3);
				String unavReason = rs.getString(4);
				
				String descr = rs.getString(5);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(6));
				String rating=rs.getString(7);
				//double basePrice = rs.getDouble(10);
				//String basePriceUnit = rs.getString(11);

				String days_fresh = rs.getString(14);
				String days_in_house = rs.getString(15);
				String freshness = getFreshnessValue(days_fresh, days_in_house);

				matNos.add(rs.getString(1));
				matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(8), rs.getString(9), rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13)));

				while (rs.next()) {
					matNos.add(rs.getString(1));
					matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(8), rs.getString(9), rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13)));
				}

				return new ErpProductInfoModel(
						skuCode,
						version,
						(String[]) matNos.toArray(new String[0]),
						(ErpProductInfoModel.ErpMaterialPrice[]) matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),
						atpRule,
						unavStatus,
						unavDate,
						unavReason,
						descr,
						rating,
						freshness);
			}
			throw new ObjectNotFoundException("SKU " + skuCode + ", version " + version + " not found");

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product for SKU " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	public Collection findProductsBySku(String[] skuCodes) {
		Connection conn = null;
		try {

			List products = new ArrayList(skuCodes.length);
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_BY_SKU);
			for (int i = 0; i < skuCodes.length; i++) {
				ps.setString(1, skuCodes[i]);
//				ps.setString(2, skuCodes[i]);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					List matNos = new ArrayList(2);
					List matPrices = new ArrayList(5);

					int version = rs.getInt(1);

					//double defPrice = rs.getDouble(2);
					//String defPriceUnit = rs.getString(3);

					String unavStatus = rs.getString(3);
					java.util.Date unavDate = rs.getDate(4);
					String unavReason = rs.getString(5);
					String descr = rs.getString(6);
					EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(7));
					String rating =rs.getString(8);
					//double basePrice = rs.getDouble(11);
					//String basePriceUnit = rs.getString(12);					

					String days_fresh = rs.getString(15);
					String days_in_house = rs.getString(16);
					String freshness = getFreshnessValue(days_fresh, days_in_house);

					matNos.add(rs.getString(2));
					matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(9), rs.getString(10), rs.getDouble(11), rs.getString(12), rs.getDouble(13), rs.getString(14)));

					while (rs.next()) {
						matNos.add(rs.getString(2));
						matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(9), rs.getString(10), rs.getDouble(11), rs.getString(12), rs.getDouble(13), rs.getString(14)));
					}

					products.add(new ErpProductInfoModel(
						skuCodes[i],
						version,
						(String[]) matNos.toArray(new String[0]),
						(ErpProductInfoModel.ErpMaterialPrice[]) matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),
						atpRule,
						unavStatus,
						unavDate,
						unavReason,
						descr,
						rating,
						freshness));
				}
				rs.close();
			}
			ps.close();

			return products;
		} catch (SQLException sqle) {
			LOGGER.error("Error finding SKUs ", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	public Collection findMaterialsByCharacteristic(String classAndCharName) {
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

	public Collection findMaterialsByCharacteristic(String className, String charName) {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(QUERY_MATERIALS_BY_CHAR);
			ps.setString(1, charName);
			ps.setString(2, className);
			ps.setString(3, charName);
			ResultSet rs = ps.executeQuery();

			ArrayList materials = new ArrayList();
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
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
	}

	private final static String QUERY_MATERIALS_BY_CLASS =
		"select mtl.id, mtl.version, mtl.sap_id, mtl.description"
			+ " from erps.class cls, erps.material_class mcl, erps.material mtl"
			+ " where cls.id = mcl.class_id and mcl.mat_id = mtl.id"
			+ " and cls.sap_id = ? and cls.version = (select max(version) from erps.class where sap_id = ?)";

	public Collection findMaterialsByClass(String className) {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(QUERY_MATERIALS_BY_CLASS);
			ps.setString(1, className);
			ps.setString(2, className);
			ResultSet rs = ps.executeQuery();

			ArrayList materials = new ArrayList();
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
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
	}

	private final static String QUERY_PRODUCTS_BY_SAP_ID =
		"select p.sku_code, p.version, m.sap_id, p.unavailability_status,"
		+ " p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
        	+ " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
		+ " p.days_fresh, p.days_in_house "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id"
		+ " and p.version = (select max(version) from erps.product p2 where p2.sku_code = p.sku_code)"
		+ " and m.sap_id like ? order by m.description";

	public Collection findProductsBySapId(String sapId) {
		Connection conn = null;
		try {

			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_BY_SAP_ID);
			ps.setString(1, "%" + sapId);
			ResultSet rs = ps.executeQuery();

			List products = new ArrayList();
			while (rs.next()) {
				List matNos = new ArrayList(2);
				List matPrices = new ArrayList(5);

				String skuCode = rs.getString(1);
				int version = rs.getInt(2);

				//double defPrice = rs.getDouble(3);
				//String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(4);
				java.util.Date unavDate = rs.getDate(5);
				String unavReason = rs.getString(6);
				String descr = rs.getString(7);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(8));
				String rating=rs.getString(9);
				//double basePrice = rs.getDouble(12);
				//String basePriceUnit = rs.getString(13);

				String days_fresh = rs.getString(16);
				String days_in_house = rs.getString(17);
				String freshness = getFreshnessValue(days_fresh, days_in_house);

				matNos.add(rs.getString(3));
				matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13), rs.getDouble(14), rs.getString(15)));

				products.add(
					new ErpProductInfoModel(
						skuCode,
						version,
						(String[]) matNos.toArray(new String[0]),
						(ErpProductInfoModel.ErpMaterialPrice[]) matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),
						atpRule,
						unavStatus,
						unavDate,
						unavReason,
						descr,
						rating,freshness));
			}

			rs.close();
			ps.close();

			return products;
		} catch (SQLException sqle) {
			LOGGER.error("Error finding SKUs ", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	private final static String QUERY_PRODUCTS_BY_DESCRIPTION =
		"select p.sku_code, p.version, m.sap_id, p.unavailability_status,"
		+ " p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
	        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, " 
		+ " p.days_fresh, p.days_in_house "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id"
		+ " and p.version = (select max(version) from erps.product p2 where p2.sku_code = p.sku_code)"
		+ " and m.description like ?"
		+ " order by m.description";

	public Collection findProductsByDescription(String description) {
		Connection conn = null;
		try {

			List products = new ArrayList();
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_BY_DESCRIPTION);
			ps.setString(1, "%" + description.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				List matNos = new ArrayList(2);
				List matPrices = new ArrayList(5);

				String skuCode = rs.getString(1);
				int version = rs.getInt(2);

				//double defPrice = rs.getDouble(3);
				//String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(4);
				java.util.Date unavDate = rs.getDate(5);
				String unavReason = rs.getString(6);
				String descr = rs.getString(7);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(8));
				String rating=rs.getString(3);
				//double basePrice = rs.getDouble(12);
				//String basePriceUnit = rs.getString(13);
				matNos.add(rs.getString(5));

				String days_fresh = rs.getString(16);
				String days_in_house = rs.getString(17);
				String freshness = getFreshnessValue(days_fresh, days_in_house);

				matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13), rs.getDouble(14), rs.getString(15)));

				products.add(
						 new ErpProductInfoModel(
								skuCode,
								version,
								(String[]) matNos.toArray(new String[0]),
								(ErpProductInfoModel.ErpMaterialPrice[]) matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),
								atpRule,
								unavStatus,
								unavDate,
								unavReason,
								descr,
								rating,
								freshness));
			}

			rs.close();
			ps.close();

			return products;
		} catch (SQLException sqle) {
			LOGGER.error("Error finding SKUs ", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	private final static String QUERY_PRODUCTS_LIKE_SKU =
		"select p.sku_code, p.version, m.sap_id, p.unavailability_status, p.unavailability_date,"
		+ " p.unavailability_reason, m.description, m.atp_rule, p.rating, mp.price,"
	        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
		+ " p.days_fresh, p.days_in_house "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id and p.sku_code like ?"
		+ " and p.version = (select max(version) from erps.product where sku_code = p.sku_code)";

	public Collection findProductsLikeSku(String skuCode) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_LIKE_SKU);
			ps.setString(1, skuCode.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();

			ArrayList results = new ArrayList();
			while (rs.next()) {
				List matNos = new ArrayList(5);
				List matPrices = new ArrayList(5);

				String sc = rs.getString(1);
				int version = rs.getInt(2);

				//double defPrice = rs.getDouble(3);
				//String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(4);
				java.util.Date unavDate = rs.getDate(5);
				String unavReason = rs.getString(6);
				String descr = rs.getString(7);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(8));
				String rating=rs.getString(9);
				//double basePrice = rs.getDouble(12);
				//String basePriceUnit = rs.getString(13);

				String days_fresh = rs.getString(16);
				String days_in_house = rs.getString(17);
				String freshness = getFreshnessValue(days_fresh, days_in_house);

				matNos.add(rs.getString(3));
				matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13), rs.getDouble(14), rs.getString(15)));

				results.add(
						 new ErpProductInfoModel(
								sc,
								version,
								(String[]) matNos.toArray(new String[0]),
								(ErpProductInfoModel.ErpMaterialPrice[]) matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),
								atpRule,
								unavStatus,
								unavDate,
								unavReason,
								descr,
								rating, freshness));
			}

			rs.close();
			ps.close();

			return results;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product for SKU " + skuCode, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	private final static String QUERY_PRODUCTS_BY_UPC =
		"select p.sku_code, p.version, m.sap_id, p.unavailability_status, p.unavailability_date, "
		+ " p.unavailability_reason,m.description, m.atp_rule,p.rating, mp.price,"
	        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id, "
		+ " p.days_fresh, p.days_in_house "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id and m.upc = ?"
		+ " and p.version = (select max(version) from erps.product where sku_code = p.sku_code)";

	public Collection findProductsByUPC(String upc) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_BY_UPC);
			ps.setString(1, upc);
			ResultSet rs = ps.executeQuery();

			ArrayList results = new ArrayList();
			while (rs.next()) {
				List matNos = new ArrayList(5);
				List matPrices = new ArrayList(5);

				String sc = rs.getString(1);
				int version = rs.getInt(2);

				//double defPrice = rs.getDouble(3);
				//String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(4);
				java.util.Date unavDate = rs.getDate(5);
				String unavReason = rs.getString(6);
				String descr = rs.getString(7);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(8));
				String rating=rs.getString(9);
				//double basePrice = rs.getDouble(12);
				//String basePriceUnit = rs.getString(13);

				String days_fresh = rs.getString(16);
				String days_in_house = rs.getString(17);
				String freshness = getFreshnessValue(days_fresh, days_in_house);

				matNos.add(rs.getString(3));
				matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13), rs.getDouble(14), rs.getString(15)));

				results.add(
						 new ErpProductInfoModel(
								sc,
								version,
								(String[]) matNos.toArray(new String[0]),
								(ErpProductInfoModel.ErpMaterialPrice[]) matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),
								atpRule,
								unavStatus,
								unavDate,
								unavReason,
								descr,
								rating,
								freshness));
			}

			rs.close();
			ps.close();

			return results;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product by UPC " + upc, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	private final static String QUERY_PRODUCTS_LIKE_UPC =
      		"select p.sku_code, p.version, m.sap_id, p.unavailability_status, p.unavailability_date,"
		+ " p.unavailability_reason, m.description, m.atp_rule,p.rating, mp.price,"
	        + " mp.pricing_unit, mp.promo_price, mp.scale_unit, mp.scale_quantity, mp.sap_zone_id,"
		+ " p.days_fresh, p.days_in_house "
		+ " from erps.product p, erps.materialproxy mpx, erps.material m, erps.materialprice mp"
		+ " where p.id = mpx.product_id and mpx.mat_id = m.id and mp.mat_id = m.id and m.upc like ?"
		+ " and p.version = (select max(version) from erps.product where sku_code = p.sku_code)";

	public Collection findProductsLikeUPC(String upc) {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(QUERY_PRODUCTS_LIKE_UPC);
			ps.setString(1, upc + "%");
			ResultSet rs = ps.executeQuery();

			ArrayList results = new ArrayList();
			while (rs.next()) {
				List matNos = new ArrayList(5);
				List matPrices = new ArrayList(5);

				String sc = rs.getString(1);
				int version = rs.getInt(2);

				//double defPrice = rs.getDouble(3);
				//String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(4);
				java.util.Date unavDate = rs.getDate(5);
				String unavReason = rs.getString(6);
				String descr = rs.getString(7);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(8));
				String rating=rs.getString(9);
				//double basePrice = rs.getDouble(12);
				//String basePriceUnit = rs.getString(13);

				String days_fresh = rs.getString(16);
				String days_in_house = rs.getString(17);
				String freshness = getFreshnessValue(days_fresh, days_in_house);

				matNos.add(rs.getString(3));
				matPrices.add(new ErpProductInfoModel.ErpMaterialPrice(rs.getDouble(10), rs.getString(11), rs.getDouble(12), rs.getString(13), rs.getDouble(14), rs.getString(15)));

				results.add(
						 new ErpProductInfoModel(
								sc,
								version,
								(String[]) matNos.toArray(new String[0]),
								(ErpProductInfoModel.ErpMaterialPrice[]) matPrices.toArray(new ErpProductInfoModel.ErpMaterialPrice[0]),
								atpRule,
								unavStatus,
								unavDate,
								unavReason,
								descr,
								rating,
								freshness));
			}

			rs.close();
			ps.close();

			return results;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find product like UPC " + upc, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	/** @return null if there's no inventory for material */
	public ErpInventoryModel getInventoryInfo(String materialNo) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			conn = getConnection();

			ps = conn.prepareStatement(
					"SELECT DATE_MODIFIED, START_DATE, QUANTITY FROM ERPS.INVENTORY i, ERPS.INVENTORY_ENTRY ie WHERE i.MATERIAL_SAP_ID=? AND i.MATERIAL_SAP_ID=ie.MATERIAL_SAP_ID ORDER BY START_DATE");
			ps.setString(1, materialNo);
			rs = ps.executeQuery();

			if (!rs.next()) {
				// no such inventory
				return null;
			}

			java.util.Date dateModified = rs.getDate("DATE_MODIFIED");

			List entryList = new ArrayList();
			do {
				entryList.add(new ErpInventoryEntryModel(rs.getDate("START_DATE"), rs.getDouble("QUANTITY")));
			} while (rs.next());

			return new ErpInventoryModel(materialNo, dateModified, entryList);

		} catch (SQLException sqle) {
			LOGGER.error("Unable to load inventory for material " + materialNo, sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}
	
	private static final String LOAD_INVENTORY = "SELECT i.MATERIAL_SAP_ID, DATE_MODIFIED, START_DATE, QUANTITY " 
		+ "FROM ERPS.INVENTORY i, ERPS.INVENTORY_ENTRY ie " 
		+ "WHERE i.MATERIAL_SAP_ID=ie.MATERIAL_SAP_ID "
		+ "AND DATE_MODIFIED > ? "
		+ "ORDER BY i.material_sap_id, date_modified ";
	
	public Map loadInventoryInfo(Date lastModified) {
		Connection conn = null;
		try{
			conn = this.getConnection();
			Map m = new HashMap();
			
			PreparedStatement ps = conn.prepareStatement(LOAD_INVENTORY);
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
			ResultSet rs = ps.executeQuery();
			String lastMatId = "";
			List entryList = new ArrayList();
			Timestamp t = null;
			while(rs.next()){
				t = rs.getTimestamp("DATE_MODIFIED");
				String matId = rs.getString("MATERIAL_SAP_ID");
				if("".equals(lastMatId)){
					lastMatId = matId;
				}
				if(!lastMatId.equals(matId)){
					m.put(lastMatId, new ErpInventoryModel(lastMatId, t, entryList));
					entryList = new ArrayList();
					lastMatId = matId;
				}
				entryList.add(new ErpInventoryEntryModel(rs.getDate("START_DATE"), rs.getDouble("QUANTITY")));
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
			try{
				if(conn != null){
					conn.close();
				}
			}catch (SQLException e) {
				LOGGER.warn("Unable to close db resources", e);
			}
		}
	}

	private final static String QUERY_NEW_SKUCODES =
		"select prd.sku_code from erps.history hst, erps.product prd"
			+ " where hst.version=prd.version"
			+ " and hst.version = (select min(version) from erps.product where sku_code=prd.sku_code and NVL(unavailability_status,' ') <> 'TEST')"
			+ " and prd.unavailability_status is null"
			+ " and hst.date_created >= (SYSDATE-?)";

	public Collection findNewSkuCodes(int days) {
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

			Map skus = new HashMap();
			while (rs.next()) {
				skus.put(rs.getString(1), new Integer(rs.getInt(2)));
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find skus", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
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

	public Collection findReintroducedSkuCodes(int days) {
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

	public Collection findOutOfStockSkuCodes() {
		return this.querySkus(QUERY_OUTOFSTOCK_SKUCODES, new int[] {});
	}

	private Collection querySkus(String query, int[] days) {
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

			List skus = new ArrayList();
			while (rs.next()) {
				skus.add(rs.getString(1));
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find skus", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}
	
	public Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes) {
		
		
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
			ps.setDouble(1, lowerLimit);
			ps.setDouble(2, upperLimit);
			
			if(hasValue(skuPrefixes)) {
				for(int i=0;i<skuPrefixes.size();i++) {
					ps.setString(i+3, skuPrefixes.get(i).toString());
				}
			}
			ResultSet rs = ps.executeQuery();
	
			List lst = new ArrayList();
			while (rs.next()) {
				lst.add(rs.getString(1));
			}
	
			rs.close();
			ps.close();
	
			return lst;
	
		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}
	}
	
	
	public List findPeakProduceSKUsByDepartment(List skuPrefixes){
		
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
					System.out.println("Statement is : "+statement.toString());
					conn = this.getConnection();
				
					PreparedStatement ps = conn.prepareStatement(statement.toString());
					
					
					if(hasValue(skuPrefixes)) {
						for(int i=0;i<skuPrefixes.size();i++) {
							System.out.println(skuPrefixes.get(i).toString()+"%");
							ps.setString(i+1, skuPrefixes.get(i).toString()+"%");
						}
					}
					
					ResultSet rs = ps.executeQuery();
				
					List lst = new ArrayList();
					while (rs.next()) {
						lst.add(rs.getString(1));
						System.out.println(rs.getString(1));
					}
				
					rs.close();
					ps.close();
				
					return lst;
				
				} catch (SQLException sqle) {
					throw new EJBException(sqle);
				} finally {
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException sqle2) {
						LOGGER.warn("Error closing connection", sqle2);
					}
				}		
	}
	
	
	private boolean hasValue(List skuPrefixes) {
		
		if(skuPrefixes!=null && skuPrefixes.size()!=0) {
			return true;
		} else {
			return false;
		}
	}

	private String getFreshnessValue(String days_fresh, String days_in_house) {
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
	}

	private static final String QUERY_NEW_SKUS =
		"SELECT * FROM erps.new_products";

	public Map<String, Date> getNewSkus() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(QUERY_NEW_SKUS);
			rs = ps.executeQuery();

			Map<String, Date> skus = new TreeMap<String, Date>();
			Date now = new Date();
			Date first = new Date(now.getTime() - 30l * 24l * 3600000l);
			while (rs.next()) {
				Date date = rs.getTimestamp(2);
				if (date.before(now) && date.after(first))
					skus.put(rs.getString(1), date);
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find new skus dates", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}
	
	private static final String QUERY_BACK_IN_STOCK_SKUS = 
		"SELECT * FROM erps.back_in_stock_products";

	public Map<String, Date> getBackInStockSkus() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(QUERY_BACK_IN_STOCK_SKUS);
			rs = ps.executeQuery();

			Map<String, Date> skus = new TreeMap<String, Date>();
			Date now = new Date();
			Date first = new Date(now.getTime() - 30l * 24l * 3600000l);
			while (rs.next()) {
				Date date = rs.getTimestamp(2);
				if (date.before(now) && date.after(first))
					skus.put(rs.getString(1), date);
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find back in stock SKU dates", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}
	
	private static DateFormat DATE_FORMAT1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");

	private static DateFormat DATE_FORMAT2 = new SimpleDateFormat("MM/dd/yyyy");

	private static DateFormat DATE_FORMAT3 = new SimpleDateFormat("MM/dd/yy");
	
	public Map<String, Date> getOverriddenNewSkus() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT root_id, atr_value FROM erps.attributes WHERE atr_name = 'new_prod_date'");
			rs = ps.executeQuery();

			Map<String, Date> skus = new TreeMap<String, Date>();
			Date now = new Date();
			Date first = new Date(now.getTime() - 120l * 24l * 3600000l);
			while (rs.next()) {
				String sku = rs.getString(1);
				String ds = rs.getString(2);
				Date date;
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
				if (date.before(now) && date.after(first))
					skus.put(sku, date);
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find overridden new skus dates", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
		}
	}

	public Map<String, Date> getOverriddenBackInStockSkus() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT root_id, atr_value FROM erps.attributes WHERE atr_name = 'back_in_stock'");
			rs = ps.executeQuery();

			Map<String, Date> skus = new TreeMap<String, Date>();
			Date now = new Date();
			Date first = new Date(now.getTime() - 30l * 24l * 3600000l);
			while (rs.next()) {
				String sku = rs.getString(1);
				String ds = rs.getString(2);
				Date date;
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
				if (date.before(now) && date.after(first))
					skus.put(sku, date);
			}

			return skus;

		} catch (SQLException sqle) {
			LOGGER.error("Unable to find overridden back in stock SKU dates", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
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
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.warn("Unable to close db resources", sqle);
			}
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
}
