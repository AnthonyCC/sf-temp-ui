/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.collection.PersistentReferences;
import com.freshdirect.framework.collection.RemoteObjectList;
import com.freshdirect.framework.core.EntityBeanRemoteI;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PayloadI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.VersionedEntityBeanSupport;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * ErpMaterial entity bean implementation.
 *
 * @version	$Revision$
 * @author	 $Author$
 *
 * @ejbHome <{ErpMaterialHome}>
 * @ejbRemote <{ErpMaterialEB}>
 * @ejbPrimaryKey <{VersionedPrimaryKey}>
 * @stereotype fd-entity
 */
public class ErpMaterialEntityBean extends VersionedEntityBeanSupport {

	private final static Category LOGGER = LoggerFactory.getInstance(ErpMaterialEntityBean.class);
	private final static ServiceLocator LOCATOR = new ServiceLocator();

	/** SAP unique material number */
	private String sapId;

	/** Base unit of measure */
	private String baseUnit;

	/** Material description */
	private String description;

	/** Characteristic name for quantity, null if none */
	private String quantityCharacteristic;

	/** Characteristic name for sales unit, null if none */
	private String salesUnitCharacteristic;

	/** the ATPRule to use when checking for availablility of this material */
	private EnumATPRule atpRule;

	/** the lead time in days to stock this product **/
	private int leadTime;
    
	private boolean taxable;
    
	private boolean kosherProduction;
	
	private boolean platter;
	
	private DayOfWeekSet blockedDays;

	/**
	 * Collection of dependent ErpMaterialPrice persistent beans.
	 *
	 * @link aggregationByValue
	 * @directed
	 * @associates <{ErpMaterialPricePersistentBean}>
	 */
	private MaterialPriceList prices;

	/**
	 * Collection of dependent ErpSalesUnit persistent beans.
	 *
	 * @link aggregationByValue
	 * @directed
	 * @associates <{ErpSalesUnitPersistentBean}>
	 */
	private SalesUnitList salesUnits;

	/**
	 * Aggregation of the related classes.
	 *
	 * @link aggregation
	 * @directed
	 * @associates <{ErpClassEntityBean}>
	 **/
	private ClassList classes;

	/** Holds value of property UPC. */
	private String upc;
    
    /** Holds value of property alcoholicContent. */
	private EnumAlcoholicContent alcoholicContent;

