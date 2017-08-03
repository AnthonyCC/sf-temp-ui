package com.freshdirect.dataloader.analytics;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.http.HttpService;

public class GoogleAnalyticsReportingService {

    private static final GoogleAnalyticsReportingService INSTANCE = new GoogleAnalyticsReportingService();
    private static final String GOOGLE_ANALYTICS_HOST = "http://www.google-analytics.com";

    private GoogleAnalyticsReportingService() {
    }

    public static GoogleAnalyticsReportingService defaultService() {
        return INSTANCE;
    }


    public void postGAReporting(FDOrderI order) throws UnsupportedEncodingException, IOException {
        HttpService.defaultService().postDataWithHttpEntity(GOOGLE_ANALYTICS_HOST, assembleTransactionPayloadForGA(order));
    }

    private HttpEntity assembleTransactionPayloadForGA(FDOrderI order) throws UnsupportedEncodingException {
        HttpEntity entity = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String version = "1";
        String trackingId = FDStoreProperties.getGoogleAnalyticsTrackingId();
        String customerId = order.getCustomerId();
        String hitType = "transaction";
        String documentHostname = "freshdirect.com";
        String transactionId = order.getErpSalesId() + "-Shipped";
        String transactionAffiliation = "FreshDirectWeb";

        params.add(new BasicNameValuePair("v", version));
        params.add(new BasicNameValuePair("tid", trackingId));
        params.add(new BasicNameValuePair("cid", customerId));
        params.add(new BasicNameValuePair("t", hitType));
        params.add(new BasicNameValuePair("dh", documentHostname));
        params.add(new BasicNameValuePair("ti", transactionId));
        params.add(new BasicNameValuePair("ta", transactionAffiliation));

        params.add(new BasicNameValuePair("tr", Double.toString(order.getTotal())));
        params.add(new BasicNameValuePair("ts", getDeliveryCost(order)));
        params.add(new BasicNameValuePair("tt", Double.toString(order.getTaxValue())));
        params.add(new BasicNameValuePair("tcc", populateRedeemedPromotionCodes(order)));

        params.add(new BasicNameValuePair("cd1", customerId));
        params.add(new BasicNameValuePair("cd12", order.getDeliveryAddress().getServiceType().name()));
        params.add(new BasicNameValuePair("cd14",
                order.getDeliveryReservation().getTimeslot().getDeliveryDate() + " " + order.getDeliveryReservation().getTimeslot().getDisplayString()));

        params.add(new BasicNameValuePair("pa", "purchase"));
        int productIndex = 1;
        for (FDCartLineI cartLine : order.getOrderLines()) {
            assembleProductList(params, order, cartLine, productIndex);
            productIndex++;
        }

        entity = new UrlEncodedFormEntity(params);
        return entity;
    }

    private void assembleProductList(List<NameValuePair> params, FDOrderI order, FDCartLineI cartLine, int productIndex) {

        ProductModel product = cartLine.lookupProduct();

        String productNameKeyStarter = "pr" + productIndex;

        params.add(new BasicNameValuePair(productNameKeyStarter + "id", product.getContentName()));
        params.add(new BasicNameValuePair(productNameKeyStarter + "nm", product.getFullName()));
        params.add(new BasicNameValuePair(productNameKeyStarter + "ca", product.getCategory().getContentName()));
        params.add(new BasicNameValuePair(productNameKeyStarter + "br", product.getPrimaryBrandName()));
        params.add(new BasicNameValuePair(productNameKeyStarter + "ps", Integer.toString(productIndex)));
        params.add(new BasicNameValuePair(productNameKeyStarter + "cd3", Boolean.toString(product.isNew())));
        params.add(new BasicNameValuePair(productNameKeyStarter + "cd4", product.getDefaultSkuCode()));

    }

    private String getDeliveryCost(FDOrderI order) {
        String deliveryCost = "";

        if (order.isDlvPassApplied() || order.isChargeWaived(EnumChargeType.DELIVERY) || order.isChargeWaived(EnumChargeType.DLVPREMIUM)) {
            deliveryCost = "$0.00";
        } else if (order.getChargeAmount(EnumChargeType.DELIVERY) > 0 || order.getChargeAmount(EnumChargeType.DLVPREMIUM) > 0) {
            deliveryCost = NumberFormat.getCurrencyInstance(Locale.US).format(order.getChargeAmount(EnumChargeType.DELIVERY));
        }

        return deliveryCost;
    }

    public String populateRedeemedPromotionCodes(FDOrderI order) {
        String result = null;
        if (order instanceof FDOrderAdapter) {
            Set<String> usedPromotionCodes = ((FDOrderAdapter) order).getUsedPromotionCodes();
            if (usedPromotionCodes != null) {
                for (String usedPromotionCode : usedPromotionCodes) {
                    PromotionI promotion = PromotionFactory.getInstance().getPromotion(usedPromotionCode);
                    if (promotion.isRedemption()) {
                        result = promotion.getRedemptionCode();
                        break;
                    }
                }
            }
        }
        return result;
    }

}
