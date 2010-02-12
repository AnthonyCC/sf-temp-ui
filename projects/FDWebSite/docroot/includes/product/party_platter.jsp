<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<%!
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<%
//*** get needed vars from request attributes, they must exist or else we throw jsp error ***/
FDUserI user = (FDUserI) request.getAttribute("user");
ProductModel productNode=(ProductModel)request.getAttribute("productNode");
String cartMode = (String) request.getAttribute("cartMode");
FDCartLineI templateLine = (FDCartLineI) request.getAttribute("templateLine");

ActionResult result = (ActionResult)request.getAttribute("actionResult");
if (result == null || productNode==null || cartMode==null || user==null ){
   throw new JspException(" One or several required request attributes are missing. ");
}
ContentFactory cf = ContentFactory.getInstance();
List skus = new ArrayList();
CategoryModel parentCat = (CategoryModel) productNode.getParentNode();
String errImage = "<img src=\"/media_stat/images/layout/error.gif\" border=\"0\">";
String noErrImage = ""; 
String chefPickIndicator = "<img src=\"/media_stat/images/layout/star11.gif\" border=\"0\">";
String prodPopup = "prod_desc_popup.jsp?";
String prodPopUpSize = "small";

EnumProductLayout prodLayout = productNode.getProductLayout(EnumProductLayout.PARTY_PLATTER);

String prodNameAttribute = JspMethods.getProductNameToUse(parentCat);
int maxWidth=430;
String suLabel="";
Collection pgErrs=((ActionResult)result).getErrors();
StringBuffer errMsgBuff = new StringBuffer();
Map availOptSkuMap = new HashMap();
String introCopyPath=null;
MediaI imgMedia=null;

{
    MediaI mediaI = parentCat.getEditorial();
    if (mediaI != null) {
        introCopyPath = mediaI.getPath();
    }
}

imgMedia = parentCat.getCategoryPhoto();
    
List prodSkus = productNode.getSkus();

boolean hasTemplate = templateLine!=null;
String skuCode = hasTemplate ? templateLine.getSkuCode() : request.getParameter("skuCode");

boolean hasSingleSku = (prodSkus.size() == 1);
SkuModel defaultSku = null;
if (hasSingleSku) {
	defaultSku = (SkuModel)prodSkus.get(0);
} else {
	if (skuCode!=null) {
		// locate the proper sku based on request
		defaultSku = productNode.getSku(skuCode);
	}
	if (defaultSku==null) {
		// no sku from request: default is the one with lowest price
		defaultSku = productNode.getDefaultSku();
	}

}

