<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.util.RatingUtil'%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.net.*'%>
<%@ page import='java.util.*'%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<% //expanded page dimensions
final int W_DEPARTMENT_TOTAL = 765;
final int W_DEPARTMENT_RATE_LEFT = 437;
final int W_DEPARTMENT_RATE_RIGHT = 328;

final int W_DEPARTMENT_COFFEE_LEFT = 656;
final int W_DEPARTMENT_COFFEE_RIGHT = 109;
%>

<%!
final Logger LOG = LoggerFactory.getInstance("department.jsp");
%>
<%
	String deptId=request.getParameter("deptId");
	final boolean isIncludeDeptBottom = !"dai".equals(deptId) && !"gro".equals(deptId) && !"hba".equals(deptId) && !"fro".equals(deptId) && !"fdi".equals(deptId);
%>

<fd:CheckLoginStatus guestAllowed="true" />
<%FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);%>
<fd:CheckDraftContextTag/>
<fd:BrowsePartialRolloutRedirector user="<%=user%>" oldToNewDirection="true" id="${param.deptId}"/>

<fd:Department id='department' departmentId='<%= deptId %>'/>
<%

	//transfer to the recipe_department page if this is a recipe department
	if (department instanceof RecipeDepartment) { 
		%><jsp:forward page="/recipe_dept.jsp" /><%
	}
	
	
	/* APPDEV-2723 stop gap ticket. remove this once cohort feature targetting is availeble. */
	boolean cohortMatch = false;
	
	//check prop for a match and redirect if one is found
	String cohortMatcher = FDStoreProperties.getCohortMatcher();
	
	String[] cMatch1 = new String[0];
	String[] cMatch2 = new String[0];
	
	cMatch1 = cohortMatcher.split(";");
	for (int n = 0; n < cMatch1.length; n++) {
		cMatch2 = cMatch1[n].split("=");
		
		if ((deptId).equalsIgnoreCase(cMatch2[0]) && cMatch2.length == 2) {
			for (String curVal : cMatch2[1].split(",")) {
				if ((user.getCohortName()).equalsIgnoreCase(curVal)) {
					cohortMatch = true;
					break;
				}
			}
		}
	}

	LOG.debug("cohortMatch info:"+cohortMatch+" "+cohortMatcher+" "+user.getCohortName());
	if (cohortMatch) {
		LOG.debug("redirecting due to cohort match");
		%><jsp:forward page="/department_cohort_match.jsp" /><%
	}

	final ContentNodeModel currentFolder = department;
	final DepartmentModel departmentModel = (DepartmentModel) department;

	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", departmentModel.getPath());
	request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,SideCartBottom,WineTopRight,WineBotLeft,WineBotMiddle,WineBotRight,4mmAd1,4mmAd2");

	String title = departmentModel != null ? departmentModel.getPageTitle() : "FreshDirect - " + currentFolder.getFullName();
	title = title.replaceAll("<[^>]*>", "");
	if (!ContentFactory.getInstance().getPreviewMode()) {
		if (currentFolder.isHidden()) {
			response.sendRedirect(response.encodeRedirectURL(currentFolder.getHideUrl()));
			return;
		}
	}

	//if there is  redirect_url setting  then go to that url regardless of the previewmode setting
	String redirectURL = (currentFolder instanceof HasRedirectUrl ? ((HasRedirectUrl)currentFolder).getRedirectUrl() : null);
	if (redirectURL != null) {
		redirectURL = response.encodeRedirectURL(redirectURL);
 		response.sendRedirect(redirectURL);
		return;
	}

	//[APPREQ-77] Page uses include media type layout
	int layouttype = departmentModel.getLayoutType(-1);
	boolean isIncludeMediaLayout = (layouttype == EnumLayoutType.MEDIA_INCLUDE.getId());
%>

<tmpl:insert template='<%= (isIncludeMediaLayout ? "/common/template/no_nav.jsp" : "/common/template/right_nav.jsp") %>'>
	
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="<%=title%>" metaDescription="<%=departmentModel.getSEOMetaDescription()%>"/>
	</tmpl:put>

