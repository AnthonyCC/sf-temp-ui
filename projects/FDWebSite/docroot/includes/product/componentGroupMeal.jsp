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
<%@ page import='com.freshdirect.fdstore.lists.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
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
%>
<%@ include file="/shared/includes/product/i_product_quality_note.jspf" %>
<%
ContentFactory cf = ContentFactory.getInstance();
List skus = new ArrayList();
CategoryModel parentCat = (CategoryModel) productNode.getParentNode();
String errImage = "<img src=\"/media_stat/images/layout/error.gif\" border=\"0\">";
String noErrImage = ""; 
String chefPickIndicator = "<img src=\"/media_stat/images/layout/star11.gif\" border=\"0\">";
String prodPopup = "prod_desc_popup.jsp?";
String prodPopUpSize = "small";

EnumProductLayout prodLayout = EnumProductLayout.getLayoutType(productNode.getAttribute("PRODUCT_LAYOUT",EnumProductLayout.PARTY_PLATTER.getId()));
boolean paintOptionalVertical= true;
String prodNameAttribute = JspMethods.getProductNameToUse(parentCat);
int maxWidth=430;
String suLabel="";
Collection pgErrs=((ActionResult)result).getErrors();
StringBuffer errMsgBuff = new StringBuffer();
Attribute attrib = parentCat.getAttribute("EDITORIAL");
String packageDescription = productNode.getAttribute("PACKAGE_DESCRIPTION","");
MediaI imgMedia=null;
   
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

List componentGroups = (List)productNode.getAttribute("COMPONENT_GROUPS",Collections.EMPTY_LIST);
Map variations = new HashMap();
try {
	for(int i=0; i<componentGroups.size(); i++) {
	 variations.putAll(((ComponentGroupModel)componentGroups.get(i)).getVariationOptions() );
	}
} catch (FDResourceException fde) {
         //* EAT EM UP, mmmmmmm  
}

skus.add(defaultSku);
FDProductInfo  defaultProductInfo = FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
FDProduct defaultProduct = FDCachedFactory.getProduct( defaultProductInfo);
FDVariation[] fdVariations = defaultProduct.getVariations();
Map fdVarOptDesc = new HashMap();
for (int varIdx = 0; varIdx<fdVariations.length;varIdx++) {
	fdVarOptDesc.put(fdVariations[varIdx].getName(),fdVariations[varIdx].getDescription());
}

if (!hasTemplate) {

	// build a map of characteristic. / req. char.values, now that we know the default sku
	Map tmpConfig = new HashMap();
	for (Iterator varItr= variations.keySet().iterator(); varItr.hasNext();) {
		String variation = (String) varItr.next();;
		String reqVarOpt = request.getParameter( variation );
		if (reqVarOpt!=null && !"".equals(reqVarOpt)) {
			tmpConfig.put(variation, reqVarOpt);
		}
	}

	String reqQuantity = request.getParameter("quantity");
	double qty = reqQuantity==null ? productNode.getQuantityMinimum() :    Double.parseDouble(reqQuantity);
	FDConfiguration cfg = new FDConfiguration(qty, request.getParameter("salesUnit"), tmpConfig);

	templateLine = new FDCartLineModel( new FDSku(defaultProduct), productNode.getProductRef(), cfg);
}


boolean isInCallCenter="CALLCENTER".equals(session.getAttribute(SessionName.APPLICATION));
boolean isAvailable= cartMode.equals(CartName.MODIFY_CART) || CartName.MODIFY_LIST.equals(cartMode) || (!productNode.isUnavailable() ); 
String suffix = CartName.MODIFY_CART.equals(cartMode) || 
                CartName.QUICKSHOP.equals(cartMode) || 
		CartName.MODIFY_LIST.equals(cartMode) ||
		CartName.ACCEPT_ALTERNATIVE.equals(cartMode) ? "" : "_0";


double defaultQuantity = templateLine.getQuantity();

    StringBuffer formAction = new StringBuffer(request.getRequestURI()).append('?');
    
    if (CartName.QUICKSHOP.equals(cartMode) || CartName.ADD_TO_CART.equals(cartMode)) {
        formAction
	    .append("catId=").append(productNode.getParentNode())
	    .append("&productId=").append(productNode);

	           
        String oid = request.getParameter("orderId=");
	if (oid != null) formAction.append("&orderId=").append(oid);

	String item = request.getParameter("item");
	if (item != null) formAction.append("&item=").append(item);

    } else if (CartName.MODIFY_LIST.equals(cartMode)) {
	formAction.append(CclUtils.CC_LIST_ID).append('=').append(request.getAttribute(CclUtils.CC_LIST_ID));
    } else {
	formAction.append(request.getQueryString());
    }
