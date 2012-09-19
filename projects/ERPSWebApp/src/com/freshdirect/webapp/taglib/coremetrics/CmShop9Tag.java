package com.freshdirect.webapp.taglib.coremetrics;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.Shop9TagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmShop9Tag extends AbstractCmShopTag<Shop9TagModelBuilder> {
	
	public static final String PENDING_SHOP_9_MODELS = "pendingCoremetricsShop9Models";
	private static final Logger LOGGER = LoggerFactory.getInstance(AbstractCmShopTag.class);
	private static final String SHOP_9_TAG_FS = "cmCreateShopAction9Tag(%s,%s,%s,%s,%s,%s,%s,%s,%s);";
	private HttpSession session;
	
	public CmShop9Tag() {
		tagModelBuilder = new Shop9TagModelBuilder();
	}
	
	public static void buildPendingModels(HttpSession session, FDCartModel cart){
		Shop9TagModelBuilder shop9TagModelBuilder = new Shop9TagModelBuilder();
		shop9TagModelBuilder.setCart(cart);
		
		try {
			session.setAttribute(PENDING_SHOP_9_MODELS, shop9TagModelBuilder.createTagModelPrototypesFromCart());
		} catch (SkipTagException e) {
			LOGGER.error("createTagModelPrototypesFromCart failed", e);
		}
	}
	
	protected void appendTag(StringBuilder shopScriptSb, ShopTagModel tagModel){
		shopScriptSb.append("\n").append(
				String.format(SHOP_9_TAG_FS, 
						toJsVar(tagModel.getProductId()), 
						toJsVar(tagModel.getProductName()), 
						toJsVar(tagModel.getQuantity()), 
						toJsVar(tagModel.getUnitPrice()), 
						toJsVar(tagModel.getRegistrationId()), 
						toJsVar(tagModel.getOrderId()), 
						toJsVar(tagModel.getOrderSubtotal()), 
						toJsVar(tagModel.getCategoryId()),
						toJsVar(mapToAttrString(tagModel.getAttributesMaps()))));
	}

	@SuppressWarnings("unchecked")
	protected void initTag(){
		PageContext ctx = (PageContext) getJspContext();
		session = ctx.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		tagModelBuilder.setUser(user);
		tagModelBuilder.setTagModels((List<ShopTagModel>) session.getAttribute(PENDING_SHOP_9_MODELS));
		session.removeAttribute(PENDING_SHOP_9_MODELS);
	}
	
	public void setOrder(FDOrderI order) {
		tagModelBuilder.setOrder(order);
	}

	protected boolean insertTagInCaseOfCrmContext(){
		return true;
	}
}
