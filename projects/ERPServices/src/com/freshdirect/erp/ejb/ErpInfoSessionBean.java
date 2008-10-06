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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Category;

import com.freshdirect.erp.EnumATPRule;
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
		"select p.version, p.default_price, p.default_unit, m.sap_id, p.unavailability_status, p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule,p.rating,p.base_price,p.base_pricing_unit"
			+ " from erps.product p, erps.materialproxy mpx, erps.material m where p.id=mpx.product_id and mpx.mat_id=m.id and p.sku_code = ?"
			+ " and p.version = (select max(version) from erps.product where sku_code = ?)";

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

			List matNos = new ArrayList(5);

			int version = rs.getInt(1);
			double defPrice = rs.getDouble(2);
			String defPriceUnit = rs.getString(3);

			String unavStatus = rs.getString(5);
			java.util.Date unavDate = rs.getDate(6);
			String unavReason = rs.getString(7);
			
			String descr = rs.getString(8);
			EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(9));
			String rating=rs.getString(10);
			double basePrice = rs.getDouble(11);
			String basePriceUnit = rs.getString(12);
			
			matNos.add(rs.getString(4));
			while (rs.next()) {
				matNos.add(rs.getString(4));
			}

			return new ErpProductInfoModel(
				skuCode,
				version,
				defPrice,
				defPriceUnit,
				(String[]) matNos.toArray(new String[0]),
				atpRule,
				unavStatus,
				unavDate,
				unavReason,
				descr,
				rating,
				basePrice,
				basePriceUnit);

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
		"select p.default_price, p.default_unit, m.sap_id, p.unavailability_status, p.unavailability_date, "
			+ "p.unavailability_reason, m.description, m.atp_rule,p.rating,p.base_price,p.base_pricing_unit from erps.product p, erps.materialproxy mpx, erps.material m "
			+ "where p.id=mpx.product_id and mpx.mat_id=m.id and p.sku_code = ? and p.version = ?";

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

				double defPrice = rs.getDouble(1);
				String defPriceUnit = rs.getString(2);

				String unavStatus = rs.getString(4);
				java.util.Date unavDate = rs.getDate(5);
				String unavReason = rs.getString(6);
				
				String descr = rs.getString(7);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(8));
				String rating=rs.getString(9);
				double basePrice = rs.getDouble(10);
				String basePriceUnit = rs.getString(11);				
				matNos.add(rs.getString(3));
				while (rs.next()) {
					matNos.add(rs.getString(3));
				}

				return new ErpProductInfoModel(
					skuCode,
					version,
					defPrice,
					defPriceUnit,
					(String[]) matNos.toArray(new String[0]),
					atpRule,
					unavStatus,
					unavDate,
					unavReason,
					descr,
					rating,
					basePrice,
					basePriceUnit);
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
				ps.setString(2, skuCodes[i]);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					List matNos = new ArrayList(2);

					int version = rs.getInt(1);
					double defPrice = rs.getDouble(2);
					String defPriceUnit = rs.getString(3);

					String unavStatus = rs.getString(5);
					java.util.Date unavDate = rs.getDate(6);
					String unavReason = rs.getString(7);
					String descr = rs.getString(8);
					EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(9));
					String rating =rs.getString(10);
					double basePrice = rs.getDouble(11);
					String basePriceUnit = rs.getString(12);					
					matNos.add(rs.getString(4));
					while (rs.next()) {
						matNos.add(rs.getString(4));
					}

					products.add(
						new ErpProductInfoModel(
							skuCodes[i],
							version,
							defPrice,
							defPriceUnit,
							(String[]) matNos.toArray(new String[0]),
							atpRule,
							unavStatus,
							unavDate,
							unavReason,
							descr,
							rating,
							basePrice,
							basePriceUnit));
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
		"select p.sku_code, p.version, p.default_price, p.default_unit, m.sap_id, p.unavailability_status, p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule"
			+ " from erps.product p, erps.materialproxy mpx, erps.material m where p.id=mpx.product_id and mpx.mat_id=m.id"
			+ " and p.version = (select max(version)from erps.product p2 where p2.sku_code = p.sku_code)"
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

				String skuCode = rs.getString(1);
				int version = rs.getInt(2);
				double defPrice = rs.getDouble(3);
				String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(6);
				java.util.Date unavDate = rs.getDate(7);
				String unavReason = rs.getString(8);
				String descr = rs.getString(9);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(10));
				String rating=rs.getString(10);
				double basePrice = rs.getDouble(11);
				String basePriceUnit = rs.getString(12);
				matNos.add(rs.getString(5));

				products.add(
					new ErpProductInfoModel(
						skuCode,
						version,
						defPrice,
						defPriceUnit,
						(String[]) matNos.toArray(new String[0]),
						atpRule,
						unavStatus,
						unavDate,
						unavReason,
						descr,
						rating,
						basePrice,
						basePriceUnit));
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
		"select p.sku_code, p.version, p.default_price, p.default_unit, m.sap_id, p.unavailability_status, p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule, p.rating,p.base_price,p.base_pricing_unit"
			+ " from erps.product p, erps.materialproxy mpx, erps.material m where p.id=mpx.product_id and mpx.mat_id=m.id"
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

				String skuCode = rs.getString(1);
				int version = rs.getInt(2);
				double defPrice = rs.getDouble(3);
				String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(6);
				java.util.Date unavDate = rs.getDate(7);
				String unavReason = rs.getString(8);
				String descr = rs.getString(9);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(10));
				String rating=rs.getString(11);
				double basePrice = rs.getDouble(12);
				String basePriceUnit = rs.getString(13);
				matNos.add(rs.getString(5));

				products.add(
					new ErpProductInfoModel(
						skuCode,
						version,
						defPrice,
						defPriceUnit,
						(String[]) matNos.toArray(new String[0]),
						atpRule,
						unavStatus,
						unavDate,
						unavReason,
						descr,
						rating,
						basePrice,
						basePriceUnit));
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
		"select p.sku_code, p.version, p.default_price, p.default_unit, m.sap_id, p.unavailability_status, p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule,p.rating,p.base_price,p.base_pricing_unit"
			+ " from erps.product p, erps.materialproxy mpx, erps.material m"
			+ " where p.id=mpx.product_id and mpx.mat_id=m.id and p.sku_code like ?"
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

				String sc = rs.getString(1);
				int version = rs.getInt(2);
				double defPrice = rs.getDouble(3);
				String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(6);
				java.util.Date unavDate = rs.getDate(7);
				String unavReason = rs.getString(8);
				String descr = rs.getString(9);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(10));
				String rating=rs.getString(11);
				double basePrice = rs.getDouble(12);
				String basePriceUnit = rs.getString(13);
				matNos.add(rs.getString(5));

				results.add(
					new ErpProductInfoModel(
						sc,
						version,
						defPrice,
						defPriceUnit,
						(String[]) matNos.toArray(new String[0]),
						atpRule,
						unavStatus,
						unavDate,
						unavReason,
						descr,
						rating,
						basePrice,
						basePriceUnit));
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
		"select p.sku_code, p.version, p.default_price, p.default_unit, m.sap_id, p.unavailability_status, p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule,p.rating,p.base_price,p.base_pricing_unit"
			+ " from erps.product p, erps.materialproxy mpx, erps.material m"
			+ " where p.id=mpx.product_id and mpx.mat_id=m.id and m.upc = ?"
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

				String sc = rs.getString(1);
				int version = rs.getInt(2);
				double defPrice = rs.getDouble(3);
				String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(6);
				java.util.Date unavDate = rs.getDate(7);
				String unavReason = rs.getString(8);
				String descr = rs.getString(9);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(10));
				String rating=rs.getString(11);
				double basePrice = rs.getDouble(12);
				String basePriceUnit = rs.getString(13);
				matNos.add(rs.getString(5));

				results.add(
					new ErpProductInfoModel(
						sc,
						version,
						defPrice,
						defPriceUnit,
						(String[]) matNos.toArray(new String[0]),
						atpRule,
						unavStatus,
						unavDate,
						unavReason,
						descr,
						rating,
						basePrice,
						basePriceUnit));
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
		"select p.sku_code, p.version, p.default_price, p.default_unit, m.sap_id, p.unavailability_status, p.unavailability_date, p.unavailability_reason, m.description, m.atp_rule,p.rating,p.base_price,p.base_pricing_unit"
			+ " from erps.product p, erps.materialproxy mpx, erps.material m"
			+ " where p.id=mpx.product_id and mpx.mat_id=m.id and m.upc like ?"
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

				String sc = rs.getString(1);
				int version = rs.getInt(2);
				double defPrice = rs.getDouble(3);
				String defPriceUnit = rs.getString(4);

				String unavStatus = rs.getString(6);
				java.util.Date unavDate = rs.getDate(7);
				String unavReason = rs.getString(8);
				String descr = rs.getString(9);
				EnumATPRule atpRule = EnumATPRule.getEnum(rs.getInt(10));
				String rating=rs.getString(11);
				double basePrice = rs.getDouble(12);
				String basePriceUnit = rs.getString(13);
				matNos.add(rs.getString(5));

				results.add(
					new ErpProductInfoModel(
						sc,
						version,
						defPrice,
						defPriceUnit,
						(String[]) matNos.toArray(new String[0]),
						atpRule,
						unavStatus,
						unavDate,
						unavReason,
						descr,
						rating,
						basePrice,
						basePriceUnit));
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

}
