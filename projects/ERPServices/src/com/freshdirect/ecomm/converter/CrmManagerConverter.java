package com.freshdirect.ecomm.converter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmAgentInfo;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthInfo;
import com.freshdirect.crm.CrmAuthSearchCriteria;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmCustomerHeaderInfo;
import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmQueueInfo;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumCannedTextCategory;
import com.freshdirect.customer.ErpCannedText;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.ecommerce.data.crm.AuthorisationData;
import com.freshdirect.ecommerce.data.crm.CannedTextData;
import com.freshdirect.ecommerce.data.crm.CrmAgentInfoData;
import com.freshdirect.ecommerce.data.crm.CrmAuthInfoData;
import com.freshdirect.ecommerce.data.crm.CrmAuthSearchCriteriaData;
import com.freshdirect.ecommerce.data.crm.CrmCaseData;
import com.freshdirect.ecommerce.data.crm.CrmCaseInfoData;
import com.freshdirect.ecommerce.data.crm.CrmCaseOperationData;
import com.freshdirect.ecommerce.data.crm.CrmCaseTemplateData;
import com.freshdirect.ecommerce.data.crm.CrmCustomerHeaderInfoData;
import com.freshdirect.ecommerce.data.crm.CrmDeliveryPassData;
import com.freshdirect.ecommerce.data.crm.CrmLateIssueData;
import com.freshdirect.ecommerce.data.crm.CrmQueueInfoData;
import com.freshdirect.ecommerce.data.crm.CrmStatusData;
import com.freshdirect.ecommerce.data.crm.DownloadCaseData;
import com.freshdirect.ecommerce.data.crm.ErpTruckInfoData;
import com.freshdirect.ecommerce.data.crm.ViewAccountData;
import com.freshdirect.ecommerce.data.customer.CrmAgentRoleData;
import com.freshdirect.ecommerce.data.dlv.PhoneNumberData;
import com.freshdirect.ecommerce.data.dlvpass.DeliveryPassData;
import com.freshdirect.ecommerce.data.ecoupon.CrmAgentModelData;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.payment.service.ModelConverter;

public class CrmManagerConverter {

	public static CrmCaseTemplateData buildCrmCaseTemplateData(CrmCaseTemplate template) {
		CrmCaseTemplateData crmCaseTemplate = new CrmCaseTemplateData();
		crmCaseTemplate.setAssignedAgentPK(template.getAssignedAgentPK().getId());
		crmCaseTemplate.setCustomerPK(template.getCustomerPK().getId());
		crmCaseTemplate.setEndDate(template.getEndDate());
		crmCaseTemplate.setEndRecord(template.getEndRecord());
		crmCaseTemplate.setId(template.getId());
		if(template.getLockedAgentPK() != null)
		crmCaseTemplate.setLockedAgentPK(template.getLockedAgentPK().getId());
		if(template.getOrigin() != null)
		crmCaseTemplate.setOriginCode(template.getOrigin().getCode());
		if(template.getPriority() != null)
		crmCaseTemplate.setPriorityCode(template.getPriority().getCode());
		if(template.getQueue() != null)
		crmCaseTemplate.setQueueCode(template.getQueue().getCode());
		if(template.getSalePK() != null)
		crmCaseTemplate.setSalePK(template.getSalePK().getId());
		crmCaseTemplate.setSortBy(template.getSortBy());
		crmCaseTemplate.setStartDate(template.getStartDate());
		crmCaseTemplate.setStartRecord(template.getStartRecord());
		crmCaseTemplate.setStateCodes(template.getStates());
		if(template.getSubject() != null)
		crmCaseTemplate.setSubjectCode(template.getSubject().getCode());
		crmCaseTemplate.setSummary(template.getSummary());
		crmCaseTemplate.setTotalRows(template.getTotalRows());
		return crmCaseTemplate;
	}

