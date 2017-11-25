<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import="com.freshdirect.storeapi.content.DomainValue"%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.content.attributes.*'%>
<%@ page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@ page import="com.freshdirect.fdstore.util.FilteringNavigator"%>
<%@ page import="com.freshdirect.fdstore.*"%>
<%@ page import="com.freshdirect.storeapi.*"%>
<%@ page import="com.freshdirect.storeapi.fdstore.FDContentTypes"%>
<%@ page import="com.freshdirect.storeapi.content.*"%>
<%@ page import='com.freshdirect.storeapi.attributes.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@ page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@page import="com.freshdirect.fdstore.util.NewProductsNavigator"%>
<%@ page import="com.freshdirect.fdstore.util.FilteringNavigator"%>
<%@ page import="com.freshdirect.fdstore.util.NewProductsGrouping"%>
<%@ page import="com.freshdirect.fdstore.util.TimeRange"%>
<%@ page import="com.freshdirect.fdstore.content.util.QueryParameterCollection"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='bean' prefix='bean'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='oscache' prefix='oscache'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<% boolean isDFGS = true; %>
<%@ include file="newproducts_page.jspf"%>