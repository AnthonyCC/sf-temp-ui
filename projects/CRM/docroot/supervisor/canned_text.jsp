<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.CrmManager"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %><%
	String action = NVL.apply(request.getParameter("action"), "");
	String actionId = NVL.apply(request.getParameter("id"), "");

	String id = NVL.apply(request.getParameter("ct_id"), "");
	String name = NVL.apply(request.getParameter("ct_name"), "");
	String catName = NVL.apply(request.getParameter("ct_category"), "");
	EnumCannedTextCategory category = EnumCannedTextCategory.getEnum(catName);
	String text = NVL.apply(request.getParameter("ct_text"), "");

	ActionResult result = new ActionResult();
	boolean createOrEdit = "create".equals(action) || "edit".equals(action);
	if (createOrEdit) {
		
		result.addError(id.trim().length() == 0, "ct_id", "ID must be specified");
		result.addError(id.length() > 10, "ct_id", "ID must be no longer than 10 characters");
		if ("create".equals(action))
			result.addError(CrmManager.getInstance().getCannedTextById(id) != null, "ct_id", "ID already exists");
		else
			result.addError(CrmManager.getInstance().getCannedTextById(id) != null
					&& !id.equals(request.getParameter("id")), "ct_id", "ID already exists");
		result.addError(name.trim().length() == 0, "ct_name", "Name must be specified");
		result.addError(name.length() > 40, "ct_name", "Name must be no longer than 40 characters");
		result.addError(category == null, "ct_category", "No such category");
		result.addError(text.trim().length() == 0, "ct_text", "Text must be specified");
		result.addError(text.length() > 1024, "ct_text", "Text must be no longer than 1024 characters");
		
		if (result.getErrors().size() == 0) {
			try {
				if ("create".equals(action))
					CrmManager.getInstance().createCannedText(new ErpCannedText(id, name, category, text));
				else
					CrmManager.getInstance().updateCannedText(new ErpCannedText(id, name, category, text), actionId);
			} catch (Exception e) {
				result.addError(true, "ct_general", "Failed to " + action + " canned text, check logs for details");
			}
		}
	} else if ("delete".equals(action)) {
		try {
			CrmManager.getInstance().deleteCannedText(actionId);
		} catch (Exception e) {
			result.addError(true, "ct_general", "Failed to " + action + " canned text, check logs for details");
		}
	}
%>
<%@page import="com.freshdirect.framework.util.NVL"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%><link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
<style type="text/css">
body {
	background-color: #FFFFFF;
}
.editor td {
	padding-top: 10px;
	vertical-align: top;
}
</style>
<script>
    function doAction(action, id, name, category, text) {
        if (action == "submit")
			document.forms[0].submit();	
            
		document.getElementById("action").value = action;
		if (id)
			document.getElementById("hidden_id").value = id;

		if (action == "edit") {
			document.getElementById("ct_id").value = id;
			document.getElementById("ct_name").value = name;
			var catSel = document.getElementById("ct_category");
			for (var i = 0; i < catSel.options.length; i++)
				if (catSel.options[i].value == category) {
					catSel.options[i].selected = true;
					break;
				}
			document.getElementById("ct_text").value = text;
		}
		if (action == "create") {
			document.getElementById("ct_id").value = "";
			document.getElementById("ct_name").value = "";
			var catSel = document.getElementById("ct_category");
			catSel.options[0].selected = true;
			document.getElementById("ct_text").value = "";
		}

		if (action == "create" || action == "edit")
			showEditor();
		else if (action == "delete" && confirm("Are you sure you want to delete canned text '" + id + "'?"))
			document.forms[0].submit();	
    }

    var orig_display = null;

    function showEditor() {
        orig_display = document.getElementById("ct_editor").style.display != "none" ?
        		document.getElementById("ct_editor").style.display :
        		document.getElementById("ct_list").style.display;
        document.getElementById("ct_list").style.display = "none";
        document.getElementById("ct_editor").style.display = orig_display;
    }

    function hideEditor() {
		document.getElementById("action").value = "";
        orig_display = document.getElementById("ct_editor").style.display != "none" ?
        		document.getElementById("ct_editor").style.display :
        		document.getElementById("ct_list").style.display;
        document.getElementById("ct_editor").style.display = "none";
        document.getElementById("ct_list").style.display = orig_display;
    }
</script>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Canned Text</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/supervisor_nav.jsp" />

