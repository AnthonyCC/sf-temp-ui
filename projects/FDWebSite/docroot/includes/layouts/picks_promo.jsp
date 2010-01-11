<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus guestAllowed="true" />
<%
String deptId = request.getParameter("deptId");
ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(deptId);

/* Layout contents */
Html topMedia = currentFolder.getEditorial();
String picksContent = topMedia == null ? "" : topMedia.getPath();

List<Html> middleMedia = ((DepartmentModel) currentFolder).getDepartmentMiddleMedia(); 
    
// get only the first content out of the attribute
String promoContent = middleMedia == null || middleMedia.size()==0 ? "" : ((MediaI)(middleMedia.get(0))).getPath();

%>
<table border="0" cellpadding="0" cellspacing="0" width="540">
	<tr valign="top">
		<td align="center"><fd:IncludeMedia name='<%= picksContent %>'/>
		</td>
		<td>&nbsp;</td>
		<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		<td>&nbsp;</td>
		<td><% if (FDStoreProperties.isAdServerEnabled()) { %>
                    <SCRIPT LANGUAGE=JavaScript>
                    <!--
                    OAS_AD('CategoryNote');
                    //-->
                    </SCRIPT>                    
                <% } %><br><br><fd:IncludeMedia name='<%= promoContent %>' />
				</td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="300" height="18"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="14" height="18"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="1" height="18"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="15" height="18"></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="210" height="18"></td>
	</tr>
</table>
