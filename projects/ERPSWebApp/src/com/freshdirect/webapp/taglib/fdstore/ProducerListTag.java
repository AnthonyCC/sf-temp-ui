package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProducerModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class ProducerListTag extends AbstractGetterTag<List<ProducerModel>> {
    private static final long serialVersionUID = 1L;

    boolean needsValidGeolocation = false;
    boolean skipBodyOnEmptyResult = true;

    public void setNeedsValidGeolocation(boolean needsValidGeolocation) {
		this.needsValidGeolocation = needsValidGeolocation;
	}

    public void setSkipBodyOnEmptyResult(boolean skipBodyOnEmptyResult) {
		this.skipBodyOnEmptyResult = skipBodyOnEmptyResult;
	}
    
    @Override
    protected List<ProducerModel> getResult() throws Exception {
        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCER);
        List<ProducerModel> result = new ArrayList<ProducerModel>(contentKeysByType.size());
        for (ContentKey key : contentKeysByType) {
            ProducerModel p = (ProducerModel) ContentFactory.getInstance().getContentNodeByKey(key);
            if (p.isActive() && (!needsValidGeolocation || p.isAddressGeolocation())) {
                result.add(p);
            }
        }
        if (skipBodyOnEmptyResult && result.size()==0) {
            // returning null will results in SKIP_BODY
            return null;
        } else {
        	// Sort list alphabetically
        	Collections.sort(result, new Comparator<ProducerModel>() {
				@Override
				public int compare(ProducerModel o1, ProducerModel o2) {
					return o1.getFullName().compareTo(o2.getFullName());
				}
        	});
        	
            return result;
        }
    }
    
    
    public static class TagEI extends AbstractGetterTag.TagEI {

        @Override
        protected String getResultType() {
            return "java.util.List<com.freshdirect.fdstore.content.ProducerModel>";
        }
        
    }

}
