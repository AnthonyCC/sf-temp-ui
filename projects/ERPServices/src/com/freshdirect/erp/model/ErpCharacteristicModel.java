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
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.ErpVisitorI;
import com.freshdirect.framework.collection.LocalObjectList;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpCharacteristic model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpCharacteristicModel extends ErpModelSupport implements DurableModelI {

	/** Characteristic name */
	private String name;

	/**
	 * Characteristic value collection
	 * @link aggregationByValue
	 * @associates <{ErpCharacteristicValueModel}>
	 */
	private LocalObjectList characteristicValues = new LocalObjectList();

	/**
	 * Default constructor.
	 */
	public ErpCharacteristicModel() {
		super();
	}

	/**
	 * Constructor with all properties.
	 *
	 * @param name characteristic name
	 * @param characteristicValues collection of CharacteristicValueModel objects
	 */
	public ErpCharacteristicModel(String name, List characteristicValues) {
		super();
		this.setName(name);
		this.setCharacteristicValues(characteristicValues);
	}

	/**
	 * Get characteristic name.
	 *
	 * @return characteristic name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set characteristic name.
	 *
	 * @param name characteristic name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get characteristic values.
	 *
	 * @return collection of ErpCharacteristicValueModel objects
	 */
	public List getCharacteristicValues() {
		return Collections.unmodifiableList( this.characteristicValues );
	}

	/**
	 * Set characteristic values.
	 *
	 * @param collection collection of ErpCharacteristicValueModel objects
	 */
	public void setCharacteristicValues(List collection) {
		this.characteristicValues.set(collection);
	}
    
    /**
     * Add a characteristic value.
     *
     * @param characteristic a characteristic that belongs to this class
     */
    public void addCharacteristicValue(ErpCharacteristicValueModel characteristicValue) {
        this.characteristicValues.add(characteristicValue);
    }
    
    /**
     * Test to see if a characteristic value contains a characteristic value
     *
     * @param characteristic the characteristic to test for
     */
    public boolean hasCharacteristicValue(ErpCharacteristicValueModel characteristicValue) {
        if (!characteristicValue.isAnonymous()) {
            //
            // if not an anonymous model object, search by primary key
            //
            return this.characteristicValues.contains(characteristicValue.getPK());
        } else {
            //
            // otherwise, try to match by the characteristic value's name
            //
            Iterator iter = characteristicValues.iterator();
            while (iter.hasNext()) {
                ErpCharacteristicValueModel erpCharVal = (ErpCharacteristicValueModel) iter.next();
                if (erpCharVal.getName().equals(characteristicValue.getName()))
                    return true;
            }
            return false;
        }
    }
    
    /**
     * Find a characteristic value  by its name
     * return null if no matching characteristic is found
     *
     * @param charValueName the name of a characteristic value to search for
     */
    public ErpCharacteristicValueModel getCharacteristicValue(String charValueName) {
        Iterator iter = this.characteristicValues.iterator();
        while (iter.hasNext()) {
            ErpCharacteristicValueModel erpCharVal = (ErpCharacteristicValueModel) iter.next();
            if (erpCharVal.getName().equals(charValueName))
                return erpCharVal;
        }
        return null;
    }

	/**
	 * Get number of characteristic values.
	 * 
	 * @return number of characteristic values.
	 */
	public int numberOfCharacteristicValues() {
		return this.characteristicValues.size();
	}

	/**
	 * Get a copy of the characteristic, with certain characteristic values hidden.
	 *
	 * @param hiddenCharValuePKs array of characteristic value PKs to hide
	 *
	 * @return a filtered copy
	 */
	public ErpCharacteristicModel getFilteredClone(PrimaryKey[] hiddenCharValuePKs) {
		// shallow copy
		LocalObjectList list = (LocalObjectList) this.characteristicValues.clone();
		// filter
		for (int i=0; i<hiddenCharValuePKs.length; i++) {
			list.removeByPK( hiddenCharValuePKs[i] );
		}
		// create clone
		ErpCharacteristicModel filteredChar = new ErpCharacteristicModel( this.getName(), list );
		filteredChar.setAttributesKey(this.getAttributesKey());
		return filteredChar;
	}


	/**
	 * Get the durable (long-lived) ID for the business object.
	 * This is the characteristic's name.
	 *
	 * @return durable ID
	 */
	public String getDurableId() {
		return this.getName();
	}

	/**
	 * Template method to visit the children of this ErpModel.
	 * It should call accept(visitor) on these (or do nothing).
	 *
	 * @param visitor visitor instance to pass around
	 */
	public void visitChildren(ErpVisitorI visitor) {
        for (Iterator i = this.characteristicValues.iterator(); i.hasNext(); ) {
            ((ErpCharacteristicValueModel) i.next()).accept( visitor );
		}
	}
	

}
