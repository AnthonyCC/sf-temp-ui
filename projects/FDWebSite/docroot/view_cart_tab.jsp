<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%><%@page import="com.freshdirect.fdstore.util.URLGenerator"%><%
	URLGenerator u = new URLGenerator(request);
	u.setURI("/view_cart.jsp");
	u.setEscapeAndSign(false);
	String variantId = u.get("variant");
	if (variantId != null) {
	    u.remove("variant");
	    session.setAttribute(SessionName.SS_SELECTED_TAB, Integer.valueOf(-1));
	    session.setAttribute(SessionName.SS_SELECTED_VARIANT, variantId);
	}
	System.err.println("new url:"+u.build());
	response.sendRedirect(u.build());
	return;
%>