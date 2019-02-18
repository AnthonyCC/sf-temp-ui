<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDReservation'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="/includes/i_dynamicRows_required.jspf" %>
<html>
<head>
<title>FreshDirect - President's Picks</title>
<fd:css href="/assets/css/pc_ie.css"/>
</head>
<%
	params = new HashMap();
	params.put("baseUrl", "http://www.freshdirect.com");
	/*
	if we're on the email.jsp, set the product base urls to PROD
	set true in email.jsp, false in newsletter.jsp
	*/
	boolean emailpage = true;
	params.put("emailpage", emailpage);

	//set media path base
	mediaPathTempBase="/media/editorial/picks/pres_picks/";
%>
<body>
	<% mediaPathTemp=mediaPathTempBase+"pres_picks.ftl"; %>
	<fd:IncludeMedia name="<%=mediaPathTemp%>" parameters="<%=params%>"/>
	<%-- //Featured Products moved into Include --%>
	<%
	//setup to allow include in PP
        Image groDeptImage = null;
		boolean isDepartment = false;
		deptId = "gro";
		String targetId = "";

		//get the WG prop.
		strDynRows = FDStoreProperties.getWhatsGoodRows();

		/*
		 * point targetId at the id you want to fetch from the property line.
		 * it will then parse out the sub attributes so the products match on both pages
		 */
		/* pres picks */
			//targetId = "picks_pres";
		/* brand name deals */
			targetId = "wgd_deals";

		//parse out only the property we want
		if (strDynRows.indexOf(targetId)>-1) {
			strDynRows = strDynRows.substring(strDynRows.indexOf(targetId));
			String[] temp = strDynRows.split(",");
			//temp[0] should now be the property
			strDynRows = temp[0];
			if (strDynRows.toLowerCase().indexOf("istx")==-1) {
				//turn off transactional
				strDynRows += ":isTx=false";
			}
		}else{
			strDynRows = ""; //show nothing
		}

		//make sure images have prefixes
		prefixURI = "http://www.freshdirect.com";
		emailStyle = "color: #336600; font-size: 8pt; font-family: Verdana,Arial,sans-serif;";
		
		%>
		<tr><td colspan="4" align="center">
			<%@ include file="/includes/i_dynamicRows_logic.jspf"%>
		</td></tr>
		<% mediaPathTemp=mediaPathTempBase+"pres_picks_footer.ftl"; %>
		<fd:IncludeMedia name="<%=mediaPathTemp%>" parameters="<%=params%>"/>
			
		

<%-- //END Featured Products --%>
</body>	
</html>
