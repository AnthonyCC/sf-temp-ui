<%@ include file="/includes/i_dynamicRows_required.jspf" %>
<%


	/* control page and row-wide debug messages */
		myDebug = FDStoreProperties.isWhatsGoodDebugOn();

	//set media path base
	mediaPathTempBase="/media/editorial/whats_good/";

	//set deptId default
	deptId = request.getParameter("deptid");

	//set deptId default
	if (deptId==null || "".equals(deptId)) { deptId="wgd"; }
	
	log(myDebug, "PAGE : Starting What's Good...");

/*
 *	Set up email toggle
 *	Assume false by default, only turning on if "email=true" is sent in the request
 */
	String templatePath = "/common/template/dnav_no_space.jsp"; //the default
	emailPage = false;

	if ( "true".equals ((String)request.getParameter("email")) ) {
		emailPage = true;
		baseUrl = "http://www.freshdirect.com";
		templatePath = "/common/template/blank.jsp"; //email
			log(myDebug, "PAGE template set: "+templatePath);
		isTransactionalRow = false; // turn off transactional for emails
			log(myDebug, "PAGE turning transactional to: "+isTransactionalRow);
	}

	//	add emailPage to params passed to ftls
	params.put("emailPage", Boolean.toString(emailPage));
	log(myDebug, "PAGE email mode: "+emailPage);
	params.put("baseUrl", baseUrl);
		log(myDebug, "PAGE baseUrl set: "+baseUrl);
	
%>

	<tmpl:insert template='<%= templatePath %>'>
	<tmpl:put name='title' direct='true'>FreshDirect - What's Good</tmpl:put>
	<tmpl:put name='content' direct='true'>

	
	<%
		//--------OAS Page Variables-----------------------
		request.setAttribute("sitePage", "www.freshdirect.com/whatsgood");
		request.setAttribute("listPos", "WGLeft,WGCenter,WGRight,SystemMessage");

	if (emailPage) {
		log(myDebug, "PAGE emailPage: "+emailPage);
	%>
		<!-- START EMAIL -->
		<style>
			body { width: 690px; text-align: center; }
			.WG_EMAIL, table { width: 100%; text-align: center; }
		</style>
		<center>
		<table width="690" border="0" cellspacing="0" cellpadding="0" align="center" class="WG_EMAIL">

		<tr>
			<td valign="bottom">

	<% } %>

	
	<% //START top section %>

		<%
			log(myDebug, "PAGE IncludeMedia: /media/editorial/whats_good/whats_good_line.html");
			mediaPathTemp=mediaPathTempBase+"whats_good_line.html";
		%>
		<fd:IncludeMedia name="<%= mediaPathTemp %>" />

		<%
			log(myDebug, "PAGE IncludeMedia: /media/editorial/whats_good/whats_good_top_msg.html");
			mediaPathTemp=mediaPathTempBase+"whats_good_top_msg.html";
		%>
		<fd:IncludeMedia name="<%= mediaPathTemp %>" />
	<% //END top section %>
	<%
		
		//get property with rows
		strDynRows = FDStoreProperties.getWhatsGoodRows();

		//set prefix
		prefix = "whats_good";
	%>


	<%@ include file="/includes/i_dynamicRows_logic.jspf" %>

	<%
	if (emailPage) {
		/* finish html for email version */
	%>
				</td>
			</tr>
		</table>
		</center>
		<!-- END EMAIL -->
	<% } %>

</tmpl:put>
</tmpl:insert>


<% log(myDebug, "PAGE : Ending What's Good..."); %>
