<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<HTML>
        <BR>
	<BODY onload="connect()" >
        <FORM name="hiddenform"  method="POST" action="<%= FDStoreProperties.getGivexWebServerURL() %>">
        <INPUT type="hidden" name="level" value="2">
        <DIV align="center">
        <TABLE border="0">
            <TR>
                <TD colspan="2"><DIV>&nbsp;</DIV><BR></TD>
            </TR> <TR>
                <TD ALIGN="right">User Name:</TD>
                <TD></TD>
            </TR> <TR>
                <TD ALIGN="right">Password:</TD>
                <TD>
			<INPUT TYPE="HIDDEN" NAME="merchant_id" VALUE="1001">
			<INPUT type="hidden" name="user" size="16" maxlength="32" value="<%=FDStoreProperties.getFDGivexWebUser() %>">
			<INPUT type="hidden" name="pass" size="16" maxlength="15" value="<%=FDStoreProperties.getFDGivexWebUserPassword() %>">
			<INPUT type="hidden" name="search_key" size="16" maxlength="15" value="card_id">
			<INPUT type="hidden" name="srch_val" size="16" maxlength="15" value="">
			<INPUT TYPE="HIDDEN" NAME="certificate_id" VALUE="">
			<INPUT TYPE="HIDDEN" NAME="card_id" VALUE="">
			<INPUT TYPE="hidden" NAME="_FUNCTION_" VALUE="select">
			<INPUT TYPE="hidden" NAME="comment" VALUE="Test">
		</TD>
            </TR> <TR>
                <TD>&nbsp;</TD>
                <TD>&nbsp;</TD>
            </TR> <TR>
                <TD colspan="2" align="center">
                    <A href="login.py?_LANGUAGE_:en+level:7">Forgot your password?</A>
                </TD>
            </TR> <TR>
                <TD>&nbsp;</TD>
                <TD>&nbsp;</TD>
        </TABLE>
        </DIV>
	<script type="text/javascript" language="JavaScript">
		function connect(){
			
			alert('Connecting to Givex. Please wait ....');
			//document.hiddenform.srch_val.value='16145304';
			//document.hiddenform.certificate_id.value='';
			//document.hiddenform.card_id.value='16145304';
			//document.hiddenform.comment.value='test cancellation';
			//var b = document.hiddenform.submit();
           //document.location.href='https://dev-wwws.givex.com/portal/store/cert_search.py?_LANGUAGE_:en';
           //document.hiddenform.action='https://dev-wwws.givex.com/portal/store/cert_search.py?_LANGUAGE_:en';
           document.hiddenform.submit();
		}
		function disconnect(){
			alert('disconnecting from Givex...');
		}
	</script>
        </FORM>
	</BODY>

</HTML>