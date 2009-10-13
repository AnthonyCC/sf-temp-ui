<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.fdstore.FDException"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.impl.ClassicYMALRecommendationService"%>
<%@page import="com.freshdirect.smartstore.impl.SmartYMALRecommendationService"%>
<%@page import="com.freshdirect.smartstore.ymal.YmalUtil"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.framework.util.CSVUtils"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%><%!
static class TestPair {
	static TestSupport ts = TestSupport.getInstance();

	private FDUserI user;
	
	private ProductModel product;
	
	public TestPair(FDUserI user, ProductModel product) {
		this.user = user;
		this.product = product;
	}
	
	public FDUserI getUser() {
		return user;
	}
	
	public ProductModel getProduct() {
		return product;
	}
	
	public static TestPair parse(String email, String product_id) {
		String customerId = ts.getErpIDForUserID(email); 
		FDUserI user = null; 
		if (customerId != null) {
			try {
				user = FDCustomerManager.getFDUser(new FDIdentity(customerId));
				if (user != null) {
					ProductModel product = (ProductModel) ContentFactory.getInstance().
							getContentNodeByKey(ContentKey.create(FDContentTypes.PRODUCT, product_id));
					if (product != null)
						return new TestPair(user, product);
				}
			} catch (FDException e) { }
		}
		
		return null;
	}
}
%><%

Logger LOG = LoggerFactory.getInstance("ymal_perf_test.jsp");

EnumSiteFeature siteFeature = EnumSiteFeature.YMAL;

boolean defaultFromFile = false;
boolean fromFile = defaultFromFile;
String testPairsStr = "";
List testPairs = new ArrayList();
String defaultNoOfCycles = "5";
String noOfCycles = defaultNoOfCycles;
int i_noOfCycles = 5;

if (ServletFileUpload.isMultipartContent(request)) {
	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	List items = upload.parseRequest(request);
	for (int i = 0; i < items.size(); i++) {
		FileItem item = (FileItem) items.get(i);
		if ("source".equals(item.getFieldName())) {
			String source = item.getString();
			if (source != null) {
				if (source.equals("text"))
					fromFile = false;
				else if (source.equals("file"))
					fromFile = true;
			}
			LOG.info("fromFile: " + fromFile);
		}
	}

	Iterator it = items.iterator();
	while (it.hasNext()) {
		FileItem item = (FileItem) it.next();
		LOG.info("item: " + item.getFieldName());
		if ("noOfCycles".equals(item.getFieldName())) {
			noOfCycles = item.getString();
			if (noOfCycles == null || noOfCycles.length() == 0
					|| !NumberUtils.isNumber(noOfCycles)) {
				noOfCycles = defaultNoOfCycles;
			}

			try {
				i_noOfCycles = Integer.parseInt(noOfCycles);
			} catch (NumberFormatException e) {
			}
			
			if (i_noOfCycles <= 0) {
				noOfCycles = defaultNoOfCycles;
				i_noOfCycles = 5;
			}
		} else if (!fromFile) {
			if ("testPairs".equals(item.getFieldName())) {
				testPairsStr = item.getString();
				if (testPairsStr != null && testPairsStr.trim().length() != 0) {
					StringTokenizer st = new StringTokenizer(testPairsStr, "\r\n");
					while (st.hasMoreTokens()) {
						String token = st.nextToken();
						int idx = token.indexOf(';');
						if (idx >= 0) {
							String email = token.substring(0, idx).trim();
							String product_id = token.substring(idx + 1).trim();
							TestPair tp = TestPair.parse(email, product_id);
							if (tp != null)
								testPairs.add(tp);
						}
					}
				}
			}
		} else {
			if ("fileSource".equals(item.getFieldName())) {
				try {
					Iterator rowIt = CSVUtils.rowIterator(item.getInputStream(), false, false);
					while (rowIt.hasNext()) {
						List row = (List) rowIt.next();
						LOG.info("row: " + row);
						if (row.size() < 2)
							continue;

						TestPair tp = TestPair.parse(
								((String) row.get(0)).trim(), 
								((String) row.get(1)).trim());
						if (tp != null)
							testPairs.add(tp);
					}
				} catch (Exception e) {
					
				}
			}
		}
	} 
}




