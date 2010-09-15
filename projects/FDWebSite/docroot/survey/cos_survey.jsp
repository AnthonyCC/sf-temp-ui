<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<fd:CheckLoginStatus />
<%

	/*
	 *	we need to replicate a bunch of variables from category.jsp since it redirected to here
	 *	we're manually setting some of them rather than pulling them from CMS
	 */

	ContentNodeModel currentFolder = null;
	String deptId = null;
	String catId = NVL.apply(request.getParameter("catId"), "");

	boolean noLeftNav = false; //default

	FDSessionUser sessionuser = (FDSessionUser)session.getAttribute(SessionName.USER);

	boolean submitted = request.getParameter("info") != null && request.getParameter("info").indexOf("thankyou") > -1;
	if (!submitted) {
		submitted = request.getParameter("successPage") != null && request.getParameter("successPage").indexOf("thankyou") > -1;
	}
	boolean testSub = false;

	String redirectSuccessPage = NVL.apply(request.getParameter("successPage"), "");

	if ("".equals(redirectSuccessPage)) {
		redirectSuccessPage = "/index.jsp";
	}

	String survey_source = NVL.apply((String)request.getAttribute("survey_source"), NVL.apply(request.getParameter("survey_source"), "cos_survey_source"));
	//reset attribute to be sure it's current
		request.setAttribute("survey_source", survey_source);
	
	String survey_name = NVL.apply(request.getParameter("survey_name"), "cos_survey_name");
	
	String customSuccessPage = request.getRequestURI();

	int templateType = 1;
	int layouttype = -1;
	String jspTemplate = "/common/template/left_dnav.jsp";


	List mediaTop = null;
	List mediaMiddle = null;
	List mediaBottom = null;

	if (!"".equals(catId)) {
		currentFolder = ContentFactory.getInstance().getContentNode(catId);
		CategoryModel categoryModel = (currentFolder instanceof CategoryModel) ? (CategoryModel) currentFolder : null;
		final ProductContainer productContainer = (currentFolder instanceof ProductContainer) ? (ProductContainer) currentFolder : null;

		templateType = productContainer != null ? productContainer.getTemplateType(1) : 1;
		layouttype = productContainer != null ? productContainer.getLayoutType(-1) : -1;

		//get left nav setting from cat
		if (productContainer!=null) {
			noLeftNav = !productContainer.isShowSideNav();
		}
		
		if (categoryModel != null) {
			//we have a category

			deptId=((CategoryModel)currentFolder).getDepartment().getContentName();

			mediaTop	= (currentFolder instanceof CategoryModel) ? ((CategoryModel)currentFolder).getTopMedia() : null;
			mediaMiddle	= (currentFolder instanceof CategoryModel) ? ((CategoryModel)currentFolder).getMiddleMedia() : null;
			mediaBottom	= (currentFolder instanceof CategoryModel) ? ((CategoryModel)currentFolder).getBottomMedia() : null;

			//this needs to get the real params, not just catId
			customSuccessPage = request.getRequestURI()+"?"+request.getQueryString();
			//redirectSuccessPage = customSuccessPage;

			if (sessionuser!= null) { if (survey_name.equals(sessionuser.getLastCOSSurvey())) { testSub=true; } }
		}

	}
	
	String customActionName = "submitCorporateServiceSurvey";
	if (!"cos_survey_name".equals(survey_name)) {
		customActionName = survey_name;
	}


	// [APPREQ-77] Page uses include media type layout
	boolean isIncludeMediaLayout = (layouttype == EnumLayoutType.MEDIA_INCLUDE.getId()); // [APPREQ-77]

	// Assign the correct template. Changed here to point to new top_nav_only.jsp instead of no_nav.jsp
	if (isIncludeMediaLayout) {
		jspTemplate = noLeftNav ? "/common/template/top_nav_only.jsp" : "/common/template/left_dnav.jsp";
	} else if (noLeftNav) {
		jspTemplate = "/common/template/right_dnav.jsp";
	} else {
		if (EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
			// assuming only 1 wine store at a time
			jspTemplate = "/common/template/usq_sidenav.jsp";
		} else { //assuming the default (Generic) Template
			jspTemplate = "/common/template/both_dnav.jsp";
		}
	}

