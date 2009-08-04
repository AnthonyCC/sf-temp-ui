<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='java.util.StringTokenizer' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<fd:ProduceRatingCheck>
<% 

	/*
	 * There is a similar setup in the JspMethods.java file
	 */
	
	//System.out.println("==== in dept peak produce include ProduceRatingCheck : ");

	boolean matchFound = false; //default to false

	//this is taking the place of a skuCode
	String deptIdCheck = request.getParameter("deptId");
	if (!"".equals(deptIdCheck) && deptIdCheck != null) {
		deptIdCheck= deptIdCheck.toUpperCase();

		//System.out.println("	* deptIdCheck :"+deptIdCheck);
		
		// grab sku prefixes that should show ratings
		String _skuPrefixes=FDStoreProperties.getRatingsSkuPrefixes();

		//System.out.println("	* getRatingsSkuPrefixes :"+_skuPrefixes);
	   
		//if we have prefixes then check them
		if (_skuPrefixes!=null && !"".equals(_skuPrefixes)) {
			StringTokenizer st=new StringTokenizer(_skuPrefixes, ","); //setup for splitting property
			String curPrefix = ""; //holds prefix to check against
			String spacer="* "; //spacing for sysOut calls
			
			//loop and check each prefix
			while(st.hasMoreElements()) {
				
				curPrefix=st.nextToken();

				//System.out.println(spacer+"Rating _skuPrefixes checking :"+curPrefix);
				
				//if prefix matches get product info
				if(deptIdCheck.startsWith(curPrefix)) {
					matchFound=true;
				}
				//exit on matched sku prefix
				//System.out.println(spacer+"Rating matchFound :"+matchFound);
				if (matchFound) { break; }
				spacer=spacer+"   ";
			}
		}

		//System.out.println("Rating matchFound :"+matchFound);
	}

	if(matchFound) {
	
		//System.out.println("=== in first if GetPeakProduce : ");
		%>
	
	<fd:GetPeakProduce deptId='<%= request.getParameter("deptId") %>' id='peakProduces'> 
	
			<%
			//System.out.println("=== in dept peak produce peakProduces.size() : "+peakProduces.size());

			if(peakProduces.size()>0) {

				String mediaPath="/media/brands/fd_ratings/"+deptIdCheck+"/peak_produce.html";
				//System.out.println("=== in dept peak produce mediaPath : "+mediaPath);
			%>
				<fd:IncludeMedia name="<%=mediaPath%>" />

	<% ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNodeByName(request.getParameter("deptId")); %>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr valign="bottom">
			<logic:iterate id="peakProduce" collection="<%= peakProduces %>" type="com.freshdirect.fdstore.content.SkuModel">
				<td>
				<% 
				  String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				 %>
				 <td align="center" width="<%=adjustedImgWidth%>" style="padding-left:5px; padding-right:5px;">
					<a href="<%=displayObj.getItemURL()%>&trk=dept"><img src="<%= displayObj.getImagePath()%>"  <%=displayObj.getImageDimensions() %> ALT="<%=displayObj.getAltText()%>" vspace="0" hspace="0" border="0"></a>
				 </td>
			</logic:iterate>
		</tr>
		<tr valign="top">
			<logic:iterate id="peakProduce" collection="<%= peakProduces %>" type="com.freshdirect.fdstore.content.SkuModel">
				<td>
				<% 
				  String prodNameAttribute = JspMethods.getProductNameToUse(peakProduce);
				  DisplayObject displayObj = JspMethods.loadLayoutDisplayStrings(response,peakProduce.getProductModel().getParentNode().getContentName(),peakProduce.getProductModel(),prodNameAttribute,true);
				  int adjustedImgWidth = displayObj.getImageWidthAsInt()+6+10;
				 %>
				 <td valign="top" width="<%=adjustedImgWidth%>" align="center" style="padding-left:5px; padding-right:5px;padding-bottom:10px;">
					<%  if (displayObj.getRating()!=null && displayObj.getRating().trim().length()>0) { %>          
						<img src="/media_stat/images/ratings/<%=displayObj.getRating()%>.gif" name="rating" width="59" height="11" border="0" vspace="3"><% } %><br><a href="<%=displayObj.getItemURL()%>&trk=dept" class="text11"><%=displayObj.getItemName()%></a><%  if (displayObj.getPrice()!=null) { %><br><span class="price"><%=displayObj.getPrice()%></span><%  } %></td>
				</logic:iterate>
			</tr>
		</table>
	<%} %>
	</fd:GetPeakProduce>
<% }%>
</fd:ProduceRatingCheck>
