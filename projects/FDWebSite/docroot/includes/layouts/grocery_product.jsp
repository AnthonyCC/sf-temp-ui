<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='org.apache.commons.lang.StringUtils' %>
<%@ page import='com.freshdirect.ErpServicesProperties' %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods'%>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.content.nutrition.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%
//********** Start of Stuff to let JSPF's become JSP's **************
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

String catId = request.getParameter("catId");
String deptId = request.getParameter("deptId");
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
CategoryModel currentCategory = null;
if (deptId != null) {
    currentFolder=ContentFactory.getInstance().getContentNode(deptId);
    isDepartment = true;
} else if (catId != null) {
    currentFolder=ContentFactory.getInstance().getContentNode(catId);
    if (currentFolder != null)
        currentCategory = (CategoryModel) currentFolder;
}

//DO render Editorial (if exists)
//[APPREQ-92] skip Editorial on Brand and on Virtual All pages
boolean doRenderEditorialPartial = (request.getParameter("brandValue") == null && !"All".equals(request.getParameter("groceryVirtual")));


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;

String sortBy = request.getParameter("sortBy");
String nutriName = request.getParameter("nutritionName");
String display = request.getParameter("disp");
boolean descending = "true".equals(request.getParameter("sortDescending"))?true:false;
if (sortBy==null) sortBy = "name";

List sortedColl = (List) request.getAttribute("itemGrabberResult");
%>

<%!
java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");

/**
 * Remove unavailable SKUs from list
 * @return the same list, for convenience
 */
public List cleanSkus(List prodSkus) {
        for (ListIterator skuItr = prodSkus.listIterator(); skuItr.hasNext(); ) {
                SkuModel s = (SkuModel)skuItr.next();
                if ( s.isUnavailable() ) {
                        skuItr.remove();
                }
        }
        return prodSkus;
}

public String buildOtherParams(boolean showThumbnails, int displayPerPageSetting, int pageNumberValue, String brandValue, String _sortBy,
                            String _nutriName, HttpServletRequest _req, String virtualValue) {
    	StringBuffer buf = new StringBuffer();
        buf.append( "&sortBy=" ).append( _sortBy==null ? "name" : _sortBy );
        if (_nutriName!=null) buf.append("&nutritionName=").append(_nutriName);
        String groceryVirtual = null;
        if (virtualValue==null || virtualValue.length() < 1) {
                 groceryVirtual = _req.getParameter("groceryVirtual");
        } else {
                groceryVirtual = virtualValue;
        }
        if (groceryVirtual!=null) {
                buf.append("&groceryVirtual=").append(groceryVirtual);
        }

        buf.append("&showThumbnails=").append(showThumbnails);

        if (displayPerPageSetting  > 0 ) {
                buf.append("&DisplayPerPage=").append(displayPerPageSetting);
        }

        if (pageNumberValue > 0) {
                buf.append("&pageNumber=").append(pageNumberValue);
        }
        if (brandValue!=null  && brandValue.length()>0) {
                buf.append("&brandValue=").append(URLEncoder.encode(brandValue));
        }

        return buf.toString();
}

public String getPageNumbers(HttpServletRequest requestObj, HttpServletResponse responseObj,int pageNumber, int itemsPerPage, CategoryModel currFolder, int itemsCount,boolean showThumbnails,String brand_Name,String _sortBy,String _nutriName, boolean descending, String display) {
        StringBuffer buf = new StringBuffer();
        String urlParams = buildOtherParams(showThumbnails, itemsPerPage, -1, brand_Name, _sortBy, _nutriName,requestObj, null);
        String urlStart = "/category.jsp?catId=" + currFolder +"&disp=" + display + "&sortDescending=" + descending + "&trk=trans";
        String fullURL = null;
        int startFrom = 1;
		if(pageNumber > 1)   {
                if (pageNumber/10 > 1) {
                        startFrom = ((pageNumber/10) * 10) + 1;
                        fullURL= responseObj.encodeURL(urlStart + urlParams + "&pageNumber=" + startFrom);

                        buf.append("<A HREF=\"");
                        buf.append(urlStart).append(urlParams);
                        buf.append("\">previous</A> . ");
                }

                for (int i=startFrom; i<pageNumber; i++) {
                        fullURL= responseObj.encodeURL(urlStart+urlParams+"&pageNumber="+i);
                        buf.append("<A HREF=\"").append(fullURL).append("\">");
                        buf.append(i).append("</A> . ");
                }
        }

        buf.append("<B>").append(pageNumber).append("</B>");

        if ( itemsCount >= (pageNumber * itemsPerPage) ) {
                // figure out how many additional pages to display
                int addToLoop = 0;
                if(itemsCount % itemsPerPage > 0) {
                        addToLoop = 1;
                }
                for (int i=(pageNumber + 1); (i <= (itemsCount/itemsPerPage + addToLoop)); i++) {
                        fullURL= responseObj.encodeURL(urlStart + urlParams + "&pageNumber=" + i);
                        buf.append(" . <A HREF=\"");
                        buf.append(fullURL);
                        buf.append("\">");
                        if (i%10 == 1) {
                                buf.append("more");
                                break;
                        } else {
                                 buf.append(i);
                        }
                        buf.append("</A>");
                }
        }
        return buf.toString();
}
%>
<%
boolean showThumbnails;
{
        String tmp = request.getParameter("showThumbnails");
        Boolean thumbParam = tmp==null ? null : Boolean.valueOf( tmp );
        Boolean thumbSession = (Boolean)session.getAttribute("showThumbnails");
        if (thumbSession==null) {
                thumbSession = Boolean.TRUE;
        }

        if (thumbParam!=null && !thumbParam.equals(thumbSession) && "true         ".equalsIgnoreCase(request.getParameter("set"))) {
                // toggle was altered, store in session
                session.setAttribute("showThumbnails", thumbParam);
        }
        showThumbnails = thumbParam==null ? thumbSession.booleanValue() : thumbParam.booleanValue();
}