	/**
	 * Collection of dependent display ErpSalesUnit persistent beans .
	 *
	 * @link aggregationByValue
	 * @directed
	 * @associates <{ErpSalesUnitPersistentBean}>
	 */
	private DisplaySalesUnitList displaySalesUnits;
	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.erp.ejb.ErpMaterialHome";
	}

	public void initialize() {
		this.sapId = null;
		this.baseUnit = null;
		this.description = null;
		this.atpRule = null;
		this.leadTime = -1;
		this.upc = null;
		this.quantityCharacteristic = null;
		this.salesUnitCharacteristic = null;
        this.alcoholicContent = null;
        this.blockedDays = null;
		this.prices = new MaterialPriceList();
		this.salesUnits = new SalesUnitList();
		this.classes = new ClassList();
		this.displaySalesUnits = new DisplaySalesUnitList();
		// find home for objects in remote object list
		this.classes.setEJBHome(getClassHome());
	}

	private static ErpClassHome getClassHome() {
		try {
			return (ErpClassHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpClass");
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpMaterialModel object.
	 */
	public ModelI getModel() {
		// build the material model
		ErpMaterialModel model =
			new ErpMaterialModel(
				this.sapId,
				this.baseUnit,
				this.description,
				this.atpRule,
				this.leadTime,
				this.upc,
				this.quantityCharacteristic,
				this.salesUnitCharacteristic,
                this.alcoholicContent,
                this.taxable,
                this.kosherProduction,
                this.platter,
                this.blockedDays,
				this.prices.getModelList(),
				this.salesUnits.getModelList(),
				this.classes.getModelList(),
				this.displaySalesUnits.getModelList());
		super.decorateModel(model);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		// copy properties from model
		ErpMaterialModel m = (ErpMaterialModel) model;
		this.sapId = m.getSapId();
		this.baseUnit = m.getBaseUnit() != null ? m.getBaseUnit().intern() : null;
		this.description = m.getDescription();
		this.atpRule = m.getATPRule();
		this.leadTime = m.getLeadTime();
		this.upc = m.getUPC();
		this.quantityCharacteristic = m.getQuantityCharacteristic();
		this.salesUnitCharacteristic = m.getSalesUnitCharacteristic();
        this.alcoholicContent = m.getAlcoholicContent();
        this.taxable = m.isTaxable();
		this.kosherProduction = m.isKosherProduction();
		this.platter = m.isPlatter();
		this.blockedDays = m.getBlockedDays();
		this.setPricesFromModel(m.getPrices());
		this.setSalesUnitsFromModel(m.getSalesUnits());
		this.setClassesFromModel(m.getClasses());
	}

	public VersionedPrimaryKey ejbFindBySapId(String sapId) throws FinderException {

		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, version, sap_id from erps.material where sap_id=? and version=(select max(version) from erps.material where sap_id=?)");
			ps.setString(1, sapId);
			ps.setString(2, sapId);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find a Material with an SAP ID = " + sapId);
			}

			VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2), null);

			rs.close();
			ps.close();

			return vpk;

		} catch (SQLException sqle) {
			throw new EJBException("Unable to find a Material by its SAP ID : " + sqle.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				throw new EJBException(sqle);
			}
		}

	}

	/**
	 * Load the row with specified PK into a payload object.
	 *
	 * @param conn database connection
	 * @param pk primary key
	 *
	 * @return payload object, null if row was not found
	 *
	 * @throws SQLException if a database error occured
	 */
	public PayloadI loadRowPayload(Connection conn, PrimaryKey pk) throws SQLException {
		VersionedPrimaryKey vpk = (VersionedPrimaryKey) pk;
		PreparedStatement ps = conn.prepareStatement("SELECT SAP_ID, BASE_UNIT, DESCRIPTION, ATP_RULE, LEAD_TIME, CHAR_QUANTITY, CHAR_SALESUNIT, UPC, ALCOHOLIC_CONTENT, TAXABLE, KOSHER_PRODUCTION, PLATTER, BLOCKED_DAYS FROM ERPS.MATERIAL WHERE ID = ? AND VERSION = ?");
		ps.setString(1, vpk.getId());
		ps.setInt(2, vpk.getVersion());
		
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			return null;
		}

		ErpMaterialPayload p = new ErpMaterialPayload();
		p.sapId = rs.getString(1);
		p.baseUnit = rs.getString(2);
		p.description = rs.getString(3);
		p.atpRule = EnumATPRule.getEnum(rs.getInt(4));
		p.leadTime = rs.getInt(5);
		p.quantityCharacteristic = rs.getString(6);
		p.salesUnitCharacteristic = rs.getString(7);
		p.UPC = rs.getString(8);
        p.alcoholicContent = EnumAlcoholicContent.getAlcoholicContent(rs.getString(9));
		p.taxable = "X".equalsIgnoreCase(rs.getString(10));
        p.kosherProduction = "X".equalsIgnoreCase(rs.getString(11));
        p.platter = "X".equalsIgnoreCase(rs.getString(12));
		p.blockedDays = DayOfWeekSet.decode(rs.getString(13));
        
		rs.close();
		ps.close();

		// load children
		p.prices.setParentPK(pk);
		p.prices.load(conn);
		p.salesUnits.setParentPK(pk);
		p.salesUnits.load(conn);
		p.classes.setParentPK(pk);
		p.classes.setEJBHome(getClassHome());
		p.classes.load(conn);
		p.displaySalesUnitList.setParentPK(pk);
		p.displaySalesUnitList.load(conn);

		return p;
	}

	/**
	 * Load from payload.
	 *
	 * @param payload payload object
	 */
	public void setFromPayload(PayloadI payload) {
		ErpMaterialPayload p = (ErpMaterialPayload) payload;
		this.sapId = p.sapId;
		this.baseUnit = p.baseUnit != null ? p.baseUnit.intern() : null;
		this.description = p.description;
		this.atpRule = p.atpRule;
		this.leadTime = p.leadTime;
		this.upc = p.UPC;
		this.quantityCharacteristic = p.quantityCharacteristic;
		this.salesUnitCharacteristic = p.salesUnitCharacteristic;
        this.alcoholicContent = p.alcoholicContent;
        this.taxable = p.taxable;
        this.kosherProduction = p.kosherProduction;
        this.platter = p.platter;
        this.blockedDays = p.blockedDays;
		this.prices = p.prices;
		this.salesUnits = p.salesUnits;
		this.classes = p.classes;
		this.displaySalesUnits = p.displaySalesUnitList;

	}

	public void remove(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - store() should not be called, business object unmutable.");
	}

	public VersionedPrimaryKey create(Connection conn, int version) throws SQLException {
		try {
			String id = this.getNextId(conn, "ERPS");

			PreparedStatement ps = conn.prepareStatement("INSERT INTO ERPS.MATERIAL (ID, VERSION, SAP_ID, BASE_UNIT, DESCRIPTION, ATP_RULE, LEAD_TIME, CHAR_QUANTITY, CHAR_SALESUNIT, UPC, ALCOHOLIC_CONTENT, TAXABLE, KOSHER_PRODUCTION, PLATTER, BLOCKED_DAYS) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, id);
			ps.setInt(2, version);
			ps.setString(3, this.sapId);
			ps.setString(4, this.baseUnit);
			ps.setString(5, this.description);
			ps.setInt(6, this.atpRule.getValue());
			ps.setInt(7, this.leadTime);
			ps.setString(8, this.quantityCharacteristic);
			ps.setString(9, this.salesUnitCharacteristic);
			ps.setString(10, this.upc);
            ps.setString(11, this.alcoholicContent.getCode());
            ps.setString(12, this.taxable ? "X" : "");
            ps.setString(13, this.kosherProduction ? "X" : "");
			ps.setString(14, this.platter ? "X" : "");
			ps.setString(15, this.blockedDays == null ? "" : this.blockedDays.encode());

			if (ps.executeUpdate() != 1) {
				throw new SQLException("No database rows created!");
			}
			ps.close();

			this.setPK(new VersionedPrimaryKey(id, version));

			// create children
			this.prices.setParentPK(this.getPK());
			this.prices.create(conn);
			this.salesUnits.setParentPK(this.getPK());
			this.salesUnits.create(conn);
			this.classes.setParentPK(this.getPK());
			this.classes.create(conn);

			return (VersionedPrimaryKey) this.getPK();
		} catch (SQLException sqle) {
			LOGGER.error("Error encountered creating ErpMaterialEntityBean", sqle);
			throw sqle;
		}
	}

	/**
	 * Protected setter for pricing conditions. Overwrites existing collection.
	 *
	 * @param collection collection of ErpMaterialPriceModel objects
	 */
	protected void setPricesFromModel(Collection collection) {
		// create persistent bean collection
		List persistentBeans = new LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpMaterialPriceModel model = (ErpMaterialPriceModel) i.next();
			persistentBeans.add(new ErpMaterialPricePersistentBean(model));
		}
		// set it
		this.prices.set(persistentBeans);
	}

	/**
	 * Protected setter for sales units. Overwrites existing collection.
	 *
	 * @param collection collection of ErpSalesUnitModel objects
	 */
	protected void setSalesUnitsFromModel(Collection collection) {
		// create persistent bean collection
		List persistentBeans = new LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpSalesUnitModel model = (ErpSalesUnitModel) i.next();
			persistentBeans.add(new ErpSalesUnitPersistentBean(model));
		}
		// set it
		this.salesUnits.set(persistentBeans);
	}

	/**
	 * Protected setter for classes. Overwrites existing collection.
	 *
	 * @param collection collection of ErpClassModel objects
	 */
	protected void setClassesFromModel(Collection collection) {
		// create a list of keys of the remote objects
		List classPkList = new LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpClassModel model = (ErpClassModel) i.next();
			classPkList.add(model.getPK());
		}
		// set
		this.classes.set(classPkList);
	}

	/**
	 * Payload class used in the fat PK
	 */
	private class ErpMaterialPayload implements PayloadI {
		String sapId;
		String baseUnit;
		String description;
		EnumATPRule atpRule;
		int leadTime;
		String UPC;
		String quantityCharacteristic;
		String salesUnitCharacteristic;
        EnumAlcoholicContent alcoholicContent;
        boolean taxable;
        boolean kosherProduction;
        boolean platter;
        DayOfWeekSet blockedDays;
		MaterialPriceList prices = new MaterialPriceList();
		SalesUnitList salesUnits = new SalesUnitList();
		ClassList classes = new ClassList();
		DisplaySalesUnitList displaySalesUnitList = new DisplaySalesUnitList();
	}

	/**
	 * Inner class for the list of dependent ErpSalesUnit persistent beans.
	 */
	private static class SalesUnitList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpSalesUnitPersistentBean.findByParent(conn, (VersionedPrimaryKey) SalesUnitList.this.getParentPK()));
		}
	}

	/**
	 * Inner class for the list of dependent ErpMaterialPrice persistent beans.
	 */
	private static class MaterialPriceList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpMaterialPricePersistentBean.findByParent(conn, (VersionedPrimaryKey) MaterialPriceList.this.getParentPK()));
		}
	}

	/**
	 * Inner class for maintaining the list of aggregated ErpClass entities.
	 */
	private class ClassList extends RemoteObjectList {

		protected EntityBeanRemoteI findEntity(PrimaryKey pk) throws RemoteException, FinderException {
			return ((ErpClassHome) getHome()).findByPrimaryKey((VersionedPrimaryKey) pk);
		}

		protected PersistentReferences createReferenceManager() {
			return new PersistentReferences("ERPS.MATERIAL_CLASS", "MAT_ID", "CLASS_ID");
		}

		protected PrimaryKey idToPK(String id) {
			return new VersionedPrimaryKey(id, ((VersionedPrimaryKey) ClassList.this.getParentPK()).getVersion());
		}

	}
	/**
	 * Inner class for the list of dependent ErpSalesUnit persistent beans.
	 */
	private static class DisplaySalesUnitList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpSalesUnitPersistentBean.findByParentForDisplay(conn, (VersionedPrimaryKey) DisplaySalesUnitList.this.getParentPK()));
		}
	}
	
}
