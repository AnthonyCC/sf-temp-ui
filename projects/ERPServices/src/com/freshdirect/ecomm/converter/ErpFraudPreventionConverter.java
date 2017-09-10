package com.freshdirect.ecomm.converter;

import java.util.ArrayList;
import java.util.Collection;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.ecommerce.data.customer.CrmAgentRoleData;
import com.freshdirect.ecommerce.data.customer.OrderFraudData;
import com.freshdirect.ecommerce.data.dlv.AddressInfoData;
import com.freshdirect.ecommerce.data.dlv.ContactAddressData;
import com.freshdirect.ecommerce.data.dlv.ErpAddressData;
import com.freshdirect.ecommerce.data.dlv.PhoneNumberData;
import com.freshdirect.ecommerce.data.sap.ErpCustomerData;
import com.freshdirect.ecommerce.data.sap.ErpCustomerInfoData;
import com.freshdirect.ecommerce.data.survey.FDIdentityData;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;

public class ErpFraudPreventionConverter {

	public static ErpCustomerData buildErpCustomerData(ErpCustomerModel erpCustomer) {
		ErpCustomerData erpCustomerData = null;
		if(erpCustomer != null){
		erpCustomerData  = new ErpCustomerData();
		ErpCustomerInfoData erpCustomerInfoData =  new ErpCustomerInfoData();
		ErpCustomerInfoModel erpcustomerInfoModel = erpCustomer.getCustomerInfo();
		if(erpcustomerInfoModel != null){
			erpCustomerInfoData.setId(erpcustomerInfoModel.getId());
			erpCustomerInfoData.setTitle(erpcustomerInfoModel.getTitle());
			erpCustomerInfoData.setFirstName(erpcustomerInfoModel.getFirstName());
			erpCustomerInfoData.setLastName(erpcustomerInfoModel.getLastName());
			erpCustomerInfoData.setMiddleName(erpcustomerInfoModel.getMiddleName());
			erpCustomerInfoData.setDisplayName(erpcustomerInfoModel.getDisplayName());
			erpCustomerInfoData.setEmail(erpcustomerInfoModel.getEmail());
			erpCustomerInfoData.setAlternateEmail(erpcustomerInfoModel.getAlternateEmail());
			erpCustomerInfoData.setEmailPlaintext(erpcustomerInfoModel.isEmailPlaintext());
			erpCustomerInfoData.setReceiveNewsletter(erpcustomerInfoModel.isReceiveNewsletter());
			erpCustomerInfoData.setHomePhone(buildPhoneNumberData(erpcustomerInfoModel.getHomePhone()));
			erpCustomerInfoData.setCellPhone(buildPhoneNumberData(erpcustomerInfoModel.getCellPhone()));
			erpCustomerInfoData.setBusinessPhone(buildPhoneNumberData(erpcustomerInfoModel.getBusinessPhone()));
			erpCustomerInfoData.setOtherPhone(buildPhoneNumberData(erpcustomerInfoModel.getOtherPhone()));
			erpCustomerInfoData.setFax(buildPhoneNumberData(erpcustomerInfoModel.getFax()));
			erpCustomerInfoData.setWorkDepartment(erpcustomerInfoModel.getWorkDepartment());
			erpCustomerInfoData.setEmployeeId(erpcustomerInfoModel.getEmployeeId());
			erpCustomerInfoData.setLastReminderEmail(erpcustomerInfoModel.getLastReminderEmailSend());
			erpCustomerInfoData.setReminderDayOfWeek(erpcustomerInfoModel.getReminderDayOfWeek());
			erpCustomerInfoData.setReminderFrequency(erpcustomerInfoModel.getReminderFrequency());
			erpCustomerInfoData.setReminderAltEmail(erpcustomerInfoModel.isReminderAltEmail());
			erpCustomerInfoData.setRsvDayOfWeek(erpcustomerInfoModel.getRsvDayOfWeek());
			erpCustomerInfoData.setRsvStartTime(erpcustomerInfoModel.getRsvStartTime());
			erpCustomerInfoData.setRsvEndTime(erpcustomerInfoModel.getRsvEndTime());
			erpCustomerInfoData.setRsvAddressId(erpcustomerInfoModel.getRsvAddressId());
			erpCustomerInfoData.setUnsubscribeDate(erpcustomerInfoModel.getUnsubscribeDate());
			erpCustomerInfoData.setReceive_emailLevel(erpcustomerInfoModel.getEmailPreferenceLevel());
			erpCustomerInfoData.setNoContactMail(erpcustomerInfoModel.isNoContactMail());
			erpCustomerInfoData.setNoContactPhone(erpcustomerInfoModel.isNoContactPhone());
			erpCustomerInfoData.setReceiveOptinNewsletter(erpcustomerInfoModel.isReceiveOptinNewsletter());
			erpCustomerInfoData.setRegRefTrackingCode(erpcustomerInfoModel.getRegRefTrackingCode());
			erpCustomerInfoData.setReferralProgId(erpcustomerInfoModel.getReferralProgId());
			erpCustomerInfoData.setReferralProgInvtId(erpcustomerInfoModel.getReferralProgInvtId());
			erpCustomerInfoData.setHasAutoRenewDP(erpcustomerInfoModel.getHasAutoRenewDP());
			erpCustomerInfoData.setAutoRenewDPSKU(erpcustomerInfoModel.getAutoRenewDPSKU());
			erpCustomerInfoData.setMobileNumber(buildPhoneNumberData(erpcustomerInfoModel.getMobileNumber()));
			erpCustomerInfoData.setMobilePrefs(erpcustomerInfoModel.getMobilePreference());
			erpCustomerInfoData.setDeliveryNotification(erpcustomerInfoModel.isDeliveryNotification());
			erpCustomerInfoData.setOffersNotification(erpcustomerInfoModel.isOffersNotification());
			erpCustomerInfoData.setGoGreen(erpcustomerInfoModel.isGoGreen());
			erpCustomerInfoData.setDpTcViewCount(erpcustomerInfoModel.getDpTcViewCount());
			erpCustomerInfoData.setDpTcAgreeDate(erpcustomerInfoModel.getDpTcAgreeDate());
			erpCustomerInfoData.setIndustry(erpcustomerInfoModel.getIndustry());
			erpCustomerInfoData.setNumOfEmployees(erpcustomerInfoModel.getNumOfEmployees());
			erpCustomerInfoData.setSecondEmailAddress(erpcustomerInfoModel.getSecondEmailAddress());
			if(erpcustomerInfoModel.getOrderNotices() != null)
			erpCustomerInfoData.setOrderNotices(erpcustomerInfoModel.getOrderNotices().value());
			if(erpcustomerInfoModel.getOrderExceptions() != null)
			erpCustomerInfoData.setOrderExceptions(erpcustomerInfoModel.getOrderExceptions().value());
			if(erpcustomerInfoModel.getOffers() != null)
			erpCustomerInfoData.setOffers(erpcustomerInfoModel.getOffers().value());
			if(erpcustomerInfoModel.getPartnerMessages() != null)
			erpCustomerInfoData.setPartnerMessages(erpcustomerInfoModel.getPartnerMessages().value());
			erpCustomerInfoData.setSmsPreferenceflag(erpcustomerInfoModel.getSmsPreferenceflag());
			erpCustomerInfoData.setSmsOptinDate(erpcustomerInfoModel.getSmsOptinDate());
			erpCustomerInfoData.setCompanyNameSignup(erpcustomerInfoModel.getCompanyNameSignup());
			erpCustomerInfoData.setSoCartOverlayFirstTime(erpcustomerInfoModel.getSoCartOverlayFirstTime());
			erpCustomerInfoData.setSoFeatureOverlay(erpcustomerInfoModel.getSoFeatureOverlay());
			erpCustomerInfoData.setFdTcAgree(erpcustomerInfoModel.getFdTcAgree());
			erpCustomerInfoData.setIdentity(buildIdentityData(new FDIdentity(erpCustomer.getPK().getId())));
			erpCustomerData.setCustomerInfo(erpCustomerInfoData);
		}
			erpCustomerData.setActive(erpCustomer.isActive());
			erpCustomerData.setId(erpCustomer.getId());
			erpCustomerData.setPasswordHash(erpCustomer.getPasswordHash());
			erpCustomerData.setSapBillToAddress(buildContactAddressData(erpCustomer.getSapBillToAddress()));
			erpCustomerData.setSapId(erpCustomer.getSapId());
			erpCustomerData.setSocialLoginOnly(erpCustomer.isSocialLoginOnly());
			erpCustomerData.setUserId(erpCustomer.getUserId());
		}
		return erpCustomerData;
	}
	
