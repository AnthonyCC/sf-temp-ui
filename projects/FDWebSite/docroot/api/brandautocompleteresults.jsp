<%@page import="com.freshdirect.fdstore.content.ContentSearch,com.freshdirect.cms.application.CmsManager,java.util.List,java.util.Iterator"
	contentType="text/plain; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
%>
<%
	/**
	/* author robi
	*/

	if (request.getParameter("prefix") != null && request.getParameter("prefix") != "") {
		List results = ContentSearch.getInstance().getBrandAutocompletions(request.getParameter("prefix"));				
		//System.err.println(request.getParameter("prefix") + "(" + (results != null ? results.size() : 0 )+ ")");
		int i = results.size();
		Iterator resIt = results.iterator();
		while( resIt.hasNext() ) {
			String term = (String) resIt.next();
			//System.err.println(term + "\t" + Integer.toString(i));
						// term				  // 'freq' ?
			out.println( term + "\t" + Integer.toString(i));
			i--;
		}
	}
%>