package com.freshdirect.tools.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.LinkedList;

import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;

public class WebStatsReport {

	private static Context jndiRoot;
	private static MBeanServer adminServer;
	private static Map servers;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 4) {
				printUsage();
			}
			System.out.println("Start of execution: "+new Date());
			jndiRoot = connectToJNDI(args[0], args[1], args[2]);
			adminServer = connectToServer(MBeanHome.ADMIN_JNDI_NAME, jndiRoot);
			servers = getServers(adminServer, jndiRoot);
			if (args[3].equals("stats")) {
				Map stats = getStats(servers);
				{
					Map aggregatedStats = aggregateStatsForServers(stats, stats.keySet());
					Set monitors = getMonitors(aggregatedStats.values());					
					System.out.println("Report 1: Aggregated statistics for each Monitor");
					System.out.println("================================================");
					System.out.println("Monitors found: "+monitors);
					System.out.println();
					for (Iterator it = monitors.iterator(); it.hasNext();) {
						String monitor = (String) it.next();
						System.out.println("Monitor: "+monitor);
						System.out.println("-----------------------------------");
						System.out.println();
						Collection statsForMon = getStatsForMonitor(aggregatedStats.values(), monitor);
	
						System.out.println("10 most used pages:");
						System.out.println("-------------------");
						printStats(sortStats(statsForMon, new MostHits()), 10);
	
						System.out.println("\n");
						System.out.println("10 slowest pages (on average):");
						System.out.println("------------------------------");
						printStats(sortStats(statsForMon, new SlowestUris()), 10);
	
						System.out.println("\n");
						System.out.println("All pages (ordered alphabetically:");
						System.out.println("----------------------------------");
						printStats(sortStats(statsForMon, new AlphabeticUris()));
						System.out.println();
						System.out.println();
					}
				}
				{
					System.out.println();
					System.out.println();
					System.out.println("Report 2: Statistics for each server separately");
					System.out.println("===============================================");
					System.out.println();
					for (Iterator sit = servers.keySet().iterator(); sit.hasNext();) {
						String server = (String) sit.next();
						System.out.println("Server: "+server);
						System.out.println("-------------------------");
						Map serverStats = (Map) stats.get(server);
						Set monitors = getMonitors(serverStats.values());						
						System.out.println("Monitors found: "+monitors);
						System.out.println();
						for (Iterator it = monitors.iterator(); it.hasNext();) {
							String monitor = (String) it.next();
							System.out.println("Monitor: "+monitor);
							System.out.println("-----------------------------------");
							System.out.println();
							Collection statsForMon = getStatsForMonitor(serverStats.values(), monitor);
		
							System.out.println("10 most used pages:");
							System.out.println("-------------------");
							printStats(sortStats(statsForMon, new MostHits()), 10);
		
							System.out.println("\n");
							System.out.println("10 slowest pages (on average):");
							System.out.println("------------------------------");
							printStats(sortStats(statsForMon, new SlowestUris()), 10);
		
							System.out.println("\n");
							System.out.println("All pages (ordered alphabetically:");
							System.out.println("----------------------------------");
							printStats(sortStats(statsForMon, new AlphabeticUris()));
							System.out.println();
							System.out.println();
						}						
					}
				}				
			} else if (args[3].equals("resetStats")) {
				String serverName = args.length >= 5 ? args[4] : null;
				String monitorName = args.length >= 6 ? args[5] : null;
				resetStats(servers, serverName, monitorName);
			} else if (args[3].equals("resetStatsForMonitor")) {
				resetStatsForMonitor(servers, args[4]);
			} else {
				printUsage();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printUsage() {
		System.out.println("Usage of WebStatsReport:");
		System.out.println("WebStatsReport <admin url> <user> <password> <command> [<parameters>]");
		System.out.println("Available commands:");
		System.out.println("    stats -- get web statistics from servers");
		System.out.println("    resetStats [<server> [<monitor>]] - reset statistics (optionally specify server and monitor)");
		System.out.println("    resetStatsForMonitor <monitor>] - reset statistics on all servers for a given monitor");
		System.out.println();
		System.exit(0);
	}
	
	public static Context connectToJNDI(String url, String user, String password) throws Exception {
		Hashtable t = new Hashtable();
		t.put(Context.PROVIDER_URL, url); //or whereever your server that runs the MBean server can be contacted.
		t.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		//		might be necessary depending on the settings in your weblogic server:
		t.put(Context.SECURITY_PRINCIPAL, user);//or whatever user you have
		t.put(Context.SECURITY_CREDENTIALS, password); //or whatever password that user has
		return new InitialContext(t);
	}

	public static MBeanServer connectToServer(String jndiName, Context ctx) throws Exception {
		MBeanHome home = (MBeanHome) ctx.lookup(jndiName);
		return home.getMBeanServer();		
	}

	public static Map getStats(Map servers) throws Exception {
		Map r = new HashMap();
		System.out.println("Gathering statistics...");
		for (Iterator sit = servers.entrySet().iterator(); sit.hasNext();) {
			Map.Entry e = (Map.Entry) sit.next();
			MBeanServer server = (MBeanServer) e.getValue();
			System.out.println("Processing server "+e.getKey());
			Set uristats = server.queryMBeans(new ObjectName("com.freshdirect:Type=WebRequestMonitor,Sensor=WebStats,*"), null); 
			Map serverStats = new HashMap();
			r.put(e.getKey(), serverStats);
			for (Iterator it = uristats.iterator(); it.hasNext(); ) {
				Object o = (Object) it.next();
				ObjectInstance oi = (ObjectInstance) o;
				if (oi.getClassName().equals("com.freshdirect.framework.monitor.UriStats")) {				
					UriStat st = new UriStat(
											oi.getObjectName().getKeyProperty("Name"),
											(String) server.getAttribute(oi.getObjectName(), "URI"),
											((Integer)server.getAttribute(oi.getObjectName(), "HitCount")).intValue(),
											((Long)server.getAttribute(oi.getObjectName(), "MaxExecTime")).longValue(),
											((Long)server.getAttribute(oi.getObjectName(), "MinExecTime")).longValue(),
											((Double)server.getAttribute(oi.getObjectName(), "AvgExecTime")).doubleValue()
					);
					serverStats.put(st.getKey(), st);
				}
			}
		}
		return r;
	}
	
	public static Map aggregateStatsForServers(Map stats, Collection serversToAggregate) {
		Map r = new HashMap();
		System.out.println("Aggregating server statistics...");
		for (Iterator sit = serversToAggregate.iterator(); sit.hasNext();) {
			String serverName = (String) sit.next();			
			Map serverStats = (Map) stats.get(serverName);
			for (Iterator it = serverStats.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry e = (Map.Entry) it.next();
				UriStat u = (UriStat) e.getValue();
				UriStat ru = (UriStat) r.get(u.getKey());
				if (ru == null) {
					ru = new UriStat(u.monitor, u.uri, u.hitCount, u.maxExecTime, u.minExecTime, u.avgExecTime);
					r.put(ru.getKey(), ru);					
				} else {
					double avgExecTime = ((u.avgExecTime * u.hitCount) + (ru.avgExecTime*ru.hitCount)) / (double) (u.hitCount + ru.hitCount);
					ru.hitCount += u.hitCount;
					ru.minExecTime = Math.min(ru.minExecTime, u.minExecTime);
					ru.maxExecTime = Math.max(ru.maxExecTime, u.maxExecTime);
					ru.avgExecTime = avgExecTime;
				}
			}
		}
		return r;
	}
	
	public static Collection getStatsForMonitor(Collection stats, String monitorName) {
		List l = new LinkedList();
		for (Iterator it = stats.iterator(); it.hasNext();) {
			UriStat u = (UriStat) it.next();
			if (u.monitor.equals(monitorName)) {
				l.add(u);
			}
		}
		return l;
	}
	
	public static Set getMonitors(Collection stats) {
		Set s = new HashSet();
		for (Iterator it = stats.iterator(); it.hasNext();) {
			UriStat u = (UriStat) it.next();
			if (!s.contains(u.monitor)) {
				s.add(u.monitor);
			}
		}
		return s;
	}
	
	public static Collection sortStats(Collection stats, Comparator filter) {
		List l = new ArrayList();
		l.addAll(stats);
		Collections.sort(l, filter);
		return l;
	}

	public static void printStats(Collection stats) {
		printStats(stats, -1, false);
	}
	
	public static void printStats(Collection stats, int num) {
		printStats(stats, num, false);
	}

	public static void printStats(Collection stats, int num, boolean printMonitor) {
		if (num <= 0) num = stats.size();
		int uriPadLength = 0;
		int monitorPadLength = 0;
		int maxMinPadLength = 12;
		for (Iterator it = stats.iterator(); it.hasNext(); ) {
			UriStat st = (UriStat) it.next();
			uriPadLength = st.uri.length() > uriPadLength ? st.uri.length() : uriPadLength;
			monitorPadLength = st.monitor.length() > monitorPadLength ? st.monitor.length() : monitorPadLength;
		}
		int i = 0;

		// Create header
		if (printMonitor) {				
			System.out.print(printPadded("", "Monitor", "\t", monitorPadLength +1, true));
		}
		System.out.print(printPadded("", "URI", "\t", uriPadLength +1, true));
		System.out.print(printPadded("",""+"Hits","\t", maxMinPadLength, false));
		System.out.print(printPadded("",""+"MaxExecTime","\t", maxMinPadLength, false));
		System.out.print(printPadded("",""+"MinExecTime","\t", maxMinPadLength, false));
		System.out.print(printPadded("",""+"AvgExecTime","\t", maxMinPadLength, false));
		System.out.println();

		for (Iterator it = stats.iterator(); it.hasNext() && i < num; ) {
			UriStat st = (UriStat) it.next();
			if (printMonitor) {				
				System.out.print(printPadded("", st.monitor, "\t", monitorPadLength +1, true));
			}
			System.out.print(printPadded("", st.uri, "\t", uriPadLength +1, true));
			System.out.print(printPadded("",""+st.hitCount,"\t", maxMinPadLength, false));
			System.out.print(printPadded("",""+st.maxExecTime,"\t", maxMinPadLength, false));
			System.out.print(printPadded("",""+st.minExecTime,"\t", maxMinPadLength, false));
			System.out.print(printPadded("",""+Math.round(st.avgExecTime),"\t", maxMinPadLength, false));
			System.out.println();
			i++;
		}
	}
	
	private static String printPadded(String prefix, String value, String suffix, int padding, boolean justifyLeft) {
		String pad = "";
		int paddDif = padding - value.length();
		while (paddDif > 0) {
			pad += " ";
			paddDif --;
		}
		String padLeft  = !justifyLeft ? pad : "";
		String padRight =  justifyLeft ? pad : "";
		return prefix+padLeft+value+padRight+suffix;
	}
	
	public static void resetStatsForMonitor(Map servers, String monitorName) throws Exception {
		resetStats(servers, null, monitorName);
	}

	public static void resetStatsForServer(Map servers, String serverName) throws Exception {
		resetStats(servers, serverName, null);
	}

	public static void resetStats(Map servers) throws Exception {
		resetStats(servers, null, null);
	}

	public static void resetStats(Map servers, String serverName, String monitorName) throws Exception {
		for (Iterator sit = servers.entrySet().iterator(); sit.hasNext();) {
			Map.Entry e = (Map.Entry) sit.next();
			MBeanServer server = (MBeanServer) e.getValue();
			if (serverName != null && !server.equals(serverName)) {
				continue;
			}
			System.out.println("Resetting stats on Server "+e.getKey());
			Set mbeans = server.queryMBeans(new ObjectName("com.freshdirect:Type=WebRequestMonitor,*,Sensor=WebStats"), null); 
			for (Iterator it = mbeans.iterator(); it.hasNext(); ) {
				Object o = (Object) it.next();
				ObjectInstance oi = (ObjectInstance) o;
				String monitor = (String) server.getAttribute(oi.getObjectName(), "Name");
				if (monitorName != null && !monitor.equals(monitorName)) {
					continue;
				}
				System.out.println("    Resetting stats on Monitor: "+monitor);
				server.invoke(oi.getObjectName(), "resetStats", null, null);
			}
		}
	}
	
	public static Map getServers(MBeanServer server, Context ctx) throws Exception {
		Map result = new HashMap();
		Set serverMbeans = server.queryMBeans(new ObjectName("*:Type=Server,*"), null);
		for (Iterator it = serverMbeans.iterator(); it.hasNext();) {
			ObjectInstance oi = (ObjectInstance) it.next();
			String serverName = (String) server.getAttribute(oi.getObjectName(), "Name");
			try {
				result.put(serverName, connectToServer("weblogic.management.home."+serverName, ctx));
			} catch (Exception e) {
				System.out.println("Unable to connect to server: "+serverName+", continuing...");
				continue;
			}
		}
		return result;		
	}
	
	public static class UriStat {
		public long maxExecTime;
		public long minExecTime;
		public double avgExecTime;
		public int hitCount;
		public String uri;
		public String monitor;
		
		public UriStat(String monitor, String uri, int hitCount, long maxExecTime, long minExecTime, double avgExecTime) {
			this.monitor = monitor;
			this.uri = uri;
			this.hitCount = hitCount;
			this.maxExecTime = maxExecTime;
			this.minExecTime = minExecTime;
			this.avgExecTime = avgExecTime;
		}

		public Object getKey() {
			if (monitor != null)
				return monitor+":"+uri;
			else
				return uri;
		}
	}
	
	public static class SlowestUris implements Comparator {

		public int compare(Object o1, Object o2) {
			UriStat u1 = (UriStat) o1;
			UriStat u2 = (UriStat) o2;
			
			return new Double(u2.avgExecTime).compareTo(new Double(u1.avgExecTime));
		}
	}
	
	public static class MostHits implements Comparator {

		public int compare(Object o1, Object o2) {
			UriStat u1 = (UriStat) o1;
			UriStat u2 = (UriStat) o2;
			
			return new Integer(u2.hitCount).compareTo(new Integer(u1.hitCount));
		}
	}

	public static class AlphabeticUris implements Comparator {

		public int compare(Object o1, Object o2) {
			UriStat u1 = (UriStat) o1;
			UriStat u2 = (UriStat) o2;
			
			return u1.uri.compareTo(u2.uri);
		}
	}
	
}
