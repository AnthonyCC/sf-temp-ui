package com.freshdirect.webapp.taglib.fdstore;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;

public class UserUtil {
	
	private static Category LOGGER = LoggerFactory.getInstance( UserUtil.class );
	
	public static void createSessionUser(HttpServletRequest request, HttpServletResponse response, FDUser loginUser)
		throws FDResourceException {
		HttpSession session = request.getSession();

		FDSessionUser sessionUser = new FDSessionUser(loginUser, session);
		sessionUser.isLoggedIn(true);

		if (loginUser.getMasqueradeContext() != null)
			CookieMonster.clearCookie(response);
		else
			CookieMonster.storeCookie(sessionUser, response);
		sessionUser.updateUserState();

		session.setAttribute(SessionName.USER, sessionUser);

		FDCustomerCouponUtil.initCustomerCoupons(session);
	}
	
	public static String getCustomerServiceContact(FDUserI user) {
		if (user==null) {
			return SystemMessageList.CUSTOMER_SERVICE_CONTACT;
		}
		return user.getCustomerServiceContact();
	}

	public static String getCustomerServiceContact(HttpServletRequest request) {
		FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
		return getCustomerServiceContact(user);
	}
	
	public static void initializeGiftCart(FDUserI user){
		try {
			FDCartModel cart = user.getGiftCart();
			FDReservation reservation= getFDReservation(user.getUserId(),null);
			cart.setDeliveryReservation(reservation);
			//Currently defaults to Personal gift card order type. This info should come from UI.
			/*
			EnumServiceType sType = EnumServiceType.GIFT_CARD_PERSONAL;
			if(user.getSelectedServiceType() != null && EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())) {
				sType = user.getSelectedServiceType();
			}
			*/
			cart.setDeliveryAddress(getGiftCardDeliveryAddress(EnumServiceType.WEB));
			cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));
			ArrayList lines = new ArrayList();
			ContentFactory contentFactory = ContentFactory.getInstance();
			int n = 0;
		
			List recipList = user.getRecipientList().getRecipients(user.getGiftCardType()); //FDCustomerManager.loadSavedRecipients(user.getUser());			
			int quantity = 1;
			Map optionMap = new HashMap();

			FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getGiftcardSkucode());
		
		    FDProduct product = FDCachedFactory.getProduct(productInfo);
		
		    ProductModel productmodel = contentFactory.getProduct(FDStoreProperties.getGiftcardSkucode());
		
		    FDSalesUnit[] units = product.getSalesUnits();
		
		    FDSalesUnit salesUnit = units[n % units.length];
		    
		    if(!recipList.isEmpty()) {
		    	ListIterator i = recipList.listIterator();
		    	while(i.hasNext()) {            		
		    		SavedRecipientModel srm = (SavedRecipientModel)i.next();
		    		
			            FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo), 
		                		  productmodel, new FDConfiguration(quantity, salesUnit.getName(), optionMap),null, user.getUserContext());
		
		            cartLine.setFixedPrice(srm.getAmount());
		            cartLine.refreshConfiguration();                
		
		            lines.add(cartLine);		            
		    	}
		    	
		        cart.setOrderLines(lines);
		        //cart.setRecipentList(recipList);
		        user.setGiftCart(cart);
		        if(user.getIdentity() != null) {		        
		        	FDCustomerCreditUtil.applyCustomerCredit(user.getGiftCart(), user.getIdentity());
		        }
			}
		}catch(Exception e){
			throw new FDRuntimeException(e);
		}

	}
	
	public static void initializeBulkGiftCart(FDUserI user){
		try {
			
	
			FDCartModel cart = user.getGiftCart();
			FDReservation reservation= getFDReservation(user.getUserId(),null);
			cart.setDeliveryReservation(reservation);
			//Currently defaults to Personal gift card order type. This info should come from UI.
			//EnumServiceType sType = EnumServiceType.GIFT_CARD_PERSONAL;
			cart.setDeliveryAddress(getGiftCardDeliveryAddress(EnumServiceType.WEB));
			cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));
			ArrayList lines = new ArrayList();
			ContentFactory contentFactory = ContentFactory.getInstance();
			int n = 0;
		
			
			FDBulkRecipientList bulkRecList = user.getBulkRecipentList(); //FDCustomerManager.loadSavedRecipients(user.getUser());
			if(bulkRecList==null || bulkRecList.size()==0) return;			
			
			bulkRecList.constructFDRecipientsList();
			//FDRecipientList oldRecList=new FDRecipientList();
			//oldRecList.addRecipients(recipList);
			//user.setRecipientList(oldRecList);
			
									
			int quantity = 1;
			Map optionMap = new HashMap();

			FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getGiftcardSkucode());
		
		    FDProduct product = FDCachedFactory.getProduct(productInfo);
		
		    ProductModel productmodel = contentFactory.getProduct(FDStoreProperties.getGiftcardSkucode());
		
		    FDSalesUnit[] units = product.getSalesUnits();
		
		    FDSalesUnit salesUnit = units[n % units.length];
		    List recipList= bulkRecList.getFDRecipentsList().getRecipients();
		    if(!recipList.isEmpty()) {
		    	ListIterator i = recipList.listIterator();
		    	while(i.hasNext()) {            		
		    		SavedRecipientModel srm = (SavedRecipientModel)i.next();
		    		
			            FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo), 
		                		  productmodel, new FDConfiguration(quantity, salesUnit.getName(), optionMap),null, user.getUserContext());
		
		            cartLine.setFixedPrice(srm.getAmount());
		            cartLine.refreshConfiguration();                
		
		            lines.add(cartLine);		            
		    	}
		    	
		        cart.setOrderLines(lines);
		        //cart.setRecipentList(recipList);
		        user.setGiftCart(cart);
		        if(user.getIdentity() != null) {
		        	FDCustomerCreditUtil.applyCustomerCredit(user.getGiftCart(), user.getIdentity());
		        }
		        
			}
		}catch(Exception e){
			throw new FDRuntimeException(e);
		}

	}
	
	
	public static FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=getFDTimeSlot();
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION, 
				customerID, addressID, false,null,-1,null, false, null);
		return reservation;

	}

	private static FDTimeslot getFDTimeSlot() {
		// TODO Auto-generated method stub		
		return null;
	}

	private static ErpAddressModel getGiftCardDeliveryAddress(EnumServiceType sType){
		ErpAddressModel address=new ErpAddressModel();
		address.setAddress1("23-30 borden ave");
		address.setCity("Long Island City");
		address.setState("NY");
		address.setCountry("US");
		address.setZipCode("11101");
		address.setServiceType(sType);
		return address;
		
	}

	private static ErpAddressModel getDonationOrderDeliveryAddress(EnumServiceType sType){
		ErpAddressModel address=new ErpAddressModel();
		address.setAddress1("23-30 borden ave");
		address.setCity("Long Island City");
		address.setState("NY");
		address.setCountry("US");
		address.setZipCode("11101");
		address.setServiceType(sType);
		address.setCharityName("ROBIN HOOD");
		return address;
		
	}
	
	public static FDDeliveryZoneInfo getZoneInfo(ErpAddressModel address) throws FDResourceException, FDInvalidAddressException {

		FDDeliveryZoneInfo zInfo =new FDDeliveryZoneInfo("1","1","1", EnumZipCheckResponses.DELIVER);
		return zInfo;
	}

	public static void initializeCartForDonationOrder(FDUserI user){
		try {
			FDCartModel cart = user.getDonationCart();
			FDReservation reservation= getFDReservation(user.getUserId(),null);
			cart.setDeliveryReservation(reservation);
			//Currently defaults to Corporate Donation order type. This info should come from UI.
			//EnumServiceType sType = EnumServiceType.DONATION_BUSINESS;
			/*if(user.getSelectedServiceType() != null && EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())) {
				sType = user.getSelectedServiceType();
			}*/
			cart.setDeliveryAddress(getDonationOrderDeliveryAddress(EnumServiceType.WEB));
			cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));
			ArrayList lines = new ArrayList();
			ContentFactory contentFactory = ContentFactory.getInstance();
			int n = 0;

			Integer totalQuantity = user.getDonationTotalQuantity();
			int quantity = 1;
			Map optionMap = new HashMap();

			FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getRobinHoodSkucode());
		
		    FDProduct product = FDCachedFactory.getProduct(productInfo);
		
		    ProductModel productmodel = contentFactory.getProduct(FDStoreProperties.getRobinHoodSkucode());
		
		    FDSalesUnit[] units = product.getSalesUnits();
		
		    FDSalesUnit salesUnit = units[n % units.length];
		    
		    FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo), 
          		  productmodel, new FDConfiguration(totalQuantity, salesUnit.getName(), optionMap),null, user.getUserContext());

		    cartLine.setFixedPrice(productInfo.getZonePriceInfo(user.getUserContext().getPricingContext().getZoneInfo()).getDefaultPrice()*totalQuantity);//TODO: check whether it gives the required price or not.

		    cartLine.refreshConfiguration();                
		
		    lines.add(cartLine);
		    /*for(int i=0; i< totalQuantity; i++){        		
		            FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo), 
		                		  productmodel.getProductRef(), new FDConfiguration(quantity, salesUnit.getName(), optionMap),null);		
		            cartLine.setFixedPrice(productInfo.getDefaultPrice());//TODO: check whether it gives the required price or not.
		            cartLine.refreshConfiguration();                
		
		            lines.add(cartLine);		            
		    }*/		    	
	        cart.setOrderLines(lines);
	        user.setDonationCart(cart);
	        if(user.getIdentity() != null) {		        
	        	FDCustomerCreditUtil.applyCustomerCredit(user.getGiftCart(), user.getIdentity());
	        }
			
		}catch(Exception e){
			throw new FDRuntimeException(e);
		}

	}
	
	public static double getRobinHoodAvailability(Date currentDate, FDProductInfo prodInfo) {
		
		double quantity = 0.0;
		ErpInventoryEntryModel inventoryMatch = null;
		Date lastInventoryDate = null;
		
		if(!FDStoreProperties.getPreviewMode()){
			if(prodInfo != null && prodInfo.getInventory() != null && prodInfo.getInventory().getEntries() != null 
						&& !prodInfo.getInventory().getEntries().isEmpty()) {
						
				for (Iterator i = prodInfo.getInventory().getEntries().iterator(); i.hasNext();) {
					ErpInventoryEntryModel e = (ErpInventoryEntryModel) i.next();
	//				System.out.println("getRobinHoodAvailability >>"+e.getStartDate()+"->"+currentDate+"->"+e.getQuantity());
					
					if (null!= e.getStartDate() && !e.getStartDate().after(currentDate)) {		
						if(lastInventoryDate == null || e.getStartDate().after(lastInventoryDate)) {
							inventoryMatch = e;
							lastInventoryDate = e.getStartDate();
						}
					}				
				}
			}
			if(inventoryMatch != null) {
				quantity = inventoryMatch.getQuantity();
			}
		}else{
			quantity=15000;//Fixed value for preview mode, to test Robin Hood in preview mode.
		}
		return quantity;
	}
	
	public static String loginUser(HttpSession session, HttpServletRequest request, HttpServletResponse response
										, ActionResult actionResult, String userId, String password
										, String mergePage, String successPage) {
		
		String updatedSuccessPage = successPage;
		
		//hard-code redirects and allow logic to override later
		if (updatedSuccessPage.indexOf("/registration/signup.jsp")!=-1 || updatedSuccessPage.indexOf("/checkout/signup_ckt.jsp")!=-1) {
			//hard-code redirects (be sure to set both vars)
			successPage = "/index.jsp";
			updatedSuccessPage = "/index.jsp";
		}
		
		if (userId == null ||  userId.length() < 1 ) {
            actionResult.addError(new ActionError(EnumUserInfoName.USER_ID.getCode(), SystemMessageList.MSG_REQUIRED));
        } else if (!EmailUtil.isValidEmailAddress(userId)) {
            actionResult.addError(new ActionError(EnumUserInfoName.EMAIL_FORMAT.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
        }
        
        if (password==null || password.length() < 1) {
            actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_REQUIRED));
        } else if (password.length() < 4) {
            actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_PASSWORD_TOO_SHORT));
        }
        
        if (!actionResult.isSuccess()) {        	
            return updatedSuccessPage;
        }
        
        try {
        	FDIdentity identity = FDCustomerManager.login(userId,password);
            LOGGER.info("Identity : erpId = " + identity.getErpCustomerPK() + " : fdId = " + identity.getFDCustomerPK());
            
            FDUser loginUser = FDCustomerManager.recognize(identity);           
            
            LOGGER.info("FDUser : erpId = " + loginUser.getIdentity().getErpCustomerPK() + " : " + loginUser.getIdentity().getFDCustomerPK());
                        
            FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
            
            if(session.getAttribute("TICK_TIE_CUSTOMER") != null) {
            	session.removeAttribute(SessionName.USER);
            	currentUser = null;
            }
                        
            LOGGER.info("loginUser is " + loginUser.getFirstName() + " Level = " + loginUser.getLevel());
            LOGGER.info("currentUser is " + (currentUser==null?"null":currentUser.getFirstName()+currentUser.getLevel()));
            String currentUserId=null;
            if (currentUser == null) {
                // this is the case right after signout
                UserUtil.createSessionUser(request, response, loginUser);
                
            } else if (!loginUser.getCookie().equals(currentUser.getCookie())) {
                // current user is different from user who just logged in
                int currentLines = currentUser.getShoppingCart().numberOfOrderLines();
                int loginLines = loginUser.getShoppingCart().numberOfOrderLines();
                
                //address needs to be set using logged in user's information - in case existing cart is used or cart merge
                currentUser.getShoppingCart().setDeliveryAddress(loginUser.getShoppingCart().getDeliveryAddress());
                
                if ((currentLines > 0) && (loginLines > 0)) {
                    // keep the current cart in the session and send them to the merge cart page
                    if(successPage != null && !successPage.contains("/robin_hood") && !successPage.contains("/gift_card")
                    		&& mergePage != null && mergePage.trim().length() > 0){
	                    session.setAttribute(SessionName.CURRENT_CART, currentUser.getShoppingCart());
	                    updatedSuccessPage =  mergePage + "?successPage=" + URLEncoder.encode( successPage ) ;
                    }
                    
                } else if ((currentLines > 0) && (loginLines == 0)) {
                    // keep current cart                	
                    loginUser.setShoppingCart(currentUser.getShoppingCart());
                    loginUser.getShoppingCart().setUserContextToOrderLines(loginUser.getUserContext());                                     
                    
                }
                
                // merge coupons
                currentUserId= currentUser.getPrimaryKey();
                
                // current user has gift card recipients that need to be added to the login user's recipients list
                if(currentUser.getLevel()==FDUserI.GUEST &&  currentUser.getRecipientList().getRecipients().size() > 0) {
                	List<RecipientModel> tempList = currentUser.getRecipientList().getRecipients();
                	ListIterator<RecipientModel> iterator = tempList.listIterator();
                	//add currentUser's list to login user
                	while(iterator.hasNext()) {
                		SavedRecipientModel srm = (SavedRecipientModel)iterator.next();
                		// reset the FDUserId to the login user
                		srm.setFdUserId(loginUser.getUserId());
                		loginUser.getRecipientList().removeRecipients(EnumGiftCardType.DONATION_GIFTCARD);
                		loginUser.getRecipientList().addRecipient(srm);
                	}                	
                }
                
                loginUser.setGiftCardType(currentUser.getGiftCardType());
                
                if(currentUser.getDonationTotalQuantity()>0){
                	loginUser.setDonationTotalQuantity(currentUser.getDonationTotalQuantity());
                }
                UserUtil.createSessionUser(request, response, loginUser);
                //The previous recommendations of the current session need to be removed.
                session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
                session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
                session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);
                
            } else {
                // the logged in user was the same as the current user,
                // that means that they were previously recognized by their cookie before log in
                // just set their login status and move on
                currentUser.isLoggedIn(true);
                session.setAttribute(SessionName.USER, currentUser);
            }
