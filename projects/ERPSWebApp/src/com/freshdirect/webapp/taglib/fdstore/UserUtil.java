package com.freshdirect.webapp.taglib.fdstore;

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

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.framework.core.PrimaryKey;

public class UserUtil {

	public static void createSessionUser(HttpServletRequest request, HttpServletResponse response, FDUser loginUser)
		throws FDResourceException {
		HttpSession session = request.getSession();

		FDSessionUser sessionUser = new FDSessionUser(loginUser, session);
		sessionUser.isLoggedIn(true);

		CookieMonster.storeCookie(sessionUser, response);
		sessionUser.updateUserState();

		session.setAttribute(SessionName.USER, sessionUser);
	}
	
	public static String getCustomerServiceContact(HttpServletRequest request) {
		FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
		if (user==null) {
			return SystemMessageList.CUSTOMER_SERVICE_CONTACT;
		}
		return user.getCustomerServiceContact();
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
		
			List recipList = user.getRecipentList().getRecipents(); //FDCustomerManager.loadSavedRecipients(user.getUser());			
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
		                		  productmodel, new FDConfiguration(quantity, salesUnit.getName(), optionMap),null, user.getPricingZoneId());
		
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
		    List recipList= bulkRecList.getFDRecipentsList().getRecipents();
		    if(!recipList.isEmpty()) {
		    	ListIterator i = recipList.listIterator();
		    	while(i.hasNext()) {            		
		    		SavedRecipientModel srm = (SavedRecipientModel)i.next();
		    		
			            FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo), 
		                		  productmodel, new FDConfiguration(quantity, salesUnit.getName(), optionMap),null, user.getPricingZoneId());
		
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
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION, customerID, addressID, false, false,null,false,null, -1);
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
	
	public static DlvZoneInfoModel getZoneInfo(ErpAddressModel address) throws FDResourceException, FDInvalidAddressException {

		DlvZoneInfoModel zInfo =new DlvZoneInfoModel("1","1","1",EnumZipCheckResponses.DELIVER,false,false);
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
          		  productmodel, new FDConfiguration(totalQuantity, salesUnit.getName(), optionMap),null, user.getPricingZoneId());

		    cartLine.setFixedPrice(productInfo.getZonePriceInfo(user.getPricingZoneId()).getDefaultPrice()*totalQuantity);//TODO: check whether it gives the required price or not.

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
}
