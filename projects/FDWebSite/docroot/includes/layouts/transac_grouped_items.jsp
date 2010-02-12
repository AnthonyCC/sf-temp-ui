<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.ErpServicesProperties' %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%!
    java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>

<%

FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 

ProductContainer currentFolder = null;
if(deptId!=null) {
    currentFolder=(ProductContainer) ContentFactory.getInstance().getContentNode(deptId);
} else {
    currentFolder=(ProductContainer) ContentFactory.getInstance().getContentNode(catId);
}


Collection sortedColl = null;

int cols = 0;
int maxWidth;
int newCategoryCount = 0;
boolean firstProduct = false;

%>
<script>
    function chgQty(idx,qtyFldName,delta,min,max) {
        var qty = parseFloat(document.transac_grouped_items[qtyFldName].value)
        if (isNaN(qty)) qty=0;
        if (qty<1) qty=0;
        qty = qty + delta;

        if (qty < min  && qty!=0) return;
        if (qty > max) return;

        if (qty<=0) {
            qty=0;
            document.transac_grouped_items[qtyFldName].value='';
        } else {
            document.transac_grouped_items[qtyFldName].value = qty;
        }

         var pricingObj = eval ("pricing"+idx);
        pricingObj.setQuantity(qty);
    }

    function updatePriceField() {
        document.transac_grouped_items.total.value = pricing.getPrice();
    }
</script>
<%
sortedColl = (Collection) request.getAttribute("itemGrabberResult");

if (sortedColl==null) sortedColl = new ArrayList();
%>
<%
//**************************************************
//***   the Transactional Grouped Items          ***
//**************************************************

int itemTotal = sortedColl.size();

if (request.getRequestURI().toLowerCase().indexOf("department.jsp")!=-1) {
    maxWidth = 550;
} else {
    maxWidth = 425;
}

String itemNameFont = null;
Image itemImage;
String currentFolderPKId = currentFolder.getContentKey().getId();
String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
CategoryModel cat = null;

//prod specs
int idx =-1;
boolean prodUnavailable = true;
SkuModel defaultSku = null;

