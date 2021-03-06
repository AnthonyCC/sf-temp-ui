<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="/includes/i_dynamicRows_required.jspf" %>
<%


	/* control page and row-wide debug messages */
		myDebug = FDStoreProperties.isWhatsGoodDebugOn();

	// set media path base
	mediaPathTempBase="/media/editorial/whats_good/";

	// set deptId default
	deptId = request.getParameter("deptId");

	//set deptId default
	if (deptId==null || "".equals(deptId)) { deptId="wgd"; }
	
	log(myDebug, "PAGE : Starting What's Good...");

/*
 *	Set up email toggle
 *	Assume false by default, only turning on if "email=true" is sent in the request
 */
	String templatePath = "/common/template/dnav_no_space_optimized.jsp"; //the default
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
		
	ContentNodeModel node = PopulatorUtil.getContentNode(deptId);
%>

<tmpl:insert template='<%= templatePath %>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - What's Good"/>
    </tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'>FreshDirect - What's Good</tmpl:put> --%>
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

		<div class="WG_line" style="background-color: #f8993b; text-align: left; margin-bottom: 12px; font-size: 0px; heigth: 15px;">
			<img src="/media/editorial/whats_good/img/bar_stripes.gif" border="0" alt="" />
		</div>

		<%
			log(myDebug, "PAGE IncludeMedia: /media/editorial/whats_good/whats_good_top_msg.html");
			mediaPathTemp=mediaPathTempBase+"whats_good_top_msg.html";
		%>
		<fd:IncludeMedia name="<%= mediaPathTemp %>" />
	<% //END top section %>
	<%
		
		//get property with rows
		strDynRows = FDStoreProperties.getWhatsGoodRows();
		//strDynRows = "useConfig:test";

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
