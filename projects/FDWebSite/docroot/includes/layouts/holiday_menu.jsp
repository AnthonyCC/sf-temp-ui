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
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<% //expanded page dimensions
final int W_HOLIDAY_MENU_TOTAL = 601;
final int W_HOLIDAY_MENU_LEFT = 519;
final int W_HOLIDAY_MENU_PADDING = 14;
final int W_HOLIDAY_MENU_RIGHT = 68;
%>

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


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");


Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
%>
<%!
        JspMethods.ContentNodeComparator contentNodeComparator = new JspMethods.ContentNodeComparator();
%>

<%
//**************************************************************
//***         the Menu Layout Pattern                       ***
//**************************************************************
	if (trkCode!=null && !"".equals(trkCode.trim()) ) {
		trkCode = "&trk="+trkCode.trim();
	}else {
		trkCode = "";
	}

	ContentNodeModel owningFolder = currentFolder;

    boolean showPrices = true;
     String clearImage = "/media_stat/images/layout/clear.gif"; 
    String unAvailableImg="/media_stat/images/template/not_available.gif";
    ContentNodeModel parentNode = currentFolder.getParentNode();
    while (parentNode!=null && !(parentNode instanceof DepartmentModel)) {
        parentNode = parentNode.getParentNode();
    }

    String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
 // don't display any heading if the favorite collection is empty or the products are unavailable.

    boolean folderAsProduct = false;
    ContentNodeModel aliasNode = null;
    ContentNodeModel prodParent = null;
    Comparator priceComp = new ProductModel.PriceComparator();

    CategoryModel displayCategory = null;
    StringBuffer appendColumn = new StringBuffer(200);
    StringBuffer appendColumnPrices = new StringBuffer(200);
    boolean headingShown = false;
%>
    <logic:iterate id='contentNode' collection="<%=sortedColl%>" type="com.freshdirect.storeapi.content.ContentNodeModel">
