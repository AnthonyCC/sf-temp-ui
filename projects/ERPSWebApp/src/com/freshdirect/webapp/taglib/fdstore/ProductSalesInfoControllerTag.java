package com.freshdirect.webapp.taglib.fdstore;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.UnsettledOrdersInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;

public class ProductSalesInfoControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( PaymentMethodControllerTag.class );
    private FDUserI user = null;

    private String actionName 		= "addPaymentMethod";
    private String successPage;
    private String result;

    public void setSuccessPage(String sp) {
        if (sp.length()>0 ){
            this.successPage = sp;
        }else {
            this.successPage = null;
        }
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }


    public void setResult(String resultName) {
        this.result = resultName;
    }

	 public int doStartTag() throws JspException {

	        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	        HttpSession session = pageContext.getSession();
	        ActionResult actionResult = new ActionResult();
	        List<UnsettledOrdersInfo> orders = null;

	            user = (FDUserI) session.getAttribute(SessionName.USER);	            

				if(actionName.equalsIgnoreCase("getUnsettledOrders")) {
					FDCustomerManager customerManager = new FDCustomerManager();
					try {
						orders =customerManager.getUnsettledOrders(null);
					} catch (FDResourceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        request.setAttribute("unsettledOrders", orders);
			return EVAL_BODY_BUFFERED;
	 }
}
