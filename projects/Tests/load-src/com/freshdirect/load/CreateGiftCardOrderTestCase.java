
/*
 * CreateOrderTestCase.java
 * JUnit based test
 *
 * Created on May 15, 2002, 8:27 PM
 */

package com.freshdirect.load;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.dataloader.autoorder.create.util.IConstants;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.ejb.GiftCardManagerHome;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

/**
 *
 * @author mrose
 */
public class CreateGiftCardOrderTestCase extends TestCase {

	public CreateGiftCardOrderTestCase(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		//junit.textui.TestRunner.run(suite());
		junit.textui.TestRunner.run(CreateGiftCardOrderTestCase.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CreateGiftCardOrderTestCase.class);
		return suite;
	}

	private static List skus = new ArrayList();

	private static List identities = new ArrayList();

	private static Object syncOnMe = new Object();
	
	private static GiftCardManagerHome managerHome=null;

	protected void setUp() {
		synchronized (syncOnMe) {
			// get skus
			if (skus.size() == 0) {
				synchronized (skus) {
					if (skus.size() == 0) {
						skus.addAll(LoadUtil.getRandomSKUList());
					}
				}
			}
			// get identities
			if (identities.size() == 0) {
				synchronized (identities) {
					if (identities.size() == 0) {
						identities.addAll(LoadUtil.getRandomCustomerList());
					}
				}
			}
			//LoadUtil.doStoreWarmup();
		}
	}

	public void testCreateOrder() {

		// pick an identity
		//FDIdentity identity = (FDIdentity) identities.get((int)(Math.random() * identities.size()));
		//System.out.println("order for erp_cust_id = " + identity.getErpCustomerPK());

		FDIdentity identity = new FDIdentity("145527951", "145527953");
		// how many orderlines per order?
		int lineCount = 30 + (int) (Math.random() * 20.0);

		FDCartModel cart = makeOrder(identity, 1);

		if (cart == null)
			return;

		try {
			// wait a few seconds between reserving a timeslot and committing the order to better emulate user behavior
			Thread.sleep(1000 * (3 + (int) (Math.random() * 5.0)));
		} catch (InterruptedException ie) {
			// who cares?
		}
		CustomerRatingI rating = new CustomerRatingAdaptor(new ProfileModel(), false, 10);
		ArrayList aList=new ArrayList();
		List bList=new ArrayList();
		try {
			FDActionInfo actionInfo = new FDActionInfo(EnumTransactionSource.SYSTEM, identity, "CreateOrderTestCase", "",IConstants.AGENT);
			
			
			ErpRecipentModel model=new ErpRecipentModel();
			model.setAmount(50);
			model.setCustomerId("145527951");
			model.setDeliveryMode(EnumGCDeliveryMode.EMAIL);
			model.setPersonalMessage("jasdasjbdasbdasbdasbdaskd");
			model.setRecipientEmail("gkalsanka@freshdirect.com");
			model.setRecipientName("gopalkrishna");
			model.setSenderName("shankar");
			model.setSenderEmail("smenon@freshdirect.com");
			model.setTemplateId("11293123");		
								
			
			SavedRecipientModel model1=new SavedRecipientModel();
			model1.setAmount(50);
			model1.setFdUserId("145527951");
			model1.setDeliveryMode(EnumGCDeliveryMode.EMAIL);
			model1.setPersonalMessage("jasdasjbdasbdasbdasbdaskd");
			model1.setRecipientEmail("gkalsankaresend@freshdirect.com");
			model1.setRecipientName("gopalkrishnaResent");
			model1.setSenderName("shankar");
			model1.setSenderEmail("smenon@freshdirect.com");
			model1.setTemplateId("11293123");		
            //model1.set("000100");
			
			aList.add(model);
			bList.add(model1);
			System.out.println("***************************trying to place giftcard order*************************************");
			FDUser user=new FDUser(new PrimaryKey("146071189"));
			user.setShoppingCart(cart);
			FDRecipientList list=new FDRecipientList(bList);
			user.setRecipientList(list);
			UserUtil.initializeGiftCart(user);	
			List repList = convertSavedToErpRecipienntModel(user.getRecipentList().getRecipents());			
			String saleId=FDCustomerManager.placeGiftCardOrder(actionInfo,cart,Collections.EMPTY_SET, false,rating,EnumDlvPassStatus.NONE,repList);
			
		} catch (FDResourceException fdre) {
			fdre.printStackTrace();
		} catch (ErpFraudException efe) {
			efe.printStackTrace();
		} catch (DeliveryPassException eae) {
			eae.printStackTrace();
		} 
			
			/*	
			try {
				Thread.sleep(40*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
				
			lookupManagerHome();
			try {
				GiftCardManagerSB gcservice=managerHome.create();
				List recList=gcservice.getGiftCardRecepientsForOrder(saleId);
				for(int i=0;i<recList.size();i++){
					ErpGCDlvInformationHolder holder=(ErpGCDlvInformationHolder)recList.get(i);	
					ErpRecipentModel model2=holder.getRecepientModel();
					model2.setPersonalMessage("trying to resnd this crap");
					model2.setRecipientEmail("gkalsankaresend101@freshdirect.com");
					model2.setRecipientName("gopalkrishnaResent101");
				    model2.setOrderLineId("000100");
				}
				
				gcservice.resendGiftCard(saleId, recList, EnumTransactionSource.WEBSITE);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		} catch (FDResourceException fdre) {
			fdre.printStackTrace();
		} catch (ErpFraudException efe) {
			efe.printStackTrace();
		} catch (DeliveryPassException eae) {
			eae.printStackTrace();
		} 
	*/	
	}

	
	private List convertSavedToErpRecipienntModel(List savedList)
	{
	 	ListIterator i = savedList.listIterator();
	 	List recList=new ArrayList();
    	while(i.hasNext()) {    		
    		SavedRecipientModel srm = (SavedRecipientModel)i.next();
    		ErpRecipentModel rm=new ErpRecipentModel();
    		rm.toModel(srm);
    		recList.add(rm);
         }
    	return recList;
	}
	
	
	private String getRandomSkuCode() {
		return (String) skus.get((int) (Math.random() * skus.size()));
	}

