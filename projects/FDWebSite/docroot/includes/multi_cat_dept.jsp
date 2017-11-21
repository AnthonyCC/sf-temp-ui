<%@ page import='com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings' %>
<%@ page import='com.freshdirect.fdstore.content.util.SortStrategyElement' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.StringTokenizer' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import="java.util.*"%>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>

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
	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(catId_1);
	List edlpProducts=new ArrayList();
	List prodList=new ArrayList();
	int totalDealProds = 0;
	int totalEDLPProds = 0;
	Collection dealCol = null;
	Collection edlpCol = null;
	Collection dealProdList = new ArrayList();
	Collection edlpProdList = new ArrayList();
	Settings layoutSettings = new Settings();
	layoutSettings.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, false));
	List tmpList=new ArrayList();
	FDUserI mcd_user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);

%>
<%-- Grab items on deals --%>
<% if(currentFolder != null && currentFolder instanceof CategoryModel){ %>
<fd:ItemGrabber
	category='<%=currentFolder %>' 
	id='rtnColl' 
	depth='99'
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
		totalDealProds = dealCol.size();
        request.setAttribute("itemGrabberResult",dealCol); //** expose result of item grabber to the layout **
%>
</fd:ItemGrabber>
<fd:ItemSorter nodes='<%=(List)dealCol%>' strategy='<%=layoutSettings.getSortStrategy()%>'/>

<logic:iterate id="contentNode" collection="<%= dealCol %>" type="java.lang.Object">
	<%if(contentNode instanceof CategoryModel) { continue; }
	  else if(contentNode instanceof ProductModel){
		  if(((ProductModel)contentNode).isUnavailable()){ continue;}
		  dealProdList.add(contentNode);
		}%>
</logic:iterate>

<% } %>

<%-- Grab edlp items --%>
<% currentFolder = ContentFactory.getInstance().getContentNode(catId_2); %>
<% if(currentFolder instanceof CategoryModel){ %>
<fd:ItemGrabber
	category='<%=currentFolder %>' 
	id='rtnColl' 
	depth='99'
	ignoreShowChildren='true' 
	filterDiscontinued='true'
	returnHiddenFolders='true'
	ignoreDuplicateProducts='true'
	returnSecondaryFolders='<%=layoutSettings.isReturnSecondaryFolders()%>' 
	returnSkus='<%=layoutSettings.isReturnSkus()%>'
>
<%
		edlpCol = rtnColl;        
		totalEDLPProds = edlpCol.size();
        request.setAttribute("itemGrabberResult",edlpCol); //** expose result of item grabber to the layout **
%>
</fd:ItemGrabber>
<fd:ItemSorter nodes='<%=(List)edlpCol%>' strategy='<%=layoutSettings.getSortStrategy()%>'/>

<logic:iterate id="contentNode" collection="<%= edlpCol %>" type="java.lang.Object">
	<%if(contentNode instanceof CategoryModel) { continue; }
	  else if(contentNode instanceof ProductModel){
		  if(((ProductModel)contentNode).isUnavailable()){ continue;}
		  edlpProdList.add(contentNode);
		}%>
</logic:iterate>
<% } %>

<% if(dealProdList.size() >= MIN_PROD2SHOW){ %>
	<% String mediaPath = "/media/editorial/meat/"+catId_1+".html"; %>
	<fd:IncludeMedia name= "<%= mediaPath %>"/>
    <table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
		<logic:iterate id="contentNode" collection="<%= dealProdList %>" type="java.lang.Object" indexId="idx">
			<%//if(idx.intValue()==MAX_PROD2SHOW) break; %>
			<% 
			  if(contentNode instanceof CategoryModel) { continue; }
			  else if(contentNode instanceof ProductModel){
				  if(((ProductModel)contentNode).isUnavailable()){ continue;}
				  prodList.add(contentNode);
				  ProductModel dealProduct = (ProductModel) contentNode;
				  String prodNameAttribute = JspMethods.getProductNameToUse(dealProduct);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId_1,dealProduct,prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				  String actionUrl = FDURLUtil.getProductURI( dealProduct, "dept" );
			 %>
			 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
				<display:ProductImage product="<%= dealProduct %>" showRolloverImage="false" action="<%= actionUrl %>"/>
			 </td>
			 <% if((idx.intValue() > 0 && (idx.intValue()%PRODUCTS_PER_LINE)==PRODUCTS_PER_LINE-1) ||
					 idx.intValue() == totalDealProds-1){ %>
			 	</tr>
			 	<tr valign="top">
			 		<logic:iterate id="tmpContentNode" collection="<%= prodList %>" type="java.lang.Object" indexId="idx1">
			 			<% 
						  if(tmpContentNode instanceof CategoryModel) { continue; }
						  else if(tmpContentNode instanceof ProductModel){
							  ProductModel pm = (ProductModel) tmpContentNode;
								ProductImpression pi = new ProductImpression( pm );
							  DisplayObject displayObjTmp = JspMethods.loadLayoutDisplayStrings(response,catId_1,pm,JspMethods.getProductNameToUse(pm),true);
							  int adjustedImgWidthTmp = displayObjTmp.getImageWidthAsInt()+6+10;
							  String actionUrlTmp = FDURLUtil.getProductURI( pm, "dept" );
						 %>
						 <td align="center" width="<%=adjustedImgWidthTmp%>" style="padding-left:5px; padding-right:5px;">
							<display:ProductName product="<%= pm %>" action="<%= actionUrlTmp %>"/>								
							<display:ProductPrice impression="<%= pi %>" showAboutPrice="false" showDescription="false"/>
							<%
								FDCustomerCoupon curCoupon = null;
								if (pi.getSku() != null && pi.getSku().getProductInfo() != null) {
									curCoupon = mcd_user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,pm.getParentId(),pm.getContentName());
								}
							%>
							<display:FDCoupon coupon="<%= curCoupon %>" contClass="fdCoupon_layMultiCatDeptDeal"></display:FDCoupon>
						 </td>
						 <% if(idx1.intValue() > 0 && (idx1.intValue()%PRODUCTS_PER_LINE)==PRODUCTS_PER_LINE-1){ break; } %>
						 <%} %>
			 		</logic:iterate>
			 	</tr>
			 	<tr><td><img src="/media_stat/images/layout/clear.gif" alt="" width="25" height="25"></td></tr>
			 	</table>
			 	<table cellpadding="0" cellspacing="0" border="0"><tr>
			 <% prodList = new ArrayList(); //create a new list for the next row of products
			 	} 
			 } %>
		</logic:iterate>
		</tr>
		</table>
		
