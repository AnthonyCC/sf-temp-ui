<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import="com.freshdirect.common.pricing.Pricing"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="com.freshdirect.common.pricing.CharacteristicValuePrice"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user"/>
<%
//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
if(deptId!=null) {
    currentFolder=ContentFactory.getInstance().getContentNode(deptId);
    isDepartment = true;
} else {
    currentFolder=ContentFactory.getInstance().getContentNode(catId);
}


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();

String multiSuccessPage = "/cart_confirm.jsp?catId="+catId;
String singleSuccessPage = "/cart_confirm.jsp?catId="+catId;
request.setAttribute("successPage",singleSuccessPage);

%>
<fd:FDShoppingCart id='cart' action='addToCart' result='result' multiSuccessPage='<%=multiSuccessPage%>' successPage='<%=singleSuccessPage%>'>
<%
request.removeAttribute("successPage");

int unAvailableCount=0;
Image catPhoto = null;
String hideURL = currentFolder.getHideUrl();
if (hideURL!=null) {
    String redirectURL = response.encodeRedirectURL(hideURL);
    response.sendRedirect(redirectURL);
    return;
}
catPhoto = ((CategoryModel) currentFolder).getCategoryDetailImage();
if (catPhoto==null) {
    catPhoto = new Image();
}
Html editorial = currentFolder.getEditorial();
String blkIntroCopy = editorial==null?"":editorial.getPath();

SkuModel defaultSku=null;
// get the first product off the list of products that were returned.  we'll need it for 
// the a.k. name, and variations.
String selectedSkuCode = request.getParameter("skuCode");
String selectedQty = request.getParameter("quantity");
String productName = "";
String productName2 = "";
String akaName = "";
String packageDesc = "";
ProductModel firstProduct = null;
FDProduct firstFDProduct=null;
List<SkuModel> prodSkus = null;
List<SkuModel> skus = new ArrayList<SkuModel>();
String IMAGE_GREEN_ARROW_DOWN = "/media/images/layout/grn_arrow_down.gif";
String IMAGE_GREEN_ARROW_UP = "/media/images/layout/grn_arrow_up.gif";
String IMAGE_CLEAR = "/media/images/layout/clear.gif";
float prodMinQuantity =1;
float prodMaxQuantity=1;
float prodIncrement = 1;
String quantityText = "";
int skuSize = 0;
boolean isAvailable= false;
for(Iterator itr= sortedColl.iterator();itr.hasNext();){
    ContentNodeModel item = (ContentNodeModel)itr.next();
    if (!(item instanceof ProductModel)) continue;
    if ( ((ProductModel)item).isUnavailable()) {
        unAvailableCount++;
        continue;
    }
    firstProduct = (ProductModel)item;

    akaName = firstProduct.getAka();

    packageDesc = firstProduct.getPackageDescription();

    //get the smallest sku
    prodSkus = firstProduct.getSkus();
    skuSize = prodSkus.size();

    String price = "";
//   SkuModel sku = null; 
    defaultSku = prodSkus.get(0);
    if (prodSkus.size()>1) {
        for (ListIterator<SkuModel> li=prodSkus.listIterator(); li.hasNext(); ) { 
             defaultSku = li.next();
            if ( defaultSku.isUnavailable() ) {
               li.remove();
            }
        }
    }
    if (prodSkus.size()==0) {
        unAvailableCount++;
        defaultSku=null;
        continue;
    }
    
    defaultSku = prodSkus.get(0);

    isAvailable = true;
    prodMinQuantity = firstProduct.getQuantityMinimum();
    prodMaxQuantity = user.getQuantityMaximum(firstProduct);
    prodIncrement = firstProduct.getQuantityIncrement();
    quantityText = firstProduct.getQuantityText();
    firstFDProduct = defaultSku.getProduct(); //FDCachedFactory.getProduct( FDCachedFactory.getProductInfo(defaultSku.getSkuCode()) );
    break;
}
String fullName = currentFolder.getFullName()!=null?currentFolder.getFullName():"--no name--";
if (result.isFailure()) {
Collection myErrs=((ActionResult)result).getErrors();
%>
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="400">
<%
    for (Iterator errItr = myErrs.iterator();errItr.hasNext(); ) {
        String errDesc = ((ActionError)errItr.next()).getDescription();
%>
<TR VALIGN="top">
    <TD WIDTH="15" height="18" VALIGN="top">
            <img src="/media_stat/images/layout/error.gif" border="0">
            </td><td width="335">
      <FONT class="text11rbold"><%=errDesc%></FONT>
    </TD></tr>
 <%   
    }
%>
</TABLE>
<%}
//promotion eligibility calculation

