package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AllPredicate;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.query.ContentKeysPredicate;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.Faq;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class SearchFaqTag extends AbstractGetterTag {
	
	protected FilteringNavigator nav;
	
	private List<Faq> faqsList;
	private Map<String, List<Faq>> faqMap;
	private Map<String, List<Faq>> page;
	
	@Override
	protected Object getResult() throws Exception {
		
		HttpServletRequest   request = (HttpServletRequest)pageContext.getRequest();
//		if(null !=request.getParameter("searchFAQButton.x")){
			
			String faqKeyword = "";
//			if ("POST".equalsIgnoreCase(request.getMethod()))
//				faqKeyword =request.getParameter("searchFAQ2");
//			else{
//				faqKeyword = request.getParameter("searchFAQ");
//			}
			
			faqKeyword =request.getParameter("searchFAQ2");
			if(faqKeyword == null){
				faqKeyword = request.getParameter("searchFAQ");
			}
			
			faqKeyword = NVL.apply(faqKeyword, "");
			if("".equals(faqKeyword.trim())){
				pageContext.setAttribute("searchResultsSize", 0);
				pageContext.setAttribute("keywords", "");

				pageContext.setAttribute("resultInvalid", "true");
				return null;				
			}

			List<ContentKey> keys = ContentSearch.getInstance().searchFaqs(faqKeyword);
			Predicate predicate = new ContentKeysPredicate(new HashSet<ContentKey>(keys));
			Predicate searchPredicate = new AllPredicate(new Predicate[]{predicate});
			CmsManager manager = CmsManager.getInstance();
			ContentFactory	    cf          = ContentFactory.getInstance();
			Map result = manager.queryContentNodes(FDContentTypes.FAQ, searchPredicate);
			
			faqsList = new ArrayList<Faq>();
			faqMap = new LinkedHashMap<String, List<Faq>>();
			
			for (Iterator it = result.keySet().iterator(); it.hasNext();) {
				ContentKey key = (ContentKey) it.next();
				Faq faq = (Faq) cf.getContentNode(key.getId());
				if(faq.calculatePriority(faqKeyword)!=-1){
					faqsList.add(faq);									
				}
			}
			
			if ( faqsList.isEmpty() ) {
				for (Iterator it = result.keySet().iterator(); it.hasNext();) {
					ContentKey key = (ContentKey) it.next();
					Faq faq = (Faq) cf.getContentNode(key.getId());
					faqsList.add(faq);									
				}
			}
			
			Collections.sort(faqsList, new FaqPriorityComparator(faqKeyword));
			
			//grouping result
			for(Iterator<Faq> it = faqsList.iterator(); it.hasNext();){
				
				Faq faq = it.next();
				
				if(faqMap.containsKey(faq.getAnswer())){
					faqMap.get(faq.getAnswer()).add(faq);
					it.remove();
				}else{
					List<Faq> faqs = new ArrayList<Faq>();
					faqs.add(faq);
					faqMap.put(faq.getAnswer(), faqs);
				}
			}
			
			createProductPageWindow();
			
//			pageContext.setAttribute("searchResult", result);
			pageContext.setAttribute("searchResultsSize", faqsList.size());
			pageContext.setAttribute("keywords", faqKeyword);
			return page;	
//			}
//		return null;
	}

	public FilteringNavigator getNav() {
		return nav;
	}

	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}
	
	private void createProductPageWindow() {
		int pageSize = nav.getPageSize();
		int pageOffset = nav.getPageOffset();

		page = new LinkedHashMap<String, List<Faq>>();

		int noOfPagedProducts = faqsList.size();
		int pageCount = pageSize == 0 ? 1 : noOfPagedProducts / pageSize;
		if (pageSize != 0 && noOfPagedProducts % pageSize > 0) {
			pageCount++;

		}
		int max = pageSize == 0 ? pageOffset + noOfPagedProducts : pageOffset + pageSize;
		for (int i = pageOffset; i < max; i++) {
			if (i >= faqsList.size()) {
				break;
			}
			Faq actual = faqsList.get(i);
			page.put(actual.getAnswer(), faqMap.get(actual.getAnswer()));
		}
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return Map.class.getName();
		}
	}
	
	static final class FaqPriorityComparator implements Comparator<Faq> {
		
		private String word;
		
        public FaqPriorityComparator(String word) {
			super();
			this.word = word;
		}

		@Override
        public int compare(Faq o1, Faq o2) {
            return o1.calculatePriority(word).compareTo(o2.calculatePriority(word));
        }
    }
	
	
}
