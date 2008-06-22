<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.ErpServicesProperties' %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
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

ContentNodeModel currentFolder = null;
if(deptId!=null) {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
} else {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
}

CategoryModel categoryModel=null;


if(currentFolder instanceof CategoryModel)
{
   categoryModel=(CategoryModel)currentFolder;
}
 
Collection sortedColl = null;
Collection subCatColl = null;
int cols = 0;
int maxWidth;
int newCategoryCount = 0;
boolean firstProduct = false;

String perfectEditPath="";
String perfectTitle="";
if(categoryModel!=null){
Html perfectDesc=categoryModel.getEditorial();
   if(perfectDesc!=null){
      perfectEditPath=perfectDesc.getPath();
   }
   perfectTitle=categoryModel.getEditorialTitle();
}


// setup for succpage redirect ....
request.setAttribute("successPage","/grocery_cart_confirm.jsp?catId="+request.getParameter("catId"));
%>   


<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= "/grocery_cart_confirm.jsp?catId="+request.getParameter("catId") %>'>    

    <div class="left">
	<span class="center_prod_name w100"><%=perfectTitle%></span>
    </div>
    <div class="left">
	    <fd:IncludeMedia name="<%=perfectEditPath%>" />
    </div>
<div class="cat_sum_top_spacer"></div>
<div class="cat_sum_top_sortbar left">	
</div>
<%
subCatColl = (Collection) request.getAttribute("itemGrabberResult");
if (subCatColl==null) subCatColl = new ArrayList();

List subCategoryList = new ArrayList();

int catIndex=0;

System.out.println("perfect pirs list***************************** :"+subCatColl.size());