<%} %>


<% if(edlpProdList.size() >= MIN_PROD2SHOW){ %>
	<% String mediaPath = "/media/editorial/meat/"+catId_2+".html"; %>
	<fd:IncludeMedia name= "<%= mediaPath %>"/>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
		<logic:iterate id="contentNode" collection="<%= edlpProdList %>" type="java.lang.Object" indexId="idx">
			<%//if(idx.intValue()==MAX_PROD2SHOW) break; %>
			<% 
			  if(contentNode instanceof CategoryModel) { continue; }
			  else if(contentNode instanceof ProductModel){
				  if(((ProductModel)contentNode).isUnavailable()){ continue;}
				  prodList.add(contentNode);
				  ProductModel edlpProduct = (ProductModel) contentNode;
					ProductImpression edlpPi = new ProductImpression( edlpProduct );
				  String prodNameAttribute = JspMethods.getProductNameToUse(edlpProduct);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,catId_2,edlpProduct,prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				  String actionUrl = FDURLUtil.getProductURI( edlpProduct, "dept" );
			 %>
			 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
				<display:ProductImage product="<%= edlpProduct %>" showRolloverImage="false" action="<%= actionUrl %>"/>
			 </td>
			 <% if((idx.intValue() > 0 && (idx.intValue()%PRODUCTS_PER_LINE)==PRODUCTS_PER_LINE-1) ||
					 idx.intValue() == totalEDLPProds-1){ %>
			 	</tr>
			 	<tr valign="top">
				<logic:iterate id="tmpContentNode" collection="<%= prodList %>" type="java.lang.Object" indexId="idx1">
					<%//if(idx.intValue()==MAX_PROD2SHOW) break; %>
					<% 
					  if(tmpContentNode instanceof CategoryModel) { continue; }
					  else if(tmpContentNode instanceof ProductModel){
						  ProductModel pm = (ProductModel) tmpContentNode;
						  DisplayObject displayObjTmp = JspMethods.loadLayoutDisplayStrings(response,catId_2,pm,JspMethods.getProductNameToUse(pm),true);
						  int adjustedImgWidthTmp = displayObjTmp.getImageWidthAsInt()+6+10;
						  String actionUrlTmp = FDURLUtil.getProductURI( pm, "dept" );
					 %>
					 <td align="center" width="<%=adjustedImgWidthTmp%>" style="padding-left:5px; padding-right:5px;">
						<display:ProductName product="<%= pm %>" action="<%= actionUrlTmp %>"/>								
						<display:ProductPrice impression="<%= edlpPi %>" showAboutPrice="false" showDescription="false"/>
						<%
							FDCustomerCoupon curCoupon = null;
							if (edlpPi.getSku() != null && edlpPi.getSku().getProductInfo() != null) {
								curCoupon = mcd_user.getCustomerCoupon(edlpPi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,pm.getParentId(),pm.getContentName());
							}
						%>
						<display:FDCoupon coupon="<%= curCoupon %>" contClass="fdCoupon_layMultiCatDeptEdlp"></display:FDCoupon>
					 </td>
					 <% if(idx1.intValue() > 0 && (idx1.intValue()%PRODUCTS_PER_LINE)==PRODUCTS_PER_LINE-1){ break; }%> 
					 <% } %>
				</logic:iterate>
				</tr>
			 	<tr><td><img src="/media_stat/images/layout/clear.gif" alt="" width="25" height="25"></td></tr>
			 	</table>
			 	<table cellpadding="0" cellspacing="0" border="0"><tr>
			 <% prodList = new ArrayList(); //create a new list for the next row of products
			 	} 
			 } %>
		</logic:iterate>
		</tr>
		</table>
		
<%} %>
