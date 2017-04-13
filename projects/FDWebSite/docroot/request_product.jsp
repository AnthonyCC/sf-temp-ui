<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.webapp.ajax.product.ProductRequestServlet" %>
<%@ page import="com.freshdirect.fdstore.request.FDProductRequest" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page buffer="64kb" autoFlush="false" %>
<%
	String successPage = "/request_product.jsp" + ((request.getQueryString() != null) ? "?"+request.getQueryString() : "" );
	String department = request.getParameter("department");

	if("wine".equalsIgnoreCase(department)) {
		%><jsp:forward page='<%="/request_wine.jsp?"+request.getQueryString()%>' /><%
	}

	String redirectPage = "/login/login_popup.jsp?successPage=" + successPage;
	Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");

	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity customerIdentity = null;
	ErpCustomerInfoModel customerInfo = null;
	if (user!=null && user.getLevel() == 2) {
		customerIdentity = user.getIdentity();
		customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);
	}

	int prodRequests = 3; //this number is also in ProductRequestServlet

	/* this is saved reqs. this is populated if a save failed (and cleared on a success) */
	List<FDProductRequest> pendingProductRequests = (List<FDProductRequest>)session.getAttribute(ProductRequestServlet.STOREDPRODUCTREQUESTS);

	Map mediaParams = new HashMap(); //params to send to media
	mediaParams.put("baseUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort());
%>

<tmpl:insert template='/shared/template/large_pop.jsp'>


	<tmpl:put name='title' direct='true'>FreshDirect - Request a Product</tmpl:put>
		<tmpl:put name='content' direct='true'>


<%@ include file="/includes/search/brandautocomplete.jspf" %>

<%@ include file="/common/template/includes/i_javascripts.jspf" %>  
<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>

<%if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){%>
	<script type="text/javascript">
	tcAgreewindow=doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'320px\' height=\'400px\' frameborder=\'0\' ></iframe>');
	</script>
<%}%>

