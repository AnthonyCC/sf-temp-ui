package com.freshdirect.giftcard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.TextEncryptor;
import com.freshdirect.payment.EnumGiftCardTransactionType;
import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpGiftCardUtil {

	public static String getCertificateNumber(String givexNum) {
		if(givexNum ==  null || givexNum.length() < 16){
			throw new IllegalArgumentException("Invalid Givex Number :"+givexNum);
		}
		return givexNum.substring(11, givexNum.length()-1);
	}
	
	public static String encryptGivexNum(String givexNum) {
		if(givexNum ==  null || givexNum.length() < 16){
			throw new IllegalArgumentException("Invalid Givex Number");
		}
		return TextEncryptor.encrypt(ErpServicesProperties.getGivexNumEncryptionKey(), givexNum);
	}
	
	
	public static String decryptGivexNum(String givexNum) {
		if(givexNum ==  null || givexNum.length() < 16){
			throw new IllegalArgumentException("Invalid Givex Number");
		}
		return TextEncryptor.decrypt(ErpServicesProperties.getGivexNumEncryptionKey(), givexNum);
	}
	
	public static double getAppliedAmount(String certificateNum, List appliedGiftCards) {
		double appliedAmount = 0.0;
		for(Iterator it = appliedGiftCards.iterator(); it.hasNext();) {
			ErpAppliedGiftCardModel model = (ErpAppliedGiftCardModel) it.next();
			if(model.getCertificateNum().equals(certificateNum)) {
				appliedAmount += model.getAmount();
			}
		}
		return appliedAmount;
	}
	
	public static List getGiftcardPaymentMethods(List appliedGiftCards) {
		Map pmMap = new HashMap();
		System.out.println("Applied gift cards "+appliedGiftCards.size());
		for(Iterator it = appliedGiftCards.iterator(); it.hasNext();) {
			ErpAppliedGiftCardModel agcmodel = (ErpAppliedGiftCardModel) it.next();
			String certNum = agcmodel.getCertificateNum();
			if(!pmMap.containsKey(certNum)){
				//Create a new Gift card model
				ErpGiftCardModel newgcModel = createGiftCardModel(agcmodel);
				pmMap.put(certNum, newgcModel);
			} 
			ErpGiftCardModel gcModel = (ErpGiftCardModel)pmMap.get(certNum);
			double balance = gcModel.getBalance();
			balance += agcmodel.getAmount();
			gcModel.setBalance(balance);
		}
		return new ArrayList(pmMap.values());
	}
	
	public static ErpGiftCardModel createGiftCardModel(ErpAppliedGiftCardModel agcModel) {
		ErpGiftCardModel gcModel = new ErpGiftCardModel();
		gcModel.setAccountNumber(agcModel.getAccountNumber());
		gcModel.setCertificateNumber(agcModel.getCertificateNum());
		return gcModel;
	}
	
	public static List checkForBadGiftcards(List verifiedList){
		List badGiftCards = new ArrayList();
		for(Iterator it = verifiedList.iterator(); it.hasNext();) {
			ErpGiftCardModel gc = (ErpGiftCardModel)it.next();
			if(!gc.isRedeemable()){
				badGiftCards.add(gc);
			}
		}
		return badGiftCards;
			
	}
	public static double getAuthorizedAmount(List auths) {
		double authAmount = 0.0;
		for(Iterator it = auths.iterator(); it.hasNext();) {
			ErpPreAuthGiftCardModel model = (ErpPreAuthGiftCardModel) it.next();
			authAmount += model.getAmount();
		}
		return authAmount;
	}
}
