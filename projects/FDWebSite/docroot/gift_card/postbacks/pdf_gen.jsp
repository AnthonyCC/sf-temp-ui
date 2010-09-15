<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.lowagie.text.pdf.*" %>
<%@ page import="org.xhtmlrenderer.pdf.ITextRenderer" %>
<%@ page import="com.lowagie.text.*" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.MediaUtils" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.common.pricing.PricingContext" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@page import="com.freshdirect.fdstore.ZonePriceListing"%>
<%@ page import='java.util.Random' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
    FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
/*****  SETUP INPUT  *****/
	/* input: string (FTL) */
	
	// using an FTL requires it to pass through MediaUtils.render first (for FTL tags)

		//we would NOT want to just pass through the form values in prod

    String gcBaseUrl = FDStoreProperties.getMediaPath();
	/*
		THESE NEED TO COME FROM THE SERVER-SIDE
		the validation file also checks these, so either update it or remove it's include
	*/
        String saleId = request.getParameter("saleId");
        String certNum = request.getParameter("certNum");
        //saleId= "2150644334";
        //certNum = "44468";
        
        if (saleId == null || saleId.trim().length() <= 0 || certNum == null || certNum.trim().length() <= 0) {
            //Do nothing
        } else {    %>
         <fd:GetGiftCardRecipientDlvInfo id="dlvInfo" saleId="<%= saleId %>" certificationNum="<%= certNum %>">
    <%      
                String gcId = "";			// Gift Card ID (domain value ID), used in paths
                String gcAmount = ""; 		// Gift Card Amount
                String gcRedempCode = "";	// Gift Card Redemption Code (fake in email preview)
                String gcFor = "";			// Gift Card "for"
                String gcFrom = "";			// Gift Card "from"
                String gcMessage = "";		// Gift Card "personal message"
            if(dlvInfo != null){
                gcId = dlvInfo.getRecepientModel().getTemplateId();                            // Gift Card ID (domain value ID), used in paths
                gcAmount = String.valueOf(dlvInfo.getRecepientModel().getFormattedAmount());   // Gift Card Amount
                gcRedempCode = dlvInfo.getGivexNum();                                          // Gift Card Redemption Code (fake in email preview)
                gcFor = dlvInfo.getRecepientModel().getRecipientName();                        // Gift Card "for"
                gcFrom = dlvInfo.getRecepientModel().getSenderName();                          // Gift Card "from"
                gcMessage = dlvInfo.getRecepientModel().getPersonalMessage();                  // Gift Card "personal message"
           
            }
				String gcIsPDF = "true";	// signal to ftl that we're making a PDF

	/* do some validation / limitation */
		%><%@ include file="/gift_card/postbacks/pb_validation.jspf" %><%

		/* prevent opening and closing CDATA tags in string */
		gcFor = gcFor.replaceAll("<!\\[CDATA\\[", "<! [CDATA[").replaceAll("]]>", "]] >");
		gcFrom = gcFrom.replaceAll("<!\\[CDATA\\[", "<! [CDATA[").replaceAll("]]>", "]] >");
		gcMessage = gcMessage.replaceAll("<!\\[CDATA\\[", "<! [CDATA[").replaceAll("]]>", "]] >");
		
		/*
		 *	fix for '&' in a xhtml file
		 * 	escape data in CDATA tags for xml
		 */
		gcFor = "<![CDATA[" + gcFor + "]]>";
		gcFrom = "<![CDATA[" + gcFrom + "]]>";
		gcMessage = "<![CDATA[" + gcMessage + "]]>";

	/* end validation */

	//make sure we have an ID so we know which ftl to use
	if (gcId != "") {
		//gcId = "giftcard_type_thanksgiving";

		//set params for FTL
		Map params = new HashMap();
			params.put("gcId", gcId);
			params.put("gcAmount", gcAmount);
			params.put("gcRedempCode", gcRedempCode);
			params.put("gcFor", gcFor);
			params.put("gcFrom", gcFrom);
			params.put("gcMessage", gcMessage);
			params.put("gcBaseUrl", gcBaseUrl);
			params.put("gcMediaRoot", gcBaseUrl+"/media/editorial/giftcards/"+gcId+"/");
			params.put("gcIsPDF", gcIsPDF);	// signal to ftl that we're making a PDF

		//setup string writer
		StringWriter sw = new StringWriter();

		//FTL path
		String ftlPath = "/media/editorial/giftcards/"+gcId+"/pdf_template.ftl";


		//call ftl render
		MediaUtils.render(ftlPath, sw, params, false, user.getPricingContext() != null ? 
                user.getPricingContext() : new PricingContext(ZonePriceListing.MASTER_DEFAULT_ZONE));


		/*****  SETUP OUTPUT  *****/

			//setup a random string for the filename
			Random generator = new Random();
			StringBuffer ranNumbFilename = new StringBuffer();
			char[] buf = new char[16];
			String charset = "abcdefghijklmonpqrstuvwxyz0123456789";
			for (int i = 0; i < buf.length; i++) {
				ranNumbFilename.append(charset.charAt(generator.nextInt(charset.length())));
			}

			/* output : browser */
				OutputStream os = response.getOutputStream();

		/*****  GENERATE OUTPUT  *****/

			//setup renderer
			ITextRenderer renderer = new ITextRenderer();

			// setDocumentFromString
					renderer.setDocumentFromString(sw.toString());

			//setup layout
			renderer.layout();

			/*
			 *	don't use: response.setContentType OR response.setHeader
			 *	when saving only as a file (otherwise, user ends up with a bad PDF)
			 */

			//set content type so we return a pdf
			response.setContentType("application/pdf");

			//set header to force download/open and set filename
			response.setHeader("Content-Disposition","attachment; filename=FD_"+ranNumbFilename+".pdf");

			//create PDF
			renderer.createPDF(os);
			
			//close stream
			os.close();
	}else{
		//since we can't return an error (we're assuming the file is returned)
	}
    %>
</fd:GetGiftCardRecipientDlvInfo>    
<%
}
%>