	private String getRandomSkuCode(String prefix) {
		String skuCode = null;
		do {
			skuCode = (String) skus.get((int) (Math.random() * skus.size()));
		} while (!skuCode.startsWith(prefix));
		return skuCode;
	}

	private FDCartModel makeOrder(FDIdentity identity, int orderSize) {

		try {
           
			FDCartModel cart = new FDCartModel();
			for (int i = 0; i < orderSize; i++) {
				cart.addOrderLines(makeOrderLines("MKT0074896"));
			}
			//
			// two meat skus
			//
			/*for (int i=0; i<2; i++) {
			    cart.addOrderLines(makeOrderLines(getRandomSkuCode("MEA")));
			}
			//
			// two seafood skus
			//
			for (int i=0; i<2; i++) {
			    cart.addOrderLines(makeOrderLines(getRandomSkuCode("SEA")));
			}
			//
			// two deli skus
			//
			for (int i=0; i<2; i++) {
			    cart.addOrderLines(makeOrderLines(getRandomSkuCode("DEL")));
			}
			//
			// and the reset are whatever
			//
			int additionalLines = orderSize - cart.numberOfOrderLines();
			for (int i=0; i<additionalLines; i++) {
			    cart.addOrderLines(makeOrderLines(getRandomSkuCode()));
			}*/

			//Collection addrs = FDCustomerManager.getShipToAddresses(identity);
			//ErpAddressModel address = (ErpAddressModel) (addrs.toArray())[0];
			
			ErpAddressModel address=new ErpAddressModel();
			address.setAddress1("23-30 borden ave");
			address.setCity("jamaica");
			address.setState("NY");
			address.setCountry("US");
			address.setZipCode("11101");
			address.setServiceType(EnumServiceType.PICKUP);

			System.out.println(address.getAddress1());
			System.out.println(address.getCity());
			System.out.println(address.getState());
			System.out.println(address.getZipCode());

			cart.setDeliveryAddress(address);
			Collection ccards = FDCustomerManager.getPaymentMethods(identity);
			Iterator iterator= ccards.iterator();
			boolean working=false;
			while(iterator.hasNext()){
				ErpPaymentMethodI p=(ErpPaymentMethodI)iterator.next();
				if("5405222222222226".equalsIgnoreCase(p.getAccountNumber()))
					{
					System.out.println("found sai ram account hahhh:");
					cart.setPaymentMethod(p);
					working=true;
					}
			}
			if(!working)
			     cart.setPaymentMethod((ErpPaymentMethodI) ((ccards.toArray())[0]));

			Calendar begCal = new GregorianCalendar();
			begCal.set(Calendar.MONTH, Calendar.JULY);
			begCal.set(Calendar.DAY_OF_MONTH, 24);
			begCal.set(Calendar.HOUR_OF_DAY, 0);
			begCal.set(Calendar.MINUTE, 1);

			Calendar endCal = new GregorianCalendar();
			endCal.set(Calendar.MONTH, Calendar.JULY);
			endCal.set(Calendar.DAY_OF_MONTH, 25);
			endCal.set(Calendar.HOUR_OF_DAY, 0);
			endCal.set(Calendar.MINUTE, 1);

			FDDeliveryManager.getInstance().scrubAddress(address);

			System.out.println("-------> find timeslots from " + begCal.getTime() + " to " + endCal.getTime());

		/*	
			List timeSlots =
				FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(begCal.getTime(), endCal.getTime(), address);

			FDTimeslot slot = null;

			int c = 0;
			while ((timeSlots.size() > 0) && (slot == null) && (c < 15)) {
				FDTimeslot fdts = (FDTimeslot) timeSlots.get((int) (Math.random() * timeSlots.size()));
				if (fdts.getBaseAvailable() > 0) {
					slot = fdts;
					System.out.println("timeslot id is : " + slot.getTimeslotId());
					System.out.println("timeslot is : " + slot.getBegDateTime());
				} else {
					c++;
				}
			}

			if (slot == null) {
				System.out.println("No timeslots available...");
				//return null;
			}
			
	*/

			

			//DlvZoneInfoModel zInfo = FDDeliveryManager.getInstance().getZoneInfo(address, new java.util.Date());
			//System.out.println("zone id is : " + zInfo.getZoneId());

			//FDReservation reservation = FDDeliveryManager.getInstance().reserveTimeslot(slot, identity.getErpCustomerPK(), 1000, EnumReservationType.STANDARD_RESERVATION, address.getPK().getId(),false);
			//cart.setDeliveryReservation(reservation);
			
			FDReservation reservation= getFDReservation(identity.getErpCustomerPK(),null);
			cart.setDeliveryReservation(reservation);
			

			//cart.setDeliveryChargeWaived(true);

			//cart.setZoneInfo(zInfo);
			
			
			try {
				cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));
			} catch (FDInvalidAddressException e1) {
				// TODO Auto-generated catch block
				// this will never happen
				e1.printStackTrace();
			}

