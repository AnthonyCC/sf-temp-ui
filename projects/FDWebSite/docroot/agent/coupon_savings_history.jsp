<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>
<%@ page import='com.freshdirect.fdstore.ecoupon.*' %>
<%@ page import='com.freshdirect.fdstore.ecoupon.model.*' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.*' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentKey' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentKeyFactory' %>
<%@ page import='com.freshdirect.cms.core.domain.ContentType' %>
<%@ page import='com.freshdirect.storeapi.application.*' %>
<%@ page import='com.freshdirect.storeapi.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='java.text.NumberFormat' %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="java.text.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%-- Customer ID is not really required in this page, however masquerade context is passed via FDUserI interface --%>
<fd:CheckLoginStatus id="user" />
<%
	// DO NOT ALLOW customers here!
	if (user == null || user.getMasqueradeContext() == null) {
		response.sendRedirect("/");
		return;
	}
%><% 	
	String errorString = null;
	int coupon_version = FDCouponManager.getMaxCouponsVersion();
	CmsManager manager = CmsManager.getInstance();
	Map<String, String> departments = new HashMap<String, String>();		
	ContentKey storeKey = ContentFactory.getInstance().getStoreKey();
	ContentNodeI contentNode = manager.getContentNode(storeKey);
	if(null !=contentNode){
		Set<ContentKey> subNodes = contentNode.getChildKeys();
		for (ContentKey subContentKey : subNodes) {
			ContentType contentType=subContentKey.getType();
			ContentNodeI subContentNode = manager.getContentNode(subContentKey);
			if(FDContentTypes.DEPARTMENT.equals(contentType)){
				Boolean hide = (Boolean) subContentNode.getAttributeValue("HIDE_IN_QUICKSHOP");
				if(hide != null && !hide)
					departments.put(subContentNode.getKey().getId(),(String) subContentNode.getAttributeValue("FULL_NAME"));
			}
		}
	}
%>
<%! Date currentDate = new Date();
	CmsManager manager = CmsManager.getInstance();
    private String getQueryString(HttpServletRequest request){
        Map m = request.getParameterMap();
        Enumeration e = request.getParameterNames();
        StringBuffer buf = new StringBuffer("?");
        while(e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if(!"startRecord".equalsIgnoreCase(name) && !"endRecord".equalsIgnoreCase(name)){
                String [] values = (String[]) m.get(name);
                if(values != null){
                    for(int i = 0; i < values.length; i++){
                        buf.append(name).append("=").append(values[i]).append("&");
                    }
                }
            }
        }
        return buf.toString();
    }
	
	private List<FDCouponInfo> getCoupons(List<ProductModel> productsList) {
		List<String> upcCodes = new ArrayList<String>();
		for (ProductModel prod : productsList) {
			for (SkuModel sku : prod.getSkus()) {
				try {
					if(!sku.isUnavailable()) {
						FDProductInfo fproduct = sku.getProductInfo();
						if(fproduct.getUpc() != null && fproduct.getUpc() != "") {									
							upcCodes.add(fproduct.getUpc());
						}
					}
				} catch(FDSkuNotFoundException e) {
					System.out.println("Sku not found"+sku.getSkuCode());
				} catch(FDResourceException e1) {
					System.out.println("FDResourceException:"+sku.getSkuCode() + e1.getMessage());
				}
			}				
		}
		//finally get the coupons
		List couponList = new ArrayList<FDCouponInfo>();
		for (String upc : upcCodes) {
			couponList.addAll(FDCouponFactory.getInstance().getCouponsByUpc(upc));
		}
		
		return couponList;
	}
	
	private List getProductsForAllDepartments(List<ProductModel> searchproducts,Map<String, String> departments,String searchTerm){
		//***Iterate thru departments
		for (String key : departments.keySet()) {
			ContentKey deptKey = ContentKeyFactory.get(ContentType.Department, key);
			ContentNodeI depContentNode = manager.getContentNode(deptKey);					
			if(null !=depContentNode) {
				Set subNodes = depContentNode.getChildKeys();
				//***Iterate thru department's children ---categories 
				for (Object object : subNodes) {
					ContentKey subContentKey= (ContentKey)object;
					if(null!=subContentKey){
						ContentType contentType=subContentKey.getType();
						ContentNodeI subContentNode = manager.getContentNode(subContentKey);
						if(FDContentTypes.CATEGORY.equals(contentType)){
							ContentNodeI catContentNode = manager.getContentNode(subContentKey);
							if(null !=catContentNode){
								Set catsubNodes = catContentNode.getChildKeys();
								Stack lifo = new Stack();
								for (Object cobject : catsubNodes) {
									lifo.push(cobject);
								}
								//***Iterate thru category children, can be subcategories or products
								while (!lifo.empty ()) {
									ContentKey bsubContentKey= (ContentKey)lifo.pop();
									if(null!=bsubContentKey) {
										ContentType bcontentType=bsubContentKey.getType();
										ContentNodeI bsubContentNode = manager.getContentNode(bsubContentKey);
										//***If the child is product, then we can get brand associated with product.
										if(FDContentTypes.PRODUCT.equals(bcontentType)){
											ProductModel bmodel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(bsubContentKey);
											List<BrandModel> brands = bmodel.getBrands();
											Iterator biter = brands.iterator();
											while(biter.hasNext()) {
												BrandModel bm = (BrandModel) biter.next();
												if(null!=searchTerm && (((bm.getContentKey().getId().toLowerCase()).indexOf(searchTerm.toLowerCase()) != -1)||((bm.getName().toLowerCase()).indexOf(searchTerm.toLowerCase()) != -1))) {
													searchproducts.add(bmodel);
												}
											}
										} else if(FDContentTypes.CATEGORY.equals(bcontentType)) {
											/****if child is a subcategory, we need to get children under that subcategory to drill down further products/categories. So category name gets added to the stack for further processing*/
											ContentNodeI subcatContentNode = manager.getContentNode(bsubContentKey);
											if(null != subcatContentNode) {
												Set subcatsubNodes = subcatContentNode.getChildKeys();
												for (Object subcobject : subcatsubNodes) {
													lifo.push(subcobject);
												}
											}
										}
									}
								}										
							}
						}
					}
				}
			}
		}
		return searchproducts;
	}
