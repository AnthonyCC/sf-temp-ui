package com.freshdirect.webapp.ajax.expresscheckout.coremetrics.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder.CustomCategory;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;



public class CoremetricsService {

	private static final Logger LOGGER = LoggerFactory.getInstance(CoremetricsService.class);
    private static final CoremetricsService INSTANCE = new CoremetricsService();

    private CoremetricsService() {
    }

    public static CoremetricsService defaultService() {
        return INSTANCE;
    }

    public List<String> getCoremetricsData(String pageId) {
        PageViewTagModel pvTagModel = new PageViewTagModel();
        pvTagModel.setCategoryId(CmContext.getContext().prefixedCategoryId(CustomCategory.CHECKOUT.toString()));
        pvTagModel.setPageId(pageId);
        PageViewTagModelBuilder.decoratePageIdWithCatId(pvTagModel);
        return pvTagModel.toStringList();
    }
    
    public String getCustomerTypeByOrderCount(FDUserI user) {
    	String result = "newCustomerPage";
    	try {
			if (user.getAdjustedValidOrderCount() > 0) {
				result = "existingCustomerPage";
			}
		} catch (FDResourceException e) {
			LOGGER.error("User[" + user.getUserId() + "] order count evaluation failed", e);
		}
    	return result;
    }

}