Image brandLogo = null;
String brandPopupLink = null;
String brandName = "";
String brandValue = request.getParameter("brandValue");
if (brandValue==null) {
        brandValue="";
} else {
        //got the brand value..need to get the name.
        BrandModel bm = (BrandModel) ContentFactory.getInstance().getContentNode(brandValue);
        if (bm!=null){
                brandName= bm.getFullName();
                brandLogo = bm.getLogoSmall();
                Html popupContent = bm.getPopupContent();
                if (popupContent!=null) {
                        TitledMedia tm = (TitledMedia)popupContent;
                        EnumPopupType popupType = EnumPopupType.getPopupType(tm.getPopupSize());
                        brandPopupLink = "javascript:pop('" +
                                response.encodeURL( "/brandpop.jsp?brandId="+bm ) + "'," +
                                popupType.getHeight() + "," + popupType.getWidth() + ")";
                }
        }
}

int totalItems = 0;
for (Iterator itr=sortedColl.iterator(); itr.hasNext();) {
    ContentNodeModel cn = (ContentNodeModel)itr.next();
    if (cn instanceof SkuModel)  {
            totalItems++;
    }
}
int itemsToDisplay = 30;
{
        String reqItemsToDisp = request.getParameter("DisplayPerPage");
        String sessItemsToDisp = (String)session.getAttribute("itemsToDisplay");
		
        if ( reqItemsToDisp!=null && (sessItemsToDisp==null || !sessItemsToDisp.equals(reqItemsToDisp)) && "true".equalsIgnoreCase(request.getParameter("set")) ) {
                // we have to update the session with the value from the request
                sessItemsToDisp = reqItemsToDisp;
                session.setAttribute("itemsToDisplay", sessItemsToDisp);
        }else{
        	reqItemsToDisp = (String)session.getAttribute("itemsToDisplay");
        }
        try {
                if (reqItemsToDisp!=null) {
                        itemsToDisplay = Integer.valueOf(reqItemsToDisp).intValue();
                } else if (sessItemsToDisp!=null) {
                        itemsToDisplay = Integer.valueOf(sessItemsToDisp).intValue();
                }
                if (itemsToDisplay!=30 && itemsToDisplay!=60 && itemsToDisplay!=totalItems) {
                        itemsToDisplay = 30;
                }
        } catch (NumberFormatException nfe) {
                itemsToDisplay = 30;
        }

}


int pageNumber = 1;
try {
        pageNumber = Integer.valueOf(request.getParameter("pageNumber")).intValue();
} catch (NumberFormatException nfe) {}
// setup for succpage redirect ....

String successPage = FDURLUtil.getCartConfirmPageURI(request);
request.setAttribute("successPage", successPage);
%>
<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= "/grocery_cart_confirm.jsp?catId="+catId %>' source='<%= request.getParameter("fdsc.source")%>'>
<%
//*** if we got this far..then we need to remove the sucess page attribute from the request.
request.removeAttribute("successPage");

{
        // there are errors..Display them
        Collection myErrs=((ActionResult)result).getErrors();
%>
        <table border="0" cellspacing="0" cellpadding="0" width="425">
	  
<%
        for (Iterator errItr = myErrs.iterator();errItr.hasNext(); ) {
                String errDesc = ((ActionError)errItr.next()).getDescription();
%>
        <tr valign="top">
        <td width="350" valign="middle">
               <div id="error_descriptions">   <FONT class="text12bold" color="#CC3300"><%=errDesc%></FONT></div>
        </td></tr>
 <%
        }
%>
       
        </table>
<%
}

int syncProdIdx = -1;
double syncProdQty=0.0;
String syncProdSkuCode = null;
String syncProdSalesUnit = null;
//fix for What's Good "context" being set to a different cat than product's cat and price not being found
String prodCatId = request.getParameter("prodCatId");
boolean bigProdShown = false;
boolean hasNutrition = false;
boolean hasIngredients = false;

String productCode = request.getParameter("productId");
String reqSkuCode = request.getParameter("skuCode");

/** List of all SKUs in the page, for the pricing structures */
List skus = new ArrayList( itemsToDisplay );

%>
<script type="text/javascript" src="/assets/javascript/grocery_product.js"></script>
<%

// iterate throught the list of items in the sorted set and remove all folders.  grab the brands in the process

for (Iterator itr = sortedColl.iterator(); itr.hasNext(); ){
        ContentNodeModel item = (ContentNodeModel)itr.next();
        boolean matchingBrand = false;
        if (!(item instanceof SkuModel)) {
                itr.remove();
        } else {
                List prodBrands = ((ProductModel)((SkuModel)item).getParentNode()).getBrands();

                if (prodBrands!=null && prodBrands.size() > 0 && brandValue!=null && brandValue.length()>0) {
                        for (Iterator brandItr = prodBrands.iterator();brandItr.hasNext();) {
                                 if (brandValue.equals(((BrandModel)brandItr.next()).getContentName())){
                                        matchingBrand = true;
                                        break;
                                 }
                        }
                }
                if (brandValue!=null && brandValue.length()>0 && !matchingBrand){
                        //remove this product, cause we're only displaying certain brands
                        itr.remove();
                }
        }
}

ProductModel[] productArray = null; //(ProductModel[])sortedColl.toArray( new ProductModel[0] );
int skuCount =0;
List allSkuModels = new ArrayList();
for (Iterator skuItr=sortedColl.iterator(); skuItr.hasNext();) {
        ContentNodeModel cn = (ContentNodeModel)skuItr.next();
        if (cn instanceof SkuModel)  {
                allSkuModels.add((SkuModel) cn);
        }
}

//fix for What's Good "context" being set to a different cat than product's cat and price not being found
	ProductModel prodModel = null;
	//check these values before using
	if ((prodCatId != null || !"".equals(prodCatId)) && (productCode != null || !"".equals(productCode))) {
		prodModel = ContentFactory.getInstance().getProductByName(prodCatId, productCode);
		//check if we have a product model, and a default sku
		if (prodModel != null && prodModel.getDefaultSku() != null && !allSkuModels.contains(prodModel.getDefaultSku()) ) {
			//add to skus list
			allSkuModels.add(prodModel.getDefaultSku());
		} 
	}

skuCount=allSkuModels.size();


