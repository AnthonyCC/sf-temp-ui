<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%
	String gotoStore = "FALSE";
	if ( request.getParameter("gotoStore") != null && "TRUE".equals(request.getParameter("gotoStore")) ) {
		gotoStore = "TRUE";
	}
	String resultPage = "/test/mobile_testing/non-supported.jsp";
	if (FDStoreProperties.isIphoneLandingEnabled()) {
		String UA = request.getHeader("User-Agent").toLowerCase();

		//check for iphone/ipod and change results
		if (UA.indexOf("iphone;")>=0 || UA.indexOf("ipod;")>=0) {
			if ("FALSE".equals(gotoStore)){
				resultPage = "/test/mobile_testing/supported.jsp";
			}
		}
	}
%>
<jsp:forward page="<%=resultPage%>"/>