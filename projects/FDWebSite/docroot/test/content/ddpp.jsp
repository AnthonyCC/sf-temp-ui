<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>DDPP Test Page</title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body class="ddpp">
<display:InitLayout/>
getProductPromotionType: <%=((com.freshdirect.storeapi.content.CategoryModel)currentFolder).getProductPromotionType()%><br />
getProducts: <%=((com.freshdirect.storeapi.content.CategoryModel)currentFolder).getProducts()%><br />
getFDProductPromotion: <%--=((com.freshdirect.storeapi.content.CategoryModel)currentFolder).getFDProductPromotion()--%><br />
<%
	ProductModel pm = null;
	//pm = ((com.freshdirect.storeapi.content.CategoryModel)currentFolder).getPromotionPageProductForSku("MEA0064908");
%>
getPromotionPageProductForSku("MEA0064908"): <% out.println((pm !=null) ? pm.toString() : "pm is null"); %><br />
<%
	//pm = ((com.freshdirect.storeapi.content.CategoryModel)currentFolder).getPromotionPageProductForSku("FRO0060591");
%>
getPromotionPageProductForSku("FRO0060591"): <% out.println((pm !=null) ? pm.toString() : "pm is null"); %><br />
<%
	//pm = ((com.freshdirect.storeapi.content.CategoryModel)currentFolder).getPromotionPageProductForSku("DAI0068808");
%>
getPromotionPageProductForSku("DAI0068808"): <% out.println((pm !=null) ? pm.toString() : "pm is null"); %><br />
<%

	/*
		if (currentFolder != null && currentFolder instanceof CategoryModel) {
			if ( ((com.freshdirect.storeapi.content.CategoryModel)currentFolder).getFDProductPromotion() != null) {
				out.println("FDProductPromotion for catId: " + categoryId);
				out.println(((com.freshdirect.storeapi.content.CategoryModel)currentFolder).getProducts());
			} else {
				out.println("No FDProductPromotion for catId: " + categoryId);
			}
		} else {
			out.println("No catId query param found, or invalid catId.");
		}
	*/
	
%>
</body>
</html>