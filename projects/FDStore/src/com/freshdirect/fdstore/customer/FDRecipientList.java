package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.giftcard.RecipientModel;

public class FDRecipientList extends ModelSupport {
	
	private static final long	serialVersionUID	= -377479613083237360L;
	
	//Holds a list of gift card recipients
	private List<RecipientModel> recipients = new ArrayList<RecipientModel>();

	public FDRecipientList() {		
	}
	
	public FDRecipientList(Collection<? extends RecipientModel> recipients){
		this.recipients.addAll(recipients);
	}
	
	public List<RecipientModel> getRecipients() {
		return recipients;
	}

	public void addRecipients(Collection<RecipientModel> recipients) {
		this.recipients.addAll(recipients);
	}
	
	public void addRecipient(RecipientModel rm) {
		this.recipients.add(rm);
	}
	
	public int getRecipientIndex(int randomId) {
		int c = 0;
		for ( RecipientModel model : recipients ) {
			if (randomId == model.getRandomId()) {
				return c;
			}
			c++;
		}
		return -1;
	}

	public RecipientModel getRecipient(int index) {
		return this.recipients.get(index);
	}
	
	public RecipientModel getRecipientById(String randomId) {
		int idx = -1;
		try{
			idx = this.getRecipientIndex(Integer.parseInt(randomId));
		}catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(nfe);
		}
		return idx == -1 ? null : this.getRecipient(idx);
	}

	public void setRecipient(int index, RecipientModel srm) {
		this.recipients.set(index, srm);
	}

	public void removeRecipient(int index) {
		this.recipients.remove(index);
	}

	public boolean removeOrderLineById(int randomId) {
		int idx = this.getRecipientIndex(randomId);
		if (idx == -1) {
			return false;
		}
		this.removeRecipient(idx);
		return true;
	}
	
	public double getSubtotal() {
		double subtotal = 0;
		for( RecipientModel model : recipients ) {
			subtotal += model.getAmount();
		}
		return subtotal;
	}
	
	public String getFormattedSubTotal() {
		return FormatterUtil.formatToTwoDecimal(getSubtotal());
	}
	
	public int size(){
		return this.recipients.size();
	}
	
	public void clear(){
		this.recipients.clear();
	}
}
