package com.freshdirect.webapp.util;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * A utility class that recognizes friendly robots
 * based on a known list of User-Agents
 */
public class RobotRecognizer {
	
    private static final Category LOGGER = LoggerFactory.getInstance(RobotRecognizer.class);

    public static boolean isFriendlyRobot(String userAgent, String serverName) {
		//
		// don't let robots index the site via an affiliate URL
		//
        if (serverName.toLowerCase().indexOf("bestcellars") >= 0) {
			return false;
		}
		
        return isFriendlyRobot(userAgent);
    }        
        
    public static boolean isFriendlyRobot(String userAgent) {

        //
        // no user agent?  very unfriendly...
        //
        if ((userAgent == null)  || "".equals(userAgent)) {
        	return false;
        }
        userAgent = userAgent.toLowerCase();

        // ISTVAN 19/03/2007; 
        // see isMoz5Googlebot, the old style strings still go through,
        // since they still start with "googlebot"
        if (isMoz5Googlebot(userAgent)) {
        	return true;
        }
        
        for (Pattern friendlyRobotPattern : friendlyRobotPatterns) {
            if (friendlyRobotPattern.matcher(userAgent).matches()) {
            	LOGGER.debug("friendly bot identified (matches pattern "+friendlyRobotPattern.pattern()+"): "+userAgent);
            	return true;
            }
        }
        
        return false;
    }
    
    
    public static boolean isHostileRobot(String userAgent) {
        //
        // no user agent?  consider as hostile...
        //
        if ((userAgent == null)  || "".equals(userAgent)) {
        	return true;
        }
        userAgent = userAgent.toLowerCase();

        for (String agentName : hostileAgents) {
            if (userAgent.startsWith(agentName)) {
            	return true;
            }
        }

        return false;
    }
    
