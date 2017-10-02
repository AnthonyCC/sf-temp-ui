<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.webapp.util.ProductRecommenderUtil"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.smartstore.fdstore.Recommendations"%>
<%@page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopYmalServlet"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<html lang="en-US" xml:lang="en-US">
<head>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
</head>
<body>
<fd:css href="/assets/css/global.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/test/template_redesign/layout.css"/>
<link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav_and_footer.css"/>

<h3>Add id parameter for dept/cat recommenders. Or leave it blank to see all departments.</h3>

<%
String id = request.getParameter("id");
if (id==null || id.equals("")){
	String[] depts = new String[]{"fru","veg","mea","sea","del","che","dai","gro","fro","fdi","rtc","hmr","bak","cat","flo","pet","pas","cof","hba","big"};
	java.lang.Integer productImageWidth = null;
    java.lang.Integer productImageHeight = null;
	for (String dept : depts){
		DepartmentModel deptModel = (DepartmentModel) ContentFactory.getInstance().getContentNode(FDContentTypes.DEPARTMENT, dept);
		%><%@ include file="i_dept_recommender.jspf" %><%
	}	
} else {
	
	ContentNodeModel node = ContentFactory.getInstance().getContentNode(id);
	if (node instanceof DepartmentModel){
		java.lang.Integer productImageWidth = null;
	    java.lang.Integer productImageHeight = null;
		DepartmentModel deptModel= (DepartmentModel) node;
		%><%@ include file="i_dept_recommender.jspf" %><%
	
	} else if (node instanceof CategoryModel){
		java.lang.Integer productImageWidth = null;
	    java.lang.Integer productImageHeight = null;
		CategoryModel catModel= (CategoryModel) node;
		%><h2><%=catModel.getFullName()%></h2>
		<div style="border: 1px orange solid; margin:15px">
		<h3>Merchant Recommender: <%=catModel.getCatMerchantRecommenderTitle()%></h3>
		<%
		for (ProductModel p : ProductRecommenderUtil.getMerchantRecommenderProducts(catModel)) {%>
			
			<div style="display: inline-block;width: 120px"">
				<display:ProductImage product='<%=p%>' />
				<display:ProductName product='<%=p%>' />
			</div>
		<%}%>
		</div><%
	}
}
%>
</body>
</html>
