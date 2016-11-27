package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class Cart extends Message {

    private List<String> recentlyAddedItems = new ArrayList<String>();

    private String modificationCutoffTime;

    public String getModificationCutoffTime() {
        return this.modificationCutoffTime;
    }

    private CartDetail cartDetail;

    public CartDetail getCartDetail() {
        return cartDetail;
    }

    /**
     * Will clear the list of recently added items, and add the new one. Use it when adding just one item to cart,
     * for multiple additions, use setRecentlyAddedItems method.
     * @param cartLineId
     */
    public void recentAddedItem(String cartLineId) {
        recentlyAddedItems.clear();
        recentlyAddedItems.add(cartLineId);
    }

    public void setCartDetail(CartDetail cartDetail) {
        this.cartDetail = cartDetail;
        if ((cartDetail != null) && (cartDetail instanceof ModifyCartDetail)) {
            this.modificationCutoffTime = ((ModifyCartDetail) cartDetail).getReservationCutoff();
        }
    }

    public List<String> getRecentlyAddedItems() {
        return recentlyAddedItems;
    }

    public void setRecentlyAddedItems(List<String> recentlyAddedItems) {
        this.recentlyAddedItems = recentlyAddedItems;
    }

}
