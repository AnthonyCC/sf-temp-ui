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
<%
String successPage = "/your_account/customer_profile_summary.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/your_account/customer_profile_summary.jsp");
request.setAttribute("listPos", "HPLeftTop");
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    ErpCustomerInfoModel customerInfo = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
        customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);	
    }
	
	FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
	EnumServiceType serviceType = FDSurveyFactory.getServiceType(user, request);

	FDSurvey customerProfileSurvey = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.CUSTOMER_PROFILE_SURVEY, serviceType);
	FDSurveyResponse surveyResponse= FDSurveyFactory.getCustomerProfileSurveyInfo(customerIdentity, serviceType);
    int coverage=SurveyHtmlHelper.getResponseCoverage(customerProfileSurvey,surveyResponse);
    %>
    <% if(coverage==0) {%>
    <jsp:forward page='<%="/your_account/brownie_points.jsp?"+request.getQueryString()%>' />
    
    <%}%>    
    
    <%
    List questions = null;
    String profileImagePath="";
    String[] birthDay=null;
    if(customerProfileSurvey!=null) {
        questions=customerProfileSurvey.getQuestions();
        if(surveyResponse!=null && surveyResponse.getAnswer(FDSurveyConstants.PROFILE)!=null) {
            profileImagePath=surveyResponse.getAnswer(FDSurveyConstants.PROFILE)[0].toLowerCase();
        } 
        if(surveyResponse!=null && surveyResponse.getAnswer(FDSurveyConstants.BIRTHDAY)!=null) {
            birthDay=surveyResponse.getAnswer(FDSurveyConstants.BIRTHDAY);
        } 
        
    }
    

response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
%>

