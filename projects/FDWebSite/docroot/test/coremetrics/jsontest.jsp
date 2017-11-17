<%@page import="java.util.Locale"%>
<%@page
language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
import="com.freshdirect.webapp.taglib.coremetrics.AbstractCmTag"
import="com.freshdirect.fdstore.content.ContentFactory"
import="com.freshdirect.cms.application.CmsManager"
import="com.freshdirect.webapp.taglib.coremetrics.CmProductViewTag"
import="com.freshdirect.webapp.taglib.coremetrics.CmPageViewTag"
import="java.util.List" 
import="java.util.ArrayList"
import="com.freshdirect.webapp.taglib.fdstore.SessionName"
import="com.freshdirect.fdstore.customer.FDUserI"
import="com.freshdirect.webapp.taglib.coremetrics.CmShop5Tag"
%><%@ taglib uri='freshdirect' prefix='fd' %><%
//test to simulate CM tag calls returning JSON data from AJAX backend servlets
//see tag lifecycle at http://docs.oracle.com/javaee/6/api/javax/servlet/jsp/tagext/SimpleTag.html

response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setContentType( "application/json" );
response.setLocale( Locale.US );
response.setCharacterEncoding( "ISO-8859-1" );

try{
	List<String> tags = new ArrayList<String>();
	
	CmPageViewTag pageViewTag = new CmPageViewTag();
	pageViewTag.setSession(session);
	pageViewTag.setRequest(request);
	pageViewTag.setReturnAsJson(true);
	tags.add(pageViewTag.getTagOutput());
	
	CmProductViewTag productViewTag = new CmProductViewTag();
	productViewTag.setSession(session);
	productViewTag.setRequest(request);
	productViewTag.setReturnAsJson(true);
	productViewTag.setProductModel(ContentFactory.getInstance().getProductByName("ban", "ban_yllw"));
	tags.add(productViewTag.getTagOutput());
	
	CmShop5Tag shop5Tag = new CmShop5Tag();
	shop5Tag.setSession(session);
	shop5Tag.setReturnAsJson(true);
	shop5Tag.setCart(((FDUserI)session.getAttribute(SessionName.USER)).getShoppingCart());
	tags.add(shop5Tag.getTagOutput());
	
	out.print("{\"coremetrics\":"+AbstractCmTag.wrapTagsIntoJson(tags)+"}");
	%><%
} catch (NullPointerException e) {
	out.print("NullPointerException, open store first");
	e.printStackTrace();
}	
%>
