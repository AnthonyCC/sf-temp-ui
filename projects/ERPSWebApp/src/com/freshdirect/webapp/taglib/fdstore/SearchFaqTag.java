package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
			if("".equals(faqKeyword.trim())){
				return null;				
			}

			List<ContentKey> keys = ContentSearch.getInstance().searchFaqs(faqKeyword);
			Predicate predicate = new ContentKeysPredicate(new HashSet<ContentKey>(keys));
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
