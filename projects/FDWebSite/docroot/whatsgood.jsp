<%@ page import='com.freshdirect.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.FDReservation' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*' %>
<%@ page import='com.freshdirect.framework.util.log.LoggerFactory' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='java.text.*' %>
<%@ page import='java.util.Collection' %>
<%@ page import='java.util.HashMap' %>
<%@ page import='java.util.Map' %>
<%@ page import='java.util.StringTokenizer' %>
<%@ page import='org.apache.log4j.Category' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>


<fd:CheckLoginStatus />

<%!
	static Category LOGGER = LoggerFactory.getInstance("WGD");

	void log (boolean debug, String strToPrint) {
		if (debug) { LOGGER.debug(strToPrint); }
	}
%>

<%

/* needed? */
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

/* needed? */
	String custFirstName = user.getFirstName();
	int validOrderCount = user.getAdjustedValidOrderCount();
	boolean mainPromo = user.getLevel() < FDUserI.RECOGNIZED && user.isEligibleForSignupPromotion();

/* control page and row-wide debug messages */
	boolean myDebug = true;

/* */
	String deptId = null; //department ID
	String catId=""; //category id


/*
 *	These are subAttributes, see comments further down for defs
 */
	boolean isTransactionalRow = true;
	int minForRowVisibility = 0; //set by "min" sub attribute
	int grabberDepth        = 0; //set by "depth" sub attribute
	boolean dealsOnly       = false; //set by "deals" sub attribute
	boolean featuredOnly    = false; //set by "feats" sub attribute
	int perRow              = 5; //used in generic_row
	int perRowPP            = 6; //used in peak_produce
	String dealsFrom        = "gro"; //set by "from" sub attritbute
	String showInContextOf  = null; //set by "context" sub attritbute
/* Deals values */
    int favoritesToShow		= 0;
    int FAVS_PER_LINE		= 5; //number of products to show per line
    int MAX_FAVSLINES2SHOW	= 1; //number of lines of featured products
    int MIN_FAVS2SHOW		= 1; //minimum featured products required to show featured section, effected by "min" sub attritbute
    int	MAX_FAVS2SHOW		= FAVS_PER_LINE*MAX_FAVSLINES2SHOW; //to keep each row matching

/* default collection declares */
	Collection sortedColl=null;
	Collection globalColl=new ArrayList();
	Collection rowColl=null;

/* */
	boolean onlyOneProduct =false;
	ProductModel theOnlyProduct =  null;
	ContentNodeModel currentFolder;
	CategoryModel currentCAT =  null;
	DepartmentModel currentDEPT =  null;
	boolean isDept;
	boolean isCat;
	String trkCode= "";
	boolean sortDescending;
	String sortNameAttrib;
	Settings layoutSettings;
	String mediaPath;

/* */
	int maxRows = 1; //used in generic_row

/* Transactional stuff */
	List impressions = new ArrayList();
	ProductImpression pi = null;
	int nConfProd = 0;
	ConfigurationContext confContext = new ConfigurationContext();
	ConfigurationStrategy cUtil = SmartStoreConfigurationStrategy.getInstance();

String TX_FORM_NAME        = "DEFAULT_form"; // impression form name
String TX_JS_NAMESPACE     = "DEFAULT_JSnamespace"; // impression javascript namespace

String mediaPathTemp="";
String mediaPathTempBase="/media/editorial/whats_good/";


deptId = request.getParameter("deptid");

if (deptId==null) { deptId="wgd"; }
	
log(myDebug, "PAGE : Starting What's Good...");

