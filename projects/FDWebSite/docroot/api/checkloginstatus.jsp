<%@ page language="java" contentType="text/plain"
%><%@ taglib uri='freshdirect' prefix='fd'
%><fd:CheckLoginStatus id="user" noRedirect="true"
/><%
if (user != null) {
%><%= user.getLevel() %><% } else { %>-1<% } %><%-- !!! DO NOT ADD NEW LINE AT THE END !!! --%>