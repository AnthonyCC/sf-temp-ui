
<%@ page import='com.freshdirect.webapp.util.ItemSelectionCheckResult' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='org.json.JSONObject' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus noRedirect="true"/>
<%
   JSONObject jsCheckResult = new JSONObject();
   String action = (String)request.getParameter("action");
   
   if (action == null) {
       jsCheckResult.put("type","invalid");
%>
       <%=jsCheckResult%>
<%
       return;
   } 
%>

<%
   if (session.getAttribute(SessionName.USER) == null) {
      jsCheckResult.put("type","no_session");
%>
      <%=jsCheckResult%>
<%
      session.invalidate();
      return;
   }
%>
<fd:FDShoppingCart id='cart' source='Quickshop' result='actionResult' action='<%= action %>' successPage=''>
<%= ((ItemSelectionCheckResult)request.getAttribute("check_result")).toJSON()%>
</fd:FDShoppingCart>