	public static List<CrmCaseModel> buildCrmCaseModelList(List<CrmCaseData> data) {
		 List<CrmCaseModel> crmCaseModelList = new  ArrayList<CrmCaseModel>();
		for (CrmCaseData crmCase : data) {
			CrmCaseModel crmCaseModel = new CrmCaseModel();
			crmCaseModel.setActions(crmCase.getActions());
			crmCaseModel.setCaseInfo(buildCrmCaseInfo(crmCase.getCaseInfo()));
			crmCaseModelList.add(crmCaseModel);
		}
		return crmCaseModelList;}

	private static CrmCaseInfo buildCrmCaseInfo(CrmCaseInfoData caseInfo) {
		CrmCaseInfo crmCaseInfoModel = new CrmCaseInfo();
		crmCaseInfoModel.setId(caseInfo.getId());
		crmCaseInfoModel.setActualQuantity(caseInfo.getActualQuantity());
		crmCaseInfoModel.setAssignedAgentPK(new PrimaryKey(caseInfo.getAssignedAgentPK()));
		crmCaseInfoModel.setCartonNumbers(caseInfo.getCartonNumbers());
		crmCaseInfoModel.setCreateDate(new Timestamp(caseInfo.getCreateDate()));
		crmCaseInfoModel.setCrmCaseMedia(caseInfo.getCrmCaseMedia());
		crmCaseInfoModel.setCustomerFirstName(caseInfo.getCustomerFirstName());
		crmCaseInfoModel.setCustomerLastName(caseInfo.getCustomerLastName());
		crmCaseInfoModel.setCustomerPK(new PrimaryKey(caseInfo.getCustomerPK()));
		crmCaseInfoModel.setCustomerTone(caseInfo.getCustomerTone());
		crmCaseInfoModel.setDepartments((caseInfo.getDepartmentCodes()));
		crmCaseInfoModel.setFirstContactForIssue(caseInfo.getFirstContactForIssue());
		crmCaseInfoModel.setFirstContactResolved(caseInfo.getFirstContactResolved());
		crmCaseInfoModel.setLastModDate(new Timestamp(caseInfo.getLastModDate()));
		if(caseInfo.getLockedAgentPK() != null)
		crmCaseInfoModel.setLockedAgentPK(new PrimaryKey(caseInfo.getLockedAgentPK()));
		crmCaseInfoModel.setMoreThenOneIssue(caseInfo.getMoreThenOneIssue());
		if( caseInfo.getOriginCode() != null)
		crmCaseInfoModel.setOrigin(CrmCaseOrigin.getEnum(caseInfo.getOriginCode()));
		if(caseInfo.getPriorityCode() != null)
		crmCaseInfoModel.setPriority(CrmCasePriority.getEnum(caseInfo.getPriorityCode()));
		crmCaseInfoModel.setPrivateCase(caseInfo.isPrivateCase());
		crmCaseInfoModel.setProjectedQuantity(caseInfo.getProjectedQuantity());
		crmCaseInfoModel.setResonForNotResolve(caseInfo.getResonForNotResolve());
		if(caseInfo.getSalePK() != null)
		crmCaseInfoModel.setSalePK(new PrimaryKey(caseInfo.getSalePK()));
		crmCaseInfoModel.setSatisfiedWithResolution(caseInfo.getSatisfiedWithResolution());
		if(caseInfo.getStateCode() != null)
		crmCaseInfoModel.setState(CrmCaseState.getEnum(caseInfo.getStateCode()));
		if(caseInfo.getSubjectCode() != null)
		crmCaseInfoModel.setSubject(CrmCaseSubject.getEnum(caseInfo.getSubjectCode()));
		crmCaseInfoModel.setSubjectName(caseInfo.getSubjectName());
		crmCaseInfoModel.setSummary(caseInfo.getSummary());
		return crmCaseInfoModel;
	}

