package com.freshdirect.cms.fdstore;

import java.util.Date;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

public class DateIntervalValidator implements ContentValidatorI {

    @Override
	public void validate( ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode ) {
		ContentType t = node.getKey().getType();

		if ( FDContentTypes.YMAL_SET.equals( t ) || FDContentTypes.STARTER_LIST.equals( t )
				|| FDContentTypes.RECIPE.equals( t )
				|| ( node.getAttributeValue( "startDate" ) != null && node.getAttributeValue( "endDate" ) != null ) ) {
			
			Date sd = tryToGetDateAttribute( node, "startDate" );
			Date ed = tryToGetDateAttribute( node, "endDate" );

			// if both date fields are filled in : start date must precede end date
			if ( sd != null && ed != null && sd.after( ed ) ) {
				reportPrecedenceProblem( delegate, node );
				return;
			}
		}
	}

	private Date tryToGetDateAttribute( ContentNodeI node, String keyName ) {
            Object obj = node.getAttributeValue(keyName);
            if (obj instanceof Date) {
                return (Date) obj;
            }
            return null;
	}	

	
	private void reportPrecedenceProblem( ContentValidationDelegate delegate, ContentNodeI node ) {
		delegate.record( node.getKey(), "startDate", "Start Date must precede End Date" );
	}
	
}
