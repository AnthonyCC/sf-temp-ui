<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> Graphics Testing </title>

		<style>
			body, input {
				font-family: monospace;
				font-size: 12px;
			}
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
				color: #fff;
				background-color: #666;
			}
			td {
				border: 1px solid #ccc;
			}
			.noborder, .noborder td {
				border-width: 0px;
			}
		</style>
		<script type="text/javascript">
		<!--
			function $(e){
				return document.getElementById(e);
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
		//-->
		</script>
	</head>
<body>

<%@ page import='com.freshdirect.framework.util.NVL'%>
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

		//check for a passed retImageWidth
			String iW = NVL.apply(request.getParameter("iW"), "");
		//check for a passed retImageHeight
			String iH = NVL.apply(request.getParameter("iH"), "");
		//check for a passed iQ
			String iQ = NVL.apply(request.getParameter("iQ"), "");

		//check for a passed bgColor
			String bgColorString = NVL.apply(request.getParameter("iBG"), "");



%>

<table width="100%">
	<tr>
		<td valign="top" align="center">
			<form method="GET" action="./index.jsp" name="graphics_form" id="graphics_form" onSubmit="return disableFields(this.id);" >
			<table>	
				<tr>
					<th>Background</th>
				</tr>
				<tr>
					<td>
						W: <input type="text" id="iW" name="iW" value="<%= iW %>" size="1" /> x
						H: <input type="text" id="iH" name="iH" value="<%= iH %>" size="1" />
					</td>
				</tr>
				<tr>
					<td>
						Background Color: #<input type="text" id="iBG" name="iBG" value="<%= bgColorString %>" size="1" />
						Quality: <input type="text" id="iQ" name="iQ" value="<%= iQ %>" size="1" />
					</td>
				</tr>
				<tr>
					<th>Product</th>
				</tr>
				<tr>
					<td>
						ID: <input type="text" id="pId" name="pId" value="<%= pId %>" style="width: 80%;" />
					</td>
				</tr>
				<tr>
					<td>
						Size: 
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
					</td>
				</tr>
				<tr>
					<th>Overlay</th>
				</tr>
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td>Type:</td>
								<td>
									<input type="radio" id="bTypeA" name="bType" value="" <%="".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />auto
									<input type="radio" id="bTypeD" name="bType" value="deal" <%="deal".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />deal
									<input type="radio" id="bTypeF" name="bType" value="fave" <%="fave".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />fave
									<br />
									<input type="radio" id="bTypeN" name="bType" value="new" <%="new".equalsIgnoreCase(bType)?"checked=\"true\"":""%> />new
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
									Value: <input type="text" id="bVal" name="bVal" value="<%=request.getParameter("bVal")%>" size="1" />
								</td>
								<td rowspan="2">
									Size:
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
					</td>
				</tr>
			</table>
				
				<input type="submit" value="generate image" style="width: auto; background-color: #666; border:2px solid #333; font-weight: bold; color: #fff;" />
			</form>
		</td>
		<td valign="top" width="65%" align="center">
			<%= ("".equals(queryString))?"":"<img src=\""+imgPath+"\" />" %><br />
			<hr />
			<textarea id="queryString" rows="3" style="width: 98%;"><%= request.getQueryString() %></textarea>
			<hr />
			<%= ("".equals(queryString))?"":"&lt;img src=\""+imgPath+"\" /&gt;" %>
		</td>
	</tr>
</table>






  
</body>
</html>