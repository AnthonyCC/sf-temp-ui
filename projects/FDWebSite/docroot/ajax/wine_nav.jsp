<%@taglib uri='freshdirect' prefix='fd'
%><%@page import="com.freshdirect.fdstore.customer.FDUserI"
%><%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%><fd:CheckLoginStatus noRedirect="true"/><%
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
if (user == null) {
	response.setStatus(500);
	return;			
}
%><fd:WineHomenav/>