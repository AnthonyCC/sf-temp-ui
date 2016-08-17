package com.freshdirect.mobileapi.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.api.data.request.SearchMessageRequest;
import com.freshdirect.mobileapi.api.service.CartService;
import com.freshdirect.mobileapi.api.service.ConfigurationService;
import com.freshdirect.mobileapi.api.service.AccountService;
import com.freshdirect.mobileapi.api.service.SearchService;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.SearchMessageResponse;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.SortType;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public SearchMessageResponse search(HttpServletRequest request, HttpServletResponse response, SearchMessageRequest searchRequest) throws FDException, ServiceException {
        SearchMessageResponse searchResponse = new SearchMessageResponse();
        SessionUser user = accountService.getSessionUser(request, response, searchRequest.getSource());
        searchResponse.setLogin(accountService.createLoginResponseMessage(user));
        searchResponse.setCartDetail(cartService.getCartDetail(user));
        searchResponse.setConfiguration(configurationService.getConfiguration(user.getFDSessionUser()));
        searchResponse.setStatus(Message.STATUS_SUCCESS);
        searchResponse.setSearch(
                searchService.search(((searchRequest.getQuery() != null) ? searchRequest.getQuery() : ""), searchRequest.getUpc(), searchRequest.getPage(), searchRequest.getMax(),
                        SortType.valueFromString(searchRequest.getSortBy()), searchRequest.getBrand(), searchRequest.getCategory(), searchRequest.getDepartment(), user));
        return searchResponse;
    }
}
