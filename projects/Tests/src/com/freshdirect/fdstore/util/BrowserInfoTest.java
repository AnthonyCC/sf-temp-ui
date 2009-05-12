package com.freshdirect.fdstore.util;

import com.freshdirect.webapp.taglib.fdstore.BrowserInfo;

import junit.framework.TestCase;

public class BrowserInfoTest extends TestCase {
	final String IE6_AGENTS[] = {
		"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)",
		"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; NeosBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
		"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
		"Mozilla/4.0 (compatible; MSIE 5.01; Windows 95; MSIECrawler) "
	};


	final String IE_AGENTS[] = {
		"Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)",
		"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/4.0)",
		"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)",
		"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)",
		"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Win64; x64; Trident/4.0)",
		"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; WOW64; Trident/4.0)",
		"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)"
	};


	final String FFX_AGENTS[] = {
		"Mozilla/5.0 (X11; U; Linux i686; pl-PL; rv:1.9.0.2) Gecko/20121223 Ubuntu/9.25 (jaunty) Firefox/3.8",
		"Mozilla/5.0 (Windows; U; Windows NT 5.1; ja; rv:1.9.2a1pre) Gecko/20090402 Firefox/3.6a1pre (.NET CLR 3.5.30729)",
		"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en-US; rv:1.9.1b4) Gecko/20090423 Firefox/3.5b4 GTB5",
		"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9pre) Gecko/2008040318 Firefox/3.0pre (Swiftfox)",
		"Mozilla/6.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.8) Gecko/2009032609 Firefox/3.0.8 (.NET CLR 3.5.30729)",
		"Mozilla/5.0 (X11; U; OpenBSD i386; en-US; rv:1.8.1.7) Gecko/20070930 Firefox/2.0.0.7",
		"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1.4pre) Gecko/20070509 Firefox/2.0.0.4pre (Swiftfox)",
		"Mozilla/5.0 (X11; U; OpenBSD i386; en-US; rv:1.8.0.8) Gecko/20061110 Firefox/1.5.0.8",
		"Mozilla/5.0 (Macintosh; U; PPC Mac OS X Mach-O; es-ES; rv:1.8.0.3) Gecko/20060426 Firefox/1.5.0.3",
		"Mozilla/5.0 (X11; U; FreeBSD i386; en-US; rv:1.8.0.2) Gecko/20060414 Firefox/1.5.0.2",
		"Mozilla/5.0 (Windows NT 5.2; U; de; rv:1.8.0) Gecko/20060728 Firefox/1.5.0",
		"Mozilla/5.0 (Macintosh; PPC Mac OS X; U; en; rv:1.8.0) Gecko/20060728 Firefox/1.5.0",
		"Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.7.6) Gecko/20050405 Firefox/1.0 (Ubuntu package 1.0.2)"
	};


	final String[] SAFARI_AGENTS = {
		"Mozilla/5.0 (Windows; U; Windows NT 5.1; en) AppleWebKit/526.9 (KHTML, like Gecko) Version/4.0dp1 Safari/526.8",
		"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_4; en-gb) AppleWebKit/528.4+ (KHTML, like Gecko) Version/4.0dp1 Safari/526.11.2",
		"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/528+ (KHTML, like Gecko) Version/4.0 Safari/528.16",
		"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.28 (KHTML, like Gecko) Version/3.2.2 Safari/525.28.1",
		"Mozilla/5.0 (Macintosh; U; PPC Mac OS X 10_4_11; it-it) AppleWebKit/525.27.1 (KHTML, like Gecko) Version/3.2.1 Safari/525.27.1",
		"Mozilla/5.0 (Windows; U; Windows NT 6.0; hu-HU) AppleWebKit/525.26.2 (KHTML, like Gecko) Version/3.2 Safari/525.26.13"
	};
	
	
	final String[] CHROME_AGENTS = {
		"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.A.B.C Safari/525.13",
		"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/530.8 (KHTML, like Gecko) Chrome/2.0.178.0 Safari/530.8",
		"Mozilla/5.0 (X11; U; Linux i686 (x86_64); en-US) AppleWebKit/530.7 (KHTML, like Gecko) Chrome/2.0.175.0 Safari/530.7"
	};


