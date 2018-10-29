package com.freshdirect.webapp.ajax.expresscheckout.cart.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.FDCouponProperties;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.EnumCouponDisplayStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.promotion.FDMinDCPDTotalPromoData;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.analytics.service.GoogleAnalyticsDataService;
import com.freshdirect.webapp.ajax.cart.CartOperations;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.BillingReferenceInfo;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.Section;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.SectionInfo;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartRequestData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartSubTotalFieldData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.ItemCount;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.ModifyCartData;
import com.freshdirect.webapp.ajax.expresscheckout.csr.service.CustomerServiceRepresentativeService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.gogreen.service.GoGreenService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FDCartModelService;
import com.freshdirect.webapp.ajax.holidaymealbundle.service.HolidayMealBundleService;
import com.freshdirect.webapp.ajax.mealkit.service.MealkitService;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.reorder.service.QuickShopCarouselService;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.taglib.callcenter.ComplaintUtil;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.ShoppingCartUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class CartDataService {

    private static final String USER_CORPORATE_JSON_KEY = "userCorporate";
    private static final String USER_RECOGNIZED_JSON_KEY = "userRecognized";
    private static final String SUB_TOTAL_BOX_JSON_KEY = "subTotalBox";
    private static final String ESTIMATED_TOTAL_BOX_JSON_KEY = "estimatedTotalBox";
    private static final String REDIRECT_URL_JSON_KEY = "redirectUrl";
    private static final String VIEW_CART_HEADER_MESSAGE_JSON_KEY = "viewCartHeaderMessage";
    private static final String WARNING_MESSAGE_JSON_KEY = "warningMessage";
    private static final String RECIPE_PREFIX_PATTERN = "\\s*(R|r)ecipe\\s*:\\s*";

    private static final Logger LOG = LoggerFactory.getInstance(CartDataService.class);

    private static final CartDataService INSTANCE = new CartDataService();

    public static CartDataService defaultService() {
        return INSTANCE;
    }

    private CartDataService() {
    }

    /**
     * Update and load shopping cart.
     * 
     * @param request
     * @param response
     * @param user
     * @return
     * @throws HttpErrorResponse
     * @throws FDResourceException
     * @throws JspException
     */
    public CartData loadCartData(HttpServletRequest request, FDUserI user) throws HttpErrorResponse, FDResourceException, JspException {
    	return loadCartData(request, user, true);
    }
    public CartData loadCartData(HttpServletRequest request, FDUserI user, boolean hasSession) throws HttpErrorResponse, FDResourceException, JspException {
        String userId = loadUser(user);
        if (hasSession) {
        updateUserAndCart(request, user);
        }
        FDCartModel cart = loadUserShoppingCart(request, user, userId);
        if (user.getIdentity() != null) {
            FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity());
        }

        if (hasSession && StandingOrderHelper.isSO3StandingOrder(user)) {
            FDStandingOrder so = user.getCurrentStandingOrder();
            cart.setTip(so.getTipAmount());
        }
        CartData cartData = new CartData();
        synchronized (cart) {
            populateCartData(user, request, userId, cart, cartData, hasSession);
        }
        return cartData;
    }

    public Map<String, Object> loadCartDataSubTotalBox(HttpServletRequest request, FDUserI user) throws HttpErrorResponse, FDResourceException, JspException {
        String userId = loadUser(user);
        updateUserAndCart(request, user);
        FDCartModel cart = loadUserShoppingCart(request, user, userId);
        if (user.getIdentity() != null) {
            FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity());
        }
        CartData cartData = new CartData();
        synchronized (cart) {
            populateSubTotalBox(cartData, cart, user);
        }
        return cartData.getSubTotalBox();
    }

    public CartData loadCartSuccessData(HttpServletRequest request, FDUserI user, String orderId) throws HttpErrorResponse, FDResourceException {
        String userId = loadUser(user);
        FDOrderI order = FDCustomerManager.getOrder(user.getIdentity(), orderId);
        CartData cartData = new CartData();
        synchronized (order) {

            // Fetch recent cartline ids
            Set<Long> recentIds = ((FDSessionUser) user).getRecentCartlineIdsSet(orderId);
            
            populateOrderData(user, request, userId, order, cartData, recentIds);
        }
        GoogleAnalyticsDataService.defaultService().populateCheckoutSuccessGAData(cartData, order, request.getSession());

        return cartData;
    }

    public CartData updateCartData(HttpServletRequest request, FDUserI user) throws HttpErrorResponse, FDResourceException, JspException {
        String userId = loadUser(user);
        FDCartModel cart = loadUserShoppingCart(request, user, userId);
        // cart.setTip(5.00);
        CartData cartData = new CartData();
        synchronized (cart) {
            updateCart(request, user, userId, cart, cartData);
            updateUserAndCart(request, user);
            populateCartData(user, request, userId, cart, cartData, true);
            if (StandingOrderHelper.isSO3StandingOrder(user)) {
                final HttpSession session = request.getSession();
                FDActionInfo info = AccountActivityUtil.getActionInfo(session);
                if (cart.getTotal() >= ErpServicesProperties.getStandingOrderSoftLimit()) {
                    StandingOrderHelper.clearSO3ErrorDetails(user.getCurrentStandingOrder(), new String[] { "MINORDER", "TIMESLOT_MINORDER" });
                    cartData.setDeliveryBegins(StandingOrderHelper.getDeliveryBeginsInfo(user));
                }
                FDStandingOrdersManager.getInstance().manageStandingOrder(info, cart, user.getCurrentStandingOrder(), null);
                user.setRefreshSO3(true);
            }
        }
        return cartData;
    }

    public void validateCarouselData(HttpServletRequest request, FDUserI user, FormDataResponse response) throws FDResourceException {
        if (!response.getSubmitForm().isSuccess()) {
            SessionInput input = QuickShopCarouselService.defaultService().createSessionInput(user, request);
            input.setError(true);
            response.getSubmitForm().getResult().put("carouselData",
                    ViewCartCarouselService.getDefaultService().populateTabsRecommendationsAndCarousel(request, (FDSessionUser) user, input));
        }
    }
    
    public void validateOrderMinimumOnStartCheckout(FDUserI user, FormDataResponse response) throws FDResourceException {
        String orderMinimumWarningMessageKey = AvailabilityService.defaultService().selectWarningType(user);
        if (orderMinimumWarningMessageKey != null) {
            Map<String, Object> headerMessageMap = new HashMap<String, Object>();
            headerMessageMap.put(WARNING_MESSAGE_JSON_KEY, AvailabilityService.defaultService().translateWarningMessage(orderMinimumWarningMessageKey, user));
            response.getSubmitForm().getResult().put(VIEW_CART_HEADER_MESSAGE_JSON_KEY, headerMessageMap);
            response.getSubmitForm().setSuccess(false);
        } else {
            response.getSubmitForm().getResult().put(REDIRECT_URL_JSON_KEY, "/expressco/checkout.jsp");
        }
    }

    private CartData.Quantity cartLineSoldByQuantity(FDCartLineI cartLine, ProductModel productNode, FDUserI user, CartData.Item item) {
        CartData.Quantity q = new CartData.Quantity();
        q.setQuantity(cartLine.getQuantity());
        q.setqMin(productNode.getQuantityMinimum());
        q.setqMax(user.getQuantityMaximum(productNode));
        q.setqInc(productNode.getQuantityIncrement());
        item.setQu(q);
        return q;
    }

    private void cartLineSoldBySaleUnit(FDCartLineI cartLine, FDProduct fdProduct, CartData.Item item) {
        List<CartData.SalesUnit> salesUnits = new ArrayList<CartData.SalesUnit>();
        String cartlineSaleUnit = cartLine.getSalesUnit();
        for (FDSalesUnit fdSaleUnit : fdProduct.getSalesUnits()) {
            CartData.SalesUnit cartDataSaleUnit = new CartData.SalesUnit();
            String fdSaleUnitId = fdSaleUnit.getName();
            cartDataSaleUnit.setId(fdSaleUnitId);
            cartDataSaleUnit.setName(fdSaleUnit.getDescription());
            cartDataSaleUnit.setSelected(fdSaleUnitId.equals(cartlineSaleUnit));
            salesUnits.add(cartDataSaleUnit);
        }
        item.setSu(salesUnits);
    }

    public void checkCartCleanUpAction(HttpServletRequest request, CartData cartData) {
        Boolean cartChangedByCleanUp = (Boolean) request.getSession().getAttribute(FDShoppingCartService.CART_CHANGED_BY_CLEAN_UP_SESSION_ATTRIBUTE_ID);
        if (cartChangedByCleanUp != null) {
            cartData.setCartChangedByCleanUp(cartChangedByCleanUp);
            request.getSession().removeAttribute(FDShoppingCartService.CART_CHANGED_BY_CLEAN_UP_SESSION_ATTRIBUTE_ID);
        }
    }

    private ModifyCartData isModifyOrderMode(HttpSession session, FDCartI loadedCart) {
        FDUserI mUser = (FDUserI) session.getAttribute(SessionName.USER);
        FDCartModel mCart = null !=mUser ? mUser.getShoppingCart() : null;
        ModifyCartData modifyCartData = new ModifyCartData();
        Calendar cal = Calendar.getInstance();
        Date cutoffTime = null;
        Date weekFromOrderDate = null;
        
        if (null != loadedCart && !(loadedCart instanceof FDModifyCartModel) && !(mCart.isDlvPassStandAloneCheckoutAllowed() && mCart.containsDlvPassOnly())) {
        	//set the cutoff time so we can use it on receipt
        	if (loadedCart.getDeliveryReservation() != null && loadedCart.getDeliveryReservation().getTimeslot() !=null) {
        		cutoffTime = loadedCart.getDeliveryReservation().getCutoffTime();
        	}
        }
        
        if (null !=mCart && mCart instanceof FDModifyCartModel) {
            modifyCartData.setModifyOrderEnabled(true);
            FDModifyCartModel modifyCart = (FDModifyCartModel) mCart;
            String orderId = modifyCart.getOriginalOrder().getErpSalesId();
            session.setAttribute("MODIFIED" + orderId, orderId);
            modifyCartData.setOrderId(orderId);
            try {
                FDOrderI order = FDCustomerManager.getOrder(orderId);
                modifyCartData.setSoName(order.getStandingOrderName());
                modifyCartData.setSoOrderDate(order.getSODeliveryDate());
            } catch (FDResourceException e) {
                LOG.error("eRROR while retreiving order details order id" + orderId);
            }
            mCart.setTransactionSource(null);
            
            cal.setTime(modifyCart.getOriginalOrder().getDatePlaced());
            cutoffTime = modifyCart.getOriginalOrder().getDeliveryReservation().getCutoffTime();
        }
        
        //bump a week
        cal.add(Calendar.DAY_OF_MONTH, 8);
        weekFromOrderDate = cal.getTime();
        modifyCartData.setOneWeekLater(printDate(weekFromOrderDate));
        
        cal.add(Calendar.DAY_OF_MONTH, -1); /* 7 days */
        Date weekFromOrderDateCutoffCheck = cal.getTime();
        
        if (cutoffTime != null) {
            cutoffTime = ShoppingCartUtil.getCutoffByContext(cutoffTime, mUser);
            //check if cutoff is more than a week out, changing format if needed
            if ( cutoffTime.after(weekFromOrderDateCutoffCheck) ) {
           	 	modifyCartData.setCutoffTime(new SimpleDateFormat("M/d, ha").format(cutoffTime));
            } else {
            	 modifyCartData.setCutoffTime(new SimpleDateFormat("EEEEE, ha").format(cutoffTime));
            }
        }
        
        return modifyCartData;
    }

    private List<FDCartLineI> loadCartOrderLines(String userId, FDCartI cart) throws HttpErrorResponse {
        List<FDCartLineI> cartLines = cart.getOrderLines();
        if (cartLines == null) {
            LOG.error("Orderlines in cart is a null object. Aborting. User:" + userId);
            BaseJsonServlet.returnHttpError(500, "Orderlines in cart is a null object. Aborting.");
        } else {
            cartLines = new ArrayList<FDCartLineI>(cartLines);
        }
        return cartLines;
    }

    public List<CartData.Item> loadDepartmentSectionList(Map<SectionInfo, List<CartData.Item>> sectionMap, SectionInfo info) {
        List<CartData.Item> sectionList = sectionMap.get(info);
        if (sectionList == null) {
            sectionList = new ArrayList<CartData.Item>();
            sectionMap.put(info, sectionList);
        }
        return sectionList;
    }

    public void loadSectionHeaderImage(Map<String, String> sectionHeaderImgMap, ProductModel productNode, String deptDesc) {
        if (!sectionHeaderImgMap.containsKey(deptDesc)) {
            String deptId = productNode.getDepartment().getContentName();
            if (null != deptDesc && deptDesc.startsWith("Recipe: ")) {
                deptId = "rec";
            }
            String imgUrl = "/media_stat/images/layout/department_headers/" + deptId + "_cart.gif";
            sectionHeaderImgMap.put(deptDesc, imgUrl);
        }
    }

    private String loadUser(FDUserI user) {
        String userId = user.getUserId();
        if (userId == null || userId.trim().isEmpty()) {
            userId = "[UNIDENTIFIED-USER]";
        }
        return userId;
    }

    private FDCartModel loadUserShoppingCart(HttpServletRequest request, FDUserI user, String userId) throws HttpErrorResponse {
        //FDCartModel cart = StandingOrderHelper.isSO3StandingOrder(user) ? user.getSoTemplateCart() : user.getShoppingCart();
    	boolean dlvPassCart = null !=request.getParameter("dlvPassCart") && "true".equalsIgnoreCase(request.getParameter("dlvPassCart")) ? true: false;
    	if(!dlvPassCart) {
    		user.getDlvPassCart().clearOrderLines();
    	}
        FDCartModel cart = UserUtil.getCart(user, "", dlvPassCart);

        if (cart == null) {
            LOG.error("No cart found for user " + userId);
            BaseJsonServlet.returnHttpError(500, "No cart found for user " + userId);
        }
        return cart;
    }

    private void populateCartData(FDUserI user, HttpServletRequest request, String userId, FDCartModel cart, CartData cartData, boolean hasSession) throws HttpErrorResponse {
        populateCartOrderData(user, request, userId, cart, cartData, populateRecentIds(cart), hasSession);
        
        }

    public CartData.Item populateCartDataItem(FDCartLineI cartLine, FDProduct fdProduct, ItemCount itemCount, FDCartI cart, Set<Long> recentIds, ProductModel productNode,
            FDUserI user) {

        CartData.Item item = populateCartDataItemByCartLine(user, cartLine, cart, recentIds);
//        populateCartDataItemWithUnitPriceAndQuantity(item, fdProduct, productNode.getPriceCalculator());
        String[] cartDataItem = cartLine.getUnitPrice().split("/");
		if (null != cartDataItem && cartDataItem.length > 1) {
			if (cartDataItem[0].contains("$")) {
				item.setUnitPrice(cartDataItem[0].substring(1));
			} else {
				item.setUnitPrice(cartDataItem[0]);
			}
			item.setUnitScale(cartDataItem[1]);
		}
        item.setMealBundle(isMealBundle(productNode));
        item.setImage((productNode.getProdImage() == null) ? "" : productNode.getProdImage().getPathWithPublishId()); // APPDEV-6014
        item.setProductId(productNode.getContentKey().getId());
        item.setCategoryId(productNode.getCategory().getContentKey().getId());

        if (cartLine.isSoldBySalesUnits()) {
            cartLineSoldBySaleUnit(cartLine, fdProduct, item);
            itemCount.setValue(itemCount.getValue() + 1);
        } else {
            CartData.Quantity q = cartLineSoldByQuantity(cartLine, productNode, user, item);
            itemCount.setValue(itemCount.getValue() + q.getQuantity());
        }

        return item;
    }

    public boolean isMealBundle(ProductModel productModel) {
        return HolidayMealBundleService.defaultService().isProductModelLayoutTypeHolidayMealBundle(productModel)
                || MealkitService.defaultService().isProductModelLayoutTypeMealkit(productModel);
    }

    private CartData.Item populateCartDataItemByCartLine(FDUserI user, FDCartLineI cartLine, FDCartI cart, Set<Long> recentIds) {
        CartData.Item item = new CartData.Item();
        int randomId = cartLine.getRandomId();
        long cartlineId = Long.valueOf(cartLine.getCartlineId());
        item.setId(randomId);
        item.setRecent(recentIds.contains(Long.valueOf(randomId)));
        item.setNewItem(((cart instanceof FDModifyCartModel) && !(cartLine instanceof FDModifyCartLineI)) || recentIds.contains(cartlineId));
        item.setPrice(JspMethods.formatPrice(cartLine.getPrice()));
        item.setDescr(cartLine.getDescription());
        item.setDescrAlt(cartLine.getDescription());
        item.setConfDescr(cartLine.getConfigurationDesc());
        item.setConfOptions(cartLine.getConfiguration().getOptions());
        Discount tempDisc = cartLine.getDiscount();
        if (tempDisc != null && EnumDiscountType.FREE.equals(tempDisc.getDiscountType())) {
            item.setFreeSamplePromoProduct(true);
        }
        CartData.Item.Discount discount = populateDiscount(cartLine);
        item.setDiscount(discount);
        ProductData productData = populateCouponInfo(cartLine, user);
        item.setCoupon(productData.getCoupon());
        item.setCouponDisplay(productData.isCouponDisplay());
        item.setCouponClipped(productData.isCouponClipped());
        item.setCouponStatusText(productData.getCouponStatusText());
        item.setHasEstimatedPrice(cartLine.isEstimatedPrice());
        item.setHasTax(cartLine.hasTax());
        item.setHasDepositValue(cartLine.hasDepositValue());
        item.setHasScaledPricing(calculateHasScaledPricing(cartLine));
        if (StandingOrderHelper.isSO3StandingOrder(user) && user.getCurrentStandingOrder() != null && "Y".equalsIgnoreCase(user.getCurrentStandingOrder().getActivate())) {
            if (user.getSoCartLineMessagesMap().containsKey(cartLine.getCustomerListLineId()))
                item.setSo3ItemStatus(user.getSoCartLineMessagesMap().get(cartLine.getCustomerListLineId()));
        }
        return item;
    }

    /**
     * @see i_viewcart.jspf:396, i_viewcart.jspf:482
     * 
     * @param cartLine
     * @return
     */
    private boolean calculateHasScaledPricing(FDCartLineI cartLine) {
        return cartLine.hasScaledPricing() || (cartLine.getDiscount() == null && cartLine.getGroupQuantity() > 0 && cartLine.getGroupScaleSavings() > 0);
    }

    private void populateCartDataItemWithUnitPriceAndQuantity(CartData.Item item, FDProduct fdProduct, PriceCalculator priceCalculator) {
        try {
            ZonePriceInfoModel zpi = priceCalculator.getZonePriceInfoModel();
            if (zpi != null) {
                DecimalFormat df = new DecimalFormat("0.00");
                item.setUnitPrice(df.format(zpi.getDefaultPrice()));
                FDProductInfo productInfo = FDCachedFactory.getProductInfo(fdProduct.getSkuCode());
                item.setUnitScale(productInfo.getDisplayableDefaultPriceUnit().toLowerCase());
            }
        } catch (FDResourceException e) {
            LOG.error("Error while loading resource", e);
        } catch (FDSkuNotFoundException e) {
            LOG.error("Sku not found", e);
        }
    }

    /**
     * Associate make-good complaint to CartData.Item
     * 
     * @param item
     * @param cartLine
     * @param complaintModel
     */
    public void populateCartDataItemWithMakeGoodAttributes(final CartData.Item item, final FDCartLineI cartLine, final ErpComplaintModel complaintModel) {
        if (complaintModel != null) {
            // match complaint line with orderline id of cart item
            ErpComplaintLineModel line = complaintModel.getComplaintLine(cartLine.getOrderLineId());
            if (line != null) {
                item.setComplaintReason(line.getReason());
            } else {
                // LOG.error("No matching complaint line for cart line " + cartLine.getOrderLineId() + " of order " + order.getErpSalesId());
            }
        }
    }

    public List<CartData.Section> populateCartDataSections(Map<SectionInfo, List<CartData.Item>> sectionMap, Map<String, String> sectionHeaderImgMap) {
        List<CartData.Section> sections = new ArrayList<CartData.Section>();
        for (SectionInfo info : sectionMap.keySet()) {
            String sectionTitle = info.getSectionTitle();
            CartData.Section section = new CartData.Section();
            section.setTitle(sectionTitle);
            section.setTitleImg(sectionHeaderImgMap.get(sectionTitle));
            section.setCartLines(sectionMap.get(info));
            section.setInfo(info);
            sections.add(section);
        }
        return sections;
    }

    private void populateCartOrderData(FDUserI user, HttpServletRequest request, String userId, FDCartI cart, CartData cartData, Set<Long> recentIds, boolean hasSession) throws HttpErrorResponse {
    	try {
            Map<Integer, String> dcpdCartlineMessage = new HashMap<Integer, String>();
            Map<String, FDMinDCPDTotalPromoData> dcpdMinPromo = user.getPromotionEligibility().getMinDCPDTotalPromos();
            List<String> usedDcpdDiscounts = new ArrayList<String>();
            List<FDCartLineI> cartLines = loadCartOrderLines(userId, cart);
            Map<SectionInfo, List<CartData.Item>> sectionMap = new HashMap<SectionInfo, List<CartData.Item>>();
            Map<String, String> sectionHeaderImgMap = new HashMap<String, String>();
            ItemCount itemCount = new ItemCount();
            Map<String, SectionInfo> sectionInfos = new HashMap<String, SectionInfo>();
            boolean isWineInCart = false;
            boolean hasEstimatedPriceItemInCart = false;

            final HttpSession session = request.getSession();
            final boolean isMakeGoodMode = user != null && user.getMasqueradeContext() != null && user.getMasqueradeContext().getMakeGoodFromOrderId() != null; // ==
            // SessionName.MAKEGOOD_COMPLAINT
            // NOTE variable below only makes sense in make-good order mode, otherwise it is null
            final ErpComplaintModel complaintModel = (ErpComplaintModel) session.getAttribute("fd.cc.makegoodComplaint"); // == SessionName.MAKEGOOD_COMPLAINT

            int sessionUserLevel = 0;
            
            for (FDCartLineI cartLine : cartLines) {
                ProductModel productNode = cartLine.lookupProduct();
                if (productNode == null) {
                    LOG.error("Failed to get product node for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping.");
                    continue;
                }
                FDProduct fdProduct = cartLine.lookupFDProduct();
                if (fdProduct == null) {
                    LOG.error("Failed to get fdproduct for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping.");
                    continue;
                }
                String sectionInfoKey;

                long cartlineId = Long.valueOf(cartLine.getCartlineId());
                
                if (cartLine.isWine()) {
                    isWineInCart = true;
                    
                    if (recentIds.contains(cartlineId)) {
                    	sectionInfoKey = "justAddedWineSectionKey"; //generic
                    } else {
                    	sectionInfoKey = "wineSectionKey";
                    }
                } else {
                    if (recentIds.contains(cartlineId)) {
                    	sectionInfoKey = "justAdded"; //generic
                    } else {
                        sectionInfoKey = cartLine.getDepartmentDesc();
                    }
                    if (cartLine.isEstimatedPrice()) {
                        hasEstimatedPriceItemInCart = true;
                    }
                }
                
                SectionInfo sectionInfo = sectionInfos.get(sectionInfoKey);
                if (sectionInfo == null) {
                    sectionInfo = new SectionInfo();
                    Discount discount = cartLine.getDiscount();
                    if (discount != null) {
                        sectionInfo.setFreeSample(EnumDiscountType.FREE.equals(discount.getDiscountType()));
                    }
                    sectionInfo.setWine(cartLine.isWine());
                    if (sectionInfo.isWine()) {
                        cartData.setContainsWineSection(true);
                    }
                    sectionInfos.put(sectionInfoKey, sectionInfo);
                }
                if (sectionInfoKey.startsWith("justAdded")) {
                	sectionInfo.setSectionTitle("Just Added"+"<span class=\"sectionInfoKey\">"+sectionInfoKey+"</span>"); //actual title, must be unique for sort
                	sectionInfo.setJustAdded(true); //set for sort comparator
                } else {
                	sectionInfo.setSectionTitle(sectionInfoKey);
                }
                sectionInfo.setExternalGroup(cartLine.getExternalGroup());
                sectionInfo.setRecipe(sectionInfoKey.contains("Recipe"));
                sectionInfo.setHasEstimatedPrice(sectionInfo.isHasEstimatedPrice() || cartLine.isEstimatedPrice());
                
                List<CartData.Item> sectionList = loadDepartmentSectionList(sectionMap, sectionInfo);
                loadSectionHeaderImage(sectionHeaderImgMap, productNode, sectionInfoKey);
                CartData.Item item = populateCartDataItem(cartLine, fdProduct, itemCount, cart, recentIds, productNode, user);

                if (isMakeGoodMode) {
                    populateCartDataItemWithMakeGoodAttributes(item, cartLine, complaintModel);
                }

                sectionList.add(item);
                String dcpdMessage = populateDCPDPromoDiscount(user, request, cartLine, dcpdMinPromo, usedDcpdDiscounts);
                if (null != dcpdMessage && !"".equals(dcpdMessage)) {
                    dcpdCartlineMessage.put(item.getId(), dcpdMessage);
                }
            }
            
            cartData.setPopulateDCPDPromoDiscount(dcpdCartlineMessage);
            List<CartData.Section> sections = populateCartDataSections(sectionMap, sectionHeaderImgMap);
            
            //separate out just added section(s) now that they're populated
            List<CartData.Section> justAddedSections = new ArrayList<CartData.Section>();
            for (Iterator<CartData.Section> iter = sections.listIterator(); iter.hasNext(); ) {
            	CartData.Section curSection = iter.next();
                if (curSection.getInfo().isJustAdded()) {
                	justAddedSections.add(curSection);
                    iter.remove();
                }
            }
            
            //sort
            Collections.sort(sections, CartData.CART_DATA_SECTION_COMPARATOR_CHAIN_BY_WINE_FREE_SAMPLE_EXTERNAL_GROUP_TITLE);
            
            //re-insert just added sections in the appropriate places
            for (CartData.Section curJustAddedSection : justAddedSections) {
            	if (!curJustAddedSection.getInfo().isWine()) {
            		//non-wine is always first
            		sections.add(0, curJustAddedSection);
            	} else {
            		//find first wine section and insert before it
            		int wineSectionIndex = -1;
            		int curIndex = 0;
            		for (CartData.Section curSection : sections) {
            			if (curSection.getInfo().isWine()) {
            				wineSectionIndex = curIndex;
            				break;
            			}
            			curIndex++;
            		}
            		if (wineSectionIndex >= 0) { //found, insert there
            			sections.add(wineSectionIndex, curJustAddedSection);
            		} else { //not found, add at the end
            			sections.add(curJustAddedSection);
            		}
            	}
            }
            
            //populate only last wine section with subtotal info
            if (cartData.isContainsWineSection()) {
            	//assume it's the last section
                int sectionsSize = sections.size();
                if (sectionsSize > 0 && sections.get(sectionsSize-1).getInfo().isWine()) {
            		sections.get(sectionsSize-1).getInfo().setSubTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getSubTotalOnlyWineAndSpirit(cart)));
            		sections.get(sectionsSize-1).getInfo().setTaxTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getTaxValueOnlyWineAndSpirit(cart)));
            	}
            }            
            
            //save
            cartData.setCartSections(sections);
            
            removeRecipePrefixFromSectionTitles(sections);
            cartData.setItemCount(itemCount.getValue());
            cartData.setSubTotal(JspMethods.formatPrice(cart.getSubTotal()));
            cartData.setEstimatedTotal(JspMethods.formatPrice(cart.getTotal()));
            cartData.setTotalWithoutTax(JspMethods.formatPrice(cart.getTotal() - cart.getTaxValue()));

            if (user != null) {
                sessionUserLevel = user.getLevel();
            }
            if (FDStoreProperties.isSocialLoginEnabled() && hasSession) {
                if (sessionUserLevel == FDUserI.GUEST) {
                    // session.setAttribute(SessionName.PREV_SUCCESS_PAGE,request.getRequestURI());
                    session.setAttribute(SessionName.PREV_SUCCESS_PAGE, "/expressco/checkout.jsp");
                    cartData.setBeforeCheckoutAction("onclick=\"FreshDirect.components.ifrPopup.open({ url: '/social/signup_lite.jsp', height: 590, opacity: .5}); return false;\"");
                } else if (sessionUserLevel == FDUserI.RECOGNIZED) {
                    // session.setAttribute(SessionName.PREV_SUCCESS_PAGE,request.getRequestURI());
                    session.setAttribute(SessionName.PREV_SUCCESS_PAGE, "/expressco/checkout.jsp");
                    cartData.setBeforeCheckoutAction("onclick=\"FreshDirect.components.ifrPopup.open({ url: '/social/login.jsp', height: 580, opacity: .5}); return false;\"");
                }
            }

            populateSubTotalBox(cartData, cart, user);
            populateSubTotalBoxForNonAlcoholSections(cart, sections, hasEstimatedPriceItemInCart, getSubTotalTextForNonAlcoholicSections(isWineInCart, hasEstimatedPriceItemInCart));

            if (FDUserI.GUEST < user.getLevel()) {
                cartData.setUserRecognized(true);
                cartData.setUserCorporate(user.isCorporateUser());
                if (StandingOrderHelper.isSO3StandingOrder(user)) {
                    cartData.setUserCorporate(true);

                }
                cartData.setGoGreen(("Y".equalsIgnoreCase(GoGreenService.defaultService().loadGoGreenOption(user))) ? true : false);
            }
            if (hasSession) {
            cartData.setBillingReferenceInfo(populateBillingReferenceInfo(session, user));
            checkCartCleanUpAction(request, cartData);
            cartData.setModifyCartData(isModifyOrderMode(session, cart));/* pass in cart, it may not be in user */
            cartData.setModifyOrder(cartData.getModifyCartData().isModifyOrderEnabled());
            cartData.setErrorMessage(null);
            }
            // if(user.getCurrentStandingOrder()!=null)
            if (!StandingOrderHelper.isSO3StandingOrder(user)) {
                cartData.setWarningMessage(AvailabilityService.defaultService().translateWarningMessage(request.getParameter("warning_message"), user));
            }
            cartData.setDisplayCheckout(StandingOrderHelper.isSO3StandingOrder(user) ? false : true);
            cartData.setSoftLimit(StandingOrderHelper.formatDecimalPrice(ErpServicesProperties.getStandingOrderSoftLimit()));
            cartData.setHardLimit(StandingOrderHelper.formatDecimalPrice(ErpServicesProperties.getStandingOrderHardLimit()));
            
            boolean dlvPassCart = null !=request.getParameter("dlvPassCart") && "true".equalsIgnoreCase(request.getParameter("dlvPassCart")) ? true: false;
            cartData.setDlvPassCart(dlvPassCart);

            cartData.setCouponMessage(populateCouponMessage(user, cartLines));
            if (StandingOrderHelper.isSO3StandingOrder(user)) {
                cartData.setDeliveryBegins(StandingOrderHelper.getDeliveryBeginsInfo(user));
            }


            cartData.setCustomerServiceRepresentative(CustomerServiceRepresentativeService.defaultService().loadCustomerServiceRepresentativeInfo(user));
            cartData.setAvalaraEnabled(FDStoreProperties.getAvalaraTaxEnabled());
            if (FDStoreProperties.isETippingEnabled()) {
                if (StandingOrderHelper.isSO3StandingOrder(user)) {
                    cartData.setEtipTotal(JspMethods.formatPrice(user.getCurrentStandingOrder().getTipAmount()));

                } else {
                    cartData.setEtipTotal(JspMethods.formatPrice(cart.getTip()));
                }
                cartData.seteTippingEnabled(true);
                cartData.setCustomTip(cart.isCustomTip());
                cartData.setTipApplied(cart.isTipApplied());

            }
            if (StandingOrderHelper.isSO3StandingOrder(user)) {
                cartData.setUserCorporate(true);
            }
            if (hasSession){
            cartData.setGoogleAnalyticsData(GoogleAnalyticsDataService.defaultService().populateCheckoutGAData(cart, cartData));
            }

        } catch (Exception e) {
            LOG.error("Error while processing cart for user " + userId, e);
            BaseJsonServlet.returnHttpError(500, "Error while processing cart for user " + userId, e);
        }
    }

    public BillingReferenceInfo populateBillingReferenceInfo(HttpSession session, FDUserI user) {
        BillingReferenceInfo billingReferenceInfo = new BillingReferenceInfo();
        billingReferenceInfo.setBillingReference((String) session.getAttribute(SessionName.PAYMENT_BILLING_REFERENCE));
        billingReferenceInfo.setCorporateUser(user.isCorporateUser());
        return billingReferenceInfo;
    }

    public String populateDCPDPromoDiscount(FDUserI user, HttpServletRequest request, FDCartLineI cartLine, Map<String, FDMinDCPDTotalPromoData> dcpdMinPromo,
            List<String> usedDcpdDiscounts) {
        String dcpdMinMessage = "";
        String promoKey = "";

        if (null != dcpdMinPromo && dcpdMinPromo.size() > 0) {
            for (Iterator<String> iter = dcpdMinPromo.keySet().iterator(); iter.hasNext();) {
                promoKey = iter.next();

                if (!usedDcpdDiscounts.contains(promoKey)) {
                    FDMinDCPDTotalPromoData dcpdPromoModel = dcpdMinPromo.get(promoKey);
                    List<FDCartLineI> dcpdCartLines = dcpdPromoModel.getDcpdCartLines();

                    for (FDCartLineI dcpdCartLine : dcpdCartLines) {
                        if (cartLine.equals(dcpdCartLine) && (dcpdPromoModel.getCartDcpdTotal() < dcpdPromoModel.getDcpdMinTotal())) {
                            StringBuffer sb = new StringBuffer();
                            sb.append(" Spend $" + Math.round(100 * (dcpdPromoModel.getDcpdMinTotal() - dcpdPromoModel.getCartDcpdTotal())) / 100d + " more on");

                            String id = (null != dcpdPromoModel.getContentKey()) ? dcpdPromoModel.getContentKey().getId() : "";
                            if (null == id || "".equals(id)) {
                                sb.append(" promotional products");
                            } else {
                                sb.append(" <a href=" + request.getContextPath() + "/browse.jsp?id=" + id + "> promotional products</a>");
                            }
                            sb.append(" to save $" + Math.round(100 * dcpdPromoModel.getHeaderDiscAmount()) / 100d);
                            dcpdMinMessage = sb.toString();
                            usedDcpdDiscounts.add(promoKey);
                            break;
                        }
                    }
                }
            }
        }
        return dcpdMinMessage;
    }

    private ProductData populateCouponInfo(FDCartLineI cartLine, FDUserI user) {
        ProductData productData = new ProductData();
        ProductDetailPopulator.postProcessPopulate(user, productData, cartLine.getSkuCode(), true, cartLine);
        return productData;
    }

    public String populateCouponMessage(FDUserI user, List<FDCartLineI> cartLines) throws FDResourceException {
        String couponMessage = null;
        boolean needsCouponMessaging = false;

        for (FDCartLineI cartLine : cartLines) {
            EnumCouponContext couponContext = EnumCouponContext.VIEWCART;
            FDCustomerCoupon currentCoupon = user.getCustomerCoupon(cartLine, couponContext);

            needsCouponMessaging = (currentCoupon != null && (currentCoupon.getDisplayStatus().equals(EnumCouponDisplayStatus.COUPON_CLIPPABLE)
                    || currentCoupon.getStatus().equals(EnumCouponStatus.COUPON_MIN_QTY_NOT_MET) || currentCoupon.getStatus().equals(EnumCouponStatus.COUPON_CLIPPED_EXPIRED)));

            if (needsCouponMessaging) {
                if (user.isCouponsSystemAvailable()) {
                    couponMessage = "One or more coupons have not been applied to your order. See indicator(s) in red below. <a href=\"#\" onclick=\"doOverlayDialog('/media/editorial/ecoupons/coupons_info.html'); return false;\">More coupon info.</a>";
                } else {
                    if (FDCouponProperties.isDisplayMessageCouponsNotAvailable())
                        couponMessage = SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE;
                }
                break;
            }
        }

        return couponMessage;
    }

    private CartData.Item.Discount populateDiscount(FDCartLineI cartLine) {
        String skuLimit = null;
        String discountPrice = null;
        String description = null;
        final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        if (cartLine.getDiscount() != null) {
            Discount discount = cartLine.getDiscount();
            if (discount.getSkuLimit() > 0 && cartLine.getUnitPrice().indexOf("lb") == -1) {
                skuLimit = Integer.toString(discount.getSkuLimit());
            }
            discountPrice = currencyFormatter.format(cartLine.getDiscountAmount());
            PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
            description = promotion.getDescription();
        } else {
            if (cartLine.getGroupQuantity() > 0) {
                double savings = cartLine.getGroupScaleSavings();
                if (savings > 0) {
                    discountPrice = currencyFormatter.format(savings);
                }
            }
        }
        CartData.Item.Discount discount = new CartData.Item.Discount();
        discount.setDiscountPrice(discountPrice);
        discount.setSkuLimit(skuLimit);
        discount.setDescription(description);
        return discount;
    }

    private void populateOrderData(FDUserI user, HttpServletRequest request, String userId, FDCartI cart, CartData cartData) throws HttpErrorResponse {
        populateCartOrderData(user, request, userId, cart, cartData, Collections.<Long> emptySet(), true);
    }
    private void populateOrderData(FDUserI user, HttpServletRequest request, String userId, FDCartI cart, CartData cartData, Set<Long> recentIds) throws HttpErrorResponse {
        populateCartOrderData(user, request, userId, cart, cartData, recentIds, true);
    }

    private SortedSet<Long> populateRecentIds(FDCartModel cart) {
        SortedSet<Long> recentIds = new TreeSet<Long>();
        for (FDCartLineI rc : cart.getRecentOrderLines()) {
            recentIds.add(Long.valueOf(rc.getRandomId()));
        }
        return recentIds;
    }

    private boolean hasMultiplestores(FDCartI cart, String userId) {
        boolean hasWine = false;
        boolean hasFdStore = false;
        try {
            List<FDCartLineI> cartLines = this.loadCartOrderLines(userId, cart);
            for (FDCartLineI cartLine : cartLines) {
                if (cartLine.isWine()) {
                    hasWine = true;
                } else {
                    hasFdStore = true;
                }
            }
        } catch (HttpErrorResponse e) {
            e.printStackTrace();
        }
        return (hasWine & hasFdStore);
    }

    public void populateSubTotalBox(CartData cartData, FDCartI cart, FDUserI user) {
        cartData.setAvalaraEnabled(FDStoreProperties.getAvalaraTaxEnabled());
        List<CartSubTotalFieldData> subTotalBox = new ArrayList<CartSubTotalFieldData>();
        CartSubTotalBoxService.defaultService().populateSubTotalToBox(subTotalBox, cart);
        CartSubTotalBoxService.defaultService().populateTaxToBox(subTotalBox, cart, hasMultiplestores(cart, user.getUserId()));
        // CartSubTotalBoxService.defaultService().populateTipToBox(subTotalBox, cart);
        CartSubTotalBoxService.defaultService().populateDepositValueToBox(subTotalBox, cart.getDepositValue());
        CartSubTotalBoxService.defaultService().populateFuelSurchargeToBox(subTotalBox, cart);
        CartSubTotalBoxService.defaultService().populateDiscountsToBox(subTotalBox, cart, cartData, user);
        CartSubTotalBoxService.defaultService().populateCustomerCreditsToBox(subTotalBox, cart);
        CartSubTotalBoxService.defaultService().populateGiftBalanceToBox(subTotalBox, user);
        CartSubTotalBoxService.defaultService().populateDeliveryFeeToBox(subTotalBox, cart, user, cartData);
        if (FDStoreProperties.getAvalaraTaxEnabled()) {
            CartSubTotalBoxService.defaultService().populateAvalaraTaxToBox(subTotalBox, cart);
        }
        CartSubTotalFieldData saveAmountBox = CartSubTotalBoxService.defaultService().createSavingToBox(cart);
        if (FDStoreProperties.isETippingEnabled()) {
            List<CartSubTotalFieldData> estimatedTotalBox = new ArrayList<CartSubTotalFieldData>();
            CartSubTotalBoxService.defaultService().populateOrderTotalToBox(estimatedTotalBox, cart);
            if (saveAmountBox != null) {
            	estimatedTotalBox.add(saveAmountBox);
            }
            cartData.getSubTotalBox().put(ESTIMATED_TOTAL_BOX_JSON_KEY, estimatedTotalBox);
        } else {
            CartSubTotalBoxService.defaultService().populateOrderTotalToBox(subTotalBox, cart);
        }
        cartData.getSubTotalBox().put(USER_CORPORATE_JSON_KEY, user.isCorporateUser());
        cartData.getSubTotalBox().put(USER_RECOGNIZED_JSON_KEY, FDUserI.GUEST < user.getLevel());
        cartData.getSubTotalBox().put(SUB_TOTAL_BOX_JSON_KEY, subTotalBox);
    }

    public String getSubTotalTextForNonAlcoholicSections(boolean isWineInCart, boolean hasEstimatedPriceItemInCart) {
        StringBuilder subTotalText = new StringBuilder(32);
        if (isWineInCart) {
            subTotalText.append("FreshDirect ");
        }
        if (hasEstimatedPriceItemInCart) {
            subTotalText.append("Estimated ");
        }
        subTotalText.append("Subtotal");
        return subTotalText.toString();
    }

    public void populateSubTotalBoxForNonAlcoholSections(FDCartI cart, List<CartData.Section> sections, boolean hasEstimatedPriceItemInCart, String subTotalText) {
    	//find last non-wine section and decorate it
    	int nonWineSection = -1;
    	for (CartData.Section curSection : sections) {
    		if (curSection.getInfo().isWine()) {
    			break;
    		}
    		nonWineSection++;
    	}
    	if (nonWineSection >= 0) {
        	decorateSubTotalWithoutWineAndSpirit(cart, sections, sections.get(nonWineSection).getInfo(), hasEstimatedPriceItemInCart, subTotalText);
    	}
    }

    private void decorateSubTotalWithoutWineAndSpirit(FDCartI cart, List<CartData.Section> sections, SectionInfo sectionInfo, boolean hasEstimatedPriceItemInCart,
            String subTotalText) {
        sectionInfo.setSubTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getSubTotalWithoutWineAndSpirit(cart)));
        sectionInfo.setTaxTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getTaxValueWithoutWineAndSpirit(cart)));
        sectionInfo.setSubTotalText(subTotalText);
        sectionInfo.setHasEstimatedPrice(hasEstimatedPriceItemInCart);
    }

    private String printDate(Date date) {
        return new SimpleDateFormat("'<b>'h:mm a'</b> on <b>'EEEEE, MM/dd/yyyy'</b>'").format(date);
    }

    public void removeRecipePrefixFromSectionTitles(List<CartData.Section> cartSections) {
        if (cartSections != null) {
            for (Section section : cartSections) {
                if (section.getInfo() != null && section.getInfo().isRecipe()) {
                    section.setTitle(section.getTitle().replaceAll(RECIPE_PREFIX_PATTERN, ""));
                }
            }
        }
    }

    private void updateCart(HttpServletRequest request, FDUserI user, String userId, FDCartModel cart, CartData cartData) throws HttpErrorResponse {
        CartRequestData reqData = BaseJsonServlet.parseRequestData(request, CartRequestData.class);
        cartData.setHeader(reqData.getHeader());
        try {
            List<FDCartLineI> cartLines = loadCartOrderLines(userId, cart);
            Map<Integer, CartRequestData.Change> changes = reqData.getChange();
            List<FDCartLineI> clines2report = new ArrayList<FDCartLineI>();
            String serverName = request.getServerName();
            if (null != changes)
                for (FDCartLineI cartLine : new ArrayList<FDCartLineI>(cartLines)) {
                    Integer id = cartLine.getRandomId();
                    CartRequestData.Change change = changes.get(id);
                    double oldQuantity = cartLine.getQuantity();
                    if (change == null) {
                        continue;
                    }
                    String changeType = change.getType();

                    if (CartRequestData.Change.CHANGE_COMPLAINT_REASON.equals(changeType)) {
                        // SPECIAL CASE: complaints are stored in a different structure
                        final String reasonId = (String) change.getData();

                        final ErpComplaintModel complaintModel = (ErpComplaintModel) request.getSession().getAttribute("fd.cc.makegoodComplaint");
                        if (complaintModel == null) {
                            LOG.error("Missing complaint data infrastructure, not found in session");
                            continue;
                        }

                        final ErpComplaintLineModel line = complaintModel.getComplaintLine(cartLine.getOrderLineId());
                        if (line == null) {
                            LOG.error("Failed to get complaint line for order line ID " + cartLine.getOrderLineId());
                            continue;
                        }

                        // set or clear selection
                        if (reasonId == null || "".equals(reasonId)) {
                            line.setReason(null);
                        } else {
                            boolean isSet = false;
                            // go through the available reasons
                            for (ErpComplaintReason selReason : ComplaintUtil.getReasonsForDepartment("Makegood")) {
                                if (reasonId.equals(selReason.getId())) {
                                    line.setReason(selReason);
                                    isSet = true;
                                    break;
                                }
                            }
                            if (!isSet) {
                                line.setReason(null);
                            }
                        }
                    } else {
                        updateCartLinesByChangeType(user, cart, clines2report, serverName, cartLine, change, changeType, cartData, oldQuantity);
                    }
                }
        } catch (Exception e) {
            LOG.error("Error while modifying cart for user " + userId, e);
            BaseJsonServlet.returnHttpError(500, "Error while modifying cart");
        }
    }

    private void updateCartLinesByChangeType(FDUserI user, FDCartModel cart, List<FDCartLineI> clines2report, String serverName, FDCartLineI cartLine,
            CartRequestData.Change change, String chType, CartData cartData, double oldQuantity) {
        if (CartRequestData.Change.CHANGE_QUANTITY.equals(chType)) {
            double qu = Double.parseDouble((String) change.getData());
            CartOperations.changeQuantity(user, cart, cartLine, qu, serverName);
            clines2report.add(cartLine);
            user.setCouponEvaluationRequired(true);
            cartData.setGoogleAnalyticsData(GoogleAnalyticsDataService.defaultService().populateAddToCartGAData(cartLine, Double.toString(cartLine.getQuantity() - oldQuantity)));
        } else if (CartRequestData.Change.CHANGE_SALESUNIT.equals(chType)) {
            String su = (String) change.getData();
            CartOperations.changeSalesUnit(user, cart, cartLine, su, serverName);
            clines2report.add(cartLine);
        } else if (CartRequestData.Change.REMOVE.equals(chType)) {
            CartOperations.removeCartLine(user, cart, cartLine, serverName);
            cartData.setGoogleAnalyticsData(GoogleAnalyticsDataService.defaultService().populateCartLineChangeGAData(cartLine, "-" + Double.toString(oldQuantity)));
        }
    }

    private void updateUserAndCart(HttpServletRequest request, FDUserI user) throws FDResourceException, JspException {
        user.updateDlvPassInfo();
        FDShoppingCartService.defaultService().updateShoppingCart(user, request.getSession());
    }

}
