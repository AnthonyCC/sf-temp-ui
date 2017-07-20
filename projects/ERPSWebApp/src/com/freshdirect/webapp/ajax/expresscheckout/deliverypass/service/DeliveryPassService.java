package com.freshdirect.webapp.ajax.expresscheckout.deliverypass.service;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.rules.FeeCalculator;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.data.DeliveryPassData;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.data.DeliveryPassProductData;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.MediaUtils;

public class DeliveryPassService {

	private static final DeliveryPassService INSTANCE = new DeliveryPassService();

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
		CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(ContentType.get("Category"), "gro_gear_dlvpass");
		List<ProductModel> availableProducts = new ArrayList<ProductModel>();

		for (ProductModel product : ContentFactory.getInstance().getProducts(category)) {
			if (product.isFullyAvailable() && !product.isDiscontinued()) {
				if (product.getSku(FDStoreProperties.getTwoMonthTrailDPSku()) != null) {
					if (!user.getDlvPassInfo().isFreeTrialRestricted() && (!user.isDlvPassActive() || user.isDlvPassExpired()) && user.getShoppingCart().getDeliveryPassCount() == 0 && user.getDlvPassInfo().getDaysSinceDPExpiry() == 0) {
						availableProducts.add(product);
					}
				} else {
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
		deliveryPassConfiguration.put("cmEventSource", EnumEventSource.BROWSE.getName());
		List<DeliveryPassProductData> products = populateDeliveryPassProducts(deliveryPasses, user);
		products.add(0, getRegualDeliveryFee(user));
		selectDeliveryPass(products, cart, user);
		deliveryPassConfiguration.put("products", products);
		deliveryPassConfiguration.put("termsAndConditions", loadTermsAndConditions());
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
			deliveryPassProductData.setDescription("with DeliveryPass");
			deliveryPassProductData.setProduct(productData);
			if (FDStoreProperties.getOneYearDPSku().equals(productData.getProductId())) {
				deliveryPassProductData.setTitle("1 Year");
				deliveryPassProductData.setTotalPrice(productData.getPrice());
				deliveryPassProductData.setPricePerMonth(calculatePricePerMonth(12, productData.getPrice()));
				deliveryPassProductData.setSaving(calculateSaving(12, productData.getPrice(), oneMonthDeliveryPassTotalPrice));
			} else if (FDStoreProperties.getSixMonthDPSku().equals(productData.getProductId())) {
				deliveryPassProductData.setTitle("6 Months");
				deliveryPassProductData.setTotalPrice(productData.getPrice());
				deliveryPassProductData.setPricePerMonth(calculatePricePerMonth(6, productData.getPrice()));
				deliveryPassProductData.setSaving(calculateSaving(6, productData.getPrice(), oneMonthDeliveryPassTotalPrice));
			} else if (FDStoreProperties.getOneMonthDPSku().equals(productData.getProductId())) {
				deliveryPassProductData.setTitle("1 Months");
				deliveryPassProductData.setTotalPrice(productData.getPrice());
				deliveryPassProductData.setPricePerMonth(calculatePricePerMonth(1, productData.getPrice()));
			} else {
				deliveryPassProductData.setTitle(productData.getProductName());
				deliveryPassProductData.setTotalPrice(productData.getPrice());
			}
			products.add(deliveryPassProductData);
		}
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
		return format.format(actualTotalPrice / month) + " p/month";
	}

	private String calculateSaving(int month, double actualTotalPrice, double oneMonthTotalPrice) {
		return "Save $" + Math.round(month * oneMonthTotalPrice - actualTotalPrice);
	}
}
