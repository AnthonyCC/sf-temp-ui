<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<%
Attribute attrib = null;
Set brands = null ; // set in the grocery_category_layout page. will be referenced by  i_bottom_template

String catId = request.getParameter("catId");
boolean isGroceryVirtual=false;
boolean isWineLayout = false;
String deptId = null;
ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNodeByName(catId);
System.out.println("currentFolder *************** "+currentFolder);
/*if(currentFolder instanceof CategoryModel) {
     deptId=((CategoryModel)currentFolder).getDepartment().getContentName();
}*/



ProductModel prodModel = ContentFactory.getInstance().getProductByName(request.getParameter("prodCatId"), request.getParameter("productId")); 

//--------OAS Page Variables-----------------------
//request.setAttribute("sitePage", prodModel == null ? currentFolder.getPath() : prodModel.getPath());
//request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom");
%>

<tmpl:insert template='/common/template/right_dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Peak Produce</tmpl:put>
<tmpl:put name='content' direct='true'>
<%@ include file="/includes/layouts/i_peak_produce_all.jspf" %>
</tmpl:put>

</tmpl:insert>
