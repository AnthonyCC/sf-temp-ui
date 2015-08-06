package com.freshdirect.webapp.ajax.expresscheckout.cart.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
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
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.Section;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.SectionInfo;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartOperations;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartRequestData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartSubTotalFieldData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.ItemCount;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.ModifyCartData;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.gogreen.service.GoGreenService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FDCartModelService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.viewcart.service.ViewCartCarouselService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.JspMethods;

public class CartDataService {

    private static final String VIEW_CART_HEADER_MESSAGE_JSON_KEY = "viewCartHeaderMessage";
    private static final String WARNING_MESSAGE_JSON_KEY = "warningMessage";

    public static CartDataService defaultService() {
        return INSTANCE;
    }

    private static final String RECIPE_PREFIX_PATTERN = "\\s*(R|r)ecipe\\s*:\\s*";

    private static final CartDataService INSTANCE = new CartDataService();

    private static final Logger LOG = LoggerFactory.getInstance(CartDataService.class);

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
        String userId = loadUser(user);
        updateUserAndCart(request, user);
        FDCartModel cart = loadUserShoppingCart(user, userId);
        if (user.getIdentity() != null) {
            FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity());
        }
        CartData cartData = new CartData();
        synchronized (cart) {
            populateCartData(user, request, userId, cart, cartData);
        }
        return cartData;
    }

    public Map<String, List<CartSubTotalFieldData>> loadCartDataSubTotalBox(HttpServletRequest request, FDUserI user) throws HttpErrorResponse, FDResourceException, JspException {
        String userId = loadUser(user);
        updateUserAndCart(request, user);
        FDCartModel cart = loadUserShoppingCart(user, userId);
        if (user.getIdentity() != null) {
            FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity());
        }
        CartData cartData = new CartData();
        synchronized (cart) {
            populateSubTotalBox(cartData, cart, user, request);
        }
        return cartData.getSubTotalBox();
    }

    public CartData loadCartSuccessData(HttpServletRequest request, FDUserI user, String orderId) throws HttpErrorResponse, FDResourceException {
        String userId = loadUser(user);
        FDOrderI order = FDCustomerManager.getOrder(user.getIdentity(), orderId);
        CartData cartData = new CartData();
        synchronized (order) {
            populateOrderData(user, request, userId, order, cartData);
        }
        return cartData;
    }

    public CartData updateCartData(HttpServletRequest request, FDUserI user) throws HttpErrorResponse, FDResourceException, JspException {
        String userId = loadUser(user);
        FDCartModel cart = loadUserShoppingCart(user, userId);
        CartData cartData = new CartData();
        synchronized (cart) {
            updateCart(request, user, userId, cart, cartData);
            updateUserAndCart(request, user);
            populateCartData(user, request, userId, cart, cartData);
        }
        return cartData;
    }

    public FormDataResponse validateOrderMinimumOnStartCheckout(FDUserI user, FormDataRequest request) throws FDResourceException {
        FormDataResponse result = createStartCheckoutResponseData(request);
        String orderMinimumWarningMessageKey = AvailabilityService.defaultService().selectWarningType(user);
        if (orderMinimumWarningMessageKey != null) {
            Map<String, Object> headerMessageMap = new HashMap<String, Object>();
            headerMessageMap.put(WARNING_MESSAGE_JSON_KEY, AvailabilityService.defaultService().translateWarningMessage(orderMinimumWarningMessageKey, user));
            result.getSubmitForm().getResult().put(VIEW_CART_HEADER_MESSAGE_JSON_KEY, headerMessageMap);
        } else {
            result.getSubmitForm().getResult().put("redirectUrl", "/expressco/checkout.jsp");
            result.getSubmitForm().setSuccess(true);
        }
        return result;
    }

    private FormDataResponse createStartCheckoutResponseData(FormDataRequest requestData) {
        FormDataResponse responseData = new FormDataResponse();
        SubmitForm submitForm = new SubmitForm();
        submitForm.setFormId(requestData.getFormId());
        responseData.setFormSubmit(submitForm);
        ValidationResult validationResult = new ValidationResult();
        validationResult.setFdform(requestData.getFormId());
        responseData.setValidationResult(validationResult);
        return responseData;
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

    private void checkCartCleanUpAction(HttpServletRequest request, CartData cartData) {
        Boolean cartChangedByCleanUp = (Boolean) request.getSession().getAttribute(FDShoppingCartService.CART_CHANGED_BY_CLEAN_UP_SESSION_ATTRIBUTE_ID);
        if (cartChangedByCleanUp != null) {
            cartData.setCartChangedByCleanUp(cartChangedByCleanUp);
            request.getSession().removeAttribute(FDShoppingCartService.CART_CHANGED_BY_CLEAN_UP_SESSION_ATTRIBUTE_ID);
        }
    }

    private ModifyCartData isModifyOrderMode(HttpSession session) {
        FDUserI mUser = (FDUserI) session.getAttribute(SessionName.USER);
        FDCartModel mCart = mUser.getShoppingCart();
        ModifyCartData modifyCartData = new ModifyCartData();
        if (mCart instanceof FDModifyCartModel) {
            modifyCartData.setModifyOrderEnabled(true);
            FDModifyCartModel modifyCart = (FDModifyCartModel) mCart;
            String orderId = modifyCart.getOriginalOrder().getErpSalesId();
            session.setAttribute("MODIFIED" + orderId, orderId);
            modifyCartData.setOrderId(orderId);
            mCart.setTransactionSource(null);
            Calendar cal = Calendar.getInstance();
            cal.setTime(modifyCart.getOriginalOrder().getDatePlaced());
            cal.add(Calendar.DAY_OF_MONTH, 8);
            Date weekFromOrderDate = cal.getTime();
            modifyCartData.setCutoffTime(printDate(modifyCart.getOriginalOrder().getDeliveryReservation().getCutoffTime()));
            modifyCartData.setOneWeekLater(printDate(weekFromOrderDate));
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

    private List<CartData.Item> loadDepartmentSectionList(Map<SectionInfo, List<CartData.Item>> sectionMap, SectionInfo info) {
        List<CartData.Item> sectionList = sectionMap.get(info);
        if (sectionList == null) {
            sectionList = new ArrayList<CartData.Item>();
            sectionMap.put(info, sectionList);
        }
        return sectionList;
    }

    private void loadSectionHeaderImage(Map<String, String> sectionHeaderImgMap, ProductModel productNode, String deptDesc) {
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

    private FDCartModel loadUserShoppingCart(FDUserI user, String userId) throws HttpErrorResponse {
        FDCartModel cart = user.getShoppingCart();
        if (cart == null) {
            LOG.error("No cart found for user " + userId);
            BaseJsonServlet.returnHttpError(500, "No cart found for user " + userId);
        }
        return cart;
    }

    private void populateCartData(FDUserI user, HttpServletRequest request, String userId, FDCartModel cart, CartData cartData) throws HttpErrorResponse {
        populateCartOrderData(user, request, userId, cart, cartData, populateRecentIds(cart));
    }

    public CartData.Item populateCartDataItem(FDCartLineI cartLine, FDProduct fdProduct, ItemCount itemCount, FDCartI cart, Set<Integer> recentIds, ProductModel productNode,
            FDUserI user) throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse {
        CartData.Item item = populateCartDataItemByCartLine(user, cartLine, cart, recentIds);
        populateCartDataItemWithUnitPriceAndQuantity(cartLine.getPrice(), item, fdProduct, productNode.getPriceCalculator());
        item.setImage(productNode.getProdImage().getPathWithPublishId());
        if (cartLine.isSoldBySalesUnits()) {
            cartLineSoldBySaleUnit(cartLine, fdProduct, item);
            itemCount.setValue(itemCount.getValue() + 1);
        } else {
            CartData.Quantity q = cartLineSoldByQuantity(cartLine, productNode, user, item);
            itemCount.setValue(itemCount.getValue() + q.getQuantity());
        }
        return item;
    }

    private CartData.Item populateCartDataItemByCartLine(FDUserI user, FDCartLineI cartLine, FDCartI cart, Set<Integer> recentIds) {
        CartData.Item item = new CartData.Item();
        int randomId = cartLine.getRandomId();
        item.setId(randomId);
        item.setRecent(recentIds.contains(randomId));
        item.setNewItem((cart instanceof FDModifyCartModel) && !(cartLine instanceof FDModifyCartLineI));
        item.setPrice(JspMethods.formatPrice(cartLine.getPrice()));
        item.setDescr(cartLine.getDescription());
        item.setConfDescr(cartLine.getConfigurationDesc());
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

    private void populateCartDataItemWithUnitPriceAndQuantity(double price, CartData.Item item, FDProduct fdProduct, PriceCalculator priceCalculator) {
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

    private List<CartData.Section> populateCartDataSections(Map<SectionInfo, List<CartData.Item>> sectionMap, Map<String, String> sectionHeaderImgMap) {
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

    private void populateCartOrderData(FDUserI user, HttpServletRequest request, String userId, FDCartI cart, CartData cartData, Set<Integer> recentIds) throws HttpErrorResponse {
        try {
            List<FDCartLineI> cartLines = loadCartOrderLines(userId, cart);
            Map<SectionInfo, List<CartData.Item>> sectionMap = new HashMap<SectionInfo, List<CartData.Item>>();
            Map<String, String> sectionHeaderImgMap = new HashMap<String, String>();
            ItemCount itemCount = new ItemCount();
            Map<String, SectionInfo> sectionInfos = new HashMap<String, SectionInfo>();
            Boolean cartHasEstimatedLines = false;
            Boolean cartHasWine = false;
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
                if (cartLine.isEstimatedPrice()) {
                    cartHasEstimatedLines = true;
                }
                String sectionInfoKey;
                if (cartLine.isWine()) {
                    cartHasWine = true;
                    sectionInfoKey = "wineSectionKey";
                } else {
                    sectionInfoKey = cartLine.getDepartmentDesc();
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
                        sectionInfo.setSubTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getSubTotalOnlyWineAndSpirit(cart)));
                        sectionInfo.setTaxTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getTaxValueOnlyWineAndSpirit(cart)));
                    }
                    sectionInfos.put(sectionInfoKey, sectionInfo);
                }
                sectionInfo.setSectionTitle(sectionInfoKey);
                sectionInfo.setExternalGroup(cartLine.getExternalGroup());
                sectionInfo.setRecipe(sectionInfoKey.contains("Recipe"));
                List<CartData.Item> sectionList = loadDepartmentSectionList(sectionMap, sectionInfo);
                loadSectionHeaderImage(sectionHeaderImgMap, productNode, sectionInfoKey);
                CartData.Item item = populateCartDataItem(cartLine, fdProduct, itemCount, cart, recentIds, productNode, user);
                sectionList.add(item);
            }
            List<CartData.Section> sections = populateCartDataSections(sectionMap, sectionHeaderImgMap);
            Collections.sort(sections, CartData.CART_DATA_SECTION_COMPARATOR_CHAIN_BY_WINE_FREE_SAMPLE_EXTERNAL_GROUP_TITLE);
            cartData.setCartSections(sections);
            removeRecipePrefixFromSectionTitles(sections);
            cartData.setItemCount(itemCount.getValue());
            cartData.setSubTotal(JspMethods.formatPrice(cart.getSubTotal()));
            cartData.setEstimatedTotal(JspMethods.formatPrice(cart.getTotal()));
            populateSubTotalBox(cartData, cart, user, request);
            populateSubTotalBoxForNonAlcoholSections(cart, sections, getSubTotalTextForNonAlcoholicSections(cartHasEstimatedLines, cartHasWine));
            if (FDUserI.GUEST < user.getLevel()) {
                cartData.setUserRecognized(true);
                cartData.setGoGreen(GoGreenService.defaultService().loadGoGreenOption(user));
            }
            checkCartCleanUpAction(request, cartData);
            cartData.setModifyCartData(isModifyOrderMode(request.getSession()));
            cartData.setModifyOrder(cartData.getModifyCartData().isModifyOrderEnabled());
            cartData.setErrorMessage(null);
            cartData.setWarningMessage(AvailabilityService.defaultService().translateWarningMessage(request.getParameter("warning_message"), user));
            cartData.setCouponMessage(populateCouponMessage(user, cartLines));
            cartData.setProductSamplesTab(ViewCartCarouselService.defaultService().populateViewCartPageProductSampleCarousel(request));
        } catch (Exception e) {
            LOG.error("Error while processing cart for user " + userId, e);
            BaseJsonServlet.returnHttpError(500, "Error while processing cart for user " + userId, e);
        }
    }

    private ProductData populateCouponInfo(FDCartLineI cartLine, FDUserI user) {
        ProductData productData = new ProductData();
        ProductDetailPopulator.postProcessPopulate(user, productData, cartLine.getSkuCode(), true, cartLine);
        return productData;
    }

    private String populateCouponMessage(FDUserI user, List<FDCartLineI> cartLines) throws FDResourceException {
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
        populateCartOrderData(user, request, userId, cart, cartData, Collections.<Integer> emptySet());
    }

    private SortedSet<Integer> populateRecentIds(FDCartModel cart) {
        SortedSet<Integer> recentIds = new TreeSet<Integer>();
        for (FDCartLineI rc : cart.getRecentOrderLines()) {
            recentIds.add(new Integer(rc.getRandomId()));
        }
        return recentIds;
    }

    private void populateSubTotalBox(CartData cartData, FDCartI cart, FDUserI user, ServletRequest request) throws FDResourceException {
        List<CartSubTotalFieldData> subTotalBox = new ArrayList<CartSubTotalFieldData>();
        cartData.getSubTotalBox().put("subTotalBox", subTotalBox);
        CartSubTotalBoxService.defaultService().populateSubTotalToBox(subTotalBox, cart.getSubTotal());
        CartSubTotalBoxService.defaultService().populateTaxToBox(subTotalBox, cart);
        CartSubTotalBoxService.defaultService().populateDepositValueToBox(subTotalBox, cart.getDepositValue());
        CartSubTotalBoxService.defaultService().populateFuelSurchargeToBox(subTotalBox, cart);
        CartSubTotalBoxService.defaultService().populateDiscountsToBox(subTotalBox, cart, cartData, user);
        CartSubTotalBoxService.defaultService().populateCustomerCreditsToBox(subTotalBox, cart);
        CartSubTotalBoxService.defaultService().populateGiftBalanceToBox(subTotalBox, user);
        CartSubTotalBoxService.defaultService().populateDeliveryFeeToBox(subTotalBox, cart, user, cartData);
        CartSubTotalBoxService.defaultService().populateOrderTotalToBox(subTotalBox, cart);
        CartSubTotalBoxService.defaultService().populateSavingToBox(subTotalBox, cart);
    }

    private String getSubTotalTextForNonAlcoholicSections(Boolean cartHasEstimatedLines, Boolean cartHasWine) {
        StringBuilder subTotalText = new StringBuilder();
        if (cartHasWine) {
            subTotalText.append("FreshDirect ");
        }
        if (cartHasEstimatedLines) {
            subTotalText.append("Estimated ");
        }
        subTotalText.append("Subtotal");
        return subTotalText.toString();
    }

    private void populateSubTotalBoxForNonAlcoholSections(FDCartI cart, List<CartData.Section> sections, String subTotalText) {
        int sectionSize = sections.size();
        if (sectionSize > 0) {
            Section lastSection = sections.get(sectionSize - 1);
            if (lastSection.getInfo().isWine()) {
                if (sectionSize > 1) {
                    Section lastNonAlcoholicSection = sections.get(sectionSize - 2);
                    lastNonAlcoholicSection.getInfo().setSubTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getSubTotalWithoutWineAndSpirit(cart)));
                    lastNonAlcoholicSection.getInfo().setTaxTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getTaxValueWithoutWineAndSpirit(cart)));
                    lastNonAlcoholicSection.getInfo().setSubTotalText(subTotalText);
                }
            } else {
                lastSection.getInfo().setSubTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getSubTotalWithoutWineAndSpirit(cart)));
                lastSection.getInfo().setTaxTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getTaxValueWithoutWineAndSpirit(cart)));
                lastSection.getInfo().setSubTotalText(subTotalText);
            }
        }
    }

    private String printDate(Date date) {
        return new SimpleDateFormat("'<b>'h:mm a'</b> on <b>'EEEEE, MM/dd/yyyy'</b>'").format(date);
    }

    private void removeRecipePrefixFromSectionTitles(List<CartData.Section> cartSections) {
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
            for (FDCartLineI cartLine : new ArrayList<FDCartLineI>(cartLines)) {
                Integer id = cartLine.getRandomId();
                CartRequestData.Change change = changes.get(id);
                if (change == null) {
                    continue;
                }
                String changeType = change.getType();
                updateCartLinesByChangeType(user, cart, clines2report, serverName, cartLine, change, changeType);
                CartOperations.populateCoremetricsShopTag(cartData, cartLine);
            }
        } catch (Exception e) {
            LOG.error("Error while modifying cart for user " + userId, e);
            BaseJsonServlet.returnHttpError(500, "Error while modifying cart");
        }
    }

    private void updateCartLinesByChangeType(FDUserI user, FDCartModel cart, List<FDCartLineI> clines2report, String serverName, FDCartLineI cartLine,
            CartRequestData.Change change, String chType) {
        if (CartRequestData.Change.CHANGE_QUANTITY.equals(chType)) {
            double qu = Double.parseDouble((String) change.getData());
            CartOperations.changeQuantity(user, cart, cartLine, qu, serverName);
            clines2report.add(cartLine);
        } else if (CartRequestData.Change.CHANGE_SALESUNIT.equals(chType)) {
            String su = (String) change.getData();
            CartOperations.changeSalesUnit(user, cart, cartLine, su, serverName);
            clines2report.add(cartLine);
        } else if (CartRequestData.Change.REMOVE.equals(chType)) {
            CartOperations.removeCartLine(user, cart, cartLine, serverName);
        }
    }

    private void updateUserAndCart(HttpServletRequest request, FDUserI user) throws FDResourceException, JspException {
        user.updateDlvPassInfo();
        FDShoppingCartService.defaultService().updateShoppingCart(user, request.getSession());
    }

}
