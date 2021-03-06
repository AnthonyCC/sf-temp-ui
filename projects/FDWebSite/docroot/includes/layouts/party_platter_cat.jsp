<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.util.*'%>
<%@ page import='java.net.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<% //expanded page dimensions
final int W_PARTY_PLATTER_TOTAL = 765;
%>


<fd:CheckLoginStatus guestAllowed="true" />
<%
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
String catId = request.getParameter("catId");
boolean isGroceryVirtual=false;
CategoryModel currentFolder = (CategoryModel) ContentFactory.getInstance().getContentNode(catId);
boolean allSoldOut=true;
boolean previewMode = ContentFactory.getInstance().getPreviewMode();
MediaI previewMedia = currentFolder.getPreviewMedia();
String previewMediaPath=null;
if (previewMedia!=null) {
	previewMediaPath = ((MediaI)previewMedia).getPath();
}

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
for(Iterator itr=sortedColl.iterator(); itr.hasNext() && allSoldOut;) {
	ContentNodeModel cn = (ContentNodeModel) itr.next();
	if (!cn.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) continue;
	if ( !((ProductModel)cn).isUnavailable()) {
	   allSoldOut=false;
	}
}   
	Html editorial = currentFolder.getEditorial();
	String introCopy = editorial==null ? "" : editorial.getPath();
	String introTitle = currentFolder.getEditorialTitle();

        if (allSoldOut && previewMediaPath!=null && !"".equals(previewMediaPath)) { %>
		<TABLE border="0" cellPadding="0" cellSpacing="0" width="<%=W_PARTY_PLATTER_TOTAL%>">
			   <tr>
			     <td align="center"><fd:IncludeMedia name='<%= previewMediaPath %>'/></td>
		          </tr>
		</table>
<% } else {  %>
<TABLE border="0" cellPadding="0" cellSpacing="0" width="<%=W_PARTY_PLATTER_TOTAL%>">
	 <tr><td>
	 <SCRIPT LANGUAGE=JavaScript>
		<!--
		OAS_AD('CategoryNote');
		//-->
	</SCRIPT><br>
	 </td></tr>
<% if ( !"nm".equalsIgnoreCase(introTitle) && introTitle!=null && introTitle.trim().length() > 0) { 	%>
	   <tr><td align="center">
	     <font class="title16"><%=introTitle%></font>
       </td></tr>
<% }  %>
      
<% if (introCopy !=null && introCopy.trim().length()>0 && introCopy.indexOf("blank_file.txt") == -1 ) {   %>
	<tr><td>
	  <img src="/media_stat/images/layout/clear.gif" alt="" height="5" width="1">
	  <br><fd:IncludeMedia name='<%= introCopy %>'/>
	  <simg src="/media_stat/images/layout/clear.gif" alt="" height="2" width="1">
	</td></tr>
<%  }  %>
</table>
<TABLE border="0" cellPadding="0" cellSpacing="0" width="<%=W_PARTY_PLATTER_TOTAL%>">
	<tr>
    <td align="center" valign="top">
<%
  if (!allSoldOut ) {  %>
  <TABLE border="0" cellPadding="0" cellSpacing="0" width="<%=W_PARTY_PLATTER_TOTAL%>">
<%          List middleMediaList = currentFolder.getMiddleMedia();
		if (middleMediaList != null && middleMediaList.size() > 0) {  %>
              <tr><td colspan="3">		
<%			for (Iterator middleMedia = middleMediaList.iterator(); middleMedia.hasNext();) {
				Html middleMediaPath = (Html)middleMedia.next();
	%>			<div style="width: <%=W_PARTY_PLATTER_TOTAL%>px;" align="left"><fd:IncludeMedia name="<%=middleMediaPath.getPath()%>" /></div>
<%			}
		}  %>
 			
<%     
        for(Iterator itr=sortedColl.iterator(); itr.hasNext();) {
            ContentNodeModel cn = (ContentNodeModel) itr.next();
            if (!cn.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) continue;

            ProductModel pm = (ProductModel)cn;
            DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,pm.getParentNode().getContentName(),pm,"full",true);
            boolean prodUnavailable = pm.isUnavailable();
            SkuModel defaultSku = !prodUnavailable?pm.getDefaultSku():(SkuModel)pm.getSkus().get(0);
%>
          <fd:FDProductInfo id="productInfo" skuCode="<%= defaultSku.getSkuCode() %>"> 
            <tr><td><font class="space4pix"><br><br></font>
<%            if (pm.isUnavailable()) {     %>
                 <td style="text-align: left; padding-left: 50px;"><font color="#cccccc"><b><%=pm.getFullName()%></b></a></td><td style="padding-right: 50px;">&nbsp;-&nbsp;<font color="#ff0000"><b>SOLD OUT</b></font></td>
<%            } else {   %>
                <td style="text-align: left; padding-left: 50px;"><a href="/product.jsp?catId=<%=pm.getParentNode()%>&productId=<%=pm%>&trk=cpage"><b><%=pm.getFullName()%></b></a></td><td style="padding-right: 50px;">&nbsp;-&nbsp;<b><%=JspMethods.formatDefaultPrice(productInfo, user.getPricingContext())%></td></b></td>
<%            }  %> </tr>
          </fd:FDProductInfo>
<%      }  %></table>
        <BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="10"><BR>
<% } %>
    </td>
   </tr>
</TABLE>

<% } %>
