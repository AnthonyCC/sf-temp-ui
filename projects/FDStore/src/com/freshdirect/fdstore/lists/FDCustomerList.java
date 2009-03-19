package com.freshdirect.fdstore.lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public abstract class FDCustomerList extends ModelSupport {
	
	private PrimaryKey customerPk;

	private String name;
	
	private Date createDate;
	
	/** The timestamp of the last modification of the list. */
	private Date modificationDate;
	
	// List<FDCustomerListItem>
	private List lineItems = new ArrayList();

	public void setCustomerPk(PrimaryKey customerPk) {
		markAsModified();
		this.customerPk = customerPk;
	}

	public PrimaryKey getCustomerPk() {
		return customerPk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markAsModified();
		this.name = name;
	}
	
	/**
	 * @return List<FDCustomerListItem>
	 */
	public List getLineItems() {
		return lineItems;
	}

	/**
	 * @param lineItems List of FDCustomerListLineItem
	 * @throws FDResourceException 
	 */
	public void setLineItems(List lineItems) {
		markAsModified();
		this.lineItems = lineItems;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		markAsModified();
		this.createDate = createDate;
	}

	/**
	 *  Get the last modification timestamp for the list.
	 *  
	 *  @return the time of when the list was last modified.
	 */
	public Date getModificationDate() {
		return modificationDate;
	}
	
	/**
	 *  Set the timestamp for the last modification of the list.
	 *  
	 *  @param modificationDate the timestamp of the last modification of the
	 *         list.
	 */
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	
	/**
	 *  Return the type of list this implementation handles.
	 *  
	 *  @return the type of list this implementation handles.
	 */
	public abstract EnumCustomerListType getType();
	
	/**
	 *  Mark the list as modified. This will update the last modification date,
	 *  for example.
	 */
	protected void markAsModified() {
		setModificationDate(new Date());
	}
	
	/**
	 *  Reverse a previous marking of this list as modified.
	 *  Resets the last modification date to the supplied date.
	 *  
	 *  @param modificationDate the old modification date, that was valid before
	 *         the call to markAsModified(). it is the responsibility of the
	 *         caller to store this date if he wants to unmark a list.
	 */
	protected void unmarkAsModified(Date modificationDate) {
		setModificationDate(modificationDate);
	}
}