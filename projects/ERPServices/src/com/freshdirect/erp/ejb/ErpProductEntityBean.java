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
import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.framework.collection.PersistentReferences;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PayloadI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.VersionedEntityBeanSupport;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * ErpProduct entity bean implementation.
 *
 * @version    $Revision$
 * @author     $Author$
 *
 * @ejbHome <{ErpProductHome}>
 * @ejbRemote <{ErpProductEB}>
 * @ejbPrimaryKey <{VersionedPrimaryKey}>
 * @stereotype fd-entity
 */
public class ErpProductEntityBean extends VersionedEntityBeanSupport {

	private final static ServiceLocator LOCATOR = new ServiceLocator();
    
    /** SKU code */
    private String skuCode;
    
    /** default price */
    private double defaultPrice;
    
    /** pricing unit for default price */
    private String defaultPriceUnit;
    
    /** unavailability status code */
    private String unavailabilityStatus;
    
    /** unavailability date */
    private Date unavailabilityDate;
    
    /** unavailability reason */
    private String unavailabilityReason;

	/** effective pricing date */
	private Date pricingDate;


	private String materialProxyId;

	private String materialId;

	private SalesUnitReferences hiddenSalesUnitRefs;

	private CharValueReferences hiddenCharValueRefs;
	
	private String rating;
    
	private double basePrice;
	
	private String basePriceUnit;
	
