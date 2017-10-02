<%@ page import='java.util.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<% //expanded page dimensions
final int W_SURVEY_RETENTION_SURVEY_TOTAL = 970;
%>

<%
  response.setHeader("Cache-Control", "no-cache");
  response.setHeader("Pragma", "no-cache");
  response.setDateHeader ("Expires", 0);
  
  String successPage = "/survey/retentionsurvey.jsp";
%>

	
<fd:RetentionSurvey result="result" surveyResult="surveyResult" successPage="<%=successPage%>">
	<tmpl:insert template='/common/template/no_space_border.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Feedback Survey"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - Feedback Survey</tmpl:put>
	    <tmpl:put name='content' direct='true'>
	        
	        	<% if(((ActionResult)result).isSuccess() && surveyResult != null) { %>
	        		<%--MEDIA INCLUDE--%><fd:IncludeMedia name="<%=surveyResult%>" /><%--END MEDIA INCLUDE --%>
	        	<% } else { %>
	            <table width="<%=W_SURVEY_RETENTION_SURVEY_TOTAL%>" cellpadding="0" cellspacing="0" border="0">
	                <tr>
	                  <td colspan="7" class="text12" align="center">
	   	            <%          Collection pgErrs=((ActionResult)result).getErrors();
	                            StringBuffer errMsgBuff = new StringBuffer();
	                            if (pgErrs != null && pgErrs.size() > 0) {
	                                List errMsgList = new ArrayList();
	                                for (Iterator errItr = pgErrs.iterator();errItr.hasNext(); ) {
	                                    //errMsgBuff.append("<br>&nbsp;&nbsp;&nbsp;");
	                                    errMsgBuff.append("<br>");
	                                    errMsgBuff.append(((ActionError)errItr.next()).getDescription());
	                                }                      
	                                                                
	                               String errorMsg=errMsgBuff.toString(); %>    
	                                <%@ include file="/includes/i_error_messages.jspf" %>                               
	                    <%  	} %>
		                        <br><a href="/index.jsp"><img src="/media_stat/images/template/help/help_home.gif" width="71" height="26" border="" alt="return to home page"></a>
		                        <br>Go to <a href="/index.jsp">Home Page</a><br><br> 
		                    </td>
	            		</tr>
	           	 </table>                          
	                <% } %>	                                                  
		</tmpl:put>
	 </tmpl:insert>
 </fd:RetentionSurvey>            


  