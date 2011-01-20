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
<%@ page import="com.freshdirect.framework.webapp.*" %>
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
	if (deptId!=null) {
	    currentFolder=ContentFactory.getInstance().getContentNode(deptId);
	    isDepartment = true;
	} else {
	    currentFolder=ContentFactory.getInstance().getContentNode(catId);
	}	
	
	boolean onlyOneProduct = false;
	ProductModel theOnlyProduct = null;
	//Siva-Changed Tracking Code Retrieval
	String trkCode = (String)request.getAttribute("trk");
	
	if (trkCode!=null && !"".equals(trkCode.trim()) ) {
	    trkCode = "&trk="+trkCode.trim();
	} else {
	    trkCode = "";
	}
	
	Collection<ContentNodeModel> sortedColl = (Collection<ContentNodeModel>) request.getAttribute("itemGrabberResult");
	if (sortedColl==null)
		sortedColl = new ArrayList<ContentNodeModel>();
	
	String BrowseHeader = null;
	String FeaturedHeader = null;
	
	String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
	String newProdsFldrId = currentFolder.getContentName().toLowerCase()+"_new";
	// !!! we should assign the broweHeader item to the folder_label attrib...and create a new one for the feature header
	String currFolderId = currentFolder.getContentName();
	if ( currFolderId.equalsIgnoreCase("DAI") ) {
	    BrowseHeader = "/media/images/navigation/department/dairy/dai_browse_categories.gif";
	    FeaturedHeader = "/media/images/navigation/department/dairy/dai_featured_items.gif";
	} else if ( currFolderId.equalsIgnoreCase("GRO") ) {
	    BrowseHeader = "/media/images/navigation/department/grocery/gro_browse_categories.gif";
	    FeaturedHeader = "/media/images/navigation/department/grocery/gro_featured_items.gif";
	} else if ( currFolderId.equalsIgnoreCase("FRO") ) {
	    BrowseHeader = "/media/images/navigation/department/frozen/fro_browse_categories.gif";
	    FeaturedHeader = "/media/images/navigation/department/frozen/fro_featured_items.gif";
	} else if ( currFolderId.equalsIgnoreCase("SPE") ) {
	    BrowseHeader = "/media/images/navigation/department/specialty/spe_browse_categories.gif";
	    FeaturedHeader = "/media/images/navigation/department/specialty/spe_featured_items.gif";
	} else if ( currFolderId.equalsIgnoreCase("HBA") ) {
	    BrowseHeader = "/media/images/navigation/department/hba/hba_cat/hba_browse_categories.gif";
	    FeaturedHeader = "/media/images/navigation/department/hba/hba_cat/hba_featured.gif";
	} else if ( currFolderId.toLowerCase().indexOf("_picks")!=-1 ) {
	    BrowseHeader =    "/media_stat/images/navigation/department/our_picks/our_picks_browse.gif";
	    FeaturedHeader =  "/media_stat/images/navigation/department/our_picks/our_fav_picks.gif";
	} else {
	    BrowseHeader = "/media_stat/images/layout/clear.gif";
	    FeaturedHeader =  "/media_stat/images/layout/clear.gif";
	}
%>

