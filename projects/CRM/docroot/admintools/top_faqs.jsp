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
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import="com.freshdirect.cms.application.CmsManager" %>
<%@ page import="com.freshdirect.cms.application.ContentTypeServiceI" %>
<%@ page import="com.freshdirect.cms.ContentKey" %>
<%@ page import="com.freshdirect.cms.ContentNodeI" %>
<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%
CmsManager          manager     = CmsManager.getInstance();
ContentTypeServiceI typeService = manager.getTypeService();
ContentKey key = new ContentKey(FDContentTypes.FDFOLDER, "FAQ");
ContentNodeI contentNode = manager.getContentNode(key);
%>
<style type="text/css">
.case_content_red_field {
color: #CC0000;
font-weight: bold;
font-size: 10pt;
}
</style>
<!-- <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div> -->
<script language="JavaScript" src="/ccassets/javascript/overlibmws.js"></script>
<script language="JavaScript">
function clearCheckbox(obj){
	for(var i=0; i<obj.length; i++)    
	    obj[i].checked = false;
}
</script>
<tmpl:insert template='/template/top_nav.jsp'>


<tmpl:put name='title' direct='true'> Top FAQs</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/admintools_nav.jsp" />

<crm:CrmTopFaqController id="faqSubFolders" actionName='<%="saveFaqs"%>' result="saveFaqResult" >
   <form method='POST' name="frmSaveTopFaq">
  		<table width="100%" cellpadding="0" cellspacing="0" class="sub_nav">
			<tr><td>Select Top 5 FAQ to display. Only 5 items can be selected at any time. Display on site will follow the order below. Click on header for details.</td></tr>
		</table>
	<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">
    
 
  

	</table>
	
	<div id="result" class="list_content" style="height:70%;">
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
				  if((saveFaqResult.isSuccess() && savedList.contains(child.getKey().getId())) ||(null != selectedList && selectedList.contains(child.getKey().getId()))){
				%>
				<td width="100%" class="border_bottom"><input name="faqId" type="checkbox" checked="true" onClick="countChecked(this);" value="<%=child.getKey().getId()%>">
					<a STYLE="text-decoration:none" href="#" onclick="return overlib(URLDecode('<%= URLEncoder.encode((String)child.getAttributeValue("ANSWER"))%>'), STICKY, CLOSECLICK, CAPTION, URLDecode('<%= URLEncoder.encode((String)child.getAttributeValue("QUESTION"))%>'), WIDTH, 512,  FGCOLOR, '#FAFDE1', CGCOLOR, '#3B0B0B', CLOSETEXT, '<img src=\'/media_stat/images/close_icon.GIF\'>');"> <%=child.getAttributeValue("QUESTION")%>&nbsp;</a></td>
				<% } else { %>
				<td width="100%"  class="border_bottom"><input name="faqId" type="checkbox"  onClick="countChecked(this);" value="<%=child.getKey().getId()%>">
					<a STYLE="text-decoration:none" href="#" onclick="return overlib(URLDecode('<%= URLEncoder.encode((String)child.getAttributeValue("ANSWER"))%>'), STICKY, CLOSECLICK, CAPTION, URLDecode('<%= URLEncoder.encode((String)child.getAttributeValue("QUESTION"))%>'), WIDTH, 512,  FGCOLOR, '#FAFDE1',CGCOLOR, '#3B0B0B', CLOSETEXT, '<img src=\'/media_stat/images/close_icon.GIF\'>');"> <%=child.getAttributeValue("QUESTION")%>&nbsp;</a></td>
				<% } %>
				
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
	<tr><td colspan="4" align="center"><br><b><input type="submit" name="save" value="SAVE CHANGES" class="checkout"/></b>&nbsp;&nbsp;<b><input type="button" name="clear" value="CLEAR" class="checkout" onclick="clearCheckbox(this.form.faqId)"/></b></td></tr> 
	</table>
	
	
</div>

</form>
<script language="javascript">
	<!--
	var customersSelected =0;
	function countChecked(cbObj) {
		
		//alert(cbObj.checked)
		if (cbObj.checked) {
			customersSelected++;
		} else {
			customersSelected--;
		}
		showSelected();
	}
	
	function showSelected() {
		var outputObj = document.forms["frmSaveTopFaq"].customersSelected;
		if (customersSelected<=0) customersSelected=0;
		outputObj.value=customersSelected;
	}
	
	function checkAll(flag) {
		var elements = document.forms["frmSaveTopFaq"].elements;
		for (var i=0;i<elements.length;i++) {
			//alert(elements[i].name+" / "+ elements[i].type);
            //100 + 3 pre-existing elements in this form
           if(i < 103) {
			if (elements[i].name=="faqId" && elements[i].type=="checkbox") {
				if (flag && !elements[i].checked) {
					elements[i].checked=true;
					customersSelected++;
				}
				if (!flag && elements[i].checked) {
				   elements[i].checked=false;
				   customersSelected--;
				}
			}
           }
           showSelected();
		}
	}
	
	
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.customer_status.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>

</crm:CrmTopFaqController>
</tmpl:put>
</tmpl:insert>