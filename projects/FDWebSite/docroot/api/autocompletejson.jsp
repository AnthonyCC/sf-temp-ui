<%@page
	import="com.freshdirect.fdstore.content.ContentSearch,com.freshdirect.cms.application.CmsManager,java.util.List,java.util.Iterator,com.freshdirect.webapp.util.AutoCompleteFacade"
	contentType="application/json;charset=UTF-8"%>
<%
	/**
            /* author greg
            */

            if (request.getParameter("term") != null && request.getParameter("term") != "") {
                List results = AutoCompleteFacade.create().getTerms(request.getParameter("term"), request);
                int i = results.size();
                boolean notfirst = false;
                Iterator resIt = results.iterator();
                out.println("[");
                while (resIt.hasNext()) {
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
