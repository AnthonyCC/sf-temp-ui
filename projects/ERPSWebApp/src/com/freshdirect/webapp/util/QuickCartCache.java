package com.freshdirect.webapp.util;

import java.util.Date;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.QuickCart;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

import com.freshdirect.framework.util.NVL;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

/**
 * Interface to the QuickCart cached in the session.
 * 
 */
public class QuickCartCache implements SessionName {

    private static Category LOGGER = LoggerFactory.getInstance(QuickCartCache.class);

    /**
     * Return cached instance
     * 
     * @param session
     *            Http session
     * @return cached instance or null
     */
    public static QuickCart getCachedInstance(HttpSession session) {
        LOGGER.debug("Retrieving cached instance " + session.getAttribute(QUICKCART));
        return (QuickCart) session.getAttribute(QUICKCART);
    }

    /**
     * Cache a quick cart instance.
     * 
     * @param session
     *            Http session
     * @param quickCart
     *            cart
     */
    public static void cacheInstance(HttpSession session, QuickCart quickCart) {
        LOGGER.debug("Caching " + quickCart);
        session.setAttribute(QUICKCART, quickCart);
    }

    /**
     * Invalidate cached instance.
     * 
     * @param session
     *            Http session
     */
    public static void invalidateCachedInstance(HttpSession session) {
        LOGGER.debug("Invalidating " + getCachedInstance(session));
        session.setAttribute(QUICKCART, null);
    }

    /**
     * Retrieves Purchase History (aka Every Item Ever Ordered) from cache
     * 
     * @return
     */
    public static QuickCart getQuickCart(HttpSession session) {
        QuickCart quickCart = null;

        FDIdentity ident = getUserIdentity(session);

        if (ident != null) {
            quickCart = QuickCartCache.getCachedInstance(session);

            // delete non-eieo cart
            if (quickCart != null && !quickCart.isEveryItemEverOrdered()) {
                QuickCartCache.invalidateCachedInstance(session);
                quickCart = null;
            }

            // create EIEO
            if (quickCart == null) {
                try {
                    quickCart = createQuickCart(ident);

                    QuickCartCache.cacheInstance(session, quickCart);
                } catch (Exception exc) {
                    quickCart = null;
                }
            }
        }
        return quickCart;
    }

    public static QuickCart createQuickCart(FDIdentity ident) throws FDResourceException {
        QuickCart quickCart;
        quickCart = new QuickCart();
        quickCart.setOrderId("every");
        quickCart.setProductType(QuickCart.PRODUCT_TYPE_PRD);

        List originalLines = FDListManager.getEveryItemEverOrdered(ident);
        quickCart.setDeliveryDate(new Date());
        quickCart.setProducts(originalLines);
        return quickCart;
    }

    static FDIdentity getUserIdentity(HttpSession httpSession) {
        FDUserI user = FDSessionUser.getFDSessionUser(httpSession);
        if (user == null) {
            return null;
        }
        return user.getIdentity();
    }

    /**
     * Invalidate the cache if the cached quick cart if it corresponds to a list
     * that has just changed.
     * 
     * Both id and name maybe null, but if not, then they must match the cached
     * cart's values to invalidate the cached instance.
     * 
     * @param session
     *            HTTP session
     * @param productType
     *            cart's product type
     * @param id
     *            cart id (maybe null)
     * @param name
     *            cart name (maybe null)
     */
    public static void invalidateOnChange(HttpSession session, String productType, String id, String name) {
        QuickCart cachedCart = (QuickCart) session.getAttribute(QUICKCART);

        LOGGER.debug("Comparing " + productType + "(id=" + NVL.apply(id, "-") + ",name=" + NVL.apply(name, "-") + ") to " + cachedCart);
        if (cachedCart != null && cachedCart.getProductType().equals(productType) && (id == null || id.equals(cachedCart.getOrderId()))
                && (name == null || name.equals(cachedCart.getName()))) {
            invalidateCachedInstance(session);
        }
    }
}
