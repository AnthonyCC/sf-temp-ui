<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.content.attributes.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*'%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='oscache' prefix='oscache'%>


<%	
	//********** Start of Stuff to let JSPF's become JSP's **************
	
	String catId = request.getParameter("catId"); 
	String deptId = request.getParameter("deptId"); 
	boolean isDepartment = false;
	
	ProductContainer currentFolder = null;
	if(deptId!=null) {
		currentFolder=(ProductContainer) ContentFactory.getInstance().getContentNode(deptId);
		isDepartment = true;
	} else {
		currentFolder=(ProductContainer) ContentFactory.getInstance().getContentNode(catId);
	}
	
	
	boolean onlyOneProduct = false;
	ProductModel theOnlyProduct = null;
	
	Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
	if (sortedColl==null) sortedColl = new ArrayList();
	
	
	//********************************************************************************
	//**            Multi Category Redirect Layout                                   *
	//**  Will build links for products pointing to their primary home category      *
	//********************************************************************************
	
	int imgWidths = 0;
	int colCount = 0;
	int maxWidth;
	int maxCols;
	int newCategoryCount = 0;
	boolean newCategory = false;
	boolean openedTable = false;
	//StringBuffer catMediaOut = new StringBuffer(200);
	boolean hasLeftNav = true;
	hasLeftNav = currentFolder.isShowSideNav();
	
	if (!hasLeftNav || request.getRequestURI().indexOf("department.jsp")!=-1) {
	    maxWidth=495;
		maxCols = 5;
	} else {
	    maxWidth = 400;
		maxCols = 4;
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
	String currentFolderPKId = currentFolder.getContentKey().getId();
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
%>

<%
imgWidths =0;
itemPrice= null;
DisplayObject displayObj = null; 
StringBuffer textRow = new StringBuffer();

for( int itmIdx=0; itmIdx < availableList.size(); itmIdx++ ) {
	
    ContentNodeModel contentNode = (ContentNodeModel)availableList.get(itmIdx);
    itemNameFont = "text11";
     if (contentNode instanceof CategoryModel) {
        cat = (CategoryModel)contentNode;
        
        if ( cat.getParentNode() != null && cat.getParentNode().getContentKey().getId().equals(currentFolderPKId) ) { 
        	
            //we dont want to print heading for categories that are empty..so peek ahead to see if there is an item that it's child
            int peekAhead = itmIdx+1;
            if (peekAhead == availableList.size()) {
                continue;
            } else {
                if ( ((ContentNodeModel)availableList.get(peekAhead)).getParentNode().getContentKey().getId().equals(currentFolderPKId)) {
                    continue;
                }
            }
            if ( newCategoryCount > 0 ) {  // print the seperator bar if one or more categories has been printed  
				if (textRow.length() > 1) { 
					%><tr valign="top"><%=textRow.toString()%></tr><%  			
				}
            
    			textRow.setLength(0); %>
    			
				</tr>
				</table>
				
				<table width="<%=maxWidth%>" align="center" cellpadding="0"	cellspacing="0" border="0">
					<tr>
						<td>
							<img src="/media_stat/images/layout/clear.gif" width="1" height="8">
						</td>
					</tr>
				</table>
			<% } %>
			
			
			
			<table width="<%=maxWidth%>" align="center" cellpadding="0" cellspacing="0" border="0">
			<%
            newCategoryCount++;
			imgWidths = 0;
			colCount = 0;
            // get the category_top attribute to display 
            List catTop = cat.getTopMedia();
            if (catTop.size() > 0) {
                MediaI catTopMedia = (MediaI)catTop.get(0);
                if (catTopMedia instanceof Image) { %>
					<tr>
						<td width="100%" align="center">
							<img src="<%=catTopMedia.getPath()%>" <%=JspMethods.getImageDimensions((Image)catTopMedia) %>>
						</td>
					</tr>
				<% } else { %>
					<tr>
						<td width="100%" align="center">
							<fd:IncludeMedia name='<%= catTopMedia.getPath()%>' />
						</td>
					</tr>
				<% } %>
				
				<tr>
					<td>
						<br/><br/>
					</td>
				</tr>
			<% } %>
			</table>
			
			
			
			<table cellpadding="6" cellspacing="0" border="0">
				<tr align="center" valign="top">
				
			<%
			openedTable=true;	  
			continue; 
        } else {
            displayObj = JspMethods.loadLayoutDisplayStrings(response,"",cat,prodNameAttribute,false,false,"&trk=picks");
            onlyOneProduct=false;
        }
	
    } else {
    	
        if( theOnlyProduct == null ) {
            theOnlyProduct = (ProductModel)contentNode;
            onlyOneProduct = true;
        } else {
            onlyOneProduct = false;
        }
        itemNameFont="catPageProdNameUnderImg";
        ProductModel product = (ProductModel)contentNode;
        cat = (CategoryModel)product.getParentNode();
        displayObj = JspMethods.loadLayoutDisplayStrings(response,product.getParentNode().getContentName(),product,prodNameAttribute,true,true,"&trk=picks",true);
    }
     
     
    //if we are about to xceed the limits of the witdth then  start a new row.
    if ( displayObj != null && ( (imgWidths + displayObj.getImageWidthAsInt() > maxWidth) || colCount+1 > maxCols)) {
        imgWidths =0;
		colCount = 0;
		%>
		</tr>
		<%
		if (textRow.length() > 1) { 
			%><tr valign="top"><%=textRow.toString()%></tr><%  
		}
   		textRow.setLength(0); 
    	%>
	</table>
	
	<table cellpadding="6" cellspacing="0" border="0">
		<tr align="center" valign="top">
		
			<%
			openedTable = true;
    	}
		if (!openedTable) {
			%>
			
			<table CELLPADDING="6" CELLSPACING="0" BORDER="0">
				<tr align="center" valign="top">
				
			<%
			openedTable = true;
		}
		
		imgWidths += (displayObj.getImageWidthAsInt() == 0 ? 80 : displayObj.getImageWidthAsInt());
		colCount++;
		
		%>
				<td valign="bottom" align="center" width="<%=displayObj.getImageWidth() %>">
					<a href="<%=displayObj.getItemURL()%>">
						<img src="<%= displayObj.getImagePath()%>" <%=displayObj.getImageDimensions() %> alt="<%=displayObj.getAltText()%>" hspace="0" border="0">
					</a>
				</td>
				
				<% 
			   	textRow.append("<td valign=\"top\" align=\"center\" width=\"");
				textRow.append(displayObj.getImageWidth());
				textRow.append("\"><a href=\"");
				textRow.append(displayObj.getItemURL());
				textRow.append("\"><font class=\"");
				textRow.append(itemNameFont);
				textRow.append("\">");
				textRow.append(displayObj.getItemName());
				textRow.append("</font></a>");
			  	if (displayObj.getPrice()!=null) { 
			    	textRow.append("<br><font class=\"price\">");
					textRow.append(displayObj.getPrice());
					textRow.append("</font>");
			 	}
			   	textRow.append("</td>");
			} 
			if (openedTable) {
				%></tr><%
				if (textRow.length() > 1) %>
					<tr valign="top"><%=textRow.toString()%>
					</tr>
				</table>
			<% }
			
if (onlyOneProduct) {
	request.setAttribute("theOnlyProduct",theOnlyProduct);
}

%>