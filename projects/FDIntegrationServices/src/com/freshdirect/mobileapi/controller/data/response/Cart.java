package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class Cart extends Message {

    private List<String> recentlyAddedItems = new ArrayList<String>();

    private Date modificationCutoffTime;

    private CartDetail cartDetail;
    
//    private boolean dpFreeTrialEligible;

	public String getModificationCutoffTime() {
        if (modificationCutoffTime == null){
            return null;
        } else {
            return formatter.format(modificationCutoffTime);
        }
    }

    public Date getModificationCutoffTimeMs() {
        return modificationCutoffTime;
    }

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
            this.modificationCutoffTime = ((ModifyCartDetail) cartDetail).getReservationCutoffDate();
        }
    }

    public List<String> getRecentlyAddedItems() {
        return recentlyAddedItems;
    }

    public void setRecentlyAddedItems(List<String> recentlyAddedItems) {
        this.recentlyAddedItems = recentlyAddedItems;
    }
}
