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
String trkCode = (String)request.getAttribute("trkCode");
if (trkCode!=null && !"".equals(trkCode.trim()) ) {
	trkCode = "&trk="+trkCode.trim();
}else {
	trkCode = "";
}
Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
%>
<TABLE BORDER="0" CELLSPACING="2" CELLPADDING="2" WIDTH="400">
    <TR VALIGN="TOP">
        <TD WIDTH="65"><img src="/media_stat/images/layout/clear.gif" width="50" height="1" border="0"></TD>
        <TD WIDTH="335" VALIGN="middle"><img src="/media_stat/images/layout/clear.gif" width="174" height="1" border="0"><BR>
    </TR>
<%!
public void prodFldrList(Collection itemCollection,ContentNodeModel currentFolder,HttpServletResponse response,JspWriter out, String trkCode) throws JspException {
    String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
    ProductModel product = null;
    CategoryModel prodFolder = null;
    CategoryModel subFolder = null;
    String initImage = null;
    StringBuffer rowOut = new StringBuffer();
    String itemURL = null;
    String itemDescription = null;
    String itemAltText = null;
    String itemImage = null;
    Attribute prodFldrAttrib = null;
    Image prodFldrImg = null;
    String unAvailFontStart="";
    String unAvailFontEnd="";
    String fullNameFont="";
        for(Iterator itmItr = itemCollection.iterator(); itmItr.hasNext();){
            ContentNodeModel itmNode = (ContentNodeModel)itmItr.next();
            rowOut.setLength(0);
            rowOut.append("<TR VALIGN=\"TOP\"><TD WIDTH=\"65\">");
            unAvailFontStart = "";
            unAvailFontEnd = "";
            fullNameFont="<font CLASS=\"text13bold\">";

            if (itmNode instanceof ProductModel){  // if the obj is a product then do product stuff
                    product = (ProductModel)itmNode;

                    if(product.isUnavailable()){
                        unAvailFontStart = "<font color=\"#999999\">";
                        fullNameFont="<font CLASS=\"text13bold\" color=\"#999999\">";
                        unAvailFontEnd = "</font>";
                    }

                    prodFolder =(CategoryModel)product.getParentNode();  // go get folder
                    prodFldrImg = product.getCategoryImage();
                    itemImage = prodFldrImg.getPath();
                    itemURL=response.encodeURL("/product.jsp?productId=" + product + "&catId=" + prodFolder+trkCode);
                    itemDescription = product.getBlurb();
                    itemAltText = product.getFullName();

                    rowOut.append("<A HREF=\"");
                    rowOut.append(itemURL);
                    rowOut.append("\"><img SRC=\"");
                    if (prodFldrImg==null) {  // if no image.. then assign the clear image with a default HxW of 50
                            rowOut.append("/media/images/layout/clear.gif");
                            rowOut.append("\" width=\"50\" height=\"50\" border=\"0\">");
                    }
                    else {
                            rowOut.append(itemImage);
                            rowOut.append("\"");
                            rowOut.append(JspMethods.getImageDimensions(prodFldrImg));
                            rowOut.append(" border=\"0\" alt=\"");
                            rowOut.append(itemAltText);
                            rowOut.append("\">");
                    }
                    rowOut.append("</A>");
                    if(product.isUnavailable()) {
                        rowOut.append("<br><img src=\"/media_stat/images/template/not_available.gif\"  width=\"70\" height=\"9\" border=\"0\">");
                    }
                    rowOut.append("</TD><TD WIDTH=\"335\" VALIGN=\"middle\">");
                    rowOut.append("<A HREF=\"");
                    rowOut.append(itemURL);
                    rowOut.append("\">");
                    rowOut.append(fullNameFont);
                    rowOut.append(JspMethods.getDisplayName(product,prodNameAttribute));
                    rowOut.append("</font>");
                    rowOut.append("</A><BR>");
                    rowOut.append(unAvailFontStart);
                    rowOut.append(itemDescription);
                    rowOut.append(unAvailFontEnd);
                    rowOut.append("</TD></TR>");
            } // end if !bizobjtype = product
            else {
                    subFolder = (CategoryModel)itmNode;  // go get folder particualrs
                    prodFldrAttrib = subFolder.getAttribute("CAT_PHOTO");
                    prodFldrImg = null;
                    if (prodFldrAttrib !=null) {
                        prodFldrImg = (Image)prodFldrAttrib.getValue();
                    }
                    itemImage = prodFldrImg!=null?prodFldrImg.getPath():null;
                    itemURL = response.encodeUrl("/category.jsp?catId="+subFolder+trkCode);
                    itemDescription =subFolder.getBlurb();
                    itemAltText = subFolder.getAltText();
                    rowOut.append("<A HREF=\"");
                    rowOut.append(itemURL);
                    rowOut.append("\"><img SRC=\"");
                    if (itemImage==null) {  // if no image.. then assign the clear image with a default HxW of 50
                            rowOut.append("/media/images/layout/clear.gif");
                            rowOut.append("\" width=\"50\" height=\"50\" border=\"0\">");
                    }
                    else {
                            rowOut.append(itemImage);
                            rowOut.append("\"");
                            rowOut.append(JspMethods.getImageDimensions(prodFldrImg));
                            rowOut.append(" border=\"0\" alt=\"");
                            rowOut.append(itemAltText);
                            rowOut.append("\">");
                    }
                    rowOut.append("</A>");
                    rowOut.append("</TD><TD WIDTH=\"335\" VALIGN=\"middle\">");
                    rowOut.append("<A HREF=\"");
                    rowOut.append(itemURL);
                    rowOut.append("\">");

                    prodFldrAttrib = subFolder.getAttribute("CAT_LABEL");
                    prodFldrImg = new Image();
                    itemImage=null;
                    if (prodFldrAttrib !=null) {
                        prodFldrImg = (Image)prodFldrAttrib.getValue();
                        itemImage=prodFldrImg.getPath();
                        rowOut.append("<img SRC=\"");
                        rowOut.append(itemImage);
                        rowOut.append("\" ");
                        rowOut.append(JspMethods.getImageDimensions(prodFldrImg));
                        rowOut.append(" border=\"0\" alt=\"");
                        rowOut.append("\">");
                    }else {
                        rowOut.append("<FONT CLASS=\"text13bold\">"); 
                        rowOut.append(subFolder.getFullName());
                        rowOut.append("</font>");
                    }
                    rowOut.append("</A><BR>");
                    rowOut.append(itemDescription);
                    rowOut.append("</TD></TR>");
            }
            try {
            out.print(rowOut.toString());
            }catch (IOException ioe) {
                throw new JspException("IOException caught in prodFldrList method in layout: product_folder_list.jsp\n"+ioe.getMessage());
            }
    }
}
%>
<%
List availableList = new ArrayList();
List unAvailableList = new ArrayList();
boolean hasAFolder = false;
for(Iterator availItr = sortedColl.iterator();availItr.hasNext();) {
    Object itemObject = availItr.next();
        
    if (itemObject instanceof ProductModel) {
        ProductModel pm = (ProductModel)itemObject;
        if (pm.isUnavailable()) {
           unAvailableList.add(pm);
        } else {
            availableList.add(pm);
        }
    } else { 
        availableList.add(itemObject);
        hasAFolder = true;
        onlyOneProduct = false;
    }
}
if (!hasAFolder && unAvailableList.size()+availableList.size() ==1) {
    theOnlyProduct = availableList.size()>0 ? (ProductModel)availableList.get(0) : (ProductModel)unAvailableList.get(0);
    onlyOneProduct=true;
	request.setAttribute("theOnlyProduct",theOnlyProduct);
}

// now process each list.
prodFldrList(availableList,currentFolder,response,out,trkCode);
if (unAvailableList.size() > 0) {
%>
<tr><TD WIDTH="400" colspan="2"><br><Font color="#999999"><b>Currently Unavailable</b></font><br></td>
<%
    prodFldrList(unAvailableList,currentFolder,response,out,trkCode);
}
%>
</table>
