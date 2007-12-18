<%@ page import="com.freshdirect.cms.*" %>
<%@ page import="com.freshdirect.cms.application.*" %>

<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.lists.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.webapp.util.RequestUtil" %>

<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='java.net.URLEncoder' %>
<%@ page import='org.apache.commons.lang.StringUtils' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/search_nav2.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Search</tmpl:put>

<%! 
private final static java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);

/**
 * @return Map of String (productId) -> Integer (score)
 */
private Map getProductHistory(HttpSession session) throws FDResourceException {
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	if (user.getIdentity()==null) {
		return	Collections.EMPTY_MAP;
	}
	Map prodHist = (Map) session.getAttribute("search.ProductHistory");
	if (prodHist == null) {
		List l = FDListManager.getEveryItemEverOrdered(user.getIdentity());
		if (l==null || l.isEmpty()) {
			prodHist = Collections.EMPTY_MAP;
		} else {
			prodHist = new HashMap();
			for (Iterator i = l.iterator(); i.hasNext(); ) {
				FDProductSelection sel = (FDProductSelection) i.next();
				String productId = sel.getProductRef().getProductName();
				Integer score = (Integer) prodHist.get(productId);
				if (score==null) {
					score = new Integer(0);
				}
				prodHist.put(productId, new Integer(score.intValue() + sel.getStatistics().getFrequency()));
			}
		}
		session.setAttribute("search.ProductHistory", prodHist);
	}
	return prodHist;
}

private class ScoreComparator implements Comparator {
	private final Map scores;

	public ScoreComparator(Map scores) {
		this.scores = scores;
	}

	private int getScore(Object o) {
		ProductModel pm = (ProductModel) o;
		String productId = pm.getProductRef().getProductName();
		Integer score = (Integer) scores.get(productId);
		return score==null ? 0 : score.intValue();
	}

	public int compare(Object o1, Object o2) {
		return getScore(o2) - getScore(o1);
	}

}

public List search(HttpSession session, String query) throws FDResourceException {

	SearchResults res = ContentFactory.getInstance().search(query);
	List products = new ArrayList(2000);
	//products.addAll(res.getExactProducts());
	products.addAll(res.getFuzzyProducts());
	
	Map scores = FDCustomerManager.getProductPopularity();
	Collections.sort(products, new ScoreComparator(scores));
	
	scores = getProductHistory(session);
	if (!scores.isEmpty()) {
		Collections.sort(products, new ScoreComparator(scores));
	}
	
	return products;
}

private List collectTop(Map counts, int threshold) {
	List topEntries = new ArrayList(counts.entrySet());
	
	Collections.sort(topEntries, new Comparator() {
		public int compare(Object o1, Object o2) {
			Map.Entry e1 = (Map.Entry)o1;
			Map.Entry e2 = (Map.Entry)o2;
			return ((Comparable)e2.getValue()).compareTo( (Comparable)e1.getValue() );
			//return ((Integer)e2.getValue()).intValue() - ((Integer)e1.getValue()).intValue();
		}
	});
	
	int max = Math.min(topEntries.size(), threshold);
	List l = new ArrayList(max);
	for (int i=0; i<max; i++) {
		Map.Entry e = (Map.Entry) topEntries.get(i);
		l.add(e.getKey());
	}
	return l;
}

/**
 * @return SortedMap of String (brand fullName) -> String (brand Id)
 */
private SortedMap collectBrands(List products) {

	/** Map of BrandModel -> Integer (count) */
	Map counts = new HashMap();
	for (Iterator i = products.iterator(); i.hasNext(); ) {
		ProductModel prod = (ProductModel) i.next();
		List brands = prod.getBrands();
		if (brands.isEmpty()) {
			continue;
		}
		BrandModel brand = (BrandModel)brands.get(0);
		Integer count = (Integer)counts.get(brand);
		count = count==null ? new Integer(1) : new Integer(count.intValue() + 1);
		counts.put(brand, count);
		
		/*
		for (Iterator j = brands.iterator(); j.hasNext(); ) {
			BrandModel brand = (BrandModel)j.next();
			Integer count = (Integer)counts.get(brand);
			count = count==null ? new Integer(1) : new Integer(count.intValue() + 1);
			counts.put(brand, count);
		}
		*/
	}
	
	List brands = collectTop(counts, 7);
	
	SortedMap m = new TreeMap();
	for (Iterator i = brands.iterator(); i.hasNext(); ) {
		BrandModel brand = (BrandModel)i.next();
		m.put(brand.getFullName(), brand.getContentName());
	}
	return m;

	/*
	SortedMap m = new TreeMap();
	for (Iterator i = products.iterator(); i.hasNext(); ) {
		ProductModel prod = (ProductModel) i.next();
		List brands = prod.getBrands();
		for (Iterator j = brands.iterator(); j.hasNext(); ) {
			BrandModel brand = (BrandModel)j.next();
			m.put(brand.getFullName(), brand.getContentName());
		}
	}
	return m;
	*/
}

