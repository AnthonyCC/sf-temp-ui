<%@page import="com.freshdirect.webapp.soy.SoyTemplateEngine"%>
<%@page import="com.freshdirect.webapp.ajax.cart.data.AddToCartItem"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<!DOCTYPE HTML>
<html lang="en-US" xml:lang="en-US">
<head>
  <meta charset="UTF-8">
  <title>Soy Template Test Page</title>
  
  <jwr:style src="/all.css"/>
  <jwr:script src="/fdmodules.js"/>
  
  <soy:import packageName="test.sub1"/>
  <soy:import packageName="test"/>
  <soy:import packageName="quickshop"/>
  
</head>
<body>
	<h1>Soy Template Test Page</h1>
	<h2>soy template rendered on the server:</h2>
  <% 
  	Map<String,Object> dataMap = new HashMap<String,Object>();
  	List<String> dataList = new ArrayList<String>();
  	dataList.add( "value1" );
  	dataList.add( "value2" );
  	dataList.add( "value3" );
  	dataMap.put( "x",dataList );
  %>
  <hr/>
  <soy:render template="test.testMain" data="<%=dataMap%>" />
  <hr/>
  
	<h2>server-side bean-test conversion:</h2>
  <% 
  	AddToCartItem item = new AddToCartItem();
  	item.setCategoryId( "someCatId" );
  	item.setProductId( "someProdId" );
  	item.setQuantity( "12" );
  	item.setSkuCode( "SKU0000" );
  	Map<String,Object> dataMap2 = new HashMap<String,Object>();
	dataMap2.put( "data", SoyTemplateEngine.convertToMap( item ) );
  %>
  <hr/>
  <soy:render template="test.beanTest" data="<%= dataMap2 %>" />
  <hr/>
  
  
</body>
</html>
