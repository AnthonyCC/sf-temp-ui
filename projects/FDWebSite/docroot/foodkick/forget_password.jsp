<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.FDSessionUser.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.content.ContentFactory'%>
<%@ page import='com.freshdirect.fdstore.EnumEStoreId' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/c.tld" prefix="c" %>
<%@ include file="includes/fk_core_settings.jspf" %>
<%!
public String result = "";
public String errorMsg = "";
public String[] checkReminderForm = {"email_not_expired", "invalid_email", "email"};
public FDSessionUser user;
%>
<%
String emailAddress = request.getParameter("emailAddress");

//expanded page dimensions (for forget password sections)
final int W_FORGET_PASSWORD_TOTAL = 700;
final int W_FORGET_PASSWORD_CONFIRMATION_TOTAL = 700;
String previousPage;

//this is to help the forgot password form understand whether it is freshdirect or foodkick.  there is different copy for both scenarios
EnumEStoreId estoreId = EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId()));
boolean isFdxOrder = estoreId.getContentId().equals( EnumEStoreId.FDX.toString() );

//transfer normal JSP/Java variable to be usable by JSTL tag. yes, we don't have to deal with this type of annoying crap in angularjs, but whatever
pageContext.setAttribute("isFdxOrder", isFdxOrder);

//for form processing, parameters to be sent to form jstl tags
pageContext.setAttribute("FKAPP_DIR", FKAPP_DIR);

//whether or not this is the main page
String url_prefix = "";
if( request.getParameter("p") != null ){
	url_prefix = "index.jsp";
}
%>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
	<tmpl:put name='title'>A fresh kick</tmpl:put>
	<tmpl:put name='content'>
		<section id="forgot_password_section" class="forgot_password_section">
			<fd:CheckLoginStatus guestAllowed='true' recognizedAllowed='true' id='user' />
			<fd:ForgotPasswordController results="result" successPage='${FKAPP_DIR}/forget_password_confirmation.jsp' password="password">	
				<%@ include file="/login/includes/forget_password.jspf" %>
			</fd:ForgotPasswordController>
		</section>
	</tmpl:put>
</tmpl:insert>