<tmpl:put name='content' direct='true'>
	<%
		/*
		int ttl=14400; 
		String keyPrefix="deptLayout_";
	
		if("fru".equals(deptId) ||"veg".equals(deptId) || "sea".equals(deptId) || "wgd".equals(deptId)) {
			if(user.isProduceRatingEnabled()) { 
				//Caching fru,veg,sea,gro,hba,dai,fro,big,mea,wgd depts per pricing zone.
	             keyPrefix=keyPrefix+user.getPricingZoneId();
	             keyPrefix=keyPrefix+user.isProduceRatingEnabled()+"_";
	             ttl=180;
	 		}
		} else if("gro".equals(deptId) ||"hba".equals(deptId)||"dai".equals(deptId) ||"fro".equals(deptId) ||"big".equals(deptId)){
			keyPrefix=keyPrefix+user.getPricingZoneId();
			ttl=3600;
		} else if("mea".equals(deptId)){
			keyPrefix=keyPrefix+user.getPricingZoneId();
			ttl=120;
		}
		
		boolean useOsCache = true;
		if ( "fdi".equals(deptId) || "usq".equals(deptId) ) {
			useOsCache = false;
		}
		//additional keyPrefix change here for cohort control
		keyPrefix += user.getCohortName();
		*/
	%>
	<%-- oscache:cache key='<%= keyPrefix+request.getQueryString() %>' time='<%= useOsCache ? ttl : 0 %>' --%>
	
	<%
	/*try {*/
		if (isIncludeMediaLayout) {
			%><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10" /> <%@ include file="/common/template/includes/catLayoutManager.jspf" %><br /><%  
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
				!JspMethods.getWineAssociateId().equals(deptId.toUpperCase()) &&
				introCopy!=null &&
				introCopy.trim().length()>0) { %>
				
				<table width="<%=W_DEPARTMENT_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
					<tr valign="top">
						<% if (deptIdentifier.equalsIgnoreCase("Coffee")) { %>
							<td class="text11" width="<%=W_DEPARTMENT_COFFEE_LEFT%>">
								<Img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="9" /><br />
								<font class="title16">
									<% if (introTitle != null || !"".equals(introTitle)) { %>
										<%=introTitle%>
									<% } %>
								</font><br />
								<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3" /><br />
								<fd:IncludeMedia name='<%= introCopy %>' /><br />
								<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4" />
							</td>
						<% } else if ( EnumLayoutType.FOURMM_DEPARTMENT.getId() != layouttype ) { %>
							<td class="text11" width="<%=W_DEPARTMENT_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="9" /><br />
								<font class="title16"><%=introTitle%></font><br />
								<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="3" /><br />
								<fd:IncludeMedia name='<%= introCopy %>' /><br />
								<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4" />
							</td>
						<% } %>
					</tr>
				</table>
			<% }

			if("big".equals(deptId)){//featured item footer-header
				%><%@ include file="/includes/department_bottom_featured.jspf"%><%
			}

			if (rateNRankLinks.length()>0) { %>
				<table width="<%=W_DEPARTMENT_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
					<tr valign="top">
						<td width="<%=W_DEPARTMENT_TOTAL%>" colspan="2"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="2" /></td>
					</tr>
					<tr valign="top">
						<td width="<%=W_DEPARTMENT_RATE_LEFT%>"><font class="text10bold">Compare <%= departmentModel.getFullName() %> by:&nbsp;</font><%= rateNRankLinks.toString() %><%-- include file="/include/i_show_rating_groups.jsp" --%><br />
						<img src="/media_stat/images/layout/clear.gif" alt="" WIDTH="10" HEIGHT="3" border="0"></td>
						<td width="<%=W_DEPARTMENT_RATE_RIGHT%>" align="right"><a href="javascript:soon()">View all <%= departmentModel.getFullName().toLowerCase() %></a><br />
						<img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_DEPARTMENT_RATE_RIGHT%>" height="1" border="0"></td>
					</tr>
					<tr valign="top">
						<td width="<%=W_DEPARTMENT_TOTAL%>" bgcolor="#cccccc" colspan="2"><img src="/media_stat/images/layout/cccccc.gif" alt="" width="10" height="1" /></td>
					</tr>
					<tr valign="top">
						<td width="<%=W_DEPARTMENT_TOTAL%>" colspan="2"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10" /></td>
					</tr>
				</table>
			<% } // rateNRankLinks.length()>0

			if ("win".equals(deptId)) { // bc wine page
				%><%@ include file="/departments/wine/bc_home.jspf"%><%
			} else if (JspMethods.getWineAssociateId().equals(deptId.toUpperCase())) { //usq wine page
				%><%@ include file="/departments/wine/usq_home.jspf"%><%
				
			//-----------------------------------------------------------------------------------
			// compatibility dept page for bakery
			// this is the old bakery page for the old cms data
			// when the bakery related cms changes are done this section is to be removed!
			} else if ("bak".equals(deptId) && !ContentNodeModelUtil.getLayout(departmentModel, null).equals(EnumLayoutType.BAKERY_DEPARTMENT) ) { 
				String trkCode= "dpage";	 
				%><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="12" />	 
				<%@ include file="/includes/layouts/bakerydpt.jspf" %><br />	 
				<%@ include file="/includes/department_bottom.jspf"%><%
			//-----------------------------------------------------------------------------------	
			
			} else {
				
				//use this spacer image if not on buy big department (media include available for spacing)
				if (!"big".equals(deptId)) {
					%><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10" /><%
				} %>

				<%@ include file="/common/template/includes/catLayoutManager.jspf" %><br /><%

				if (isIncludeDeptBottom) {
					// Display this piece of code if layout is not 'grocery_dept_layout_new' (?)
					List<Html> middleMediaList = departmentModel.getDepartmentMiddleMedia();
					if (middleMediaList != null && !middleMediaList.isEmpty() && !"our_picks".equals(deptId)  ) {
						for ( Html middleMediaPath : middleMediaList ) {
							%><div style="width: <%=W_DEPARTMENT_TOTAL%>px;" align="center"><fd:IncludeMedia name="<%=middleMediaPath.getPath()%>" /></div><%
						}
					}
				}
		
				if ("local".equals(deptId)) { 
					//featured item footer
					%>
					<%@ include file="/includes/department_brand_category_dropdown.jspf" %>					
					<%@ include file="/includes/department_bottom_featured.jspf"%><%
				} else { 
					if (isIncludeDeptBottom) {
						%><%@ include file="/includes/department_bottom.jspf"%><%
					}
				}
			}
		} // !isIncludeMediaLayout
		
	/*} catch (Exception ex) {
		LOG.error("error while generating department page body", ex);
		*/
  		%>
		<%-- oscache:usecached/ --%>
  	<%-- } --%>
	
	<%-- /oscache:cache --%>
		
</tmpl:put>
</tmpl:insert>
