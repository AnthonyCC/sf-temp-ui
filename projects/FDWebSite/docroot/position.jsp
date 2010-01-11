
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />

<%
String catId = request.getParameter("catId");

ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(catId);

Html introCopyAttribute = currentFolder.getEditorial();
String introCopy = introCopyAttribute==null ? "": introCopyAttribute.getPath();

MediaModel catImage = ((ProductContainer) currentFolder).getCategoryPhoto();

%>
<tmpl:insert template='/common/template/left_dnav.jsp'>

<%-- tmpl:put name='leftnav' direct='true'></tmpl:put--%>

<tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName() %></tmpl:put>
<tmpl:put name='content' direct='true'>
	<table cellpadding="0" cellspacing="0" border="0" width="580">
	<tr>
	<td class="title14"><span class="text15"><b><span style="color:#FF9933;">POSITION</span> <%=currentFolder.getFullName().toUpperCase()%></b></span>
		<br><br>
		<b>Job Responsibilities</b><br><br></td>
	</tr>
	<tr><td>
	<% if (introCopy !=null && introCopy.trim().length()>0 && introCopy.indexOf("blank_file.txt") == -1) { %><fd:IncludeMedia name='<%= introCopy %>'/><% } %>
	</td></tr>
	<tr>
		<td class="text12"><br>
		<span style="color:#FF9933;"><b>Important:</b></span> The subject field of your email must include <b><%=currentFolder.getFullName()%></b>.
		<br><br>
		FreshDirect is an equal opportunity employer.
		<br><br><br>
		<a href="<%=FDStoreProperties.getCareerLink()%>"><b>Back to job listings</b></a>
		</td>
	</tr>
	</table>
	<br>
	
</tmpl:put>
</tmpl:insert>