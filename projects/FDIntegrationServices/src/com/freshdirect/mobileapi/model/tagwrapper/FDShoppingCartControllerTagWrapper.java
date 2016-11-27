package com.freshdirect.mobileapi.model.tagwrapper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.framework.util.QueryStringBuilder;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ActionWarning;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.request.AddItemToCart;
import com.freshdirect.mobileapi.controller.data.request.AddMultipleItemsToCart;
import com.freshdirect.mobileapi.controller.data.request.SmartStoreConfiguration;
import com.freshdirect.mobileapi.controller.data.request.UpdateItemInCart;
import com.freshdirect.mobileapi.model.CartEvent;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.webapp.taglib.fdstore.FDShoppingCartControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class FDShoppingCartControllerTagWrapper extends CartEventTagWrapper {

    public static final String ACTION_REMOVE_FROM_CART = "remove";

    public static final String ACTION_ADD_TO_CART = "addToCart";

    public static final String ACTION_UPDATE_ITEM_IN_CART = "changeOrderLine";

    public static final String ACTION_ADD_MULTIPLE_TO_CART = "addMultipleToCart";

    public FDShoppingCartControllerTagWrapper(SessionUser user) {
        super(new FDShoppingCartControllerTag(), user);
    }

    /**
     * @param cartLineId
     * @param cartEvent
     * @return
     * @throws FDException
     */
    public ResultBundle updateItemInCart(UpdateItemInCart updateItemInCart, CartEvent cartEvent, FDCartLineI cartLine) throws FDException {
        setCartEventLoggingSetsAndGets(cartEvent);
        addExpectedRequestValues(new String[] { REQ_PARAM_YMAL_BOX, REQ_PARAM_YMAL_SET_ID, REQ_PARAM_YMAL_ORIG_PROD_ID,
                REQ_PARAM_YMAL_ORIG_ORDER_LINE_ID, REQ_PARAM_ATC_SUFFIX, REQ_PARAM_VARIANT, REQ_PARAM_CONSENTED, REQ_PARAM_AGREE_TO_TERMS,
                REQ_PARAM_RECIPE_ID, REQ_PARAM_CUSTOMER_CREATED_LIST_ID, "remove_from_cart.x", REQ_PARAM_CARTONNUMBER, SessionName.PARAM_ADDED_FROM_SEARCH,SessionName.PARAM_ADDED_FROM,SessionName.PARAM_EVALUATE_COUPONS,REQ_PARAM_IS_QUICKBUY,REQ_PARAM_CM_PAGEID,REQ_PARAM_CM_PAGECONTENT_HIERARCHY,REQ_PARAM_CM_VIRTUAL_CATEGORY, REQ_PARAM_CARTLINE  }, new String[] { REQ_PARAM_ATC_SUFFIX,
                REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG, SessionName.PARAM_ADDED_FROM_SEARCH, SessionName.PARAM_ADDED_FROM, SessionName.PARAM_EVALUATE_COUPONS, REQ_PARAM_CARTLINE}); //gets,sets
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER, SESSION_PARAM_SKUS_ADDED }); //gets,sets
        addRequestValue(REQ_PARAM_CART_LINE_ID, updateItemInCart.getCartLineId());

        ProductConfiguration productConfiguration = updateItemInCart.getProductConfiguration();

        //Somehow account for all the "get"s on variations. Initialize
        String skuCode = productConfiguration.getSkuCode();
        FDProduct product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
        FDVariation[] variations = product.getVariations();
        String[] variationNames = new String[variations.length];
        for (int i = 0; i < variations.length; i++) {
            FDVariation variation = variations[i];
            variationNames[i] = variation.getName();
        }
        addExpectedRequestValues(variationNames, EMPTY_ARRAY);

        addRequestValue("save_changes.x", "non-null-value");
        addRequestValue("cartLine", updateItemInCart.getCartLineId());

        String passback = productConfiguration.getPassback();
        if (passback != null) {
            handlePassbackParameters(passback);
        }

        addRequestValue(REQ_PARAM_SKU_CODE, skuCode);
        addRequestValue(REQ_PARAM_CATEGORY_ID, productConfiguration.getCategoryId());
        addRequestValue(REQ_PARAM_PRODUCT_ID, productConfiguration.getProductId());
        addRequestValue(REQ_PARAM_WINE_CATEGORY_ID, productConfiguration.getCategoryId());
        addRequestValue(REQ_PARAM_QUANTITY, productConfiguration.getQuantity());
        if(productConfiguration.getSalesUnit() != null){
        	addRequestValue(REQ_PARAM_SALES_UNIT, productConfiguration.getSalesUnit().getName());
        } else {
        	addRequestValue(REQ_PARAM_SALES_UNIT, null);
        }

        /*
        addRequestValue(REQ_PARAM_CATEGORY_ID, productConfiguration.getCategoryId());
        addRequestValue(REQ_PARAM_PRODUCT_ID, productConfiguration.getProductId());
        addRequestValue(REQ_PARAM_WINE_CATEGORY_ID, productConfiguration.getCategoryId());
        addRequestValue(REQ_PARAM_QUANTITY, productConfiguration.getQuantity());
        addRequestValue(REQ_PARAM_SALES_UNIT, productConfiguration.getSalesUnit().getName());
        */
      //Coremetrics tracking params, they do not exist in mobile implementation atm (22.07.2013)
        if(productConfiguration.getCmPageId()!=null && !productConfiguration.getCmPageId().isEmpty()){
        	addRequestValue(REQ_PARAM_CM_PAGEID, productConfiguration.getCmPageId());
        } else {
        	addRequestValue(REQ_PARAM_CM_PAGEID, "");
        }
        if(productConfiguration.getCmPageContentHeirarchy()!=null && !productConfiguration.getCmPageContentHeirarchy().isEmpty()){
        	addRequestValue(REQ_PARAM_CM_PAGECONTENT_HIERARCHY, productConfiguration.getCmPageContentHeirarchy());
        } else {
        	addRequestValue(REQ_PARAM_CM_PAGECONTENT_HIERARCHY, "");
        }
        if(productConfiguration.getCmVirtualCategory()!=null && !productConfiguration.getCmVirtualCategory().isEmpty()){
        	addRequestValue(REQ_PARAM_CM_VIRTUAL_CATEGORY, productConfiguration.getCmVirtualCategory());
        } else {
        	addRequestValue(REQ_PARAM_CM_VIRTUAL_CATEGORY, "");
        }
        if(productConfiguration.getAddedFrom()!=null && !productConfiguration.getAddedFrom().isEmpty()){
        	addRequestValue(REQ_PARAM_ADDED_FROM, productConfiguration.getAddedFrom());
        } else {
        	addRequestValue(REQ_PARAM_ADDED_FROM, "");
        }
        
        if(productConfiguration.getVariantId()!=null && !productConfiguration.getVariantId().isEmpty()){
        	addRequestValue(REQ_PARAM_VARIANT_ID, productConfiguration.getVariantId());
        } else {
        	addRequestValue(REQ_PARAM_VARIANT_ID, "");
        }
        if(productConfiguration.getSavingsId()!=null && !productConfiguration.getSavingsId().isEmpty()){
        	addRequestValue(REQ_PARAM_SAVINGS_ID, productConfiguration.getSavingsId());
        } else {
        	addRequestValue(REQ_PARAM_SAVINGS_ID, "");
        }
        
        
        
        
        if(cartLine != null)
        	addRequestValue(REQ_PARAM_REQUEST_NOTIFICATION, cartLine.isRequestNotification());

        //Add all the configuration options
        if (null != productConfiguration.getOptions()) {
            addRequestValues(productConfiguration.getOptions());
        }

        ((FDShoppingCartControllerTag) getWrapTarget()).setAction(ACTION_UPDATE_ITEM_IN_CART);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);

    }

    public ResultBundle refreshDeliveryPass() throws FDException {
        addExpectedRequestValues(new String[] { REQ_PARAM_CUSTOMER_CREATED_LIST_ID, REQ_PARAM_REMOVE, REQ_PARAM_REMOVE_RECIPE,SessionName.PARAM_EVALUATE_COUPONS, REQ_PARAM_CARTLINE },
                new String[] { REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG, REQ_PARAM_CARTLINE }); //gets,sets
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER, SESSION_PARAM_SKUS_ADDED }); //gets,sets
        ((FDShoppingCartControllerTag) getWrapTarget()).setAction(null);
        return new ResultBundle(executeTagLogic(), this);
    }

    /**
     * @param cartLineId
     * @return
     * @throws FDException
     */
    public ResultBundle removeItemFromCart(String cartLineId, CartEvent cartEvent) throws FDException {
        setCartEventLoggingSetsAndGets(cartEvent);
        addExpectedRequestValues(new String[] { REQ_PARAM_CUSTOMER_CREATED_LIST_ID, SessionName.PARAM_EVALUATE_COUPONS,REQ_PARAM_CM_PAGEID,REQ_PARAM_CM_PAGECONTENT_HIERARCHY,REQ_PARAM_CM_VIRTUAL_CATEGORY, REQ_PARAM_CARTLINE  },
                new String[] { REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG, REQ_PARAM_CARTLINE }); //gets,sets
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER, SESSION_PARAM_SKUS_ADDED }); //gets,sets
        addRequestValue(REQ_PARAM_CART_LINE_ID, cartLineId);

        /*
         * For "remove", it's a GET request and "action" parameter is not used.
         * Instead existence of "remove" parameter is check against the request object
         */
        //((FDShoppingCartControllerTag) getWrapTarget()).setAction(ACTION_REMOVE_FROM_CART);
        // As long as a parameter w/ key "remove" exists in the request w/ a non-null value, 
        // it's enough to trick the tag controller.
        addRequestValue(ACTION_REMOVE_FROM_CART, "some-not-null-value");

        return new ResultBundle(executeTagLogic(), this);
    }

    /**
     * @param addItemToCart
     * @param cartEvent
     * @return
     * @throws FDException
     */
    public ResultBundle addItemToCart(AddItemToCart addItemToCart, CartEvent cartEvent) throws FDException {

        setCartEventLoggingSetsAndGets(cartEvent);

        //"ymal_box"
        //ymalSetId
        addExpectedRequestValues(new String[] { REQ_PARAM_VARIANT, REQ_PARAM_YMAL_BOX, REQ_PARAM_YMAL_SET_ID, REQ_PARAM_YMAL_ORIG_PROD_ID,
                REQ_PARAM_YMAL_ORIG_ORDER_LINE_ID, REQ_PARAM_ATC_SUFFIX, REQ_PARAM_CUSTOMER_CREATED_LIST_ID, REQ_PARAM_CARTONNUMBER,SessionName.PARAM_EVALUATE_COUPONS,REQ_PARAM_CM_PAGEID,REQ_PARAM_CM_PAGECONTENT_HIERARCHY,REQ_PARAM_CM_VIRTUAL_CATEGORY, REQ_PARAM_ADDED_FROM,
                REQ_PARAM_VARIANT_ID, REQ_PARAM_SAVINGS_ID, REQ_PARAM_CARTLINE}, new String[] {
                REQ_PARAM_ATC_SUFFIX, REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG, SessionName.PARAM_EVALUATE_COUPONS, REQ_PARAM_CARTLINE }); //gets,sets
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER, SESSION_PARAM_SKUS_ADDED,
                REQ_PARAM_ATC_SUFFIX }); //gets,sets

        ProductConfiguration productConfiguration = addItemToCart.getProductConfiguration();
        SmartStoreConfiguration smartStoreConfiguration = addItemToCart.getSmartStoreConfiguration();

        //Somehow account for all the "get"s on variations. Initialize
        String skuCode = productConfiguration.getSkuCode();
        FDProduct product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
        FDVariation[] variations = product.getVariations();
        String[] variationNames = new String[variations.length];
        for (int i = 0; i < variations.length; i++) {
            FDVariation variation = variations[i];
            variationNames[i] = variation.getName();
        }
        addExpectedRequestValues(variationNames, EMPTY_ARRAY);

        addRequestValue(REQ_PARAM_SKU_CODE, skuCode);
        addRequestValue(REQ_PARAM_CATEGORY_ID, productConfiguration.getCategoryId());
        addRequestValue(REQ_PARAM_PRODUCT_ID, productConfiguration.getProductId());
        addRequestValue(REQ_PARAM_WINE_CATEGORY_ID, productConfiguration.getCategoryId());
        addRequestValue(REQ_PARAM_QUANTITY, productConfiguration.getQuantity());
        addRequestValue(REQ_PARAM_SALES_UNIT, productConfiguration.getSalesUnit().getName());
        addRequestValue(REQ_PARAM_CONSENTED, null); //Looks like this isn't being "set" anywhere. 
        addRequestValue(REQ_PARAM_AGREE_TO_TERMS, addItemToCart.getAgreeToTerms()); //
        addRequestValue(REQ_PARAM_RECIPE_ID, addItemToCart.getRecipeId());
        addRequestValue(REQ_PARAM_IS_QUICKBUY, addItemToCart.isQuickBuy());
        
        //Coremetrics tracking params, they do not exist in mobile implementation atm (22.07.2013)
        if(productConfiguration.getCmPageId()!=null && !productConfiguration.getCmPageId().isEmpty()){
        	addRequestValue(REQ_PARAM_CM_PAGEID, productConfiguration.getCmPageId());
        } else {
        	addRequestValue(REQ_PARAM_CM_PAGEID, "");
        }
        if(productConfiguration.getCmPageContentHeirarchy()!=null && !productConfiguration.getCmPageContentHeirarchy().isEmpty()){
        	addRequestValue(REQ_PARAM_CM_PAGECONTENT_HIERARCHY, productConfiguration.getCmPageContentHeirarchy());
        } else {
        	addRequestValue(REQ_PARAM_CM_PAGECONTENT_HIERARCHY, "");
        }
        if(productConfiguration.getCmVirtualCategory()!=null && !productConfiguration.getCmVirtualCategory().isEmpty()){
        	addRequestValue(REQ_PARAM_CM_VIRTUAL_CATEGORY, productConfiguration.getCmVirtualCategory());
        } else {
        	addRequestValue(REQ_PARAM_CM_VIRTUAL_CATEGORY, "");
        }
        if(productConfiguration.getAddedFrom()!=null && !productConfiguration.getAddedFrom().isEmpty()){
        	addRequestValue(REQ_PARAM_ADDED_FROM, productConfiguration.getAddedFrom());
        } else {
        	addRequestValue(REQ_PARAM_ADDED_FROM, "");
        }
        
        if(productConfiguration.getVariantId()!=null && !productConfiguration.getVariantId().isEmpty()){
        	addRequestValue(REQ_PARAM_VARIANT_ID, productConfiguration.getVariantId());
        } else {
        	addRequestValue(REQ_PARAM_VARIANT_ID, "");
        }
        if(productConfiguration.getSavingsId()!=null && !productConfiguration.getSavingsId().isEmpty()){
        	addRequestValue(REQ_PARAM_SAVINGS_ID, productConfiguration.getSavingsId());
        } else {
        	addRequestValue(REQ_PARAM_SAVINGS_ID, "");
        }
        
        
        
        /*
         * Adding external Group external Source and external agency
         */
        if(productConfiguration.getExternalAgency()!=null){
        	addRequestValue(REQ_PARAM_EXTERNAL_AGENCY, productConfiguration.getExternalAgency().name());
        	
        } else {
        	addRequestValue(REQ_PARAM_EXTERNAL_AGENCY, null);
        }
        if(productConfiguration.getExternalGroup()!=null && !productConfiguration.getExternalGroup().isEmpty()){
        	addRequestValue(REQ_PARAM_EXTERNAL_GROUP, productConfiguration.getExternalGroup());
        } else {
        	addRequestValue(REQ_PARAM_EXTERNAL_GROUP, null);
        }
        if(productConfiguration.getExternalSource()!=null && !productConfiguration.getExternalSource().isEmpty()){
        	addRequestValue(REQ_PARAM_EXTERNAL_SOURCE, productConfiguration.getExternalSource());
        } else {
        	addRequestValue(REQ_PARAM_EXTERNAL_SOURCE, null);
        }
        
        
        
        

        //Pass all smart store configuration values:
        String parameterBundle = smartStoreConfiguration.getParameterBundle();
        if (parameterBundle != null) {
            handlePassbackParameters(parameterBundle);
        }
        String passback = productConfiguration.getPassback();
        if (passback != null) {
            handlePassbackParameters(passback);
        }

        //addRequestValue(REQ_PARAM_VARIANT, smartStoreConfiguration.getVariant()); //Value from smart store

        //Indicates users has checked "Email a copy of this recipe on the day of delivery!"
        addRequestValue(REQ_PARAM_REQUEST_NOTIFICATION, addItemToCart.getRequestNotification());

        //Add all the configuration options
        if (null != productConfiguration.getOptions()) {
            addRequestValues(productConfiguration.getOptions());
        }

        ((FDShoppingCartControllerTag) getWrapTarget()).setAction(ACTION_ADD_TO_CART);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    private void handlePassbackParameters(String parameterBundle) throws FDException {
        if (parameterBundle != null) {
            parameterBundle = parameterBundle.substring(parameterBundle.indexOf("?") + 1);
            try {
                parameterBundle = StringEscapeUtils.unescapeHtml(URLDecoder.decode(parameterBundle, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new FDException(e, "UnsupportedEncodingException encountered while trying to decode parameterBundle"
                        + parameterBundle);
            }
            String[] params = parameterBundle.split("&");
            for (String param : params) {
                String[] nameValue = param.split("=");
                if (nameValue.length > 1) {
                    addRequestValue(nameValue[0], nameValue[1]);
                } else {
                    addRequestValue(nameValue[0], "");
                }
            }
        }
    }

    /**
     * 
     * @param multipleItemsToCart
     * @param cartEvent
     * @return
     * @throws FDException
     */
    public ResultBundle addMultipleItemsToCart(AddMultipleItemsToCart multipleItemsToCart, CartEvent cartEvent) throws FDException {
        addExpectedRequestValues(new String[] { REQ_PARAM_REMOVE, REQ_PARAM_REMOVE_RECIPE, REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG,
                REQ_PARAM_CATEGORY_ID, REQ_PARAM_YMAL_BOX, REQ_PARAM_IMPRESSESION_ID, REQ_PARAM_ATC_SUFFIX,
                REQ_PARAM_CUSTOMER_CREATED_LIST_ID, REQ_PARAM_CARTONNUMBER, SessionName.PARAM_ADDED_FROM_SEARCH,SessionName.PARAM_ADDED_FROM,SessionName.PARAM_EVALUATE_COUPONS,REQ_PARAM_CM_PAGEID,REQ_PARAM_CM_PAGECONTENT_HIERARCHY,REQ_PARAM_CM_VIRTUAL_CATEGORY,
                REQ_PARAM_VARIANT_ID, REQ_PARAM_SAVINGS_ID, REQ_PARAM_CARTLINE}, new String[] { REQ_PARAM_FD_ACTION, REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG,
                REQ_PARAM_ATC_SUFFIX, SessionName.PARAM_ADDED_FROM_SEARCH, SessionName.PARAM_ADDED_FROM, SessionName.PARAM_EVALUATE_COUPONS, REQ_PARAM_CARTLINE  }); //gets,sets
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_SKUS_ADDED }); //gets,sets

        addRequestValue(REQ_PARAM_FD_ACTION, "addMultipleToCart");
        addRequestValue(REQ_PARAM_SOURCE, "Quickshop");
        addRequestValue(REQ_PARAM_ITEM_COUNT, multipleItemsToCart.getProductsConfiguration().size());
        addRequestValue(REQ_PARAM_IS_QUICKBUY, multipleItemsToCart.isQuickBuy());

        List<ProductConfiguration> pcs = multipleItemsToCart.getProductsConfiguration();
        int index =0;
        for(ProductConfiguration productConfiguration : pcs){
        	String skuCode = productConfiguration.getSkuCode();
        	FDProduct product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
        	FDVariation[] variations = product.getVariations();
        	String[] variationNames = new String[variations.length];
        	for (int i = 0; i < variations.length; i++) {
        		FDVariation variation = variations[i];
        		variationNames[i] = variation.getName()+ "_" + index;
        	}
        	addExpectedRequestValues(variationNames, EMPTY_ARRAY);
        	index++;
        }
        for (int idx = 0; idx < pcs.size(); idx++) {
            ProductConfiguration pc = pcs.get(idx);
            addRequestValue(REQ_PARAM_SKU_CODE + "_" + idx, pc.getSkuCode());
            addRequestValue(REQ_PARAM_CATEGORY_ID + "_" + idx, pc.getCategoryId());
            addRequestValue(REQ_PARAM_WINE_CATEGORY_ID + "_" + idx, pc.getCategoryId());
            addRequestValue(REQ_PARAM_PRODUCT_ID + "_" + idx, pc.getProductId());
            addRequestValue(REQ_PARAM_QUANTITY + "_" + idx, pc.getQuantity());
            
            if (pc.getSalesUnit() != null) {
                addRequestValue(REQ_PARAM_SALES_UNIT + "_" + idx, pc.getSalesUnit().getName());

            } else {
                addRequestValue(REQ_PARAM_SALES_UNIT + "_" + idx, null);
            }
            
          //Coremetrics tracking params, they do not exist in mobile implementation atm (22.07.2013)
            if(pc.getCmPageId()!=null && !pc.getCmPageId().isEmpty()){
            	addRequestValue(REQ_PARAM_CM_PAGEID, pc.getCmPageId());
            } else {
            	addRequestValue(REQ_PARAM_CM_PAGEID, "");
            }
            if(pc.getCmPageContentHeirarchy()!=null && !pc.getCmPageContentHeirarchy().isEmpty()){
            	addRequestValue(REQ_PARAM_CM_PAGECONTENT_HIERARCHY, pc.getCmPageContentHeirarchy());
            } else {
            	addRequestValue(REQ_PARAM_CM_PAGECONTENT_HIERARCHY, "");
            }
            if(pc.getCmVirtualCategory()!=null && !pc.getCmVirtualCategory().isEmpty()){
            	addRequestValue(REQ_PARAM_CM_VIRTUAL_CATEGORY, pc.getCmVirtualCategory());
            } else {
            	addRequestValue(REQ_PARAM_CM_VIRTUAL_CATEGORY, "");
            }
            if(pc.getAddedFrom()!=null && !pc.getAddedFrom().isEmpty()){
            	addRequestValue(REQ_PARAM_ADDED_FROM, pc.getAddedFrom());
            } else {
            	addRequestValue(REQ_PARAM_ADDED_FROM, "");
            }
            
            if(pc.getVariantId()!=null && !pc.getVariantId().isEmpty()){
            	addRequestValue(REQ_PARAM_VARIANT_ID, pc.getVariantId());
            } else {
            	addRequestValue(REQ_PARAM_VARIANT_ID, "");
            }
            if(pc.getSavingsId()!=null && !pc.getSavingsId().isEmpty()){
            	addRequestValue(REQ_PARAM_SAVINGS_ID, pc.getSavingsId());
            } else {
            	addRequestValue(REQ_PARAM_SAVINGS_ID, "");
            }
            
            /*
             * Adding external Group external Source and external agency
             */
            if(pc.getExternalAgency()!=null){
            	addRequestValue(REQ_PARAM_EXTERNAL_AGENCY, pc.getExternalAgency().name());
            	
            } else {
            	addRequestValue(REQ_PARAM_EXTERNAL_AGENCY, null);
            }
            if(pc.getExternalGroup()!=null && !pc.getExternalGroup().isEmpty()){
            	addRequestValue(REQ_PARAM_EXTERNAL_GROUP, pc.getExternalGroup());
            } else {
            	addRequestValue(REQ_PARAM_EXTERNAL_GROUP, null);
            }
            if(pc.getExternalSource()!=null && !pc.getExternalSource().isEmpty()){
            	addRequestValue(REQ_PARAM_EXTERNAL_SOURCE, pc.getExternalSource());
            } else {
            	addRequestValue(REQ_PARAM_EXTERNAL_SOURCE, null);
            }
            /*
             * The following values are "expected" by the wrapper but not required as in i_vieworder.jspf. But as they are associated
             * to idx that can't be added on expected values as part of the array.
             * The easier way is to add them during the products iteration.
             */
            addRequestValue(REQ_PARAM_VARIANT + "_" + idx, null);
            addRequestValue(REQ_PARAM_CONSENTED + "_" + idx, null);
            addRequestValue(REQ_PARAM_RECIPE_ID + "_" + idx, null);
            addRequestValue(REQ_PARAM_YMAL_SET_ID + "_" + idx, null);
            addRequestValue(REQ_PARAM_YMAL_ORIG_PROD_ID + "_" + idx, null);
            addRequestValue(REQ_PARAM_YMAL_ORIG_ORDER_LINE_ID + "_" + idx, null);
            addRequestValue(REQ_PARAM_TRACKING_CODE + "_" + idx, null);
            addRequestValue(REQ_PARAM_TRACKING_CODE_EXT + "_" + idx, null);
            addRequestValue(REQ_PARAM_IMPRESSESION_ID + "_" + idx, null);
            

            if (pc.getOptions() != null) {
                Iterator<String> keys = pc.getOptions().keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = pc.getOptions().get(key);
                    addRequestValue(key + "_" + idx, value);
                }
            }
            
            String passback = pc.getPassback();
            if (passback != null) {
                handlePassbackParameters(passback);
            }

        }

        setMethodMode(true);
        ((FDShoppingCartControllerTag) getWrapTarget()).setAction(ACTION_ADD_MULTIPLE_TO_CART);
        return new ResultBundle(executeTagLogic(), this);
    }

    /**
     * Removes multiple items from cart using {@see FDShoppingCartControllerTagWrapper#removeItemFromCart(String, CartEvent)} method per id
     * @param cartLineIds
     * @param cartEvent
     * @return
     * @throws FDException
     */
    public ResultBundle removeMultipleItemsFromCart(List<String> cartLineIds, CartEvent cartEvent) throws FDException {
    	
    	addExpectedRequestValues(new String[] { REQ_PARAM_CARTLINE } , new String[] {REQ_PARAM_CARTLINE} );
        ActionResult actionResult = new ActionResult();
        for (String cartLineId : cartLineIds) {
            ResultBundle rmResult = this.removeItemFromCart(cartLineId, cartEvent);

            for (Object error : rmResult.getActionResult().getErrors()) {
                actionResult.addError((ActionError) error);
            }

            for (Object warning : rmResult.getActionResult().getWarnings()) {
                actionResult.addWarning((ActionWarning) warning);
            }

            // TODO This values are set in the pageContext (PageContextWrapper, which add them to request). See how to fix this.
            this.addRequestValue(ACTION_RESULT, null);
            this.addRequestValue("cart", null);
            this.addRequestValue("getResult", null);
            this.addRequestValue(REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG, null);
        }

        return new ResultBundle(actionResult, this);
    }

    @Override
    protected void setResult() {
        ((FDShoppingCartControllerTag) wrapTarget).setResult(ACTION_RESULT);
    }

}
