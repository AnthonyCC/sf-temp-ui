<!DOCTYPE html>
<%@page import="java.util.*"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <title>Product Data Potato</title>
  
  <jwr:style src="/all.css"/>
  <jwr:style src="/global.css"/>
  <jwr:style src="/quickshop.css"/>
  
  <jwr:script src="/fdmodules.js"/>
  
  <soy:import packageName="common"/>
  <soy:import packageName="test"/>  
  
</head>
<body>

  <hr/>
  	<h1> BASIC PRODUCT (from url params) </h1>
  <hr/>
  <potato:product name="productPotato" productId='<%=pageContext.getRequest().getParameter( "productId" )%>' categoryId='<%=pageContext.getRequest().getParameter( "catId" )%>'/>
  <soy:render template="test.product_data" data="<%= productPotato %>" />
  <hr/>
  <fd:ToJSON object="<%= productPotato %>" noHeaders="true"/>
  <hr/>


  <hr/>
  	<h1> SCARAB PERSONAL RECOMMENDATIONS </h1>
  <hr/>
  <potato:recommender name="recommenderPotato" siteFeature="SCR_PERSONAL" maxItems="50"/>
  <soy:render template="test.product_iterator" data="<%= recommenderPotato %>" />
  <hr/>
  <fd:ToJSON object="<%= recommenderPotato %>" noHeaders="true"/>
  <hr/>
  
</body>
</html>
