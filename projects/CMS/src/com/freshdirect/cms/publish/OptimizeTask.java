package com.freshdirect.cms.publish;

import com.freshdirect.cms.search.ContentSearchServiceI;

public class OptimizeTask implements PublishTask {
	ContentSearchServiceI searchService;
	
	public OptimizeTask(ContentSearchServiceI searchService) {
		super();
		this.searchService = searchService;
	}

	public void execute(Publish publish) {
		searchService.optimize();
	}

	public String getComment() {
		return "Optimizing Lucene indexes";
	}
}
