package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
		friendlyRobotPatterns.add(Pattern.compile("^.*phantomjs.*$")); //Mozilla/5.0 (Unknown; Linux x86_64) AppleWebKit/538.1 (KHTML, like Gecko) PhantomJS/2.1.1 Safari/538.1 Prerender (+https://github.com/prerender/prerender)
		//END : OCT 2016 Start Blocking more bots and crawlers
		
		//START : Feb 2017 Start Blocking Movable Ink email campaign web crop module 
		friendlyRobotPatterns.add(Pattern.compile("^.*movableink.*$")); // Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_7; en-us) AppleWebKit/533.21.1 (KHTML, like Gecko) Version/5.0.5 Safari/533.3 with MovableInk
		//END : Feb 2017 Start Blocking Movable Ink email campaign web crop module 
		
		friendlyRobotPatterns.add(Pattern.compile("^.*bot.*$")); //Try to catch all bots
		friendlyRobotPatterns.add(Pattern.compile("^.*prerender.*$")); //Prerender used for FoodKick SEO
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
    
    public static void main(String a[]) {
    	List<String> bAgents = new ArrayList<String>();
    	
    	//Good Browsers
    	bAgents.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
    	bAgents.add("Mozilla/5.0 (Windows NT 5.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586");
    	bAgents.add("Mozilla/5.0 (iPad; CPU OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Version/10.0 Mobile/14D27 Safari/602.1");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; Trident/7.0; rv:11.0) like Gecko");
    	bAgents.add("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.65 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
    	bAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/603.3.8 (KHTML, like Gecko) Version/10.1.2 Safari/603.3.8");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36 OPR/43.0.2442.991");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36 OPR/42.0.2393.94");
    	bAgents.add("Mozilla/5.0 (Windows NT 5.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 5.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240");
    	bAgents.add("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 5.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
    	bAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/601.4.4 (KHTML, like Gecko) Version/9.0.3 Safari/601.4.4");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
    	bAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Version/10.0 Mobile/14D27 Safari/602.1");
    	bAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/602.4.8 (KHTML, like Gecko) Version/10.0.3 Safari/602.4.8");
    	bAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E188a Safari/601.1");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.104 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.1 Safari/603.1.30");
    	bAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/601.5.17 (KHTML, like Gecko) Version/9.1 Safari/601.5.17");
    	bAgents.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/603.2.4 (KHTML, like Gecko) Version/10.1.1 Safari/603.2.4");
    	bAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
    	bAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
    	
    	//Good and Bad Bots
    	bAgents.add("rogerbot/1.2 (https://moz.com/help/guides/moz-procedures/what-is-rogerbot, rogerbot-crawler+aardwolf-crawl-production-crawler-18@moz.com)");
    	bAgents.add("Mozilla/5.0 (Unknown; Linux x86_64) AppleWebKit/538.1 (KHTML, like Gecko) PhantomJS/2.1.1 Safari/538.1 Prerender (+https://github.com/prerender/prerender)");
    	bAgents.add("rogerbot/1.2 (https://moz.com/help/guides/moz-procedures/what-is-rogerbot, rogerbot-crawler+aardwolf-crawl-production-crawler-60@moz.com)");
    	bAgents.add("Caliperbot/1.0 (+http://www.conductor.com/caliperbot)");
    	bAgents.add("Mozilla/5.0 (compatible; YandexBot/3.0; +http://yandex.com/bots)");
    	bAgents.add("Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)");
    	bAgents.add("Mozilla/5.0 (compatible; SemrushBot/1.2~bl; +http://www.semrush.com/bot.html)");
    	bAgents.add("Zend\\Http\\Client");
    	bAgents.add("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
    	bAgents.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; CHKD 1.2; Akamai_Site_Analyzer)");
    	bAgents.add("Companybook-Crawler (+https://www.companybooknetworking.com/)");
    	bAgents.add("Pingdom.com_bot_version_1.4_(http://www.pingdom.com/)");
    	bAgents.add("Microsoft Office/14.0 (Windows NT 6.1; Microsoft Outlook 14.0.7177; Pro)");
    	bAgents.add("CCBot/2.0 (http://commoncrawl.org/faq/)");
    	bAgents.add("Panopta v1.1");
    	bAgents.add("Mozilla/5.0 (Windows; U; Windows NT 6.0; en-GB; rv:1.0; trendictionbot0.5.0; trendiction search; http://www.trendiction.de/bot; please let us know of any problems; web at trendiction.com) Gecko/20071127 Firefox/3.0.0.11");
    	bAgents.add("Mozilla/5.0 (compatible; MJ12bot/v1.4.8; http://mj12bot.com/)");
    	bAgents.add("Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)");
    	bAgents.add("AdsBot-Google (+http://www.google.com/adsbot.html)");
    	bAgents.add("Mozilla/5.0 (compatible; DotBot/1.1; http://www.opensiteexplorer.org/dotbot, help@moz.com)");

    	for(String userAgent : bAgents) {
    		RobotRecognizer.isFriendlyRobot(userAgent);
    	}
    }

    
}
