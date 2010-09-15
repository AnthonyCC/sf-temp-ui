<%@page import="com.freshdirect.fdstore.content.ContentSearch,com.freshdirect.cms.application.CmsManager,java.util.List,java.util.Iterator"
	contentType="text/plain; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
%>
<%
	/**
	/* author greg
	*/

	if (request.getParameter("prefix") != null && request.getParameter("prefix") != "") {
		List results = ContentSearch.getInstance().getAutocompletions(request.getParameter("prefix"));				
		int i = results.size();
		Iterator resIt = results.iterator();
		while( resIt.hasNext() ) {
			String term = (String) resIt.next();
						// term				  // 'freq' ?
			out.println( term + "\t" + Integer.toString(i) + "\n");
			i--;
		}
	}
%>