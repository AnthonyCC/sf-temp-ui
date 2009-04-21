<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
<%@ page import='java.io.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.NumberFormat' %>
<%@ page import='com.freshdirect.framework.util.CSVUtils' %>
<%@ page import='com.freshdirect.framework.util.StringUtil' %>
<%@ page import='org.apache.commons.fileupload.servlet.ServletFileUpload' %>
<%@ page import='org.apache.commons.fileupload.FileItemFactory' %>
<%@ page import='org.apache.commons.fileupload.FileItem' %>
<%@ page import='org.apache.commons.fileupload.disk.DiskFileItemFactory' %>
<%@ page import="com.freshdirect.webapp.taglib.test.SmartStoreSession"%>
<%@ page import="com.freshdirect.smartstore.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.lists.*" %>
<%@ page import="com.freshdirect.fdstore.content.ContentNodeModel" %>

<%@ page import="com.freshdirect.cms.ContentKey" %>
<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes" %>

<%@ page import="com.freshdirect.fdstore.content.ProductRef" %>
<%@ page import="com.freshdirect.fdstore.content.SkuModel" %>
<%@ page import="com.freshdirect.fdstore.content.ProductModel" %>
<%@ page import="com.freshdirect.fdstore.content.AvailabilityI" %>
<%@ page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil" %>

<%@ page import="com.freshdirect.fdstore.FDProductInfo" %>
<%@ page import="com.freshdirect.fdstore.FDCachedFactory" %>
<%@ page import="com.freshdirect.fdstore.FDProduct" %>
<%@ page import="com.freshdirect.fdstore.FDSku" %>
<%@ page import="com.freshdirect.fdstore.FDConfiguration" %>
<%@ page import='com.freshdirect.webapp.taglib.test.SmartStoreSession' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.content.ContentNodeModelUtil" %>
<%

String action = request.getParameter("action");
String errorMsg = null;
Exception trouble = null;


List customers = new ArrayList();

List cartLineItems = new ArrayList();

%><%!

public static class ProdFreqCouple {
	public ProductModel product;
	public int frequency;
	public ProdFreqCouple(ProductModel prd, int frq) {
		this.product = prd;
		this.frequency = frq;
	}
}


%><%!

String getSkuStyle(Object sku, List cart) {
	String style = "";
	// unavailable
	if (sku instanceof AvailabilityI && ((AvailabilityI)sku).isUnavailable()) {
		style += "text-decoration: line-through; ";
	}
	// already in cart
	if (cart != null && cart.contains(sku)) {
		style += "color: gray; ";
	}
	
	return style;
}

%><%!

// Copied from FDCartLineDAO.java
HashMap convertStringToHashMap(String configuration) {
	StringTokenizer st = new StringTokenizer(configuration, ",");
	HashMap ret = new HashMap();
	while (st.hasMoreTokens()) {
		String token = st.nextToken().trim();
		int idx = token.indexOf("=");
		String key = token.substring(0, idx++);
		String value = token.substring(idx, token.length());
		ret.put(key, value);
	}

	return ret;
}

%><%!
// Creates cart line from SKU code and configuration parameters
//
FDCartLineModel createCartLine(String skuCode, double quantity, String salesUnit, Map options) throws FDSkuNotFoundException, FDResourceException {
	FDCartLineModel line = null;
	try {
		FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
		FDProduct fdProd = FDCachedFactory.getProduct(skuCode, productInfo.getVersion() );
		ContentKey ckey = new ContentKey(FDContentTypes.SKU,skuCode);
		SkuModel skuModel = new SkuModel(ckey);

		FDConfiguration conf = new FDConfiguration(quantity,salesUnit,options);

		if (skuModel.getProductModel() != null) {
			line = new FDCartLineModel(new FDSku(fdProd),skuModel.getProductModel().getProductRef(),conf,null);
		} else {
			System.err.println("[" + skuCode + "]-> skuModel.getProductModel() = NULL, Skipping ...");
		}
	} catch (FDSkuNotFoundException sku_exc) {
		System.err.println("SKU not found " + sku_exc);
		return null;
	} catch (Exception exc) {
		System.err.println("General exception: " + exc + ", SKU=" + skuCode);
		exc.printStackTrace();
		return null;
	}
	
	return line;
}

%><%!