			return cart;

		} catch (FDResourceException fdre) {
			fdre.printStackTrace();
			return null;
		} 
		/*catch (ReservationException re) {
			re.printStackTrace();
			return null;
		} catch (FDInvalidAddressException fdiae) {
			fdiae.printStackTrace();
			return null;
		} */

	}
	
	
	private static DlvZoneInfoModel getZoneInfo(ErpAddressModel address) throws FDResourceException, FDInvalidAddressException {

		DlvZoneInfoModel zInfo =new DlvZoneInfoModel("1","1","1",EnumZipCheckResponses.DELIVER,false);
		return zInfo;
	}
	
	
	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (GiftCardManagerHome) ctx.lookup("freshdirect.erp.GiftCardManager");
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				System.out.println("Cannot close Context while trying to cleanup");
			}
		}
	}
	
	
	private  FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=getFDTimeSlot();
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION, customerID, addressID, false);
		return reservation;

	}
	
	private  FDTimeslot getFDTimeSlot() {
		// TODO Auto-generated method stub
		return null;
	}

	private List makeOrderLines(String skuCode) throws FDResourceException {
		boolean multiple = false; // one order line per sku
		ArrayList lines = new ArrayList();
		try {
			ContentFactory contentFactory = ContentFactory.getInstance();
			FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
			FDProduct product = FDCachedFactory.getProduct(productInfo);
			ProductModel productmodel = contentFactory.getProduct(skuCode);
			/*
			SkuModel skumodel = null;
			for (Iterator sIter = productmodel.getSkus().iterator(); sIter.hasNext();) {
			    SkuModel sku = (SkuModel) sIter.next();
			    if (sku.getSkuCode().equalsIgnoreCase(skuCode)) {
			        skumodel = sku;
			    }
			}
			*/

			//CategoryModel categorymodel = (CategoryModel) productmodel.getParentNode();
			//DepartmentModel deptmodel = categorymodel.getDepartment();

			// creates the minimum number of configured products to exercise
			// all of the options of all variations and all of the sales units
			// how many to make?
			// find the maximum of the # of sales units and the number of options in each varaition
			int max = product.getSalesUnits().length;
			FDVariation[] variations = product.getVariations();
			for (int i = 0; i < variations.length; i++) {
				FDVariation variation = variations[i];
				max = Math.max(max, variation.getVariationOptions().length);
			}

			for (int n = 0;((n < max) && ((!multiple && (n == 0)) || multiple)); n++) {

				// pick a sales unit
				FDSalesUnit[] units = product.getSalesUnits();
				FDSalesUnit salesUnit = units[n % units.length];
				// make a variation map
				HashMap optionMap = new HashMap();
				for (int i = 0; i < variations.length; i++) {
					FDVariation variation = variations[i];
					FDVariationOption[] options = variation.getVariationOptions();
					FDVariationOption option = options[n % options.length];
					optionMap.put(variation.getName(), option.getName());
				}

				// make the configured product for this sales unit
				//
				// pick a random quantity between 5 and 10, except for...
				//
				int quantity = 5 + (int) (5.0 * Math.random());
				if (skuCode.startsWith("MEA")) {
					//
					// MEAT
					// quantity 1 -> 3 for meat
					//
					quantity = 1 + (int) (3.0 * Math.random());
				} else if (skuCode.startsWith("TEA") || skuCode.startsWith("COF")) {
					//
					// COFFEE & TEA
					// quantity 1
					//
					quantity = 1;
				} else if (skuCode.startsWith("SEA")) {
					//
					// SEAFOOD
					// quantity 1 -> 5
					//
					quantity = 1 + (int) (5.0 * Math.random());
				} else if (skuCode.startsWith("DEL")) {
					//
					// DELI
					// quantity 1
					//
					quantity = 1;
				} else if (skuCode.startsWith("FRU") || skuCode.startsWith("YEL")) {
					//
					// FRUIT
					// quantity 3
					//
					quantity = 3;
				} else if (skuCode.startsWith("VEG")) {
					//
					// VEGGIES
					// quantity 1 -> 5
					//
					quantity = 1 + (int) (5.0 * Math.random());
				} else if (skuCode.startsWith("CHE")) {
					//
					// CHEESE
					// quantity 1
					//
					quantity = 1;
				}else{
					quantity = 1;
				}
				

				FDCartLineModel cartLine =
					new FDCartLineModel(
						new FDSku(productInfo),
						productmodel,
						new FDConfiguration(quantity, salesUnit.getName(), optionMap),null);

				cartLine.setFixedPrice(50);
				cartLine.refreshConfiguration();                
            	lines.add(cartLine);
			}
			
			System.out.println("cartline model:"+lines);
		} catch (FDInvalidConfigurationException ex) {

		} catch (FDSkuNotFoundException fdsnfe) {
			// silently ignore skus that aren't in the store
			fdsnfe.printStackTrace();
			
		}

		return lines;
	}

}