// OLD -- String formAction = request.getRequestURI() + "?" + request.getQueryString();

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
    prodPopup = "/cg_meal_item_detail.jsp?mcatId=" 
       + parentCat  + "&mproductId=" +productNode  + "&mskuCode=" + defaultSku + "&";
	prodPopUpSize = "large_long" ;     
	String prodDescr = "";
	attrib = productNode.getAttribute("PROD_DESCR"); 
	if (attrib!=null) {
		MediaI mediaI = (MediaI)attrib.getValue();
		prodDescr = mediaI.getPath();
	} 
	attrib =productNode.getAttribute("PROD_IMAGE_DETAIL");

	if (attrib!=null) {
    		imgMedia = (MediaI)attrib.getValue();
	} 

	String prodSubtitle=productNode.getSubtitle();
%>

<%-- start display of product --%>
  <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td valign="top" align="center" colspan="3">
	  <span class="title18"><%=productNode.getFullName()%></span><br>
  <%      if (prodSubtitle!=null && !"".equals(prodSubtitle)) { %>
	    <span class="title13"><%=productNode.getSubtitle()%></span><br>
  <%      } %>  
	    <span class="space2pix"><br></span>
	  <span class="title16"><%= currencyFormatter.format(defaultProductInfo.getDefaultPrice()) %>&nbsp;-&nbsp;<%=productNode.getBlurb()%></span>
	  <br><span class="space8pix"><br></span>
	</td></tr>
	<tr>
	  <td valign="top"><%	if (imgMedia!=null) {   %> <img src="<%=imgMedia.getPath()%>" border="0" width="<%=imgMedia.getWidth()%>" height="<%=imgMedia.getHeight()%>"> <%  } %></td>
	  <td><img src="/media_stat/images/layout/clear.gif" width="20" height="1"></td>
	  <td valign="top" width="90%"><% if (prodDescr!=null && prodDescr.indexOf("blank_file.txt") < 0) { %><fd:IncludeMedia name='<%=prodDescr %>'/><span class="space8pix"><br><br></span><%  }  %>
	  <a href="javascript:popup('<%=prodPopup%>catId=<%=parentCat%>&prodId=<%=productNode%>&skuCode=<%=defaultSku%>','<%=prodPopUpSize%>')"><b>Click here for details.</b></a>
	</td>
	</tr>
	<tr><td colspan="3"><span class="space8pix"><br></span></td></tr>
  </table>

<form id="productForm" name="productForm" method="POST" action="<%=formAction%>">
	<input type="hidden" name="productId<%=suffix%>" value='<%= templateLine.getProductName() %>'>
	<input type="hidden" name="catId<%=suffix%>" value='<%= templateLine.getCategoryName() %>'>
<% if (cartMode.equals(CartName.MODIFY_CART)) { %>
	 <input type="hidden" name="cartLine" value="<%= templateLine.getRandomId() %>">
<% } %>
  <input type="hidden" name="salesUnit<%=suffix%>" value="<%=defaultProduct.getSalesUnits()[0].getName()%>">
  <input type="hidden" name="skuCode<%=suffix%>" value="<%=defaultSku.getSkuCode()%>">
