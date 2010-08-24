<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.util.RatingUtil'%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.net.*'%>
<%@ page import='java.util.*'%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%!
	final Logger LOG = LoggerFactory.getInstance("department.jsp");
%>
<%
	String deptId=request.getParameter("deptId");
	final boolean isIncludeDeptBottom = !"dai".equals(deptId) && !"gro".equals(deptId) && !"hba".equals(deptId) && !"fro".equals(deptId) && !"fdi".equals(deptId);
%>

<fd:CheckLoginStatus guestAllowed="true" />
<fd:Department id='department' departmentId='<%= deptId %>'/>
<%

	//transfer to the recipe_department page if this is a recipe department
	if (department instanceof RecipeDepartment) { 
		%><jsp:forward page="/recipe_dept.jsp" /><%
	}

	final ContentNodeModel currentFolder = department;
	final DepartmentModel departmentModel = (DepartmentModel) department;

	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", departmentModel.getPath());
	request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,SideCartBottom,WineTopRight,WineBotLeft,WineBotMiddle,WineBotRight");


	if (!ContentFactory.getInstance().getPreviewMode()) {
		if (currentFolder.isHidden()) {
			response.sendRedirect(response.encodeRedirectURL(currentFolder.getHideUrl()));
			return;
		}
	}

	//if there is  redirect_url setting  then go to that url regardless of the previewmode setting
	String redirectURL = (currentFolder instanceof HasRedirectUrl ? ((HasRedirectUrl)currentFolder).getRedirectUrl() : null);
	if (redirectURL != null) {
		redirectURL = response.encodeRedirectURL(redirectURL); 		response.sendRedirect(redirectURL);
		return;
	}

	//[APPREQ-77] Page uses include media type layout
	int layouttype = departmentModel.getLayoutType(-1);
	boolean isIncludeMediaLayout = (layouttype == EnumLayoutType.MEDIA_INCLUDE.getId());
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
%>

