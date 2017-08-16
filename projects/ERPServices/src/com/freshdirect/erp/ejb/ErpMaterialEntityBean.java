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

import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.EnumProductApprovalStatus;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
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
import com.freshdirect.framework.util.DaoUtil;
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

	private static final long serialVersionUID = 4175026067622154552L;

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
    
	/** skucode */
	private String skuCode;
	
	/**Avalara tax code */
	private String taxCode;
	
	/** days fresh */
	private String daysFresh;
	
	/** Type material */
	private String materialType;
	
	/** material approval status */
	private EnumProductApprovalStatus approvedStatus;
   
	/** isTaxable */
	private boolean taxable;
	
	private String materialGroup;


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
	
	private PlantMaterialList materialPlants;
	
	private MaterialSalesAreaList materialSalesAreas;
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
		this.daysFresh = null;
		this.upc = null;
		this.quantityCharacteristic = null;
		this.salesUnitCharacteristic = null;
      this.alcoholicContent = null;
      this.skuCode = null;
      this.materialType = null;
		this.prices = new MaterialPriceList();
		this.salesUnits = new SalesUnitList();
		this.classes = new ClassList();
		this.displaySalesUnits = new DisplaySalesUnitList();
		// find home for objects in remote object list
		this.classes.setEJBHome(getClassHome());
		this.materialPlants = new PlantMaterialList();
		this.materialSalesAreas = new MaterialSalesAreaList();
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
	public ModelI getModel()
	{
		// build the material model
		ErpMaterialModel model = new ErpMaterialModel(this.sapId, this.baseUnit, this.description, this.upc,
				this.quantityCharacteristic, this.salesUnitCharacteristic, this.alcoholicContent, this.taxable, this.taxCode, this.skuCode,
				this.daysFresh, this.approvedStatus, this.materialType, this.prices.getModelList(), this.salesUnits.getModelList(),
				this.classes.getModelList(), this.displaySalesUnits.getModelList(), this.materialPlants.getModelList(),this.materialSalesAreas.getModelList(), this.materialGroup);

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
		this.upc = m.getUPC();
		this.quantityCharacteristic = m.getQuantityCharacteristic();
		this.salesUnitCharacteristic = m.getSalesUnitCharacteristic();
		this.alcoholicContent = m.getAlcoholicContent();
		this.taxable = m.isTaxable();
		this.skuCode = m.getSkuCode();
		this.daysFresh = m.getDaysFresh();
		this.materialType = m.getMaterialType();
		this.approvedStatus = m.getApprovalStatus();
		this.taxCode = m.getTaxCode();
		this.setPricesFromModel(m.getPrices());
		this.setSalesUnitsFromModel(m.getSalesUnits());
		this.setClassesFromModel(m.getClasses());
		this.setMaterialPlantsFromModel(m.getMaterialPlants());
		this.setMaterialSalesAreasFromModel(m.getMaterialSalesAreas());
		this.materialGroup = m.getMaterialGroup();
	}
	
	/**
	 * Overriden isModified.
	 */
	public boolean isModified()
	{
		// check children too
		return super.isModified() || this.prices.isModified() || this.salesUnits.isModified() || this.materialPlants.isModified() || this.materialSalesAreas.isModified();
	}

	/**
	 * @param sapId
	 * @return VersionedPrimaryKey
	 * @throws FinderException
	 */
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
	 * @param skuCode
	 * @return VersionedPrimaryKey
	 * @throws FinderException
	 */
	public VersionedPrimaryKey ejbFindBySkuCode(String skuCode) throws FinderException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;	
		try {
			conn = getConnection();
			ps = conn.prepareStatement("select id, version, sap_id, skucode from erps.material where skucode=? and version=(select max(version) from erps.material where skucode=?)");
			ps.setString(1, skuCode);
			ps.setString(2, skuCode);
			rs = ps.executeQuery();

			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find a Material with an SAP ID = " + sapId);
			}

			VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2), null);

			return vpk;

		} catch (SQLException sqle) {
			throw new EJBException("Unable to find a Material by its Sku Code: " + sqle.getMessage());
		} finally {
			DaoUtil.close(rs);
			DaoUtil.close(ps);
			DaoUtil.close(conn);
			}

	}
	
	/**
	 * @param skuCode
	 * @return VersionedPrimaryKey
	 * @throws FinderException
	 */
	public VersionedPrimaryKey ejbFindBySkuCodeAndVersion(String skuCode, int version) throws FinderException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("select id, version, sap_id, skucode from erps.material where skucode=? and version=?");
			ps.setString(1, skuCode);
			ps.setInt(2, version);
			rs = ps.executeQuery();

			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find a Material with an SAP ID = " + sapId);
			}

			VersionedPrimaryKey vpk = new VersionedPrimaryKey(rs.getString(1), rs.getInt(2), null);

			return vpk;

		} catch (SQLException sqle) {
			throw new EJBException("Unable to find a Material by its Sku Code: " + sqle.getMessage());
		} finally {
			DaoUtil.close(rs);
			DaoUtil.close(ps);
			DaoUtil.close(conn);
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
		
		ErpMaterialPayload p = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT SAP_ID, skucode, BASE_UNIT, DESCRIPTION, CHAR_QUANTITY, CHAR_SALESUNIT, UPC, ALCOHOLIC_CONTENT, " +
					"TAXABLE,  MATERIAL_TYPE, APPROVAL_STATUS, TAX_CODE, MATERIAL_GROUP FROM ERPS.MATERIAL WHERE ID = ? AND VERSION = ?");
			
			ps.setString(1, vpk.getId());
			ps.setInt(2, vpk.getVersion());
			
			rs = ps.executeQuery();
			if (!rs.next())
			{
				return null;
			}

			p = new ErpMaterialPayload();
			p.sapId = rs.getString(1);
			p.skuCode = rs.getString(2);
			p.baseUnit = rs.getString(3);
			p.description = rs.getString(4);
			p.quantityCharacteristic = rs.getString(5);
			p.salesUnitCharacteristic = rs.getString(6);
			p.UPC = rs.getString(7);
			p.alcoholicContent = EnumAlcoholicContent.getAlcoholicContent(rs.getString(8));
					p.taxable = "X".equalsIgnoreCase(rs.getString(9));
			// p.daysFresh = rs.getString(10);
			p.materialType = rs.getString(10);
			p.approvedStatus = EnumProductApprovalStatus.getApprovalStatus(rs.getString(11));
			p.taxCode = rs.getString(12);
			p.materialGroup = rs.getString(13);
     
