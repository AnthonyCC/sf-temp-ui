package com.freshdirect.delivery.planning;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class DlvPlan extends ModelSupport {

    private String name;
    
    public DlvPlan(){
        super();
    }
    
    public DlvPlan(PrimaryKey pk){
    	this.setPK(pk);
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}
