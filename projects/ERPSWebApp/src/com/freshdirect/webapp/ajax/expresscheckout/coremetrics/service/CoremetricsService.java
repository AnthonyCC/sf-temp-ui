package com.freshdirect.webapp.ajax.expresscheckout.coremetrics.service;

import java.util.List;

import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder.CustomCategory;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;



public class CoremetricsService {

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

}