//does this product qualify for signup promo?
boolean qualifies =
    firstProduct==null ? false :
    (firstProduct.isQualifiedForPromotions() && user.getMaxSignupPromotion()>0);
double promotionValue = 0.0;
if (qualifies) {
    promotionValue = user.getMaxSignupPromotion();
}
String prefix = promotionValue+"";
prefix = prefix.substring(0, prefix.indexOf('.'));

// get the brand info for the first product, to display the brand logo and possible link to the popup info for the brand
Image brandLogo = null;
String brandPopupLink=null;
String brandName = null;
// get the brand logo, if any.
if (firstProduct!=null) {
    List<BrandModel> prodBrands = firstProduct.getBrands();
    if (prodBrands!=null && prodBrands.size() > 0 ) {
        BrandModel bm = (BrandModel)prodBrands.get(0);
        if (bm!=null){
            brandName= bm.getFullName();
            brandLogo = bm.getLogoSmall();

            Html popupContent = bm.getPopupContent();
            if (popupContent!=null) {
                TitledMedia tm = (TitledMedia)popupContent;
                EnumPopupType popupType=EnumPopupType.getPopupType(tm.getPopupSize());
                brandPopupLink = "javascript:pop('"+response.encodeURL("/brandpop.jsp?brandId="+bm)+"',"+popupType.getHeight()+","+popupType.getWidth()+")";
            }
        }
    }
}

 %>

<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="400">
<TR VALIGN="TOP">
<TD CLASS="text11" WIDTH="150" ALIGN="RIGHT" valign="top">
<% 
if (brandLogo !=null) {
    if (brandPopupLink!=null) {
%>
    <a href="<%=brandPopupLink%>"><img src="<%=brandLogo.getPath()%>" width="<%=brandLogo.getWidth()%>"  height="<%=brandLogo.getHeight()%>" alt="<%=brandName%>" border="0"></a><br><br>
<% } else {%>
    <img src="<%=brandLogo.getPath()%>" width="<%=brandLogo.getWidth()%>"  height="<%=brandLogo.getHeight()%>" alt="<%=brandName%>" border="0"><br><br>
<% }
  }%>

<FONT CLASS="title18"><%= fullName.toUpperCase() %></FONT><BR>
<%  if ((akaName != null) && !"".equals(akaName)) { %>
"<%= akaName %>"<BR>
<% }
   if ((packageDesc != null) && !"".equals(packageDesc)) { %>
<br><%= packageDesc %><BR>
<% } %>
</TD><TD WIDTH="5"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="5" HEIGHT="1" BORDER="0"><BR></TD>
<TD WIDTH="245">
<% if(qualifies && firstProduct!=null && !firstProduct.isUnavailable()){%>

    <table border=0>
        <tr>
            <td width=5></td>
            <td><img src="/media_stat/images/template/offer_icon.gif" alt="Promotion icon"></td>
            <td><font class="title12">Free!<br></font><A HREF="promotion.jsp?cat=<%=catId%>">See our $<%=prefix%> offer</a></td>
            <td width=90></td>
        </tr>
    </table>
<%}%>
&nbsp; <BR><img src="<%= catPhoto.getPath() %>" <%= JspMethods.getImageDimensions(catPhoto) %> border="0" alt="<%= productName %>">

