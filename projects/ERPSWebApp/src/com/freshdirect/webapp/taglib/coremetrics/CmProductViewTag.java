package com.freshdirect.webapp.taglib.coremetrics;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.coremetrics.builder.ProductViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.AbstractTagModel;
import com.freshdirect.fdstore.coremetrics.tagmodel.ProductViewTagModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmProductViewTag extends AbstractCmTag {

    private static final Logger LOGGER = LoggerFactory.getInstance(CmProductViewTag.class);
    private ProductViewTagModelBuilder builder = new ProductViewTagModelBuilder();

    @Override
    protected String getFunctionName() {
        return "cmCreateProductviewTag";
    }

    @Override
    public String getTagJs() throws SkipTagException {

        builder.setVirtualCategoryId(extractVirtualCategoryId());
        ProductViewTagModel model = builder.buildTagModel();
        Object CM_VC = this.getRequest().getParameter("cm_vc");
        if (CM_VC == null || "".equals(CM_VC)) {
            CM_VC = model.getVirtualCategoryId();
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
        String virtualCategoryId = null;

        HttpServletRequest request = getRequest();

        String referer = request.getHeader("referer");
        int refererUriSeparatorLocation = StringUtils.ordinalIndexOf(referer, "/", 3);

        try {
            String refererURI = referer.substring(refererUriSeparatorLocation);

            QueryParameterCollection qv = QueryParameterCollection.decode(getRequest().getHeader("referer"));

            if (refererURI.equals("/index.jsp") || refererURI.equals("/")) {
                virtualCategoryId = "HOME_PAGE_CAROUSEL";
            } else {
                virtualCategoryId = qv.getParameterValue("cm_vc");
            }
        } catch (NullPointerException e) {
            LOGGER.debug("Referrer was empty, product page opened directly.", e);
        }

        return virtualCategoryId;
    }

    public void setProductModel(ProductModel productModel) {
        builder.setProductModel(productModel);
    }

    public void setQuickbuy(boolean quickbuy) {
        builder.setQuickbuy(quickbuy);
    }

}
