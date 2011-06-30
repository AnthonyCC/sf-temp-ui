<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.ErpServicesProperties' %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.fdstore.pricing.ProductPricingFactory' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%!
	java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<%

FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 

ContentNodeModel currentFolder = null;
CategoryModel categoryModel=null;

if (deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNode(deptId);
} else {
	currentFolder=ContentFactory.getInstance().getContentNode(catId);
}

if (currentFolder instanceof CategoryModel) {
	categoryModel=(CategoryModel)currentFolder;
}

Collection sortedColl = null;
Collection subCatColl = null;
int cols = 0;
int maxWidth;
int newCategoryCount = 0;
boolean firstProduct = true;

String perfectEditPath = "";
String perfectTitle = "";

/* pricing fix */
List skus = new ArrayList();
List prices = new ArrayList();
List salesUnitNames = new ArrayList();
int idx =-1;
List sortedList_forPricing = new ArrayList();

if(categoryModel!=null){
	Html perfectDesc=categoryModel.getEditorial();
	
	if(perfectDesc!=null){
		perfectEditPath=perfectDesc.getPath();
	}

	perfectTitle=categoryModel.getEditorialTitle();
}

// setup for succpage redirect ....
request.setAttribute("successPage","/wine_cart_confirm.jsp?catId="+request.getParameter("catId"));

if (request.getRequestURI().toLowerCase().indexOf("department.jsp")!=-1) {
	maxWidth = 550;
} else {
	maxWidth = 425;
}
%>
<%
	/* Alcohol Restriction info
	 *	since we're not using the TxProductControlTag here, we need to add this manually
	 */
%>
<script type="text/javascript">
	if (!window.alcohol) { window.alcohol = {}; }
	addAlcoholHelpers();
	<%
		/* set if user has already agreed to alcohol disclaimer
		 *	check new session value here to avoid template differences
		 */
		FDSessionUser alcUser = (FDSessionUser)session.getAttribute(SessionName.USER);
	%>
	hasAgreedToAlcoholDisclaimer = <%=alcUser.isHealthWarningAcknowledged()%>;
