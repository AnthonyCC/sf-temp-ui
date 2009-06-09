<%@ page import='java.util.List,java.util.Map,java.util.Iterator'  %>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Arrays"%>

<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.content.ContentNodeModel' %>
<%@ page import='com.freshdirect.fdstore.content.ProductModel' %>
<%@ page import='com.freshdirect.fdstore.content.CategoryModel' %>
<%@ page import='com.freshdirect.fdstore.content.Html' %>

<%@ page import='com.freshdirect.webapp.util.DisplayObject' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.TableLayoutHelper' %>
<%@ page import="com.freshdirect.webapp.util.ProductImpression"%>

<%@ page import='org.apache.commons.collections.Predicate' %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%! // INTERNALS
	
	
	// IS FEATURED: CATEGORY MODE AND FEATURED CATEGORY
	static Predicate featuredFilter = new  Predicate() {
	
		public boolean evaluate(Object o) {
			if (!(o instanceof CategoryModel)) return false;
			return ((CategoryModel)o).isFeatured();
		}
	};
	
	// NON FEATURED: PRODUCT MODEL OR CATEGORY MODEL AND NOT FEATURED
	static Predicate nonFeaturedFilter = new Predicate() {
		public boolean evaluate(Object o) {
			if (o instanceof ProductModel) return true;
			else if (o instanceof CategoryModel) return !((CategoryModel)o).isFeatured();
			else return false;
		}
	};
	
	// SEPARATOR: CATEGORY MODEL AND HAS SEPARATOR MEDIA
	static Predicate separatorFilter = new Predicate() {
		public boolean evaluate(Object o) {
			if (!(o instanceof CategoryModel)) return false;
			return ((CategoryModel)o).getSeparatorMedia() != null;
		}
	};
	
	// DISPLAY OBJECTS CORRESPONDING TO CONTENT NODES
	// Once a display object has been calculated, do not calculate it again.
	// Also implements width calculator
	static class DisplayObjectRepo implements TableLayoutHelper.WidthCalculator {
	
		// Map<ContentNodeModel,DisplayObject>
		private Map displayObjects = new java.util.HashMap();
	
		// stuff needed for calculating a display object
		private HttpServletResponse response;
		private String prodNameAttribute;
		private String trkCode;
	
	
		// stuff needed to calculate a display object
		public DisplayObjectRepo(HttpServletResponse response, String prodNameAttribute, String trkCode)  {
			this.response = response;
			this.prodNameAttribute = prodNameAttribute;
			this.trkCode = trkCode;
		}
	
		// get display object from content node
		public DisplayObject getDisplayObject(ContentNodeModel c) throws JspException {
			DisplayObject displayObject = (DisplayObject)displayObjects.get(c);
			if (displayObject == null) {
				if (c instanceof ProductModel) {	
					ProductModel product = (ProductModel)c;
					displayObject = JspMethods.loadLayoutDisplayStrings(response,product.getParentNode().getContentName(),product,prodNameAttribute,true,false,trkCode);
				} else if (c instanceof CategoryModel) {
					CategoryModel cat = (CategoryModel)c;
					displayObject = JspMethods.loadLayoutDisplayStrings(response,"",cat,prodNameAttribute,true,false,trkCode);
				}
	
				if (displayObject != null) displayObjects.put(c,displayObject);
			}
	
			return displayObject;
		}
	
		// get width, implements WidthCalculator.getWidth
		public int getWidth(Object o) {
			try {
				DisplayObject displayObject = getDisplayObject((ContentNodeModel)o);
				return displayObject == null ? 0 : displayObject.getImageWidthAsInt();
			} catch (JspException e) {
				return 0;
			}
		}
	
	}
%>


