package com.freshdirect.backoffice.selfcredit.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditComplaintReason;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderDetailsData;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderItemData;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
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
import com.freshdirect.storeapi.content.EnumProductLayout;
import com.freshdirect.storeapi.content.ProductModel;
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
        
        Map<String, List<SelfCreditComplaintReason>> complaintReasonMap = collectAllComplaintReasons();
        Map<String, Double> creditedQuantities = collectApprovedComplaintQuantity(orderDetailsToDisplay.getComplaints());

        List<FDCartLineI> orderLinesToDisplay = orderDetailsToDisplay.getOrderLines();
        for (FDCartLineI fdCartLine : orderLinesToDisplay) {
            final boolean isDeliveryPass = fdCartLine.lookupFDProduct().isDeliveryPass();
            final boolean hasSubstitute = null != fdCartLine.getInvoiceLine() && null != fdCartLine.getInvoiceLine().getSubstituteProductName();
        	
        	FDProductInfo productInfo = FDCachedFactory.getProductInfo(fdCartLine.getSku().getSkuCode());
        	ProductModel product = fdCartLine.lookupProduct();
        	
    		double deliveredQuantity = collectQuantity(fdCartLine, creditedQuantities);

            if (!isDeliveryPass && Double.compare(deliveredQuantity, 0d) > 0) {
            	SelfCreditOrderItemData item = new SelfCreditOrderItemData();
                item.setOrderLineId(fdCartLine.getOrderLineId());
                item.setBrand((null == product) ?"" : product.getPrimaryBrandName());
                item.setProductName(collectProductName(item.getBrand(), fdCartLine.getDescription()));
                item.setQuantity(deliveredQuantity);
                item.setProductImage((null == product) ? "" : product.getProdImage().getPathWithPublishId());
                item.setComplaintReasons(collectComplaintReasons(complaintReasonMap, fdCartLine.getDepartmentDesc()));
                item.setBasePrice(fdCartLine.getBasePrice());
                item.setBasePriceUnit(productInfo.getDefaultPriceUnit());
                item.setConfigurationDescription(fdCartLine.getConfigurationDesc());
                item.setSample(fdCartLine.isSample());
                item.setFree((null == fdCartLine.getDiscount()) ? false : EnumDiscountType.FREE.equals(fdCartLine.getDiscount().getDiscountType()));
                item.setMealBundle(null == product ? false : isItemMealBundle(product));
                item.setCartonNumbers(collectCartonNumbers(orderDetailsToDisplay.getCartonContents(fdCartLine.getOrderLineNumber())));
                item.setFinalPrice(collectFinalPrice(deliveredQuantity, fdCartLine));
                item.setSubstituted(hasSubstitute);
                orderLines.add(item);
			}
        }

        SelfCreditOrderDetailsData selfCreditOrderDetailsData = new SelfCreditOrderDetailsData();
        selfCreditOrderDetailsData.setCustomerServiceContact(user.getCustomerServiceContact());
        selfCreditOrderDetailsData.setOrderLines(orderLines);
        return selfCreditOrderDetailsData;
    }

	private double collectQuantity(FDCartLineI fdCartLine, Map<String, Double> creditedQuantities) {
    	String quantity = "".equals(fdCartLine.getDeliveredQuantity()) ? fdCartLine.getOrderedQuantity() : fdCartLine.getDeliveredQuantity();
    	double deliveredQuantity = Double.parseDouble(quantity);
    	
    	final String orderLineId = fdCartLine.getOrderLineId();
    	double creditedQuantity = creditedQuantities.containsKey(orderLineId) ? creditedQuantities.get(orderLineId) : 0d;
    	
    	double displayQuantity = deliveredQuantity - creditedQuantity;
    	return  Double.compare(displayQuantity, 0d) == 0 ? 0d : (Double.compare(Math.floor(displayQuantity), 0d) == 0 ? 1d : Math.floor(displayQuantity));
	}

	private Map<String, Double> collectApprovedComplaintQuantity(Collection<ErpComplaintModel> complaints) {
		Map<String, Double> creditedQuantitiesByOrderLineId = new HashMap<String, Double>();

		for (ErpComplaintModel complaint : complaints) {
			if (EnumComplaintStatus.APPROVED.equals(complaint.getStatus())) {
				List<ErpComplaintLineModel> complaintLines = null != complaint.getComplaintLines() 
						? complaint.getComplaintLines()
								: new ArrayList<ErpComplaintLineModel>();
				for (ErpComplaintLineModel complaintLine : complaintLines) {
					String orderLineId = complaintLine.getOrderLineId();
					Double creditedQuantity = complaintLine.getQuantity();
					if (creditedQuantitiesByOrderLineId.containsKey(orderLineId)) {
						creditedQuantity += creditedQuantitiesByOrderLineId.get(orderLineId);
					}
					creditedQuantitiesByOrderLineId.put(orderLineId, creditedQuantity);
				}
			}
		}
		return creditedQuantitiesByOrderLineId;
	}

	private List<String> collectCartonNumbers(List<FDCartonInfo> cartonContents) {
    	List<String> cartonNumbers = new ArrayList<String>();
    	for (FDCartonInfo cartonContent : cartonContents) {
			cartonNumbers.add(cartonContent.getCartonInfo().getCartonNumber());
		}
		return cartonNumbers;
	}
	
	private double collectFinalPrice(double deliveredQuantity, FDCartLineI fdCartLine) {
		double finalPrice = fdCartLine.getInvoiceLine().getPrice();
		return Double.compare(deliveredQuantity, 0d) == 0 ? 0d : finalPrice/deliveredQuantity;
	}

	private String collectProductName(String brandName, String description) {
    	String productNameNoBrand = description;
    	if (brandName != null && brandName.length() > 0 && description.length() >= brandName.length() && description.substring(0, brandName.length()).equalsIgnoreCase(brandName)) {
            productNameNoBrand = description.substring(brandName.length()).trim();
        }
    	return productNameNoBrand;
	}

	private List<SelfCreditComplaintReason> collectComplaintReasons(
			Map<String, List<SelfCreditComplaintReason>> complaintReasonMap, String department) {
		List<SelfCreditComplaintReason> complaintReasons = complaintReasonMap.get(ComplaintUtil.standardizeDepartment(department));
		return complaintReasons;
	}

    private Map<String, List<SelfCreditComplaintReason>> collectAllComplaintReasons() throws JsonParseException, JsonMappingException, IOException {
        Map<String, List<SelfCreditComplaintReason>> complaintReasonMap = new HashMap<String, List<SelfCreditComplaintReason>>();

        StringBuilder content = new StringBuilder();
        BufferedReader input = null;
        String backofficeUrl = new StringBuilder().append(FDStoreProperties.getBackOfficeApiUrl()).append(FDStoreProperties.getBkofficeComplaintReasonsUrl()).toString();
        URL url = new URL(backofficeUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
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
        complaintReasonMap = objectMapper.readValue(creditReasons, new TypeReference<Map<String, List<SelfCreditComplaintReason>>>() {
        });

        return complaintReasonMap;
    }

    private boolean isItemMealBundle(ProductModel product) {
    	final boolean mealBundle = CartDataService.defaultService().isMealBundle(product);
    	final boolean componentGroupMeal = null == product.getSpecialLayout() ? false : EnumProductLayout.COMPONENTGROUP_MEAL.equals(product.getSpecialLayout());
        return  mealBundle || componentGroupMeal;
    }

}