/*
 *	Set up email toggle
 *	Assume false by default, only turning on if "email=true" is sent in the request
 */
	Map params = new HashMap();
	String baseUrl = "";
	String templatePath = "/common/template/dnav_no_space.jsp"; //the default
	boolean emailPage = false;

	if ( "true".equals ((String)request.getParameter("email")) ) {
		emailPage = true;
		baseUrl = "http://www.freshdirect.com";
		templatePath = "/common/template/blank.jsp"; //email
			log(myDebug, "PAGE template set: "+templatePath);
		isTransactionalRow = false; // turn off transactional for emails
			log(myDebug, "PAGE turning transactional to: "+isTransactionalRow);
	}

	//	add emailPage to params passed to ftls
	params.put("emailPage", Boolean.toString(emailPage));
	log(myDebug, "PAGE email mode: "+emailPage);
	params.put("baseUrl", baseUrl);
		log(myDebug, "PAGE baseUrl set: "+baseUrl);
%>

<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= "/grocery_cart_confirm.jsp?catId="+request.getParameter("catId") %>' source='<%= request.getParameter("fdsc.source")%>'>
	<tmpl:insert template='<%=templatePath%>'>
	<tmpl:put name='title' direct='true'>FreshDirect - What's Good</tmpl:put>
	<tmpl:put name='content' direct='true'>

	
	<script type="text/javascript" src="/assets/javascript/pricing.js"></script>
	<%
		//--------OAS Page Variables-----------------------
		request.setAttribute("sitePage", "www.freshdirect.com/whatsgood");
		request.setAttribute("listPos", "WGLeft,WGCenter,WGRight");

	if (emailPage) {
		log(myDebug, "PAGE emailPage: "+emailPage);
	%>
		<!-- START EMAIL -->
		<style>
			body { width: 690px; text-align: center; }
			.WG_EMAIL, table { width: 100%; text-align: center; }
		</style>
		<center>
		<table width="690" border="0" cellspacing="0" cellpadding="0" align="center" class="WG_EMAIL">

		<tr>
			<td valign="bottom">

	<% } %>


	<% //START error messaging %>
		<fd:ErrorHandler result='<%=result%>' name='quantity' id='errorMsg'>
			<img src="/media_stat/images/layout/clear.gif" width="20" height="12" alt="" border="0">
			<%@ include file="/includes/i_error_messages.jspf" %>
		</fd:ErrorHandler>
	<% //END error messaging %>
	
	<% //START top section %>

		<%
			log(myDebug, "PAGE IncludeMedia: /media/editorial/whats_good/whats_good_line.html");
			mediaPathTemp=mediaPathTempBase+"whats_good_line.html";
		%>
		<fd:IncludeMedia name="<%= mediaPathTemp %>" />

		<%
			log(myDebug, "PAGE IncludeMedia: /media/editorial/whats_good/whats_good_top_msg.html");
			mediaPathTemp=mediaPathTempBase+"whats_good_top_msg.html";
		%>
		<fd:IncludeMedia name="<%= mediaPathTemp %>" />
	<% //END top section %>


