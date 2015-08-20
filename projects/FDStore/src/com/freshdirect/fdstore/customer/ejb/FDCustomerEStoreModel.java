package com.freshdirect.fdstore.customer.ejb;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.core.ModelSupport;

/**
 * 
 * @author ksriram
 * Customer's store specific default delivery address, payment method and depot location info.
 */
public class FDCustomerEStoreModel extends ModelSupport{

	private static final long serialVersionUID = -8890913479519201820L;
	
	private String fdCustomerPk;
	private String defaultShipToAddressPK;
	private String defaultPaymentMethodPK;
	private String defaultDepotLocationPK;
	private EnumEStoreId eStoreId;
	
	/**
	 * @return the fdCustomerPk
	 */
	public String getFdCustomerPk() {
		return fdCustomerPk;
	}
	/**
	 * @param fdCustomerPk the fdCustomerPk to set
	 */
	public void setFdCustomerPk(String fdCustomerPk) {
		this.fdCustomerPk = fdCustomerPk;
	}
	/**
	 * @return the defaultShipToAddressPK
	 */
	public String getDefaultShipToAddressPK() {
		return defaultShipToAddressPK;
	}
	/**
	 * @param defaultShipToAddressPK the defaultShipToAddressPK to set
	 */
	public void setDefaultShipToAddressPK(String defaultShipToAddressPK) {
		this.defaultShipToAddressPK = defaultShipToAddressPK;
	}
	/**
	 * @return the defaultPaymentMethodPK
	 */
	public String getDefaultPaymentMethodPK() {
		return defaultPaymentMethodPK;
	}
	/**
	 * @param defaultPaymentMethodPK the defaultPaymentMethodPK to set
	 */
	public void setDefaultPaymentMethodPK(String defaultPaymentMethodPK) {
		this.defaultPaymentMethodPK = defaultPaymentMethodPK;
	}
	/**
	 * @return the defaultDepotLocationPK
	 */
	public String getDefaultDepotLocationPK() {
		return defaultDepotLocationPK;
	}
	/**
	 * @param defaultDepotLocationPK the defaultDepotLocationPK to set
	 */
	public void setDefaultDepotLocationPK(String defaultDepotLocationPK) {
		this.defaultDepotLocationPK = defaultDepotLocationPK;
	}
	/**
	 * @return the eStoreId
	 */
	public EnumEStoreId geteStoreId() {
		return eStoreId;
	}
	/**
	 * @param eStoreId the eStoreId to set
	 */
	public void seteStoreId(EnumEStoreId eStoreId) {
		this.eStoreId = eStoreId;
	}
	
	

}
