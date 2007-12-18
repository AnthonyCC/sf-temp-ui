/**
 * 
 * EmailPreviewGetterTag.java
 * Created Dec 6, 2002
 */
package com.freshdirect.webapp.taglib.fdstore;

/**
 *
 *  @author knadeem
 */
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.mail.TellAFriend;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.TellAFriendUtil;

public class SiteEmailPreviewTag extends AbstractGetterTag implements SessionName{
	
	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		TellAFriend taf = (TellAFriend) session.getAttribute(TELL_A_FRIEND);
		
		return taf.getPreview(pageContext.getServletContext());
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.lang.String";
		}

	}

}
