package com.freshdirect.storeapi.attributes.cms;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.Html;
import com.freshdirect.storeapi.content.TitledMedia;

@CmsLegacy
public class HtmlBuilder extends AbstractAttributeBuilder {

    @Override
    public Object buildValue(Attribute aDef, Object value) {
        ContentKey contentKey = (ContentKey) value;

        TitledMedia result = null;

        ContentNodeI mediaNode = CmsManager.getInstance().getContentNode(contentKey);
        Media media = CmsManager.getInstance().getMedia(contentKey);
        String title = getAttributeValue(mediaNode, ContentTypes.Html.title.getName());
        String sizeName = getAttributeValue(mediaNode, ContentTypes.Html.popupSize.getName());
        if (media != null) {
            result = new TitledMedia(new Html(media.getUri()), title, sizeName);
        }
        return result;
    }

    private String getAttributeValue(ContentNodeI node, String attribute) {
        StringBuilder value = new StringBuilder();
        if (node != null) {
            value.append(node.getAttributeValue(attribute));
        }
        return value.toString();
    }
}