    /**
     * Default constructor.
     */
    public ErpProductEntityBean() {
        super();
    }

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.erp.ejb.ErpProductHome";
	}
	    
    /**
     * Copy into model.
     *
     * @return ErpProductModel object.
     */
    public ModelI getModel() {
        ErpProductModel model = new ErpProductModel( this.skuCode, this.defaultPrice, this.defaultPriceUnit,
            this.unavailabilityStatus, this.unavailabilityDate, this.unavailabilityReason, this.pricingDate,
			this.getProxiedMaterial(),
			this.getHiddenSalesUnitPKs(),
			this.getHiddenCharacteristicValuePKs(),
			this.rating,this.basePrice,this.basePriceUnit);
        super.decorateModel(model);
        return model;
    }
    
    /**
     * Copy from model.
     */
    public void setFromModel(ModelI model) {
        // copy properties from model
        ErpProductModel m = (ErpProductModel)model;
        this.skuCode = m.getSkuCode();
        this.defaultPrice = m.getDefaultPrice();
        this.defaultPriceUnit = m.getDefaultPriceUnit();
        this.unavailabilityStatus = m.getUnavailabilityStatus();
        this.unavailabilityDate = m.getUnavailabilityDate();
        this.unavailabilityReason = m.getUnavailabilityReason();

		ErpMaterialModel matModel = (ErpMaterialModel) m.getProxiedMaterial();
		if (matModel.isAnonymous()) {
			throw new IllegalStateException("Proxied material cannot be anonymous");
		}
		this.materialId = matModel.getPK().getId();

		this.setHiddenSalesUnitPKs(m.getHiddenSalesUnitPKs());
		this.setHiddenCharacteristicValuePKs(m.getHiddenCharacteristicValuePKs());
		this.rating=m.getRating();
		this.basePrice=m.getBasePrice();
		this.basePriceUnit=m.getBasePriceUnit();

    }
    
    public VersionedPrimaryKey ejbFindBySkuCode(String sku) throws FinderException  {
        Connection conn = null;
        try {
            conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, p.version, sku_code, default_price, default_unit, unavailability_status, unavailability_date, unavailability_reason, date_created as pricing_date, rating, base_price, base_pricing_unit from erps.product p, erps.history h where sku_code = ? and p.version=(select max(version) from erps.product where sku_code = ?) and h.version=p.version");
			ps.setString(1, sku);
            ps.setString(2, sku);
			ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new ObjectNotFoundException("Unable to find a Product for SKU " + sku);
            }
            
			VersionedPrimaryKey vpk = this.fillPayload(conn, rs);

			rs.close();
			ps.close();

            return vpk;
            
        } catch (SQLException sqle) {
            throw new EJBException("Unable to find a Product by its SKU : " + sqle.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
        }
    }

	private VersionedPrimaryKey fillPayload(Connection conn, ResultSet rs) throws SQLException {
		ErpProductPayload p = new ErpProductPayload();
		p.skuCode = rs.getString(3);
		p.defaultPrice = rs.getDouble(4);
		p.defaultPriceUnit = rs.getString(5);
		p.unavailabilityStatus = rs.getString(6);
		p.unavailabilityDate = rs.getTimestamp(7);
		p.unavailabilityReason = rs.getString(8);
		p.pricingDate = rs.getTimestamp(9);
		p.rating=rs.getString(10);
		p.basePrice=rs.getDouble(11);
		p.basePriceUnit=rs.getString(12);
		
		String id = rs.getString(1);
		int version = rs.getInt(2);

		this.loadChildren(conn, p, id);

		VersionedPrimaryKey vpk = new VersionedPrimaryKey(id, version, p);

		return vpk;
	}

	private void loadChildren(Connection conn, ErpProductPayload p, String id) throws SQLException {
		PreparedStatement ps2 = conn.prepareStatement("select id, mat_id from erps.materialproxy where product_id = ?");
		ps2.setString(1, id);
		ResultSet rs2 = ps2.executeQuery();
		
		if (!rs2.next()) {
			throw new SQLException("No materialProxy entry for product "+id);	
		}
		
		String mpxId = rs2.getString(1);
		p.materialId = rs2.getString(2);
		
		rs2.close();
		ps2.close();
		
		p.hiddenSalesUnitRefs = new SalesUnitReferences(mpxId);
		p.hiddenSalesUnitRefs.load(conn);
		p.hiddenCharValueRefs = new CharValueReferences(mpxId);
		p.hiddenCharValueRefs.load(conn);
	}

	public VersionedPrimaryKey ejbFindBySkuCodeAndVersion(String sku, int version) throws FinderException {
        Connection conn = null;
        try {
            conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select id, p.version, sku_code, default_price, default_unit, unavailability_status, unavailability_date, unavailability_reason, date_created as pricing_date, rating, base_price, base_pricing_unit  from erps.product p, erps.history h where sku_code = ? and p.version = ? and h.version=p.version");
            ps.setString(1, sku);
            ps.setInt(2, version);
			ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new ObjectNotFoundException("Unable to find a Product for SKU " + sku + " version "+version);
            }
            
			VersionedPrimaryKey vpk = this.fillPayload(conn, rs);
            
            rs.close();
            ps.close();

            // load children
            
            return vpk;
            
        } catch (SQLException sqle) {
            throw new EJBException("Unable to find a Product by SKU and version: " + sqle.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
        }
    }

   
    /**
     * Create with version
     */
    public VersionedPrimaryKey create(Connection conn, int version) throws SQLException {
    	String id = this.getNextId(conn, "ERPS");
        PreparedStatement ps = conn.prepareStatement("insert into erps.product (id, version, sku_code, default_price, default_unit, unavailability_status, unavailability_date, unavailability_reason, rating, base_price, base_pricing_unit ) values (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
        ps.setString(1, id);
        ps.setInt(2, version);
        ps.setString(3, this.skuCode);
        ps.setDouble(4, this.defaultPrice );
        ps.setString(5, this.defaultPriceUnit);
        ps.setString(6, this.unavailabilityStatus);
        ps.setTimestamp(7, new Timestamp(this.unavailabilityDate.getTime()));
        ps.setString(8, this.unavailabilityReason);
        ps.setString(9, this.rating);
        ps.setDouble(10, this.basePrice);
        ps.setString(11, this.basePriceUnit);

        if (ps.executeUpdate() != 1) {
            throw new SQLException("Row not created");
        }
        ps.close();

		this.setPK(new VersionedPrimaryKey(id, version));

        // create materialproxy
		this.materialProxyId = this.getNextId(conn, "ERPS");
		ps = conn.prepareStatement("insert into erps.materialproxy (id, product_id, version, mat_id) values (?, ?, ?, ?)");
		ps.setString(1, this.materialProxyId);
		ps.setString(2, id);
		ps.setInt(3, version);
		ps.setString(4, this.materialId);

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}
		ps.close();

		// create children
		this.hiddenSalesUnitRefs.setParentId(this.materialProxyId);
		this.hiddenSalesUnitRefs.create(conn);
		this.hiddenCharValueRefs.setParentId(this.materialProxyId);
		this.hiddenCharValueRefs.create(conn);
        
        return (VersionedPrimaryKey) this.getPK();
    }
    
    
    /**
     * Set from payload.
     *
     * @param payload payload object
     */
    public void setFromPayload(PayloadI payload) {
        ErpProductPayload p = (ErpProductPayload)payload;
        // copy properties from payload
        this.skuCode = p.skuCode;
        this.defaultPrice = p.defaultPrice;
        this.defaultPriceUnit = p.defaultPriceUnit;
        this.unavailabilityStatus = p.unavailabilityStatus;
        this.unavailabilityDate = p.unavailabilityDate;
        this.unavailabilityReason = p.unavailabilityReason;
        this.pricingDate = p.pricingDate;
        this.rating=p.rating;
        
		this.materialProxyId = p.materialProxyId;
		this.materialId = p.materialId;
		this.hiddenSalesUnitRefs = p.hiddenSalesUnitRefs;
		this.hiddenCharValueRefs = p.hiddenCharValueRefs;
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
        PreparedStatement ps = conn.prepareStatement("select sku_code, default_price, default_unit, unavailability_status, unavailability_date, unavailability_reason, date_created as pricing_date, rating,base_price, base_pricing_unit from erps.product p, erps.history h where id = ? and h.version=p.version");
        ps.setString(1, pk.getId());
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return null;
        }

		// fill payload properties from resultset
		ErpProductPayload p = new ErpProductPayload();
		p.skuCode = rs.getString(1);
		p.defaultPrice = rs.getDouble(2);
		p.defaultPriceUnit = rs.getString(3);
		p.unavailabilityStatus = rs.getString(4);
		p.unavailabilityDate = rs.getTimestamp(5);
		p.unavailabilityReason = rs.getString(6);
		p.pricingDate = rs.getTimestamp(7);
		p.rating=rs.getString(8);
		p.basePrice=rs.getDouble(9);
		p.basePriceUnit=rs.getString(10);
        
        rs.close();
        ps.close();
        
        // load children
		this.loadChildren(conn, p, pk.getId());

        return p;
    }


	public void remove(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - store() should not be called, business object unmutable.");
	}
    
    public void initialize(){
        this.skuCode = null;
        this.defaultPrice = 0.0;
        this.defaultPriceUnit = null;
        this.unavailabilityStatus = null;
        this.unavailabilityDate = null;
        this.unavailabilityReason = null;
        this.pricingDate = null;
        this.rating=null;
        this.basePrice=0.0;
        this.basePriceUnit=null;
    }
    
	private VersionedPrimaryKey[] getHiddenSalesUnitPKs() {
		return this.getPKArray(this.hiddenSalesUnitRefs.getRefs());
	}


	private void setHiddenSalesUnitPKs(VersionedPrimaryKey[] pks) {
		this.hiddenSalesUnitRefs = new SalesUnitReferences();
		this.hiddenSalesUnitRefs.setRefs(this.getIDArray(pks));
	}


	private VersionedPrimaryKey[] getHiddenCharacteristicValuePKs() {
		return this.getPKArray(this.hiddenCharValueRefs.getRefs());
	}


	private void setHiddenCharacteristicValuePKs(VersionedPrimaryKey[] pks) {
		this.hiddenCharValueRefs = new CharValueReferences();
		this.hiddenCharValueRefs.setRefs(this.getIDArray(pks));
	}

	private String[] getIDArray(VersionedPrimaryKey[] pks) {
		String[] ids = new String[pks.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = pks[i].getId();
		}
		return ids;
	}

	private VersionedPrimaryKey[] getPKArray(String[] ids) {
		int version = ((VersionedPrimaryKey) this.getPK()).getVersion();
		VersionedPrimaryKey[] pks = new VersionedPrimaryKey[ids.length];
		for (int i = 0; i < pks.length; i++) {
			pks[i] = new VersionedPrimaryKey(ids[i], version);
		}
		return pks;
	}
    

	public ErpMaterialModel getProxiedMaterial() {
		int version = ((VersionedPrimaryKey) this.getPK()).getVersion();
		VersionedPrimaryKey matPK = new VersionedPrimaryKey(materialId, version);
		try {

			ErpMaterialHome matHome =
				(ErpMaterialHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpMaterial", ErpMaterialHome.class);

			ErpMaterialEB matEB = matHome.findByPrimaryKey(matPK);
			return (ErpMaterialModel) matEB.getModel();
		} catch (javax.ejb.FinderException ex) {
			throw new EJBException(ex);
		} catch (javax.naming.NamingException ex) {
			throw new EJBException(ex);
		} catch (java.rmi.RemoteException ex) {
			throw new EJBException(ex);
		}
	}


    /**
     * Payload class used in the fat PK
     */
    private static class ErpProductPayload implements PayloadI {
        String skuCode;
        double defaultPrice;
        String defaultPriceUnit;
        String unavailabilityStatus;
        java.util.Date unavailabilityDate;
        String unavailabilityReason;
        java.util.Date pricingDate;
    
		String materialProxyId;
		String materialId;
		SalesUnitReferences hiddenSalesUnitRefs;
		CharValueReferences hiddenCharValueRefs;
		String rating;
		double basePrice;
		String basePriceUnit;
    }
    

	private static class SalesUnitReferences extends PersistentReferences {
		public SalesUnitReferences() {
			super("ERPS.MATERIALPROXY_SALESUNIT", "MATPROXY_ID", "SALESUNIT_ID");
		}

		public SalesUnitReferences(String parentId) {
			this();
			super.setParentId(parentId);
		}
	}

	private static class CharValueReferences extends PersistentReferences {
		public CharValueReferences() {
			super("ERPS.MATERIALPROXY_CHARVALUE", "MATPROXY_ID", "CV_ID");
		}

		public CharValueReferences(String parentId) {
			this();
			super.setParentId(parentId);
		}
	}
    
}
