package com.freshdirect.cms.application.draftchange.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cmsadmin.domain.DraftChange;

public class DraftChangeToContentNodeApplicator {

    public static final String SEPARATOR = "|";

    private DraftChangeToContentNodeApplicator() {
    }

    public static ContentNodeI applyDraftChangeToNode(DraftChange draftChange, ContentNodeI node) {
        final ContentTypeDefI typeDef = node.getDefinition();
        final String attributeName = draftChange.getAttributeName();
        AttributeDefI attrDef = typeDef.getAttributeDef(attributeName);

        if (draftChange.getValue() == null) {
            node.setAttributeValue(attributeName, null);
        } else {

            if (attrDef instanceof RelationshipDefI) {
                List<ContentKey> keys = getContentKeysFromRelationshipValue((RelationshipDefI) attrDef, draftChange.getValue());
                if (attrDef.isCardinalityOne()) {
                    node.setAttributeValue(attributeName, keys.isEmpty() ? null : keys.get(0));
                } else {
                    // This sets the value of OneToManyRelation fields, based on the draftchange attribute.
                    node.setAttributeValue(attributeName, keys);
                }
            } else if (attrDef instanceof EnumDefI) {
                EnumDefI enumDef = (EnumDefI) attrDef;

                if (enumDef.isCardinalityOne()) {
                    final Object value = ContentTypeUtil.convertAttributeValues(enumDef, Arrays.asList(draftChange.getValue()));
                    node.setAttributeValue(attributeName, value);
                } else {
                    // NOTE: theoretically, enums can also have more cardinality. However, this case is never used
                }
            } else {
                node.setAttributeValue(attributeName, ContentTypeUtil.coerce(attrDef.getAttributeType(), draftChange.getValue()));
            }
        }
        return node;
    }

    public static List<ContentKey> getContentKeysFromRelationshipValue(RelationshipDefI attrDef, String value) {
        List<ContentKey> result = new ArrayList<ContentKey>();
        if (attrDef.isCardinalityOne()) {
            result.add(ContentKey.decode(value));
        } else {
            String[] changedKeys = StringUtils.split(value, SEPARATOR);

            if (changedKeys != null) {
                for (int i = 0; i < changedKeys.length; i++) {
                    result.add(ContentKey.decode(changedKeys[i]));
                }
            }

        }
        return result;
    }

    public static ContentNodeI createContentNodeFromDraftChange(DraftChange draftChange, ContentServiceI contentService, DraftContext draftContext) {
        ContentKey key = ContentKey.decode(draftChange.getContentKey());
        ContentNodeI node = contentService.createPrototypeContentNode(key, draftContext);
        node = applyDraftChangeToNode(draftChange, node);
        return node;
    }
}
