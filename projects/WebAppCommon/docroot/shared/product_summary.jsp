<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.content.ProductModel'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='java.util.*'%>
<%@ page import='java.io.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>

<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%!
//**** Methods used on the product pages.  **** 

public DepartmentModel findDepartment (String deptId) throws FDResourceException {
		return (DepartmentModel)ContentFactory.getInstance().getContentNode(deptId);
}

public String findParentOfCategory (String catId) throws FDResourceException {
		CategoryModel categoryNode = (CategoryModel)ContentFactory.getInstance().getContentNode(catId);
		DepartmentModel dept = categoryNode.getDepartment();
		return dept.getContentName();
}
public String getProdPageRatings(ProductModel _productNode, HttpServletResponse _response)  throws FDResourceException {
  WebProductRating webProductRating = RatingUtil.getRatings(_productNode);
  StringBuffer rtnString = new StringBuffer(200);

    if (webProductRating!=null) {
        StringBuffer ratingLines  = new StringBuffer();
        String ratingLabel = "<br/><b>"+webProductRating.getRatingLabel()+"</b>";
        List ratings = webProductRating.getRatings();
        List textRatings = webProductRating.getTextRatings();

        // add rating heading and rating lines
        if (ratings.size() > 0 ) {
            if (webProductRating.getRatingLabel()!=null) {
                rtnString.append("<br/><b>");
                rtnString.append(webProductRating.getRatingLabel());
                rtnString.append("</b>");
            }

            for (Iterator itrRatings = ratings.iterator();itrRatings.hasNext();) {
               ProductRating prodRating = (ProductRating)itrRatings.next();
               rtnString.append("<br/><img width=\"63\" height=\"8\" src=\"/media_stat/images/template/rating3_05_0");
               rtnString.append(prodRating.getRating());
               rtnString.append(".gif\" alt=\"");
               rtnString.append(prodRating.getRating());
               rtnString.append(" (out of 5)\">&nbsp;<font class=\"text9\">");
               rtnString.append(prodRating.getRatingName().toUpperCase());
               rtnString.append("</font>");
            }
        }

        //add the text ratings
        if (textRatings.size() > 0 ) {
		rtnString.append("<br/>");
            for (Iterator itrRatings = textRatings.iterator();itrRatings.hasNext();) {
               ProductRating prodRating = (ProductRating)itrRatings.next();
               rtnString.append("<br/><b>");
               rtnString.append(prodRating.getRatingName());
               rtnString.append(":</b>&nbsp;");
               rtnString.append(prodRating.getRating());
            }
        }
    }
    return rtnString.toString();
}

%>

<% String deptId = "";
		if (request.getParameter("catId")!=null) {
		  deptId = findDepartment(findParentOfCategory (request.getParameter("catId"))).getContentName();
}%>

<fd:Department id='department' departmentId='<%= deptId %>'/>
<%
    ContentNodeModel currentFolder = department;
%>

<tmpl:insert template='/shared/template/summary.jsp'>
<fd:ProductGroup id='productNode' categoryId='<%= request.getParameter("catId") %>' productId='<%= request.getParameter("productId") %>'>
<%String prodPageRatingStuff = getProdPageRatings(productNode,response);%>
<tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName()  %> Guide: <%= productNode.getFullName() %></tmpl:put>
<tmpl:put name='pageTitle' direct='true'><%= currentFolder.getFullName().toUpperCase()  %> GUIDE</tmpl:put>

<tmpl:put name='content' direct='true'>

<%
	Image productImage = productNode.getDetailImage();
        Image zoomImage=(Image) productNode.getZoomImage();
%>

<table  width="80%" border="0" cellpadding="0" cellspacing="0" align="center">
<tr>
	<td align="center" class="title12"><br/><span class="title18"><b><%= productNode.getFullName().toUpperCase() %></b></span><br/><br/>
	<% if( zoomImage!=null && zoomImage.getPath().indexOf("clear.gif")==-1 ){%>
		<a href="javascript:window.close();"><img src="<%= zoomImage.getPath() %>" width="<%= zoomImage.getWidth() %>" height="<%= zoomImage.getHeight() %>" ALT="<%= productNode.getFullName() %>  (click to close window)" border="0"></a>
	<% } else if (productImage!=null && productImage.getPath().indexOf("clear.gif")==-1) { %>
		<a href="javascript:window.close();"><img border=0 src="<%= productImage.getPath() %>" width="<%= productImage.getWidth() %>" height="<%= productImage.getHeight() %>" ALT="<%= productNode.getFullName() %>  (click to close window)" border="0"></a>
	<%} else { %>
		<a href="javascript:window.close();"><img src="/media/images/temp/soon_260x260.gif" ALT="Photo Coming Soon  (click to close window)" border="0"></a>
	<%}%>
	<br/><br/>
	</td></tr>
<tr><td class="text15">
<%
			String seasonText = productNode.getSeasonText();
			if (seasonText!=null) {
			%><%=seasonText%><br/><br/><% } %>

	<% if (prodPageRatingStuff != null && !"".equals(prodPageRatingStuff)) { %><%= prodPageRatingStuff %><br/><br/><% } %>
	
		
	<% Html productDesc = productNode.getProductDescription(); 
	if ( productDesc != null && !"blank.txt".equals(productDesc) ) {
	%><b>About:</b><br/>
	<%@ include file="/shared/includes/product/i_product_about.jspf" %><br/>
	<% } %>
	<%  SkuModel defaultSku = !productNode.isUnavailable()?productNode.getDefaultSku():(SkuModel)productNode.getSkus().get(0);
        if (defaultSku!=null) {
        	FDProduct fdprd = productNode.isUnavailable()? null:defaultSku.getProduct(); %>
			<% if (fdprd != null && fdprd.getIngredients() != null && !"".equals(fdprd.getIngredients())) { %>
			<b>Ingredients:</b><br/><%= fdprd.getIngredients() %>
			<%@ include file="/shared/includes/product/allergens.jspf" %>
			<br/>
			<% } %>
			<% if ( fdprd != null && fdprd.getMaterial() != null && fdprd.getMaterial().getMaterialNumber() != null ) { %>
				<b>Material #:</b> <%=fdprd.getMaterial().getMaterialNumber()%><br/><br/>
			<% } %>
	<% } %>
	</td>
</tr>
</table>
</tmpl:put>
</fd:ProductGroup>
</tmpl:insert>
