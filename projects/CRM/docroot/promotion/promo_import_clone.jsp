<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionNewModel"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%@ page import="com.freshdirect.webapp.util.json.FDPromotionJSONSerializer"%>
<%@ page import="com.metaparadigm.jsonrpc.JSONSerializer"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Promotion Details</tmpl:put>
	<%-- Moved JS out into promo.js file --%>
	<tmpl:put name='styles' direct='true'>
		<script type="text/javascript" language="javascript" src="/assets/javascript/promo.js"></script>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
<crm:GetCurrentAgent id='currentAgent'>
	<crm:ImportPromotion promo="promotion" fieldName="promoFile" result="result" jsonContent="jsonForm">
	<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
<%
	final BrowserInfo bi = new BrowserInfo(request);

	if (!result.isSuccess()) {
%>
	<div style="padding-top: 2em; width: 400px; margin-left: auto; margin-right: auto">
	<fd:ErrorHandler id="errorMsg" result="<%= result %>" name="promo.import">
	   <%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>
	</div>
<%		
	} 


	// SAVE pressed in form, save the promo now
	final boolean savePromo = promotion != null && "store".equalsIgnoreCase(request.getParameter("action"));
	ActionResult storeResult = null;
	
	if (promotion != null) {
		if (savePromo) {
%><crm:StoreImportedPromotion result="sResult" actionName="store" promotion="<%= promotion %>" agent="<%= currentAgent %>"><% storeResult = sResult; %></crm:StoreImportedPromotion><%
		}

		// promotion is just imported (no save happened) or save failed
		boolean shouldShowForm = storeResult == null || !storeResult.isSuccess();

		// saving promotion failed
		boolean shouldDisplayErrors = storeResult != null && !storeResult.isSuccess();
		
%>
<% if (bi.isInternetExplorer()) { %>
<style type="text/css">
.pic-frame form {
	padding: 1em 1em;
	display: inline-table;
	border: solid 2px #ddd;
	text-align: left;
	width: 490px;
}

.pic-row div {
	width: 9em;
	display: inline;
	font-weight: bold;
	color: #666;
}

.pic-row input {
	border: 1px solid black;
}


.pic-row-err {
	height: 2em;
}

.pic-row-err div {
	width: 9em;
	display: inline;
}


.pic-err div {
	color: #c00;
	font-weight: bold;
}
</style>
<% } else { %>
<style type="text/css">
	
.pic-frame form {
	padding: 1em 1em;
	display: inline-table;
	border: solid 2px #ddd;
	text-align: left;

	-moz-border-radius: 7px;
	-webkit-border-radius: 7px;
}
	
.pic-row div {
	width: 9em;
	display: inline-block;
	font-weight: bold;
	color: #666;
}

.pic-row input {
	border: 1px solid black;
}


.pic-row-err {
	height: 2em;
}

.pic-row-err div {
	width: 9em;
	display: inline-block;
}


.pic-err div {
	color: #c00;
	font-weight: bold;
}
</style>
<% } %>
	<div class="pic-frame" style="padding-top: 3em; text-align: center;">
<%
		if (shouldShowForm) {
%>
		<form method="post">
			<input type="hidden" name="action" value="store"/>

<%			if (storeResult == null) {
%>			<div class="title18" style="padding: 0.3em 0 1em 0;">Promotion has been <%= "clone".equalsIgnoreCase(request.getParameter("mode")) ? "cloned" : "imported" %> successfully!</div>
<%			} else if (!storeResult.isSuccess()) {
%>			<div class="title18" style="padding: 0.3em 0 1em 0; color: #c00; font-weight: bold;">Save failed.</div>
<%			}
%>			<div class="pic-row">
				<div>Name:</div>
				<input id="promoname-input" type="text" name="newName" value="<%= promotion.getName() %>" size="16" maxlength="32"/>
				<button type="button" onclick="doCheckName($('promoname-input').value); return false;">Check</button>
			</div>
			<div class="pic-row-err">
				<div>&nbsp;</div>
				<span id="name-check-ok-div" style="display: none">Name is OK.</span>
				<span id="name-check-err-div" class="error" style="display: none">Name used.</span>
			</div>

			<div class="pic-row">
				<div>Promotion code:</div>
				<input id="promocode-input" type="text" name="newPromoCode" value="<%= promotion.getPromotionCode() %>" size="16" maxlength="16"/>
				<button type="button" onclick="doCheckCode($('promocode-input').value); return false;">Check</button>
			</div>
			<div class="pic-row-err">
				<div>&nbsp;</div>
				<span id="code-check-ok-div" style="display: none">Code is OK.</span>
				<span id="code-check-err-div" class="error" style="display: none">Code used.</span>
			</div>

			<div class="pic-row">
				<div>Redemption code:</div>
				<input id="rcode-input" type="text" name="newRedemptionCode" value="<%= promotion.getRedemptionCode() %>" size="16" maxlength="16"/>
				<button type="button" onclick="doCheckRedemptionCode($('rcode-input').value); return false;">Check</button>
			</div>
			<div class="pic-row-err">
				<div>&nbsp;</div>
				<span id="rcode-check-ok-div" style="display: none">Redemption Code is OK.</span>
				<span id="rcode-check-err-div" class="error" style="display: none">Redemption Code used.</span>
			</div>

			<textarea style="display: none" name="promoFile"><crm:SerializePromotion promo="<%= promotion %>"/></textarea>
			<button type="submit" style="x-padding: 0.5em 0.5em; x-margin-top: 1em;">SAVE</button>
<%
			if (storeResult != null) {
				if (!storeResult.isSuccess()) {
%>			<div class="pic-err" style="padding: 1em 0">
<%
					for (ActionError err : storeResult.getErrors()) {
%>						<div><%=  err.getDescription() %></div>
<%
					}
				}
%>			</div>
<%
			}
%>
		</form>
<%		} else {
			// red
			response.sendRedirect("/promotion/promo_details.jsp?promoId=" + promotion.getPromotionCode());
		}
%>	</div>
<%
	}
%>
	</crm:ImportPromotion>
</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>