<script type="text/javascript">

	function deptsObj() {
		this.debug = false;	// debug mode

		this.log = function () {
			var logMsgs = new Array();
			for (var i = 0; i < arguments.length; i++) {
				logMsgs.push(arguments[i]);
			}
			var logMsg = logMsgs.join(",");

			if (logMsg.constructor == Array) { console.log("logMsg is arr"); logMsg = logMsg.join(","); }
			var time = new Date();
			var timeNow = time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+"."+time.getMilliseconds();
			if (this.debug) {
				if (window.console) {
					console.log(timeNow+' Log: '+logMsg);
				}
			}
		}

		this.deptsList = new Array(0);

		this.addDept = function(deptId, name) {
			if (!this.deptsList[deptId]) {
				this.deptsList[deptId] = new Array(0, new Array());
			}
			this.deptsList[deptId][0] = name;
			
			this.log("added dept", deptId, name);
		}

		this.addCatToDept = function (deptId, catId, name) {
			if (!this.deptsList[deptId]) {
				this.deptsList[deptId] = new Array(0, new Array());
				this.log("addToCat, dept didn't exist", deptId);
			}
			if (this.deptsList[deptId][catId]) {
				this.log("addToCat, cat already exists?", catId);
			}else{
				this.deptsList[deptId][1][catId] = name;
				this.log("addCatToDept", deptId, catId, name);
			}
		}
		
		this.getDepts = function() {
			var deptsList = new Array();
			for (dept in this.deptsList) {
				if (typeof(dept) != "string" || typeof(this.deptsList[dept][0]) != "string") { continue; }
				deptsList.push(dept, this.deptsList[dept][0]);
			}
			this.log(deptsList);

			return deptsList;
		}

		this.getCatsFromDept = function(deptId) {
			if (this.deptsList[deptId]) {
				var catsList = new Array();
				//fetch cats under this id
				for (cat in this.deptsList[deptId][1]) {
					if (typeof(cat) != "string" || typeof(this.deptsList[deptId][1][cat]) != "string") { continue; }
					catsList.push(cat, this.deptsList[deptId][1][cat]);
				}
				this.log(typeof(this.deptsList[deptId][1][cat]));

				this.log("getCatsFromDept", "fetching cats", this.deptsList[deptId][1]);
				//this.log("getCatsFromDept", "fetching cats", deptId, catsList);}
			}else{
				this.log("getCatsFromDept", "deptId does not exist?", deptId);
			}

			return catsList;
		}

		this.populateDeptsList = function (deptsListId) {
			var deptsList = this.getDepts();
			//clean list
			YAHOO.util.Dom.get(deptsListId).options.length = 0;

			var optCount = 0;
			var optTexts = new Array();
			var optValues = new Array();

			for (i=0; i < deptsList.length; i++) {
				
				if (i%2) {
					optTexts[optTexts.length] = deptsList[i];
				}else{
					optValues[optValues.length] = deptsList[i];
				}

			}

			for (i=0; i < optTexts.length; i++) {
				YAHOO.util.Dom.get(deptsListId).options[YAHOO.util.Dom.get(deptsListId).options.length] = new Option(optTexts[i], optValues[i]);
			}

		}

		this.populateCatsList = function (catsListId, deptsListId) {
			//enable cat now that we have selections
			YAHOO.util.Dom.get(catsListId).disabled = false;
			if (YAHOO.util.Dom.get(deptsListId).value == 'D_NULL') { YAHOO.util.Dom.get(catsListId).disabled = true; }

			var deptId = YAHOO.util.Dom.get(deptsListId).value;

			var catsList = this.getCatsFromDept(deptId);

			this.log(catsList);

			//clean list
			YAHOO.util.Dom.get(catsListId).options.length = 0;

			var optCount = 0;
			var optTexts = new Array();
			var optValues = new Array();

			for (i=0; i < catsList.length; i++) {
				
				if (i%2) {
					optTexts[optTexts.length] = catsList[i];
				}else{
					optValues[optValues.length] = catsList[i];
				}

			}

			for (i=0; i < optTexts.length; i++) {
				YAHOO.util.Dom.get(catsListId).options[YAHOO.util.Dom.get(catsListId).options.length] = new Option(optTexts[i], optValues[i]);
			}

			//alphabetize
			this.alphaLists(catsListId);
		}

		this.alphaLists = function(specId) {
			var specId = specId || '';
			
			//alphabetize
			var selectArr = new Array();

			if (specId!='') {
				selectArr[0] = YAHOO.util.Dom.get(specId); 
			}else{
				selectArr = document.getElementsByTagName('select'); 
			}

			for (var i = 0; i < selectArr.length; i++) { 
				var oArr = []; 
				// Get the options for the select element 
				for (var j = 0; j < selectArr[i].options.length; j++) { 
					// Store this as an object that has an option object member, and a toString function (which will be used for sorting) 
					oArr[oArr.length] = { 
						option : selectArr[i].options[j], 
						toString : function() { 
							// Return the text of the option, not the value 
							return this.option.text; 
						}
					}
				} 
				// Sort the array of options for this select 
				oArr.sort();
				

				// Remove all options from the select
				selectArr[i].options.length = 0;

				this.log("selectArr[i].id", i, selectArr[i].id);
				//add choose...
				if ( (selectArr[i].id).indexOf('dept_prod') > -1 ) {
					YAHOO.util.Dom.get(selectArr[i].id).options[YAHOO.util.Dom.get(selectArr[i].id).options.length] = new Option("Choose Department", "D_NULL");
				}
				if ( (selectArr[i].id).indexOf('cat_prod') > -1 ) {
					YAHOO.util.Dom.get(selectArr[i].id).options[YAHOO.util.Dom.get(selectArr[i].id).options.length] = new Option("Choose Category", "C_NULL");
				}
				// Rebuild the select using our sorted array
				for (var j = 0; j < oArr.length; j++) {
					selectArr[i].options[j+1] = oArr[j].option;
				}
				selectArr[i].selectedIndex = 0;
			}

			
			//add default dept and cat
			this.addDept("D_NULL", "Choose Department");
			this.addCatToDept("D_NULL", "C_NULL", "Choose Category");

		}

		return true;

	}

	function initProdReq() {
		window['depts'] = new deptsObj();
		return true;
	}

	function fillVal(fromId, fillWith) {
		var fillWith = fillWith || '';
		if (fillWith == '') {
			if (YAHOO.util.Dom.get(fromId).value == 'Brand' || YAHOO.util.Dom.get(fromId).value == 'Description') {
				YAHOO.util.Dom.get(fromId).value = '';
			}
		} else if (fillWith == 'D' && YAHOO.util.Dom.get(fromId).value == '') {
			YAHOO.util.Dom.get(fromId).value = 'Description';
		} else if (fillWith == 'B' && YAHOO.util.Dom.get(fromId).value == ''){
			YAHOO.util.Dom.get(fromId).value = 'Brand';
		}
	}

	function fillBrandVal() {
		for (var n=1; n<=<%=prodRequests%>; n++) {
			YAHOO.util.Dom.get('brandParams_prod'+n).value = 'Brand';
		}
	}

	function disCats() {
		for (var n=1; n<=<%=prodRequests%>; n++) {
			YAHOO.util.Dom.get('cat_prod'+n).disabled = true;
		}
	}
	
	var pendingRequests = [];
	function setPendingRequest(index, dept, cat, brand, descrip) {
		$jq('#dept_prod'+index).val(dept).change();
		$jq('#cat_prod'+index).val(cat).change();
		$jq('#brandParams_prod'+index).val(brand);
		$jq('#descrip_prod'+index).val(descrip);
	}

	while(!initProdReq());
	
	$jq(document).ready(function() {
		
		$jq.getJSON( "/api/productrequest/", function( data ) {
			window['prodReqData'] = data; //we need this for setPendingRequest 
			
			for (var cur in data) {
				if (data[cur].type === 'MAP') {
					var ids = cur.split('|'); //dept,cat
					depts.addDept(data[ids[0]].id, data[ids[0]].name);
					depts.addCatToDept(data[ids[0]].id, data[ids[1]].id, data[ids[1]].name);
				}
			}
			
			<% for (int n=1; n<=prodRequests; n++) { %>
				<%--
					Remove all these java comments to re-enable brand autocomplete
				--%>
				depts.populateDeptsList("dept_prod<%= n %>");
				YAHOO.util.Event.onDOMReady(autoCompleteFunctionFactory("/api/brandautocompleteresults.jsp","brands_prod<%= n %>","brandParams_prod<%= n %>",false));
			<% } %>
			
			
			document.request_product.dept_prod1.focus();
			fillBrandVal();
			
			//alphabetize
			depts.alphaLists();
			
			for (var i = 0; i < pendingRequests.length; i++) {
				setPendingRequest.apply(null, pendingRequests[i]);				
			}
		});

		$jq('#prodReq_clear').on('click', function(e) {
			document.request_product.reset();
			fillBrandVal();
			disCats();
			
			return false;
		});
		
		$jq('#prodReq_send').on('click', function(e) {
			$jq('#prodReqError').html('');
			
			$jq.post("/api/productrequest/", $jq('#request_product').serialize(), function(data) {
				if (data.SUCCESS) {
					$jq('#prodReq_cont_submit').hide(); //main content
					$jq('#prodReq_cont_submit_success').show(); //success msg
					
					if (parent.opener && !(parent.opener['shouldClearProdReqSelection'] == null || parent.opener['shouldClearProdReqSelection'] == 'undefined')) {
						parent.opener['shouldClearProdReqSelection'] = true;
					}
				} else if (data.hasOwnProperty('ERRORS')) {
					//needs loop
					for (var i = 0; i < data.ERRORS.length; i++) {
						switch (data.ERRORS[i].type) {
							case '<%= ProductRequestServlet.ERR_NOCUSTOMERID %>':
								<%-- user needs to log in, redirect them there --%>
								window.location = '<%= redirectPage %>';
								break;
							case '<%= ProductRequestServlet.ERR_NOREQUEST %>':
							case '<%= ProductRequestServlet.ERR_NOSAVE %>':
								$jq('#prodReqError').append('<div>'+data.ERRORS[i].description+'</div>');
								break;
						}
					}
				}
			});
			return false;
		});
	});
