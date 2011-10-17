package com.freshdirect.cms.fdstore;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.labels.ILabelProvider;

public class MenuItemLabelProvider implements ILabelProvider {

    @Override
    public String getLabel(ContentNodeI node) {
        if (!FDContentTypes.MENU_ITEM.equals(node.getKey().getType())) {
            return null;
        }
        Object value = node.getAttributeValue("FULL_NAME");
        if (value instanceof String && !((String)value).trim().isEmpty()) {
            return (String) value;
        }
        Object linked = node.getAttributeValue("linked");
        if (linked instanceof ContentKey) {
            ContentNodeI node2 = CmsManager.getInstance().getContentNode((ContentKey) linked);
            if (node2 != null) {
                Object linkedName = node2.getAttributeValue("FULL_NAME");
                if (linkedName instanceof String) {
                    return (String) linkedName;
                }
            }
        }
        return node.getKey().getId();
    }

}