	public static List<CrmQueueInfo> buildCrmQueueInfoList(List<CrmQueueInfoData> data) {
		List<CrmQueueInfo> crmQueueInfoList = new ArrayList<CrmQueueInfo>();
		for (CrmQueueInfoData crmQueueInfoData : data) {
			CrmQueueInfo crmQueueInfo= new CrmQueueInfo(CrmCaseQueue.getEnum(crmQueueInfoData.getQueueCode()), crmQueueInfoData.getOpen(), crmQueueInfoData.getUnassigned(), new Timestamp(crmQueueInfoData.getOldest()));
			crmQueueInfoList.add(crmQueueInfo);
		}
		return crmQueueInfoList;
	}

	public static List<CrmAgentInfo> buildCrmAgentInfoList(List<CrmAgentInfoData> data) {
		List<CrmAgentInfo> crmAgentInfoList = new ArrayList<CrmAgentInfo>();
		for (CrmAgentInfoData crmAgentData : data) {
			CrmAgentInfo crmAgentInfo = new CrmAgentInfo(new PrimaryKey(crmAgentData.getAgentPK()),
					crmAgentData.getAssigned(), crmAgentData.getOpen(), crmAgentData.getReview(),
					crmAgentData.getClosed(), crmAgentData.getOldest(), crmAgentData.getFirstName(),
					crmAgentData.getLastName(), crmAgentData.isActive(), CrmAgentRole.getEnum(crmAgentData.getRole()),
					crmAgentData.getUserId());
			crmAgentInfoList.add(crmAgentInfo);
		}
		return crmAgentInfoList;
	}

	public static List<CrmCaseOperation> buildCrmCaseOperationList(List<CrmCaseOperationData> data) {
		List<CrmCaseOperation> crmCaseOperationList = new ArrayList<CrmCaseOperation>();
		for (CrmCaseOperationData crmCaseOperationData : data) {
			CrmCaseOperation crmCaseOperation = new CrmCaseOperation(CrmAgentRole.getEnum(crmCaseOperationData.getRoleCode()),
					CrmCaseSubject.getEnum(crmCaseOperationData.getSubjectCode()), CrmCaseState.getEnum(crmCaseOperationData.getStartStateCode()),
					CrmCaseState.getEnum(crmCaseOperationData.getEndStateCode()), CrmCaseActionType.getEnum(crmCaseOperationData.getActionTypeCode()));
			crmCaseOperationList.add(crmCaseOperation);
		}
		return crmCaseOperationList;
	}

	public static DownloadCaseData buildCrmDownloadCaseData(PrimaryKey agentPK,String queue, String subject, int numberToDownload) {
		DownloadCaseData downloadCaseData = new DownloadCaseData();
		downloadCaseData.setAgentPK(agentPK.getId());
		downloadCaseData.setNumberToDownload(numberToDownload);
		downloadCaseData.setQueue(queue);
		downloadCaseData.setSubject(subject);
		return downloadCaseData;
	}

	public static CrmStatus buildCrmStatus(CrmStatusData data) {
		CrmStatus crmStatus = new CrmStatus(new PrimaryKey(data.getAgentPK()));
		crmStatus.setCaseId(data.getCaseId());
		crmStatus.setSaleId(data.getSaleId());
		crmStatus.setErpCustomerId(data.getErpCustomerId());
		return crmStatus;
	}

	public static CrmStatusData buildCrmStatusData(CrmStatus sessionStatus) {
		CrmStatusData crmStatus = new CrmStatusData();
		crmStatus.setAgentPK(sessionStatus.getAgentPK().getId());
		crmStatus.setCaseId(sessionStatus.getCaseId());
		crmStatus.setErpCustomerId(sessionStatus.getErpCustomerId());
		crmStatus.setSaleId(sessionStatus.getSaleId());
		return crmStatus;
	}

