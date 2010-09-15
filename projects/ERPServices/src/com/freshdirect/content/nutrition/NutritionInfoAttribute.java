/*
 * NutritionAttribute.java
 *
 * Created on June 8, 2003, 7:11 PM
 */

package com.freshdirect.content.nutrition;

import java.io.Serializable;

/**
 *
 * @author  mrose
 * @version 
 */
public class NutritionInfoAttribute implements Serializable {

    private ErpNutritionInfoType type;
    private int priority;
    private Object value;
    
    public NutritionInfoAttribute(ErpNutritionInfoType type, Serializable value) {
        this.type=type;
        this.value=value;
        this.priority=-1;
    }
    
    /** Creates new NutritionAttribute */
    public NutritionInfoAttribute(ErpNutritionInfoType type, int priority, Object value) {
        this.type=type;
        this.value=value;
        this.priority=priority;
    }
    
    public ErpNutritionInfoType getNutritionInfoType() {
        return this.type;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public Object getValue() {
        return this.value;
    }

}
