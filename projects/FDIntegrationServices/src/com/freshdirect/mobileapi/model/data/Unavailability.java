package com.freshdirect.mobileapi.model.data;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDCompositeAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDMuniAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.mobileapi.controller.data.ProductSearchResult;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.ProductRecommenderUtil;
import com.freshdirect.webapp.util.ShoppingCartUtil;

public class Unavailability {
	public static class Line {
		private String availableQuantity="0";
		private String cartLineId;
		private List<ProductSearchResult> recommendedProducts = new ArrayList<ProductSearchResult>();
		private String description;
		public String getAvailableQuantity() {
			return availableQuantity;
		}
		public String getCartLineId() {
			return cartLineId;
		}
		public List<ProductSearchResult> getRecommendedProducts() {
			return recommendedProducts;
		}
		public String getDescription() {
			return description;
		}
		public void setAvailableQuantity(String availableQuantity) {
			this.availableQuantity = availableQuantity;
		}
		public void setCartLineId(String cartLineId) {
			this.cartLineId = cartLineId;
		}
		public void setRecommendedProducts(List<ProductSearchResult> recommendedProducts) {
			this.recommendedProducts = recommendedProducts;
		}
		public void setDescription(String description) {
			this.description = description;
		}
//		private static Line wrap(
//				com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData.Line data, SessionUser user ) {
//			Line line = new Line();
//			line.setAvailableQuantity(data.getAvailableQuantity());
//			line.setCartLineId(Integer.toString(data.getCartLine().getRandomId()));
//			line.setDescription(data.getDescription());
//			line.setRecommendedProducts(new ArrayList<ProductSearchResult>(data.getRecommendedProducts().size()));
//			for (ProductData item : data.getRecommendedProducts()) {
//				try {
//					Product product = Product.getProduct(item.getProductId(), item.getCatId(), null, user);
//					line.getRecommendedProducts().add(ProductSearchResult.wrap(product));
//				} catch (ServiceException ignored) {
//				}
//			}
//			return line;
//		}
	}
	private String deliveryDate;
	private String deliveryTimeSlot;
	private String notMetMinAmount; //contains an amount in case of a problem, else null 
	private List<Unavailability.Line> replaceableLines = new ArrayList<Unavailability.Line>();
	private List<Unavailability.Line> nonReplaceableLines = new ArrayList<Unavailability.Line>();
	private List<Unavailability.Line> passes = new ArrayList<Unavailability.Line>();
	private List<Unavailability.Line> invalidLines = new ArrayList<Unavailability.Line>();
	private ArrayList<String> issues;
	
//	private static Unavailability wrap(UnavailabilityData data, SessionUser user) {
//		Unavailability unavailability = new Unavailability();
//		unavailability.setDeliveryDate(data.getDeliveryDate());
//		unavailability.setDeliveryTimeSlot(data.getDeliveryTimeSlot());
//		unavailability.setNotMetMinAmount(data.getNotMetMinAmount());
//		for (UnavailabilityData.Line line : data.getNonReplaceableLines()) {
//			unavailability.getNonReplaceableLines().add(Line.wrap(line, user));
//		}
//		for (UnavailabilityData.Line line : data.getReplaceableLines()) {
//			unavailability.getReplaceableLines().add(Line.wrap(line, user));
//		}
//		for (UnavailabilityData.Line line : data.getPasses()) {
//			unavailability.getPasses().add(Line.wrap(line, user));
//		}
//		return unavailability;
//	}
	
