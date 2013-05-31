<%@ tag import="java.lang.String"
	import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"
	import="com.freshdirect.fdstore.FDStoreProperties"%><%@ 
	attribute
	name="user" required="true" rtexprvalue="true"
	type="com.freshdirect.fdstore.customer.FDUserI"
%><%@ taglib uri='freshdirect' prefix='fd' 
%><fd:IncludeMedia name="<%= (user.getAdjustedValidOrderCount() < 1) ? FDStoreProperties.getHPLetterMediaPathForNewUser() : FDStoreProperties.getHPLetterMediaPathForOldUser() %>" /><%
	// update user already visited home page letter
	user.setHomePageLetterVisited(true);
	// not sure we need to do this here because saving cart too often is not recomended

	if (user instanceof FDSessionUser) {
		FDSessionUser sessionUser = (FDSessionUser) user;
		sessionUser.saveCart(true);
	}
%>