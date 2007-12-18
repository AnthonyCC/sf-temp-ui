<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
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

//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;
String trkCode = (String)request.getAttribute("trkCode");

ContentNodeModel currentFolder = null;
if(deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
	isDepartment = true;
	trkCode = "dpage";
} else {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
	trkCode = "cpage";
}

boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();

//**********************  End of stuff to make JSPF's become JSP's

String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);

/* Layout contents */
List availableList = new ArrayList();
for(Iterator availItr = sortedColl.iterator();availItr.hasNext();) {
    Object availObject = availItr.next();
        //throw away hidden folders
        //if (availObject instanceof CategoryModel && ((CategoryModel)availObject).getShowSelf()==false) continue;
        availableList.add(availObject);
} 

DisplayObject displayTopTen = null;
boolean isProduct = false;
boolean isCategory = false;
int itemNum = 1;
Image img = null;
%>
<table border="0" cellpadding="0" cellspacing="0" align="center">
<logic:iterate id="topTenItem" collection="<%= availableList %>" type="com.freshdirect.fdstore.content.ContentNodeModel">
<%
if (itemNum>10) break;
	if ((topTenItem instanceof ProductModel) || ( (topTenItem instanceof CategoryModel) && ((CategoryModel)topTenItem).getShowSelf()==true)) {
		if(topTenItem instanceof ProductModel) {
			isProduct = true;
			if (theOnlyProduct!=null) {
				onlyOneProduct=false;
			}else {
				onlyOneProduct=true;
				theOnlyProduct = (ProductModel)topTenItem;
			}
			displayTopTen = JspMethods.loadLayoutDisplayStrings(response,topTenItem.getParentNode().getContentName(),topTenItem,prodNameAttribute,true,false,trkCode);
		}
		else if(topTenItem instanceof CategoryModel) {
			isCategory = true;
			onlyOneProduct=false;
			displayTopTen = JspMethods.loadLayoutDisplayStrings(response,"",topTenItem,prodNameAttribute,true,false,trkCode);
		}
	}
%>
<tr valign="top">
<td align="right" style="padding-right:10px;">
<% if ( isProduct && !isCategory /*&& ((AbstractProductModelImpl)topTenItem).isShowTopTenImage() */) { %>
	<img src="<%=displayTopTen.getImagePath()%>" width="<%=displayTopTen.getImageWidth()%>" height="<%=displayTopTen.getImageHeight()%>" alt="<%=displayTopTen.getAltText()%>"><br><br>
<% } %>
</td>
<td><img src="/media_stat/images/template/topten/num_<%=itemNum%>.gif" width="25" height="26" border="0"></td>
<td class="text12"  style="padding-left:10px;">
	<% if ( isProduct && !isCategory && ((ProductModel)topTenItem).isUnavailable()) { %>
		<span style="color:#999999;"><strong><%=displayTopTen.getItemName()%></strong><br>
		Currently Unavailable</span>
	<% } else { %>
		<a href="<%=displayTopTen.getItemURL()%>?trk=<%=trkCode%>"><strong><%=displayTopTen.getItemName()%></strong></a><br>
		<% if (displayTopTen.getPrice()!=null) {%> 
        <font class="price"><strong><%=displayTopTen.getPrice()%></strong></font>
		<% } %>
	<% } %>
<br><br>
</td>
</tr>
<% itemNum++; %>
</logic:iterate>
</table>
<%
//**** bottom Of Jsp's  *******************
if (onlyOneProduct) {
    request.setAttribute("theOnlyProduct",theOnlyProduct);
}
%>