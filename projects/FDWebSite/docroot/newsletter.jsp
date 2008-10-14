<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.FDReservation'%>
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
<fd:CheckLoginStatus />
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
String custFirstName = user.getFirstName();
int validOrderCount = user.getAdjustedValidOrderCount();
boolean mainPromo = user.getLevel() < FDUserI.RECOGNIZED && user.isEligibleForSignupPromotion();

Map params = new HashMap();
params.put("baseUrl", "");
%>

<tmpl:insert template='/common/template/dnav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - President's Picks</tmpl:put>
	<tmpl:put name='content' direct='true'>
	
	<fd:IncludeMedia name="/media/editorial/picks/pres_picks/pres_picks.ftl" parameters="<%=params%>"/>
	
<%-- //Featured Products moved into Include --%>
	<%
	//setup to allow include in PP
        Image groDeptImage = null;
		boolean isDepartment = false;
		
		String trkCode = (String)request.getAttribute("trk");

		if (trkCode!=null && !"".equals(trkCode.trim()) ) {
			trkCode = "&trk="+trkCode.trim();
		} else {
			trkCode = "";
		}

		String catId = request.getParameter("catId");
		String deptId = request.getParameter("deptId");

		ContentNodeModel currentFolder = null;
		if (catId!=null) {
			currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
			if ("picks_love".equalsIgnoreCase(catId)) {
				deptId = "gro";
				isDepartment = true;
			%>
				<%@ include file="/includes/layouts/i_featured_products_picks.jspf" %>
				<fd:IncludeMedia name="/media/editorial/picks/pres_picks/pres_picks_footer.ftl" parameters="<%=params%>"/>
			<%
			}
		} else {
			currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
		}
	%>
		

<%-- //END Featured Products --%>
</tmpl:put>
</tmpl:insert>
