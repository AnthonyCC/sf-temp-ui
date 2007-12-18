
/*
 * CreateOrderTestCase.java
 * JUnit based test
 *
 * Created on May 15, 2002, 8:27 PM
 */

package com.freshdirect.pci;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
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
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;

/**
 *
 * @author mrose
 */
public class CreateOrderTestCase extends TestCase {

	public CreateOrderTestCase(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		//junit.textui.TestRunner.run(suite());
		junit.textui.TestRunner.run(CreateOrderTestCase.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CreateOrderTestCase.class);
		return suite;
	}

	private static List skus = new ArrayList();

	private static List identities = new ArrayList();

	private static Object syncOnMe = new Object();

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
		System.out.println("Random "+Math.random());
		System.out.println("identities size "+identities.size());;
		// pick an identity
		FDIdentity identity = (FDIdentity) identities.get((int)(Math.random() * identities.size()));
		System.out.println("order for erp_cust_id = " + identity.getErpCustomerPK());

		// how many orderlines per order?
		int lineCount = 1;

		FDCartModel cart = makeOrder(identity, lineCount);

		if (cart == null)
			return;

		try {
			// wait a few seconds between reserving a timeslot and committing the order to better emulate user behavior
			Thread.sleep(1000 * (3 + (int) (Math.random() * 5.0)));
		} catch (InterruptedException ie) {
			// who cares?
		}
		CustomerRatingI rating = new CustomerRatingAdaptor(new ProfileModel(), false, 10);
		String initiator = "CUSTOMER";
		try {
			FDActionInfo actionInfo = new FDActionInfo(EnumTransactionSource.SYSTEM, identity, initiator, "CreateOrderTestCase", null);
			System.out.println("Order total "+cart.getTotal());
			FDCustomerManager.placeOrder(actionInfo, cart, new FDPromotionEligibility(), false, rating, EnumDlvPassStatus.NONE);
		} catch (FDResourceException fdre) {
			fdre.printStackTrace();
		} catch (ErpFraudException efe) {
			efe.printStackTrace();
		} catch (ErpAuthorizationException eae) {
			eae.printStackTrace();
		} catch (ReservationException re) {
			re.printStackTrace();
		} catch (DeliveryPassException de) {
			de.printStackTrace();
		}

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
			FDCartModel clonedCart = new FDCartModel(cart);
			for (int i = 0; i < orderSize; i++) {
				List orderLines = makeOrderLines("MEA0004666");
				clonedCart.addOrderLines(orderLines);
				if(clonedCart.getTotal() > 75.0){
						break;
				}
				cart.addOrderLines(orderLines);
				System.out.println("Total "+cart.getTotal());
			}

			Collection addrs = FDCustomerManager.getShipToAddresses(identity);
			ErpAddressModel address = (ErpAddressModel) (addrs.toArray())[0];

			System.out.println(address.getAddress1());
			System.out.println(address.getCity());
			System.out.println(address.getState());
			System.out.println(address.getZipCode());

			cart.setDeliveryAddress(address);
			Collection ccards = FDCustomerManager.getPaymentMethods(identity);
			cart.setPaymentMethod((ErpPaymentMethodI) ((ccards.toArray())[0]));

			Calendar begCal = new GregorianCalendar();
			begCal.set(Calendar.MONTH, Calendar.MAY);
			begCal.set(Calendar.DAY_OF_MONTH, 05);
			begCal.set(Calendar.HOUR, 0);
			begCal.set(Calendar.MINUTE, 0);
			begCal.set(Calendar.SECOND, 0);
			begCal.set(Calendar.AM_PM, Calendar.PM);

			Calendar endCal = new GregorianCalendar();
			endCal.set(Calendar.MONTH, Calendar.MAY);
			endCal.set(Calendar.DAY_OF_MONTH, 12);
			endCal.set(Calendar.HOUR, 0);
			endCal.set(Calendar.MINUTE, 0);
			endCal.set(Calendar.SECOND, 0);
			endCal.set(Calendar.AM_PM, Calendar.PM);
			
			FDDeliveryManager.getInstance().scrubAddress(address);

			System.out.println("-------> find timeslots from " + begCal.getTime() + " to " + endCal.getTime());

			DlvZoneInfoModel zInfo = FDDeliveryManager.getInstance().getZoneInfo(address, new java.util.Date());
			System.out.println("zone id is : " + zInfo.getZoneId());
			//System.out.println("Zone id "+address.getAddressInfo().getZoneId());
			List timeSlots =
				FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(begCal.getTime(), endCal.getTime(), address);

			FDTimeslot slot = null;

			int c = 0;
			while ((timeSlots.size() > 0) && (slot == null) && (c < 15)) {
				FDTimeslot fdts = (FDTimeslot) timeSlots.get(c);
				System.out.println("Timeslot Available "+fdts.getBaseAvailable());
				if (fdts.getBaseAvailable() >= 500) {
					slot = fdts;
					System.out.println("timeslot id is : " + slot.getTimeslotId());
					System.out.println("timeslot is : " + slot.getBegDateTime());
				} else {
					c++;
				}
			}

			if (slot == null) {
				System.out.println("No timeslots available...");
				return null;
			}

			FDReservation reservation = FDDeliveryManager.getInstance().reserveTimeslot(slot, identity.getErpCustomerPK(), 10000, EnumReservationType.STANDARD_RESERVATION, address.getPK().getId(),false);
			cart.setDeliveryReservation(reservation);

			//cart.setDeliveryChargeWaived(true);
			cart.setZoneInfo(zInfo);

			return cart;

		} catch (FDResourceException fdre) {
			fdre.printStackTrace();
			return null;
		} catch (ReservationException re) {
			re.printStackTrace();
			return null;
		} catch (FDInvalidAddressException fdiae) {
			fdiae.printStackTrace();
			return null;
		}

	}

	private List makeOrderLines(String skuCode) throws FDResourceException {
		boolean multiple = false; // one order line per sku
		ArrayList lines = new ArrayList();
		try {
			ContentFactory contentFactory = ContentFactory.getInstance();
			FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
			FDProduct product = FDCachedFactory.getProduct(productInfo);
			ProductModel productmodel = contentFactory.getProduct(skuCode);
			System.out.println("After getting productmodel");

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
				int quantity = 5;

				FDCartLineModel cartLine =
					new FDCartLineModel(
						new FDSku(productInfo),
						productmodel.getProductRef(),
						new FDConfiguration(quantity, salesUnit.getName(), optionMap));

				cartLine.refreshConfiguration();
				
				lines.add(cartLine);
			}
		} catch (FDInvalidConfigurationException ex) {

		} catch (FDSkuNotFoundException fdsnfe) {
			// silently ignore skus that aren't in the store
			//fdsnfe.printStackTrace();
		}

		return lines;
	}

}
