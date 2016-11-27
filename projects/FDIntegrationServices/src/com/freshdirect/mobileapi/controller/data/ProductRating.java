package com.freshdirect.mobileapi.controller.data;

/**
 * Prouct rating from 1 to 5 
 * @author fgarcia
 *
 */
public class ProductRating {
    /**
     * Value from 1 to 5 indicationg the product rating
     */
    private Integer rating;

    /**
     * Feature that is used to rate the product
     */
    private String name;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating < 1 || rating > 5){
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