	public static CrmLateIssueData buildCrmLateIssueData(CrmLateIssueModel lateIssue) {
		CrmLateIssueData lateIssueData = new CrmLateIssueData();
		lateIssueData.setActualStopCount(lateIssue.getActualStopCount());
		lateIssueData.setActualStopsText(lateIssue.getActualStopsText());
		lateIssueData.setAgentUserId(lateIssue.getAgentUserId());
		lateIssueData.setComments(lateIssue.getComments());
		lateIssueData.setDelayMinutes(lateIssue.getDelayMinutes());
		lateIssueData.setDelivery_window(lateIssue.getDelivery_window());
		lateIssueData.setDeliveryDate(lateIssue.getDeliveryDate().getTime());
		lateIssueData.setId(lateIssue.getId());
		lateIssueData.setReportedAt(lateIssue.getReportedAt().getTime());
		lateIssueData.setReportedBy(lateIssue.getReportedBy());
		lateIssueData.setReportedStopsText(lateIssue.getReportedStopsText());
		lateIssueData.setRoute(lateIssue.getRoute());
		TreeMap map = new TreeMap();
		map.putAll(lateIssue.getStopsAndOrders());
		lateIssueData.setStopsAndOrders(map);
		return lateIssueData;
	}

	public static CrmLateIssueModel buildCrmLateIssueModel(CrmLateIssueData data) {
		CrmLateIssueModel lateIssueModel = new CrmLateIssueModel(new PrimaryKey(data.getId()));
		lateIssueModel.setActualStopCount(data.getActualStopCount());
		lateIssueModel.setActualStopsText(data.getActualStopsText());
		lateIssueModel.setAgentUserId(data.getAgentUserId());
		lateIssueModel.setComments(data.getComments());
		lateIssueModel.setDelayMinutes(data.getDelayMinutes());
		lateIssueModel.setDelivery_window(data.getDelivery_window());
		lateIssueModel.setDeliveryDate(new java.sql.Date(data.getDeliveryDate()));
		lateIssueModel.setId(data.getId());
		lateIssueModel.setReportedAt(new Timestamp(data.getReportedAt()));
		lateIssueModel.setReportedBy(data.getReportedBy());
		lateIssueModel.setReportedStopsText(data.getReportedStopsText());
		lateIssueModel.setRoute(data.getRoute());
		lateIssueModel.setStopsAndOrders(data.getStopsAndOrders());
		return lateIssueModel;
	}

	public static Collection<CrmLateIssueModel> buildCrmLateIssueModelCollection(Collection<CrmLateIssueData> data) {
		Collection<CrmLateIssueModel> crmLateIssueModel = new ArrayList<CrmLateIssueModel>();
		for (CrmLateIssueData crmLateIssueData : data) {
			crmLateIssueModel.add(buildCrmLateIssueModel(crmLateIssueData));
		}
		return crmLateIssueModel;
	}

	public static List<ErpTruckInfo> buildErpTruckInfoList(List<ErpTruckInfoData> data) {
		List<ErpTruckInfo> erpTruckInfoList = new ArrayList<ErpTruckInfo>();
		for (ErpTruckInfoData erpTruckInfoData : data) {
			ErpTruckInfo truckInfoData = new ErpTruckInfo(erpTruckInfoData.getTruckNumber(), erpTruckInfoData.getEnrouteOrders(), erpTruckInfoData.getTotalOrders());
			erpTruckInfoList.add(truckInfoData);
		}
		return erpTruckInfoList;
	}

	public static CrmCustomerHeaderInfo buildCrmCustomerHeaderInfo(CrmCustomerHeaderInfoData data) {
		CrmCustomerHeaderInfo crmHeaderInfo = new CrmCustomerHeaderInfo(data.getId(), data.getEmail());
		crmHeaderInfo.setActive(data.isActive());
		crmHeaderInfo.setBusinessPhone(buildPhone(data.getBusinessPhone()));
		crmHeaderInfo.setCases(data.getCases());
		crmHeaderInfo.setCellPhone(buildPhone(data.getCellPhone()));
		crmHeaderInfo.setCredits(data.getCredits());
		crmHeaderInfo.setFirstName(data.getFirstName());
		crmHeaderInfo.setHomePhone(buildPhone(data.getHomePhone()));
		crmHeaderInfo.setLastName(data.getLastName());
		crmHeaderInfo.setMiddleName(data.getMiddleName());
		crmHeaderInfo.setOnAlert(data.isOnAlert());
		crmHeaderInfo.setRemainingAmount(data.getRemainingAmount());
		return crmHeaderInfo;
	}
	
