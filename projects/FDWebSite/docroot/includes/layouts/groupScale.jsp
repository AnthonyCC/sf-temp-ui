<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='com.freshdirect.ErpServicesProperties' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.content.nutrition.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.framework.util.log.LoggerFactory' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods'%>
<%@ page import='com.freshdirect.webapp.util.ProductImpression' %>
<%@ page import='com.freshdirect.webapp.util.prodconf.SmartStoreConfigurationStrategy' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.util.*' %>
<%@ page import='org.apache.log4j.Category' %>
<%@ page import='org.apache.commons.lang.StringUtils' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%!
	java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
	NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
	/**
	 * Remove unavailable SKUs from list
	 * @return the same list, for convenience
	 */
	public List cleanSkus(List prodSkus) {
		for (ListIterator skuItr = prodSkus.listIterator(); skuItr.hasNext(); ) {
			SkuModel s = (SkuModel)skuItr.next();
			if ( s.isUnavailable() ) {
					skuItr.remove();
			}
		}
		return prodSkus;
	}

	public String buildOtherParams(boolean showThumbnails, int displayPerPageSetting, int pageNumberValue, String brandValue, String _sortBy, String _nutriName, HttpServletRequest _req, String virtualValue) {
		StringBuffer buf = new StringBuffer();
		buf.append( "&sortBy=" ).append( _sortBy==null ? "name" : _sortBy );
		if (_nutriName!=null) buf.append("&nutritionName=").append(_nutriName);
		String groceryVirtual = null;
		if (virtualValue==null || virtualValue.length() < 1) {
			groceryVirtual = _req.getParameter("groceryVirtual");
		} else {
			groceryVirtual = virtualValue;
		}
		if (groceryVirtual!=null) {
			buf.append("&groceryVirtual=").append(groceryVirtual);
		}

		buf.append("&showThumbnails=").append(showThumbnails);

		if (displayPerPageSetting  > 0 ) {
			buf.append("&DisplayPerPage=").append(displayPerPageSetting);
		}

		if (pageNumberValue > 0) {
			buf.append("&pageNumber=").append(pageNumberValue);
		}
		if (brandValue!=null  && brandValue.length()>0) {
			buf.append("&brandValue=").append(URLEncoder.encode(brandValue));
		}

		return buf.toString();
	}

	public String getPageNumbers(HttpServletRequest requestObj, HttpServletResponse responseObj,int pageNumber, int itemsPerPage, CategoryModel currFolder, int itemsCount,boolean showThumbnails,String brand_Name,String _sortBy,String _nutriName, boolean descending, String display) {
		StringBuffer buf = new StringBuffer();
		String urlParams = buildOtherParams(showThumbnails, itemsPerPage, -1, brand_Name, _sortBy, _nutriName,requestObj, null);
		String urlStart = "/category.jsp?catId=" + currFolder +"&disp=" + display + "&sortDescending=" + descending + "&trk=trans";
		String fullURL = null;
		int startFrom = 1;
		if(pageNumber > 1)   {
			if (pageNumber/10 > 1) {
				startFrom = ((pageNumber/10) * 10) + 1;
				fullURL= responseObj.encodeURL(urlStart + urlParams + "&pageNumber=" + startFrom);

				buf.append("<A HREF=\"");
				buf.append(urlStart).append(urlParams);
				buf.append("\">previous</A> . ");
			}

			for (int i=startFrom; i<pageNumber; i++) {
				fullURL= responseObj.encodeURL(urlStart+urlParams+"&pageNumber="+i);
				buf.append("<A HREF=\"").append(fullURL).append("\">");
				buf.append(i).append("</A> . ");
			}
		}

		buf.append("<B>").append(pageNumber).append("</B>");

		if ( itemsCount >= (pageNumber * itemsPerPage) ) {
			// figure out how many additional pages to display
			int addToLoop = 0;
			if(itemsCount % itemsPerPage > 0) {
				addToLoop = 1;
			}
			for (int i=(pageNumber + 1); (i <= (itemsCount/itemsPerPage + addToLoop)); i++) {
				fullURL= responseObj.encodeURL(urlStart + urlParams + "&pageNumber=" + i);
				buf.append(" . <A HREF=\"");
				buf.append(fullURL);
				buf.append("\">");
				if (i%10 == 1) {
					buf.append("more");
					break;
				} else {
					buf.append(i);
				}
				buf.append("</A>");
			}
		}
		return buf.toString();
	}

	//check for param string, then attribute, return null if final result is and empty string
	public String checkParam(HttpServletRequest requestObj, String paramString) {
		String temp = requestObj.getParameter(paramString);
		if (temp == null || "".equals(temp)) {
			temp = (requestObj.getAttribute(paramString)!=null)?requestObj.getAttribute(paramString).toString():"";
		}
		return ("".equals(temp))?null:temp;

	}

	static Category LOGGER = LoggerFactory.getInstance("Group Pricing");
