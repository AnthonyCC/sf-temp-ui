<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<SCRIPT LANGUAGE=JavaScript>
	<!--
	OAS_AD('CategoryNote');
	//-->
</SCRIPT>

<%

//**************************************************************
//***          the GENERIC_LAYOUT Pattern                    ***
//**************************************************************

//********** Start of Stuff to let JSPF's become JSP's **************


String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;
String trkCode = "";

ContentNodeModel currentFolder = null;
if(deptId!=null) {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
    isDepartment = true;
	trkCode = "dpage";
} else {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
	trkCode = "cpage";
}
boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();

//**********************  End of stuff to make JSPF's become JSP's


String altText=null;
String organicFlag = "";
String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
int counter = 0;
int tablewidth=0;
int tdwidth=0;
int maxCols = 0;
int maxWidth = 0;
Image img = null;
int itemWidth = 0;
int totalWidth = 0;
String itemNameFont= null;
StringBuffer tblCells = new StringBuffer(600);

if (isDepartment) {
    maxCols=5;
    maxWidth = 550;
    tdwidth=20;
} else {
         maxCols=4;
         maxWidth = 410;
         tdwidth=25;
}
List availableList = new ArrayList();
for(Iterator availItr = sortedColl.iterator();availItr.hasNext();) {
    Object availObject = availItr.next();
    if (availObject instanceof ProductModel && ((ProductModel)availObject).isUnavailable()) {
       continue;
    } else {
        //throw away hidden folders
        if (availObject instanceof CategoryModel && ((CategoryModel)availObject).getShowSelf()==false) continue;
        availableList.add(availObject);
    }
} 

DisplayObject displayObj = null;
// if we are on the vegetable folder: (not in a subfolder of veg) then don't show any products.
boolean inVeg = false;
int rowsPainted = 0;
if (((ContentNodeModel)currentFolder).getFullName().toUpperCase().indexOf("VEGETABLES")>=0
  || ((ContentNodeModel)currentFolder).getFullName().toUpperCase().indexOf("SEAFOOD")>=0
) {
    inVeg = true;
}
%>

<jsp:include page="/includes/department_peakproduce.jsp" flush="true"/> 

<% if (isDepartment && "fru".equals(deptId) ) {%>
    <BR><img src="/media_stat/images/layout/ourfreshfruit.gif" name="greatRightNow" border="0">
<%}%>
<table WIDTH="<%=maxWidth%>" CELLPADDING="0" CELLSPACING="0" BORDER="0">

<logic:iterate id="displayThing" collection="<%= availableList %>" type="com.freshdirect.fdstore.content.ContentNodeModel">
<%
Attribute attribInGenericLayout = null;
//Object displayThing= hashMapThing.get("item");
itemWidth = 0;
boolean showSelf = false;
if (displayThing instanceof DepartmentModel) {
    showSelf = true;
} else if (displayThing instanceof CategoryModel) {
    showSelf = ((CategoryModel)displayThing).getShowSelf();
}

