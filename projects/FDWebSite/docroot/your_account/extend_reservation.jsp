<%@ taglib uri="freshdirect" prefix="fd" %>

<% 
String successPage = request.getParameter("successPage");
if(successPage.indexOf("?") >=0 ){
	successPage += "&extended=true";
}else{
	successPage += "?extended=true";
}
%>
<fd:ReserveTimeslotController actionName="extendReservation" result="result" successPage="<%=successPage%>"/>