// get all the brands that in the category that we are in
CategoryModel groceryCategory;
{
        ContentNodeModel node = currentFolder;
        // find the topmost Category
        while (!(node.getParentNode() instanceof DepartmentModel)) {
                node = node.getParentNode();
        }
        groceryCategory=(CategoryModel)node;
}

//if we change the display of items per page to a higher number on a page other than one, we could stay on that page with no products shown on it
if(((pageNumber -1) * itemsToDisplay) > skuCount) {
        //then set the page number back to one
        pageNumber = 1;
}
%>
<table border="0" cellspacing="0" cellpadding="0" width="425">
<form name="groceryForm" id='grocery_form' method="POST">
<%
//If there is a specific product selected then show it above the listings here
//lets get the product with the product cod in the section, display this product, then the rest of the products

if(productCode!=null && prodCatId !=null ) {
        Image bigProductImage = null;

%>
<%if (FDStoreProperties.isAdServerEnabled()) {%>
<tr><td colspan="3">
        <SCRIPT LANGUAGE=JavaScript>
        <!--
        OAS_AD('ProductNote');
        //-->
        </SCRIPT>
</td></tr>
<%}%>
  <fd:ProductGroup id='productNode' categoryId='<%= prodCatId %>' productId='<%= productCode %>'>
<%

        boolean qualifies = productNode.isQualifiedForPromotions() && user.getMaxSignupPromotion()>0;
        double promotionValue = 0.0;
        if (qualifies) {
                promotionValue = user.getMaxSignupPromotion();
        }
        String prefix = String.valueOf(promotionValue);
        prefix = prefix.substring(0, prefix.indexOf('.'));

        List prodSkus = productNode.getSkus();
        bigProdShown = true;
        SkuModel minSku = null;
        bigProductImage = productNode.getDetailImage();
        String thisProdBrandLabel = "";
        String thisProdBrand = "";
        String prodNameLower= productNode.getFullName().toLowerCase();
        BrandModel thisBrandModel = null;

        // get the first brand name, if any.
        Image titleBrandLogo = null;
        List brandsForProd = productNode.getBrands();
        if (brandsForProd!=null && brandsForProd.size()>0) {
                thisBrandModel = (BrandModel)brandsForProd.get(0);
                thisProdBrand = thisBrandModel.getContentName();
                titleBrandLogo = thisBrandModel.getLogoSmall();
                Html popupContent = thisBrandModel.getPopupContent();
                if (popupContent!=null) {
                        TitledMedia tm = (TitledMedia)popupContent;
                        EnumPopupType popupType=EnumPopupType.getPopupType(tm.getPopupSize());
                        brandPopupLink = "javascript:pop('"+response.encodeURL("/brandpop.jsp?brandId="+thisBrandModel)+"',"+popupType.getHeight()+","+popupType.getWidth()+")";
                }


                for (int bx = 0;bx<brandsForProd.size();bx++){
                        if (prodNameLower.startsWith(((BrandModel)brandsForProd.get(bx)).getFullName().toLowerCase())) {
                                thisProdBrandLabel = ((BrandModel)brandsForProd.get(bx)).getFullName();
                                break;
                        }
                }

        }

        String prodPrice = "";
        String prodBasePrice="";
        int deal=0;
        boolean hasWas=false;
        String dealsImage="";
    String priceUnit = "";
        String firstSalesUnit = null;
        //get the first sku..in the event that this product is unavailabe. Ideally we should only be in this blokc
        // if the product is available
        minSku = (SkuModel)prodSkus.get(0);  // we only need one sku
        
        for (ListIterator li=prodSkus.listIterator(); li.hasNext(); ) {
                        SkuModel sku = (SkuModel)li.next();
                        if ( sku.isUnavailable() ) {
                                        li.remove();
                        }
        }
        if (prodSkus.size() > 0 && reqSkuCode==null) {
            minSku = (SkuModel) ( prodSkus.size()==1 ? prodSkus.get(0)  : Collections.min(prodSkus, ProductModel.PRICE_COMPARATOR) );
        } else if (reqSkuCode!=null){
                minSku = productNode.getSku(reqSkuCode);
        }
        FDProduct product = null;
        boolean skuAvailable=false; 
%>
<fd:FDProductInfo id="productInfo" skuCode="<%= minSku.getSkuCode() %>">
<%
	/* In preview mode..prods may not have an underlying FDProduct..so if the productInfo says its
    * discontinued or tempUnavailable then skip the product */

         skuAvailable = !minSku.isUnavailable();
         try{
            product = FDCachedFactory.getProduct(productInfo);
        } catch (FDSkuNotFoundException fdsnf){
            JspLogger.PRODUCT.warn("Grocery Page: catching FDSkuNotFoundException and Continuing:\n FDProductInfo:="+productInfo+"\nException message:= "+fdsnf.getMessage());
        }

        prodPrice = JspMethods.currencyFormatter.format(productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getDefaultPrice());
        hasWas=productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).isItemOnSale();
        if(hasWas) {
            prodBasePrice=JspMethods.currencyFormatter.format(productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getSellingPrice());
        }
        deal=productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getHighestDealPercentage();
        if (deal > 0) {
            dealsImage=new StringBuffer("/media_stat/images/deals/brst_lg_").append(deal).append(".gif").toString();        	
        }
        
        priceUnit = productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
        String salesUnitDesc = "N/A";
        firstSalesUnit = "N/A";
        if (product!=null){
            FDSalesUnit[] salesUnits = product.getSalesUnits();
            if (salesUnits.length > 0 ) {
                    salesUnitDesc = " - "+salesUnits[0].getDescription();
                    firstSalesUnit = salesUnits[0].getName();
            }
            hasNutrition = product.hasNutritionFacts();
            hasIngredients = product.hasIngredients();
        }
%>
<input type="hidden" name="skuCode_big" value="<%=minSku.getSkuCode()%>">
<input type="hidden" name="salesUnit_big" value="<%=firstSalesUnit%>">
<input type="hidden" name="catId_big" value="<%=prodCatId%>">
<input type="hidden" name="productId_big" value="<%=productCode%>">
<%
	if (request.getParameter("fdsc.source") != null) {
%><input type="hidden" name="fdsc.source" value="<%=request.getParameter("fdsc.source")%>"/> <%
 	}
 %>
