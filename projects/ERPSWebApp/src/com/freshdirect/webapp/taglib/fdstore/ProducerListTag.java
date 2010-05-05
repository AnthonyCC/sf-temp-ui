package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProducerModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class ProducerListTag extends AbstractGetterTag<List<ProducerModel>> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    @Override
    protected List<ProducerModel> getResult() throws Exception {
        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCER);
        List<ProducerModel> result = new ArrayList<ProducerModel>(contentKeysByType.size());
        for (ContentKey key : contentKeysByType) {
            ProducerModel p = (ProducerModel) ContentFactory.getInstance().getContentNodeByKey(key);
            if (p.isActive()) {
//                if (p.getBrandCategory() != null) {
                result.add(p);
            }
        }
        return result;
    }
    
    
    public static class TagEI extends AbstractGetterTag.TagEI {

        @Override
        protected String getResultType() {
            return "java.util.List";
        }
        
    }

}
