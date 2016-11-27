<%@ page import='com.freshdirect.framework.util.NVL'%>
<%
	//check for a passed pId
		String pId = NVL.apply(request.getParameter("pId"), "");
		String tType =  NVL.apply(request.getParameter("tType"), "");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title> Gateway Testing</title>
	
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%@ include file="/shared/template/includes/ccl.jspf" %>

	<script type="text/javascript">
		function ajaxUpdate(targetId, url, params) {
			if (!$(targetId) || url == '') { return; }
			
			new Ajax.Updater(targetId, url, { evalScripts: true, method: 'get', parameters: params });
			
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

			var cursorX = 0; var cursorY = 0; var rollX = 0; var rollY = 0;

			function UpdateCursorPosition(e) {
				cursorX = e.pageX; cursorY = e.pageY;
			}

			function UpdateCursorPositionDocAll(e) {
				cursorX = event.clientX; cursorY = event.clientY;
			}

			document.observe("dom:loaded", function() {
				if(document.all) { document.onmousemove = UpdateCursorPositionDocAll; }
					else { document.onmousemove = UpdateCursorPosition; }
			});

			function AssignPosition(d) {
				if(self.pageYOffset) {
					rollX = self.pageXOffset;
					rollY = self.pageYOffset;
					}
				else if(document.documentElement && document.documentElement.scrollTop) {
					rollX = document.documentElement.scrollLeft;
					rollY = document.documentElement.scrollTop;
					}
				else if(document.body) {
					rollX = document.body.scrollLeft;
					rollY = document.body.scrollTop;
					}
				if(document.all) {
					cursorX += rollX; 
					cursorY += rollY;
					}
				d.style.left = (cursorX+10) + "px";
				d.style.top = (cursorY+10) + "px";
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
		#resData {
			border: 1px solid #ccc;
			float: left;
			height: auto;
			width: auto;
		}
		.ind { padding-left: 20px; }
		.help { display: none; font-size: 12px; }
	</style>
</head>

<body>
	<div id="overDiv"></div>
	
	<div class="help" id="dyn_pId_help">
		<div>Profile ID to be deleted</div>
		<div>DEFAULT: nothing</div>
		<div class="ind">
			If no ID or an invalid ID is passed, no data is returned.<br />
		</div>
	</div>
	<br/><br/><br/>
	<form id="formData" onsubmit="ajaxUpdate('resData', '/test/gateway/gatewayResponse.jsp', $(this.id).serialize()); return false;">
	<input type="hidden" name="action" id="action" value="deleteProfile"/>
	<table id="" width="800">
		<tr>
			<td>
				Profile ID: <a href="#" onclick="return false;" onmouseover="helper('dyn_pId');" onmouseout="javascript: hideContent('overDiv');">?</a> <input type="text" id="pId" name="pId" value="<%= pId %>" style="width: 80%;" />
			</td>
		</tr>
		<tr>
			<td>
				<input type="submit" name="deleteProfile" id="deleteProfile" value="Delete Profile" />
			</td>
		</tr>
	</table>
	</form>
	<div id="resData"><!-- --></div>
</body>
</html>
