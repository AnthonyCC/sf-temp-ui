package com.freshdirect.fdstore.promotion;

import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class PromotionDlvDate extends ModelSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8904166992937432121L;
	private Date dlvDateStart;
	private Date dlvDateEnd;
	
	public PromotionDlvDate(PrimaryKey pk, Date dlvDateStart, Date dlvDateEnd) {
		this.setPK(pk);
		this.dlvDateStart = dlvDateStart;
		this.dlvDateEnd = dlvDateEnd;
	}

	public PromotionDlvDate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getDlvDateStart() {
		return dlvDateStart;
	}

	public void setDlvDateStart(Date dlvDateStart) {
		this.dlvDateStart = dlvDateStart;
	}

	public Date getDlvDateEnd() {
		return dlvDateEnd;
	}

	public void setDlvDateEnd(Date dlvDateEnd) {
		this.dlvDateEnd = dlvDateEnd;
	}
}
