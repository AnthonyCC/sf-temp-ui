package com.freshdirect.mobileapi.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.CMSWebPageModel;
import com.freshdirect.mobileapi.api.DateConverter;
import com.freshdirect.mobileapi.api.data.request.PageMessageRequest;
import com.freshdirect.mobileapi.api.data.response.PageMessageResponse;
import com.freshdirect.mobileapi.api.service.CartService;
import com.freshdirect.mobileapi.api.service.ConfigurationService;
import com.freshdirect.mobileapi.api.service.AccountService;
import com.freshdirect.mobileapi.api.service.PageService;
import com.freshdirect.mobileapi.api.service.ProductCatalogService;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.model.SessionUser;

@RestController
public class PageController {

    public static final String FEED_PAGE_TYPE = "Feed";
    public static final String TODAYS_PICK_PAGE_TYPE = "TodaysPick";
    public static final String HOME_PAGE_PATH = "/page/home";
    public static final String REQUESTED_DATE = "requestedDate";
    private static final String PREVIEW = "preview";
    private static final String IGNORE_SCHEDULE = "ignoreSchedule";
    private static final String PLANTID = "plantid";
    private static final String PAGE_TYPE = "pageType";
    private static final String SOURCE = "source";
    private static final String FEED_ID = "feedId";

    @Autowired
    private AccountService loginService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private PageService pageService;

    @Autowired
    private DateConverter dateConverter;

    @Autowired
    private ProductCatalogService productCatalogService;

    @RequestMapping(value = HOME_PAGE_PATH, method = RequestMethod.GET)
    public PageMessageResponse getHomePage(HttpServletRequest request, HttpServletResponse response) throws FDException {
        PageMessageResponse pageResponse = new PageMessageResponse();
        PageMessageRequest pageRequest = parsePageRequest(request);
        SessionUser user = loginService.checkLogin(request, response, pageRequest.getSource());
        pageRequest.setPlantId(productCatalogService.getPlantId(request, user));
        LoggedIn loginMessage = loginService.createLoginResponseMessage(user);
        pageResponse.setStatus(loginMessage.getStatus());
        pageResponse.setLogin(loginMessage);
        pageResponse.setCartDetail(cartService.getCartDetail(user));
        pageResponse.setConfiguration(configurationService.getConfiguration(user.getFDSessionUser()));
        List<String> errorProductKeys = new ArrayList<String>();
        List<CMSWebPageModel> pages = pageService.getPages(user, pageRequest, errorProductKeys);
        for (CMSWebPageModel page : pages) {
            if (TODAYS_PICK_PAGE_TYPE.equalsIgnoreCase(page.getType())) {
                pageResponse.setPick(page);
            } else if (FEED_PAGE_TYPE.equalsIgnoreCase(page.getType())) {
                pageResponse.setPage(page);
            }
        }
        for (String errorProductKey : errorProductKeys) {
            pageResponse.addErrorMessage(errorProductKey);
        }
        return pageResponse;
    }

    private PageMessageRequest parsePageRequest(HttpServletRequest request) {
        PageMessageRequest pageRequest = new PageMessageRequest();
        pageRequest.setFeedId(request.getParameter(FEED_ID));
        pageRequest.setSource(request.getParameter(SOURCE));
        pageRequest.setPageType(request.getParameter(PAGE_TYPE));
        pageRequest.setPlantId(request.getParameter(PLANTID));
        pageRequest.setIgnoreSchedule("true".equalsIgnoreCase(request.getParameter(IGNORE_SCHEDULE)));
        pageRequest.setPreview("true".equalsIgnoreCase(request.getParameter(PREVIEW)));
        pageRequest.setRequestedDate(dateConverter.convert(request.getParameter(REQUESTED_DATE)));
        return pageRequest;
    }

}
