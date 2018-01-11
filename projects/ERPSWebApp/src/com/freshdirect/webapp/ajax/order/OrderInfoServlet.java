package com.freshdirect.webapp.ajax.order;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.soy.SoyTemplateEngine;

public class OrderInfoServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 911555192346360828L;

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.SIGNED_IN;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        String orderId = request.getParameter("orderId");
        if (orderId == null) {
            BaseJsonServlet.returnHttpError(400, "orderId is not specified");
        }

        String productData = request.getParameter("productData");

        try {
            Map<String, Object> result = new HashMap<String, Object>();
            if (OrderInfoService.defaultService().isOrderValid(orderId)) {
                FDOrderI order = FDCustomerManager.getOrder(user.getIdentity(), orderId);
                OrderInfoData orderData = OrderInfoService.defaultService().populateOrderData(request.getSession(), user, order);
                if (productData != null) {
                    orderData = OrderInfoService.defaultService().populateProductPotato(user, orderData);
                }
                result.put("order", SoyTemplateEngine.convertToMap(orderData));
            } else {
                result.put("error", "Invalid order id: " + orderId);
            }

            writeResponseData(response, result);
        } catch (FDResourceException e) {
            BaseJsonServlet.returnHttpError(500, "Failed to load order by order id: " + orderId);
        }
    }

}