	public static Unavailability collectData(SessionUser sessionUser) {
		ArrayList<String> issues = new ArrayList<String>();
		FDSessionUser user = sessionUser.getFDSessionUser();
		Unavailability data = new Unavailability();
		FDCartModel cart = user.getShoppingCart();
		Map<String,FDAvailabilityInfo> unavailabilityMap = cart.getUnavailabilityMap(); // cartLineId -> unav FDAvailabilityInfos
		Set<String> unavailableKeys = unavailabilityMap.keySet();

		if(unavailabilityMap.size() > 0 ){
			try {
				collectUnavailableProducts(user, unavailableKeys);
			} catch (Throwable e) {
				issues.add(traceFor(e));
			}
			
			for (String key : unavailableKeys) {//process cart lines with issues
				try {
					processCartLine(data, cart.getOrderLineById(Integer.parseInt(key, 10)), unavailabilityMap.get(key), user);
				} catch (Throwable e) {
					issues.add(traceFor(e));
				}
			}
			try {
				processReservation(data, cart, user); //this modifies amounts as a side affect, it needs to be after cart line potatoes are created
			} catch (Throwable e) {
				issues.add(traceFor(e));
			}
		}
		data.setIssues(issues);

	//processDeliveryPasses(data, cart, user);
	processInvalidLines(data, cart, user);
		return data;
	}
	
	private static void collectUnavailableProducts(FDSessionUser user,
			Set<String> unavailableKeys) {
		FDCartModel cart = user.getShoppingCart();
		Set<ContentKey> unavailableProductKeys = user.getCheckoutUnavailableProductKeys();
		
		if (unavailableProductKeys == null){
			unavailableProductKeys = new HashSet<ContentKey>();
			user.setCheckoutUnavailableProductKeys(unavailableProductKeys);
		}
		
		for (String key : unavailableKeys) {
			ProductReference productReference = cart.getOrderLineById(Integer.parseInt(key)).getProductRef(); 
			if (productReference != null){
				ProductModel product = productReference.lookupProductModel();
				if (product!=null){
					unavailableProductKeys.add(product.getContentKey());
				}
			}
		}
	}

