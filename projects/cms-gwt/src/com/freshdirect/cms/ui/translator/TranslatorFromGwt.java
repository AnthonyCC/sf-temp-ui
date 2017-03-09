package com.freshdirect.cms.ui.translator;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.widget.form.Time;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.CompositeContentNode;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Static class for converting client-side Gwt data types to server-side CMS data types.
 * 
 * @author treer
 */

public class TranslatorFromGwt {

    private static final Logger LOGGER = LoggerFactory.getInstance(TranslatorFromGwt.class);

    public static ContentNodeI getContentNode(GwtContentNode clientNode, DraftContext draftContext) {
        LOGGER.debug(MessageFormat.format("Starting translates gwtnode={0} draftContext={1}", clientNode, draftContext));

        ContentKey key = ContentKey.getContentKey(clientNode.getKey());
        ContentNodeI node = CmsManager.getInstance().getContentNode(key, draftContext);
        if (node == null) {
            // new node creation
            node = CmsManager.getInstance().createPrototypeContentNode(key, draftContext);
        }
        
        LOGGER.debug(MessageFormat.format("Copy node={0}", node));
        
        // calling 'copy' is essential !
        ContentNodeI copiedNode = node.copy();

        LOGGER.debug(MessageFormat.format("Copied node={0}", copiedNode));
        return copiedNode;
    }

    @SuppressWarnings("unchecked")
    public static Object getServerValue(Serializable value) {
        if (value instanceof ContentNodeModel) {
            return ContentKey.getContentKey(((ContentNodeModel) value).getKey());
        }
        if (value instanceof EnumModel) {
            return ((EnumModel) value).getKey();
        }
        if (value instanceof Time) {
            return ((Time) value).getDate();
        }
        if (value instanceof List) {
            List<Object> result = new ArrayList<Object>();
            for (Object item : (List<Object>) value) {
                result.add(getServerValue((Serializable) item));
            }
            return result;
        }
        return value;
    }

}
