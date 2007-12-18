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
		
        String userAgent = request.getHeader("User-Agent");
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
        
        for (Iterator botIter = friendlyRobotSet.iterator(); botIter.hasNext(); ) {
            String agentName = (String) botIter.next();
            if (userAgent.startsWith(agentName)) {
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
    
    
    private final static HashSet friendlyRobotSet = new HashSet();
    static {
        friendlyRobotSet.add("scooter");            //  AltaVista
        friendlyRobotSet.add("vscooter");           //  AltaVista
        friendlyRobotSet.add("googlebot");          //  Google
        friendlyRobotSet.add("lycos");              //  Lycos
        friendlyRobotSet.add("robozilla");          //  DMOZ
        friendlyRobotSet.add("teoma");              //  Teoma
        friendlyRobotSet.add("yahoo-fetch");        //  Yahoo
        friendlyRobotSet.add("cnet_snoop");         //  CNet
        friendlyRobotSet.add("webcrawler");         //  WebCrawler
        friendlyRobotSet.add("architextspider");    //  Excite
        friendlyRobotSet.add("excitespider");       //  Excite
        friendlyRobotSet.add("slurp");              //  HotBot
        friendlyRobotSet.add("inktomi");            //  HotBot
        friendlyRobotSet.add("infoseek");           //  InfoSeek
        friendlyRobotSet.add("ultraseek");          //  InfoSeek
        friendlyRobotSet.add("lnspiderguy");        //  LexisNexis
        friendlyRobotSet.add("metacrawler");        //  MetaCrawler
        friendlyRobotSet.add("overture");           //  Overture
        friendlyRobotSet.add("looksmart");          //  LookSmart
        friendlyRobotSet.add("ask");                //  Ask Jeeves
        friendlyRobotSet.add("fast");               //  All The Web
		friendlyRobotSet.add("yahooseeker");        //  Yahoo crawler
		friendlyRobotSet.add("sherlock");        	//  Apple's Sherlock
		friendlyRobotSet.add("msnbot");        		//  MSN
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
