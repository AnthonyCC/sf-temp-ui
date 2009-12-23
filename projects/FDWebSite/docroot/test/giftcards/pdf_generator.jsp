<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.lowagie.text.pdf.*" %>
<%@ page import="org.xhtmlrenderer.pdf.ITextRenderer" %>
<%@ page import="com.lowagie.text.*" %>
<%@ page import="com.freshdirect.webapp.util.MediaUtils" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import='java.util.Random' %><%

	/*
	 *	----------
	 *
	 *		If the SOURCE contains relative URLs, it needs to be called via
	 *		setDocument. Otherwise, the baseURL is null, and any relative
	 *		images will not load. (Unless they're all in the dir with the JSP)
	 *
	 *		Doing it this way will require these imports:
	 *			"org.w3c.dom.Document", 
	 *			"org.xhtmlrenderer.resource.XMLResource", 
	 *			"org.xml.sax.InputSource"
	 *	
	 *		You can set the baseURL (unless you want it to use the jsp's path)
	 *			String baseURL = "http://localhost:7001";
	 *		
	 *		Setup a new InputSource (where SOURCE is the xhtml doc):
	 *			InputSource is = new InputSource(new BufferedReader(new StringReader(SOURCE)));
	 *
	 *		Load to Document object
	 *			Document dom = XMLResource.load(is).getDocument();
	 *
	 *		Set renderer (using baseURL):
	 *			renderer.setDocument(dom, baseURL);
	 *
	 *		Now any releative URLs will use baseURL as the base.
	 *
	 *		If the SOURCE does NOT contain relative URLs, it can be called via
	 *		setDocumentFromString (which always sets the baseURL to null).
	 *
	 *	----------
	 */


	/*****  SETUP INPUT  *****/

		/* input: file */
			// If using an input file, use "renderer.setDocument(dom, baseURL)"
			//String inputFile = "c:\\FreshDirect\\projects\\FDWebSite\\docroot\\test\\giftcards\\test.xhtml";
			//String urlOld = new File(inputFile).toURI().toURL().toString();

		/* input: string */
			// using an FTL requires it to pass through MediaUtils.render first (for FTL tags)

			//we would NOT want to just pass through the form values in prod
			
			String gcBaseUrl = FDStoreProperties.getMediaPath();

			//String gcBaseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();

			String gcId = request.getParameter("gcTemplateId");           // Gift Card ID (domain value ID), used in paths
			String gcAmount = request.getParameter("fldAltAmount");       // Gift Card Amount
			String gcRedempCode = request.getParameter("gcRedempCode");   // Gift Card Redemption Code (fake in email preview)
			String gcFor = request.getParameter("gcRecipientName");       // Gift Card "for"
			String gcFrom = request.getParameter("gcBuyerName");          // Gift Card "from"
			String gcMessage = request.getParameter("fldMessage");        // Gift Card "personal message"
			String gcIsPDF = "true";                                      // signal to ftl that we're making a PDF

			/* do some validation / limitation */
				%><%@ include file="gc_validation.jspf" %><%

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

			//for testing, we'll use a fallback ftl
			if (gcId == "") { gcId = "giftcard_type_test5"; }

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
			MediaUtils.render(ftlPath, sw, params, false);


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

		/* output : file */
			//String outputFile = "c:\\FreshDirect\\projects\\FDWebSite\\docroot\\test\\giftcards\\FD_"+ranNumbFilename+".pdf";
			//OutputStream os = new FileOutputStream(outputFile);
	

	/*****  GENERATE OUTPUT  *****/

		//setup renderer
		ITextRenderer renderer = new ITextRenderer();

			/* set document : setDocument */
				//String baseURL = "http://localhost:7001";
				//InputSource is = new InputSource(new BufferedReader(new StringReader(sw.toString())));
				//Document dom = XMLResource.load(is).getDocument();
				//renderer.setDocument(dom, baseURL);
		
			/* set document : setDocumentFromString */
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
	
	// Debug Prints ******************************
		//System.out.println("----- inputFile : "+inputFile);
		//System.out.println("----- URL inputFile : "+inputFile);
		//System.out.println("----- getContextPath : "+request.getContextPath());
		//System.out.println("----- URL : "+url);
		//System.out.println("----- sw.toString() : "+sw.toString());
	// Debug Prints ******************************

%>