<tr valign="top"><td width="275">
<table cellpadding="0" cellspacing="0" border="0"><tr>
<%
	if (titleBrandLogo!=null) {
%>
        <td>
<%
	if (brandPopupLink!=null) {
%>
<table cellpadding="0" cellspacing="0" border="0"><tr><td><a href="<%=brandPopupLink%>"><img src="<%=titleBrandLogo.getPath()%>" width="<%=titleBrandLogo.getWidth()%>" height="<%=titleBrandLogo.getHeight()%>" border="0"></a></td><td>&nbsp; <a href="<%=brandPopupLink%>">Learn more about <%=thisProdBrandLabel%></a></td></tr></table>
        <%
        	} else {
        %>
                <img src="<%=titleBrandLogo.getPath()%>" width="<%=titleBrandLogo.getWidth()%>" height="<%=titleBrandLogo.getHeight()%>" border="0">
        <%
        	}
        %>
        </td>
<%
	}
%>
</tr></table><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br>
<font class="title14" <%if (!skuAvailable) {%>color="#999999"<%}%>><%
	//
        // annotation mode
        //
        String productTitle = thisProdBrandLabel + " " + productNode.getFullName().substring(thisProdBrandLabel.length()).trim();
        List domains = minSku.getVariationMatrix();
        StringBuffer key = new StringBuffer();
        for(Iterator i = domains.iterator(); i.hasNext(); ){
                DomainValue domainValue = ((DomainValue)i.next());
                key.append(domainValue.getLabel());
                key.append("  ");
                key.deleteCharAt(key.length()-1);
        }
        productTitle= productTitle + (key.length()>0 ? ", " + key.toString() : "");
        if (!" - nm".equalsIgnoreCase(salesUnitDesc)) {
                        productTitle += salesUnitDesc;
                }
        if (FDStoreProperties.isAnnotationMode()) {
%><%@ include file="/includes/layouts/i_grocery_annotated_title.jspf" %><%
	} else {
                // no annotation, just display title
%><%=productTitle%><%
	}
%></font><br>
<%
	boolean showUnavailableText = true;
        Date earliestDate = minSku.getEarliestAvailability();
        Calendar testDate = new GregorianCalendar();
        testDate.add(Calendar.DATE, 1);
    // cheat: if no availability indication, show the horizon as the
    //        earliest availability
    if (earliestDate == null) {
        earliestDate = DateUtil.addDays(DateUtil.truncate(new Date()),
                                        ErpServicesProperties.getHorizonDays());
    }
        if(skuAvailable && QuickDateFormat.SHORT_DATE_FORMATTER.format(testDate.getTime()).compareTo(QuickDateFormat.SHORT_DATE_FORMATTER.format(earliestDate)) < 0){
                SimpleDateFormat sf = new SimpleDateFormat("MM/dd");
                showUnavailableText = false;
%>
<br>
<b><font class="text12rbold">Earliest Delivery - <%=sf.format(earliestDate)%></font></b>
<br>
<%
	}
%>

<%
	if (!skuAvailable) {
%>
        <%
        	if (showUnavailableText) {
        %>
        <br>
        <b><font class="text12rbold">This item is temporarily unavailable.</font></b><br><br>
        <img src="/media_stat/images/layout/999999.gif" width="225" height="1" border="0" vspace"5"><br>
        <%
        	}
        %>
        <%
        	if (productNode.getProductDescription()!=null && productNode.getProductDescription().getPath()!=null && (productNode.getProductDescription().getPath().toString()).indexOf("blank_file") < 0 ) {
        %>

        <br><fd:IncludeMedia name="<%=productNode.getProductDescription().getPath()%>" /><br>
        <%
        	}
        %>

        <input type="hidden" value="" name="quantity_big">
        <INPUT type="hidden" class="text11bold" NAME="PRICE" SIZE="7" onFocus="blur()" value="">
<%
	} else {
        // !productNode.isUnavailable()
        syncProdQty = productNode.getQuantityMinimum();
        syncProdSkuCode = minSku.getSkuCode();
        syncProdSalesUnit = firstSalesUnit;
%>
<br>
<%
	if(hasWas) {
%>
			 <div>
				<table>
					<tr>
						<td>
							<display:ProductPrice impression="<%= new ProductImpression(productNode) %>" grcyProd="true" showRegularPrice="true" showScalePricing="false" showWasPrice="false" showDescription="false"/>
						</td>
						<td>
							<display:ProductPrice impression="<%= new ProductImpression(productNode) %>" grcyProd="true" showRegularPrice="false" showScalePricing="false" showWasPrice="true" showDescription="false"/>
						</td>
					</tr>
				</table>
						<display:ProductPrice impression="<%= new ProductImpression(productNode) %>" grcyProd="true" showRegularPrice="false" showScalePricing="true" showWasPrice="false" showDescription="false"/>
			</div>
<%
	} else {
%>    
			<div>
					<display:ProductPrice impression="<%= new ProductImpression(productNode) %>" grcyProd="true" showDescription="false"/>
			<br></div>
<%
	}
%>

<%@include file="/includes/product/i_price_taxdeposit.jspf"%>

<br>
        <table border="0" cellspacing="0" cellpadding="1" width="215">
        <tr valign="MIDDLE">
        <td width="25" CLASS="text11bold">Quantity&nbsp;</td>
        <td width="30" ALIGN="CENTER"><INPUT TYPE="text" NAME="quantity_big" SIZE="2" MAXLENGTH="2" CLASS="text11" value="<%=Math.round(productNode.getQuantityMinimum())%>" onChange="chgQty('quantity_big',0,<%=productNode.getQuantityMinimum()%>,<%=user.getQuantityMaximum(productNode)%>);"></td>
        <td width="10"><A HREF="javascript:chgQty('quantity_big',<%=productNode.getQuantityIncrement()%>,<%=productNode.getQuantityMinimum()%>,<%=user.getQuantityMaximum(productNode)%>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="2" alt="greater quantity"></A><br>
        <A HREF="javascript:chgQty('quantity_big',-<%=productNode.getQuantityIncrement()%>,<%=productNode.getQuantityMinimum()%>,<%=user.getQuantityMaximum(productNode)%>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="2" alt="lesser quantity"></A></td>
        <td width="50" CLASS="text11bold" ALIGN="RIGHT">Price&nbsp;</td>
        <td width="100"><INPUT class="text11bold" TYPE="text" NAME="PRICE" SIZE="7" onFocus="blur()" value=""></td>
        </tr>
        </table>
<br>
<input type="image" name="addSingleToCart_big" src="/media_stat/images/buttons/add_to_cart.gif"  ALT="ADD THIS ITEM TO YOUR CART" width="93" height="20" HSPACE="2" VSPACE="2" border="0"><br>
<%=FDURLUtil.getHiddenCommonParameters(request.getParameterMap(), "_big")%>
<%@ include file="/shared/includes/product/i_minmax_note.jspf" %>
<%@ include file="/includes/product/i_delivery_note.jspf" %>
<%@ include file="/includes/product/i_cancellation_note.jspf" %>


<br><img src="/media_stat/images/layout/999999.gif" width="225" height="1" border="0" vspace"5"><br>
	<fd:CCLCheck>
		<div style="margin: 15px 5px 0 5px;">       
			<a id="ccl-add-action" class="text12" href="/unsupported.jsp" onclick="return CCL.save_items('grocery_form',this,'action=CCL:AddMultipleToList&source=ccl_sidebar_big')"><img src="/media_stat/ccl/lists_link_with_icon_dfgs.gif" width="112" height="15"  style="padding: 0 14px 0 0; border: 0;"></a><fd:CCLNew/>
		</div>		             
		<div style="margin: 0 0 0 5px;">       
             <fd:CCLNew template="/common/template/includes/ccl_moreabout.jspf"/>
		</div>		             
	</fd:CCLCheck>
<%
	if (productNode.getProductDescription()!=null && productNode.getProductDescription().getPath()!=null && productNode.getProductDescription().getPath().indexOf("blank_file.txt") < 0) {
%>
          <br><fd:IncludeMedia name="<%=productNode.getProductDescription().getPath()%>" /><br>
<%
	}
%>

<%
	if(productNode.getCountryOfOrigin().size()>0) {
%>
				<br><b>Origin: </b>
                
			   <logic:iterate id="coolText" collection="<%=productNode.getCountryOfOrigin()%>" type="java.lang.String">
               <br><%=coolText%>
               </logic:iterate>
               <br>
  <%
  	}
  %>

    <%
    	if (product!= null && product.hasNutritionInfo(ErpNutritionInfoType.HEATING)) {
    %>
                     <br><font class="title12">Heating Instructions</font><br>
                     <%=product.getNutritionInfoString(ErpNutritionInfoType.HEATING)%><br>
<%
	}
%>

<%
	}
