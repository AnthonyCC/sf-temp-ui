<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="java.net.URLEncoder" %>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
	String successPage = "/request_product.jsp?"+request.getQueryString();
	String department = request.getParameter("department");

	if("wine".equalsIgnoreCase(department)) {
		%><jsp:forward page='<%="/request_wine.jsp?"+request.getQueryString()%>' /><%
	}

	String redirectPage = "/login/login_popup.jsp?successPage=" + successPage;
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

	int prodRequests = 3;

%>
<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Request a Product</tmpl:put>
		<tmpl:put name='content' direct='true'>


<%@ include file="/includes/search/brandautocomplete.jspf" %>
<script src="/assets/javascript/prototype.js" type="text/javascript" language="javascript"></script>
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

</script>
<script language="JavaScript">
<!--
	function linkTo(url){
		redirectUrl = "http://" + location.host + url;
		parent.opener.location = redirectUrl;
	}
	
		while(!initProdReq());
//-->
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

<%@ include file="/includes/product/i_request_product_dept_cat_map.jspf" %>

<fd:RequestAProductNewTag actionName="requestProducts" result="result" successPage="request_product_conf.jsp">

	<form name="request_product" method="post">
	<table width="520" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td colspan="4" class="text12"><img src="/media_stat/images/template/newproduct/request_product.gif" width="202" height="17" alt="Request A Product" /><br /><img src="/media_stat/images/layout/clear.gif" width="1" height="14" alt="" />We try to offer our customers the best selection of fresh foods as well as the most popular packaged brands. Your product requests will help us make FreshDirect a better place to shop and we read every request submitted by our customers.<br />
		<br /><strong>Please describe the products you'd like in as much detail as possible including specific brands, sizes, and flavors. </strong>
		</td>
	</tr>
	<tr>
		<td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="20" height="20" alt="" /></td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="60" height="1" alt="" /></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" /></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="200" height="1" alt="" /></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="200" height="1" alt="" /></td>
	</tr>
	<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="12" alt="" /></td></tr>
<% for (int n=1; n<=prodRequests; n++) { %>
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
			<script type="text/javascript">
			<%--
				Remove all these java comments to re-enable brand autocomplete
			--%>
			<!--
				depts.populateDeptsList("dept_prod<%= n %>");
				YAHOO.util.Event.onDOMReady(autoCompleteFunctionFactory("/api/brandautocompleteresults.jsp","brands_prod<%= n %>","brandParams_prod<%= n %>",false));
			//-->
			</script>
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
		<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="12" alt="" /></td></tr>
		<tr><td colspan="4" bgcolor="#ccc" height="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td></tr>
		<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="12" alt="" /></td></tr>
	<% } %>
<% } %>
	<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="24" alt="" /></td></tr>
	<tr>
		<td colspan="4" align="center"><a href="javascript:document.request_product.reset(); fillBrandVal(); disCats();"><img src="/media_stat/images/template/newproduct/b_clear.gif" width="47" height="17" border="0" alt="Clear" /></a>&nbsp;&nbsp;<input type="image" name="send_email" src="/media_stat/images/template/newproduct/b_send.gif" width="45" height="15" vspace="1" border="0" alt="Send Request"><br /><img src="/media_stat/images/layout/clear.gif" width="1" height="12" alt="" /><br>
		</td>
	</tr>
	<tr><td colspan="4" bgcolor="#999966" height="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td></tr>
	</table>
	
	<script language="JavaScript"><!--
		document.request_product.dept_prod1.focus();
		fillBrandVal();
		
		//alphabetize
		depts.alphaLists();
	//-->
	</script>
	</form>

</fd:RequestAProductNewTag>
	</tmpl:put>
</tmpl:insert>