<%
	/*
	 *	Use property to control page functionality on a row per row basis
	 *
	 *	property: 
	 *		fdstore.fdwhatsgood.rows
	 *
	 *	A comma seperated list of values of one of the following:
	 *		CONFIG (this value takes priority over all others)
	 *			useConfig:DOMAINVALUE_ID
	 *				DOMAINVALUE_ID = the ID of the domain value to use
	 *				This will take configuration from the VALUE of this domain value
	 *		SPECIAL (one of the following three)
	 *			wgd_produce	-> /includes/department_peakproduce_whatsgood.jspf
	 *			wg_deals	-> /includes/layouts/i_featured_products_whatsgood.jspf
	 *			wg_ads		-> (if email) mediaPathTempBase+whats_good_AD_space.ftl
	 *		CATEGORY
	 *			ANY_CATID	-> /departments/whatsgood/generic_row.jspf
	 *		MEDIA FILE
	 *			ANY_MEDIA	->  mediaPathTempBase+ANY_MEDIA
	 *
	 *	Inside of each value attributes are available, seperated by a colon as
	 *	KEY=VALUE pairs. KEY is case insensitive (and so is VALUE, assuming VALUE doesn't
	 *	NEED to be case sensitve for it's use. so true == TRUE == TrUe)
	 *	
	 *	These can be any of the following:
	 *		isTx = [true|false]   DEFAULT: true
	 *			Show row as transactional.
	 *			on (true) or off (false) 
	 *			NOTE: email=true AND isTx=true results in transactional in email mode
	 *		min  = [INT]   DEFAULT: 0 (no min)
	 *			the minimum products required to show row.
	 *		max  = [INT]   DEFAULT: 5/6 (5 in GENERIC/6 in PEAK PRODUCE)
	 *			the maximum products for a row. (currently does not wrap products)
	 *		from  = [STRING]   DEFAULT: "gro"
	 *			where the deal products are pulled from (for wg_deals) //phased out
	 *		depth  = [INT]   DEFAULT: 0 (no children)
	 *			the depth the item grabber should use for fetching products
	 *		context = [STRING]   DEFAULT: NULL
	 *			used for faking the context (sets catId = VALUE).
	 *			if null, sets to current catId (if not null)
	 *			flips over to category page
	 *			Results in:
	 *				category.jsp?catId=CONTEXT&prodCatId=CATID&trk=cpage&productId=PRODID
	 *					Where CONTEXT = VALUE and CATID = catId
	 *		deals = [true|false]   DEFAULT: false
	 *			Show ONLY deal products
	 *			on (true) or off (false) 
	 *		feats = [true|false]   DEFAULT: false
	 *			Show featured products instead of normal children products
	 *			on (true) or off (false)
	 */

	String strWGRows = "";
	
	//get property with rows
	strWGRows = FDStoreProperties.getWhatsGoodRows();

	//check for config
	if (strWGRows.toLowerCase().indexOf("useconfig:") > -1) {
		log(myDebug, "PAGE config: config found!");

		String[] resultConfig = strWGRows.split(":");
		DomainValue configTest = null;

		//make sure we get an actual DomainValue
        try {
			log(myDebug, "PAGE config: using domainValueId: "+resultConfig[1]);
        	configTest=ContentFactory.getInstance().getDomainValueById(resultConfig[1]);
			//log(myDebug, "PAGE config: config.getLabel()="+configTest.getLabel());
			log(myDebug, "PAGE config: Using config: "+configTest.getLabel());
			log(myDebug, "PAGE config: config.getTheValue()="+configTest.getTheValue());
			strWGRows = configTest.getTheValue();
        }catch(Exception e){
			log(myDebug, "PAGE config: EXCEPTION: "+e);
			log(myDebug, "PAGE config: DomainValue has an ERROR. Check fdstore.properties file.");
        }
		
	}

	log(myDebug, "PAGE property: fdstore.fdwhatsgood.rows="+strWGRows);

	//if there are rows, use them
	if (strWGRows !=null && !"".equals(strWGRows)) {

		String curRow = ""; //holds current row
		String[] resultWGRows = strWGRows.split(",");

		for (int rowId=0; rowId<resultWGRows.length; rowId++) {
			//reset defaults so each attribute starts clean
				if (!emailPage) { isTransactionalRow = true; }else{ isTransactionalRow = false; }
				minForRowVisibility = 0;
				MIN_FAVS2SHOW = 1;
				perRow = 5; //used in generic_row
				perRowPP = 6; //used in peak_produce
				grabberDepth = 0;
				dealsFrom = "gro";
				showInContextOf = null;
				dealsOnly = false;
				featuredOnly = false;


			//get current row
			curRow=resultWGRows[rowId];

			try {
				//check for attributes
				if (curRow.indexOf(":") > 0) { //zero since it can't START with a colon (correctly)

					//log(myDebug, "found colon: ");
					String[] resultSub = curRow.split(":"); //found sub attrs

					//now reset curRow
					if (!"".equals(resultSub[0])) {
						curRow=resultSub[0];
						log(myDebug, "found curRow: "+curRow);
					}

					if(resultSub.length>1){
						log(myDebug, "found additional resultSubs: "+resultSub.length);

						for (int rowSubAtr=1; rowSubAtr<resultSub.length; rowSubAtr++) {
							if (resultSub[rowSubAtr].indexOf("=") > 0) {
								/* Add new KEY=VALUE pairs here */
								log(myDebug, "found key=val pair: "+resultSub[rowSubAtr]);
								String[] resultSubSub = resultSub[rowSubAtr].split("=");
								if ("isTx".equalsIgnoreCase(resultSubSub[0])) {
									/* this allows isTx email=true overriding */
									log(myDebug, "found isTx: "+resultSubSub[1]+", emailPage: "+emailPage);
										if ("true".equalsIgnoreCase(resultSubSub[1])) {isTransactionalRow = true;} 
										if ("false".equalsIgnoreCase(resultSubSub[1])) {isTransactionalRow = false;}
								}else if ("min".equalsIgnoreCase(resultSubSub[0])) {
									log(myDebug, "found min: "+Integer.parseInt(resultSubSub[1]));
										minForRowVisibility = Integer.parseInt(resultSubSub[1]);
										if (minForRowVisibility > 0) { MIN_FAVS2SHOW = minForRowVisibility; }
								}else if ("max".equalsIgnoreCase(resultSubSub[0])) {
									log(myDebug, "found max: "+Integer.parseInt(resultSubSub[1]));
										perRow = Integer.parseInt(resultSubSub[1]);
										perRowPP = perRow;
								}else if ("from".equalsIgnoreCase(resultSubSub[0])) {
									log(myDebug, "found from: "+resultSubSub[1]);
										dealsFrom = resultSubSub[1];
								}else if ("context".equalsIgnoreCase(resultSubSub[0])) {
									log(myDebug, "found context: "+resultSubSub[1]);
										showInContextOf = resultSubSub[1];
								}else if ("depth".equalsIgnoreCase(resultSubSub[0])) {
									log(myDebug, "found depth: "+Integer.parseInt(resultSubSub[1]));
										grabberDepth = Integer.parseInt(resultSubSub[1]);
								}else if ("deals".equalsIgnoreCase(resultSubSub[0])) {
									log(myDebug, "found deals: "+resultSubSub[1]);
										if ("true".equalsIgnoreCase(resultSubSub[1])) {dealsOnly = true;} 
										if ("false".equalsIgnoreCase(resultSubSub[1])) {dealsOnly = false;}
								}else if ("feats".equalsIgnoreCase(resultSubSub[0])) {
									log(myDebug, "found feats: "+resultSubSub[1]);
										if ("true".equalsIgnoreCase(resultSubSub[1])) {featuredOnly = true;} 
										if ("false".equalsIgnoreCase(resultSubSub[1])) {featuredOnly = false;}
								}else{
									log(myDebug, "found ???=???: "+resultSubSub[0]+"="+resultSubSub[1]);
								}
							}
						}
						//set context
						if (catId != null && showInContextOf == null) {
							showInContextOf = catId;
							log(myDebug, "PAGE property: set showInContextOf: "+showInContextOf);
						}
					}
				}
			}catch(Exception e){
				log(myDebug, "PAGE property: EXCEPTION: "+e);
				log(myDebug, "PAGE property: Parsing property has an ERROR. Check fdstore.properties file.");
			}

			log(myDebug, "ROW rowId: "+rowId);
			log(myDebug, "ROW curRow: "+curRow);

			//first, check for specials
			
			//special : peak produce
			if ("wgd_produce".equals(curRow)) {

				//do peak produce stuff
				//START Great Right Now 

				//see if row is a category
				currentFolder = ContentFactory.getInstance().getContentNode(curRow);
				if(currentFolder instanceof CategoryModel) {
					//is a category
					log(myDebug, "PP: IS cat");

					//we know it's a category, so use it in the generic row
					catId = curRow;
				
					log(myDebug, "PP: entering: wgd_produce:/includes/department_peakproduce_whatsgood.jspf");
					%><%@ include file="/includes/department_peakproduce_whatsgood.jspf" %><%
				}else{
					log(myDebug, "PP: IS NOT cat");
				}
				//END Great Right Now

			//special : deals
			}else if ("wg_deals".equals(curRow)) {

				//START Grocery Deals
				Image groDeptImage = null;
				boolean isDepartment = true;

				if (trkCode!=null && !"".equals(trkCode.trim()) ) {
					trkCode = "&trk="+trkCode.trim();
				} else {
					trkCode = "";
				}

				//these are needed in the include
					isDepartment = true;
					
					//this determines where the products are pulled from...
					currentFolder=ContentFactory.getInstance().getContentNodeByName(dealsFrom);
					
					/*
						...and the dept context (if null, not used)
						override the default here, for deals.
					*/	
					if (showInContextOf == null) { showInContextOf = "wgd"; }

					log(myDebug, "DEALS: entering: wg_deals:/includes/layouts/i_featured_products_whatsgood.jspf");
					%><%@ include file="/includes/layouts/i_featured_products_whatsgood.jspf" %><%
				//END Grocery Deals

			//special : ADs
			}else if ("wg_ads".equals(curRow)) {

				//do ad row
				//START AD spots
					mediaPathTemp=mediaPathTempBase+"whats_good_line.html"; //set media to line

					log(myDebug, "ADS: including: wg_ads:"+mediaPathTemp);
					%><fd:IncludeMedia name="<%= mediaPathTemp %>" /><%

					if (!emailPage && FDStoreProperties.isAdServerEnabled()) {
						//not an email
						mediaPathTemp=mediaPathTempBase+"whats_good_ads_on.ftl"; //ads are on
					}else{
						//is an email, or adserver is disabled
						mediaPathTemp=mediaPathTempBase+"whats_good_ads_off.ftl"; //ads are off
					}
					log(myDebug, "ADS: including: ads:"+mediaPathTemp);
					%><fd:IncludeMedia name="<%= mediaPathTemp %>" parameters="<%=params%>" withErrorReport="false" /><%
				//END AD spots

			//no specials
			}else{
				//see if row is a category
				currentFolder = ContentFactory.getInstance().getContentNode(curRow);
				//System.out.println(curRow);

				if(currentFolder instanceof CategoryModel || currentFolder instanceof DepartmentModel) {

					if (currentFolder instanceof CategoryModel) { log(myDebug, "???: IS cat"); /* is a category */ }
					if (currentFolder instanceof DepartmentModel) { log(myDebug, "???: IS dept"); /* is a department */ }

					catId = curRow; /* catId is used even if it's a department */

					//we know it's a category or department, so use it in the generic row
					log(myDebug, "???: including: GENERIC:/departments/whatsgood/generic_row.jspf");
					log(myDebug, "???: including: GENERIC:deptId="+deptId+", catId="+catId);
					%><%@include file="/departments/whatsgood/generic_row.jspf" %><%

				}else{
					//is NOT a category
					log(myDebug, "???: IS NOT cat or dept");

					//try using it as media instead
					mediaPathTemp=mediaPathTempBase+curRow;

					
					//add an id to params
					params.put("rowId", String.valueOf(rowId) );
					params.put("rowName", curRow);
					
					log(myDebug, "???: including: MEDIA:"+mediaPathTemp);
					%><fd:IncludeMedia name="<%= mediaPathTemp %>" parameters="<%=params%>" withErrorReport="false" /><%
				}
			}

		}
	//no rows were found
	}else{
		log(myDebug, "PAGE strWGRows: No rows found? Check fdstore.properties file");
	}


	if (emailPage) {
		/* finish html for email version */
	%>
				</td>
			</tr>
		</table>
		</center>
		<!-- END EMAIL -->
	<% } %>
	
</tmpl:put>
</tmpl:insert>
</fd:FDShoppingCart>


<% log(myDebug, "PAGE : Ending What's Good..."); %>