<%@ page import='com.freshdirect.fdstore.productpromotion.FDProductPromotionFactory' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.webapp.taglib.productpromotion.ErpProductPromotionUtil' %>
<%@ page import='com.freshdirect.erp.EnumProductPromotionType' %>
<%@ page import='com.freshdirect.erp.EnumFeaturedHeaderType' %>
<%@ page import='com.freshdirect.erp.ejb.FDProductPromotionManager' %>
<%@ page import='com.freshdirect.cms.ContentKey' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.FDProductInfo' %>
<%@ page import='com.freshdirect.fdstore.FDProductPromotionInfo' %>
<%@ page import='com.freshdirect.cms.ContentType' %>
<%@ page import='com.freshdirect.cms.fdstore.FDContentTypes' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%
List<EnumProductPromotionType> listTypes =EnumProductPromotionType.getEnumList();
List availablePricingZones = (List)ErpProductPromotionUtil.getAvailablePricingZones();
String zoneSelected = request.getParameter("zone");
String getProducts=request.getParameter("getProducts");
String typeSelected=request.getParameter("typeSelected");
String refresh = request.getParameter("refresh");
if(null !=refresh && null !=EnumProductPromotionType.getEnum(typeSelected)){
	FDProductPromotionFactory.getInstance().forceRefresh(typeSelected);
%>
<b><i><%= EnumProductPromotionType.getEnum(typeSelected).getDescription() %> cache refreshed successfully.</i><b>
<% }else{ %>

<%@page import="com.freshdirect.fdstore.FDCachedFactory"%><br/>
<% } %>
<form method="POST">
<br/>
<table><tr>
<td>Product Promotion Type :</td>
<td><select id="typeSelected" name="typeSelected" size="1">
	<logic:iterate id="promotionType" collection="<%= listTypes %>" type="com.freshdirect.erp.EnumProductPromotionType" indexId="idx">
		<% if("PRESIDENTS_PICKS".contains(promotionType.getName())){ %>
		<option value="<%= promotionType.getName() %>"  <%= null!=typeSelected &&typeSelected.equals(promotionType.getName())?"selected":"" %> ><%= promotionType.getDescription() %> </option>
		<% } %>
	</logic:iterate>
</select></td>
<td><input type="submit"  name="refresh" value="Refresh"/></td></tr>
<tr><td colspan="3">&nbsp;&nbsp;</td></tr>
<tr>
<td align="right">Zone:</td>
<td><select id="zone" name="zone" size="1">
	<logic:iterate id="zoneModel" collection="<%= availablePricingZones %>" type="com.freshdirect.customer.ErpZoneMasterInfo" indexId="idx">
		<%-- if(!selected.contains(zoneModel.getZoneDescriptor().getZoneCode())){ --%>
		<option value="<%= zoneModel.getSapId() %>"  <%= null!=zoneSelected &&zoneModel.getSapId().equals(zoneSelected)?"selected":"" %> ><%= zoneModel.getSapId() %>-<%= zoneModel.getDescription() %> </option>
		<%-- } --%>
	</logic:iterate>
</select></td> 
<td><input type="submit"  name="getProducts" value="Get Products"/></td></tr></table><br/>
<% if(null !=zoneSelected && null !=getProducts){ 
	List<ProductModel> productModelList = null;
	ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY,"picks_love");
	if(null != contentNode && contentNode instanceof CategoryModel){
		CategoryModel categoryModel = (CategoryModel)contentNode;	
		productModelList =categoryModel.getProducts();
	}
	Map<String,List<FDProductPromotionInfo>> productPromoInfoMap =FDProductPromotionManager.getProductPromotion(typeSelected);
	if(null != productPromoInfoMap){
		List<FDProductPromotionInfo> list = productPromoInfoMap.get(zoneSelected);
		if(null ==list || list.isEmpty()){%>
			No products found for the selected zone. Showing products for Master Default Zone-<%=ProductPromotionData.DEFAULT_ZONE %>.<br/><br/>
			<%list = productPromoInfoMap.get(ProductPromotionData.DEFAULT_ZONE);
		}
		if(null != list){
			FDProductPromotionInfo info1= (FDProductPromotionInfo)list.get(0);
		%>  <b>Version:</b><%= info1.getVersion()  %> &nbsp;&nbsp;<b>No.of Products:</b><%=list.size() %>
			<table border="1">
			<tr><td><b>Sku Code</td><td><b>Mat.#</td><td><b>Priority</td><td><b>Type</td><td><b>Featured Header</td><td><b>Mat. Status</td></tr>
			<%for(Iterator<FDProductPromotionInfo> itr=list.iterator();itr.hasNext();){
				FDProductPromotionInfo info = itr.next(); %>
				
				<tr><td>&nbsp;<%= info.getSkuCode() %></td>
				<td>&nbsp;<%= info.getMatNumber() %></td>
				<td>&nbsp;<%= info.getPriority() %></td>
				<%FDProductInfo fdInfo =FDCachedFactory.getProductInfo(info.getSkuCode()); %>
				<td><%= info.isFeatured()?"Featured":"Non-Featured" %></td>
				<% EnumFeaturedHeaderType fhType =EnumFeaturedHeaderType.getEnum(info.getFeaturedHeader()); %>
				<td><%= null!=fhType?fhType.getName():info.getFeaturedHeader() %></td>
				<td>&nbsp;<%= null !=fdInfo && fdInfo.isAvailable()?"Available":"Unavailable" %></td></tr>
			<%}%></table>
		<%} else {	%>	
		No products found for the selected zone.
	<%} }
	if(null !=productModelList && !productModelList.isEmpty()){
		
	}
 } %>
</form>