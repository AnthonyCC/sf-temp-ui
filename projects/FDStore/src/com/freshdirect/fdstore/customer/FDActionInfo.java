package com.freshdirect.fdstore.customer;

import java.io.Serializable;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;

public class FDActionInfo implements Serializable {

	private static final long	serialVersionUID	= 5436805819502477394L;
	
	private EnumTransactionSource source;
	private FDIdentity identity;
	private String initiator;
	private String note;
	private CrmAgentModel agent;
    private boolean isPR1;
    
	public FDActionInfo(EnumTransactionSource source, FDIdentity identity, String initiator, String note, CrmAgentModel agent) {
		this.identity = identity;
		this.source = source;
		this.initiator = initiator;
		this.note = note;
		this.agent = agent;
	}

	public FDIdentity getIdentity() {
		return identity;
	}
	
	public void setIdentity(FDIdentity identity) {
		this.identity = identity;
	}

	public EnumTransactionSource getSource() {
		return source;
	}

	public void setSource(EnumTransactionSource source) {
		this.source = source;
	}

	public String getInitiator() {
		return initiator;
	}
	
	public CrmAgentModel getAgent() {
		return agent;
	}
	
	public void setAgent(CrmAgentModel agent) {
		this.agent = agent;
	}

	public String getNote() {
		return note;
	}
	
	public void setNote(String note){
		this.note = note;
	}

	public ErpActivityRecord createActivity(EnumAccountActivityType type) {
		return this.createActivity(type, null);
	}

	public ErpActivityRecord createActivity(EnumAccountActivityType type, String note) {
		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setActivityType(type);

		rec.setSource(source);
		rec.setInitiator(initiator);
		rec.setCustomerId(identity.getErpCustomerPK());

		StringBuffer sb = new StringBuffer();
		if (note != null) {
			sb.append(note);
		}
		if (this.note != null) {
			sb.append(this.note);
		}
		rec.setNote(sb.toString());

		return rec;
	}

	public boolean isPR1() {
		return isPR1;
	}

	public void setPR1(boolean isPR1) {
		this.isPR1 = isPR1;
	}

}