private List filterByAvailability(List products) {
	List l = new ArrayList(products.size());
	for (Iterator i = products.iterator(); i.hasNext(); ) {
		ProductModel product = (ProductModel) i.next();
		//if (!product.isUnavailable()) {
		//if (!product.isHidden() && product.isSearchable() && !product.isDiscontinued()) {
		if (!product.isHidden() && product.isSearchable() && !product.isUnavailable()) {
			l.add(product);
		}
	}
	return l;
}

private List filterByBrand(List products, String brandId) {
	BrandModel brand = (BrandModel)ContentFactory.getInstance().getContentNodeByName(brandId);
	
	List l = new ArrayList(products.size());
	for (Iterator i=products.iterator(); i.hasNext(); ) {
		ProductModel prod = (ProductModel) i.next();
		List brands = prod.getBrands();
		if (brands.contains(brand)) {
			l.add(prod);
		}
	}
	return l;
}

private void collectParentPathIds(List parentIds, ContentNodeModel node) {
	if (node==null || node instanceof StoreModel) {
		return;
	}
	ContentNodeModel parent = node.getParentNode();
	parentIds.add(0, parent);
	collectParentPathIds(parentIds, parent);
}

private List filterByPath(List products, String pathIds) {
	List l = new ArrayList(products.size());
	for (Iterator i = products.iterator(); i.hasNext(); ) {
		ProductModel prod = (ProductModel) i.next();
		List parentPathIds = new ArrayList();
		collectParentPathIds(parentPathIds, prod);
		String ppath = StringUtils.join(parentPathIds.iterator(), "/");
		if (ppath.startsWith(pathIds)) {
			l.add(prod);
		}
	}
	return l;
}


	private static void displayTreeProduct(JspWriter out, ProductModel product) throws FDResourceException, IOException {
		boolean unav = product.isUnavailable();
		String productName = product.getFullName();
		String brandName = product.getPrimaryBrandName();
		if (brandName != null
			&& brandName.length() > 0
			&& (productName.length() >= brandName.length())
			&& productName.substring(0, brandName.length()).equalsIgnoreCase(brandName)) {
			String shortenedProductName = productName.substring(brandName.length()).trim();
			productName = "<b>" + brandName + "</b> " + shortenedProductName;
		}

		if (unav) {
			out.print("<tr class='unavailable'>");
		} else {
			out.print("<tr class='searchHit'>");
		}

		Image img = product.getThumbnailImage();
		//Image img  = product.getCategoryImage();
		out.print("<td class='thumbnail'>");
		if (img!=null) {
			out.print("<img src='");
			out.print(img.getPath());
			out.print("' width='");
			out.print(img.getWidth());
			out.print("' height='");
			out.print(img.getHeight());
			out.print("'>");
		}
		out.print("</td>");
		
		out.print("<td>");
		out.print("<a href='/product.jsp?productId=");
		out.print(product);
		out.print("&catId=");
		out.print(product.getParentNode());
		out.print("&trk=srch'>");
		out.print(productName);
		out.print("</a>");

		if (product.getAka() != null && !"".equals(product.getAka())) {
			out.print("<span class='aka'>");
			out.print(" (" + product.getAka() + ") ");
			out.print("</span>");
		}


		String productPrice = null;
		Comparator priceComp = new ProductModel.PriceComparator();
		List skus = product.getSkus();
		for (ListIterator li = skus.listIterator(); li.hasNext();) {
			SkuModel sku = (SkuModel) li.next();
			if (sku.isUnavailable()) {
				li.remove();
			}
		}
		int skuSize = skus.size();

		SkuModel sku = null;

		// skip this item..it has no skus.  Hmmm?
		if (skuSize == 1) {
			sku = (SkuModel) skus.get(0); // we only need one sku
		} else if (skus.size() > 1) {
			sku = (SkuModel) Collections.min(skus, priceComp);
		}

		FDProductInfo pi;

		if (sku != null) {
			try {
				pi = FDCachedFactory.getProductInfo(sku.getSkuCode());
				productPrice = currencyFormatter.format(pi.getDefaultPrice())
					+ "/"
					+ pi.getDisplayableDefaultPriceUnit().toLowerCase();
			} catch (FDSkuNotFoundException ex) {
				// safe to ignore
			}
		}

		String sizeDesc = product.getSizeDescription();
		if (sizeDesc != null) {
			out.print("<span class='sizeDescription'>");
			out.print("(" + sizeDesc + ") ");
			out.print("</span>");
		}
		out.print("</td>");

		out.print("<td class='price'>");
		if (unav) {
			out.print("N/A");
		} else {
			out.print(productPrice);
		}
		out.print("</td>");
		
		out.print("</tr>");
	}
	
	public void displayNodeTree(HttpServletRequest request, JspWriter out, Map map) throws FDResourceException, IOException {
		String baseQueryString = RequestUtil.getFilteredQueryString(request, new String[] { "path", "x", "y" });
		Collection root = (Collection) map.get(null);
		displayNodeTree(baseQueryString, request.getParameter("path"), out, map, new Stack(), root);
	}
	
	private void displayNodeTree(String baseQueryString, String filterPath, JspWriter out, Map map, Stack path, Collection ls) throws FDResourceException, IOException {
		for (Iterator i = ls.iterator(); i.hasNext();) {
			ContentNodeModel node = (ContentNodeModel) i.next();
			
			if (node instanceof ProductModel) {
				continue;
			}
			
			path.push(node.getContentName());
			
			out.print("<div class='" + node.getContentKey().getType() + "' style='");
			out.print("margin-left: " + (((path.size() - 1) * 8) + 4) + "px;");
			out.print("text-indent: -4px'>");
			
			String currPath = StringUtils.join(path.iterator(), "/");

			boolean samePath = filterPath!=null && filterPath.startsWith(currPath);
			
			if (!samePath) out.print("<a href='?" + baseQueryString + "&amp;path=" + URLEncoder.encode( currPath )+ "'>");
			out.print(node.getFullName());
			if (!samePath) out.print("</a>");
			
			out.print("</div>\n");
			
			boolean inPath = filterPath != null && currPath.startsWith(filterPath);
			if (inPath || path.size() < 3) {
				Collection children = (Collection) map.get(node);
				if (children != null) {
					displayNodeTree(baseQueryString, filterPath, out, map, path, children);
				}
			}
			
			path.pop();
		}
	}
