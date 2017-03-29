<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user"/>
<fd:FDShoppingCart id='cart' result='result'>
<html>
<head>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
<fd:javascript src="/assets/javascript/coremetrics.js"/>
</head>
<body>
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<fd:CmShop5 wrapIntoScriptTag="true" cart="<%=cart%>"/>
</body>
</html>
</fd:FDShoppingCart>