	public static FDIdentityData buildIdentityData(FDIdentity identity) {
		FDIdentityData fdidentity = new FDIdentityData();
		fdidentity.setErpCustomerPK(identity.getErpCustomerPK());
		fdidentity.setFdCustomerPK(identity.getFDCustomerPK());
		return fdidentity;
	}

	public static OrderFraudData buildOrderFraudData(PrimaryKey erpCustomerPk,ErpAbstractOrderModel order, CrmAgentRole agentRole,PrimaryKey saleId) {
		OrderFraudData orderFraud = new OrderFraudData();
		orderFraud.setAbstractOrderModel(SapGatewayConverter.buildOrderData(order));
		orderFraud.setAgentData(buildCrmAgentRoleData(agentRole));
		orderFraud.setErpCustomerId(erpCustomerPk.getId());
		if(saleId != null)
		orderFraud.setSaleId(saleId.getId());
		return orderFraud;
	}

	private static CrmAgentRoleData buildCrmAgentRoleData(CrmAgentRole agentRole) {
		CrmAgentRoleData agentRoleData = new CrmAgentRoleData();
		if(agentRole != null){
		agentRoleData.setAgentRole(agentRole.getName());
		agentRoleData.setLdapRoleName(agentRole.getLdapRoleName());
		}
		return agentRoleData;
	}

