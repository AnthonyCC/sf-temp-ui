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


/*
 *	Set up email toggle
 *	Assume false by default, only turning on if "email=true" is sent in the request
 */
	Map params = new HashMap();
	String baseUrl = "";
	boolean emailPage = false;

	if ( "true".equals ((String)request.getParameter("email")) ) {
		emailPage = true;
		//baseUrl = "http://www.freshdirect.com";
	}

	//	add emailPage to params passed to ftls
	params.put("emailPage", Boolean.toString(emailPage));
	params.put("baseUrl", baseUrl);
%>

<tmpl:insert template='/common/template/dnav_no_space.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - What's Good</tmpl:put>
	<tmpl:put name='content' direct='true'>
	<%
		//--------OAS Page Variables-----------------------
		request.setAttribute("sitePage", "www.freshdirect.com/whatsgood");
		request.setAttribute("listPos", "WGL,WGC,WGR");
	%>

	<!-- START EMAIL -->
	
	<% //START top section %>
		<fd:IncludeMedia name="/media/editorial/whats_good/whats_good_line.html" />
		<fd:IncludeMedia name="/media/editorial/whats_good/whats_good_top_msg.html" />
		<fd:IncludeMedia name="/media/editorial/whats_good/whats_good.ftl" parameters="<%=params%>"/>
	<% //START end top section %>


	<% //START Great Right Now %>
		<jsp:include page="/includes/department_peakproduce_whatsgood.jsp" flush="true"/>
	<% //END Great Right Now %>

	<% //START Now in Pres Picks %>
		<%@ include file="/departments/whatsgood/now_in_prespicks.jspf" %>
	<% //End Now in Pres Picks %>

	<%
	//START Grocery Deals
        Image groDeptImage = null;
		boolean isDepartment = true;
		
		//String trkCode = (String)request.getAttribute("trk");

		if (trkCode!=null && !"".equals(trkCode.trim()) ) {
			trkCode = "&trk="+trkCode.trim();
		} else {
			trkCode = "";
		}

		catId = request.getParameter("catId");
		String deptId = request.getParameter("deptId");

		//ContentNodeModel 
		currentFolder = null;
		if (catId!=null) {
			currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
			if ("picks_love".equalsIgnoreCase(catId) || "wgd".equalsIgnoreCase(deptId)) {
				deptId = "gro";
				isDepartment = true;
			%>
				<%@ include file="/includes/layouts/i_featured_products_whatsgood.jspf" %>
			<%
			}
		} else {
			currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
		} 
	//END Grocery Deals
	%>

	<% //START AD spots %>
		<fd:IncludeMedia name="/media/editorial/whats_good/whats_good_line.html" />

		<% if (!emailPage && FDStoreProperties.isAdServerEnabled()) {
			//not an email 
			%>
			<table style="text-align: center;">
				<tr>
					<td>
						<!-- AD SPOT Left -->
						<SCRIPT LANGUAGE=JavaScript>
							<!--
								OAS_AD('WGL');
							//-->
						</SCRIPT>
					</td>
					<td>
						<!-- AD SPOT Center -->
						<SCRIPT LANGUAGE=JavaScript>
							<!--
								OAS_AD('WGC');
							//-->
						</SCRIPT>
					</td>
					<td>
						<!-- AD SPOT Right -->
						<SCRIPT LANGUAGE=JavaScript>
							<!--
								OAS_AD('WGR');
							//-->
						</SCRIPT>
					</td>
				</tr>
			</table>
		<%}else{
			//is an email, or adserver is disabled
			%>
			<fd:IncludeMedia name="/media/editorial/whats_good/whats_good_AD_space.ftl" parameters="<%=params%>"/>
		<%}%>
	<% //END AD spots %>
	
	<!-- END EMAIL -->

</tmpl:put>
</tmpl:insert>
