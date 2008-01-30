<%@ page import='org.dom4j.*' %>
<%@ page import='com.freshdirect.fdstore.mail.*' %>
<%@ page import='com.freshdirect.framework.mail.XMLEmailI' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<html>
<body>
<%!

	private static final String[] ENTITIES = new String[64];
	static {
		ENTITIES['"'] = "&quot;";
		ENTITIES['<'] = "&lt;";
		ENTITIES['>'] = "&gt;";
		ENTITIES['&'] = "&amp;";
	}
 
	public static void printEscaped(JspWriter out, String str) throws IOException {
    	char[] chrs = str.toCharArray();
    	for (int i=0; i<chrs.length; i++) {
    		char c=chrs[i];
    		if (c<64 && ENTITIES[c]!=null) {
    			out.print(ENTITIES[c]);
    		} else {
    			out.print(c);
    		}
    	}
	}
%>
<%
	String addresses = request.getParameter("addresses");
	if (addresses ==null) {
		addresses = "maroy@euedge.com";
	}

	String subject = request.getParameter("subject");
	if (subject==null) {
		subject = "FreshDirect";
	}

    String recipeId = request.getParameter("recipeId");
    if (recipeId == null) {
        recipeId = "recwk_frittata_mint";
    }
    String variantId = request.getParameter("variantId");

    RecipeVariant   variant;
    Recipe          recipe;

    if (variantId != null) {
        variant = (RecipeVariant) ContentFactory.getInstance().getContentNode(variantId);
        recipe = (Recipe) variant.getParentNode();

    } else if (recipeId !=null) {
        recipe = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
        variant = recipe.getDefaultVariant();

    } else {
        throw new IllegalArgumentException("No variantId or recipeId supplied");
    }

	if ("POST".equals(request.getMethod())) {
		List customers = new ArrayList();
		StringTokenizer tok = new StringTokenizer(addresses, "\n\r,;");
		while (tok.hasMoreTokens()) {
			String addr = tok.nextToken().trim();
			System.out.println("Finding "+addr);
			
			//FDCustomerModel fdCust = FDCustomerFactory.getFDCustomerByUserId(addr);
			//FDIdentity id = new FDIdentity(fdCust.getErpCustomerPK(), fdCust.getPK().getId());
			//FDCustomerInfo custInfo = FDCustomerManager.getCustomerInfo(id);

			FDCustomerInfo custInfo = new FDCustomerInfo("Dummy", "Dummy");
			custInfo.setEmailAddress( addr );
			custInfo.setHtmlEmail( true );
			customers.add( custInfo );
		}
System.out.println("customers: " + customers.size());

        for (Iterator i=customers.iterator(); i.hasNext(); ) {
            XMLEmailI m = FDEmailFactory.getInstance().createRecipeEmail( (FDCustomerInfo)i.next(), recipe);
            FDCustomerManager.doEmail(m);
        }

	}
%>
<form action="recipe.jsp" method="post">

<table width="90%">
<tr>
<td>
<b>To:</b><br/>
<textarea name="addresses" cols="30" rows="10"><%= addresses %></textarea>
</td>

<td>
<b>Subject:</b> <input type="text" name="subject" value="<%= subject %>" />

<br/><br/>

<textarea name="recipeId" cols="80" rows="10"><% printEscaped(out, recipeId); %></textarea>

<br/>

<input type="submit"/>
</td>
</tr>
</table>

</form>
</body>
</html>
