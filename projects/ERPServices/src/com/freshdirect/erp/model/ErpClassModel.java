/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.erp.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.erp.DurableModelI;
import com.freshdirect.erp.EntityModelI;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;
import com.freshdirect.framework.collection.LocalObjectList;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpClass model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpClassModel extends ErpModelSupport implements DurableModelI, EntityModelI {

	/** SAP unique ID */
	private String sapId;

	/**
	 * Characteristics
	 * @link aggregationByValue
	 * @associates <{com.freshdirect.erp.model.ErpCharacteristicModel}>
	 */
	private LocalObjectList characteristics = new LocalObjectList();

	/**
	 * Default constructor.
	 */
	public ErpClassModel() {
		super();
	}

	/**
	 * Constructor with all properties.
	 *
	 * @param sapId SAP unique ID
	 * @param characteristics collection of ErpCharacteristicModel objects
	 */
	public ErpClassModel(String sapId, List characteristics) {
		super();
		this.setSapId(sapId);
		this.setCharacteristics(characteristics);
	}

	/**
	 * Get SAP unique class ID.
	 *
	 * @return SAP ID
	 */
	public String getSapId() {
		return this.sapId;
	}

	/**
	 * Set SAP unique class ID.
	 *
	 * @param sapId SAP ID
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	/**
	 * Get characteristics.
	 *
	 * @return collection of ErpCharacteristicModel objects
	 */
	public List getCharacteristics() {
		return Collections.unmodifiableList( this.characteristics );
	}
    
    /**
     * Add a characteristic.
     *
     * @param characteristic a characteristic that belongs to this class
     */
    public void addCharacteristic(ErpCharacteristicModel characteristic) {
        this.characteristics.add(characteristic);
    }
    
    /**
     * Test to see if a class contains a characteristic
     *
     * @param characteristic the characteristic to test for
     */
    public boolean hasCharacteristic(ErpCharacteristicModel characteristic) {
        if (!characteristic.isAnonymous()) {
            //
            // if not an anonymous model object, search by primary key
            //
            return this.characteristics.contains(characteristic.getPK());
        } else {
            //
            // otherwise, try to match by the characteristic's name
            //
            Iterator iter = this.characteristics.iterator();
            while (iter.hasNext()) {
                ErpCharacteristicModel erpChar = (ErpCharacteristicModel) iter.next();
                if (erpChar.getName().equals(characteristic.getName()))
                    return true;
            }
            return false;
        }
    }
    
    /**
     * Find a characteristic by its name
     * return null if no matching characteristic is found
     *
     * @param characteristicName the of a characteristic to search for
     */
    public ErpCharacteristicModel getCharacteristic(String characteristicName) {
        Iterator iter = this.characteristics.iterator();
        while (iter.hasNext()) {
            ErpCharacteristicModel erpChar = (ErpCharacteristicModel) iter.next();
            if (erpChar.getName().equals(characteristicName))
                return erpChar;
        }
        return null;
    }

	/**
	 * Get a copy of the class, with certain characteristic values hidden.
	 *
	 * @param hiddenCharValuePKs array of characteristic value PKs to hide
	 *
	 * @return a filtered copy
	 */
	public ErpClassModel getFilteredClone(PrimaryKey[] hiddenCharValuePKs) {
		LocalObjectList list = new LocalObjectList();
		for (Iterator i=this.characteristics.iterator(); i.hasNext(); ) {
            ErpCharacteristicModel erpCharac = ((ErpCharacteristicModel)i.next()).getFilteredClone( hiddenCharValuePKs );
            if (erpCharac.getCharacteristicValues().size() > 0) {
                //
                // if all the charValues have been filtered from the characteristic,
                // don't add the empty characteristic to the filtered clone
                //
                list.add(erpCharac);
            }
		}
		ErpClassModel filteredClass = new ErpClassModel(this.getSapId(), list);
		filteredClass.setAttributes( this.getAttributes() );
		return filteredClass;
	}

	/**
	 * Set characteristics.
	 *
	 * @param collection collection of ErpCharacteristicModel objects
	 */
	public void setCharacteristics(List collection) {
		this.characteristics.set(collection);
	}

	/**
	 * Get number of characteristic in this class.
	 * 
	 * @return number of characteristics
	 */
	public int numberOfCharacteristics() {
		return this.characteristics.size();
	}

	/**
	 * Get the durable (long-lived) ID for the business object.
	 * This is the class's SAP ID.
	 *
	 * @return durable ID
	 */
	public String getDurableId() {
		return this.getSapId();
	}

	/**
	 * Template method to visit the children of this ErpModel.
	 * It should call accept(visitor) on these (or do nothing).
	 *
	 * @param visitor visitor instance to pass around
	 */
	public void visitChildren(ErpVisitorI visitor) {
        for (Iterator i = this.characteristics.iterator(); i.hasNext(); ) {
            ((ErpCharacteristicModel)i.next()).accept( visitor );
		}
	}

}
