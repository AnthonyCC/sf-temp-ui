package com.freshdirect.webapp.ajax.browse.paging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.PagerData;
import com.freshdirect.webapp.ajax.browse.data.SectionData;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;

public class BrowseDataPagerHelper {

	private static final String CURRENT_PAGE_LAST_ITEM_INDEX = "currentPageLastItemIndex";
	private static final String MARKED_FOR_REMOVAL = "markedForRemoval";
	private static final String CURRENT_PAGE_FIRST_ITEM_INDEX = "currentPageFirstItemIndex";
	private static final String LOGICAL_ITEM_COUNT = "logicalItemCount";
	private static final String ITEM_COUNT = "itemCount";
	private static final String LAST_ITEM_INDEX = "lastItemIndex";
	private static final String FIRST_ITEM_INDEX = "firstItemIndex";
	private static final String SPACER_COUNT = "spacerCount";

	/**
	 * Calculates item and page indexes based on context objects.
	 * 
	 * @param browseData
	 * @param cmsFilteringNavigator
	 * @return
	 */
	public static void createPagerContext(BrowseData browseData, CmsFilteringNavigator cmsFilteringNavigator, FDUserI user) {
		
		PagerData pagerData = new PagerData();
		pagerData.setPageSize(cmsFilteringNavigator.getPageSize());
		pagerData.setActivePage(cmsFilteringNavigator.getActivePage());
		pagerData.setAll(cmsFilteringNavigator.getActivePage()==0);
		
		Map<String, Integer> fetchResults = new HashMap<String, Integer>();
		
		//fragmented lines count as full lines at section changes so this value is a multiple of PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD  
		fetchResults.put(LOGICAL_ITEM_COUNT, 0);
		fetchResults.put(ITEM_COUNT, 0);
		fetchResults.put(SPACER_COUNT, 0);
		
		fetchResults.put(CURRENT_PAGE_FIRST_ITEM_INDEX, (pagerData.getActivePage() - 1) * pagerData.getPageSize() + 1); 
		fetchResults.put(CURRENT_PAGE_LAST_ITEM_INDEX, pagerData.getActivePage() * pagerData.getPageSize());
		
		//here we keep track of the actual item indexes throughout the fetching, accumulating them by each cycle
		fetchResults.put(FIRST_ITEM_INDEX, 1); 
		fetchResults.put(LAST_ITEM_INDEX, 0);

		Iterator<SectionData> sectionDataIterator = browseData.getSections().getSections().iterator();
		
		while (sectionDataIterator.hasNext()) {
			fetchResults = fetchSectionData(sectionDataIterator.next(), fetchResults, pagerData.isAll(), cmsFilteringNavigator, user);
		}
		
		pagerData.setItemCount(fetchResults.get(ITEM_COUNT));
		
		/* remove spacers from total count */
		pagerData.setItemCount(fetchResults.get(ITEM_COUNT) - fetchResults.get(SPACER_COUNT));
		
		
		
		if (pagerData.isAll()) {
			pagerData.setPageCount(0);
			pagerData.setFirstItemIndex(1);
			pagerData.setLastItemIndex(pagerData.getItemCount());
		} else {
			pagerData.setPageCount(Math.max(0, fetchResults.get(LOGICAL_ITEM_COUNT) - 1 - fetchResults.get(SPACER_COUNT)) / pagerData.getPageSize() + 1);
			pagerData.setLastItemIndex(fetchResults.get(LAST_ITEM_INDEX));
			pagerData.setFirstItemIndex(fetchResults.get(FIRST_ITEM_INDEX));
		}
		
		if (pagerData.getLastItemIndex() == 0) { //No item to display, remove offset
			pagerData.setFirstItemIndex(0);
		} 
		
		browseData.setPager(pagerData);

	}
	
