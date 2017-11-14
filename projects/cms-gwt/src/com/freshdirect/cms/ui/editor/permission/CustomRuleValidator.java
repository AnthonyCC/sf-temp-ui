package com.freshdirect.cms.ui.editor.permission;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;

@Service
public class CustomRuleValidator {

    public static final CustomPermissionRule FOODKICK_FEED_RULE = new CustomPermissionRule(ContentKeyFactory.get("FDFolder:Feed_fdx"), ContentType.FDFolder, ContentType.WebPage);
    public static final CustomPermissionRule FOODKICK_PICKLIST_RULE = new CustomPermissionRule(ContentKeyFactory.get("FDFolder:PickList_fdx"), ContentType.FDFolder,
            ContentType.WebPage);
    public static final CustomPermissionRule FOODKICK_SCHEDULE_RULE = new CustomPermissionRule(ContentKeyFactory.get("FDFolder:Schedule_fdx"), ContentType.FDFolder,
            ContentType.Schedule);
    public static final CustomPermissionRule FOODKICK_SECTION_RULE = new CustomPermissionRule(ContentKeyFactory.get("FDFolder:Section_fdx"), ContentType.FDFolder,
            ContentType.Section);
    public static final CustomPermissionRule FOODKICK_DARKSTORE_RULE = new CustomPermissionRule(ContentKeyFactory.get("FDFolder:DarkStore_fdx"), ContentType.FDFolder,
            ContentType.DarkStore);

    private static final CustomRuleValidator INSTANCE = new CustomRuleValidator();

    public static CustomRuleValidator getInstance() {
        return INSTANCE;
    }

    private CustomRuleValidator() {
    }

    public boolean validate(ContentKey nodeKey, ContentType relatedType, Collection<CustomPermissionRule> rules) {
        boolean isValid = false;
        for (CustomPermissionRule rule : rules) {
            boolean isKeyMatched = rule.getContentKey().equals(nodeKey);
            boolean isTypeMatched = rule.getNodeType().equals(nodeKey.type);
            boolean isRelatedTypeMatched = rule.getRelatedNodeType().equals(relatedType);
            if (isKeyMatched && isTypeMatched && isRelatedTypeMatched) {
                isValid = true;
            }
        }
        return isValid;
    }

}
