package com.freshdirect.webapp.taglib.coremetrics;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.OrderTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.extradata.CoremetricsExtraData;
import com.freshdirect.fdstore.coremetrics.tagmodel.OrderTagModel;
import com.freshdirect.fdstore.coremetrics.util.CoremetricsUtil;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmOrderTag extends AbstractCmTag {

    private static final Logger LOGGER = LoggerFactory.getInstance(CmOrderTag.class);
    private FDOrderI order;

    @Override
    protected String getFunctionName() {
        return "cmCreateOrderTag";
    }

    @Override
    public String getTagJs() throws SkipTagException {

        FDUserI user = (FDUserI) getSession().getAttribute(SessionName.USER);

        if (order == null) {
            throw new SkipTagException("Order is null");
        }

        OrderTagModelBuilder builder = new OrderTagModelBuilder(order, user);

        CoremetricsExtraData cmExtraData = new CoremetricsExtraData();
        cmExtraData.setCustomerType(CoremetricsUtil.defaultService().getCustomerTypeByOrderCount(user));
        builder.setCoremetricsExtraData(cmExtraData);

        OrderTagModel model = builder.buildTagModel();

        String tagJs = getFormattedTag(toJsVar(model.getOrderId()), toJsVar(model.getOrderSubtotal()), toJsVar(model.getOrderShipping()), toJsVar(model.getRegistrationId()),
                toJsVar(model.getRegistrantCity()), toJsVar(model.getRegistrantState()), toJsVar(model.getRegistrantPostalCode()),
                toJsVar(mapToAttrString(model.getAttributesMaps())));

        LOGGER.debug(tagJs);
        return tagJs;
    }

    @Override
    protected boolean insertTagInCaseOfCrmContext() {
        return true;
    }

    public void setOrder(FDOrderI order) {
        this.order = order;
    }
}
