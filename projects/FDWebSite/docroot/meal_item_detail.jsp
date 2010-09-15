<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='java.net.URLEncoder' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
FDCartLineI templateLine = null ;
//m for meal ids
String mcatId = request.getParameter("mcatId");
String mproductId = request.getParameter("mproductId");
String mskuCode  = request.getParameter("mskuCode");

//for shown product
String catId = request.getParameter("catId");
String productId = request.getParameter("productId");
String skuCode  = request.getParameter("skuCode");


int maxWidth=320;
ContentFactory cf = ContentFactory.getInstance();
List skus = new ArrayList();
ProductModel product =  ContentFactory.getInstance().getProductByName(mcatId,mproductId);

if (!product.getComponentGroups().isEmpty()) {
    String redirectURL = "/cg_meal_item_detail.jsp?" + request.getQueryString();
    response.sendRedirect(response.encodeRedirectURL(redirectURL));
    return;
};

//accomodate claims include
ProductModel productNode = product;
CategoryModel parentCat = (CategoryModel) productNode.getParentNode();
String prodNameAttribute = JspMethods.getProductNameToUse(parentCat);
ContentFactory contentFactory= ContentFactory.getInstance();
SkuModel defaultSku = product.getDefaultSku();
Image productImage = product.getDetailImage();

Map availOptSkuMap = new HashMap();
    
List prodSkus = productNode.getSkus();

boolean hasSingleSku = (prodSkus.size() == 1);

if (hasSingleSku) {
	defaultSku = (SkuModel)prodSkus.get(0);
} else {
	if (mskuCode!=null) {
		// locate the proper sku based on request
		defaultSku = productNode.getSku(mskuCode);
	}
	if (defaultSku==null) {
		// no sku from request: default is the one with lowest price
		defaultSku = productNode.getDefaultSku();
	}

}

FDProductInfo  defaultProductInfo = FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
FDProduct defaultProduct = FDCachedFactory.getProduct( defaultProductInfo);
FDVariation[] variations = defaultProduct.getVariations();

//* check to see if any of the mandatory variations are all unavailable.  (rule: platter unavailable if all comp of a var is unavail)
//* while we're at it, save skucode and productModel for future use.
boolean aVariationUnavailable = false;
for (int cvIdx = 0; cvIdx < variations.length && !aVariationUnavailable; cvIdx++) {
	FDVariationOption[] varOpts = variations[cvIdx].getVariationOptions(); 
	if (variations[cvIdx].isOptional()) continue;

	int unAvailCount=0;
	for (int voIdx = 0; voIdx < varOpts.length;voIdx++) {
		String optSkuCode=varOpts[voIdx].getSkuCode();
		if (optSkuCode==null) {
			unAvailCount++;
			continue;
		}
		try {
			ProductModel pm =cf.getProduct(optSkuCode);
			if (pm!=null ) {
			   if ( !pm.isUnavailable()   && availOptSkuMap.get(optSkuCode)==null) {
				availOptSkuMap.put(optSkuCode,pm);
			   } else if ( pm.isUnavailable() ) {
					unAvailCount++;
			   }
			} else {
				unAvailCount++;
			}
		} catch (Exception e) {
				JspLogger.PRODUCT.warn("party_platter_cat.jsp: catching FDSkuNotFoundException for SkuCode: "+optSkuCode+" and Continuing:\nException message:= "+e.getMessage());
				unAvailCount++;
		}

	}
	if (unAvailCount==varOpts.length && unAvailCount!=0) aVariationUnavailable=true;
}
%>

<tmpl:insert template='/common/template/large_long_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - <%=productNode.getFullName()%> Details</tmpl:put>
		<tmpl:put name='content' direct='true'>
<table border="0" cellpadding="0" cellspacing="0" width="520">
<tr valign="top"><td colspan="5" width="520"></td></tr>

<tr valign="top">
<td width="115" align="right" rowspan="2">
<%-- START NAVIGATION --%>
<a href="?mcatId=<%=mcatId%>&mproductId=<%=mproductId%>&mskuCode=<%=mskuCode%>&catId=<%=mcatId%>&productId=<%=mproductId%>&skuCode=<%=mskuCode%>"><img src="/media_stat/images/headers/about_the_meal.gif" border="0" width="82" height="32"></a>
<br><br><a href="?mcatId=<%=mcatId%>&mproductId=<%=mproductId%>&mskuCode=<%=mskuCode%>&catId=<%=mcatId%>&productId=<%=mproductId%>&skuCode=<%=mskuCode%>" class="text12"><b><%=productNode.getFullName()%></b></a><br><span class="space8pix"><br></span>

