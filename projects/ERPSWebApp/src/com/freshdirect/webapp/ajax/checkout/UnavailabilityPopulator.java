package com.freshdirect.webapp.ajax.checkout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDCompositeAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDMuniAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.EnumProductLayout;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData;
import com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData.Line;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.ProductRecommenderUtil;
import com.freshdirect.webapp.util.ShoppingCartUtil;

public class UnavailabilityPopulator {

	private static final Logger LOG = LoggerFactory.getInstance(UnavailabilityPopulator.class);
	private static final String UNAVAILABLE_TXT = "This item is unavailable.";
	
	public static UnavailabilityData createUnavailabilityData(FDSessionUser user) {
		UnavailabilityData data = new UnavailabilityData();
		FDCartModel cart = user.getShoppingCart();
		Map<String,FDAvailabilityInfo> unavailabilityMap = cart.getUnavailabilityMap(); // cartLineId -> unav FDAvailabilityInfos
		Set<String> unavailableKeys = unavailabilityMap.keySet();

		if(unavailabilityMap.size() > 0 ){
			collectUnavailableProducts(user, unavailableKeys);
			
			for (String key : unavailableKeys) {//process cart lines with issues
				processCartLine(data, cart.getOrderLineById(Integer.parseInt(key)), unavailabilityMap.get(key), user);
			}
			processReservation(data, cart, user); //this modifies amounts as a side affect, it needs to be after cart line potatoes are created
		}

		processDeliveryPasses(data, cart, user);
		return data;
	}
	
	
	private static void processReservation(UnavailabilityData data, FDCartModel cart, FDUserI user){
		FDReservation reservation = cart.getDeliveryReservation();

		//format dates
		data.setDeliveryDate(new SimpleDateFormat("EEEEE, MMM d").format(reservation.getStartTime()));
		data.setDeliveryTimeSlot(FDTimeslot.format(reservation.getStartTime(), reservation.getEndTime()));
		
		
		//check OrderMinimum for time slot
		FDCartModel clonedCart = new FDCartModel( cart );
		List<DlvPassAvailabilityInfo> unavailablePasses = cart.getUnavailablePasses();
		if (unavailablePasses!=null){
			clonedCart.setUnavailablePasses(new ArrayList<DlvPassAvailabilityInfo>(unavailablePasses));	
		}
		clonedCart.setAvailability(cart.getAvailability());
		//this calls ShoppingCartUtil.evaluateCart() which sets amounts on "cart" to deliverable amount as a side affect of cloning the cart
		Double subTotal = ShoppingCartUtil.getSubTotal(clonedCart);
		TimeslotLogic.applyOrderMinimum(user, reservation.getTimeslot(), subTotal);		
			
		if(subTotal!=null && subTotal < reservation.getMinOrderAmt()) {
			data.setNotMetMinAmount(TimeslotLogic.formatMinAmount(reservation.getMinOrderAmt()));
		} 
	}
	
	
	private static void collectUnavailableProducts(FDSessionUser user, Set<String> unavaibleKeys){
		FDCartModel cart = user.getShoppingCart();
		Set<ContentKey> unavailableProductKeys = user.getCheckoutUnavailableProductKeys();
		
		if (unavailableProductKeys == null){
			unavailableProductKeys = new HashSet<ContentKey>();
			user.setCheckoutUnavailableProductKeys(unavailableProductKeys);
		}
		
		for (String key : unavaibleKeys) {
			ProductReference productReference = cart.getOrderLineById(Integer.parseInt(key)).getProductRef(); 
			if (productReference != null){
				ProductModel product = productReference.lookupProductModel();
				if (product!=null){
					unavailableProductKeys.add(product.getContentKey());
				}
			}
		}
	}
	
    private static boolean hasProductSpecialLayout(ProductModel productNode, EnumProductLayout layout) {
        return (productNode != null && productNode.getSpecialLayout() != null && productNode.getSpecialLayout() == layout);
	}

