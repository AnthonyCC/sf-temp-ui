package com.freshdirect.ecomm.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.BasicContactAddressI;
import com.freshdirect.common.address.ContactAddressAdapter;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.EnumWebServiceType;
import com.freshdirect.common.pricing.CatalogKey;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.common.pricing.ZoneInfo.PricingIndicator;
import com.freshdirect.customer.EnumATCContext;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpCreditModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpECheckModel;
import com.freshdirect.customer.ErpEbtCardModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPayPalCardModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.adapter.CustomerAdapter;
import com.freshdirect.customer.adapter.PaymentMethodAdapter;
import com.freshdirect.customer.adapter.SapOrderAdapter;
import com.freshdirect.ecommerce.data.dlv.AddressData;
import com.freshdirect.ecommerce.data.dlv.AddressInfoData;
import com.freshdirect.ecommerce.data.dlv.ContactAddressData;
import com.freshdirect.ecommerce.data.dlv.ErpAddressData;
import com.freshdirect.ecommerce.data.dlv.PhoneNumberData;
import com.freshdirect.ecommerce.data.ecoupon.DiscountData;
import com.freshdirect.ecommerce.data.ecoupon.ErpCouponDiscountLineModelData;
import com.freshdirect.ecommerce.data.ecoupon.ErpCouponTransactionDetailModelData;
import com.freshdirect.ecommerce.data.ecoupon.ErpCouponTransactionModelData;
import com.freshdirect.ecommerce.data.ecoupon.ErpOrderLineModelData;
import com.freshdirect.ecommerce.data.ecoupon.FDConfigurationData;
import com.freshdirect.ecommerce.data.enums.ErpAffiliateData;
import com.freshdirect.ecommerce.data.erp.pricing.ZoneInfoData;
import com.freshdirect.ecommerce.data.fdstore.FDGroupData;
import com.freshdirect.ecommerce.data.fdstore.FDSkuData;
import com.freshdirect.ecommerce.data.list.UserContextData;
import com.freshdirect.ecommerce.data.payment.ErpPaymentMethodData;
import com.freshdirect.ecommerce.data.sap.BasicContactAddressData;
import com.freshdirect.ecommerce.data.sap.CatalogKeyData;
import com.freshdirect.ecommerce.data.sap.ChangeSalesOrderData;
import com.freshdirect.ecommerce.data.sap.ContactAddressAdapterData;
import com.freshdirect.ecommerce.data.sap.ErpAbstractOrderModelData;
import com.freshdirect.ecommerce.data.sap.ErpAppliedCreditData;
import com.freshdirect.ecommerce.data.sap.ErpAppliedGiftCardData;
import com.freshdirect.ecommerce.data.sap.ErpChargeLineData;
import com.freshdirect.ecommerce.data.sap.ErpCustomerData;
import com.freshdirect.ecommerce.data.sap.ErpCustomerInfoData;
import com.freshdirect.ecommerce.data.sap.ErpDeliveryInfoData;
import com.freshdirect.ecommerce.data.sap.ErpDeliveryPlantInfoData;
import com.freshdirect.ecommerce.data.sap.ErpDiscountLineData;
import com.freshdirect.ecommerce.data.sap.ErpGiftCardData;
import com.freshdirect.ecommerce.data.sap.ErpInvoiceData;
import com.freshdirect.ecommerce.data.sap.ErpInvoiceLineData;
import com.freshdirect.ecommerce.data.sap.ErpRecipentData;
import com.freshdirect.ecommerce.data.sap.FDRafTransData;
import com.freshdirect.ecommerce.data.sap.SapCustomerData;
import com.freshdirect.ecommerce.data.sap.SapOrderData;
import com.freshdirect.ecommerce.data.sap.SapPostReturnCommandData;
import com.freshdirect.ecommerce.data.survey.FDIdentityData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.ecoupon.EnumCouponOfferType;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionDetailModel;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.EnumGiftCardStatus;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.referral.extole.EnumRafTransactionStatus;
import com.freshdirect.referral.extole.EnumRafTransactionType;
import com.freshdirect.referral.extole.model.FDRafTransModel;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sms.EnumSMSAlertStatus;

public class SapGatewayConverter {

	public static SapCustomerData buildSapCustomerData(SapCustomerI customer) {
		SapCustomerData sapCustomerData = new SapCustomerData();
		CustomerAdapter customerAdapter = (CustomerAdapter) customer;
		sapCustomerData.setAlternateAddress(buildBasiccontactAdressData(customerAdapter.getAlternateAddress()));
		sapCustomerData.setBillToAddress(buildBasiccontactAdressData(customerAdapter.getBillToAddress()));
		sapCustomerData.setGoGreen(customerAdapter.isGoGreen());
		PaymentMethodAdapter adapter = (PaymentMethodAdapter) customerAdapter.getPaymentMethod();
		sapCustomerData.setPaymentMethod(buildPaymentMethodModel(adapter));
		sapCustomerData.setSapCustomerNumber(customerAdapter.getSapCustomerNumber());
		sapCustomerData.setShipToAddress(buildBasiccontactAdressData(customerAdapter.getShipToAddress()));
		return sapCustomerData;
	}

	



	public  static ErpPaymentMethodData buildPaymentMethodModel(PaymentMethodAdapter adapter) {
		ErpPaymentMethodData erpPayment = new ErpPaymentMethodData();
		erpPayment = buildPaymentMethodData(adapter.getErpPaymentMethod());
		erpPayment.setAbaRouteNumber(adapter.getAbaRouteNumber());
		if(adapter.getBankAccountType() != null)
		erpPayment.setBankAccountType(adapter.getBankAccountType().getName());
		erpPayment.setBankName(adapter.getBankName());
		erpPayment.setExpirationDate(adapter.getExpiration().getTime());
		erpPayment.setName(adapter.getName());
		erpPayment.setAccountNumber(adapter.getNumber());
		if(adapter.getType() != null)
		erpPayment.setCardType(adapter.getType().getName());
		return erpPayment;
	}





	private static BasicContactAddressData buildBasiccontactAdressData(
			BasicContactAddressI alternateAddress) {
		BasicContactAddressData addressData = new BasicContactAddressData();
		if(alternateAddress instanceof ContactAddressAdapter){
			ContactAddressAdapterData addressAdapter = new ContactAddressAdapterData();
			addressAdapter.setAddress(buildAddressData(alternateAddress));
			addressAdapter.setCustomerId(alternateAddress.getCustomerId());
			addressAdapter.setFirstName(alternateAddress.getFirstName());
			addressAdapter.setLastName(alternateAddress.getLastName());
			addressAdapter.setPhone(buildPhoneData(alternateAddress.getPhone()));
			addressData.setContactAddressAdapter(addressAdapter);
			addressData.setReturnType(ContactAddressAdapter.class.getSimpleName());
		}else if(alternateAddress instanceof ContactAddressModel){
			ContactAddressData contactAddressData =  new ContactAddressData();
			ContactAddressModel addressModel = (ContactAddressModel) alternateAddress;
			contactAddressData.setAddress1(addressModel.getAddress1());
			contactAddressData.setAddress2(addressModel.getAddress2());
			contactAddressData.setAddressInfoData(buildAddressInfoData(addressModel.getAddressInfo()));
			contactAddressData.setApartment(addressModel.getApartment());
			contactAddressData.setCity(addressModel.getCity());
			contactAddressData.setCompanyName(addressModel.getCompanyName());
			contactAddressData.setCountry(addressModel.getCountry());
			if(addressModel.getServiceType() != null)
			contactAddressData.setServiceType(addressModel.getServiceType().getName());
			contactAddressData.setState(addressModel.getState());
			contactAddressData.setZipCode(addressModel.getZipCode());
			contactAddressData.setFirstName(addressModel.getFirstName());
			contactAddressData.setLastName(addressModel.getLastName());
			contactAddressData.setPhone(buildPhoneData(addressModel.getPhone()));
			contactAddressData.setCustomerId(addressModel.getCustomerId());
			addressData.setReturnType(ContactAddressModel.class.getSimpleName());
		}
		return addressData;
	}

	private static PhoneNumberData buildPhoneData(PhoneNumber phone) {
		PhoneNumberData phoneNumber = null;
		if(phone != null)
		phoneNumber = new PhoneNumberData(phone.getPhone(), phone.getExtension(), phone.getType());
		return phoneNumber;
	}

	private static AddressData buildAddressData(BasicContactAddressI basicContactAddress) {
		AddressData addressData = new AddressData();
		ContactAddressAdapter contactAddressAdapter = (ContactAddressAdapter) basicContactAddress;
		addressData.setAddress1(contactAddressAdapter.getAddress1());
		addressData.setAddress2(contactAddressAdapter.getAddress2());
		addressData.setAddressInfoData(buildAddressInfoData(contactAddressAdapter.getAddressInfo()));
		addressData.setApartment(contactAddressAdapter.getApartment());
		addressData.setCity(contactAddressAdapter.getCity());
		addressData.setCountry(contactAddressAdapter.getCountry());
		addressData.setState(basicContactAddress.getState());
		addressData.setZipCode(basicContactAddress.getZipCode());
		return addressData;
		
	}
	

	private static AddressInfoData buildAddressInfoData(AddressInfo addressInfo) {
		AddressInfoData addressInfoData = null;
		if(addressInfo != null){
		addressInfoData = new AddressInfoData();
		addressInfoData.setZoneId(addressInfo.getZoneId());
		addressInfoData.setZoneCode(addressInfo.getZoneCode());
		addressInfoData.setLongitude(addressInfo.getLongitude());
		addressInfoData.setLatitude(addressInfo.getLatitude());
		addressInfoData.setScrubbedStreet(addressInfo.getScrubbedStreet());
		if(addressInfo.getAddressType() != null)
		addressInfoData.setAddressType(addressInfo.getAddressType().getName());
		addressInfoData.setCounty(addressInfo.getCounty());
		addressInfoData.setGeocodeException(addressInfo.isGeocodeException());
		addressInfoData.setBuildingId(addressInfo.getBuildingId());
		addressInfoData.setLocationId(addressInfo.getLocationId());
		addressInfoData.setSsScrubbedAddress(addressInfo.getSsScrubbedAddress());
		}
		return addressInfoData;
	}

	public static SapPostReturnCommandData buildSapPostReturnCommandData(ErpAbstractOrderModel order, ErpInvoiceModel invoice, boolean cancelCoupons) {
		SapPostReturnCommandData sapPostReturnCommand = new SapPostReturnCommandData();
		sapPostReturnCommand.setCancelCoupons(cancelCoupons);
		sapPostReturnCommand.setInvoice(buildErpInvoiceData(invoice));
		sapPostReturnCommand.setOrder(buildOrderData(order));
		return sapPostReturnCommand;
	}

	private static ErpInvoiceData buildErpInvoiceData(ErpInvoiceModel invoice) {
		ErpInvoiceData invoiceData =  null;
		if(invoice != null){
			invoiceData = new ErpInvoiceData();
			invoiceData.setAmount(invoice.getAmount());
			invoiceData.setCouponTransModel(buildCouponTransData(invoice.getCouponTransModel()));
			invoiceData.setCustomerId(invoice.getCustomerId());
			invoiceData.setTransactionId(invoice.getId());
			invoiceData.setInvoiceLines(buildInvoiceLineList(invoice.getInvoiceLines()));
			invoiceData.setInvoiceNumber(invoice.getInvoiceNumber());
			invoiceData.setSubtotal(invoice.getActualSubTotal());
			invoiceData.setTax(invoice.getTax());
			invoiceData.setTransactionDate(invoice.getTransactionDate());
			invoiceData.setTransactionInitiator(invoice.getTransactionInitiator());
			if(invoice.getTransactionSource() != null)
			invoiceData.setTransactionSource(invoice.getTransactionSource().getName());
		}
		return invoiceData;
	}





