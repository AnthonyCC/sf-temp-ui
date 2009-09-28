<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<%
	/*
		passing in one var "rhShareInfo" as string (boolean)
	*/
	boolean rhShareInfo = false;
String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
String erpCustomerPk = identity.getErpCustomerPK();
%>

<% 
	if ( request.getParameter("rhShareInfo") != null ) {
		if ( "true".equals((String)request.getParameter("rhShareInfo")) ) {
			rhShareInfo = true;			
		}
		FDCustomerManager.saveDonationOptIn(erpCustomerPk,orderNumber,rhShareInfo);
	}

	//do saving

	if (rhShareInfo) {
		%>Saved as Yes<%
	}else{
		%>Saved as No<%
	}
%>