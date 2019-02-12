<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionType" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Alcohol Restrictions</tmpl:put>

<tmpl:put name='content' direct='true'>
<script type="text/javascript">
	function toggleDate(resType){
	}
</script>

<jsp:include page="/includes/admintools_nav.jsp" />
<div class="home_search_module_content" style="height:91%;">
<%
		String state = null;
		String county = null;
		EnumDlvRestrictionReason reason = null;
		EnumDlvRestrictionType restrictedType = null;
		/*
		GenericSearchCriteria criteria = (GenericSearchCriteria) session.getAttribute("ALCOHOL_RESTRICTION_CRITERIA");
		System.out.println("Criteria : " + criteria);
		if(criteria != null){
			Map criteriaMap = criteria.getCriteriaMap();
			state = (String)criteriaMap.get("state");
			county = (String)criteriaMap.get("county");
			reason = (EnumDlvRestrictionReason) criteriaMap.get("reason");
			restrictedType = (EnumDlvRestrictionType)criteriaMap.get("type");
		} else {*/
			state = NVL.apply(request.getParameter("state"), "NY");
			county = NVL.apply(request.getParameter("county"), "");
			reason = EnumDlvRestrictionReason.getEnum(NVL.apply(request.getParameter("reason"), "WIN"));
			restrictedType = EnumDlvRestrictionType.getEnum(NVL.apply(request.getParameter("restrictedType"), "RRN"));
		//}

%>

</div>
</tmpl:put>
</tmpl:insert>

