<%@ taglib prefix="fd" uri="freshdirect"%>
<%
String successPage = request.getParameter("successPage");
if (successPage == null) {      		
	    successPage = "/index.jsp";        
}
%><fd:ExternalCampaign successPage="<%=successPage %>"></fd:ExternalCampaign>

