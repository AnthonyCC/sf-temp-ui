package com.freshdirect.cms.fdstore;

import java.util.Collection;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.labels.ILabelProvider;

public class TileLabelProvider implements ILabelProvider {

    @Override
    public String getLabel(ContentNodeI node) {
        if (FDContentTypes.TILE.equals(node.getKey().getType())) {
            return "Tile[" + node.getKey().getId() + ", media:"+formatContentKey(node.getAttributeValue("media"))+']';
        }
        if (FDContentTypes.TILE_LIST.equals(node.getKey().getType())) {
            return "TileList[" + node.getKey().getId() + ", filter:" + formatContentKey(node.getAttributeValue("filter")) + ']';
        }
        return null;
    }

    String formatContentKey(Object obj) {
        if (obj instanceof Collection) {
            StringBuilder s = new StringBuilder("[");
            boolean first = true;
            for (Object o : (Collection) obj) {
                if (!first) {
                    s.append(',');
                } else {
                    first = false;
                }
                s.append(formatContentKey(o));
            }
            s.append(']');
            return s.toString();
        }
        if (obj instanceof ContentKey) {
            return ((ContentKey)obj).getType().getName() + ':' + ((ContentKey)obj).getId(); 
        }
        return "";
    }
    
}
