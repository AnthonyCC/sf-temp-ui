package com.freshdirect.webapp.ajax.filtering;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.SectionContext;

public class ProductItemComparatorUtil {
	private static final Logger LOGGER = LoggerFactory.getInstance(ProductItemComparatorUtil.class);

	public static void sortSectionDatas(BrowseDataContext data, Comparator<FilteringProductItem> comparator) {
	
		if (comparator == null) {
			LOGGER.error("No sorter given, leave collections in natural order");
			return;
		}

		for(SectionContext context : data.getSectionContexts()){
			sortSectionContext(context, comparator);
		}
	}
	
	private static void sortSectionContext(SectionContext context, Comparator<FilteringProductItem> comparator){
		List<SectionContext> subSectionContexts = context.getSectionContexts();
		List<FilteringProductItem> productItems = context.getProductItems();

		if (subSectionContexts==null || subSectionContexts.size()==0){
			if(productItems!=null){
				Collections.sort(productItems, comparator);
			}
		} else {
			for (SectionContext subSectionContext : subSectionContexts){
				sortSectionContext(subSectionContext, comparator);
			}
		}
		
	}
}