	final String[] OPERA_AGENTS = {
		"Opera/9.70 (Linux i686 ; U; zh-cn) Presto/2.2.0",
		"Opera/9.64 (Windows NT 6.1; U; de) Presto/2.1.1",
		"Opera/9.63 (X11; FreeBSD 7.1-RELEASE i386; U; en) Presto/2.1.1",
		"Mozilla/5.0 (Windows NT 5.1; U; en-GB; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.61",
		"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; ru) Opera 9.52",
		"Opera/9.51 (Macintosh; Intel Mac OS X; U; en)",
		"Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9b3) Gecko/2008020514 Opera 9.5",
		"Mozilla/4.1 (compatible; MSIE 5.0; Symbian OS; Nokia 3650;424) Opera 6.10  [en]",
		"Mozilla/4.0 (compatible; MSIE 6.0; Symbian OS; Nokia 6600/4.09.1; 6329) Opera 8.00 [en]",
		"Mozilla/4.1 (compatible; MSIE 5.0; Symbian OS Series 60 42) Opera 6.0  [en-US]",
		"Mozilla/4.1 (compatible; MSIE 5.0; EPOC) Opera 6.0  [it]Nokia/Series-9200",
		"Mozilla/4.1 (compatible; MSIE 5.0; EPOC) Opera 6.0  [en-US]",
		"Mozilla/4.0 (compatible; MSIE 5.0; Linux 2.4.18-rmk7-pxa3-embedix armv4l; 240x320) Opera 6.0  [en]"
	};


	final String[] IPHONE_AGENTS = {
		"Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3",
		"Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543 Safari/419.3",
		"Mozilla/5.0 (iPhone; U; CPU like Mac OS X; ja-jp) AppleWebKit/420.1 (KHTML, like Gecko) Version/3.0 Mobile/3A100a Safari/419.3"
	};


	final String[] ANDROID_AGENTS = {
		"Mozilla/5.0 (Linux; U; Android 0.5; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3",
		"Mozilla/5.0 (Linux; U; Android 1.0; en-us; dream) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2",
		"Mozilla/5.0 (Linux; U; Android 1.0; en-us; generic) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2"
	};
	
	
	/**
	 * Test Microsoft Internet Explorer (<= 6.0) UA strings
	 */
	public void testInternetExplorerSix() {
		for (int i=0; i<IE6_AGENTS.length; i++) {
			BrowserInfo bi = new BrowserInfo(IE6_AGENTS[i]);
			
			assertTrue(bi.isInternetExplorer());
			assertTrue(bi.isIE6());

			assertFalse(bi.isFirefox());
			assertFalse(bi.isWebKit());
			assertFalse(bi.isChrome());
			assertFalse(bi.isSafari());
			assertFalse(bi.isIPhone());
			assertFalse(bi.isAndroid());
			assertFalse(bi.isOpera());
			assertFalse(bi.isUnsupported());
		}
	}
	

	/**
	 * Test Microsoft Internet Explorer (> 6.0) UA strings
	 */
	public void testInternetExplorer() {
		for (int i=0; i<IE_AGENTS.length; i++) {
			BrowserInfo bi = new BrowserInfo(IE_AGENTS[i]);

			assertTrue(bi.isInternetExplorer());
			assertFalse(bi.isIE6());

			assertFalse(bi.isFirefox());
			assertFalse(bi.isWebKit());
			assertFalse(bi.isChrome());
			assertFalse(bi.isSafari());
			assertFalse(bi.isIPhone());
			assertFalse(bi.isAndroid());
			assertFalse(bi.isOpera());
			assertFalse(bi.isUnsupported());
		}
	}