<style>

	/* default styles from website css */
	body { font-family: Verdana, Arial, sans-serif; font-size: 10px; height: 100%; }
	a:link, a:visited { color:#360; }
	a:active { color:#f90; }


	/* these are summary styles. (combined with table styles) */
	#promocol { display: table; table-layout: fixed;}
	#promocol1, #promocol2, #promocol3 { display: table-cell; }
	#promocol1 { width: 33%; border: 2px solid red;}
	#promocol2 { width: 33%; border: 2px solid blue; }
	#promocol3 { width: 33%; border: 2px solid green;}
	#prettylink {width:675; align:center;}
	#sharebox {border:1; border-style:solid; border-color:#D4D9DE; border-width: thin;}
	#shareheader1 {padding: 15px; font-style:bold; float:left;}
	#emailnumber {background-color:#89A54F; color:#ffffff; padding:2px; }
	#emailtextbox { border:1; border-style:solid; border-color:#D4D9DE; border-width: thin; padding:10px; width:300px;}
	#emailbuttons {float:left;}
	#emailbuttons {float:left;}
	.managebut {float:left;}
	.submitbut {float:right;}
	#friends {padding:10px;}

</style>

<script type="text/javascript" src="http://www.plaxo.com/css/m/js/util.js"></script>
<script type="text/javascript" src="http://www.plaxo.com/css/m/js/basic.js"></script>
<script type="text/javascript" src="http://www.plaxo.com/css/m/js/abc_launcher.js"></script>


<script type="text/javascript"><!--
function onABCommComplete() {
  // OPTIONAL: do something here after the new data has been populated in your text area
  var eCount = 0;
  var element = document.getElementById("recipient_list")
  if(element != null) {
	  var data = element.value;  
	  //window.alert("data:" + data);
	  if(data.trim().length > 0) {
		var currentTagTokens = data.split( "," );
		if(currentTagTokens.length > 0)
			eCount = currentTagTokens.length;
	  }
   }
   document.getElementById("emailnumber").innerHTML=eCount;
}

//--></script>

<tmpl:insert template='/common/template/brownie_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
<tmpl:put name='content' direct='true'>

<script language="javascript">
window.fbAsyncInit = function() {
 FB.init({appId: '207273882654288', status: true, cookie: true, xfbml: true});
 
     /* All the events registered */
     FB.Event.subscribe('auth.login', function(response) {
         // do something with response
         login();
     });
     FB.Event.subscribe('auth.logout', function(response) {
         // do something with response
         logout();
     });
 
     FB.getLoginStatus(function(response) {
         if (response.session) {
             // logged in and connected user, someone you know
             login();
			 //window.alert('calling friends');
			 getFriends();
         }
     });
	 
 };
 
</script>

<script language="javascript">
function callFBUI() {
	//process FBUI here
	//First get the selected email addresses
	var toString = new Array();
	var cnt = 0;
	for(i=0;i<6;i++) {
		var felement = document.getElementById("friend" + i);
		if(felement != null && felement.checked) {
			//friend is selected so add them to the string
			//toString += felement.value + ",";
			toString[cnt] = felement.value;
			cnt++;
		}
	}
	//alert(toString);
	FB.ui(
   {
     method: 'send',
     name: 'FreshDirect.com - Welcome',
     link: 'http://www.freshdirect.com/invites/swathikanury',
     picture: 'http://www.freshdirect.com/media/images/navigation/global_nav/fd_logo_off.gif',
     caption: 'www.Freshdirect.com',
     description: 'Images and text TBD. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
	 to:toString
   },
   function(response) {
     if (response && response.post_id) {
       alert('Post was published.' + response);
     } else {
       alert('Post was not published.' + response);
     }
   }
	);

}

function randomFromTo(from, to){
   return Math.floor(Math.random() * (to - from + 1) + from);
}
</script>

<!-- Start of promotion bar -->	
<IMG src="/media_stat/images/layout/line_cccccc.gif" WIDTH="675" HEIGHT="1" BORDER="0" /><BR>
<table width="675" cellpadding="0" cellspacing="5" border="0">
<tr>
<td width="273" align="center"><font color="#CE9870" face="arial black, monaco, chicago" size=2>SHARE YOUR LOVE</font><br/>for freshdirect with your <br/>friends and family</td>
<td width="1" bgcolor="#cccccc"><img src="1-1.gif" width="1" height="1" border="0" alt="" /></td>
<td width="273" align="center"><font color="#000000" face="arial black, monaco, chicago" size=2>GIVE</font> <font color="#CE9870" face="arial black, monaco, chicago" size=2>test1</font><br/>Give text <i>Its on us!</i></td>
<td width="1" bgcolor="#cccccc"><img src="1-1.gif" width="1" height="1" border="0" alt="" /></td>
<td width="273" align="center"><font color="#000000" face="arial black, monaco, chicago" size=2>GET</font> <font color="#A6A64D" face="arial black, monaco, chicago" size=2>$15</font><br/>get text</td>

</tr>
</table>
<IMG src="/media_stat/images/layout/line_cccccc.gif" WIDTH="675" HEIGHT="1" BORDER="0" /><BR>
<table width="675" cellpadding="0" cellspacing="5" border="0" >
<tr>
<td width="273" align="center"></td>
<td width="2" bgcolor="#ffffff"></td>
<td width="547" align="left" style="font-size:9px; font-style:italic; color:#cccccc">*Credit to be automatically applied to your account when your friend gets delivery for their first order.</td>
</tr>
</table>

<p>
<div id="prettylink">
<b>Share your unique link!</b> Add to any email, tweet or post it on Facebook. <br/>
<button style="background-color:#B871B8;color:white;border-radius:40px;-moz-border-radius:5px;height:30px;" onClick="history.go(-1)"><b>www.freshdirect.com/invite/ruchidesai %></b></button>
</div>
</p>

<table width="675" cellpadding="0" cellspacing="5" border="0">
<tr>
<td width="75%" valign="top">
	<form name="sendmails" action="brownie_points.jsp" method="post">	
	<div id="sharebox">
		<% if("sendmails".equals(request.getParameter("action"))) { %>
		<input type="hidden" name="action" value="processmails"/>
		<div id="sharelabel"><img src="background2.gif" width="100%" height="20"></div>
		<div id="shareheader1">
			Enter a personal message for your friend or family.
		</div>
		
		<table width="100%" cellpadding="5px">
			<tr>
				<td>
					<textarea id="recipient_list" name="recipient_list" rows="6" cols="52">Hi,
I thought you'd love the delicious food from FreshDirect. I use it all the time!
					
Enjoy!
					</textarea>
				</td>
			</tr>
		</table>
		<table width="100%" cellpadding="5px" style="background-color:#E2E6E9;"><tr><td>
			<a href="/your_account/manage_invities.jsp" class="managebut">manage invites</a>
			<INPUT TYPE="image" src="/media/images/buttons/COS_submit.gif" class="submitbut"/>
		</td></tr></table>
		<% } else { %>
		<input type="hidden" name="action" value="sendmails"/>
		<div id="sharelabel"><img src="background1.gif" width="100%" height="20"></div>
		<div id="shareheader1">
			Your Email list <label id="emailnumber"></label>
		</div>
		
		<table width="100%" cellpadding="5px">
			<tr>
				<td>
					<textarea id="recipient_list" name="recipient_list" rows="6" cols="42" onchange="updateCount()"></textarea>
				</td>
				<td class="emailbuttons">
					<a href="#" onclick="showPlaxoABChooser('recipient_list', 'your_account/plaxo_cb.html'); return false">GMail</a><br/>
					<a href="#" onclick="showPlaxoABChooser('recipient_list', 'your_account/plaxo_cb.html'); return false">AOL</a><br/>
					<a href="#" onclick="showPlaxoABChooser('recipient_list', 'your_account/plaxo_cb.html'); return false">Yahoo!</a><br/>
					<a href="#" onclick="showPlaxoABChooser('recipient_list', 'your_account/plaxo_cb.html'); return false">Outlook</a>
				</td>
			</tr>
		</table>
		<table width="100%" cellpadding="5px" style="background-color:#E2E6E9;"><tr><td>
			<a href="/your_account/manage_invities.jsp" class="managebut">manage invites</a>
			<INPUT TYPE="image" src="/media/images/buttons/COS_submit.gif" class="submitbut"/>
		</td></tr></table>		
		<% } %>
		
		</form>
	</div>
</td>
<script language="javascript">onABCommComplete()</script>
<td width="25%" valign="top">Post it on your Facebook or Tweeter
<br/>
<div id="fb-root"></div><fb:login-button>Login</fb:login-button>
      <script src="http://platform.twitter.com/widgets.js" type="text/javascript"></script>
        <a href="http://twitter.com/share?url=http%3A%2F%2Ffreshdirect.com%2Finvites%2Fswathikanury&text=$25%20off%20your%20first%20order%20when%20you%20order%20from%20freshdirect.&amp;count=none" class="twitter-share-button">Tweet</a>
<br/>
<div id="login"></div>
<div id="friends"></div>
</td>
</tr>
</table>
<script language="javascript">

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
                    //document.getElementById('login').style.display = "block";
                    //document.getElementById('login').innerHTML = response.name + " succsessfully logged in!<br/>" + response.id;	
                });
            }
            function logout(){
                document.getElementById('login').style.display = "none";
            }
			
			function getFriends(){
                FB.api('/me/friends?fields=name,picture,id', function(response) {
					var divInfo = document.getElementById("friends");
                    var friends = response.data;
					divInfo.innerHTML = "";
					var htmlString = "<span style=\"float:left\">Tell your friends</span><span style=\"float:right\"><a href=\"#\" onclick=\"getFriends()\"> <img src=\"/media_stat/images/buttons/refresh.jpg\" /></a></span>  <br/> <hr width=\"100%\"> <br> New! Tell your Facebook friends about Freshdirect with a private message. <br/> <table width=\"100%\" cellpadding=\"2px\">";
					var j = randomFromTo(0, friends.length);
					//alert("friends.length:"+ friends.length + "-j:" + j);
					var cnt = 0;
					for (var i = j; i < friends.length; i++) {
						if(cnt<6) {						
							htmlString += "<tr><td valign=\"center\"><input type=\"checkbox\" id=\"friend" + cnt + "\" name=\"friend" + cnt + "\" value=\""+ friends[i].id + "\"/></td>" ;
							htmlString += "<td><img src=\"" + friends[i].picture + "\" /></td><td valign=\"center\">";
							htmlString += friends[i].name +"</td></tr>";							
						}
						cnt++;
					}
					if(cnt < 6) {
						for(i=cnt; i<6;i++) {
							var k = randomFromTo(0, j);
							htmlString += "<tr><td valign=\"center\"><input type=\"checkbox\" id=\"friend" + cnt + "\" name=\"friend" + cnt + "\" value=\""+ friends[k].id + "\"/></td>" ;
							htmlString += "<td><img src=\"" + friends[k].picture + "\" /></td><td valign=\"center\">";
							htmlString += friends[k].name + "</td></tr>";		
						}
					}
					htmlString += "</table><br/><a href=\"#\" class=\"previewbut\" onclick=\"callFBUI()\">Preview Message</a>";
					divInfo.innerHTML = htmlString;

                });
            }
			
 </script>

<!-- End of promotion bar -->						


<!-- * end the actual summary info * -->
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="675" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
<tr VALIGN="TOP">
<td WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></td>
<td WIDTH="640"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></td>
</tr>

</TABLE>


	</tmpl:put>
</tmpl:insert>