// collect SKUs and create cart line items from CSV input
//
void processCartLinesFromCSV(File inpFile, Long customerID, List cartLineItems) {
	if (customerID == null)
		return;
	
	FileInputStream is = null;
	try {
		// Build map from input file
		is = new FileInputStream(inpFile);
		List results = CSVUtils.parse(is, false, false);
		Iterator it = results.iterator();
		while (it.hasNext()) {
			List row = (List) it.next();
			// load cust_sku PKs to cust_sku array
			Long custID = new Long((String)row.get(0));
			
			// process only items belonging to a particular customer - if given
			if (customerID != null && !customerID.equals(custID))
				continue;

			String SKU_ID = (String)row.get(1);
			
			double q = (new Double((String) row.get(2))).doubleValue();
			
			// FDConfiguration conf = new FDConfiguration(q, (String) row.get(3), convertStringToHashMap((String) row.get(4)));
			String options = (String) row.get(4);
			Map optMap = Collections.EMPTY_MAP;
			
			// "-" String means empty options
			if (options != null && !"-".equalsIgnoreCase(options)) {
				optMap = convertStringToHashMap(options);
			}
			
			// create cart line from input
			FDCartLineModel cartLine = createCartLine(SKU_ID, q, (String) row.get(3), optMap);
			
			if (cartLine != null) {
				cartLineItems.add(cartLine); 
			}
		}
	} catch (Exception exc) {
		System.err.println("Trouble striked into processCSVInputFile; exc=" + exc);
	}
}

%><%



// Check work folder
//
File SSdir = new File("work" + File.separatorChar + "smartstore");
if (!SSdir.exists()) {
	// go to upload page
	response.sendRedirect("dyf.jsp");
}


// Check input CSV file
File inpFile = new File(SSdir, "cust_sku_input.csv");
if (!inpFile.exists()) {
	// go to upload page
	response.sendRedirect("dyf.jsp");
}

if ("recommend".equals(action)) {
	
} else {
	// default action
	
	// Load CSV file in
	//
	FileInputStream is = null;
	try {
		// Build map from input file
		is = new FileInputStream(inpFile);
		List results = CSVUtils.parse(is, false, false);
		Iterator it = results.iterator();
		while (it.hasNext()) {
			List row = (List) it.next();
			// load cust_sku PKs to cust_sku array
			Long custID = new Long((String)row.get(0));
			String SKU_ID = (String)row.get(1);

			if (!customers.contains(custID)) {
				customers.add(custID);
			}
		}
	} catch (Exception exc) {
		trouble = exc;
	}
}

%>

<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<% if ("recommend".equals(action)) { %>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<% } else { %>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<% } %>
	<title>SmartStore DYF Test Page</title>
	<style>
		body {
			margin: 0;
			padding: 0;
		}
		
		.text1 {
			font-family: Arial,Helvetica,sans-serif;
			font-size: 24px;
			font-weight: bold;
		}
		
		#result-table td {
			font-size: 12px;
		}
	</style>
</head>
<body>
<%
if (errorMsg != null ) { %>
	<div style="padding: 3px 3px; background-color: red; color: white; font-weight: bold" id="debug">ERROR: <%= errorMsg %></div>
<%
}
if (trouble != null) {
%>
	<span style="width: 40px; height: 40px; background-color: red; text-align: center; vertical-align: middle; color: white;" onclick="var x = document.getElementById('exception'); x.style.display = (x.style.display=='none'?'':'none');"><b>+</b></span>
	<span style="color: red; font-weight: bold">EXCEPTION: <%= trouble.getMessage() %></span><br/>
	<div style="font-size: 10px; border: 1px solid red; padding: 5px 5px; display: none;" id="exception">
		<pre>
<%
		trouble.printStackTrace(new PrintWriter(out));
%>
		</pre>
	</div>
<%
}