<%
int prodCount = 0;%>
<%
	int numProducts=0;
	int catIdx = 0;
	for (Iterator sci = parentCat.getSubcategories().iterator(); sci.hasNext(); catIdx++) {
		CategoryModel stepCat = (CategoryModel)sci.next();
		List matCharNames=new ArrayList();
		List prodList = new ArrayList();
		String _matChar = stepCat.getMaterialCharacteristic();
		if (_matChar!=null) {
			StringTokenizer matCharTkns = new StringTokenizer(_matChar,",");
			for (;matCharTkns.hasMoreTokens(); ) {
				matCharNames.add(matCharTkns.nextToken());
			}
			for (int vIdx = 0; vIdx <variations.length; vIdx++) {
				if (matCharNames.contains(variations[vIdx].getName())) {
					FDVariationOption[] varOpts = variations[vIdx].getVariationOptions();
					for (int voIdx = 0; voIdx < varOpts.length;voIdx++) {
						String optSkuCode=varOpts[voIdx].getSkuCode();
						if (optSkuCode==null || availOptSkuMap.get(optSkuCode)==null) continue;
							ProductModel pm =(ProductModel)availOptSkuMap.get(optSkuCode);
							if (pm!=null && !prodList.contains(pm)) {
								prodList.add(pm);
						}
					}

				}
			}
		} 
		//show category image and into copy if there are characteristics names
		if (matCharNames.size()>0) { 	%>
		   <span class="space8pix"><br></span><b><%=stepCat.getFullName()%></b><br>
	<%  }  %>
	<%	
		numProducts =prodList.size(); 
		boolean tagIsOpen = false;
		if (matCharNames.size() > 0) {
			for (int prodIdx = 0; prodIdx < numProducts ; prodIdx++) {
				ProductModel oneProd =(ProductModel)prodList.get(prodIdx);
				Image prodImage =oneProd.getCategoryImage();
				String imgName = "prodImg_"+catIdx;
				boolean currentSelection = oneProd.getDefaultSku().toString().equals(skuCode) && !mskuCode.equals(skuCode);
		%>
		       <%= currentSelection ? "<i><b>":""%><a href="?mcatId=<%=mcatId%>&mproductId=<%=mproductId%>&mskuCode=<%=mskuCode%>&catId=<%=oneProd.getParentNode()%>&productId=<%=oneProd%>&skuCode=<%=oneProd.getDefaultSku()%>"><%=JspMethods.getDisplayName(oneProd,prodNameAttribute)%></a><%= currentSelection ? "</b></i>":""%>
				<br>
		<%	 }  %>
			  <br>
		<%}
         }%>

	<br><br><br>
<%-- END NAVIGATION --%>
</td>
<td width="10" rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
<td width="1" bgcolor="#999966" rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
<td width="15" rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="15" height="1"></td>
<td valign="top" width="379">

<table width="379" border="0" cellpadding="0" cellspacing="2">
	<tr>
		<td>
	<% try {
		ProductModel compProduct = null;
		Image prodImg = null;
		String prodDescription = null;

		compProduct =  ContentFactory.getInstance().getProduct(skuCode);
		prodImg = compProduct.getDetailImage();
		prodDescription = ((Html)compProduct.getProductDescription()).getPath();	
        SkuModel selectSku = (SkuModel)compProduct.getSku(skuCode);
        FDProduct fdprd = null;
        try {
            fdprd = selectSku.getProduct();
        } catch (FDSkuNotFoundException fdsnfe) { throw fdsnfe; }
%>
		<img src="/media_stat/images/layout/clear.gif" width="379" height="3"><br>
		<img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" height="<%=prodImg.getHeight()%>" alt="" border="0">
		
		<br><span class="space2pix"><br></span>
		<font class="title18"><%=compProduct.getFullName()%></font><br>
		
		<%if(prodDescription!=null){%>
			<span class="space2pix"><br></span><font class="text11"><fd:IncludeMedia name='<%=prodDescription%>'/><br></font>
		<%}%><br>
		
	    <%@ include file="/includes/product/i_heating_instructions.jspf"%>
		<%
if (fdprd != null && fdprd.hasNutritionInfo(ErpNutritionInfoType.HEATING)) {
	%><br><% } %>

	<% 
		if (fdprd!=null && fdprd.hasIngredients()) {    %>
	        <%@ include file="/includes/product/i_product_ingredients.jspf"%>
	<%  }      
	    if (fdprd != null && fdprd.hasNutritionFacts()) { %>
	        <%@ include file="/includes/product/i_product_nutrition.jspf"%>
	        <br><br>
	<%  }
	 
	}
	catch (FDSkuNotFoundException ex){
		throw ex;
	}	%>
		
		
		</td>
	</tr>
	
</table>
</td>
</tr>				
</table>
	</tmpl:put>
</tmpl:insert>