	private static List<ErpInvoiceLineData> buildInvoiceLineList(List<ErpInvoiceLineModel> invoiceLines) {
		List<ErpInvoiceLineData> invoiceLineDataList = null;
		if(invoiceLines != null){
			invoiceLineDataList = new ArrayList<ErpInvoiceLineData>();
		for (ErpInvoiceLineModel erpInvoiceLineModel : invoiceLines) {
			ErpInvoiceLineData invoiceLineData = new ErpInvoiceLineData();
			invoiceLineData.setActualCost(erpInvoiceLineModel.getActualCost());
			invoiceLineData.setActualDiscountAmount(erpInvoiceLineModel.getActualDiscountAmount());
			invoiceLineData.setCouponDiscountAmount(erpInvoiceLineModel.getCouponDiscountAmount());
			invoiceLineData.setCustomizationPrice(erpInvoiceLineModel.getCustomizationPrice());
			invoiceLineData.setDepositValue(erpInvoiceLineModel.getDepositValue());
			invoiceLineData.setInvoiceLineId(erpInvoiceLineModel.getId());
			invoiceLineData.setMaterialNumber(erpInvoiceLineModel.getMaterialNumber());
			invoiceLineData.setOrderLineNumber(erpInvoiceLineModel.getOrderLineNumber());
			invoiceLineData.setPrice(erpInvoiceLineModel.getPrice());
			invoiceLineData.setQuantity(erpInvoiceLineModel.getQuantity());
			invoiceLineData.setTaxValue(erpInvoiceLineModel.getTaxValue());
			invoiceLineData.setWeight(erpInvoiceLineModel.getWeight());
			//invoiceLineData.setSubSkuStatus(erpInvoiceLineModel.getSubSkuStatus());
			//invoiceLineData.setSubstitutedSkuCode(erpInvoiceLineModel.getSubstitutedSkuCode());
			invoiceLineDataList.add(invoiceLineData);
		}
		}
		return invoiceLineDataList;
	}





	public  static ErpAbstractOrderModelData buildOrderData(ErpAbstractOrderModel abstractModel) {
		ErpAbstractOrderModelData abstractOrderModelData = new ErpAbstractOrderModelData();
		if(abstractModel != null){
		abstractOrderModelData.setTransactionSource(abstractModel.getTransactionSource().getCode());
		abstractOrderModelData.setOrderLines(buildOrderLineData(abstractModel.getOrderLines()));
		abstractOrderModelData.setRequestedDate(abstractModel.getRequestedDate());
		abstractOrderModelData.setDiscounts(buildDiscountData(abstractModel.getDiscounts()));
		abstractOrderModelData.setPricingDate(abstractModel.getPricingDate());
		abstractOrderModelData.setPaymentMethod(buildPaymentMethodData(abstractModel.getPaymentMethod()));
		abstractOrderModelData.setSubTotal(abstractModel.getSubTotal());
		abstractOrderModelData.setTax(abstractModel.getTax());
		abstractOrderModelData.setCustomerServiceMessage(abstractModel.getCustomerServiceMessage());
		abstractOrderModelData.setMarketingMessage(abstractModel.getMarketingMessage());
		abstractOrderModelData.setGlCode(abstractModel.getGlCode());
		if(abstractModel.getTransactionType() != null)
		abstractOrderModelData.setTransactionType(abstractModel.getTransactionType().getCode());
		if(abstractModel.getTaxationType() != null)
		abstractOrderModelData.setTaxationType(abstractModel.getTaxationType().getName());
		abstractOrderModelData.setDeliveryPassCount(abstractModel.getDeliveryPassCount());
		abstractOrderModelData.setDlvPassApplied(abstractModel.isDlvPassApplied());
		abstractOrderModelData.setDlvPromotionApplied(abstractModel.isDlvPromotionApplied());
		abstractOrderModelData.setBufferAmt(abstractModel.getBufferAmt());
		abstractOrderModelData.setAppliedCredits(buildAppliedCreditsData(abstractModel.getAppliedCredits()));
		abstractOrderModelData.setDeliveryInfo(buildDeliveryInfoData(abstractModel.getDeliveryInfo()));
		abstractOrderModelData.setCharges(buildChargeLineData(abstractModel.getCharges()));
		abstractOrderModelData.setDlvPassExtendDays(abstractModel.getDlvPassExtendDays());
		abstractOrderModelData.setCurrentDlvPassExtendDays(abstractModel.getCurrentDlvPassExtendDays());
		abstractOrderModelData.setCouponTransModel(buildCouponTransData(abstractModel.getCouponTransModel()));
		abstractOrderModelData.setRafTransModel(buildRefTransModelData(abstractModel.getRafTransModel()));
		if(abstractModel.geteStoreId() != null)
		abstractOrderModelData.seteStoreId(abstractModel.geteStoreId().getContentId());
		abstractOrderModelData.setSelectedGiftCards(buildSelectedGiftCardData(abstractModel.getSelectedGiftCards()));
		abstractOrderModelData.setAppliedGiftcards(buildAppliedGiftCardData(abstractModel.getAppliedGiftcards()));
		abstractOrderModelData.setRecipientsList(buildRecepientsListData(abstractModel.getRecipientsList()));
		abstractOrderModelData.setCustomerId(abstractModel.getCustomerId());
		}
		
		return  abstractOrderModelData;
	}





	public static ChangeSalesOrderData buildChangeSalesOrderData(
			String webOrderNumber, String sapOrderNumber, SapOrderI order, boolean isPlantChanged) {
		ChangeSalesOrderData changesalesOrderData = new ChangeSalesOrderData();
		changesalesOrderData.setOrder(buildSapOrderData(order));
		changesalesOrderData.setPlantChanged(isPlantChanged);
		changesalesOrderData.setSapOrderNumber(sapOrderNumber);
		changesalesOrderData.setWebOrderNumber(webOrderNumber);
		return changesalesOrderData;
	}

	public static SapOrderData buildSapOrderData(SapOrderI order) {
		SapOrderData sapOrderdata = new SapOrderData();
		ErpAbstractOrderModelData abstractOrderModelData = new ErpAbstractOrderModelData();
		
		if(order != null){
		SapOrderAdapter sapOrderAdapter = (SapOrderAdapter) order;
		ErpAbstractOrderModel abstractModel = sapOrderAdapter.getErpOrder();
		abstractOrderModelData.setOrderLines(buildOrderLineData(abstractModel.getOrderLines()));
		if(abstractModel.getTransactionType() != null)
		abstractOrderModelData.setCustomerId(abstractModel.getCustomerId());
		abstractOrderModelData.setTransactionType(abstractModel.getTransactionType().getName());
		abstractOrderModelData.setRequestedDate(abstractModel.getRequestedDate());
		abstractOrderModelData.setDiscounts(buildDiscountData(abstractModel.getDiscounts()));
		abstractOrderModelData.setPricingDate(abstractModel.getPricingDate());
		abstractOrderModelData.setPaymentMethod(buildPaymentMethodData(abstractModel.getPaymentMethod()));
		abstractOrderModelData.setSubTotal(abstractModel.getSubTotal());
		abstractOrderModelData.setTax(abstractModel.getTax());
		abstractOrderModelData.setCustomerServiceMessage(abstractModel.getCustomerServiceMessage());
		abstractOrderModelData.setMarketingMessage(abstractModel.getMarketingMessage());
		abstractOrderModelData.setGlCode(abstractModel.getGlCode());
		if(abstractModel.getTaxationType() != null)
		abstractOrderModelData.setTaxationType(abstractModel.getTaxationType().getName());
		abstractOrderModelData.setDeliveryPassCount(abstractModel.getDeliveryPassCount());
		abstractOrderModelData.setDlvPassApplied(abstractModel.isDlvPassApplied());
		abstractOrderModelData.setDlvPromotionApplied(abstractModel.isDlvPromotionApplied());
		abstractOrderModelData.setBufferAmt(abstractModel.getBufferAmt());
		abstractOrderModelData.setAppliedCredits(buildAppliedCreditsData(abstractModel.getAppliedCredits()));
		abstractOrderModelData.setDeliveryInfo(buildDeliveryInfoData(abstractModel.getDeliveryInfo()));
		abstractOrderModelData.setCharges(buildChargeLineData(abstractModel.getCharges()));
		abstractOrderModelData.setDlvPassExtendDays(abstractModel.getDlvPassExtendDays());
		abstractOrderModelData.setCurrentDlvPassExtendDays(abstractModel.getCurrentDlvPassExtendDays());
		abstractOrderModelData.setCouponTransModel(buildCouponTransData(abstractModel.getCouponTransModel()));
		abstractOrderModelData.setRafTransModel(buildRefTransModelData(abstractModel.getRafTransModel()));
		if(abstractModel.geteStoreId() != null)
		abstractOrderModelData.seteStoreId(abstractModel.geteStoreId().getContentId());
		abstractOrderModelData.setSelectedGiftCards(buildSelectedGiftCardData(abstractModel.getSelectedGiftCards()));
		abstractOrderModelData.setAppliedGiftcards(buildAppliedGiftCardData(abstractModel.getAppliedGiftcards()));
		abstractOrderModelData.setRecipientsList(buildRecepientsListData(abstractModel.getRecipientsList()));
		sapOrderdata.setErpOrder(abstractOrderModelData);
		}
		
		return  sapOrderdata;
	}
	
	private static List<ErpChargeLineData> buildChargeLineData(List<ErpChargeLineModel> charges) {
		List<ErpChargeLineData> erpChargeLineDataList = new ArrayList<ErpChargeLineData>();
		for (ErpChargeLineModel erpChargeLine : charges) {
			ErpChargeLineData chargeLineData = new ErpChargeLineData();
			chargeLineData.setAmount(erpChargeLine.getAmount());
			chargeLineData.setDiscount(buildDiscountData(erpChargeLine.getDiscount()));
			chargeLineData.setId(erpChargeLine.getId());
			chargeLineData.setReasonCode(erpChargeLine.getReasonCode());
			if(erpChargeLine.getTaxationType() != null)
			chargeLineData.setTaxationType(erpChargeLine.getTaxationType().getName());
			chargeLineData.setTaxRate(erpChargeLine.getTaxRate());
			if(erpChargeLine.getTaxationType() != null)
			chargeLineData.setType(erpChargeLine.getType().getName());
			erpChargeLineDataList.add(chargeLineData);
		}
		return erpChargeLineDataList;
	}
	

	private static ErpCouponTransactionModelData buildCouponTransData(ErpCouponTransactionModel couponTransModel) {
		ErpCouponTransactionModelData erpCouponTransactionData = null;
		if(couponTransModel != null){
			erpCouponTransactionData = new  ErpCouponTransactionModelData();
			erpCouponTransactionData.setCreateTime(couponTransModel.getCreateTime());
			erpCouponTransactionData.setErrorDetails(couponTransModel.getErrorDetails());
			erpCouponTransactionData.setErrorMessage(couponTransModel.getErrorMessage());
			erpCouponTransactionData.setId(couponTransModel.getId());
			erpCouponTransactionData.setSaleActionId(couponTransModel.getSaleActionId());
			erpCouponTransactionData.setSaleId(couponTransModel.getSaleId());
			List<ErpCouponTransactionDetailModelData> erpCouponTransactionDetailData = new ArrayList<ErpCouponTransactionDetailModelData>();
			if (couponTransModel.getTranDetails() != null) {
				for (ErpCouponTransactionDetailModel erpCouponTransactionModel: couponTransModel.getTranDetails()) {
					ErpCouponTransactionDetailModelData couponTransactionDetailModelData = new ErpCouponTransactionDetailModelData();
					couponTransactionDetailModelData.setCouponId(erpCouponTransactionModel.getCouponId());
					couponTransactionDetailModelData.setCouponLineId(erpCouponTransactionModel.getCouponLineId());
					couponTransactionDetailModelData.setCouponTransId(erpCouponTransactionModel.getCouponTransId());
					couponTransactionDetailModelData.setDiscountAmt(erpCouponTransactionModel.getDiscountAmt());
					couponTransactionDetailModelData.setId(erpCouponTransactionModel.getId());
					couponTransactionDetailModelData.setCouponTransId(erpCouponTransactionModel.getId());
					couponTransactionDetailModelData.setTransTime(erpCouponTransactionModel.getTransTime());
					erpCouponTransactionDetailData.add(couponTransactionDetailModelData);
				}
			}
			erpCouponTransactionData.setTranDetails(erpCouponTransactionDetailData);
			if(couponTransModel.getTranStatus() != null)
			erpCouponTransactionData.setTranStatus(couponTransModel.getTranStatus().getName());
			erpCouponTransactionData.setTranTime(couponTransModel.getTranTime());
			if(couponTransModel.getTranType() != null)
			erpCouponTransactionData.setTranType(couponTransModel.getTranType().getName());
		}
		return erpCouponTransactionData;
	
	}

