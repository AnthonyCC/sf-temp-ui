package com.freshdirect.cms.fdstore;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.labels.ILabelProvider;
import com.freshdirect.cms.node.ContentNodeUtil;

public class SearchRelevancyLabelProvider implements ILabelProvider {

    @Override
	public String getLabel(ContentNodeI node, ContentServiceI contentService, DraftContext draftContext) {
		ContentType type = node.getKey().getType();

		if (FDContentTypes.SEARCH_RELEVANCY_LIST.equals(type)) {
			return "Search for:" + node.getAttributeValue("Keywords");
		}
		if (FDContentTypes.SEARCH_RELEVANCY_HINT.equals(type)) {
			ContentKey key = (ContentKey) node.getAttributeValue("category");
			return "Category " + (key != null ? key.getId() : "<not specified>") + " score : " + node.getAttributeValue("score");
		}
		if (FDContentTypes.SYNONYM.equals(type)) {
			String word = ContentNodeUtil.getStringAttribute(node, "word");
			String synonymValue = ContentNodeUtil.getStringAttribute(node, "synonymValue");
			return "Synonyms of '" + word + "': '" + synonymValue + "'";
		}
		if (FDContentTypes.SPELLING_SYNONYM.equals(type)) {
			String word = ContentNodeUtil.getStringAttribute(node, "word");
			String synonymValue = ContentNodeUtil.getStringAttribute(node, "synonymValue");
			return "Spelling synonyms of '" + word + "': '" + synonymValue + "'";
		}
		if (FDContentTypes.WORD_STEMMING_EXCEPTION.equals(type)) {
			String word = ContentNodeUtil.getStringAttribute(node, "word");
			return "DEPRECATED Bad singular form : " + word;
		}
		return null;
	}

}