%>


<!-- include template <%= jspTemplate %> layout : <%= EnumLayoutType.getLayoutType(layouttype)%> -->
<tmpl:insert template='<%=jspTemplate%>'>
	<% if (!noLeftNav) { %>
		<tmpl:put name='leftnav' direct='true'> <%-- <<< some whitespace is needed here --%></tmpl:put>
	<% } %>

	<tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName() %></tmpl:put>

	<tmpl:put name='content' direct='true'>
		
		<fd:CorporateServiceSurvey result='result' actionName='<%= customActionName %>' successPage='<%= customSuccessPage %>'>
			<% if (submitted) {
				//successful submit

				//if we have a middle media, include it here
				if (mediaMiddle != null && mediaMiddle.size() > 0) {
					for (int i=0; i < mediaMiddle.size(); i++) {
						if ( ((Html)mediaMiddle.get(i)).getPath() != null ) {
							%><fd:IncludeMedia name='<%= ((Html)mediaMiddle.get(i)).getPath() %>' /><%
						}
					}
				}

			} else {
				

					//if we have a top media, include it here
					if (mediaTop != null && mediaTop.size() > 0) {
						if ( ((Html)mediaTop.get(0)).getPath() != null ) {
							%><fd:IncludeMedia name='<%= ((Html)mediaTop.get(0)).getPath() %>' /><%
						}
					}

					if (!result.isSuccess()) {
					  String errorMsg=SystemMessageList.MSG_MISSING_SURVEY_INFO;
					  //clear from session on error, needed?
					 sessionuser.setLastCOSSurvey("");
					%>
						<%@ include file="/includes/i_error_messages.jspf" %>
				<% } %>
				<table class="COSSurvey">
				<form method="post" name="corporateServiceSurvey" id="corporateServiceSurvey">
					<tr>
						<td><img src="/media_stat/images/layout/clear.gif" class="COSSurvey_spacer01" border="0" alt="" /></td>
						<td><img src="/media_stat/images/layout/clear.gif" class="COSSurvey_spacer02" border="0" alt="" /></td>
						<td><img src="/media_stat/images/layout/clear.gif" class="COSSurvey_spacer03" border="0" alt="" /></td>
						<td><img src="/media_stat/images/layout/clear.gif" class="COSSurvey_spacer04" border="0" alt="" /></td>
					</tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="companyName"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>Company Name<fd:ErrorHandler result="<%=result%>" name="companyName"></span></fd:ErrorHandler></td>
						<td colspan="3"><input type="text" size="25" class="text13" name="companyName" value="<%=request.getParameter("companyName")%>"></td>
					</tr>
					<tr><td colspan="4"><span class="space4pix"><br /></span></td></tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="streetAddress"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>Street Address<fd:ErrorHandler result="<%=result%>" name="streetAddress"></span></fd:ErrorHandler></td>
						<td><input type="text" size="25" class="text13" name="streetAddress" value="<%=request.getParameter("streetAddress")%>"></td>
						<td class="text12 padLeft10" align="right"><fd:ErrorHandler result="<%=result%>" name="floorSuite"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>Floor/Suite&nbsp;#<fd:ErrorHandler result="<%=result%>" name="floorSuite"></span></fd:ErrorHandler>&nbsp;</td>
						<td class="padRight10"><input type="text" size="5" class="text13" name="floorSuite" value="<%=request.getParameter("floorSuite")%>"></td>
					</tr>
					<tr><td colspan="4"><span class="space2pix"><br /></span></td></tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="city"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>City<fd:ErrorHandler result="<%=result%>" name="city"></span></fd:ErrorHandler></td>
						<td><input type="text" size="25" class="text13" name="city" value="<%=request.getParameter("city")%>"></td>
						<td align="right" class="text12"><fd:ErrorHandler result="<%=result%>" name="state"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>State<fd:ErrorHandler result="<%=result%>" name="state"></span></fd:ErrorHandler>&nbsp;</td>
						<td class="padRight10"><input type="text" size="3" class="text13" name="state" value="NY" value="<%=request.getParameter("state")%>"></td>
					</tr>
					<tr><td colspan="4"><span class="space4pix"><br /></span></td></tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="zip"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>ZIP<fd:ErrorHandler result="<%=result%>" name="zip"></span></fd:ErrorHandler> <fd:ErrorHandler result="<%=result%>" name="zip4"><span style="color:#CC0000; font-weight: bold;"></fd:ErrorHandler>+ 4<fd:ErrorHandler result="<%=result%>" name="zip4"></span></fd:ErrorHandler></td>
						<td><input type="text" size="5" maxlength="5" class="text13" name="zip" value="<%=request.getParameter("zip")%>"> - <input type="text" size="4" class="text13" maxlength="4" name="zip4" value="<%=request.getParameter("zip4")%>"></td>
						<td colspan="2" rowspan="8" class="padRight10"><%
							//if we have a second top media, include it here
							if (mediaTop != null && mediaTop.size() > 1) {
								if ( ((Html)mediaTop.get(1)).getPath() != null ) {
									%><fd:IncludeMedia name='<%= ((Html)mediaTop.get(1)).getPath() %>' /><%
								}
							}
						%></td>
					</tr>
					<tr><td colspan="2"><span class="space4pix"><br /></span></td></tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="numEmp"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>Number of Employees<fd:ErrorHandler result="<%=result%>" name="numEmp"></span></fd:ErrorHandler></td>
						<td><input type="text" size="8" class="text13" name="numEmp" value="<%=request.getParameter("numEmp")%>"></td>
					</tr>
					<tr><td colspan="2"><span class="space8pix"><br /></span></td></tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="contact"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>Contact Name<fd:ErrorHandler result="<%=result%>" name="contact"></span></fd:ErrorHandler></td>
						<td><input type="text" size="25" class="text13" name="contact" value="<%=request.getParameter("contact")%>"></td>
					</tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="title"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>Title<fd:ErrorHandler result="<%=result%>" name="title"></span></fd:ErrorHandler></td>
						<td><input type="text" size="25" class="text13" name="title" value="<%=request.getParameter("title")%>"></td>
					</tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="phone"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>Contact Number<fd:ErrorHandler result="<%=result%>" name="phone"></span></fd:ErrorHandler></td>
						<td><input type="text" size="25" class="text13" name="phone" value="<%=request.getParameter("phone")%>"></td>
					</tr>
					<tr>
						<td class="text12 padLeft10"><fd:ErrorHandler result="<%=result%>" name="email"><span style="color:#CC0000; font-weight: bold;">*</fd:ErrorHandler>Email Address<fd:ErrorHandler result="<%=result%>" name="email"></span></fd:ErrorHandler></td>
						<td><input type="text" size="25" class="text13" name="email" value="<%=request.getParameter("email")%>"><fd:ErrorHandler result="<%=result%>" name="email" id="errorMsg"> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
					</tr>
					<tr>
						<td colspan="4" align="left">
							<br /><img src="/media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="10" alt="" /><br />
						</td>
					<tr>
						<td colspan="4" class="text12 padLeft10" align="left">
							<br />
							<input type="image" value="submit" src="/media/images/buttons/COS_submit.gif" width="87" height="20" />
							<input type="image" value="clear" src="/media/images/buttons/COS_clear.gif" width="88" height="22" onclick="document.corporateServiceSurvey.reset(); return false;" />

							<input type="hidden" name="successPage" value="<%= customSuccessPage %>" />
						</td>
					</tr>
				</form>
				</table>
			<% } %>
		</fd:CorporateServiceSurvey>

		<%
			//if we have a bottom media, include it here
			if (mediaBottom != null && mediaBottom.size() > 0) {
				for (int i=0; i < mediaBottom.size(); i++) {
					if ( ((Html)mediaBottom.get(i)).getPath() != null ) {
						%><fd:IncludeMedia name='<%= ((Html)mediaBottom.get(i)).getPath() %>' /><%
					}
				}
			}
		%>
	</tmpl:put>
	
</tmpl:insert>