<%
      if (displayCategory==null) {
            if (contentNode.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
               displayCategory=(CategoryModel)currentFolder;
            } else {
              displayCategory=(CategoryModel)contentNode;
            }
        }

        folderAsProduct = false;
        if (contentNode.getContentType().equals(ContentNodeModel.TYPE_CATEGORY)) {
            CategoryModel category = (CategoryModel) contentNode;
            folderAsProduct = category.getTreatAsProduct();

            aliasNode = category.getAlias();
        }

        if (contentNode.getContentType().equals(ContentNodeModel.TYPE_CATEGORY) && !folderAsProduct) {
           if (headingShown) {  %>
                    </table>
<%         }

            headingShown = false;
            owningFolder = (CategoryModel)contentNode;
            onlyOneProduct = false;
            displayCategory=(CategoryModel)contentNode;
	    }//end of folder instance check
        else {  // must be a product. we'll need the usual stuff, and possibly the price
            ProductModel product = null;

            if (folderAsProduct ){
                if (aliasNode instanceof CategoryModel){
                    onlyOneProduct = false;
                }
            } else if (contentNode.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)){
                product = (ProductModel)contentNode;
                if(product.isUnavailable()) continue;

                owningFolder = (CategoryModel) product.getParentNode();
                if (theOnlyProduct!=null) {
                    onlyOneProduct=false;
                }else {
                    onlyOneProduct=true;
                    theOnlyProduct = product;
                }

            }

            appendColumn.setLength(0);
            appendColumnPrices.setLength(0);
            String lstUnitPrice = null;

            if (!folderAsProduct) {
                List skus = product.getSkus();
                for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
                        SkuModel sku = (SkuModel)li.next();
                if ( sku.isUnavailable() ) {
                                li.remove();
                        }
                }
                int skuSize = skus.size();

                SkuModel sku = null;
                String prodPrice = null;
                  // skip this item..it has no skus.  Hmmm?
                if (skuSize==1) {
                    sku = (SkuModel)skus.get(0);  // we only need one sku
                }
                else if (skus.size() > 1) {
                    sku = (SkuModel) Collections.min(skus, priceComp);
                }
              if (sku!=null) {
%>
                    <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<%
                    lstUnitPrice = "<font class=\"price\">"+JspMethods.formatPrice(productInfo, user.getPricingContext())+"</font>";
%>
                    </fd:FDProductInfo>
<%              }
            }
            if (lstUnitPrice==null ) {
                lstUnitPrice = "&nbsp;";
            } else if (product.isUnavailable()){
                lstUnitPrice = "Not Avail.";
            }
            if (folderAsProduct ) {
                if (aliasNode !=null) {
                    String fldrURL = response.encodeURL("/category.jsp?catId=" + aliasNode+trkCode);
                    appendColumn.append("<div style=\"margin-left: 8px; text-indent: -8px;\"><A HREF=\"");
                    appendColumn.append(fldrURL);
                    appendColumn.append("\" ");
                    appendColumn.append(">");
                    appendColumn.append(aliasNode.getFullName());
                    appendColumn.append("</A>");
                    appendColumnPrices.append("&nbsp;");
                }
            } else {
                String theProductName = JspMethods.getDisplayName(product,prodNameAttribute);
                String theBrandName = product.getPrimaryBrandName();
               // String theBrandName = product.getPrimaryBrandName(theProductName);
                if (theBrandName!=null && theBrandName.length()>0 && theProductName.startsWith(theBrandName)) {
                    String shortenedProductName = theProductName.substring(theBrandName.length()).trim();
                    //theProductName = "<b>"+theBrandName + "</b> " + theProductName.substring(theBrandName.length()).trim();
                    theProductName = "<b>"+theBrandName + "</b> " + shortenedProductName;
                }

                String prodURL = response.encodeURL("/product.jsp?productId=" + product + "&catId=" + owningFolder+trkCode);
                appendColumn.append("<div style=\"margin-left: 8px; text-indent: -8px;\"><A HREF=\"");
                appendColumn.append(prodURL);
                appendColumn.append("\">");
                appendColumn.append(theProductName);
                appendColumn.append("</A></div>");
                appendColumnPrices.append(lstUnitPrice);
            }
            // display it //
            if (!headingShown) {   %>
                <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="<%=W_HOLIDAY_MENU_TOTAL%>">
                <TR VALIGN="MIDDLE">
                	<TD WIDTH="<%=W_HOLIDAY_MENU_TOTAL%>" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="12" border="0"></TD>
                </TR>
                <TR VALIGN="MIDDLE">
                	<TD ALIGN="CENTER" WIDTH="<%=W_HOLIDAY_MENU_TOTAL%>" COLSPAN="4" CLASS="title12"><font color="#FF9933"><%=displayCategory.getFullName().toUpperCase()%></font></TD>
                </TR>
                <TR VALIGN="MIDDLE"><TD WIDTH="<%=W_HOLIDAY_MENU_TOTAL%>" COLSPAN="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="2" alt="" border="0"></TD></TR>
                <TR VALIGN="MIDDLE">
                	<TD WIDTH="<%=W_HOLIDAY_MENU_TOTAL%>" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="4"></TD>
                </TR>
                </TABLE>

                <%-- set up for the rows of products --%>
                <TABLE CELLSPACING="0" CELLPADDING="1" BORDER="0" WIDTH="<%=W_HOLIDAY_MENU_TOTAL%>">
 <%              headingShown = true;
             }  %>
             <tr valign="top">
                <TD  align="left" width="<%=W_HOLIDAY_MENU_LEFT%>"><%=appendColumn.toString()%></td>
                <TD width="<%=W_HOLIDAY_MENU_PADDING%>">&nbsp;</td>
                <TD align="right" width="<%=W_HOLIDAY_MENU_RIGHT%>" class="text11bold"><%=appendColumnPrices.toString()%>&nbsp;</td>
             </tr>
<%
    }// end of Product instance check
%>
 </logic:iterate>
<%
  if (headingShown) {//need to close table tag  %>
  </TABLE>
<%  }

if (onlyOneProduct) {
	request.setAttribute("theOnlyProduct",theOnlyProduct);
}
%>
