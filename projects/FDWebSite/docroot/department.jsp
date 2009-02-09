<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%
String deptId=request.getParameter("deptId");
final boolean isIncludeDeptBottom = !"dai".equals(deptId) && !"gro".equals(deptId) && !"hba".equals(deptId) && !"fro".equals(deptId);
%>

<fd:CheckLoginStatus guestAllowed="true" />
<fd:Department id='department' departmentId='<%= deptId %>'/>

<%
//transfer to the recipe_department page if this is a recipe department
if (department instanceof RecipeDepartment) {  %>
<jsp:forward page="/recipe_dept.jsp" />
<% }  

//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", department.getPath());
request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,SideCartBottom,WineTopRight,WineBotLeft,WineBotMiddle,WineBotRight");


ContentNodeModel currentFolder = department;

if (!ContentFactory.getInstance().getPreviewMode()) {
    if (currentFolder.isHidden()) {
        response.sendRedirect(response.encodeRedirectURL(currentFolder.getHideUrl()));
        return;
    }
}
//if there is  redirect_url setting  then go to that url regardless of the previewmode setting
Attribute attrib=currentFolder.getAttribute("REDIRECT_URL");
if (attrib!=null && attrib.getValue() !=null) {
    String redirectURL = response.encodeRedirectURL((String)attrib.getValue());
    response.sendRedirect(redirectURL);
    return;
}


//[APPREQ-77] Page uses include media type layout
int layouttype = currentFolder.getAttribute("LAYOUT", -1);
boolean isIncludeMediaLayout = (layouttype == EnumLayoutType.MEDIA_INCLUDE.getId());

%><tmpl:insert template='<%= (isIncludeMediaLayout ? "/common/template/no_nav.jsp" : "/common/template/right_nav.jsp") %>'>
    <tmpl:put name='title' direct='true'>FreshDirect - <%= department.getFullName() %></tmpl:put>
    <tmpl:put name='content' direct='true'>
<% int ttl=14400; 
   String keyPrefix="deptLayout_"; 
