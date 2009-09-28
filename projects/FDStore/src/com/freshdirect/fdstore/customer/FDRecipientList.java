package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.giftcard.RecipientModel;

public class FDRecipientList extends ModelSupport {
	
    //Holds a list of gift card recipients
	private List recipents = new ArrayList();

	public FDRecipientList(){
		
	}
	
	public FDRecipientList(Collection recipients){
		this.recipents.addAll(recipients);
	}
	
	public List getRecipents() {
		return recipents;
	}

	public void addRecipients(Collection recipents) {
		this.recipents.addAll(recipents);
	}
	
	public void addRecipient(RecipientModel rm) {
		this.recipents.add(rm);
	}
	
	public int getRecipientIndex(int randomId) {
		int c = 0;
		for (Iterator i = this.recipents.iterator(); i.hasNext(); c++) {
			if (randomId == ((RecipientModel) i.next()).getRandomId()) {
				return c;
			}
		}
		return -1;
	}

	public RecipientModel getRecipient(int index) {
		return (RecipientModel) this.recipents.get(index);
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
		this.recipents.set(index, srm);
	}

	public void removeRecipient(int index) {
		this.recipents.remove(index);
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
		for(Iterator it = this.recipents.iterator(); it.hasNext();){
			RecipientModel model = (RecipientModel)it.next();
			subtotal += model.getAmount();
		}
		return subtotal;
	}
	public String getFormattedSubTotal() {
		return FormatterUtil.formatToTwoDecimal(getSubtotal());
	}
	
	public int size(){
		return this.recipents.size();
	}

	
	public void clear(){
		this.recipents.clear();
	}
}
