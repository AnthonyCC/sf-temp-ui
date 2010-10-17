<%@ page import="com.freshdirect.framework.util.NVL" %>
<%
	/*
		Things that need to be added:

			String prefixURI = ""; //prefix for URLs (see: productImageTag.java)
			String emailStyle = ""; //style to pass into ProductName tags
			MULTI
	 */

	String dyn_catId = NVL.apply(request.getParameter("dyn_catId"), "");

	String dyn_min = NVL.apply(request.getParameter("dyn_min"), "");

	String dyn_perRow = NVL.apply(request.getParameter("dyn_perRow"), "");
	String dyn_rows = NVL.apply(request.getParameter("dyn_rows"), "");

	//String dyn_from = NVL.apply(request.getParameter("dyn_from"), "");
	String dyn_depth = NVL.apply(request.getParameter("dyn_depth"), "");
	String dyn_context = NVL.apply(request.getParameter("dyn_context"), "");
	String dyn_sortBy = NVL.apply(request.getParameter("dyn_sortBy"), "");
	
	String dyn_add = NVL.apply(request.getParameter("dyn_add"), "");
	String dyn_mediaPath = NVL.apply(request.getParameter("dyn_mediaPath"), "/media/testing/dynamic_rows/");

	String dyn_isTx = NVL.apply(request.getParameter("dyn_isTx"), "def");
	String dyn_deals = NVL.apply(request.getParameter("dyn_deals"), "def");
	String dyn_feats = NVL.apply(request.getParameter("dyn_feats"), "def");

	String dyn_newOnly = NVL.apply(request.getParameter("dyn_newOnly"), "def");
	String dyn_bisOnly = NVL.apply(request.getParameter("dyn_bisOnly"), "def");

	String dyn_descrips = NVL.apply(request.getParameter("dyn_descrips"), "def");

	String dyn_exPer = NVL.apply(request.getParameter("dyn_exPer"), "def");
	String dyn_exFro = NVL.apply(request.getParameter("dyn_exFro"), "def");
	String dyn_exDisc = NVL.apply(request.getParameter("dyn_exDisc"), "def");

	String dyn_uniqG = NVL.apply(request.getParameter("dyn_uniqG"), "def");
	String dyn_uniqR = NVL.apply(request.getParameter("dyn_uniqR"), "def");

	String dyn_igShowCh = NVL.apply(request.getParameter("dyn_igShowCh"), "def");

	String dyn_retHidFolds = NVL.apply(request.getParameter("dyn_retHidFolds"), "def");
	String dyn_retSecFolds = NVL.apply(request.getParameter("dyn_retSecFolds"), "def");

	String dyn_dynImg = NVL.apply(request.getParameter("dyn_dynImg"), "def");
	String dyn_shortBools = NVL.apply(request.getParameter("dyn_shortBools"), "def");

	String dyn_showNewText = NVL.apply(request.getParameter("dyn_showNewText"), "def");

	String dyn_rowList = NVL.apply(request.getParameter("dyn_rowList"), "");
	
	String templatePath = "/common/template/no_space_border.jsp"; //the default

%>
<%@ include file="/includes/i_dynamicRows_required.jspf" %>
<%
	
	String dyn_myDebugPrint = NVL.apply(request.getParameter("dyn_myDebugPrint"), "false");
		if ("on".equalsIgnoreCase(dyn_myDebugPrint)) { dyn_myDebugPrint = "true"; }
		myDebugPrint = Boolean.valueOf(dyn_myDebugPrint);
		myDebug = myDebugPrint;
