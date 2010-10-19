<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> Graphics Testing </title>

			<%@ include file="/common/template/includes/metatags.jspf" %>
			<%@ include file="/common/template/includes/i_javascripts.jspf" %>
			<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
			<%@ include file="/shared/template/includes/ccl.jspf" %>
		<script type="text/javascript">

			var cX = 0; var cY = 0; var rX = 0; var rY = 0;

			function UpdateCursorPosition(e) {
				cX = e.pageX; cY = e.pageY;
			}

			function UpdateCursorPositionDocAll(e) {
				cX = event.clientX; cY = event.clientY;
			}

			document.observe("dom:loaded", function() {
				if(document.all) { document.onmousemove = UpdateCursorPositionDocAll; }
					else { document.onmousemove = UpdateCursorPosition; }
			});

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

			function disableFields(f) {
				var form = $(f);
				for (var i = 0; i<form.elements.length; i++) {
					if (form.elements[i].value == '') {
						form.elements[i].disabled = true;
					}
				}
				return true;
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
		</script>
		<style>
			input {
				width: 50px;
				text-align: center;
			}
			input[type="radio"] {
				width: 20px;
				text-align: center;
			}
			table {
				border: 0px;
				padding: 0px;
				margin: 0px;
			}
			th {
				padding: 3px;
				text-align: center;
			}
			td {
				border: 1px solid #ccc;
			}
			.noborder, .noborder td {
				border-width: 0px;
			}
			.f11px { font-size: 11px; }
			.w30px { width: 30px; }
			.w50px { width: 50px; }
			.w100px { width: 100px; }
			.w98p { width: 98%; }
			.w100p { width: 100%; }
			.bgCCC { background-color: #ccc; }
			.help { display: none; font-size: 12px; }
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
	</head>
<body>

<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='java.net.URLEncoder' %>
<%
	//defaults
		String queryString = NVL.apply(request.getQueryString(), "");
		String imgPath = "/test/graphics_testing/generateImage.jsp?"+queryString;
		String prodWidth = NVL.apply(request.getParameter("prodWidth"), "");
		String prodHeight = NVL.apply(request.getParameter("prodHeight"), "");
		//check for a passed pSize
			String pSize =  NVL.apply(request.getParameter("pSize"), ""); //this is the code-letter (cx,p,c,cr,z, etc)
			String pSizeTemp = "";
		//check for a passed pOx
			String pOx = NVL.apply(request.getParameter("pOx"), "");
		//check for a passed pOy
			String pOy = NVL.apply(request.getParameter("pOy"), "");

		//check for a passed bType
			String bType = NVL.apply(request.getParameter("bType"), "");
		//check for a passed bSize
			String bSize =  NVL.apply(request.getParameter("bSize"), "");
		//check for a passed bVal
			String bVal = NVL.apply(request.getParameter("bVal"), "-1");
			int bValTemp = 0;
		//check for a passed pId
			String pId = NVL.apply(request.getParameter("pId"), "");
		String overlayWidth = NVL.apply(request.getParameter("overlayWidth"), "");
		String overlayHeight = NVL.apply(request.getParameter("overlayHeight"), "");
		//check for a passed bOx
			String bOx = NVL.apply(request.getParameter("bOx"), "");
		//check for a passed bOy
			String bOy = NVL.apply(request.getParameter("bOy"), "");

		//Rating
		//check for a passed rVal
			String rVal = NVL.apply(request.getParameter("rVal"), "-1");
		//check for a passed rSize
			String rType =  NVL.apply(request.getParameter("rType"), "");
		//check for a passed rOx
			String rOx = NVL.apply(request.getParameter("rOx"), "");
		//check for a passed rOy
			String rOy = NVL.apply(request.getParameter("rOy"), "");
		//check for a passed rCent
			String rCent = NVL.apply(request.getParameter("rCent"), "false");

		//check for a passed retImageWidth
			String iW = NVL.apply(request.getParameter("iW"), "");
		//check for a passed retImageHeight
			String iH = NVL.apply(request.getParameter("iH"), "");
		//check for a passed iQ
			String iQ = NVL.apply(request.getParameter("iQ"), "");

		//check for a passed bgColor
			String bgColorString = NVL.apply(request.getParameter("iBG"), "");

%>

	<div id="overDiv"></div>

	<div class="help" id="dyn_iWiH_help">
		<div>The WIDTH and HEIGHT of the image generated.</div>
		<div>DEFAULT: auto (Uses product image size)</div>
	</div>

	<div class="help" id="dyn_iBG_help">
		<div>The BACKGROUND color to use in the generated image.</div>
		<div>DEFAULT: FFFFFF (white)</div>
		<div class="ind">This can be either a 3 or 6 digit hexcode.</div>
	</div>

	<div class="help" id="dyn_iBGDemo_help">
		<div id="dyn_iBGDemo_demo">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br /></div>
	</div>

	<div class="help" id="dyn_iQ_help">
		<div>The image QUALITY.</div>
		<div>DEFAULT: 1.0 (no compression)</div>
		<div class="ind">This is a FLOAT where
			<div class="ind">1.0 = 100% (no compression)<br />0.5 = 50% (2:1 compression)</div>
		</div>
	</div>

	<div class="help" id="dyn_pId_help">
		<div>A PRODUCT_ID or SKU_CODE to get the info for.</div>
		<div>DEFAULT: nothing</div>
		<div class="ind">
			If this is not passed (or an invalid ID is used), <br />
			the "photo coming soon" image is returned.<br />
			</br /><br />
			When using a SKU, only the RATING comes from the SKU.
		</div>
	</div>

	<div class="help" id="dyn_pSize_help">
		<div>A product image size CODE.</div>
		<div>DEFAULT: c</div>
		<div class="ind">One of:
			<div class="ind">
				a (Alternate Image)<br />
				c (Product Image)<br />
				cr (Rollover Image)<br />
				ct (Category Image)<br />
				cx (Confirm Image)<br />
				p (Detail Image)<br />
				f (Thumbnail Image)<br />
				z (Zoom Image)<br />
			</div>
		</div>
	</div>

	<div class="help" id="dyn_pOxpOy_help">
		<div>Product OFFSET.</div>
		<div>DEFAULT: x0, y0</div>
		<div class="ind">Sets the offset of the product image (relative to background)</div>
	</div>

	<div class="help" id="dyn_bOxbOy_help">
		<div>Burst OFFSET.</div>
		<div>DEFAULT: x0, y0</div>
		<div class="ind">Sets the offset of the burst image (relative to background)</div>
	</div>

	<div class="help" id="dyn_bType_help">
		<div>TYPE of burst image to show.</div>
		<div>DEFAULT: auto</div>
		<div class="ind">Determines the type of burst to use.
			<div class="ind">
				auto would apply the standard store logic to determine the burst.<br />
				force none will force no burst even if it would normally be used by auto.<br />
				<br />
				NOTE: Some types do not have a "large" size.<br />
				If the images are added using the normal naming conventions <br />
				(i.e. "brst_lg_fave.gif"), they will be picked up by the code automatically.
			</div>
		</div>
	</div>

	<div class="help" id="dyn_bSize_help">
		<div>SIZE of burst image to show.</div>
		<div>DEFAULT: small</div>
		<div class="ind">Determines the size of burst to use.
			<div class="ind">
				auto will try to adjust for product size (currently only p = lg)<br />
				<br />
				NOTE: Some types do not have a "large" size.<br />
				If the images are added using the normal naming conventions <br />
				(i.e. "brst_lg_fave.gif"), they will be picked up by the code automatically.
			</div>
		</div>
	</div>

	<div class="help" id="dyn_bVal_help">
		<div>VALUE to use in burst image.</div>
		<div>DEFAULT: none</div>
		<div class="ind">
			Over rides the auto value for a "deal" type burst.<br />
			<br />
			If the value corresponds to a deal image that doesn't exist, no burst<br />
			is displayed. If that image is added later, it will be picked up by the code.
		</div>
	</div>

	<div class="help" id="dyn_bTypeDeal_help">
		<div>
			<img src="/media_stat/images/deals/brst_sm_15.gif" />
			<img src="/media_stat/images/deals/brst_lg_15.gif" />
		</div>
	</div>

	<div class="help" id="dyn_bTypeFave_help">
		<div>
			<img src="/media_stat/images/bursts/brst_sm_fave.gif" />
			<img src="/media_stat/images/bursts/brst_lg_fave.gif" />
		</div>
	</div>

	<div class="help" id="dyn_bTypeNew_help">
		<div>
			<img src="/media_stat/images/bursts/brst_sm_new.gif" />
			<img src="/media_stat/images/bursts/brst_lg_new.gif" />
		</div>
	</div>

	<div class="help" id="dyn_bTypeBis_help">
		<div>
			<img src="/media_stat/images/bursts/brst_sm_bis.gif" />
			<img src="/media_stat/images/bursts/brst_lg_bis.gif" />
		</div>
	</div>

	<div class="help" id="dyn_bTypeSale_help">
		<div>
			<img src="/media_stat/images/bursts/brst_sm_sale.gif" />
			<img src="/media_stat/images/bursts/brst_lg_sale.gif" />
		</div>
	</div>
	
	<div class="help" id="dyn_rVal_help">
		<div>VALUE to use in rating image.</div>
		<div>DEFAULT: auto</div>
		<div class="ind">
			Overrides the auto value for a rating image.<br />
			<br />
			"auto" refers to the rating returned from the product (if any).<br />
			<br />
			This is a value of between 1 and 10<br />
			Each star is an even number, with halves are odds. (so a 2 = 1 star)
		</div>
	</div>

	<div class="help" id="dyn_rType_help">
		<div>TYPE of rating image to show.</div>
		<div>DEFAULT: Additional</div>
		<div class="ind">
			Determines if rating should be additional or exclusive.<br />
			<br />
			"Additional" adds the rating image to the Product image being shown,<br />
			where "Exclusive" will ONLY return the rating image. If no product is<br />
			being used, "Additional" will not include a rating.
		</div>
	</div>

	<div class="help" id="dyn_rOxrOy_help">
		<div>rating OFFSET.</div>
		<div>DEFAULT: see note</div>
		<div class="ind">
			Sets the offset of the burst image.<br />
			<br />
			Default for Additional is x0, y[PODUCT IMAGE HEIGHT]<br />
			Default for Exclusive is x0, y0
		</div>
	</div>

	<div class="help" id="dyn_rCent_help">
		<div>rather to CENTER rating image or not.</div>
		<div>DEFAULT: false</div>
		<div class="ind">Determines if rating should be centered.
			<div class="ind">
				If this is true, the rating image will be centered in the image.<br />
				NOTE: This will only override the horizontal (x) offset if x, y is defined.
			</div>
		</div>
	</div>
	
<table width="100%">
	<tr>
		<td valign="top" align="center">
			<form method="GET" action="./index.jsp" name="graphics_form" id="graphics_form" onSubmit="return disableFields(this.id);" >
			<table>	
				<tr>
					<th class="roundedLeft roundedRight bgCCC">Background</th>
				</tr>
				<tr>
					<td>
						<a href="#" onclick="return false;" onmouseover="helper('dyn_iWiH');" onmouseout="javascript: hideContent('overDiv');">?</a>
						W: <input type="text" id="iW" name="iW" value="<%= iW %>" size="1" /> x
						H: <input type="text" id="iH" name="iH" value="<%= iH %>" size="1" />
						
						Quality: 
						<a href="#" onclick="return false;" onmouseover="helper('dyn_iQ');" onmouseout="javascript: hideContent('overDiv');">?</a> <input type="text" id="iQ" name="iQ" value="<%= iQ %>" size="1" />
					</td>
				</tr>
				<tr>
					<td>
						Background Color: 
						<a href="#" onclick="return false;" onmouseover="helper('dyn_iBG');" onmouseout="javascript: hideContent('overDiv');">?</a> #<input type="text" id="iBG" name="iBG" value="<%= bgColorString %>" size="1" /> <a href="#" onclick="return false;" onmouseover="helper('dyn_iBGDemo'); ($('iBG').value=='') ? $('dyn_iBGDemo_demo').style.backgroundColor='#fff' : $('dyn_iBGDemo_demo').style.backgroundColor='#'+$('iBG').value" onmouseout="javascript: hideContent('overDiv');">?</a>
					</td>
				</tr>
				<tr>
					<th class="roundedLeft roundedRight bgCCC">Product</th>
				</tr>
				<tr>
					<td>
						ID: <a href="#" onclick="return false;" onmouseover="helper('dyn_pId');" onmouseout="javascript: hideContent('overDiv');">?</a> <input type="text" id="pId" name="pId" value="<%= pId %>" style="width: 80%;" />
					</td>
				</tr>
				<tr>
					<td>
						Size: <a href="#" onclick="return false;" onmouseover="helper('dyn_pSize');" onmouseout="javascript: hideContent('overDiv');">?</a>
						<select id="pSize" name="pSize">
							<option value=""<%="".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>></option>
							<option value="a"<%="a".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>>a</option>
							<option value="c"<%="c".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>>c</option>
							<option value="cr"<%="cr".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>>cr</option>
							<option value="ct"<%="ct".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>>ct</option>
							<option value="cx"<%="cx".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>>cx</option>
							<option value="p"<%="p".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>>p</option>
							<option value="f"<%="f".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>>f</option>
							<option value="z"<%="z".equalsIgnoreCase(pSize)?" selected=\"selected\"":""%>>z</option>
						</select>
						Offset:
						X: <input type="text" id="pOx" name="pOx" value="<%= pOx %>" size="1" /> , 
						Y: <input type="text" id="pOy" name="pOy" value="<%= pOy %>" size="1" />
						<a href="#" onclick="return false;" onmouseover="helper('dyn_pOxpOy');" onmouseout="javascript: hideContent('overDiv');">?</a>
					</td>
				</tr>
				<tr>
					<th class="roundedLeft roundedRight bgCCC">Overlay</th>
				</tr>
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td>Type: <a href="#" onclick="return false;" onmouseover="helper('dyn_bType');" onmouseout="javascript: hideContent('overDiv');">?</a></td>
								<td>
									<input type="radio" id="bTypeA" name="bType" value="" <%="".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />auto
									<input type="radio" id="bTypeD" name="bType" value="deal" <%="deal".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />deal
										<a href="#" onclick="return false;" onmouseover="helper('dyn_bTypeDeal');" onmouseout="javascript: hideContent('overDiv');">?</a>
									<input type="radio" id="bTypeF" name="bType" value="fave" <%="fave".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />fave
										<a href="#" onclick="return false;" onmouseover="helper('dyn_bTypeFave');" onmouseout="javascript: hideContent('overDiv');">?</a>
									<br />
									<input type="radio" id="bTypeN" name="bType" value="new" <%="new".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />new
										<a href="#" onclick="return false;" onmouseover="helper('dyn_bTypeNew');" onmouseout="javascript: hideContent('overDiv');">?</a>
									<input type="radio" id="bTypeB" name="bType" value="bis" <%="new".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />bis
										<a href="#" onclick="return false;" onmouseover="helper('dyn_bTypeBis');" onmouseout="javascript: hideContent('overDiv');">?</a>
									<input type="radio" id="bTypeS" name="bType" value="sale" <%="new".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />sale
										<a href="#" onclick="return false;" onmouseover="helper('dyn_bTypeSale');" onmouseout="javascript: hideContent('overDiv');">?</a>
									<br />
									<input type="radio" id="bTypeX" name="bType" value="X" <%="X".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />force none
								</td>
							</tr>
						</table>
						
					</td>
				</tr>
				<tr>
					<td>
						<table class="noborder">
							<tr>
								<td rowspan="2">
									Value: <a href="#" onclick="return false;" onmouseover="helper('dyn_bVal');" onmouseout="javascript: hideContent('overDiv');">?</a> <input type="text" id="bVal" name="bVal" value="<%=request.getParameter("bVal")%>" size="1" />
								</td>
								<td rowspan="2">
									Size: <a href="#" onclick="return false;" onmouseover="helper('dyn_bSize');" onmouseout="javascript: hideContent('overDiv');">?</a>
								</td>
								<td>
									<input type="radio" id="bSizeSM" name="bSize" value="sm" <%="sm".equalsIgnoreCase(bSize)?"checked=\"true\"":""%> />small
								</td>
								<td>
									<input type="radio" id="bSizeLG" name="bSize" value="lg" <%="lg".equalsIgnoreCase(bSize)?"checked=\"true\"":""%> />large
								</td>
							</tr>
							<tr>
								<td>
									<input type="radio" id="bSizeX" name="bSize" value="" <%="".equalsIgnoreCase(bSize)?"checked=\"true\"":""%> />auto
								</td>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						Offset:
						X: <input type="text" id="bOx" name="bOx" value="<%= bOx %>" size="1" /> , 
						Y: <input type="text" id="bOy" name="bOy" value="<%= bOy %>" size="1" />
						<a href="#" onclick="return false;" onmouseover="helper('dyn_bOxbOy');" onmouseout="javascript: hideContent('overDiv');">?</a>
					</td>
				</tr>
				<tr>
					<th class="roundedLeft roundedRight bgCCC">Rating</th>
				</tr>
				<tr>
					<td>
						<table class="noborder">
							<tr>
								<td>
									Value: <a href="#" onclick="return false;" onmouseover="helper('dyn_rVal');" onmouseout="javascript: hideContent('overDiv');">?</a> <input type="text" id="rVal" name="rVal" value="<%=request.getParameter("rVal")%>" size="1" />
								</td>
								<td>
									Type: <a href="#" onclick="return false;" onmouseover="helper('dyn_rType');" onmouseout="javascript: hideContent('overDiv');">?</a>
								</td>
								<td>
									<input type="radio" id="rTypeAdd" name="rType" value="add" <%="".equalsIgnoreCase(rType)||"add".equalsIgnoreCase(rType)?"checked=\"true\"":""%> />Additional
								</td>
								<td>
									<input type="radio" id="rTypeExc" name="rType" value="exc" <%="exc".equalsIgnoreCase(rType)?"checked=\"true\"":""%> />Exclusive
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						Offset:
						X: <input type="text" id="rOx" name="rOx" value="<%= rOx %>" size="1" /> , 
						Y: <input type="text" id="rOy" name="rOy" value="<%= rOy %>" size="1" />
						<a href="#" onclick="return false;" onmouseover="helper('dyn_rOxrOy');" onmouseout="javascript: hideContent('overDiv');">?</a>
						<input type="checkbox" name="rCent" id="rCent" style="width: 30px;" <%="on".equalsIgnoreCase(rCent)?"checked=\"true\"":""%> /> center
						<a href="#" onclick="return false;" onmouseover="helper('dyn_rCent');" onmouseout="javascript: hideContent('overDiv');">?</a>
					</td>
				</tr>
			</table>
				
				<input type="submit" value="generate image" style="width: auto; background-color: #666; border:2px solid #333; font-weight: bold; color: #fff;" />
			</form>
		</td>
		<td valign="top" width="65%" align="center">
			<%= ("".equals(queryString))?"":"<img src=\""+imgPath+"\" />" %><br />
			<hr />
			<div style="text-align: left;">Query Params:</div>
			<textarea id="queryString" rows="3" style="width: 98%;"><%= request.getQueryString() %></textarea>
			<hr />
			<div style="text-align: left; word-wrap: break-word;">
				URL:<br />
				&nbsp;&nbsp;<%= ("".equals(queryString))?"":"&lt;img src=\""+imgPath+"\" /&gt;" %><br />
			</div>
		</td>
	</tr>
</table>





  
</body>
</html>