package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AllPredicate;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.query.ContentKeysPredicate;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentSearchUtil;
import com.freshdirect.fdstore.content.Faq;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SearchQueryStemmer;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class SearchFaqTag extends AbstractGetterTag {

	@Override
	protected Object getResult() throws Exception {
		
		HttpServletRequest    request = (HttpServletRequest)pageContext.getRequest();
//		if(null !=request.getParameter("searchFAQButton.x")){
			
			String faqKeyword = "";
			if ("POST".equalsIgnoreCase(request.getMethod()))
				faqKeyword =request.getParameter("searchFAQ2");
			else{
				faqKeyword = request.getParameter("searchFAQ");
			}
			faqKeyword = NVL.apply(faqKeyword, "");
			faqKeyword = ContentSearchUtil.normalizeTerm(faqKeyword);
			if("".equals(faqKeyword.trim())){
				return null;				
			}
			//search for faq					
			Collection hits = CmsManager.getInstance().search(faqKeyword, 2000);
			Map hitsByType = ContentSearchUtil.mapHitsByType(hits);

			String[] tokens = ContentSearchUtil.tokenizeTerm(faqKeyword);

			// TODO : refactor, this way, we load ContentNodes twice, here, and in the upper method.
			List faqs = ContentSearchUtil.filterRelevantNodes(
					ContentSearchUtil.resolveHits((List) hitsByType
							.get(FDContentTypes.FAQ)), tokens, SearchQueryStemmer.LowerCase);
			
			Set keys = new HashSet(faqs.size());
			for (Iterator i=faqs.iterator(); i.hasNext(); ) {
				SearchHit r = (SearchHit) i.next();
				keys.add(r.getContentKey());
			}
			Predicate predicate = new ContentKeysPredicate(keys);
			Predicate searchPredicate = new AllPredicate(new Predicate[]{predicate});
			CmsManager manager = CmsManager.getInstance();
			ContentFactory	    cf          = ContentFactory.getInstance();
			Map result = manager.queryContentNodes(FDContentTypes.FAQ, searchPredicate);
			
			List faqsList = new ArrayList();
			
			for (Iterator it = result.keySet().iterator(); it.hasNext();) {
				ContentKey key = (ContentKey) it.next();
				Faq faq = (Faq) cf.getContentNode(key.getId());
				faqsList.add(faq);
				
			}
			
//			pageContext.setAttribute("searchResult", result);
			pageContext.setAttribute("keywords", faqKeyword);
			return faqsList;	
//			}
//		return null;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return List.class.getName();
		}
	}
}
