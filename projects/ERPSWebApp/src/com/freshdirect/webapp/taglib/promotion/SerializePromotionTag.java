package com.freshdirect.webapp.taglib.promotion;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.util.json.FDPromotionJSONSerializer;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.metaparadigm.jsonrpc.JSONSerializer;

public class SerializePromotionTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	FDPromotionNewModel promo;
	
	public void setPromo(FDPromotionNewModel promo) {
		this.promo = promo;
	}

	@Override
	public int doStartTag() throws JspException {
		JSONSerializer ser = new JSONSerializer();
		
		try {
			ser.registerDefaultSerializers();
			ser.registerSerializer(FDPromotionJSONSerializer.getInstance());
			
			String serializedPromotionObject = ser.toJSON(this.promo);
			
			// write string out
			pageContext.getOut().append(serializedPromotionObject);
		} catch (Exception e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}


	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}
