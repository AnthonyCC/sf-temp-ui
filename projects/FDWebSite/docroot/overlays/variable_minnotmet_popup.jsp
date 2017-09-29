
<%@ page import="com.freshdirect.fdstore.util.TimeslotLogic" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:javascript src="/assets/javascript/common_javascript.js"/>
<fd:css href="/assets/css/global.css" />
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<%
	String view_cart_redir = "/checkout/view_cart.jsp";
	String timeslot_redir = "/checkout/step_2_select.jsp";
	Double minorderamt = Double.parseDouble(request.getParameter("amt"));

%>

			<div style="width:650px; padding:20px 20px; display:inline-block;">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
					    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
					    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" alt="" width="18" height="5" border="0"></td>
					    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
					    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" alt="" width="6" height="5" border="0"></td>
					    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
					</tr>
					<tr>
					    <td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
					    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
					</tr>
					<tr>
					    <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
					    <td class="text12rnormal" width="100%" bgcolor="#FFFFFF">
								<div style="padding: 3px 1px 3px 1px"><strong>ATTENTION</strong> <br><b>Your order total is below the <strong style="color:#000000"><%= TimeslotLogic.formatMinAmount(minorderamt) %></strong> minimum</b> required for your selected delivery time.<br>
									Choose a different delivery window, or continue shopping.</div>
						
						</td>
					    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
					    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
					</tr>
					
					<tr>
					    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" alt="" width="18" height="5" border="0"></td>
					    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
					    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" alt="" width="6" height="5" border="0"></td>
					</tr>
					<tr>
					    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
					</tr>
				</table>
				
				<div style="padding:30px 20px 0px 0px;">
					<div style="float: right;">
						<a class="butText" href="<%=response.encodeURL(view_cart_redir) %>">
						<img name="viewcartbutton" onmouseout="swapImage('viewcartbutton','/media_stat/images/buttons/continueshop_f1.png')" onmouseover="swapImage('viewcartbutton','/media_stat/images/buttons/continueshop_f2.png')" src="/media_stat/images/buttons/continueshop_f1.png" alt="continue shopping" border="0"></a>
					</div>
					<div style="float: right;margin-right: 10px">
						<a class="butText" id="choosenewtimeslot" href="#">
						<img name="timeslotbutton" onmouseout="swapImage('timeslotbutton','/media_stat/images/buttons/newtime_popup_f1.png')" onmouseover="swapImage('timeslotbutton','/media_stat/images/buttons/newtime_popup_f2.png')" src="/media_stat/images/buttons/newtime_popup_f1.png" alt="" border="0"></a>
					</div>
					
					<br style="clear:both" />
				</div>			
			</div>
				
			
			