	private static final String UNAVAILABLE_TXT = "This item is unavailable.";
	private static void processCartLine(Unavailability data,
			FDCartLineI cartLine, FDAvailabilityInfo info,
			FDSessionUser user) {
		Line line = new Line();
		line.setCartLineId(Integer.toString((cartLine.getRandomId())));
		
		if (info instanceof FDRestrictedAvailabilityInfo) {
			RestrictionI restriction = ((FDRestrictedAvailabilityInfo)info).getRestriction();

			if (EnumDlvRestrictionReason.KOSHER.equals(restriction.getReason())) {
				line.setDescription("Kosher production item - not available Fri, Sat, Sun AM, and holidays");
			} else {
				line.setDescription(restriction.getMessage());
			}
		
			data.getNonReplaceableLines().add(line);
			
		} else if (info instanceof FDCompositeAvailabilityInfo) {
			StringBuilder description = new StringBuilder();
			description.append("The following options are unavailable:<br>");

			Map<String,FDAvailabilityInfo> componentInfos = ((FDCompositeAvailabilityInfo)info).getComponentInfo();
			for (String componentKey : componentInfos.keySet()) {
				if (componentKey != null) {
					FDProduct fdp = cartLine.lookupFDProduct();
					String matNo = StringUtils.right(componentKey, 9);
					FDVariationOption option = fdp.getVariationOption(matNo);
					if (option != null) {
						description.append("<b>" + option.getDescription() + "</b><br>");
					}
				}
			}
			
			line.setDescription(description.toString());
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
			
				if ("".equals(label)) {
					if (availQty == 1) {
						line.setDescription("Only 1 is available.");
					} else {
						line.setDescription("Only " + availQtyFormatted + " are available.");
					}
					line.setAvailableQuantity(availQtyFormatted);

				} else {
					String labelledQuantity = availQtyFormatted + " " + label;
					line.setDescription("Only " + labelledQuantity + " available.");
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
	}

	private static List<ProductSearchResult> collectRecommendedProducts(
			FDCartLineI cartLine, FDSessionUser user) {
		List<ProductSearchResult> replacements = new ArrayList<ProductSearchResult>();
		
		ProductReference productReference = cartLine.getProductRef(); 
		if (productReference != null){
			for (ProductModel replacementProduct : ProductRecommenderUtil.getUnavailableReplacementProducts(productReference.lookupProductModel(), user.getCheckoutUnavailableProductKeys())){
				try {
					Product prod = Product.wrap(replacementProduct, user);
					replacements.add(ProductSearchResult.wrap(prod));
				} catch (ModelException e) {
				}	
			}
		}
		return replacements;
	}

	private static void processReservation(Unavailability data,
			FDCartModel cart, FDSessionUser user) {
		FDReservation reservation = cart.getDeliveryReservation();

		//format dates
		data.setDeliveryDate(new SimpleDateFormat("EEEEE, MMM d").format(reservation.getStartTime()));
		data.setDeliveryTimeSlot(FDTimeslot.format(reservation.getStartTime(), reservation.getEndTime()));

		//check OrderMinimum for time slot
		FDCartModel clonedCart = new FDCartModel( cart );
		List<DlvPassAvailabilityInfo> unavailablePasses = cart.getUnavailablePasses();
		if (unavailablePasses != null){
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

	private static void processDeliveryPasses(Unavailability data,
			FDCartModel cart, FDSessionUser user) {
		List<DlvPassAvailabilityInfo> unavailPasses = cart.getUnavailablePasses();
		
		if (unavailPasses != null) {
			for (DlvPassAvailabilityInfo info : unavailPasses) {
				Integer key = info.getKey();
				Line line = new Line();
				line.setCartLineId(cart.getOrderLineById(key).getCartlineId());
				line.setDescription(info.getReason());
				data.getPasses().add(line);
			}
		}
	}
	private static void processInvalidLines(Unavailability data,
			FDCartModel cart, FDSessionUser user) {
		
		List<FDCartLineI> invalidLines = OrderLineUtil.getInvalidLines(cart.getOrderLines(), user.getUserContext());
				
		//List<FDCartLineI> invalidLines =cart.getInvalidOrderLines();
		
			for (FDCartLineI info : invalidLines) {
				
				Line line = new Line();
				line.setCartLineId(String.valueOf(info.getRandomId()));
				line.setDescription(info.getSkuCode()+" is not available");
				data.getInvalidLines().add(line);
			}
		
	}

	protected static String traceFor(Throwable e) {
		if (e == null) return "No Exception";
		while (true) {
			if (e.getCause() == null) break;
			e = e.getCause();
		}
	    StringWriter trace = new StringWriter();
	    e.printStackTrace(new PrintWriter(trace));
	    String printedTrace = "Exception: " + e.getMessage() + "\n" + trace.toString();
	    return printedTrace;
    }

	public String getDeliveryDate() {
		return deliveryDate;
	}
	public String getDeliveryTimeSlot() {
		return deliveryTimeSlot;
	}
	public String getNotMetMinAmount() {
		return notMetMinAmount;
	}
	public List<Unavailability.Line> getReplaceableLines() {
		return replaceableLines;
	}
	public List<Unavailability.Line> getNonReplaceableLines() {
		return nonReplaceableLines;
	}
	public List<Unavailability.Line> getPasses() {
		return passes;
	}
	
	public List<Unavailability.Line> getInvalidLines() {
		return invalidLines;
	}
	
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public void setDeliveryTimeSlot(String deliveryTimeSlot) {
		this.deliveryTimeSlot = deliveryTimeSlot;
	}
	public void setNotMetMinAmount(String notMetMinAmount) {
		this.notMetMinAmount = notMetMinAmount;
	}
	public void setReplaceableLines(List<Unavailability.Line> replaceableLines) {
		this.replaceableLines = replaceableLines;
	}
	public void setNonReplaceableLines(List<Unavailability.Line> nonReplaceableLines) {
		this.nonReplaceableLines = nonReplaceableLines;
	}
	public void setPasses(List<Unavailability.Line> passes) {
		this.passes = passes;
	}
	
	public void setInvalidLines(List<Unavailability.Line> lines) {
		this.invalidLines = lines;
	}

	public ArrayList<String> getIssues() {
		return issues;
	}

	public void setIssues(ArrayList<String> issues) {
		this.issues = issues;
	}
}