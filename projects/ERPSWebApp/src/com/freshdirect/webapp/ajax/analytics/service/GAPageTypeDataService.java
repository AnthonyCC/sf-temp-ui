package com.freshdirect.webapp.ajax.analytics.service;

import java.util.Arrays;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.webapp.ajax.analytics.data.GAPageTypeData;
import com.freshdirect.webapp.ajax.analytics.domain.GAPageTypeDistinguisher;
import com.freshdirect.webapp.ajax.analytics.domain.PageType;

public class GAPageTypeDataService {

    private static final List<String> SPECIAL_BROWSE_IDS = Arrays.asList("wgd_deals", "top_rated", "wgd_summer_central", "about_overview", "local", "meals_kits_meals");

    private static final GAPageTypeDataService INSTANCE = new GAPageTypeDataService();

    private GAPageTypeDataService() {

    }

    public static GAPageTypeDataService defaultService() {
        return INSTANCE;
    }

    public GAPageTypeData populatePageTypeData(GAPageTypeDistinguisher distinguisher) {

        GAPageTypeData pageTypeData = new GAPageTypeData();

        pageTypeData.setPageType(getGAUrl(distinguisher));
        pageTypeData.setPageLanguage("english");

        return pageTypeData;
    }

    public String getGAUrl(GAPageTypeDistinguisher distinguisher) {


        String id = distinguisher.getId();
        String pageType = distinguisher.getPageType();
        String requestURI = distinguisher.getRequestURI();
        StringBuilder url = new StringBuilder(requestURI);

        if (SPECIAL_BROWSE_IDS.contains(id)) {
            url.append(getSpecialBrowseUrl(id));
        } else if ("/browse.jsp".equals(requestURI)) {
            url.append(getBrowseUrl(distinguisher));
        } else if ("/srch.jsp".equals(requestURI)) {
            url.append(getSearchUrl(pageType));
        }
        
        return PageType.namePageType(url.toString());
    }

    private String getSpecialBrowseUrl(String id) {
        String gaParameter = "";
        if (id != null) {
            gaParameter = "?id=" + id;
        }
        return gaParameter;
    }

    private String getBrowseUrl(GAPageTypeDistinguisher distinguisher) {
        String id = distinguisher.getId();
        String gaParameter = "";
        if (id != null) {

            ContentNodeModel contentNodeModel = ContentFactory.getInstance().getContentNode(id);
            ContentType contentType = contentNodeModel.getContentKey().getType();

            if (contentType == FDContentTypes.SUPER_DEPARTMENT || contentType == FDContentTypes.DEPARTMENT) {
                gaParameter = "?pageType=category_list";
            } else if (contentType == FDContentTypes.CATEGORY) {
                CategoryModel cat = (CategoryModel) contentNodeModel;
                List<CategoryModel> subCats = cat.getSubcategories();
                if (distinguisher.isAll() || subCats.isEmpty()) {
                    gaParameter = "?pageType=product_list";
                } else {
                    gaParameter = "?pageType=category_list";
                }
            }
        }
        return gaParameter;
    }

    private String getSearchUrl(String pageType) {
        String gaParameter = "";
        if (pageType != null) {
            gaParameter = "?pageType=" + pageType;
        }
        return gaParameter;
    }
}
