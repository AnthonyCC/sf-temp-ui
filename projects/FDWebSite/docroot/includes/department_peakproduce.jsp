<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.StringTokenizer' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.pricing.ProductPricingFactory' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<% FDUserI p_user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER); %>
<fd:ProduceRatingCheck>
<% 

	/*
	 * There is a similar setup in the JspMethods.java file
	 */

	boolean matchFound = false; //default to false

	//this is taking the place of a skuCode
	String deptIdCheck = request.getParameter("deptId");
	if (!"".equals(deptIdCheck) && deptIdCheck != null) {
		deptIdCheck= deptIdCheck.toUpperCase();
		
		// grab sku prefixes that should show ratings
		String _skuPrefixes=FDStoreProperties.getRatingsSkuPrefixes();

		//if we have prefixes then check them
		if (_skuPrefixes!=null && !"".equals(_skuPrefixes)) {
			StringTokenizer st=new StringTokenizer(_skuPrefixes, ","); //setup for splitting property
			String curPrefix = ""; //holds prefix to check against
			String spacer="* "; //spacing for sysOut calls
			
			//loop and check each prefix
			while(st.hasMoreElements()) {
				
				curPrefix=st.nextToken();

				//if prefix matches get product info
				if(deptIdCheck.startsWith(curPrefix)) {
					matchFound=true;
				}
				//exit on matched sku prefix
				if (matchFound) { break; }
				spacer=spacer+"   ";
			}
		}
	}

	if(matchFound) {
		%>
	
	<fd:GetPeakProduce deptId='<%= request.getParameter("deptId") %>' id='peakProduces'> 
	
			<%

			if(peakProduces.size()>0) {

				String mediaPath="/media/brands/fd_ratings/"+deptIdCheck+"/peak_produce.html";
			%>
				<fd:IncludeMedia name="<%=mediaPath%>" />

	<% ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(request.getParameter("deptId")); %>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
			<logic:iterate id="peakProduce" collection="<%= peakProduces %>" type="com.freshdirect.fdstore.content.SkuModel">
				<td>
				<% 
                  //Convert this to Product Pricing Adapter model for zone pricing.
                  ProductModel pm = ProductPricingFactory.getInstance().getPricingAdapter(peakProduce.getProductModel(), p_user.getPricingContext());
				  String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,pm.getParentNode().getContentName(),pm,prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				  String actionUrl = FDURLUtil.getProductURI( pm, "dept" );
				 %>
				 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
					<!-- APPDEV-401 Update product display(deals & burst) -->
					<display:ProductImage product="<%= pm %>" showRolloverImage="false" action="<%= actionUrl %>"/>
					<!--<a href="<%=displayObj.getItemURL()%>&trk=dept"><img src="<%= displayObj.getImagePath()%>"  <%=displayObj.getImageDimensions() %> ALT="<%=displayObj.getAltText()%>" vspace="0" hspace="0" border="0"></a>-->
				 </td>
			</logic:iterate>
		</tr>
		<tr valign="top">
			<logic:iterate id="peakProduce" collection="<%= peakProduces %>" type="com.freshdirect.fdstore.content.SkuModel">
				<td>
				<% 
				  String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
                  ProductModel pm = ProductPricingFactory.getInstance().getPricingAdapter(peakProduce.getProductModel(), p_user.getPricingContext());
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,pm.getParentNode().getContentName(),pm,prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				  String actionUrl = FDURLUtil.getProductURI( pm, "dept" );
				 %>
				 <td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
					<!-- APPDEV-401 Update product display(deals & burst) -->
					<display:ProductRating product="<%= pm %>" action="<%= actionUrl %>"/>
					<display:ProductName product="<%= pm %>" action="<%= actionUrl %>"/>								
					<display:ProductPrice impression="<%= new ProductImpression( pm ) %>" showAboutPrice="false" showDescription="false"/>
					<%  //if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>          
						<!--<img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif" name="rating" width="59" height="11" border="0" vspace="3">-->
					<% //} %>
					<!--<br><a href="<%=displayObj.getItemURL()%>&trk=dept" class="text11"><%=displayObj.getItemName()%></a>-->
					<%  //if (displayObj.getPrice()!=null) { %>
						<!--<br><span class="price"><%=displayObj.getPrice()%></span>-->
					<%  //} %>
				</td>
				</logic:iterate>
			</tr>
		</table>
	<%} %>
	</fd:GetPeakProduce>
<% }%>
</fd:ProduceRatingCheck>
