package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class CheckOrderStatusTag extends BodyTagSupportEx {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5324357781039723506L;

    @Override
    public int doStartTag() throws JspException {

        // HttpSession session = pageContext.getSession();
        // FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
        String orderId = request.getParameter("orderId");

        try {
            FDOrderI order = FDCustomerManager.getOrder(orderId);
            // FDOrderHistory history=FDCustomerManager.getOrderHistoryInfo(user.getIdentity());
            // history.getFDOrderInfo(orderId);
            if (isOrderModifiable(order)) {

                JspWriter out = pageContext.getOut();
                try {
                    out.append("Modifiable");
                } catch (IOException e) {
                    throw new JspException();
                }

            }
        } catch (FDResourceException e1) {
            e1.printStackTrace();
        }

        return SKIP_BODY;
    }

    private static boolean isModifiable(FDOrderI order) {
        final EnumSaleStatus status = order.getOrderStatus();
        return (EnumSaleStatus.SUBMITTED.equals(status) || EnumSaleStatus.AUTHORIZED.equals(status) || EnumSaleStatus.AVS_EXCEPTION.equals(status))
                && !order.getOrderType().equals(EnumSaleType.DONATION);
    }

    public static boolean isOrderModifiable(FDOrderI order) {
        return isModifiable(order) && new Date().before(order.getDeliveryReservation().getCutoffTime());
    }

}
