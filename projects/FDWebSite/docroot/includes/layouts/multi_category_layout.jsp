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

String catId = request.getParameter("catId");
String deptId = request.getParameter("deptId");
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
if(deptId!=null) {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
    isDepartment = true;
} else {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
}


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");
if ("".equals(trkCode) || trkCode == null) {
	trkCode = "cpage";
}

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
//**************************************************************
//***          the Multi Category Layout                     ***
//**************************************************************

int imgWidths = 0;
int maxWidth;
int newCategoryCount = 0;
boolean newCategory = false;
StringBuffer catMediaOut = new StringBuffer(200);

if (request.getRequestURI().toLowerCase().indexOf("department.jsp")!=-1) {
    maxWidth=550;
} else {
    maxWidth = 380;
}

String itemNameFont = null;
Image itemImage;
String itemAltText = null;
String itemLabel = null;
String itemUrl = null;
String itemPrice = null;
List availableList = new ArrayList();
Map categoryItemCount = new HashMap();
SkuModel dfltSku = null;
String currentFolderPKId = currentFolder.getPK().getId();
String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
CategoryModel cat = null;

for(Iterator availItr = sortedColl.iterator();availItr.hasNext();) {
    Object availObject = availItr.next();
    if (availObject instanceof ProductModel && ((ProductModel)availObject).isUnavailable()) {
       continue;
    } else {
        availableList.add(availObject);
    }
}

imgWidths =0;
itemPrice= null;
DisplayObject displayObj = null;
boolean needToCloseTable=false;
for(int itmIdx=0; itmIdx < availableList.size();itmIdx++) {
    ContentNodeModel contentNode = (ContentNodeModel)availableList.get(itmIdx);
    itemNameFont = "text11";
     if (contentNode instanceof CategoryModel){
        cat = (CategoryModel)contentNode;
        if (cat.getParentNode()!=null && cat.getParentNode().getPK().getId().equals(currentFolderPKId)) {
            //we dont want to print heading for categories that are empty..so peek ahead to see if there is an item that it's child
            int peekAhead = itmIdx+1;
            if (peekAhead == availableList.size()) {
                continue;
            } else {
                if ( ((ContentNodeModel)availableList.get(peekAhead)).getParentNode().getPK().getId().equals(currentFolderPKId)) {
                    continue;
                }
            }
            if (newCategoryCount > 0) {  // print the seperator bar if one or more categories has been printed  %>
               </tr></table>
                <table WIDTH="<%=maxWidth%>"  ALIGN="CENTER" CELLPADDING="0" CELLSPACING="0" BORDER="0">
                <tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"></td></tr>
                <tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
                <tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr></table>
<%          }  %>
            <table WIDTH="<%=maxWidth%>"  ALIGN="CENTER" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<%
            needToCloseTable = true;
            newCategoryCount++;
                        imgWidths =0;
            // get the category_top attribute to display
            MultiAttribute catTop = (MultiAttribute) cat.getAttribute("CATEGORY_TOP_MEDIA");
            if (catTop!=null) {
                MediaI catTopMedia = (MediaI)catTop.getValue(0);
                if (catTopMedia instanceof Image) {
%>
                    <tr><td WIDTH="100%" align="center">
                        <img src="<%=catTopMedia.getPath()%>" <%=JspMethods.getImageDimensions((Image)catTopMedia) %>>
                    </td></tr>
<%              } else {    %>
                     <tr><td WIDTH="100%" align="center">
                        <fd:IncludeMedia name='<%= catTopMedia.getPath()%>' />
                     </td></tr>
<%              } %>
                     <tr><td><br><br></td></tr>


<%          } %>
             </table>
            <table CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr valign="top">
<%
            continue;
        } else {
            displayObj = JspMethods.loadLayoutDisplayStrings(response,"",cat,prodNameAttribute);
            onlyOneProduct=false;
        }
     } else {
        if(theOnlyProduct==null) {
            theOnlyProduct = (ProductModel)contentNode;
            onlyOneProduct=true;
        } else {
            onlyOneProduct = false;
        }
        itemNameFont="catPageProdNameUnderImg";
        ProductModel product = (ProductModel)contentNode;
        cat = (CategoryModel)product.getParentNode();
        displayObj = JspMethods.loadLayoutDisplayStrings(response,product.getParentNode().getContentName(),product,prodNameAttribute,true);
    }
    if (needToCloseTable==false) {
    	needToCloseTable=true;  // we have not opened a table as yet  
%>
                <table CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr valign="top">
<%    }    
    //if we are about to xceed the limits of the witdth then  start a new row.
    if ((displayObj!=null && imgWidths + displayObj.getImageWidthAsInt() > maxWidth)) {
        imgWidths =0;
%>  </tr></table>
    <table CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr valign="top">
<%
    }
    imgWidths+=displayObj.getImageWidthAsInt();
    int adjustedImgWidth = displayObj.getImageWidthAsInt()+6;
%>
    <td valign="top" align="center" WIDTH=<%=adjustedImgWidth%>">
        <a href="<%=displayObj.getItemURL()%>&trk=<%=trkCode%>"><img src="<%= displayObj.getImagePath()%>"  <%=displayObj.getImageDimensions() %> ALT="<%=displayObj.getAltText()%>" hspace="0" border="0"></a>
        <br>
        <a href="<%=displayObj.getItemURL()%>&trk=<%=trkCode%>"><font class="<%=itemNameFont%>"><%=displayObj.getItemName()%></font></a>
<%  if (displayObj.getPrice()!=null) { %>
            <br><font class="price"><%=displayObj.getPrice()%></font>
<%  } %>
   <br><br></td>
<%
}
if (onlyOneProduct) {
    request.setAttribute("theOnlyProduct",theOnlyProduct);
}
if (needToCloseTable ) { %>
</tr></table>
<%}%>