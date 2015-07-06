<%@ taglib uri='freshdirect' prefix='fd' %>
<%
String lp = request.getParameter("listPos");
String sp = request.getParameter("sitePage");
if (lp != null) {
  request.setAttribute("listPos", lp);
}
if (sp != null) {
  request.setAttribute("sitePage", sp);
}
%>
<!DOCTYPE html>
<html>
<head>
<title>OAS</title>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  <jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
</head>
<body class="OASonly">
  <div id="OAS_<%= lp %>"></div>
</body>
<html>
