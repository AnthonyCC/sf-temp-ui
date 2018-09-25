package com.freshdirect.mobileapi.controller.data.response.recommender;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;

public class ProductRecommendationData implements Serializable {

    private static final long serialVersionUID = 8989185690733319022L;

    /**
     * recommender ID
     */
    private String id;

    /**
     * recommender title
     */
    private String title;

    private String dealInfo;

    /**
     * List of recommended product keys
     * Note: only for mobile applications
     */
    @JsonInclude(Include.NON_NULL)
    private List<String> productIds;

    /**
     * List of recommended products
     * Note: only for FK Web
     */
    @JsonInclude(Include.NON_NULL)
    private List<ProductPotatoData> products;

    private TrackingData tracking;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDealInfo() {
        return dealInfo;
    }

    public void setDealInfo(String dealInfo) {
        this.dealInfo = dealInfo;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public List<ProductPotatoData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductPotatoData> products) {
        this.products = products;
    }

    public TrackingData getTracking() {
        return tracking;
    }

    public void setTracking(TrackingData tracking) {
        this.tracking = tracking;
    }

}
