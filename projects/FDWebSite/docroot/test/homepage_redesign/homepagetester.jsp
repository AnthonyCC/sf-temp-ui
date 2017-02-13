<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil"%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDReservation'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDTimeslot'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.cms.fdstore.FDContentTypes' %>
<%@ page import="com.freshdirect.cms.ContentKey"%>
<%@ page import="com.freshdirect.fdstore.content.StoreModel"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.freshdirect.cms.application.CmsManager"%>
<%@ page import="com.freshdirect.cms.ContentType"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %><%

String moduleContainerId = "ModuleContainer:"+request.getParameter("moduleContainerId");

%><fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" />
<fd:CheckDraftContextTag/>


<html>
	<head>
		<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>		
</head>

<body>
	<%if (request.getParameter("moduleContainerId") == null){%>
	Module container id was not provided.
	<%}
	else{%>
	<potato:modulehandling name="welcomepagePotato" moduleContainerId="<%=moduleContainerId%>" />
	
	<div style="width:970px;margin: auto;">
		<soy:render template="common.contentModules" data="${welcomepagePotato}" />
	</div>
	<%} %>
	
<script>
    window.FreshDirect = window.FreshDirect || {};
    window.FreshDirect.welcomepage = window.FreshDirect.welcomepage || {};

    window.FreshDirect.welcomepage.data = <fd:ToJSON object="${welcomepagePotato}" noHeaders="true"/>
    console.log(window.FreshDirect.welcomepage.data);
    </script>
    <jwr:script src="/fdlibs.js" useRandomParam="false" />
        <script>
      var $jq = FreshDirect.libs.$;
    </script>
    <soy:import packageName="common"/>
     <jwr:script src="/fdmodules.js"  useRandomParam="false" />
    <jwr:script src="/fdcomponents.js"  useRandomParam="false" />
    <jwr:script src="/fdmisc.js" useRandomParam="false" />

   

   
    
    
  </body>
</html>
