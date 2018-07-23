<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdlogistics.model.FDReservation'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<%@ include file="/includes/i_dynamicRows_required.jspf" %>

<%
if(ContentFactory.getInstance().isEligibleForDDPP()){
	String trkCodeRedir = "wgd";
	if (request.getParameter("trk")!=null ) {
		trkCodeRedir=request.getParameter("trk");
	}
	response.sendRedirect("/category.jsp?catId=picks_love"+"&trk="+trkCodeRedir);
	return;
}else{
	user = (FDUserI)session.getAttribute(SessionName.USER);
	String custFirstName = user.getFirstName();
	int validOrderCount = user.getAdjustedValidOrderCount();

	/*
	if we're on the email.jsp, set the product base urls to PROD
	set true in email.jsp, false in newsletter.jsp
	*/
	boolean emailpage = false;

	params = new HashMap();
	params.put("baseUrl", "");

	//set media path base
	mediaPathTempBase="/media/editorial/picks/pres_picks/";
}
%>
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Fresh Deals"/>
    </tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'>FreshDirect - Fresh Deals</tmpl:put> --%>
	<tmpl:put name='content' direct='true'>
	
	<% mediaPathTemp=mediaPathTempBase+"pres_picks.ftl"; %>
	<fd:IncludeMedia name="<%=mediaPathTemp%>" parameters="<%=params%>"/>
	
<%-- //Featured Products moved into Include --%>
	<%
	//setup to allow include in PP
        Image groDeptImage = null;
		boolean isDepartment = false;
		String targetId = "";
		
		trkCode = (String)request.getAttribute("trk");

		if (trkCode!=null && !"".equals(trkCode.trim()) ) {
			trkCode = "&trk="+trkCode.trim();
		} else {
			trkCode = "";
		}

		catId = request.getParameter("catId");
		deptId = request.getParameter("deptId");

		request.setAttribute("sitePage", "www.freshdirect.com/wgd/picks_love");

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

		
		%>
		<tr><td colspan="4" align="center">
		<%@ include file="/includes/i_dynamicRows_logic.jspf"%>
		</td></tr>
		<% mediaPathTemp=mediaPathTempBase+"pres_picks_footer.ftl"; %>
		<fd:IncludeMedia name="<%=mediaPathTemp%>" parameters="<%=params%>"/>

<%-- //END Featured Products --%>
</tmpl:put>
</tmpl:insert>
