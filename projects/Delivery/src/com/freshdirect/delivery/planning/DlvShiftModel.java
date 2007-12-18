package com.freshdirect.delivery.planning;

import com.freshdirect.delivery.EnumDayCode;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

public class DlvShiftModel extends DlvAbstractShiftModel {
    
    private EnumDayCode dayOfWeek;

    public DlvShiftModel (){
        super();
    }
    
    public DlvShiftModel(PrimaryKey pk){
    	this.setPK(pk);
    }
 
    public EnumDayCode getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(EnumDayCode dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

	public ModelI deepCopy() {
		DlvShiftModel c = new DlvShiftModel();
		super.decorateCopy(c);
		c.setDayOfWeek(dayOfWeek);
		return c;
	}
}
