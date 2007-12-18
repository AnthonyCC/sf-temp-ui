<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
String catId = request.getParameter("catId");
String prodId = request.getParameter("prodId");
String skucode  = request.getParameter("skucode");

String charName = request.getParameter("charName");
String charPath = "";
String charFile = "";
String pathToMedia = "";

String title = request.getParameter("title");

String tmpl = request.getParameter("tmpl");

boolean isMarinade = false;

charName = charName.toLowerCase();

	//investigate charName
	if (charName.indexOf("mar") != -1) {
	//is marinade
		isMarinade = true;
		
	} else {
	//to characteristics file
		charPath = "/media/editorial/fd_defs/characteristics/";
		
			if (charName.indexOf("wf_opt") != -1){
			charName = "c_sf_wf_opt";
			}
			
		charFile = charName + ".html";
		pathToMedia = charPath + charFile;
	}


String tmplFile = "/shared/template/" + tmpl + ".jsp";

String skuCode = "";
ContentNodeModel dept = null;
String deptName = "";
List variations = null;
String level = "";
ProductModel productNode = null;

	if (isMarinade) {
	
	ProductModel product =  ContentFactory.getInstance().getProductByName(catId,prodId);
	//accomodate claims include
	productNode = product;
	skuCode = product.getDefaultSku().getSkuCode();
	FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
	FDProduct defaultProduct = FDCachedFactory.getProduct( productInfo );
	
	dept = product.getDepartment();
	deptName = dept.getFullName();
	
	variations = Arrays.asList(defaultProduct.getVariations());
	
	title = (deptName.equals("Meat")? "Meat" : "Seafood") + " - About Our Marinades";
	
		if(request.getParameter("level")!=null){
			level = request.getParameter("level");
		}
		else
		{
			level = "home";
		}
	
	}

%>
<tmpl:insert template='<%=tmplFile%>'>
	<tmpl:put name='title' direct='true'>FreshDirect - <%=title%></tmpl:put>
		<tmpl:put name='content' direct='true'>
	<% if (isMarinade) { %>
		<%@ include file="/shared/includes/common_marinades.jspf"%>
	<% } else { %> 
		<fd:IncludeMedia name="<%=pathToMedia%>" />
	<% } %>

	</tmpl:put>
</tmpl:insert>