%>
<%

	//********** Start of Stuff to let JSPF's become JSP's **************
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

	//using getAttribute across the board here, set the values in parent - group.jsp so we can remove them from the URL
	String catId = request.getAttribute("catId").toString();
		if ("".equals(catId)) { catId = null; }
	String deptId = request.getAttribute("deptId").toString();
		if ("".equals(deptId)) { deptId = null; }
	boolean isDepartment = false;
	
	String grpId=NVL.apply(request.getParameter("grpId"), "");
	String version=NVL.apply(request.getParameter("version"), "");
	String trkCode= NVL.apply(request.getParameter("trk"), "trkCode");


	ContentNodeModel currentFolder = null;
	CategoryModel currentCategory = null;

	if (deptId != null) {
		currentFolder=ContentFactory.getInstance().getContentNode(deptId);
		isDepartment = true;
	} else if (catId != null) {
		currentFolder=ContentFactory.getInstance().getContentNode(catId);
		if (currentFolder != null)
			currentCategory = (CategoryModel) currentFolder;
	}

	//DO render Editorial (if exists)
	//[APPREQ-92] skip Editorial on Brand and on Virtual All pages
	boolean doRenderEditorialPartial = (request.getParameter("brandValue") == null && !"All".equals(request.getParameter("groceryVirtual")));

	boolean onlyOneProduct = false;
	ProductModel theOnlyProduct = null;

	String sortBy = request.getParameter("sortBy");
	String nutriName = request.getParameter("nutritionName");
	String display = request.getParameter("disp");
	boolean descending = "true".equals(request.getParameter("sortDescending"))?true:false;
	if (sortBy==null) sortBy = "name";

	//List sortedColl = (List) request.getAttribute("itemGrabberResult");
	List<SkuModel> sortedColl = (List) request.getAttribute("smList");

		if (sortedColl==null) {
			LOGGER.debug("Error, sortedColl is empty? currentFolder:"+currentFolder);
			return;
		}

	boolean showThumbnails;
	{
		String tmp = request.getParameter("showThumbnails");
		Boolean thumbParam = tmp==null ? null : Boolean.valueOf( tmp );
		Boolean thumbSession = (Boolean)session.getAttribute("showThumbnails");
		if (thumbSession==null) {
			thumbSession = Boolean.TRUE;
		}

		if (thumbParam!=null && !thumbParam.equals(thumbSession) && "true         ".equalsIgnoreCase(request.getParameter("set"))) {
			// toggle was altered, store in session
			session.setAttribute("showThumbnails", thumbParam);
		}
		showThumbnails = thumbParam==null ? thumbSession.booleanValue() : thumbParam.booleanValue();
	}

	Image brandLogo = null;
	String brandPopupLink = null;
	String brandName = "";
	String brandValue =NVL.apply(request.getParameter("brandValue"), "");

	if ("".equals(brandValue)) {
		//got the brand value..need to get the name.
		BrandModel bm = (BrandModel) ContentFactory.getInstance().getContentNode(brandValue);
		if (bm!=null){
			brandName= bm.getFullName();
			brandLogo = bm.getLogoSmall();
			Html popupContent = bm.getPopupContent();

			if (popupContent!=null) {
				TitledMedia tm = (TitledMedia)popupContent;
				EnumPopupType popupType = EnumPopupType.getPopupType(tm.getPopupSize());
				brandPopupLink = "javascript:pop('" +response.encodeURL( "/brandpop.jsp?brandId="+bm ) + "'," +popupType.getHeight() + "," + popupType.getWidth() + ")";
			}
		}
	}

	int totalItems = 0;

	for (Iterator itr=sortedColl.iterator(); itr.hasNext();) {
		ContentNodeModel cn = (ContentNodeModel)itr.next();
		if (cn instanceof SkuModel) {
			totalItems++;
		}
	}
	LOGGER.debug("totalItems "+totalItems);

	int itemsToDisplay = 99999;
	

	int pageNumber = 1;
	try {
		pageNumber = Integer.valueOf(request.getParameter("pageNumber")).intValue();
	} catch (NumberFormatException nfe) {}

	// setup for succpage redirect ....
	String successPage = FDURLUtil.getCartConfirmPageURI(request);
	request.setAttribute("successPage", successPage);