if("fru".equals(deptId) ||"veg".equals(deptId))  {
    
    FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
    if(user.isProduceRatingEnabled()) {
        keyPrefix=keyPrefix+user.isProduceRatingEnabled()+"_";
        ttl=180;
    }
} else if("gro".equals(deptId) ||"hba".equals(deptId)||"dai".equals(deptId) ||"fro".equals(deptId))  {
    ttl=3600;
}
%>
<oscache:cache key='<%= keyPrefix+request.getQueryString() %>'  time='<%=ttl%>'>
<%
try {
    if (isIncludeMediaLayout) {
        %><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10">
        <%@ include file="/common/template/includes/catLayoutManager.jspf" %><br><%
    } else {
		Attribute introCopyAttribute = department.getAttribute("EDITORIAL");
	    String introCopy = (introCopyAttribute == null ? "" : ((Html)introCopyAttribute.getValue()).getPath());
	    String introTitle = department.getEditorialTitle();
	    StringBuffer organicLegend = new StringBuffer();
	    String deptIdentifier = department.getFullName().toUpperCase();
	    Image deptImage = (Image)department.getAttribute("DEPT_PHOTO").getValue();
	

	    //  get the rating & ranking stuff
	    Attribute tmpAttribute = department.getAttribute("RATING_GROUP_NAMES");
	    StringBuffer rateNRankLinks = new StringBuffer();
	    
	    if (tmpAttribute !=null) {
	        StringTokenizer stRRNames = new StringTokenizer((String)tmpAttribute.getValue(),",");
	        while (stRRNames.hasMoreTokens()) {
	            String rrName = stRRNames.nextToken().toUpperCase();
	            // go find the attribute with that name and it's label
	            tmpAttribute = department.getAttribute(rrName);
	            if (tmpAttribute !=null) {
	                if (rateNRankLinks.length() > 1) rateNRankLinks.append(" | ");
	                rateNRankLinks.append("<a href=\"");
	                rateNRankLinks.append(response.encodeURL("/rating_ranking.jsp?deptId="+department+"&ratingGroupName="+rrName));
	                rateNRankLinks.append("\"");

	                // get the label for this rating group name.
	                tmpAttribute = department.getAttribute(rrName+"_LABEL");
	                if (tmpAttribute!=null) {
	                    rateNRankLinks.append((String)tmpAttribute.getValue());
	                } else {
	                  rateNRankLinks.append(rrName.replace('_',' '));
	                }
	                rateNRankLinks.append("</a>");
	            }
	        }
	    }




	    // temporary: if grocery, wine or bakery department..then we dont want to use the stantard logic
	    if ( "OUR_PICKS, FRO, GRO, DAI, SPE".indexOf(department.getContentName().toUpperCase())==-1 &&
	    		!"bak".equals(deptId) &&
                !"win".equals(deptId) &&
	    		!"usq".equals(deptId) &&
	    		introCopy!=null &&
	    		introCopy.trim().length()>0) {
	        %><table width="550" CELLPADDING="0" CELLSPACING="0" BORDER="0">
		<tr valign="top">
<%
	        if (deptIdentifier.equalsIgnoreCase("Coffee")) {
	            %>
		<td CLASS="text11" width="462"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="9"><BR><font class="title16">
<%
            if (introTitle != null || !"".equals(introTitle)) {
                %><%=introTitle%><%}%></font><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="3"><BR><fd:IncludeMedia name='<%= introCopy %>' /><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"></td>
		<td CLASS="text11" width="88" align="right"><img src="<%=deptImage.getPath()%>" width="<%=deptImage.getWidth()%>"  height="<%=deptImage.getHeight()%>" border="0" alt="Coffee Beans"><td>
<%
            } else {
                %><td class="text11" width="550"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="9"><BR><font class="title16"><%=introTitle%></font><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="3"><BR><fd:IncludeMedia name='<%= introCopy %>' /><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"></td><%
            }
            %></tr>
</TABLE>
<%
	    }


	    if (rateNRankLinks.length()>0) { //if (deptIdentifier.equalsIgnoreCase("COFFEE") || deptIdentifier.equalsIgnoreCase("TEA")) %>
<table width="550" CELLPADDING="0" CELLSPACING="0" BORDER="0">
	<TR VALIGN="TOP">
		<TD WIDTH="550" COLSPAN="2"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="2"></TD>
	</TR>
	<tr valign="top">
		<td width="335">
		    <font class="text10bold">Compare <%= department.getFullName() %> by:&nbsp;</font><%= rateNRankLinks.toString() %><%-- include file="/include/i_show_rating_groups.jsp" --%>
            <br><IMG src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="3" border="0"></td>
		<td width="215" align="right"><A HREF="javascript:soon()">View all <%= department.getFullName().toLowerCase() %></A><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="215" HEIGHT="1" border="0"></td>
	</tr>
	<TR VALIGN="TOP">
		<TD WIDTH="550" BGCOLOR="#CCCCCC" COLSPAN="2"><IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="10" HEIGHT="1"></TD>
	</TR>
	<TR VALIGN="TOP">
		<TD WIDTH="550" COLSPAN="2"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"></TD>
	</TR>
</table>
<%
        } // rateNRankLinks.length()>0


	    if (false) { //( "our_picks".equals(deptId) && introCopy!=null  && introCopy.trim().length()>0) {
%><table width="550" CELLPADDING="0" CELLSPACING="0" BORDER="0">
    <tr valign="top">
        <td class="text11" width="550">
	        <IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="3">
	        <BR><fd:IncludeMedia name='<%= introCopy %>' />
        </td>
    </tr>
</TABLE>
<%
        } // false

	    if ("win".equals(deptId)) { // bc wine page
	        %><%@ include file="/departments/wine/bc_home.jspf"%><%
	    } else if ("usq".equals(deptId)) { //usq wine page
	        %><%@ include file="/departments/wine/usq_home.jspf"%><%
	    } else if ("bak".equals(deptId)) { // bak needs top buffer 
	        String trkCode= "dpage"; 
	        %>
	<IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="12">
	<%@ include file="/includes/layouts/bakerydpt.jspf" %><br>
	<%@ include file="/includes/department_bottom.jspf"%>
<%
	    } else {
	        %><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10">
		    <%@ include file="/common/template/includes/catLayoutManager.jspf" %><br><%
	
			if (isIncludeDeptBottom) {
				// Display this piece of code if layout is not 'grocery_dept_layout_new'
				List middleMediaList = department instanceof DepartmentModel ? ((DepartmentModel)department).getDepartmentMiddleMedia() : null;
				if (middleMediaList != null && !middleMediaList.isEmpty() && !"our_picks".equals(deptId)  ) {
					for (Iterator middleMedia = middleMediaList.iterator(); middleMedia.hasNext();) {
						Html middleMediaPath = (Html)middleMedia.next();
						%><div style="width: 550px;" align="left"><fd:IncludeMedia name="<%=middleMediaPath.getPath()%>" /></div><%
					}
				}
			}
	
	
		    if ("big".equals(deptId) || "local".equals(deptId)) { 
				//featured item footer
				%><%@ include file="/includes/department_bottom_featured.jspf"%><%
			} else if("fdi".equals(deptId)) {
				%><%@ include file="/includes/department_fea_edit_bottom.jspf"%><%
			} else { 
				if (isIncludeDeptBottom) {
					%><%@ include file="/includes/department_bottom.jspf"%><%
				}
			}
	    }
    } // !isIncludeMediaLayout
} catch (Exception ex) {
	ex.printStackTrace();
    %><oscache:usecached /><%
}
%>
</oscache:cache>
	</tmpl:put>

</tmpl:insert>
