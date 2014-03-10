<%@page import="com.freshdirect.fdstore.content.ContentSearch,com.freshdirect.cms.application.CmsManager,java.util.List,java.util.Iterator"
	contentType="application/json"
%>
<%
	/**
	/* author greg
	*/

	if (request.getParameter("term") != null && request.getParameter("term") != "") {
		List results = ContentSearch.getInstance().getAutocompletions(request.getParameter("term"));				
		int i = results.size();
    boolean notfirst = false;
		Iterator resIt = results.iterator();
    out.println("[");
		while( resIt.hasNext() ) {
      if (notfirst) {
        out.print(",");
      } else {
        notfirst = true;
      }
			String term = (String) resIt.next();
			out.print("\"" + term + "\"");
		}
    out.println("]");
	}
%>
