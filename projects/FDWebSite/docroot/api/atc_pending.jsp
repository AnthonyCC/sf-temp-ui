<%@ page language="java" contentType="text/plain"
%><%@ taglib uri='freshdirect' prefix='fd'
%><fd:CheckLoginStatus id="user" noRedirect="true"
/><%
String action = request.getParameter("atc_pending_action");
if (action != null && !action.trim().isEmpty()) {
	try {
%><fd:FDShoppingCart result="result" id="cart" action="<%= action %>" pending="true"
><%
if (result.isSuccess()) {
%>1<% } else { %>0<% } %></fd:FDShoppingCart><%
	} catch (Throwable e) {
%>0<%
	}
} else { %>0<% } %><%-- !!! DO NOT ADD NEW LINE AT THE END !!! --%>