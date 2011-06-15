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


<%

//********** Start of Stuff to let JSPF's become JSP's **************
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
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



Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
%>
<script language="javascript" src="/assets/javascript/transac_multi_cat.js"></script>

<%
//**************************************************************
//***   the Transactional Multi Category Layout        ***
//**************************************************************

int itemTotal = sortedColl.size();
int cols = 0;
int maxWidth;
int newCategoryCount = 0;
boolean firstProduct = false;

if (request.getRequestURI().toLowerCase().indexOf("department.jsp")!=-1) {
    maxWidth = 550;
} else {
    maxWidth = 400;
}

String itemNameFont = null;
Image itemImage;
String currentFolderPKId = currentFolder.getContentKey().getId();
String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
CategoryModel cat = null;
DisplayObject displayObj = null;

//prod specs
int idx =-1;
boolean prodUnavailable = true;
String qtyFldName = null;
SkuModel defaultSku = null;
String salesUnitDescription = "";
String salesUnitName = "NA";

List sortedList = new ArrayList();
//convert collection to list
for(Iterator collIter = sortedColl.iterator();collIter.hasNext();) {
     Object currItem = collIter.next();
     if (currItem instanceof ProductModel && ((ProductModel)currItem).isUnavailable()) continue;
     sortedList.add(currItem);
} 

int itemsToDisplay = sortedList.size();
%>

<%
	// sku list for pricing
	List skus = new ArrayList();
    List salesUnitNames = new ArrayList();

	// setup for succpage redirect ....
	request.setAttribute("successPage","/grocery_cart_confirm.jsp?catId="+request.getParameter("catId"));
%>


<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= "/grocery_cart_confirm.jsp?catId="+request.getParameter("catId") %>'>
<form name="transac_multi_cat" method="POST">
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
	</td></tr>
 <%   
	}
%>
	</table>