	private static FDRafTransData buildRefTransModelData(FDRafTransModel rafTransModel) {
		FDRafTransData fdRafTransData = null;
		if(rafTransModel != null){
			fdRafTransData = new FDRafTransData();
			fdRafTransData.setCreateTime(rafTransModel.getCreateTime());
			fdRafTransData.setErrorDetails(rafTransModel.getErrorDetails());
			fdRafTransData.setErrorMessage(rafTransModel.getErrorMessage());
			fdRafTransData.setExtoleEventId(rafTransModel.getExtoleEventId());
			fdRafTransData.setId(rafTransModel.getId());
			fdRafTransData.setSalesActionId(rafTransModel.getSalesActionId());
			if(rafTransModel.getTransStatus() != null)
			fdRafTransData.setTransStatus(rafTransModel.getTransStatus().getValue());
			fdRafTransData.setTransTime(rafTransModel.getTransTime());
			if(rafTransModel.getTransType() != null)
			fdRafTransData.setTransType(rafTransModel.getTransType().toString());
		}
		return fdRafTransData;
	}
	private static List<ErpGiftCardData> buildSelectedGiftCardData(List<ErpGiftCardModel> selectedGiftCards) {
		List<ErpGiftCardData> erpGiftCardDataList = new ArrayList<ErpGiftCardData>();
		if(selectedGiftCards!=null){
			for (ErpGiftCardModel erpGiftCardModel : selectedGiftCards) {
				ErpGiftCardData erpGiftCardData = (ErpGiftCardData) buildPaymentMethodData(erpGiftCardModel);
				erpGiftCardData.setBalance(erpGiftCardData.getBalance());
				erpGiftCardData.setOriginalAmount(erpGiftCardData.getOriginalAmount());
				erpGiftCardData.setPurchaseSaleId(erpGiftCardData.getPurchaseSaleId());
				erpGiftCardData.setPurchaseDate(erpGiftCardData.getPurchaseDate());
				erpGiftCardDataList.add(erpGiftCardData);
			}
		}
		return erpGiftCardDataList;
	}

	private static List<ErpAppliedGiftCardData> buildAppliedGiftCardData(List<ErpAppliedGiftCardModel> appliedGiftcards) {
		List<ErpAppliedGiftCardData> giftCardListData = new ArrayList<ErpAppliedGiftCardData>();
		if(appliedGiftcards!=null){
			for (ErpAppliedGiftCardModel erpAppliedGiftCard : appliedGiftcards) {
				ErpAppliedGiftCardData erpAppliedGiftCardData = new ErpAppliedGiftCardData();
				erpAppliedGiftCardData.setAccountNumber(erpAppliedGiftCard.getAccountNumber());
				erpAppliedGiftCardData.setAffiliate(buildErpAffiliateData(erpAppliedGiftCard.getAffiliate()));
				erpAppliedGiftCardData.setAmount(erpAppliedGiftCard.getAmount());
				erpAppliedGiftCardData.setCertificateNum(erpAppliedGiftCard.getCertificateNum());
				erpAppliedGiftCardData.setId(erpAppliedGiftCard.getId());
				giftCardListData.add(erpAppliedGiftCardData);
			}
		}
		return giftCardListData;
	}
	private static List<ErpRecipentData> buildRecepientsListData(List<ErpRecipentModel> recipientsList) {
		 List<ErpRecipentData> erpRecipentDataList = new ArrayList<ErpRecipentData>();
		for (ErpRecipentModel erpRecipent : recipientsList) {
			ErpRecipentData erpRecipentData = new ErpRecipentData();
			erpRecipentData.setAmount(erpRecipent.getAmount());
			erpRecipentData.setCustomerId(erpRecipent.getCustomerId());
			if(erpRecipent.getDeliveryMode() != null)
			erpRecipentData.setDeliveryMode(erpRecipent.getDeliveryMode().getName());
			erpRecipentData.setDonorOrganizationName(erpRecipent.getDonorOrganizationName());
			if(erpRecipent!= null)
			erpRecipentData.setGiftCardType(erpRecipent.getGiftCardType().getName());
			erpRecipentData.setRecipeId(erpRecipent.getId());
			erpRecipentData.setOrderLineId(erpRecipent.getOrderLineId());
			erpRecipentData.setPersonalMessage(erpRecipent.getPersonalMessage());
			erpRecipentData.setRecipientEmail(erpRecipent.getRecipientEmail());
			erpRecipentData.setSale_id(erpRecipent.getSale_id());
			erpRecipentData.setSenderEmail(erpRecipent.getSenderEmail());
			erpRecipentData.setSenderName(erpRecipent.getSenderName());
			erpRecipentData.setTemplateId(erpRecipent.getTemplateId());
			erpRecipentData.setRecipientName(erpRecipent.getRecipientName());
			erpRecipentDataList.add(erpRecipentData);
		}
		return erpRecipentDataList;
	}
	private static List<ErpDiscountLineData> buildDiscountData(List<ErpDiscountLineModel> discounts) {
		List<ErpDiscountLineData> discountLineDataList = new ArrayList<ErpDiscountLineData>();
		for (ErpDiscountLineModel erpDiscountLineModel : discounts) {
			ErpDiscountLineData discountLineData = new ErpDiscountLineData();
			discountLineData.setDiscount(buildDiscountData(erpDiscountLineModel.getDiscount()));
			discountLineData.setId(erpDiscountLineModel.getId());
		}
		return discountLineDataList;
	}
	private static DiscountData buildDiscountData(Discount discount) {
		DiscountData discountData = null;
		if(discount != null){
			 discountData = new DiscountData();
			discountData.setAmount(discount.getAmount());
			if(discount.getDiscountType() != null)
			discountData.setDiscountType(String.valueOf(discount.getDiscountType().getId()));
			discountData.setPromotionCode(discount.getPromotionCode());
			discountData.setMaxPercentageDiscount(discount.getMaxPercentageDiscount());
			discountData.setPromotionDescription(discount.getPromotionDescription());
			discountData.setSkuLimit(discount.getSkuLimit());
		}
		return discountData;
	}
	private static List<ErpAppliedCreditData> buildAppliedCreditsData(List<ErpAppliedCreditModel> appliedCredits) {
		 List<ErpAppliedCreditData> erpAppliedCreditDataList = new ArrayList<ErpAppliedCreditData>();
		for (ErpAppliedCreditModel erpAppliedCreditModel : appliedCredits) {
			ErpAppliedCreditData erpAppliedCreditData = new ErpAppliedCreditData();
			erpAppliedCreditData.setAffiliate(buildErpAffiliateData(erpAppliedCreditModel.getAffiliate()));
			erpAppliedCreditData.setCustomerCreditPk(erpAppliedCreditModel.getCustomerCreditPk().getId());
			erpAppliedCreditData.setAmount(erpAppliedCreditModel.getAmount());
			erpAppliedCreditData.setDepartment(erpAppliedCreditModel.getDepartment());
			erpAppliedCreditData.setCreditId(erpAppliedCreditModel.getId());
			erpAppliedCreditData.setSapNumber(erpAppliedCreditModel.getSapNumber());
			erpAppliedCreditDataList.add(erpAppliedCreditData);
		}
		return erpAppliedCreditDataList;
	}
	private static ErpAffiliateData buildErpAffiliateData(ErpAffiliate affiliate) {
		ErpAffiliateData erpAffiliateData = new ErpAffiliateData();
		erpAffiliateData.setCode(affiliate.getCode());
		erpAffiliateData.setName(affiliate.getName());
		erpAffiliateData.setDescription(affiliate.getDescription());
		erpAffiliateData.setTaxConditionType(affiliate.getTaxConditionType());
		erpAffiliateData.setDepositConditionType(affiliate.getDepositConditionType());
		erpAffiliateData.setMerchants(affiliate.getMerchants());
		erpAffiliateData.setPaymentechTxDivisions(getDivisions(affiliate.getPayementechTxDivision()));
		return erpAffiliateData;
	}
	
	private static Set<String> getDivisions(String divisions) {
		Set<String> s = new HashSet<String>();
		if (divisions != null) {
			String[] tokens = divisions.split("\\,", -2);
			for (int i = 0; i < tokens.length; i++) {
				s.add(tokens[i]);
			}
		}
		return s;
	}
	public static  ErpAddressData buildErpAddressData(ErpAddressModel erpAddressModel) {
		ErpAddressData erpAddressData = new ErpAddressData();
		buildErpAddress(erpAddressModel, erpAddressData);
		erpAddressData.setId(erpAddressModel.getId());
		if(erpAddressModel.getAltContactPhone()!=null)
		erpAddressData.setAltContactPhone(buildPhoneNumberData(erpAddressModel.getAltContactPhone()));
		if(erpAddressModel.getAltPhone()!=null)
		erpAddressData.setAltPhone(buildPhoneNumberData( erpAddressModel.getAltPhone()));
		if(erpAddressModel.getAltDelivery()!=null)
			erpAddressData.setAltDeliverySetting(erpAddressModel.getAltDelivery().getName());
		if(erpAddressModel.getUnattendedDeliveryFlag()!=null)
			erpAddressData.setUnattendedDeliveryFlag(erpAddressModel.getUnattendedDeliveryFlag().getName());
		if(erpAddressModel.getWebServiceType()!=null)
			erpAddressData.setWebServiceType(erpAddressModel.getWebServiceType().getName());
//		erpAddressData.setAddress(encodeAddress(erpAddressModel));
		return erpAddressData;
	}
	private static void buildErpAddress(ErpAddressModel erpAddressModel,ErpAddressData erpAddressData) {
		erpAddressData.setInstructions(erpAddressModel.getInstructions());
		if(erpAddressModel.getAltDelivery() != null)
		erpAddressData.setAltDeliverySetting(erpAddressModel.getAltDelivery().getName());
		erpAddressData.setAltFirstName(erpAddressModel.getAltFirstName());
		erpAddressData.setAltLastName(erpAddressModel.getAltLastName());
		erpAddressData.setAltApartment(erpAddressModel.getAltApartment());
		erpAddressData.setAltPhone(buildPhoneData(erpAddressModel.getAltPhone()));
		erpAddressData.setAltContactPhone(buildPhoneData(erpAddressModel.getAltContactPhone()));
		if(erpAddressModel.getUnattendedDeliveryFlag() != null)
		erpAddressData.setUnattendedDeliveryFlag(erpAddressModel.getUnattendedDeliveryFlag().getName());
		erpAddressData.setUnattendedDeliveryInstructions(erpAddressModel.getUnattendedDeliveryInstructions());
		erpAddressData.setCharityName(erpAddressModel.getCharityName());
		erpAddressData.setOptInForDonation(erpAddressModel.isOptInForDonation());
		if(erpAddressModel.getWebServiceType() != null)
		erpAddressData.setWebServiceType(erpAddressModel.getWebServiceType().getName());
		erpAddressData.setEbtAccepted(erpAddressModel.isEbtAccepted());
		erpAddressData.setScrubbedStreet(erpAddressModel.getScrubbedStreet());
		erpAddressData.setFirstName(erpAddressModel.getFirstName());
		erpAddressData.setLastName(erpAddressModel.getLastName());
		erpAddressData.setPhone(buildPhoneNumberData(erpAddressModel.getPhone()));
		erpAddressData.setAddress1(erpAddressModel.getAddress1());
		erpAddressData.setAddress2(erpAddressModel.getAddress2());
		erpAddressData.setApartment(erpAddressModel.getApartment());
		erpAddressData.setCity(erpAddressModel.getCity());
		erpAddressData.setState(erpAddressModel.getState());
		erpAddressData.setZipCode(erpAddressModel.getZipCode());
		erpAddressData.setServiceType(erpAddressModel.getServiceType().getName());
		erpAddressData.setCompanyName(erpAddressModel.getCompanyName());
		erpAddressData.setAddressInfoData(buildAddressInfoData(erpAddressModel.getAddressInfo()));
		
	}

