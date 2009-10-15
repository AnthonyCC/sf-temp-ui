<%@ page import='com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings' %>
<%@ page import='com.freshdirect.fdstore.content.util.SortStrategyElement' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
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
	int favoritesToShow = 0;
	int PRODUCTS_PER_LINE		= 5; //number of products to show per line
	int MAX_PRODLINES2SHOW	= 2; //number of lines of featured products
	int MIN_PROD2SHOW		= 3; //minimum featured products required to show featured section
	int	MAX_PROD2SHOW		= PRODUCTS_PER_LINE*MAX_PRODLINES2SHOW; //to keep each row matching
	String catId_1 = FDStoreProperties.getDeptMeatDealsCatId();
	String catId_2 = FDStoreProperties.getDeptEDLPCatId(); 
	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNodeByName(catId_1);
	System.out.println("multi_cat_dept>> catId_1: " + catId_1 + " catId_2: " + catId_2);
	List edlpProducts=new ArrayList();
	Collection dealCol = null;
	Collection edlpCol = null;
	Settings layoutSettings = new Settings();
	layoutSettings.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, false));
	List tmpList=new ArrayList();
%>
<%-- Grab items on deals --%>
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
	workSet='<%=tmpList%>'
>
<%
		dealCol = rtnColl;        
		System.out.println("Deals>> dealCol size: " + dealCol.size());
		System.out.println("Deals>> dealCol: " + dealCol);
        request.setAttribute("itemGrabberResult",dealCol); //** expose result of item grabber to the layout **
%>
</fd:ItemGrabber>
<fd:ItemSorter nodes='<%=(List)dealCol%>' strategy='<%=layoutSettings.getSortStrategy()%>'/>

<%-- Grab edlp items --%>
<% currentFolder = ContentFactory.getInstance().getContentNodeByName(catId_2); %>
<fd:ItemGrabber
	category='<%=currentFolder %>' 
	id='rtnColl' 
	depth='1'
	ignoreShowChildren='true' 
	filterDiscontinued='true'
	returnHiddenFolders='true'
	ignoreDuplicateProducts='true'
	returnSecondaryFolders='<%=layoutSettings.isReturnSecondaryFolders()%>' 
	returnSkus='<%=layoutSettings.isReturnSkus()%>'
>
<%
		edlpCol = rtnColl;        
		System.out.println("EDLP>> edlpCol size: " + edlpCol.size());
		System.out.println("EDLP>> edlpCol: " + edlpCol.size());
        request.setAttribute("itemGrabberResult",edlpCol); //** expose result of item grabber to the layout **
%>
</fd:ItemGrabber>
<fd:ItemSorter nodes='<%=(List)edlpCol%>' strategy='<%=layoutSettings.getSortStrategy()%>'/>

<% if(dealCol.size() >= MIN_PROD2SHOW){ %>
	<br /><img src="/media_stat/images/layout/cccccc.gif" width="550" height="1" border="0"><br />
	<div align="center"><img src="/media/images/navigation/department/meat/meat_deals.gif"></img></div>
    <table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
		<logic:iterate id="contentNode" collection="<%= dealCol %>" type="java.lang.Object" indexId="idx">
			<%//if(idx.intValue()==MAX_PROD2SHOW) break; %>
			<% 
			  if(contentNode instanceof CategoryModel) { continue; }
			  else if(contentNode instanceof ProductModel){
				  ProductModel dealProduct = (ProductModel) contentNode;
				  String prodNameAttribute = JspMethods.getProductNameToUse(dealProduct);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId_1,dealProduct,prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				  String actionUrl = FDURLUtil.getProductURI( dealProduct, "dept" );
			 %>
			 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
				<display:ProductImage product="<%= dealProduct %>" showRolloverImage="false" action="<%= actionUrl %>"/><br>
				<display:ProductName product="<%= dealProduct %>" action="<%= actionUrl %>"/><br>								
				<display:ProductPrice impression="<%= new ProductImpression( dealProduct ) %>" showAboutPrice="false" showDescription="false"/>
			 </td>
			 <% if(idx.intValue() > 0 && (idx.intValue()%PRODUCTS_PER_LINE)==PRODUCTS_PER_LINE-1){ %>
			 	</tr><tr><td><img src="/media_stat/images/layout/clear.gif" width="25" height="25"></td></tr></table>
			 	<table cellpadding="0" cellspacing="0" border="0"><tr>
			 <% } 
			 } %>
		</logic:iterate>
		</tr>
		</table>
		
<%} %>


<% if(edlpCol.size() >= MIN_PROD2SHOW){ %>
	<br /><img src="/media_stat/images/layout/cccccc.gif" width="550" height="1" border="0"><br />
	<div align="center"><img src="/media/images/navigation/department/meat/meat_deals_edlp.gif"></img></div>
    <table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
		<logic:iterate id="contentNode" collection="<%= edlpCol %>" type="java.lang.Object" indexId="idx">
			<%//if(idx.intValue()==MAX_PROD2SHOW) break; %>
			<% 
			  if(contentNode instanceof CategoryModel) { continue; }
			  else if(contentNode instanceof ProductModel){
				  ProductModel edlpProduct = (ProductModel) contentNode;
				  String prodNameAttribute = JspMethods.getProductNameToUse(edlpProduct);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId_2,edlpProduct,prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				  String actionUrl = FDURLUtil.getProductURI( edlpProduct, "dept" );
			 %>
			 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
				<display:ProductImage product="<%= edlpProduct %>" showRolloverImage="false" action="<%= actionUrl %>"/><br>
				<display:ProductName product="<%= edlpProduct %>" action="<%= actionUrl %>"/><br>								
				<display:ProductPrice impression="<%= new ProductImpression( edlpProduct ) %>" showAboutPrice="false" showDescription="false"/>
			 </td>
			 <% if(idx.intValue() > 0 && (idx.intValue()%PRODUCTS_PER_LINE)==PRODUCTS_PER_LINE-1){ %>
			 	</tr><tr><td><img src="/media_stat/images/layout/clear.gif" width="25" height="25"></td></tr></table>
			 	<table cellpadding="0" cellspacing="0" border="0"><tr>
			 <% } 
			 } %>
		</logic:iterate>
		</tr>
		</table>
		
<%} %>
