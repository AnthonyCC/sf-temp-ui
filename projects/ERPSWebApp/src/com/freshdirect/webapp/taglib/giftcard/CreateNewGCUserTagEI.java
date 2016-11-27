package com.freshdirect.webapp.taglib.giftcard;


import javax.servlet.jsp.tagext.*;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class CreateNewGCUserTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     *
     */
    public VariableInfo[] getVariableInfo(TagData data) {

        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("result"),
                "com.freshdirect.framework.webapp.ActionResult",true, VariableInfo.NESTED)
        };

    }

}
