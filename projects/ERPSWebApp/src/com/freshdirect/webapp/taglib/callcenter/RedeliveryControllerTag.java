/**
 * 
 * RedeliveryControllerTag.java
 * Created Nov 27, 2002
 */
package com.freshdirect.webapp.taglib.callcenter;

/**
 *
 *  @author knadeem
 */
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import com.freshdirect.framework.webapp.*;
import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.customer.*;

import com.freshdirect.webapp.taglib.*;
import com.freshdirect.webapp.taglib.fdstore.*;

public class RedeliveryControllerTag extends AbstractControllerTag implements SessionName {
	
	private String orderNumber;
	
	public void setOrderNumber(String orderNumber){
		this.orderNumber = orderNumber;
	}
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = this.getActionName();
		HttpSession session = request.getSession();
		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		if("schedule_redelivery".equalsIgnoreCase(actionName)){
			RedeliveryForm form = new RedeliveryForm(orderNumber);
			form.populateForm(request);
			form.validateForm(actionResult);
			try{			
				CallCenterServices.scheduleRedelivery(currentUser.getIdentity(), orderNumber, form.getRedeliveryModel());
			}catch(ErpTransactionException te){
				te.printStackTrace();
				throw new JspException(te.getMessage());	
			}catch(FDResourceException re){
				re.printStackTrace();
				throw new JspException(re.getMessage());
			}
		} else if ("return".equalsIgnoreCase(actionName)) {
            try {
                CallCenterServices.changeRedeliveryToReturn(currentUser.getIdentity(), orderNumber);
            }catch (ErpSaleNotFoundException snfe) {
                snfe.printStackTrace();
				throw new JspException(snfe.getMessage());
            }catch(ErpTransactionException te){
				te.printStackTrace();
				throw new JspException(te.getMessage());	
			}catch(FDResourceException re){
				re.printStackTrace();
				throw new JspException(re.getMessage());
			}
        }
		return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }
    
    private static class RedeliveryForm implements WebFormI {
    	
    	private String saleId;
    	private String timeslotId;
		
		public RedeliveryForm(String saleId){
			this.saleId = saleId;
		}
		
		public void populateForm(HttpServletRequest request) {
			this.timeslotId = request.getParameter("deliveryTimeslotId");
		}
		
		public void validateForm(ActionResult result){
			if(this.timeslotId == null || this.timeslotId.length() <= 0){
				result.addError(new ActionError("Missing Timeslot", "Please select a timeslot for delivery"));
			}
		}
		
		public ErpRedeliveryModel getRedeliveryModel() throws FDResourceException {
			
			FDTimeslot timeslot = FDDeliveryManager.getInstance().getTimeslotsById(this.timeslotId);
			FDOrderI order = FDCustomerManager.getOrder(saleId);
			FDReservation reservation = order.getDeliveryReservation();
			
			ErpDeliveryInfoModel deliveryInfo = new ErpDeliveryInfoModel();
			deliveryInfo.setDeliveryAddress(order.getDeliveryAddress());
			deliveryInfo.setDeliveryCutoffTime(timeslot.getCutoffDateTime());
			deliveryInfo.setDeliveryEndTime(timeslot.getEndDateTime());
			deliveryInfo.setDeliveryStartTime(timeslot.getBegDateTime());
			deliveryInfo.setDeliveryReservationId(reservation.getPK().getId());
			deliveryInfo.setDeliveryZone(order.getDeliveryZone());
			deliveryInfo.setDepotLocationId(order.getDepotFacility());
			deliveryInfo.setDeliveryType(order.getDeliveryType());
			
			ErpRedeliveryModel redeliveryModel = new ErpRedeliveryModel();
			redeliveryModel.setDeliveryInfo(deliveryInfo);
			redeliveryModel.setTransactionDate(new Date());
			redeliveryModel.setTransactionSource(EnumTransactionSource.CUSTOMER_REP);
			
			return redeliveryModel;
		}	
    }
    
    
}
