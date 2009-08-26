/*
 * $Workfile:ComplaintCreatorTag.java$
 *
 * $Date:8/6/2003 2:19:20 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.customer.EnumComplaintLineType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumComplaintType;
import com.freshdirect.customer.EnumSendCreditEmail;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCustomerEmailModel;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.mail.FDInfoEmail;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.CallcenterUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.CCFormatter;

public class ComplaintCreatorTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName  {
	private static final long serialVersionUID = -3777077993663056139L;

	private static Category LOGGER 	= LoggerFactory.getInstance( ComplaintCreatorTag.class );
    
    private final String GENERAL_ERR_MSG 		= "The marked lines have invalid or missing data.";
    private final long ALLOWED_CREDIT_WINDOW = 30 * DateUtil.DAY;
    
    // PK from original order
    private String orderId					= null;
    
    // OrderLine credit inputs
    private String [] orderLineQty			= null;
    private String [] orderLineId			= null;
    private String [] orderLineReason		= null;
    private String [] orderLineMethod		= null;
    private String [] orderLineQtyReturned	= null;
    private String [] orderLineCreditAmount = null;
    private String [] orderLineOriginalQty	= null;
    private String [] orderLineOriginalPrice = null;
    private String [] orderLineDeposit        = null;
    private String [] orderLineTaxRate        = null;
    private String [] orderLineCartonNumber	= null;
    private String [] orderLineRetAmnt		= null;
    
    // General credit inputs
    String [] miscCreditAmount      = null;
    String [] miscCreditReason      = null;
    String [] miscCreditMethod      = null;
    
    // Credit notes
    String description				= null;
    
    // Cash-back recipient input
    String paymentMethodType 		= null;
    
    private String result 			= null;
    private String newComplaint		= null;
    private String emailPreview	    = null;
    
    //email options
    String send_email = null;
    String email_type = null;
    String email_template_code=null;
    boolean agent_signature=false;
    String custom_message = null;
    boolean previewEmail = false;
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public void setNewComplaint(String newComplaint) {
        this.newComplaint = newComplaint;
    }
    
    public void setEmailPreview(String emailPreview){
    	this.emailPreview = emailPreview;
    }

    public int doStartTag() throws JspException {
        
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = pageContext.getSession();
        ActionResult actionResult = new ActionResult();
        ErpComplaintModel complaintModel = new ErpComplaintModel();

		FDIdentity identity;
		FDCustomerInfo ci; 
		try {
			 identity = ((FDUserI)session.getAttribute(SessionName.USER)).getIdentity();
			 ci = FDCustomerManager.getCustomerInfo(identity);
		} catch (NullPointerException npe) {
			LOGGER.error("Customer is not drawn into CRM system", npe);
			throw new JspException(npe.getMessage());
		} catch (FDResourceException ex) {
			LOGGER.warn("FDResourceException while building ErpComplaintModel", ex);
			throw new JspException(ex.getMessage());
		}

        XMLEmailI xmlEmailObj = new FDInfoEmail(ci);
        
        if ( "POST".equalsIgnoreCase( request.getMethod() ) ) {
            
            this.getFormData(request, actionResult);
            
            try {
                buildComplaint(actionResult, complaintModel);
				if (actionResult.isSuccess() && previewEmail) {
					String orderId = (String) session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
					if (orderId!=null && ci!=null){
						xmlEmailObj = FDCustomerManager.makePreviewCreditEmail(ci,orderId,complaintModel);
					}
				}
            } catch (FDResourceException ex) {
                LOGGER.warn("FDResourceException while building ErpComplaintModel", ex);
                throw new JspException(ex.getMessage());
            } 
            
        }
        
        pageContext.setAttribute(this.result, actionResult);
        pageContext.setAttribute(this.newComplaint, complaintModel);
        pageContext.setAttribute(this.emailPreview, xmlEmailObj);
        
        return EVAL_BODY_BUFFERED;
        
    } 
    
    
    /**
     * Builds a valid, well-formed ErpComplaintModel
     *
     */
    private void buildComplaint(ActionResult result, ErpComplaintModel complaintModel) throws FDResourceException, JspException  {
        
        this.parseOrderLines(result, complaintModel);
        this.parseMiscellaneous(result, complaintModel);
        this.setComplaintDetails(result, complaintModel);
        this.checkForCashBack(result, complaintModel);
        this.parseEmailOptions(result,complaintModel);
    }
    

    /**
     * Build complaint lines for each order line and validate data.
     *
     */
    private void parseOrderLines(ActionResult result, ErpComplaintModel complaintModel) throws FDResourceException, JspException {
        
        List<ErpComplaintLineModel> lines = new ArrayList<ErpComplaintLineModel>();
        FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder(this.orderId);
        
        // Map<String,Double> => orderline ID -> credit amount
        Map<String,Double> crAmounts = new HashMap<String,Double>();
        Map<String,Double> crRetAmounts = new HashMap<String,Double>();

        for (int i = 0; i < orderLineQty.length; i++) {
            
            ErpComplaintLineModel line = new ErpComplaintLineModel();
            //
            // allow complaints with a zero quantity...
            //
            //if ( orderLineQty[i] != null && !"".equals(orderLineQty[i]) && Double.parseDouble(orderLineQty[i]) <= 0 )
            //    continue;
            //
            final String oID = this.orderLineId[i];

            double retAmount = ((double)Math.round(Double.parseDouble( orderLineRetAmnt[i] )*100))/100;
    		double retAmountSum = retAmount;
    		if (crRetAmounts.get(oID) == null) {
    			crRetAmounts.put(oID, retAmountSum);
    		} else {
    			retAmountSum += crRetAmounts.get(oID);
    			crRetAmounts.put(oID, retAmountSum);
    		}

            //
            // ...but make sure they at least have a reason code
            //
            if (orderLineReason[i] == null || "".equals(orderLineReason[i]))
            	continue;
            ErpOrderLineModel orderline = order.getOrderLine(oID);
            
            // Set up the Complaint Line Model with proper info
            //
            line.setType(EnumComplaintLineType.ORDER_LINE);
			line.setOrderLineId(oID);
            line.setComplaintLineNumber(""+i);
            //line.setDepartment( orderLineDept[i] );


            processCreditAmount(line, orderline, crAmounts, crRetAmounts, i, result);

            if ( orderLineReason[i] != null && !"".equals(orderLineReason[i]) )
                line.setReason( ComplaintUtil.getReasonById(orderLineReason[i]) );
            if ( EnumComplaintLineMethod.STORE_CREDIT.getStatusCode().equals( orderLineMethod[i] ) ) {
                line.setMethod( EnumComplaintLineMethod.STORE_CREDIT );
            } else if ( EnumComplaintLineMethod.CASH_BACK.getStatusCode().equals( orderLineMethod[i] ) ) {
            	line.setMethod( EnumComplaintLineMethod.CASH_BACK );
            }
            
            // assign carton number to complaint line if not null and not 'default' (this comes from dept view)s
            if (orderLineCartonNumber[i] != null && !"default".equalsIgnoreCase(orderLineCartonNumber[i])) {
            	line.setCartonNumber(orderLineCartonNumber[i]);
            }

            lines.add(line);
            //
            // Investigate for errors
            //
            if ( !line.isValidComplaintLine() ) {
                result.addError(new ActionError("ol_error_"+i,"Missing or invalid data in this line."));
                addGeneralError(result);
            }
            
            //
            // Check that item is within appropriate return window (depends on Method and department)
            //
            if ( !FDStoreProperties.getDisableTimeWindowCheck() && !isWithinReturnWindow(line.getMethod() ) ) {
                result.addError(new ActionError("ol_error_"+i,"It is too late to return this item for the designated credit type."));
                addGeneralError(result);
                return;
            }
            
            if (orderLineQty[i] != null && !"".equals(orderLineQty[i]) && orderLineOriginalQty[i] != null && orderLineQtyReturned[i] != null) {
                if (Double.parseDouble(orderLineQty[i]) > Double.parseDouble(orderLineOriginalQty[i]) - Double.parseDouble(orderLineQtyReturned[i])) {
                    result.addError(new ActionError("ol_error_qty_"+i,"Quantity to be returned is too large."));
                    addGeneralError(result);
                }
            }
            
            final double previousAmount = this.getPreviousComplaintAmount(order.getComplaints(), line.getOrderLineId());
            // ErpOrderLineModel orderline = order.getOrderLine(line.getOrderLineId());
            ErpInvoiceLineI invline = order.getInvoiceLine(orderline.getOrderLineNumber());
            final double allowedAmount = MathUtil.roundDecimal((invline != null ? invline.getPrice() : orderline.getPrice()) * (1 + ErpServicesProperties.getCreditBuffer()));
            
            if(MathUtil.roundDecimal(previousAmount + line.getAmount()) > allowedAmount){
            	result.addError(new ActionError("ol_error_"+i, "Amount larger than the allowed amount."));
            	addGeneralError(result);
            }
        }
        
        if (lines.size() > 0)
            complaintModel.addComplaintLines(lines);
        complaintModel.setType(EnumComplaintType.getEnum(complaintModel.getComplaintMethod()));  
    } 
    
    /**
     * 
     * @param complaint current complaint in process
     * @param m order line price accumulator
     * @param i form input index
     */
    private void processCreditAmount(ErpComplaintLineModel line, ErpOrderLineModel orderline, Map<String,Double> crAmounts, Map<String,Double> crRetAmounts, int i, ActionResult result) {
    	final String oID = orderline.getPK().getId();

    	// quantity parameter is set
    	double quantity = 0.0;
        double amount = 0.0;
        double amount_ex = 0.0;
        if ( orderLineQty[i] != null && !"".equals(orderLineQty[i]) ){
        	// quantity is set
            quantity = Double.parseDouble(orderLineQty[i]);
        }	

        // Stage I - set amount by given quantity OR credit amount
        //   check quantity first
    	if (quantity > 0) {
            if (Double.parseDouble(orderLineOriginalQty[i])>0) {
                amount= Double.parseDouble(orderLineQty[i]) * (Double.parseDouble( orderLineOriginalPrice[i])/Double.parseDouble(orderLineOriginalQty[i]) );
                amount_ex = amount;
				// [debug] System.err.println("Amount["+i+"/1]="+amount);

				if (orderLineTaxRate[i] != null && !"".equals(orderLineTaxRate[i])) {
					amount_ex+= (amount_ex * Double.parseDouble( orderLineTaxRate[i]));
					// [debug] System.err.println("Amount["+i+"/2]="+amount_ex);
				}

                if (orderLineDeposit[i] != null && !"".equals(orderLineDeposit[i])) {
                	amount_ex+= Double.parseDouble(orderLineQty[i]) * (Double.parseDouble( orderLineDeposit[i])/Double.parseDouble(orderLineOriginalQty[i]) );
    				// [debug] System.err.println("Amount["+i+"/3]="+amount_ex);
                }

            	line.setQuantity(quantity);
            }
    	} else if(this.orderLineCreditAmount[i] != null && !"".equals(orderLineCreditAmount[i])){
    		// ... then the explicitly set amount
        	amount = Double.parseDouble(orderLineCreditAmount[i]);
        	amount_ex = amount;
        }

		// previously returned dollars
		double retAmountSum = crRetAmounts.get(oID).doubleValue();
		

    	// Stage II - Check amount fits into spendable / freely usable credit amount
    	if (amount > 0) {
    		// Ensure price does not exceed orderline total
    		Double accValue = (Double) crAmounts.get(oID);
    		double acc = (accValue != null ? accValue.doubleValue() : 0);
    		
    		double origTotal = ((double)Math.round(Double.parseDouble( orderLineOriginalPrice[i] )*100))/100;
    		double accumulatedTotal = ((double)Math.round( (amount+acc) *100))/100;

    		double amax = origTotal-retAmountSum;

    		if (amax < accumulatedTotal) {
    			// calculate the difference
    			amount = amax - acc;

    			// total is exceeded
    			crAmounts.put(oID, new Double(amax));

    			// [debug] System.err.println("OL["+i+"]/E: Total $" + max + " exceeded by $"+accumulatedTotal+" for order line " + oID + "; adjusted price=" + amount + ";");

    			result.addError(new ActionError("ol_error_"+i, "Amount exceeded the maximum available "+CCFormatter.formatCurrency(amount)+" for this order."));
            	addGeneralError(result);
            	return;
    		} else {
    			// store the increased value
    			crAmounts.put(oID, new Double(accumulatedTotal));
    			// [debug] System.err.println("OL["+i+"]/N: Store incremented price " + crAmounts.get(oID) + " for " + oID);
    		}
    	}
		// [debug] System.err.println("OL["+i+"]: Set amount " + amount_ex + " for " + oID);
		line.setAmount(amount_ex);
    }
    
    
    private double getPreviousComplaintAmount(Collection<ErpComplaintModel> complaints, String orderlineId) {
    	if(complaints.isEmpty()) {
    		return 0.0;
    	}
    	
    	double amount = 0.0;
    	
    	for(Iterator<ErpComplaintModel> i = complaints.iterator(); i.hasNext(); ) {
    		ErpComplaintModel c = (ErpComplaintModel) i.next();
    		if(EnumComplaintStatus.REJECTED.equals(c.getStatus())) {
    			continue;
    		}
    		ErpComplaintLineModel cl = c.getComplaintLine(orderlineId);
    		if(cl != null) {
    			amount = MathUtil.roundDecimal(amount + cl.getAmount());
    		}
    	}
    	
    	return amount;
    }
    
    
    /**
     * Build complaint lines for each miscellaneous line and validate data.
     *
     */
    private void parseMiscellaneous(ActionResult result, ErpComplaintModel complaintModel) throws FDResourceException {
        
        List<ErpComplaintLineModel> lines = new ArrayList<ErpComplaintLineModel>();
        
        for (int i = 0; i < miscCreditAmount.length; i++) {
            
            if (miscCreditReason[i] == null || "".equals(miscCreditReason[i]) || Double.parseDouble(miscCreditAmount[i]) < 0)
                continue;
            
            ErpComplaintLineModel line = new ErpComplaintLineModel();
            //
            // Set up the Complaint Line Model with proper info
            //
            line.setType(EnumComplaintLineType.DEPARTMENT);
            
            if (miscCreditAmount[i] != null && !"".equals( miscCreditAmount[i] ) )
                line.setAmount( Double.parseDouble(miscCreditAmount[i]) );
            if ( miscCreditReason[i] != null && !"".equals(miscCreditReason[i]) )
                line.setReason( ComplaintUtil.getReasonById(miscCreditReason[i]) );
            if ( EnumComplaintLineMethod.STORE_CREDIT.getStatusCode().equals( miscCreditMethod[i] ) ) {
                line.setMethod( EnumComplaintLineMethod.STORE_CREDIT );
            } else if ( EnumComplaintLineMethod.CASH_BACK.getStatusCode().equals( miscCreditMethod[i] ) ) {
                line.setMethod( EnumComplaintLineMethod.CASH_BACK );
            }
            lines.add(line);
            //
            // Investigate for errors
            //
            if ( !line.isValidComplaintLine() ) {
                result.addError(new ActionError("misc_error_"+i,"Missing or invalid data in this line."));
                addGeneralError(result);
            }
            
        } // end for
        
        if (lines.size() > 0)
            complaintModel.addComplaintLines(lines);
        complaintModel.setType(EnumComplaintType.getEnum(complaintModel.getComplaintMethod())); 
        
    } // method parseMiscellaneous
    
    private void parseEmailOptions(ActionResult result, ErpComplaintModel complaintModel) {
    	ErpCustomerEmailModel  cem = new ErpCustomerEmailModel();
		
		//check for an amount on the  complaint greater than 0, cause we doent want to send 
		// an email when 0 dollars are sent.
		
    	if (send_email!= null &&  !EnumSendCreditEmail.DONT_SEND.getName().equals(send_email)) {
    		if (complaintModel.getAmount()< 0.01) {
				result.addError(new ActionError("send_email","Cannot send email for complaints without credits."));
    		}
    		
			complaintModel.setEmailOption(EnumSendCreditEmail.getEnum(send_email));
			complaintModel.setCustomerEmail(cem);
			
			if (custom_message!=null && !"".equals(custom_message)) {
				cem.setCustomMessage(custom_message);
			}
			
			if (agent_signature) {
				CrmAgentModel agent = CrmSession.getCurrentAgent(pageContext.getSession());
				cem.setSignature(agent.getFirstName());
			}
    	} else {
			complaintModel.setEmailOption(EnumSendCreditEmail.DONT_SEND);
			if (previewEmail ) {
				result.addError(new ActionError("previewEmail","Can't preview, Dont send email was selected"));
			}
    	}
    }
    
    private void setComplaintDetails(ActionResult result, ErpComplaintModel complaintModel) { 
		CrmAgentModel agent = CrmSession.getCurrentAgent(pageContext.getSession());
			if (agent != null) {
				complaintModel.setCreatedBy(agent.getUserId());
			} else {
				CallcenterUser ccUser = (CallcenterUser) pageContext.getSession().getAttribute(SessionName.CUSTOMER_SERVICE_REP);	
				complaintModel.setCreatedBy(ccUser.getId());
			}

		result.addError(this.description == null || "".equals(this.description),"credit_description", "Must enter credit description and/or reason.");
        complaintModel.setDescription(this.description);
        complaintModel.setCreateDate(new java.util.Date());
        complaintModel.setStatus(EnumComplaintStatus.PENDING);
    } // method setComplaintDetails
    
    
    private void checkForCashBack(ActionResult result, ErpComplaintModel complaintModel) {
        if (complaintModel.getCashBackAmount() > 0 && paymentMethodType == null) {
            result.addError(new ActionError("payment_method_type","Must select a card or mailing address in order to issue cash back to a customer."));
        }
    } // method checkForCashBack
    
    private boolean isWithinReturnWindow(EnumComplaintLineMethod method) throws FDResourceException {
        //
        // Get delivery date and today's date
        //
        long deliveryDateInMillis = getOrder(orderId).getRequestedDate().getTime();
        long todaysDateInMillis = Calendar.getInstance().getTime().getTime();
        
        return (todaysDateInMillis - deliveryDateInMillis) < ALLOWED_CREDIT_WINDOW;
    }
    
    
    /**
     * Checks for the presence of a general error message in the ActionResult parameter. If none is
     * present, one is added.
     *
     * @param ActionResult
     */
    private void addGeneralError(ActionResult result) {
        if ( !result.hasError("general_error_msg") )
            result.addError(new ActionError("general_error_msg", GENERAL_ERR_MSG));
    }
    
    
    /**
     * Gathers registration-specific data (i.e., customer data)
     *
     */
    private void getFormData(HttpServletRequest request, ActionResult result) {
        
        orderId					= request.getParameter("orig_sale_id");
        this.orderLineId 		= request.getParameterValues("orderlineId");
        orderLineQty 			= request.getParameterValues("ol_credit_qty");
        orderLineQtyReturned	= request.getParameterValues("ol_credit_qty_returned");
        orderLineCreditAmount	= request.getParameterValues("ol_credit_amount");
        orderLineReason 		= request.getParameterValues("ol_credit_reason");
        orderLineMethod			= request.getParameterValues("ol_credit_method");
        orderLineOriginalQty	= request.getParameterValues("ol_orig_qty");
        orderLineOriginalPrice	= request.getParameterValues("ol_orig_price");
		orderLineDeposit        = request.getParameterValues("ol_deposit_value");
		orderLineTaxRate        = request.getParameterValues("ol_tax_rate");
		orderLineCartonNumber	= request.getParameterValues("ol_cartnum");
		orderLineRetAmnt		= request.getParameterValues("ol_ret_amount");

        miscCreditAmount          = request.getParameterValues("misc_credit_amount");
        miscCreditReason          = request.getParameterValues("misc_credit_reason");
        miscCreditMethod          = request.getParameterValues("misc_credit_method");
        
        description				= request.getParameter("description");
        paymentMethodType		= request.getParameter("payment_method_type");

		send_email  = request.getParameter("send_email");
		email_type = request.getParameter("email_type");
		email_template_code = request.getParameter("email_template_code");
		custom_message = request.getParameter("custom_message");
		agent_signature = "yes".equalsIgnoreCase(request.getParameter("agent_signature"));	
 		previewEmail = "yes".equalsIgnoreCase(request.getParameter("previewEmail")) ? true : false;
        
    } // method getFormData
    
    
    /*
     * utility method
     */
    private FDOrderI getOrder(String orderId) throws FDResourceException {
        return FDCustomerManager.getOrder(orderId);
    }   
}