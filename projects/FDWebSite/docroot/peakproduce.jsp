<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<%
Set brands = null ; // set in the grocery_category_layout page. will be referenced by  i_bottom_template

String catId = request.getParameter("catId");
boolean isGroceryVirtual=false;
boolean isWineLayout = false;
String deptId = null;
ContentNodeModel currentFolder = PopulatorUtil.getContentNode(catId);
/*if(currentFolder instanceof CategoryModel) {
     deptId=((CategoryModel)currentFolder).getDepartment().getContentName();
}*/



ProductModel prodModel = ContentFactory.getInstance().getProductByName(request.getParameter("prodCatId"), request.getParameter("productId")); 

//--------OAS Page Variables-----------------------
//request.setAttribute("sitePage", prodModel == null ? currentFolder.getPath() : prodModel.getPath());
//request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom");
%>

<tmpl:insert template='/common/template/right_dnav.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Peak Produce"/>
    </tmpl:put>
    <tmpl:put name='title' direct='true'>FreshDirect - Peak Produce</tmpl:put>
<tmpl:put name='content' direct='true'>
<div align="center">
	<%@ include file="/includes/layouts/i_peak_produce_all.jspf" %>
</div>
</tmpl:put>

</tmpl:insert>
