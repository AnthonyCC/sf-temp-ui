<%@ taglib uri='freshdirect' prefix='fd' %><%
%><fd:CheckLoginStatus noRedirect="true" recognizedAllowed="true" guestAllowed="true"/><%
%><fd:CartData/><%
%><fd:CmShop5 wrapIntoScriptTag="false" outStringVar="cmStr" cart="<%= cart %>"/><%
%><% if( cartData != null ) cartData.setCoremetricsScript( (String)pageContext.getAttribute("cmStr") ); %><%
%><fd:ToJSON object="<%= cartData %>" />