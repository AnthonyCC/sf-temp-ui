package com.freshdirect.mobileapi.controller.data.response.recommender;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class CartRecommenderResponse extends Message {

    private List<ProductRecommendationData> recommendations;

    public CartRecommenderResponse(List<ProductRecommendationData> recommendations) {
        this.recommendations = recommendations;
    }

    public List<ProductRecommendationData> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<ProductRecommendationData> recommendations) {
        this.recommendations = recommendations;
    }
}
