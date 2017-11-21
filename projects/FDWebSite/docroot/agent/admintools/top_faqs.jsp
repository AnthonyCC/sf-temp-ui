<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import="com.freshdirect.storeapi.application.CmsManager" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentKey" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentKeyFactory" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentType" %>
<%@ page import="com.freshdirect.storeapi.ContentNodeI" %>
<%@ page import="com.freshdirect.storeapi.fdstore.FDContentTypes" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%-- Customer ID is not really required in this page, however masquerade context is passed via FDUserI interface --%>
<fd:CheckLoginStatus id="user" />
<%
	// DO NOT ALLOW customers here!
	if (user == null || user.getMasqueradeContext() == null) {
		response.sendRedirect("/");
		return;
	}
%>
<%
CmsManager          manager     = CmsManager.getInstance();
// FIXME FDX version?
final boolean isFd = "FreshDirect".equalsIgnoreCase( manager.getSingleStoreKey().getId() );
final ContentKey key = isFd ? ContentKeyFactory.get(FDContentTypes.FDFOLDER, "FAQ") : ContentKeyFactory.get(FDContentTypes.FDFOLDER, "FAQ_fdx") ;
ContentNodeI contentNode = manager.getContentNode(key);
%><tmpl:insert template='/common/template/no_border_nonav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Top FAQs"/>
  </tmpl:put>
  <tmpl:put name='title'>FreshDirect - Top FAQs</tmpl:put>

<tmpl:put name='content' direct='true'>

<!-- jsp:include page="/includes/admintools_nav.jsp" / -->
<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
<style type="text/css">
/** extracted from crm.css **/
/* SUB NAV */

.sub_nav {
/*position: relative;*/
/*width: 100%;*/
height: auto;
padding: 4px;
padding-left: 6px;
padding-right: 6px;
background-color: #FFFFFF;
border-bottom: 1px #000000 solid;
color: #000000;
font-size: 10pt;
}

.sub_nav_title {
font-size: 12pt;
font-weight: bold;
}

.sub_nav_text {
color: #000000;
font-size: 10pt;
}


/* ----- */

.case_content_red_field {
color: #CC0000;
font-weight: bold;
font-size: 10pt;
}
</style>



<fd:CrmTopFaqController id="faqSubFolders" actionName='<%="saveFaqs"%>' result="saveFaqResult" >
   <form method='POST' name="frmSaveTopFaq">
  		<table width="100%" cellpadding="0" cellspacing="0" class="sub_nav">
			<tr><td>Select Top 5 FAQ to display. Only 5 items can be selected at any time. Display on site will follow the order below. Click on header for details.</td></tr>
		</table>

	<div id="result" class="" style="height:86%;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">     
       
   <%	if (!saveFaqResult.isSuccess() ) {	%>
		<logic:iterate id="errs" collection="<%= saveFaqResult.getErrors() %>" type="com.freshdirect.framework.webapp.ActionError" indexId="idx">
		<tr>
			
			<td colspan="3" align="left" class="case_content_red_field"><%=errs.getDescription()%></td>
			<td width="25%">&nbsp;</td>
		</tr>         
		</logic:iterate>
        
<%   }   %> 
		<tr>
		
			<td colspan="3" align="left" class="case_content_field"><%= pageContext.getAttribute("SUCCESS_MSG")%></td>
			<td width="25%">&nbsp;</td>
		</tr>  
		<tr>
		
			<td colspan="4" >&nbsp;</td>
			
		</tr> 
