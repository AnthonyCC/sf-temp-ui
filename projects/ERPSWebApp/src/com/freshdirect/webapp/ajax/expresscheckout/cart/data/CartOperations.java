package com.freshdirect.webapp.ajax.expresscheckout.cart.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.event.EventLogger;
import com.freshdirect.event.FDAddToCartEvent;
import com.freshdirect.event.FDCartLineEvent;
import com.freshdirect.event.FDEditCartEvent;
import com.freshdirect.event.FDRemoveCartEvent;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.coremetrics.builder.AbstractShopTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.extradata.CoremetricsExtraData;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.ICoremetricsResponse;
import com.freshdirect.webapp.taglib.coremetrics.AbstractCmShopTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.FDEventFactory;

/**
 * Helper class for cart operations
 *
 * Static, stateless utility class.
 *
 * Implements only simple cart operations for the cart widget and add-to-cart servlet (e.g. change quantity, remove items, add to cart), not the full set of cart operations (e.g.
 * view cart page, checkout, ccl, etc...)
 *
 * Implements 'pure' operations: i.e. change quantity will not remove cartlines, only remove operation does. change quantity to zero will set quantity to the minimum and will not
 * remove the cartline.
 *
 * So the decision of what type of operation should happen has to be made before calling.
 *
 * business logic parts copied from FDShoppingCartController, 2013/03 (change/remove cartlines) business logic parts copied from FDShoppingCartController, 2013/05 (add to cart)
 *
 * @author treer
 */
public class CartOperations {

    private static final Logger LOG = LoggerFactory.getInstance(CartOperations.class);

    // Epsilon value for comparing floating point values - as we use that for quantities a very high value is used - quantities should have a granularity of about two decimal
    // points only.
    private static final double EPSILON = 5E-3; // 0.005

    public static void populateCoremetricsShopTag(ICoremetricsResponse responseData, FDCartLineI cartLine, CoremetricsExtraData coremetricsExtraData) {
        try {

            cartLine.refreshConfiguration();

            ShopTagModel cmTag = AbstractShopTagModelBuilder.createTagModel(cartLine, cartLine.getProductRef(), false, coremetricsExtraData);
            responseData.addCoremetrics(cmTag.toStringList());

            List<String> cmFinalTag = new ArrayList<String>(1);
            cmFinalTag.add(AbstractCmShopTag.DISPLAY_SHOPS);
            responseData.addCoremetrics(cmFinalTag);

        } catch (SkipTagException ignore) {
            LOG.warn("Failed to generate coremetrics data", ignore);
        } catch (FDResourceException ignore) {
            LOG.warn("Failed to generate coremetrics data", ignore);
        } catch (FDInvalidConfigurationException ignore) {
            LOG.warn("Failed to generate coremetrics data", ignore);
        }
    }

    public static void changeQuantity(FDUserI user, FDCartModel cart, FDCartLineI cartLine, double newQ, String serverName) {

        // parameter validation
        if (user == null) {
            LOG.error("user is null");
            return;
        }
        if (cart == null) {
            LOG.error("cart is null");
            return;
        }
        if (cartLine == null) {
            LOG.error("cartLine is null");
            return;
        }

        // Fetch additional data
        ProductModel product = cartLine.lookupProduct();
        if (product == null) {
            LOG.error("Failed to get product node for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping.");
            return;
        }

        synchronized (cart) {

            // ====================
            // Quantity calculation
            // ====================

            double min = product.getQuantityMinimum();
            double max = extractMaximumQuantity(user, cartLine.getOrderLineId(), product);
            double inc = product.getQuantityIncrement();
            double oldQ = cartLine.getQuantity();

            if (newQ <= min) {
                newQ = min;
            } else {
                double totalQty = cart.getTotalQuantity(product);
                if (newQ + totalQty - oldQ > max) {
                    newQ = max - totalQty + oldQ;
                }
                newQ = Math.floor((newQ - min) / inc) * inc + min;
            }

            if (newQ <= 0) {
                // Strange, something is not right, skip
                LOG.warn("new quantity <= 0");
                return;
            }

            if (Math.abs(newQ - oldQ) < EPSILON) {
                // Quantity did not change after all, do nothing
                LOG.info("cartline quantity did not change");
                return;
            }

            // ===============
            // Modify cartline
            // ===============

            if (!(cartLine instanceof FDModifyCartLineI)) {
                // Normal cart

                cartLine.setQuantity(newQ);
                if (null == user.getMasqueradeContext() && (cart instanceof FDModifyCartModel)) {
                    cartLine.setOrderId(((FDModifyCartModel) cart).getOriginalOrder().getSale().getId());
                    updateModifiedCartlineQuantity(cartLine);
                }
                logEditCart(user, cartLine, serverName);

            } else {
                // Modify order cart

                // how much we're adding/removing
                double deltaQty = newQ - oldQ;

                if (Math.abs(deltaQty) < EPSILON) {
                    // nothing to do
                    return;

                } else if (deltaQty < 0) {
                    // need to remove some
                    cartLine.setQuantity(newQ);
                    logEditCart(user, cartLine, serverName);

                } else {
                    // deltaQty > 0, see how much can we add to this orderline

                    double origQuantity = ((FDModifyCartLineI) cartLine).getOriginalOrderLine().getQuantity();
                    double origDiff = origQuantity - oldQ;

                    if (origDiff > 0) {
                        double addToLine = Math.min(origDiff, deltaQty);
                        cartLine.setQuantity(oldQ + addToLine);
                        logEditCart(user, cartLine, serverName);
                        deltaQty -= addToLine;
                    }

                    // add a new orderline for rest of the difference, if any
                    if (deltaQty > 0) {

                        FDCartLineI newLine = cartLine.createCopy();
                        // newLine.setPricingContext( new PricingContext( user.getPricingZoneId() ) );
                        newLine.setUserContext(user.getUserContext());
                        newLine.setQuantity(deltaQty);
                        saveNewCartline(user, newLine, deltaQty, ((FDModifyCartModel) cart).getOriginalOrder().getSale().getId());

                        try {
                            OrderLineUtil.cleanup(newLine);
                        } catch (FDInvalidConfigurationException e) {
                            LOG.error("Orderline [" + newLine.getDescription() + "] configuration no longer valid", e);
                            return;
                        } catch (FDResourceException e) {
                            LOG.error("Failed to add new orderline [" + newLine.getDescription() + "]", e);
                            return;
                        }

                        cart.addOrderLine(newLine);
                        logAddToCart(user, newLine, product, serverName);
                    }
                }
            }

            saveUserAndCart(user, cart);
        }
    }

