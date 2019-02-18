<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<% //expanded page dimensions
final int W_COFFEE_DEPT_LAYOUT_TOTAL = 765;
final int W_COFFEE_DEPT_LAYOUT_IMG = 68;
final int W_COFFEE_DEPT_LAYOUT_IMG_PADDING = 14;
final int W_COFFEE_DEPT_LAYOUT_TEXT = 273;
final int W_COFFEE_DEPT_LAYOUT_PADDING = 55;
%>

<%

//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
if(deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNode(deptId);
	isDepartment = true;
} else {
	currentFolder=ContentFactory.getInstance().getContentNode(catId);
}


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();



Image coffImage = null;
String folderImage  = null;
String folderImageDim = null;
String folderUrl = null;
String labelImage = null;
String labelImageDim = null;
int itmCounter = -1;
%>
<TABLE CELLPADDING="2" CELLSPACING="0" BORDER="0" WIDTH="<%=W_COFFEE_DEPT_LAYOUT_TOTAL%>">
<%
for(Iterator itmItr = sortedColl.iterator();itmItr.hasNext();) {
    String catDescription = "";
    ContentNodeModel itrItem = (ContentNodeModel)itmItr.next();
    if (!(itrItem instanceof CategoryModel) ) continue; // only renders Categories now
    CategoryModel category = (CategoryModel)itrItem;
    coffImage = category.getCategoryPhotoNotNull();

    folderImage = coffImage.getPath();
    folderImageDim = JspMethods.getImageDimensions(coffImage);

    coffImage = category.getCategoryLabelNotNull();

    labelImage = coffImage.getPath();
    labelImageDim=JspMethods.getImageDimensions(coffImage);
    folderUrl = response.encodeURL("/category.jsp?catId="+category+"&trk=dpage");
    catDescription = category.getBlurb();

    itmCounter++;
    if (itmCounter %2==0) {
        if (itmCounter !=0 ) { %>
</tr>
<%     } %>
<TR VALIGN="TOP">
<%  } %>
<TD align="center" WIDTH="<%=W_COFFEE_DEPT_LAYOUT_IMG%>"><A HREF="<%=folderUrl%>"><img src="<%=folderImage%>" <%=folderImageDim%> border="0" alt="<%=category.getFullName()%>"></A></TD>
<td width="<%=W_COFFEE_DEPT_LAYOUT_IMG_PADDING%>"></td>
<TD WIDTH="<%=W_COFFEE_DEPT_LAYOUT_TEXT%>"><A HREF="<%=folderUrl%>"><img src="<%=labelImage%>" <%=labelImageDim%> border="0" alt="<%=category.getFullName()%>"></A><BR>
<% 
if (catDescription !=null && catDescription.trim().length() >=1) { %> 
<%=catDescription%>
<%}%><BR>
<FONT CLASS="space4pix"><BR></FONT></TD>
<%
    if (itmCounter %2==0) { %>
<TD WIDTH="<%=W_COFFEE_DEPT_LAYOUT_PADDING%>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="35" HEIGHT="1" BORDER="0"></TD>
<%  }%>
<%}
// if we did not print an even amount then put the remain cell in.
if (itmCounter<=0 && itmCounter%2==0 ) {
%>
<TD WIDTH="<%=W_COFFEE_DEPT_LAYOUT_IMG%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="50" height ="1" border="0"></TD>
<td width="<%=W_COFFEE_DEPT_LAYOUT_IMG_PADDING%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_COFFEE_DEPT_LAYOUT_IMG_PADDING%>" height ="1" border="0"></td>
<TD WIDTH="<%=W_COFFEE_DEPT_LAYOUT_TEXT%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_COFFEE_DEPT_LAYOUT_TEXT%>" height ="1" border="0"><BR>
<BR><FONT CLASS="space4pix"><BR></FONT></TD>
<%} %>
</TR></TABLE>