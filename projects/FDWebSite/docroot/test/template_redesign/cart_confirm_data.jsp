<!DOCTYPE html>
<%@page import="java.util.*"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Cart Confirm Data Potato</title>
  
  <jwr:style src="/all.css"/>
  <jwr:style src="/global.css"/>
  <jwr:style src="/quickshop.css"/>
  
  <jwr:script src="/fdmodules.js"/>
  
  <soy:import packageName="common"/>
  <soy:import packageName="test"/>
  
</head>
<body>

  <hr/>
  <potato:cartConfirm name="cartConfirmPotato" cartlineId='<%= request.getParameter( "cartlineId" ) %>'/>
  <soy:render template="test.cartConfirm" data="<%= cartConfirmPotato %>" />
  <hr/>
  
</body>
</html>
