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
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<script type="text/javascript">
	OAS_AD('CategoryNote');
</script>

<display:InitLayout/>

<%
	//**************************************************************
	//***          the GENERIC_LAYOUT Pattern                    ***
	//**************************************************************

	String altText = null;
	String organicFlag = "";
	String prodNameAttribute = JspMethods.getProductNameToUse( currentFolder );
	int counter = 0;
	int tablewidth = 0;
	int tdwidth = 0;
	int maxCols = 0;
	int maxWidth = 0;
	Image img = null;
	int itemWidth = 0;
	int totalWidth = 0;
	String itemNameFont = null;
	StringBuffer tblCells = new StringBuffer( 600 );

	if ( isDepartment.booleanValue() ) {
		maxCols = 5;
		maxWidth = 550;
		tdwidth = 20;
	} else {
		maxCols = 4;
		maxWidth = 410;
		tdwidth = 25;
	}

	
	DisplayObject displayObj = null;
	
	// if we are on the vegetable folder: (not in a subfolder of veg) then don't show any products.
	boolean showOnlyFolders = false;
	int rowsPainted = 0;
	if ( ( (ContentNodeModel)currentFolder ).getFullName().toUpperCase().indexOf( "VEGETABLES" ) >= 0 || ( (ContentNodeModel)currentFolder ).getFullName().toUpperCase().indexOf( "SEAFOOD" ) >= 0 ) {
		showOnlyFolders = true;
	}
%>
<jsp:include page="/includes/department_peakproduce.jsp" flush="true"/>

<% if ( isDepartment.booleanValue() && "fru".equals( departmentId ) ) { %>
   	<br/><img src="/media_stat/images/layout/ourfreshfruit.gif" name="Our Fresh Fruit" width="535" height="15" border="0">
<% } %>

<br/>[horizontal-pattern]<br/>

<display:HorizontalPattern 
	id="horizontalPattern" 
	itemsToShow="<%=sortedCollection%>" 
	showCategories="<%= isDepartment.booleanValue() || showOnlyFolders %>"
	showProducts="<%= !showOnlyFolders %>"
	productCellWidth="<%= isDepartment.booleanValue() ? 137 : 100 %>" 
	folderCellWidth="137" 
	currentFolder="<%= currentFolder %>" 
	useLayoutPattern="false" 
	maxColumns="<%= isDepartment.booleanValue() ? 5 : 4 %>" 
	dynamicSize="true" 
	tableWidth="<%= isDepartment.booleanValue() ? 550 : 410 %>"
	>		
		
	<table cellspacing="0" cellpadding="0" width="<%=tableWidth%>">
	<tr align="center" valign="top">	
		
		<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
		
			<% if ( currentItem instanceof ProductModel ) { 				// ===== PRODUCT =====
				
				ProductModel product = (ProductModel)currentItem;
				String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
				%>
			
				<td width="<%= horizontalPattern.getProductCellWidth() %>"><font class="catPageProdNameUnderImg">
					
					<display:ProductImage product="<%= product %>" showRolloverImage="true" action="<%= actionUrl %>"/><br/>
					<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
					<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
					<display:ProductPrice impression="<%= new ProductImpression(product) %>"/>
					
				</font></td> 
				
			<% } else if ( currentItem instanceof CategoryModel ) { 		// ===== CATEGORY ===== 
				
				CategoryModel category = (CategoryModel) currentItem;
				String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode ); 
				%>
				
				<td width="<%= horizontalPattern.getFolderCellWidth() %>"><font class="text11">
				
					<display:CategoryImage category="<%= category %>" action="<%= actionUrl %>"/>
					<display:CategoryName category="<%= category %>" action="<%= actionUrl %>" style="font-style:normal"/>		
					
				</font></td> 
				
			<% } else { // TODO remove debug info display %>
				[ ERROR : <%= currentItem.getFullName() %> is a <%= currentItem.getClass().toString() %> ]<br/>				
			<% } %>
		
		</display:PatternRow>			
	</tr>
	</table>
	
	<font class="space4pix"><br/>&nbsp;<br/></font>
	
</display:HorizontalPattern>


	
<%
	//**** bottom Of Jsp's  *******************
	if ( onlyOneProduct.booleanValue() ) {
		request.setAttribute( "theOnlyProduct", theOnlyProduct );
	}
%>	
	