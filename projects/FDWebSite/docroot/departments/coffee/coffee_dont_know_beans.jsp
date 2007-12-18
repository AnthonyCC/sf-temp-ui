<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.fdstore.*, com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<tmpl:insert template='/common/template/right_dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Dont't Know Beans</tmpl:put>
		<tmpl:put name='content' direct='true'>
			 <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="550">
			 <tr>
				 <td class="text13">
					<img src="/media_stat/images/template/coffee/beans.gif" width="30" height="30" alt="" border="0"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"><img src="/media_stat/images/template/coffee/dont_know_beans.gif" width="241" height="17" alt="" border="0"><br>
					<font class="space2pix"><br></font>
					<img src="/media_stat/images/layout/cccccc.gif" width="500" height="1" alt="" border="0"><br>
					<font class="space2pix"><br></font>
					Answer a few simple questions and we'll recommend coffees that we know you'll like. <br>			 
				 </td>
			 </tr>
			 </table>
		  <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="550">
		      <TR VALIGN="TOP">
			      <TD COLSPAN="5" WIDTH="550"><IMG SRC="/media_stat/images/layout/ffffff.gif" WIDTH="550" HEIGHT="20"></TD>
			  </TR>
			  <form action="/departments/coffee/coffee_beans_results.jsp" method="get">
			  	<input type="hidden" name="successPage" value="/help/coffee_beans_results.jsp">
			  	<input type="hidden" name="deptId" value="cof">
				<input type="hidden" name="catId" value="cof_dkbns">				
				<TR VALIGN="Top">
			      <TD CLASS="text13" width="200">
				      <FONT CLASS="text13bold">The flavors I enjoy are...</FONT><BR>
					  <INPUT TYPE="radio" CLASS="radio"  NAME="question1" VALUE="medium"> Light, mild<BR>
					  <INPUT TYPE="radio" CLASS="radio"  NAME="question1" VALUE="viennese"> Not too light, not too dark<BR>
					  <INPUT TYPE="radio" CLASS="radio"  NAME="question1" VALUE="italian,French"> Rich, bold<BR>
					  <P>
					  <FONT CLASS="text13bold">I prefer my coffee...</FONT><BR>
					  <INPUT TYPE="radio" CLASS="radio"  NAME="question2" VALUE="4,5"> Sharper<BR>
					  <INPUT TYPE="radio" CLASS="radio"  NAME="question2" VALUE="1,2,3"> Smoother<BR>
					  <P>
					  <FONT CLASS="text13bold">I usually drink my coffee...</FONT><BR>
					  <INPUT TYPE="radio" CLASS="radio"  NAME="question3" VALUE="1"> With milk/cream<BR>
					  <INPUT TYPE="radio" CLASS="radio"  NAME="question3" VALUE="2"> Black<BR>
				</TD>
				<TD WIDTH="5"><IMG SRC="/media_stat/images/layout/ffffff.gif" WIDTH="5" HEIGHT="1" BORDER="0" VSPACE="0"></TD>
				<TD><IMG SRC="/media_stat/images/layout/ff9933.gif" WIDTH="1" HEIGHT="250" BORDER="0" VSPACE="0"></TD>
				<TD WIDTH="40"><IMG SRC="/media_stat/images/layout/ffffff.gif" WIDTH="20" HEIGHT="1" BORDER="0" VSPACE="0"></TD>
				<TD CLASS="text13" width="204">
				    <FONT CLASS="text13bold">Do you drink flavored coffees?</FONT><BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question4" VALUE="Yes"> Yes<BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question4" VALUE="No"> No<BR>
					<P>
					<FONT CLASS="text13bold">Do you drink decaf?</FONT><BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question5" VALUE="Yes"> Yes<BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question5" VALUE="No"> No<BR>
					<P>
					<FONT CLASS="text13bold">How do you brew your coffee?</FONT><BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question6" VALUE="Automatic drip"> Automatic drip<BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question6" VALUE="French press"> French press<BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question6" VALUE="Melitta cone"> Melitta cone<BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question6" VALUE="Percolator"> Percolator<BR>
					<INPUT TYPE="radio" CLASS="radio"  NAME="question6" VALUE="Espresso"> Espresso<BR>
					<P>

				</TD>
			</TR>
		
		</TABLE>
			 <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="550">
			 <tr>
				 <td>
					<font class="space4pix"><br></font>
					<img src="/media_stat/images/layout/cccccc.gif" width="500" height="1" alt="" border="0"><br>
					<font class="space6pix"><br></font>
					<input type="image" src="/media/images/buttons/submit.gif" width="48" height="16" border="0" alt="SUBMIT">
					</form>		
				 </td>
			 </tr>
			 </table>		
		
		
<%
Set brands = null; //required variables for the category layout and bottom_template
%>		
<%@ include file="/includes/i_bottom_template.jspf" %>
</tmpl:put>
</tmpl:insert>