<%   if (faqSubFolders!=null && faqSubFolders.size() == 0) { %>
		<tr><td colspan="4" align="center"><br><b>There were no FAQs configured.</td></tr>
<%   } else if (faqSubFolders!=null && faqSubFolders.size() > 0) { %>
		
		<logic:iterate id="object" collection="<%= faqSubFolders %>" type="java.lang.Object" indexId="idx">
			
				
				<% if(!(object instanceof ContentNodeI)){
					
					Map map = (Map)object;
					%>
			<tr>
				<td >&nbsp;</td>	    
				<td >
				
				<logic:iterate id="list" collection="<%= map %>"  indexId="idx">
				<% Map.Entry map1 =(Map.Entry)list;
				   ContentNodeI subContent =(ContentNodeI)map1.getKey();				   
				%><b><%= subContent.getLabel() %></b><br/>
				<table>
				<logic:iterate id="children" collection="<%= map1.getValue() %>"  indexId="idx">
				<% ContentNodeI child =(ContentNodeI)children;
				%>
				<tr>
				<% List savedList=(List)pageContext.getAttribute("savedFaqs");
				   List selectedList=(List)pageContext.getAttribute("SELECTED_FAQS");
				   if(null !=child.getAttributeValue("ANSWER") && null !=child.getAttributeValue("QUESTION")){
				  if((saveFaqResult.isSuccess() && savedList.contains(child.getKey().getId())) ||(null != selectedList && selectedList.contains(child.getKey().getId()))){
				%>
				<td width="100%" class="border_bottom"><input name="faqId" type="checkbox" checked="true" value="<%=child.getKey().getId()%>">
					<a STYLE="text-decoration:none" href="#" onclick="return overlib(URLDecode('<%= URLEncoder.encode((String)child.getAttributeValue("ANSWER"))%>'), STICKY, CLOSECLICK, CAPTION, URLDecode('<%= URLEncoder.encode((String)child.getAttributeValue("QUESTION"))%>'), WIDTH, 512,  FGCOLOR, '#FAFDE1', CGCOLOR, '#3B0B0B', CLOSETEXT, '<img src=\'/media_stat/crm/images/close.gif\'>');"> <%=child.getAttributeValue("QUESTION")%>&nbsp;</a></td>
				<% } else { %>
				<td width="100%"  class="border_bottom"><input name="faqId" type="checkbox" value="<%=child.getKey().getId()%>">
					<a STYLE="text-decoration:none" href="#" onclick="return overlib(URLDecode('<%= URLEncoder.encode((String)child.getAttributeValue("ANSWER"))%>'), STICKY, CLOSECLICK, CAPTION, URLDecode('<%= URLEncoder.encode((String)child.getAttributeValue("QUESTION"))%>'), WIDTH, 512,  FGCOLOR, '#FAFDE1',CGCOLOR, '#3B0B0B', CLOSETEXT, '<img src=\'/media_stat/crm/images/close.gif\'>');"> <%=child.getAttributeValue("QUESTION")%>&nbsp;</a></td>
				<% }} %>
				
				</logic:iterate>
				</table><br/>
				</logic:iterate>
				</td>
				<% } %>
				
			</tr>
		</logic:iterate>
<%	 	
	 } %>
	<tr><td colspan="4" class="sub_nav">&nbsp;</td></tr>
	<tr><td colspan="4" align="center"><br><b><input type="submit" name="save" value="SAVE CHANGES" class="checkout"/></b>&nbsp;&nbsp;<b><input type="button" name="clear" value="CLEAR" class="checkout"/></b></td></tr> 
	</table>
	
	
</div>

</form>

</fd:CrmTopFaqController>

<script src="/ccassets/javascript/overlibmws.js"></script>
<script>
	function URLDecode(encStr) {
		// Replace + with ' '
		// Replace %xx with equivalent character
		// Put [ERROR] in output if %xx is invalid.
		var HEXCHARS = "0123456789ABCDEFabcdef";
		var encoded = encStr;
		var plaintext = "";
		var i = 0;
		while (i < encoded.length) {
			var ch = encoded.charAt(i);
			if (ch == "+") {
				plaintext += " ";
				i++;
			} else if (ch == "%") {
				if (i < (encoded.length-2)
						&& HEXCHARS.indexOf(encoded.charAt(i+1)) != -1
						&& HEXCHARS.indexOf(encoded.charAt(i+2)) != -1 ) {
					plaintext += unescape( encoded.substr(i,3) );
					i += 3;
				} else {
					//alert( 'Bad escape combination near ...' + encoded.substr(i) );
					plaintext += "%[ERROR]";
					i++;
				}
			} else {
				plaintext += ch;
				i++;
			}
		} // while
	
		return plaintext;
	}
</script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
</tmpl:put>
</tmpl:insert>