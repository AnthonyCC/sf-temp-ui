<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<% //expanded page dimensions
final int W_PICKS_PROMO_TOTAL = 765;
final int W_PICKS_PROMO_LEFT = 330;
final int W_PICKS_PROMO_CENTER_PADDING = 29;
final int W_PICKS_PROMO_RIGHT = 406;
%>


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
<table border="0" cellpadding="0" cellspacing="0" width="<%=W_PICKS_PROMO_TOTAL%>">
	<tr valign="top">
		<td align="center"><fd:IncludeMedia name='<%= picksContent %>'/>
		</td>
		<td>&nbsp;</td>
		<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
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
		<td><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_PICKS_PROMO_LEFT%>" height="18"></td>
		<td><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=(W_PICKS_PROMO_CENTER_PADDING-1)/2%>" height="18"></td>
		<td><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="18"></td>
		<td><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=(W_PICKS_PROMO_CENTER_PADDING-1)/2%>" height="18"></td>
		<td><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_PICKS_PROMO_RIGHT%>" height="18"></td>
	</tr>
</table>