%>

<%@ include file="/shared/includes/product/organic_claims.jspf" %>

<%@ include file="/includes/product/claims.jspf" %>

<%@ include file="/includes/product/kosher.jspf" %>

<%
	if (hasNutrition || hasIngredients) {
%>
<br><A HREF="javascript:pop('/nutrition_info.jsp?catId=<%=request.getParameter("prodCatId")%>&productId=<%=request.getParameter("productId")%>',335,375)">Nutrition, Ingredients, and Allergens</A>
<%
	} else {
%><br>Please check product label for nutrition, ingredients, and allergens.<%
	}
%><br>
</td>
<td width="10">&nbsp;</td><%-- buffer cell --%>
<td align="center">
	<br />
	<table border="0" width="100%" cellspacing="0" cellpadding="0">
		<tr>
			<td align="left">
        		<% if ( FDStoreProperties.useOscache() ) { %> 
					<oscache:cache time="300">
						<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
					</oscache:cache>
        		<% } else { %>			        
						<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
        		<% } %>
				<% if(qualifies && !productNode.isUnavailable()) { %>
					<table>
						<tr>
							<td><img src="/media_stat/images/template/offer_icon.gif" alt="Promotion icon"></td>
							<td><font class="title12">Free!<br></font><A HREF="promotion.jsp?cat=<%=catId%>">See our $<%=prefix%> offer</a></td>
						</tr>
					</table>
					<br />
				<% } %>
				<%@ include file="/shared/includes/product/i_product_image.jspf" %>
			</td>
		</tr>
	</table>
	<%
// ******** START -- Freshness Guarantee graphic ******************	
String shelfLife = JspMethods.getFreshnessGuaranteed(productNode);
if(shelfLife != null && shelfLife.trim().length() > 0) { %>		

	<table width="0" border="0" cellspacing="0" cellpadding="0">
		<tr>
		    <td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="140" height="9"></td>
		</tr>
		<tr>
		    <td height="5"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6"></td>
		    <td height="5" style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="160" height="1"></td>
		    <td height="5"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6"></td>
		</tr>


		<tr> 
		    <td colspan="3" align="center" valign="top">

			<table width="0" border="0" cellspacing="0" cellpadding="0">
				<tr><td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;">
					<table border="0" cellspacing="0" cellpadding="0" width="0">


						<tr valign="top">
						    <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
						    <td width="27"><img src="/media/images/site_pages/shelflife/days_<%=shelfLife%>.gif" width="27" height="27" border="0"></td>
						    <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
						    <td  valign="top"><img src="/media/images/site_pages/shelflife/guarant_fresh_hdr_lg.gif" width="129" height="10"><br />
						    <span class="text12">at least </span><span class="title12"><%=shelfLife%> days</span><span class="text12"><br> from delivery</span></td>
						    <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>								    
						</tr>


					</table>
				</td></tr>
			</table>
		    </td>
		</tr>
		<tr>
		    <td height="5"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6"></td>
		    <td height="5" style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		    <td height="5"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6"></td>
		</tr>
		
	</table>
	<table width="188">
		<tr>
			<td align="right">
			<img src="/media_stat/images/layout/clear.gif" width="100%" height="6">
			<a href="javascript:pop('/brandpop.jsp?brandId=bd_fd_fresh_guarantee',400,585)">Learn more about our Freshness Guarantee - CLICK HERE</a>
			</td>
		</tr>
	</table>

<%}
// ******** END -- Freshness Guarantee graphic ******************	
%>


