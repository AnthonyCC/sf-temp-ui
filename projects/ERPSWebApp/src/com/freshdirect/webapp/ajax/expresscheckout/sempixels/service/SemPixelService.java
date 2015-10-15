package com.freshdirect.webapp.ajax.expresscheckout.sempixels.service;

import java.text.DecimalFormat;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.expresscheckout.sempixels.data.SemPixelData;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SemPixelService {

    private static final SemPixelService INSTANCE = new SemPixelService();

    private SemPixelService() {
    }

    public static SemPixelService defaultService() {
        return INSTANCE;
    }

    public SemPixelData populateSemPixelMediaInfo(FDUserI user, HttpSession session, FDOrderI order) throws FDResourceException {
        SemPixelData result = new SemPixelData();
        DecimalFormat sem_df = new DecimalFormat("0.00");
        DecimalFormat ndFormatter = new DecimalFormat("0");
        result.setDiscountAmount(sem_df.format(order.getTotalDiscountValue()));
        result.setDiscountAmountND(ndFormatter.format(100 * order.getTotalDiscountValue()));
        result.setModifyOrder(order.isModifiedOrder());
        Boolean orderSubmittedFlagForSemPixel = (Boolean) session.getAttribute(SessionName.ORDER_SUBMITTED_FLAG_FOR_SEM_PIXEL);
        if (orderSubmittedFlagForSemPixel != null && orderSubmittedFlagForSemPixel) {
            result.setNewOrder(true);
            session.removeAttribute(SessionName.ORDER_SUBMITTED_FLAG_FOR_SEM_PIXEL);
        }
        result.setOrderId(order.getErpSalesId());
        result.setSubtotal(sem_df.format(order.getSubTotal()));
        result.setSubtotalND(ndFormatter.format(100 * order.getSubTotal()));
        result.setTotalCartItems(order.getLineCnt());
        result.setUserCounty(user.getDefaultCounty());
        result.setValidOrders(user.getAdjustedValidOrderCount());
        return result;
    }
}
