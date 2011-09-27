<%@ page import='java.util.List' 
%><%@ page import='java.util.Iterator' %><%@ 
  taglib uri='freshdirect' prefix='fd' 
%><fd:GetSearchTerms id="terms"><%
  for(Iterator i=terms.iterator(); i.hasNext();) {
     List row = (List)i.next();
     for(Iterator j=row.iterator(); j.hasNext();) {
	%><%=j.next()%><%
        if (j.hasNext()) %>,<%
     } 
%>
<%
  }
%></fd:GetSearchTerms>
