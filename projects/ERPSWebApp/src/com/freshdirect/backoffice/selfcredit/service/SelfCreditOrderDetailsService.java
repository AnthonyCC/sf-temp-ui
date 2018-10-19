package com.freshdirect.backoffice.selfcredit.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.backoffice.selfcredit.data.ComplaintReason;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditComplaintReason;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderDetailsData;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderItemData;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartonInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.taglib.callcenter.ComplaintUtil;

public class SelfCreditOrderDetailsService {

    private static final Logger LOGGER = LoggerFactory.getInstance(SelfCreditOrderDetailsService.class);
    private static final SelfCreditOrderDetailsService INSTANCE = new SelfCreditOrderDetailsService();

    private SelfCreditOrderDetailsService() {
    }

    public static SelfCreditOrderDetailsService defaultService() {
        return INSTANCE;
    }

    public SelfCreditOrderDetailsData collectOrderDetails(FDUserI user, String orderId) throws FDResourceException, JsonParseException, JsonMappingException, IOException, FDSkuNotFoundException, HttpErrorResponse {

        List<SelfCreditOrderItemData> orderLines = new ArrayList<SelfCreditOrderItemData>();
        
        FDOrderAdapter orderDetailsToDisplay = (FDOrderAdapter) FDCustomerManager.getOrder(user.getIdentity(), orderId);
        
        Map<String, List<ComplaintReason>> complaintReasonMap = collectAllComplaintReasons();

        List<FDCartLineI> orderLinesToDisplay = orderDetailsToDisplay.getOrderLines();
        for (FDCartLineI fdCartLine : orderLinesToDisplay) {
        	final boolean isDeliverPass = fdCartLine.lookupFDProduct().isDeliveryPass();
        	
        	FDProductInfo productInfo = FDCachedFactory.getProductInfo(fdCartLine.getSku().getSkuCode());
        	
        	if (!isDeliverPass) {
            	SelfCreditOrderItemData item = new SelfCreditOrderItemData();
                item.setOrderLineId(fdCartLine.getOrderLineId());
                item.setBrand((null == fdCartLine.lookupProduct()) ?"" : fdCartLine.lookupProduct().getPrimaryBrandName());
                item.setProductName(collectProductName(item.getBrand(), fdCartLine.getDescription()));
                item.setQuantity(collectQuantity(fdCartLine));
                item.setProductImage((null == fdCartLine.lookupProduct()) ? "" : fdCartLine.lookupProduct().getProdImage().getPathWithPublishId());
                item.setComplaintReasons(collectComplaintReasons(complaintReasonMap, fdCartLine.getDepartmentDesc()));
                item.setBasePrice(fdCartLine.getBasePrice());
                item.setBasePriceUnit(productInfo.getDefaultPriceUnit());
                item.setConfigurationDescription(fdCartLine.getConfigurationDesc());
                item.setSample(fdCartLine.isSample());
                item.setFree((null == fdCartLine.getDiscount()) ? false : EnumDiscountType.FREE.equals(fdCartLine.getDiscount().getDiscountType()));
                item.setMealBundle(isItemMealBundle(fdCartLine));
                item.setCartonNumbers(collectCartonNumbers(orderDetailsToDisplay.getCartonContents(fdCartLine.getOrderLineNumber())));
                orderLines.add(item);
			}
        }

        SelfCreditOrderDetailsData selfCreditOrderDetailsData = new SelfCreditOrderDetailsData();
        selfCreditOrderDetailsData.setOrderLines(orderLines);
        return selfCreditOrderDetailsData;
    }

    private double collectQuantity(FDCartLineI fdCartLine) {
    	String quantity = "".equals(fdCartLine.getDeliveredQuantity()) ? fdCartLine.getOrderedQuantity() : fdCartLine.getDeliveredQuantity();
    	return  Double.parseDouble(quantity);
	}

	private List<String> collectCartonNumbers(List<FDCartonInfo> cartonContents) {
    	List<String> cartonNumbers = new ArrayList<String>();
    	for (FDCartonInfo cartonContent : cartonContents) {
			cartonNumbers.add(cartonContent.getCartonInfo().getCartonNumber());
		}
		return cartonNumbers;
	}

	private String collectProductName(String brandName, String description) {
    	String productNameNoBrand = description;
    	if (brandName != null && brandName.length() > 0 && description.length() >= brandName.length() && description.substring(0, brandName.length()).equalsIgnoreCase(brandName)) {
            productNameNoBrand = description.substring(brandName.length()).trim();
        }
    	return productNameNoBrand;
	}

	private List<SelfCreditComplaintReason> collectComplaintReasons(
			Map<String, List<ComplaintReason>> complaintReasonMap, String department) {
		List<ComplaintReason> complaintReasons = complaintReasonMap.get(ComplaintUtil.standardizeDepartment(department));
		List<SelfCreditComplaintReason> selfCreditComplaintReasons = new ArrayList<SelfCreditComplaintReason>();
		
		for (ComplaintReason complaintReason : complaintReasons) {
			if (complaintReason.isShowCustomer()) {
				SelfCreditComplaintReason selfCreditComplaintReason = new SelfCreditComplaintReason();
				selfCreditComplaintReason.setId(complaintReason.getId());
                selfCreditComplaintReason.setReason(collectComplaintReasonName(complaintReason));
				selfCreditComplaintReasons.add(selfCreditComplaintReason);
			}
		}
		
		return selfCreditComplaintReasons;
	}

    private String collectComplaintReasonName(ComplaintReason complaintReason) {
        final boolean hasCustomerDisplayName = !StringUtils.isEmpty(complaintReason.getCustomerDisplayName());
        return hasCustomerDisplayName ? complaintReason.getCustomerDisplayName() : complaintReason.getReason();
    }

    private Map<String, List<ComplaintReason>> collectAllComplaintReasons() throws JsonParseException, JsonMappingException, IOException {
        Map<String, List<ComplaintReason>> complaintReasonMap = new HashMap<String, List<ComplaintReason>>();

        StringBuilder content = new StringBuilder();
        BufferedReader input = null;
        String backofficeUrl = new StringBuilder().append(FDStoreProperties.getBackOfficeApiUrl()).append(FDStoreProperties.getBkofficeComplaintReasonsUrl()).toString();
        URL url = new URL(backofficeUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");

        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine = null;

        while ((inputLine = input.readLine()) != null) {
            content.append(inputLine);
        }
        if (input != null) {
            input.close();
        }

        String creditReasons = content.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        complaintReasonMap = objectMapper.readValue(creditReasons, new TypeReference<Map<String, List<ComplaintReason>>>() {
        });

        return complaintReasonMap;
    }

    private boolean isItemMealBundle(FDCartLineI fdCartLine) {
        return CartDataService.defaultService().isMealBundle(fdCartLine.lookupProduct());
    }

}

