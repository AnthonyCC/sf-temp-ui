package com.freshdirect.webapp.helppage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.webapp.helppage.data.HelpPageCategoryData;
import com.freshdirect.webapp.helppage.data.HelpPageContentNodeData;

public class HelpService {

    private static final HelpService INSTANCE = new HelpService();
    private static final Logger LOGGER = LoggerFactory.getInstance(HelpService.class);
    private static final String FOOD_SAFETY_CATEGORY_ID = "food_safety_freshdirect";

    private HelpService() {
    }

    public static HelpService defaultService() {
        return INSTANCE;
    }

    public Map<String, Object> collectHelpPageData(FDUserI user) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        collectFaqSections(dataMap);
        collectFoodSafetySubCategories(dataMap);
        collectCustomerCreditRestrictionInfo(dataMap, user);
        dataMap.put("customerServiceEmail", FDStoreProperties.getCustomerServiceEmail());
        return dataMap;
    }

    private void collectFaqSections(Map<String, Object> dataMap) {
        String faqSections = FDStoreProperties.getFaqSections();
        List<HelpPageContentNodeData> faqContentNodes = new ArrayList<HelpPageContentNodeData>();
        if (null != faqSections) {
            StringTokenizer st = new StringTokenizer(faqSections, ",");
            while (st.hasMoreTokens()) {
                ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(st.nextToken().trim());
                if (null != contentNode) {
                    faqContentNodes.add(new HelpPageContentNodeData(contentNode.getContentKey().getId(), contentNode.getCmsAttributeValue("name").toString()));
                }
            }
        }
        dataMap.put("faqSections", faqContentNodes);
    }

    private void collectFoodSafetySubCategories(Map<String, Object> dataMap) {
        CategoryModel foodSafetyCategory = (CategoryModel) ContentFactory.getInstance().getContentNode(FOOD_SAFETY_CATEGORY_ID);
        List<HelpPageCategoryData> foodSafetySubCategories = new ArrayList<HelpPageCategoryData>();
        if (null != foodSafetyCategory && !foodSafetyCategory.getSubcategories().isEmpty()) {
            for (CategoryModel foodSafetySubCategory : foodSafetyCategory.getSubcategories()) {
                foodSafetySubCategories.add(new HelpPageCategoryData(foodSafetySubCategory.getContentName(), foodSafetySubCategory.getFullName()));
            }
        }
        dataMap.put("foodSafetySubCategories", foodSafetySubCategories);
    }

    private void collectCustomerCreditRestrictionInfo(Map<String, Object> dataMap, FDUserI user) {
        try {
            dataMap.put("customerCreditRestricted", user.isCreditRestricted());
        } catch (FDResourceException e) {
            LOGGER.error("Error when collecting credit restriction status of customer with id: " + user.getUserId(), e);
        }
    }
}
