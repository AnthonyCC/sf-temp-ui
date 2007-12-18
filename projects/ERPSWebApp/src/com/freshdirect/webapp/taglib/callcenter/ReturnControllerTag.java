/*
 * 
 * ReturnControllerTag.java
 * Date: Oct 31, 2002 Time: 6:28:22 PM
 */
package com.freshdirect.webapp.taglib.callcenter;

/**
 * 
 * @author knadeem
 */
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ReturnControllerTag extends AbstractControllerTag implements SessionName{
	
	private String orderNumber;
	
	private FDOrderI order;
	
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
		try{
			order = FDCustomerManager.getOrder(orderNumber);
		}catch(FDResourceException e){
			actionResult.addError(new ActionError("technical_difficulty", "Cannot lookup the order for validation"));
		}
		ReturnOrderForm form = new ReturnOrderForm();
		form.populateForm(request);
		form.validateForm(actionResult);

		//This is called to check if delivery pass is returned and decide whether delivery charge
		//need to be applied or not.
		form.handleCharges(request);
		
		if(actionResult.isSuccess()){
			try{
				ErpReturnOrderModel returnOrder = form.getReturnOrder();
				if(returnOrder.getInvoiceLines().size() > 0){
					//it is a csr entering a return
					if("add_return".equalsIgnoreCase(actionName)){			
						CallCenterServices.returnOrder(currentUser.getIdentity(), orderNumber, form.getReturnOrder());
					}
					//it is a supervisor approving a return
					if("approve_return".equalsIgnoreCase(actionName)){
						CallCenterServices.approveReturn(currentUser.getIdentity(), orderNumber, form.getReturnOrder());
						//Remove the Delivery Pass Session ID If any. So that any changes to the pass will be reloaded.
						session.removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);
						if(returnOrder.isContainsDeliveryPass()) {
							//Load the delivery pass status from DB.
							currentUser.updateDlvPassInfo();
						}
					}
				}
				CrmSession.invalidateCachedOrder(session);
			}catch (ErpTransactionException te){
				te.printStackTrace();
				actionResult.addError(new ActionError("order_status", "This order is not in the proper state to be returned."));
			}catch (FDResourceException ex) {
				ex.printStackTrace();
				throw new JspException(ex.getMessage());
			}
		}else{
			System.out.println("Result: "+actionResult.isSuccess());
			for(Iterator i = actionResult.getErrors().iterator(); i.hasNext(); ){
				System.out.println(((ActionError)i.next()).getDescription());
			}
		}
						
		return true;
	}
	private class ReturnOrderForm implements WebFormI {
		
		private ErpReturnOrderModel returnOrder;	
		
		public ReturnOrderForm (){
			this.returnOrder = new ErpReturnOrderModel();
		}
		
		public ErpReturnOrderModel getReturnOrder(){
			return this.returnOrder;
		}
		
		public void populateForm(HttpServletRequest request) {
			Enumeration paramNames = request.getParameterNames();
			boolean restockingApplied = false;
			List returnLines = new ArrayList();
			while(paramNames.hasMoreElements()){
				String paramName = (String)paramNames.nextElement();
				String returnQuantity = null;
				String lineNumber = null;
				String restocking = null;
				
				if(paramName.startsWith("ret_qty_")){
					returnQuantity = request.getParameter(paramName);
					lineNumber = paramName.substring("ret_qty_".length(), paramName.length());
					restocking = request.getParameter("fee_"+lineNumber);
				}
				if(returnQuantity != null && !"".equals(returnQuantity)){
					ErpReturnLineModel returnLine = new ErpReturnLineModel();
					returnLine.setLineNumber(lineNumber);
					returnLine.setQuantity(Double.parseDouble(returnQuantity));
					returnLine.setRestockingOnly((restocking == null ? false : "restock_fee".equalsIgnoreCase(restocking) ? true : false));
					if(returnLine.isRestockingOnly()) {

						restockingApplied = true;
					}
					returnLines.add(returnLine);
				}
			}
			String dlvCharge = request.getParameter("delivery_checkbox");
			String phoneCharge = request.getParameter("phone_checkbox");
			String miscCharge = request.getParameter("misc_checkbox");
			List charges = new ArrayList();
			/*
			 * This block has been commented out because the going through
			 * right now the charge lines corresponding to INV action type 
			 * for delivery charges that are waived shows up $0.00 instead
			 * of the actual delivery charge and promotion amount as 1.  
			 * Time being the return process is looking up the charge lines
			 * from create or modify order.
			 * 
			 */
			/*
			for(Iterator i = order.getInvoicedCharges().iterator(); i.hasNext(); ){
				ErpChargeLineModel charge = new ErpChargeLineModel((ErpChargeLineModel)i.next());
				charges.add(charge);
				if(EnumChargeType.DELIVERY.equals(charge.getType()) && dlvCharge != null) {
					charge.setDiscount(new Discount(null, EnumDiscountType.PERCENT_OFF, 1.0));
					continue;
				}
				if(EnumChargeType.PHONE.equals(charge.getType()) && phoneCharge != null) {
					charge.setDiscount(new Discount(null, EnumDiscountType.PERCENT_OFF, 1.0));
					continue;
				}
				if(EnumChargeType.MISCELLANEOUS.equals(charge.getType()) && miscCharge != null) {
					charge.setDiscount(new Discount(null, EnumDiscountType.PERCENT_OFF, 1.0));
					continue;
				}
			}*/
			for(Iterator i = order.getCharges().iterator(); i.hasNext(); ){
				ErpChargeLineModel charge = new ErpChargeLineModel((ErpChargeLineModel)i.next());
				charges.add(charge);
				if(EnumChargeType.DELIVERY.equals(charge.getType()) && dlvCharge != null) {
					charge.setDiscount(new Discount("DELIVERY", EnumDiscountType.PERCENT_OFF, 1.0));
					continue;
				}
				if(EnumChargeType.PHONE.equals(charge.getType()) && phoneCharge != null) {
					charge.setDiscount(new Discount(null, EnumDiscountType.PERCENT_OFF, 1.0));
					continue;
				}
				if(EnumChargeType.MISCELLANEOUS.equals(charge.getType()) && miscCharge != null) {
					charge.setDiscount(new Discount("DELIVERY", EnumDiscountType.PERCENT_OFF, 1.0));
					continue;
				}
			}
			
			returnOrder.setInvoiceLines(returnLines);
			returnOrder.setCharges(charges);
			//Set Delivery Pass Attributes.
			returnOrder.setDlvPassApplied(order.isDlvPassApplied());
			returnOrder.setDeliveryPassId(order.getDeliveryPassId());
			returnOrder.setRestockingApplied(restockingApplied);
		}
		
		public void validateForm(ActionResult result){ 
			if(order == null){
				result.addError(new ActionError("technical_difficulty", "Cannot lookup the order for validation"));
				return;
			}
			if(!order.hasInvoice()){
				result.addError(new ActionError("Wrong Status", "No invoice added yet cannot do return"));
				return;
			}
			for (Iterator i = this.returnOrder.getInvoiceLines().iterator(); i.hasNext(); ) {
				ErpReturnLineModel returnLine = (ErpReturnLineModel)i.next();

				for(Iterator j = order.getOrderLines().iterator(); j.hasNext(); ){
					FDCartLineI line = (FDCartLineI) j.next();
					 
					ErpInvoiceLineI invoiceLine = line.getInvoiceLine();
					if(!returnLine.getLineNumber().equals(invoiceLine.getOrderLineNumber())){
						continue;
					}
					if(line.isSoldBySalesUnits() || line.isSoldByLb()){
						if((int)Math.round(returnLine.getQuantity()*100) != (int)Math.round(invoiceLine.getQuantity()*100)){
							result.addError(new ActionError("qty_error_"+returnLine.getLineNumber(), "you can only return full qty"));
						}
					}else{
						if((int)Math.round(returnLine.getQuantity()*100) > (int)Math.round(invoiceLine.getQuantity()*100)){
							result.addError(new ActionError("qty_error_"+returnLine.getLineNumber(), "qty more than delivered qty"));
						}
					}
					if(line.lookupFDProduct().isDeliveryPass() && returnLine.getQuantity() > 0) {
						//Return order contains a delivery pass.
						returnOrder.setContainsDeliveryPass(true);
					}
					
				}
			}
		}
		
		/**
		 * This method is called only when return order contains a delivery pass.
		 * @param request
		 */
		public void handleCharges(HttpServletRequest request){
			if(returnOrder.isContainsDeliveryPass() && returnOrder.isDlvPassApplied()){
				//Delivery pass is also returned and delivery pass was applied to this order.
				String dlvCharge = request.getParameter("delivery_checkbox");
				if(dlvCharge ==  null) {
					//The CSR did not waive off the dlv fee.
					//Get the Delivery charge line.
					ErpChargeLineModel dlvChargeLine = returnOrder.getCharge(EnumChargeType.DELIVERY);
					ErpChargeLineModel mscChargeLine = returnOrder.getCharge(EnumChargeType.MISCELLANEOUS);
					if(returnOrder.isRestockingApplied()){
						//The CSR did not waive off the delivery fee and restocking fee was applied.
						//Apply delivery fee and msc charges is any.
						dlvChargeLine.setDiscount(null);
						if(mscChargeLine.getAmount() > 0.0){
							mscChargeLine.setDiscount(null);	
						}
						
					} else {
						//The CSR did not waive off the delivery fee and no restocking fee was applied.
						//System automatically Waives delivery fee and msc charges is any.
						dlvChargeLine.setDiscount(new Discount("DELIVERY", EnumDiscountType.PERCENT_OFF, 1.0));
						if(mscChargeLine.getAmount() > 0.0){
							mscChargeLine.setDiscount(new Discount("DELIVERY", EnumDiscountType.PERCENT_OFF, 1.0));	
						}
						
					}
					
				}
			 }
			if(!returnOrder.isContainsDeliveryPass() && returnOrder.isDlvPassApplied()){
				//Delivery pass is not returned/Not the original order and delivery pass was applied to this order.
				String dlvCharge = request.getParameter("delivery_checkbox");
				if(dlvCharge ==  null) {
					//If csr did not waive the delivery fee. Get the Delivery charge line.
					if(!returnOrder.isRestockingApplied()){
						ErpChargeLineModel dlvChargeLine = returnOrder.getCharge(EnumChargeType.DELIVERY);
						ErpChargeLineModel mscChargeLine = returnOrder.getCharge(EnumChargeType.MISCELLANEOUS);
						//No restocking fee was applied.
						//System automatically Waives delivery fee and msc charges is any.
						dlvChargeLine.setDiscount(new Discount("DELIVERY", EnumDiscountType.PERCENT_OFF, 1.0));
						if(mscChargeLine.getAmount() > 0.0){
							mscChargeLine.setDiscount(new Discount("DELIVERY", EnumDiscountType.PERCENT_OFF, 1.0));	
						}
						
					}
				}

				
			}
		}
	}
		
	public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

}
