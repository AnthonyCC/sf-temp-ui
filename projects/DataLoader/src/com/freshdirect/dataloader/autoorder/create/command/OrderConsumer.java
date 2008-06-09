package com.freshdirect.dataloader.autoorder.create.command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.dataloader.autoorder.create.util.DateUtil;
import com.freshdirect.dataloader.autoorder.create.util.IConstants;
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
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;


public class OrderConsumer implements IConsumer {
	
	private List skus = null;
	
	private java.util.Date baseDate = null;
	
	
	public void start(String filePath, java.util.Date baseDate)  {
		skus = readSkus(filePath);
		if(skus == null || skus.size() == 0) {
    		throw new IllegalArgumentException("Invalid Sku Path Input Argument");
    	}
		this.baseDate = baseDate;
	}
	
	public synchronized void consume(Object object, Connection conn) {
		try {
			OrderBean model = (OrderBean)object;
			createOrder(new FDIdentity(model.getErpCustomerPK(),model.getFdCustomerPK()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createOrder(FDIdentity identity) {
		
		// how many orderlines per order?
		int lineCount = 30 + (int) (Math.random() * 20.0);

		FDCartModel cart = makeOrder(identity, lineCount);

		if (cart == null)
			return;
		
		//System.out.println("cart>>"+cart);
		/*try {
			// wait a few seconds between reserving a timeslot and committing the order to better emulate user behavior
			Thread.sleep(1000 * (3 + (int) (Math.random() * 5.0)));
		} catch (InterruptedException ie) {
			// who cares?
		}*/
		CustomerRatingI rating = new CustomerRatingAdaptor(new ProfileModel(), false, 10);

		try {
			FDActionInfo actionInfo = new FDActionInfo(EnumTransactionSource.SYSTEM, identity, "AutoOrder", "",IConstants.AGENT);			
			FDCustomerManager.placeOrder(actionInfo, cart,(Set)new HashSet(), false, rating, EnumDlvPassStatus.NONE);
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
	

	private FDCartModel makeOrder(FDIdentity identity, int orderSize) {

		try {

			FDCartModel cart = new FDCartModel();
			for (int i = 0; i < orderSize; i++) {				
				cart.addOrderLines(makeOrderLines(getRandomSkuCode()));
			}
			
			Collection addrs = FDCustomerManager.getShipToAddresses(identity);
			ErpAddressModel address = (ErpAddressModel) (addrs.toArray())[0];

			/*System.out.println(address.getAddress1());
			System.out.println(address.getCity());
			System.out.println(address.getState());
			System.out.println(address.getZipCode());*/

			cart.setDeliveryAddress(address);
			Collection ccards = FDCustomerManager.getPaymentMethods(identity);
			cart.setPaymentMethod((ErpPaymentMethodI) ((ccards.toArray())[0]));

			Calendar begCal = Calendar.getInstance();
			begCal.setTime(DateUtil.addDays(baseDate,-1));			
			begCal.set(Calendar.HOUR_OF_DAY, 0);
			begCal.set(Calendar.MINUTE, 0);
			begCal.set(Calendar.SECOND, 0);

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(DateUtil.addDays(baseDate,1));
			endCal.set(Calendar.HOUR_OF_DAY, 0);
			endCal.set(Calendar.MINUTE, 0);
			endCal.set(Calendar.SECOND, 0);

			FDDeliveryManager.getInstance().scrubAddress(address);

			//System.out.println("-------> find timeslots from " + begCal.getTime() + " to " + endCal.getTime());

			List timeSlots =
				FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(begCal.getTime(), endCal.getTime(), address);

			FDTimeslot slot = null;

			int c = 0;
			while ((timeSlots.size() > 0) && (slot == null) && (c < 15)) {
				FDTimeslot fdts = (FDTimeslot) timeSlots.get((int) (Math.random() * timeSlots.size()));
				if (fdts.getDlvTimeslot().getCapacity() > 0) {
					slot = fdts;
					//System.out.println("timeslot id is : " + slot.getTimeslotId());
					//System.out.println("timeslot is : " + slot.getBegDateTime());
				} else {
					c++;
				}
			}

			if (slot == null) {
				System.out.println("No timeslots available...");
				return null;
			}

			DlvZoneInfoModel zInfo = FDDeliveryManager.getInstance().getZoneInfo(address, new java.util.Date());
			System.out.println("zone id is : " + zInfo.getZoneId());

			FDReservation reservation = FDDeliveryManager.getInstance()
											.reserveTimeslot(slot, identity.getErpCustomerPK(), (long)1000
														, EnumReservationType.STANDARD_RESERVATION, address.getPK().getId(), false);
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
				}

				FDCartLineModel cartLine =
					new FDCartLineModel(
						new FDSku(productInfo),
						productmodel.getProductRef(),
						new FDConfiguration(quantity, salesUnit.getName(), optionMap));

				cartLine.refreshConfiguration();
				
				lines.add(cartLine);
				//System.out.println("lines >>"+lines);
			}
		} catch (FDInvalidConfigurationException ex) {
			ex.printStackTrace();
		} catch (FDSkuNotFoundException fdsnfe) {
			// silently ignore skus that aren't in the store
			fdsnfe.printStackTrace();
		}

		return lines;
	}
	
	public void end() {
		
	}
	
	private List readSkus(String path) {
		List dataList = new ArrayList(); 
		try {			
		
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s = br.readLine()) != null) {
				if(!StringUtils.isEmpty(s)) {
					dataList.add(s);
				}
			}
		} catch(Exception e) {
			throw new IllegalArgumentException("Invalid Sku File Argument");
		}
		return dataList; 
	}

}
