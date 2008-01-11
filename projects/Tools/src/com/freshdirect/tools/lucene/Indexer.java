package com.freshdirect.tools.lucene;

import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.framework.conf.FDRegistry;


/**
 * A command line tool to perform a search (thus checking indexes and rebuilding them if necessary).
 * To force rebuild, remove the index directory first.
 * @author istvan
 */
public class Indexer {
	
	private ContentSearchServiceI service;
	
	Indexer() {
		ContentFactory.getInstance();
		service = (ContentSearchServiceI) FDRegistry.getInstance().getService(ContentSearchServiceI.class);
		service.search("zizi",1);
	}
	
	public static void main(String argv[]) {
		System.out.println("Started ...");
		new Indexer();
		System.out.println("Finished");
	}
}
