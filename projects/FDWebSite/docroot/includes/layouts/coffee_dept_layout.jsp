<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
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
<%

//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
if(deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
	isDepartment = true;
} else {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
}


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
String trkCode = (String)request.getAttribute("trkCode");

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();



Attribute coffAttrib = null;
Image coffImage = null;
String folderImage  = null;
String folderImageDim = null;
String folderUrl = null;
String labelImage = null;
String labelImageDim = null;
int itmCounter = -1;
%>
<TABLE CELLPADDING="2" CELLSPACING="0" BORDER="0" WIDTH="535">
<%
for(Iterator itmItr = sortedColl.iterator();itmItr.hasNext();) {
    String catDescription = "";
    ContentNodeModel itrItem = (ContentNodeModel)itmItr.next();
    if (!(itrItem instanceof CategoryModel) ) continue; // only renders Categories now
    CategoryModel category = (CategoryModel)itrItem;
    coffAttrib = category.getAttribute("CAT_PHOTO");
    if (coffAttrib!=null) {
        coffImage = (Image)coffAttrib.getValue();
    }else {
        coffImage = new Image();
    }
    folderImage = coffImage.getPath();
    folderImageDim = JspMethods.getImageDimensions(coffImage);

    coffAttrib = category.getAttribute("CAT_LABEL");
    if (coffAttrib!=null) {
        coffImage = (Image)coffAttrib.getValue();
    }else {
        coffImage = new Image();
    }

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
<TD WIDTH="50"><A HREF="<%=folderUrl%>"><img src="<%=folderImage%>" <%=folderImageDim%> border="0" alt="<%=category.getFullName()%>"></A></TD>
<TD WIDTH="200"><A HREF="<%=folderUrl%>"><img src="<%=labelImage%>" <%=labelImageDim%> border="0" alt="<%=category.getFullName()%>"></A><BR>
<% 
if (catDescription !=null && catDescription.trim().length() >=1) { %> 
<%=catDescription%>
<%}%><BR>
<FONT CLASS="space4pix"><BR></FONT></TD>
<%
    if (itmCounter %2==0) { %>
<TD WIDTH="35"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="35" HEIGHT="1" BORDER="0"></TD>
<%  }%>
<%}
// if we did not print an even amount then put the remain cell in.
if (itmCounter<=0 && itmCounter%2==0 ) {
%>
<TD WIDTH="50"><img src="/media_stat/images/layout/clear.gif" width="50" height ="1" border="0"></TD>
<TD WIDTH="200"><img src="/media_stat/images/layout/clear.gif" width="200" height ="1" border="0"><BR>
<BR><FONT CLASS="space4pix"><BR></FONT></TD>
<%} %>
</TR></TABLE>