	//TODO revise html generation
	private static void processCartLine(UnavailabilityData data, FDCartLineI cartLine, FDAvailabilityInfo info, FDSessionUser user){
		try {
			Line line = new Line();
			line.setCartLine(ProductDetailPopulator.createCartLineData(user, cartLine));
			
			if (info instanceof FDRestrictedAvailabilityInfo) {
				RestrictionI restriction = ((FDRestrictedAvailabilityInfo)info).getRestriction();
	
				if (EnumDlvRestrictionReason.KOSHER.equals(restriction.getReason())) { //TODO use embedded popup instead of opening a new window
					line.setDescription("<a href=\"javascript:popup('/shared/departments/kosher/delivery_info.jsp','small')\">Kosher production item</a> - not available Fri, Sat, Sun AM, and holidays");
				} else {
					line.setDescription(restriction.getMessage());
				}
			
				data.getNonReplaceableLines().add(line);
				
			} else if (info instanceof FDCompositeAvailabilityInfo) {
				StringBuilder description = new StringBuilder();
				description.append("The following options are unavailable:<br>");
	
				Map<String,FDAvailabilityInfo> componentInfos = ((FDCompositeAvailabilityInfo)info).getComponentInfo();
				boolean singleOptionIsOut= false;
				for (Iterator<Map.Entry<String,FDAvailabilityInfo>> i = componentInfos.entrySet().iterator(); i.hasNext(); ) {
					Map.Entry<String,FDAvailabilityInfo> e = i.next();
					String componentKey = e.getKey();
					if (componentKey != null) {
						FDProduct fdp = cartLine.lookupFDProduct();
						String matNo = StringUtils.right(componentKey, 9);
						FDVariationOption option = fdp.getVariationOption(matNo);
						if (option!=null) {
							description.append("<b>" + option.getDescription() + "</b><br>");
							//Check to see if this option is the only option for the variation
							FDVariation[] vars = fdp.getVariations();
							for (int vi=0; vi <vars.length;vi++){
								if (vars[vi].getVariationOption(matNo)!=null && vars[vi].getVariationOptions().length>0) {
								    singleOptionIsOut=true;
								};
							}
						}
					}
				}
				
				if (!singleOptionIsOut) {
                    description.append("<a href=\"/product_modify.jsp?cartLine=" + cartLine.getRandomId() + "&skuCode=" + cartLine.getSku().getSkuCode()
                            + "\">Click here</a> to select other options.");
				}
	
                line.setDescription((hasProductSpecialLayout(cartLine.lookupProduct(), EnumProductLayout.HOLIDAY_MEAL_BUNDLE_PRODUCT)
                        || hasProductSpecialLayout(cartLine.lookupProduct(), EnumProductLayout.RECIPE_MEALKIT_PRODUCT)) ? "" : description.toString());
				data.getNonReplaceableLines().add(line);
				
			} else if (info instanceof FDMuniAvailabilityInfo) {
				line.setDescription("FreshDirect does not deliver alcohol outside NY.");
				data.getNonReplaceableLines().add(line);
	
			} else if (info instanceof FDStockAvailabilityInfo) {
				double availQty = ((FDStockAvailabilityInfo)info).getQuantity();

				if (availQty == 0){
					line.setDescription(UNAVAILABLE_TXT);

				} else {
					String availQtyFormatted = new DecimalFormat("0.##").format(availQty);
					String label = cartLine.getLabel();
				
					if ("".equals(label)){
						if (availQty==1) {
							line.setDescription("Only 1 is available.");
						} else {
							line.setDescription("Only "+availQtyFormatted + " are available.");
						}
						line.setAvailableQuantity(availQtyFormatted);
					
					} else {
						String labelledQuantity = availQtyFormatted+" "+label;
						line.setDescription("Only "+labelledQuantity+" available.");
						line.setAvailableQuantity(labelledQuantity);
					}
				}
				line.setRecommendedProducts(collectRecommendedProducts(cartLine, user));
				data.getReplaceableLines().add(line);
				
			} else { //FDStatusAvailabilityInfo - doesn't seem to be used in code, use default out of stock message
				line.setDescription(UNAVAILABLE_TXT);
				line.setRecommendedProducts(collectRecommendedProducts(cartLine, user));
				data.getReplaceableLines().add(line);
			}
		
		} catch (FDSkuNotFoundException e){
			LOG.error("Cannot process cart line " + cartLine.getRandomId(), e);
		} catch (FDResourceException e) {
			LOG.error("Cannot process cart line " + cartLine.getRandomId(), e);
		} catch (HttpErrorResponse e) {
			LOG.error("Cannot process cart line " + cartLine.getRandomId(), e);
		}
	}

	
	private static void processDeliveryPasses(UnavailabilityData data, FDCartModel cart, FDUserI user){
		List<DlvPassAvailabilityInfo> unavailPasses = cart.getUnavailablePasses();
		
		if (unavailPasses != null) {
			for (DlvPassAvailabilityInfo info : unavailPasses) {
				Integer key = info.getKey();
				try {
					Line line = new Line();
					line.setCartLine(ProductDetailPopulator.createCartLineData(user, cart.getOrderLineById(key)));
					line.setDescription(info.getReason());
					data.getPasses().add(line);
				
				} catch (FDSkuNotFoundException e){
					LOG.error("Cannot process cart line " + key, e);
				} catch (FDResourceException e) {
					LOG.error("Cannot process cart line " + key, e);
				} catch (HttpErrorResponse e) {
					LOG.error("Cannot process cart line " + key, e);
				}
			}	
		}
	}
	
	
	private static List<ProductData> collectRecommendedProducts(FDCartLineI cartLine, FDSessionUser user){
		List<ProductData> replacements = new ArrayList<ProductData>();
		
		ProductReference productReference = cartLine.getProductRef(); 
		if (productReference != null){
			for (ProductModel replacementProduct : ProductRecommenderUtil.getUnavailableReplacementProducts(productReference.lookupProductModel(), user.getCheckoutUnavailableProductKeys())){
				try {
					replacements.add(ProductDetailPopulator.createProductData(user, replacementProduct));
				
				} catch (FDSkuNotFoundException e){
					LOG.error("Cannot process cart line " + cartLine.getRandomId(), e);
				} catch (FDResourceException e) {
					LOG.error("Cannot process cart line " + cartLine.getRandomId(), e);
				} catch (HttpErrorResponse e) {
					LOG.error("Cannot process cart line " + cartLine.getRandomId(), e);
				}	
			}
		}
		return replacements;
	}
}
