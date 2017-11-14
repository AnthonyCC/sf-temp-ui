/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.storeapi.attributes.cms;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.TitledMedia;

@CmsLegacy
public class ImageBuilder extends AbstractAttributeBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageBuilder.class);

    @Override
    public Object buildValue(Attribute aDef, Object value) {
        ContentKey mediaKey = (ContentKey) value;

        Media media = CmsManager.getInstance().getMedia(mediaKey);

        if (media == null) { return null; }

		String path = media.getUri();

		if (path == null) {
		    LOGGER.warn("ImageBuilder.buildValue(): Image without path " + media.getContentKey());
			path = mediaKey.getId();
		}
		// append last modify param
		String lastModify = ISODateTimeFormat.date().print(new DateTime(media.getLastModified()));
        path = path+"?lastModify="+lastModify;

		Object widthObj = media.getWidth();
		if (widthObj == null) {
		    LOGGER.warn("ImageBuilder.buildValue(): Image without width " + media.getContentKey());
			return new Image(path, 1, 1);
		}

		int width = media.getWidth().intValue();
		int height = media.getHeight().intValue();
        ContentNodeI mediaNode = CmsManager.getInstance().getContentNode(mediaKey);
		if (mediaNode == null || mediaNode.getAttributeValue("title") == null) {
			Image i = new Image(path, width, height);
			return i;
		}

		// Most probably null ...
		String title = mediaNode != null ? (String) mediaNode.getAttributeValue(ContentTypes.Html.title.getName()) : "";
		return new TitledMedia(new Image(path, width, height), title, null);
	}
}