	private static CatalogKeyData buildCatalogKeyData(CatalogKey catalogKey) {
		CatalogKeyData keyData = new CatalogKeyData();
		keyData.seteStore(catalogKey.geteStore());
		keyData.setPlantId(catalogKey.getPlantId());
		keyData.setPricingZone(buildZoneInfoData(catalogKey.getPricingZone()));
		return keyData;
		
	}

	private static ZoneInfoData buildZoneInfoData(ZoneInfo pricingZone) {
		ZoneInfoData zoneInfoData = null;
		if(pricingZone.getParentZone()==null){
			  zoneInfoData  = new ZoneInfoData();
			  zoneInfoData.setZoneId(pricingZone.getPricingZoneId());
			  zoneInfoData.setSalesOrg(pricingZone.getSalesOrg());
			  zoneInfoData.setDistributionChanel(pricingZone.getDistributionChanel());
			}else {
			  zoneInfoData  = new ZoneInfoData();
			  zoneInfoData.setDistributionChanel(pricingZone.getDistributionChanel());
			  zoneInfoData.setParent(buildZoneInfoData(pricingZone.getParentZone()));
			  zoneInfoData.setSalesOrg( pricingZone.getSalesOrg());
			  zoneInfoData.setZoneId(pricingZone.getPricingZoneId());
			  zoneInfoData.setPricingIndicator(pricingZone.getPricingIndicator().getValue());
			  zoneInfoData.setParent(buildZoneInfoData(pricingZone.getParentZone()));
						
			}
		return zoneInfoData;
	}


	private static ErpDeliveryPlantInfoData buildErpDeliveryPlantInfoData(ErpDeliveryPlantInfoModel deliveryPlantInfo) {
		ErpDeliveryPlantInfoData erpDeliveryPlantInfoData = null;
		if(deliveryPlantInfo != null){
			erpDeliveryPlantInfoData = new ErpDeliveryPlantInfoData();
			erpDeliveryPlantInfoData.setCatalogKey(buildCatalogKeyData(deliveryPlantInfo.getCatalogKey()));
			erpDeliveryPlantInfoData.setDistChannel(deliveryPlantInfo.getDistChannel());
			erpDeliveryPlantInfoData.setDivision(deliveryPlantInfo.getDivision());
			erpDeliveryPlantInfoData.setPlantId(deliveryPlantInfo.getPlantId());
			erpDeliveryPlantInfoData.setSalesOrg(deliveryPlantInfo.getSalesOrg());
		}
		return erpDeliveryPlantInfoData;
	}
	public static ErpDeliveryInfoData buildDeliveryInfoData(ErpDeliveryInfoModel deliveryInfo) {
		ErpDeliveryInfoData erpDeliveryInfoData = new ErpDeliveryInfoData();
		if(deliveryInfo.getDeliveryAddress() != null)
		erpDeliveryInfoData.setDeliveryAddress(buildErpAddressData(deliveryInfo.getDeliveryAddress()));
		erpDeliveryInfoData.setDeliveryCutoffTime(deliveryInfo.getDeliveryCutoffTime());
		erpDeliveryInfoData.setDeliveryEndTime(deliveryInfo.getDeliveryEndTime());
		erpDeliveryInfoData.setDeliveryHandoffTime(deliveryInfo.getDeliveryHandoffTime());
		if(deliveryInfo.getDeliveryPlantInfo() != null)
		erpDeliveryInfoData.setDeliveryPlantInfo(buildErpDeliveryPlantInfoData(deliveryInfo.getDeliveryPlantInfo()));
		erpDeliveryInfoData.setDeliveryRegionId(deliveryInfo.getDeliveryRegionId());
		erpDeliveryInfoData.setDeliveryReservationId(deliveryInfo.getDeliveryReservationId());
		erpDeliveryInfoData.setDeliveryStartTime(deliveryInfo.getDeliveryStartTime());
		if(deliveryInfo.getDeliveryType() != null)
		erpDeliveryInfoData.setDeliveryType(deliveryInfo.getDeliveryType().getCode());
		erpDeliveryInfoData.setDeliveryZone(deliveryInfo.getDeliveryZone());
		erpDeliveryInfoData.setDepotLocationId(deliveryInfo.getDepotLocationId());
		erpDeliveryInfoData.setId(deliveryInfo.getId());
		erpDeliveryInfoData.setMinDurationForModification(deliveryInfo.getMinDurationForModification());
		erpDeliveryInfoData.setMinDurationForModStart(deliveryInfo.getMinDurationForModStart());
		if(deliveryInfo.getDeliveryPlantInfo() != null)
		erpDeliveryInfoData.setOrderMobileNumber(buildPhoneNumberData(deliveryInfo.getOrderMobileNumber()));
		erpDeliveryInfoData.setOriginalCutoffTime(deliveryInfo.getOriginalCutoffTime());
		if(deliveryInfo.getServiceType() != null)
		erpDeliveryInfoData.setServiceType(deliveryInfo.getServiceType().getName());
		return erpDeliveryInfoData;
	}
	
	private static List<ErpOrderLineModelData> buildOrderLineData(List<ErpOrderLineModel> orderLines) {
		List<ErpOrderLineModelData>  orderLineList = new ArrayList<ErpOrderLineModelData>();
		for (ErpOrderLineModel orderLine : orderLines) {
			ErpOrderLineModelData orderLineData = new ErpOrderLineModelData();
			orderLineData.setOrderLineNumber(orderLine.getOrderLineNumber());
			orderLineData.setOrderLineId(orderLine.getOrderLineId());
			orderLineData.setSku(buildFDSkuData(orderLine.getSku()));
			orderLineData.setConfiguration(buildFDConfigurationData(orderLine.getConfiguration()));
			orderLineData.setDiscount(buildDiscountData(orderLine.getDiscount()));
			orderLineData.setMaterialNumber(orderLine.getMaterialNumber());
			orderLineData.setDescription(orderLine.getDescription());
			orderLineData.setConfigurationDesc(orderLine.getConfigurationDesc());
			orderLineData.setDepartmentDesc(orderLine.getDepartmentDesc());
			orderLineData.setPrice(orderLine.getPrice());
			orderLineData.setPerishable(orderLine.isPerishable());
			orderLineData.setTaxRate(orderLine.getTaxRate());
			orderLineData.setTaxCode(orderLine.getTaxCode());
			orderLineData.setDepositValue(orderLine.getDepositValue());
			orderLineData.setWine(orderLine.isWine());
			orderLineData.setBeer(orderLine.isBeer());
			if(orderLine.getSource() != null)
			orderLineData.setSource(orderLine.getSource().getName());
			orderLineData.setAlcohol(orderLine.isAlcohol());
			orderLineData.setRecipeSourceId(orderLine.getRecipeSourceId());
			orderLineData.setCartLineId(orderLine.getCartlineId());
			orderLineData.setRequestNotification(orderLine.isRequestNotification());
			orderLineData.setDeliveryPass(orderLine.isDeliveryPass());
			orderLineData.setDiscountAmount(orderLine.getDiscountAmount());
			orderLineData.setDiscountApplied(orderLine.isDiscountFlag());
			if(orderLine.getProduceRating() != null)
			orderLineData.setProduceRating(orderLine.getProduceRating().getStatusCode());
			orderLineData.setGroup(buildFdGroupData(orderLine.getFDGroup()));
			orderLineData.setGrpQuantity(orderLine.getGroupQuantity());
			if(orderLine.getSustainabilityRating() != null)
			orderLineData.setSustainabilityRating(orderLine.getSustainabilityRating().getStatusCode());
			orderLineData.setAddedFromSearch(orderLine.isAddedFromSearch());
			if(orderLine.getAddedFrom() != null)
			orderLineData.setAddedFrom(orderLine.getAddedFrom().getName());
			orderLineData.setUpc(orderLine.getUpc());
			orderLineData.setCouponDiscount(buildErpCouponDiscountlineModelData(orderLine.getCouponDiscount()));
			if(orderLine.getTaxationType() != null)
			orderLineData.setTaxationType(orderLine.getTaxationType().getName());
			orderLineData.setCoremetricsPageId(orderLine.getCoremetricsPageId());
			orderLineData.setCoremetricsPageContentHierarchy(orderLine.getCoremetricsPageContentHierarchy());
			orderLineData.setCoremetricsVirtualCategory(orderLine.getCoremetricsVirtualCategory());
			if(orderLine.getExternalAgency() != null)
			orderLineData.setExternalAgency(orderLine.getExternalAgency().toString());
			orderLineData.setExternalSource(orderLine.getExternalSource());
			orderLineData.setExternalGroup(orderLine.getExternalGroup());
			orderLineData.setSalesOrg(orderLine.getSalesOrg());
			orderLineData.setDistChannel(orderLine.getDistChannel());
			if(orderLine.getEStoreId() != null)
			orderLineData.seteStoreId(orderLine.getEStoreId().getContentId());
			orderLineData.setPlantID(orderLine.getPlantID());
			orderLineData.setScaleQuantity(orderLine.getScaleQuantity());
			orderLineData.setMaterialGroup(orderLine.getMaterialGroup());
			orderLineData.setYmalCategoryId(orderLine.getYmalCategoryId());
			orderLineData.setYmalSetId(orderLine.getYmalSetId());
			orderLineData.setOriginatingProductId(orderLine.getOriginatingProductId());
			orderLineData.setVariantId(orderLine.getVariantId());
			orderLineData.setBasePrice(orderLine.getBasePrice());
			orderLineData.setBasePriceUnit(orderLine.getBasePriceUnit());
			orderLineData.setSavingsId(orderLine.getSavingsId());
			orderLineData.setUserCtx(buildUserContextData(orderLine.getUserContext()));
			orderLineData.setPricingZoneId(orderLine.getPricingZoneId());
			orderLineData.setAffiliateData(buildErpAffiliateData(orderLine.getAffiliate()));
			orderLineList.add(orderLineData);
		}
		return orderLineList;
	}

	private static UserContextData buildUserContextData(UserContext userContext) {
		UserContextData userContextData = new UserContextData();
		if(userContext.getStoreContext() != null && userContext.getStoreContext().getEStoreId() != null)
		userContextData.setEstoreId(userContext.getStoreContext().getEStoreId().getContentId());
		return userContextData;
	}
	public  static ErpPaymentMethodData buildPaymentMethodData(ErpPaymentMethodI payment) {
		ErpPaymentMethodModel paymentMethod = (ErpPaymentMethodModel) payment;
		ErpPaymentMethodData paymentData = new ErpPaymentMethodData();
		paymentData.setId(paymentMethod.getPK().getId());
		paymentData.setCustomerId(paymentMethod.getCustomerId());
		paymentData.setName(paymentMethod.getName());
		paymentData.setAccountNumber(paymentMethod.getAccountNumber());
		paymentData.setAddress(buildContactAddressData(paymentMethod.getAddress()));
		paymentData.setBillingRef(paymentMethod.getBillingRef());
		if(paymentMethod.getPaymentMethodType() != null)
		paymentData.setPaymentType(paymentMethod.getPaymentMethodType().getName());
		paymentData.setReferencedOrder(paymentMethod.getReferencedOrder());
		paymentData.setCvv(paymentMethod.getCVV());
		paymentData.setProfileID(paymentMethod.getProfileID());
		paymentData.setAccountNumLast4(paymentMethod.getAccountNumLast4());
		paymentData.setBestNumberForBillingInquiries(paymentMethod.getBestNumberForBillingInquiries());
		paymentData.seteWalletID(paymentMethod.geteWalletID());
		paymentData.setVendorEWalletID(paymentMethod.getVendorEWalletID());
		paymentData.seteWalletTrxnId(paymentMethod.geteWalletTrxnId());
		paymentData.setEmailID(paymentMethod.getEmailID());
		paymentData.setDeviceId(paymentMethod.getDeviceId());
		paymentData.setDebitCard(paymentMethod.isDebitCard());
		if(paymentMethod.getPaymentType() != null)
		paymentData.setPaymentType(paymentMethod.getPaymentType().getName());
		if(paymentMethod instanceof ErpCreditCardModel){
			if(paymentMethod.getExpirationDate() != null)
			paymentData.setExpirationDate(paymentMethod.getExpirationDate().getTime());
			if(paymentMethod.getCardType() != null)
			paymentData.setCardType(paymentMethod.getCardType().getName());
			paymentData.setAvsCkeckFailed(paymentMethod.isAvsCkeckFailed());
		}
		else if (paymentMethod instanceof ErpEbtCardModel){
			if(paymentMethod.getCardType() != null)
				paymentData.setCardType(paymentMethod.getCardType().getName());
		}
		else if (paymentMethod instanceof ErpECheckModel){
			if(paymentMethod.getPaymentMethodType() != null)
			paymentData.setBankAccountType(paymentMethod.getPaymentMethodType().getName());
			paymentData.setBankName(paymentMethod.getBankName());
			paymentData.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
			paymentData.setTermsAccepted(paymentMethod.getIsTermsAccepted());
		}
		else if (paymentMethod instanceof ErpGiftCardModel){
			paymentData.setBalance(paymentMethod.getBalance());
			paymentData.setOriginalAmount(((ErpGiftCardModel) paymentMethod).getOriginalAmount());
			paymentData.setPurchaseSaleId(((ErpGiftCardModel) paymentMethod).getPurchaseSaleId());
			paymentData.setPurchaseDate(((ErpGiftCardModel) paymentMethod).getPurchaseDate().getTime());
		}
		return paymentData;
	}

