<%@ page import='java.util.List,java.util.Map,java.util.Iterator'  %>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Arrays"%>
<%@ page import='java.util.HashMap' %>

<%@ page import='com.freshdirect.storeapi.content.ContentFactory' %>
<%@ page import='com.freshdirect.storeapi.content.ContentNodeModel' %>
<%@ page import='com.freshdirect.storeapi.content.ProductModel' %>
<%@ page import='com.freshdirect.storeapi.content.CategoryModel' %>
<%@ page import='com.freshdirect.storeapi.content.Html' %>

<%@ page import='com.freshdirect.webapp.util.DisplayObject' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.TableLayoutHelper' %>
<%@ page import="com.freshdirect.webapp.util.ProductImpression"%>

<%@ page import='org.apache.commons.collections.Predicate' %>

<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<% FDUserI feat_user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER); %>

<% //expanded page dimensions
final int W_FEATURED_CATEGORY_IS_DEPARTMENT = 715;
final int W_FEATURED_CATEGORY_OLD_DEPARTMENT = 550;
final int W_FEATURED_CATEGORY_NOT_DEPARTMENT = 601;
final int W_FEATURED_CATEGORY_WIDE_DEPARTMENT = 765;
final int[] fruPattern = { 1, 5, 4, 3, 2 }; // - no of items per row
final int[] fruGap = { -1, -1, -1, 0, -1 }; // -1 means automatic strech to full width
final int[] seaPattern = { 1, 5, 4, 1, 4, 2 }; // - no of items per row
final int[] seaGap = { -1, -1, -1, -1, -1, 40 }; // -1 means automatic strech to full width
final int[] vegPattern = { 1, 5, 4, 5, 5, 4, 1, 5 }; // - no of items per row
final int[] vegGap = { -1, -1, -1, -1, -1, -1, -1, -1 }; // -1 means automatic strech to full width
final int[] delPattern = { 2, 7, 5 }; // - no of items per row
final int[] delGap = { 40, -1, -1 }; // -1 means automatic strech to full width
final int[] delPatternFallback = {2, 7, 6};
%>

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

	
<display:InitLayout useAlternateWidth="true"/>
<%
	//**************************************************************
	//***          the Featured Category Layout                  ***
	//**************************************************************

  boolean isDepartmentLayout = request.getRequestURI().toLowerCase().indexOf("department.jsp") != -1;
  boolean widenedWidth = (Boolean)pageContext.getAttribute("useWidenedWidth");
  boolean specialDept = "fru".equals(departmentId) || "sea".equals(departmentId) || "veg".equals(departmentId) || "del".equals(departmentId);
  List<CategoryModel> categories = null;
  int noOfItems = 0;
  int[] pattern = {};
  int[] gap = {};
  
	/* allow pattern override */
	String patOver = request.getParameter("patOver");
	
  if (specialDept || patOver != null) {
		List<CategoryModel> featured = new ArrayList<CategoryModel>();
		List<CategoryModel> nonFeatured = new ArrayList<CategoryModel>();
		
		Iterator availItr = sortedCollection.iterator();
		while( availItr.hasNext() ) {
			
		    Object availObject = availItr.next();
		    if ( availObject instanceof CategoryModel) {
		    	CategoryModel model = (CategoryModel) availObject;
		    	if (model.getShowSelf() == false ) 
		        	continue;
		    	if (model.isFeatured())
		    		featured.add(model);
		    	else
		    		nonFeatured.add(model);
		    }
		}

		categories = new ArrayList<CategoryModel>(featured);
		categories.addAll(nonFeatured);
		

		if ( "fru".equals(departmentId) || "fru".equalsIgnoreCase(patOver) ) {
			pattern = fruPattern;
			gap = fruGap;
		} else if ( "sea".equals(departmentId) || "sea".equalsIgnoreCase(patOver) ) {
			pattern = seaPattern;
			gap = seaGap;
		} else if ( "veg".equals(departmentId) || "veg".equalsIgnoreCase(patOver) ) {
			pattern = vegPattern;
			gap = vegGap;
		} else if ( ("del".equals(departmentId) && categories.size()==14) || "del".equalsIgnoreCase(patOver) ) {
			pattern = delPattern;
			gap = delGap;
		} else if ( ("del".equals(departmentId) && categories.size()==15) || "delFb".equalsIgnoreCase(patOver) ) {
			pattern = delPatternFallback;
			gap = delGap;
		}
		
		noOfItems = 0;
		for (int p : pattern)
			noOfItems += p;
		
		if (noOfItems != categories.size())
			specialDept = false;
  }
  if (!specialDept) {
	int maxWidth = isDepartmentLayout ? widenedWidth ? W_FEATURED_CATEGORY_WIDE_DEPARTMENT : W_FEATURED_CATEGORY_OLD_DEPARTMENT : W_FEATURED_CATEGORY_NOT_DEPARTMENT;
	
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
		
		/* loop through and create coupon info */
		List<ArrayList<FDCustomerCoupon>> ref_coupons = new ArrayList<ArrayList<FDCustomerCoupon>>(); //list of rows
		ArrayList<FDCustomerCoupon> ref_coupons_row = new ArrayList<FDCustomerCoupon>(); //each row of prods
		HashMap<Integer, Boolean> containsCoupons = new HashMap<Integer, Boolean>(); //if row has coupon
		
		for( int i = 0; i < rows.size(); ++i ) {
			List row = (List)rows.get(i);
	        containsCoupons.put(i, false);
	        
			for( int j=0; j < row.size(); ++j ) {
				ContentNodeModel contentNode = (ContentNodeModel)layout.getElement(i,j);
				FDCustomerCoupon curCoupon = null;
				
				if ( contentNode instanceof ProductModel ) {
					ProductImpression featCat_pi = new ProductImpression((ProductModel)contentNode);
					if (featCat_pi.getSku() != null && featCat_pi.getSku().getProductInfo() != null) {
						curCoupon = feat_user.getCustomerCoupon((new ProductImpression((ProductModel)contentNode)).getSku().getProductInfo(), EnumCouponContext.PRODUCT,((ProductModel)contentNode).getParentId(),((ProductModel)contentNode).getContentName());
					}
					
					if (curCoupon != null) {
				        containsCoupons.put(i, true);
					}
				}
				ref_coupons_row.add( curCoupon );
			}
			
			ref_coupons.add( ref_coupons_row );
		}
		
		for( int i = 0; i < rows.size(); ++i ) {
			
			List row = (List)rows.get(i);
			int rowWidth = layout.getRowWidth(i);
			if ( rowWidth == 0 ) 
				rowWidth = maxWidth;

      		int spareWidth = 0;
      		if ( row.size() > 3 && rowWidth > (2 * maxWidth / 3) ) {
        		spareWidth = ((isDepartmentLayout ? W_FEATURED_CATEGORY_IS_DEPARTMENT : maxWidth) - rowWidth) / (row.size() - 1);
      		}
			%>
     		 <table width="<%=isDepartmentLayout ? W_FEATURED_CATEGORY_IS_DEPARTMENT : maxWidth %>" cellpadding="0" cellspacing="0" border="0" style="margin-top: 10px;">
				<tr>
					<%	// ======= IMAGE ROW =======
						
					for( int j=0; j < row.size(); ++j ) { // for each image
						ContentNodeModel contentNode = (ContentNodeModel)layout.getElement(i,j);
						String prodImageClassName = (containsCoupons.get(i)) ? "couponLogo" : null;
		
						if ( separatorFilter.evaluate(contentNode) ) {
							CategoryModel cat = (CategoryModel)contentNode;
							Html html = cat.getSeparatorMedia();
							%>
              				<td align="center"><div style="text-align: center">
					        	<fd:IncludeMedia name="<%=html.getPath()%>"/>
             				 </div></td>
							<%
							break;
						}
		
						DisplayObject displayObject = (DisplayObject)repo.getDisplayObject(contentNode);
						String actionUrl = displayObject.getItemURL() + "&trk=" + trackingCode; 

           				 if (j > 0 && spareWidth > 0) {
            			%>
              				<td width="<%=spareWidth%>"><div style="width: <%=spareWidth%>px;"></div></td>
            			<%
            			}
						%>
						<td align="center" style="padding-top: 10px">
							<% if ( contentNode instanceof ProductModel ) { %>
								<display:ProductImage product="<%= (ProductModel)contentNode %>" action="<%= actionUrl %>" showRolloverImage="true" coupon="<%= ref_coupons.get(i).get(j) %>" className="<%= prodImageClassName %>"/>
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
				
            if (j > 0  && spareWidth > 0) {
            %>
              <td width="<%=spareWidth%>"><div style="width: <%=spareWidth%>px;"></div></td>
            <%
            }
					%>
						<td align="center" style="padding-bottom: 10px">
							<% if ( contentNode instanceof ProductModel ) {
								ProductImpression pi = new ProductImpression( (ProductModel)contentNode );
							%>
							
								<display:ProductRating product="<%= (ProductModel)contentNode %>" action="<%= actionUrl %>"/>
								<display:ProductName product="<%= (ProductModel)contentNode %>" action="<%= actionUrl %>"/>								
								<display:ProductPrice impression="<%= pi %>" showDescription="false"/>
								<display:FDCoupon coupon="<%= ref_coupons.get(i).get(j) %>" contClass="fdCoupon_layFeatCat"></display:FDCoupon>
								
							<% } else if ( contentNode instanceof CategoryModel ) { %>
							
								<display:CategoryName category="<%= (CategoryModel)contentNode %>" action="<%= actionUrl %>" style="font-style:normal"/>
								
							<% } %>							
						</td>
						
					<% } // links %>
			
				</tr>
			
			</table>
		<% }
	} // layouts
  } else { // specialDept
		int maxWidth = W_FEATURED_CATEGORY_WIDE_DEPARTMENT;
		

		String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
		DisplayObjectRepo repo = new DisplayObjectRepo(response,prodNameAttribute,trackingCode);
				
		if (noOfItems == categories.size()) {
		
		    int base = 0;

			for( int i = 0; i < pattern.length; i++ ) {
				
				%>
	     		 <table <% if (gap[i] < 0) { %>width="<%= maxWidth %>" <% } %>cellpadding="0" cellspacing="0" border="0" style="margin-top: 10px;">
					<tr>
						<%	// ======= IMAGE ROW =======
						int spareWidth = 0;

						if (gap[i] < 0) {
							for ( int j = 0; j < pattern[i]; j++ ) { // for each image
								CategoryModel category = categories.get(base + j);
								
								if ( category.getSeparatorMedia() != null || pattern[i] == 1 ) {
									spareWidth = 0;
									break;
								}
								
								if (category.getCategoryPhoto() != null)
									spareWidth += category.getCategoryPhoto().getWidth();
							}
							if (spareWidth != 0)
								spareWidth = maxWidth - spareWidth;
							if (spareWidth > 0)
								spareWidth = spareWidth / (pattern[i] + 1);
							else
								spareWidth = 0;
						} else {
							spareWidth = gap[i];
						}
						
						for ( int j = 0; j < pattern[i]; j++ ) { // for each image
							CategoryModel category = categories.get(base + j);
			
							if ( separatorFilter.evaluate(category) ) {
								Html html = category.getSeparatorMedia();
								%>
	              				<td align="center"><div style="text-align: center">
						        	<fd:IncludeMedia name="<%=html.getPath()%>"/>
	             				 </div></td>
								<%
								break;
							}
			
							DisplayObject displayObject = repo.getDisplayObject(category);
							String actionUrl = displayObject.getItemURL() + "&trk=" + trackingCode; 

	           				 if ((gap[i] < 0 || (gap[i] >= 0 && j > 0)) && spareWidth > 0) {
	                 			%>
	                   				<td width="<%=spareWidth%>"><div style="width: <%=spareWidth%>px;"></div></td>
	                 			<%
	                 			}
	     						%>
							<td align="center" style="padding-top: 10px">
								<display:CategoryImage category="<%= category %>" action="<%= actionUrl %>"/>
							</td>
			
						<% } // images 
           				 if (gap[i] < 0 && spareWidth > 0) {
                 			%>
                   				<td width="<%=spareWidth%>"><div style="width: <%=spareWidth%>px;"></div></td>
                 			<%
                 			}
     						%>
					</tr>
				
					<tr>
						<%	// ======= TEXT ROW =======
							
						for( int j = 0; j < pattern[i]; ++j ) { // for each link
							CategoryModel category = categories.get(base + j);
							if ( separatorFilter.evaluate(category) ) 
								break;
							DisplayObject displayObject = repo.getDisplayObject(category);
							String actionUrl = displayObject.getItemURL() + "&trk=" + trackingCode; 
					
           				 if ((gap[i] < 0 || (gap[i] >= 0 && j > 0)) && spareWidth > 0) {
            			%>
              				<td width="<%=spareWidth%>"><div style="width: <%=spareWidth%>px;"></div></td>
            			<%
            			}
						%>
							<td align="center" style="padding-bottom: 10px">
								<display:CategoryName category="<%= category %>" action="<%= actionUrl %>" style="font-style:normal"/>
							</td>
							
						<% } // links 
           				 if (gap[i] < 0 && spareWidth > 0) {
            			%>
              				<td width="<%=spareWidth%>"><div style="width: <%=spareWidth%>px;"></div></td>
            			<%
            			}
						%>
				
					</tr>
				
				</table>
			<%
			base += pattern[i];
		} // for
	} else { // no of items match
		%>
		<h1>ERROR - This department uses custom pattern for rendering the categories as per requested by Ruchi.</h1>
		<h2>This feature has been implemented for site widening.</h2>
		<h2>If you change the number of categories then we should modify the hardcoded pattern. Consult EU Edge for further details.</h2>
		<%
	}
  } // specialDept
%>
