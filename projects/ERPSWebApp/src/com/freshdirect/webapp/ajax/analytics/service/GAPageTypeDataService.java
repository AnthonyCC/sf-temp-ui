package com.freshdirect.webapp.ajax.analytics.service;

import java.util.List;

import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.webapp.ajax.analytics.data.GAPageTypeData;
import com.freshdirect.webapp.ajax.analytics.domain.GAPageTypeDistinguisher;
import com.freshdirect.webapp.ajax.analytics.domain.PageType;

public class GAPageTypeDataService {

    private static final GAPageTypeDataService INSTANCE = new GAPageTypeDataService();

    private GAPageTypeDataService() {

    }

    public static GAPageTypeDataService defaultService() {
        return INSTANCE;
    }

    public GAPageTypeData populatePageTypeData(GAPageTypeDistinguisher distinguisher) {

        GAPageTypeData pageTypeData = new GAPageTypeData();

        pageTypeData.setPageType(PageType.namePageType(getParameters(distinguisher)));
        pageTypeData.setPageLanguage("english");

        return pageTypeData;
    }

    public String getParameters(GAPageTypeDistinguisher distinguisher) {

        String pageType = distinguisher.getPageType();
        String id = distinguisher.getId();
        String requestURI = distinguisher.getRequestURI();
        String url = requestURI;

        if (pageType != null && id != null) {
            if (requestURI.equals("/browse.jsp")) {
                ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNode(id);
                ContentType contentType = contentNodeModel.getContentKey().getType();

                if (contentType == FDContentTypes.SUPER_DEPARTMENT || contentType == FDContentTypes.DEPARTMENT) {
                    url += "?pageType=category_list";
                } else if (contentType == FDContentTypes.CATEGORY) {
                    CategoryModel cat = (CategoryModel) contentNodeModel;
                    List<CategoryModel> subCats = cat.getSubcategories();
                    if (distinguisher.isAll() || subCats.isEmpty()) {
                        url += "?pageType=product_list";
                    } else if (EnumLayoutType.RECIPE_MEALKIT_CATEGORY.equals(cat.getSpecialLayout())) {
                        url += "?pageType=meal_kits";
                    } else {
                        url += "?pageType=category_list";
                    }
                }
            } else if ("/srch.jsp".equals(requestURI)) {
                url += "?pageType=" + pageType;

            } else if ("about_overview".equals(id)) {
                url += "?pageType=about_us";
            }
        }
        return url;
    }

}
