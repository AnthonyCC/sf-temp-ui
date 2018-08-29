<%@ page import="java.util.*"%>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*, com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<% //expanded page dimensions
final int W_CAREERS_TOTAL = 970;
%>

<fd:CheckLoginStatus />

<%
String catId = request.getParameter("catId");

ContentNodeModel currentFolder = PopulatorUtil.getContentNode(catId);

Html introCopyHtml = currentFolder.getEditorial();
String introCopy = introCopyHtml==null?"":introCopyHtml.getPath();

MediaModel catImage = ((ProductContainer) currentFolder).getCategoryPhoto();
String title = "FreshDirect - " + currentFolder.getFullName();
%>
<tmpl:insert template='/common/template/dnav.jsp'>

<%-- tmpl:put name='leftnav' direct='true'></tmpl:put--%>

    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="<%= title %>"/>
    </tmpl:put>
<%-- <tmpl:put name='title' direct='true'><%= title %></tmpl:put> --%>
<tmpl:put name='content' direct='true'>
<table width="<%=W_CAREERS_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
	<tr valign="top">
		<td width="<%=W_CAREERS_TOTAL-120%>"><% if (introCopy !=null && introCopy.trim().length()>0 && introCopy.indexOf("blank_file.txt") == -1) { %><fd:IncludeMedia name='<%= introCopy %>'/><% } %></td>
		<td width="120" rowspan="2" align="right"><% if (catImage != null) { %><img src="<%=catImage.getPath()%>" width="<%=catImage.getWidth()%>" height="<%=catImage.getHeight()%>" border="0"><% } %></td>
	</tr>
	<tr>
		<td class="text12"><center>
<fd:ItemGrabber category='<%=currentFolder %>' id='rtnColl' depth='99' ignoreShowChildren='false' filterDiscontinued='false' returnHiddenFolders='true' ignoreDuplicateProducts='true' returnSecondaryFolders='true' returnSkus='false'>
<% 
Collection itemsColl = rtnColl;  
%>
<logic:iterate id="job" collection="<%= itemsColl %>" type="com.freshdirect.storeapi.content.ContentNodeModel">
<% 
boolean mainHeader = (job instanceof CategoryModel) ? ((CategoryModel)job).getSideNavBold() : false;
if (mainHeader) {
%>	
	<b><%=job.getFullName()%></b><br><span class="space8pix"><br></span>
<% } else { %>
	<a href="/position.jsp?catId=<%=job.getContentName()%>"><%=job.getFullName()%></a><br><span class="space4pix"><br></span>
<% } %>
</logic:iterate>
</fd:ItemGrabber> 
		
		
		</center></td>
	</tr>
</table>
</tmpl:put>
</tmpl:insert>