%>

<tmpl:insert template='/common/template/no_border_nonav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Coupons Savings History"/>
  </tmpl:put>
<%--   <tmpl:put name='title'>FreshDirect - Coupons Savings History</tmpl:put> --%>
	<tmpl:put name='content' direct='true'>
	
<link rel="stylesheet" href="/ccassets/css/promo.css" type="text/css">
<style type="text/css">
/* GENERIC LIST */

.list_header {
position: relative;
/*width: 100%;*/
height: auto;
padding: 0px;
background-color: #333366;
border-bottom: 1px #000000 solid;
color: #FFFFFF;
font-size: 10pt;
}

.list_header_text {
	color: #FFFFFF;
	font-size: 12px;
	font-weight: bold;
	padding-top: 1px;
	padding-bottom: 2px; 
}

a.list_header_text:link     { color: #FFFFFF; text-decoration: underline; }
a.list_header_text:visited  { color: #FFFFFF; text-decoration: underline; }
a.list_header_text:active   { color: #FFFFFF; text-decoration: underline; }
a.list_header_text:hover    { background: #9999CC; padding-left: 3px; padding-right: 3px; color: #333366; text-decoration: none; }

.list_header_detail {
color: #FFFFFF;
font-size: 8pt;
font-weight: bold;
padding-left: 4px;
padding-top: 1px;
padding-bottom: 2px; 
}

.list_content {
	position: relative;
	/*width: 100%;*/
	height: 75%;
	padding: 0px;
	background-color: #FFFFFF;
	color: #000000;
	font-size: 10px;
	overflow: auto;
}

.list_content_text {
color: #000000;
font-size: 9pt;
padding-left: 4px;
padding-top: 4px;
padding-right: 6px;
padding-bottom: 5px;
}

.list_odd_row {
background-color: #E1E1E1;
}

.list_even_row {
background-color: #FFFFFF;
}

.list_duplicate_row {
background-color: #FFFFCC;
}

.list_separator {
	background-color: #999999;
	line-height: 1px;
	height: 1px;
	padding: 0px;
	font-size: 1px;
}

.column_separator {
background-color: #CCCCCC;
}

.header_separator {
background-color: #FFFFFF;
}

.key            { font-size: 9pt; color: #000000; font-weight: bold; }
a.key:link      { color: #000000; text-decoration: none; }
a.key:visited   { color: #000000; text-decoration: none; }
a.key:active    { color: #000000; text-decoration: none; }
a.key:hover     { border: 1px #FF9900 solid; background: #FFFFCE; padding-left: 3px; padding-right: 3px; color: #FF6600; text-decoration: none; }


.time_stamp {
font-size: 8pt;
color: #333333;
}

.log_info {
font-size: 9pt;
color: #000000;
}

/* GENERIC LIST */


	.case_content_red_field {
		color: #CC0000;
		font-weight: bold;
		font-size: 10pt;
	}
	.yui-skin-sam .yui-pg-container {
		text-align: right;
		padding-right: 30px;
	}
	.yui-skin-sam .yui-pg-page {
		border: 0px !important;
		padding: 2px !important;		
	}
	.yui-skin-sam .yui-pg-first, .yui-skin-sam .yui-pg-previous, .yui-skin-sam .yui-pg-next, .yui-skin-sam .yui-pg-last, .yui-skin-sam .yui-pg-current, .yui-skin-sam .yui-pg-pages, .yui-skin-sam .yui-pg-page {
		font-family: Verdana,Arial,sans-serif !important;
		font-size: 9px;
		font-weight: bold;
	}
</style>

<script language="javascript">
	function DeptQueue(name, id, categories) {
		this.name = name;
		this.id = id;
		this.categories = categories;
	}

	function Category(name, id, brands) {
		this.name = name;
		this.id = id;
		this.brands = brands;
	}

	function Brand(name, id) {
		this.name = name;
		this.id = id;
	}

	var queues = {};
</script>
	
	<% 	
		Calendar cal = Calendar.getInstance();  
		cal.add(Calendar.DAY_OF_MONTH, -30);
		List<FDCouponInfo> cList = new ArrayList<FDCouponInfo>();//FDCouponManager.getActiveCoupons(cal.getTime());
		if(!"done".equals(request.getParameter("submission"))) {
			cList.addAll(FDCouponManager.getCouponsForCRMSearch(""));
		}
		//cList = FDCouponFactory.getInstance().getCoupons();
		String selectedDepartment = null;
		String selectedCategory = null;
		String selectedBrand = null;
		String searchTerm = null;
		if("done".equals(request.getParameter("submission"))) {
			//form submitted
			String find = request.getParameter("find");
			if("FIND".equals(find)) {
				cList = new ArrayList<FDCouponInfo>();
				searchTerm = request.getParameter("search_term");
				//search products,
				if(null != searchTerm && !"".equals(searchTerm.trim())){
					SearchResults results = StoreServiceLocator.contentSearch().searchProducts(searchTerm);
	                for ( FilteringSortingItem  node : results.getProducts() ) {
	                    ContentNodeModel n1 = node.getNode();
	                    if ( n1 instanceof ProductModel) {
							ProductModel pmodel = (ProductModel)n1;
	                        List<SkuModel> skus = pmodel.getSkus();
	                        Iterator skuIter = skus.iterator();
	                        while(skuIter.hasNext()) {
								SkuModel sku = (SkuModel) skuIter.next();
	                            try {
									if(!sku.isUnavailable()) {
										FDProductInfo fproduct = sku.getProductInfo();
	                                    if(fproduct.getUpc() != null && fproduct.getUpc() != "") {
											cList.addAll(FDCouponFactory.getInstance().getCouponsByUpc(fproduct.getUpc()));
	                                    }
	                                }
								} catch(FDSkuNotFoundException e) {
									System.out.println("Sku not found"+sku.getSkuCode());
	                            }
							}
						}
	                }
	                List<ProductModel> searchproducts = new ArrayList<ProductModel>();
	                cList.addAll(getCoupons(getProductsForAllDepartments(searchproducts,departments,searchTerm)));
				}
				//search coupon name, search coupon ID, search coupon value, 
				cList.addAll(FDCouponManager.getCouponsForCRMSearch(searchTerm));
				//search brands,
				
				
				
				//search dates.
			} else if("FILTER".equals(request.getParameter("so_filter_submit"))) {
				selectedDepartment = request.getParameter("department");
				selectedCategory = request.getParameter("category");
				selectedBrand = request.getParameter("brand");	
				
				if("-1".equals(selectedDepartment)) {
					cList.addAll(FDCouponManager.getCouponsForCRMSearch(""));
				}
				else {
					//get all products for selected brand
					ContentKey departmentKey = ContentKeyFactory.get("Department:"+selectedDepartment);
					ContentNodeI deptContentNode = manager.getContentNode(departmentKey);
					cList = new ArrayList<FDCouponInfo>();
					if(null !=deptContentNode) {
						Set subNodes = deptContentNode.getChildKeys();
						boolean breakloop = ("-1".equals(selectedCategory))?false:true;
						List<ProductModel> products = new ArrayList<ProductModel>();
						for (Object object : subNodes) {
							ContentKey subContentKey= (ContentKey)object;
							ContentNodeI subContentNode =null;
							boolean proceed = false;
							if(breakloop){
								ContentKey categoryKey = ContentKeyFactory.get("Category:"+selectedCategory);
								subContentNode = manager.getContentNode(categoryKey);
								proceed = true;
								
							}else if(null!=subContentKey){
								ContentType contentType=subContentKey.getType();
								subContentNode = manager.getContentNode(subContentKey);
								if(FDContentTypes.CATEGORY.equals(contentType)){
									subContentNode = manager.getContentNode(subContentKey);								
									if(!breakloop) {
										proceed = true;
									} else {
										if(selectedCategory.equals(subContentNode.getKey().getId())) {
											proceed = true;
										}
									}
								}
							}
							if(proceed) {									
								if(null!=subContentNode){
									Set categorySubNodes = subContentNode.getChildKeys();
									Stack catStack = new Stack();
									for (Object catObject : categorySubNodes) {
										catStack.push(catObject);
									}				
									while (!catStack.empty ()) {
										ContentKey bsubContentKey= (ContentKey)catStack.pop();
										if(null!=bsubContentKey) {
											ContentType bcontentType=bsubContentKey.getType();
											ContentNodeI bsubContentNode = manager.getContentNode(bsubContentKey);
											if(FDContentTypes.PRODUCT.equals(bcontentType)){
												ProductModel bmodel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(bsubContentKey);
												List<BrandModel> brands = bmodel.getBrands();
												Iterator biter = brands.iterator();
												while(biter.hasNext()) {
													BrandModel bm = (BrandModel) biter.next();
													if("-1".equals(selectedBrand)) {
														products.add(bmodel);
													} else {
														if(bm.getContentKey().getId().equals(selectedBrand)) {										
															products.add(bmodel);
														}
													}
												}
											} else if(FDContentTypes.CATEGORY.equals(bcontentType)) {
												ContentNodeI subcatContentNode = manager.getContentNode(bsubContentKey);
												if(null != subcatContentNode) {
													Set subcatsubNodes = subcatContentNode.getChildKeys();
													for (Object subcobject : subcatsubNodes) {
														catStack.push(subcobject);
													}
												}
											}
										}
									}
								}	
							}									
							
							if(breakloop && proceed)
								break;
						}
					
						//finally get the coupons
						cList.addAll(getCoupons(products));
					}
					}
			}else{
				cList.addAll(FDCouponManager.getCouponsForCRMSearch(""));
			}
		}
		
		/*_______________________________Load dept, cat and brand lists into javascript________________________________________________*/
		for (String key : departments.keySet()) {
			StringBuilder sb = new StringBuilder();
			// String key = (String) enumer.nextElement();
			String value = (String) departments.get(key);
			ContentKey deptKey = ContentKeyFactory.get("Department:"+key);
			ContentNodeI depContentNode = manager.getContentNode(deptKey);
			if(null !=depContentNode) {
				sb.append("{");
				Set<ContentKey> subNodes = depContentNode.getChildKeys();
				int subNodesIndex = 0;
				for (ContentKey subContentKey : subNodes) {
					subNodesIndex++;
					// ContentKey subContentKey= (ContentKey)object;
					if(null!=subContentKey){
						ContentType contentType=subContentKey.getType();
						ContentNodeI subContentNode = manager.getContentNode(subContentKey);
						if(FDContentTypes.CATEGORY.equals(contentType)){
							sb.append("\""+subContentNode.getKey().getId()+"\"" + ": new Category(");
							sb.append("\"");
							sb.append(subContentNode.getAttributeValue("FULL_NAME"));
							sb.append("\",\"");
							sb.append(subContentNode.getKey().getId());
							sb.append("\",");
							ContentNodeI catContentNode = manager.getContentNode(subContentKey);
							if(null !=catContentNode){
								Set catsubNodes = catContentNode.getChildKeys();
								Stack lifo = new Stack();
								for (Object cobject : catsubNodes) {
									lifo.push(cobject);
								}
								sb.append("{");
								Hashtable brandHash = new Hashtable();
								while (!lifo.empty ()) {
									ContentKey bsubContentKey= (ContentKey)lifo.pop();
									if(null!=bsubContentKey) {
										ContentType bcontentType=bsubContentKey.getType();
										ContentNodeI bsubContentNode = manager.getContentNode(bsubContentKey);
										if(FDContentTypes.PRODUCT.equals(bcontentType)){
											ProductModel bmodel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(bsubContentKey);
											List<BrandModel> brands = bmodel.getBrands();
											Iterator biter = brands.iterator();
											while(biter.hasNext()) {
												BrandModel bm = (BrandModel) biter.next();
												brandHash.put(bm.getContentKey().getId(), bm.getName());
											}
										} else if(FDContentTypes.CATEGORY.equals(bcontentType)) {
											ContentNodeI subcatContentNode = manager.getContentNode(bsubContentKey);
											if(null != subcatContentNode) {
												Set subcatsubNodes = subcatContentNode.getChildKeys();
												for (Object subcobject : subcatsubNodes) {
													lifo.push(subcobject);
												}
											}
										}
									}
								}
								Enumeration benumer = brandHash.keys();
								while(benumer.hasMoreElements()) {
									String bkey = (String) benumer.nextElement();
									String bvalue = (String) brandHash.get(bkey);
									sb.append("\""+bkey+"\"" + ": new Brand(");
									sb.append("\"");
									sb.append(bvalue);
									sb.append("\",\"");
									sb.append(bkey);
									sb.append("\")");
									if (benumer.hasMoreElements()) {
										sb.append(",");
									}
								}
								sb.append("}");
								sb.append(")");//end cat
								if (subNodesIndex != subNodes.size()) {
									sb.append(",");
								}
							}
						}
					}
				}
			}
			sb.append("}");
		%>
		<script language="JavaScript">
			queues["<%= key %>"] = new DeptQueue("<%= value %>", "<%= key %>", <%=sb.toString()%>);
		</script>
		<%
	}	
	%>

<script>
	/* default select options. key is the elemId */
	var defaultListValues = {
		'department': ['All Departments', '-1'],
		'category': ['All Categories', '-1'],
		'brand': ['All Brands', '-1']
	};
	/* queue helper, prevents missing data errors
	 * based on the params passed in, fetches the correct queue level
	 * or returns an empty object.
	 */
		function getQueue(deptId, catId) {
			var queue = {};
			
			if (window['queues']) {
				queue = queues;

				if (deptId != null) {
					if (deptId != '' && queue.hasOwnProperty(deptId) && queue[deptId].hasOwnProperty('categories')) { //get Cats queue
							queue = queue[deptId].categories;
	
							if (catId != null) {
								if (catId != '' && queue.hasOwnProperty(catId) && queue[catId].hasOwnProperty('brands')) { //get Brands queue
									queue = queue[catId].brands;
								} else {
									queue = {};
								}
							}
					} else {
						queue = {};
					}
				}
			}
			
			return queue;
		}
	/* fetch data from an object{objects} and sorts it based on "object.name:object.id"
	 *	(to prevent name duplicates). Returns sorted array.
	 */
	function getSortedData(dataObjs) {
		var tempArrToSort = [];
		for (data in dataObjs) {
			tempArrToSort.push(dataObjs[data].name+':'+dataObjs[data].id);
		}
		return tempArrToSort.sort();
	}
	/* populates a list (by elemId), using an object{objects} as a data source
	 *	clears list and adds sorted items (using getSortedData())
	 * 	checks for defaults in global defaultListValues object
	 *	uses generic populateLists() method in common_javascript.js
	 */
	function popList(selectElemId, defSelectedVar, dataSourceObj) {
		var sortedDataArray = [];
		var defValueArray = [];
		
		$jq('#'+selectElemId).empty();
		if (defaultListValues.hasOwnProperty(selectElemId)) {
			addOpt(selectElemId, defaultListValues[selectElemId][0], defaultListValues[selectElemId][1]);
		}

		if (defSelectedVar != null && defSelectedVar != '') {
			defValueArray.push([selectElemId, defSelectedVar]);
		}

		var tempArrToSort = getSortedData(dataSourceObj);

		for (var i = 0; i < tempArrToSort.length; i++) {
			var optData = tempArrToSort[i].split(':');
			
			sortedDataArray.push([selectElemId, optData[0], optData[1]]);
		}

		populateLists(sortedDataArray, defValueArray);
	}

	function clearoptions() {
		document.getElementById("search_term").value="";
		popList('department', '', {});
		popList('category', '', {});
		popList('brand', '', {});
	}

</script>

<form method='POST' name="frmStndOrders" id="frmStndOrders">
	<input type="hidden" name="submission" value="done" />

	<div class="BG_live" style="border-bottom: 2px solid #000000; border-top: 1px solid #000000;">

		<table class="BG_live">
			<tr>
				<td class="promo_page_header_text" rowspan="2" width="175">Coupon&nbsp;Savings&nbsp;History</td>
				<td width="600" align="right">Search <input type="text" value="<%=searchTerm%>" name="search_term" size="50" id="search_term" />
				</td>
				<td><input type="submit" name="find" value="FIND" class="submit" /></td>
				<td><input type="submit" name="clear" value="Clear" class="submit" onclick="clearoptions()" /></td>
				<td rowspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td align="right">
					<select name="department" id="department" onchange="popList( 'category', $jq('#department option:selected').val(), getQueue($jq('#department option:selected').val()) ); popList('brand', '', {});" class="w175px"></select>
					<select name="category" id="category" onchange="popList( 'brand', $jq('#category option:selected').val(), getQueue($jq('#department option:selected').val(), $jq('#category option:selected').val()) );" class="w175px"></select>
					<select name="brand" id="brand" class="w175px"></select>
					<script type="text/javascript">
						popList('department', '<%= selectedDepartment %>', getQueue());
						popList('category', '<%= selectedCategory %>', getQueue('<%= selectedDepartment %>'));
						popList('brand', '<%= selectedBrand %>', getQueue('<%= selectedDepartment %>', '<%= selectedCategory %>'));
					</script>
				</td>
				<td><input type="submit" value="FILTER" id="so_filter_submit" name="so_filter_submit" class="promo_btn_grn" /></td>
				<td><input type="submit" name="clear" value="Clear" class="submit" onClick="clearoptions()" /></td>
			</tr>
		</table>

	</div>			
			
	<%
		Hashtable uniquecoupons = new Hashtable(); 
		List<FDCouponInfo> finalCouponList = new ArrayList<FDCouponInfo>();
	%>
	<logic:iterate id="coupon" collection="<%= cList %>" type="com.freshdirect.fdstore.ecoupon.model.FDCouponInfo" indexId="idx">				
	<%				
		if(!uniquecoupons.containsKey(coupon.getCouponId())) {
			uniquecoupons.put(coupon.getCouponId(),coupon.getCouponId());
			finalCouponList.add(coupon);
		}
	%>
	</logic:iterate>
	<% if(finalCouponList == null || finalCouponList.size() == 0){ %>
		<div style="color:#CC0000; font-weight: bold;">No matching coupons found.</div>
	<% } else { 
		String queryString = getQueryString(request);
		int increment = 25;
		int startRecord = Integer.parseInt(NVL.apply(request.getParameter("startRecord"), "1"));
		if (startRecord < 1) {
			startRecord = 1;
		}
		int endRecord = startRecord + (increment-1); //Integer.parseInt(NVL.apply(request.getParameter("endRecord"), Integer.toString(increment)));
		if(endRecord > finalCouponList.size())
			endRecord = finalCouponList.size();
		int pages = 0;
	%>

	<%!
		private static NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
		private static NumberFormat FORMAT_PERCENTAGE = NumberFormat.getPercentInstance(Locale.US);
		private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy");
		
		public String getFormattedAmount(FDCustomerCoupon fdCoupon) {
			String couponValDisplay = "";
			double couponVal = 0;
			
			try {
    			couponVal = Double.parseDouble(fdCoupon.getValue());
				if (EnumCouponOfferType.PERCENT_OFF.equals(fdCoupon.getOfferType())) {
					couponValDisplay = FORMAT_PERCENTAGE.format(couponVal/100);
				} else {
					couponValDisplay = FORMAT_CURRENCY.format(couponVal);
				}
    		} catch(NumberFormatException nfe) {
    		}
			return couponValDisplay;
		}
	
		public class PromoRow {
			private FDCustomerCoupon fdCoupon;
			private String code;
			private String name;
			private String brand;
			private double amount;
			private Integer qty;
			private Date starts;
			private Date expires;
			private String status;
		};
	
		private static Comparator<PromoRow> COMP_CODE = new Comparator<PromoRow>() {
			public int compare(PromoRow o1, PromoRow o2) {
				PromoRow p1 = (PromoRow)o1;
				PromoRow p2 = (PromoRow)o2;
				
				return p1.code.compareTo(p2.code);
			}
		};
		private static Comparator<PromoRow> COMP_NAME = new Comparator<PromoRow>() {
			public int compare(PromoRow o1, PromoRow o2) {
				PromoRow p1 = (PromoRow)o1;
				PromoRow p2 = (PromoRow)o2;
				
				return p1.name.compareTo(p2.name);
			}
		};
		private static Comparator<PromoRow> COMP_BRAND = new Comparator<PromoRow>() {
			public int compare(PromoRow o1, PromoRow o2) {
				PromoRow p1 = (PromoRow)o1;
				PromoRow p2 = (PromoRow)o2;
				if (p1.brand == null || p2.brand == null) { return 0; }
				return p1.brand.compareTo(p2.brand);
			}
		};
		private static Comparator<PromoRow> COMP_AMOUNT = new Comparator<PromoRow>() {
			public int compare(PromoRow o1, PromoRow o2) {
				PromoRow p1 = (PromoRow)o1;
				PromoRow p2 = (PromoRow)o2;
				
				Double d=new Double(p1.amount - p2.amount);
				if(d > 0){
					return 1;
				}else if(d < 0){
					return -1;
				}else{
					return 0;
				}
			}
		};
		private static Comparator<PromoRow> COMP_QTY = new Comparator<PromoRow>() {
			public int compare(PromoRow o1, PromoRow o2) {
				PromoRow p1 = (PromoRow)o1;
				PromoRow p2 = (PromoRow)o2;
				
				return p1.qty.compareTo(p2.qty);
			}
		};
		private static Comparator<PromoRow> COMP_STARTS = new Comparator<PromoRow>() {
			public int compare(PromoRow o1, PromoRow o2) {
				PromoRow p1 = (PromoRow)o1;
				PromoRow p2 = (PromoRow)o2;
				
				if(p1.starts == null) return 1;
				if(p2.starts == null) return -1;
				
				return p1.starts.compareTo(p2.starts);
			}
		};
		private static Comparator<PromoRow> COMP_EXPIRES = new Comparator<PromoRow>() {
			public int compare(PromoRow o1, PromoRow o2) {
				PromoRow p1 = (PromoRow)o1;
				PromoRow p2 = (PromoRow)o2;
				
				if(p1.expires == null) return 1;
				if(p2.expires == null) return -1;
				
				return p1.expires.compareTo(p2.expires);
			}
		};
		private static Comparator<PromoRow> COMP_STATUS = new Comparator<PromoRow>() {
			public int compare(PromoRow o1, PromoRow o2) {
				PromoRow p1 = (PromoRow)o1;
				PromoRow p2 = (PromoRow)o2;
				
				if(p1.status == null) return 1;
				if(p2.status == null) return -1;
				
				return p1.status.compareTo(p2.status);
			}
		};
	
		
		public final static Map<String, Comparator<PromoRow>> PROMO_COMPARATORS = new HashMap<String, Comparator<PromoRow>>();
		static {
			PROMO_COMPARATORS.put("code", COMP_CODE);
			PROMO_COMPARATORS.put("name", COMP_NAME);
			PROMO_COMPARATORS.put("brand", COMP_BRAND);
			PROMO_COMPARATORS.put("amount", COMP_AMOUNT);
			PROMO_COMPARATORS.put("qty", COMP_QTY);
			PROMO_COMPARATORS.put("starts", COMP_STARTS);
			PROMO_COMPARATORS.put("expires", COMP_EXPIRES);
			PROMO_COMPARATORS.put("status", COMP_STATUS);
		};
	%>
	<%
	
		List<PromoRow> promoRows = new ArrayList<PromoRow>();
		JspTableSorter sort = new JspTableSorter(request);

	%>
	
	<logic:iterate id="coupon" collection="<%= finalCouponList %>" type="com.freshdirect.fdstore.ecoupon.model.FDCouponInfo" indexId="idx">
		<%
			FDCustomerCoupon fdCoupon = new FDCustomerCoupon(coupon,EnumCouponStatus.COUPON_ACTIVE,null,false,null);
			fdCoupon.setDisplayStatus(EnumCouponDisplayStatus.COUPON_CLIPPABLE);
			
			PromoRow p = new PromoRow();
				p.fdCoupon = fdCoupon;
				p.code = coupon.getCouponId();
				p.name = coupon.getShortDescription();
				
				if("FILTER".equals(request.getParameter("so_filter_submit")) && (request.getParameter("brand") != null && !"-1".equals(request.getParameter("brand")))) {
					ContentKey brandKey = ContentKeyFactory.get(ContentType.Brand, request.getParameter("brand"));
					ContentNodeI brandContentNode = manager.getContentNode(brandKey);
					BrandModel _bmodel = (BrandModel) ContentFactory.getInstance().getContentNodeByKey(brandKey);
				
					p.brand = _bmodel.getName();
				} else {
					//get brand from coupon upc
					Hashtable bHash = new Hashtable();
					if(null !=coupon.getRequiredUpcs()) {
						for (Iterator iterator = coupon.getRequiredUpcs().iterator(); iterator.hasNext();) {
							FDCouponUPCInfo fdCouponUpcInfo = (FDCouponUPCInfo) iterator.next();
							String couponUPC = StringUtil.convertToUPCA(fdCouponUpcInfo.getUpc());//EAN13 to UPCA conversion
							FDProductInfo cachedProductInfo = FDCachedFactory.getProductInfoByUpc(couponUPC);										
							if(null !=cachedProductInfo) {
								ProductModel pm = ContentFactory.getInstance().getProduct(cachedProductInfo.getSkuCode());
								List<BrandModel> brands = pm.getBrands();
								Iterator biter = brands.iterator();
								while(biter.hasNext()) {
									BrandModel bm = (BrandModel) biter.next();
									bHash.put(bm.getName(), bm.getName());
								}
							}
						}
					}
					p.brand = StringUtil.join(bHash.values(), ", ");
				}
				
				p.amount = 0;
				try {
					p.amount = Double.parseDouble(coupon.getValue());
				} catch(NumberFormatException nfe) {
				}
				
				p.qty = 0;
				try {
					p.qty = Integer.parseInt(coupon.getRequiredQuantity());
				} catch(NumberFormatException nfe) {
				}
				
				
				p.starts = DateUtil.parseMDY(coupon.getStartDate());
				p.expires = coupon.getExpirationDate();
				
				p.status = "Active";
				if ( currentDate.after(p.expires) && !DateUtil.isSameDay(currentDate, p.expires)) { p.status = "Expired"; }
				if ( !coupon.isEnabled()) { p.status = "Disabled"; }
				
				promoRows.add(p);
		%>
	</logic:iterate>
	<%
		Comparator comp = (Comparator)PROMO_COMPARATORS.get(sort.getSortBy());
		if (comp != null) {
			Collections.sort(promoRows, sort.isAscending() ? comp : new ReverseComparator(comp));
		}
	%>
	<div style="padding: 10px;">
		<div id="pageresults" style="text-align:left;padding-left:30px;float:left;font-weight:bold;font-size: 10pt;">
			Results: <%=startRecord%> to <%=endRecord%> (of <%=finalCouponList.size()%>)
			<%
				if (comp != null) {
					String sortByString = sort.getSortBy();
					if ("amount".equals(sortByString)) { sortByString = "Value"; }
					if ("code".equals(sortByString)) { sortByString = "Coupon ID"; }
					if ("name".equals(sortByString)) { sortByString = "Ecoupon Offer"; }
					if ("qty".equals(sortByString)) { sortByString = "Required Quantity"; }
					if ("starts".equals(sortByString)) { sortByString = "Start Date"; }
					if ("expires".equals(sortByString)) { sortByString = "Expiration Date"; }
					char[] chars = sortByString.toCharArray();
					chars[0] = Character.toUpperCase(chars[0]);
					
					%> - sorted by <%= String.valueOf(chars) %>,&nbsp;<%= (sort.isAscending()) ? "Ascending" : "Descending" %><%
				}
			%>
		</div>
		<div id="pagenums" style="float:right;">			
			<div id="pagenums" class="yui-pg-container">Page: 
				<%
					if (finalCouponList.size() > increment) {						
						for(int i = finalCouponList.size(); i > 0; i=i-increment ) {
							int startIdx = (increment * pages)+1;
							int endIdx = startIdx + (increment-1);
							pages++;
							if (endRecord == (pages*increment) || startIdx == startRecord) { %>
								<span class="yui-pg-current-page yui-pg-page"><%= pages %></span>
							<% } else { %>
								<span id="yui-pg0-0-pages72" class="yui-pg-pages"><a title="Page <%= pages %>" class="yui-pg-page" href="<%=request.getRequestURI() + queryString%>startRecord=<%=startIdx%>&endRecord=<%=(endIdx>endRecord)?finalCouponList.size():endIdx%>"><%= pages %></a></span>
							<%
							}
						}
					} else { %>
						<span class="yui-pg-current-page yui-pg-page">1</span>
				<% } %>
			</div>
		</div>
		<br style="clear: both" />
	</div>
	<div id="result">
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr class="list_header">
				<td width="10">&nbsp;</td>
				<td width="75"><a href="?<%= sort.getFieldParams("code") %>" class="list_header_text">Coupon&nbsp;ID</a></td>
				<td width=""><a href="?<%= sort.getFieldParams("name") %>" class="list_header_text">Ecoupon&nbsp;Offer</a></td>
				<td width="20%"><a href="?<%= sort.getFieldParams("brand") %>" class="list_header_text">Brand</a></td>
				<td width="50"><a href="?<%= sort.getFieldParams("amount") %>" class="list_header_text">Value</a></td>
				<td width="75" align="center"><a href="?<%= sort.getFieldParams("qty") %>" class="list_header_text">Req.&nbsp;Qty</a></td>
				<td width="100" align="center"><a href="?<%= sort.getFieldParams("starts") %>" class="list_header_text">Start&nbsp;Date</a></td>
				<td width="120" align="center"><a href="?<%= sort.getFieldParams("expires") %>" class="list_header_text">Expiration&nbsp;Date</a></td>
				<td width="75"><a href="?<%= sort.getFieldParams("status")%>" class="list_header_text">Status</a></td>
				<td width="10">&nbsp;</td>
			</tr>
			
			<%
				int counter = 0;
				for(Iterator i = promoRows.iterator(); i.hasNext();){
					PromoRow p = (PromoRow) i.next();
					if (counter >= (startRecord-1) && counter < endRecord) {
			%>
					    <tr valign="top" <%= counter % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
							<td>&nbsp;</td>
					        <td><%= p.code %></td>
					        <td><%= p.name %></td>
					        <td><%= p.brand %></td>
					        <td><%= getFormattedAmount(p.fdCoupon) %></td>
					        <td align="center"><%= p.qty %></td>
					        <td align="center"><%= DATE_FORMATTER.format(p.starts) %></td>
					        <td align="center"><%= DATE_FORMATTER.format(p.expires) %></td>
							<td><%= p.status %></td>
							<td>&nbsp;</td>
						</tr>
					    <tr class="list_separator" style="padding: 0px;">
							<td colspan="10"></td>
					    </tr>
			<%
				    }
					counter++;
			    }
			%>
		</table>
	</div>
				
		<% } /* end content if */ %>
		</div>
	</form>
	</tmpl:put>
</tmpl:insert>
