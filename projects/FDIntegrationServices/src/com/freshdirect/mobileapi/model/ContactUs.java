package com.freshdirect.mobileapi.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.request.ContactUsData;
import com.freshdirect.mobileapi.model.tagwrapper.ContactFdControllerWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.OrderHistoryInfoTagWrapper;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection;

public class ContactUs {

    private SessionUser sessionUser;

    private static Category LOGGER = LoggerFactory.getInstance(Checkout.class);

    public ContactUs(SessionUser sessionUser) {
        this.sessionUser = sessionUser;
    }

    /**
     * @return
     */
    public Map<String, String> getContactUsSubjects() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        if(this.sessionUser!=null){
	        ContactFdControllerWrapper tagWrapper = new ContactFdControllerWrapper(this.sessionUser);
	        Selection[] selections;
	        String storeKey = ContentFactory.getInstance().getCurrentUserContext() != null 
					&& ContentFactory.getInstance().getCurrentUserContext().getStoreContext() != null
						&& ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId() != null
							&& !EnumEStoreId.FD.equals(ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId()) 
									? ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId().getContentId().toLowerCase() : null;
	        
	        if(storeKey!=null){
	        	if(this.sessionUser.isLoggedIn()){
	        		selections = tagWrapper.getSubjectsFdx();
	        	}else{
	        		selections = tagWrapper.getSubjectsFdxAnonymous();
	        	}
	        }else{
	        	selections = tagWrapper.getSubjects();
	        }
	        int index = 0;
	        for (Selection selection : selections) {
	            values.put(Integer.toString(index), selection.getDescription());
	            index++;
	        }
        }
        return values;
    }

    /**
     * @return
     * @throws FDException
     */
    public List<Map<String, String>> getPreviousOrders() throws FDException {
        DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
        OrderHistoryInfoTagWrapper orderHistoryInfoTagWrapper = new OrderHistoryInfoTagWrapper(this.sessionUser);
        List<FDOrderInfoI> orderInfos = orderHistoryInfoTagWrapper.getOrderHistoryInfo();

        /*
         * DUP: FDWebSite/docroot/help/contact_fd.jsp
         * LAST UPDATED ON: 11/04/2009
         * LAST UPDATED WITH SVN#: 6677
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: Logic to limit previous orders to "5"
         */
        //        <logic:iterate id="orderInfo" indexId="idx" collection="<%= orderHistoryInfo %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
        //        <% if (idx.intValue() == 5) break; %>
        //        <option value="<%= orderInfo.getErpSalesId() %>">#<%= orderInfo.getErpSalesId() %> - <%=orderInfo.getOrderStatus().getDisplayName()%> - <%= dateFormatter.format( orderInfo.getRequestedDate() ) %>
        //        </logic:iterate>
        List<Map<String, String>> values = new ArrayList<Map<String, String>>();//new LinkedHashMap<String, String>();
        //List<Map<String, String>> values = new LinkedHashMap<String, String>();
        for (FDOrderInfoI orderInfo : orderInfos) {
            StringBuilder buffer = new StringBuilder("#").append(orderInfo.getErpSalesId()).append(" - ").append(
                    orderInfo.getOrderStatus().getDisplayName()).append(" - ").append(dateFormatter.format(orderInfo.getRequestedDate()));
            HashMap<String, String> item = new HashMap<String, String>();
            item.put(orderInfo.getErpSalesId(), buffer.toString());
            values.add(item);
            if (values.size() >= 5) {
                break;
            }
        }
        return values;
    }

    public ResultBundle submitContactUs(String subject, String orderId, String message, ContactUsData contactUsData) throws FDException {
        ContactFdControllerWrapper tagWrapper = new ContactFdControllerWrapper(this.sessionUser);
        ResultBundle bundle = tagWrapper.submitRequest(subject, orderId, message, contactUsData);
        if (bundle.getActionResult() == null) {
            bundle.setActionResult(new ActionResult());
        }
        return bundle;
    }
}
