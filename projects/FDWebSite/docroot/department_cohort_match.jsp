<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.util.RatingUtil'%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.net.*'%>
<%@ page import='java.util.*'%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%!
	final Logger LOG = LoggerFactory.getInstance("department_cohort_match.jsp");
%>
<%
	/*
		this is a copy of the department.jsp to handle APPDEV-2723
		all excess functionality has been removed.
		so this is basically the dept page, using the no nav template,
		with the contents as the dept template path, taken from CMS.
		
		EVERYTHING ELSE CMS-WISE IS IGNORED! Do not forward to this jsp
		without checking everything needed from CMS first.
		
	*/
%>
<fd:CheckLoginStatus guestAllowed="true" />
<%
	String deptId = request.getParameter("deptId");
%>
<fd:Department id='department' departmentId='<%= deptId %>'/>
<%
	final ContentNodeModel currentFolder = department;
	final DepartmentModel departmentModel = (DepartmentModel) department;

	String trkCode= "";
	boolean isDept = (currentFolder instanceof DepartmentModel);
	
	if ( isDept ) {
		trkCode = "dpage";
		request.setAttribute("trk","dpage");
	}

	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
	
	Map<String, String> params = new HashMap<String, String>();

	Enumeration keys = request.getParameterNames();  
	while (keys.hasMoreElements() ) {  
		String key = (String)keys.nextElement(); 

		//To retrieve a single value  
		String value = request.getParameter(key); 

		params.put(key, value);
	
		// If the same key has multiple values (check boxes)  
		String[] valueArray = request.getParameterValues(key);  
		 
		for(int i = 0; i > valueArray.length; i++){
			params.put(key+valueArray[i], valueArray[i]);
		}  
	}

	//add a few manual params, jic.
	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	params.put("baseUrl", baseUrl);
	params.put("userCohort", user.getCohortName());

	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", departmentModel.getPath());
	request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,SideCartBottom,WineTopRight,WineBotLeft,WineBotMiddle,WineBotRight,4mmAd1,4mmAd2");

    String title = "FreshDirect - " + departmentModel.getFullName();
%>

<tmpl:insert template='/common/template/no_nav.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="<%= title %>"/>
    </tmpl:put>
    <tmpl:put name='title' direct='true'><%= title %></tmpl:put>
<tmpl:put name='content' direct='true'>
	<fd:CmPageView wrapIntoScriptTag="true" currentFolder="<%=currentFolder%>"/>
	<%
		/*
		int ttl=14400; 
		String keyPrefix="deptLayout_";
	
		if("fru".equals(deptId) ||"veg".equals(deptId) || "sea".equals(deptId) || "wgd".equals(deptId)) {
			if(user.isProduceRatingEnabled()) { 
				//Caching fru,veg,sea,gro,hba,dai,fro,big,mea,wgd depts per pricing zone.	             keyPrefix=keyPrefix+user.getPricingZoneId();	             keyPrefix=keyPrefix+user.isProduceRatingEnabled()+"_";	             ttl=180;	 		}
		} else if("gro".equals(deptId) ||"hba".equals(deptId)||"dai".equals(deptId) ||"fro".equals(deptId) ||"big".equals(deptId)){
			keyPrefix=keyPrefix+user.getPricingZoneId();
			ttl=3600;
		} else if("mea".equals(deptId)){
			keyPrefix=keyPrefix+user.getPricingZoneId();
			ttl=120;
		}
		
		boolean useOsCache = true;
		if ( "fdi".equals(deptId) || "usq".equals(deptId) ) {
			useOsCache = false;
		}
		*/
	%>
	<%-- oscache:cache key='<%= keyPrefix+request.getQueryString() %>' time='<%= useOsCache ? ttl : 0 %>' --%>
	
	<%-- try { --%>
		<% if (departmentModel.getAltTemplatePath() != null && departmentModel.getAltTemplatePath().trim().length() > 0) { 
			LOG.debug("including template path: "+departmentModel.getAltTemplatePath().trim());
			%><fd:IncludeMedia name="<%= departmentModel.getAltTemplatePath().trim() %>" parameters="<%= params %>" /><%
		} else {
			LOG.debug("template path was NULL or zero length for "+deptId);
		}
		%>
	<% /*} catch (Exception ex) {
		LOG.error("error while generating department page body", ex);*/  		%>
		<%--oscache:usecached/ --%>
<%--   	<% } %> --%>
	
	<%-- /oscache:cache --%>
		
</tmpl:put>
</tmpl:insert>