<%
}
%>
<%
for (int itemIndex=0; itemIndex < sortedList.size();itemIndex++) {
     ContentNodeModel contentNode = (ContentNodeModel)sortedList.get(itemIndex);
     itemNameFont = "text11";
     if (contentNode instanceof CategoryModel){
          itemTotal--;
          cat = (CategoryModel)contentNode;
          firstProduct = true; //first prod under this
          
          if (cat.getParentNode()!=null && cat.getParentNode().getContentKey().getId().equals(currentFolderPKId)) {
          //dont print heading for categories that are empty..so peek ahead to see if there is an item that it's child
            int peekAhead = itemIndex +1;
            if (peekAhead == sortedList.size()) {
                continue;
            } else {
                if ( ((ContentNodeModel)sortedList.get(peekAhead)).getParentNode().getContentKey().getId().equals(currentFolderPKId)) {
                    continue;
                }
            }
%>
<%
               if (newCategoryCount > 0) {  // close out previous table
                    if (cols ==1) { //finish out col %>
                         <td width="50%" colspan="3">&nbsp;</td>
<%                cols = 0; //reset for next category
                    }
               %>
                     
                    </table>
</td>
</tr>
<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>
</table>
<br>
<%
               }    %>

<%
          newCategoryCount++;
          // get the category_top attribute to display
          Image catHeader = cat.getCategoryLabel();
          if (catHeader != null) {
%>
          <table width="<%=maxWidth%>"  align="center" cellpadding="0" cellspacing="0" border="0">
          <tr><td>
          <img src="<%=catHeader.getPath()%>" width="<%=catHeader.getWidth()%>" height="<%=catHeader.getHeight()%>">
          <br><img src="media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="4"><br>
          <%= cat.getBlurb() %>
          </td></tr>
          </table>
          <br>
<%
          }
          %>
          
<%
          continue;
          } else {
          //if category not child of top folder
               displayObj = JspMethods.loadLayoutDisplayStrings(response,"",cat,prodNameAttribute);
          }

     } else {
          itemTotal--;
     //!contentNode instanceof CategoryModel
          if (newCategoryCount == 0) { //hasProduct, no Category
               firstProduct =true;
               newCategoryCount = 1; //pretend we have a category
          } 
          itemNameFont="catPageProdNameUnderImg";
          ProductModel product = (ProductModel)contentNode;
          cat = (CategoryModel)product.getParentNode();
          displayObj = JspMethods.loadLayoutDisplayStrings(response,product.getParentNode().getContentName(),product,prodNameAttribute,true);
   
          if (firstProduct) { //open table show img
%>
               <table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
               <tr valign="top">
               <td width="100"><img src="<%= displayObj.getImagePath()%>" alt="<%=displayObj.getAltText()%>" border="0" name="<%=cat%>_prodImg"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="12"></td>
               <td>
                    <table width="100%" cellpadding="0" cellspacing="3" border="0">
<%
               firstProduct = false; //negate after painted
          }
          if (cols == 0) {
%>
                         <tr valign="top">
<%
          } %>
<%
               if (cols < 2) { 
               prodUnavailable = product.isUnavailable();
                    if (!prodUnavailable){
                         idx++;
                    }
               qtyFldName = "quantity_"+idx;
               defaultSku = !prodUnavailable?product.getDefaultSku():(SkuModel)product.getSkus().get(0);
%>
                    <fd:FDProductInfo id="productInfo" skuCode="<%= defaultSku.getSkuCode() %>">
<%
               FDProduct fdProduct = null;

               try {
                    fdProduct = FDCachedFactory.getProduct(productInfo);
                    FDSalesUnit[] salesUnits = fdProduct.getSalesUnits();
                         if (salesUnits.length>0) {
                         salesUnitDescription = " ("+salesUnits[0].getDescription()+")";
                         salesUnitName = salesUnits[0].getName();
                         salesUnitNames.add(salesUnitName);
                         }
               } catch (FDSkuNotFoundException fdsnf){
                    JspLogger.PRODUCT.warn("Multi Cat Transac Page: catching FDSkuNotFoundException and Continuing:\n FDProductInfo:="+productInfo+"\nException message:= "+fdsnf.getMessage());
        }
%>
           
                         <td width="30"><img src="/media_stat/images/layout/clear.gif" width="1" height="2"><br><% if (prodUnavailable) {%><div align="center"><font color="#999999">NA</font></div><%} else {%><input type="text" name="<%= qtyFldName %>" size="2" maxlength="2" class="text11" value="<%= prodUnavailable ? "" : request.getParameter(qtyFldName) %>" onChange="javascript:chgQty(<%=idx%>,'<%= qtyFldName %>', 0, <%= product.getQuantityMinimum() %>, <%= user.getQuantityMaximum(product) %>);"><%}%></td>
	                    <td width="10"><% if (!prodUnavailable) {%><A HREF="javascript:chgQty(<%=idx%>,'<%= qtyFldName %>',<%= product.getQuantityIncrement() %>,<%= product.getQuantityMinimum() %>,<%= user.getQuantityMaximum(product) %>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="2" alt="Increase quantity"></A><br>
	                    <A HREF="javascript:chgQty(<%=idx%>,'<%= qtyFldName %>',-<%= product.getQuantityIncrement()%>,<%= product.getQuantityMinimum() %>,<%= user.getQuantityMaximum(product) %>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="2" alt="Decrease quantity"></A><%} else {%><img src="/media_stat/images/layout/clear.gif" width="10" height="1"><%}%></td>
                         <td width="50%" valign="top">
                         <a href="javascript:popup('prod_desc_popup.jsp?catId=<%=(product.getParentNode()).getContentName()%>&prodId=<%=product.getContentName()%>','small');" onMouseover="swapImage('<%=cat%>_prodImg','<%= displayObj.getImagePath()%>')"><font class="<%=itemNameFont%>">
                         <% if (prodUnavailable) {%><font color="#999999"><%}%><%=displayObj.getItemName()%><% if (product.isUnavailable()) {%></font><%}%></font></a><%=salesUnitDescription%>
                         <%  if (displayObj.getPrice()!=null) { %>
                         <br><font class="price"><%=displayObj.getPrice()%></font>
                         <%  } %>
                         <br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
                         <% if (!prodUnavailable) {

			               skus.add( defaultSku );

                         %>
                              <input type="hidden" name="estPrice_<%=idx%>" value="">
                              <input type="hidden" name="salesUnit_<%= idx %>" value="<%= salesUnitName %>">
                         	<input type="hidden" name="skuCode_<%= idx %>" value="<%= defaultSku %>">
                         	<input type="hidden" name="catId_<%= idx %>" value="<%= product.getParentNode() %>">
                         	<input type="hidden" name="productId_<%= idx %>" value="<%= product %>">
                         <%}%>
                    </fd:FDProductInfo>
<%
                         if (cols==0) { //paint separator cell
%>                         
                         <td><img src="/media_stat/images/layout/clear.gif" width="4" height="1"></td>                         

<%
                    }
               cols++;
               }
               
           if (cols == 2) {
%>
               </tr>
<% 
          cols = 0;
          } 
          %>

<%
     }

} //close for loop
                    if (cols ==1) { //finish out col %>
                         <td width="50%" colspan="3">&nbsp;</td>
<%                cols = 0; //reset
                    } %>
<%@ include file="/shared/includes/product/i_pricing_script.jspf" %>

<script>
     function tmc_updateTotal() {
     	var total=0;
     	var p;
     	for(i=0; i<=<%=idx%>; i++ ){
     		p = eval("pricing"+i+".getPrice()");
     		if (p!="") {
     			total+=new Number(p.substring(1));
     		}
     	}
     	document.forms["transac_multi_cat"]["total"].value="$"+currencyFormat(total);
     }     
<% 
// build the pricing object for each of the products
idx=0;
for (Iterator itrItms = sortedList.iterator(); itrItms.hasNext(); ) {
     ContentNodeModel cn = (ContentNodeModel) itrItms.next();
     if (!(cn instanceof ProductModel)  || ((ProductModel)cn).isUnavailable()) continue;
     
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
<%
if (newCategoryCount > 0) {
%>
     </table>
</td>
</tr>
</table>
<br>
<%}%>
<table width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0" align="center">
<tr><td colspan="3"><img src="media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="6"></td></tr>
<tr>
<td width="80">&nbsp;&nbsp;<b>Total Price:</b>&nbsp;</td>
<td><input type="text" name="total" size="8" maxlength="8" class="text11" value="" onFocus="blur()"></td>
<td align="right"><input type="image" name="addMultipleToCart" src="media_stat/images/buttons/add_to_cart.gif" width="93" height="20" hspace="4" vspace="4" border="0" alt="ADD SELECTED ITEMS TO CART"></td></tr>
<tr><td colspan="3"><img src="media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="6"></td></tr>
</table>
<br>
</form>
</fd:FDShoppingCart>