	private static PhoneNumber buildPhone(PhoneNumberData phone) {
		if(phone != null){
		return new PhoneNumber(phone.getPhone(), NVL.apply(phone.getExtension(), ""), NVL.apply(phone.getType(), ""));
		}
		return null;
	}

	public static CrmDeliveryPassData buildCrmDlvPassData(DeliveryPassModel model, CrmAgentModel agentmodel, String note,
			String reasonCode, String saleId) {
		CrmDeliveryPassData deliveryPassData = new CrmDeliveryPassData();
		deliveryPassData.setAgentmodel(buildCrmAgentModelData(agentmodel));
		deliveryPassData.setDeliveryPassModel(buildDlvPassData(model));
		deliveryPassData.setNote(note);
		deliveryPassData.setReasonCode(reasonCode);
		deliveryPassData.setSaleId(saleId);
		return deliveryPassData;
	}

	public static DeliveryPassData buildDlvPassData(DeliveryPassModel model) {
		return ModelConverter.buildDeliveryPassData(model);
	}

	public static CrmAgentModelData buildCrmAgentModelData(CrmAgentModel agent) {
		CrmAgentModelData crmAgentData = new CrmAgentModelData();
		crmAgentData.setActive(agent.isActive());
		crmAgentData.setAgentCaseQueues(buildCrmCaseQueuesData(agent.getAgentQueues()));
		crmAgentData.setCreateDate(agent.getCreateDate());
		crmAgentData.setCurFacilityContext(agent.getCurFacilityContext());
		crmAgentData.setFirstName(agent.getFirstName());
		crmAgentData.setCrmAgentId(agent.getId());
		crmAgentData.setLastName(agent.getLastName());
		crmAgentData.setLdapId(agent.getLdapId());
		crmAgentData.setMasqueradeAllowed(agent.isMasqueradeAllowed());
		crmAgentData.setPassword(agent.getPassword());
		if(agent.getRole() != null)
		crmAgentData.setRoleCode(agent.getRole().getName());
		crmAgentData.setUserId(agent.getUserId());
		return crmAgentData;
		
	}

	private static List<String> buildCrmCaseQueuesData(List<CrmCaseQueue> agentQueues) {
		List<String> crmCaseQueue = new ArrayList<String>();
		for (CrmCaseQueue caseQueue : agentQueues) {
			crmCaseQueue.add(caseQueue.getName());
		}
		return crmCaseQueue;
	}

	public static ViewAccountData buildViewAccountData(CrmAgentModel agent,String customerID, EnumAccountActivityType activityType,
			String maskedAcctNumber) {
		ViewAccountData accountData = new ViewAccountData();
		accountData.setActivityType(activityType.getName());
		accountData.setAgent(buildCrmAgentModelData(agent));
		accountData.setCustomerID(customerID);
		accountData.setMaskedAcctNumber(maskedAcctNumber);
		return accountData;
	}

	public static CannedTextData buildCannedTextData(ErpCannedText cannedText) {
		CannedTextData cannedTextData = new CannedTextData();
		if(cannedText.getCategory()!= null)
		cannedTextData.setCategory(cannedText.getCategory().getName());
		cannedTextData.setId(cannedText.getId());
		cannedTextData.setName(cannedText.getName());
		cannedTextData.setText(cannedText.getText());
		return cannedTextData;
	}