%>
	<%
	//*** if we got this far..then we need to remove the sucess page attribute from the request.
	request.removeAttribute("successPage");

	

	int syncProdIdx = -1;
	double syncProdQty=0.0;
	String syncProdSkuCode = null;
	String syncProdSalesUnit = null;
	boolean bigProdShown = false;
	int bigProdIndex = -1; //index in impressions where big prod is
	boolean hasNutrition = false;
	boolean hasIngredients = false;
	int itemShownIndex = 0;
	int imgShownIndex = 0;
	String qtyFldName = "quantity_"+itemShownIndex;
	String imgLinkUrl = null;
	String TX_FORM_NAME = (request.getAttribute("TX_FORM_NAME")!=null)?request.getAttribute("TX_FORM_NAME").toString():"";
	String TX_JS_NAMESPACE = (request.getAttribute("TX_JS_NAMESPACE")!=null)?request.getAttribute("TX_JS_NAMESPACE").toString():"";

	String prodCatId = checkParam(request, "prodCatId");
	String productCode = checkParam(request, "productId");
	String reqSkuCode = checkParam(request, "skuCode");
	//if any of these are null, set from the first product
	if (prodCatId == null || productCode == null || reqSkuCode == null) {
		ProductModel pmTemp = (ProductModel)request.getAttribute("defaultPM");
		if (prodCatId == null) {
			if (pmTemp != null && pmTemp.getParentNode() instanceof CategoryModel) {
				prodCatId = pmTemp.getParentNode().toString();
				LOGGER.debug("prodCatId "+catId);
			}
		}
		if (productCode == null) {
			productCode = pmTemp.toString();
		}
		if (reqSkuCode == null) {
			reqSkuCode = sortedColl.get(0).toString();
		}
	}
	boolean showCancellationNote = false; //show cancellation note at the end of the page
	

	/** List of all SKUs in the page, for the pricing structures */
	List skus = new ArrayList( itemsToDisplay );

	// iterate throught the list of items in the sorted set and remove all folders.  grab the brands in the process

	for (Iterator itr = sortedColl.iterator(); itr.hasNext(); ){
		ContentNodeModel item = (ContentNodeModel)itr.next();
		boolean matchingBrand = false;
		if (!(item instanceof SkuModel)) {
			itr.remove();
		} else {
			List prodBrands = ((ProductModel)((SkuModel)item).getParentNode()).getBrands();

			if (prodBrands!=null && prodBrands.size() > 0 && brandValue!=null && brandValue.length()>0) {
				for (Iterator brandItr = prodBrands.iterator();brandItr.hasNext();) {
					if (brandValue.equals(((BrandModel)brandItr.next()).getContentName())){
						matchingBrand = true;
						break;
					}
				}
			}
			if (brandValue!=null && brandValue.length()>0 && !matchingBrand){
				//remove this product, cause we're only displaying certain brands
				itr.remove();
			}
		}
	}

	ProductModel[] productArray = null; //(ProductModel[])sortedColl.toArray( new ProductModel[0] );
	int skuCount =0;
	List allSkuModels = new ArrayList();
	for (Iterator skuItr=sortedColl.iterator(); skuItr.hasNext();) {
		ContentNodeModel cn = (ContentNodeModel)skuItr.next();
		if (cn instanceof SkuModel)  {
			allSkuModels.add((SkuModel) cn);
		}
	}

	skuCount=allSkuModels.size();

	// get all the brands that in the category that we are in
	CategoryModel groceryCategory;
	{
			if (currentFolder instanceof DepartmentModel) {
				if (catId != null) {
					currentFolder=ContentFactory.getInstance().getContentNode(catId);
					if (currentFolder != null)
						currentCategory = (CategoryModel) currentFolder;
				}
			}
			ContentNodeModel node = currentFolder;
			// find the topmost Category
			while (!(node.getParentNode() instanceof DepartmentModel)) {
				node = node.getParentNode();
			}
			groceryCategory=(CategoryModel)node;
	}

	//if we change the display of items per page to a higher number on a page other than one, we could stay on that page with no products shown on it
	if(((pageNumber -1) * itemsToDisplay) > skuCount) {
			//then set the page number back to one
			pageNumber = 1;
	}
	%>
	
