<%@page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
if (user!=null){
	MasqueradeContext masqueradeContext = user.getMasqueradeContext();
	if(masqueradeContext!=null){
		masqueradeContext.clearMakeGoodContext();		
	}
}
response.sendRedirect("/");
%>