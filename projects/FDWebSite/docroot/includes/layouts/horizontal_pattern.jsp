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
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");
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

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();

//**********************  End of stuff to make JSPF's become JSP's

int productCellWidth=isDepartment==true?137:100; //make cell larger, for products, when on a Department Page
int folderCellWidth = 137;
int cellWidth = productCellWidth;
String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);

//segrigate the available stuff from the unavailable stuff
List availableList = new ArrayList();
int prodsShown = 0;
int tblWidth=0;
String itemNameFont=null;
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

if (availableList.size() > 0) {
	int totalItemsToDisplay = (availableList.size()>JspMethods.displayPattern.length?JspMethods.displayPattern.length-1:availableList.size()-1);
	Integer[] patternArray = JspMethods.displayPattern[totalItemsToDisplay];

%>

<fd:horizontalpattern itemsToShow='<%=availableList.toArray()%>' patternArray='<%=patternArray%>' id='itemsToDisplay'>

<%//compute the width of the table
	if (itemsToDisplay.length > 0) {
		tblWidth = 0;
		for (int idx = 0;idx<itemsToDisplay.length;idx++){
			ContentNodeModel item = (ContentNodeModel)itemsToDisplay[idx];
			//if (item instanceof ProductModel ) tblWidth+=productCellWidth;
			if (item instanceof CategoryModel && currentFolder instanceof DepartmentModel) {
                            tblWidth+=folderCellWidth;
                        } else tblWidth+=productCellWidth;
		}
%>
 <table cellspacing="0" cellpadding="0" width="<%=tblWidth%>"><TR ALIGN="CENTER" valign="top">
    <logic:iterate id="displayThing" collection="<%= itemsToDisplay %>" type="com.freshdirect.fdstore.content.ContentNodeModel">
<%
                DisplayObject displayObj = null;
                itemNameFont = "text11";
                if ((displayThing instanceof ProductModel) || ( (displayThing instanceof CategoryModel) && ((CategoryModel)displayThing).getShowSelf()==true)) {
                    if(displayThing instanceof ProductModel) {
                        if (theOnlyProduct!=null) {
                            onlyOneProduct=false;
                        }else {
                            onlyOneProduct=true;
                            theOnlyProduct = (ProductModel)displayThing;
                        }
                        itemNameFont = "catPageProdNameUnderImg";
                        displayObj = JspMethods.loadLayoutDisplayStrings(response,displayThing.getParentNode().getContentName(),displayThing,prodNameAttribute,true,false,trkCode);
                        cellWidth = productCellWidth;
                    }
                    else if(displayThing instanceof CategoryModel) {
                        onlyOneProduct=false;
                        displayObj = JspMethods.loadLayoutDisplayStrings(response,"",displayThing,prodNameAttribute,true,false,trkCode);
                        cellWidth = folderCellWidth;
                    }
%>
<td width="<%=cellWidth %>"><font class="<%=itemNameFont%>"><a href="<%=displayObj.getItemURL()%>&trk=<%=pageTrkCode%>" <%=displayObj.getRolloverString()%>><img src="<%=displayObj.getImagePath()%>"  name="<%=displayObj.getImageName()%>" width="<%=displayObj.getImageWidth()%>"  height="<%=displayObj.getImageHeight()%>" alt="<%=displayObj.getAltText()%>" border="0"></a>
<% System.out.println("displayObj.getRating()"+displayObj.getRating()); %>

<%  if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>
<fd:ProduceRatingCheck>
            <br><font class="center"><img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif"  name="rating" width="59"  height="11" alt="" border="0"></font>
</fd:ProduceRatingCheck>            
<%  } %>
<br><a href="<%=displayObj.getItemURL()%>&trk=<%=pageTrkCode%>"><%=displayObj.getItemName()%></a></font>

<%=displayObj.getPrice()!=null?"<br><font class=\"price\">"+displayObj.getPrice()+"</font>":""%><br>

</td>
<%}  //end of if hidden folder %>
		</logic:iterate>
</tr></table>
<br><font class="space4pix"><br>&nbsp;<br></font>
<% } // end if itemsToDisplay.length() > 0%>
</fd:horizontalpattern>
<%} // End if displayItem.size() > 0
//**** bottom Of Jsp's  *******************
if (onlyOneProduct) {
	request.setAttribute("theOnlyProduct",theOnlyProduct);
}

//}  //********** End: of Horizontal Pattern ********************* %>
