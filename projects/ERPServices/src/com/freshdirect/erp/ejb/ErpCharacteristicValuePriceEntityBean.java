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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PayloadI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedEntityBeanSupport;
import com.freshdirect.framework.core.VersionedPrimaryKey;

/**
 * ErpCharacteristicValuePrice entity bean implementation.
 *
 * @version    $Revision$
 * @author     $Author$
 *
 * @ejbHome <{ErpCharacteristicValuePriceHome}>
 * @ejbRemote <{ErpCharacteristicValuePriceEB}>
 * @ejbPrimaryKey <{VersionedPrimaryKey}>
 * @stereotype fd-entity
 */
public class ErpCharacteristicValuePriceEntityBean extends VersionedEntityBeanSupport {
    
    /**@link dependency*/
    /*#ErpMaterialEntityBean lnkErpMaterialEntityBean;*/
    
    /**@link dependency*/
    /*#ErpCharacteristicValuePersistentBean lnkErpCharacteristicValuePersistentBean;*/
    
    /** ErpMaterial ID */
    private String materialId;
    
    /** ErpCharacteristicValue ID */
    private String characteristicValueId;
    
    /** SAP unique ID */
    private String sapId;
    
    /** Price in USD */
    private double price;
    
    /** Pricing unit of measure */
    private String pricingUnit;
    
    /** Condition type */
    private String conditionType;
    
    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    protected String getResourceCacheKey() {
        return "com.freshdirect.erp.ejb.ErpCharacteristicValuePriceHome";
    }
    
    /**
     * Copy into model.
     *
     * @return ErpCharacteristicValuePriceModel object.
     */
    public ModelI getModel() {
        ErpCharacteristicValuePriceModel model = new ErpCharacteristicValuePriceModel(
        this.materialId,
        this.characteristicValueId,
        this.sapId,
        this.price,
        this.pricingUnit,
        this.conditionType );
        super.decorateModel(model);
        return model;
    }
    
    /**
     * Copy from model.
     */
    public void setFromModel(ModelI model) {
        // copy properties from model
        ErpCharacteristicValuePriceModel m = (ErpCharacteristicValuePriceModel)model;
        this.materialId = m.getMaterialId();
        this.characteristicValueId = m.getCharacteristicValueId();
        this.sapId = m.getSapId();
        this.price = m.getPrice();
        this.pricingUnit = m.getPricingUnit();
        this.conditionType = m.getConditionType();
    }
    
    /**
     * Create with version
     */
    public VersionedPrimaryKey create(Connection conn, int version) throws SQLException {
    	String id = this.getNextId(conn, "ERPS");
		
        PreparedStatement ps = conn.prepareStatement("insert into erps.charvalueprice (id, version, mat_id, cv_id, sap_id, price, pricing_unit, condition_type) values (?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, id);
        ps.setInt(2, version);
        ps.setString(3, this.materialId);
        ps.setString(4, this.characteristicValueId);
        ps.setString(5, this.sapId);
        ps.setDouble(6, this.price);
        ps.setString(7, this.pricingUnit);
        ps.setString(8, this.conditionType);
        
        if (ps.executeUpdate() != 1) {
            throw new SQLException("Row not created");
        }
		ps.close();

		this.setPK(new VersionedPrimaryKey(id, version));
        
        return (VersionedPrimaryKey) this.getPK();
    }
    
    /**
     * Set from payload.
     *
     * @param payload payload object
     */
    public void setFromPayload(PayloadI payload) {
        ErpCharacteristicValuePricePayload p = (ErpCharacteristicValuePricePayload)payload;
        // copy properties from payload
        this.materialId = p.materialId;
        this.characteristicValueId = p.characteristicValueId;
        this.sapId = p.sapId;
        this.price = p.price;
        this.pricingUnit = p.pricingUnit;
        this.conditionType = p.conditionType;
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
        
        PreparedStatement ps = conn.prepareStatement("select mat_id, cv_id, sap_id, price, pricing_unit, condition_type from erps.charvalueprice where id = ?");
        ps.setString(1, pk.getId());

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        }
        
        ErpCharacteristicValuePricePayload p = new ErpCharacteristicValuePricePayload();
        // fill payload properties from resultset
        p.materialId = rs.getString(1);
        p.characteristicValueId = rs.getString(2);
        p.sapId = rs.getString(3);
        p.price = rs.getDouble(4);
        p.pricingUnit = rs.getString(5);
        p.conditionType = rs.getString(6);
        
        rs.close();
        ps.close();
        
        return p;
    }
    
