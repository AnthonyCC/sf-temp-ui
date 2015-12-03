package com.freshdirect.webapp.crm.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.customer.EnumComplaintLineType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumComplaintType;
import com.freshdirect.customer.EnumSendCreditEmail;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.crm.security.CrmSecurityManager;
import com.freshdirect.webapp.taglib.callcenter.ComplaintUtil;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.CallcenterUser;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class MakeGoodOrderUtility {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(MakeGoodOrderUtility.class);
	
	
    private static final String GENERAL_ERR_MSG = "The marked lines have invalid or missing data.";

    // Credit notes
    private static final String MAKE_GOOD_ORDER_COMPLAINT_DESCRIPTION = "Make good 0 credit complaint";

	/**
	 * It provides a getter interface
	 * for acquiring various parameters required
	 * for the utility
	 * 
	 * @author segabor
	 */
	public static interface ParamGetter {
		public String getMakeGoodOrderId();
		public ErpComplaintModel getComplaintModel();
		public FDIdentity getIdentity();
		public String getReferencedOrder();
		public CrmAgentModel getAgentModel();
	}

	public static interface PostAction {
		public void handle(MasqueradeContext masqueradeContext);
	}

	/**
	 * Default implementation that supplies
	 * parameters straight from session
	 * 
	 * @author segabor
	 */
	public static final class SessionParamGetter implements ParamGetter {
		private HttpSession session;
		private FDUserI user;
		
		public SessionParamGetter(HttpSession session) {
			this.session = session;
			if (session != null) {
				user = (FDUserI) session.getAttribute(SessionName.USER);
			}
		}

		@Override
		public String getReferencedOrder() {
			return (String) session.getAttribute("referencedOrder");
		}

		@Override
		public String getMakeGoodOrderId() {
			return (String) session.getAttribute( SessionName.RECENT_ORDER_NUMBER );
		}
		
		@Override
		public FDIdentity getIdentity() {
			return user != null ? user.getIdentity() : null;
		}
		
		@Override
		public ErpComplaintModel getComplaintModel() {
			return (ErpComplaintModel) session.getAttribute( SessionName.MAKEGOOD_COMPLAINT );
		}
		
		@Override
		public CrmAgentModel getAgentModel() {
			return CrmSession.getCurrentAgent(session);
		}
	}
	
	public static void processComplaint(final MasqueradeContext masqueradeContext, ParamGetter getter, PostAction postAction, final String outcome) {
		// add logic to process make good order complaint.
		// MasqueradeContext masqueradeContext = currentUser.getMasqueradeContext();
		String masqueradeMakeGoodOrderId = masqueradeContext==null ? null : masqueradeContext.getMakeGoodFromOrderId();
		
		if ( /* "true".equals( session.getAttribute( "makeGoodOrder" ) ) || */ masqueradeMakeGoodOrderId!=null ) {
			ErpComplaintModel complaintModel = getter.getComplaintModel() /* (ErpComplaintModel)session.getAttribute( SessionName.MAKEGOOD_COMPLAINT ) */;
			String makeGoodOrderId = getter.getMakeGoodOrderId() /* (String)session.getAttribute( SessionName.RECENT_ORDER_NUMBER ) */;
			if (makeGoodOrderId == null){
				makeGoodOrderId = masqueradeMakeGoodOrderId;
			}
			
			complaintModel.setMakegood_sale_id( makeGoodOrderId );
			try {
				addMakeGoodComplaint( masqueradeContext, getter );
			} catch ( Exception e ) {
				LOGGER.error( "Add 0 credit complaint failed for make good order:" + makeGoodOrderId, e );
				// create case here
			}
			
			if ( !outcome.equalsIgnoreCase( Action.ERROR ) ) { //only clear these if there's no error on submit
				postAction.handle(masqueradeContext);
			}
		}
		
	}



	private static void addMakeGoodComplaint( final MasqueradeContext masqueradeContext, ParamGetter getter ) throws FDResourceException, ErpComplaintException {

		final NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );

		LOGGER.debug( "Creating credits for the following departments:" );
		LOGGER.debug( "  Method\t\tDepartment\t\t\tAmount\t\t\tReason\t\t\tCarton Number" );
		LOGGER.debug( "  ------\t\t----------\t\t\t------\t\t\t------\t\t\t------" );
		
		final ErpComplaintModel complaintModel = getter.getComplaintModel();
		
		// FIXME: temporary solution
		// Sort out invalid complaint lines - eg lines without reason set
		//   Note, that the new masq flow pre-populates complaint structure
		//   with lines without particular reason
		//   XC checkout will set reason for items added to cart
		//   See:
		//     CrmMasqueradeUtil.prepareMakeGoodContext(...)
		//     MakeGoodOrderUtility.handleMakeGood(...)
		List<ErpComplaintLineModel> validLines = new ArrayList<ErpComplaintLineModel>( complaintModel.getComplaintLines().size() );
		for ( ErpComplaintLineModel line : complaintModel.getComplaintLines() ) {
			if (line.getReason() == null) {
				continue;
			}
			validLines.add(line);
		}
		complaintModel.setComplaintLines(validLines);

		for ( ErpComplaintLineModel line : complaintModel.getComplaintLines() ) {
			LOGGER.debug( line.getMethod().getStatusCode() + "\t\t" + line.getDepartmentCode() + "\t\t\t" + currencyFormatter.format( line.getAmount() ) + "\t\t\t" + line.getReason().getReason() + "\t\t\t" + line.getCartonNumber() );			
		}
		
		LOGGER.debug( "  Credit Notes: " + complaintModel.getDescription() );
		// HttpSession session = pageContext.getSession();
		FDIdentity identity = getter.getIdentity() /*  getUser().getIdentity() */;
		/* MasqueradeContext masqueradeContext = getUser().getMasqueradeContext(); */

		if (masqueradeContext==null){
			String orderId = getter.getReferencedOrder() /*  (String)session.getAttribute( "referencedOrder" )*/;
			CrmAgentModel agentModel =  getter.getAgentModel() /*  CrmSession.getCurrentAgent(session) */;
			CrmAgentRole agentRole = agentModel.getRole();
			boolean autoApproveAuthorized = CrmSecurityManager.isAutoApproveAuthorized(agentRole.getLdapRoleName());
			Double limit = CrmSecurityManager.getAutoApprovalLimit(agentRole.getLdapRoleName());

			FDCustomerManager.addComplaint( complaintModel, orderId, identity,autoApproveAuthorized,limit );

		} else {
			FDCustomerManager.addComplaint( complaintModel, masqueradeContext.getMakeGoodFromOrderId(), identity, masqueradeContext.isAutoApproveAuthorized(), masqueradeContext.getAutoApprovalLimit());
		}
	}
	

	
	
	// ====================================================================================== //



	/**
	 * Main entry called from {@link FDShoppingCartControllerTag}
	 *
	 * Originally refactored from above class
	 * 
	 * @param orderLineReason Array of complaint reason PKs
	 * @param orderLineId Array of order line PKs
	 * @param session
	 * @param cartOrderLines
	 * @param result
	 * 
	 * @throws JspException
	 */
    public static void handleMakeGood(String[] orderLineReason, String[] orderLineId, HttpSession session, List<FDCartLineI> cartOrderLines, ActionResult result)
            throws FDResourceException {
        checkMissingOrderLineIds(orderLineId, result);
        checkMissingOrderLineReasons(orderLineReason, result);

        ErpComplaintModel complaintModel = new ErpComplaintModel();
        try {
            complaintModel = buildComplaint(orderLineReason, orderLineId, session, cartOrderLines, result);
        } catch (FDResourceException ex) {
            LOGGER.warn("FDResourceException while building ErpComplaintModel", ex);
            throw ex;
        }
        session.setAttribute(SessionName.MAKEGOOD_COMPLAINT, complaintModel);
    }


    // validate order line reasons
    private static void checkMissingOrderLineReasons(String[] orderLineReason, ActionResult result) {
    	for (final String reason : orderLineReason) {
            if (reason == null || "".equals(reason.trim())) {
                result.addError(new ActionError("system", "Make good reason is missing"));
            }
    	}
    }

    // validate order line ids
    private static void checkMissingOrderLineIds(String[] orderLineId, ActionResult result) {
    	for (final String lineId : orderLineId) {
            if (lineId == null || "".equals(lineId.trim())) {
                result.addError(new ActionError("system",
                        "Order line ID from original order is missing, please go back clean all items in cart and select items from original order again."));
            }
        }
    }

    
    /**
     * Builds a valid, well-formed ErpComplaintModel
     * 
     */
    private static ErpComplaintModel buildComplaint(String[] orderLineReason, String[] orderLineId, HttpSession session, List<FDCartLineI> cartOrderLines, ActionResult result)
            throws FDResourceException {
        ErpComplaintModel complaintModel = parseOrderLines(orderLineReason, orderLineId, cartOrderLines, result);
        setComplaintDetails(session, result, complaintModel);
        return complaintModel;
    }

    
    /**
     * Build complaint lines for each order line and validate data.
     * 
     */
    private static ErpComplaintModel parseOrderLines(String[] orderLineReason, String[] orderLineId, List<FDCartLineI> cartOrderLines, ActionResult result)
            throws FDResourceException {
        ErpComplaintModel complaintModel = new ErpComplaintModel();
        List<ErpComplaintLineModel> lines = new ArrayList<ErpComplaintLineModel>();
        List<FDCartLineI> lineItems = new ArrayList<FDCartLineI>(cartOrderLines);

        for (int i = 0; i < orderLineReason.length; i++) {
            final String _orderlineId = orderLineId[i];

            ErpComplaintLineModel line = new ErpComplaintLineModel();
            //
            // allow complaints with a zero quantity...
            //
            // if ( orderLineQty[i] != null && !"".equals(orderLineQty[i]) &&
            // Double.parseDouble(orderLineQty[i]) <= 0 )
            // continue;
            //
            //
            // ...but make sure they at least have a reason code
            //
            if (orderLineReason[i] == null || "".equals(orderLineReason[i]))
                continue;

            // Set up the Complaint Line Model with proper info
            //
            line.setType(EnumComplaintLineType.ORDER_LINE);
			line.setOrderLineId(_orderlineId);
            line.setComplaintLineNumber( Integer.toString(i) );

            double quantity = 0.0;
            line.setQuantity(quantity);

            double amount = 0.0;
            line.setAmount(amount);

            if (orderLineReason[i] != null && !"".equals(orderLineReason[i]))
                line.setReason(ComplaintUtil.getReasonById(orderLineReason[i]));

            line.setMethod(EnumComplaintLineMethod.STORE_CREDIT);

            // pick carton number
            Iterator<FDCartLineI> lineItemIterator = lineItems.iterator();
            while (lineItemIterator.hasNext()) {
            	final FDCartLineI lineItem = lineItemIterator.next();
                if (_orderlineId.equals(lineItem.getOrderLineId())) {
                    line.setCartonNumber(lineItem.getCartonNumber());
                	lineItemIterator.remove();
                	break;
                }
            }
            if (line.getCartonNumber() == null) {
            	LOGGER.error("Failed to find carton number of orderline item " + _orderlineId);
            }

            lines.add(line);
            //
            // Investigate for errors
            //
            if (!line.isValidComplaintLine()) {
                result.addError(new ActionError("ol_error_" + i, "Missing or invalid data in this line."));
                LOGGER.error("Missing or invalid data in this line " + _orderlineId);

                addGeneralError(result);
                continue;
            }
        }

        if (lines.size() > 0)
            complaintModel.addComplaintLines(lines);
        complaintModel.setType(EnumComplaintType.STORE_CREDIT);
        return complaintModel;
    }

    private static void setComplaintDetails(HttpSession session, ActionResult result, ErpComplaintModel complaintModel) {
        CrmAgentModel agent = CrmSession.getCurrentAgent(session);
        CallcenterUser ccUser = (CallcenterUser) session.getAttribute(SessionName.CUSTOMER_SERVICE_REP);
        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
        MasqueradeContext masqueradeContext = user.getMasqueradeContext();
        if (agent != null) {
            complaintModel.setCreatedBy(agent.getUserId());
        } else if (ccUser != null) {
            complaintModel.setCreatedBy(ccUser.getId());
        } else if (masqueradeContext != null) {
            complaintModel.setCreatedBy(masqueradeContext.getAgentId());
        }
        complaintModel.setDescription(MAKE_GOOD_ORDER_COMPLAINT_DESCRIPTION);
        complaintModel.setCreateDate(new java.util.Date());
        complaintModel.setStatus(EnumComplaintStatus.PENDING);
        complaintModel.setEmailOption(EnumSendCreditEmail.DONT_SEND);

    }

    
    
    /**
     * Checks for the presence of a general error message in the ActionResult parameter. If none is present, one is added.
     * 
     * @param ActionResult
     */
    private static void addGeneralError(ActionResult result) {
        if (!result.hasError("general_error_msg"))
            result.addError(new ActionError("general_error_msg", GENERAL_ERR_MSG));
    }
}
