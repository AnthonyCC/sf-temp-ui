package com.freshdirect.mobileapi.controller.data;


/**
 * Acknowledge as "Deli Buying Guide". A list of posible container size for a product. 
 * @author fgarcia
 *
 */
public class ContainerSize {
    /**
     * Possible sizes
     * @author fgarcia
     *
     */
    public enum Size {
        HALF_PINT, PINT, QUART
    }

    Size size;

    float weigth;

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public float getWeigth() {
        return weigth;
    }

    public void setWeigth(float weigth) {
        this.weigth = weigth;
    }

}