</td>
</tr>
</table>
<br>
<%
	if (thisBrandModel !=null) { 
String  viewBrandURL = response.encodeURL("/category.jsp?catId="+groceryCategory+buildOtherParams(showThumbnails,itemsToDisplay,-1,thisProdBrand,sortBy,nutriName,request,groceryCategory.getContentName())+"&sortDescending=" + descending + "&disp=" + display + "&trk=pkprod");
%>
<table width="425" cellpadding="1" cellspacing="0" border="0" bgcolor="#FF9933" align="center">
        <tr><td><table width="100%" cellpadding="3" bgcolor="#FFFFFF"><tr><td align="center" class="text11bold"><a href="<%=viewBrandURL%>">View All
<%=thisProdBrandLabel%> Products in <%=groceryCategory.getFullName()%></a></td></tr></table></td></tr>
</table>
<%
	} else {
%>
<img src="/media_stat/images/layout/999999.gif" width="425" height="1" border="0" vspace"5"><br>
<%
	}
%>
</fd:FDProductInfo>
</fd:ProductGroup>
<br>
<%
	}
if (!bigProdShown) {
        // build hidden field to hold price..so java script does not cause err.
%><input type="hidden" name="PRICE">
<input type="hidden" name="quantity_big" value="">
<input type="hidden" name="skuCode_big" value="noBigProductDisplay">
<%
	}


//==== grocery top nav ends here  ======
// if we have a product that was specified from the featured list then we must find the page that it's on
//count how many products that are to be displayed.
if(productCode != null || reqSkuCode!=null) {
        int currPage = 1;
        for (int j=0;j<allSkuModels.size();j++) {
                        SkuModel skuModel = (SkuModel)allSkuModels.get(j);
                        
                        if(j%itemsToDisplay==0 && j !=0 ) currPage++;
                        if ((reqSkuCode!=null && skuModel.getSkuCode().equals(reqSkuCode) ) || reqSkuCode==null && ((ProductModel)skuModel.getParentNode()).getContentName().equals(productCode)) {
                                pageNumber = currPage;
                                break;
                        }
                }
}


// the grocery top nav stuff starts here
%>
<%--- ====================   Start of Header Area ==============================   ----%>
<%
	String pageNumberLinks = getPageNumbers(request,response,pageNumber,itemsToDisplay, (CategoryModel)currentFolder, skuCount,showThumbnails,brandValue,sortBy,nutriName, descending, display);

String brandOrFolderName = "".equals(brandValue)?currentFolder.getFullName():brandName;
if (brandLogo !=null) {
        brandOrFolderName = "<img src=\""+brandLogo.getPath()+"\" width=\""+brandLogo.getWidth()+"\"  height=\""+brandLogo.getHeight()+"\" alt=\""+brandName+"\" border=\"0\">";
        if (brandPopupLink!=null) { brandOrFolderName += "</a>"; }
}
%>
<table border="0" cellspacing="0" cellpadding="0" width="425">
<tr valign="BOTTOM">
    <td><%
    	if (brandPopupLink!=null) {
    %><a href="<%=brandPopupLink%>"><%
    	}
    %><FONT CLASS="title12"><%=brandOrFolderName%></FONT><%
    	if (brandPopupLink!=null) {
    %></a><%
    	}
    %> (<%=skuCount%>&nbsp;items)
    </td>
    <td ALIGN="RIGHT">
        <SELECT NAME="brand" onChange="javascript:gotoWindow(this.options[this.selectedIndex].value)" CLASS="text10">
<%
	String categoryTitle = groceryCategory.getFullName();
        String optionURL = response.encodeURL("/category.jsp?catId=" + groceryCategory + buildOtherParams(showThumbnails,itemsToDisplay,-1,"",sortBy,nutriName,request,groceryCategory.getContentName()) + "&disp=" + display + "&sortDescending=" + descending + "&trk=trans");
%>
            <OPTION NAME="ACK">Find a Brand in  <%=categoryTitle%>
<%
	if(!"".equals(brandValue)) {
%>
            <OPTION NAME="ACK" value="<%=optionURL%>">All Brands<%
            	}

                    for (Iterator br = groceryCategory.getAllBrands().iterator(); br.hasNext(); ) {
                            BrandModel myBrand = (BrandModel)br.next();
                            optionURL = response.encodeURL("/category.jsp?catId=" + groceryCategory + buildOtherParams(showThumbnails,itemsToDisplay,-1,myBrand.getContentName(),sortBy,nutriName,request,groceryCategory.getContentName())+ "&disp=" + display + "&sortDescending=" + descending + "&trk=trans");
            %>
            <OPTION  value="<%=optionURL%>"><%=myBrand.getFullName()%></OPTION>
<%
	}
%>
        </SELECT>
    </td>
</tr>
<tr>
    <td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
<tr>
    <td colspan="2" bgcolor="#FF9933"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>
<%
	if (FDStoreProperties.isAdServerEnabled()) {
%>
<tr>
    <td colspan="2">
        <SCRIPT LANGUAGE=JavaScript>
        <!--
        OAS_AD('CategoryNote');
        //-->
        </SCRIPT>
    </td>
</tr>
<%
	}

//
// EDITORIAL PARTIAL
//

Html editorialMedia = currentFolder.getEditorial();
if (doRenderEditorialPartial && editorialMedia != null && !editorialMedia.isBlank()) {
%>
<tr>
    <td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td>
</tr>
<tr>
    <td colspan="2"><fd:IncludeMedia name='<%=editorialMedia.getPath()%>'/></td>
</tr>
<tr>
    <td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td>
</tr>
<tr>
    <td colspan="2" bgcolor="#FF9933"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
</tr>
<%
	}
%>
<tr>
    <td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
<%
	if (!bigProdShown && brandPopupLink!=null) {
%>
<tr>
    <td colspan="2"><a href="<%=brandPopupLink%>">Learn more about <%=brandName%></a></td>
</tr>
<tr>
    <td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
<tr valign="top">
    <td colspan="2" bgcolor="#FF9933"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0"></td>
</tr>
<tr>
    <td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
<%
	}
