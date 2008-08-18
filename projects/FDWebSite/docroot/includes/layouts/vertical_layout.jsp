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
<%

//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;
String pageTrkCode = "";

ContentNodeModel currentFolder = null;
if(deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
	isDepartment = true;
	pageTrkCode = "dpage";
} else {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
	pageTrkCode = "cpage";
}


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
int cellWidth = 100;  // default cell width
int tblWidth = 0;
//**********************  End of stuff to make JSPF's become JSP's
//**************************************************************
//***          Vertical Layout Pattern                       ***
//**************************************************************
final int PRODUCT_CELL_WIDTH=105;
final int MAX_COLUMNS = 4;	
boolean firstRow = true;
boolean showSeparator[] = null; 
List availableList = new ArrayList();
String itemNameFont = "text11";
for(Iterator availItr = sortedColl.iterator();availItr.hasNext();) {
    Object availObject = availItr.next();

    if (availObject instanceof ProductModel && ((ProductModel)availObject).isUnavailable()) {
       continue;
    } else {
        availableList.add(availObject);
    }
}

if (availableList.size() > 0) {
    
%>
<fd:VerticalPattern collection='<%=availableList%>' id='itemsToDisplay' showFolder='false'  columns='<%= MAX_COLUMNS %>' >
<%
//    boolean showSeparator[] = new boolean[itemsToDisplay.length-1];
	if (itemsToDisplay.length>0) {
        tblWidth = PRODUCT_CELL_WIDTH * itemsToDisplay.length;
        // determine, one time, where the seperators should be displayed.
        if (firstRow) {
            firstRow = false;  
            boolean isLastItemNull=false;
            int colSpan = 0;
            showSeparator = new boolean[itemsToDisplay.length-1];
            
            for(int x=0; x<itemsToDisplay.length; x++) {
            	ContentNodeModel contentNode = itemsToDisplay[x];
                if (contentNode==null) {
                    if (x>=showSeparator.length) continue;
                    showSeparator[x]=false;		// no seperator...might change if next item is not null
                    isLastItemNull = true;
                    continue;
                } else if (isLastItemNull && x>0) {
                    showSeparator[x-1]=true;  //if the previous item was null then set the seperator indicator 
                }
                isLastItemNull = false;

                //if we are looking at the last item...then there will be no repective showSeparator element for it
                if (x>=showSeparator.length) continue;

                //get the folder for this item, if it is a product
	            CategoryModel category = (contentNode instanceof CategoryModel) ? (CategoryModel)contentNode : (CategoryModel)contentNode.getParentNode();

                //Check the span for this item
                colSpan = category.getAttribute("COLUMN_SPAN", 1);
                int colIdx = category.getAttribute("COLUMN_NUM", 1);
                
                showSeparator[x] = (colSpan<2) || ((colIdx+colSpan-2)<=x);
            }
        }  // end of seperator determination logic

//***********  end of seperator bar logic *****************//
%>
<table cellspacing="0" cellpadding="0" width="<%=tblWidth%>"><TR ALIGN="center" valign="top">
<%
	for (int nodeCounter=0; nodeCounter<itemsToDisplay.length; nodeCounter++) {
                ContentNodeModel currentNode = itemsToDisplay[nodeCounter];
                DisplayObject displayObj = null;
                if (currentNode!=null) {
                    itemNameFont = "text11";
	                //get the folder for this item, if it is a product
                            if (currentNode instanceof ProductModel) {
                                itemNameFont = "catPageProdNameUnderImg";
                                if(theOnlyProduct==null) {
                                    theOnlyProduct = (ProductModel)currentNode;
                                    onlyOneProduct=true;
                                } else {
                                    onlyOneProduct = false;
                                }
                            } else onlyOneProduct = false;
		            CategoryModel category = (currentNode instanceof CategoryModel) ? (CategoryModel)currentNode : (CategoryModel)currentNode.getParentNode();

					// !!! uses BlueMartini ID
					displayObj = JspMethods.loadLayoutDisplayStrings(response, category.getContentName(), currentNode, currentFolder.getAttribute("LIST_AS", "full"),true,false,trkCode);
%>
<td width="<%= PRODUCT_CELL_WIDTH %>"><a href="<%=displayObj.getItemURL()%>&trk=<%=pageTrkCode%>" <%=displayObj.getRolloverString()%>>
<img src="<%=displayObj.getImagePath()%>"  name="<%=displayObj.getImageName()%>" width="<%=displayObj.getImageWidth()%>"  height="<%=displayObj.getImageHeight()%>" ALT="<%=displayObj.getAltText()%>" border="0"></a>
<%  if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>
<fd:ProduceRatingCheck>
       <br><font class="center"><img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif"  name="rating" width="59"  height="11" alt="" border="0"></font>
</fd:ProduceRatingCheck>       
<%  } %>
<br>
<a href="<%=displayObj.getItemURL()%>&trk=<%=pageTrkCode%>"><font class="<%=itemNameFont%>"><%=displayObj.getItemName()%></font></a>

<%=displayObj.getPrice()!=null?"<br><font class=\"price\">"+displayObj.getPrice()+"</font>":""%><br>
<font class="space4pix"><br></font>              
</td>
<%
                } else {
%>
<td width="<%= PRODUCT_CELL_WIDTH %>"><img src="/media_stat/images/layout/clear.gif" width="70" height="70"  ALT="" border="0" ><font class="space4pix"><br></font>              
</td>
<%                }
                if (nodeCounter+1!=MAX_COLUMNS) {
%>
<TD width="1" <%= showSeparator[nodeCounter] ? "bgcolor=\"#CCCCCC\"" : "" %>><img src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1" ALT=""></td>
<%
              } 
	}	// end for nodeCounter
%>

</tr></table>
<% } // end if itemsToDisplay.length>0 %>
</fd:VerticalPattern> 
<%} // End if sortedColl.size() > 0 

//**** bottom Of Jsp's  *******************
if (onlyOneProduct) {
	request.setAttribute("theOnlyProduct",theOnlyProduct);
}
%>
