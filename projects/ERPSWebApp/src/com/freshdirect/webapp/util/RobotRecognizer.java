/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2002 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.util;

import java.util.*;
import java.util.regex.Pattern;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

import javax.servlet.http.*;

/**
 *
 * A utility class that recognizes friendly robots
 * based on a known list of User-Agents
 *
 * @author  $Author$
 * @version $Revision$
 *
 */
public class RobotRecognizer {
	
	private static Category LOGGER = LoggerFactory.getInstance(RobotRecognizer.class);

    /** Creates new RobotRecognizer */
    private RobotRecognizer() {
        super();
    }
    
    
    public static boolean isFriendlyRobot(HttpServletRequest request) {
		//
		// don't let robots index the site via an affiliate URL
		//
		if (request.getServerName().toLowerCase().indexOf("bestcellars") >= 0) {
			return false;
		}
		
        return isFriendlyRobot(request.getHeader("User-Agent"));
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
        
        for (Pattern friendlyRobotPattern : friendlyRobotPatternSet) {
            if (friendlyRobotPattern.matcher(userAgent).matches()) {
            	LOGGER.debug("friendly bot identified (matches pattern "+friendlyRobotPattern.pattern()+"): "+userAgent);
            	return true;
            }
        }
        
        return false;
    }
    
    
    public static boolean isHostileRobot(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        //
        // no user agent?  consider as hostile...
        //
        if ((userAgent == null)  || "".equals(userAgent)) {
        	return true;
        }
        userAgent = userAgent.toLowerCase();
        
        	
        for (Iterator botIter = hostileAgentSet.iterator(); botIter.hasNext(); ) {
            String agentName = (String) botIter.next();
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
    private final static Set<Pattern> friendlyRobotPatternSet = new HashSet<Pattern>();
    static {
        friendlyRobotPatternSet.add(Pattern.compile("scooter.*"));            //  AltaVista
        friendlyRobotPatternSet.add(Pattern.compile("vscooter.*"));           //  AltaVista
        friendlyRobotPatternSet.add(Pattern.compile("googlebot.*"));          //  Google
        friendlyRobotPatternSet.add(Pattern.compile("lycos.*"));              //  Lycos
        friendlyRobotPatternSet.add(Pattern.compile("robozilla.*"));          //  DMOZ
        friendlyRobotPatternSet.add(Pattern.compile("teoma.*"));              //  Teoma
        friendlyRobotPatternSet.add(Pattern.compile("yahoo-fetch.*"));        //  Yahoo
        friendlyRobotPatternSet.add(Pattern.compile("cnet_snoop.*"));         //  CNet
        friendlyRobotPatternSet.add(Pattern.compile("webcrawler.*"));         //  WebCrawler
        friendlyRobotPatternSet.add(Pattern.compile("architextspider.*"));    //  Excite
        friendlyRobotPatternSet.add(Pattern.compile("excitespider.*"));       //  Excite
        friendlyRobotPatternSet.add(Pattern.compile("slurp.*"));              //  HotBot
        friendlyRobotPatternSet.add(Pattern.compile("inktomi.*"));            //  HotBot
        friendlyRobotPatternSet.add(Pattern.compile("infoseek.*"));           //  InfoSeek
        friendlyRobotPatternSet.add(Pattern.compile("ultraseek.*"));          //  InfoSeek
        friendlyRobotPatternSet.add(Pattern.compile("lnspiderguy.*"));        //  LexisNexis
        friendlyRobotPatternSet.add(Pattern.compile("metacrawler.*"));        //  MetaCrawler
        friendlyRobotPatternSet.add(Pattern.compile("overture.*"));           //  Overture
        friendlyRobotPatternSet.add(Pattern.compile("looksmart.*"));          //  LookSmart
        friendlyRobotPatternSet.add(Pattern.compile("ask.*"));                //  Ask Jeeves
        friendlyRobotPatternSet.add(Pattern.compile("fast.*"));               //  All The Web
		friendlyRobotPatternSet.add(Pattern.compile("yahooseeker.*"));        //  Yahoo crawler
		friendlyRobotPatternSet.add(Pattern.compile("sherlock.*"));        	  //  Apple's Sherlock
		friendlyRobotPatternSet.add(Pattern.compile("msnbot.*"));        	  //  MSN
		friendlyRobotPatternSet.add(Pattern.compile("facebook.*"));        	  //  Facebook
		//APPDEV-3197 top requests
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.google\\.com/bot\\.html.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?addthis\\.com.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?akamai_site_analyze.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?panopta.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?pingdom.com_bot_version.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?check_http.*?nagios-plugins.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.bing\\.com/bingbot\\.htm.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://ahrefs\\.com/robot/.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.google\\.com/adsbot.html.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.tacitknowledge\\.com/halebot\\.shtml.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?ezooms\\.bot@gmail\\.com.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.majestic12\\.co\\.uk/bot\\.php.*"));
		//like '%bot%'
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://blekko\\.com/about/blekkobot.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://go\\.mail\\.ru/help/robots.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.semrush\\.com/bot\\.html.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?tweetmemebot.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?paperlibot.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?semrushbot.*"));
		//like '%crawl'
		friendlyRobotPatternSet.add(Pattern.compile(".*?ravencrawler.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.grapeshot\\.co\\.uk/crawler\\.php.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?crawler@alexa\\.com.*"));
		//like '%spiders%'
		friendlyRobotPatternSet.add(Pattern.compile(".*?360spider.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.baidu\\.com/search/spider\\.html.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?sogou web spider.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www.proximic.com/info/spider.php.*"));
		friendlyRobotPatternSet.add(Pattern.compile(".*?http://www\\.youdao\\.com/help/webmaster/spider.*"));
    }
    
    
    private final static HashSet hostileAgentSet = new HashSet();
    static {
        hostileAgentSet.add("bmclient");
        hostileAgentSet.add("martini");
        hostileAgentSet.add("freefind.com");
        hostileAgentSet.add("libwww");
        hostileAgentSet.add("microsoft data access");
        hostileAgentSet.add("microsoft url control");
        hostileAgentSet.add("lwp");
        hostileAgentSet.add("wget");
        hostileAgentSet.add("php");
        hostileAgentSet.add("ms front");
    }
    
    // ISTVAN 19/03/2007 for Moz5 googlebot 2.1
    // Mozilla/5.0 (compatible; googlebot/2.1; +http://www.google.com/bot.html)
    // from http://www.useragentstring.com/pages/Googlebot 
    private final static Vector googlebotUserAgentParts = new Vector(4);
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
        Set S = new HashSet(parts.length);
	    for(int i=0; i < parts.length; ++i) S.add(parts[i]);
	    return S.containsAll(googlebotUserAgentParts);
    }
    
    
}