    public Collection ejbFindByMaterial(VersionedPrimaryKey materialPK) throws FinderException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();
            
            ps = conn.prepareStatement("select id,version,mat_id,cv_id,sap_id,price,pricing_unit,condition_type from erps.charvalueprice where mat_id = ?");
            ps.setString(1, materialPK.getId());
            
            rs = ps.executeQuery();
            
            int materialVersion=materialPK.getVersion();
            
            List lst = new LinkedList();
            while (rs.next()) {
                
                String id = rs.getString(1);
                int version = rs.getInt(2);
                if (materialVersion != version) {
                    throw new FinderException("Data integrity error: version out of synch on char value ID "+id);
                }
                
                ErpCharacteristicValuePricePayload p = new ErpCharacteristicValuePricePayload();
                p.materialId = rs.getString(3);
                p.characteristicValueId = rs.getString(4);
                p.sapId = rs.getString(5);
                p.price = rs.getDouble(6);
                p.pricingUnit = rs.getString(7);
                p.conditionType = rs.getString(8);
                
                lst.add( new VersionedPrimaryKey(id, version, p) );
            }
            
            return lst;
            
        } catch (SQLException sqle) {
            throw new FinderException(sqle.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle2) {
                // eat it
            }
        }
    }
    
    public VersionedPrimaryKey ejbFindByMaterialAndCharValue(VersionedPrimaryKey materialPK, VersionedPrimaryKey charValPK)
    throws FinderException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();
            
            ps = conn.prepareStatement("select id,version,mat_id,cv_id,sap_id,price,pricing_unit,condition_type from erps.charvalueprice where mat_id = ? and cv_id = ?");
            ps.setString(1, materialPK.getId());
            ps.setString(2, charValPK.getId());
            
            rs = ps.executeQuery();
            if (rs.next()) {
                
                String id = rs.getString(1);
                int version = rs.getInt(2);
                
                ErpCharacteristicValuePricePayload p = new ErpCharacteristicValuePricePayload();
                p.materialId = rs.getString(3);
                p.characteristicValueId = rs.getString(4);
                p.sapId = rs.getString(5);
                p.price = rs.getDouble(6);
                p.pricingUnit = rs.getString(7);
                p.conditionType = rs.getString(8);
                
                return new VersionedPrimaryKey(id, version, p);
            } else {
                throw new ObjectNotFoundException("No such CharacteristicValuePrice for Material ID = " + materialPK.getId() + " and CharValue ID = " + charValPK.getId());
            }
            
        } catch (SQLException sqle) {
            throw new FinderException(sqle.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle2) {
                throw new EJBException(sqle2);
            }
        }
    }
    
    
    
    public void remove(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Internal error - store() should not be called, business object unmutable.");
    }
    
    public void initialize(){
        this.materialId = null;
        this.characteristicValueId = null;
        this.sapId = null;
        this.conditionType = null;
        this.pricingUnit = null;
        this.price = 0.0;
    }
    
    /**
     * Payload class used in the fat PK
     */
    private static class ErpCharacteristicValuePricePayload implements PayloadI {
        String materialId;
        String characteristicValueId;
        String sapId;
        double price;
        String pricingUnit;
        String conditionType;
    }
    
}