<jsp:include page="/includes/department_peakproduce.jsp" flush="true"/>

	
<display:InitLayout/>
<%
	//**************************************************************
	//***          the Featured Category Layout                  ***
	//**************************************************************

	int maxWidth = (request.getRequestURI().toLowerCase().indexOf("department.jsp") != -1 ) ? 550 : 400;
	
	List availableList = new ArrayList();
	String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
	
	Iterator availItr = sortedCollection.iterator();
	while( availItr.hasNext() ) {
		
	    Object availObject = availItr.next();
	    if ( availObject instanceof ProductModel && ((ProductModel)availObject).isUnavailable() ) {
	       continue;
	    } else {
	        //throw away hidden folders
	        if ( availObject instanceof CategoryModel && ((CategoryModel)availObject).getShowSelf() == false ) 
	        	continue;
	        availableList.add(availObject);
	    }
	}
	
	// display objects
	DisplayObjectRepo repo = new DisplayObjectRepo(response,prodNameAttribute,trackingCode);
	
	// featured layouts
	TableLayoutHelper featuredTableLayout = new TableLayoutHelper(availableList.iterator(),featuredFilter,separatorFilter,repo,maxWidth);
	
	// non-featured layouts
	TableLayoutHelper nonFeaturedTableLayout = new TableLayoutHelper(availableList.iterator(),nonFeaturedFilter,separatorFilter,repo,maxWidth);

	
	
	// for featured and non-featured
	Iterator layoutI = Arrays.asList( new Object[] { featuredTableLayout, nonFeaturedTableLayout } ).iterator();
	while( layoutI.hasNext() ) {
		
		TableLayoutHelper layout = (TableLayoutHelper)layoutI.next();
	
		List rows = layout.getRows();
		
		if ( rows.isEmpty() ) 
			continue;
		
		for( int i = 0; i < rows.size(); ++i ) {
			
			List row = (List)rows.get(i);
			int rowWidth = layout.getRowWidth(i);
			if ( rowWidth == 0 ) 
				rowWidth = maxWidth;
			
			%>
			<table width="<%=rowWidth%>" cellpadding="0" cellspacing="0" border="0">
			
				<tr>
					<%	// ======= IMAGE ROW =======
						
					for( int j=0; j < row.size(); ++j ) { // for each image
						ContentNodeModel contentNode = (ContentNodeModel)layout.getElement(i,j);
		
						if ( separatorFilter.evaluate(contentNode) ) {
							CategoryModel cat = (CategoryModel)contentNode;
							Html html = cat.getSeparatorMedia();
							%>
							<td align="center">
					        	<fd:IncludeMedia name="<%=html.getPath()%>"/>
							</td>
							<%
							break;
						}
		
						DisplayObject displayObject = (DisplayObject)repo.getDisplayObject(contentNode);
						String actionUrl = displayObject.getItemURL() + "&trk=" + trackingCode; 
						
						%>	
						<td align="center" width="<%=displayObject.getImageWidth()%>" style="padding-top: 10px">
							<% if ( contentNode instanceof ProductModel ) { %>
								<display:ProductImage product="<%= (ProductModel)contentNode %>" action="<%= actionUrl %>" showRolloverImage="true"/>
							<% } else if ( contentNode instanceof CategoryModel ) { %>
								<display:CategoryImage category="<%= (CategoryModel)contentNode %>" action="<%= actionUrl %>"/>
							<% } %>
						</td>
		
					<% } // images %>
				</tr>
			
				<tr>
					<%	// ======= TEXT ROW =======
						
					for( int j = 0; j < row.size(); ++j ) { // for each link
						ContentNodeModel contentNode = (ContentNodeModel)layout.getElement(i,j);
						if ( separatorFilter.evaluate(contentNode) ) 
							break;
						DisplayObject displayObject = (DisplayObject)repo.getDisplayObject(contentNode);
						String actionUrl = displayObject.getItemURL() + "&trk=" + trackingCode; 
				
					%>
						<td align="center" width="<%=displayObject.getImageWidth()%>" style="padding-bottom: 10px">
							<% if ( contentNode instanceof ProductModel ) { %>
							
								<display:ProductRating product="<%= (ProductModel)contentNode %>" action="<%= actionUrl %>"/>
								<display:ProductName product="<%= (ProductModel)contentNode %>" action="<%= actionUrl %>"/>								
								<display:ProductPrice impression="<%= new ProductImpression( (ProductModel)contentNode ) %>"/>
								
							<% } else if ( contentNode instanceof CategoryModel ) { %>
							
								<display:CategoryName category="<%= (CategoryModel)contentNode %>" action="<%= actionUrl %>" style="font-style:normal"/>
								
							<% } %>							
						</td>
						
					<% } // links %>
			
				</tr>
			
			</table>
		<% }
	} // layouts
%>