<form name="cannedText" method="POST">
<input type="hidden" name="action" id="action" value="<%= action %>">
<input type="hidden" name="id" id="hidden_id" value="<%= actionId %>">
<table id="ct_list" width="100%" cellpadding="0" cellspacing="3" bgcolor="#FFFFFF"<%= createOrEdit && result.getErrors().size() != 0 ? " style=\"display: none;\"" : "" %>> 
	<tr>
		<td colspan="9" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
			<input type="button" value="CREATE NEW" class="submit" onclick="javascript:doAction('create');">&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
	<td valign="bottom" class="border_bold" nowrap="nowrap"><span class="detail_text"><b>ID</b></span></td>	
	<td class="border_bold" nowrap="nowrap">&nbsp;</td>
	<td valign="bottom" class="border_bold" nowrap="nowrap"><span class="detail_text"><b>Name</b></span></td>	
	<td class="border_bold" nowrap="nowrap">&nbsp;</td>
	<td valign="bottom" class="border_bold" nowrap="nowrap"><span class="detail_text"><b>Category</b></span></td>	
	<td class="border_bold" nowrap="nowrap">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Text</b></span></td>	
	<td class="border_bold" style="text-align: right;" nowrap="nowrap">&nbsp;</td>
	<td valign="bottom" class="border_bold" style="text-align: right;" nowrap="nowrap"><span class="detail_text"><b>Commands</b></span></td>	
	</tr>
	
	<%
		Collection allCt = CrmManager.getInstance().getAllCannedText();
		if(allCt ==  null || allCt.size() == 0) {
	%>
	<tr><td><span class="error">No canned text.</span></td></tr>

	<%
		} else {
	%>	

	<logic:iterate id="cannedText" collection="<%= allCt %>" type="com.freshdirect.customer.ErpCannedText">
	<tr>
	<td class="border_bottom" nowrap="nowrap"><span class="detail_text"><%= cannedText.getId() %></span></td>	
	<td class="border_bottom" nowrap="nowrap">&nbsp;</td>
	<td class="border_bottom" nowrap="nowrap"><span class="detail_text"><%= cannedText.getName() %></span></td>
	<td class="border_bottom" nowrap="nowrap">&nbsp;</td>
	<td class="border_bottom" nowrap="nowrap"><span class="detail_text"><%= cannedText.getCategory().getDescription() %></span></td>
	<td class="border_bottom" nowrap="nowrap">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text"><%= cannedText.getText() %></span></td>
	<td class="border_bottom" style="text-align: right;" nowrap="nowrap">&nbsp;</td>
	<td class="border_bottom" style="text-align: right;" nowrap="nowrap">
		<input type="button" name="edit" value="EDIT" class="submit" onclick="javascript:doAction('edit', '<%= StringEscapeUtils.escapeJavaScript(cannedText.getId()) %>',
				'<%= StringEscapeUtils.escapeJavaScript(cannedText.getName()) %>', '<%= StringEscapeUtils.escapeJavaScript(cannedText.getCategory().getName()) %>', '<%= StringEscapeUtils.escapeJavaScript(cannedText.getText()) %>');">
		<input type="button" name="delete" value="DELETE" class="submit" onclick="javascript:doAction('delete', '<%= StringEscapeUtils.escapeJavaScript(cannedText.getId()) %>');">
	</td>
	</tr>
	</logic:iterate>
	<tr>
		<td colspan="9" align="center">
			<img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
			<input type="button" value="CREATE NEW" class="submit" onclick="javascript:doAction('create');">&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
	<td colspan="9">
		<fd:ErrorHandler result='<%=result%>' name='ct_general' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
	</td>
	</tr>
</table>
<table id="ct_editor" width="100%" cellpadding="0" cellspacing="3" bgcolor="#FFFFFF"<%= createOrEdit && result.getErrors().size() != 0 ? "" : " style=\"display: none;\"" %>> 
	<tr>
	<td valign="bottom" class="border_bold" nowrap="nowrap"><span class="detail_text"><b>ID</b></span></td>	
	<td class="border_bold" nowrap="nowrap">&nbsp;</td>
	<td valign="bottom" class="border_bold" nowrap="nowrap"><span class="detail_text"><b>Name</b></span></td>	
	<td class="border_bold" nowrap="nowrap">&nbsp;</td>
	<td valign="bottom" class="border_bold" nowrap="nowrap"><span class="detail_text"><b>Category</b></span></td>	
	<td class="border_bold" nowrap="nowrap">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Text</b></span></td>	
	</tr>
	<tr id="create_row" class="editor">
	<td class="border_bottom" nowrap="nowrap"><input type="text" name="ct_id" id="ct_id" size="10" style="width: 80px;" value="<%= id %>"></td>	
	<td class="border_bottom" nowrap="nowrap">&nbsp;</td>
	<td class="border_bottom" nowrap="nowrap"><input type="text" name="ct_name" id="ct_name" size="40" style="width: 200px;" value="<%= name %>"></td>
	<td class="border_bottom" nowrap="nowrap">&nbsp;</td>
	<td class="border_bottom" nowrap="nowrap">
		<select name="ct_category" id="ct_category"><% Iterator cats = EnumCannedTextCategory.getEnumList().iterator();
			while (cats.hasNext()) {
				EnumCannedTextCategory cat = (EnumCannedTextCategory) cats.next(); %>
			<option value="<%= cat.getName() %>"<%= cat.getName().equals(catName) ? " selected" : "" %>><%= cat.getDescription() %></option>
		<%  } %></select>
	</td>
	<td class="border_bottom" nowrap="nowrap">&nbsp;</td>
	<td class="border_bottom" colspan="3" nowrap="nowrap">
		<div style="float: left;">
		<textarea name="ct_text" id="ct_text" cols="80" rows="4" style="width: 400px;"><%= text %></textarea><br>
		<input type="button" value="SAVE" class="submit" id="save_button" onclick="javascript:doAction('submit');">&nbsp;&nbsp;
		<input type="button" value="CANCEL" class="submit" onclick="javascript:hideEditor();">
		</div>
		<div style="float: left; padding-left: 10px;">
		<fd:ErrorHandler result='<%=result%>' name='ct_id' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
		<fd:ErrorHandler result='<%=result%>' name='ct_name' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
		<fd:ErrorHandler result='<%=result%>' name='ct_category' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
		<fd:ErrorHandler result='<%=result%>' name='ct_text' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
		</div>
		<div style="clear: both;"></div>
	</td>
	</tr>
	<tr>
	<td colspan="7">
		<fd:ErrorHandler result='<%=result%>' name='ct_general' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
	</td>
	</tr>
	<%
		}
	%>
</table>
</form>

</tmpl:put>
</tmpl:insert>