	/**
	 * Test Mozilla Firefox UA strings
	 */
	public void testFirefox() {
		for (int i=0; i<FFX_AGENTS.length; i++) {
			BrowserInfo bi = new BrowserInfo(FFX_AGENTS[i]);

			assertFalse(bi.isInternetExplorer());
			assertFalse(bi.isIE6());

			assertTrue(bi.isFirefox());
			assertFalse(bi.isWebKit());
			assertFalse(bi.isChrome());
			assertFalse(bi.isSafari());
			assertFalse(bi.isIPhone());
			assertFalse(bi.isAndroid());
			assertFalse(bi.isOpera());
			assertFalse(bi.isUnsupported());
		}
	}


	/**
	 * Test Apple Safari UA strings
	 */
	public void testSafari() {
		for (int i=0; i<SAFARI_AGENTS.length; i++) {
			BrowserInfo bi = new BrowserInfo(SAFARI_AGENTS[i]);

			assertFalse(bi.isInternetExplorer());
			assertFalse(bi.isIE6());

			assertFalse(bi.isFirefox());
			assertTrue(bi.isWebKit());
			assertFalse(bi.isChrome());
			assertTrue(bi.isSafari());
			assertFalse(bi.isIPhone());
			assertFalse(bi.isAndroid());
			assertFalse(bi.isOpera());
			assertFalse(bi.isUnsupported());
		}
	}


	/**
	 * Test Google Chrome UA strings
	 */
	public void testChrome() {
		for (int i=0; i<CHROME_AGENTS.length; i++) {
			BrowserInfo bi = new BrowserInfo(CHROME_AGENTS[i]);

			assertFalse(bi.isInternetExplorer());
			assertFalse(bi.isIE6());

			assertFalse(bi.isFirefox());
			assertTrue(bi.isWebKit());
			assertTrue(bi.isChrome());
			assertFalse(bi.isSafari());
			assertFalse(bi.isIPhone());
			assertFalse(bi.isAndroid());
			assertFalse(bi.isOpera());
			assertFalse(bi.isUnsupported());
		}
	}


	/**
	 * Test Opera UA strings
	 */
	public void testOpera() {
		for (int i=0; i<OPERA_AGENTS.length; i++) {
			BrowserInfo bi = new BrowserInfo(OPERA_AGENTS[i]);

			assertFalse(bi.isInternetExplorer());
			assertFalse(bi.isIE6());

			assertFalse(bi.isFirefox());
			assertFalse(bi.isWebKit());
			assertFalse(bi.isChrome());
			assertFalse(bi.isSafari());
			assertFalse(bi.isIPhone());
			assertFalse(bi.isAndroid());
			assertTrue(bi.isOpera());
			assertFalse(bi.isUnsupported());
		}
	}


	/**
	 * Test Apple iPhone UA strings
	 */
	public void testIPhone() {
		for (int i=0; i<IPHONE_AGENTS.length; i++) {
			BrowserInfo bi = new BrowserInfo(IPHONE_AGENTS[i]);

			assertFalse(bi.isInternetExplorer());
			assertFalse(bi.isIE6());

			assertFalse(bi.isFirefox());
			assertTrue(bi.isWebKit());
			assertFalse(bi.isChrome());
			assertTrue(bi.isSafari());
			assertTrue(bi.isIPhone());
			assertFalse(bi.isAndroid());
			assertFalse(bi.isOpera());
			assertFalse(bi.isUnsupported());
		}
	}
	
	
	/**
	 * Test Google Android UA strings
	 */
	public void testAndroid() {
		for (int i=0; i<ANDROID_AGENTS.length; i++) {
			BrowserInfo bi = new BrowserInfo(ANDROID_AGENTS[i]);

			assertFalse(bi.isInternetExplorer());
			assertFalse(bi.isIE6());

			assertFalse(bi.isFirefox());
			assertTrue(bi.isWebKit());
			assertFalse(bi.isChrome());
			assertTrue(bi.isSafari());
			assertFalse(bi.isIPhone());
			assertTrue(bi.isAndroid());
			assertFalse(bi.isOpera());
			assertFalse(bi.isUnsupported());
		}
	}
}
