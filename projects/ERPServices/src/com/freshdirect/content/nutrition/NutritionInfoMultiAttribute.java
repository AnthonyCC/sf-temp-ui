/*
 * NutritionInfoMultiAttribute.java
 *
 * Created on June 8, 2003, 7:26 PM
 */

package com.freshdirect.content.nutrition;

import java.util.*;

/**
 *
 * @author  mrose
 * @version 
 */
public class NutritionInfoMultiAttribute extends NutritionInfoAttribute {
    
    public NutritionInfoMultiAttribute(ErpNutritionInfoType type) {
        super(type, new ArrayList());
    }
    
    private List getValueList() {
        return (List) this.getValue();
    }
    
    public int numberOfValues() {
        return this.getValueList().size();
    }
    
	public Object getValue(int idx) {
		return this.getValueList().get(idx);
	}

	public void addValue(NutritionInfoAttribute attr) {
		this.getValueList().add(attr);
	}
    
    public List getValues() {
        List retval = new ArrayList();
        for (Iterator iter = this.getValueList().iterator(); iter.hasNext(); ) {
            retval.add(((NutritionInfoAttribute)iter.next()).getValue());
        }
        return retval;
    }
	
    public String toString() {
        return "NutritionInfoMultiAttribute[" + this.getNutritionInfoType().getName() + " - " + this.numberOfValues() + " values ]";
    }

}
