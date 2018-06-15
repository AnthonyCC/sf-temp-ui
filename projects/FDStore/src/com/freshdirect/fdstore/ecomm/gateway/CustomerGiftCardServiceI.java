package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;

public interface CustomerGiftCardServiceI {

	public ErpGCDlvInformationHolder getRecipientDlvInfo(FDIdentity identity, String saleId, String certificationNum)
			throws FDResourceException, RemoteException;

	public boolean resendEmail(String saleId, String certificationNum, String resendEmailId, String recipName,
			String personMsg, EnumTransactionSource source) throws FDResourceException, RemoteException;

	public ErpGiftCardModel applyGiftCard(FDIdentity identity, String givexNum, FDActionInfo info)
			throws ServiceUnavailableException, InvalidCardException, CardInUseException, CardOnHoldException,
			FDResourceException, RemoteException;

}