	private static ContactAddressData buildContactAddressData(ContactAddressModel address) {
		ContactAddressData contactAddressData = buildAddressData(address);
		contactAddressData.setFirstName(address.getFirstName());
		contactAddressData.setLastName(address.getLastName());
		contactAddressData.setPhone(buildPhoneNumberData(address.getPhone()));
		contactAddressData.setCustomerId(address.getCustomerId());
		
		return contactAddressData;
	}
	
	private static ContactAddressData  buildAddressData(ContactAddressModel address) {
		ContactAddressData contactAddress = new ContactAddressData();
		contactAddress.setAddress1(address.getAddress1());
		contactAddress.setAddress2(address.getAddress2());
		contactAddress.setAddressInfoData(buildAddressInfoData(address.getAddressInfo()));
		contactAddress.setApartment(address.getApartment());
		contactAddress.setCity(address.getCity());
		contactAddress.setCompanyName(address.getCompanyName());
		contactAddress.setCountry(address.getCountry());
		if(address.getServiceType() != null)
		contactAddress.setServiceType(address.getServiceType().getName());
		contactAddress.setState(address.getState());
		contactAddress.setZipCode(address.getZipCode());
		return contactAddress;
		
	}

	private static PhoneNumberData buildPhoneNumberData(PhoneNumber phone) {
		if(phone != null){
		return new PhoneNumberData(phone.getPhone(), NVL.apply(phone.getExtension(), ""), NVL.apply(phone.getType(), ""));
		}
		return null;
	}
	private static ErpCouponDiscountLineModelData buildErpCouponDiscountlineModelData(ErpCouponDiscountLineModel couponDiscount) {
		ErpCouponDiscountLineModelData discountLineData =null;
		if(couponDiscount != null){
			discountLineData = new ErpCouponDiscountLineModelData();
			discountLineData.setOrderLineId(couponDiscount.getOrderLineId());
			discountLineData.setCouponId(couponDiscount.getCouponId());
			discountLineData.setVersion(couponDiscount.getVersion());
			discountLineData.setRequiredQuantity(couponDiscount.getRequiredQuantity());
			discountLineData.setCouponDesc(couponDiscount.getCouponDesc());
			discountLineData.setDiscountAmt(couponDiscount.getDiscountAmt());
			if(couponDiscount.getDiscountType() != null)
			discountLineData.setDiscountType(couponDiscount.getDiscountType().getName());
		}
		return discountLineData;
		
	}

	private static FDSkuData buildFDSkuData(FDSku sku) {
		FDSkuData skuData = new FDSkuData();
		skuData.setSkuCode(sku.getSkuCode());
		skuData.setVersion(sku.getVersion());
		return skuData;
	}

	private static FDGroupData buildFdGroupData(FDGroup fdGroup) {
		FDGroupData groupData = null;
		if(fdGroup != null){
			groupData = new FDGroupData();
			groupData.setGroupId(fdGroup.getGroupId());
			groupData.setSkipProductPriceValidation(fdGroup.isSkipProductPriceValidation());
			groupData.setVersion(fdGroup.getVersion());
		}
		return groupData;
	}


	private static FDConfigurationData buildFDConfigurationData(FDConfiguration configuration) {
		FDConfigurationData configurationData = null;
		if(configuration != null){
			configurationData = new FDConfigurationData();
			configurationData.setOptions(configuration.getOptions());
			configurationData.setQuantity(configuration.getQuantity());
			configurationData.setSalesUnit(configuration.getSalesUnit());
		}
		return configurationData;
	}

	public static SapOrderI buildSapOrderI(SapOrderData data) throws FDResourceException {
		ErpAbstractOrderModel abstractOrderModel = buildAbstractOrderModel(data.getErpOrder());
		SapOrderAdapter orderAdapter = new SapOrderAdapter(abstractOrderModel, buildErpCustomer(data.getCustomerData()), null);
		return  orderAdapter;
	}
	
	private static ErpCustomerModel buildErpCustomer(ErpCustomerData erpcustomerData) {
		ErpCustomerModel customerModel = null;
		if(erpcustomerData != null){
		ErpCustomerInfoModel erpCustomerInfo =  new ErpCustomerInfoModel();
		customerModel = new ErpCustomerModel();
		ErpCustomerInfoData erpcustomerInfoData = erpcustomerData.getCustomerInfo();
		if(erpcustomerInfoData != null){
			erpCustomerInfo.setId(erpcustomerInfoData.getId());
			erpCustomerInfo.setTitle(erpcustomerInfoData.getTitle());
			erpCustomerInfo.setFirstName(erpcustomerInfoData.getFirstName());
			erpCustomerInfo.setLastName(erpcustomerInfoData.getLastName());
			erpCustomerInfo.setMiddleName(erpcustomerInfoData.getMiddleName());
			erpCustomerInfo.setDisplayName(erpcustomerInfoData.getDisplayName());
			erpCustomerInfo.setEmail(erpcustomerInfoData.getEmail());
			erpCustomerInfo.setAlternateEmail(erpcustomerInfoData.getAlternateEmail());
			erpCustomerInfo.setEmailPlaintext(erpcustomerInfoData.isEmailPlaintext());
			erpCustomerInfo.setReceiveNewsletter(erpcustomerInfoData.isReceiveNewsletter());
			erpCustomerInfo.setHomePhone(buildPhoneNumber(erpcustomerInfoData.getHomePhone()));
			erpCustomerInfo.setCellPhone(buildPhoneNumber(erpcustomerInfoData.getCellPhone()));
			erpCustomerInfo.setBusinessPhone(buildPhoneNumber(erpcustomerInfoData.getBusinessPhone()));
			erpCustomerInfo.setOtherPhone(buildPhoneNumber(erpcustomerInfoData.getOtherPhone()));
			erpCustomerInfo.setFax(buildPhoneNumber(erpcustomerInfoData.getFax()));
			erpCustomerInfo.setWorkDepartment(erpcustomerInfoData.getWorkDepartment());
			erpCustomerInfo.setEmployeeId(erpcustomerInfoData.getEmployeeId());
			erpCustomerInfo.setLastReminderEmailSend(erpcustomerInfoData.getLastReminderEmail());
			erpCustomerInfo.setReminderDayOfWeek(erpcustomerInfoData.getReminderDayOfWeek());
			erpCustomerInfo.setReminderFrequency(erpcustomerInfoData.getReminderFrequency());
			erpCustomerInfo.setReminderAltEmail(erpcustomerInfoData.isReminderAltEmail());
			erpCustomerInfo.setRsvDayOfWeek(erpcustomerInfoData.getRsvDayOfWeek());
			erpCustomerInfo.setRsvStartTime(erpcustomerInfoData.getRsvStartTime());
			erpCustomerInfo.setRsvEndTime(erpcustomerInfoData.getRsvEndTime());
			erpCustomerInfo.setRsvAddressId(erpcustomerInfoData.getRsvAddressId());
			erpCustomerInfo.setUnsubscribeDate(erpcustomerInfoData.getUnsubscribeDate());
			erpCustomerInfo.setEmailPreferenceLevel(erpcustomerInfoData.getReceive_emailLevel());
			erpCustomerInfo.setNoContactMail(erpcustomerInfoData.isNoContactMail());
			erpCustomerInfo.setNoContactPhone(erpcustomerInfoData.isNoContactPhone());
			erpCustomerInfo.setReceiveOptinNewsletter(erpcustomerInfoData.isReceiveOptinNewsletter());
			erpCustomerInfo.setRegRefTrackingCode(erpcustomerInfoData.getRegRefTrackingCode());
			erpCustomerInfo.setReferralProgId(erpcustomerInfoData.getReferralProgId());
			erpCustomerInfo.setReferralProgInvtId(erpcustomerInfoData.getReferralProgInvtId());
			erpCustomerInfo.setHasAutoRenewDP(erpcustomerInfoData.getHasAutoRenewDP());
			erpCustomerInfo.setAutoRenewDPSKU(erpcustomerInfoData.getAutoRenewDPSKU());
			erpCustomerInfo.setMobileNumber(buildPhoneNumber(erpcustomerInfoData.getMobileNumber()));
			erpCustomerInfo.setMobilePreference(erpcustomerInfoData.getMobilePrefs());
			erpCustomerInfo.setDeliveryNotification(erpcustomerInfoData.isDeliveryNotification());
			erpCustomerInfo.setOffersNotification(erpcustomerInfoData.isOffersNotification());
			erpCustomerInfo.setGoGreen(erpcustomerInfoData.isGoGreen());
			erpCustomerInfo.setDpTcViewCount(erpcustomerInfoData.getDpTcViewCount());
			erpCustomerInfo.setDpTcAgreeDate(erpcustomerInfoData.getDpTcAgreeDate());
			erpCustomerInfo.setIndustry(erpcustomerInfoData.getIndustry());
			erpCustomerInfo.setNumOfEmployees(erpcustomerInfoData.getNumOfEmployees());
			erpCustomerInfo.setSecondEmailAddress(erpcustomerInfoData.getSecondEmailAddress());
			erpCustomerInfo.setOrderNotices(EnumSMSAlertStatus.getEnum(erpcustomerInfoData.getOrderNotices()));
			erpCustomerInfo.setOrderExceptions(EnumSMSAlertStatus.getEnum(erpcustomerInfoData.getOrderExceptions()));
			erpCustomerInfo.setOffers(EnumSMSAlertStatus.getEnum(erpcustomerInfoData.getOffers()));
			erpCustomerInfo.setPartnerMessages(EnumSMSAlertStatus.getEnum(erpcustomerInfoData.getPartnerMessages()));
			erpCustomerInfo.setSmsPreferenceflag(erpcustomerInfoData.getSmsPreferenceflag());
			erpCustomerInfo.setSmsOptinDate(erpcustomerInfoData.getSmsOptinDate());
			erpCustomerInfo.setCompanyNameSignup(erpcustomerInfoData.getCompanyNameSignup());
			erpCustomerInfo.setSoCartOverlayFirstTime(erpcustomerInfoData.getSoCartOverlayFirstTime());
			erpCustomerInfo.setSoFeatureOverlay(erpcustomerInfoData.getSoFeatureOverlay());
			erpCustomerInfo.setFdTcAgree(erpcustomerInfoData.getFdTcAgree());
			erpCustomerInfo.setIdentity(buildIdentity(erpcustomerInfoData.getIdentity()));
			customerModel.setCustomerInfo(erpCustomerInfo);
			customerModel.setActive(erpcustomerData.isActive());
			customerModel.setId(erpcustomerData.getId());
			customerModel.setPasswordHash(erpcustomerData.getPasswordHash());
			customerModel.setSapBillToAddress(buildContactAddressModel(erpcustomerData.getSapBillToAddress()));
			customerModel.setSapId(erpcustomerData.getSapId());
			customerModel.setSocialLoginOnly(erpcustomerData.isSocialLoginOnly());
			customerModel.setUserId(erpcustomerData.getUserId());
		}
		}
		return customerModel;
	}
	