if ("recommend".equals(action)) {
	FDStoreRecommender recommender = FDStoreRecommender.getInstance();

	Long custID = new Long((String) request.getParameter("customer"));
	HttpSession sess = request.getSession();
	SmartStoreSession mockSession = new SmartStoreSession(request.getParameter("customer"), sess);

	// process cart line items from CSV
	//
	processCartLinesFromCSV(inpFile, custID, cartLineItems);

	// create fake user
	//
	FDUserI looser = (FDUserI)mockSession.getAttribute("fd.user");
	FDCartModel model = looser.getShoppingCart();
	model.clearOrderLines();

	// add cart line items to customer
	//
	model.addOrderLines(cartLineItems);

	
	// recommend!
	//
	SessionInput sessInp = new SessionInput(looser);
	sessInp.setMaxRecommendations(6);
	Recommendations recommendations = recommender.getRecommendations(EnumSiteFeature.DYF, looser, sessInp, null);
%>
<center>
	<span style="font-size: 11px;"><b>Legend</b> <span style="text-decoration: line-through; border: 1px dotted grey;">Unavailable product</span> <span style="color: gray; border: 1px dotted grey;">Product already in cart</span></span> | 
	<a href="<%= request.getRequestURI() %>?action=recommend&customer=<%= request.getParameter("customer") %>">New suggestions</a><br/>
	<br/>
</center>
<table id="result-table" border="0">
	<thead>
		<th>Cart</th>
		<th>Every Item Ever Ordered</th>
		<th>Suggestions / <%= recommendations.getVariant().getId() %></th>
	</thead>
	<tr>
	</tr>
	<tr>
<%
	ArrayList ProdsInCart = new ArrayList(cartLineItems.size());
	

	// aggregate SKUs to products
	Iterator it = cartLineItems.iterator();
	while (it.hasNext()) {
		FDCartLineModel clItem = (FDCartLineModel) it.next();
		SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(clItem.getSkuCode());
		ProductModel prd = sku.getProductModel();

		if (!ProdsInCart.contains(prd)) {
			ProdsInCart.add(prd);
		}
	}
%>
		<td width="33%" valign="top">
<%
	// enumerate products found in cart
	it = ProdsInCart.iterator();
	while (it.hasNext()) {
		ProductModel prd = (ProductModel) it.next();

		%>			<span style="<%= getSkuStyle(prd, null) %>"><%= prd.getFullName() %></span><br/>
<%
	}
%>
		</td>
		<td width="33%" valign="top">
<%
	List lineItems = FDListManager.getEveryItemEverOrdered(new FDIdentity( (String) request.getParameter("customer") ));
	Set pEIEO = new TreeSet(new Comparator() {
		public int compare(Object o1, Object o2) {
			ProdFreqCouple sel1 = (ProdFreqCouple)o1;
			ProdFreqCouple sel2 = (ProdFreqCouple)o2;
			
			int distance  = sel2.frequency - sel1.frequency;
			if (distance == 0) {
				return sel1.product.getFullName().compareTo(sel2.product.getFullName());
			}
			
			return distance;
		}
	});

	// aggregate sku frequencies
	//   prdKey -> {prd,freq}
	HashMap aggregator = new HashMap();
	
	it = lineItems.iterator();
	while (it.hasNext()) {
		FDProductSelectionI sel = (FDProductSelectionI) it.next();

		int frq = sel.getStatistics().getFrequency();
		ProductModel prd = sel.lookupProduct();
		ContentKey key = prd.getContentKey();
		
		ProdFreqCouple obj = null;
		if (aggregator.keySet().contains(key)) {
			((ProdFreqCouple)aggregator.get(key)).frequency += frq;
		} else {
			aggregator.put(key, new ProdFreqCouple(prd, frq));
		}
	}

	// sort products by their aggregated frequencies
	pEIEO.addAll(aggregator.values());

	// display products
	it = pEIEO.iterator();
	while (it.hasNext()) {
		ProdFreqCouple item = (ProdFreqCouple) it.next();

		%>			<i>(<%= item.frequency %>)</i> <span style="<%= getSkuStyle(item.product, ProdsInCart) %>"><%= item.product.getFullName() %></span><br/>
<%
	}
%>
		</td>
		<td width="33%" valign="top">
<%
	if (recommendations.getProducts().size() > 0) {
		Iterator r_it = recommendations.getProducts().iterator();
		while (r_it.hasNext()) {
			ProductModel item = (ProductModel) r_it.next();
			%>			<%= item.getFullName() %><br/>
<%
		}
	} else {
		%>
			<center><i>No suggestions</i></center><br/>
<%
	}


%>
		</td>
	</tr>
</table>
<%
} else {
%>

<div style="padding: 20px 20px;">

	<form action="dyf_test.jsp" method="GET" target="details">
		<input type="hidden" name="action" value="recommend"/>
		<span class="text1">Choose a Customer:</span><select id="c_chooser" name="customer" onchange="if (this.value != '-----'){ document.getElementById('details-iframe').style.display = ''; document.forms[0].submit(); return false;};">
			<option name="cust_none" selected="1">-----</option>
<%
	Iterator it = customers.iterator();
	while (it.hasNext()) {
		Long cust_pk = (Long) it.next();
		%>		<option name="cust_<%= cust_pk %>"><%= cust_pk %></option>
<%
	}
%>
		</select>
	</form>
</div>
<iframe id="details-iframe" name="details" style="border: 0; width: 99%; height: 600px; display: none; border-left: 4px dotted green">
</iframe>
<%
} // !if ("recommend".equals(action))
%>
</body>
</html>
