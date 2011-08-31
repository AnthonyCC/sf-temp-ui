<%@page import="com.freshdirect.fdstore.content.ContentSearch,com.freshdirect.cms.application.CmsManager,java.util.List,java.util.Iterator"
	contentType="text/plain; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
%><%
	/**
	/* author robi
	*/

	if (request.getParameter("prefix") != null && request.getParameter("prefix") != "") {
		List<String> results = ContentSearch.getInstance().getBrandAutocompletions(request.getParameter("prefix"), 10);				
		int i = results.size();
		Iterator<String> resIt = results.iterator();
		while( resIt.hasNext() ) {
			String term = resIt.next();
			          // term       // 'freq' ?
			out.println( term + "\t" + Integer.toString(i));
			i--;
		}
	}
%>