    /**
     * list refreshed by APPDEV-3197, use the following queries to refresh the list from time to time: 
     * select count(*), user_agent from MIS.iplocator_event_log group by user_agent having count(*) > 1000 order by count(*) desc
	 * select count(*), user_agent from MIS.iplocator_event_log where lower(user_agent) like '%bot%' group by user_agent having count(*) > 100 order by count(*) desc
	 * select count(*), user_agent from MIS.iplocator_event_log where lower(user_agent) like '%crawl%' group by user_agent having count(*) > 100 order by count(*) desc
	 * select count(*), user_agent from MIS.iplocator_event_log where lower(user_agent) like '%spider%' group by user_agent having count(*) > 100 order by count(*) desc
     */
    private final static Set<Pattern> friendlyRobotPatterns = new HashSet<Pattern>();
    static {
        friendlyRobotPatterns.add(Pattern.compile("scooter.*"));            //  AltaVista
        friendlyRobotPatterns.add(Pattern.compile("vscooter.*"));           //  AltaVista
        friendlyRobotPatterns.add(Pattern.compile("googlebot.*"));          //  Google
        friendlyRobotPatterns.add(Pattern.compile("lycos.*"));              //  Lycos
        friendlyRobotPatterns.add(Pattern.compile("robozilla.*"));          //  DMOZ
        friendlyRobotPatterns.add(Pattern.compile("teoma.*"));              //  Teoma
        friendlyRobotPatterns.add(Pattern.compile("yahoo-fetch.*"));        //  Yahoo
        friendlyRobotPatterns.add(Pattern.compile("cnet_snoop.*"));         //  CNet
        friendlyRobotPatterns.add(Pattern.compile("webcrawler.*"));         //  WebCrawler
        friendlyRobotPatterns.add(Pattern.compile("architextspider.*"));    //  Excite
        friendlyRobotPatterns.add(Pattern.compile("excitespider.*"));       //  Excite
        friendlyRobotPatterns.add(Pattern.compile("slurp.*"));              //  HotBot
        friendlyRobotPatterns.add(Pattern.compile("inktomi.*"));            //  HotBot
        friendlyRobotPatterns.add(Pattern.compile("infoseek.*"));           //  InfoSeek
        friendlyRobotPatterns.add(Pattern.compile("ultraseek.*"));          //  InfoSeek
        friendlyRobotPatterns.add(Pattern.compile("lnspiderguy.*"));        //  LexisNexis
        friendlyRobotPatterns.add(Pattern.compile("metacrawler.*"));        //  MetaCrawler
        friendlyRobotPatterns.add(Pattern.compile("overture.*"));           //  Overture
        friendlyRobotPatterns.add(Pattern.compile("looksmart.*"));          //  LookSmart
        friendlyRobotPatterns.add(Pattern.compile("ask.*"));                //  Ask Jeeves
        friendlyRobotPatterns.add(Pattern.compile("fast.*"));               //  All The Web
		friendlyRobotPatterns.add(Pattern.compile("yahooseeker.*"));        //  Yahoo crawler
		friendlyRobotPatterns.add(Pattern.compile("sherlock.*"));        	  //  Apple's Sherlock
		friendlyRobotPatterns.add(Pattern.compile("msnbot.*"));        	  //  MSN
		friendlyRobotPatterns.add(Pattern.compile("facebook.*"));        	  //  Facebook
		//APPDEV-3197 top requests
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.google\\.com/bot\\.html.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?addthis\\.com.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?akamai_site_analyze.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?panopta.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?pingdom.com_bot_version.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?check_http.*?nagios-plugins.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.bing\\.com/bingbot\\.htm.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://ahrefs\\.com/robot/.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.google\\.com/adsbot.html.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.tacitknowledge\\.com/halebot\\.shtml.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?ezooms\\.bot@gmail\\.com.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.majestic12\\.co\\.uk/bot\\.php.*"));
		//like '%bot%'
		friendlyRobotPatterns.add(Pattern.compile(".*?http://blekko\\.com/about/blekkobot.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://go\\.mail\\.ru/help/robots.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.semrush\\.com/bot\\.html.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?tweetmemebot.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?paperlibot.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?semrushbot.*"));
		//like '%crawl'
		friendlyRobotPatterns.add(Pattern.compile(".*?ravencrawler.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.grapeshot\\.co\\.uk/crawler\\.php.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?crawler@alexa\\.com.*"));
		//like '%spiders%'
		friendlyRobotPatterns.add(Pattern.compile(".*?360spider.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.baidu\\.com/search/spider\\.html.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?sogou web spider.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www.proximic.com/info/spider.php.*"));
		friendlyRobotPatterns.add(Pattern.compile(".*?http://www\\.youdao\\.com/help/webmaster/spider.*"));
		friendlyRobotPatterns.add(Pattern.compile("rogerbot.*"));
		friendlyRobotPatterns.add(Pattern.compile("^.*halebot.*"));
		friendlyRobotPatterns.add(Pattern.compile("adidxbot.*"));
		
		//START : OCT 2016 Start Blocking more bots and crawlers
		friendlyRobotPatterns.add(Pattern.compile("^.*mastercard server.*$"));
		friendlyRobotPatterns.add(Pattern.compile("^.*pinterest.*$")); // Pinterest/0.2 (+http://www.pinterest.com/)
		friendlyRobotPatterns.add(Pattern.compile("^.*slurp.*$")); // Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)
		friendlyRobotPatterns.add(Pattern.compile("^.*yandexbot.*$")); // Mozilla/5.0 (compatible; YandexBot/3.0; +http://yandex.com/bots)
		friendlyRobotPatterns.add(Pattern.compile("^.*myagent.*$"));	// MyAgent/1.0
		friendlyRobotPatterns.add(Pattern.compile("^.*test.*$"));
		friendlyRobotPatterns.add(Pattern.compile("^.*webclient.*$"));
		friendlyRobotPatterns.add(Pattern.compile("^.*adsbot.*$")); // AdsBot-Google (+http://www.google.com/adsbot.html)
		friendlyRobotPatterns.add(Pattern.compile("^.*twitterbot.*$")); // Twitterbot/1.0
		friendlyRobotPatterns.add(Pattern.compile("^.*slackbot.*$")); // Slackbot-LinkExpanding 1.0 (+https://api.slack.com/robots)
		friendlyRobotPatterns.add(Pattern.compile("^.*feedfetcher.*$"));
		friendlyRobotPatterns.add(Pattern.compile("^.*zoombot.*$"));
		friendlyRobotPatterns.add(Pattern.compile("^.*dwbot.*$"));
		friendlyRobotPatterns.add(Pattern.compile("^.*yisouspider.*$")); // YisouSpider
		friendlyRobotPatterns.add(Pattern.compile("^.*wordpress.*$"));
		friendlyRobotPatterns.add(Pattern.compile("^.*ccbot.*$")); // CCBot/2.0 (http://commoncrawl.org/faq/)
		friendlyRobotPatterns.add(Pattern.compile("^.*mj12bot.*$")); // Mozilla/5.0 (compatible; MJ12bot/v1.4.6; http://mj12bot.com/)
		friendlyRobotPatterns.add(Pattern.compile("^.*okhttp.*$"));  // okhttp/3.4.1
		friendlyRobotPatterns.add(Pattern.compile("^.*maxpointcrawler.*$")); // MaxPointCrawler/Nutch-1.10 (maxpoint.crawler at maxpointinteractive dot com)
		friendlyRobotPatterns.add(Pattern.compile("^.*alertbot.*$")); // Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.1; AlertBot)
		friendlyRobotPatterns.add(Pattern.compile("^.*prerender.*$")); //Mozilla/5.0 (Unknown; Linux x86_64) AppleWebKit/538.1 (KHTML, like Gecko) PhantomJS/2.1.1 Safari/538.1 Prerender (+https://github.com/prerender/prerender)
		friendlyRobotPatterns.add(Pattern.compile("^.*weborama.*$")); // weborama-fetcher (+http://www.weborama.com)
		friendlyRobotPatterns.add(Pattern.compile("^.*cxense.*$")); // Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en-US) AppleWebKit/533.3 (KHTML, like Gecko) cXensebot/1.1a; +http://www.cxense.com/bot.html/0.1 Safari/533.3
		friendlyRobotPatterns.add(Pattern.compile("^.*outlook.*$")); //Microsoft Office/15.0 (Windows NT 6.1; Microsoft Outlook 15.0.4919; Pro)
		//END : OCT 2016 Start Blocking more bots and crawlers
		
		//START : Feb 2017 Start Blocking Movable Ink email campaign web crop module 
		friendlyRobotPatterns.add(Pattern.compile("^.*movableink.*$")); // Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_7; en-us) AppleWebKit/533.21.1 (KHTML, like Gecko) Version/5.0.5 Safari/533.3 with MovableInk
		//END : Feb 2017 Start Blocking Movable Ink email campaign web crop module 
    }
    
