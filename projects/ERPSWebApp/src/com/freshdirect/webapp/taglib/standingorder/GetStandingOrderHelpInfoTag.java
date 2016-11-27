package com.freshdirect.webapp.taglib.standingorder;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.builder.ConversionEventTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.coremetrics.CmConversionEventTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetStandingOrderHelpInfoTag extends AbstractGetterTag<String> {
	private static final long serialVersionUID = -5753626070767051722L;
	private static final Logger LOGGER = LoggerFactory.getInstance(GetStandingOrderHelpInfoTag.class);
	private FDStandingOrder so;
	
	@Override
	protected String getResult() throws Exception {
        HttpSession session = pageContext.getSession();
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        
		ErpCustomerInfoModel custInfo = FDCustomerFactory.getErpCustomerInfo(user.getIdentity());
		String email = StringUtil.escapeJavaScript(custInfo.getEmail());
		String firstName =  StringUtil.escapeJavaScript(custInfo.getFirstName());
		String lastName =  StringUtil.escapeJavaScript(custInfo.getLastName());

		StringBuilder result = new StringBuilder("{");
		result.append("custName:'").append((firstName==null ? "" : firstName+" ") + (lastName==null ? "" : lastName)).append("',");
		result.append("custEmail:'").append(email).append("',");
		result.append("standingOrderCsPhone:'").append(FDStoreProperties.getStandingOrderCsPhone()).append("',");
		
		String custPhone = null;
		String companyName = null;
		
		if (so != null){
			ErpAddressModel addr = so.getDeliveryAddress();
			custPhone = getPhoneFromAddr(addr);
			companyName = getCompanyFromAddr(addr);
			appendSoInfo(result);
		}

		if (custPhone == null || "".equals(custPhone) || companyName == null || "".equals(companyName)){
			ErpAddressModel addr = user.getShoppingCart().getDeliveryAddress();
			custPhone = getPhoneFromAddr(addr);
			companyName = getCompanyFromAddr(addr);
		}
		
		result.append("custPhone:'").append(custPhone==null? "" : StringUtil.escapeJavaScript(custPhone)).append("',");
		result.append("companyName:'").append(companyName==null ? "" : StringUtil.escapeJavaScript(companyName)).append("',");
		result.append("sourcePage:'").append(request.getServletPath());
		
		String queryString = request.getQueryString();
		if (queryString != null){
			result.append("?").append(queryString);
		}
		result.append("',");

	
		CmConversionEventTag conversionEventTag = new CmConversionEventTag();
		conversionEventTag.setJspContext(pageContext);
		conversionEventTag.setOnlyFormatModel(true);
		conversionEventTag.setEventId(ConversionEventTagModelBuilder.EVENT_SO_HELP);
		conversionEventTag.setUrl(request.getRequestURI());
		conversionEventTag.setFirstPhase(true);
		String conversionEventStartJs = conversionEventTag.getTagOutput();
		result.append("onOpen: function() { ").append(conversionEventStartJs).append(" },");

		CmConversionEventTag conversionEventTag2 = new CmConversionEventTag();
		conversionEventTag2.setJspContext(pageContext);
		conversionEventTag2.setOnlyFormatModel(true);
		conversionEventTag2.setEventId(ConversionEventTagModelBuilder.EVENT_SO_HELP);
		conversionEventTag2.setUrl(request.getRequestURI());
		String conversionEventEndJs = conversionEventTag2.getTagOutput();
		result.append("onSubmit: function() { ").append(conversionEventEndJs).append(" }");
		
		return result.append("}").toString();
	}

	private String getPhoneFromAddr(ErpAddressModel addr){
		if (addr == null){
			return null;
		}
		
		PhoneNumber phoneNumber = addr.getPhone();
		String extension = phoneNumber.getExtension();
		String phoneFull = phoneNumber.getPhone();
		
		if ( !(extension==null || "".equals(extension))){
			phoneFull += " Ext. " + extension;
		}
		
		return phoneFull;
	}

	private String getCompanyFromAddr(ErpAddressModel addr){
		return addr==null ? null : addr.getCompanyName();
	}

	private void appendSoInfo(StringBuilder result) throws FDResourceException{
		result.append("entityLabel:'Standing Order',");
		result.append("entityId:'").append(StringUtil.escapeJavaScript(so.getCustomerListName())).append(" (").append(so.getId()).append(")',");
	}
	
	
	public void setSo(FDStandingOrder so){
		this.so = so;
	}
	
    public static class TagEI extends AbstractGetterTag.TagEI {
        @Override
		protected String getResultType() {
            return "java.lang.String";
        }
    }
}