<tmpl:insert template='<%= (isIncludeMediaLayout ? "/common/template/no_nav.jsp" : "/common/template/right_nav.jsp") %>'>
<tmpl:put name='title' direct='true'>FreshDirect - <%= departmentModel.getFullName() %></tmpl:put>
<tmpl:put name='content' direct='true'>
	<%
		int ttl=14400; 
		String keyPrefix="deptLayout_";
	
		if("fru".equals(deptId) ||"veg".equals(deptId) || "sea".equals(deptId) || "wgd".equals(deptId)) {
			if(user.isProduceRatingEnabled()) { 
				//Caching fru,veg,sea,gro,hba,dai,fro,big,mea,wgd depts per pricing zone.	             keyPrefix=keyPrefix+user.getPricingZoneId();	             keyPrefix=keyPrefix+user.isProduceRatingEnabled()+"_";	             ttl=180;	 		}
		} else if("gro".equals(deptId) ||"hba".equals(deptId)||"dai".equals(deptId) ||"fro".equals(deptId) ||"big".equals(deptId)){
			keyPrefix=keyPrefix+user.getPricingZoneId();
			ttl=3600;
		} else if("mea".equals(deptId)){
			keyPrefix=keyPrefix+user.getPricingZoneId();
			ttl=120;
		}
		
		boolean useOsCache = true;
		if ( "fdi".equals(deptId) ) {
			useOsCache = false;
		}
	%>
	<oscache:cache key='<%= keyPrefix+request.getQueryString() %>' time='<%= useOsCache ? ttl : 0 %>'>
	
	<%
	try {
		if (isIncludeMediaLayout) {
			%><img src="/media_stat/images/layout/clear.gif" width="1" height="10" /> <%@ include file="/common/template/includes/catLayoutManager.jspf" %><br /><%  
		} else {
			Html introCopyAttribute = departmentModel.getEditorial();
			String introCopy = (introCopyAttribute == null ? "" : introCopyAttribute.getPath());
			String introTitle = departmentModel.getEditorialTitle();
			StringBuffer organicLegend = new StringBuffer();
			String deptIdentifier = departmentModel.getFullName().toUpperCase();
			Image deptImage = (Image)departmentModel.getPhoto();

			//  get the rating & ranking stuff
			String ratingGroupNames = departmentModel.getRatingGroupNames();
			String rateNRankLinks = RatingUtil.buildDepartmentRatingLink(departmentModel, response);

	
	    	// temporary (???): if grocery, wine or bakery department..then we dont want to use the standard logic
			if ( "OUR_PICKS, FRO, GRO, DAI, SPE".indexOf(departmentModel.getContentName().toUpperCase())==-1 &&
				!"bak".equals(deptId) &&
				!"win".equals(deptId) &&
				!"usq".equals(deptId) &&
				introCopy!=null &&
				introCopy.trim().length()>0) { %>
				
				<table width="550" cellpadding="0" cellspacing="0" border="0">
					<tr valign="top">
						<% if (deptIdentifier.equalsIgnoreCase("Coffee")) { %>
							<td class="text11" width="462">
								<Img src="/media_stat/images/layout/clear.gif" width="1" height="9" /><br />
								<font class="title16">
									<% if (introTitle != null || !"".equals(introTitle)) { %>
										<%=introTitle%>
									<% } %>
								</font><br />
								<img src="/media_stat/images/layout/clear.gif" width="1" height="3" /><br />
								<fd:IncludeMedia name='<%= introCopy %>' /><br />
								<img src="/media_stat/images/layout/clear.gif" width="1" height="4" />
							</td>
							<td class="text11" width="88" align="right">
								<img src="<%=deptImage.getPath()%>" width="<%=deptImage.getWidth()%>"  height="<%=deptImage.getHeight()%>" border="0" alt="Coffee Beans" />
							</td>
						<% } else if ( EnumLayoutType.FOURMM_DEPARTMENT.getId() != layouttype ) { %>
							<td class="text11" width="550"><img src="/media_stat/images/layout/clear.gif" width="1" height="9" /><br />
								<font class="title16"><%=introTitle%></font><br />
								<img src="/media_stat/images/layout/clear.gif" width="1" height="3" /><br />
								<fd:IncludeMedia name='<%= introCopy %>' /><br />
								<img src="/media_stat/images/layout/clear.gif" width="1" height="4" />
							</td>
						<% } %>
					</tr>
				</table>
			<% }

			if("big".equals(deptId)){//featured item footer-header
				%><%@ include file="/includes/department_bottom_featured.jspf"%><%
			}

			if (rateNRankLinks.length()>0) { %>
				<table width="550" cellpadding="0" cellspacing="0" border="0">
					<tr valign="top">
						<td width="550" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="2" /></td>
					</tr>
					<tr valign="top">
						<td width="335"><font class="text10bold">Compare <%= departmentModel.getFullName() %> by:&nbsp;</font><%= rateNRankLinks.toString() %><%-- include file="/include/i_show_rating_groups.jsp" --%><br />
						<img src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="3" border="0"></td>
						<td width="215" align="right"><a href="javascript:soon()">View all <%= departmentModel.getFullName().toLowerCase() %></a><br />
						<img src="/media_stat/images/layout/clear.gif" width="215" height="1" border="0"></td>
					</tr>
					<tr valign="top">
						<td width="550" bgcolor="#cccccc" colspan="2"><img src="/media_stat/images/layout/cccccc.gif" width="10" height="1" /></td>
					</tr>
					<tr valign="top">
						<td width="550" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" /></td>
					</tr>
				</table>
			<% } // rateNRankLinks.length()>0

			if ("win".equals(deptId)) { // bc wine page
				%><%@ include file="/departments/wine/bc_home.jspf"%><%
			} else if ("usq".equals(deptId)) { //usq wine page
				%><%@ include file="/departments/wine/usq_home.jspf"%><% 
			} else if ("bak".equals(deptId)) { // bak needs top buffer 
				String trkCode= "dpage"; 
				%><img src="/media_stat/images/layout/clear.gif" width="1" height="12" />
				<%@ include file="/includes/layouts/bakerydpt.jspf" %><br />
				<%@ include file="/includes/department_bottom.jspf"%><%
			} else {
				//use this spacer image if not on buy big department (media include available for spacing)
				if (!"big".equals(deptId)) {
					%><img src="/media_stat/images/layout/clear.gif" width="1" height="10" /><%
				} %>

				<%@ include file="/common/template/includes/catLayoutManager.jspf" %><br /><%

				if (isIncludeDeptBottom) {
					// Display this piece of code if layout is not 'grocery_dept_layout_new' (?)
					List<Html> middleMediaList = departmentModel.getDepartmentMiddleMedia();
					if (middleMediaList != null && !middleMediaList.isEmpty() && !"our_picks".equals(deptId)  ) {
						for ( Html middleMediaPath : middleMediaList ) {
							%><div style="width: 550px;" align="left"><fd:IncludeMedia name="<%=middleMediaPath.getPath()%>" /></div><%
						}
					}
				}
		
				if ("local".equals(deptId)) { 
					//featured item footer
					%>
					<%@ include file="/includes/department_brand_category_dropdown.jspf" %>					
					<%@ include file="/includes/department_bottom_featured.jspf"%><%
				} else if("fdi".equals(deptId) && !ContentNodeModelUtil.getLayout(departmentModel, null).equals(EnumLayoutType.FOURMM_DEPARTMENT) ) {
					// old 4mm bottom page - remove this section if 4mm is finished
					%><%@ include file="/includes/department_fea_edit_bottom.jspf"%><%
				} else { 
					if (isIncludeDeptBottom) {
						%><%@ include file="/includes/department_bottom.jspf"%><%
					}
				}
			}
		} // !isIncludeMediaLayout
	} catch (Exception ex) {
		LOG.error("error while generating department page body", ex);  		%>
		<oscache:usecached/>
  	<% } %>
	
	</oscache:cache>
		
</tmpl:put>
</tmpl:insert>
