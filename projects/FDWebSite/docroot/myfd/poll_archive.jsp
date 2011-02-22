<%@page import="java.util.Collection"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.freshdirect.framework.util.DateUtil"%>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.MyFD"%>
<%@page import="com.freshdirect.fdstore.content.StoreModel"%>
<%@page import="com.freshdirect.fdstore.myfd.poll.Poll"%>
<%@page import="com.freshdirect.fdstore.myfd.poll.PollDaddyService"%>
<%@taglib uri='template' prefix='tmpl'%>
<%@taglib uri='logic' prefix='logic'%>
<%@taglib uri='freshdirect' prefix='fd'%>

<fd:CheckLoginStatus id="user" guestAllowed="false" />
<%
	//--------OAS Page Variables-----------------------
    request.setAttribute("sitePage", "www.freshdirect.com/myfd/poll_archive.jsp");
    request.setAttribute("listPos", "SystemMessage,PollArchive");

    // MyFD content fabrication
	MyFD myfd = MyFD.getMyFDInstance();
	
	if (myfd == null)
		throw new IllegalStateException("MyFD does not exist in CMS!");
	
	Collection<Poll> polls = PollDaddyService.getPolls();
	Poll current = polls.size() > 0 ? polls.iterator().next() : null;
	String pollId = request.getParameter("pollId");
	if (pollId != null) {
		boolean found = false;
		for (Poll poll : polls) {
			if (pollId.equals(poll.getId())) {
				found = true;
				break;
			}
		}
		if (!found) {
			String trk = request.getParameter("trk");
			response.sendRedirect("/myfd/" + (trk != null ? "?trk=" + trk : ""));
			return;
		}
	} else {
		pollId = current != null ? current.getId() : null;
	}
%>
<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Poll Archive</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<%-- MyFD header --%>
		<div>
		<%@ include file="/includes/myfd/i_myfd_header.jspf" %>
		</div>
		<%-- Poll Archive content --%>
		<div style="padding: 30px 25px;">
			<table cellpadding="0" cellspacing="0" style="width: 693px; text-align: center; margin: 0px auto;">
				<tr>
					<td style="width: 481px; text-align: left; vertical-align: top;">
						<div style="width: 456px; overflow: hidden;">
							<div class="title20 eagle-bold">POLL ARCHIVE</div>
							<div style="padding: 6px 0px 10px;">
							<a href="/myfd/index.jsp?trk=pollarch" class="text14">Back to the myFD page</a>
							</div>
							<div style="font-size: 0px; height: 2px; background-color: #dadada;">&nbsp;</div>
							<div style="padding-top: 20px;" class="text11">
							Tell us what you think, then see what your fellow FreshDirect customers had to say!
							We conduct polls to learn about how you cook, how you eat, your favorite foods,
							the ways you like to celebrate and more. In the list below, you'll see some of the polls we've already completed.
							</div>
							<div style="padding-top: 25px;">
								<% if (current != null) { %>
								<table cellpadding="0" cellspacing="0" style="width: 456px;">
									<tr>
										<td style="width: 212px; vertical-align: top;">
											<div><img src="/media_stat/images/myfd/poll-header.png" width="212" height="26" border="0"></div>
											<div style="border: solid #F69638; border-width: 0px 1px; padding: 15px 15px 5px;">
											
											<div style="width: 180px; overflow: hidden;">
											<!-- PollDaddy embed start -->
											<script type="text/javascript" charset="utf-8" src="http://static.polldaddy.com/p/<%= pollId %>.js"></script>
											<!-- PollDaddy embed end -->
											</div>
											
											</div>
											<div><img src="/media_stat/images/myfd/poll-footer.png" width="212" height="16" border="0"></div>							
										</td>
										<td style="width: 244px; vertical-align: top;">
											<div style="padding-left: 25px;">
												<div style="width: 219px; overflow: hidden;">
												<div style="color: #959595; padding-bottom: 5px;" class="eagle-bold title20">Current Poll</div>
												<div style="padding-bottom: 10px;">
													<% if (current.getId().equals(pollId)) { %>
													<span class="title12">&lt;&lt;&lt; <%= current.getQuestion() %></span>
													<% } else { %>
													<a href="/myfd/poll_archive.jsp?pollId=<%= current.getId() %>" class="title12"><%= current.getQuestion() %></a>
													<% } %>
													<div>
														<span class="text10" style="color: #959595;"><%= DateUtil.relativeDifferenceAsString2(new Date(), current.getCreated()) %></span>
													</div>							
												</div>
												<%
												Iterator<Poll> it = polls.iterator();
												it.next();
												if (it.hasNext()) {
												%>
												<div style="color: #959595; padding: 10px 0px 5px;" class="eagle-bold title20">Closed Polls</div>
												<%
												}
												while (it.hasNext()) {
													Poll poll = it.next();
												%>
												<div style="padding-bottom: 10px;">
													<% if (poll.getId().equals(pollId)) { %>
													<span class="title12">&lt;&lt;&lt; <%= poll.getQuestion() %></span>
													<% } else { %>
													<a href="/myfd/poll_archive.jsp?pollId=<%= poll.getId() %>" class="title12"><%= poll.getQuestion() %></a>
													<% } %>
													<div>
														<span class="text10" style="color: #959595;"><%= DateUtil.relativeDifferenceAsString2(new Date(), poll.getCreated()) %></span>
													</div>							
												</div>							
												<%
												}
												%>
												</div>
											</div>
										</td>
									</tr>
								</table>
								<% } else { %><%-- poll list empty --%>
								<table cellpadding="0" cellspacing="0" border="0" style="width: 456px; height: 456px;">
									<tr>
										<td style="text-align: center; vertical-align: middle;" class="title18or">Sorry for the delay, but the FreshDirect poll archive is currently unavailable. Please check back later!</td>
									</tr>
								</table>
								<% } %>
							</div>
						</div>
					</td>
					<td style="width: 212px; text-align: left; vertical-align: top;">
						<div style="width: 212px; overflow: hidden;">
			                <% if (FDStoreProperties.isAdServerEnabled()) { %>
							<div>
			                    <script type="text/javascript">
			                    OAS_AD("PollArchive");
			                    </script>                    
							</div>
			                <% } %>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</tmpl:put>
</tmpl:insert>