	public static ErpAddressData buildErpAddressData(ErpAddressModel address) {
		return SapGatewayConverter.buildErpAddressData(address);
	}

	public static ContactAddressData buildContactAddressData(ContactAddressModel address) {
		ContactAddressData contactAddressData  = null;
		if(address != null){
		contactAddressData = buildAddressData(address);
		contactAddressData.setFirstName(address.getFirstName());
		contactAddressData.setLastName(address.getLastName());
		contactAddressData.setPhone(buildPhoneNumberData(address.getPhone()));
		contactAddressData.setCustomerId(address.getCustomerId());
		}
		
		return contactAddressData;
	}
	
	public static PhoneNumberData buildPhoneNumberData(PhoneNumber phone) {
		if(phone != null){
		return new PhoneNumberData(phone.getPhone(), NVL.apply(phone.getExtension(), ""), NVL.apply(phone.getType(), ""));
		}
		return null;
	}
	public static ContactAddressData  buildAddressData(ContactAddressModel address) {
		ContactAddressData contactAddress = null;
		if(address != null){
			contactAddress = new ContactAddressData();
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
		}
		return contactAddress;
		
	}
	
	public static AddressInfoData buildAddressInfoData(AddressInfo addressInfo) {
		AddressInfoData addressInfoData =  null;
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

	public static Collection<PhoneNumberData> buildPhoneNumberCollectionData(
			Collection<PhoneNumber> phones) {
		Collection<PhoneNumberData> phoneNumberData = new ArrayList<PhoneNumberData>();
		for (PhoneNumber phoneNumber : phones) {
			phoneNumberData.add(buildPhoneNumberData(phoneNumber));
		}
		return phoneNumberData;
	}



}