	private static FDIdentity buildIdentity(FDIdentityData identity) {
		FDIdentity fdIdentity = new FDIdentity(identity.getErpCustomerPK(),identity.getFdCustomerPK());
		return fdIdentity;
	}
	private static ErpAbstractOrderModel buildAbstractOrderModel(ErpAbstractOrderModelData erpOrder) {
		@SuppressWarnings("serial")
		ErpAbstractOrderModel abstractOrderModel = new ErpAbstractOrderModel(EnumTransactionType.getTransactionType(erpOrder.getTransactionType())) {
		};
		abstractOrderModel.setOrderLines(buildOrderLine(erpOrder.getOrderLines()));
		abstractOrderModel.setRequestedDate(erpOrder.getRequestedDate());
		abstractOrderModel.setDiscounts(buildDiscountDataList(erpOrder.getDiscounts()));
		abstractOrderModel.setPricingDate(erpOrder.getPricingDate());
		abstractOrderModel.setPaymentMethod(buildPaymentMethodModel(erpOrder.getPaymentMethod()));
		abstractOrderModel.setSubTotal(erpOrder.getSubTotal());
		abstractOrderModel.setTax(erpOrder.getTax());
		abstractOrderModel.setCustomerServiceMessage(erpOrder.getCustomerServiceMessage());
		abstractOrderModel.setMarketingMessage(erpOrder.getMarketingMessage());
		abstractOrderModel.setGlCode(erpOrder.getGlCode());
		abstractOrderModel.setTaxationType(EnumNotificationType.getNotificationType(erpOrder.getTaxationType()));
		abstractOrderModel.setDeliveryPassCount(erpOrder.getDeliveryPassCount());
		abstractOrderModel.setDlvPassApplied(erpOrder.isDlvPassApplied());
		abstractOrderModel.setDlvPromotionApplied(erpOrder.isDlvPromotionApplied());
		abstractOrderModel.setBufferAmt(erpOrder.getBufferAmt());
		abstractOrderModel.setAppliedCredits(buildAppliedCredits(erpOrder.getAppliedCredits()));
		abstractOrderModel.setDeliveryInfo(buildDeliveryInfo(erpOrder.getDeliveryInfo()));
		abstractOrderModel.setCharges(buildChargeLineModel(erpOrder.getCharges()));
		abstractOrderModel.setDlvPassExtendDays(erpOrder.getDlvPassExtendDays());
		abstractOrderModel.setCurrentDlvPassExtendDays(erpOrder.getCurrentDlvPassExtendDays());
		abstractOrderModel.setCouponTransModel(buildCouponTransModel(erpOrder.getCouponTransModel()));
		abstractOrderModel.setRafTransModel(buildRefTransModel(erpOrder.getRafTransModel()));
		abstractOrderModel.seteStoreId(EnumEStoreId.valueOfContentId(erpOrder.geteStoreId()));
		abstractOrderModel.setSelectedGiftCards(buildSelectedGiftCard(erpOrder.getSelectedGiftCards()));
		abstractOrderModel.setAppliedGiftcards(buildAppliedGiftCards(erpOrder.getAppliedGiftcards()));
		abstractOrderModel.setRecepientsList(buildRecepientsList(erpOrder.getRecipientsList()));
		return abstractOrderModel;
	}
	
	private static List<ErpOrderLineModel> buildOrderLine(List<ErpOrderLineModelData> orderLines) {
		List<ErpOrderLineModel>  orderlineList = new ArrayList<ErpOrderLineModel>();
		for (ErpOrderLineModelData orderLine : orderLines) {
			ErpOrderLineModel orderLineModel = new ErpOrderLineModel();
			orderLineModel.setOrderLineNumber(orderLine.getOrderLineNumber());
			orderLineModel.setOrderLineId(orderLine.getOrderLineId());
			orderLineModel.setSku(buildFDSku(orderLine.getSku()));
			orderLineModel.setConfiguration(buildFDConfiguration(orderLine.getConfiguration()));
			orderLineModel.setDiscount(buildDiscount(orderLine.getDiscount()));
			orderLineModel.setMaterialNumber(orderLine.getMaterialNumber());
			orderLineModel.setDescription(orderLine.getDescription());
			orderLineModel.setConfigurationDesc(orderLine.getConfigurationDesc());
			orderLineModel.setDepartmentDesc(orderLine.getDepartmentDesc());
			orderLineModel.setPrice(orderLine.getPrice());
			orderLineModel.setPerishable(orderLine.isPerishable());
			orderLineModel.setTaxRate(orderLine.getTaxRate());
			orderLineModel.setTaxCode(orderLine.getTaxCode());
			orderLineModel.setDepositValue(orderLine.getDepositValue());
			orderLineModel.setWine(orderLine.isWine());
			orderLineModel.setBeer(orderLine.isBeer());
			if(orderLine.getSource() != null)
			orderLineModel.setSource(EnumEventSource.valueOf(orderLine.getSource()));
			orderLineModel.setAlcohol(orderLine.isAlcohol());
			orderLineModel.setRecipeSourceId(orderLine.getRecipeSourceId());
			orderLineModel.setCartlineId(orderLine.getCartLineId());
			orderLineModel.setRequestNotification(orderLine.isRequestNotification());
			orderLineModel.setDeliveryPass(orderLine.isDeliveryPass());
			orderLineModel.setDiscountAmount(orderLine.getDiscountAmount());
			orderLineModel.setDiscountFlag(orderLine.isDiscountApplied());
			if(orderLine.getProduceRating() != null)
			orderLineModel.setProduceRating(EnumOrderLineRating.getEnumByStatusCode(orderLine.getProduceRating()));
			orderLineModel.setFDGroup(buildFdGroup(orderLine.getGroup()));
			orderLineModel.setGroupQuantity(orderLine.getGrpQuantity());
			if(orderLine.getSustainabilityRating() != null)
			orderLineModel.setSustainabilityRating(EnumSustainabilityRating.getEnumByStatusCode(orderLine.getSustainabilityRating()));
			orderLineModel.setAddedFromSearch(orderLine.isAddedFromSearch());
			orderLineModel.setAddedFrom(EnumATCContext.getEnum(orderLine.getAddedFrom()));
			orderLineModel.setUpc(orderLine.getUpc());
			orderLineModel.setCouponDiscount(buildErpCouponDiscountlineModel(orderLine.getCouponDiscount()));
			orderLineModel.setTaxationType(EnumTaxationType.getEnum(orderLine.getTaxationType()));
			orderLineModel.setCoremetricsPageId(orderLine.getCoremetricsPageId());
			orderLineModel.setCoremetricsPageContentHierarchy(orderLine.getCoremetricsPageContentHierarchy());
			orderLineModel.setCoremetricsVirtualCategory(orderLine.getCoremetricsVirtualCategory());
			if(orderLine.getExternalAgency() != null)
			orderLineModel.setExternalAgency(ExternalAgency.valueOf(orderLine.getExternalAgency()));
			orderLineModel.setExternalSource(orderLine.getExternalSource());
			orderLineModel.setExternalGroup(orderLine.getExternalGroup());
			orderLineModel.setSalesOrg(orderLine.getSalesOrg());
			orderLineModel.setDistChannel(orderLine.getDistChannel());
			orderLineModel.setEStoreId(EnumEStoreId.valueOfContentId(orderLine.geteStoreId()));
			orderLineModel.setPlantID(orderLine.getPlantID());
			orderLineModel.setScaleQuantity(orderLine.getScaleQuantity());
			orderLineModel.setMaterialGroup(orderLine.getMaterialGroup());
			orderLineModel.setYmalCategoryId(orderLine.getYmalCategoryId());
			orderLineModel.setYmalSetId(orderLine.getYmalSetId());
			orderLineModel.setOriginatingProductId(orderLine.getOriginatingProductId());
			orderLineModel.setVariantId(orderLine.getVariantId());
			orderLineModel.setBasePrice(orderLine.getBasePrice());
			orderLineModel.setBasePriceUnit(orderLine.getBasePriceUnit());
			orderLineModel.setSavingsId(orderLine.getSavingsId());
			orderLineModel.setUserContext(buildUserContext(orderLine.getUserCtx()));
			orderLineModel.setPricingZoneId(orderLine.getPricingZoneId());
			orderlineList.add(orderLineModel);
		}
		return orderlineList;
	}


	private static UserContext buildUserContext(UserContextData userCtx) {
		UserContext userContext = new UserContext();
		userContext.createUserContext(EnumEStoreId.valueOfContentId(userCtx.getEstoreId()));
		return userContext;
	}


	private static ErpCouponDiscountLineModel buildErpCouponDiscountlineModel(ErpCouponDiscountLineModelData couponDiscount) {
		ErpCouponDiscountLineModel erpCouponDiscountLineModel =null;
		if(couponDiscount != null){
			erpCouponDiscountLineModel = new ErpCouponDiscountLineModel();
			erpCouponDiscountLineModel.setOrderLineId(couponDiscount.getOrderLineId());
			erpCouponDiscountLineModel.setCouponId(couponDiscount.getCouponId());
			erpCouponDiscountLineModel.setVersion(couponDiscount.getVersion());
			erpCouponDiscountLineModel.setRequiredQuantity(couponDiscount.getRequiredQuantity());
			erpCouponDiscountLineModel.setCouponDesc(couponDiscount.getCouponDesc());
			erpCouponDiscountLineModel.setDiscountAmt(couponDiscount.getDiscountAmt());
			if(couponDiscount.getDiscountType() != null)
			erpCouponDiscountLineModel.setDiscountType(EnumCouponOfferType.getEnum(couponDiscount.getDiscountType()));
		}
		return erpCouponDiscountLineModel;
		
	}


	private static FDGroup buildFdGroup(FDGroupData group) {
		FDGroup fdGroup = null;
		if(group != null){
			fdGroup = new FDGroup(group.getGroupId(), group.getVersion());
			fdGroup.setSkipProductPriceValidation(group.isSkipProductPriceValidation());
		}
		return fdGroup;
	}


	private static FDConfiguration buildFDConfiguration(FDConfigurationData configuration) {
		FDConfiguration fdConfiguration = new FDConfiguration(configuration.getQuantity(), configuration.getSalesUnit(), configuration.getOptions());
		return fdConfiguration;
	}


	private static FDSku buildFDSku(FDSkuData sku) {
		FDSku fdSku = new FDSku(sku.getSkuCode(), sku.getVersion());
		return fdSku;
	}


	public static ErpPaymentMethodI buildPaymentMethodModel(ErpPaymentMethodData paymentData) {

		ErpPaymentMethodI model = null;
		if(paymentData.getPaymentMethodType()==null){
			model = createErpCreditCardModel(paymentData);
		}else if(paymentData.getPaymentMethodType().equals(EnumPaymentMethodType.GIFTCARD.getName())){
//			model = mapperFacade.map(source, ErpGiftCardModel.class);
			model = createErpGiftCardModel(paymentData);
		}
		else if(paymentData.getPaymentMethodType().equals(EnumPaymentMethodType.ECHECK.getName())){
			model = createECheckModelModel(paymentData);
//			model = mapperFacade.map(source, ErpECheckModel.class);

		}
		else if(paymentData.getPaymentMethodType().equals(EnumPaymentMethodType.EBT.getName())){
			model = createErpEbtCardModel(paymentData);
//			model = mapperFacade.map(source, ErpEbtCardModel.class);
		}
		else if(paymentData.equals(EnumPaymentMethodType.PAYPAL.getName())){
//			model = mapperFacade.map(source, ErpPayPalCardModel.class);
			model = createErpPayPalCardModel(paymentData);
		}
		/*else{
			
//			model = mapperFacade.map(source, ErpCreditCardModel.class);
		}*/
		return model;
	}
	
