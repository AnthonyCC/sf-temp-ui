<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='org.json.JSONObject' %>

<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.common.customer.EnumServiceType' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.delivery.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>

<%
	boolean isEmailPreview = false;
	boolean isPdfPreview = false;
	boolean isRecipPreview = false;

	
	if ( "true".equals((String)request.getParameter("isEmailPreview")) ) {
		isEmailPreview = true;
	}
	if ( "true".equals((String)request.getParameter("isPdfPreview")) ) {
		isPdfPreview = true;
	}
	if ( "true".equals((String)request.getParameter("isRecipPreview")) ) {
		isRecipPreview = true;
	}

	// if we're targeting an email preview or a PDF preview
	if (isEmailPreview || isPdfPreview) {

		String gcBaseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();

		String gcId = request.getParameter("gcId");				// Gift Card ID (domain value ID), used in paths
		String gcAmount = request.getParameter("gcAmount");		// Gift Card Amount
		String gcRedempCode = request.getParameter("gcRedempCode");	// Gift Card Redemption Code (fake in email preview)
		String gcFor = request.getParameter("gcFor");			// Gift Card "for"
		String gcFrom = request.getParameter("gcFrom");			// Gift Card "from"
		String gcMessage = request.getParameter("gcMessage");	// Gift Card "personal message"

		// these values come in the request, so pass through validation
		/* do some validation / limitation */
			%><%@ include file="pb_validation.jspf" %><%
		/* end validation */

		String mediaPath = "";

		//params is passed to the FTL
		Map params = new HashMap();

		params.put("gcId", gcId);
		params.put("gcAmount", gcAmount);
		params.put("gcRedempCode", gcRedempCode);
		params.put("gcFor", gcFor);
		params.put("gcFrom", gcFrom);
		params.put("gcMessage", gcMessage);
		params.put("gcBaseUrl", gcBaseUrl);
		params.put("gcMediaRoot", "/media/editorial/giftcards/"+gcId+"/");
		params.put("gcIsPDF", ""); // signal to ftl that we're NOT making a PDF

		// if an email preview, target email ftl
		if (isEmailPreview) {
			mediaPath = "/media/editorial/giftcards/"+gcId+"/email_template.ftl";
		}

		// if PDF preview, target PDF ftl
		if (isPdfPreview) {
			
			/* testing
				mediaPath = "/media/editorial/giftcards/_TEMPLATE-email_preview/pdf_template.ftl";
				params.put("gcMediaRoot", "/media/editorial/giftcards/_TEMPLATE-email_preview/");
			*/
			mediaPath = "/media/editorial/giftcards/"+gcId+"/pdf_template.ftl";
		}
		

		%>
		<fd:IncludeMedia name="<%= mediaPath %>" parameters="<%=params%>"/>
		<%
		
	
	} else if (isRecipPreview) {
		/*
		 *	data is coming from the stored session, so no validation (again)
		 *	return as a JSON string. we always want to return a JSON string,
		 *	if there's no data/error, just use
		 *		json.put("status", "error");
		 *	and we'll catch it on the return process (in javascript)
		 */

		String recipId = request.getParameter("recipId");	// ID of the recip to get data for

		JSONObject json = new JSONObject();
			json.put("status", "ok");

		if (recipId == null || recipId.trim().length() <= 0) {
			json.put("status", "error");
		}else{
			
			FDUserI _user = (FDUserI) session.getAttribute(SessionName.USER);
			FDIdentity _identity = null;
			if (_user != null){
				_identity = _user.getIdentity();
			}else {
				throw new JspException("Missing Identity.");
			}

			SavedRecipientModel recipModel = null;

			recipModel = (SavedRecipientModel) _user.getRecipientList().getRecipientById(recipId);

			
			json.put("gcId", recipModel.getTemplateId());
			json.put("gcAmount", Double.toString(recipModel.getAmount()));
			json.put("gcRedempCode", "fake code");
			json.put("gcFor", recipModel.getRecipientName());
			json.put("gcFrom", recipModel.getSenderName());
			json.put("gcMessage", recipModel.getPersonalMessage());
			json.put("gcMediaRoot", "/media/editorial/giftcards/"+recipModel.getTemplateId()+"/");
			json.put("gcDeliveryType", recipModel.getDeliveryMode().getName());
			
			%><%=json.toString()%><%
		}
		
	}

%>