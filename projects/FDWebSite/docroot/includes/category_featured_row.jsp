<%@ page import='com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings' %>
<%@ page import='com.freshdirect.fdstore.content.util.SortStrategyElement' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.StringTokenizer' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import="java.util.*"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%
	String deptIdCheck = request.getParameter("deptId");
	String trkCode = "";
	int productsTarget = 4;
	int dealProductsCount = 2;
	int edlpProductsCount = 2;
	int displayProdCount = 0;
	String parentCat = request.getParameter("parentCat");
	String catId_1 = parentCat+"_feat";
	String catId_2 = parentCat+"_edlp"; 
	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(catId_1);
	List edlpProducts=new ArrayList();
	Collection dealCol = new ArrayList();
	Collection edlpCol = new ArrayList();
	Settings layoutSettings = new Settings();
	layoutSettings.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, false));
	List tmpList=new ArrayList();
%>
<%-- Grab items on deals --%>
<% if(currentFolder != null && currentFolder instanceof CategoryModel){ %>
<fd:ItemGrabber
	category='<%=currentFolder %>' 
	id='rtnColl' 
	depth='1'
	ignoreShowChildren='<%=layoutSettings.isIgnoreShowChildren()%>' 
	filterDiscontinued='true'
	returnHiddenFolders='<%=layoutSettings.isReturnHiddenFolders()%>'
	ignoreDuplicateProducts='true'
	returnSecondaryFolders='<%=layoutSettings.isReturnSecondaryFolders()%>' 
	returnSkus='<%=layoutSettings.isReturnSkus()%>'
>
<%
		dealCol = rtnColl;        
        //request.setAttribute("itemGrabberResult",dealCol); //** expose result of item grabber to the layout **
%>
</fd:ItemGrabber>
<fd:ItemSorter nodes='<%=(List)dealCol%>' strategy='<%=layoutSettings.getSortStrategy()%>'/>
<% } %>
<%-- Grab edlp items --%>
<% currentFolder = ContentFactory.getInstance().getContentNode(catId_2); %>
<% if(currentFolder instanceof CategoryModel){ %>
<fd:ItemGrabber
	category='<%=currentFolder %>' 
	id='rtnColl' 
	depth='1'
	ignoreShowChildren='<%=layoutSettings.isIgnoreShowChildren()%>' 
	filterDiscontinued='true'
	returnHiddenFolders='<%=layoutSettings.isReturnHiddenFolders()%>'
	ignoreDuplicateProducts='true'
	returnSecondaryFolders='<%=layoutSettings.isReturnSecondaryFolders()%>' 
	returnSkus='<%=layoutSettings.isReturnSkus()%>'
>
<%
		edlpCol = rtnColl;        
        //request.setAttribute("itemGrabberResult",edlpCol); //** expose result of item grabber to the layout **
%>
</fd:ItemGrabber>
<fd:ItemSorter nodes='<%=(List)edlpCol%>' strategy='<%=layoutSettings.getSortStrategy()%>'/>
<% } %>

<% 	//set the sizes
	dealProductsCount = dealCol.size();
	edlpProductsCount = edlpCol.size();
	
	if(dealProductsCount > 2){
		dealProductsCount = 2;
		if(edlpProductsCount > 2){
			dealProductsCount = 2;
			edlpProductsCount = 2;
		}else{
			dealProductsCount = productsTarget - edlpProductsCount;
		}
	}else{
		if(edlpProductsCount > 2)
			edlpProductsCount = productsTarget - dealProductsCount;
	}
	displayProdCount = dealProductsCount + edlpProductsCount;
%>

<% if(displayProdCount>1){ %>
	<fd:IncludeMedia name= "/media/editorial/meat/meat_deals_everyday.html"/>
    <table cellpadding="0" cellspacing="0" border="0">
	<tr valign="bottom">
		<logic:iterate id="contentNode" collection="<%= dealCol %>" type="java.lang.Object" indexId="idx">
			<%if(!(idx.intValue()>=dealProductsCount)) {
			  if(!(contentNode instanceof CategoryModel)) { 
			   if(contentNode instanceof ProductModel){
				  if(!(((ProductModel)contentNode).isUnavailable())){ 
				  ProductModel dealProduct = (ProductModel) contentNode;
				  String prodNameAttribute = JspMethods.getProductNameToUse(dealProduct);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId_1,dealProduct,prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				  String actionUrl = FDURLUtil.getProductURI( dealProduct, "dept" );
			 %>
			 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
				<display:ProductImage product="<%= dealProduct %>" showRolloverImage="false" action="<%= actionUrl %>"/>
			 </td>
			 <% } } } }%>
		</logic:iterate>
			<logic:iterate id="contentNode" collection="<%= edlpCol %>" type="java.lang.Object" indexId="idx">
			<% 
			  if(!(idx.intValue()>=edlpProductsCount)) {
			  if(!(contentNode instanceof CategoryModel)) {
			   if(contentNode instanceof ProductModel){
				  if(!(((ProductModel)contentNode).isUnavailable())){ 
				  ProductModel edlpProduct = (ProductModel) contentNode;
				  String prodNameAttribute = JspMethods.getProductNameToUse(edlpProduct);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId_2,edlpProduct,prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				  String actionUrl = FDURLUtil.getProductURI( edlpProduct, "dept" );
			 %>
			 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
				<display:ProductImage product="<%= edlpProduct %>" showRolloverImage="false" action="<%= actionUrl %>"/>
			 </td>
			 <% } } } }%>
		</logic:iterate>
		</tr>
		<tr>
			<logic:iterate id="contentNode" collection="<%=dealCol %>" type="java.lang.Object" indexId="idx">
			<%
				if(!(idx.intValue()>=dealProductsCount)) {
				if(!(contentNode instanceof CategoryModel)) { 
				 if(contentNode instanceof ProductModel){
					if(!(((ProductModel)contentNode).isUnavailable())){ 
					ProductModel dealProduct = (ProductModel) contentNode;
					String prodNameAttribute = JspMethods.getProductNameToUse(dealProduct);
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId_1,dealProduct,prodNameAttribute,true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
					String actionUrl = FDURLUtil.getProductURI( dealProduct, "dept" );
			%>

				<td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
					<display:ProductName product="<%= dealProduct %>" action="<%= actionUrl %>"/>								
					<display:ProductPrice impression="<%= new ProductImpression( dealProduct ) %>" showAboutPrice="false" showDescription="false"/>
				</td>
				<% } } } }%>
			</logic:iterate>
			<logic:iterate id="contentNode" collection="<%= edlpCol %>" type="java.lang.Object" indexId="idx">
			<%
				if(!(idx.intValue()>=edlpProductsCount)) {
				if(!(contentNode instanceof CategoryModel)) { 
				 if(contentNode instanceof ProductModel){
					if(!(((ProductModel)contentNode).isUnavailable())){ 
					ProductModel edlpProduct = (ProductModel) contentNode;
					String prodNameAttribute = JspMethods.getProductNameToUse(edlpProduct);
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId_2,edlpProduct,prodNameAttribute,true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
					String actionUrl = FDURLUtil.getProductURI( edlpProduct, "dept" );
			%>

				<td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
					<display:ProductName product="<%= edlpProduct %>" action="<%= actionUrl %>"/>								
					<display:ProductPrice impression="<%= new ProductImpression( edlpProduct ) %>" showAboutPrice="false" showDescription="false"/>
				</td>
				<% } } } }%>
		</logic:iterate>
		</tr>
		</table>
<% } %>