	private static ErpPaymentMethodI createErpCreditCardModel(ErpPaymentMethodData source) {
		ErpCreditCardModel model = new ErpCreditCardModel();
		createErpPaymentMethod(model,source);
		model.setExpirationDate(new java.sql.Date(source.getExpirationDate()));
		model.setCardType(EnumCardType.getCardType(source.getCardType()));
		model.setAvsCkeckFailed(source.isAvsCkeckFailed());
		model.setBypassAVSCheck(source.isBypassAVSCheck());
		return model;
	}

	private static ErpPaymentMethodI createErpPayPalCardModel(ErpPaymentMethodData source) {
		ErpPayPalCardModel model = new ErpPayPalCardModel();
		createErpPaymentMethod(model,source);
		return model;
	}

	private static ErpPaymentMethodI createErpEbtCardModel(ErpPaymentMethodData source) {
		ErpEbtCardModel model = new ErpEbtCardModel();
		createErpPaymentMethod(model,source);
		model.setCardType(EnumCardType.getCardType(source.getCardType()));
		return model;
	}

	private static ErpPaymentMethodI createECheckModelModel(ErpPaymentMethodData source) {
		ErpECheckModel model = new ErpECheckModel();
		createErpPaymentMethod(model,source);
		model.setBankName(source.getBankName());
		model.setAbaRouteNumber(source.getAbaRouteNumber());
		model.setBankAccountType(EnumBankAccountType.getEnum(source.getBankAccountType()));
		model.setIsTermsAccepted(source.isTermsAccepted());
		return model;
	}

	private static ErpPaymentMethodI createErpGiftCardModel(ErpPaymentMethodData source) {
		ErpGiftCardModel model = new ErpGiftCardModel();
		createErpPaymentMethod(model,source);
		model.setBalance(source.getBalance());
		if(source.getStatus().equals("U")){
			model.setStatus(EnumGiftCardStatus.UNKNOWN);
		}
		else if(source.getStatus().equals("A")){
			model.setStatus(EnumGiftCardStatus.ACTIVE);
		}
		else if(source.getStatus().equals("I")){
			model.setStatus(EnumGiftCardStatus.INACTIVE);
		}
		else if(source.getStatus().equals("Z")){
			model.setStatus(EnumGiftCardStatus.ZERO_BALANCE);
		}
		model.setOriginalAmount(source.getOriginalAmount());
		model.setPurchaseSaleId(source.getPurchaseSaleId());
		model.setPurchaseDate(new java.sql.Date(source.getPurchaseDate()));
		return model;
		
	}
	private static void createErpPaymentMethod(ErpPaymentMethodModel model, ErpPaymentMethodData source) {
		model.setId(source.getId()==null?"":source.getId());
		model.setCustomerId(source.getCustomerId());
		model.setName(source.getName());
		model.setAccountNumber(source.getAccountNumber());
		if(source.getAddress() !=null &&source.getAddress().getId() == null)
			source.getAddress().setId("");
		model.setAddress(buildContactAddressModel(source.getAddress()));
		model.setBillingRef(source.getBillingRef());
		model.setPaymentType(EnumPaymentType.getEnum(source.getPaymentType()));
		model.setReferencedOrder(source.getReferencedOrder());
		model.setCVV(source.getCvv());
		model.setProfileID(source.getProfileID());
		model.setAccountNumLast4(source.getAccountNumLast4());
		model.setBestNumberForBillingInquiries(source.getBestNumberForBillingInquiries());
		model.seteWalletID(source.geteWalletID());
		model.setVendorEWalletID(source.getVendorEWalletID());
		model.seteWalletTrxnId(source.geteWalletTrxnId());
		model.setEmailID(source.getEmailID());
		model.setDeviceId(source.getDeviceId());
		model.setDebitCard(source.isDebitCard());
		
	}

	public static ContactAddressModel buildContactAddressModel(ContactAddressData address) {
		ContactAddressModel contactAddressModel = null;
		if(address != null){
		contactAddressModel = buildAddress(address);
		contactAddressModel.setFirstName(address.getFirstName());
		contactAddressModel.setLastName(address.getLastName());
		contactAddressModel.setPhone(buildPhoneNumber(address.getPhone()));
		contactAddressModel.setCustomerId(address.getCustomerId());
		}
		return contactAddressModel;
	}

	private static ContactAddressModel buildAddress(AddressData address) {
		ContactAddressModel contactAddressModel = null;
		if(address != null){
			contactAddressModel = new ContactAddressModel();
		contactAddressModel.setAddress1(address.getAddress1());
		contactAddressModel.setAddress2(address.getAddress2());
		contactAddressModel.setAddressInfo(buildAddressInfo(address.getAddressInfoData()));
		contactAddressModel.setApartment(address.getApartment());
		contactAddressModel.setCity(address.getCity());
		contactAddressModel.setCompanyName(address.getCompanyName());
		contactAddressModel.setCountry(address.getCountry());
		if(address.getServiceType() != null)
		contactAddressModel.setServiceType(EnumServiceType.getEnum(address.getServiceType()));
		contactAddressModel.setState(address.getState());
		contactAddressModel.setZipCode(address.getZipCode());
		}
		return contactAddressModel;
		
	}

	private static List<ErpRecipentModel> buildRecepientsList(List<ErpRecipentData> recipientsList) {
		List<ErpRecipentModel> recipientModelList = new ArrayList<ErpRecipentModel>();
		if(recipientsList != null){
		for (ErpRecipentData erpRecipentData : recipientsList) {
			ErpRecipentModel recipeModel = new ErpRecipentModel();
			recipeModel.setAmount(erpRecipentData.getAmount());
			recipeModel.setCustomerId(erpRecipentData.getCustomerId());
			recipeModel.setDeliveryMode(EnumGCDeliveryMode.getEnum(erpRecipentData.getDeliveryMode()));
			recipeModel.setDonorOrganizationName(erpRecipentData.getDonorOrganizationName());
			recipeModel.setGiftCardType(EnumGiftCardType.getEnum(erpRecipentData.getGiftCardType()));
			recipeModel.setId(erpRecipentData.getRecipeId());
			recipeModel.setOrderLineId(erpRecipentData.getOrderLineId());
			recipeModel.setPersonalMessage(erpRecipentData.getPersonalMessage());
			recipeModel.setRecipientEmail(erpRecipentData.getRecipientEmail());
			recipeModel.setSale_id(erpRecipentData.getSale_id());
			recipeModel.setSenderEmail(erpRecipentData.getSenderEmail());
			recipeModel.setSenderName(erpRecipentData.getSenderName());
			recipeModel.setTemplateId(erpRecipentData.getTemplateId());
			recipeModel.setRecipientName(erpRecipentData.getRecipientName());
			recipientModelList.add(recipeModel);
		}
		}
		return recipientModelList;
	}

	private static List<ErpAppliedGiftCardModel> buildAppliedGiftCards(List<ErpAppliedGiftCardData> appliedGiftcards) {
		List<ErpAppliedGiftCardModel> giftCardList = new ArrayList<ErpAppliedGiftCardModel>();
		if(appliedGiftcards != null){
		for (ErpAppliedGiftCardData erpAppliedGiftCardData : appliedGiftcards) {
			ErpAppliedGiftCardModel giftCardmodel = new ErpAppliedGiftCardModel();
			giftCardmodel.setAccountNumber(erpAppliedGiftCardData.getAccountNumber());
			giftCardmodel.setAffiliate(buildErpAffiliate(erpAppliedGiftCardData.getAffiliate()));
			giftCardmodel.setAmount(erpAppliedGiftCardData.getAmount());
			giftCardmodel.setCertificateNum(erpAppliedGiftCardData.getCertificateNum());
			giftCardmodel.setId(erpAppliedGiftCardData.getId());
			giftCardList.add(giftCardmodel);
		}
		}
		return giftCardList;
	}

	private static ErpAffiliate buildErpAffiliate(ErpAffiliateData affiliate) {
		ErpAffiliate erpAffiliate = new ErpAffiliate(affiliate.getCode(), affiliate.getName(), affiliate.getDescription(),
				affiliate.getTaxConditionType(), affiliate.getDepositConditionType(), affiliate.getMerchants(),
				affiliate.getPaymentechTxDivisions());
		return erpAffiliate;
	}

	private static List<ErpGiftCardModel> buildSelectedGiftCard(List<ErpGiftCardData> selectedGiftCards) {
		List<ErpGiftCardModel> giftCardModelList = new ArrayList<ErpGiftCardModel>();
		if(selectedGiftCards != null){
		for (ErpGiftCardData erpGiftCardData : selectedGiftCards) {
			ErpGiftCardModel giftCardModel = new ErpGiftCardModel();
			giftCardModel = (ErpGiftCardModel) buildPaymentMethodModel(erpGiftCardData);
			giftCardModel.setBalance(erpGiftCardData.getBalance());
			giftCardModel.setOriginalAmount(erpGiftCardData.getOriginalAmount());
			giftCardModel.setPurchaseSaleId(erpGiftCardData.getPurchaseSaleId());
			giftCardModel.setPurchaseDate(new java.sql.Date(erpGiftCardData.getPurchaseDate()));
			giftCardModelList.add(giftCardModel);
		}
		}
		return giftCardModelList;
	}

	private static FDRafTransModel buildRefTransModel(FDRafTransData rafTransModel) {
		FDRafTransModel fdRafTransModel = null;
		if(rafTransModel != null){
		fdRafTransModel = new FDRafTransModel();
		fdRafTransModel.setCreateTime(rafTransModel.getCreateTime());
		fdRafTransModel.setErrorDetails(rafTransModel.getErrorDetails());
		fdRafTransModel.setErrorMessage(rafTransModel.getErrorMessage());
		fdRafTransModel.setExtoleEventId(rafTransModel.getExtoleEventId());
		fdRafTransModel.setId(rafTransModel.getId());
		fdRafTransModel.setSalesActionId(rafTransModel.getSalesActionId());
		fdRafTransModel.setTransStatus(EnumRafTransactionStatus.getEnumFromValue(rafTransModel.getTransStatus()));
		fdRafTransModel.setTransTime(rafTransModel.getTransTime());
		fdRafTransModel.setTransType(EnumRafTransactionType.getEnum(rafTransModel.getTransType()));
		}
		return fdRafTransModel;
	}

	private static ErpCouponTransactionModel buildCouponTransModel(ErpCouponTransactionModelData couponTransModel) {
		ErpCouponTransactionModel erpCouponTransactionModel = null;
		if(couponTransModel != null ){
			erpCouponTransactionModel = new  ErpCouponTransactionModel();
		erpCouponTransactionModel.setCreateTime(couponTransModel.getCreateTime());
		erpCouponTransactionModel.setErrorDetails(couponTransModel.getErrorDetails());
		erpCouponTransactionModel.setErrorMessage(couponTransModel.getErrorMessage());
		erpCouponTransactionModel.setId(couponTransModel.getId());
		erpCouponTransactionModel.setPK(new PrimaryKey(couponTransModel.getId()));
		erpCouponTransactionModel.setSaleActionId(couponTransModel.getSaleActionId());
		erpCouponTransactionModel.setSaleId(couponTransModel.getSaleId());
		List<ErpCouponTransactionDetailModel> erpCouponTransactionDetailModels = new ArrayList<ErpCouponTransactionDetailModel>();
		if (couponTransModel.getTranDetails() != null) {
			for (ErpCouponTransactionDetailModelData erpCouponTransactionData : couponTransModel.getTranDetails()) {
				ErpCouponTransactionDetailModel couponTransactionDetailModel = new ErpCouponTransactionDetailModel();
				couponTransactionDetailModel.setCouponId(erpCouponTransactionData.getCouponId());
				couponTransactionDetailModel.setCouponLineId(erpCouponTransactionData.getCouponLineId());
				couponTransactionDetailModel.setCouponTransId(erpCouponTransactionData.getCouponTransId());
				couponTransactionDetailModel.setDiscountAmt(erpCouponTransactionData.getDiscountAmt());
				couponTransactionDetailModel.setId(erpCouponTransactionData.getId());
				couponTransactionDetailModel.setPK(new PrimaryKey(erpCouponTransactionData.getId()));
				couponTransactionDetailModel.setTransTime(erpCouponTransactionData.getTransTime());
				erpCouponTransactionDetailModels.add(couponTransactionDetailModel);
			}
		}
		erpCouponTransactionModel.setTranDetails(erpCouponTransactionDetailModels);
		if(!StringUtil.isEmpty(couponTransModel.getTranStatus()))
		erpCouponTransactionModel.setTranStatus(EnumCouponTransactionStatus.getEnum(couponTransModel.getTranStatus()));
		erpCouponTransactionModel.setTranTime(couponTransModel.getTranTime());
		if(!StringUtil.isEmpty(couponTransModel.getTranType()))
		erpCouponTransactionModel.setTranType(EnumCouponTransactionType.getEnum(couponTransModel.getTranType()));
		}
		return erpCouponTransactionModel;
	
	}

