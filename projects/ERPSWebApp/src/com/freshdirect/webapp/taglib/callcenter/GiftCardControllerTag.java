package com.freshdirect.webapp.taglib.callcenter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumWebServiceType;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.dataloader.autoorder.create.util.IConstants;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDBulkRecipientModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.framework.util.EnumSearchType;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.EnumGiftCardStatus;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.giftcard.ejb.GiftCardManagerHome;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.taglib.giftcard.GiftCardFormFields;
import com.freshdirect.webapp.util.JspMethods;

public class GiftCardControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName{
	private static final long serialVersionUID = -8417107634078016471L;

	private static Category LOGGER = LoggerFactory.getInstance(GiftCardControllerTag.class);
	FDUser user = null;
	
	// Var's declared in the TLD for this tag
    String actionName = null;
    String successPage = null;
    String resultName = null;
    
    
    public String getSuccessPage() {
        return this.successPage;
    }

    public void setSuccessPage(String sp) {
        this.successPage = sp;
    }

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    
    public String getResultName() {
        return this.resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    
    private final static ServiceLocator LOCATOR = new ServiceLocator();
    
    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        ActionResult result = new ActionResult();
        //
        // perform any actions requested by the user if the request was a POST
        //
        if (("GET".equalsIgnoreCase(request.getMethod()))) {
        	HttpSession session = pageContext.getSession();
            FDSessionUser fs_user = (FDSessionUser)session.getAttribute(USER);        
//            user = fs_user.getUser();        	
            
        	if ("getSearchResults".equalsIgnoreCase(actionName)){
        		String gcNumber=request.getParameter("gcNumber");
        		String recEmail=request.getParameter("recEmail");
        		
        		List list=null;
        		GenericSearchCriteria criteria=new GenericSearchCriteria(EnumSearchType.GIFTCARD_SEARCH);
        		try
        		{
        			
	        		GiftCardManagerHome home= getGiftCardManagerHome();        		        		        	
	        		GiftCardManagerSB remote=home.create();             		        			        		
	        		if(gcNumber!=null && gcNumber.trim().length()>0)
	        		{
	        			if(gcNumber.length()<16)
	        			   criteria.setCriteriaMap("certNum", gcNumber);
	        			else
	        				criteria.setCriteriaMap("gcNumber", gcNumber);
	        			
						list=remote.getGiftCardModel(criteria);							
						request.setAttribute("GC_MODEL_LIST", list);
	        		}
	        		if(recEmail!=null && recEmail.trim().length()>0)
	        		{
	        			criteria.setCriteriaMap("recEmail", recEmail);
						list=remote.getGiftCardModel(criteria);													
						request.setAttribute("GC_MODEL_LIST", list);
	        		}	
				} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
				} catch (CreateException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
				}
       		
        	}
        	
        	else if ("generateGiftCard".equalsIgnoreCase(actionName)){
        		String saleId=request.getParameter("saleId");
	        	 if(saleId!=null && saleId.trim().length()>0)
	        	 {
	        		try
	        		{
	        			FDOrderI order = FDCustomerManager.getOrderForCRM(saleId);
	        			if(order.getOrderStatus().equals(EnumSaleStatus.CANCELED)){
	        				result.addError(new ActionError("generate_error", "Please try again later."));
	        			}
	        			
	        			GiftCardManagerHome home= getGiftCardManagerHome();        		        		        	
		        		GiftCardManagerSB remote=home.create();       
		        		List gcList= remote.getGiftCardForOrder(saleId);
		        		
		        		if(gcList!=null && gcList.size()>0){
		        			ErpGiftCardModel gcModel=(ErpGiftCardModel)gcList.get(0);
		        			request.setAttribute("newGivexNum",gcModel.getAccountNumber());
		        		}
	        			
	        		} catch (RemoteException e1) {
						throw new JspException(e1);
				   } catch (CreateException e1) {
						throw new JspException(e1);
				   } catch(FDResourceException r){
						throw new JspException(r);
				   }
				   
        	   }  
        	}		
        	// FIXME: this action has been moved to fd:GiftCardControllerTag, soon to be removed
        	else if("deleteBulkSavedRecipient".equalsIgnoreCase(actionName)) {
        		user = fs_user.getUser();   
            	String repId = request.getParameter("deleteId");
        		if (repId == null) {
        			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
        		} else {
        			int repIndex = -1;
            		try {
            			repIndex = user.getBulkRecipentList().getRecipientIndex(Integer.parseInt(repId));

            			user.getBulkRecipentList().removeOrderLineById(repIndex);
            			user.getBulkRecipentList().constructFDRecipientsList();
            		} catch (NumberFormatException nfe) {
            			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
            		}
            		if (repIndex == -1) {
            			result.addError(new ActionError("system",SystemMessageList.MSG_IDENTIFY_RECIPIENT));
            		} else {
            			//remove recipient
            			user.getBulkRecipentList().removeRecipient(repIndex);
            		}
        		}
            }

        	
        
        }
        if (("POST".equalsIgnoreCase(request.getMethod()))) {
        	
        	HttpSession session = pageContext.getSession();
            FDSessionUser fs_user = (FDSessionUser)session.getAttribute(USER);        
            user = fs_user.getUser();        	
            JspMethods.dumpRequest(request);                                    

            try {
            	
            	
            	if ("btGiftCard".equalsIgnoreCase(actionName) ) {
                   
            		String fromGivexNum=request.getParameter(EnumUserInfoName.GC_ACCOUNT_FROM.getCode());
            		String toGivexNum=request.getParameter(EnumUserInfoName.GC_ACCOUNT_TO.getCode());
            		//String amountStr=request.getParameter(EnumUserInfoName.GC_AMOUNT.getCode()); 
            		double amount=0;
            		
            		if(fromGivexNum==null || fromGivexNum.length() < 1) {
                        result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_FROM.getCode(), "Invalid or missing givex number "));
                    }
                	
                	if(toGivexNum==null || toGivexNum.length() < 1) {
                        result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), "Invalid or missing givex number"));
                    }
                	
                   if(result.getErrors().isEmpty()) {                        
                    	try
    	        		{	        			
                    	     ErpGiftCardModel fromModel=FDCustomerManager.validateAndGetGiftCardBalance(fromGivexNum);                    	    
                    	     EnumGiftCardStatus status= fromModel.getStatus();
                    	     if(EnumGiftCardStatus.INACTIVE==status){
                                result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_FROM.getCode(), "Some issue with Gift certificate either card on hold or cancelled"));
                    	     }
                    	     
                    	     try{
	                    	     ErpGiftCardModel toModel=FDCustomerManager.validateAndGetGiftCardBalance(toGivexNum);                    	    
	                    	     EnumGiftCardStatus toStatus= toModel.getStatus();
	                    	     
	                    	     if(EnumGiftCardStatus.INACTIVE==toStatus){
	                                 result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), "Some issue with TO Account Number  either card on hold or cancelled"));
	                     	     }
	                    	     
	                    	     // if no error then do the balance transfer
	                    	     if(result.getErrors().isEmpty()) {      
	                    	    	 
	                    	    	 try{
	                    	    		 FDCustomerManager.transferGiftCardBalance(user.getIdentity(),fromGivexNum, toGivexNum, amount);
	                    	    		 result.addError(new ActionError("bt_success", "Balance Transfered Successfully."));
	                    	    	 }catch(Exception e){
	                    	    		 e.printStackTrace();
	                    	    	    result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), "Some issue in transfering the balance received exception :"+e.getMessage()));
	                    	    	 }	                    	    	 
	                    	     }	                    	         	                    	                          	                         	    
                    	     
                    	     } catch(FDResourceException ic){
                                result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_FROM.getCode(), "Invalid givex number for to Account"));
        	        		}                    	                         	                         	                         	                         	                        	                         	                         	   
    	        		} catch(FDResourceException ic){
                            result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_FROM.getCode(), "Invalid givex number Entered for from GiftCard Account"));
    	        		}
                    	
                    }
                }
            	
            	
            	if ("generateGiftCard".equalsIgnoreCase(actionName) ) {
            		
                    user = fs_user.getUser();        	
            		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
            		if(agent == null){            			
            			result.addError(new ActionError("technical_difficulty", "Could not update profile due to technical difficulty."));            			
            		}
                    try{
                    	SavedRecipientModel model=new SavedRecipientModel();
                    	model.setAmount(0);
                    	model.setDeliveryMode(EnumGCDeliveryMode.PDF);
                    	model.setSenderEmail(user.getCustomerInfoModel().getEmail());
                    	model.setSenderName(user.getFirstName());
                    	model.setRecipientEmail(user.getCustomerInfoModel().getEmail());
                    	model.setRecipientName(user.getFirstName());
                    	model.setTemplateId("giftcard_system_default");
                    	model.setPersonalMessage("New 0$ GiftCard is created by "+agent.getFirstName()+" "+agent.getLastName());
                    	model.setGiftCardType(EnumGiftCardType.REGULAR_GIFTCARD);
                    	
                    	FDRecipientList list=new FDRecipientList();
                    	list.addRecipient(model);
                    	user.setRecipientList(list);
                    	user.setGiftCardType(EnumGiftCardType.REGULAR_GIFTCARD);
                    	UserUtil.initializeGiftCart(user);
                    	
                    	
                    	FDActionInfo actionInfo = new FDActionInfo(EnumTransactionSource.CUSTOMER_REP, user.getIdentity(), "GenerateNewGiftCard", "",IConstants.AGENT, user.getPrimaryKey());
                    	CustomerRatingI rating = new CustomerRatingAdaptor(new ProfileModel(), false, 10);                    	            			                    	
                    	Collection<ErpPaymentMethodI> ccards = FDCustomerManager.getPaymentMethods(user.getIdentity());


            			user.getGiftCart().setPaymentMethod((ErpPaymentMethodI) ((ccards.toArray())[0]));
            			//Set the default web service type
            			user.getGiftCart().getDeliveryAddress().setWebServiceType(EnumWebServiceType.GIFT_CARD_PERSONAL);

            			FDReservation reservation= UserUtil.getFDReservation(user.getIdentity().getErpCustomerPK(),null);
            			user.getGiftCart().setDeliveryReservation(reservation);
            			
            			List<ErpRecipentModel> repList = convertSavedToErpRecipientModel(user.getRecipientList().getRecipients(user.getGiftCardType()),user.getIdentity().getErpCustomerPK());	
            			
            		    try {
    						String saleId;
							
								saleId = FDCustomerManager.placeGiftCardOrder(actionInfo, user.getGiftCart(), Collections.<String> emptySet(), false, rating, EnumDlvPassStatus.NONE, repList,false);
							
    						
    						try{
    						   Thread.sleep(30000);
            		        }catch(Exception e){
            		        	e.printStackTrace();
            		        }	
    						
    						GiftCardManagerHome home= getGiftCardManagerHome();        		        		        	
    		        		GiftCardManagerSB remote=home.create();            
    						
    		        		List gcList= remote.getGiftCardForOrder(saleId);
    		        		if(gcList!=null && gcList.size()>0){
    		        			ErpGiftCardModel gcModel=(ErpGiftCardModel)gcList.get(0);
    		        			request.setAttribute("newGivexNum",gcModel.getAccountNumber());
    		        		}else{
    		        			request.setAttribute("waitMessage","wait for 30 second and try entering the sale Id :"+saleId+". If still doesn't work try again later.");
    		        			request.setAttribute("saleId",saleId);
    		        		}    						   
    						
    					} catch(ServiceUnavailableException se){
    						result.addError(new ActionError("service_unavailable",SystemMessageList.MSG_GC_SERVICE_UNAVAILABLE));
    					} catch (ErpFraudException e) {
    						throw new JspException(e);
    					}  catch (ErpAuthorizationException ae) {
    						throw new JspException(ae);
    					}
                    	catch (ErpAddressVerificationException e1) {
						  // TODO Auto-generated catch block
                    		e1.printStackTrace();
                    		result.addError(new ActionError("technical_difficulty", e1.getMessage()));
                    	}
                    } catch (RemoteException e1) {
						throw new JspException(e1);
    			   } catch (CreateException e1) {
						throw new JspException(e1);
    			   }
            	   catch (FDResourceException ex) {
                     LOGGER.warn("FDResourceException while trying to update customer info & addresses", ex);
                     result.addError(new ActionError("technical_difficulty", "Could not update profile due to technical difficulty."));
                  }
            	   user.setRecipientList(new FDRecipientList());	
                }

            	// FIXME: this action has been moved to fd:GiftCardControllerTag, soon to be removed
            	if ("addBulkSavedRecipient".equalsIgnoreCase(actionName) ) {
            		GiftCardFormFields fld = new GiftCardFormFields(request);
                    fld.validateBulkGiftCard(user, result);
                    if(result.getErrors().isEmpty()) {
                    	FDBulkRecipientModel srm = fld.populateBulkSavedRecipient(user);
                                            	
                        user.getBulkRecipentList().addRecipient(srm);  
                        
                    } 
            	}            	
            	// FIXME: this action has been moved to fd:GiftCardControllerTag, soon to be removed
            	else if ("editBulkSavedRecipient".equalsIgnoreCase(actionName)) {            		
            		GiftCardFormFields fld = new GiftCardFormFields(request);
                    fld.validateBulkGiftCard(user, result);
                    if(result.getErrors().isEmpty()) {
                    	FDBulkRecipientModel srm = fld.populateBulkSavedRecipient(user);
                    	
                    	String repId = request.getParameter("recipId");
                		if (repId == null) {
                			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
                		} else {
	                		int repIndex = -1;
	                		try {
	                			repIndex = user.getBulkRecipentList().getRecipientIndex(Integer.parseInt(repId));
	                			
	                			
	                		} catch (NumberFormatException nfe) {
	                			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	                		}
	                		if (repIndex == -1) {
	                			result.addError(new ActionError("system",SystemMessageList.MSG_IDENTIFY_RECIPIENT));
	                		} else {
	                			//update recipient
	                			user.getBulkRecipentList().setRecipient(repIndex, srm);
	                		}
                		}
                    }
                }
               	
            } catch (FDResourceException ex) {
                LOGGER.warn("FDResourceException while trying to update customer info & addresses", ex);
                result.addError(new ActionError("technical_difficulty", "Could not update profile due to technical difficulty."));
            }
            //
            // redirect to success page if an action was successfully performed and a success page was defined
            //
            if (result.getErrors().isEmpty() && (successPage != null)) {
                LOGGER.debug("Success, redirecting to: "+successPage);
                HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
                try {
                    response.sendRedirect(response.encodeRedirectURL(successPage));
                    JspWriter writer = pageContext.getOut();
                    writer.close();
                } catch (IOException ioe) {
                    //
                    // if there was a problem redirecting, continue and evaluate/skip tag body as usual
                    //
                    LOGGER.warn("IOException during redirect", ioe);
                }
            }
        }
        //
        // place the result as a scripting variable in the page
        //
        pageContext.setAttribute(resultName, result);
        return EVAL_BODY_BUFFERED;
    }
    
    
    private List<ErpRecipentModel> convertSavedToErpRecipientModel(List<RecipientModel> savedList,String customerId)
	{
	 	Iterator<RecipientModel> i = savedList.listIterator();
	 	List<ErpRecipentModel> recList=new ArrayList<ErpRecipentModel>();
    	while(i.hasNext()) {    		
    		RecipientModel srm = (RecipientModel)i.next();
    		ErpRecipentModel rm=new ErpRecipentModel();
    		rm.setCustomerId(customerId);
    		rm.toModel(srm);
    		recList.add(rm);
         }
    	return recList;
	}
    
    private static FDDeliveryZoneInfo getZoneInfo(ErpAddressModel address) throws FDResourceException, FDInvalidAddressException {

    	FDDeliveryZoneInfo zInfo =new FDDeliveryZoneInfo("1","1","1", EnumZipCheckResponses.DELIVER);
		return zInfo;
	}
    
    
    private  FDTimeslot getFDTimeSlot() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
    private  FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=getFDTimeSlot();
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION, 
				customerID, addressID, false,null,-1,null,false,null,null);
		return reservation;
    }
    
    	
	private static GiftCardManagerHome getGiftCardManagerHome() {
		try {
			return (GiftCardManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.GiftCardManager");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
}
		
