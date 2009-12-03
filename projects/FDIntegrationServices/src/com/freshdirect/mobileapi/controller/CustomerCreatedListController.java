package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.response.QuickShop;
import com.freshdirect.mobileapi.controller.data.response.QuickShopLists;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.CustomerCreatedList;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;

public class CustomerCreatedListController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(CustomerCreatedListController.class);

    private static String ACTION_GET_SHOPPING_LISTS = "getshoppinglists";

    private static String ACTION_QUICK_SHOP = "quickshop";

    /* (non-Javadoc)
     * @see com.freshdirect.mobileapi.controller.BaseController#processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.String, com.freshdirect.mobileapi.model.SessionUser)
     */
    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

        if (ACTION_GET_SHOPPING_LISTS.equals(action)) {
            model = getCustomerCreatedLists(model, user);
        } else if (ACTION_QUICK_SHOP.equals(action)) {
            String cclId = request.getParameter("cclId");
            model = getProductsFromList(model, user, cclId);
        }
        return model;
    }

    /**
     * @param model
     * @param user
     * @return
     * @throws FDException
     * @throws JsonException 
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private ModelAndView getCustomerCreatedLists(ModelAndView model, SessionUser user) throws FDException, JsonException {
        List<CustomerCreatedList> lists = user.getCustomerCreatedList();
        Message responseMessage = QuickShopLists.initWithList(lists);
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param model
     * @param user
     * @param listId
     * @return
     * @throws FDException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     * @throws JsonException 
     */
    private ModelAndView getProductsFromList(ModelAndView model, SessionUser user, String listId) throws FDException, JsonException {
        List<ProductConfiguration> products;
        try {
            products = user.getCustomerCreatedListProducts(listId);
        } catch (ModelException e) {
            throw new FDException(e);
        }
        QuickShop quickShop = new QuickShop();
        quickShop.setProducts(products);
        setResponseMessage(model, quickShop, user);
        return model;
    }

}