if (inVeg && displayThing instanceof ProductModel) continue;  //skip products if on veg Dept Folder
itemNameFont = "text11";
if ((displayThing instanceof ProductModel) ||  showSelf) {
    if (displayThing instanceof ProductModel) {
        if(theOnlyProduct==null) {
            theOnlyProduct = (ProductModel)displayThing;
            onlyOneProduct=true;
        } else {
            onlyOneProduct = false;
        }
        itemNameFont = "catPageProdNameUnderImg";
        displayObj = JspMethods.loadLayoutDisplayStrings(response,displayThing.getParentNode().getContentName(),displayThing,prodNameAttribute,true);
        img = ((ProductModel)displayThing).getCategoryImage();
    } else if (displayThing instanceof CategoryModel){
        onlyOneProduct=false;
        displayObj = JspMethods.loadLayoutDisplayStrings(response,"",displayThing,prodNameAttribute);
        attribInGenericLayout = ((CategoryModel)displayThing).getAttribute("CAT_PHOTO");
        img = attribInGenericLayout==null?null:(Image)attribInGenericLayout.getValue();
    } else if (displayThing instanceof DepartmentModel){
        onlyOneProduct=false;
        displayObj = JspMethods.loadLayoutDisplayStrings(response,"",displayThing,prodNameAttribute);
        attribInGenericLayout = ((DepartmentModel)displayThing).getAttribute("DEPT_PHOTO");
        img = attribInGenericLayout==null?null:(Image)attribInGenericLayout.getValue();
    }
    if (img !=null ) {
        itemWidth = img.getWidth();
    }

    if(counter >= maxCols || (totalWidth+itemWidth) > maxWidth){
        int numOfColumns = (sortedColl.size() - counter) / maxCols;
        if (numOfColumns == 0) {
                tablewidth = ((sortedColl.size() - counter) % maxCols) * tdwidth;
        }
        else {
                tablewidth = maxCols * tdwidth;
        }
        
        if (tablewidth > maxWidth) tablewidth = maxWidth;
%>
<table WIDTH="<%=maxWidth%>" CELLPADDING="0" CELLSPACING="0" BORDER="0">
 <tr align="center" valign="top"><%=tblCells.toString()%></tr></table>
<%
      rowsPainted++;
      counter=0;
     totalWidth = 0;
     tblCells.setLength(0);
    }

    totalWidth+=itemWidth;
    tblCells.append("<td align=\"center\" width=\"");
    tblCells.append(tdwidth);
    tblCells.append("%\"><a href=\"");
    tblCells.append(displayObj.getItemURL());
	tblCells.append("&trk="+trkCode);
    tblCells.append("\"");
    tblCells.append(displayObj.getRolloverString());
    tblCells.append("><img src=\"");
    tblCells.append(displayObj.getImagePath());
    tblCells.append("\"  name=\"");
    tblCells.append(displayObj.getImageName());
    tblCells.append("\" width=\"");
    tblCells.append(displayObj.getImageWidth());
    tblCells.append("\"  height=\"");
    tblCells.append(displayObj.getImageHeight());
    tblCells.append("\" ALT=\"");
    tblCells.append(displayObj.getAltText());
    tblCells.append("\" border=\"0\"></a>");    
  %>
  <fd:ProduceRatingCheck>
  <%
   if (displayObj.getRating()!=null) { 
        tblCells.append("<br><font class=\"center\">");        
        tblCells.append("<img src=\"");
        tblCells.append("/media_stat/images/ratings/"+displayObj.getRating()+".gif");
        tblCells.append("\"  name=\"");
        tblCells.append("rating"+displayObj.getRating());
        tblCells.append("\" width=\"");
        tblCells.append("59");
        tblCells.append("\"  height=\"");
        tblCells.append("11");
        tblCells.append("\" ALT=\"");
        tblCells.append(displayObj.getAltText());
        tblCells.append("\" border=\"0\"");         
        tblCells.append(">");
        tblCells.append("</font>");
    }
   %>
   </fd:ProduceRatingCheck>
   <%   
    tblCells.append("<br><a href=\"");
    tblCells.append(displayObj.getItemURL());
	tblCells.append("&trk="+trkCode);
    tblCells.append("\"><font class=\"");
    tblCells.append(itemNameFont);
    tblCells.append("\">");
    tblCells.append(displayObj.getItemName());
    tblCells.append("</font></a>");
    if (displayObj.getPrice()!=null) { 
        tblCells.append("<br><font class=\"price\">");
        tblCells.append(displayObj.getPrice());
        tblCells.append("</font>");
    }
    tblCells.append("<br><br></td>");
    counter++;
}
%>
</logic:iterate>
<%
   if (tblCells.length() !=0) {
/* need to do some sort of adjusting so that the last rows looks ok: here's the "formula"
 * 1- if there is only one item..then size the table to the width of the item
 * 2- if the number of items is the same as the max allowable items then size the table to the maxWidth
 * 3 - if the number of items  > 1 and less than 60% of the maxWidht then size table to 60% of the maxWidth
 */
    int tblWidth = totalWidth; 
    //if (rowsPainted!=0)  {
    if (counter>=maxCols) {
            tblWidth = maxWidth;
        } else if( counter>1) {
            double myMultiplier = counter==2?.50:.75;
            if (totalWidth<(int)(myMultiplier * maxWidth)) {
                tblWidth = (int)(maxWidth * myMultiplier);
            }
        }
    //} else { // its the only row, so use maxWidth
    //    tblWidth = maxWidth;
    //}
%>
<table WIDTH="<%=tblWidth%>" CELLPADDING="0" CELLSPACING="0" BORDER="0" align="center">
 <tr align="center" valign="top"><%=tblCells.toString()%></tr></table>
<%  } 
//**** bottom Of Jsp's  *******************
if (onlyOneProduct) {
    request.setAttribute("theOnlyProduct",theOnlyProduct);
}
%>