</TD>
</TR>
<%
if (firstProduct==null) { 
%>
    <tr><td align="left" colspan="3"><p align="left"><font class="text12" color="#999999">
        <b>We're sorry! This item is temporarily unavailable.</b><br>
        <br>
        We're proud to offer New York's widest selections of fresh foods. Unfortunately, this product is temporarily unavailable.
        Please check back on your next visit.
</td></tr></TABLE>
<%
}else { // something is available.. 
%>
<TR VALIGN="TOP">
<TD CLASS="text11" WIDTH="400" COLSPAN="3">
<%
if ( blkIntroCopy!=null && blkIntroCopy.trim().length() > 0) {
%><BR><fd:IncludeMedia name='<%=blkIntroCopy%>' />
<% }
int prodsShown=0;
boolean isOnePricedByLb = false;
boolean productSelected = false;
SkuModel leastPriceSku = null;
String selectedSU = null;
int selectedSUIndex = 0;
%>
<BR><FONT CLASS="space4pix"><BR></FONT>
<br>
Learn more about <a href="javascript:popup('/departments/meat/info_buying_bulk_meat.jsp','small');">buying bulk meat</a>.
</TD></TR></TABLE>
<form  name="bulk_meat_product" method="post">
<TABLE CELLSPACING="2" CELLPADDING="0" BORDER="0" WIDTH="400">
<TR VALIGN="TOP">
<TD WIDTH="400" COLSPAN="3">&nbsp;<br>
<font class="text10bold"><%=fullName%> cut into: </font> <br>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="3"><BR>
<IMG src="/media_stat/images/layout/999966.gif" WIDTH="400" HEIGHT="1"><BR>
<IMG src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="7">
</TD>
</TR>
<input type="hidden" value="<%=catId%>" name="catId">
<input type="hidden" value="<%=request.getParameter("productId")%>" name="productId">
<input type="hidden" value="<%=request.getParameter("salesUnit")%>" name="salesUnit">
<%--<input type="hidden" value="<%=request.getParameter("quantity")%>" name="quantity"> --%>
<input type="hidden" value="<%=request.getParameter("skuCode")%>" name="skuCode">
<logic:iterate id='contentNode' collection="<%=sortedColl%>" type="com.freshdirect.fdstore.content.ContentNodeModel">
<%
Image bulkImage = null;
Image optionImage = null;
String prodDescPath = null;
    if (!(contentNode instanceof ProductModel)) continue;
    ProductModel bulkProduct = (ProductModel) contentNode;

    if (bulkProduct.isUnavailable()) continue;
// get the images and Content for this product
    prodSkus = bulkProduct.getSkus();
    if (prodSkus.size()>1) {
        for (ListIterator<SkuModel> li=prodSkus.listIterator(); li.hasNext(); ) { 
            defaultSku = li.next();
            if ( defaultSku.isUnavailable() ) {
                li.remove();
            }
        }
    }

    skuSize = prodSkus.size();
    if (skuSize==0) {
        continue;
    }
    skus.addAll(prodSkus); //load these skus into the list of skus needed by the pricing javascript include
    String price = "";
    SkuModel sku = null; 
    if (skuSize==1) {
        leastPriceSku = prodSkus.get(0);  // we only need one sku
    } else {
        Comparator<SkuModel> priceComp = new ProductModel.PriceComparator();
        leastPriceSku = Collections.<SkuModel>min(prodSkus, priceComp);
    }
    bulkImage = bulkProduct.getDetailImage();
    if (bulkProduct.getProductDescription()==null) {
      prodDescPath=null;
    } else {
        prodDescPath = bulkProduct.getProductDescription().getPath();
    }
    optionImage=bulkProduct.getDescriptiveImage();
%>
    <TR VALIGN="TOP">
    <TD WIDTH="175">
<% if(bulkImage!=null) { %>
    <img src="<%= bulkImage.getPath() %>" <%= JspMethods.getImageDimensions(bulkImage) %> border="0" alt="<%= bulkProduct.getFullName() %>">
<% }else {%>&nbsp;<%}%></TD>
<TD WIDTH="200">
<% if(optionImage!=null) { %>
<img src="<%= optionImage.getPath() %>" <%= JspMethods.getImageDimensions(optionImage) %> border="0" alt="<%= bulkProduct.getFullName() %>"><br>
<img src="/media_stat/images/layout/cccccc.gif" width="200" height="1" border="0"><br>
<% }else {%>&nbsp;<%}%>
<% if (prodDescPath!=null) {%> <fd:IncludeMedia name="<%= prodDescPath%>" /> <BR><%}%>
<input type="hidden" value="<%=currentFolder.getContentName()%>" name="catId_<%=prodsShown%>"><input type="hidden" value="<%=bulkProduct.getContentName()%>"   name="productId_<%=prodsShown%>"><input type="hidden" value="" name="salesUnit_<%=prodsShown%>">
<% 
    // show the sku options
    FDProduct blkFDProd=null;
    for(Iterator<SkuModel> skuItr = prodSkus.iterator();skuItr.hasNext();){
        sku = skuItr.next();
        if (sku.isUnavailable()) continue;
        String skuPrice = "";
        String skuSalesUnit="";
        String gradePath="";
%>
        <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<%
            skuPrice = JspMethods.currencyFormatter.format(productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
        blkFDProd = FDCachedFactory.getProduct(productInfo);
            if ("LB".equalsIgnoreCase((blkFDProd.getPricing().getZonePrice(user.getPricingContext().getZoneId()).getMaterialPrices()[0]).getPricingUnit())) {
                isOnePricedByLb = true;
            }
            skuSalesUnit = blkFDProd.getSalesUnits()[0].getName();
%>
        </fd:FDProductInfo>
<%
            DomainValue domainValue = null;
            List<DomainValue> variationMatrix =sku.getVariationMatrix();
            domainValue = variationMatrix!=null && variationMatrix.size()>0 ?(DomainValue)variationMatrix.get(0):null;
            String matrixValue = null;
            if (domainValue!=null) {
                Html _fdDefGrade = bulkProduct.getFddefGrade();
                if (_fdDefGrade!=null) { 
                   gradePath = ((MediaI)_fdDefGrade).getPath();
                   String popup = "/shared/popup.jsp?catId="+catId + "&prodId="+bulkProduct.getContentName(); 
                   matrixValue = "<A HREF=\"javascript:popup('"+popup+"&attrib=FDDEF_GRADE&tmpl=large','large')\">"+domainValue.getLabel().toUpperCase()+"</A>";
                } else {
                   matrixValue = domainValue.getLabel().toUpperCase();
                }
            }
            String selectedRB="";
/* ******
 * 
 * figure out if this sku's radio button should be checked and also save the details for the javascricpt later
 *
********/
            if (!productSelected) {
                if (selectedSkuCode==null || (selectedSkuCode!=null && sku.getSkuCode().equals(selectedSkuCode))) {
                    selectedRB="checked";
                    productSelected=true;
                    selectedSUIndex=prodsShown;
                    if (selectedSkuCode==null) {
                        selectedSkuCode = leastPriceSku.getSkuCode();
                        selectedSU = leastPriceSku.getProduct().getSalesUnits()[0].getName();
                    } else {
                        selectedSkuCode = sku.getSkuCode();
                        selectedSU = sku.getProduct().getSalesUnits()[0].getName();
                    } 
                }
            }

            String onClickHandler="pricing.setSKU('"+sku.getSkuCode()+"');setSalesUnit('"+prodsShown+"','"+skuSalesUnit+"');";
            if (matrixValue!=null) { %>
                <div style="margin-left: 24px; text-indent: -24px;"><input type="radio"  value="<%=sku.getSkuCode()%>" <%=selectedRB%> name="blkSkuCode" onClick="<%=onClickHandler%>"><font CLASS="text11bold"><%= matrixValue %> - <%= skuPrice%></FONT></div>
<%          } else {%>
                <div style="margin-left: 24px; text-indent: -24px;"><input type="radio"  value="<%=sku.getSkuCode()%>" <%=selectedRB%> name="blkSkuCode" onClick="<%=onClickHandler%>">&nbsp;<font CLASS="text11bold"><%= skuPrice%></FONT></div> 
<%
            }
    }
    prodsShown++;
%>  </TD></TR>
    <TR VALIGN="TOP"><TD COLSPAN="3" WIDTH="400"><BR></TD>
    </TR>
</logic:iterate>
<input type="hidden" name="itemCount" value="<%=prodsShown%>">
</TABLE>
<BR><!-- End of Customization display -->
<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="400">

<TR VALIGN="TOP">
<TD COLSPAN="4" WIDTH="400" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<TR VALIGN="TOP">
<TD COLSPAN="4" WIDTH="400"><IMG SRC="<%= IMAGE_CLEAR %>" WIDTH="1" HEIGHT="4"></TD>
</TR>
<TR VALIGN="TOP">
<TD ALIGN="RIGHT"><FONT CLASS="text10bold"><%= quantityText %></FONT> <INPUT TYPE="text" class="text10" NAME="quantity" SIZE="3" MAXLENGTH="4" value="<%=selectedQty!=null?selectedQty:prodMinQuantity+""%>" onChange="chgQty(0,<%= prodMinQuantity%>,<%= prodMaxQuantity%>);"></TD>
<TD WIDTH="15"><A HREF="javascript:chgQty(<%= prodIncrement%>,<%= prodMinQuantity%>,<%= prodMaxQuantity%>);"><img SRC="<%= IMAGE_GREEN_ARROW_UP %>" width="10" height="9" border="0" vspace="1" alt="greater quantity"></A><BR>
<A HREF="javascript:chgQty(<%= -prodIncrement%>,<%= prodMinQuantity%>,<%= prodMaxQuantity%>);"><img SRC="<%= IMAGE_GREEN_ARROW_DOWN %>" width="10" height="9" border="0" vspace="1" alt="lesser quantity"></A></TD>
<TD colspan="2" ALIGN="RIGHT"><FONT CLASS="text10bold"><%
if (isOnePricedByLb) { %>
<A HREF="javascript:popup('/help/estimated_price.jsp','small')">Est. Price:</a>
<%
} else { %>Price:<%}
%>&nbsp;</FONT><INPUT TYPE="text" CLASS="text11" NAME="PRICE" SIZE="7" onFocus="blur();"/>
<!-- stuff for the bulk meat customization goes here -->
<% 
   List variations = Arrays.asList(firstFDProduct.getVariations());
  EnumPopupType smallPopup = EnumPopupType.getPopupType("small");
%>    
<logic:iterate id="variation" collection="<%= variations %>" type="com.freshdirect.fdstore.FDVariation" indexId="idx">
<%
    if("checkbox".equalsIgnoreCase(variation.getDisplayFormat())) {
        Pricing defaultPricing = firstFDProduct.getPricing();
        FDVariationOption cboxValue = null;
        FDVariationOption[] options = variation.getVariationOptions();
        for (int i=0; i<options.length; i++) {
            if (options[i].isLabelValue()) cboxValue = options[i];
        }
        boolean isChecked = (cboxValue != null) && cboxValue.isSelected();
  %>
&nbsp;&nbsp;<FONT CLASS="text13">
<%
        String fddefPop = "/shared/fd_def_popup.jsp";
        String charName = variation.getName();
        String charFileName = "/media/editorial/fd_defs/characteristics/"+charName.toLowerCase()+ ".html";
        String spop = "small_pop";
        String title = variation.getDescription();

		boolean theFileExists = false;

		try {
		     java.net.URL url = MediaUtils.resolve(FDStoreProperties.getMediaPath(), charFileName);
		     InputStream in = url.openStream();
			 if(in != null) {
			     in.close();
				  theFileExists = true;
			 }
		} catch(Exception ex) {
		}
				
        if (theFileExists) {
    %>
            <a href="javascript:pop('<%=fddefPop%>?charName=<%=charName%>&tmpl=<%=spop%>&title=<%=title%>',<%=smallPopup.getHeight()%>,<%=smallPopup.getWidth()%>)"><%= variation.getAttribute(EnumAttributeName.DESCRIPTION) %></a>
    <% 
    }else{
    %>  
            <%= variation.getAttribute(EnumAttributeName.DESCRIPTION) %>
<%  }%>
    </FONT>
<%  double cvprice = 0.0;
CharacteristicValuePrice cvp = defaultPricing.findCharacteristicValuePrice(variation.getName(), cboxValue.getName());
if (cvp != null) cvprice = cvp.getPrice();
if (cvprice > 0.0) { %>
- <%= JspMethods.currencyFormatter.format(cvp.getPrice()) %>/<%= cvp.getPricingUnit() %>
<%  } %>
<input type="checkbox"<%= isChecked ? " CHECKED " : "" %> name="<%= variation.getName() %>" value="<%= cboxValue.getName() %>" onClick="pricing.setOption('<%= variation.getName() %>', this.checked ? this.value : '');"><BR>
<%  } %>
</logic:iterate>
</TD></tr>
<TR VALIGN="TOP">
<TD COLSPAN="4" WIDTH="400" BGCOLOR="#999966"><IMG src="/media_stat/images/layout/999966.gif" WIDTH="1" HEIGHT="1"></TD>
</TR>
<tr><TD colspan="4" ALIGN="center"><br>
<input type="image" name="addToCart" src="/media/images/buttons/add_to_cart.gif"  ALT="ADD ITEMS TO YOUR CART" height="20" width="93" HSPACE="2" VSPACE="0" BORDER="0"><br>
<%@ include file="/shared/includes/product/i_pricing_script.jspf" %>

<script type="text/javascript">
var pricing = new Pricing();
var currentSelection = new Array();

function updatePriceField() {
    document.bulk_meat_product.PRICE.value = pricing.getPrice();
}
function setSalesUnit(suffix,suValue) {
    catIdFldName="catId_"+suffix;
    prodIdFldName="productId_"+suffix;
    
    document.bulk_meat_product.catId.value = document.bulk_meat_product[catIdFldName].value;
    document.bulk_meat_product.productId.value = document.bulk_meat_product[prodIdFldName].value;
    document.bulk_meat_product.salesUnit.value = suValue;
    document.bulk_meat_product.skuCode.value = pricing.selectedSku; //document.bulk_meat_product["blkSkuCode"].value;
    pricing.setSalesUnit(suValue);
}

function chgQty(delta,minQ, maxQ) {
    quantity = parseFloat(document.bulk_meat_product.quantity.value) + delta;
    if (isNaN(quantity) || quantity < minQ) {
        quantity = minQ;
    } else if (quantity >= maxQ) {
        quantity = maxQ;
    }
        absDelta = Math.abs(delta);
        if (absDelta==0) absDelta=1;
    quantity = Math.floor( (quantity-minQ)/absDelta)*absDelta  + minQ;
    document.bulk_meat_product.quantity.value = quantity;
    pricing.setQuantity(quantity);
}

pricing.setCallbackFunction( updatePriceField );
pricing.setSKU('<%=selectedSkuCode%>');
setSalesUnit(<%=selectedSUIndex%>,'<%=selectedSU%>')
pricing.setQuantity(<%=selectedQty==null?prodMinQuantity+"":selectedQty%>);

</script>
</td>
</tr>
</table>
<%}// end of else for: if firstProduct==null %>
</fd:FDShoppingCart>
