<%@ page import='java.util.*'%>
<%@ page import="com.freshdirect.mail.EmailUtil"%>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.fdstore.referral.ReferralPromotionModel"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.storeapi.attributes.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*'%>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import='java.text.*'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='bean' prefix='bean'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='template' prefix='tmpl'%>
<%
	//expanded page dimensions
	final int W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL = 970;
%>
<%
	String successPage = FDStoreProperties.isExtoleRafEnabled()? FDStoreProperties.getPropExtoleMicrositeUrl():"/your_account/brownie_points.jsp";
	String redirectPage = "/login/login.jsp?successPage=" + successPage;
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false'	redirectPage='<%=redirectPage%>' />
<%
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	FDIdentity customerIdentity = null;
	if (user != null && user.getLevel() == 2) {
		customerIdentity = user.getIdentity();
	}

	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	String browserType = (String) request.getHeader("User-Agent");
%>

<tmpl:insert template='/common/template/brownie_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="brownie_point"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<%
			boolean valid = true;
					boolean msg_valid = true;
					if ("sendmails".equals(request.getParameter("action"))) {
						//check the emails
						String recipient_list = request.getParameter("form_tags_input");
						StringTokenizer stokens = new StringTokenizer(
								recipient_list, ",");
						if (stokens.countTokens() == 0) {
							valid = false;
						} else {
							while (stokens.hasMoreTokens()) {
								String recipient = stokens.nextToken().trim();
								if (!EmailUtil.isValidEmailAddress(recipient)) {
									valid = false;
									break;
								}
							}
						}

						if (request.getParameter("mail_message") == null
								|| request.getParameter("mail_message")
										.length() == 0) {
							msg_valid = false;
						}
						if (request.getParameter("mail_message").length() > 160) {
							msg_valid = false;
						}
					}
		%>
		<fd:ReferAFriend>
			<%
			//System.out.println("inside RAF tag");
				String referralUrl = "http://"
									+ request.getServerName() + "/invite/"
									+ user.getReferralLink();
							ReferralPromotionModel rpModel = FDReferralManager
									.getReferralPromotionDetails(customerIdentity
											.getErpCustomerPK());
							String stlSaleID = FDReferralManager
									.getLatestSTLSale(customerIdentity
											.getErpCustomerPK());

							//System.out.println("inside RAF tag - before if");

							//System.out.println("inside RAF tag - rpModel "+((rpModel != null)?rpModel.toString():"rpModel == null"));
							//System.out.println("inside RAF tag - stlSaleID "+stlSaleID);

							if ((rpModel != null && stlSaleID != null
									&& stlSaleID.length() > 0)||true) {

								//System.out.println(rpModel.toString());

								String email_txt = rpModel.getReferralPageText();
								email_txt = email_txt.replaceAll("<personal url>",
										referralUrl);

								String fb_headline = rpModel.getFbHeadline();
								fb_headline = fb_headline.replaceAll(
										"<personal url>", referralUrl);

								String twitter_Text = rpModel.getTwitterText();
								twitter_Text = twitter_Text.replaceAll(
										"<personal url>", referralUrl);

								request.setAttribute("sitePage", "www.freshdirect.com/your_account/brownie_points.jsp");
								request.setAttribute("listPos", "RAFBanner,RAFLeftHeader");

								//System.out.println("inside RAF tag1");
			%>
			<script language="javascript">
			var curvyCornersVerbose = false;
			var clip = null;

			$jq(document).ready(function () {
				clip = new ZeroClipboard.Client();
				clip.setHandCursor( true );
				ZeroClipboard.setMoviePath( '/assets/javascript/ZeroClipboard.swf' );

				clip.addEventListener('mouseOver', my_mouse_over);

				clip.addEventListener('complete',function(client,text) {
					document.getElementById('d_clip_button').style.backgroundcolor='pink';
				});

				clip.glue( 'd_clip_button' );


				var curButton = $jq('.current:first');
				$jq('#fbmenu a').each(function(i,e) {
					$jq(e).on({
						mouseenter: function() {
							curButton.removeClass('current');
						}, mouseleave: function() {
							curButton.addClass('current');

						}
					});
				});
			});

			function my_mouse_over(client) {
				// we can cheat a little here -- update the text on mouse over
				clip.setText( '<%=request.getServerName() + "/invite/" + user.getReferralLink()%>' );
			}
		</script>
			<%
				if ("fb".equals(request.getParameter("current"))) {
			%>
			<div id="fb-root"></div>
			<%
				}
			%>

			<table width="<%=W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL%>" border="0"
				cellpadding="0" cellspacing="0">
				<tr>
					<td width="970" valign="top"><img
						src="/media_stat/images/layout/clear.gif" width="970" height="5"
						alt="" border="0"></td>
				</tr>
				<tr>
					<td width="<%=W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL%>" id='oas_RAFBanner'>

						<div style="width: 970; align: center;">
							<script language="JavaScript">
							OAS_AD('RAFBanner');
						</script>
						</div>
					</td>
				</tr>
				<tr>
					<td width="<%=W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL%>">

						<table width="100%">
							<tr>
								<td width="230">
                  <div id='oas_RAFLeftHeader'>
                    <script language="JavaScript">
									    OAS_AD('RAFLeftHeader');
								    </script>
                  </div>
                </td>
								<td><img src="/media_stat/images/profile/arrow.jpg"
									style="padding-right: 15px;"></td>
								<td width="230" valign="center" align="center">
									<table>
										<tr align="left">
											<td><font face="BebasRegular, verdana" size="5"
												font-weight="bold"><%=rpModel.getShareHeader()%></font></td>
										</tr>
										<tr align="left">
											<td><%=rpModel.getShareText()%></td>
										</tr>
									</table>
								</td>
								<td><img src="/media_stat/images/profile/arrow.jpg"
									style="padding-right: 15px;"></td>
								<td width="230" valign="center" align="center">
									<table>
										<tr align="left">
											<td><font face="BebasRegular, verdana" size="5"
												font-weight="bold"><%=rpModel.getGiveHeader()%></font></td>
										</tr>
										<tr align="left">
											<td><%=rpModel.getGive_text()%></td>
										</tr>
									</table>
								</td>
								<td><img src="/media_stat/images/profile/arrow.jpg"
									style="padding-right: 15px;"></td>
								<td width="230" valign="center" align="center">
									<table>
										<tr align="left">
											<td><font face="BebasRegular, verdana" size="5"
												font-weight="bold"><%=rpModel.getGetHeader()%></font></td>
										</tr>
										<tr align="left">
											<td><%=rpModel.getGet_text()%></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>

						<hr width="<%=W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL%>"
							style="background-color: #FFFFFF; border: 1px dotted #C0C0C0; border-style: none none dotted;">
						<br />
					<br /> <%
 					String current = request.getParameter("current");
 					if (current == null || current.length() == 0) {
 						current = "email";
 					}
 %>
					</td>
				</tr>
				<tr>
					<td width="100%">
						<table id="bottomlayer" width="100%" border="0">
							<tr>
								<td id="sharebox" style="width: 60%; background-color: #ffffff;" valign="top">
									<div id="fbmenu">
										<div id="menu">
											<ul>
												<li class="em"><a href="/your_account/brownie_points.jsp?current=email"	<%="email".equals(current) ? " class=\"current\"" : ""%> id="raf_em"> <img src="/media_stat/images/profile/share_email.png" /> <span class="line1">share with<br /></span><span class="line2">Email</span></a></li>
												<li class="fb"><a href="/your_account/brownie_points.jsp?current=fb" <%="fb".equals(current) ? " class=\"current\"" : ""%> id="raf_fb"><img src="/media_stat/images/profile/share_fb.png" /> <span class="line1">share with<br /></span><span class="line2">Facebook</span></a></li>
												<li class="tw"><a href="/your_account/brownie_points.jsp?current=twitter" <%="twitter".equals(current) ? " class=\"current\"" : ""%> id="raf_tw"><img src="/media_stat/images/profile/share_twitter.png" /> <span class="line1">share with<br /></span><span class="line2">Twitter</span></a></li>
											</ul>
										</div>
									</div>
									<div id="fbbox">
										<div id="testbox1">
											<%
												if ("email".equals(current)) {
													//System.out.println("^^^^^^^^^^^^^^^^^^^^^^^" + request.getParameter("action") + "-" + valid);
											%>
												<%
													if ("sendmails".equals(request.getParameter("action")) && valid	&& msg_valid) {
														//request submitted
												%>
														<div style="float: left; width: 100%; padding: 20px;">
															<table>
																<tr>
																	<td><img src="/media_stat/images/profile/success.png"
																		alt="success" border="0" /></td>
																	<td><span style="color: #777777;" class="text12bold">Your
																			email has been sent to your friends. </span><br /> <a
																		href="/your_account/manage_invites.jsp"
																		style="font-weight: normal;">View sent invites</a>&nbsp;&nbsp;&nbsp;
																		<a href="/your_account/brownie_points.jsp?current=email"
																		style="font-weight: normal;">Invite more friends</a></td>
																</tr>
															</table>
														</div>
												<%
													} else {
												%>
														<table width="100%" style="">
															<tr>
																<td colspan="3" id="greytext"
																	style="padding-left: 15px; padding-top: 15px;">
																	<form name="sendmails" action="brownie_points.jsp"
																		method="post">
																		<input type="hidden" name="action" value="sendmails" />
																		<input type="hidden" name="recipient_list"
																			id="recipient_list" /> <input type="hidden" name="rpid"
																			value="<%=rpModel.getReferral_prgm_id()%>" /> Type
																		email address(es) <span>(Separate email addresses
																			with commas)</span>
																</td>
															</tr>
															<tr>
																<td width="420px">
																	<div class="form_tags"
																		style="width: 100%; padding-left: 15px; float: left;">
																		<input type="text" name="form_tags_input" value="" id="form_tags_input" />
																	</div>
																</td>
																<td width="2%" align="center"><img
																	src="/media_stat/images/profile/or.jpg"></td>
																<td width="28%" align="left" valign="top"><a href="#"
																	onclick="showPlaxoABChooser('recipient_list', '/your_account/plaxo_cb.jsp'); return false;"
																	class="cs_import"><img
																		src="/media_stat/images/buttons/import_address.jpg" /></a></td>
															</tr>

															<%
																if (!valid) {
															%>
															<tr>
																<td colspan="3">
																	<div
																		style="width: 90%; padding-left: 15px; float: left; color: red; font-weight: bold;">Please
																		enter valid email addresses</div>
																</td>
															</tr>
															<%
																}
															%>

															<tr>
																<td colspan="3">
																	<div id="greytext"
																		style="float: left; width: 90%; padding-top: 15px; padding-left: 15px;">
																		Enter personal message <span>(optional)</span> <br />
																		<textarea id="mail_message" name="mail_message" rows="6"
																			cols="40" maxlength="160"><%=email_txt%></textarea>
																	</div> <br />
																</td>
															</tr>
															<tr>
																<td colspan="3">
																	<div
																		style="width: 93%; padding-left: 15px; float: left; color: #777777; font-size: 11px; font-style: italic; text-align: right;">160
																		characters maximum</div>
																</td>
															</tr>
															<%
																if (!msg_valid) {
															%>
															<tr>
																<td colspan="3">
																	<div
																		style="width: 90%; padding-left: 15px; float: left; color: red; font-weight: bold;">Your
																		personal message should not be more than 160 characters.</div>
																</td>
															</tr>
															<%
																}
															%>
															<tr>
																<td colspan="3">
																	<div id="orangebutton" style="padding: 10px;">
																		<center>

																			<a class="butCont butOrange" style="margin-right: 10px;"
																				href="#" onclick="document.sendmails.submit();"> <span
																				class="butLeft">
																					<!-- -->
																			</span><span class="butMiddle butText"
																				style="font-weight: bold; padding: 0 60px; font-size: 14px;">send
																					email</span><span class="butRight">
																					<!-- -->
																			</span>
																			</a> <span class="greytext_normal"><br />
																			<a href="/your_account/manage_invites.jsp">view sent
																					invites</a></span>
																		</center>
																	</div>
																	</form>
																</td>
															</tr>
														</table>
													<%
														}
													%>
											<%
												} else if ("fb".equals(current)) {
											%>
													<br />
													<div id="login"></div>
													<span id="hide_login"
														style="padding: 15px; float: left; display: none;"><fb:login-button
															onlogin="window.fbAsyncInit();" id="fb_loginButton">Login</fb:login-button></span>
													<div id="friends">
														<div
															style="height: 326px; position: relative; display: none;"
															id="fb_friendsListLoading">
															<div
																style="height: 50px; position: absolute; top: 45%; left: 150px; margin-top: -25px;">
																<img src="/media_stat/images/navigation/spinner.gif"
																	class="fleft" /><span
																	style="line-height: 50px; float: left; font-size: 22px;">Loading
																	Your Friend List...</span>
															</div>
														</div>
													</div>
													<script language="javascript">

														$jq(document).ready(function() {
															var	e =	document.createElement('script');
															e.type = 'text/javascript';
															e.src =	'//connect.facebook.net/en_US/all.js';
															e.async	= true;
															$jq('#fb-root').append(e);
														});

														var	loginPrompted =	false;
														window.fbAsyncInit = function()	{
															FB.init({
																appId: '<%=FDStoreProperties.getFacebookAppKey()%>',
																status:	true,
																cookie:	true,
																xfbml: true
															});

															FB.getLoginStatus(function(response) {

																if (response.status	== 'connected')	{
																	// logged in and connected user, someone you know
																	$jq('#hide_login').hide();
																	//window.alert('calling	friends');
																	//show loading screen
																	if ($jq('#fb_friendsListLoading')) { $jq('#fb_friendsListLoading').show(); }

																	getFriends();
																} else {
																	//window.alert('not	logged in');
																	$jq('#hide_login').show();
																	//auto prompt for login
																	if (!loginPrompted)	{
																		$jq('#fb_loginButton').children('a').click();
																		loginPrompted = !loginPrompted;<%-- if you don't do this, it will prompt over and over until you login --%>
																	}
																}
															});
														};

														function login() {
															FB.api('/me', function(response) {
																$jq('#hide_login').hide().empty();
															});
														}

														function randomFromTo(from,	to){
														   return Math.floor(Math.random() * (to - from	+ 1) + from);
														}

														function getPicture(id)	{
															FB.api('/' + id+'/picture',	function(response1)	{
																document.getElementById(id).src=response1;
															});
														}

														function postToFeed() {
															// calling the API ...
															var	obj	= {
																method: 'feed',
																link:	'<%=referralUrl%>',
																picture: 'http://www.freshdirect.com/<%=rpModel.getFbFile()%>',
																name:	'FreshDirect.com',
																caption: '<%=fb_headline%> ',
																description: '<%=rpModel.getFbText()%> '
															};

															function callback(response)	{
															  //alert("Post	ID:	" +	response['post_id']);
															}

															FB.ui(obj, callback);
														}

														function sendFBMsg(friendId)	{
															FB.ui({
																method: 'send',
																name: '<%=fb_headline%>',
																link: '<%=referralUrl%>',
																picture: 'http://www.freshdirect.com/<%=rpModel.getFbFile()%>',
																caption: 'Caption',
																description: '<%=rpModel.getFbText()%>',
																to: friendId
															}, function(response) {
																if (response &&	response.request_ids) {
																   //alert('Post was published.' + response);
																   getFriends();
																} else	{
																	//alert('Post was not published.' + response);
																	getFriends();
																}
															});
														}

														var	post_on_wall_but = '<div class="fright">' +
														'<a	href="#" onclick="postToFeed();	return false;" class="previewbut"><img src="/media_stat/images/buttons/post_on_wall.jpg" />' +
														'</a></div>';
														/*var	preview_but	= '<div	class="fright"><table class="butCont">'	+
																'<tr><td class="butBlueMiddle20" valign="middle"><a	href="#fbarea" onclick="callFBUI();	return false;" class="previewbut"><img src="/media_stat/images/buttons/send_message.jpg" /></a></td></tr>' +
																'</table></div>';*/

														function fbGetFriendCont(i,	friendObj) {
															return '<table width=\"100%\">'+
																	'<tr>'+
																		'<td style="width: 80px; height: 50px;"><img id="' + friendObj.id + '" src="https://graph.facebook.com/' + friendObj.id + '/picture" alt="profile image" /></td>'+
																		'<td valign="center" align="left" style="width: 134px; text-align:left;">' + friendObj.name + '</td>'+
																		'<td valign="center" style="width: 90px;"><input type="image" src="/media_stat/images/buttons/send_message.jpg" id="friend_' + friendObj.id + '" name="friendSend" value="send" /></td>'+
																	'</tr>'+
																'</table>';
														}

														function getFriends(){
															FB.api('/me/friends?fields=name,id', function(response)	{
																var	friendsDiv = $jq('#friends');
																friendsDiv.empty();
																var	friends	= response.data;
																if (friends.length == 0) { friendsDiv.append('You have no friends.'); return; }

																friendsDiv.append('<div	id="fb_wallpost" /><hr />');
																	$jq('#fb_wallpost').html(post_on_wall_but).append('<br style="clear: right;" />');
																friendsDiv.append('<div	id="fb_friendsList"	/>');
																	if (friends.length > 9)	{ $jq('#fb_friendsList').addClass('fbMore'); };
																/*
																	friendsDiv.append('<hr /><div id="fb_preview" />');
																		$jq('#fb_preview').html(preview_but).append('<br style="clear: right;" />');
																*/
																friendsDiv.append('<hr />');

																$jq('#fb_friendsList').append('<table	id="fb_friendsListCont"	width="100%" cellpadding="2" />');
																var	friendsListCont	= $jq('#fb_friendsListCont');
																var	curRow = null;
																$jq.each(friends,	function(index,	elem) {
																	if (index %	2 == 0)	{
																		curRow = friendsListCont.append('<tr class="fb_friendRow" />').find('tr.fb_friendRow:last');
																		friendsListCont.append('<tr><td	colspan="3"	class="fb_friendRowSpacer">&nbsp;</td></tr>');
																	}else{
																		curRow.find('td:last').css({ borderRight: '1px solid #ccc' });
																	}
																	curRow.append(function() { return '<td width="50%">'+fbGetFriendCont(index,	elem)+'</td>'; });
																});
																if (friends.length % 2 != 0) {
																	curRow.append('<td class="spacer">&nbsp;</td>');
																}
																$jq('#friends input[name="friendSend"]').each(function(elem, index) {
																	$jq(this).on('click', function (event) {
																		event.stopPropagation();
																		event.preventDefault();

																		sendFBMsg( (this.id).split('_')[1] );
																	});
																});

															});
														}
														<%-- logout funtion for ref --%>
														function logout() { var logout = FB.logout(); $jq('#friends').empty(); $jq('#hide_login').show(); }
													</script>
											<%
												} else if ("twitter".equals(current)) {
											%>
													<br />
													<br />&nbsp;&nbsp; <a href="#" onclick="window.open('https://twitter.com/intent/tweet?text=<%=twitter_Text%>&amp;count=none&original_referrer=www.freshdirect.com', 'Twitter', 'height=400,width=600');" class="twitterbutton">Sign In</a>
													<script type="text/javascript">
														window.open("https://twitter.com/intent/tweet?original_referer=https%3A%2F%2Ftwitter.com%2Fabout%2Fresources%2Fbuttons&source=tweetbutton&text=<%=twitter_Text%>","Twitter", "height=400,width=600");
													</script>
											<%
												}
											%>
										</div>
									</div>
								</td>
								<td width="1%">
									<!--  -->
								</td>
								<td id="infobox" valign="top">
									<div style="padding: 5px 15px;">
										<p id="greytext" align="left">
											Your Personal Link <span class="greytext_normal">(click
												on the link to copy)</span>
										</p>
										<div id="d_clip_button" class="copylink"
											onclick="this.style.backgroundColor='#F78C0D !important';"><%=request.getServerName() + "/invite/"
									+ user.getReferralLink()%></div>
									</div>
									<hr id="hrline" /> <span id="greytext" align="left" style="padding: 5px 15px;">Reward Balance <span class="greytext_normal">|&nbsp;<a href="/your_account/credits.jsp">View Details</a></span></span><br />
									<%
									 	if (user.getAvailableCredit() > 0) {
 									%>
											<span id="dollartext"><%=JspMethods.formatPrice(user.getAvailableCredit())%></span>
									<%
										} else {
									%>
											<table>
												<tr>
													<td id="dollartext">$0</td>
													<td id="greytext"
														style="color: #F78C0D; font-size: 12px; padding: 5px;">Invite
														more friends to <br /> earn rewards!
													</td>
												</tr>
											</table>
									<% } %>
									<hr id="hrline" /> <span id="greytext" style="padding: 5px 15px;">Terms and conditions</span><br />
									<div style="padding: 5px 15px;">
										<span id="smalltext"><%=rpModel.getReferralPageLegal()%></span>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>


			<%
				} else {

					//System.out.println("inside RAF tag inside if- else");
			%>

			<table border="0" cellpadding="0" cellspacing="0"
				width="<%=W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL%>">
				<tr>
					<td align="center"></td>
				</tr>
				<tr>
					<td><br />
					<br />
						<table cellspacing="0" cellpadding="0" border="0" width="100%">
							<tr>
								<td rowspan="5" width="20"><img
									src="/media_stat/images/layout/clear.gif" width="20" height="1"
									alt="" border="0"></td>
								<td rowspan="2"><img
									src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif"
									width="18" height="5" alt="" border="0"></td>
								<td colspan="2" bgcolor="#CC3300"><img
									src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
								<td rowspan="2" colspan="2"><img
									src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif"
									width="6" alt="" height="5" border="0"></td>
								<td rowspan="5"><img
									src="/media_stat/images/layout/clear.gif" width="10" height="1"
									alt="" border="0"></td>
							</tr>
							<tr>
								<td rowspan="3"><img
									src="/media_stat/images/layout/clear.gif" width="10" height="1"
									alt="" border="0"></td>
								<td width="100%"><img
									src="/media_stat/images/layout/clear.gif" width="1" height="4"
									alt="" border="0"></td>
							</tr>
							<tr>
								<td width="18" bgcolor="#CC3300"><img
									src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif"
									width="18" height="22" border="0" alt="!"></td>
								<td class="text11rbold">Sorry, the Web site is unable to
									process that request. We apologize for any inconvenience.
									Please check back later. <img
									src="/media_stat/images/layout/clear.gif" width="1" height="3"
									alt="" border="0"><br />

								</td>
								<td><img src="/media_stat/images/layout/clear.gif"
									width="5" height="1" alt="" border="0"></td>
								<td bgcolor="#CC3300"><img
									src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
							</tr>
							<tr>
								<td rowspan="2"><img
									src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif"
									width="18" alt="" height="5" border="0"></td>
								<td><img src="/media_stat/images/layout/clear.gif"
									width="1" height="4" alt="" border="0"></td>
								<td rowspan="2" colspan="2"><img
									src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif"
									width="6" alt="" height="5" border="0"></td>
							</tr>
							<tr>
								<td colspan="2" bgcolor="#CC3300"><img
									src="/media_stat/images/layout/cc3300.gif" alt="" width="1" height="1"></td>
							</tr>
						</table> <br /> <br />
					<br /></td>
				</tr>
			</table>

			<%
				}

							//System.out.println("inside RAF tag after else");
			%>




			<table>
				<TR VALIGN="TOP">
					<TD width="970" align="center"><br />
					<br /> <IMG src="/media_stat/images/layout/ff9933.gif"
						WIDTH="<%=W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL%>" ALT="" HEIGHT="1"
						BORDER="0"><br /> <FONT CLASS="space4pix"><br />
						<br /></FONT>
						<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0"
							WIDTH="<%=W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL%>">
							<tr VALIGN="TOP">
								<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="" ALIGN="LEFT">
                                CONTINUE SHOPPING
                                <BR>from <FONT CLASS="text11bold">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
							</tr>
						</TABLE></td>
				</tr>
			</table>

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
