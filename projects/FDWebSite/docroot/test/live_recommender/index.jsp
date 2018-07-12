<!DOCTYPE html>
<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopHelper"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%
  request.setAttribute("sitePage", "www.freshdirect.com/quickshop/");
	request.setAttribute("listPos", "SystemMessage,QSTop");
%>
<html lang="en-US" xml:lang="en-US">
	<head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" lang="en-US"/>
        <title>Live Recommender Test Page</title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
        <%@ include file="/common/template/includes/i_javascripts.jspf" %>
	  	<jwr:style src="/grid.css"/>
		<jwr:style src="/global.css"/>
        <jwr:style src="/oldglobal.css"/>
        <%@ include file="/shared/template/includes/i_head_end.jspf" %>
    </head>
    <body>
    <%@ include file="/common/template/includes/globalnav.jspf" %>

    <div id="content">
        <div id="quickshop"  class="container text10">
          <!-- content lands here -->

            <div data-component="liverecommender" data-direction="horizontal" class="live-recommender"></div>
            <div data-component="liverecommender" data-direction="vertical" data-limit="5" class="live-recommender"></div>

          <!-- content ends above here-->
        </div>
    </div>

    <%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
    </body>
</html>
