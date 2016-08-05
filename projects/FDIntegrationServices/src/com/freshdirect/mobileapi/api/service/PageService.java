package com.freshdirect.mobileapi.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freshdirect.fdstore.content.CMSSectionModel;
import com.freshdirect.fdstore.content.CMSWebPageModel;
import com.freshdirect.mobileapi.api.data.request.PageMessageRequest;
import com.freshdirect.mobileapi.controller.data.CMSSectionProductModel;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.wcms.CMSContentFactory;

@Component
public class PageService {

    @Autowired
    private ProductService productService;

    public List<CMSWebPageModel> getPages(SessionUser user, PageMessageRequest pageRequest, List<String> errors) {
        List<CMSWebPageModel> pages = new ArrayList<CMSWebPageModel>();
        if (pageRequest.isPreview()) {
            pages.addAll(getPreviewPages(user, pageRequest, errors));
        } else if (pageRequest.getRequestedDate() == null) {
            CMSWebPageModel page = getCachedPage(user, pageRequest, errors);
            if (page == null) {
                errors.add("Can not find page(s) in cache.");
            } else {
                pages.add(page);
            }
        } else {
            pages.addAll(getPagesByParameters(user, pageRequest, errors));
        }
        return pages;
    }

    // Refresh the feed if it is for preview
    public List<CMSWebPageModel> getPreviewPages(SessionUser user, PageMessageRequest pageRequest, List<String> errors) {
        CMSContentFactory.getInstance().cacheAllPages();
        return getPagesByParameters(user, pageRequest, errors);
    }

    // Get the feed from cache if it doesn't have request date / if it is not for preview
    public List<CMSWebPageModel> getPagesByParameters(SessionUser user, PageMessageRequest pageRequest, List<String> errors) {
        List<CMSWebPageModel> pages = CMSContentFactory.getInstance().getCMSPageByParameters(pageRequest);
        for (CMSWebPageModel page : pages) {
            addProductsToSection(user, page, errors);
        }
        return pages;
    }

    // Refresh the feed if it has the date in the request
    public CMSWebPageModel getCachedPage(SessionUser user, PageMessageRequest pageRequest, List<String> errors) {
        CMSWebPageModel page = CMSContentFactory.getInstance().getCMSPageByName(pageRequest.getPageType());
        addProductsToSection(user, page, errors);
        return page;
    }

    private void addProductsToSection(SessionUser user, CMSWebPageModel page, List<String> errors) {
        if (page != null) {
            List<CMSSectionModel> sectionWithProducts = new ArrayList<CMSSectionModel>();
            for (CMSSectionModel section : page.getSections()) {
                CMSSectionProductModel sectionWithProduct = new CMSSectionProductModel(section);
                sectionWithProduct.setProducts(productService.getProducts(user, section.getProductList(), errors));
                sectionWithProducts.add(sectionWithProduct);
            }
            page.setSections(sectionWithProducts);
        }
    }

}