<table cellpadding="0" cellspacing="0" border="0" width="550">
    <tr valign="top">
    	<td width="550"> <%-- this is the category column --%>
        	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		    	<% if (currentFolder.getEditorialTitle() != null && currentFolder.getEditorialTitle().length() > 0) { %>
					<tr id="tr-001"><td colspan="6">
						<span class="title16"><%= currentFolder.getEditorialTitle() %></span>
					</td></tr>
	
					<!-- separator  -->
					<tr id="tr-002"><td colspan="6">
						<br/><img src="/media_stat/images/layout/cccccc.gif" width="550" height="1" border="0"><br/>
						<font CLASS="space4pix"><br/><br/></font>
					</td></tr>
				<% } %>

	            <tr id="tr-003">
	                <td colspan="6"><img src="<%= BrowseHeader %>"><br/><br/></td>
	            </tr>
	            
				<%
			    //display the subfolders 
			
			    StringBuilder imageCell = new StringBuilder("");
			    StringBuilder listColumn1 = new StringBuilder("");
			    StringBuilder listColumn2 = new StringBuilder("");
			
			    int firstRowSize = sortedColl.size()/2 + (sortedColl.size()%2);
			    String IMG_NAME = "subfolder_image";
			
			    int count = 0;
			    int imgIndex = 0;
			    CategoryModel categoryModel = null;
		        ContentNodeModel itmNode = null;
		        Image groDeptImage = null;
		        boolean makeImageCol = true;
		        StringBuilder tmpColumn = new StringBuilder();
		    	for(Iterator<ContentNodeModel> itmIter = sortedColl.iterator();itmIter.hasNext();) {
		            itmNode = itmIter.next();
		            if (! (itmNode instanceof CategoryModel)) continue;  //ignore anything that's not a Category
		            categoryModel = (CategoryModel)itmNode;
		            groDeptImage = categoryModel.getCategoryPhoto();
		
		            if (makeImageCol && !categoryModel.getContentName().equalsIgnoreCase(newProdsFldrId)) { //build the image column
		                makeImageCol = false;
		                imageCell.append("<img name=\"");
		                imageCell.append(IMG_NAME);
		                imageCell.append("\" src=\"");
		                imageCell.append(groDeptImage!=null?groDeptImage.getPath():"");
		                imageCell.append("\" ");
		                imageCell.append(JspMethods.getImageDimensions(groDeptImage));
		                imageCell.append("border=\"0\">"); //<BR><img src=\"
		            }
		            tmpColumn.setLength(0);
		            tmpColumn.append("<a href=\"");
		            tmpColumn.append(response.encodeURL( "/category.jsp?catId=" + categoryModel + "&trk=dpage")); //here
		            tmpColumn.append("\" onMouseover='");
		            tmpColumn.append("swapImage(\""+IMG_NAME+"\",\""+(groDeptImage!=null?groDeptImage.getPath():"")+"\"");
		            tmpColumn.append(")'>");
		            tmpColumn.append(categoryModel.getFullName());
		            tmpColumn.append("</a>");
		            if (categoryModel.getContentName().equalsIgnoreCase(newProdsFldrId)) {
		                tmpColumn.append("&nbsp;<img src=\"/media_stat/images/template/newproduct/star_CC0033.gif\" width=\"12\" height=\"12\"  border=\"0\">");
		            } 
		            tmpColumn.append("<br><img src=\"/media_stat/images/layout/clear.gif\" width=\"1\" height=\"5\"  border=\"0\"><br>");
		
		            if (count < firstRowSize) {
			            listColumn1.append(tmpColumn.toString());
		            } else {
			            listColumn2.append(tmpColumn.toString());
		            }
		
		            count++;
		            imgIndex++;
			
			    } //
			
			    //writing the actual html for displaying all subfolders
				%>
				
				<tr id="tr-004">
					<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
					<td width="100" valign="top"><%= imageCell.toString() %></td>
					<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
					<td width="205" valign="top" class="text13bold"><%= listColumn1.toString() %></td>
					<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
					<td width="205" valign="top" class="text13bold"><%= listColumn2.toString() %></td>
				</tr><%-- end of categories --%>

				<%-- //Featured Products moved into Include --%>
					<%@ include file="/includes/layouts/i_featured_products.jspf" %>
				<%-- //END Featured Products --%>

				<%
				// display the featured brands, based on each of the featured categories
		        List<CategoryModel> featuredCats = ((DepartmentModel)currentFolder).getFeaturedCategories();
		        List<BrandModel> featuredBrands = null;
		        int trimAt=10;
		
		        int brandsShown=0;
		
		        if (featuredCats != null && featuredCats.size() > 0) { %>
					<!-- separator  -->
		            <tr id="tr-005"><td colspan="6"><br/>
	            		<img src="/media_stat/images/layout/cccccc.gif" width="550" height="1" border="0"><br/>
	            		<font CLASS="space4pix"><br/><br/></font>
	            		<img src="/media_stat/images/layout/dfgs_featured_brands.gif" width="115" height="10" border="0"><br/><br/>
		            </td></tr>
		
		            <tr id="tr-006"><td colspan="6">
						<%
						for (int fc = 0; fc < featuredCats.size(); fc++) {
			                CategoryModel catMod= featuredCats.get(fc);
			                String catRefUrl = response.encodeURL("/category.jsp?catId="+catMod.getContentKey().getId()+"&trk=feat");
			  
			                featuredBrands = catMod.getFeaturedBrands();
			                brandsShown=0;
			                
			       			for(int i=0; i<featuredBrands.size();i++){                	
			                    BrandModel brandMod= featuredBrands.get(i);
			
			                    if (brandMod==null) 
			                    	continue;
			                    Image bLogo = brandMod.getLogoMedium();
			                    if (bLogo==null) {
			                        bLogo = new Image();
			                        bLogo.setPath("/media_stat/images/layout/clear.gif");
			                        bLogo.setWidth(12);
			                        bLogo.setHeight(30);
			                     };
			
			                    String brandLink = response.encodeURL("/category.jsp?catId="+catMod+"&brandValue="+brandMod.getContentName()+"&groceryVirtual="+catMod+"&trk=feat");
			                    brandsShown++;
								%><a href="<%= brandLink %>"><img src="<%= bLogo.getPath() %>" width="<%= bLogo.getWidth() %>" height="<%= bLogo.getHeight() %>" alt="<%= brandMod.getFullName() %>" border="0"></a><%= (brandsShown%trimAt)==0 ? "<br/>": "" %><%                      
			                }
			                   
							// put a break between categories, if any brand images were displayed
							if (brandsShown > 0) { 
					 			%><font class="space4pix"><br/></font><%
							}
			        	}
						%>
					</td></tr>
				<% } %>
			</table>
		</td><%-- end the category column --%>
	</tr>
</table>
