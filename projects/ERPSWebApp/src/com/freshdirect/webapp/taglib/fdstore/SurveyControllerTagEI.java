/*
 * SurveyControllerTagEI.java
 *
 * Created on March 7, 2002, 11:16 AM
 */

package com.freshdirect.webapp.taglib.fdstore;

/**
 *
 * @author  knadeem
 * @version 
 */

import javax.servlet.jsp.tagext.*;

public class SurveyControllerTagEI extends TagExtraInfo {

	public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("resultName"),
                            "com.freshdirect.framework.webapp.ActionResult",
                            true,
                            VariableInfo.NESTED),
			new VariableInfo(data.getAttributeString("formName"), "com.freshdirect.webapp.taglib.fdstore.SurveyControllerTag.SignupSurveyForm",
							true, VariableInfo.NESTED)
        };
    }

}