//			rs.close();
//			ps.close();
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			if(null != rs){
				try {
					rs.close();
				} catch (Exception e) {
					
				}
			}
			
			if(null != ps){
				try {
					ps.close ();
				} catch (Exception e) {
					
				}
			}
		}

		// load children
		if(null != p){
			p.prices.setParentPK(pk);
			p.prices.load(conn);
			p.salesUnits.setParentPK(pk);
			p.salesUnits.load(conn);
			p.classes.setParentPK(pk);
			p.classes.setEJBHome(getClassHome());
			p.classes.load(conn);
			p.displaySalesUnitList.setParentPK(pk);
			p.displaySalesUnitList.load(conn);
			p.plantMaterials.setParentPK(pk);
			p.plantMaterials.load(conn);
			p.materialSalesAreas.setParentPK(pk);
			p.materialSalesAreas.load(conn);
		}

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
		this.upc = p.UPC;
		this.quantityCharacteristic = p.quantityCharacteristic;
		this.salesUnitCharacteristic = p.salesUnitCharacteristic;
		this.alcoholicContent = p.alcoholicContent;
		this.taxable = p.taxable;
		this.daysFresh = p.daysFresh;
		this.approvedStatus = p.approvedStatus;
		this.materialType = p.materialType;
		this.prices = p.prices;
		this.salesUnits = p.salesUnits;
		this.classes = p.classes;
		this.displaySalesUnits = p.displaySalesUnitList;
		this.skuCode =p.skuCode;
		this.materialPlants=p.plantMaterials;
		this.materialSalesAreas=p.materialSalesAreas;
		this.taxCode = p.taxCode;
		this.materialGroup = p.materialGroup;

	}

	public void remove(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - store() should not be called, business object unmutable.");
	}

	public VersionedPrimaryKey create(Connection conn, int version) throws SQLException {
		try {
			String id = this.getNextId(conn, "ERPS");

			PreparedStatement ps = conn.prepareStatement("INSERT INTO ERPS.MATERIAL (ID, VERSION, SAP_ID, skucode, BASE_UNIT, DESCRIPTION, CHAR_QUANTITY, CHAR_SALESUNIT, UPC, ALCOHOLIC_CONTENT, TAXABLE, daysfresh, MATERIAL_TYPE, APPROVAL_STATUS, TAX_CODE, MATERIAL_GROUP) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, id);
			ps.setInt(2, version);
			ps.setString(3, this.sapId);
			ps.setString(4, this.skuCode);
			ps.setString(5, this.baseUnit);
			ps.setString(6, this.description);
			ps.setString(7, this.quantityCharacteristic);
			ps.setString(8, this.salesUnitCharacteristic);
			ps.setString(9, this.upc);
			ps.setString(10, null !=this.alcoholicContent? this.alcoholicContent.getCode():"");
			ps.setString(11, this.taxable ? "X" : "");
			ps.setString(12, this.daysFresh);
			ps.setString(13, this.materialType);
			ps.setString(14, null !=this.approvedStatus?this.approvedStatus.getStatusCode():"N");
			ps.setString(15, this.taxCode);
			ps.setString(16, this.materialGroup);
			
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
			this.materialPlants.setParentPK(this.getPK());
			this.materialPlants.create(conn);
			this.materialSalesAreas.setParentPK(this.getPK());
			this.materialSalesAreas.create(conn);

			return (VersionedPrimaryKey) this.getPK();
		} catch (SQLException sqle) {
			LOGGER.error("Error encountered creating ErpMaterialEntityBean", sqle);
			throw sqle;
		}
	}
	
	/**
	 * Store the updated sales unit, price rows, plant material info.
	 *
	 * @param conn database connection
	 * 
	 * @throws SQLException if a database error occured
	 */
	public void store(Connection conn) throws SQLException
	{
		
		if (this.isModified())
		{
			PreparedStatement ps = conn.prepareStatement("UPDATE ERPS.MATERIAL SET APPROVAL_STATUS = ? WHERE ID = ?");
			ps.setString(1, null !=this.approvedStatus?this.approvedStatus.getStatusCode():"N");
			ps.setString(2, this.getPK().getId());
			
			if (ps.executeUpdate() != 1) 
			{
				throw new SQLException("Failed to update row");
			}
		}
		
		// store children
		if (this.salesUnits.isModified()) {
			this.salesUnits.store(conn);
		}
		
		if (this.prices.isModified()) {
			this.prices.store(conn);
		}
		
		if (this.materialPlants.isModified()) {
			this.materialPlants.store(conn);
		}
		
		if(this.materialSalesAreas.isModified()){
			this.materialSalesAreas.store(conn);
		}
	}
	
	
	/**
	 * Set ErpSalesUnitModel entries. Overwrites existing collection.
	 * 
	 * @param salesUnits collection of ErpSalesUnitModel objects
	 *
	 * @return true if the entires were stored
	 */
	public boolean setSalesUnits(@SuppressWarnings("rawtypes") Collection salesUnits)
	{		
		this.setSalesUnitsFromModel(salesUnits);
		this.setModified();
		return true;		
	}

	/**
	 * Set ErpSalesUnitModel entries. Overwrites existing collection.
	 * 
	 * @param prices collection of ErpSalesUnitModel objects
	 *
	 * @return true if the entires were stored
	 */
	public boolean setPrices(@SuppressWarnings("rawtypes") Collection prices) {
		this.setPricesFromModel(prices);
		this.setModified();
		return true;	
	}

	
	/**
	 * Set ErpPlantMaterialModel entries. Overwrites existing collection.
	 * 
	 * @param plantMaterials collection of ErpPlantMaterialModel objects
	 *
	 * @return true if the entires were stored
	 */
	public boolean setMaterialPlants(@SuppressWarnings("rawtypes") Collection plantMaterials) {
		this.setMaterialPlantsFromModel(plantMaterials);
		this.setModified();
		return true;	
	}
	
	/**
	 * Protected setter for pricing conditions. Overwrites existing collection.
	 *
	 * @param collection collection of ErpMaterialPriceModel objects
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void setMaterialPlantsFromModel(Collection collection) {
		// create persistent bean collection
		List persistentBeans = new LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpPlantMaterialModel model = (ErpPlantMaterialModel) i.next();
			persistentBeans.add(new ErpPlantMaterialPersistentBean(model));
		}
	
		this.materialPlants.set(persistentBeans);
	}

	
	/**
	 * Set ErpPlantMaterialModel entries. Overwrites existing collection.
	 * 
	 * @param plantMaterials collection of ErpPlantMaterialModel objects
	 *
	 * @return true if the entires were stored
	 */
	public boolean setMaterialSalesAreas(@SuppressWarnings("rawtypes") Collection materialSalesAreas) {
		this.setMaterialSalesAreasFromModel(materialSalesAreas);
		this.setModified();
		return true;	
	}
	
	/**
	 * Protected setter for pricing conditions. Overwrites existing collection.
	 *
	 * @param collection collection of ErpMaterialPriceModel objects
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void setMaterialSalesAreasFromModel(Collection collection) {
		// create persistent bean collection
		List persistentBeans = new LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpMaterialSalesAreaModel model = (ErpMaterialSalesAreaModel) i.next();
			persistentBeans.add(new ErpMaterialSalesAreaPersistentBean(model));
		}
	
		this.materialSalesAreas.set(persistentBeans);
	}
	/**
	 * Protected setter for pricing conditions. Overwrites existing collection.
	 *
	 * @param collection collection of ErpMaterialPriceModel objects
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void setPricesFromModel(Collection collection) {
		// create persistent bean collection
		List persistentBeans = new LinkedList();
		for (Iterator i = collection.iterator(); i.hasNext();) {
			ErpMaterialPriceModel model = (ErpMaterialPriceModel) i.next();
			persistentBeans.add(new ErpMaterialPricePersistentBean(model));
		}
	
		this.prices.set(persistentBeans);
	}
	
	/**
	 * Protected setter for sales units. Overwrites existing collection.
	 *
	 * @param collection collection of ErpSalesUnitModel objects
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		String UPC;
		String taxCode;
		String quantityCharacteristic;
		String salesUnitCharacteristic;
		EnumAlcoholicContent alcoholicContent;
		boolean taxable;
		String skuCode;
		private String daysFresh;
		private EnumProductApprovalStatus approvedStatus;
		private String materialType;	
		private String materialGroup;
		
     	MaterialPriceList prices = new MaterialPriceList();
		SalesUnitList salesUnits = new SalesUnitList();
		ClassList classes = new ClassList();
		DisplaySalesUnitList displaySalesUnitList = new DisplaySalesUnitList();
		PlantMaterialList plantMaterials = new PlantMaterialList();
		MaterialSalesAreaList materialSalesAreas = new MaterialSalesAreaList();
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
	
	
	/**
	 * Inner class for the list of dependent ErpPlantMaterial persistent beans.
	 */
	private static class PlantMaterialList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpPlantMaterialPersistentBean.findByParent(conn, (VersionedPrimaryKey) PlantMaterialList.this.getParentPK()));
		}
	}
	
	/**
	 * Inner class for the list of dependent ErpPlantMaterial persistent beans.
	 */
	private static class MaterialSalesAreaList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpMaterialSalesAreaPersistentBean.findByParent(conn, (VersionedPrimaryKey) MaterialSalesAreaList.this.getParentPK()));
		}
	}
}
