<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDReservation' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.util.StringTokenizer' %>
<%@ page import='java.text.*' %>
<%@ page import='java.util.Collection' %>
<%@ page import='java.util.Map' %>
<%@ page import='java.util.HashMap' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>


<fd:CheckLoginStatus />
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
String custFirstName = user.getFirstName();
int validOrderCount = user.getAdjustedValidOrderCount();
boolean mainPromo = user.getLevel() < FDUserI.RECOGNIZED && user.isEligibleForSignupPromotion();
String deptId = null;
String showInContextOf = null;
String catId="";
//String deptId = "wgd";

//generic_row variables
	Collection sortedColl=null;
	Collection globalColl=new ArrayList();
	Collection rowColl=null;
	boolean onlyOneProduct =false;
	ProductModel theOnlyProduct =  null;
	ContentNodeModel currentFolder;
	boolean isDept;
	boolean isCat;
	String trkCode= "";
	boolean sortDescending;
	String sortNameAttrib;
	Settings layoutSettings;
	String mediaPath;
	int perRow = 5; //used in generic_row
	int maxRows = 1; //used in generic_row


deptId = request.getParameter("deptid");

if (deptId==null) { deptId="wgd"; }

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
	}

	//	add emailPage to params passed to ftls
	params.put("emailPage", Boolean.toString(emailPage));
	params.put("baseUrl", baseUrl);
%>

	<tmpl:insert template='<%=templatePath%>'>
	<tmpl:put name='title' direct='true'>FreshDirect - What's Good</tmpl:put>
	<tmpl:put name='content' direct='true'>
	<%
		//--------OAS Page Variables-----------------------
		request.setAttribute("sitePage", "www.freshdirect.com/whatsgood");
		request.setAttribute("listPos", "WGLeft,WGCenter,WGRight");

	if (emailPage) { %>
		<!-- START EMAIL -->
		<style>
			body { width: 690px; text-align: center; }
			.WG_EMAIL, table { width: 100%; text-align: center; }
		</style>
		<center>
		<table width="690" border="0" cellspacing="0" cellpadding="0" align="center" class="WG_EMAIL">

		<tr>
			<td valign="bottom">

	<%}%>
	
	<% //START top section %>
		<fd:IncludeMedia name="/media/editorial/whats_good/whats_good_line.html" />
		<fd:IncludeMedia name="/media/editorial/whats_good/whats_good_top_msg.html" />
	<% //START end top section %>


<%

	String strWGRows = "";
	
	//System.out.println("================inside WG :");

	//get property with rows
	strWGRows = FDStoreProperties.getWhatsGoodRows();

	//if there are rows, use them
	if (strWGRows !=null && !"".equals(strWGRows)) {

		//System.out.println("=============* strWGRows :"+strWGRows);

		String curRow = ""; //holds current row

		String[] result = strWGRows.split(",");
		//System.out.println("=============rows total :"+result.length);

		for (int rowId=0; rowId<result.length; rowId++) {


			//get current row
			curRow=result[rowId];
			System.out.println("=============row curRow :"+curRow);
			System.out.println("=============row indexOf :"+(curRow.indexOf(":")));

			//check here for row limits
			if (curRow.indexOf(":") > 0) { //zero since it can't START with a colon (correctly)
				String[] resultSub = curRow.split(":");
				//now reset curRow
				curRow=resultSub[0];
				if(resultSub.length>1){ perRow=Integer.parseInt(resultSub[1]); }
				if(resultSub.length>2){ maxRows=Integer.parseInt(resultSub[2]); }
			}

			//System.out.println("=============row :"+rowId);
			//System.out.println("=============row strWGRows :"+curRow);


			//first, check for specials
			
			//special : peak produce
			if ("wg_peakproduce".equals(curRow)) {

				//do peak produce stuff
				//START Great Right Now 
				
					System.out.println("=============row in wg_peakproduce :");
					%><%@ include file="/includes/department_peakproduce_whatsgood.jspf" %><%
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
					currentFolder=ContentFactory.getInstance().getContentNodeByName("gro");
					
					//...and the dept context (if null, not used)
					showInContextOf = "wgd";

					//System.out.println("=============row in wg_deals :");
					%><%@ include file="/includes/layouts/i_featured_products_whatsgood.jspf" %><%
				//END Grocery Deals

			//special : ADs
			}else if ("wg_ads".equals(curRow)) {

				//do ad row
				//START AD spots

					//System.out.println("=============row in wg_ads :");

					%><fd:IncludeMedia name="/media/editorial/whats_good/whats_good_line.html" /><%

					if (!emailPage && FDStoreProperties.isAdServerEnabled()) {
						//not an email %>
						<table style="text-align: center;">
							<tr>
								<td>
									<!-- AD SPOT Left -->
									<script type="text/javascript">
											OAS_AD('WGLeft');
									</script>
								</td>
								<td>
									<!-- AD SPOT Center -->
									<script type="text/javascript">
											OAS_AD('WGCenter');
									</script>
								</td>
								<td>
									<!-- AD SPOT Right -->
									<script type="text/javascript">
											OAS_AD('WGRight');
									</script>
								</td>
							</tr>
						</table>
					<%
					}else{
						//is an email, or adserver is disabled
						%><fd:IncludeMedia name="/media/editorial/whats_good/whats_good_AD_space.ftl" parameters="<%=params%>"/><%
					}
				//END AD spots

			//no specials
			}else{
				//see if row is a category
				ContentNodeModel currentFolderTemp = ContentFactory.getInstance().getContentNode(curRow);
				System.out.println(curRow);

				if(currentFolderTemp instanceof CategoryModel) {
					//is a category
					System.out.println("==============cat : IS cat");

					//we know it's a category, so use it in the generic row
					catId = curRow;
					%><%@include file="/departments/whatsgood/generic_row.jspf" %><%

				}else{
					//is NOT a category
					System.out.println("==============cat : IS NOT cat");

					//try using it as media instead
					String mediaPathTemp="/media/editorial/whats_good/"+curRow;

					
					//add an id to params
					params.put("rowId", String.valueOf(rowId) );
					params.put("rowName", curRow);
					
					%><fd:IncludeMedia name="<%= mediaPathTemp %>" parameters="<%=params%>"/><%
				}
			}

		}
	//no rows were found
	}else{
		//System.out.println("=============* strWGRows returning null/empty:");
	}


	if (emailPage) {	%>

				</td>
			</tr>
		</table>
		</center>
		<!-- END EMAIL -->
	<%}%>
	
	
	

</tmpl:put>
</tmpl:insert>