	private static List<ErpChargeLineModel> buildChargeLineModel(List<ErpChargeLineData> charges) {
		List<ErpChargeLineModel> chargeLineModelData = new ArrayList<ErpChargeLineModel>();
		if(charges != null){
		for (ErpChargeLineData erpChargeLineData : charges) {
			ErpChargeLineModel chargeLineModel = new ErpChargeLineModel();
			chargeLineModel.setAmount(erpChargeLineData.getAmount());
			chargeLineModel.setDiscount(buildDiscount(erpChargeLineData.getDiscount()));
			chargeLineModel.setId(erpChargeLineData.getId());
			chargeLineModel.setReasonCode(erpChargeLineData.getReasonCode());
			chargeLineModel.setTaxationType(EnumTaxationType.getEnum(erpChargeLineData.getTaxationType()));
			chargeLineModel.setTaxRate(erpChargeLineData.getTaxRate());
			chargeLineModel.setType(EnumChargeType.getEnum(erpChargeLineData.getType()));
			chargeLineModelData.add(chargeLineModel);
		}
		}
		return chargeLineModelData;
	}

	private static Discount buildDiscount(DiscountData discountData) {
		Discount discount =  null;
		if(discountData != null){
			discount = new Discount(discountData.getPromotionCode(), EnumDiscountType.getPromotionType(Integer.parseInt(discountData.getDiscountType())), discountData.getAmount());
			discount.setMaxPercentageDiscount(discountData.getMaxPercentageDiscount());
			discount.setPromotionDescription(discountData.getPromotionDescription());
			discount.setSkuLimit(discountData.getSkuLimit());
		}
		return discount;
	}

	private static  ErpDeliveryInfoModel buildDeliveryInfo(ErpDeliveryInfoData deliveryInfo) {
		ErpDeliveryInfoModel deliveryInfoModel = null;
		if(deliveryInfo != null){
			deliveryInfoModel = new ErpDeliveryInfoModel();
			deliveryInfoModel.setDeliveryAddress(buildErpAddress(deliveryInfo.getDeliveryAddress()));
			deliveryInfoModel.setDeliveryCutoffTime(deliveryInfo.getDeliveryCutoffTime());
			deliveryInfoModel.setDeliveryEndTime(deliveryInfo.getDeliveryEndTime());
			deliveryInfoModel.setDeliveryHandoffTime(deliveryInfo.getDeliveryHandoffTime());
			deliveryInfoModel.setDeliveryPlantInfo(buildErpDeliveryPlantInfo(deliveryInfo.getDeliveryPlantInfo()));
			deliveryInfoModel.setDeliveryRegionId(deliveryInfo.getDeliveryRegionId());
			deliveryInfoModel.setDeliveryReservationId(deliveryInfo.getDeliveryReservationId());
			deliveryInfoModel.setDeliveryStartTime(deliveryInfo.getDeliveryStartTime());
			deliveryInfoModel.setDeliveryType(EnumDeliveryType.getDeliveryType(deliveryInfo.getDeliveryType()));
			deliveryInfoModel.setDeliveryZone(deliveryInfo.getDeliveryZone());
			deliveryInfoModel.setDepotLocationId(deliveryInfo.getDepotLocationId());
			deliveryInfoModel.setId(deliveryInfo.getId());
			deliveryInfoModel.setMinDurationForModification(deliveryInfo.getMinDurationForModification());
			deliveryInfoModel.setMinDurationForModStart(deliveryInfo.getMinDurationForModStart());
			deliveryInfoModel.setOrderMobileNumber(buildPhoneNumber(deliveryInfo.getOrderMobileNumber()));
			deliveryInfoModel.setOriginalCutoffTime(deliveryInfo.getOriginalCutoffTime());
			deliveryInfoModel.setServiceType(EnumServiceType.getEnum(deliveryInfo.getServiceType()));
		}
		return deliveryInfoModel;
	}

	private static ErpAddressModel buildErpAddress(ErpAddressData deliveryAddress) {
		ErpAddressModel erpAddressModel = null;
		if(deliveryAddress != null){
			erpAddressModel = new ErpAddressModel();
			erpAddressModel.setInstructions(deliveryAddress.getInstructions());
			erpAddressModel.setAltDelivery(EnumDeliverySetting.getDeliverySetting(deliveryAddress.getAltDeliverySetting()));
			erpAddressModel.setAltFirstName(deliveryAddress.getAltFirstName());
			erpAddressModel.setAltLastName(deliveryAddress.getAltLastName());
			erpAddressModel.setApartment(deliveryAddress.getAltApartment());
			erpAddressModel.setAltPhone(buildPhoneNumber(deliveryAddress.getAltPhone()));
			erpAddressModel.setAltContactPhone(buildPhoneNumber(deliveryAddress.getAltContactPhone()));
			erpAddressModel.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.getEnum(deliveryAddress.getUnattendedDeliveryFlag()));
			erpAddressModel.setUnattendedDeliveryInstructions(deliveryAddress.getUnattendedDeliveryInstructions());
			erpAddressModel.setCharityName(deliveryAddress.getCharityName());
			erpAddressModel.setOptInForDonation(deliveryAddress.isOptInForDonation());
			erpAddressModel.setWebServiceType(EnumWebServiceType.getEnum(deliveryAddress.getWebServiceType()));
			erpAddressModel.setEbtAccepted(deliveryAddress.isEbtAccepted());
			erpAddressModel.setScrubbedStreet(deliveryAddress.getScrubbedStreet());
			erpAddressModel.setFirstName(deliveryAddress.getFirstName());
			erpAddressModel.setLastName(deliveryAddress.getLastName());
			erpAddressModel.setPhone(buildPhoneNumber(deliveryAddress.getPhone()));
			erpAddressModel.setAddress1(deliveryAddress.getAddress1());
			erpAddressModel.setAddress2(deliveryAddress.getAddress2());
			erpAddressModel.setApartment(deliveryAddress.getApartment());
			erpAddressModel.setCity(deliveryAddress.getCity());
			erpAddressModel.setState(deliveryAddress.getState());
			erpAddressModel.setZipCode(deliveryAddress.getZipCode());
			erpAddressModel.setServiceType(EnumServiceType.getEnum(deliveryAddress.getServiceType()));
			erpAddressModel.setCompanyName(deliveryAddress.getCompanyName());
			erpAddressModel.setAddressInfo(buildAddressInfo(deliveryAddress.getAddressInfoData()));
		}
		return erpAddressModel;
	}

	private static AddressInfo buildAddressInfo(AddressInfoData addressInfoData) {
		AddressInfo addressInfo = null;
		if(addressInfoData != null){
			addressInfo = new AddressInfo();
			addressInfo.setZoneId(addressInfoData.getZoneId());
			addressInfo.setZoneCode(addressInfoData.getZoneCode());
			addressInfo.setLongitude(addressInfoData.getLongitude());
			addressInfo.setLatitude(addressInfoData.getLatitude());
			addressInfo.setScrubbedStreet(addressInfoData.getScrubbedStreet());
			addressInfo.setAddressType(EnumAddressType.getEnum(addressInfoData.getAddressType()));
			addressInfo.setCounty(addressInfoData.getCounty());
			addressInfo.setGeocodeException(addressInfoData.isGeocodeException());
			addressInfo.setBuildingId(addressInfoData.getBuildingId());
			addressInfo.setLocationId(addressInfoData.getLocationId());
			addressInfo.setSsScrubbedAddress(addressInfoData.getSsScrubbedAddress());
		}
			
		return addressInfo;
	}

	private static ErpDeliveryPlantInfoModel buildErpDeliveryPlantInfo(ErpDeliveryPlantInfoData deliveryPlantInfo) {
		ErpDeliveryPlantInfoModel plantinfomodel =  null;
		if(deliveryPlantInfo != null) {
			plantinfomodel = new ErpDeliveryPlantInfoModel();
			plantinfomodel.setCatalogKey(buildCatalogKey(deliveryPlantInfo.getCatalogKey()));
			plantinfomodel.setDistChannel(deliveryPlantInfo.getDistChannel());
			plantinfomodel.setDivision(deliveryPlantInfo.getDivision());
			plantinfomodel.setPlantId(deliveryPlantInfo.getPlantId());
			plantinfomodel.setSalesOrg(deliveryPlantInfo.getSalesOrg());
		}
		return plantinfomodel;
	}

	private static CatalogKey buildCatalogKey(CatalogKeyData catalogKey) {
		CatalogKey key  = new CatalogKey(catalogKey.geteStore(), catalogKey.getPlantId(),buildZoneInfo(catalogKey.getPricingZone()));
		return key;
	}

	private static ZoneInfo buildZoneInfo(ZoneInfoData pricingZone) {
		ZoneInfo zoneInfo =null;
		if(pricingZone.getParent()==null){
		  zoneInfo  = new ZoneInfo(pricingZone.getZoneId(), 
				  pricingZone.getSalesOrg(), 
				  pricingZone.getDistributionChanel()
				);
		}else {
			
			  zoneInfo  = new ZoneInfo(pricingZone.getZoneId(), 
					  pricingZone.getSalesOrg(), 
					  pricingZone.getDistributionChanel(),
					PricingIndicator.valueOf(pricingZone.getPricingIndicator()),
					buildZoneInfo(pricingZone.getParent())
					);
		}
		
		return zoneInfo;
	}

	private static PhoneNumber buildPhoneNumber(PhoneNumberData phone) {
		PhoneNumber phoneNumber = null;
		if(phone != null)
		phoneNumber = new PhoneNumber(phone.getPhone(), phone.getExtension(), phone.getType());
		return phoneNumber;
	}
	
	private static List<ErpAppliedCreditModel> buildAppliedCredits(List<ErpAppliedCreditData> appliedCredits) {
		 List<ErpAppliedCreditModel> appliedCreditModeList = new ArrayList<ErpAppliedCreditModel>();
		 if(appliedCredits != null){
		for (ErpAppliedCreditData erpAppliedCreditData : appliedCredits) {
			ErpAppliedCreditModel appliedCreditModel = new ErpAppliedCreditModel();
			ErpCreditModel credit = new ErpCreditModel(erpAppliedCreditData.getDepartment(),erpAppliedCreditData.getAmount(),buildErpAffiliate(erpAppliedCreditData.getAffiliate())) {
			};
			appliedCreditModel = (ErpAppliedCreditModel) credit;
			//appliedCreditModel.setAffiliate(buildErpAffiliate(erpAppliedCreditData.getAffiliate()));
			appliedCreditModel.setCustomerCreditPk(new PrimaryKey(erpAppliedCreditData.getCustomerCreditPk()));
//			/appliedCreditModel.setAmount(erpAppliedCreditData.getAmount());
			//appliedCreditModel.setDepartment();
			appliedCreditModel.setId(erpAppliedCreditData.getCreditId());
			appliedCreditModel.setSapNumber(erpAppliedCreditData.getSapNumber());
			appliedCreditModeList.add(appliedCreditModel);
		}
		 }
		return appliedCreditModeList;
	}

	private  static List<ErpDiscountLineModel> buildDiscountDataList(List<ErpDiscountLineData> discounts) {
		List<ErpDiscountLineModel> discountLineModelList = new ArrayList<ErpDiscountLineModel>();
		if(discounts != null){
		for (ErpDiscountLineData discountData : discounts) {
			ErpDiscountLineModel discountLineModel = new ErpDiscountLineModel(buildDiscount(discountData.getDiscount()));
			discountLineModel.setId(discountData.getId());
			discountLineModelList.add(discountLineModel);
		}
		}
		return discountLineModelList;
	}

}
