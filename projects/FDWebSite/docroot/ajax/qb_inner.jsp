
<%@page import="com.freshdirect.webapp.util.TransactionalProductImpression"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="com.freshdirect.webapp.util.prodconf.SmartStoreConfigurationStrategy"%>
<%@page import="com.freshdirect.webapp.util.ConfigurationStrategy"%>
<%@page import="com.freshdirect.webapp.util.ConfigurationContext"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%
	final String frameId = request.getParameter("frameId");
	final FDUserI u = (FDUserI) session.getAttribute(SessionName.USER);
	final ProductModel product = ContentFactory.getInstance().getProduct(request.getParameter("catId"), request.getParameter("prdId"));
	
	
	// try to configure product
	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setFDUser( u );

	ConfigurationStrategy cUtil = SmartStoreConfigurationStrategy.getInstance();
	ProductImpression pi = cUtil.configure(product, confContext);

	if (pi instanceof TransactionalProductImpression) {
		
	} else {
		// ERROR - product is not transactional
		response.setStatus(500);
		%>ERROR<%
	}
%>