<%@page import="com.freshdirect.fdstore.pricing.ProductPricingFactory"%>
<fd:ProductGroup id='productNode' categoryId='<%= prodCatId %>' productId='<%= productCode %>'>
		<%
			boolean qualifies = productNode.isQualifiedForPromotions() && user.getMaxSignupPromotion()>0;
			double promotionValue = 0.0;
			if (qualifies) {
				promotionValue = user.getMaxSignupPromotion();
			}
			String prefix = String.valueOf(promotionValue);
			prefix = prefix.substring(0, prefix.indexOf('.'));

			List prodSkus = productNode.getSkus();
			bigProdShown = true;
			SkuModel minSku = null;
			Image bigProductImage = productNode.getDetailImage();
			String thisProdBrandLabel = "";
			String thisProdBrand = "";
			String prodNameLower= productNode.getFullName().toLowerCase();
			BrandModel thisBrandModel = null;
			String typedQuantity = NVL.apply(request.getParameter("typedQuantity"), "");

			// get the first brand name, if any.
			Image titleBrandLogo = null;
			List brandsForProd = productNode.getBrands();
			if (brandsForProd!=null && brandsForProd.size()>0) {
				thisBrandModel = (BrandModel)brandsForProd.get(0);
				thisProdBrand = thisBrandModel.getContentName();
				titleBrandLogo = thisBrandModel.getLogoSmall();
				Html popupContent = thisBrandModel.getPopupContent();
				if (popupContent!=null) {
					TitledMedia tm = (TitledMedia)popupContent;
					EnumPopupType popupType=EnumPopupType.getPopupType(tm.getPopupSize());
					brandPopupLink = "javascript:pop('"+response.encodeURL("/brandpop.jsp?brandId="+thisBrandModel)+"',"+popupType.getHeight()+","+popupType.getWidth()+")";
				}


				for (int bx = 0;bx<brandsForProd.size();bx++){
					if (prodNameLower.startsWith(((BrandModel)brandsForProd.get(bx)).getFullName().toLowerCase())) {
						thisProdBrandLabel = ((BrandModel)brandsForProd.get(bx)).getFullName();
						break;
					}
				}
			}

			String prodPrice = "";
			String prodBasePrice="";
			int deal=0;
			boolean hasWas=false;
			String dealsImage="";
			String priceUnit = "";
			String firstSalesUnit = null;
			//get the first sku..in the event that this product is unavailabe. Ideally we should only be in this blokc
			// if the product is available
			minSku = (SkuModel)prodSkus.get(0);  // we only need one sku

			for (ListIterator li=prodSkus.listIterator(); li.hasNext(); ) {
				SkuModel sku = (SkuModel)li.next();
				if ( sku.isUnavailable() ) {
					li.remove();
				}
			}
			if (prodSkus.size() > 0 && reqSkuCode==null) {
				minSku = (SkuModel) ( prodSkus.size()==1 ? prodSkus.get(0)  : Collections.min(prodSkus, ProductModel.PRICE_COMPARATOR) );
			} else if (reqSkuCode!=null){
				minSku = productNode.getSku(reqSkuCode);
			}
			FDProduct product = null;
			boolean skuAvailable=false; 
		%>
		<fd:FDProductInfo id="productInfo" skuCode="<%= minSku.getSkuCode() %>">
			<%
				/*
				 *	In preview mode..prods may not have an underlying FDProduct..so if the productInfo says its
				 *	discontinued or tempUnavailable then skip the product
				 */

					skuAvailable = !minSku.isUnavailable();
					try{
						product = FDCachedFactory.getProduct(productInfo);
					} catch (FDSkuNotFoundException fdsnf){
						JspLogger.PRODUCT.warn("Grocery Page: catching FDSkuNotFoundException and Continuing:\n FDProductInfo:="+productInfo+"\nException message:= "+fdsnf.getMessage());
					}

					prodPrice = currencyFormatter.format(productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getDefaultPrice());
					hasWas=productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).isItemOnSale();
					if(hasWas) {
						prodBasePrice=currencyFormatter.format(productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getSellingPrice());
					}
					deal=productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).getHighestDealPercentage();
					if (deal > 0) {
						dealsImage=new StringBuffer("/media_stat/images/deals/brst_lg_").append(deal).append(".gif").toString();
					}
					
					priceUnit = productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
					String salesUnitDesc = "N/A";
					firstSalesUnit = "N/A";
					if (product!=null) {
						FDSalesUnit[] salesUnits = product.getSalesUnits();
						if (salesUnits.length > 0 ) {
							salesUnitDesc = " - "+salesUnits[0].getDescription();
							firstSalesUnit = salesUnits[0].getName();
						}
						hasNutrition = product.hasNutritionFacts();
						hasIngredients = product.hasIngredients();
					}
			%>
			<form name="<%= TX_FORM_NAME %>" id='groupScale_form' method="POST">
			<input type="hidden" name="skuCode_big" value="<%=minSku.getSkuCode()%>" />
			<input type="hidden" name="salesUnit_big" value="<%=firstSalesUnit%>" />
			<input type="hidden" name="catId_big" value="<%=prodCatId%>" />
			<input type="hidden" name="productId_big" value="<%=productCode%>">
			<% if (request.getParameter("fdsc.source") != null) { %>
				<input type="hidden" name="fdsc.source" value="<%=request.getParameter("fdsc.source")%>" />
			<% } %>
			<table border="0" cellspacing="0" cellpadding="0" width="425"><%-- entire product table --%>
				<%
				//If there is a specific product selected then show it above the listings here
				//lets get the product with the product cod in the section, display this product, then the rest of the products

			if(productCode!=null && prodCatId !=null ) {
				bigProductImage = null;

				%>
				<% if (FDStoreProperties.isAdServerEnabled()) { %>
					<tr>
						<td colspan="5">
							<SCRIPT LANGUAGE=JavaScript>
							<!--
							OAS_AD('ProductNote');
							//-->
							</SCRIPT>
						</td>
					</tr>
				<% }  
				if (skuAvailable) { %>
				
				<tr>
					<td colspan="5">
						<div style="line-height: 16px; font-size: 13px; font-weight: bold; color: #C94747; font-family: Verdana,Arial,sans-serif;">Buy More &amp; Save!</div><div class="title14">Any Combination of <%=request.getAttribute("grpShortDesc")%> Listed Below</div><div style="line-height: 16px; font-size: 13px; font-weight: bold; color: #C94747; font-family: Verdana,Arial,sans-serif;"><%=request.getAttribute("grpQty")%> for <%=request.getAttribute("grpTotalPrice")%></div>
						<table cellspacing="0" cellpadding="0" border="0" align="left"> 
						<tr>
							<td colspan="3"><img width="1" height="4" src="/media_stat/images/layout/clear.gif" alt="" /></td>
						</tr>
						<tr>
							<td align="right" style="padding-top: 5px;" class="text11">
								<input width="93" type="image" height="20" border="0" style="padding-top: 5px; padding-bottom: 3px; display: block;" src="/media_stat/images/buttons/add_to_cart.gif" name="add_to_cart">
							</td>
							<td width="10">&nbsp;</td><%-- buffer cell --%>
							<td align="right" style="padding-top: 8px; 5px;" class="text11bold">Price&nbsp;<input type="text" value="" onfocus="blur()" onchange="" size="6" name="total" id="total_top" class="text11bold">
							</td>
						</tr>
						</table>
					</td>
				</tr>				
				<tr>
					<td colspan="5"><img width="1" height="4" src="/media_stat/images/layout/clear.gif" alt="" /></td>
				</tr>
				<tr>
					<td colspan="5" bgcolor="#996"><img width="1" height="1" src="/media_stat/images/layout/clear.gif" alt="" /></td>
				</tr>
				<tr>
					<td colspan="5"><img width="1" height="4" src="/media_stat/images/layout/clear.gif" alt="" /></td>
				</tr>
				<% } %>
				<tr valign="top">
					<td align="center" colspan="3"><%-- image/summary column --%>
						<br />
						<table border="0" width="100%" cellspacing="0" cellpadding="0"><%-- image --%>
							<tr>
								<td align="left">
									<% if ( FDStoreProperties.useOscache() ) { %> 
										<oscache:cache time="300">
											<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
										</oscache:cache>
									<% } else { %>
											<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
									<% } %>
									<% if(qualifies && !productNode.isUnavailable()) { %>
										<table>
											<tr>
												<td><img src="/media_stat/images/template/offer_icon.gif" alt="Promotion icon"></td>
												<td><font class="title12">Free!<br /></font><A HREF="promotion.jsp?cat=<%=catId%>">See our $<%=prefix%> offer</a></td>
											</tr>
										</table>
										<br />
									<% } %>
									<%@ include file="/shared/includes/product/i_product_image.jspf" %>
								</td>
							</tr>
						</table>
					</td>
					<td width="10" rowspan="2">&nbsp;</td><%-- buffer cell --%>
					<td width="275" rowspan="2" ><%-- text/info column --%>
						<% if (titleBrandLogo!=null) { %>
							<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>
								<% if (brandPopupLink!=null) { %>
									<table cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td><a href="<%=brandPopupLink%>"><img src="<%=titleBrandLogo.getPath()%>" width="<%=titleBrandLogo.getWidth()%>" height="<%=titleBrandLogo.getHeight()%>" border="0"></a></td>
										</tr>
										<tr>
											<td>&nbsp; <a href="<%=brandPopupLink%>">Learn more about <%=thisProdBrandLabel%></a></td>
										</tr>
									</table><br />
								<% } else { %>
									<img src="<%=titleBrandLogo.getPath()%>" width="<%=titleBrandLogo.getWidth()%>" height="<%=titleBrandLogo.getHeight()%>" border="0">
								<% } %>
								</td>
							</tr>
							</table>
						<% } %>
						<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" /><br />
						<font class="title14" <%if (!skuAvailable) {%>color="#999999"<%}%>><%
							//
							// annotation mode
							//
							String productTitle = thisProdBrandLabel + " " + productNode.getFullName().substring(thisProdBrandLabel.length()).trim();
							List domains = minSku.getVariationMatrix();
							StringBuffer key = new StringBuffer();
							for(Iterator i = domains.iterator(); i.hasNext(); ){
								DomainValue domainValue = ((DomainValue)i.next());
								key.append(domainValue.getLabel());
								key.append("  ");
								key.deleteCharAt(key.length()-1);
							}
							productTitle= productTitle + (key.length()>0 ? ", " + key.toString() : "");
							if (!" - nm".equalsIgnoreCase(salesUnitDesc)) {
								productTitle += salesUnitDesc;
							}
							if (FDStoreProperties.isAnnotationMode()) {
								%><%@ include file="/includes/layouts/i_grocery_annotated_title.jspf" %><%
							} else {
								// no annotation, just display title
								%><%=productTitle%><%
							}
							//make sure there's no html breaks before this
							if (productNode.isPlatter()) {
								%><font class="text10"> *</font><%
								showCancellationNote = true;
							}
						%></font><br />
						<%
						boolean showUnavailableText = true;
							Date earliestDate = minSku.getEarliestAvailability();
							Calendar testDate = new GregorianCalendar();
							testDate.add(Calendar.DATE, 1);
						// cheat: if no availability indication, show the horizon as the earliest availability
						if (earliestDate == null) {
							earliestDate = DateUtil.addDays(DateUtil.truncate(new Date()), ErpServicesProperties.getHorizonDays());
						}

						if(skuAvailable && QuickDateFormat.SHORT_DATE_FORMATTER.format(testDate.getTime()).compareTo(QuickDateFormat.SHORT_DATE_FORMATTER.format(earliestDate)) < 0) {
							SimpleDateFormat sf = new SimpleDateFormat("MM/dd");
							showUnavailableText = false;
							%>
							<br />
							<b><font class="text12rbold">Earliest Delivery - <%=sf.format(earliestDate)%></font></b>
							<br />
							<%
						}

						if (!skuAvailable) {
							if (showUnavailableText) { %>
								<br />
								<b><font class="text12rbold">This item is temporarily unavailable.</font></b><br /><br />
								<img src="/media_stat/images/layout/999999.gif" width="225" height="1" border="0" vspace="5"><br />
							<% }

							if (productNode.getProductDescription()!=null && productNode.getProductDescription().getPath()!=null && (productNode.getProductDescription().getPath().toString()).indexOf("blank_file") < 0 ) { %>
								<br /><fd:IncludeMedia name="<%=productNode.getProductDescription().getPath()%>" /><br />
							<% } %>
							<%-- Unavailable item, GroupScale doesn't use these --%>
							<input type="hidden" value="" name="quantity_big" />
							<input type="hidden" class="text11bold" name="PRICE" size="7" onFocus="blur()" value="" />
							<%-- GroupScale CAN use this --%>
							<% syncProdSkuCode = minSku.getSkuCode(); %>
							<%
						} else {
							// !productNode.isUnavailable()
							syncProdQty = productNode.getQuantityMinimum();
							syncProdSkuCode = minSku.getSkuCode();
							syncProdSalesUnit = firstSalesUnit;
						%>
							<br />
							<%-- Product Pricing START --%>
								<% 
								ProductSkuImpression imp = new ProductSkuImpression(productNode, syncProdSkuCode);
								if (hasWas) { 
								%>
									<div>
										<table cellpadding="0" border="0">
											<tr>
												<td style="line-height: 16px; font-size: 13px; font-weight: bold; font-family: Verdana,Arial,sans-serif; color: #555;">Reg. </td>
												<td>
													<display:ProductPrice impression="<%= imp %>" grcyProd="true" showRegularPrice="true" showScalePricing="false" showWasPrice="false" showDescription="false" />
												</td>
												<td>
													<display:ProductPrice impression="<%= imp %>" grcyProd="true" showRegularPrice="false" showScalePricing="false" showWasPrice="true" showDescription="false" />
												</td>
											</tr>
										</table>
										<display:ProductPrice impression="<%= imp %>" grcyProd="true" showRegularPrice="false" showScalePricing="true" showWasPrice="false" showDescription="false" grpDisplayType="LARGE_RED" />
									</div>
								<% } else { %>
									<div>
										<table cellpadding="0">
											<tr>
												<td style="line-height: 16px; font-size: 13px; font-weight: bold; font-family: Verdana,Arial,sans-serif; color: #555;">Reg. </td>
												<td>
													<display:ProductPrice impression="<%= imp %>" grcyProd="true" showRegularPrice="true" showScalePricing="false" showWasPrice="false" showDescription="false"/>
												</td>
											</tr>
										</table>
										<display:ProductPrice impression="<%= imp %>" grcyProd="true" showRegularPrice="false" showScalePricing="true" showWasPrice="false" showDescription="false" grpDisplayType="LARGE_RED"/>
									</div>
								<% } %>
								<%@include file="/includes/product/i_price_taxdeposit.jspf"%>
							<%-- Product Pricing END --%>
							<br />
							
							<font class="text12bold">ABOUT:</font>
							<% if (hasNutrition || hasIngredients) { %>
							<br /><a href="javascript:pop('/shared/nutrition_info.jsp?catId=<%=request.getParameter("prodCatId")%>&productId=<%=request.getParameter("productId")%>',335,375)">Nutrition | Ingredients</a>
							<% }  %>
							<%
							if (productNode.getProductDescription()!=null && productNode.getProductDescription().getPath()!=null && productNode.getProductDescription().getPath().indexOf("blank_file.txt") < 0) { %>
								<br /><fd:IncludeMedia name="<%=productNode.getProductDescription().getPath()%>" /><br />
							<% } %>

							<% if(productNode.getCountryOfOrigin().size()>0) { %>
								<br /><b>Origin: </b>
								<logic:iterate id="coolText" collection="<%=productNode.getCountryOfOrigin()%>" type="java.lang.String">
									<br /><%=coolText%>
								</logic:iterate>
								<br />
							<% 
							}

							if (product!= null && product.hasNutritionInfo(ErpNutritionInfoType.HEATING)) { %>
							<br /><a href="javascript:pop('/shared/heating_instructions.jsp?catId=<%=request.getParameter("prodCatId")%>&productId=<%=request.getParameter("productId")%>',335,375)" class="title12">Heating Instructions</a>
							<% } %>
						<% } %>
						
						<%@ include file="/shared/includes/product/organic_claims.jspf" %>

						<%@ include file="/includes/product/claims.jspf" %>

						<%@ include file="/includes/product/kosher.jspf" %>

						<% if (!hasNutrition && !hasIngredients) { %>
							<br />Please check product label for nutrition, ingredients, and allergens.
						<% } %>
						
						<br />
						<%-- freshness guarantee --%>
						<%
						// ******** START -- Freshness Guarantee graphic ******************	
						String shelfLife = JspMethods.getFreshnessGuaranteed(productNode);
						if(shelfLife != null && shelfLife.trim().length() > 0) { %>

							<table width="0" border="0" cellspacing="0" cellpadding="0"><%-- Freshness Guaranteed --%>
								<tr>
									<td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="140" height="9"></td>
								</tr>
								<tr>
									<td height="5"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6"></td>
									<td height="5" style="border-top: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="160" height="1"></td>
									<td height="5"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6"></td>
								</tr>


								<tr> 
									<td colspan="3" align="center" valign="top">

									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr><td colspan="3" align="center" style="border-left: solid 1px #999966; border-right: solid 1px #999966;">
											<table border="0" cellspacing="0" cellpadding="0" width="100%">


												<tr valign="top">
													<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
													<td width="27"><img src="/media/images/site_pages/shelflife/days_<%=shelfLife%>.gif" width="27" height="27" border="0"></td>
													<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>
													<td  valign="top"><img src="/media/images/site_pages/shelflife/guarant_fresh_hdr_lg.gif" width="129" height="10"><br />
													<span class="text12">at least </span><span class="title12"><%=shelfLife%> days</span><span class="text12"><br /> from delivery</span></td>
													<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1"></td>								    
												</tr>
											</table>
										</td></tr>
									</table>
									</td>
								</tr>
								<tr>
									<td height="5"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6"></td>
									<td height="5" style="border-bottom: solid 1px #999966;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
									<td height="5"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6"></td>
								</tr>
								
							</table>
							<table width="188">
								<tr>
									<td align="left">
									<img src="/media_stat/images/layout/clear.gif" width="100%" height="6">
									<a href="javascript:pop('/brandpop.jsp?brandId=bd_fd_fresh_guarantee',400,585)">Learn more about our Freshness Guarantee - CLICK HERE</a>
									</td>
								</tr>
							</table>

						<% }
						// ******** END -- Freshness Guarantee graphic ******************	
						%>
					</td>
				</tr>
				<tr>
					<!-- <td valign="bottom" colspan="5"> --> <%-- qty/small image --%>
						<%-- Product Qty Controls START --%>
							<%
								/*imgLinkUrl = response.encodeURL("/group.jsp?catId="+currentFolder
									+ "&prodCatId="+productNode.getParentNode()
									+ "&productId="+productNode.getContentName())+"&trk=trans";*/
								//use pop link for small image of first prod
								imgLinkUrl = "javascript:pop('/shared/prodpop.jsp?productId="+productNode+"&catId="+productNode.getParentNode()+"',335,375)";
								int txCount = itemShownIndex;
								int temp = -1;
								bigProdIndex = 0;
								ProductModel pmTemp = null;
								ProductImpression piTemp = null;
								
								List impressions = (request.getAttribute("impressions")!=null)?(List)request.getAttribute("impressions"):new ArrayList();

								for (int i = 0; i<impressions.size(); i++) {
									//out.println("reqSkuCode: "+reqSkuCode);

									piTemp = (ProductImpression)impressions.get(i);
									pmTemp = (ProductModel)piTemp.getProductModel();
									//out.println(i+" : "+pmTemp.getFullName()+"<br />");
									//if a product is selected reqSkuCode will reflect it, otherwise bigProdIndex is the actual index number
									if (reqSkuCode != null && !"".equals(reqSkuCode)){
										if ((pmTemp.toString()).equals(productCode)) {
											SkuModel sku = sortedColl.get(i);//get the corresponding sku
											if(sku != null && reqSkuCode.equals(sku.getSkuCode()))
												bigProdIndex = i;
											//out.println("set bigProdIndex: "+bigProdIndex);
										}
									}else{
										//out.println("first if, reqSkuCode: "+reqSkuCode);
										if (impressions.get(i) instanceof TransactionalProductImpression) {
											bigProdIndex++;
										} else {
											bigProdIndex--;
										}
									}
									
									
										if (impressions.get(i) instanceof TransactionalProductImpression) {
											temp++;
										}
									// if (bigProdIndex<-1) { bigProdIndex = -1; }
									//out.println(bigProdIndex+",");
									LOGGER.debug(i+": "+pmTemp+" "+pmTemp.getDefaultSku());
								}
								if (temp > -1 && bigProdIndex > temp) { bigProdIndex = temp; } //avoid index oob
								//bigProdIndex should now be namespace.pricing[bigProdIndex]
								
								if (impressions.size() > 0) {
									//int displayIdx = (bigProdIndex > temp) ? temp : bigProdIndex;
									int displayIdx = bigProdIndex;
									piTemp = (ProductImpression)impressions.get(bigProdIndex);
									pmTemp = (ProductModel)piTemp.getProductModel();
									LOGGER.debug("Using: bigProdIndex: "+bigProdIndex);
									LOGGER.debug("pmTemp: "+pmTemp);
									LOGGER.debug("syncProdSkuCode: "+syncProdSkuCode);
									LOGGER.debug("temp: "+temp);
								%>
								<!-- <table border="0" cellspacing="5" cellpadding="1"> --> <%-- Qty control table --%>
									<td width="40" align="center"><nobr>
									<% if (!skuAvailable) { %>
										<font color="#999999">NA</font>
									<%
										//itemShownIndex--; /* no qty box, decrease display count */
									} else { %>
										<% if (impressions.get(bigProdIndex) instanceof TransactionalProductImpression) { %>
											<div class="groceryProductLinePrice" style="text-align: left; padding-left: 2px;">QTY</div>
											<img width="1" height="4" src="/media_stat/images/layout/clear.gif" alt="" /><br />
											<fd:TxProductControl txNumber="<%= displayIdx %>" namespace="<%= TX_JS_NAMESPACE %>" impression="<%= (TransactionalProductImpression) impressions.get(bigProdIndex) %>"/>
										<% } else { %>
											<span class="text10"><a href="/product.jsp?productId=<%= productNode %>&catId=<%= productNode.getParentNode() %>&trk=pmod">(click here to buy)</a></span>
											<%-- product needs configuring --%>
											<% itemShownIndex--; /* no qty box, decrease display count */ %>
										<% } %>
										<% if (typedQuantity != "") { %>
											<%-- set zero item to use passed in qty --%>
											<script type="text/javascript">
											<!--
												if (document.<%= TX_FORM_NAME %>['quantity_<%=displayIdx%>']) {
													document.<%= TX_FORM_NAME %>['quantity_<%=displayIdx%>'].focus();
													document.<%= TX_FORM_NAME %>['quantity_<%=displayIdx%>'].value = Number(<%=typedQuantity%>);
													document.<%= TX_FORM_NAME %>['quantity_<%=displayIdx%>'].blur();
												}
											//-->
											</script>
										<% } %>
									<% } %>
									</nobr></td>
									<td width="10" align="center" valign="middle">
										<img src="/media_stat/images/layout/clear.gif" name="bullet<%= itemShownIndex %>" width="6" height="6" border="0" alt="" />
									</td>
									<td align="center" valign="middle">
										<%
											Set hideBursts = new HashSet();
											hideBursts.add(EnumBurstType.DEAL);
										%>
										<display:ProductImage product="<%= productNode %>" action="<%= imgLinkUrl %>" showRolloverImage="true" hideBursts="<%= hideBursts %>"/>
									</td>
									</tr>
									<tr>
										<td colspan="5"><img width="1" height="4" src="/media_stat/images/layout/clear.gif" alt="" /></td>
									</tr>
									<tr>
										<td colspan="5">
											<%=FDURLUtil.getHiddenCommonParameters(request.getParameterMap(), "_big")%>
											<%@ include file="/shared/includes/product/i_minmax_note.jspf" %>
											<%@ include file="/includes/product/i_delivery_note.jspf" %>
										</td>
									</tr>
								<!-- </table> -->
								<% } %>
							
						<%-- Product Qty Controls END --%>
					<!-- </td>
				</tr> -->
<!--			</table> -->
		<% } %>
		</fd:FDProductInfo>
	</fd:ProductGroup>
	<% if (!bigProdShown) {
		// build hidden field to hold price..so java script does not cause err.
		/* !! GROUPSCALE NEVER HITS THIS? !! */
		%>
		<input type="hidden" name="PRICE">
		<input type="hidden" name="quantity_big" value="">
		<input type="hidden" name="skuCode_big" value="noBigProductDisplay">
		<%
	}

		/* !! GROUPSCALE NEVER HITS THIS? !! */

		// if we have a product that was specified from the featured list then we must find the page that it's on
		//count how many products that are to be displayed.
		if(productCode != null || reqSkuCode!=null) {
			int currPage = 1;
			for (int j=0;j<allSkuModels.size();j++) {
				SkuModel skuModel = (SkuModel)allSkuModels.get(j);
				
				if(j%itemsToDisplay==0 && j !=0 ) currPage++;
				if ((reqSkuCode!=null && skuModel.getSkuCode().equals(reqSkuCode) ) || reqSkuCode==null && ((ProductModel)skuModel.getParentNode()).getContentName().equals(productCode)) {
					pageNumber = currPage;
					break;
				}
			}
		}

	//begin displaying the products on the page
	boolean isAnyProdAvailable = true; //this will always be true for groupScale
	int loopEnd = skuCount;
	%>
	<!-- <table width="425" border="0" cellspacing="0" cellpadding="0"> -->
	<%
		int txCount = 0;
		int tpCount = 0;//Keeps track of Transaction product count
		String otherParams = buildOtherParams(showThumbnails, itemsToDisplay, -1, brandValue, sortBy, nutriName,request, null);
		LOGGER.debug("loopEnd "+loopEnd);
		Set<String> displaySet =  new HashSet<String>();
		List impressions = (request.getAttribute("impressions")!=null)?(List)request.getAttribute("impressions"):new ArrayList();			
		for(int jj = 0; jj < loopEnd;  jj++) {
			SkuModel sku = (SkuModel)allSkuModels.get(jj);
			
			LOGGER.debug("sku "+jj+" "+sku.getSkuCode());
			/*
			 *  material already displayed. skip it.
			 *	check if skuCode is the syncSkuCode, unless it's not set (selected item is unavailable), then
			 *	check if txCount is the bigProdIndex, and skip.
			 */
			String matId = sku.getProduct().getMaterial().getMaterialNumber();
			if (displaySet.contains(matId) || sku.getSkuCode().equals(syncProdSkuCode)) {
				if (!sku.isUnavailable()) {
					itemShownIndex++; //display count
				}
				if(impressions.get(txCount) instanceof TransactionalProductImpression)
					tpCount++;
				txCount++; //actual, transactional List count
				continue; //skip shown item
			}
			ProductModel displayProduct = ProductPricingFactory.getInstance().getPricingAdapter(sku.getProductModel(), user.getPricingContext());
			imgLinkUrl = response.encodeURL("/group.jsp?catId="+currentFolder
				+ "&prodCatId="+displayProduct.getParentNode()
				+ "&productId="+displayProduct.getContentName())+"&trk=trans";
			skus.add(sku);
			//Add the material number to the displaySet.
			displaySet.add(matId);
			
			%><%@include file="/includes/layouts/i_groupScale_product_separator.jspf"%>
			<fd:ProductGroup id='productNode' categoryId='<%= displayProduct.getParentNode().toString() %>' productId='<%= displayProduct.toString() %>'><%@include file="/includes/layouts/i_groupScale_product_line.jspf"%></fd:ProductGroup>
			<%
			if(impressions.get(txCount) instanceof TransactionalProductImpression)
				tpCount++;
			txCount++; //actual, transactional List count
			itemShownIndex++; //display count
		}	
	%>
	<% if(isAnyProdAvailable) { %>
		<tr>
			<td colspan="5"><img width="1" height="4" src="/media_stat/images/layout/clear.gif" alt="" /></td>
		</tr>
		<tr>
			<td colspan="5" bgcolor="#996"><img width="1" height="1" src="/media_stat/images/layout/clear.gif" alt="" /></td>
		</tr>
		<tr>
			<td colspan="5"><img width="1" height="4" src="/media_stat/images/layout/clear.gif" alt="" /></td>
		</tr>
		<tr>
			<td align="left" colspan="5">
				<table cellspacing="0" cellpadding="0" border="0" align="left"> 
				<tr>
					<td align="right" style="padding-top: 5px;" class="text11">
						<input width="93" type="image" height="20" border="0" style="padding-top: 5px; padding-bottom: 3px; display: block;" src="/media_stat/images/buttons/add_to_cart.gif" name="add_to_cart">
					</td>
					<td width="10">&nbsp;</td><%-- buffer cell --%>
					<td align="right" style="padding-top: 8px; 5px;" class="text11bold">Price&nbsp;<input type="text" value="" onfocus="blur()" onchange="" size="6" name="total" id="total_bottom" class="text11bold">
					</td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="5"><img width="1" height="8" src="/media_stat/images/layout/clear.gif" alt="" /></td>
		</tr>
		<tr>
			<td align="left" colspan="5"><fd:ProductGroup id='productNode' categoryId='<%= prodCatId %>' productId='<%= productCode %>'><%
				if (showCancellationNote) {
					%><%@ include file="/includes/product/i_cancellation_note.jspf" %><%
				}
				%></fd:ProductGroup></td>
		</tr>
	<% } %>
	</table>

		<%-- if we are adding to the cart via the form submit, then we need to set a variable in a hidden field --%>
		<input type="hidden" name="itemCount" value="<%= Math.min(itemsToDisplay, itemShownIndex) %>">
		<input type="hidden" name="totalQty" id="totalQty" value="0" />

		<%-- the controller tag must also know if an item is added to the list from the side bar (i.e. that particular item) or the actual selected ones 
			 the values are "actual_selection" or "side_bar" + _ + item number
		<input type="hidden" name="source" value="cart_selection" />
		  --%>
	</form>

