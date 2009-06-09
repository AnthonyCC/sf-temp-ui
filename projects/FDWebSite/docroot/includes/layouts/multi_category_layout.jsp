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

<display:InitLayout/>

<%
	// create separate lists for each category
	List multiList = new ArrayList();
	List l = null;

	Iterator it = sortedCollection.iterator();
	while ( it.hasNext() ) {
		ContentNodeModel node = (ContentNodeModel)it.next();
		
		if ( node instanceof CategoryModel ) {
			// add new category
			multiList.add( node );
			l = new ArrayList();
			multiList.add( l );			
			
		} else if ( node instanceof ProductModel ) {
			// add product
			if ( l == null ) 
				multiList.add( l = new ArrayList() );
			l.add( node );
			
		} else {
			//skip node
		}
	}
	
	int maxWidth = isDepartment.booleanValue() ? 550 : 380;
	
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
			MultiAttribute catTop = (MultiAttribute)( (CategoryModel)obj ).getAttribute( "CATEGORY_TOP_MEDIA" );
			if ( catTop != null ) {
				MediaI catTopMedia = (MediaI)catTop.getValue( 0 );
				if ( catTopMedia instanceof Image ) { 
					%><img src="<%=catTopMedia.getPath()%>" <%=JspMethods.getImageDimensions( (Image)catTopMedia )%> /><% 
				} else { 
					%><fd:IncludeMedia name="<%= catTopMedia.getPath()%>" /><% 
				}
				%><br/><br/><%
			} 
			
		} else if ( obj instanceof List ) {
			%>
				<display:HorizontalPattern 
					id="horizontalPattern" 
					currentFolder="<%= currentFolder %>" 
					folderCellWidth="136" 
					productCellWidth="105" 
					itemsToShow="<%= (List)obj %>" 
					useLayoutPattern="true"
					dynamicSize="false"
					tableWidth="<%= maxWidth %>" 
				>
				
					<table cellspacing="0" cellpadding="0" width="<%=tableWidth%>">
						<tr align="center" valign="top">
						
							<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
							
								<%
									ProductModel product = (ProductModel)currentItem;
									String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
								%>
								
								<td width="<%= horizontalPattern.getProductCellWidth() %>" style="padding-bottom: 15px"><font class="catPageProdNameUnderImg">
									
									<display:ProductImage product="<%= product %>" showRolloverImage="true" action="<%= actionUrl %>"/><br/>
									<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
									<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
									<display:ProductPrice impression="<%= new ProductImpression(product) %>"/>
									
								</font></td> 
									
							</display:PatternRow>
							
						</tr>
					</table>
							
				</display:HorizontalPattern>
			<%			
		}		
	}
%>
