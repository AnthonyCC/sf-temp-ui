<%@ taglib uri='template' prefix='tmpl' %>


<tmpl:insert template='/common/template/dnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put>

    <tmpl:put name='content' direct='true'>
	<br>
	<table border="0" cellspacing="0" cellpadding="2" width="675">
	    <tr valign="TOP">
			<td width="675" align="center" class="text13">
			<div style="font-size: 28px; font-face:Arial,Verdana,Helvetica; color: #FF9933; margin-bottom: 6px;"><b>THANK YOU!</b></div>
			<span class="text15"><b>We've received your message.</b></span>
			<br><font class="space8pix"><br></font>
			  <b> We generally respond within 1 to 3 hours, </b> during our business day.<br/><br/>
			 		 As a reminder, we are here:<br><br>
			 
			  <div align="center">
                            <table>
                            <tr>
                                <td>Monday-Thursday</td>
                                <td>6:30 AM to 12 AM</td>
                            </tr>
                            <tr>
                                <td>Friday</td>
                                <td>6:30 AM to 11 PM</td>
                            </tr>
                            <tr>
                                <td>Saturday</td>
                                <td>7:30 AM to 8 PM</td>
                            </tr>
                            <tr>
                                <td>Sunday</td>
                                <td>7:30 AM to 12 AM</td>
                            </tr>
                            </table>
                        </div>
			</td>
		</tr>
		<tr>
			<td>
			</td>
		</tr>
		<tr>
		    <td width="675" align="center">
				<br><a href="/index.jsp" onmouseover="swapImage('home_img','/media_stat/images/template/help/help_home_r.gif')" onmouseout="swapImage('home_img','/media_stat/images/template/help/help_home.gif')"><img src="/media_stat/images/template/help/help_home.gif" name="home_img" width="71" height="26" alt="" border="0"></a>
			</td>		
		</tr>
	</table><br><br>
</tmpl:put>
</tmpl:insert>