%>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="425">
<tr valign="top">
    <td CLASS="text10bold" width="215">Page: <%=pageNumberLinks%></td>
    <td ALIGN="RIGHT"><b>Sort by:</b>
        <SELECT NAME="sort" onChange="javascript:gotoWindow(this.options[this.selectedIndex].value!=''?this.options[this.selectedIndex].value:null)" CLASS="text10">
<%
	optionURL = response.encodeURL("/category.jsp?catId=" + currentFolder + "&groceryVirtual=All&trk=cpage");
%>
        <OPTION NAME="Ack" <%="name".equalsIgnoreCase(sortBy)?"selected":""%> value="<%=optionURL%>">Name</option>
<%
	optionURL = response.encodeURL("/category.jsp?catId=" + currentFolder + "&sortBy=price&groceryVirtual=All&trk=cpage");
%>
        <OPTION NAME="Ack" <%="price".equalsIgnoreCase(sortBy)?"selected":""%>  value="<%=optionURL%>">Price</option>
<%
	optionURL = response.encodeURL("/category.jsp?catId=" + currentFolder + "&sortBy=kosher&groceryVirtual=All&trk=cpage");
%>
        <OPTION NAME="Ack" <%="kosher".equalsIgnoreCase(sortBy)?"selected":""%>  value="<%=optionURL%>">Kosher</option>
<%
        boolean allowNutSort = ((CategoryModel)currentFolder).isNutritionSort();
        if(allowNutSort){
%>
        <OPTION value="">Nutrition:</option>
<%
	for(Iterator i = ErpNutritionType.getCommonList().iterator(); i.hasNext();){
                ErpNutritionType.Type nutriType = (ErpNutritionType.Type)i.next();
                optionURL = response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(false,45,-1,brandValue,"nutrition",nutriType.getName(),request,null)+"&disp="+nutriType.getName()+"&sortDescending="+nutriType.isGood()+"&trk=trans");
                String optionDisplay = StringUtils.replace( nutriType.getDisplayName(), " quantity", "");
                if(nutriType.getUom().equals("%"))
                    optionDisplay = optionDisplay + " % daily value";
%>
        <OPTION NAME="Ack" <%=nutriType.getName().equalsIgnoreCase(display)?"selected":""%>  value="<%=optionURL%>">&nbsp;&nbsp;<%=optionDisplay%></option>
<%
	}
        }
%>

        </SELECT>
    </td>
</tr>
</table>
<%
	//========================  end header area ===============================================

//begin displaying the products on the page
boolean isAnyProdAvailable = true;
int loopEnd = Math.min(skuCount, itemsToDisplay * pageNumber);
int itemShownIndex =-1;
int imgShownIndex =-1;
//for every five items we display we are going to want to print out a closing and opening table tag

int displaySection = 0;        //integer to keep track of how many rows of 5 we are printing out
//figure out if all the products that will be displayed are unavailable, and set a flag
for(int i = (pageNumber -1) * itemsToDisplay; i < loopEnd && isAnyProdAvailable==false;i++) {
   // isAnyProdAvailable = (!displayProduct.isUnavailable());
}
 // if we are not displaying the amplified product (bigProduct area) then show the add selected to cart button at the top
%>
<table border="0" cellspacing="0" cellpadding="0" width="425"><tr>
<%
	if(!bigProdShown && isAnyProdAvailable) {
%><td><input type="image" name="addMultipleToCart" src="/media_stat/images/buttons/add_selected_to_cart.gif" width="145" height="20" hspace="4" vspace="4" border="0" alt="ADD SELECTED ITEMS TO CART"></td><%
	} if(allowNutSort){
%><td align="right"><img src="/media_stat/images/template/grocery/sort_by_nutrition.gif"></td><%
	}
%></tr></table>
<img src="/media_stat/images/layout/clear.gif" width="1" height="6">


<%
	if(showThumbnails) {
        for(int i = (pageNumber -1) * itemsToDisplay; i < loopEnd; i += 5) {
                //now in the main loop, we need two inner loops, the first loop prints 5 images horizontally, the second print the products vertically
                int innerLoopEnd = Math.min(i + 5, loopEnd);
%>
<table border="0" cellspacing="0" cellpadding="0" width="425">
        <tr valign="top">
<%
	String otherParams = buildOtherParams(showThumbnails, itemsToDisplay, -1, brandValue, sortBy, nutriName,request, null);
                for(int j = i; j < innerLoopEnd;j++) {
                    SkuModel sku = (SkuModel)allSkuModels.get(j);
                    if (sku==null) continue;
                    String _dealImage="";
                    if(sku.getProductInfo().getZonePriceInfo(user.getPricingContext().getZoneId()).getHighestDealPercentage() > 0) {
                        _dealImage=new StringBuffer("/media_stat/images/deals/brst_sm_").append(sku.getProductInfo().getZonePriceInfo(user.getPricingContext().getZoneId()).getHighestDealPercentage()).append(".gif").toString();
                    }
                    ProductModel displayProduct = sku.getProductModel();
                        Image groDeptImage = (Image)displayProduct.getCategoryImage();
                        imgShownIndex++;
                        String imgLinkUrl = response.encodeURL("/category.jsp?catId="+currentFolder
                                + otherParams
                                + "&prodCatId="+displayProduct.getParentNode()
                                + "&productId="+displayProduct.getContentName())+"&trk=trans";
%>
                <td width="85">
                <div id="prod_container" style="height: 90px; width: 90px; text-align: left;" onMouseOver="changeImg(document.bullet<%=imgShownIndex%>,'in',0)" onMouseOut="changeImg(document.bullet<%=imgShownIndex%>,'out',0)">
					<div>

						  <display:ProductImage product="<%= displayProduct %>" action="<%= imgLinkUrl %>" showRolloverImage="true" />
					
					</div>
                </div>
                <IMG SRC="/media_stat/images/layout/dot_clear.gif" width="5" height="1" border="0"></td>
<%
	}
%>
        </tr>
</table>
<br>
<table width="425" border="0" cellspacing="0" cellpadding="0">
<%@include file="/includes/layouts/i_grocery_product_separator.jspf"%>
<%
	//now the second loop - displays product descriptions vertically
                for(int jj = i; jj < innerLoopEnd;  jj++) {
                    SkuModel sku = (SkuModel)allSkuModels.get(jj);
                    ProductModel displayProduct = sku.getProductModel();
                        itemShownIndex++;
                        skus.add( sku);
%><%@include file="/includes/layouts/i_grocery_product_line.jspf"%><%
	if (sku.getSkuCode().equals(syncProdSkuCode)) {
                        //if (displayProduct.getContentName().equals(productCode)) {
                                syncProdIdx = itemShownIndex;
                        }
                }
%></table><br>
<%
	} // end outer loop

} else {

        // we are not displaying thumbnails
%><table width="425" border="0" cellspacing="0" cellpadding="0">
<%@include file="/includes/layouts/i_grocery_product_separator.jspf"%>
<%
	for(int i = (pageNumber -1) * itemsToDisplay; i < loopEnd; i++) {
            SkuModel sku = (SkuModel)allSkuModels.get(i);
            ProductModel displayProduct = sku.getProductModel();
                skus.add( sku);
                itemShownIndex++;
%><%@include file="/includes/layouts/i_grocery_product_line.jspf"%><%

                if (displayProduct.getContentName().equals(productCode)) {
                        syncProdIdx = itemShownIndex;
                }
        }
%></table><br><br>
<%
} //end else

