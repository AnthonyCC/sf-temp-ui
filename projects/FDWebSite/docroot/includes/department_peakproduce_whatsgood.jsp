<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>


<fd:ProduceRatingCheck>

<%
	//var defines
	int ppTarget = 6;
	int ppVeg = 0;
	int ppFru = 0;

	Collection vegCol = null;
	Collection fruCol = null;

	
	String catId = "picks_pres";
	//request.getParameter("catId");
	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNodeByName(catId);
	boolean isDept = (currentFolder instanceof DepartmentModel);
	boolean isCat = (currentFolder instanceof CategoryModel);
	String trkCode= "";
	//** pass the code that should be used as the tracking code **/
	if (isDept) {
		trkCode = "dpage";
		request.setAttribute("trk","dpage");
	} else if(isCat) {
		trkCode = "cpage";
		request.setAttribute("trk","cpage");
	}
%>


	
	<fd:GetPeakProduce deptId='fru' id='peakProduces' useMinCount='false'>
		<% fruCol = peakProduces; %>
	</fd:GetPeakProduce>

	<fd:GetPeakProduce deptId='veg' id='peakProduces' useMinCount='false'>
		<% vegCol = peakProduces; %>
	</fd:GetPeakProduce>

	<%
		//iterate over vegCol and remove dupes contained in fruCol
		Iterator iterator = vegCol.iterator();
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (fruCol.contains(element)) {
				iterator.remove();
			}
		}
		//set sizes
		ppVeg = vegCol.size();
		ppFru = fruCol.size();

		//logic to determine products from each dept to show
		if (ppVeg > 3) {
			//veg = 3
			ppVeg = 3;
			if (ppFru > 3){
				//veg = 3, fru = 3
				ppVeg = 3;
				ppFru = 3;
				//done
			}else{
				//fru<3
				//veg = tar-fru
				ppVeg = ppTarget-ppFru;
			}
		}else{
			//fru = tar - veg
			ppFru = ppTarget-ppVeg;
		}
		%>

		<% if (ppFru > 0 || ppVeg > 0) { // min to require to display section %>
			<fd:IncludeMedia name="/media/editorial/whats_good/whats_good_line.html" />
			<fd:IncludeMedia name= "/media/editorial/whats_good/whats_good_GRN_above.html"/>
			<table cellpadding="0" cellspacing="0" border="0">
			<tr valign="bottom">
				<logic:iterate id="peakProduce" collection="<%= fruCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
				<% if (idx.intValue() >= ppFru) { continue; }
					String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
					ProductModel pm = peakProduce.getProductModel();
					Comparator priceComp = new ProductModel.PriceComparator();

					ContentNodeModel prodParent = pm.getParentNode();
					List skus = pm.getSkus();

					SkuModel sku = null;
					
					String prodPrice = null;
					String prodBasePrice=null;
					boolean isDeal=false;
					int deal=0;
					String dealImage="";

					if (skus.size() == 1) {
							sku = (SkuModel)skus.get(0);  // we only need one sku
					} else {
						sku = (SkuModel) Collections.min(skus, priceComp);
					}

					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response, 
							peakProduce.getProductModel().getParentNode().getContentName(), 
							peakProduce.getProductModel(), prodNameAttribute, 
							true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				%>
					<fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
					<% 
						prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()); 


						//comment out here for DEBUG, just make everything a deal
						isDeal=productInfo.isDeal();
						//isDeal = true;

						if(isDeal) {
							prodBasePrice=JspMethods.currencyFormatter.format(productInfo.getBasePrice());
							deal=productInfo.getDealPercentage();
							dealImage=new StringBuffer("/media_stat/images/deals/brst_sm_").append(deal).append(".gif").toString();
						}  

					%>
					</fd:FDProductInfo>

					<td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
						<div style="width: 100px; height: 100px; text-align: left; position: relative;">
							<div style="padding: 0px; line-height: 0px; position: absolute; height: 0px; padding: 10px 10px 0px;">
								<a href="<%=displayObj.getItemURL()%>&trk=<%= trkCode %>"><img src="<%= displayObj.getImagePath()%>" <%=displayObj.getImageDimensions() %> alt="<%=displayObj.getAltText()%>" vspace="0" hspace="0" border="0" /></a>
							</div>
						<% if(isDeal) {	%>
							<div style="position: absolute; top: 0px; left: 0px;">
								<a id="prod_link_starimg_<%= sku %>" style="display: block;" href="<%=displayObj.getItemURL()%>&trk=<%= trkCode %>" name="prod_link_starimg_<%= sku %>"><img style="border: 0px" alt="SAVE %<%=deal%>" src="<%= dealImage %>" /></a>
							</div>
						<% } %>
						</div>
					</td>
				</logic:iterate>
				<logic:iterate id="peakProduce" collection="<%= vegCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
				<% if (idx.intValue() >= ppVeg) { continue; }
					String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
					ProductModel pm = peakProduce.getProductModel();
					Comparator priceComp = new ProductModel.PriceComparator();

					ContentNodeModel prodParent = pm.getParentNode();
					List skus = pm.getSkus();

					SkuModel sku = null;
					
					String prodPrice = null;
					String prodBasePrice=null;
					boolean isDeal=false;
					int deal=0;
					String dealImage="";

					if (skus.size() == 1) {
							sku = (SkuModel)skus.get(0);  // we only need one sku
					} else {
						sku = (SkuModel) Collections.min(skus, priceComp);
					}

					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response, 
							peakProduce.getProductModel().getParentNode().getContentName(), 
							peakProduce.getProductModel(), prodNameAttribute, 
							true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				%>
					<fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
					<% 
						prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()); 


						//comment out here for DEBUG, just make everything a deal
						isDeal=productInfo.isDeal();
						//isDeal = true;

						if(isDeal) {
							prodBasePrice=JspMethods.currencyFormatter.format(productInfo.getBasePrice());
							deal=productInfo.getDealPercentage();
							dealImage=new StringBuffer("/media_stat/images/deals/brst_sm_").append(deal).append(".gif").toString();
						}  

					%>
					</fd:FDProductInfo>

					<td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
						<div style="width: 100px; height: 100px; text-align: left; position: relative;">
							<div style="padding: 0px; line-height: 0px; position: absolute; height: 0px; padding: 10px 10px 0px;">
								<a href="<%=displayObj.getItemURL()%>&trk=<%= trkCode %>"><img src="<%= displayObj.getImagePath()%>" <%=displayObj.getImageDimensions() %> alt="<%=displayObj.getAltText()%>" vspace="0" hspace="0" border="0" /></a>
							</div>
						<% if(isDeal) {	%>
							<div style="position: absolute; top: 0px; left: 0px;">
								<a id="prod_link_starimg_<%= sku %>" style="display: block;" href="<%=displayObj.getItemURL()%>&trk=<%= trkCode %>" name="prod_link_starimg_<%= sku %>"><img style="border: 0px" alt="SAVE %<%=deal%>" src="<%= dealImage %>" /></a>
							</div>
						<% } %>
						</div>
					</td>
				</logic:iterate>
			</tr>
			<tr valign="top">
				<logic:iterate id="peakProduce" collection="<%= fruCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
				
				<% if (idx.intValue() >= ppFru) { continue; }
										
					String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);

					ProductModel pm = peakProduce.getProductModel();
					Comparator priceComp = new ProductModel.PriceComparator();

					ContentNodeModel prodParent = pm.getParentNode();
					List skus = pm.getSkus();

					SkuModel sku = null;
					
					String prodPrice = null;
					String prodBasePrice=null;
					boolean isDeal=false;
					int deal=0;
					String dealImage="";

					if (skus.size() == 1) {
							sku = (SkuModel)skus.get(0);  // we only need one sku
					} else {
						sku = (SkuModel) Collections.min(skus, priceComp);
					}
					
					
					
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(), peakProduce.getProductModel(), prodNameAttribute, true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
					String thisProdBrandLabel = pm.getPrimaryBrandName();
				%>

					<fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
							<% 
							prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()); //+"/"+ productInfo.getDisplayableDefaultPriceUnit();


							//comment out here for DEBUG, just make everything a deal
							isDeal=productInfo.isDeal();
							//isDeal = true;

							if(isDeal) {
								prodBasePrice=JspMethods.currencyFormatter.format(productInfo.getBasePrice()); //+"/"+ productInfo.getBasePriceUnit().toLowerCase();
								deal=productInfo.getDealPercentage();
							}  

							%>
					</fd:FDProductInfo>


					<td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
						

						<div class="WG_deals">
						<% if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>          
							<img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif" name="rating" width="59" height="11" border="0" vspace="3" /><br />
						<% } %>
							<a id=\"prod_link_txt_<%= sku %>" name="prod_link_txt_<%= sku %>" style="color: #360" href="<%=displayObj.getItemURL()%>&trk=<%= trkCode %>">
								<% if (thisProdBrandLabel.length()>0) { %>
									<b><%= thisProdBrandLabel %></b><br />
								<% } %>
									<%= pm.getFullName().substring(thisProdBrandLabel.length()).trim() %>
							</a>
							<div class="price <% if(isDeal) { %>dealPrice<% } %>">
								<%= displayObj.getPrice() %>
							</div>
						<% if(isDeal) {	%>
							<div class="wasPrice">
								(was <%= prodBasePrice %>)
							</div>
						<% } %>
						</div>

					</td>
				</logic:iterate>



				<logic:iterate id="peakProduce" collection="<%= vegCol %>" type="com.freshdirect.fdstore.content.SkuModel" indexId="idx">
				<% if (idx.intValue() >= ppVeg) { continue; }
										
					String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);

					ProductModel pm = peakProduce.getProductModel();
					Comparator priceComp = new ProductModel.PriceComparator();

					ContentNodeModel prodParent = pm.getParentNode();
					List skus = pm.getSkus();

					SkuModel sku = null;
					
					String prodPrice = null;
					String prodBasePrice=null;
					boolean isDeal=false;
					int deal=0;
					String dealImage="";

					if (skus.size() == 1) {
							sku = (SkuModel)skus.get(0);  // we only need one sku
					} else {
						sku = (SkuModel) Collections.min(skus, priceComp);
					}
					
					
					
					DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(), peakProduce.getProductModel(), prodNameAttribute, true);
					int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
					String thisProdBrandLabel = pm.getPrimaryBrandName();
				%>

					<fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
							<% 
							prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()); //+"/"+ productInfo.getDisplayableDefaultPriceUnit();


							//comment out here for DEBUG, just make everything a deal
							isDeal=productInfo.isDeal();
							//isDeal = true;

							if(isDeal) {
								prodBasePrice=JspMethods.currencyFormatter.format(productInfo.getBasePrice()); //+"/"+ productInfo.getBasePriceUnit().toLowerCase();
								deal=productInfo.getDealPercentage();
							}  

							%>
					</fd:FDProductInfo>


					<td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
						

						<div style="font-weight: normal; font-size: 8pt; width: 100px; text-align: center">
						<% if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>          
							<img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif" name="rating" width="59" height="11" border="0" vspace="3" /><br />
						<% } %>
							<a id=\"prod_link_txt_<%= sku %>" name="prod_link_txt_<%= sku %>" style="color: #360" href="<%=displayObj.getItemURL()%>&trk=<%= trkCode %>">
								<% if (thisProdBrandLabel.length()>0) { %>
									<b><%= thisProdBrandLabel %></b><br />
								<% } %>
									<%= pm.getFullName().substring(thisProdBrandLabel.length()).trim() %>
							</a>
							<div style="font-weight: bold; font-size: 8pt; <% if(isDeal) {	%>color: #CC0000;<% } %>">
								<%= displayObj.getPrice() %>
							</div>
						<% if(isDeal) {	%>
							<div style="font-size: 7pt; color: #888">
								(was <%= prodBasePrice %>)
							</div>
						<% } %>
						</div>

					</td>
				</logic:iterate>
			</tr>
			</table>
		<% } %>
</fd:ProduceRatingCheck>