<%@ page import='java.util.Random' %>
<%@ page import='org.json.JSONObject' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<%
	boolean isEmailPreview = false;
	boolean isPdfPreview = false;
	boolean isRecipTest = false;
	boolean isRecipTestAdd = false;
	boolean isRecipTestSub = false;
	boolean isRecipTestPreview = false;

	Random generator = new Random();
	int ranNumb = generator.nextInt(99) + 1;
	int ranNumbAsID = generator.nextInt(999999) + 1;

	if ( "true".equals ((String)request.getParameter("deliveryMethodEmail")) ) {
		isEmailPreview = true;
	}
	if ( "true".equals ((String)request.getParameter("deliveryMethodPdf")) ) {
		isPdfPreview = true;
	}
	if ( "on".equals ((String)request.getParameter("isPdfPreview")) ) {
		isPdfPreview = true;
	}
	if ( "true".equals ((String)request.getParameter("isRecipTest")) ) {
		isRecipTest = true;
	}
	if ( "true".equals ((String)request.getParameter("isRecipTestAdd")) ) {
		isRecipTestAdd = true;
	}
	if ( "true".equals ((String)request.getParameter("isRecipTestSub")) ) {
		isRecipTestSub = true;
	}	
	if ( "true".equals ((String)request.getParameter("isRecipTestPreview")) ) {
		isRecipTestPreview = true;
	}
	
	System.out.println("isEmailPreview : "+(String)request.getParameter("isEmailPreview"));
	System.out.println("isPdfPreview : "+(String)request.getParameter("isPdfPreview"));
	System.out.println("isRecipTest : "+(String)request.getParameter("isRecipTest"));
	System.out.println("isRecipTestAdd : "+(String)request.getParameter("isRecipTestAdd"));
	System.out.println("isRecipTestSub : "+(String)request.getParameter("isRecipTestSub"));

	String gcBaseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();

	String gcId = request.getParameter("gcTemplateId");				// Gift Card ID (domain value ID), used in paths
	String gcAmount = request.getParameter("gcAmount");		// Gift Card Amount
	String gcRedempCode = request.getParameter("gcRedempCode");	// Gift Card Redemption Code (fake in email preview)
	String gcFor = request.getParameter("gcRecipientName");			// Gift Card "for"
	String gcFrom = request.getParameter("gcBuyerName");			// Gift Card "from"
	String gcMessage = request.getParameter("fldMessage");	// Gift Card "personal message"

	String rowRecipId = request.getParameter("rowRecipId");	// Recipient Row Id

	/* do some validation / limitation */
		%><%@ include file="gc_validation.jspf" %><%
		
		if ( rowRecipId == null ) { rowRecipId = ""; }
	/* end validation */


	// if we're targeting an email preview or a PDF preview
	if (isEmailPreview || isPdfPreview) {

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
	}else{
	// neither an email preview or a PDF preview
		// testing recipient list?
		if (isRecipTest) {

			// testing recipient list ADD?
			if (isRecipTestAdd) {
				String tempId = Integer.toString(ranNumbAsID);
				System.out.println("tempId : "+(String)request.getParameter("tempId"));
				%>
				<div class="recipRow" id="<%= tempId %>Row">
					<div class="recipNumber" id="<%= tempId %>Number"><%= ranNumb %>. </div>
					<div class="recipName" id="<%= tempId %>Name"><%= gcFrom %></div>

					<div class="recipAmount" id="<%= tempId %>Amount"><%= gcAmount %></div>
					<div class="recipLinks" id="<%= tempId %>Links"><a href="#" onclick="emailPreview('<%= tempId %>'); return false;">Preview</a> <a href="#" onclick="editRecip('<%= tempId %>'); remRecip('<%= tempId %>'); return false;">Edit</a> <a href="#" onclick="remRecip('<%= tempId %>'); return false;">Remove</a></div>
				</div>
				<script>updRecipTotal('<%= gcAmount %>');</script>
				<%
			}
			// testing recipient list REMOVE?
			if (isRecipTestSub) {
					//stuff to remove from back-end
				%>
				<script>
					$("<%= rowRecipId %>Row").remove();
					updRecipTotal('<%= gcAmount %>', 'sub');
				</script>
				<%
			}
			// testing recipient list PREVIEW?
			if (isRecipTestPreview) {
					//stuff to remove from back-end
					JSONObject json = new JSONObject();
					json.put("gcId", gcId);
					json.put("gcAmount", gcAmount);
					json.put("gcRedempCode", gcRedempCode);
					json.put("gcFor", gcFor);
					json.put("gcFrom", gcFrom);
					json.put("gcMessage", gcMessage);
					json.put("gcMediaRoot", "/media/editorial/giftcards/"+gcId+"/");
					System.out.println("json.toString : "+json.toString());
				%>
				<%=json.toString()%>
				<%
			}
		}else{
			// neither an email preview or a PDF preview or recipient list test
			%>
				This is NOT an email/pdf preview.<br /><br />
				
				<form action="pdf_generator.jsp" method="POST">
				emailPreview:	<input type="checkbox" id="isEmailPreview" name="isEmailPreview" value="<%= isEmailPreview %>" checked="<%= isEmailPreview %>" /><br />
				gcID:			<input type="text" id="gcId" name="gcId" value="<%= gcId %>" /><br />
				gcAmount:		<input type="text" id="gcAmount" name="gcAmount" value="<%= gcAmount %>" /><br />
				gcRedempCode:	<input type="text" id="gcRedempCode" name="gcRedempCode" value="<%= gcRedempCode %>" /><br />
				gcFor:			<input type="text" id="gcFor" name="gcFor" value="<%= gcFor %>" /><br />
				gcFrom:			<input type="text" id="gcFrom" name="gcFrom" value="<%= gcFrom %>" /><br />
				gcMessage:		<textarea type="text" id="gcMessage" name="gcMessage"><%= gcMessage %></textarea><br />
				
				<br /><br />
				<input type="submit" value="SAVE PDF" name="submit" />
				</form>
			<%
		}
	}
%>


<%
%>