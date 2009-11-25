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
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>


<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@page import="org.apache.log4j.Logger"%>

<display:InitLayout/>
<%
	// create separate lists for each category
	List multiList = new ArrayList();
	List l = null;
	String currentFolderPKId = currentFolder.getPK().getId();
	boolean hasAnyProducts = false;

	Iterator it = sortedCollection.iterator();
	while ( it.hasNext() ) {
		ContentNodeModel node = (ContentNodeModel)it.next();		
		
		if ( node instanceof CategoryModel && node.getParentNode() != null && node.getParentNode().getPK().getId().equals(currentFolderPKId) ) {
			// add new separating category
			multiList.add( node );
			l = new ArrayList();
			multiList.add( l );			
			
		} else if ( node instanceof ProductModel || node instanceof CategoryModel ) {
			// add product or subcategory
			if ( l == null ) 
				multiList.add( l = new ArrayList() );
			l.add( node );
			
			if ( node instanceof ProductModel ) 
				hasAnyProducts = true;
			
		} else {
			//skip node
		}
	}
	
	int maxWidth = isDepartment.booleanValue() ? 550 : 380;
	boolean useAlternate = false;
	
	for ( int i = 0; i < multiList.size(); i++ ) {		
		Object obj = multiList.get( i );
		
		if ( obj instanceof CategoryModel && !((List)multiList.get(i+1)).isEmpty() ) {
			
			// show separator line except the first category
			if ( i != 0 ) {
				%>
				<br/><br/>
				<table width="<%=maxWidth%>" align="center" cellpadding="0" cellspacing="0" border="0">
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" /></td></tr>
				<tr><td bgcolor="#cccccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td></tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" /></td></tr></table>
				<%
			}			    
			
			// get the category_top attribute to display
			List<Object> catTop =( (CategoryModel)obj ).getCategoryTopMedia();
			if ( catTop != null && catTop.size() > 0 ) {
				MediaI catTopMedia = (MediaI)catTop.get( 0 );
				if ( catTopMedia instanceof Image ) { 
					%><img src="<%=catTopMedia.getPath()%>" <%=JspMethods.getImageDimensions( (Image)catTopMedia )%> /><% 
				} else { 
					%><fd:IncludeMedia name="<%= catTopMedia.getPath()%>" /><% 
				}
				%><br/><br/><%
			} 
			
			useAlternate = ((CategoryModel)obj).isUseAlternateImages();
			
		} else if ( obj instanceof List ) {

			%>
				<display:HorizontalPattern 
					id="horizontalPattern" 
					currentFolder="<%= currentFolder %>" 
					folderCellWidth="136" 
					productCellWidth="105" 
					itemsToShow="<%= (List)obj %>" 
					useLayoutPattern="<%= hasAnyProducts && !useAlternate %>"
					dynamicSize="<%= useAlternate || !hasAnyProducts %>"
					tableWidth="<%= maxWidth %>"
					showCategories="true" 
					showProducts="true"
					maxColumns="4"
					useAlternateImage="<%= useAlternate %>"
				>
				
					<table cellspacing="0" cellpadding="0" width="<%=tableWidth%>">
						<tr align="center" valign="bottom">						
							<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
							
								<% if ( currentItem instanceof ProductModel ) {		
									ProductModel product = (ProductModel)currentItem;
									String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
									%>
								
									<td width="<%= horizontalPattern.getProductCellWidth() %>" style="padding-bottom: 5px;">										
										<display:ProductImage product="<%= product %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="<%= useAlternate %>"/>										
									</td>
								
								<% } else { // is a category 
									CategoryModel category = (CategoryModel)currentItem;
									String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode );
									%>
									
									<td width="<%= horizontalPattern.getFolderCellWidth() %>" style="padding-bottom: 5px;">									
										<display:CategoryImage category="<%= category %>" action="<%= actionUrl %>"/>										
									</td>
								
								<% } %> 
									
							</display:PatternRow>							
						</tr>
						
						<tr align="center" valign="top">						
							<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
							
								<% if ( currentItem instanceof ProductModel ) {		
									ProductModel product = (ProductModel)currentItem;
									String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
									%>
								
									<td width="<%= horizontalPattern.getProductCellWidth() %>" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="catPageProdNameUnderImg">
										
										<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
										<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
										<display:ProductPrice impression="<%= new ProductImpression(product) %>" showDescription="false"/>
										
									</font></td>
								
								<% } else { // is a category 
									CategoryModel category = (CategoryModel)currentItem;
									String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode );
									%>
									
									<td width="<%= horizontalPattern.getFolderCellWidth() %>" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="text11">
										
										<display:CategoryName category="<%= category %>" action="<%= actionUrl %>" style="font-weight:normal;"/>
										
									</font></td>
								
								<% } %> 
									
							</display:PatternRow>							
						</tr>
					</table>
							
				</display:HorizontalPattern>
			<%			
		}		
	}
%>