    private static void updateModifiedCartlineQuantity(FDCartLineI cartLine) {
        synchronized (cartLine) {
            try {
                FDCustomerManager.updateModifiedCartlineQuantity(cartLine);
            } catch (FDResourceException e) {
                LOG.error("could not save Modified", e);
            }
        }
    }

    private static void saveNewCartline(FDUserI user, FDCartLineI newLine, double deltaQty, String orderId) {
        if (null == user.getMasqueradeContext()) {
            synchronized (newLine) {
                try {
                    newLine.setQuantity(deltaQty);
                    FDCustomerManager.storeModifiedCartline(((FDSessionUser) user).getUser(), newLine, orderId);
                } catch (FDResourceException e) {
                    LOG.error("could not save Modified", e);
                }
            }
        }
    }

    private static void removeModifiedCartline(FDCartLineI cartLine) {
        synchronized (cartLine) {
            try {
                FDCustomerManager.removeModifiedCartline(cartLine);
            } catch (FDResourceException e) {
                LOG.error("could not save Modified", e);
            }
        }
    }

    public static void changeSalesUnit(FDUserI user, FDCartModel cart, FDCartLineI cartLine, String newSalesUnit, String serverName) {

        // parameter validation
        if (user == null) {
            LOG.error("user is null");
            return;
        }
        if (cart == null) {
            LOG.error("cart is null");
            return;
        }
        if (cartLine == null) {
            LOG.error("cartLine is null");
            return;
        }
        if (newSalesUnit == null) {
            LOG.error("newSalesUnit is null");
            return;
        }

        // Fetch additional data
        FDProduct fdProduct = cartLine.lookupFDProduct();
        if (fdProduct == null) {
            LOG.error("Failed to get fdproduct for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping.");
            return;
        }

        synchronized (cart) {

            // =================
            // Modify sales unit
            // =================

            if (!newSalesUnit.equals(cartLine.getSalesUnit())) {

                if (fdProduct.getSalesUnit(newSalesUnit) == null) {
                    LOG.error("Sales unit " + newSalesUnit + " is not valid");
                    return;
                }

                cartLine.setSalesUnit(newSalesUnit);
                logEditCart(user, cartLine, serverName);

                saveUserAndCart(user, cart);
            }
        }
    }

    public static void removeCartLine(FDUserI user, FDCartModel cart, FDCartLineI cartLine, String serverName) {

        // parameter validation
        if (user == null) {
            LOG.error("user is null");
            return;
        }
        if (cart == null) {
            LOG.error("cart is null");
            return;
        }
        if (cartLine == null) {
            LOG.error("cartLine is null");
            return;
        }

        synchronized (cart) {

            if (cartLine instanceof FDCartLineModel) {
                removeModifiedCartline(cartLine);
            }

            cart.removeOrderLine(cartLine);

            logRemoveFromCart(user, cartLine, serverName);

            saveUserAndCart(user, cart);
        }
    }

