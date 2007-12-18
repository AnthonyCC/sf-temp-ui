package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.jsp.tagext.*;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class VerticalPatternTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     *
     */
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[] {
            new VariableInfo(data.getAttributeString("id"),
                "com.freshdirect.fdstore.content.ContentNodeModel[]", true, VariableInfo.NESTED),
        };

    }

}
