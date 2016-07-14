package com.freshdirect.cms.util;

import java.util.Collection;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class CustomRuleValidator {

    public static final CustomPermissionRule FOODKICK_FEED_RULE = new CustomPermissionRule("FDFolder:Feed_fdx", FDContentTypes.FDFOLDER, FDContentTypes.WEBPAGE);
    public static final CustomPermissionRule FOODKICK_PICKLIST_RULE = new CustomPermissionRule("FDFolder:PickList_fdx", FDContentTypes.FDFOLDER, FDContentTypes.WEBPAGE);
    public static final CustomPermissionRule FOODKICK_SCHEDULE_RULE = new CustomPermissionRule("FDFolder:Schedule_fdx", FDContentTypes.FDFOLDER, FDContentTypes.SCHEDULE);
    public static final CustomPermissionRule FOODKICK_SECTION_RULE = new CustomPermissionRule("FDFolder:Section_fdx", FDContentTypes.FDFOLDER, FDContentTypes.SECTION);
    public static final CustomPermissionRule FOODKICK_DARKSTORE_RULE = new CustomPermissionRule("FDFolder:DarkStore_fdx", FDContentTypes.FDFOLDER, FDContentTypes.DARKSTORE);

    private static final CustomRuleValidator INSTANCE = new CustomRuleValidator();

    private CustomRuleValidator() {
    }

    public static CustomRuleValidator getInstance() {
        return INSTANCE;
    }

    public boolean validate(ContentNodeI node, ContentType relatedType, Collection<CustomPermissionRule> rules) {
        boolean isValid = false;
        for (CustomPermissionRule rule : rules) {
            boolean isKeyMatched = rule.getContentKey().equals(node.getKey().getEncoded());
            boolean isTypeMatched = rule.getNodeType().equals(node.getKey().getType());
            boolean isRelatedTypeMatched = rule.getRelatedNodeType().equals(relatedType);
            if (isKeyMatched && isTypeMatched && isRelatedTypeMatched) {
                isValid = true;
            }
        }
        return isValid;
    }

}