</script>
<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= "/wine_cart_confirm.jsp?catId="+request.getParameter("catId") %>'>
	<div class="left" style="padding-top:8px; padding-bottom:5px; border-bottom: solid 5px #8C1920; margin-bottom:15px; width:<%=maxWidth%>px;">
		<span class="title18" style="padding-bottom:5px;"><%=perfectTitle%></span><br />
		<fd:IncludeMedia name="<%=perfectEditPath%>" />
	</div>
	<%
		//*** if we got this far..then we need to remove the sucess page attribute from the request.
		request.removeAttribute("successPage");

		subCatColl = (Collection) request.getAttribute("itemGrabberResult");
		if (subCatColl==null) subCatColl = new ArrayList();

		List subCategoryList = new ArrayList();
		int catIndex=0;

		/* start category loop */
		for (Iterator CatIter = subCatColl.iterator(); CatIter.hasNext() ;) {

			catIndex++;

			Object catItem = CatIter.next(); 
			
			if (catItem instanceof CategoryModel) {
				CategoryModel currentCat=(CategoryModel)catItem ;
				subCategoryList.add(currentCat);
				sortedColl = currentCat.getProducts();
			%>
			
			<script language="Javascript">

				function chgQty<%=catIndex%>(idx,qtyFldName,delta,min,max) {
					var qty = parseFloat(document.wine_perfect_form_<%=catIndex%>[qtyFldName].value);

					if (isNaN(qty)) qty=0;
					if (qty<1) qty=0;

					qty = qty + delta;

					if (qty < min  && qty!=0) return;
					if (qty > max) return;

					if (qty<=0) {
						qty=0;
						document.wine_perfect_form_<%=catIndex%>[qtyFldName].value='';
					} else {
						document.wine_perfect_form_<%=catIndex%>[qtyFldName].value = qty;
					}
					
					//var pricingObj = eval ("pricing"+idx);
					//pricingObj.setQuantity(qty);
				}

				function updatePriceField() {
					//document.wine_detail_form.total.value = pricing.getPrice();
				}
			</script>

			<form name="wine_perfect_form_<%=catIndex%>" id="wine_perfect_form_<%=catIndex%>" method="POST">


			<%
				//**************************************************
				//***   the Transactional Grouped Items          ***
				//**************************************************

				int itemTotal = sortedColl.size();

				String itemNameFont = null;
				Image itemImage;
				String currentFolderPKId = currentFolder.getContentKey().getId();
				String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
				CategoryModel cat = null;

				//prod specs
				boolean prodUnavailable = true;
				SkuModel defaultSku = null;

				//convert collection to list
				boolean oneNotAvailable = false;
				int prodsAvailable = 0;
				List sortedList = new ArrayList();

				for(Iterator collIter = sortedColl.iterator(); collIter.hasNext();) {
					Object currItem = collIter.next();
					
					if (currItem instanceof ProductModel) {
						if (((ProductModel)currItem).isUnavailable()) {
							oneNotAvailable = true;
							continue;
							/*	dont add unavailable items to the list, which makes the unavailable logic below useless
							 *	but I suspect that creative will want to display unavailable items...hope I'm wrong. (RG)
							 */
						}
						
						prodsAvailable++;
						sortedList.add(ProductPricingFactory.getInstance().getPricingAdapter(((ProductModel)currItem) ,user.getPricingContext()));
						sortedList_forPricing.add(ProductPricingFactory.getInstance().getPricingAdapter(((ProductModel)currItem) ,user.getPricingContext()));
					}
				}

				int itemsToDisplay = sortedList.size();
				String ediDescPath="";
				Html htmlDesc=currentCat.getEditorial();
				
				if (htmlDesc!=null) {
					ediDescPath=htmlDesc.getPath();
				}

				String catDetailImagePath="";
				int catDetailWidth=120;
				int catDetailHeight=150;
				Image catImage=currentCat.getCategoryDetailImage();

				if (catImage!=null) {
					catDetailImagePath = catImage.getPath();
					catDetailWidth = catImage.getWidth();
					catDetailHeight = catImage.getHeight();
				}

				if (prodsAvailable > 0 && !oneNotAvailable) {
				%>
					<table width="<%=maxWidth%>" cellspacing="0" cellpadding="0" border="0">
						<% if (!firstProduct) { %>
							<tr><td colspan="2" style="border-top:solid 1px #CCCCCC;"><img src="/media_stat/images/layout/clear.gif" width="1" height="15" alt="" /></td></tr>
						<% } %>
						<tr valign="top">
							<td width="95%" style="padding-right:8px;">
								<div class="center_prod_name">
									<%-- a href="/category.jsp?catId=< %=currentCat.getContentKey().getId()% >&trk=cPage" --%><%=currentCat.getFullName()%><%--/a--%>
								</div>
								<div class="padt6">
									<fd:IncludeMedia name="<%=ediDescPath%>" />
								</div>
								<input type="hidden" name="enableWineSubmit_<%=catIndex%>" value="false">
							</td>  
							<td width="5%">
								<img src="<%=catDetailImagePath%>" width="<%=""+catDetailWidth%>" height="<%=""+catDetailHeight%>" alt="<%=currentCat.getFullName()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" /></td>
						</tr>
						<%
							firstProduct = false;
							// there are errors..Display them
							Collection myErrs=((ActionResult)result).getErrors();

							if (myErrs.size()>0) {
								for (Iterator errItr = myErrs.iterator();errItr.hasNext(); ) {
									String errDesc = ((ActionError)errItr.next()).getDescription();
									%>
									<tr>
										<td colspan="2">
											<font class="text12bold" color="#cc3300"><%=errDesc%></font>
										</td>
									</tr>
									<%
								}
								request.setAttribute("doRedirect","false");
							} else {
								if (request.getMethod().equalsIgnoreCase("post")) {
									request.setAttribute("doRedirect","true");
								}
							}
							%>
							<tr>
								<td colspan="2">
									<table cellpadding="0" cellspacing="0" border="0">    
									<%
										int itemShownIndex=-1;
										idx = 0;

										for (int itemIndex=0; itemIndex < sortedList.size(); itemIndex++) {
											ContentNodeModel contentNode = (ContentNodeModel)sortedList.get(itemIndex);
											itemNameFont = "text11";
											ProductModel displayProduct = (ProductModel)contentNode;

											List prodSkus = displayProduct.getSkus();

											if (prodSkus.size()==0) continue;

											SkuModel sku = (SkuModel) (prodSkus.size()==1 ? prodSkus.get(0) : Collections.min(prodSkus, ProductModel.PRICE_COMPARATOR));

											//******************************  Start  Product Line Display  ******************
											%>
											<tr valign="middle">
												<fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
													<%
														FDProduct product = null;
														String salesUnitDescription = "NA";
														String salesUnitName = "NA";
														boolean prodUnAvailable = displayProduct.isUnavailable();

														try {
															product = FDCachedFactory.getProduct(productInfo);
															FDSalesUnit[] salesUnits = product.getSalesUnits();

															if (salesUnits.length>0) {
																salesUnitDescription = salesUnits[0].getDescription();
																salesUnitName = salesUnits[0].getName();
																salesUnitNames.add(salesUnitName);
															}
														} catch (FDSkuNotFoundException fdsnf) {
															JspLogger.PRODUCT.warn("Grocery Page: catching FDSkuNotFoundException and Continuing:\n FDProductInfo:="+productInfo+"\nException message:= "+fdsnf.getMessage());
														}

														String qtyFldName = "quantity_"+itemShownIndex*catIndex;
														
														SkuModel dfltSku = displayProduct.getDefaultSku();
														String priceStr = "";

														try {
															if (dfltSku !=null) {
																FDProductInfo pi = FDCachedFactory.getProductInfo( dfltSku.getSkuCode());
																priceStr = JspMethods.formatPrice(pi, user.getPricingContext());
															}
														} catch (FDResourceException fde) {
															throw new JspException(fde);
														} catch (FDSkuNotFoundException sknf) {
															throw new JspException(sknf);
														}
													%>
													<td style="padding-right:2px;">
														<%
															String displayQuantity="";
															if (prodUnAvailable) {
																qtyFldName = "hidnqty_"+itemIndex;
																displayQuantity="";
																%>
																<font color="#999999">NA</font><input type="hidden"
															<% } else {
																itemShownIndex++;
																skus.add( sku );
																qtyFldName = "quantity_"+itemShownIndex;
																displayQuantity=request.getParameter(qtyFldName);

																if (displayQuantity==null) {
																	//displayQuantity = quantityFormatter.format(displayProduct.getQuantityMinimum());
																	displayQuantity="";
																}
																prices.add(new Double(displayProduct.getQuantityMinimum()));
															%>
																<input type="text"<%
															}

															%> name="<%= qtyFldName %>" size="2" maxlength="2"  class="text11" value="<%= displayQuantity %>" onChange="javascript:chgQty<%=catIndex%>(<%=itemShownIndex%>,'<%= qtyFldName %>', 0, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);" />
													</td>
													<td width="10">
													<%
														if (prodUnAvailable) {
															%>&nbsp;<%
														} else {
															%>
															<a href="javascript:chgQty<%=catIndex%>(<%=itemShownIndex%>,'<%= qtyFldName %>', <%= displayProduct.getQuantityIncrement() %>, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);"><img src="/media_stat/images/layout/grn_arrow_up.gif" width="10" height="9" border="0" vspace="1" alt="Increase quantity" /></a><br />
															<a href="javascript:chgQty<%=catIndex%>(<%=itemShownIndex%>,'<%= qtyFldName %>', -<%= displayProduct.getQuantityIncrement() %>, <%= displayProduct.getQuantityMinimum() %>, <%= user.getQuantityMaximum(displayProduct) %>);"><img src="/media_stat/images/layout/grn_arrow_down.gif" width="10" height="9" border="0" vspace="1" alt="Decrease quantity" /></a>
															<%
														}
													%>
													</td>
													<td style="padding-left:8px;">
														<%
															String unAvailableFontStart = "";
															String unAvailableFontEnd = "";

															if (prodUnAvailable) {
																unAvailableFontStart = "<font color=\"#999999\">";
																unAvailableFontEnd = "</font>";
															}

															String thisProdBrandLabel = displayProduct.getPrimaryBrandName();
															String productURL = "javascript:popup('prod_desc_popup.jsp?catId="+(displayProduct.getParentNode()).getContentName()+"&prodId=" + displayProduct + "','small')";
														%>
															<%= unAvailableFontStart %><a href="<%= productURL %>"><font class="text10bold"><%= unAvailableFontStart %><%= thisProdBrandLabel %><%= unAvailableFontEnd %></font><font class="text10"><%= unAvailableFontStart %><%= displayProduct.getFullName().substring(thisProdBrandLabel.length()).trim() %><%= unAvailableFontEnd %></font></a>
														<%
															if (prodUnAvailable) { %>
																<font class="text10">Not&nbsp;Available</font>
															<% } else { %>
																<font class="text10"><%=" "+salesUnitDescription+" - <b>"+priceStr+"</b>"%>&nbsp;</font>
															<% } %>
															<%= unAvailableFontStart %><nobr></nobr><br />
															<%= unAvailableFontEnd %>
														<%
															Date earliestDate = displayProduct.getSku(0).getEarliestAvailability();
															Calendar testDate = new GregorianCalendar();
															testDate.add(Calendar.DATE, 1);

														// cheat: if no availability indication, show the horizon as the earliest availability
														if (earliestDate == null) {
															earliestDate = DateUtil.addDays(DateUtil.truncate(new Date()), ErpServicesProperties.getHorizonDays());
														}
													
														if (QuickDateFormat.SHORT_DATE_FORMATTER.format(testDate.getTime()).compareTo(QuickDateFormat.SHORT_DATE_FORMATTER.format(earliestDate)) < 0) {
															SimpleDateFormat sf = new SimpleDateFormat("MM/dd");
															%><b><font color="#999999">Earliest Delivery - <%= sf.format(earliestDate) %></font></b><%
														}

														if (!prodUnAvailable) { %><%@include file="/includes/product/i_scaled_prices_nobr.jspf"%><% } %>
														
														<%
															/* Alcohol Restriction info
															 *	since we're not using the TxProductControlTag here, we need to add this manually
															 */
														%>
														<script type="text/javascript">
														<%
															FDProduct fdProd = null;
															SkuModel alcSku = sku;

															try {
																fdProd = alcSku.getProduct();
															} catch (FDResourceException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															} catch (FDSkuNotFoundException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
															boolean isAlc = false;
															String alcRest = "";
															if (fdProd != null) {
																isAlc = fdProd.isAlcohol();
																alcRest = fdProd.getMaterial().getAlcoholicContent().getCode();
															}
														%>
															window.alcohol['<%=alcSku.getSkuCode()%>'] = { isAlc: <%=isAlc%>, alcRest: '<%=alcRest%>' }
														</script>
														<input type="hidden" name="isAlc_<%= itemShownIndex %>" value="<%= isAlc %>" />

														<input type="hidden" name="salesUnit_<%= itemShownIndex %>" value="<%= salesUnitName %>">
														<input type="hidden" name="skuCode_<%= itemShownIndex %>" value="<%= displayProduct.getSku(0).getSkuCode() %>">
														<input type="hidden" name="catId_<%= itemShownIndex %>" value="<%= displayProduct.getParentNode() %>">
														<input type="hidden" name="productId_<%= itemShownIndex %>" value="<%= displayProduct %>">
													</td>
												</fd:FDProductInfo>
											</tr>
											<%
												idx++;
											//*****************  END Product Line *********************************
										}
									%>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding-bottom:10px;">
									<input type="hidden" name="itemCount" value="<%= Math.min(itemsToDisplay, idx) %>" /><br />
									<table cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td style="padding-bottom:10px;"><input type="image" name="addMultipleToCart<%=catIndex%>" src="media_stat/images/buttons/add_to_cart_small.gif"width="76" height="17" border="0" alt="ADD SELECTED ITEMS TO CART"></td>
											<fd:CCLCheck>
												<td style="padding-bottom:10px;">
													<a href="/unsupported.jsp" onclick="return CCL.save_items('wine_perfect_form_<%=catIndex%>',this,'action=CCL:AddMultipleToList&source=ccl_actual_selection','source=ccl_actual_selection')"><img src="/media_stat/ccl/lists_save_icon_lg.gif" width="12" height="14" border="0" hspace="5"/></a>
												</td>
											</fd:CCLCheck> 
										</tr>
									</table>
								</td>
							</tr>
						</table>

					<%
				}
				%>
				<%-- else { // end of If !oneNotAvailable
				% >
					<table align="center" width="<%=maxWidth%>" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td>
								<br />
								<font class="text12" color="#999999"><b>We're sorry! This item is temporarily unavailable.</b><br />
									<br />
									We're proud to offer New York's widest selections of fresh foods. Unfortunately, this product is temporarily unavailable. Please check back on your next visit.
								</font>
							</td>
						</tr>
					</table>
				< %

					}
				--%>
				
			</form>
			<%
			}
		}
		/* end of category loop */
	%>
	<%@ include file="/shared/includes/product/i_pricing_script.jspf" %>
	<script>
		function tmc_updateTotal() {
			var total = 0;
			var p;

			if (!window['pricings']) {
				window['pricings'] = [];
			}
			/* convert single refs into an array if needed */
			if (window['pricing0'] && window['pricing0'] !== null) {

				for (var i = 0; i < <%=skus.size()%>; i++ ) {
					if (window.pricings[i]) {
						continue;
					}

					if (window['pricing'+i]) {
						window.pricings[i] = window['pricing'+i];

						//and relink setQuanitity (used by onBlur)
						window['pricing'+i].setQuantity = window.pricings[i].setQuantity;
					}
				}
			}

			if (window.pricings) {
				for (var i = 0; i < <%=skus.size()%>; i++ ) {
					if (window.pricings[i]) {
						p = window.pricings[i].getPrice();
						if (p !== '') {
							total+=new Number(p.substring(1));
						}
					}else{
						//not loaded all the way, set to call again on window load instead
						Event.observe(window, 'load', function() {
							tmc_updateTotal();
						});
						return;
					}
				}
				//make sure helpers load in
				addAlcoholHelpers();
			}
		}
		<%
			// build the pricing object for each of the products
			idx = 0;
			for (Iterator itrItms = sortedList_forPricing.iterator(); itrItms.hasNext();) {
				ContentNodeModel cn = (ContentNodeModel) itrItms.next();
				
				if (!(cn instanceof ProductModel)  || ((ProductModel)cn).isUnavailable()) continue;
				if (skus.size() <= idx) continue; //wouldn't this mean -1?
				%>
				var pricing<%=idx%> = new Pricing();

				pricing<%=idx%>.setSKU("<%=((SkuModel)skus.get(idx)).getSkuCode() %>");
				pricing<%=idx%>.setSalesUnit("<%=salesUnitNames.get(idx) %>");
				pricing<%=idx%>.setCallbackFunction(tmc_updateTotal);
				pricing<%=idx%>.setQuantity(<%=((Double)prices.get(idx)).doubleValue()%>);

				<%
				idx++;
			}
		%>
	</script>
</fd:FDShoppingCart>
