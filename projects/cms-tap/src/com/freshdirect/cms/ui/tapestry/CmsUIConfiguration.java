package com.freshdirect.cms.ui.tapestry;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.search.SearchRelevancyList;
import com.freshdirect.cms.search.SynonymDictionary;

/**
 * Configuration parameters for CMS UI.
 * TODO Make this a hivemodule
 */
public class CmsUIConfiguration {


	private final static CmsUIConfiguration INSTANCE = new CmsUIConfiguration();

	private final static Set DEFAULT_ROOTS = new HashSet();
	static {
		DEFAULT_ROOTS.add(ContentKey.decode("Store:FreshDirect"));
		DEFAULT_ROOTS.add(ContentKey.decode("MediaFolder:/"));
		DEFAULT_ROOTS.add(ContentKey.decode("CmsFolder:forms"));
		DEFAULT_ROOTS.add(ContentKey.decode("CmsQueryFolder:queries"));
		DEFAULT_ROOTS.add(ContentKey.decode("CmsQuery:unreachable"));
		DEFAULT_ROOTS.add(ContentKey.decode("CmsQuery:orphans"));
		DEFAULT_ROOTS.add(ContentKey.decode("CmsQuery:ocfCampaigns"));
		DEFAULT_ROOTS.add(ContentKey.decode("FDFolder:recipes"));
		DEFAULT_ROOTS.add(ContentKey.decode("FDFolder:ymals"));
		DEFAULT_ROOTS.add(ContentKey.decode("FDFolder:starterLists"));
		DEFAULT_ROOTS.add(ContentKey.decode(SynonymDictionary.SYNONYM_LIST_KEY));
		DEFAULT_ROOTS.add(ContentKey.decode(SearchRelevancyList.SEARCH_RELEVANCY_KEY));
	}

	private CmsUIConfiguration() {
	}

	public static CmsUIConfiguration getInstance() {
		return INSTANCE;
	}

	public Set getTreeRootKeys() {
		return DEFAULT_ROOTS;
	}

}