for(Iterator CatIter = subCatColl.iterator();CatIter.hasNext() ;) {

catIndex++;

        Object catItem = CatIter.next(); 
    if (catItem instanceof CategoryModel) {   
      CategoryModel currentCat=(CategoryModel)catItem ;  
      subCategoryList.add(currentCat);
      sortedColl = currentCat.getProducts();    



%>

<script>
    function chgQty<%=catIndex%>(idx,qtyFldName,delta,min,max) {
        alert("entering here"+qtyFldName);
        var qty = parseFloat(document.wine_perfect_form_<%=catIndex%>[qtyFldName].value)
        if (isNaN(qty)) qty=0;
        if (qty<1) qty=0;
        qty = qty + delta;

        if (qty < min  && qty!=0) return;
        if (qty > max) return;

        if (qty<=0) {
            qty=0;
            document.wine_perfect_form_<%=catIndex%>[qtyFldName].value='';
        } else {
            document.wine_perfect_form_<%=catIndex%>[qtyFldName].value = qty;
        }

      //   var pricingObj = eval ("pricing"+idx);
     //   pricingObj.setQuantity(qty);
  }

    function updatePriceField() {
        //document.wine_detail_form.total.value = pricing.getPrice();
    }
    
  
    

</script>


<form name="wine_perfect_form_<%=catIndex%>" method="POST">

<%
//**************************************************
//***   the Transactional Grouped Items          ***
//**************************************************

int itemTotal = sortedColl.size();

System.out.println("sortedColl :"+sortedColl);

if (request.getRequestURI().toLowerCase().indexOf("department.jsp")!=-1) {
    maxWidth = 550;
} else {
    maxWidth = 400;
}

String itemNameFont = null;
Image itemImage;
String currentFolderPKId = currentFolder.getPK().getId();
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


String ediDescPath="";
Html htmlDesc=currentCat.getEditorial();
   if(htmlDesc!=null){
      ediDescPath=htmlDesc.getPath();
   }

String catDetailImagePath="";
     Image catImage=currentCat.getCategoryDetailImage();
     if(catImage!=null){
        catDetailImagePath=catImage.getPath();      
     }

if (prodsAvailable>0) {
    %>                  
    
    
    	<div class="cat_sum_top_spacer"></div>
<table class="w100 left">
	<tr>
		<td class="padlr3">
			<div class="center_prod_name text_red">
				<span>Parfect Pair #</span><span><%=catIndex%></span>
			</div>
			<div class="center_prod_name">
				<a href="/category.jsp?catId=<%=currentCat.getPK().getId()%>&trk=cPage"><%=currentCat.getFullName()%></a>
			</div>
			<div class="padt6">
				<fd:IncludeMedia name="<%=ediDescPath%>" />
			</div>
			<div class="padt6"><!-- items -->            
  

    <input type="hidden" name="enableWineSubmit_<%=catIndex%>" value="false">
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
    <table class="center_qty_table left">
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
        String qtyFldName = "quantity_"+itemShownIndex*catIndex;
    %>
    <td width="40" align="center"><NOBR>
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
        onChange="javascript:chgQty<%=catIndex%>(<%=itemShownIndex%>,'<%= qtyFldName %>', 0, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);">

    </NOBR></td>
    <td width="10">
        <%  if (prodUnAvailable) {  %>&nbsp;<%  } else {    %>
            <A HREF="javascript:chgQty<%=catIndex%>(<%=itemShownIndex%>,'<%= qtyFldName %>', <%= displayProduct.getQuantityIncrement() %>, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);">
            <img SRC="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="2" alt="Increase quantity"></A><BR>
            <A HREF="javascript:chgQty<%=catIndex%>(<%=itemShownIndex%>,'<%= qtyFldName %>', -<%= displayProduct.getQuantityIncrement() %>, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);">
            <img SRC="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="2" alt="Decrease quantity"></A>
        <%  }   %>
    </td>
    <td width="290">
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
        <%= unAvailableFontStart %><A HREF="<%= productURL %>">
        <FONT CLASS="text10bold"><%= unAvailableFontStart %><%= thisProdBrandLabel %><%= unAvailableFontEnd %></FONT>
        <FONT CLASS="text10"><%= unAvailableFontStart %><%= displayProduct.getFullName().substring(thisProdBrandLabel.length()).trim() %><%= unAvailableFontEnd %></FONT>                
            <%  if (prodUnAvailable) {  %>
        <FONT CLASS="text10">Not&nbsp;Available</FONT>
    <%  } else {    %>
        <FONT CLASS="text10"><%= "   -"+JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()) %>&nbsp;</font>
    <%  }   %>
        <%= unAvailableFontStart %><NOBR>/ <%= salesUnitDescription %></NOBR><BR><%= unAvailableFontEnd %>
        </A>
        
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
        <input type="hidden" name="salesUnit_<%= itemShownIndex %>" value="<%= salesUnitName %>">
        <input type="hidden" name="skuCode_<%= itemShownIndex %>" value="<%= displayProduct.getSku(0).getSkuCode() %>">
        <input type="hidden" name="catId_<%= itemShownIndex %>" value="<%= displayProduct.getParentNode() %>">
        <input type="hidden" name="productId_<%= itemShownIndex %>" value="<%= displayProduct %>">   
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
            //document.forms["wine_detail_form"]["total"].value="$"+currencyFormat(total);
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
    
   </td>  
    <td class="padlr3 vtop">
				<img src="<%=catDetailImagePath%>" width="120" height="150" alt="" />
    </td>
   	</tr>
</table>
    
    <table class="w100">
	<tr>
		<td class="left">
			<input type="image" name="addMultipleToCart<%=catIndex%>" src="media_stat/images/buttons/add_to_cart.gif" width="93" height="20" hspace="4" vspace="4" border="0" alt="ADD SELECTED ITEMS TO CART">
            <fd:CCLCheck>
              <fd:CCLNew/>
                 <a href="/unsupported.jsp" onclick="return CCL.save_items('qs_cart',this,'action=CCL:AddMultipleToList&source=ccl_actual_selection','source=ccl_actual_selection')"><img src="/media_stat/ccl/lists_link_selected_dfgs.gif" width="112" height="13" style="border: 0; padding-left: 14px"><img src="/media_stat/ccl/lists_save_icon_lg.gif" width="12" height="14" style="margin: 0 0 1px 5px; border: 0"/></a>   		     		         
                </fd:CCLCheck>                        
		</td>
		<td class="right">
			<span class="ital text10 bold"><%=currentCat.getBlurb()%></span>
		</td>
	</tr>
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
</form>
<%
// end of category loops

  } 
 }   
%>

</fd:FDShoppingCart>
