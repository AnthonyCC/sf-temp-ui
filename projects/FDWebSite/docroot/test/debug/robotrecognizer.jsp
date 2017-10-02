<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.freshdirect.webapp.util.RobotRecognizer"%>
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>
<h1>Friendly Robot Recognizer Test Page</h1>

<h2>Known Bots Test</h2>
based on 30/07/13 state of<br>
select count(*), user_agent from MIS.iplocator_event_log group by user_agent having count(*) > 1000 order by count(*) desc<br>
select count(*), user_agent from MIS.iplocator_event_log where lower(user_agent) like '%bot%' group by user_agent having count(*) > 100 order by count(*) desc<br>
select count(*), user_agent from MIS.iplocator_event_log where lower(user_agent) like '%crawl%' group by user_agent having count(*) > 100 order by count(*) desc<br>
select count(*), user_agent from MIS.iplocator_event_log where lower(user_agent) like '%spider%' group by user_agent having count(*) > 100 order by count(*) desc<br>
<br/>
<%
String[] userAgents = new String[]{ 
	//legacy
	"scooter",
	"vscooter",
	"googlebot",
	"lycos",
	"robozilla",
	"teoma",
	"yahoo-fetch",
	"cnet_snoop",
	"webcrawler",
	"architextspider",
	"excitespider",
	"slurp",
	"inktomi",
	"infoseek",
	"ultraseek",
	"lnspiderguy",
	"metacrawler",
	"overture",
	"looksmart",
	"ask",
	"fast",
	"yahooseeker",
	"sherlock",
	"msnbot",
	"facebook",
	//top requests
	"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
	"AddThis.com robot tech.support@clearspring.com",
	"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; CHKD 1.2; Akamai_Site_Analyzer)",
	"Panopta v1.1",
	"Pingdom.com_bot_version_1.4_(http://www.pingdom.com/)",
	"check_http/v1.4.15 (nagios-plugins 1.4.15)",
	"msnbot/2.0b (+http://search.msn.com/msnbot.htm)",
	"Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)",
	"Mozilla/5.0 (compatible; AhrefsBot/5.0; +http://ahrefs.com/robot/)",
	"msnbot-UDiscovery/2.0b (+http://search.msn.com/msnbot.htm)",
	"AdsBot-Google (+http://www.google.com/adsbot.html)",
	"Halebot (Mozilla/5.0 compatible; Halebot/2.1; http://www.tacitknowledge.com/halebot.shtml)",
	"Mozilla/5.0 (compatible; Ezooms/1.0; ezooms.bot@gmail.com)",
	"Mozilla/5.0 (compatible; MJ12bot/v1.4.3; http://www.majestic12.co.uk/bot.php?+)",
	//bots
	"msnbot/0.01 (+http://search.msn.com/msnbot.htm)",
	"Mozilla/5.0 (compatible; Blekkobot; ScoutJet; +http://blekko.com/about/blekkobot)",
	"Mozilla/5.0 (compatible; Linux x86_64; Mail.RU_Bot/2.0; +http://go.mail.ru/help/robots)",
	"DoCoMo/2.0 N905i(c100;TB;W24H16) (compatible; Googlebot-Mobile/2.1; +http://www.google.com/bot.html)",
	"Mozilla/5.0 (compatible; SemrushBot/0.96.3; +http://www.semrush.com/bot.html)",
	"Mozilla/5.0 (compatible; TweetmemeBot/3.0; +http://tweetmeme.com/)",
	"Mozilla/5.0 (compatible; PaperLiBot/2.1; http://support.paper.li/entries/20023257-what-is-paper-li)",
	//crawlers
	"RavenCrawler",
	"Mozilla/5.0 (compatible; GrapeshotCrawler/2.0; +http://www.grapeshot.co.uk/crawler.php)",
	"ia_archiver (+http://www.alexa.com/site/help/webmasters; crawler@alexa.com)",
	//spiders
	"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.0.11)  Firefox/1.5.0.11; 360Spider",
	"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; MDDR; .NET4.0C; .NET4.0E; .NET CLR 1.1.4322; Tablet PC 2.0); 360Spider",
	"Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)",
	"Sogou web spider/4.0(+http://www.sogou.com/docs/help/webmasters.htm#07)",
	"Mozilla/5.0 (compatible; proximic; +http://www.proximic.com/info/spider.php)",
	"Mozilla/5.0 (compatible; YoudaoBot/1.0; http://www.youdao.com/help/webmaster/spider/; )"
};
%>
<table cellspacing="5"><%
for (String ua : userAgents){
	%><tr><td><%=ua%></td><td width="100"><%=RobotRecognizer.isFriendlyRobot(ua)?" is a bot" : "is NOT a bot"%></td></tr><%
}

%></table>

<h2>My User-Agent Test</h2>
<%=request.getHeader("User-Agent")%><br>
<%=RobotRecognizer.isFriendlyRobot(request.getHeader("User-Agent"))?" is a bot" : "is NOT bot"%><br>


<h2>Custom User-Agent Test</h2>
<% String ua = request.getParameter("useragent"); 
%>
<form>
	<input type="text" name="useragent" size="100" value="<%=ua%>"/>
	<input type="submit" value="Check User-Agent"/>
</form>

<%=RobotRecognizer.isFriendlyRobot(ua)?" is a bot" : "is NOT a bot"%><br>
</body>
</html>
