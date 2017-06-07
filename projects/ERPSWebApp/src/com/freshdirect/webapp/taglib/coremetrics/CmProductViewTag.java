package com.freshdirect.webapp.taglib.coremetrics;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.builder.ProductViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.extradata.CoremetricsExtraData;
import com.freshdirect.fdstore.coremetrics.tagmodel.AbstractTagModel;
import com.freshdirect.fdstore.coremetrics.tagmodel.ProductViewTagModel;
import com.freshdirect.fdstore.coremetrics.util.CoremetricsUtil;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmProductViewTag extends AbstractCmTag {

    private static final Logger LOGGER = LoggerFactory.getInstance(CmProductViewTag.class);
    private ProductViewTagModelBuilder builder = new ProductViewTagModelBuilder();

    @Override
    protected String getFunctionName() {
        return "cmCreateProductviewTag";
    }

    @Override
    public String getTagJs() throws SkipTagException {
        FDUserI user = (FDUserI) getSession().getAttribute(SessionName.USER);

        builder.setVirtualCategoryId(extractVirtualCategoryId());

        CoremetricsExtraData cmExtraData = new CoremetricsExtraData();
        cmExtraData.setCustomerType(CoremetricsUtil.defaultService().getCustomerTypeByOrderCount(user));
        builder.setCoremetricsExtraData(cmExtraData);

        ProductViewTagModel model = builder.buildTagModel();
        Object CM_VC = this.getRequest().getParameter("cm_vc");
        if (CM_VC == null || "".equals(CM_VC)) {
            CM_VC = model.getVirtualCategoryId();
        }

        if (CM_VC != null || !"".equals(CM_VC)) {
            CM_VC = CmContext.getContext().prefixedCategoryId((String) CM_VC);
        }

        String tagJs = getFormattedTag(toJsVar(model.getProductId()), toJsVar(model.getProductName()), toJsVar(CM_VC == null ? model.getCategoryId() : CM_VC),
                toJsVar(mapToAttrString(model.getAttributesMaps())) + decorateFromCoremetricsTrackingObject(), toJsVar(model.getVirtualCategoryId()));

        LOGGER.debug(tagJs);
        return tagJs;
    }

    private String decorateFromCoremetricsTrackingObject() {

        StringBuilder sb = new StringBuilder();
        sb.append(" + \"" + AbstractTagModel.ATTR_DELIMITER + "\" + ");
        sb.append(CmFieldDecoratorTag.CM_PAGE_CONTENT_HIERARCHY);
        sb.append(" + \"" + AbstractTagModel.ATTR_DELIMITER + "\" + ");
        sb.append(CmFieldDecoratorTag.CM_PAGE_ID);
        return sb.toString();

    }

    private String extractVirtualCategoryId() {
        QueryParameterCollection qv = QueryParameterCollection.decode(getRequest().getHeader("referer"));
        return qv.getParameterValue("cm_vc");
    }

    public void setProductModel(ProductModel productModel) {
        builder.setProductModel(productModel);
    }

    public void setQuickbuy(boolean quickbuy) {
        builder.setQuickbuy(quickbuy);
    }

}
