package com.freshdirect.cms.fdstore;

import java.util.Date;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

public class DateIntervalValidator implements ContentValidatorI {

	public void validate(ContentValidationDelegate delegate,
			ContentServiceI service, ContentNodeI node, CmsRequestI request) {
		ContentType t = node.getKey().getType();
		
		if (FDContentTypes.YMAL_SET.equals(t) || FDContentTypes.STARTER_LIST.equals(t) || FDContentTypes.RECIPE.equals(t) ||
				(node.getAttribute("startDate") != null && node.getAttribute("endDate") != null) ) {
			Date sd = tryToGetDateAttribute(node, "startDate");
			Date ed = tryToGetDateAttribute(node, "endDate");
			
			if (sd == null ^ ed == null) {
				reportInconsistentFieldsProblem(delegate, node);
				return;
			}
			
			if (sd != null && ed != null && sd.after(ed)) {
				reportPrecedenceProblem(delegate, node);
				return;
			}
		}
	}



	private Date tryToGetDateAttribute(ContentNodeI node, String keyName) {
		Date aDate = null;
		try {
			AttributeI attr = node.getAttribute(keyName);
			if (attr != null) {
				aDate = (Date) attr.getValue();
			}
		} catch(Exception exc) {
			System.out.println("DateIntervalValidator.tryToGetDateAttribute(): failed to get attribute for key '" + keyName + "'; exc=" + exc);
		}
		return aDate;
	}
	
	
	
	
	private void reportPrecedenceProblem(ContentValidationDelegate delegate,
			ContentNodeI node) {
		delegate.record(node.getKey(), "startDate", "Start Date must precede End Date");
	}
	
	private void reportInconsistentFieldsProblem(ContentValidationDelegate delegate, ContentNodeI node) {
		delegate.record(node.getKey(), "startDate", "Date fields must either be empty or consistently filled.");
	}
}
