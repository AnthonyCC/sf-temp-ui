package com.freshdirect.webapp.ajax.expresscheckout.deliverypass.service;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.rules.FeeCalculator;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.data.DeliveryPassData;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.data.DeliveryPassProductData;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.location.LocationHandlerService;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.taglib.location.LocationHandlerTag;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.MediaUtils;

public class DeliveryPassService {

	private static final DeliveryPassService INSTANCE = new DeliveryPassService();
	private static final Logger LOGGER = LoggerFactory.getInstance(DeliveryPassService.class);

	private DeliveryPassService() {
	}

	public static DeliveryPassService defaultService() {
		return INSTANCE;
	}

	public DeliveryPassData loadDeliveryPasses(FDUserI user) throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse, IOException, TemplateException {
		List<ProductModel> availableProducts = collectDeliveryPassProducts(user);
		FDCartModel cart = user.getShoppingCart();
		DeliveryPassData decoratedDeliveryPassProducts = decorateDeliveryPass(availableProducts, cart, user);
		return decoratedDeliveryPassProducts;
	}

	private DeliveryPassProductData getRegualDeliveryFee(FDUserI user) {
		FeeCalculator calc = new FeeCalculator("DLV");
		FDCartModel cart = user.getShoppingCart();
		double dlvFee = 0.0;
		if (null == cart.getPaymentMethod() || !EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType())) {
			dlvFee = calc.calculateFee(new FDRulesContextImpl(user));
		}
		DeliveryPassProductData result = new DeliveryPassProductData();
		result.setId("REGULAR_DELIVERY_FEE");
		result.setTitle("Regular " + JspMethods.formatPrice(dlvFee));
		result.setDescription("Per Delivery");
		return result;
	}

	private void selectDeliveryPass(List<DeliveryPassProductData> deliveryPasses, FDCartModel cart, FDUserI user) {
		String deliveryPassSkuCodeInCart = null;
		if (user.isDlvPassActive()) {
			deliveryPassSkuCodeInCart = user.getDlvPassInfo().getTypePurchased().getCode();
		} else {
			for (FDCartLineI item : cart.getOrderLines()) {
				if (item.lookupFDProduct().isDeliveryPass()) {
					deliveryPassSkuCodeInCart = item.getSkuCode();
					break;
				}
			}
		}
		boolean deliveryPassSelected = false;
		if (deliveryPassSkuCodeInCart != null) {
			for (DeliveryPassProductData data : deliveryPasses) {
				if (data.getId().equals(deliveryPassSkuCodeInCart)) {
					data.setSelected(true);
					deliveryPassSelected = true;
					break;
				}
			}
		}
		if (!deliveryPassSelected) {
			deliveryPasses.get(0).setSelected(true);
		}
	}

	private List<ProductModel> collectDeliveryPassProducts(FDUserI user) {
        CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(ContentType.Category, "gro_gear_dlvpass");
        List<ProductModel> availableProducts = new ArrayList<ProductModel>();

        for (ProductModel product : ContentFactory.getInstance().getProducts(category)) {
            if (product.isFullyAvailable() && !product.isDiscontinued()) {
                if (product.getSku(FDStoreProperties.getTwoMonthTrailDPSku()) != null) {
                    if (!user.getDlvPassInfo().isFreeTrialRestricted() && (!user.isDlvPassActive() || user.isDlvPassExpired()) && user.getShoppingCart().getDeliveryPassCount() == 0 && user.getDlvPassInfo().getDaysSinceDPExpiry() == 0) {
                        availableProducts.add(product);
                    }
                } else if(!product.getContentKey().getId().equalsIgnoreCase(FDStoreProperties.getOneMonthDPSku())){
                    availableProducts.add(product);
                }
            }
        }

		Collections.sort(availableProducts, Collections.reverseOrder(ProductModel.GENERIC_PRICE_COMPARATOR));
		return availableProducts;
	}

	private DeliveryPassData decorateDeliveryPass(List<ProductModel> deliveryPasses, FDCartModel cart, FDUserI user) throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse,
			IOException, TemplateException {
		DeliveryPassData data = new DeliveryPassData();
		Map<String, Object> deliveryPassConfiguration = data.getDeliveryPass();
		deliveryPassConfiguration.put("eventSource", EnumEventSource.BROWSE.getName());
		List<DeliveryPassProductData> products = populateDeliveryPassProducts(deliveryPasses, user);
	//	products.add(0, getRegualDeliveryFee(user));
		selectDeliveryPass(products, cart, user);
		deliveryPassConfiguration.put("products", products);
		deliveryPassConfiguration.put("termsAndConditions", loadTermsAndConditions());
		deliveryPassConfiguration.put("customerContact", populateCustomerServiceContact(user));
		deliveryPassConfiguration.put("freeTrialEligible", user.isDPFreeTrialOptInEligible());
		deliveryPassConfiguration.put("zipCheckFkDeliveryPassMsg", zipCheckFkDeliveryPassMsg(user));
		return data;
	}

	private List<DeliveryPassProductData> populateDeliveryPassProducts(List<ProductModel> deliveryPasses, FDUserI user) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		List<DeliveryPassProductData> products = new ArrayList<DeliveryPassProductData>();
		ProductModel oneMonthDeliveryPassModel = (ProductModel) ContentFactory.getInstance().getContentNode("Product", FDStoreProperties.getOneMonthDPSku());
		double oneMonthDeliveryPassTotalPrice = ProductDetailPopulator.createProductData(user, oneMonthDeliveryPassModel).getPrice();

		for (ProductModel product : deliveryPasses) {
			ProductData productData = ProductDetailPopulator.createProductData(user, product);
			DeliveryPassProductData deliveryPassProductData = new DeliveryPassProductData();
			deliveryPassProductData.setId(productData.getSkuCode());
		//	deliveryPassProductData.setDescription("with DeliveryPass");
			deliveryPassProductData.setProduct(productData);
			DeliveryPassType deliveryPassType =DeliveryPassType.getEnum(productData.getSkuCode());
			if(null !=deliveryPassType){
				deliveryPassProductData.setTitle(deliveryPassType.getShortName());
				if(!deliveryPassType.getEligibleDlvDays().isEmpty() && deliveryPassType.getEligibleDlvDays().size() < 7){
					deliveryPassProductData.setMidWeekSku(true);				
				}
				deliveryPassProductData.setDuration(deliveryPassType.getDuration());
				deliveryPassProductData.setTotalPrice(productData.getPrice());
				deliveryPassProductData.setPricePerMonth(calculatePricePerMonth(convertDaystoMonth(deliveryPassType.getDuration()), productData.getPrice()));
				deliveryPassProductData.setSaving(calculateSaving(12, productData.getPrice(), oneMonthDeliveryPassTotalPrice));
				products.add(deliveryPassProductData);

			}
		}
		Collections.sort(products, new Comparator<DeliveryPassProductData>() {

			@Override
			public int compare(DeliveryPassProductData d1,
					DeliveryPassProductData d2) {
				
				return d1.getDuration().compareTo(d2.getDuration());
			}
		});
		return products;
	}

	private String loadTermsAndConditions() throws IOException, TemplateException {
		StringWriter out = new StringWriter();
		String result = null;
		try {
			MediaUtils.render("/media/editorial/site_pages/deliverypass/DP_terms.html", out, null, null);
			result = out.toString();
		} finally {
			out.close();
		}
		return result;
	}

	private String calculatePricePerMonth(int month, double actualTotalPrice) {
		NumberFormat format = DecimalFormat.getInstance();
		format.setMinimumFractionDigits(0);
		format.setMaximumFractionDigits(2);
		return format.format(actualTotalPrice / month);
	}
	
	private int convertDaystoMonth(int duration) {
		
		return duration/30;
	}

	private String calculateSaving(int month, double actualTotalPrice, double oneMonthTotalPrice) {
		return "Save $" + Math.round(month * oneMonthTotalPrice - actualTotalPrice);
	}
	
	private String populateCustomerServiceContact(FDUserI user) {
        return UserUtil.getCustomerServiceContact(user);
    }
	
	//	DP17-266 Add geo-appropriate FK messaging to Plans page/pop up
	private boolean zipCheckFkDeliveryPassMsg(FDUserI user) {
		String zipCode = user.getZipCode();
		try {
			FDDeliveryServiceSelectionResult result = FDDeliveryManager.getInstance()
					.getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FDX);
			Set<EnumServiceType> availServices = result.getAvailableServices();

			// remove pickup
			availServices.remove(EnumServiceType.PICKUP);
			if (availServices.contains(EnumServiceType.FDX)) {
				return true;
			}

		} catch (FDResourceException e) {
			LOGGER.debug(e);
		}
		return false;
	}
}
