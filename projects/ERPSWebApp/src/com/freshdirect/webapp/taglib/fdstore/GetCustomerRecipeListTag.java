package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.lists.FDCustomerRecipeList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetCustomerRecipeListTag extends AbstractGetterTag {

	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		FDCustomerRecipeList recipes = FDListManager
				.getEveryRecipeList(user.getIdentity());
		return (recipes == null || recipes.getAvailableLineItems().isEmpty()) ? null
				: recipes;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return FDCustomerRecipeList.class.getName();
		}
	}

}