</script>
<style>
	.brandsAC ul {
		margin: 0px;
		padding: 0px;
	}
	.brandsAC li {
		list-style-type: none;
		text-indent: -8px;
		padding: 2px;
		padding-left: 10px;
		cursor: default ! important;
	}
	
	.yui-ac-bd {
		width: 204px;
		border: 1px solid black;
		border-top: none;
	}

	.selectFree { z-index: 2000; }

	<%--
		Here's the original css:

		.selectFree IFRAME {
			display:none;/*sorry for IE5*/ 
			display/**/:block;/*sorry for IE5*/
			position:absolute;/*must have*/
			top:0;/*must have*/
			left:0;/*must have*/
			z-index:-1;/*must have*/
			filter:mask();/*must have*/
			width:2000px;/*must have for any big value*/
			height:3000px/*must have for any big value*/;
		}
	--%>
	.selectFree IFRAME {
		display:none;
		display/**/:block;
		position:absolute;
		top:0;
		left:0;
		z-index:-1;
		filter:mask();
		width:2000px;
		height:3000px;
	}

</style>
	<div id="prodReq_cont_submit">
		<form name="request_product" id="request_product" method="post">
			<input type="hidden" id="action" name="action" value="requestProducts" />
			
			<table width="520" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td colspan="4">
					
						<fd:IncludeMedia name="/media/editorial/site_pages/product_requests/top.ftl" parameters="<%= mediaParams %>" withErrorReport="false">
							<div class="text12">
								<span class="title18or">REQUEST A PRODUCT</span><br />
								<br />
								We try to offer our customers the best selection of fresh foods as well as the most popular packaged brands. Your product requests will help us make 
								FreshDirect a better place to shop and we read every request submitted by our customers.<br />
								<br />
								<strong>Please describe the products you'd like in as much detail as possible including specific brands, sizes, and flavors. </strong>
							</div>
						</fd:IncludeMedia>
					</td>
				</tr>
				<tr>
					<td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="20" height="20" alt="" /></td>
				</tr>
				<tr>
					<td width="60"></td>
					<td width="20"></td>
					<td width="200"></td>
					<td width="200"></td>
				</tr>
			<% for (int n=1; n<=prodRequests; n++) { %>
				<%
					if (pendingProductRequests != null && pendingProductRequests.size() > n-1) {
						FDProductRequest curPendingRequest = pendingProductRequests.get(n-1);
						if (curPendingRequest != null) {
							%><script>pendingRequests.push([<%= n %>, '<%= curPendingRequest.getDept() %>', '<%= curPendingRequest.getCategory() %>', '<%= curPendingRequest.getSubCategory() %>', '<%= curPendingRequest.getProductName() %>']);</script><%
						}
					}
				%>
				<% int z = (prodRequests-n)*1000+5; %>
				<tr>
					<td align="right" rowspan="2"><strong>Request<br /> #<%= n %><strong></td>
					<td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" /></td>
					<td>
						<select id="dept_prod<%= n %>" name="dept_prod<%= n %>" class="text11" onchange="depts.populateCatsList('cat_prod<%= n %>', this.id);" style="width: 206px;">
						</select>
					</td>
					<td>
						<select id="cat_prod<%= n %>" name="cat_prod<%= n %>" class="text11" style="width: 206px;" disabled="true">
						</select>
					</td>
				</tr>
				<tr>
					<td style="padding-top: 12px;">
						<div id="brandSearchContainer_prod<%= n %>" style="position: relative; z-index: <%= z %>;">
							<input type="text" id="brandParams_prod<%= n %>" name="brandParams_prod<%= n %>" value="" style="width: 200px;" maxlength="50" class="text11" onfocus="fillVal(this.id);" onblur="fillVal(this.id, 'B');" value="Brand" />
							<div id="brands_prod<%= n %>" name="brands_prod<%= n %>" style="position: absolute;background-color: white" class="brandsAC selectFree"><!--[if lte IE 6.5]><iframe></iframe><![endif]--></div>
						</div>
			
					</td>
					<td style="padding-top: 12px;">
						<input id="descrip_prod<%= n %>" name="descrip_prod<%= n %>" style="width: 200px;" maxlength="255" class="text11" onfocus="fillVal(this.id);" onblur="fillVal(this.id, 'D');" value="Description" />
					</td>
				</tr>
				<% if (n<prodRequests) {%>
					<tr><td colspan="4"><hr style="margin: 12px 0; background-color: #ccc;" /></td></tr>
				<% } %>
			<% } %>
				<tr><td colspan="4">
					<div style="text-align: center; margin: 20px 0;">
						<button class="cssbutton small green transparent" id="prodReq_clear">CLEAR</button>
						<button class="cssbutton small orange" id="prodReq_send">SEND</button>
					</div>
					<div id="prodReqError" class="text12 error" style="height: 40px; margin-top: 20px;"></div>
				</td></tr>
			</table>
		</form>
	</div>
	
	<div id="prodReq_cont_submit_success" style="display: none;">
		<fd:IncludeMedia name="/media/editorial/site_pages/product_requests/success.ftl" parameters="<%= mediaParams %>" withErrorReport="false">
			<script>
				$jq(document).ready(function() {
					$jq('.closeLink').on('click', function(e) { window.top['FreshDirect'].components.ifrPopup.close(); });
					return false;
				});
			</script>
			<table width="520" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td align="center" class="text12">
						<div style="font-size: 48px; font-face:Arial,Verdana,Helvetica; font-weight: bold; padding: 12px 0">THANK YOU.</div>
						<% if ("wine".equalsIgnoreCase(request.getParameter("department"))) { %>
							<div class="text12"><img src="/media_stat/images/template/newproduct/wine_request_img.jpg"><br /><br /><strong>Your feedback is important to helping us improve.</strong>
							<br />To continue shopping, <a href="#" class="closeLink"><b>click here</b></a> to close this window.<br /><br /></div>
						<% } else { %>
							<div class="text12">We will do our best to add to our selection based on your requests.<br />To continue shopping <a href="#" class="closeLink">close this window</a> or <a href="#" onClick="javascript:backtoWin('/newproducts.jsp'); window.top['FreshDirect'].components.ifrPopup.close();">click here to see our New Products!</a></div>
							<div style="margin: 36px;"><img src="/media_stat/images/template/newproduct/confirm_berry.jpg" width="70" height="70"></div>
						<% }%>
					</td>
				</tr>
			</table>
		</fd:IncludeMedia>
	</div>

	</tmpl:put>
</tmpl:insert>
