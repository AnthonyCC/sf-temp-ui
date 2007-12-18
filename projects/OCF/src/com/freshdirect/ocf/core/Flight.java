/**
 * @author ekracoff
 * Created on Apr 15, 2005*/

package com.freshdirect.ocf.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Flight extends Entity {
	private Campaign campaign;
	private String name;
	private DbListGenerator listGenerator;
	private List actions;
	private String scheduleName;
	private boolean multipleContact = false;

	public Flight() {
	}

	public Flight(DbListGenerator listGenerator, List activities) {
		this.listGenerator = listGenerator;
		this.actions = activities;
	}
	
	public Campaign getCampaign() {
		return campaign;
	}
	
	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getActions() {
		return actions;
	}

	public void setActions(List activities) {
		this.actions = activities;
	}

	public DbListGenerator getListGenerator() {
		return listGenerator;
	}

	public void setListGenerator(DbListGenerator customerList) {
		this.listGenerator = customerList;
	}
	
	public String getScheduleName() {
		return scheduleName;
	}
	
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public boolean isMultipleContact() {
		return multipleContact;
	}
	
	public void setMultipleContact(boolean multipleContact) {
		this.multipleContact = multipleContact;
	}
	
	public void addAction(ActionI action) {
		if(this.actions == null){
			actions = new ArrayList();
		}
		this.actions.add(action);
		action.setFlight(this);
	}

	public void removeAction(ActionI action) {
		for(Iterator i = this.actions.iterator(); i.hasNext();){
			ActionI act = (ActionI) i.next();
			if(act.equals(action)) {
				i.remove();
			}
		}
	}

}