skus.add(defaultSku);
FDProductInfo  defaultProductInfo = FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
FDProduct defaultProduct = FDCachedFactory.getProduct( defaultProductInfo);
FDVariation[] variations = defaultProduct.getVariations();
if (!hasTemplate) {

	// build a map of characteristic. / req. char.values, now that we know the default sku
	Map tmpConfig = new HashMap();
	for (int i=0; i<variations.length; i++) {
		String variation = variations[i].getName();
		String reqVarOpt = request.getParameter( variation );
		if (reqVarOpt!=null && !"".equals(reqVarOpt)) {
			tmpConfig.put(variation, reqVarOpt);
		}
	}

	String reqQuantity = request.getParameter("quantity");
	double qty = reqQuantity==null ? productNode.getQuantityMinimum() :    Double.parseDouble(reqQuantity);
	FDConfiguration cfg = new FDConfiguration(qty, request.getParameter("salesUnit"), tmpConfig);

	String variantId = request.getParameter("variant");

	templateLine = new FDCartLineModel( new FDSku(defaultProduct), productNode, cfg, variantId, user.getPricingContext().getZoneId());
}
//* check to see if any of the mandatory variations are all unavailable.  (rule: platter unavailable if all comp of a var is unavail)
//* while we're at it, save skucode and productModel for future use.
boolean aVariationUnavailable = false;
for (int cvIdx = 0; cvIdx < variations.length && !aVariationUnavailable; cvIdx++) {
	FDVariationOption[] varOpts = variations[cvIdx].getVariationOptions(); 
	if (variations[cvIdx].isOptional()) continue;

	int unAvailCount=0;
	for (int voIdx = 0; voIdx < varOpts.length;voIdx++) {
		String optSkuCode=varOpts[voIdx].getAttribute(EnumAttributeName.SKUCODE);
		if (optSkuCode==null) {
			unAvailCount++;
			continue;
		}
		try {
			ProductModel pm =cf.getProduct(optSkuCode);
			if (pm!=null ) {
			   if ( (cartMode.equals(CartName.MODIFY_CART) || !pm.isUnavailable() )   && availOptSkuMap.get(optSkuCode)==null) {
				availOptSkuMap.put(optSkuCode,pm);
				} else if (!cartMode.equals(CartName.MODIFY_CART) && pm.isUnavailable() ) {
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

boolean isInCallCenter="CALLCENTER".equals(session.getAttribute(SessionName.APPLICATION));
boolean isAvailable= cartMode.equals(CartName.MODIFY_CART) || (!productNode.isUnavailable()  && !aVariationUnavailable); 
String suffix = cartMode.equals(CartName.MODIFY_CART) || cartMode.equals(CartName.QUICKSHOP)     ? "" : "_0";


double defaultQuantity = templateLine.getQuantity();
String formAction = request.getRequestURI() + "?" + request.getQueryString();

//** Display Error Messages
if (pgErrs.size()>0) {
	List errMsgList = new ArrayList();
	for (Iterator errItr = pgErrs.iterator();errItr.hasNext(); ) {
			errMsgList.add(((ActionError)errItr.next()).getDescription());
	}
	Collections.sort(errMsgList);
	errMsgBuff.append("Please make a selection for the following options:");
	for (int i=0; i<errMsgList.size();i++ ) {
		errMsgBuff.append("<br>&nbsp;&nbsp;&nbsp;");
		errMsgBuff.append((String)errMsgList.get(i));
	}

	String errorMsg=errMsgBuff.toString();
%>
  <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td align="left">
	<%@ include file="/includes/i_error_messages.jspf" %>
	</td></tr>
  </table>
<%  }   
if (EnumProductLayout.MULTI_ITEM_MEAL.equals(prodLayout)) { 
    prodPopup = "/meal_item_detail.jsp?mcatId=" /*
       + request.getParameter("catId") 
       +"&mproductId=" + request.getParameter("productId")
       +"&mskuCode="+defaultSku+"&"; */
       + parentCat 
       +"&mproductId=" +productNode
       +"&mskuCode="+defaultSku+"&";
	prodPopUpSize = "large_long" ;     
%>
  <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
	 <tr><td valign="top" align="center" colspan="3">
	<span class="title18"><%=productNode.getFullName()%></span><br><span class="space2pix"><br></span>
	<span class="title16"><%= currencyFormatter.format(defaultProductInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getDefaultPrice()) %>&nbsp;-&nbsp;<%=productNode.getBlurb()%></span>
	<br><span class="space8pix"><br></span>
	</td></tr>
  	<% 
  	{
		Image _image =productNode.getDetailImage();
		if (_image!=null) {
	    	imgMedia = _image;
		}
  	}
	%>
	<tr>
	<td valign="top"><%	if (imgMedia!=null) {   %> <img src="<%=imgMedia.getPath()%>" border="0" width="<%=imgMedia.getWidth()%>" height="<%=imgMedia.getHeight()%>"> <%  } %></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1"></td>
	<%
	String prodDescr = "";
	{
	    Html mediaI = productNode.getProductDescription();
		if (mediaI!=null) {
			prodDescr = mediaI.getPath();
		}
	}
	%>
	
	<td valign="top" width="90%"><% if (prodDescr!=null && prodDescr.indexOf("blank_file.txt") < 0) { %><fd:IncludeMedia name='<%=prodDescr %>'/><span class="space8pix"><br><br></span><%  }  %>
	<a href="javascript:popup('<%=prodPopup%>catId=<%=parentCat%>&prodId=<%=productNode%>&skuCode=<%=defaultSku%>','<%=prodPopUpSize%>')"><b>Click here for details and cooking tips.</b></a>
	</td>
	</tr>
	<tr><td colspan="3"><span class="space8pix"><br></span></td></tr>
  </table>
<%} else  {  

%>
  <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
	 <tr><td valign="top" align="center">
	<%	if (introCopyPath!=null) {   %><fd:IncludeMedia name='<%= introCopyPath %>'/> <%  }  %>
	<tr><td align="center"><FONT CLASS="title18"><%= currencyFormatter.format(defaultProductInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getDefaultPrice()) %>&nbsp;-&nbsp;<%=productNode.getBlurb()%></font>
	<br><span class="space8pix"><br></span></td></tr>
  </table>
<% }  %>  
<form id="productForm" name="productForm" method="POST" action="<%=formAction%>">
	<input type="hidden" name="productId<%=suffix%>" value='<%= templateLine.getProductName() %>'>
	<input type="hidden" name="catId<%=suffix%>" value='<%= templateLine.getCategoryName() %>'>
<% if (cartMode.equals(CartName.MODIFY_CART)) { %>
	 <input type="hidden" name="cartLine" value="<%= templateLine.getRandomId() %>">
<% } %>
  <input type="hidden" name="salesUnit<%=suffix%>" value="<%=defaultProduct.getSalesUnits()[0].getName()%>">
  <input type="hidden" name="skuCode<%=suffix%>" value="<%=defaultSku.getSkuCode()%>">
<%
int prodCount = 0;
if (isAvailable ) { %>
<%
	int numProducts=0;
	int catIdx = 0;
	for (Iterator sci = parentCat.getSubcategories().iterator(); sci.hasNext(); catIdx++) {
		CategoryModel stepCat = (CategoryModel)sci.next();
		List matCharNames=new ArrayList();
		List varList = new ArrayList();
		List prodList = new ArrayList();
		//List prodList = new ArrayList();
		String _matChar = stepCat.getMaterialCharacteristic();
		if (_matChar!=null) {
			StringTokenizer matCharTkns = new StringTokenizer(_matChar,",");
			for (;matCharTkns.hasMoreTokens(); ) {
				matCharNames.add(matCharTkns.nextToken());
			}
			for (int vIdx = 0; vIdx <variations.length; vIdx++) {
				if (matCharNames.contains(variations[vIdx].getName())) {
					varList.add(variations[vIdx]);
					FDVariationOption[] varOpts = variations[vIdx].getVariationOptions();
					for (int voIdx = 0; voIdx < varOpts.length;voIdx++) {
						String optSkuCode=varOpts[voIdx].getAttribute(EnumAttributeName.SKUCODE);
						if (optSkuCode==null || availOptSkuMap.get(optSkuCode)==null) continue;
							ProductModel pm =(ProductModel)availOptSkuMap.get(optSkuCode);
							if (pm!=null && !prodList.contains(pm)) {
								prodList.add(pm);
						}
					}

				}
			}
		} 
		List featuredProdIds = stepCat.getFeaturedProductIds();
		
		introCopyPath=null;
		imgMedia=null;
		{
		    MediaI mediaI = stepCat.getEditorial();
		    if (mediaI!=null) {
				introCopyPath = mediaI.getPath();
		    }
		}

		imgMedia = stepCat.getCategoryPhoto();

		//show category image and into copy if there are characteristics names
		if (matCharNames.size()>0) { 	%>
		   <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
			<tr><td  align="left">
		<%	if (imgMedia!=null) {   %> <img src="<%=imgMedia.getPath()%>" border="0" width="<%=imgMedia.getWidth()%>" height="<%=imgMedia.getHeight()%>"> <%  }  %>
		<%	if (introCopyPath!=null) { %><br><fd:IncludeMedia name='<%= introCopyPath %>'/> <%  }  %>
			<br><br></td></tr>
		   </table>
	<%  }  %>
	<%	numProducts =prodList.size(); 
		boolean tagIsOpen = false;
		boolean isChefsPick = false;
		if (matCharNames.size() > 0) {
			for (int prodIdx = 0; prodIdx < numProducts ; prodIdx++) {
				ProductModel oneProd =(ProductModel)prodList.get(prodIdx);
				isChefsPick =featuredProdIds.contains(oneProd.getContentName());
				Image prodImage =oneProd.getCategoryImage();
				String imgName = "prodImg_"+catIdx;
				String imgRollOver = "";
				if (prodIdx==0) {  
					if (prodCount==0) prodCount=1;
					tagIsOpen=true;    %>
					<table  width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
					<tr>
						<td><img src="/media_stat/images/layout/clear.gif" width="95" height="1"></td>
						<td><img src="/media_stat/images/layout/clear.gif" width="15" height="1"></td>
						<td><img src="/media_stat/images/layout/clear.gif" width="<%=maxWidth-115%>" height="1"></td>
					</tr>
					<tr><td valign="top" align="left"><img name="<%=imgName %>" src="<%=(prodImage==null ? "/media_stat/images/layout/clear.gif" : prodImage.getPath())%>"></td>
					<td valign="top"><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
					<td valign="top">
		<%		} 
				if (prodImage!=null) {
					StringBuffer sbRollover=new StringBuffer();
					sbRollover.append("onMouseover='");
					sbRollover.append("swapImage(\""+imgName+"\",\""+prodImage.getPath()+"\"");
					sbRollover.append(")'");
					imgRollOver = sbRollover.toString();
					sbRollover = null;
				}
		%>
		   <% if (isChefsPick) {%><%=chefPickIndicator%>&nbsp;
		   <% } else {%><img src="/media_stat/images/layout/clear.gif" width="11" height="11">&nbsp; <% }  %>
		       <a href="javascript:popup('<%=prodPopup%>catId=<%=oneProd.getParentNode()%>&prodId=<%=oneProd%>&skuCode=<%=oneProd.getDefaultSku()%>','<%=prodPopUpSize%>')" <%=imgRollOver%>><%=JspMethods.getDisplayName(oneProd,prodNameAttribute)%></a>
				<br>
		<%	 }  %>
			  <br>
		<%
				for (int cvIdx = 0; cvIdx < varList.size(); cvIdx++) {
					FDVariation variation = (FDVariation)varList.get(cvIdx); 
					FDVariationOption[] varOpts = variation.getVariationOptions(); 
					if (!variation.isOptional()) { %>
					  <%= result.hasError(variation.getName()+suffix) ? errImage : noErrImage %>
		<%  		} 
					if ( varOpts.length>1) { %>
						<img src="/media_stat/images/layout/clear.gif" width="11" height="11">&nbsp;
						<select name="<%=variation.getName()+suffix%>"><option value=""><%=variation.getDescription()%></option>
		<%				
						for (int voIdx = 0; voIdx < varOpts.length;voIdx++) {
							String optSkuCode=varOpts[voIdx].getAttribute(EnumAttributeName.SKUCODE);
							String selected= (hasTemplate && varOpts[voIdx].getName().equals(templateLine.getOptions().get(variation.getName()+suffix)) )
							  ? "selected"
							  : varOpts[voIdx].getName().equals(request.getParameter(variation.getName()+suffix)) 
								? "selected" 
								: "";
							if (optSkuCode==null  || availOptSkuMap.get(optSkuCode)==null) continue;	%> 
							<option value="<%=varOpts[voIdx].getName()%>" <%=selected%>><%=varOpts[voIdx].getDescription()%></option> <%
						}
						%> </select><br> <%
					} else { %>
							<input type="hidden" name="<%=variation.getName()+suffix%>" value="<%=varOpts[0].getName()%>">
<%					}
					
				}

	} else {
		//if not modifying then go get the optional stuff, which is in a subfolder of this products parent
	    boolean openTable = true;
		EnumLayoutType layoutType=  stepCat.getLayout(EnumLayoutType.MULTI_ITEM_MEAL_OPTION_HORZ);
	    int layout = layoutType.getId();
		if (!cartMode.equals(CartName.MODIFY_CART)  && !cartMode.equals(CartName.QUICKSHOP) && stepCat.getProducts().size()>0 ) {
		   boolean headingDone=false;		   int optCnt=0;
		   FDProduct fdProd = null;
		   String salesUnitDescription = "NA";
		   String salesUnitName = "NA";
		   FDSalesUnit[] salesUnits;    %>
		  <logic:iterate id='optProd' collection="<%=stepCat.getProducts()%>" type="com.freshdirect.fdstore.content.ProductModel" indexId='optIdx'>
	<%	
			   if (optProd.isDiscontinued() || optProd.isUnavailable()) continue;
			   SkuModel dfltSku = optProd.getDefaultSku();
			   if (dfltSku==null) continue;
			   skus.add(dfltSku);    %>
		<fd:FDProductInfo id="productInfo" skuCode="<%=dfltSku.getSkuCode()%>">
	<%
			   try {
					fdProd = FDCachedFactory.getProduct(productInfo);
					salesUnits = fdProd.getSalesUnits();
					if (salesUnits.length>0) {
							salesUnitDescription = salesUnits[0].getDescription();
							salesUnitName = salesUnits[0].getName();
					}
				 } catch (FDSkuNotFoundException fdsnf){
					JspLogger.PRODUCT.warn("Party Platter Page: catching FDSkuNotFoundException and Continuing:\n FDProductInfo:="+productInfo+"\nException message:= "+fdsnf.getMessage());
					throw new JspException("FDProduct not found for available product",fdsnf);
				}    %>
		</fd:FDProductInfo>
	<%
			   DisplayObject dispObj = JspMethods.loadLayoutDisplayStrings(response, optProd.getParentNode().getContentName(),optProd,"full",true,false,"",true);
				if (!headingDone) {
				   headingDone = true;  %>
				   <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
					<tr><td  align="left">
	<%	  			if (imgMedia!=null) {   %> <img src="<%=imgMedia.getPath()%>" border="0" width="<%=imgMedia.getWidth()%>" height="<%=imgMedia.getHeight()%>"> <%  }  %>
	<%	  			if (introCopyPath!=null) { %><br><bt><fd:IncludeMedia name='<%= introCopyPath %>'/> <%  }  %>
					<br><br></td></tr>
				   </table>
				   <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
					<tr>
	<%          }  
			    if (EnumLayoutType.MULTI_ITEM_MEAL_OPTION_HORZ.getId() == layout) {  // paint the optional category using the Horizontal style%>
				<td valign="top" align="center" width="140"><img src="/media_stat/images/layout/clear.gif" width="140" height="1"><br>
					<table width="140" border="0" cellspacing="0" cellpadding="0" align="center">
					  <tr>
						<td colspan="2" align="center"><a href="javascript:popup('prod_desc_popup.jsp?catId=<%=optProd.getParentNode()%>&prodId=<%=optProd%>','small')"><img src="<%=dispObj.getImagePath()%>" border="0" width="<%=dispObj.getImageWidth()%>" height="<%=dispObj.getImageHeight()%>"></a></td>
					  </tr>
					  <tr>
						<td colspan="2" align="center"><a href="javascript:popup('prod_desc_popup.jsp?catId=<%=optProd.getParentNode()%>&prodId=<%=optProd%>','small')"><%=dispObj.getItemName()%></a><br><%=dispObj.getPrice()%>
						   <br><img src="/media_stat/images/layout/clear.gif" width="1" height="1">
						</td>
					  </tr>
					  <tr>
						<td colspan="2" align="center">
						   <table border="0" cellspacing="0" cellpadding="0" align="center"><tr> 
							<td valign="bottom" align="right">
							  <input type="hidden" name="salesUnit_<%= prodCount %>" value="<%= salesUnitName %>">
							  <input type="hidden" name="skuCode_<%= prodCount %>" value="<%= dfltSku.getSkuCode() %>">
							  <input type="hidden" name="catId_<%= prodCount %>" value="<%= optProd.getParentNode() %>">
							  <input type="hidden" name="productId_<%= prodCount %>" value="<%= optProd %>">
							  <input name ="quantity_<%=prodCount%>" value="<%=(request.getParameter("quantity_"+prodCount)==null ?"" : request.getParameter("quantity_"+prodCount))%>" type="text" size="3" onChange="chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',0,<%= optProd.getQuantityMinimum() %>,<%= user.getQuantityMaximum(optProd) %>)">
							</td>
							<td valign="bottom" align="left">
							  <a HREF="javascript:chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',<%= optProd.getQuantityIncrement() %>,0,<%= user.getQuantityMaximum(optProd) %>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></a><br>
							  <a HREF="javascript:chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',-<%= optProd.getQuantityIncrement() %>,0,<%= user.getQuantityMaximum(optProd) %>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity"></a>
							  <ximg src="/media_stat/images/layout/clear.gif" width="10" height="1">
							</td>
						   </tr>
						  </table>
						</td>
					  </tr>
					  <tr><td colspan="2"><br></td></tr>
					</table>
				</td>

	<% 		   if (prodCount % 3==0 && prodCount!=0) { %>
				   </tr><tr>
	<%         }  
			   prodCount++;  
	      } else { // paint the optional category using the Vertical style
				String imgRollOver = "";
				String imgName = "optProdImg";
	 			if (openTable) { // first item, paint main table 
				openTable = false;
	%>
		<table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
			<tr valign="top">
			<td width="115">
			<a href="javascript:popup('prod_desc_popup.jsp?catId=<%=optProd.getParentNode()%>&prodId=<%=optProd%>','small')"><img src="<%=dispObj.getImagePath()%>" border="0" name="<%=imgName%>"></a></td>
			<td width="<%=maxWidth-115%>">
				<table width="<%=maxWidth-115%>" cellpadding="0" cellspacing="0" border="0" align="center">
	<% 			} 
		
				StringBuffer sbRollover=new StringBuffer();
				sbRollover.append("onMouseover='");
				sbRollover.append("swapImage(\""+imgName+"\",\""+dispObj.getImagePath()+"\"");
				sbRollover.append(")'");
				imgRollOver = sbRollover.toString();
				sbRollover = null;
	%>
			<tr valign="top">
				<td><input type="hidden" name="salesUnit_<%= prodCount %>" value="<%= salesUnitName %>">
							  <input type="hidden" name="skuCode_<%= prodCount %>" value="<%= dfltSku.getSkuCode() %>">
							  <input type="hidden" name="catId_<%= prodCount %>" value="<%= optProd.getParentNode() %>">
							  <input type="hidden" name="productId_<%= prodCount %>" value="<%= optProd %>">
							  <input name ="quantity_<%=prodCount%>" value="<%=(request.getParameter("quantity_"+prodCount)==null ?"" : request.getParameter("quantity_"+prodCount))%>" type="text" size="3" onChange="chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',0,<%= optProd.getQuantityMinimum() %>,<%= user.getQuantityMaximum(optProd) %>)"></td>
				
				<td width="15"><a HREF="javascript:chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',<%= optProd.getQuantityIncrement() %>,0,<%= user.getQuantityMaximum(optProd) %>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></a><br>
							  <a HREF="javascript:chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',-<%= optProd.getQuantityIncrement() %>,0,<%= user.getQuantityMaximum(optProd) %>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity"></a></td>
				
				<td width="83%"><a href="javascript:popup('prod_desc_popup.jsp?catId=<%=optProd.getParentNode()%>&prodId=<%=optProd%>','small')" <%=imgRollOver%>><%=dispObj.getItemName()%></a><br><%=dispObj.getPrice()%>
						   <br><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			</tr>
			<tr><td colspan="3"><span class="space2pix"><br></span></td></tr>
		
	<%	prodCount++;  %>

	<%
		} //end optional layout %>
	 </logic:iterate>  
<%      if (prodCount>1) { 
	        if (EnumLayoutType.MULTI_ITEM_MEAL_OPTION_HORZ.getId() != layout) { %>
			  </table></td>
<%          }   %>            
			  </tr>
			  </table>
	<%	 }
	    }
	} %>
		<br>
	<%   if (tagIsOpen) { %>
			</td></tr> 
		  </table>
	<%
		} 
	 } 
        List catMidMedias = parentCat.getMiddleMedia();
        if (catMidMedias != null && catMidMedias.size()>0) {  %>
            <FONT CLASS="space4pix"><br><br></FONT>
            <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=maxWidth%>">
               <TR VALIGN="TOP">
                  <TD align="center">
            <BR>
            <IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="<%=maxWidth%>" HEIGHT="1" BORDER="0"><BR>
              <logic:iterate id='mediaRef' indexId='indexNo' collection="<%=catMidMedias%>" type="com.freshdirect.fdstore.content.MediaModel">
    <%          if (((Html)mediaRef).getPath()!=null  && ((Html)mediaRef).getPath().toLowerCase().indexOf("blank.")==-1) { 
                               if(indexNo.intValue()!=0){ %>
                                     <img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
                                     <IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="295" HEIGHT="1" BORDER="0"><BR>
                                     <img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
                               <%}%>
                            <fd:IncludeMedia name='<%= ((Html)mediaRef).getPath() %>' />

    <%          } %>
              </logic:iterate>
              </td></tr></table>
<%      }

	 if (prodCount > 0) {  %>
	<table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
		<tr><td><BR>
            <IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="<%=maxWidth%>" HEIGHT="1" BORDER="0"><BR><br>
        </td></tr>
    </table>
	<table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
		<tr>
		  <td width="93">
			<b>Quantity</b>&nbsp;<input type="text" CLASS="text11" size="3" name="quantity<%=suffix%>" value="<%= quantityFormatter.format(defaultQuantity) %>" onChange="chgNamedQty(pricing,'quantity<%=suffix%>',0,<%= productNode.getQuantityIncrement() %>,<%= productNode.getQuantityMinimum() %>,<%= user.getQuantityMaximum(productNode) %>);" onccur="pricing.setQuantity(this.value);">
		  </td>
		  <td align="left" WIDTH="14" VALIGN="BOTTOM">
			  <a HREF="javascript:chgNamedQty(pricing,'quantity<%=suffix%>',<%= productNode.getQuantityIncrement() %>,<%= productNode.getQuantityMinimum() %>,<%= user.getQuantityMaximum(productNode) %>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></a><br>
			  <a HREF="javascript:chgNamedQty(pricing,'quantity<%=suffix%>',-<%=productNode.getQuantityIncrement() %>,<%= productNode.getQuantityMinimum() %>,<%= user.getQuantityMaximum(productNode) %>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity"></a>
		  </td>
		  <td width="<%=maxWidth-104%>*">&nbsp;&nbsp;<b>Price</b>&nbsp;<INPUT class="text11bold" TYPE="text" NAME="price" SIZE="6" onChange="" onFocus="blur()" value=""></td>
		</tr>
		<tr>
		 <td colspan="3">

	<% if(CartName.MODIFY_CART.equals(cartMode) ) {
			String referer = request.getParameter("referer");
			if (referer==null) referer = request.getHeader("Referer");
			if (referer==null) referer = "/view_cart.jsp";			%>
			<input type="image" name="save_changes" src="/media_stat/images/buttons/save_changes_cart.gif" width="109" height="20" border="0" alt="SAVE CHANGES" VSPACE="2"><BR>
			<input type="image" name="remove_from_cart" src="/media_stat/images/buttons/remove_item.gif" width="109" height="20" border="0" alt="REMOVE ITEM" VSPACE="2"><BR>
			<input type="hidden" name="referer" value="<%= referer %>">
			<a href="<%=referer%>"><img src="/media_stat/images/buttons/no_change.gif" width="109" height="20" border="0" alt="NO CHANGE" VSPACE="2"></a><BR>
	<%	} else {   %>
		 <input type="image" name="addMultipleToCart" src="/media_stat/images/buttons/add_to_cart.gif" width="93" height="20" hspace="4" vspace="4" border="0" alt="ADD SELECTED ITEMS TO CART">
	<%  } 

		if (cartMode.equals(CartName.QUICKSHOP)) {
	%>
	        <fd:QuickShopController id="quickCart">
	        <fd:GetBackToListLink id='backToList' quickCart='<%= quickCart %>' deptId='<%=request.getParameter("qsDeptId")%>'>
		<a href="<%=backToList.toString()%>"><img src="/media_stat/images/buttons/back_to_list.gif" width="99" height="21" border="0" alt="Back to List" vspace="2"></a><br>
		</fd:GetBackToListLink>
		</fd:QuickShopController>
        <%
		}   %>
		<%@ include file="/shared/includes/product/i_minmax_note.jspf" %>
		<%@ include file="/includes/product/i_delivery_note.jspf" %>
		<%@ include file="/includes/product/i_cancellation_note.jspf" %>
		</td></tr><tr></tr><tr>
<fd:CCLCheck>
                    <td colspan="9" align="left"> 
<font class="text11gbold"><a href="/unsupported.jsp" 
	      onclick="return CCL.save_items('productForm',this,'action=CCL:AddMultipleToList&source=ccl_actual_selection')">Save selected to shopping list</a></font> <fd:CCLNew/>
    <br/>
    <br/>
    </td>
</fd:CCLCheck>
        </tr>
	</table>
	<br> 
	<%@ include file="/shared/includes/product/i_pricing_script.jspf" %>
		<script language="javascript">
		<!--
			var pricingArray = new Array();
			var pricing = new Pricing();
			pricingArray[0]=pricing;
	<%      for (int pidx=1; pidx<prodCount; pidx++) { %>
			var pricing_<%=pidx%> = makePricing(document.productForm['skuCode_<%=pidx%>'].value,document.productForm['salesUnit_<%=pidx%>'].value,document.productForm['quantity_<%=pidx%>'].value);
			pricingArray[<%=pidx%>]=pricing_<%=pidx%>;
	<%      }  %>
			var currentSelection = new Array();

			function showDetail(formElement){
			if (formElement.value == "details") {
				popup('category_popup.jsp?catId=hmr_fon_liq&title=Fondue Styles','large');
				formElement.selectedIndex = 0;;
			}
			return true;
		}

			function chgNamedQty(pObject,qtyFldName,delta,min,max) {
					var qty = parseFloat(document.productForm[qtyFldName].value)
					if (isNaN(qty)) qty=0;
					qty = qty + delta;

					if (qty < 0 || (qty < min && delta < 0) ){ 
							qty=min;
					} else if(qty < min && delta >=0) {
							qty=min;
					} else if (qty > max) {
							qty=max;
					}

					if (qty<=0) {
							document.productForm[qtyFldName].value='';
					} else {
							document.productForm[qtyFldName].value = qty;
					}
					pObject.setQuantity(qty);
			}

	<%-- Set up the pricing callback, now that the field exists on the page --%>

		function updatePriceField() {
				var totPrice=0.0;
			for (var x=0; x< pricingArray.length;x++) {
					totPrice+= pricingArray[x].price;
				}
				document.productForm.price.value = totPrice==0 ? "" : "$" + currencyFormat(totPrice);
		}

			function makePricing(skuCode,su,qty) {
				var po = new Pricing();
				po.setSKU(skuCode);
				po.setSalesUnit(su);
				po.setCallbackFunction(updatePriceField );
				po.setQuantity(qty);
				return po;
			}

		pricing.setSKU('<%=defaultSku.getSkuCode()%>');
		pricing.setSalesUnit('<%=defaultProduct.getSalesUnits()[0].getName()%>');
		pricing.setCallbackFunction( updatePriceField );
		pricing.setQuantity(<%=defaultQuantity%>);
	 // -->
	</script>
			<input name="itemCount" type="hidden" value="<%=prodCount%>">
	</form>
	<% }
} else {  %>
<table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
    <tr>
      <td width="100%">
	<div align="center"><font class="text12" color="#999999">
		<b>We're sorry! This item is temporarily unavailable.</b>
	</font></div>
	</td></tr></table>
<% } 

	List catBottomMedias =  parentCat.getBottomMedia();
	if (catBottomMedias.size()>0  && !CartName.MODIFY_CART.equals(cartMode) ) {
    %>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=maxWidth%>">
       <TR VALIGN="TOP">
          <TD align="center">
			<BR>
			<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="<%=maxWidth%>" HEIGHT="1" BORDER="0"><BR>
			<FONT CLASS="space4pix"><br><br></FONT>
       </TD>
       <TR><TD align="center">
              <logic:iterate id='mediaRef' indexId='indexNo' collection="<%=catBottomMedias%>" type="com.freshdirect.fdstore.content.MediaModel">
    <%          if (((Html)mediaRef).getPath()!=null) { %>
                               <% if(indexNo.intValue()!=0){ %>
                                     <img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
                                     <IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="295" HEIGHT="1" BORDER="0"><BR>
                                     <img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
                               <%}%>
                            <fd:IncludeMedia name='<%= ((Html)mediaRef).getPath() %>' />

    <%          } %>
              </logic:iterate>
        </TD>
    </TR>
    </TABLE>
    <% } %>
