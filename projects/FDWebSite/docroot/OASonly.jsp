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
<html lang="en-US" xml:lang="en-US">
<head>
    <title>OAS</title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  <jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
</head>
<body class="OASonly">
  <div id="oas_<%= lp %>"><script type="text/javascript">OAS_AD('<%= lp %>');</script></div>
</body>
</html>