%>
<fd:CheckLoginStatus />
<tmpl:insert template='<%= templatePath %>'>
<tmpl:put name='title' direct='true'>Dynamic Rows Test Page</tmpl:put>
<tmpl:put name='content' direct='true'>

	<script type="text/javascript">
		function $(e){
			return document.getElementById(e);
		}

		function addToList() {
			if ($('dyn_rowList').value != '') { 
				$('dyn_rowList').value += ','+constructAdd(); 
			}else{
				$('dyn_rowList').value = constructAdd();
			}
		}

		var txtFields = new Array("dyn_catId", "dyn_context", "dyn_depth", "dyn_sortBy", "dyn_min", "dyn_perRow", "dyn_rows", "dyn_add");
		var cbFields = new Array("dyn_isTx", "dyn_deals", "dyn_feats", "dyn_newOnly", "dyn_bisOnly", "dyn_descrips", "dyn_uniqG", "dyn_uniqR", "dyn_exPer", "dyn_exFro", "dyn_dynImg", "dyn_igShowCh", "dyn_retHidFolds", "dyn_retSecFolds", "dyn_exDisc", "dyn_igShowCh", "dyn_showNewText");

		function constructAdd() {
			var rowString = '';
			if ($('dyn_catId').value != '') { 
				for (var i=0; i<txtFields.length; i++) {
					if ($(txtFields[i]).value!='') {
						if (rowString!='') { rowString += ':'; }
						if (txtFields[i] == 'dyn_catId') {
							rowString += $(txtFields[i]).value;
						}else{
							rowString += txtFields[i].replace("dyn_","")+"="+$(txtFields[i]).value;
						}
					}
				}
				for (var i=0; i<cbFields.length; i++) {
					if ($(cbFields[i]+'_T')) {
						if ($(cbFields[i]+'_T').checked) {
							if (rowString!='') { rowString += ':'; }
							var TF = "";
							($('dyn_shortBools_T').checked)?TF = "=t":TF = "=true";
							rowString += cbFields[i].replace("dyn_","")+TF;
						}
						if ($(cbFields[i]+'_F').checked) {
							if (rowString!='') { rowString += ':'; }
							var TF = "";
							($('dyn_shortBools_T').checked)?TF = "=f":TF = "=false";
							rowString += cbFields[i].replace("dyn_","")+TF;
						}
					}
				}
			}
			
			return rowString;
		}

		function clearChoices() {
			for (var i=0; i<txtFields.length; i++) {
				$(txtFields[i]).value='';
			}
			for (var i=0; i<cbFields.length; i++) {
				if ($(cbFields[i]+'_D')) { $(cbFields[i]+'_D').checked = true; };
			}
			//and clear debug messages which is not in the cbFields list
			$('dyn_myDebugPrint_D').checked = true;
			//and clear short bools which is not in the cbFields list
			$('dyn_shortBools_D').checked = true;
		}

		var cX = 0; var cY = 0; var rX = 0; var rY = 0;

		function UpdateCursorPosition(e) {
			cX = e.pageX; cY = e.pageY;
		}

		function UpdateCursorPositionDocAll(e) {
			cX = event.clientX; cY = event.clientY;
		}

		if(document.all) { document.onmousemove = UpdateCursorPositionDocAll; }
			else { document.onmousemove = UpdateCursorPosition; }

		function AssignPosition(d) {
			if(self.pageYOffset) {
				rX = self.pageXOffset;
				rY = self.pageYOffset;
				}
			else if(document.documentElement && document.documentElement.scrollTop) {
				rX = document.documentElement.scrollLeft;
				rY = document.documentElement.scrollTop;
				}
			else if(document.body) {
				rX = document.body.scrollLeft;
				rY = document.body.scrollTop;
				}
			if(document.all) {
				cX += rX; 
				cY += rY;
				}
			d.style.left = (cX+10) + "px";
			d.style.top = (cY+10) + "px";
		}

		function hideContent(d) {
			if(d.length < 1) { return; }
			document.getElementById(d).style.display = "none";
		}

		function showContent(d) {
			if(d.length < 1) { return; }
			var dd = document.getElementById(d);
			AssignPosition(dd);
			dd.style.display = "block";
		}

		function helper(showFromId){
			if (!$(showFromId+'_help')) {
				 while (!helper_changer('No help available.', 'overDiv'));
				showContent('overDiv');
				return;
			}
			while (!helper_changer(showFromId+'_help', 'overDiv'));
			showContent('overDiv');
		}

		function helper_changer(from, to){
			if (typeof(from) == 'String') {
				$(to).innerHTML = from;
				return true;
			}
			if (!$(from)) {
				$(to).innerHTML = 'No help available.';
			}else{
				$(to).innerHTML = $(from).innerHTML;
			}
			return true;
		}
		function disableFields(f) {
			var form = $(f);
			for (var i = 0; i<form.elements.length; i++) {
				if (form.elements[i].value == '') {
					form.elements[i].disabled = true;
				}
			}
			return true;
		}
		
	</script>
	<style>
		.f11px { font-size: 11px; }
		.w30px { width: 30px; }
		.w50px { width: 50px; }
		.w100px { width: 100px; }
		.w98p { width: 98%; }
		.w100p { width: 100%; }
		.bgCCC { background-color: #ccc; }
		.help { display: none; }
		.ind { padding-left: 20px; }
		.myDebug { font-family: monospace; color: #0c0; background-color: #000; width: 725px; word-wrap: break-word; white-space: -moz-pre-wrap; white-space: pre-wrap; text-align: left; }
		#overDiv {
			display: none;
			z-index:1000;
			border: 2px solid #666;
			background-color: #eee;
			position: absolute;
			padding: 4px;
			text-align: left;
			-moz-border-radius-topleft: 5px;
			-webkit-border-top-left-radius: 5px;
			-moz-border-radius-topright: 5px;
			-webkit-border-top-right-radius: 5px;
			-moz-border-radius-bottomleft: 5px;
			-webkit-border-bottom-left-radius: 5px;
			-moz-border-radius-bottomright: 5px;
			-webkit-border-bottom-right-radius: 5px;
		}
		.roundedTop {
			-moz-border-radius-topleft: 5px;
			-webkit-border-top-left-radius: 5px;
			-moz-border-radius-topright: 5px;
			-webkit-border-top-right-radius: 5px;
		}
		.roundedBot {
			-moz-border-radius-bottomleft: 5px;
			-webkit-border-bottom-left-radius: 5px;
			-moz-border-radius-bottomright: 5px;
			-webkit-border-bottom-right-radius: 5px;
		}
		.roundedLeft {
			-moz-border-radius-topleft: 5px;
			-webkit-border-top-left-radius: 5px;
			-moz-border-radius-bottomleft: 5px;
			-webkit-border-bottom-left-radius: 5px;
		}
		.roundedRight {
			-moz-border-radius-topright: 5px;
			-webkit-border-top-right-radius: 5px;
			-moz-border-radius-bottomright: 5px;
			-webkit-border-bottom-right-radius: 5px;
		}
	</style>

	<!-- 
		<div></div>
		<div class="ind">
			
		</div>
	-->
	<div id="overDiv"></div>
	<div class="help" id="dyn_catId_help">
		<div>A comma seperated list of values of one of the following:</div>
		<div class="ind">
			CONFIG (this value takes priority over all others)<br />
			<div class="ind">
				useConfig:DOMAINVALUE_ID
				<div class="ind">
					DOMAINVALUE_ID = the ID of the domain value to use<br />
					This will take configuration from the VALUE of this domain value
				</div>
			</div>

			SPECIAL (one of the following three)
			<div class="ind">
				wgd_produce, wg_deals, wg_ads
			</div>
			CATEGORY
			<div class="ind">
				ANY_CATID
			</div>
			MEDIA FILE
			<div class="ind">
				ANY_MEDIA
			</div>
			
		</div>
	</div>
	<div class="help" id="dyn_context_help">
		<div>context = [STRING]   DEFAULT: NULL</div>
		<div class="ind">
			used for faking the context (sets catId = VALUE).
			<div class="ind">
				if null, sets to current catId (if it's not null)<br />
				flips over to category page
			</div>
			<br />
			Results in:
			<div class="ind">
				category.jsp?catId=CONTEXT&prodCatId=CATID&trk=cpage&productId=PRODID<br />
				Where CONTEXT = VALUE and CATID = catId
			</div>
		</div>
	</div>
	<div class="help" id="dyn_add_help">
		<div>add = [STRING]   DEFAULT: ""</div>
		<div class="ind">
			Adds additional IDs into list (in order specified)
			<div class="ind">
				To use multiple IDs, seperate with an Ampersand ("&"):
				<div class="ind">
					add=catId1&catId2&catdId3
				</div>
			</div>
			<br />
			Note:
			<div class="ind">
				Each Id will use a depth of "0" (self-only), so only<br />
				direct children products will be used.
			</div>
		</div>
	</div>
	<div class="help" id="dyn_sortBy_help">
		<div>sortBy = [STRING]   DEFAULT: ""</div>
		<div class="ind">
			Applies sorting / grouping to results.<br /><br />

			Values can be one or more of:<br />
			<div class="ind">
				<strong>priority</strong> / <strong>priorityDesc</strong>
					<div class="ind">Sort by priority</div>
				<strong>price</strong> / <strong>priceDesc</strong>
					<div class="ind">Sort by price</div>
				<strong>name</strong> / <strong>nameDesc</strong>
					<div class="ind">Sort by name</div>
				<strong>kosher</strong> / <strong>kosherDesc</strong>
					<div class="ind">Sort by kosher</div>
				<strong>nutrition</strong> / <strong>nutritionDesc</strong>
					<div class="ind">Sort by nutrition</div>
				<strong>wineAttr</strong> / <strong>wineAttrDesc</strong>
					<div class="ind">Sort by Wine Attr</div>
				<strong>rating</strong> / <strong>ratingDesc</strong>
					<div class="ind">Sort by rating</div>
				<strong>domainRating</strong> / <strong>domainRatingDesc</strong>
					<div class="ind">Sort by domain rating</div>
				<strong>wineCountry</strong> / <strong>wineCountryDesc</strong>
					<div class="ind">Sort by wine country</div>
				<strong>grpCatName</strong> / <strong>grpCatNameDesc</strong>
					<div class="ind">Group by Category name</div>
				<strong>grpCatPriority</strong> / <strong>grpCatPriorityDesc</strong>
					<div class="ind">Group by Category priority</div>
				<strong>grpAvailability</strong> / <strong>grpAvailabilityDesc</strong>
					<div class="ind">Group by availibility</div>
			</div>
			<br />
			Multiple sorts can be passed by seperating each<br />
			value with an underscore ("_").<br />
		</div>
	</div>
	<div class="help" id="dyn_depth_help">
		<div>depth  = [INT]   DEFAULT: 0 (no children)</div>
		<div class="ind">
			the depth the item grabber should use for fetching products
		</div>
	</div>
	<div class="help" id="dyn_min_help">
		<div>min  = [INT]   DEFAULT: 0 (no min)</div>
		<div class="ind">
			the minimum products required to show row.<br /><br />

			if minimum is not met, the row will not display.
		</div>
	</div>
	<div class="help" id="dyn_perRow_help">
		<div>perRow = [INT]   DEFAULT: 5/6 (5 in GENERIC/6 in PEAK PRODUCE)</div>
		<div class="ind">
			the number of products to show in each row.<br /><br />

			take this number and multiply by rows to get the max<br />
			number of products that will be shown.
		</div>
	</div>
	<div class="help" id="dyn_rows_help">
		<div>rows = [INT]   DEFAULT: 1</div>
		<div class="ind">
			the number of rows of products to show<br /><br />

			take this number and multiply by perRow to get the max<br />
			number of products that will be shown.
		</div>
	</div>
	<div class="help" id="dyn_isTx_help">
		<div>isTx = [true|false]   DEFAULT: true</div>
		<div class="ind">
			Show row as transactional.<br />
			on (true) or off (false) <br />
			<!-- NOTE: email=true AND isTx=true results in transactional in email mode -->
		</div>
	</div>
	<div class="help" id="dyn_descrips_help">
		<div>descrips = [true|false]   DEFAULT: true</div>
		<div class="ind">
			Show descriptions (each/4pk/fillet/etc)<br />
			on (true) or off (false)
		</div>
	</div>
	<div class="help" id="dyn_deals_help">
		<div>deals = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Show ONLY deal products
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_newOnly_help">
		<div>newOnly = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Show ONLY new products<br />
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_bisOnly_help">
		<div>bisOnly = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Show ONLY back in stock products<br />
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_exPer_help">
		<div>exPer = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Exclude Perishable products<br />
			(products with PERISHABLE flag)<br />
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_exFro_help">
		<div>exFro = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Exclude Frozen products<br />
			(products with FROZEN flag)<br />
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_exDisc_help">
		<div>exDisc = [true|false]   DEFAULT: true</div>
		<div class="ind">
			Exclude Discontinued products<br />
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_feats_help">
		<div>feats = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Show featured products instead of normal children products<br />
			on (true) or off (false)
		</div>
	</div>
	<div class="help" id="dyn_uniqG_help">
		<div>uniqG = [true|false]   DEFAULT: true</div>
		<div class="ind">
			Inlcude row in uniqueness check at the Global level.<br />
			on (true) or off (false)
		</div>
		<br />
		Note: If two rows have the same products, and row1 is NOT<br />
		included in uniqueness check, then row2 will not recognize the<br />
		products from row1 as duplicates.
	</div>
	<div class="help" id="dyn_uniqR_help">
		<div>uniqR = [true|false]   DEFAULT: true</div>
		<div class="ind">
			Inlcude row in uniqueness check at the Row level.<br />
			on (true) or off (false)
		</div>
		<br />
		Note: If two rows have the same products, and row1 is NOT<br />
		included in uniqueness check, then row2 will not recognize the<br />
		products from row1 as duplicates.
	</div>
	<div class="help" id="dyn_igShowCh_help">
		<div>igShowCh = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Ignore the category's attribute "SHOWCHILDREN" setting<br />
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_retHidFolds_help">
		<div>retHidFolds = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Gets folders with the "HIDDEN" attribute set to true<br />
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_retSecFolds_help">
		<div>retSecFolds = [true|false]   DEFAULT: false</div>
		<div class="ind">
			Gets folders with the "SECONDARY_CATEGORY" attribute set to true<br />
			on (true) or off (false) 
		</div>
	</div>
	<div class="help" id="dyn_showNewText_help">
		<div>showNewText = [true|false]   DEFAULT: true</div>
		<div class="ind">
			Show the "NEW!" text on new product text links<br />
			on (true) or off (false) 
		</div>
	</div>

	<div class="help" id="dyn_mediaPath_help">
		<div>mediaPath</div>
		<div class="ind">
			Sets the path where media is pulled from.
		</div>
		<br />
		This value does not come from the property, but is only set in code.
	</div>


	<div class="help" id="dyn_shortBools_help">
		Use short Boolean values.<br />
		DEFAULT: false (off)<br /><br />
		This simply makes add choices use "t" and "f"<br />
		instead of the whole words.<br /><br />
		Either "true" or "t" are valid regardless of<br />
		this setting.
	</div>
	<div class="help" id="dyn_dynImg_help">
		Use dynamic image generation.<br />
		DEFAULT: false (off)<br /><br />
		This is also available independently at it's test page:
		<div class="ind">
			/test/graphics_testing/index.jsp
		</div>
	</div>
	<div class="help" id="dyn_myDebugPrint_help">
		Outputs debug messages directly to the page.<br />
		DEFAULT: false (off)<br /><br />
		Debug Messages look like:
		<div class="myDebug" style="width: 150px; margin-left: 20px;">This is a debug message.</div>
	</div>


	<form action="/test/content/dynamic_rows.jsp" id="choices" name="choices" onSubmit="return disableFields(this.id);">
		<table width="75%" border="0">
			<tr>
				<td>catId: <a href="#" onclick="return false;" onmouseover="helper('dyn_catId');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
				<td>
					<input type="text" id="dyn_catId" name="dyn_catId" value="<%=dyn_catId%>" class="w100px f11px" />
				</td>
				<td>context: <a href="#" onclick="return false;" onmouseover="helper('dyn_context');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
				<td>
					<input type="text" id="dyn_context" name="dyn_context" value="<%=dyn_context%>" class="w100px f11px" />
				</td>
				<td>add: <a href="#" onclick="return false;" onmouseover="helper('dyn_add');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
				<td>
					<input type="text" id="dyn_add" name="dyn_add" value="<%=dyn_add%>" class="w100px f11px" />
				</td>
			</tr>
			<tr>
				<td>sortBy: <a href="#" onclick="return false;" onmouseover="helper('dyn_sortBy');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
				<td><input type="text" id="dyn_sortBy" name="dyn_sortBy" value="<%=dyn_sortBy%>" class="w100px f11px" /></td>
				<td colspan="4" align="right">
					<table>
						<tr>
							<td>depth: <a href="#" onclick="return false;" onmouseover="helper('dyn_depth');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
							<td><input type="text" id="dyn_depth" name="dyn_depth" value="<%=dyn_depth%>" class="w30px" /></td>
							<td>min: <a href="#" onclick="return false;" onmouseover="helper('dyn_min');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
							<td><input type="text" id="dyn_min" name="dyn_min" value="<%=dyn_min%>" class="w30px" /></td>
							<td>perRow: <a href="#" onclick="return false;" onmouseover="helper('dyn_perRow');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
							<td><input type="text" id="dyn_perRow" name="dyn_perRow" value="<%=dyn_perRow%>" class="w30px" /></td>
							<td>rows: <a href="#" onclick="return false;" onmouseover="helper('dyn_rows');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
							<td><input type="text" id="dyn_rows" name="dyn_rows" value="<%=dyn_rows%>" class="w30px" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>base media<br />path: <a href="#" onclick="return false;" onmouseover="helper('dyn_mediaPath');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
				<td colspan="6">
					<input type="text" id="dyn_mediaPath" name="dyn_mediaPath" value="<%=dyn_mediaPath%>" class="w98p f11px" />
				</td>
			</tr>
		</table>
		<table width="75%" border="0">
			<tr>
				<td valign="top">
					<table width="100%">
						<tr>
							<td valign="top">
								<table border="0" cellspacing="0" cellpadding="0">
									<tr>
										<th colspan="4" align="center" class="roundedLeft roundedRight bgCCC">SUB-ATTRIBUTES</th>
									</tr>
									<tr>
										<td colspan="4" align="center"><img src="/media_stat/images/layout/clear.gif" width="5" height="3" border="0" alt="" /></td>
									</tr>
									<tr>
										<td width="10%"><!--  --></td><td width="1%" align="center" class="roundedTop bgCCC"> </td><td width="1%" align="center">T</td><td width="1%" align="center">F</td>
										<td width="1%" rowspan="12"><!--  --></td>
									</tr>
									<tr>
										<td>isTx:<a href="#" onclick="return false;" onmouseover="helper('dyn_isTx');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_isTx_D" name="dyn_isTx" value="" <%= ("def".equals(dyn_isTx))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_isTx_T" name="dyn_isTx" value="true" <%= ("true".equals(dyn_isTx))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_isTx_F" name="dyn_isTx" value="false" <%= ("false".equals(dyn_isTx))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>deals:<a href="#" onclick="return false;" onmouseover="helper('dyn_deals');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_deals_D" name="dyn_deals" value="" <%= ("def".equals(dyn_deals))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_deals_T" name="dyn_deals" value="true" <%= ("true".equals(dyn_deals))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_deals_F" name="dyn_deals" value="false" <%= ("false".equals(dyn_deals))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>feats:<a href="#" onclick="return false;" onmouseover="helper('dyn_feats');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_feats_D" name="dyn_feats" value="" <%= ("def".equals(dyn_feats))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_feats_T" name="dyn_feats" value="true" <%= ("true".equals(dyn_feats))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_feats_F" name="dyn_feats" value="false" <%= ("false".equals(dyn_feats))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>newOnly:<a href="#" onclick="return false;" onmouseover="helper('dyn_newOnly');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_newOnly_D" name="dyn_newOnly" value="" <%= ("def".equals(dyn_newOnly))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_newOnly_T" name="dyn_newOnly" value="true" <%= ("true".equals(dyn_newOnly))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_newOnly_F" name="dyn_newOnly" value="false" <%= ("false".equals(dyn_newOnly))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>bisOnly:<a href="#" onclick="return false;" onmouseover="helper('dyn_bisOnly');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_bisOnly_D" name="dyn_bisOnly" value="" <%= ("def".equals(dyn_bisOnly))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_bisOnly_T" name="dyn_bisOnly" value="true" <%= ("true".equals(dyn_bisOnly))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_bisOnly_F" name="dyn_bisOnly" value="false" <%= ("false".equals(dyn_bisOnly))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>showNewText:<a href="#" onclick="return false;" onmouseover="helper('dyn_showNewText');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_showNewText_D" name="dyn_showNewText" value="" <%= ("def".equals(dyn_showNewText))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_showNewText_T" name="dyn_showNewText" value="true" <%= ("true".equals(dyn_showNewText))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_showNewText_F" name="dyn_showNewText" value="false" <%= ("false".equals(dyn_showNewText))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>exPer:<a href="#" onclick="return false;" onmouseover="helper('dyn_exPer');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_exPer_D" name="dyn_exPer" value="" <%= ("def".equals(dyn_exPer))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_exPer_T" name="dyn_exPer" value="true" <%= ("true".equals(dyn_exPer))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_exPer_F" name="dyn_exPer" value="false" <%= ("false".equals(dyn_exPer))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>exFro:<a href="#" onclick="return false;" onmouseover="helper('dyn_exFro');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_exFro_D" name="dyn_exFro" value="" <%= ("def".equals(dyn_exFro))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_exFro_T" name="dyn_exFro" value="true" <%= ("true".equals(dyn_exFro))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_exFro_F" name="dyn_exFro" value="false" <%= ("false".equals(dyn_exFro))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>exDisc:<a href="#" onclick="return false;" onmouseover="helper('dyn_exDisc');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_exDisc_D" name="dyn_exDisc" value="" <%= ("def".equals(dyn_exDisc))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_exDisc_T" name="dyn_exDisc" value="true" <%= ("true".equals(dyn_exDisc))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_exDisc_F" name="dyn_exDisc" value="false" <%= ("false".equals(dyn_exDisc))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>igShowCh:<a href="#" onclick="return false;" onmouseover="helper('dyn_igShowCh');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_igShowCh_D" name="dyn_igShowCh" value="" <%= ("def".equals(dyn_igShowCh))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_igShowCh_T" name="dyn_igShowCh" value="true" <%= ("true".equals(dyn_igShowCh))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_igShowCh_F" name="dyn_igShowCh" value="false" <%= ("false".equals(dyn_igShowCh))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td class="roundedBot bgCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
									</tr>
								</table>
							</td>
							<td valign="top">
								<img src="/media_stat/images/layout/clear.gif" width="5" height="3" border="0" alt="" />
							</td>
							<td valign="top" align="right">
								<table border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="10%"><!--  --></td><td width="1%" align="center" class="roundedTop bgCCC"> </td><td width="1%" align="center">T</td><td width="1%" align="center">F</td>
										<td width="1%" rowspan="12"><!--  --></td>
									</tr>
									<tr>
										<td>uniqG:<a href="#" onclick="return false;" onmouseover="helper('dyn_uniqG');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_uniqG_D" name="dyn_uniqG" value="" <%= ("def".equals(dyn_uniqG))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_uniqG_T" name="dyn_uniqG" value="true" <%= ("true".equals(dyn_uniqG))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_uniqG_F" name="dyn_uniqG" value="false" <%= ("false".equals(dyn_uniqG))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>uniqR:<a href="#" onclick="return false;" onmouseover="helper('dyn_uniqR');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_uniqR_D" name="dyn_uniqR" value="" <%= ("def".equals(dyn_uniqR))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_uniqR_T" name="dyn_uniqR" value="true" <%= ("true".equals(dyn_uniqR))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_uniqR_F" name="dyn_uniqR" value="false" <%= ("false".equals(dyn_uniqR))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>descrips:<a href="#" onclick="return false;" onmouseover="helper('dyn_descrips');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_descrips_D" name="dyn_descrips" value="" <%= ("def".equals(dyn_descrips))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_descrips_T" name="dyn_descrips" value="true" <%= ("true".equals(dyn_descrips))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_descrips_F" name="dyn_descrips" value="false" <%= ("false".equals(dyn_descrips))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>retHidFolds:<a href="#" onclick="return false;" onmouseover="helper('dyn_retHidFolds');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_retHidFolds_D" name="dyn_retHidFolds" value="" <%= ("def".equals(dyn_retHidFolds))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_retHidFolds_T" name="dyn_retHidFolds" value="true" <%= ("true".equals(dyn_retHidFolds))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_retHidFolds_F" name="dyn_retHidFolds" value="false" <%= ("false".equals(dyn_retHidFolds))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>retSecFolds:<a href="#" onclick="return false;" onmouseover="helper('dyn_retSecFolds');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_retSecFolds_D" name="dyn_retSecFolds" value="" <%= ("def".equals(dyn_retSecFolds))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_retSecFolds_T" name="dyn_retSecFolds" value="true" <%= ("true".equals(dyn_retSecFolds))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_retSecFolds_F" name="dyn_retSecFolds" value="false" <%= ("false".equals(dyn_retSecFolds))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td class="roundedBot bgCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
									</tr>
									<tr>
										<td colspan="4" align="center"><img src="/media_stat/images/layout/clear.gif" width="5" height="3" border="0" alt="" /></td>
									</tr>
									<tr>
										<th colspan="4" align="center" class="roundedLeft roundedRight bgCCC">DEBUG</th>
									</tr>
									<tr>
										<td colspan="4" align="center"><img src="/media_stat/images/layout/clear.gif" width="5" height="3" border="0" alt="" /></td>
									</tr>
									<tr>
										<td width="20%"><!--  --></td><td width="1%" align="center" class="roundedTop bgCCC"> </td><td width="1%" align="center">T</td><td width="1%" align="center">F</td>
									</tr>
									<tr>
										<td>use short bools:<a href="#" onclick="return false;" onmouseover="helper('dyn_shortBools');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_shortBools_D" name="dyn_shortBools" value="" <%= ("def".equals(dyn_shortBools))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_shortBools_T" name="dyn_shortBools" value="true" <%= ("true".equals(dyn_shortBools))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_shortBools_F" name="dyn_shortBools" value="false" <%= ("false".equals(dyn_shortBools))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>use dynamic images:<a href="#" onclick="return false;" onmouseover="helper('dyn_dynImg');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_dynImg_D" name="dyn_dynImg" value="" <%= ("def".equals(dyn_dynImg))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_dynImg_T" name="dyn_dynImg" value="true" <%= ("true".equals(dyn_dynImg))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_dynImg_F" name="dyn_dynImg" value="false" <%= ("false".equals(dyn_dynImg))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td>print debug msgs:<a href="#" onclick="return false;" onmouseover="helper('dyn_myDebugPrint');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
										<td class="bgCCC">
											<input type="radio" id="dyn_myDebugPrint_D" name="dyn_myDebugPrint" value="" <%= ("def".equals(dyn_myDebugPrint))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_myDebugPrint_T" name="dyn_myDebugPrint" value="true" <%= ("true".equals(dyn_myDebugPrint))?"checked=\"true\"":"" %> />
										</td>
										<td>
											<input type="radio" id="dyn_myDebugPrint_F" name="dyn_myDebugPrint" value="false" <%= ("false".equals(dyn_myDebugPrint))?"checked=\"true\"":"" %> />
										</td>
									</tr>
									<tr>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td class="roundedBot bgCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
										<td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt="" /></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
				<td width="30%" valign="top">
					<input type="button" value="clear choices" onclick="javascript:clearChoices();" class="w100p" /><br />
					<input type="button" value="add choices to list" onclick="javascript:addToList();" class="w100p" /><br />
					<input type="button" value="clear list" onclick="javascript:$('dyn_rowList').value='';" class="w100p" style="" /><br />
					<input type="submit" value="test list" class="w100p" /><br />
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><textarea id="dyn_rowList" name="dyn_rowList" style="width: 98%; height: 70px;"><%=dyn_rowList%></textarea></td>
			</tr>
		</table>
	</form>

	<%

		//fill in catId
		if ("".equals(catId)) { catId = dyn_catId; }
		
		//transactional, use the cart

		//successPage when adding to cart
		successPage = "/grocery_cart_confirm.jsp?catId="+catId;

		//javascript required for transactional
		%><script type="text/javascript" src="/assets/javascript/pricing.js"></script><%

		//set media path base
		mediaPathTempBase=dyn_mediaPath;

		strDynRows=dyn_rowList;
	%>
	<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= successPage %>' source='<%= request.getParameter("fdsc.source")%>'>
		<% //START error messaging %>
			<fd:ErrorHandler result='<%=result%>' name='quantity' id='errorMsg'>
				<img src="/media_stat/images/layout/clear.gif" width="20" height="12" alt="" border="0">
				<%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>
		<% //END error messaging %>

		<%@ include file="/includes/i_dynamicRows_logic.jspf" %>
	</fd:FDShoppingCart>
</tmpl:put>
</tmpl:insert>