%>

<%
String query = NVL.apply(request.getParameter("q"), "").trim();
List results = search(session, query);

String filterBrand = request.getParameter("brand");
String filterPath = request.getParameter("path");

if (!results.isEmpty()) {
	results = filterByAvailability(results);
}
if (!results.isEmpty() && filterBrand != null) {
	results = filterByBrand(results, filterBrand);
}
if (!results.isEmpty() && filterPath != null) {
	results = filterByPath(results, filterPath);
}
%>

<tmpl:put name='leftnav' direct='true'>
<style>
#tree {
	margin-bottom: 1em;
}

#tree a {
	text-decoration: none;
}

#tree a:hover {
	text-decoration: underline;
}

#tree a.clear {
	color: #CC6666;
	float: right;
}

#tree .header {
	font-weight: bold;
	margin-top: 1em;
	margin-bottom: 1em;
	border-bottom: 1px solid #999;
}

#tree .Department {
	font-weight: bold;
	text-transform: uppercase;
	margin-top: 1em;
}
</style>

<%
if ( "".equals(query) || results==null || results.isEmpty() ) {
} else {
	%>
	<!--
	<div class="text11pkbold" style="border-bottom: 2px solid #ccc">
	Narrow your search
	</div>
	-->
	<div id="tree">
		<%
		SortedMap brands = collectBrands(results);
		if (!brands.isEmpty()) {
			String baseQueryString = RequestUtil.getFilteredQueryString(request, new String[] { "brand", "x", "y" });
			%>
			<div class="header">
				<%
				if (filterBrand!=null) {
					%><a title="Show All" class="clear" href="?<%= baseQueryString %>">X</a><%
				}
				%>
				Narrow by Brand
			</div>
			<%
			for (Iterator i = brands.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry e = (Map.Entry) i.next();
				%>
				<div class="Brand" style="margin-left: 8px">
					<%
					if (filterBrand == null) {
						%>
						<a href="?<%= baseQueryString %>&amp;brand=<%= e.getValue() %>"><%= e.getKey() %></a>
						<%
					} else {
						%><%= e.getKey() %><% 
					}
					%>
				</div>
				<%
			}
		}
		%>
		
		<div class="header">
			<%
			if (filterPath != null) {
				String baseQueryString = RequestUtil.getFilteredQueryString(request, new String[] { "path", "x", "y" });
				%><a title="Show All" class="clear" href="?<%= baseQueryString %>">X</a><%
			}
			%>
			Narrow by Department
		</div>
		
		<%
		Map nodeTree = com.freshdirect.webapp.util.SearchResultUtil.buildNodeTree(results);
		displayNodeTree(request, out, nodeTree);
		%>
	</div>
	<%
}
%>
</div>
</tmpl:put>

<tmpl:put name='content' direct='true'>
<style>
#searchResults {
	text-align: left;
	font-size: 8pt;
	width: 520px;
}

#searchResults td {
	vertical-align: top;
	padding: 4px;
	border-bottom: 1px solid #ccc;
}

div:hover.searchHit {
	background-color: #fafafa;
}

.thumbnail {
	width: 36px;
	height: 36px;
}


.unavailable * {
	color: #999 !important;
}

.unavailable img {
	FILTER: Gray() Alpha(Opacity=50);
	-moz-opacity: 0.5;
}

.sizeDescription {
	display: block;
	color: #666;
	margin-left: 1em;
}

.aka {
	color: #666;
}

.price {
	text-align: right;
}
</style>
<%
if ( "".equals(query) || results==null || results.isEmpty() ) {
	%>
	<%@ include file="/includes/search/search_tips.jspf"%>
	<%
} else {
	%>
	<table id="searchResults" cellspacing="0" cellpadding="0">
		<logic:iterate id="product" collection="<%= results %>" type="com.freshdirect.fdstore.content.ProductModel" length="30">
			<% displayTreeProduct(out, product); %>
		</logic:iterate>
	</table>
	<%
}
%>
</div>


</tmpl:put>
</tmpl:insert>