	public static ErpCannedText builderpCannedText(CannedTextData data) {
		ErpCannedText cannedText = new ErpCannedText(data.getId(), data.getName(), EnumCannedTextCategory.getEnum(data.getCategory()), data.getText());
		return cannedText;
	}

	public static Collection<ErpCannedText> builderpCannedTextCollection(Collection<CannedTextData> data) {
		Collection<ErpCannedText> cannedTextList = new ArrayList<ErpCannedText>(10); 
		for (CannedTextData erpCannedText : data) {
			cannedTextList.add(builderpCannedText(erpCannedText));
		}
		return cannedTextList;
		
	}

	public static AuthorisationData buildAuthorizationData(CrmAgentRole role,CrmAuthSearchCriteria filter) {
		AuthorisationData authorisationData = new AuthorisationData();
		authorisationData.setAgentRole(buildCrmAgentRoleData(role));
		authorisationData.setSearchCriteria(buildCrmSearchCriteria(filter));
		return authorisationData;
	}

	private static CrmAuthSearchCriteriaData buildCrmSearchCriteria(CrmAuthSearchCriteria filter) {
		CrmAuthSearchCriteriaData searchCriteria = new CrmAuthSearchCriteriaData();
		searchCriteria.setAmount(filter.getAmount());
		searchCriteria.setCustomerName(filter.getCustomerName());
		searchCriteria.setFromDate(filter.getFromDate());
		searchCriteria.setFromDateStr(filter.getFromDateStr());
		searchCriteria.setToDate(filter.getToDate());
		searchCriteria.setToDateStr(filter.getToDateStr());
		return searchCriteria;
	}

	public static CrmAgentRoleData buildCrmAgentRoleData(CrmAgentRole agentRoleData) {
		CrmAgentRoleData agentRole = new CrmAgentRoleData();
		agentRole.setAgentRole(agentRoleData.getCode());
		agentRole.setLdapRoleName(agentRoleData.getLdapRoleName());
		return agentRole;
	}
	public static List<CrmAuthInfo> buildCrmAuthInfoList(List<CrmAuthInfoData> data) {
		List<CrmAuthInfo> crmAuthInfoList = new ArrayList<CrmAuthInfo>();
		for (CrmAuthInfoData crmAuthInfoData : data) {
			crmAuthInfoList.add(buildCrmAuthInfo(crmAuthInfoData));
		}
		return crmAuthInfoList;
	}

	private static CrmAuthInfo buildCrmAuthInfo(CrmAuthInfoData crmAuthInfoData) {
		CrmAuthInfo crmAuthInfo = new CrmAuthInfo();
		crmAuthInfo.setAddress(crmAuthInfoData.getAddress());
		crmAuthInfo.setAmount(crmAuthInfoData.getAmount());
		crmAuthInfo.setApprovalCode(crmAuthInfoData.getApprovalCode());
		crmAuthInfo.setAuthResponse(crmAuthInfoData.getAuthResponse());
		crmAuthInfo.setCardType(EnumCardType.getEnum(crmAuthInfoData.getCardType()));
		crmAuthInfo.setCustomerId(crmAuthInfoData.getCustomerId());
		crmAuthInfo.setCustomerName(crmAuthInfoData.getCustomerName());
		crmAuthInfo.setCvvResponseCode(crmAuthInfoData.getCvvResponseCode());
		crmAuthInfo.setMerchantId(crmAuthInfoData.getMerchantId());
		crmAuthInfo.setOrder(crmAuthInfoData.getOrder());
		crmAuthInfo.setTransactionTime(new Timestamp(crmAuthInfoData.getTransactionTime()));
		crmAuthInfo.setValidOrder(crmAuthInfoData.isValidOrder());
		crmAuthInfo.setZipCheckReponse(crmAuthInfoData.getZipCheckReponse());
		return crmAuthInfo;
	}

	public static DeliveryPassModel buildDlvPassModel(DeliveryPassData data) {
		return ModelConverter.buildDeliveryPassModel(data);
	}

}