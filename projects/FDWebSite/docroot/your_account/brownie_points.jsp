<%@ page import="com.freshdirect.mail.EmailUtil" %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.fdstore.referral.ReferralPromotionModel"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<% //expanded page dimensions
final int W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL = 970;
%>
<%
String successPage = "/your_account/brownie_points.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;

%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
    }
	

response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
String browserType=(String)request.getHeader("User-Agent");
%>

<tmpl:insert template='/common/template/brownie_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
<tmpl:put name='content' direct='true'>

<fd:ReferAFriend result="result" successPage="<%= successPage %>">



<%
	String referralUrl = "http://" + request.getServerName() + "/invite/" + user.getReferralLink();
	ReferralPromotionModel rpModel = FDReferralManager.getReferralPromotionDetails(customerIdentity.getErpCustomerPK());
	if(rpModel != null) {
	System.out.println(rpModel.toString());
	
	String email_txt = rpModel.getReferralPageText();
	email_txt = email_txt.replaceAll("<personal url>", referralUrl);
	
	String fb_headline = rpModel.getFbHeadline();
	fb_headline = fb_headline.replaceAll("<personal url>", referralUrl);
	
	String twitter_Text = rpModel.getTwitterText();
	twitter_Text = twitter_Text.replaceAll("<personal url>", referralUrl);
	
	request.setAttribute("sitePage", "www.freshdirect.com/your_account/brownie_points.jsp");
	request.setAttribute("listPos", "RAFBanner,RAFLeftHeader");
	
	boolean valid = true;
	if("sendmails".equals(request.getParameter("action"))) {
		//check the emails
		String recipient_list = request.getParameter("form_tags_input");
		StringTokenizer stokens = new StringTokenizer(recipient_list, ",");		
		if(stokens.countTokens() == 0) {
			valid = false;
		} else {
			while(stokens.hasMoreTokens()) {
				String recipient = stokens.nextToken();
				if(!EmailUtil.isValidEmailAddress(recipient)) {
					valid = false;
					break;
				}					
			}
		}
	}
%>	
<script language="javascript">
	var clip = null;
		
	window.onload= function copy_function() {
		clip = new ZeroClipboard.Client();
		clip.setHandCursor( true );
		ZeroClipboard.setMoviePath( '/assets/javascript/ZeroClipboard.swf' );
			
		clip.addEventListener('mouseOver', my_mouse_over);
			
		clip.glue( 'd_clip_button' );
	};
		
	function my_mouse_over(client) {
		// we can cheat a little here -- update the text on mouse over
		clip.setText( '<%= request.getServerName() + "/invite/" + user.getReferralLink() %>' );
	}

	var curButton = null;
	function currentSectionArrow(id, over) {
		if  (curButton == null) { curButton = YAHOO.util.Dom.getElementsByClassName('current')[0]; }
		if (over && id != curButton.id) {
			YAHOO.util.Dom.replaceClass(curButton, 'current', 'currentWas');
		}
		if (!over) {
			YAHOO.util.Dom.replaceClass(curButton, 'currentWas', 'current');
		}
	} 

</script>	
					

<% if("fb".equals(request.getParameter("current"))) { %>
<script language="text/javascript">
	<script language="text/javascript">
           function login(){
                FB.api('/me', function(response) {
                    document.getElementById('hide_login').style.display = "none";
                    document.getElementById('hide_login').innerHTML = "";						
                });
            }
            function logout(){
                document.getElementById('login').style.display = "none";
            }
			
			function wallpost() {
				FB.ui({ method: 'feed', 
				message: 'Facebook for Websites is super-cool'});
			}
			
 </script>
<% } %>




<!-- Start of promotion bar -->	
<TABLE WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>" BORDER="0" CELLPADDING="0" CELLSPACING="0">
	<TR>
		<td width="970" valign="top"><img src="/media_stat/images/layout/clear.gif" width="970" height="5" border="0"></td>
	</TR>
	<TR>
		<TD WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>">
			<!-- OAS ADD comes here. The big brownie banner -->
			<div style="width:970;align:center;">
			<script language="JavaScript">			
				OAS_AD('RAFBanner');				
			</script>
			</div>
		</td>
	</tr>
	<tr><TD WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>">
		<!-- SHARE GIVE GET--->
		<table width="100%">
			<tr>
				<td width="230">
					<!--Spot for OAS image -->
					<script language="JavaScript">
						OAS_AD('RAFLeftHeader');
					</script> 
				</td>
				<td><img src="/media_stat/images/profile/arrow.jpg" style="padding-right:15px;"></td>
				<td width="230" valign="center" align="center">
					<table><tr align="left"><td><font face="BebasRegular, verdana" size="5" font-weight="bold"><%= rpModel.getShareHeader()%></font></td></tr> 
					<tr align="left"><td> <%= rpModel.getShareText() %></td></tr></table>
				</td>
				<td><img src="/media_stat/images/profile/arrow.jpg" style="padding-right:15px;"></td>	
				<td width="230" valign="center" align="center">
					<table><tr align="left"><td><font face="BebasRegular, verdana" size="5" font-weight="bold"><%= rpModel.getGiveHeader() %></font></td></tr> 
					<tr align="left"><td><%= rpModel.getGive_text() %></td></tr></table>
				</td>
				<td><img src="/media_stat/images/profile/arrow.jpg" style="padding-right:15px;"></td>
				<td width="230" valign="center" align="center">
					<table><tr align="left"><td><font face="BebasRegular, verdana" size="5" font-weight="bold"><%= rpModel.getGetHeader() %></font></td></tr> 
					<tr align="left"><td><%= rpModel.getGet_text() %></td></tr></table>
				</td>
			</tr>
		</table>

		<hr width="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>" style="background-color:#FFFFFF; border: 1px dotted #C0C0C0; border-style: none none dotted;">
		<br/><br/>
		<%
		String current = request.getParameter("current");
		if(current == null || current.length() == 0) {
			current = "email";
		}
		%>
	</td><tr>
	<tr><TD WIDTH="100%"> <!-- sharing box -->
		<table id="bottomlayer" width="100%" border="0">
			<tr>
				<td id="sharebox" style="width:60%;background-color: #ffffff;" valign="top">
					<!--FB TWITTER and PLAXO ---->
							<div id="fbmenu">
								<div id="menu">
									<ul>
										<li class="em">
											<a href="/your_account/brownie_points.jsp?current=email"<%= "email".equals(current)?" class=\"current\"":""%> id="raf_em" onmouseover="currentSectionArrow(this.id, true)" onmouseout="currentSectionArrow(this.id)">
												<img src="/media_stat/images/profile/share_email.png" />
												<span class="line1">share with<br /></span><span class="line2">Email</span>
											</a>
										</li>
										<li class="fb">
											<a href="/your_account/brownie_points.jsp?current=fb"<%= "fb".equals(current)?" class=\"current\"":""%> id="raf_fb" onmouseover="currentSectionArrow(this.id, true)" onmouseout="currentSectionArrow(this.id)">
												<img src="/media_stat/images/profile/share_fb.png"/>
												<span class="line1">share with<br /></span><span class="line2">Facebook</span>
											</a>
										</li>
										<li class="tw">
											<a href="/your_account/brownie_points.jsp?current=twitter"<%= "twitter".equals(current)?" class=\"current\"":""%> id="raf_tw" onmouseover="currentSectionArrow(this.id, true)" onmouseout="currentSectionArrow(this.id)">
												<img src="/media_stat/images/profile/share_twitter.png" />
												<span class="line1">share with<br /></span><span class="line2">Twitter</span>
											</a>
										</li>
									</ul>	
								</div>								
							</div>
							<div id="fbbox">
								<div id="testbox1">
								<% if("email".equals(current)) { %>
									<% if("sendmails".equals(request.getParameter("action")) && valid) {
									//request submitted
									%>
										<div style="float:left;width:100%;padding:20px;">
											<table><tr><td><img src="/media_stat/images/profile/success.png" border="0"/></td>
											<td><span style="color:#777777;" class="text12bold">Your email has been sent to your friends. </span><br/>
											<a href="/your_account/manage_invites.jsp" style="font-weight:normal;">Manage sent invites</a>&nbsp;&nbsp;&nbsp;
											<a href="/your_account/brownie_points.jsp?current=email" style="font-weight:normal;">Invite more friends</a>
											</td></tr></table>
										</div>
									<% } else { %>									
									<form name="sendmails" action="brownie_points.jsp" method="post">	
										<input type="hidden" name="action" value="sendmails"/>
										<input type="hidden" name="recipient_list" id="recipient_list" >
										<input type="hidden" name="rpid" value="<%= rpModel.getReferral_prgm_id() %>" />
										<table width="100%" style="float:left;">
										<tr> <td colspan="3" id="greytext" style="padding-left:15px;padding-top:15px;width:90%;">
											Type email address(es) <span>(Separate email addresses with commas)</span>
										</td></tr>
										<tr><td width="420px">
											<div class="form_tags" style="width:100%;padding-left:15px;float:left;"><input type="text" name="form_tags_input" value="" id="form_tags_input"/> </div></td>
										<td width="2%" align="center"><img src="/media_stat/images/profile/or.jpg"></td><td  width="28%" align="left" valign="top"><a href="#" onclick="showPlaxoABChooser('recipient_list', '/your_account/plaxo_cb.jsp'); return false"><img src="/media_stat/images/buttons/import_address.jpg"/></a></td></tr>
										
										<% if (!valid) { %>
											<div style="width:90%;padding-left:15px;float:left;color:red;font-weight:bold;">Please enter valid email addresses</div>
										<% } %>
										
										<tr><td colspan="3">
										<div id="greytext" style="float:left;width:90%;padding-top:15px;padding-left:15px;">
											Enter personal message <span>(optional)</span>
											<br/>
											<textarea id="mail_message" name="mail_message" rows="6" cols="40"><%= email_txt %></textarea>
										</div>
										</td></tr>
										<tr><td colspan="3">
										<div id="orangebutton" style="padding:10px;">
											<center>
											<input type="image" src="/media_stat/images/buttons/send_email.jpg" /> <br/>
											<span class="greytext_normal"><br/><a href="/your_account/manage_invites.jsp">manage sent invites</a></span>
											</center>
										</div>
										</td></tr>
										</table>
										
									</form>
									<% } %>
								<% } else if ("fb".equals(current)) { %>
									<br/>									
									<div id="login"></div>
									<div id="fb-root"></div>
									<span id="hide_login" style="padding:15px;float:left;display:none;"><fb:login-button onlogin="window.location.reload();">Login</fb:login-button></span>
									<div id="friends"></div>									
									<script language="javascript">
										window.fbAsyncInit = function() {
										 FB.init({appId: '176418392468226', 												  
												  status: true, 
												  cookie: true, 
												  xfbml: true});
										 
											 
										 
											 FB.getLoginStatus(function(response) {
												if (response.status == 'connected') {
													 // logged in and connected user, someone you know
													 document.getElementById('hide_login').style.display = "none";
													 document.getElementById('hide_login').innerHTML = "";						
													 window.alert('calling friends');
													 getFriends();
												 } else {
													window.alert('not logged in');
													document.getElementById('hide_login').style.display = "";
												 }
											 });
											 
										 };
										 
										 (function() {
											var e = document.createElement('script');
											e.type = 'text/javascript';
											e.src = document.location.protocol +
												'//connect.facebook.net/en_US/all.js';
											e.async = true;
											document.getElementById('fb-root').appendChild(e);
										}());
										 
										function login(){
											FB.api('/me', function(response) {
												document.getElementById('hide_login').style.display = "none";
												document.getElementById('hide_login').innerHTML = "";						
											});
										}
										function randomFromTo(from, to){
										   return Math.floor(Math.random() * (to - from + 1) + from);
										}
										function getPicture(id) {
											FB.api('/' + id+'/picture', function(response1) {					
												document.getElementById(id).src=response1;	
											});
										}
										function postToFeed() {

												// calling the API ...
												var obj = {
												  method: 'feed',
												  link: '<%= referralUrl %>',
												  picture: 'http://www.freshdirect.com/<%= rpModel.getFbFile() %>',
												  name: 'FreshDirect.com',
												  caption: '<%= fb_headline %> ',
												  description: '<%= rpModel.getFbText() %> '
												};

												function callback(response) {
												  //alert("Post ID: " + response['post_id']);
												}

												FB.ui(obj, callback);
										  }

										function callFBUI() {
											//process FBUI here
											//First get the selected email addresses
											var toString = [];
											var cnt = 0;

											for(i=0;i<9;i++) {
												var felement = document.getElementById("friend" + i);
												if(felement != null && felement.checked) {
													//friend is selected so add them to the string
													//toString += felement.value + ",";
													toString[cnt] = felement.value;
													cnt++;
												}
											}
										
										
											//alert(toString.toString());
										
											FB.ui(
										   {
											 method: 'send',
											 name: '<%= fb_headline %>',
											 link: '<%= referralUrl %>',
											 picture: 'http://www.freshdirect.com/<%= rpModel.getFbFile() %>',
											 caption: 'Caption',
											 description: '<%= rpModel.getFbText() %>',
											 to: toString     
										   },
										   function(response) {
											 if (response && response.request_ids) {
											   //alert('Post was published.' + response);
											   getFriends();
											 } else {
											   //alert('Post was not published.' + response);
											   getFriends();
											 }
										   }
											);

										}
										var post_on_wall_but = '<table class="butCont" style=\"line-height: 20px;\">' + 
																		'<tr>' + 
																		'<td class=\"butBlueMiddle20\" valign=\"middle\"><a href=\"#\" onclick=\"postToFeed()\" class=\"previewbut\" style=\"color:#ffffff;text-shadow:none;font-weight:bold;vertical-align:middle;padding:7px;text-decoration:none;\"><img src="/media_stat/images/buttons/post_on_wall.jpg" /></a></td>' +
																		'</tr>' +
																	'</table>';
										var preview_but = '<div style=\"padding-left:15px;float:left;\"><table class="butCont" style=\"line-height: 20px;\">' + 
																		'<tr>' + 
																		'<td class=\"butBlueMiddle20\" valign=\"middle\"><a href=\"#\" onclick=\"callFBUI()\" class=\"previewbut\" style=\"color:#ffffff;text-shadow:none;font-weight:bold;vertical-align:middle;padding:7px;text-decoration:none;\"><img src="/media_stat/images/buttons/send_message.jpg" /></a></td>' +
																		'</tr>' +
																	'</table></div>';
										function getFriends(){
											FB.api('/me/friends?fields=name,picture,id', function(response) {
												var divInfo = document.getElementById("friends");
												var friends = response.data;					
												divInfo.innerHTML = "";
												var htmlString = "<br/><div class=\"text12bold\" style=\"float:left;padding:15px;color:#777777\">Tell your Friends <a href=\"#fbarea\" onclick=\"getFriends()\" name=\"fbarea\"> <img src=\"/media_stat/images/buttons/refresh.jpg\" /></a></div><div style=\"float:right;padding:15px;\">" + post_on_wall_but + "</div>  <br/> <hr width=\"100%\" style=\"float:left;background-color:#FFFFFF; border: 1px solid #C0C0C0; border-style: none none solid;\"> <br/> <table width=\"100%\" cellpadding=\"2px\">";
												var j = randomFromTo(0, friends.length);
												//alert("friends.length:"+ friends.length + "-j:" + j);
												var cnt = 0;
												for (var i = j; i < friends.length; i++) {						
													//alert(fpicture);
													if(cnt == 0) {
														htmlString += "<tr>";
													}
													if(cnt == 3 || cnt == 6) {
														htmlString += "</tr><tr><td colspan=\"3\">&nbsp;</td></tr><tr>";
													}
													if(cnt<9) {							
														htmlString += "<td valign=\"center\"><table width=\"100%\"><tr><td valign=\"center\" width=\"10%\"><input type=\"checkbox\" id=\"friend" + cnt + "\" name=\"friend" + cnt + "\" value=\""+ friends[i].id + "\"/></td>" ;
														htmlString += "<td width=\"30%\"><img id=\"" + friends[i].id +"\" src=\"\" /></td><td valign=\"center\" align=\"left\" width=\"60%\">";
														htmlString += friends[i].name +"</td></tr></table></td>";							
														getPicture(friends[i].id);
													}
													cnt++;						
												}
												if(cnt < 9) {
													for(i=cnt; i<9;i++) {
														var k = randomFromTo(0, j);
														//alert(fpicture);
														htmlString += "<tr><td valign=\"center\"><input type=\"checkbox\" id=\"friend" + cnt + "\" name=\"friend" + cnt + "\" value=\""+ friends[k].id + "\"/></td>" ;
														htmlString += "<td><img src=\"\" /></td><td valign=\"center\" align=\"left\">";
														htmlString += friends[k].name + "</td></tr>";		
														getPicture(friends[k].id);
													}
												}
												htmlString += "</table><br/><hr width=\"100%\" style=\"float:left;background-color:#FFFFFF; border: 1px solid #C0C0C0; border-style: none none solid;\"><br/><br/>"+preview_but+"<br/><br/>";
												divInfo.innerHTML = htmlString;

											});
										}
										</script>
								<% } else if ("twitter".equals(current)) { %>
									<br/><br/>&nbsp;&nbsp;
									<a href="#" onclick="window.open('http://twitter.com/share?text=<%= twitter_Text %>&amp;count=none', 'Twitter', 'height=400,width=600');" class="twitterbutton" >Sign In</a>	
									<script language="Javascript">
										window.open("http://twitter.com/share?text=<%= twitter_Text %>&amp;count=none", "Twitter", "height=400,width=600");
									</script>
								<% } %>
							</div>
							</div>
							
							<!-- </td>
						</tr>
					</table> -->
				</td>
				<td width="1%"></td>
				<td id="infobox" valign="top">
					<div style="padding: 5px 15px;">
						<p id="greytext" align="left">Your Personal Link</p>
						<div id="d_clip_button" class="copylink"><%= request.getServerName() + "/invite/" + user.getReferralLink() %></div>
					</div>
					<hr id="hrline" />
					
					<span id="greytext" align="left" style="padding: 5px 15px;">Reward Balance <span class="greytext_normal">|&nbsp;<a href="/your_account/credits.jsp">View Details</a></span></span><br/>
					<% if(user.getAvailableCredit() > 0) { %>
					<span id="dollartext"><%= JspMethods.formatPrice(user.getAvailableCredit()) %></span>					
					<% } else { %>
						<table><tr><td id="dollartext">$0</td>
						<td id="greytext" style="color:#F78C0D;font-size:12px;padding:5px;">Invite more friends to <br/> earn rewards!</td>
						</tr></table>
					<% } %>
					<hr id="hrline" />
					
					<span id="greytext" style="padding: 5px 15px;">Terms and conditions</span><br/>
					<div style="padding: 5px 15px;">
						<span id="smalltext"><%= rpModel.getReferralPageLegal() %></span>
					</div>					
				</td>
			</tr>
		</table>
	</td></tr>
	

	<% } else { %>
		<!-- promotion is null, so display a 404 message -->
		<table border="0" cellpadding="0" cellspacing="0" width="<%=W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL%>">
		<tr>
			<td align="center">
			<!-- 
				<img src="/media_stat/images/template/error/error_01.gif" alt="ERR" border="0"><img src="/media_stat/images/template/error/error_02.jpg"  alt="O" border="0"><img src="/media_stat/images/template/error/error_03.gif"  alt="R" border="0">
			 -->
			</td>
		</tr>
		<tr>
		    <TD>

					<br><br>
					<table cellspacing="0" cellpadding="0" border="0" width="100%">
					<tr>
					    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
					    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" width="18" height="5" border="0"></td>
					    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
					    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" width="6" height="5" border="0"></td>
					    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
					</tr>
					<tr>
					    <td rowspan="3"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
					    <td width="100%"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
					</tr>
					<tr>
					    <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
					    <td class="text11rbold">
														
								Sorry, the Web site is unable to process that request. We apologize for any inconvenience. Please check back later.
								<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
						
						</td>
					    <td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
					    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
					</tr>
					<tr>
					    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" width="18" height="5" border="0"></td>
					    <td><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
					    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" width="6" height="5" border="0"></td>
					</tr>
					<tr>
					    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
					</tr>
					</table>
					<br>			
					<br><br>

			</TD>
		</tr></table>

	<% } %>
	

<!-- End of promotion bar -->						


<TR VALIGN="TOP">
<TD width="970" align="center">

<!-- * end the actual summary info * -->
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>">
<tr VALIGN="TOP">
<td WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></td>
<td WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL - 35 %>"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></td>
</tr>

</TABLE>

<!--[if lt IE 9 ]>
<script language="javascript" type="text/javascript">
	if (!window.$) {
      window.$ = function(id) { return document.getElementById(id); }
    }
	
    var myBorder = RUZEE.ShadedBorder.create({ corner:5, edges:"tlr" });
    myBorder.render('fbmenu');
  </script>
<![endif]-->
</fd:ReferAFriend>
	</tmpl:put>
</tmpl:insert>

								
