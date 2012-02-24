package com.freshdirect.cms.fdstore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.search.IBackgroundProcessor;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.framework.conf.FDRegistry;

public class SynonymValidator implements ContentValidatorI {

    public SynonymValidator() {
    }

    @Override
    public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode) {
        if (request == null) {
            // during publish we don't want to re-index recursively everything.
            return;
        }
        
        if (FDContentTypes.SYNONYM.equals(node.getKey().getType())) {
            Set<String> keywords = new HashSet<String>();
            String[] fromValues = SynonymDictionary.getSynonymFromValues(node);
            keywords.addAll(Arrays.asList(fromValues));
            if (oldNode != null) {
                fromValues = SynonymDictionary.getSynonymFromValues(oldNode);
                keywords.addAll(Arrays.asList(fromValues));
            }
            IBackgroundProcessor adminTool = (IBackgroundProcessor) FDRegistry.getInstance().getService(IBackgroundProcessor.class);
            adminTool.backgroundReindex(keywords);
        }
        if (FDContentTypes.FDFOLDER.equals(node.getKey().getType()) && SynonymDictionary.SYNONYM_LIST_KEY.equals(node.getKey().getId())) {
            IBackgroundProcessor adminTool = (IBackgroundProcessor) FDRegistry.getInstance().getService(IBackgroundProcessor.class);
            adminTool.backgroundReindex();
        }
    }

}
