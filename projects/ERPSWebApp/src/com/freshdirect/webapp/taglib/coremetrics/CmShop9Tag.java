package com.freshdirect.webapp.taglib.coremetrics;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.Shop9TagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.extradata.CoremetricsExtraData;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;
import com.freshdirect.fdstore.coremetrics.util.CoremetricsUtil;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmShop9Tag extends AbstractCmShopTag<Shop9TagModelBuilder> {

    public static final String PENDING_SHOP_9_MODELS = "pendingCoremetricsShop9Models";
    private static final Logger LOGGER = LoggerFactory.getInstance(AbstractCmShopTag.class);

    public CmShop9Tag() {
        tagModelBuilder = new Shop9TagModelBuilder();
    }

    @Override
    protected String getFunctionName() {
        return "cmCreateShopAction9Tag";
    }

    public static void buildPendingModels(HttpSession session, FDCartModel cart) {
        Shop9TagModelBuilder shop9TagModelBuilder = new Shop9TagModelBuilder();

        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        CoremetricsExtraData cmExtraData = new CoremetricsExtraData();
        cmExtraData.setCustomerType(CoremetricsUtil.defaultService().getCustomerTypeByOrderCount(user));

        shop9TagModelBuilder.setCoremetricsExtraData(cmExtraData);
        shop9TagModelBuilder.setCart(cart);

        try {
            session.setAttribute(PENDING_SHOP_9_MODELS, shop9TagModelBuilder.createTagModelPrototypesFromCart());
        } catch (SkipTagException e) {
            LOGGER.error("createTagModelPrototypesFromCart failed", e);
        }
    }

    @Override
    protected void appendTag(StringBuilder shopScriptSb, ShopTagModel tagModel) {
        shopScriptSb.append(getFormattedTag(toJsVar(tagModel.getProductId()), toJsVar(tagModel.getProductName()), toJsVar(tagModel.getQuantity()),
                toJsVar(tagModel.getUnitPrice()), toJsVar(tagModel.getRegistrationId()), toJsVar(tagModel.getOrderId()), toJsVar(tagModel.getOrderSubtotal()),
                toJsVar(tagModel.getCategoryId()), toJsVar(mapToAttrString(tagModel.getAttributesMaps()))));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initTag() {
        HttpSession session = getSession();
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

        CoremetricsExtraData cmExtraData = new CoremetricsExtraData();
        cmExtraData.setCustomerType(CoremetricsUtil.defaultService().getCustomerTypeByOrderCount(user));

        tagModelBuilder.setCoremetricsExtraData(cmExtraData);
        tagModelBuilder.setUser(user);
        tagModelBuilder.setTagModels((List<ShopTagModel>) session.getAttribute(PENDING_SHOP_9_MODELS));
        session.removeAttribute(PENDING_SHOP_9_MODELS);
    }

    public void setOrder(FDOrderI order) {
        tagModelBuilder.setOrder(order);
    }

    @Override
    protected boolean insertTagInCaseOfCrmContext() {
        return true;
    }
}