    public static void main(String a[]) {
    	
    	LOGGER.info("IsFriendly Robot : "+ "Pinterest/0.2 (+http://www.pinterest.com/)" + " --> " 
    			+ RobotRecognizer.isFriendlyRobot("Pinterest/0.2 (+http://www.pinterest.com/)") );
    }
    
    private final static Set<String> hostileAgents = new HashSet<String>();
    static {
        hostileAgents.add("bmclient");
        hostileAgents.add("martini");
        hostileAgents.add("freefind.com");
        hostileAgents.add("libwww");
        hostileAgents.add("microsoft data access");
        hostileAgents.add("microsoft url control");
        hostileAgents.add("lwp");
        hostileAgents.add("wget");
        hostileAgents.add("php");
        hostileAgents.add("ms front");
    }
    
    // ISTVAN 19/03/2007 for Moz5 googlebot 2.1
    // Mozilla/5.0 (compatible; googlebot/2.1; +http://www.google.com/bot.html)
    // from http://www.useragentstring.com/pages/Googlebot 
    private final static Vector<String> googlebotUserAgentParts = new Vector<String>(4);
    static { 
        googlebotUserAgentParts.add("mozilla/5.0");
        googlebotUserAgentParts.add("compatible");
        googlebotUserAgentParts.add("googlebot/2.1");
        googlebotUserAgentParts.add("+http://www.google.com/bot.html");
     };

    
    // regular white-space, parantheses and semicolons token delimiter
    private final static Pattern googlebotUserAgentPattern = Pattern.compile("[ \t\n\f\r;\\(\\)]+");
    
    /** Whether user agent is a new google bot.
     * 
     * Expects <tt>Mozilla/5.0 (compatible; googlebot/2.1; +http://www.google.com/bot.html)</tt>;
     * with some leanience on the placement of white spaces and ordering.
     *  @param userAgent (expected to be lower-case)
     *  @return if userAgent matches the Mozilla 5.0 googlebot 2.1 user agent string
     */
    public static boolean isMoz5Googlebot(String userAgent) {
        String [] parts = googlebotUserAgentPattern.split(userAgent);
        Set<String> s = new HashSet<String>(parts.length);
        for (int i = 0; i < parts.length; ++i) {
            s.add(parts[i]);
        }
        return s.containsAll(googlebotUserAgentParts);
    }
    
}
