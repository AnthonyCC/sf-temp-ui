package com.freshdirect.webapp.taglib.coremetrics;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.OrderTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.OrderTagModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmOrderTag extends AbstractCmTag {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(CmOrderTag.class);
	private static final String ORDER_TAG_FS = "cmCreateOrderTag(%s,%s,%s,%s,%s,%s,%s,%s);";
	
	private FDOrderI order;

	@Override
	protected String getTagJs() throws SkipTagException {
		
		PageContext ctx = (PageContext) getJspContext();
		HttpSession session = ctx.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);		
		
		if( order == null ){
			throw new SkipTagException("Order is null");
		}

		OrderTagModelBuilder builder = new OrderTagModelBuilder(order, user);
		OrderTagModel model = builder.buildTagModel();
		
		String setOrderScript=String.format(ORDER_TAG_FS,
				toJsVar(model.getOrderId()),
				toJsVar(model.getOrderSubtotal()),
				toJsVar(model.getOrderShipping()),
				toJsVar(model.getRegistrationId()),
				toJsVar(model.getRegistrantCity()),
				toJsVar(model.getRegistrantState()),
				toJsVar(model.getRegistrantPostalCode()),
				toJsVar(mapToAttrString(model.getAttributesMaps())));
		
		LOGGER.debug(setOrderScript);
		return setOrderScript;
	}

	protected boolean insertTagInCaseOfCrmContext(){
		return true;
	}
	
	public void setOrder(FDOrderI order) {
		this.order = order;
	}
}
