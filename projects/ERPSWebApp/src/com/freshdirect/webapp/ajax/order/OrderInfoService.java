package com.freshdirect.webapp.ajax.order;

import static java.util.regex.Pattern.compile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.SectionInfo;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.ItemCount;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FDCartModelService;
import com.freshdirect.webapp.ajax.product.ProductPotatoService;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.util.JspMethods;

public class OrderInfoService {

    private static final Logger LOGGER = LoggerFactory.getInstance(OrderInfoService.class);
    private static final OrderInfoService INSTANCE = new OrderInfoService();

    private static final String REGEX = "^\\d+$";
    private static final Pattern PATTERN = compile(REGEX);

    private OrderInfoService() {
    }

    public static OrderInfoService defaultService() {
        return INSTANCE;
    }

    public OrderInfoData populateOrderData(HttpSession session, FDUserI user, FDOrderI order) {
        OrderInfoData orderData = new OrderInfoData();
        orderData.setOrderId(order.getErpSalesId());
        List<FDCartLineI> cartLines = order.getOrderLines();
        Map<SectionInfo, List<CartData.Item>> sectionMap = new HashMap<SectionInfo, List<CartData.Item>>();
        Map<String, String> sectionHeaderImgMap = new HashMap<String, String>();
        ItemCount itemCount = new ItemCount();
        Map<String, SectionInfo> sectionInfos = new HashMap<String, SectionInfo>();
        boolean isWineInCart = false;
        boolean hasEstimatedPriceItemInCart = false;

        final boolean isMakeGoodMode = user != null && user.getMasqueradeContext() != null && user.getMasqueradeContext().getMakeGoodFromOrderId() != null; // ==
        // SessionName.MAKEGOOD_COMPLAINT
        // NOTE variable below only makes sense in make-good order mode, otherwise it is null
        final ErpComplaintModel complaintModel = (ErpComplaintModel) session.getAttribute("fd.cc.makegoodComplaint"); // == SessionName.MAKEGOOD_COMPLAINT

        for (FDCartLineI cartLine : cartLines) {
            ProductModel productNode = cartLine.lookupProduct();
            if (productNode == null) {
                LOGGER.error("Failed to get product node for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping.");
                continue;
            }
            FDProduct fdProduct = cartLine.lookupFDProduct();
            if (fdProduct == null) {
                LOGGER.error("Failed to get fdproduct for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping.");
                continue;
            }
            String sectionInfoKey;
            if (cartLine.isWine()) {
                isWineInCart = true;
                sectionInfoKey = "wineSectionKey";
            } else {
                sectionInfoKey = cartLine.getDepartmentDesc();
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
                    sectionInfo.setSubTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getSubTotalOnlyWineAndSpirit(order)));
                    sectionInfo.setTaxTotal(JspMethods.formatPrice(FDCartModelService.defaultService().getTaxValueOnlyWineAndSpirit(order)));
                    orderData.setContainsWineSection(true);
                }
                sectionInfos.put(sectionInfoKey, sectionInfo);
            }
            sectionInfo.setSectionTitle(sectionInfoKey);
            sectionInfo.setExternalGroup(cartLine.getExternalGroup());
            sectionInfo.setRecipe(sectionInfoKey.contains("Recipe"));
            sectionInfo.setHasEstimatedPrice(sectionInfo.isHasEstimatedPrice() || cartLine.isEstimatedPrice());
            List<CartData.Item> sectionList = CartDataService.defaultService().loadDepartmentSectionList(sectionMap, sectionInfo);
            CartDataService.defaultService().loadSectionHeaderImage(sectionHeaderImgMap, productNode, sectionInfoKey);
            CartData.Item item = CartDataService.defaultService().populateCartDataItem(cartLine, fdProduct, itemCount, order, Collections.<Integer> emptySet(), productNode, user);

            if (isMakeGoodMode) {
                CartDataService.defaultService().populateCartDataItemWithMakeGoodAttributes(item, cartLine, complaintModel);
            }

            sectionList.add(item);
        }
        List<CartData.Section> sections = CartDataService.defaultService().populateCartDataSections(sectionMap, sectionHeaderImgMap);
        Collections.sort(sections, CartData.CART_DATA_SECTION_COMPARATOR_CHAIN_BY_WINE_FREE_SAMPLE_EXTERNAL_GROUP_TITLE);
        orderData.setCartSections(sections);
        CartDataService.defaultService().removeRecipePrefixFromSectionTitles(sections);
        CartDataService.defaultService().populateSubTotalBoxForNonAlcoholSections(order, sections, hasEstimatedPriceItemInCart,
                CartDataService.defaultService().getSubTotalTextForNonAlcoholicSections(isWineInCart, hasEstimatedPriceItemInCart));
        orderData.setItemCount(itemCount.getValue());
        orderData.setSubTotal(JspMethods.formatPrice(order.getSubTotal()));
        orderData.setEstimatedTotal(JspMethods.formatPrice(order.getTotal()));
        orderData.setTotalWithoutTax(JspMethods.formatPrice(order.getTotal() - order.getTaxValue()));
        return orderData;
    }

    public boolean isOrderValid(String orderId) {
        return PATTERN.matcher(orderId).matches();
    }

    public OrderInfoData populateProductPotato(FDUserI user, OrderInfoData order) {
        for (CartData.Section section : order.getCartSections()) {
            Map<String, ProductData> productPotatos = new HashMap<String, ProductData>();
            for (CartData.Item item : section.getCartLines()) {
                ProductData productPotato = ProductPotatoService.defaultService().getProductPotato(user, item.getProductId(), item.getCategoryId());
                if (productPotato != null) {
                    productPotatos.put(item.getProductId(), productPotato);
                }
            }
            section.setProducts(productPotatos);
        }
        return order;
    }

}