<%
int prodCount = 1;
if (isAvailable ) { 
	int numProducts=0;
	int compGrpIdx = 0;
	for (Iterator cgItr = componentGroups.iterator(); cgItr.hasNext(); compGrpIdx++) {
		ComponentGroupModel compGroup = (ComponentGroupModel) cgItr.next();
		
		//  dont show this component group stuff if it is for popup only 
		if (compGroup.isShowInPopupOnly()) continue;
		
		List matCharNames=compGroup.getCharacteristicNames();
		List prodList = new ArrayList();
		for (Iterator optionsItr=matCharNames.iterator(); optionsItr.hasNext();) {
			String matCharName = (String)optionsItr.next();
			FDVariationOption[] varOpts = (FDVariationOption[]) variations.get(matCharName);
			if (varOpts==null) continue;
			for (int optIdx=0;optIdx<varOpts.length;optIdx++ ){
				String optSkuCode=varOpts[optIdx].getAttribute(EnumAttributeName.SKUCODE);
				
				//somtimes skucode attrib in erps may be missing..so handle it, so we dont get SkuNotFoundException
				if (optSkuCode==null || "".equals(optSkuCode.trim())) {
				   continue;
				}
				
				ProductModel pm =(ProductModel)cf.getProduct(optSkuCode);
				if (pm==null) {
					continue;
				}
				SkuModel sku = pm.getSku(optSkuCode);
				
				
				if (sku!=null && !prodList.contains(pm) && !sku.isUnavailable()) {
					prodList.add(pm);
				}
			}
		}
		
		attrib = compGroup.getAttribute("CHEFS_PICKS");
		List featuredProdIds = new ArrayList();
		if (attrib!=null) {
			for (Iterator itr=((List)attrib.getValue()).iterator(); itr.hasNext();) {
				ProductRef cr = (ProductRef) itr.next();
				if (!featuredProdIds.contains(cr.getRefName2())) {
					featuredProdIds.add(cr.getRefName2());
				}
			}
		} 
		String mediaPath=null;
		imgMedia=null;
		attrib = compGroup.getAttribute("EDITORIAL"); 
		if (attrib!=null) {
			MediaI mediaI = (MediaI)attrib.getValue();
			mediaPath = mediaI.getPath();
		}

		attrib = compGroup.getAttribute("HEADER_IMAGE");
		if (attrib!=null) {
			imgMedia = (MediaI)attrib.getValue();
		}  

		//show ComponentGroup  Header Image and Editorial if there are characteristics names
		if (matCharNames.size()>0) { 	%>
		   <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
			<tr><td  align="left"><FONT CLASS="space4pix"><BR><BR></FONT>
		<%	if (imgMedia!=null) {   %> <img src="<%=imgMedia.getPath()%>" border="0" width="<%=imgMedia.getWidth()%>" height="<%=imgMedia.getHeight()%>"> <%  }  %>
		<%	if (mediaPath!=null) { %><br><fd:IncludeMedia name='<%= mediaPath %>'/> <%  }  %>
			<BR></td></tr>
		   </table>
	<%      }  
	  	numProducts =prodList.size(); 
		boolean tagIsOpen = false;
		boolean isChefsPick = false;
		if (matCharNames.size() > 0) {
			boolean showOptions = compGroup.isShowOptions();
			Attribute atb = compGroup.getAttribute("OPTIONS_DROPDOWN_STYLE");
			boolean paintDropdownVertical = compGroup.isOptionsDropDownVertical();
			ProductModel oneProd =(ProductModel)prodList.get(0);
			Image prodImage =oneProd.getCategoryImage();
			String imgName = "prodImg_"+compGrpIdx;
			String imgRollOver = "";   %>
			<table  width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
			  <tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="95" height="6"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="15" height="1"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="<%=maxWidth-115%>" height="1"></td>
			  </tr>
   			  <tr>
<%			if (paintDropdownVertical) { %>
				  <td valign="top" align="left"><img name="<%=imgName %>" src="<%=(prodImage==null ? "/media_stat/images/layout/clear.gif" : prodImage.getPath())%>"></td>
				  <td valign="top"><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
				  <td valign="top">
                        <% } else { %>
				  <td colspan="3" valign="top">
<%                      }
 			if (showOptions) {
			// paint the list of items that user will scroll over
				for (int prodIdx = 0; prodIdx < numProducts && showOptions ; prodIdx++) {
					oneProd =(ProductModel)prodList.get(prodIdx);
					isChefsPick = featuredProdIds.contains(oneProd.getContentName());
					prodImage =oneProd.getCategoryImage();
					imgName = "prodImg_"+compGrpIdx;
					imgRollOver = "";
					if (paintDropdownVertical && prodImage!=null) {
						StringBuffer sbRollover=new StringBuffer();
						sbRollover.append("onMouseover='");
						sbRollover.append("swapImage(\""+imgName+"\",\""+prodImage.getPath()+"\"");
						sbRollover.append(")'");
						imgRollOver = sbRollover.toString();
						sbRollover = null;
					}    			%>
			   <%          if (isChefsPick) {%><%=chefPickIndicator%>&nbsp;
			   <%          } else {%><img src="/media_stat/images/layout/clear.gif" width="14" height="11">&nbsp;<% }  %>
				      <a href="javascript:popup('<%=prodPopup%>catId=<%=oneProd.getParentNode()%>&prodId=<%=oneProd%>&skuCode=<%=oneProd.getDefaultSku()%>','<%=prodPopUpSize%>')" <%=imgRollOver%>><%=JspMethods.getDisplayName(oneProd,prodNameAttribute)%></a>
				     <br>
		<% 	     } %> <br>
<%		
			} 
			// paint the SELECT tag and its options
				int selectTagCnt=0;
				for (Iterator mcnItr = matCharNames.iterator(); mcnItr.hasNext();) {
					String mcName = (String) mcnItr.next();
					FDVariationOption[] varOpts = (FDVariationOption[])variations.get(mcName);  %>
					<%= result.hasError(mcName+suffix) ? errImage : noErrImage %>

					<%
					if ( varOpts!=null && varOpts.length>1) {
						if (paintDropdownVertical) { %>
						  <img src="/media_stat/images/layout/clear.gif" width="14" height="11">&nbsp;
<%                                              }  %>						
						<select name="<%=mcName+suffix%>" CLASS="text11" <%=(!paintDropdownVertical ?"style=\"width: 190px\"" :"")%>><option value=""><%=(String)fdVarOptDesc.get(mcName)%></option>
						<%				
						for (int voIdx = 0; voIdx < varOpts.length; voIdx++) {
							String optSkuCode=varOpts[voIdx].getAttribute(EnumAttributeName.SKUCODE);
				                        if (optSkuCode==null || "".equals(optSkuCode.trim()) ) continue;
				                        
							ProductModel pm = (ProductModel)cf.getProduct(optSkuCode);
							if (pm==null) continue;
							SkuModel sku = pm.getSku(optSkuCode);
							if (sku==null || sku.isUnavailable()) continue;
							
							String selected= (varOpts[voIdx].getName().equals(templateLine.getOptions().get(mcName)) )
							  ? "selected"
							  : varOpts[voIdx].getName().equals(request.getParameter(mcName+suffix)) 
								? "selected" 
								: "";
							%> 
							<option value="<%=varOpts[voIdx].getName()%>" <%=selected%>>&nbsp;<%=varOpts[voIdx].getDescription()%></option> <%
						}
						%> </select>
						<% if (paintDropdownVertical || selectTagCnt %2!=0  || !mcnItr.hasNext() ) { %>
						     <br> <font class="space4pix"><br></font><%
						   } else { %>
						    <img src="/media_stat/images/layout/clear.gif" width="2" height="1">
						<% } 
						selectTagCnt++;
					} else if (varOpts!=null){ %>
							<input type="hidden" name="<%=mcName+suffix%>" value="<%=varOpts[0].getName()%>">
<%					}
					
				} %>
				</td></tr></table>
<%	        } else {
		//if not modifying then go get the optional stuff, which is in a subfolder of this products parent
	    	 boolean openTable = true;
	    	 List optionalProducts = compGroup.getAvailableOptionalProducts(); //compGroup.getOptionalProducts(); 
	    	 int layout = EnumLayoutType.MULTI_ITEM_MEAL_OPTION_HORZ.getId(); //stepCat.getAttribute("LAYOUT", EnumLayoutType.MULTI_ITEM_MEAL_OPTION_HORZ.getId());
	    	 if (!cartMode.equals(CartName.MODIFY_CART)  && 
		     !CartName.MODIFY_LIST.equals(cartMode) && 
		     !CartName.ACCEPT_ALTERNATIVE.equals(cartMode) &&
		     !cartMode.equals(CartName.QUICKSHOP) && 
		     optionalProducts.size()>0 ) {
		   boolean headingDone=false;		   int optCnt=0;
		   FDProduct fdProd = null;
		   String salesUnitDescription = "NA";
		   String salesUnitName = "NA";
		   
		   FDSalesUnit[] salesUnits;    %>
		  <logic:iterate id='optProd' collection="<%=optionalProducts%>" type="com.freshdirect.fdstore.content.ProductModel" indexId='optIdx'>
	<%	
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
	<%	      if (imgMedia!=null) {   %><img src="<%=imgMedia.getPath()%>" border="0" width="<%=imgMedia.getWidth()%>" height="<%=imgMedia.getHeight()%>"> <%  }  %>
	<%	      if (mediaPath!=null) { %><br><bt><fd:IncludeMedia name='<%= mediaPath %>'/> <%  }  %>
		       <br><br></td></tr>
		    </table>
		    <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
	<%         }  
	
	    	  if (!paintOptionalVertical) {  // paint the optional category using the Horizontal style
	    	     if (prodCount==0){%><tr><% }  %>
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
					  <input name ="quantity_<%=prodCount%>" value="<%=(request.getParameter("quantity_"+prodCount)==null ?"" : request.getParameter("quantity_"+prodCount))%>" type="text" size="3" onChange="chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',0,<%= optProd.getQuantityMinimum() %>,<%= user.getQuantityMaximum(optProd) %>,true)">
					</td>
					<td valign="bottom" align="left">
					  <a HREF="javascript:chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',<%= optProd.getQuantityIncrement() %>,<%= optProd.getQuantityMinimum() %>,<%= user.getQuantityMaximum(optProd) %>,true);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></a><br>
					  <a HREF="javascript:chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',-<%= optProd.getQuantityIncrement() %>,<%= optProd.getQuantityMinimum() %>,<%= user.getQuantityMaximum(optProd) %>,true);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity"></a>
					  <ximg src="/media_stat/images/layout/clear.gif" width="10" height="1">
					</td>
				   </tr>
				  </table>
				</td>
			  </tr>
			  <tr><td colspan="2"><br></td></tr>
			</table>
		    </td>

<% 		     if (prodCount % 3==0 && prodCount!=0) { %>
		       </tr><tr>
<%         	     }  
		     prodCount++;  
	          } else { // paint the optional category using the Vertical style
				String imgRollOver = "";
				String imgName = "optProdImg";
	 			if (openTable) { // first item, paint main table 
				openTable = false;
	%>
			<tr valign="top">
			<td width="115">
			<img src="<%=dispObj.getImagePath()%>" border="0" name="<%=imgName%>"></td>
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
				  <input name ="quantity_<%=prodCount%>" value="<%=(request.getParameter("quantity_"+prodCount)==null ?"" : request.getParameter("quantity_"+prodCount))%>" type="text" size="3" onChange="chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',0,<%= optProd.getQuantityMinimum() %>,<%= user.getQuantityMaximum(optProd) %>,true)"></td>
				
				<td width="15"><a HREF="javascript:chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',<%= optProd.getQuantityIncrement() %>,<%= optProd.getQuantityMinimum() %>,<%= user.getQuantityMaximum(optProd) %>,true);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></a><br>
				  <a HREF="javascript:chgNamedQty(pricing_<%=prodCount%>,'quantity_<%=prodCount%>',-<%= optProd.getQuantityIncrement() %>,<%= optProd.getQuantityMinimum() %>,<%= user.getQuantityMaximum(optProd) %>,true);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity"></a></td>
				
				<td width="<%=maxWidth-120%>"><a href="javascript:popup('prod_desc_popup.jsp?catId=<%=optProd.getParentNode()%>&prodId=<%=optProd%>','small')" <%=imgRollOver%>><%=dispObj.getItemNameWithoutBreaks()%></a><nobr> <%=(!"".equals(dispObj.getSalesUnitDescription()) ? dispObj.getSalesUnitDescription()+" - " : "")%><%=dispObj.getPrice()%></nobr>
				   <br><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			</tr>
			<tr><td colspan="3"><span class="space4pix"><br><br></span></td></tr>
		
	<%	    prodCount++;  %>

	<%
		  } //end optional layout %>
	       </logic:iterate>  
<%             if (prodCount>1) { 
	          if (paintOptionalVertical) { %></table></td> <%   }   %>            
		    </tr>
		   </table>
<%	       }
	    }
	} %>
	  <br>
<%      } 
        attrib =parentCat.getAttribute("CATEGORY_MIDDLE_MEDIA");
        List catMidMedias =  attrib==null?new ArrayList():(List)attrib.getValue();
        if (catMidMedias.size()>0) {  %>
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
    
<%
    if (productNode.hasTerms()) { %>
    <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td align="left"><fd:IncludeMedia name='<%= productNode.getProductTerms().getPath()%>' /></td></tr>
        <tr><td align="left">
    		<fd:ErrorHandler result='<%=result%>' name='agreeToTerms'>
    		  <span class="text11rbold">To add meal to cart, please agree to these terms.</span><br>
    		</fd:ErrorHandler></td>
        </tr>
        <tr>
        <td align="left"><%= result.hasError("agreeToTerms") ? errImage : noErrImage%>
          <input type="checkbox" <%=(cartMode.equals(CartName.MODIFY_CART) || "yes".equalsIgnoreCase(request.getParameter("agreeToTerms")) ? "checked" : "")%> name="agreeToTerms" value="yes">&nbsp;<b>I have read and agree to the terms above.</b><br><br></td>
        </tr>
    </table>

<% }%>
    
	<table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
		<tr valign="top">
		  <td width="93">
			<b>Quantity</b>&nbsp;<input type="text" CLASS="text11" size="3" name="quantity<%=suffix%>" value="<%= quantityFormatter.format(defaultQuantity) %>" onChange="chgNamedQty(pricing,'quantity<%=suffix%>',0,<%= productNode.getQuantityIncrement() %>,<%= productNode.getQuantityMinimum() %>,<%= user.getQuantityMaximum(productNode) %>);" onChange="pricing.setQuantity(this.value);">
		  </td>
		  <td align="left" WIDTH="14">
			  <a HREF="javascript:chgNamedQty(pricing,'quantity<%=suffix%>',<%= productNode.getQuantityIncrement() %>,<%= productNode.getQuantityMinimum() %>,<%= user.getQuantityMaximum(productNode) %>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></a><br>
			  <a HREF="javascript:chgNamedQty(pricing,'quantity<%=suffix%>',-<%=productNode.getQuantityIncrement() %>,<%= productNode.getQuantityMinimum() %>,<%= user.getQuantityMaximum(productNode) %>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity"></a>
		  </td>
		  <td width="150">&nbsp;&nbsp;<b>Price</b>&nbsp;<INPUT class="text11bold" TYPE="text" NAME="price" SIZE="6" onChange="" onFocus="blur()" value=""></td>

                   <td width="<%=maxWidth-257%>" align="right">
	<% if(CartName.MODIFY_CART.equals(cartMode) ) {
			String referer = request.getParameter("referer");
			if (referer==null) referer = request.getHeader("Referer");
			if (referer==null) referer = "/view_cart.jsp";			%>
			<input type="image" name="save_changes" src="/media_stat/images/buttons/save_changes_cart.gif" width="109" height="20" border="0" alt="SAVE CHANGES" VSPACE="2"><BR>
			<input type="image" name="remove_from_cart" src="/media_stat/images/buttons/remove_item.gif" width="109" height="20" border="0" alt="REMOVE ITEM" VSPACE="2"><BR>
			<input type="hidden" name="referer" value="<%= referer %>">
			<a href="<%=referer%>"><img src="/media_stat/images/buttons/no_change.gif" width="109" height="20" border="0" alt="NO CHANGE" VSPACE="2"></a><BR>
        <% } else if (CartName.MODIFY_LIST.equals(cartMode) ||
	              CartName.ACCEPT_ALTERNATIVE.equals(cartMode)) {

	        String referer = request.getParameter("referer");
		if (referer==null) referer = request.getHeader("Referer");
		if (referer == null) referer = "/quickshop/all_lists.jsp";
	        String ccListIdVal = (String)request.getAttribute(CclUtils.CC_LIST_ID);
	        String lineId = (String)request.getAttribute("lineId");
        %>
	
	
	<% {
	     String prodLink = (String)request.getAttribute("productLink");
	     if (prodLink != null && !"".equals(prodLink)) {
	%>
	     <input type="hidden" name="productLink" value="<%=prodLink%>">
	<%
             } // if
           } // local block
	%>

	
	       <input type="hidden" name="list_action" value="">

               <%
	       if (CartName.ACCEPT_ALTERNATIVE.equals(cartMode)) {
	       %>
	       <input type="image" name="save_changes" src="/media_stat/images/template/quickshop/alt_replace_item_btn.gif" 
	       onclick="document.productForm.list_action.value='modify';document.productForm.submit();"
	       HSPACE="2" BORDER="0"><BR><FONT CLASS="space4pix"><BR></FONT>

	       <%
	       } else {
	       %>
               <input type="image" name="save_changes" src="/media_stat/images/buttons/list_save_changes.gif" 
	              onclick="document.productForm.list_action.value='modify';document.productForm.submit();"
	              HSPACE="2" BORDER="0"><BR><FONT CLASS="space4pix"><BR></FONT>

               <input type="image" name="remove_from_list" src="/media_stat/images/buttons/remove_item.gif" HSPACE="2" BORDER="0" 
	              onclick="document.productForm.list_action.value='remove';document.productForm.submit();"><BR><FONT CLASS="space4pix"><BR></FONT>
               <%
	       } // cartMode
	       %>

              <input type="hidden" name="referer" value="<%= referer %>">
	      <input type="hidden" name="<%=CclUtils.CC_LIST_ID%>" value="<%=ccListIdVal%>">
	      <input type="hidden" name="originalSku" value="<%=skuCode%>">
	      <input type="hidden" name="lineId" value="<%=lineId%>">
	      <% if (request.getParameter("recipeId") != null) { %>
	      <input type="hidden" name="recipeId" value="<%=request.getParameter("recipeId")%>"/>
	      <% } %>

    
              <a href="<%=referer%>"><img name="no_changes" src="/media_stat/images/buttons/no_change.gif" HSPACE="2" BORDER="0"></a><BR><FONT CLASS="space4pix"><BR></FONT>

	
	<% } else {   %>
		 <input type="image" name="addMultipleToCart" src="/media_stat/images/buttons/add_to_cart.gif" width="93" height="20" hspace="4" vspace="0" border="0" alt="ADD SELECTED ITEMS TO CART">
	<%  } %>
		</td>
		</tr>
		<tr>
		 <td colspan="4" align="right">
<%
		if (cartMode.equals(CartName.QUICKSHOP)) {
			%>
					<fd:QuickShopController id="quickCart">
					<fd:GetBackToListLink id='backToList' quickCart='<%= quickCart %>' deptId='<%=request.getParameter("qsDeptId")%>'>
					<a href="<%=backToList.toString()%>"><img src="/media_stat/images/buttons/back_to_list.gif" width="99" height="21" border="0" alt="Back to List" vspace="2"></a><br>
					</fd:GetBackToListLink>
					</fd:QuickShopController>
			<%
		}   %>
		</td>
	</tr>
        <tr>
		<td colspan="4">
		<%@ include file="/shared/includes/product/i_minmax_note.jspf" %>
		</td>
        </tr><tr></tr><tr></tr>
<%
        if (!CartName.MODIFY_CART.equals(cartMode) && !CartName.MODIFY_LIST.equals(cartMode)) {
%>
        <tr>
<fd:CCLCheck>
        <td colspan="9" align="right"> 
	<br/>
	<div>
	    <fd:CCLNew/><a id="ccl-add-action" href="/unsupported.jsp" 
		      onclick="return CCL.save_items('productForm',this,'action=CCL:AddMultipleToList&source=ccl_actual_selection')"><img src="/media_stat/ccl/lists_link_selected_dfgs.gif" width="133" height="13" style="border: 0; padding-left: 14px"><img src="/media_stat/ccl/lists_save_icon_lg.gif" width="12" height="14" style="margin: 0 0 1px 5px; border: 0"/></a>
		<div style="text-align: right; margin-bottom: 1ex;">       
		    <fd:CCLNew template="/common/template/includes/ccl_moreabout.jspf"/>
		</div>
    </div>
    <br/>
    <br/>
    </td>
</fd:CCLCheck>
        </tr>
<%
       } // if not modify cart or modify list
%>
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

			function chgNamedQty(pObject,qtyFldName,delta,min,max,clearOnZero) {
				if (clearOnZero==null) clearOnZero=false;
					var qty = parseFloat(document.productForm[qtyFldName].value)
					if (isNaN(qty)) qty=0;
					qty = qty + delta;
					
					if (qty<=0 && !clearOnZero) {

					  if (qty < 0 || (qty < min && delta < 0) ){ 
						qty=min;
					  } else if(qty < min && delta >=0) {
						qty=min;
					  } else if (qty > max) {
						qty=max;
					  }
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

	attrib =parentCat.getAttribute("CATEGORY_BOTTOM_MEDIA");
	List catBottomMedias =  attrib==null?new ArrayList():(List)attrib.getValue();
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