    public static void saveUserAndCart(FDUserI user, FDCartModel cart) {

        synchronized (cart) {
            try {
                cart.refreshAll(true);
            } catch (FDResourceException e) {
                LOG.error("Cart refresh failed", e);
            } catch (FDInvalidConfigurationException e) {
                LOG.error("Cart refresh failed, invalid configuration", e);
            }

            cart.sortOrderLines();
            user.updateUserState(false);

            if (!(cart instanceof FDModifyCartModel)) {
                try {
                    if (user instanceof FDUser) {
                        FDCustomerManager.storeUser((FDUser) user);
                    } else if (user instanceof FDSessionUser) {
                        FDCustomerManager.storeUser(((FDSessionUser) user).getUser());
                    }
                } catch (FDResourceException e) {
                    LOG.error("Store user (save cart) failed", e);
                }
            }
        }
    }

    private static void logEditCart(FDUserI user, FDCartLineI cartLine, String serverName) {
        FDCartLineEvent event = new FDEditCartEvent();
        event.setEventType(FDEventFactory.FD_MODIFY_CART_EVENT);
        logCartEvent(event, user, cartLine, EnumEventSource.CART, serverName);
    }

    private static void logAddToCart(FDUserI user, FDCartLineI cartLine, ProductModel product, String serverName) {
        FDCartLineEvent event = new FDAddToCartEvent();
        event.setEventType(FDEventFactory.FD_ADD_TO_CART_EVENT);
        logCartEvent(event, user, cartLine, EnumEventSource.CART, serverName);
    }

    private static void logRemoveFromCart(FDUserI user, FDCartLineI cartLine, String serverName) {
        FDCartLineEvent event = new FDRemoveCartEvent();
        event.setEventType(FDEventFactory.FD_REMOVE_CART_EVENT);
        logCartEvent(event, user, cartLine, EnumEventSource.CART, serverName);
    }

    private static void logCartEvent(FDCartLineEvent event, FDUserI user, FDCartLineI cartLine, EnumEventSource eventSource, String serverName) {
        try {

            FDIdentity identity = user.getIdentity();
            if (identity != null) {
                event.setCustomerId(identity.getErpCustomerPK());
            }

            ProductModel productModel = cartLine.lookupProduct();

            event.setServer(serverName);
            event.setCookie(user.getCookie());
            event.setTimestamp(new Date());
            event.setUrl(null);
            event.setQueryString(null);

            EnumTransactionSource src = user.getApplication();
            event.setApplication(src != null ? src.getCode() : EnumTransactionSource.WEBSITE.getCode());

            event.setCartlineId(cartLine.getCartlineId());
            if (productModel != null) {
                DepartmentModel departmentModel = productModel.getDepartment();
                if (departmentModel != null) {
                    event.setDepartment(departmentModel.getContentKey().id);
                }
            }
            event.setCategoryId(cartLine.getCategoryName());
            if (productModel != null) {
                event.setProductId(productModel.getContentKey().id);
            }
            event.setSkuCode(cartLine.getSkuCode());
            event.setQuantity(String.valueOf(cartLine.getQuantity()));
            event.setSalesUnit(cartLine.getSalesUnit());
            event.setConfiguration(cartLine.getConfigurationDesc() != null ? cartLine.getConfigurationDesc() : "");
            event.setOriginatingProduct(cartLine.getOriginatingProductId());
            event.setSource(eventSource);

            // Ymal attributes? Should we keep them? Probably not.
            // event.setYmalSet( cartLine.getYmalSetId() );
            // event.setYmalCategory( cartLine.getYmalCategoryId() );

            event.setVariantId(cartLine.getVariantId());

            // [APPDEV-3683] Duplicate value of external agency field (e.g. Foodily) into tracking code
            if (FDEventFactory.FD_ADD_TO_CART_EVENT.equals(event.getEventType()) && event.getTrackingCode() == null && cartLine.getExternalAgency() != null) {
                event.setTrackingCode(cartLine.getExternalAgency().toString());
            }

            EventLogger.getInstance().logEvent(event);
            if (FDEventFactory.FD_MODIFY_CART_EVENT.equals(event.getEventType())) {
                if (!user.getSoCartLineMessagesMap().containsKey(cartLine.getCartlineId()) &&
                		user.getCurrentStandingOrder() != null && "Y".equalsIgnoreCase(user.getCurrentStandingOrder().getActivate()))
                    user.getSoCartLineMessagesMap().put(cartLine.getCustomerListLineId(), "ModifiedItem");
            }
        } catch (Exception e) {
            LOG.error("Error while logging cart event", e);
        }
    }

    // =========================================== Methods for add to cart processing ===========================================

    public static double extractMaximumQuantity(FDUserI user, String lineId, ProductModel prodNode) {
        double maximumQuantity = 0.0;
        MasqueradeContext context = user.getMasqueradeContext();
        if (context != null && context.isMakeGood() && context.containsMakeGoodOrderLineIdQuantity(lineId)) {
            maximumQuantity = context.getMakeGoodOrderLineIdQuantity(lineId);
        } else {
            maximumQuantity = user.getQuantityMaximum(prodNode);
        }
        return maximumQuantity;
    }

}
