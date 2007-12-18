/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.content.nutrition.*;

/**
 * Lightweight class representing a SKU code / version pair.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDKosherInfo implements Serializable {

	private EnumKosherSymbolValue   kSymbol;
    private EnumKosherTypeValue     kType;
    private boolean                 kProduction;
    private int                     kPriority;
    
    public FDKosherInfo() {
        super();
    }

	public FDKosherInfo(EnumKosherSymbolValue kSymbol, EnumKosherTypeValue kType, boolean kProduction, int kPriority) {
		this.kSymbol = kSymbol;
		this.kType = kType;
        this.kProduction = kProduction;
        this.kPriority = kPriority;
	}
	
	public EnumKosherSymbolValue getKosherSymbol() {
		return this.kSymbol;
	}
    
    public EnumKosherTypeValue getKosherType() {
        return this.kType;
    }
    
    public boolean isKosherProduction() {
        return this.kProduction;
    }
    
    public boolean hasKosherSymbol() {
        return this.kSymbol != null;
    }
    
    public boolean hasKosherType() {
        return this. kType != null;
    }

    public int getPriority() {
        return this.kPriority;
    }

}