%>
<%-- if we are adding to the cart via the form submit, then we need to set a variable in a hidden field --%>
<input type="hidden" name="itemCount" value="<%= Math.min(itemsToDisplay, itemShownIndex+1) %>">

<%-- the controller tag must also know if an item is added to the list from the side bar (i.e. that particular item) or the actual selected ones 
     the values are "actual_selection" or "side_bar" + _ + item number
<input type="hidden" name="source" value="cart_selection">
  --%>

<%
if(isAnyProdAvailable) {
%>
        <table border="0" cellspacing="0" cellpadding="0" width="425"><tr valign="BOTTOM"><td width="425">
        <input type="image" name="addMultipleToCart" src="/media_stat/images/buttons/add_selected_to_cart.gif" width="145" height="20" hspace="4" vspace="4" border="0" alt="ADD SELECTED ITEMS TO CART">
        <br>
        <fd:CCLCheck>
            <div style="margin: 7px 4px 0 4px;"><a id="ccl-add-action" href="/unsupported.jsp" onclick="return CCL.save_items('grocery_form',this,'action=CCL:AddMultipleToList&source=ccl_actual_selection','source=ccl_actual_selection')"><img src="/media_stat/ccl/lists_link_selected_with_icon_dfgs.gif" width="151" height="15" style="border: 0; padding-right: 14px"></a><fd:CCLNew/></div>
			<div style="margin: 0 0 1ex 4px;">
                <fd:CCLNew template="/common/template/includes/ccl_moreabout.jspf"/>
            </div>
        </fd:CCLCheck>
        </td></tr></table>
<%} %>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="425">
<tr>
<td><FONT CLASS="title11"><%=brandOrFolderName%></FONT> <FONT CLASS="text9">(<%= skuCount %> items)</FONT><br></td>
<td ALIGN="RIGHT"><%
String thumbNailURL = response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams( !showThumbnails, itemsToDisplay, pageNumber,brandValue,sortBy,nutriName,request,null)+"&disp=" + display + "&sortDescending=" + descending + "&trk=thum&set=true");
if(showThumbnails) {
%>
Thumbnails <B>ON</B> | <A HREF="<%=thumbNailURL%>">OFF</A><%
} else {
%>
Thumbnails <A HREF="<%=thumbNailURL%>">ON</A> | <B>OFF</B><%
}
%></td>
</tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
<tr><td colspan="2" bgcolor="#FF9933"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td></tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="425">
<tr valign="top"><td CLASS="text10bold" width="240">Page: <%= pageNumberLinks %><br></td>
<td ALIGN="RIGHT" width="185">Display <%
if (itemsToDisplay == 30) {
%>
<B>30</B> |
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(showThumbnails,60,-1, brandValue,sortBy,nutriName,request,null)+"&disp=" + display + "&sortDescending=" + descending + "&trk=numb&set=true") %>">60</A> |
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(showThumbnails,skuCount,-1, brandValue,sortBy,nutriName,request,null)+"&disp=" + display + "&sortDescending=" + descending + "&trk=numb&set=true") %>">ALL</A>
<%
} else if (itemsToDisplay == 60) {
%>
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(showThumbnails,30,-1,brandValue,sortBy,nutriName,request,null)+"&disp=" + display + "&sortDescending=" + descending + "&trk=numb&set=true") %>">30</A> |
<B>60</B> |
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(showThumbnails,skuCount,-1,brandValue,sortBy,nutriName,request,null)+"&disp=" + display + "&sortDescending=" + descending + "&trk=numb&set=true") %>">ALL</A>
<%
} else {
%>
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(showThumbnails,30,-1,brandValue,sortBy,nutriName,request,null)+"&disp=" + display + "&sortDescending=" + descending + "&trk=numb&set=true") %>">30</A> |
<A HREF="<%= response.encodeURL("/category.jsp?catId=" + currentFolder + buildOtherParams(showThumbnails,60,-1,brandValue,sortBy,nutriName,request,null)+"&disp=" + display + "&sortDescending=" + descending + "&trk=numb&set=true") %>">60</A> |
<B>ALL</B><%
}
%> per page</td>
</tr></table>
<%@include file="/shared/includes/product/i_pricing_script.jspf"%>
<script>
        var pricing = new Pricing();
        var syncProdIdx = null;
<% if (productCode!=null && prodCatId !=null && syncProdIdx!=-1) { %>
        pricing.setQuantity(<%= syncProdQty %>);
        pricing.setSKU('<%=syncProdSkuCode%>');
        pricing.setCallbackFunction( updatePriceField );
        pricing.setSalesUnit('<%=syncProdSalesUnit%>');
        syncProdIdx = '<%=syncProdIdx%>';
<% } %>
        syncQty('big',syncProdIdx);
</script>

</fd:FDShoppingCart>
