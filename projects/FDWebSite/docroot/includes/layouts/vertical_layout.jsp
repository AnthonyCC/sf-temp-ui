<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'%>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*'%>

<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='oscache' prefix='oscache'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>


<display:InitLayout/>

<%
	//**************************************************************
	//***          Vertical Layout Pattern                       ***
	//**************************************************************
	final int PRODUCT_CELL_WIDTH = 105;
	final int FOLDER_CELL_WIDTH = 136;
	final int MAX_COLUMNS = 4;
	boolean firstRow = true;
	boolean showSeparator[] = null;
	
	String itemNameFont = "text11";
	
%>

<display:VerticalPattern 
	id='verticalPattern' 
	showFolders="<%= isDepartment.booleanValue() %>" 
	returnCategory="<%= isDepartment.booleanValue() %>" 
	labeled="<%= isDepartment.booleanValue() %>" 
	maxColumns='<%= MAX_COLUMNS %>' 
	productCellWidth="<%= PRODUCT_CELL_WIDTH %>" 
	folderCellWidth="<%= FOLDER_CELL_WIDTH %>" 
	currentFolder="<%= currentFolder %>" 
	itemsToShow='<%= sortedCollection %>'
>
	
	<%		
		// determine, one time, where the seperators should be displayed.
		if ( firstRow ) {
			firstRow = false;
			boolean isLastItemNull = false;
			int colSpan = 0;
			showSeparator = new boolean[MAX_COLUMNS];
		
			for ( int x = 0; x < rowList.size(); x++ ) {
				ContentNodeModel contentNode = (ContentNodeModel)rowList.get(x);
				if ( contentNode == null ) {
					if ( x >= showSeparator.length )
						continue;
					showSeparator[x] = false; // no seperator...might change if next item is not null
					isLastItemNull = true;
					continue;
				} else if ( isLastItemNull && x > 0 ) {
					showSeparator[x - 1] = true; //if the previous item was null then set the seperator indicator 
				}
				isLastItemNull = false;
		
				//if we are looking at the last item...then there will be no repective showSeparator element for it
				if ( x >= showSeparator.length )
					continue;
		
				//get the folder for this item, if it is a product
				CategoryModel category = ( contentNode instanceof CategoryModel ) ? (CategoryModel)contentNode : (CategoryModel)contentNode.getParentNode();
		
				//Check the span for this item
				colSpan = category.getAttribute( "COLUMN_SPAN", 1 );
				int colIdx = category.getAttribute( "COLUMN_NUM", 1 );
		
				showSeparator[x] = ( colSpan < 2 ) || ( ( colIdx + colSpan - 2 ) <= x );
			}
		} // end of seperator determination logic
	%>
	
		
	<% if ( verticalPattern.isLabeled() && rowIndex.intValue() == 0 ) { %>
		<table cellspacing="0" cellpadding="0" width="<%=tableWidth%>">
			<tr align="center" valign="top">
				<%
				for ( int i = 0; i < verticalPattern.getMaxColumns(); i++ ) {
					Image headerLabel = verticalPattern.getHeader( i ); 
					if ( headerLabel != null ) { %>
						<td width="<%= verticalPattern.isLabeled() ? verticalPattern.getFolderCellWidth() : verticalPattern.getProductCellWidth() %>">
							<img src="<%=headerLabel.getPath()%>" width="<%=headerLabel.getWidth()%>" height="<%=headerLabel.getHeight()%>" border="0"/>			
							<br/>
						</td>
					<% } else { %>
						<td width="<%= verticalPattern.isLabeled() ? verticalPattern.getFolderCellWidth() : verticalPattern.getProductCellWidth() %>">
							&nbsp;<br/>
						</td>	
					<% } %>				
				<% } %>
			</tr>
		</table> 
	<% } %>
			
	<table cellspacing="0" cellpadding="0" width="<%=tableWidth%>">	
		<tr align="center" valign="top">
			
			<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
			
				<% if ( currentItem == null ) { 				// ===== null =====
					%>					
					<td width="<%= verticalPattern.isLabeled() ? verticalPattern.getFolderCellWidth() : verticalPattern.getProductCellWidth() %>">
						<img src="/media_stat/images/layout/clear.gif" width="70" height="70" alt="" border="0"><font class="space4pix"><br/></font>
						&nbsp;
					</td>					
				<% } else if ( currentItem instanceof ProductModel ) { 				// ===== PRODUCT =====
					
					ProductModel product = (ProductModel)currentItem;
					String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
					%>
				
					<td	width="<%= verticalPattern.isLabeled() ? verticalPattern.getFolderCellWidth() : verticalPattern.getProductCellWidth() %>" > 
						
					<font class="catPageProdNameUnderImg">
						
						<display:ProductImage product="<%= product %>" showRolloverImage="true" action="<%= actionUrl %>"/><br/>
						<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
						<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
						<display:ProductPrice impression="<%= new ProductImpression(product) %>" showDescription="false"/>
						
					</font>
					</td> 
					
				<% } else if ( currentItem instanceof CategoryModel ) { 		// ===== CATEGORY ===== 
					
					CategoryModel category = (CategoryModel)currentItem;
					String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode ); 
					%>
					
					<td width="<%= verticalPattern.isLabeled() ? verticalPattern.getFolderCellWidth() : verticalPattern.getProductCellWidth() %>" style="padding-top: 15px; padding-bottom: 15px">
						<font class="text11">
						
							<display:CategoryImage category="<%= category %>" action="<%= actionUrl %>"/>
							<display:CategoryName category="<%= category %>" action="<%= actionUrl %>"/>
									
						</font>
					</td> 
					
				<% } else { %>
					[ ERROR : <%= currentItem.getFullName() %> is a <%= currentItem.getClass().toString() %> ]<br/>				
				<% } %>
				
				<% if ( columnIndex.intValue() != MAX_COLUMNS-1 ) { %>
					<td width="1" <%= showSeparator[columnIndex.intValue()] ? "bgcolor=\"#CCCCCC\"" : "" %>>
						<img src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"	ALT="">
					</td>
				<% } %>
				
			</display:PatternRow>
		</tr>
	</table>
	
</display:VerticalPattern>

<%
	//**** bottom Of Jsp's  *******************
	if ( onlyOneProduct.booleanValue() ) {
		request.setAttribute( "theOnlyProduct", theOnlyProduct );
	}
%>
