package com.freshdirect.webapp.soy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspMethods;


public class CartConfirmPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( CartConfirmPotatoTag.class );
		
	private String name;
	private String cartlineId;
	
	public String getName() {
		return name;
	}	
	public void setName( String name ) {
		this.name = name;
	}
	
	public String getCartlineId() {
		return cartlineId;
	}	
	public void setCartlineId( String cartlineId ) {
		this.cartlineId = cartlineId;
	}

	@Override
	public void doTag() throws JspException {
		
		LOGGER.info( "Creating data potato: " + name );
		
		FDUserI user = (FDUserI)((PageContext)getJspContext()).getSession().getAttribute( SessionName.USER );
		
		List<Map<String,?>> dataList = new ArrayList<Map<String,?>>();
		double subTotal = 0;
		String backUrl=null;
		FDCartModel cart = user.getShoppingCart();
		
		if(cartlineId==null || "".equals(cartlineId)){
			
			List<FDCartLineI> recentLines = cart.getRecentOrderLines();
			
			for(FDCartLineI line : recentLines){
				Map<String, ?> potato = DataPotatoField.digCartConfirm( user, line.getRandomId()+"");
				subTotal = cart.getSubTotal();
				dataList.add(potato);
				
				//TODO check back url with business
				if(backUrl==null){
					backUrl=(String)potato.get("backUrl");
				}
			}
			
		}else{
			
			Map<String, ?> potato = DataPotatoField.digCartConfirm( user, cartlineId );
			subTotal = cart.getSubTotal();
			//TODO check back url with business
			if(potato!=null)
			backUrl=(String)potato.get("backUrl");
			
			dataList.add(DataPotatoField.digCartConfirm( user, cartlineId ));
		}
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("cartConfirmPotatoes", dataList);
		dataMap.put("backUrl", backUrl);
		dataMap.put("subTotal", JspMethods.formatPrice( subTotal ));
		dataMap.put("isModifyingOrder", FDUserUtil.getModifyingOrder(user) != null);

		((PageContext)getJspContext()).setAttribute( name, dataMap );
		
	}
	
	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
		@Override
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "name" ),
	            		"java.util.Map<String,Object>",
	            		true, 
	            		VariableInfo.AT_BEGIN )
	        };
	    }
	}
	
}