//          loginUser.setEbtAccepted(loginUser.isEbtAccepted()&&(loginUser.getOrderHistory().getUnSettledEBTOrderCount()<=0));  
          FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
          if(user != null) {
              user.setEbtAccepted(user.isEbtAccepted()&&(user.getOrderHistory().getUnSettledEBTOrderCount()<1)&&!user.hasEBTAlert());
              FDCustomerCouponUtil.initCustomerCoupons(session,currentUserId);
          }
          
          
          if (user!=null && EnumServiceType.CORPORATE.equals(user.getUserServiceType())) {
        	  if(request.getRequestURI().indexOf("index.jsp")!=-1 || (successPage != null && successPage.indexOf("/login/index.jsp")!=-1)){        	  
        		  updatedSuccessPage = "/department.jsp?deptId=COS";
        	  }
          }
          
          //tick and tie for refer a friend program
          if(session.getAttribute("TICK_TIE_CUSTOMER") != null) {
        	  String ticktie = (String) session.getAttribute("TICK_TIE_CUSTOMER");
        	  String custID = ticktie.substring(0, ticktie.indexOf("|"));
        	  String refName = ticktie.substring(ticktie.indexOf("|"));
        	  if(custID.equals(identity.getErpCustomerPK())) {
        		  //the session is for this user only
        		  String referralCustomerId = FDCustomerManager.recordReferral(custID, (String) session.getAttribute("REFERRALNAME"), user.getUserId());
        		  LOGGER.debug("Tick and tie:" + user.getUserId() + " with:" + referralCustomerId);
        		  user.setReferralCustomerId(referralCustomerId);
        		  user.setReferralPromoList();
        		  session.setAttribute(SessionName.USER, user);
        	  }
        	  session.removeAttribute("TICK_TIE_CUSTOMER");
          }
          
          if(user != null) {
        	  user.setJustLoggedIn(true);
          }

          CmRegistrationTag.setPendingLoginEvent(session);
          
        } catch (FDResourceException fdre) {
            LOGGER.warn("Resource error during authentication", fdre);
            actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
            
        } catch (FDAuthenticationException fdae) {
        	if("Account disabled".equals(fdae.getMessage())) {
        		actionResult.addError(new ActionError("authentication", 
	            		MessageFormat.format(SystemMessageList.MSG_DEACTIVATED, 
	            		new Object[] { UserUtil.getCustomerServiceContact(request)})));
        	} else {
        		actionResult.addError(new ActionError("authentication", 
	            		MessageFormat.format(SystemMessageList.MSG_AUTHENTICATION, 
	            		new Object[] { UserUtil.getCustomerServiceContact(request)})));
        	}
        }
        return updatedSuccessPage;		
	}

	/* Login Social User added for social login. Login by only userid */
	
	public static String loginSocialUser(HttpSession session,
			HttpServletRequest request, HttpServletResponse response,
			ActionResult actionResult, String userId,
			String mergePage, String successPage) {

		String updatedSuccessPage = successPage;

		// hard-code redirects and allow logic to override later
		if (updatedSuccessPage.indexOf("/registration/signup.jsp") != -1
				|| updatedSuccessPage.indexOf("/checkout/signup_ckt.jsp") != -1) {
			// hard-code redirects (be sure to set both vars)
			successPage = "/index.jsp";
			updatedSuccessPage = "/index.jsp";
		}

		

		try {
			FDIdentity identity = FDCustomerManager.login(userId);
			LOGGER.info("Identity : erpId = " + identity.getErpCustomerPK()
					+ " : fdId = " + identity.getFDCustomerPK());

			FDUser loginUser = FDCustomerManager.recognize(identity);

			LOGGER.info("FDUser : erpId = "
					+ loginUser.getIdentity().getErpCustomerPK() + " : "
					+ loginUser.getIdentity().getFDCustomerPK());

			FDSessionUser currentUser = (FDSessionUser) session
					.getAttribute(SessionName.USER);

			if (session.getAttribute("TICK_TIE_CUSTOMER") != null) {
				session.removeAttribute(SessionName.USER);
				currentUser = null;
			}

			LOGGER.info("loginUser is " + loginUser.getFirstName()
					+ " Level = " + loginUser.getLevel());
			LOGGER.info("currentUser is "
					+ (currentUser == null ? "null" : currentUser
							.getFirstName() + currentUser.getLevel()));
			String currentUserId = null;
			if (currentUser == null) {
				// this is the case right after signout
				UserUtil.createSessionUser(request, response, loginUser);

			} else if (!loginUser.getCookie().equals(currentUser.getCookie())) {
				// current user is different from user who just logged in
				int currentLines = currentUser.getShoppingCart()
						.numberOfOrderLines();
				int loginLines = loginUser.getShoppingCart()
						.numberOfOrderLines();

				// address needs to be set using logged in user's information -
				// in case existing cart is used or cart merge
				currentUser.getShoppingCart().setDeliveryAddress(
						loginUser.getShoppingCart().getDeliveryAddress());

				if ((currentLines > 0) && (loginLines > 0)) {
					// keep the current cart in the session and send them to the
					// merge cart page
					if (successPage != null
							&& !successPage.contains("/robin_hood")
							&& !successPage.contains("/gift_card")
							&& mergePage != null
							&& mergePage.trim().length() > 0) {
						session.setAttribute(SessionName.CURRENT_CART,
								currentUser.getShoppingCart());
						updatedSuccessPage = mergePage + "?successPage="
								+ URLEncoder.encode(successPage);
					}

				} else if ((currentLines > 0) && (loginLines == 0)) {
					// keep current cart
					loginUser.setShoppingCart(currentUser.getShoppingCart());
                    loginUser.getShoppingCart().setUserContextToOrderLines(loginUser.getUserContext());  

				}

				// merge coupons
				currentUserId = currentUser.getPrimaryKey();

				// current user has gift card recipients that need to be added
				// to the login user's recipients list
				if (currentUser.getLevel() == FDUserI.GUEST
						&& currentUser.getRecipientList().getRecipients()
								.size() > 0) {
					List<RecipientModel> tempList = currentUser
							.getRecipientList().getRecipients();
					ListIterator<RecipientModel> iterator = tempList
							.listIterator();
					// add currentUser's list to login user
					while (iterator.hasNext()) {
						SavedRecipientModel srm = (SavedRecipientModel) iterator
								.next();
						// reset the FDUserId to the login user
						srm.setFdUserId(loginUser.getUserId());
						loginUser.getRecipientList().removeRecipients(
								EnumGiftCardType.DONATION_GIFTCARD);
						loginUser.getRecipientList().addRecipient(srm);
					}
				}

				loginUser.setGiftCardType(currentUser.getGiftCardType());

				if (currentUser.getDonationTotalQuantity() > 0) {
					loginUser.setDonationTotalQuantity(currentUser
							.getDonationTotalQuantity());
				}
				UserUtil.createSessionUser(request, response, loginUser);
				// The previous recommendations of the current session need to
				// be removed.
				session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
				session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
				session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);

			} else {
				// the logged in user was the same as the current user,
				// that means that they were previously recognized by their
				// cookie before log in
				// just set their login status and move on
				currentUser.isLoggedIn(true);
				session.setAttribute(SessionName.USER, currentUser);
			}
			// loginUser.setEbtAccepted(loginUser.isEbtAccepted()&&(loginUser.getOrderHistory().getUnSettledEBTOrderCount()<=0));
			FDSessionUser user = (FDSessionUser) session
					.getAttribute(SessionName.USER);
			if (user != null) {
				user.setEbtAccepted(user.isEbtAccepted()
						&& (user.getOrderHistory().getUnSettledEBTOrderCount() < 1)
						&& !user.hasEBTAlert());
				FDCustomerCouponUtil
						.initCustomerCoupons(session, currentUserId);
			}

			if (user != null
					&& EnumServiceType.CORPORATE.equals(user
							.getUserServiceType())) {
				if (request.getRequestURI().indexOf("index.jsp") != -1
						|| (successPage != null && successPage
								.indexOf("/login/index.jsp") != -1)) {
					updatedSuccessPage = "/department.jsp?deptId=COS";
				}
			}

			// tick and tie for refer a friend program
			if (session.getAttribute("TICK_TIE_CUSTOMER") != null) {
				String ticktie = (String) session
						.getAttribute("TICK_TIE_CUSTOMER");
				String custID = ticktie.substring(0, ticktie.indexOf("|"));
				String refName = ticktie.substring(ticktie.indexOf("|"));
				if (custID.equals(identity.getErpCustomerPK())) {
					// the session is for this user only
					String referralCustomerId = FDCustomerManager
							.recordReferral(custID, (String) session
									.getAttribute("REFERRALNAME"), user
									.getUserId());
					LOGGER.debug("Tick and tie:" + user.getUserId() + " with:"
							+ referralCustomerId);
					user.setReferralCustomerId(referralCustomerId);
					user.setReferralPromoList();
					session.setAttribute(SessionName.USER, user);
				}
				session.removeAttribute("TICK_TIE_CUSTOMER");
			}

			if (user != null) {
				user.setJustLoggedIn(true);
			}

			CmRegistrationTag.setPendingLoginEvent(session);

		} catch (FDResourceException fdre) {
			LOGGER.warn("Resource error during authentication", fdre);
			
		} catch (FDAuthenticationException fdae) {
			LOGGER.error(fdae.getMessage());
		}
		return updatedSuccessPage;
	}

}
