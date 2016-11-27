package com.freshdirect.customer;

import java.rmi.RemoteException;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;



/**
 * @author Aniwesh
 *
 */
public class ErpEWalletModel extends ModelSupport{

	private static final long serialVersionUID = -7861663963588991563L;

	private String eWalletType;
	private String eWalletStatus;
	private String eWalletVerify;
	private String ewalletmStatus;
	
	/**
	 * @return
	 * @throws RemoteException
	 */
	public List<ErpEWalletModel> getEWallets() throws RemoteException {
		return null;
	}

	/**
	 * @param primaryKey
	 * @return
	 * @throws RemoteException
	 */
	public ErpEWalletModel getEWallet(PrimaryKey primaryKey) throws RemoteException {
		return null;
	}

	/**
	 * @return the eWalletType
	 */
	public String geteWalletType() {
		return eWalletType;
	}

	/**
	 * @param eWalletType the eWalletType to set
	 */
	public void seteWalletType(String eWalletType) {
		this.eWalletType = eWalletType;
	}

	/**
	 * @return the eWalletStatus
	 */
	public String geteWalletStatus() {
		return eWalletStatus;
	}

	/**
	 * @param eWalletStatus the eWalletStatus to set
	 */
	public void seteWalletStatus(String eWalletStatus) {
		this.eWalletStatus = eWalletStatus;
	}

	/**
	 * @return the eWalletVerify
	 */
	public String geteWalletVerify() {
		return eWalletVerify;
	}

	/**
	 * @param eWalletVerify the eWalletVerify to set
	 */
	public void seteWalletVerify(String eWalletVerify) {
		this.eWalletVerify = eWalletVerify;
	}

	/**
	 * @return the ewalletmStatus
	 */
	public String getEwalletmStatus() {
		return ewalletmStatus;
	}

	/**
	 * @param ewalletmStatus the ewalletmStatus to set
	 */
	public void setEwalletmStatus(String ewalletmStatus) {
		this.ewalletmStatus = ewalletmStatus;
	}
	
}
