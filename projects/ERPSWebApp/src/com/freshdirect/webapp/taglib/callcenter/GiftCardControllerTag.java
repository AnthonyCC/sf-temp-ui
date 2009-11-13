package com.freshdirect.webapp.taglib.callcenter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

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
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.EnumSearchType;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDBulkRecipientModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.EnumGiftCardStatus;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ejb.GiftCardManagerHome;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.AddSavedRecipientControllerTag;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.JspMethods;

public class GiftCardControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName{

	private static Category LOGGER = LoggerFactory.getInstance(AddSavedRecipientControllerTag.class);
	FDUser user = null;
	
	// Var's declared in the TLD for this tag
    String actionName = null;
    String successPage = null;
    String resultName = null;
    
    String fldAmount = null;
    String fldAltAmount = null;
    String fldYourName = null;
    String fldYourEmail = null;
    String fldRecipientName = null;
    String fldRecipientEmail = null;
    String fldDeliveryMethod = null;
    String fldMessage = null;
    String gcTemplateId = null;
    String fldQuantity=null;
    
   
    
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
        		GenericSearchCriteria criteria=new GenericSearchCriteria(com.freshdirect.framework.util.EnumSearchType.GIFTCARD_SEARCH);
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
	        			FDOrderI order = FDCustomerManager.getOrder(saleId);
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
        	else if("deleteBulkSavedRecipient".equalsIgnoreCase(actionName)) {
        		user = fs_user.getUser();   
            	String repId = request.getParameter("deleteId");
        		if (repId == null) {
        			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
        		} else {
        			int repIndex = -1;
            		try {
            			repIndex = user.getBulkRecipentList().getRecipientIndex(Integer.parseInt(repId));
            			FDBulkRecipientModel m=user.getBulkRecipentList().getRecipientById(repId);
            		/*	List recList=m.getRecipientsIdList();
            			for(int i=0;i<recList.size();i++){
            				SavedRecipientModel sm=(SavedRecipientModel)recList.get(i);
            				user.getRecipentList().removeOrderLineById(sm.getRandomId());
            			}  */
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

            	//FDCustomerManager.deleteSavedRecipient(request.getParameter("deleteId"));
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
                /*	
                	if(amountStr==null || amountStr.length() < 1) {
                		System.out.println("validateGiftCard() amountStr = " + amountStr);
                        result.addError(new ActionError(EnumUserInfoName.GC_AMOUNT.getCode(), "Invalid or missing Amount"));
                    }else{
                    	try{
                    		amount=Double.parseDouble(amountStr);
                    	}catch(NumberFormatException e){
                    		result.addError(new ActionError(EnumUserInfoName.GC_AMOUNT.getCode(), "Invalid Amount"));
                    	}
                    }
                    */
                	
                   if(result.getErrors().isEmpty()) {                        
                        //FDCustomerManager.storeSavedRecipient(user, srm);
                    	try
    	        		{	        			
//    	        			 GiftCardManagerHome home= getGiftCardManagerHome();        		        		        	
//    		        		 GiftCardManagerSB remote=home.create();
                    		 
                    	     ErpGiftCardModel fromModel=FDCustomerManager.validateAndGetGiftCardBalance(fromGivexNum);                    	    
                    	     EnumGiftCardStatus status= fromModel.getStatus();
                    	     if(EnumGiftCardStatus.INACTIVE==status){
                                result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_FROM.getCode(), "Some issue with Gift certificate either card on hold or cancelled"));
                    	     }
                    	    /* 
                    	     if(fromModel.getBalance()<amount)
                    	     {
                    	    		System.out.println("validateGiftCard() fldYourName = " + fromGivexNum);
                                    result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_FROM.getCode(), "Amount entered is more than the balance available"));
                    	     } */
                    	     
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
            		
            		//HttpSession session = pageContext.getSession();
                    //FDSessionUser fs_user = (FDSessionUser)session.getAttribute(USER);        
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
                    	
                    	FDRecipientList list=new FDRecipientList();
                    	list.addRecipient(model);
                    	user.setRecipientList(list);
                    	UserUtil.initializeGiftCart(user);
                    	
                    	
                    	FDActionInfo actionInfo = new FDActionInfo(EnumTransactionSource.CUSTOMER_REP, user.getIdentity(), "GenerateNewGiftCard", "",IConstants.AGENT);
                    	CustomerRatingI rating = new CustomerRatingAdaptor(new ProfileModel(), false, 10);                    	            			                    	
                    	Collection ccards = FDCustomerManager.getPaymentMethods(user.getIdentity());
            			Iterator iterator= ccards.iterator();
            			boolean working=false;            			
            			user.getGiftCart().setPaymentMethod((ErpPaymentMethodI) ((ccards.toArray())[0]));
            			//Set the default web service type
            			user.getGiftCart().getDeliveryAddress().setWebServiceType(EnumWebServiceType.GIFT_CARD_PERSONAL);

            			FDReservation reservation= UserUtil.getFDReservation(user.getIdentity().getErpCustomerPK(),null);
            			user.getGiftCart().setDeliveryReservation(reservation);
            			
            			List repList = convertSavedToErpRecipienntModel(user.getRecipentList().getRecipents(),user.getIdentity().getErpCustomerPK());	
            			
            		    try {
    						String saleId;
							
								saleId = FDCustomerManager.placeGiftCardOrder(actionInfo, user.getGiftCart(), Collections.EMPTY_SET, false, rating, EnumDlvPassStatus.NONE, repList,false);
							
    						
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
            	    //validateGCBalanceTransfer(request, result);            		                           		                    
                }

            	if ("addBulkSavedRecipient".equalsIgnoreCase(actionName) ) {
            		getBulkFormData(request, result);
                    validateBulkGiftCard(request, result);
                    if(result.getErrors().isEmpty()) {
                    	FDBulkRecipientModel srm = populateBulkSavedRecipient();
                                            	
                    /*	for(int i=0;i<Integer.parseInt(fldQuantity);i++)
            	    	{
                    		SavedRecipientModel sm = populateSavedRecipient();
            	    		srm.addRecipientList(sm);
            	    		user.getRecipentList().addRecipient(sm);
            	    	}
                    */	
                        user.getBulkRecipentList().addRecipient(srm);  
                        
                    } 
            	}            	
            	else if ("editBulkSavedRecipient".equalsIgnoreCase(actionName)) {            		
            		getBulkFormData(request, result);
                    validateBulkGiftCard(request, result);
                    if(result.getErrors().isEmpty()) {
                    	FDBulkRecipientModel srm = populateBulkSavedRecipient();
                    	
                    /*	
                    	for(int i=0;i<Integer.parseInt(fldQuantity);i++)
            	    	{
                    		SavedRecipientModel sm = populateSavedRecipient();
            	    		srm.addRecipientList(sm);
            	    		user.getRecipentList().addRecipient(sm);
            	    	}
                    */	
                    	//srm.setId(request.getParameter("recipId"));
                    	String repId = request.getParameter("recipId");
                		if (repId == null) {
                			result.addError(new ActionError("system", SystemMessageList.MSG_IDENTIFY_RECIPIENT));
                		} else {
	                		int repIndex = -1;
	                		try {
	                			repIndex = user.getBulkRecipentList().getRecipientIndex(Integer.parseInt(repId));
	                			FDBulkRecipientModel m=user.getBulkRecipentList().getRecipientById(repId);
	                		/*	List recList=m.getRecipientsIdList();
	                			for(int i=0;i<recList.size();i++){
	                				SavedRecipientModel sm=(SavedRecipientModel)recList.get(i);
	                				user.getRecipentList().removeOrderLineById(sm.getRandomId());
	                			}  */
	                			
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
                    	//FDCustomerManager.updateSavedRecipient(user, srm);
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
    
    
    private List convertSavedToErpRecipienntModel(List savedList,String customerId)
	{
	 	ListIterator i = savedList.listIterator();
	 	List recList=new ArrayList();
    	while(i.hasNext()) {    		
    		SavedRecipientModel srm = (SavedRecipientModel)i.next();
    		ErpRecipentModel rm=new ErpRecipentModel();
    		rm.setCustomerId(customerId);
    		rm.toModel(srm);
    		recList.add(rm);
         }
    	return recList;
	}
    
    private static DlvZoneInfoModel getZoneInfo(ErpAddressModel address) throws FDResourceException, FDInvalidAddressException {

		DlvZoneInfoModel zInfo =new DlvZoneInfoModel("1","1","1",EnumZipCheckResponses.DELIVER,false);
		return zInfo;
	}
    
    
    private  FDTimeslot getFDTimeSlot() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
    private  FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=getFDTimeSlot();
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION, customerID, addressID, false, false,null,false,null, -1);
		return reservation;

	}
    
    
    /**
     * Retrieves form field data for processing by the tag.
     * @param HttpServletRequest contains the form fields to be retrieved
     */
    private void getFormData(HttpServletRequest request, ActionResult result){
    	
    	fldAmount = request.getParameter("fldAmount");
    	fldAltAmount =  request.getParameter("fldAltAmount");
    	fldYourName =  request.getParameter(EnumUserInfoName.GC_BUYER_NAME.getCode());
    	fldRecipientName =  request.getParameter(EnumUserInfoName.GC_RECIPIENT_NAME.getCode());
    	fldYourEmail =  request.getParameter(EnumUserInfoName.GC_BUYER_EMAIL.getCode());
    	fldRecipientEmail =  request.getParameter(EnumUserInfoName.GC_RECIPIENT_EMAIL.getCode());
    	fldDeliveryMethod =  request.getParameter(EnumUserInfoName.DLV_METHOD.getCode());
    	fldMessage =  request.getParameter("fldMessage");
    	gcTemplateId = request.getParameter("gcTemplateId");
    	fldQuantity = request.getParameter(EnumUserInfoName.GC_QUANTITY.getCode());
    }
    
    
    /**
     * Retrieves form field data for processing by the tag.
     * @param HttpServletRequest contains the form fields to be retrieved
     */
    private void getBulkFormData(HttpServletRequest request, ActionResult result){
    	
    	fldAmount = request.getParameter("fldAmount");
    	fldAltAmount =  request.getParameter("fldAltAmount");
    	fldYourName =  request.getParameter(EnumUserInfoName.GC_BUYER_NAME.getCode());
    	fldRecipientName =  request.getParameter(EnumUserInfoName.GC_BUYER_NAME.getCode());
    	fldYourEmail =  request.getParameter(EnumUserInfoName.GC_BUYER_EMAIL.getCode());
    	fldRecipientEmail =  request.getParameter(EnumUserInfoName.GC_BUYER_EMAIL.getCode());
    	fldDeliveryMethod =  request.getParameter(EnumUserInfoName.DLV_METHOD.getCode());
    	fldMessage =  request.getParameter("fldMessage");
    	gcTemplateId = request.getParameter("gcTemplateId");
    	fldQuantity = request.getParameter(EnumUserInfoName.GC_QUANTITY.getCode());
    }
    
    
    
    
    /**
     * Checks for gift card data validity.
     * @param HttpServletRequest request
     * @param ActionResult result
     */
    private void validateandGCBalanceTransfer(HttpServletRequest request, ActionResult result) throws FDResourceException {
    	
    	
    	String fromGC=request.getParameter("fromGiftcard");
		String toGC=request.getParameter("toGiftCard");
		String amountStr=request.getParameter("amount");
    	
    	if(fromGC==null || fromGC.length() < 1) {
            result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_FROM.getCode(), "Invalid or missing From Account Number"));
        }

    	if(toGC==null || toGC.length() < 1) {
            result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), "Invalid or missing To Account Number"));
        }
		
    	if(amountStr==null || amountStr.length() < 1) {
            result.addError(new ActionError(EnumUserInfoName.GC_AMOUNT.getCode(), "Invalid or missing Amount"));
        }
    	double amount=0;
    	try{
    		 amount=Double.parseDouble(amountStr);
    	}catch(NumberFormatException e){
    		result.addError(new ActionError(EnumUserInfoName.GC_AMOUNT.getCode(), "Invalid or missing Amount"));
    	}
    	
    	 GiftCardManagerHome home= getGiftCardManagerHome();
    	 GiftCardManagerSB remote=null;
    	 try {
			   remote= home.create();			   			   
			try{
				ErpGiftCardModel model=remote.validate(fromGC);
				if(amount>model.getBalance())
				{
					result.addError(new ActionError(EnumUserInfoName.GC_AMOUNT.getCode(), "From Aaccount balance is less than transfer amount entered "));
				}
				
			} catch (InvalidCardException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_FROM.getCode(), e.getMessage()));
				return;
			} catch (CardInUseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), e.getMessage()));
				return;
			}catch(CardOnHoldException e){
				//user.incrementGCRetryCount();
				result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), e.getMessage()));
			}
			
			
			try{
				ErpGiftCardModel model=remote.validate(toGC);
				
			} catch (InvalidCardException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), e.getMessage()));
				return;
			} catch (CardInUseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), e.getMessage()));
				return;
			}catch(CardOnHoldException e){
				//user.incrementGCRetryCount();
				result.addError(new ActionError(EnumUserInfoName.GC_ACCOUNT_TO.getCode(), e.getMessage()));
			}						
			
									
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    

    
    
    /**
     * Checks for gift card data validity.
     * @param HttpServletRequest request
     * @param ActionResult result
     */
    private void validateBulkGiftCard(HttpServletRequest request, ActionResult result) throws FDResourceException {
    	if(fldYourName==null || fldYourName.length() < 1) {
            result.addError(new ActionError(EnumUserInfoName.GC_BUYER_NAME.getCode(), "Invalid or missing name"));
        }
    	
    	if(fldYourEmail==null || fldYourEmail.length() < 1) {
            result.addError(new ActionError(EnumUserInfoName.GC_BUYER_EMAIL.getCode(), "Invalid or missing email"));
        }else if(!EmailUtil.isValidEmailAddress(fldYourEmail)){
    		result.addError(new ActionError(EnumUserInfoName.GC_BUYER_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
    	}
    	
    	if((fldAmount==null || fldAmount.length() < 1) && ( fldAltAmount==null || fldAltAmount.length() < 1)) {
            result.addError(new ActionError("amount", "Invalid or missing amount"));
        }  

    	if((fldQuantity==null || fldQuantity.length() < 1) && ( fldQuantity==null || fldQuantity.length() < 1)) {
            result.addError(new ActionError(EnumUserInfoName.GC_QUANTITY.getCode(), "Invalid or missing quantity"));
        }else{
        	try {
				Integer qty = Integer.parseInt(fldQuantity);
			} catch (NumberFormatException e) {
				result.addError(new ActionError(EnumUserInfoName.GC_QUANTITY.getCode(), "Invalid or missing quantity"));
			}
        }
    	if((fldAmount==null || "".equals(fldAmount)) && ( fldAltAmount==null || "".equals(fldAltAmount))) {
            result.addError(new ActionError("fldAmount", "Invalid or missing amount"));
        }else {
        	if(fldAltAmount != null && fldAltAmount.length() > 0) {
        		try {
					double amount = Double.parseDouble(fldAltAmount);
					if(amount < FDStoreProperties.getGiftCardMinAmount()){
						result.addError(new ActionError("gc_amount_minimum", formatGCMinMaxMsg(SystemMessageList.MSG_GC_MIN_AMOUNT, FDStoreProperties.getGiftCardMinAmount())));
					}
					if(amount > FDStoreProperties.getGiftCardMaxAmount()){
						result.addError(new ActionError("gc_amount_maximum", formatGCMinMaxMsg(SystemMessageList.MSG_GC_MAX_AMOUNT, FDStoreProperties.getGiftCardMaxAmount())));
					}
				}catch (NumberFormatException e) {
					  result.addError(new ActionError("fldAmount", "Invalid or missing amount"));
				}

        	}
        }
    	
    }
    
	private String formatGCMinMaxMsg(String pattern, double amount) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String formattedAmt = FormatterUtil.formatToGrouping(amount);
		return MessageFormat.format(
			pattern,
			new Object[] {formattedAmt, UserUtil.getCustomerServiceContact(request)});
	}
    
    
    
    /**
     * Adds new recipient to SavedRecipientModel
     */
    private SavedRecipientModel populateSavedRecipient() throws FDResourceException {
    	SavedRecipientModel srm = new SavedRecipientModel();
    	srm.setRecipientEmail(fldRecipientEmail);
    	srm.setSenderEmail(fldYourEmail);
    	srm.setDeliveryMode(EnumGCDeliveryMode.getEnum(fldDeliveryMethod));
    	srm.setPersonalMessage(fldMessage);
    	srm.setFdUserId(user.getPrimaryKey());
    	srm.setSenderName(fldYourName);
    	srm.setRecipientName(fldRecipientName);
    	srm.setTemplateId(gcTemplateId);
    	if(fldAmount != null && fldAmount.length() > 1 && !fldAmount.equalsIgnoreCase("other")) {
    		srm.setAmount(Double.parseDouble(fldAmount));
    	} else if(fldAltAmount != null && fldAltAmount.length() > 1) {
    		srm.setAmount(Double.parseDouble(fldAltAmount));
    	}
    	return srm;
    }
    
    /**
     * Adds new recipient to SavedRecipientModel
     */
    private FDBulkRecipientModel populateBulkSavedRecipient() throws FDResourceException {
    		FDBulkRecipientModel srm=new FDBulkRecipientModel(); 
    	    srm.setRecipientEmail(fldRecipientEmail);
	    	srm.setSenderEmail(fldYourEmail);
	    	srm.setDeliveryMode(EnumGCDeliveryMode.PDF);
	    	srm.setPersonalMessage(fldMessage);
	    	srm.setFdUserId(user.getPrimaryKey());
	    	srm.setSenderName(fldYourName);
	    	srm.setRecipientName(fldRecipientName);
	    	srm.setQuantity(fldQuantity);
	    	srm.setTemplateId(gcTemplateId);
	    	if(fldAmount != null && fldAmount.length() > 1 && !fldAmount.equalsIgnoreCase("other")) {
	    		srm.setAmount(Double.parseDouble(fldAmount));
	    	} else if(fldAltAmount != null && fldAltAmount.length() > 1) {
	    		srm.setAmount(Double.parseDouble(fldAltAmount));
	    	}
	    	return srm;		    	    
    }
    
    
	private static GiftCardManagerHome getGiftCardManagerHome() {
		try {
			return (GiftCardManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.GiftCardManager", GiftCardManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
}
		