// debug
LOG.info("# of cycles to simulate: " + noOfCycles);

%><%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>YMAL PERFORMANCE TEST PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

.test-page{margin:20px 60px;color:#333333;background-color:#fff;}
.test-page input{font-weight:normal;}
.test-page p{margin:0px;padding:0px;}
.test-page .bull{color:#cccccc;}
.test-page a{color:#336600;}.test-page a:VISITED{color:#336600;}
.test-page table{border-collapse:collapse;border-spacing:0px;width:100%;}
.test-page .rec-chooser{margin:0px 0px 6px;text-align:right;}
.test-page .rec-options{border:1px solid black;font-weight:bold;}
.test-page .rec-options .view-label{text-transform:capitalize;}
.test-page .rec-options table td{vertical-align: top; padding: 5px 10px 10px;}
.test-page .rec-options table td p.label{padding-bottom:4px;}
.test-page .rec-options table td p.result{padding-top:4px;}
.test-page .rec-options table div{padding-right:20px;}
table.rec-inner td {padding: 0px 2px !important; vertical-align: top !important;}
.test-page .var-comparator{margin-top:60px;}
.test-page .var-comparator td{width:50%;text-align:center;vertical-align:top;}
.test-page .var-comparator .left{padding-right:20px;}
.test-page .var-comparator .right{padding-left:20px;}
.test-page .var-comparator .var-cmp-head{margin-bottom:20px;}
.test-page .var-comparator .var-cmp-head .title14 {margin-bottom:5px;}
.test-page .prod-items {width:auto;}
.test-page .prod-items td{border:1px solid black;width:auto;padding:5px;}
.test-page .prod-items td.pic{width:100px;}
.test-page .prod-items td.info{text-align:left;vertical-align:top;}
.test-page .prod-items td.value{text-align:right;vertical-align:middle;}
.test-page .prod-items .taxonomy{font-style:italic;}
.test-page .prod-items td.info div{position:relative;height:80px;}
.test-page .prod-items .position{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;right:0px;}
.test-page .prod-items .score{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;left:0px;}
.test-page .prod-items .positive{color:#006600;}
.test-page .prod-items .negative{color:#990000;}
.test-page .prod-items .unknown{color:#FF9900;}
.not-found{color:red;}
.disabled{color:gray;font-style:italic;}
.selected{font-weight:bold;color:blue;}

	</style>
</head>
<body class="test-page">
	<form method="post" action="<%= request.getRequestURI() %>" enctype="multipart/form-data">
    <div class="rec-options" class="rec-chooser title14">
    	<table style="width: auto;">
    		<tr>
				<td>
    				<p class="label">
    					Test Pairs:
    				</p>
					<table class="rec-inner">
						<tr>
							<td>
								<p class="label">
									<input type="radio" name="source"
											value="text"<%= !fromFile ? " checked=\"checked\"" : "" %>>
								</p>
							</td>
							<td style="vertical-align: top; padding: 2px;">
			    				<p class="label" title="customer; product&lt;new line&gt; ...">
			    					From text (<span style="font-weight: normal;">customer; product&lt;new line&gt; ...</span>):
			    				</p>
								<p>
									<textarea name="testPairs" id="testPairs" rows="4" 
										cols="30"><%= testPairsStr %></textarea>
								</p>
							</td>
						</tr>
						<tr>
							<td>
			    				<p class="label result">
									<input type="radio" name="source"
											value="file"<%= fromFile ? " checked=\"checked\"" : "" %>>
								</p>
							</td>
							<td>
			    				<p class="label result">
			    					From file:
			    				</p>
								<p>
									<input type="file" name="fileSource">
								</p>
							</td>
						</tr>
					</table>
					<% if (testPairs.size() > 0) { %>
					<p class="result">
						Test pair(s) recognized:
					</p>
					<%     for (int i = 0; i < testPairs.size(); i++) {
						       TestPair tp = (TestPair) testPairs.get(i);
					%>
					<p class="result" style="font-weight: normal;">
						<%=  tp.getUser().getUserId() %> - <%= tp.getProduct().getContentKey().getId() %>
					</p>
					<%     } 
					   } else { %>
					<p class="result not-found">
						No test pairs were recognized.
					</p>
					<% } %>
				</td>
    			<td class="text12">
    				<p class="label">
    					<span
    							title="The # of test cycles the simulation will perform"
    							># of test cycles</span>
    				</p>
    				<p>
    					<input type="text" name="noOfCycles" value="<%= noOfCycles %>"
    							onfocus="this.select();"
    							title="Press &lt;Enter&gt; to activate the entered value">
    				</p>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="3">
    				<p>
    					<input type="submit" value="Submit">
    				</p>
    			</td>
    		</tr>
    	</table>
    </div>
    <table id="message" class="var-comparator"><tr><td>
    	<p class="text13 not-found" style="text-align: center;">Processing...</p>
    </td></tr></table>
    <%
    	pageContext.getOut().flush();
    
		VariantSelector vs = VariantSelectorFactory.getInstance(siteFeature);
	    List cohortNames = CohortSelector.getCohortNames();
	    RecommendationService classic = null;
	    RecommendationService smart = null;
	    Set recommenders = new HashSet(2);
	    for (int i = 0; i < cohortNames.size(); i++)
	    	recommenders.add(vs.getService((String) cohortNames.get(i)));
	    Iterator rIt = recommenders.iterator();
	    while (rIt.hasNext()) {
	    	RecommendationService rs = (RecommendationService) rIt.next();
	    	if (rs instanceof ClassicYMALRecommendationService)
	    		classic = (ClassicYMALRecommendationService) rs;
	    	else if (rs instanceof SmartYMALRecommendationService)
	    		smart = (SmartYMALRecommendationService) rs;
	    }
	    
	    // classic
    	long classicTime = Long.MIN_VALUE;
    	if (classic != null) {
	    	long start = System.currentTimeMillis();
	    	for (int i = 0; i < i_noOfCycles; i++) {
	    		Iterator it = testPairs.iterator();
	    		while (it.hasNext()) {
	    			TestPair tp = (TestPair) it.next();
	    			SessionInput input = new SessionInput(tp.getUser());
	    			input.setCurrentNode(tp.getProduct());
	    			input.setYmalSource(tp.getProduct());
	    		    YmalUtil.resetActiveYmalSetSession(input.getYmalSource(), request);
	    			input.setMaxRecommendations(6);
	    			classic.recommendNodes(input);
	    		}
	    	}
	    	long end = System.currentTimeMillis();
	    	classicTime = end - start;
    	}
    	
    	// smart
    	long smartTime = Long.MIN_VALUE;
    	if (smart != null) {
	    	long start = System.currentTimeMillis();
	    	for (int i = 0; i < i_noOfCycles; i++) {
	    		Iterator it = testPairs.iterator();
	    		while (it.hasNext()) {
	    			TestPair tp = (TestPair) it.next();
	    			SessionInput input = new SessionInput(tp.getUser());
	    			input.setCurrentNode(tp.getProduct());
	    			input.setYmalSource(tp.getProduct());
	    		    YmalUtil.resetActiveYmalSetSession(input.getYmalSource(), request);
	    			input.setMaxRecommendations(6);
	    			smart.recommendNodes(input);
	    		}
	    	}
	    	long end = System.currentTimeMillis();
	    	smartTime = end - start;
    	}
    	
    	NumberFormat format = NumberFormat.getInstance();
    	double requestCount = i_noOfCycles <= 0 || testPairs.size() == 0 ?
    			Double.POSITIVE_INFINITY :
    			i_noOfCycles * testPairs.size();
    %>
    <script type="text/javascript">
    	document.getElementById("message").style.display = "none";
    </script>
	<table class="var-comparator">
		<tr>
			<td class="left">
				<p class="title13">
					Classic YMAL runtime: <span style="font-weight: normal"><%= format.format(classicTime) %> milliseconds 
					(<%= format.format(classicTime / requestCount) %> milliseconds / request)</span> 
				</p>
			</td>
			<td class="right">
				<p class="title13">
					Smart YMAL runtime: <span style="font-weight: normal"><%= format.format(smartTime) %> milliseconds 
					(<%= format.format(smartTime / requestCount) %> milliseconds / request)</span> 
				</p>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>