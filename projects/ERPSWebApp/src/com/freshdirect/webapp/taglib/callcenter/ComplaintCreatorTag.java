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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.freshdirect.fdstore.customer.FDCartLineI;
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
	private static final double EPSILON = 0.005;	// tolerance threshol

	private static final long serialVersionUID = -3777077993663056139L;

	private static Category LOGGER 	= LoggerFactory.getInstance( ComplaintCreatorTag.class );
    
    private final String GENERAL_ERR_MSG 		= "The marked lines have invalid or missing data.";
    private final long ALLOWED_CREDIT_WINDOW = 30 * DateUtil.DAY;
    
    // PK from original order
    private String orderId					= null;
    
    // OrderLine credit inputs
    private String [] orderLineQty			= null;	// credit quantity
    private String [] orderLineId			= null;
    private String [] orderLineReason		= null;	// credit reason
    private String [] orderLineMethod		= null;	// credit method
    private String [] orderLineQtyReturned	= null;	// quantity of previous returns
    private String [] orderLineCreditAmount = null;	// explicit credit value
    private String [] orderLineOriginalQty	= null; // orderline quantity (per split items)
    private String [] orderLineOriginalPrice = null; // orderline total price
    private String [] orderLineDeposit        = null; // deposit
    private String [] orderLineTaxRate        = null; // tax rate
    private String [] orderLineCartonNumber	= null; // ID of carton that contains the product (optional - only in cartonized view)
    
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
     * Orderline entity. It can be single or split.
	 *
     * @author segabor
     *
     */
    static class OLStat {
    	public String	orderlineId;
    	public Set<Integer> indices = new HashSet<Integer>(); // set of indices pointing to form table entries
    	public double	prevCredits = 0; // sum of already issued credits
    	public double	newCredits = 0;  // (new) credit to be issued
    	
    	public double	price = 0; // original (total) price
    	
    	public double		quantity = 0; // quantity to be credited
    	public double		prevQuantity = 0; // 
    	
    	public OLStat(String id, double prev, double prevQty) {
    		this.orderlineId = id;
    		this.prevCredits = prev;
    		this.prevQuantity = prevQty;
    	}

    	public void setPrice(double price) {
    		this.price = price;
    	}
    	
    	/**
    	 * Returns the (total) net price of orderline
    	 * @return
    	 */
    	public double getPrice() {
    		return this.price;
    	}
    	
    	public double getPrevCredits() {
    		return this.prevCredits;
    	}
    	
    	public void addIndex(int ix) {
    		this.indices.add(ix);
    	}

    	public Set<Integer> getIndices() {
    		return new HashSet<Integer>(this.indices);
    	}
    	
    	/**
    	 * Credit to be issued for this particular (split) order line
    	 * @return
    	 */
    	public double getCredits() {
    		return this.newCredits;
    	}
    	
    	public void setCredits(double amnt) {
    		this.newCredits = amnt;
    	}

    	public void addCredit(double amnt) {
    		this.newCredits += amnt;
    	}

    	/**
    	 * Return quantity to be returned
    	 * @return
    	 */
    	public double getQuantity() {
    		return this.quantity;
    	}

    	public double getPrevQuantity() {
    		return this.prevQuantity;
    	}

    	public void addQuantity(double qty) {
    		this.quantity += qty;
    	}
    	
    	/**
    	 * Is order line single or split into more cartons?
    	 * 
    	 * @return
    	 */
    	public boolean isSingle() {
    		return this.indices.size() == 1;
    	}

    	/**
    	 * Is orderline split?
    	 * @return
    	 */
    	public boolean isSplit() {
    		return this.indices.size() > 1;
    	}
    }

    /**
     * Build complaint lines for each order line and validate data.
     *
     */
    private void parseOrderLines(ActionResult result, ErpComplaintModel complaintModel) throws FDResourceException, JspException {
        
        List<ErpComplaintLineModel> lines = new ArrayList<ErpComplaintLineModel>();
        FDOrderAdapter order = (FDOrderAdapter) FDCustomerManager.getOrder(this.orderId);
        
        Map<String,OLStat> olstat = new HashMap<String,OLStat>();
        
        /*
         * Get already issued credits for each order lines
         */
        for (int i = 0; i < orderLineQty.length; i++) {
            final double previousAmount = this.getPreviousComplaintAmount(order.getComplaints(), this.orderLineId[i]);
            final double previousQty = this.getPreviousQuantitiesReturned(order.getComplaints(), this.orderLineId[i]);            
            if (olstat.get(this.orderLineId[i]) == null) {
            	OLStat s = new OLStat(this.orderLineId[i], previousAmount, previousQty);
            	olstat.put(this.orderLineId[i], s );
            }
            olstat.get(this.orderLineId[i]).addIndex(i);
        }


        for (int i = 0; i < orderLineQty.length; i++) {
            
            ErpComplaintLineModel line = new ErpComplaintLineModel();
            //
            // allow complaints with a zero quantity...
            //
            //if ( orderLineQty[i] != null && !"".equals(orderLineQty[i]) && Double.parseDouble(orderLineQty[i]) <= 0 )
            //    continue;
            //
            //
            // ...but make sure they at least have a reason code
            //
            if (orderLineReason[i] == null || "".equals(orderLineReason[i])) continue;
	            final String oID = this.orderLineId[i];
	            ErpOrderLineModel orderline = order.getOrderLine(oID);
            
            // Set up the Complaint Line Model with proper info
            //
            line.setType(EnumComplaintLineType.ORDER_LINE);
			line.setOrderLineId(oID);
            line.setComplaintLineNumber(""+i);
            //line.setDepartment( orderLineDept[i] );


            processCreditAmount(line, orderline, order.getInvoiceLine(oID), olstat, result);

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

            /* if (orderLineQty[i] != null && !"".equals(orderLineQty[i]) && orderLineOriginalQty[i] != null && orderLineQtyReturned[i] != null) {
                if (Double.parseDouble(orderLineQty[i]) > Double.parseDouble(orderLineOriginalQty[i]) - Double.parseDouble(orderLineQtyReturned[i])) {
                    result.addError(new ActionError("ol_error_qty_"+i,"Quantity to be returned is too large."));
                    addGeneralError(result);
                }
            } */
            
            final double previousAmount = olstat.get(oID).getPrevCredits() /* this.getPreviousComplaintAmount(order.getComplaints(), line.getOrderLineId()) */;
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
    private void processCreditAmount(ErpComplaintLineModel line, final ErpOrderLineModel orderline, final ErpInvoiceLineI invline, Map<String,OLStat> stat, ActionResult result) {
    	final String oID = orderline.getPK().getId();
    	final OLStat st = stat.get(oID);

    	final int i = Integer.parseInt(line.getComplaintLineNumber());

    	// quantity parameter is set
    	final double quantity; // credit quantity
        final double amount; // credit amount increased with various rates

        double taxRate = 0.0;
        double deposit = 0.0;

        final double origQty = Double.parseDouble(orderLineOriginalQty[i]); // original orderline quantity (eg. in the invoice)
        final double netTotal = Double.parseDouble( orderLineOriginalPrice[i]);

        // additional rates
        if (orderLineTaxRate[i] != null && !"".equals(orderLineTaxRate[i]))
        	taxRate = Double.parseDouble( orderLineTaxRate[i]);
        if (orderLineDeposit[i] != null && !"".equals(orderLineDeposit[i]))
        	deposit = Double.parseDouble( orderLineDeposit[i]);


        if ( orderLineQty[i] != null && !"".equals(orderLineQty[i]) ){
        	// quantity is set
            quantity = Double.parseDouble(orderLineQty[i]);
            
            if (st.getPrevQuantity() + st.getQuantity() + quantity > origQty) {
                result.addError(new ActionError("ol_error_qty_"+i,"Quantity to be returned is too large."));
                addGeneralError(result);
                return;
            }
            
        	line.setQuantity(quantity);
        	st.addQuantity(quantity);
        } else {
        	 quantity = 0.0;
        }




        // Stage I - set amount by given quantity OR credit amount
        //   check quantity first
    	if (quantity > 0) {
            double x = quantity * ( netTotal/origQty );

            // Handle tax
			if (taxRate > 0) {
				x+= (x * taxRate);
			}

			// Handle deposit value
            if (deposit > 0) {
            	x+= quantity * ( deposit/origQty ); // += credit qty * ( deposit / orig qty)
            }
            amount = MathUtil.roundDecimal(x);
    	} else if (this.orderLineCreditAmount[i] != null && !"".equals(orderLineCreditAmount[i])) {
    		// ... or get the explicit value
        	amount = Double.parseDouble(orderLineCreditAmount[i]);
        } else {
        	amount = 0;
        }






    	// Stage II - Check amount size
    	if (amount > 0) {
    		double credits = amount+st.getCredits();

            final double allowedAmount = MathUtil.roundDecimal((invline != null ? invline.getPrice() : orderline.getPrice()) * (1 + ErpServicesProperties.getCreditBuffer()));
            
            if (allowedAmount < MathUtil.roundDecimal(st.getPrevCredits() + credits) ) {
            	result.addError(new ActionError("ol_error_"+i, "Amount larger than the allowed amount."));
            	addGeneralError(result);
            	return;
    		} else {
    			// store the increased value
    			st.setCredits(credits);
    			
    			line.setAmount( amount );
    		}
    	} else {
    		line.setAmount(0);
    	}
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
    		
    		for (ErpComplaintLineModel cl : c.getComplaintLines()) {
    			if (orderlineId.equalsIgnoreCase(cl.getOrderLineId())) {
        			amount = MathUtil.roundDecimal(amount + cl.getAmount());
    			}
    		}
    	}
    	
    	return amount;
    }
    
    
    private double getPreviousQuantitiesReturned(Collection<ErpComplaintModel> complaints, String orderlineId) {
    	double qty = 0;
    	for(Iterator<ErpComplaintModel> i = complaints.iterator(); i.hasNext(); ) {
    		ErpComplaintModel c = (ErpComplaintModel) i.next();
    		if(EnumComplaintStatus.REJECTED.equals(c.getStatus())) {
    			continue;
    		}
    		
    		for (ErpComplaintLineModel cl : c.getComplaintLines()) {
    			if (orderlineId.equalsIgnoreCase(cl.getOrderLineId())) {
    				qty += cl.getQuantity();
    			}
    		}
    	}
    	return qty;
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
        orderLineId 			= request.getParameterValues("orderlineId");
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