	/**
	 * Recursive fetching algorithm. 
	 * Small amount of additional objects are created so it should be safe memory wise as long as section depths don't increase like crazy.
	 * 
	 * @param sectionData
	 * @param fetchResults
	 * @return
	 */
	private static Map<String, Integer> fetchSectionData(SectionData sectionData, Map<String, Integer> fetchResults, boolean all, CmsFilteringNavigator nav, FDUserI user) {
		
		if (sectionData.getSections() != null && sectionData.getSections().size() > 0) {
			Iterator<SectionData> sectionDataIterator = sectionData.getSections().iterator();
			while (sectionDataIterator.hasNext()) {
				fetchResults = fetchSectionData(sectionDataIterator.next(), fetchResults, all, nav, user);
				if (fetchResults.get(MARKED_FOR_REMOVAL) != null && !all) { //dropping sections that won't be displayed on the current page
					fetchResults.remove(MARKED_FOR_REMOVAL);
					sectionDataIterator.remove();
				}
			}
			if (sectionData.getSections().size() == 0) {
				fetchResults.put(MARKED_FOR_REMOVAL, 1);
			}
			return fetchResults;
			
		} else if (sectionData.getProducts() != null && sectionData.getProducts().size() > 0) {

			List<ProductData> entityList = null;
			
			if (sectionData.getProducts() != null && sectionData.getProducts().size() > 0){
				entityList = sectionData.getProducts();
			} else {
				entityList = new ArrayList<ProductData>();
			}

			fetchResults.put(ITEM_COUNT, fetchResults.get(ITEM_COUNT) + entityList.size());

			//fetching already passed by the current page
			if (fetchResults.get(LOGICAL_ITEM_COUNT).compareTo(fetchResults.get(CURRENT_PAGE_LAST_ITEM_INDEX)) > -1) { 
				populateProducts(entityList, fetchResults, nav, user, -1);
				//accumulating logical item index by '(int)rounding' to the next PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD multiple
//				fetchResults.put(LOGICAL_ITEM_COUNT, (Math.max(0, entityList.size() - 1) / PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD + 1) * PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD + fetchResults.get(LOGICAL_ITEM_COUNT)); 
				fetchResults.put(LOGICAL_ITEM_COUNT, entityList.size() + fetchResults.get(LOGICAL_ITEM_COUNT)); 
				fetchResults.put(MARKED_FOR_REMOVAL, 1);
				return fetchResults; 
			}
			
			//fetching still hasn't reached the current page
			if (fetchResults.get(CURRENT_PAGE_FIRST_ITEM_INDEX).compareTo(entityList.size() + fetchResults.get(LOGICAL_ITEM_COUNT)) == 1) { 
				//accumulating logical item index by '(int)rounding' to the next PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD multiple
//				fetchResults.put(LOGICAL_ITEM_COUNT, (Math.max(0, entityList.size() - 1) / PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD + 1) * PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD + fetchResults.get(LOGICAL_ITEM_COUNT)); 
				fetchResults.put(LOGICAL_ITEM_COUNT, entityList.size() + fetchResults.get(LOGICAL_ITEM_COUNT)); 
				fetchResults.put(FIRST_ITEM_INDEX, fetchResults.get(FIRST_ITEM_INDEX) + entityList.size());
				fetchResults.put(LAST_ITEM_INDEX, fetchResults.get(LAST_ITEM_INDEX) + entityList.size());
				fetchResults.put(MARKED_FOR_REMOVAL, 1);
				return fetchResults; 
			}
			
			int entityListSize = entityList.size();

			fetchResults.put(FIRST_ITEM_INDEX, fetchResults.get(FIRST_ITEM_INDEX) + entityListSize);
			fetchResults.put(LAST_ITEM_INDEX, fetchResults.get(LAST_ITEM_INDEX) + entityListSize);

			//set the spacer count
			int spacerCount = 0;
			if (entityListSize > 0) {

				Iterator<ProductData> entityListIterator = entityList.iterator();
				while (entityListIterator.hasNext()) {
					ProductData curEntity = entityListIterator.next();
					if (curEntity != null && "!_SPACER_!".equals(curEntity.getProductId()) ) { /* this ID is also used in fragments.soy */
						spacerCount++;
					}
				}
					
				fetchResults.put(SPACER_COUNT, fetchResults.get(SPACER_COUNT) + spacerCount);
			}
			
			//Overlapping section, remove entities from 'previous page'
			int itemNumberToBeDropped = fetchResults.get(CURRENT_PAGE_FIRST_ITEM_INDEX) - 1 - fetchResults.get(LOGICAL_ITEM_COUNT);
			
			for (int i = 0; i < itemNumberToBeDropped; i++) {
				if (!all) {
					entityList.remove(0);
				}
			}
			//we reached the current page so we need the first item index 
			fetchResults.put(FIRST_ITEM_INDEX, fetchResults.get(FIRST_ITEM_INDEX) - entityList.size());
			
			//Overlapping section, remove entities from 'next page'
			itemNumberToBeDropped = fetchResults.get(CURRENT_PAGE_FIRST_ITEM_INDEX) - 1 + (Math.max(fetchResults.get(LOGICAL_ITEM_COUNT), fetchResults.get(CURRENT_PAGE_FIRST_ITEM_INDEX) - 1) - fetchResults.get(CURRENT_PAGE_FIRST_ITEM_INDEX) + 1) + entityList.size() - fetchResults.get(CURRENT_PAGE_LAST_ITEM_INDEX);
			
			itemNumberToBeDropped -= spacerCount; //account for spacers
			
			int itemFailedToPopulated = populateProducts(entityList, fetchResults, nav, user, fetchResults.get(CURRENT_PAGE_LAST_ITEM_INDEX) - fetchResults.get(CURRENT_PAGE_FIRST_ITEM_INDEX));
			fetchResults.put(ITEM_COUNT, fetchResults.get(ITEM_COUNT) - itemFailedToPopulated);
			itemNumberToBeDropped -= itemFailedToPopulated;
			
			//we reached the end of current page so we need the last item index 
			fetchResults.put(LAST_ITEM_INDEX, fetchResults.get(LAST_ITEM_INDEX) - Math.max(0, itemNumberToBeDropped));
			
			//accumulating logical item index by '(int)rounding' to the next PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD multiple
			if (entityListSize > 0) {
//				fetchResults.put(LOGICAL_ITEM_COUNT, (Math.max(0, entityListSize - 1) / PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD + 1) * PagerData.GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD + fetchResults.get(LOGICAL_ITEM_COUNT)); 
				fetchResults.put(LOGICAL_ITEM_COUNT, entityListSize + fetchResults.get(LOGICAL_ITEM_COUNT)); 
			}
		}
		
		return fetchResults;
		
	}
	
	private static int populateProducts(List<ProductData> productList, Map<String, Integer> fetchResults, CmsFilteringNavigator nav, FDUserI user, int displaySize){
		Iterator<ProductData> productIterator = productList.iterator();
		int validProductNumber = 0;
		int productRemoved = 0;
		while (productIterator.hasNext()) {
			ProductData product = productIterator.next();
			// if displaySize is -1, display all items.
			if (displaySize == -1 || validProductNumber <= displaySize) {
				if (!populateProductData(product, nav, user)) {
					productRemoved++;
					productIterator.remove();
				} else {
					validProductNumber++;
				}
			} else {
				productIterator.remove();
			}

		}
		return productRemoved;
	}
	
	private static boolean populateProductData(ProductData productData, CmsFilteringNavigator nav, FDUserI user) {
		try {
			ProductDetailPopulator.populateBrowseProductData(productData, user, nav, (nav.isPdp() || FDStoreProperties.getPreviewMode()));
		} catch(Exception e) {
			return false;
		}
    	return true;
    }
}
