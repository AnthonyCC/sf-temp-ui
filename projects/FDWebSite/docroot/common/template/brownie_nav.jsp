<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import='com.freshdirect.fdstore.sempixel.FDSemPixelCache' %>
<%@ page import='com.freshdirect.fdstore.sempixel.SemPixelModel' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_DNAV_TOTAL = 970;
%>
<html lang="en-US" xml:lang="en-US" xmlns:fb="http://www.facebook.com/2008/fbml">
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" lang="en-US"/>
    <tmpl:get name="seoMetaTag"/>
    <%@ include file="/common/template/includes/seo_canonical.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
	
	<% if(!"fb".equals(request.getParameter("current"))) { %>	
	
		<script type="text/javascript" src="https://platform.twitter.com/widgets.js"></script>

		<!-- required for TextboxList -->
		<jwr:script src="/assets/javascript/GrowingInput.js" useRandomParam="false" />
		<jwr:script src="/assets/javascript/TextboxList.js" useRandomParam="false" />
		
		<script type="text/javascript">
			var t2;
			$jq(document).ready(function () {
				if ($jq('#form_tags_input').length === 0) { return; }
				// With custom adding keys 
				t2 = new $jq.TextboxList('#form_tags_input', {bitsOptions:{editable:{addKeys: [188,13],addOnBlur: true}}});
				<%
					if("sendmails".equals(request.getParameter("action"))) {
						//check the emails
						String recipient_list = request.getParameter("form_tags_input");
						StringTokenizer stokens = new StringTokenizer(recipient_list, ",");		
						if (stokens.countTokens() > 0) {
							while(stokens.hasMoreTokens()) {
								String recipient = stokens.nextToken();
								%>
									t2.add('<%=recipient%>');
								<%
							}
						}
					}
				%>
			});
			
			function onABCommComplete() {
			  // OPTIONAL: do something here after the new data has been populated in your text area
			  var eCount = 0;
			  var element = document.getElementById("recipient_list");
			  if(element != null) {
				  var data = element.value;  
				  //window.alert("data:" + data);			  
				  if(data.length > 0) {
					var currentTagTokens = data.split( "," );
					for(i=0;i<currentTagTokens.length;i++) {
						var email = currentTagTokens[i].substring(currentTagTokens[i].indexOf("<") + 1, currentTagTokens[i].indexOf(">"));
						if(email.length > 0) {
							//window.alert("email:"+email);
							t2.add(email);
						}
					}
				  }
				  element.value = "";
			   }
			   //document.getElementById("emailnumber").innerHTML=eCount;
			}
		</script>	
		
		<% if("true".equals(FDStoreProperties.getCouldSpongeAddressImports())) { %>
			<script type="text/javascript" src="https://api.cloudsponge.com/address_books.js"></script>
			<script type="text/javascript">
				var csPageOptions = {  
					domain_key:'<%= FDStoreProperties.getCloudSpongeDomainKey() %>',   
					textarea_id:'recipient_list'
				};
				var onImportComplete = onABCommComplete;
			</script>
		<% } else { %>
			<script type="text/javascript" src="https://www.plaxo.com/css/m/js/util.js"></script>
            <script type="text/javascript" src="https://www.plaxo.com/css/m/js/basic.js"></script>
            <script type="text/javascript" src="https://www.plaxo.com/css/m/js/abc_launcher.js"></script>
		<% } %>
	<% } %>
	
	<jwr:script src="/assets/javascript/ZeroClipboard.js" useRandomParam="false" />
	<jwr:script src="/assets/javascript/shadedborder.js" useRandomParam="false" />

	<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
	
	<jwr:style src="/assets/css/brownie_points.css" media="all" />
	<jwr:style src="/assets/css/TextboxList.css" media="all" />

	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
  </head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#ff9900" text="#333333">
	<%@ include file="/shared/template/includes/i_body_start.jspf" %>
	<%@ include file="/common/template/includes/globalnav_optimized.jspf" %> 		
	<center class="text10">
		<table width="<%=W_DNAV_TOTAL%>" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="<%=W_DNAV_TOTAL%>" valign="top"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_DNAV_TOTAL%>" height="5" border="0"></td>
			</tr>
			<tr>
				<td width="<%=W_DNAV_TOTAL%>"><%@ include file="/common/template/includes/deptnav.jspf" %></td>
			</tr>
			<tr>
				<td width="<%=W_DNAV_TOTAL%>" bgcolor="#999966" colspan="7"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" alt="" /></TD>
			</tr>
			<tr valign="top">
				<td width="<%=W_DNAV_TOTAL%>" align="center">
					<img src="/media_stat/images/layout/clear.gif" height="20" width="<%=W_DNAV_TOTAL%>" alt="" /><br />
					<!-- content lands here -->
					<tmpl:get name='content'/>
					<!-- content ends above here-->
					<br /><br /></TD>
			</tr>
			<%-- spacers --%>
			<tr valign="top">
				<td><img src="/media_stat/images/layout/clear.gif" alt="" height="1" width="<%=W_DNAV_TOTAL%>"></td>
			</tr>
			<tr valign="bottom">
				<td width="<%=W_DNAV_TOTAL%>"><img src="/media_stat/images/layout/clear.gif" alt="" width="<%=W_DNAV_TOTAL%>" height="5" border="0"></td>
			</tr>
		</table>
	</center>
	<%@ include file="/common/template/includes/footer.jspf" %>
	<%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
</html>
