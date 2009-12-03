package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.SmartStoreProductResult;
import com.freshdirect.mobileapi.controller.data.request.SmartStoreConfiguration;
import com.freshdirect.mobileapi.model.SmartStore.SmartStoreRecommendationContainer;
import com.freshdirect.mobileapi.model.SmartStore.SmartStoreRecommendationProduct;

public class SmartStoreRecommendations extends Message {

    private String ymalSetId;

    private String originatingProductId;

    public List<SmartStoreProductResult> getProducts() {
        return products;
    }

    private String baseUrl;

    private String recProductIds;

    private String recCurrentNode;

    private String recYmalSource;

    private boolean isLast = false;

    private String recommendationType;

    public String getRecommendationType() {
        return recommendationType;
    }

    public SmartStoreRecommendations(SmartStoreRecommendationContainer data) {
        this.ymalSetId = data.getYmalSetId();
        this.originatingProductId = data.getOriginatingProductId();
        this.baseUrl = data.getBaseUrl();
        this.recProductIds = data.getRecProductIds();
        this.recCurrentNode = data.getRecCurrentNode();
        this.recYmalSource = data.getRecYmalSource();
        this.isLast = data.isLast();

        for (SmartStoreRecommendationProduct recommendationProduct : data.getProducts()) {
            SmartStoreProductResult resultItem = SmartStoreProductResult.wrap(recommendationProduct.getProduct());

            SmartStoreConfiguration smartStoreConfiguration = new SmartStoreConfiguration();
            smartStoreConfiguration.setParameterBundle(recommendationProduct.getParameterBundle());
//            resultItem.setTrackingCode(recommendationProduct.getTrackingCode());
//            smartStoreConfiguration.setVariant(recommendationProduct.getVariant());
//            smartStoreConfiguration.setRecProductIds(recommendationProduct.getRecProductIds());
//            smartStoreConfiguration.setRecCurrentNode(recommendationProduct.getRecCurrentNode());
//            smartStoreConfiguration.setRecYmalSource(recommendationProduct.getRecYmalSource());

            resultItem.setSmartStoreConfiguration(smartStoreConfiguration);
            resultItem.setOptions(recommendationProduct.getOptions());
            products.add(resultItem);
        }
        if (data.getType() != null) {
            this.recommendationType = data.getType().name();
        }
    }

    public String getYmalSetId() {
        return ymalSetId;
    }

    public void setYmalSetId(String ymalSetId) {
        this.ymalSetId = ymalSetId;
    }

    public String getOriginatingProductId() {
        return originatingProductId;
    }

    public void setOriginatingProductId(String originatingProductId) {
        this.originatingProductId = originatingProductId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRecProductIds() {
        return recProductIds;
    }

    public void setRecProductIds(String recProductIds) {
        this.recProductIds = recProductIds;
    }

    public String getRecCurrentNode() {
        return recCurrentNode;
    }

    public void setRecCurrentNode(String recCurrentNode) {
        this.recCurrentNode = recCurrentNode;
    }

    public String getRecYmalSource() {
        return recYmalSource;
    }

    public void setRecYmalSource(String recYmalSource) {
        this.recYmalSource = recYmalSource;
    }

    private List<SmartStoreProductResult> products = new ArrayList<SmartStoreProductResult>();

    public void addProduct(SmartStoreProductResult product) {
        this.products.add(product);
    }

}