List sortedList = new ArrayList();
//convert collection to list
boolean oneNotAvailable = false;
int prodsAvailable = 0;
for(Iterator collIter = sortedColl.iterator();collIter.hasNext() ;) {
        Object currItem = collIter.next();
    if (currItem instanceof ProductModel) {
      if  (((ProductModel)currItem).isUnavailable()) {
        //oneNotAvailable = true;
        continue;  /* dont add unavailable items to the list, which makes the unavailable logic below useless
                    but I suspect that creative will want to display unavailable items...hope I'm wrong. (RG)  */
      }
      prodsAvailable++;
      sortedList.add(currItem);
    }
}  
int itemsToDisplay = sortedList.size();
// setup for succpage redirect ....
String succPage = "";
int templateType=currentFolder.getTemplateType(1);
if (EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
    request.setAttribute("successPage","/wine_cart_confirm.jsp?catId="+request.getParameter("catId"));
    succPage = "/wine_cart_confirm.jsp?catId="+request.getParameter("catId");
} else {
    request.setAttribute("successPage","/grocery_cart_confirm.jsp?catId="+request.getParameter("catId"));
    succPage = "/grocery_cart_confirm.jsp?catId="+request.getParameter("catId");
}
if (prodsAvailable>0) {
    %>
	<table align="center" width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" >
        <tr><td align="center"><img src="media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="6"></td></tr>
        <tr><td align="center" style="padding-bottom:8px;"><i>Click on name for more info.</i></td></tr>
    </table>
<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= succPage %>'>
    <table><form name="transac_grouped_items" id="transac_grouped_items" method="POST"></table>
    <%

    //*** if we got this far..then we need to remove the sucess page attribute from the request.
    request.removeAttribute("successPage");

    // there are errors..Display them
    Collection myErrs=((ActionResult)result).getErrors();
    if (myErrs.size()>0) {   
    %>
        <table border="0" cellspacing="0" cellpadding="0" width="<%=maxWidth%>">
    <%
        for (Iterator errItr = myErrs.iterator();errItr.hasNext(); ) {
            String errDesc = ((ActionError)errItr.next()).getDescription();
    %>
        <tr valign="top">
            <td valign="middle">
                <FONT class="text12bold" color="#CC3300"><%=errDesc%></FONT>
            </td>
        </tr>
     <%   
        }
    %>
        </table>
    <%
        request.setAttribute("doRedirect","false");
    } else {
        if (request.getMethod().equalsIgnoreCase("post") ){
        request.setAttribute("doRedirect","true");
        }
    }
    %>
    <table border="0" cellspacing="0" cellpadding="0" width="<%=maxWidth%>">
    <%
    int itemShownIndex=-1;
    List skus = new ArrayList();
    List prices = new ArrayList();
    List salesUnitNames = new ArrayList();

    for (int itemIndex=0; itemIndex < sortedList.size();itemIndex++) {
        ContentNodeModel contentNode = (ContentNodeModel)sortedList.get(itemIndex);
        itemNameFont = "text11";
        ProductModel displayProduct = (ProductModel)contentNode;

        List prodSkus = displayProduct.getSkus();
        if (prodSkus.size()==0) continue;
        SkuModel sku = (SkuModel) (prodSkus.size()==1 ? prodSkus.get(0) : Collections.min(prodSkus, ProductModel.PRICE_COMPARATOR));
    //******************************  Start  Product Line Display  ******************
    %>
    <%-- <table border="0" cellspacing="0" cellpadding="0" width="<%=maxWidth%>"> --%>
    <tr valign="middle">
    <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
    <%
        FDProduct product = null;
        String salesUnitDescription = "NA";
        String salesUnitName = "NA";
        boolean prodUnAvailable = displayProduct.isUnavailable();

        try {
            product = FDCachedFactory.getProduct(productInfo);
            FDSalesUnit[] salesUnits = product.getSalesUnits();
            if (salesUnits.length>0) {
                salesUnitDescription = salesUnits[0].getDescription();
                salesUnitName = salesUnits[0].getName();
                salesUnitNames.add(salesUnitName);
           }
        } catch (FDSkuNotFoundException fdsnf){
                JspLogger.PRODUCT.warn("Grocery Page: catching FDSkuNotFoundException and Continuing:\n FDProductInfo:="+productInfo+"\nException message:= "+fdsnf.getMessage());
        }
        String qtyFldName = "quantity_"+itemShownIndex;
    %>
    <td width="30" align="center"><NOBR>
<%
    String displayQuantity="";
    if (prodUnAvailable) {
        qtyFldName = "hidnqty_"+itemIndex;
        displayQuantity="";
%>
        <font color="#999999">NA</font><INPUT TYPE="hidden" 
    <%  } else {
            itemShownIndex++;
            skus.add( sku );
            qtyFldName = "quantity_"+itemShownIndex;
            displayQuantity=request.getParameter(qtyFldName);
            if (displayQuantity==null) displayQuantity = quantityFormatter.format(displayProduct.getQuantityMinimum());
            prices.add(new Double(displayProduct.getQuantityMinimum()));
    %>
        <INPUT TYPE="text"
    <%  }   %>
        NAME="<%= qtyFldName %>" SIZE="2" MAXLENGTH="2"  CLASS="text11" value="<%= displayQuantity %>"
        onChange="javascript:chgQty(<%=itemShownIndex%>,'<%= qtyFldName %>', 0, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);">

    </NOBR></td>
    <td width="10">
        <%  if (prodUnAvailable) {  %>&nbsp;<%  } else {    %>
            <A HREF="javascript:chgQty(<%=itemShownIndex%>,'<%= qtyFldName %>', <%= displayProduct.getQuantityIncrement() %>, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);"><img SRC="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity"></A><BR>
            <A HREF="javascript:chgQty(<%=itemShownIndex%>,'<%= qtyFldName %>', -<%= displayProduct.getQuantityIncrement() %>, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);"><img SRC="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity"></A>
        <%  }   %>
    </td>
    <td width="<%= prodUnAvailable ? "275" : "305" %>" style="padding-left:8px; padding-right:3px;">
    <%
        String unAvailableFontStart = "";
        String unAvailableFontEnd = "";
        if (prodUnAvailable) {
            unAvailableFontStart = "<font color=\"#999999\">";
            unAvailableFontEnd = "</font>";
        }

        String thisProdBrandLabel = displayProduct.getPrimaryBrandName();
        String productURL = "javascript:popup('prod_desc_popup.jsp?catId="
            +(displayProduct.getParentNode()).getContentName()
            +"&prodId=" + displayProduct + "','small')";

    %>
        <%= unAvailableFontStart %><A HREF="<%= productURL %>"><FONT CLASS="text10bold"><%= unAvailableFontStart %><%= thisProdBrandLabel %><%= unAvailableFontEnd %></FONT>
        <FONT CLASS="text10"><%= unAvailableFontStart %><%= displayProduct.getFullName().substring(thisProdBrandLabel.length()).trim() %><%= unAvailableFontEnd %></FONT></A>
        <%= unAvailableFontStart %><NOBR>- <%= salesUnitDescription %></NOBR><BR><%= unAvailableFontEnd %>
    <%
        Date earliestDate = displayProduct.getSku(0).getEarliestAvailability();
        Calendar testDate = new GregorianCalendar();
        testDate.add(Calendar.DATE, 1);
        // cheat: if no availability indication, show the horizon as the
        //        earliest availability
        if (earliestDate == null) {
            earliestDate = DateUtil.addDays(DateUtil.truncate(new Date()),
                                            ErpServicesProperties.getHorizonDays());
        }
        if(QuickDateFormat.SHORT_DATE_FORMATTER.format(testDate.getTime()).compareTo(QuickDateFormat.SHORT_DATE_FORMATTER.format(earliestDate)) < 0){
            SimpleDateFormat sf = new SimpleDateFormat("MM/dd");
            %><b><font color="#999999">Earliest Delivery - <%= sf.format(earliestDate) %></font></b><%
        }
        if (!prodUnAvailable) { %><%@include file="/includes/product/i_scaled_prices_nobr.jspf"%><% } %>
    </td>
    <%  if (prodUnAvailable) {  %>
    <td colspan="2" width="85" align="center"><font color="#999999">Not&nbsp;Available</font>
    <%  } else {    %>
    <td width="55" align="right">&nbsp;<font class="groceryProductLinePrice"><%= JspMethods.currencyFormatter.format(productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getDefaultPrice()) %>/<%=productInfo.getDisplayableDefaultPriceUnit().toLowerCase()%></font>
    </td>
    <td width="30" align="center">
    <%  }   %>
        <input type="hidden" name="salesUnit_<%= itemShownIndex %>" value="<%= salesUnitName %>">
        <input type="hidden" name="skuCode_<%= itemShownIndex %>" value="<%= displayProduct.getSku(0).getSkuCode() %>">
        <input type="hidden" name="catId_<%= itemShownIndex %>" value="<%= displayProduct.getParentNode() %>">
        <input type="hidden" name="productId_<%= itemShownIndex %>" value="<%= displayProduct %>">
    <%  if(!prodUnAvailable) {  %>
        <input type="image" name="addSingleToCart_<%= itemShownIndex %>" src="/media_stat/images/buttons/grocery_addtocart.gif" border="0" alt="Add this item to your cart">
    <%  }   %>
    </td>
    </fd:FDProductInfo>
    </tr>
    <%
    //*****************  END Product Line *********************************
    }
    %>
    </table>
    <%@ include file="/shared/includes/product/i_pricing_script.jspf" %>
    <script>
         
         function tmc_updateTotal() {
            var total=0;
            var p;
            for(i=0; i<<%=skus.size()%>; i++ ){
                p = eval("pricing"+i+".getPrice()");
                if (p!="") {
                    total+=new Number(p.substring(1));
                }
            }
            document.forms["transac_grouped_items"]["total"].value="$"+currencyFormat(total);
         }     
    <% 
    // build the pricing object for each of the products
    idx=0;
    for (Iterator itrItms = sortedList.iterator(); itrItms.hasNext(); ) {
         ContentNodeModel cn = (ContentNodeModel) itrItms.next();
         if (!(cn instanceof ProductModel)  || ((ProductModel)cn).isUnavailable()) continue;
         if (skus.size() <= idx) continue;       
    %>
         var pricing<%=idx%> = new Pricing();
         pricing<%=idx%>.setSKU("<%=((SkuModel)skus.get(idx)).getSkuCode() %>");
         pricing<%=idx%>.setSalesUnit("<%=salesUnitNames.get(idx) %>");
         pricing<%=idx%>.setCallbackFunction(tmc_updateTotal);

    <%
         idx++;
    }
    %>
    </script>
    
    <input type="hidden" name="itemCount" value="<%= Math.min(itemsToDisplay, idx) %>">
    <table align="center" width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" >
        <tr><td colspan="4"><img src="media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="6"></td>
        </tr>
        <tr>
            <td width="98" style="padding-right:4px;"><input type="image" name="addMultipleToCart" src="media_stat/images/buttons/add_to_cart.gif" width="93" height="20" border="0" alt="ADD SELECTED ITEMS TO CART"></td>
			<td width="12"><fd:CCLCheck><a href="/unsupported.jsp" onclick="return CCL.save_items('transac_grouped_items',this,'action=CCL:AddMultipleToList&source=ccl_actual_selection','source=ccl_actual_selection')"><img src="/media_stat/ccl/lists_save_icon_lg.gif" width="12" height="14" border="0"/></a></fd:CCLCheck></td>
            <td align="right">&nbsp;&nbsp;<b>Total Price:</b>&nbsp;&nbsp;&nbsp;
            <input type="text" name="total" size="8" maxlength="8" class="text11" value="" onFocus="blur()"></td>
            <td><img src="media_stat/images/layout/clear.gif"  height="1" width="10"></td>
        </tr>
		</form>
    </table>
    <br>
    
    <script>
    <% //set the quantities on the pricing object so that the total can be calculated 
        idx=0;
        for (Iterator itrItms = sortedList.iterator(); itrItms.hasNext(); ) {
             ContentNodeModel cn = (ContentNodeModel) itrItms.next();
             if (!(cn instanceof ProductModel)  || ((ProductModel)cn).isUnavailable()) continue;
        %>
             pricing<%=idx%>.setQuantity(<%=((Double)prices.get(idx)).doubleValue()%>);
        <%
             idx++;
        }
        %>
    </script>
    </fd:FDShoppingCart>
<%
} else {// end of If !oneNotAvailable
%>
    <table align="center" width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0">
    <tr><td><br><font class="text12" color="#999999">
        <b>We're sorry! This item is temporarily unavailable.</b><br>
        <br>
        We're proud to offer New York's widest selections of fresh foods. Unfortunately, this product is temporarily unavailable.
        Please check back on your next visit.
        </font></td>
    </tr>
    </table>
<%      
}
%>
