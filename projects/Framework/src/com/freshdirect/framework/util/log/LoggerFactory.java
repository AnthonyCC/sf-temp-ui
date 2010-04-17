package com.freshdirect.framework.util.log;

import org.apache.log4j.*;
import org.apache.log4j.xml.*;
import java.net.URL;

/**
 * Small factory wrapper for Apache Log4J.
 *
 * The preferred way to get an logger instance:
 * <PRE>
 *  import com.freshdirect.framework.util.log.LoggerFactory;
 *  import org.apache.log4j.Logger;
 *
 * 	private static Logger LOGGER = LoggerFactory.getInstance( MyClass.class );
 * </PRE>
 * Usage sample:
 * <PRE>
 *  LOGGER.debug("Blah blah");
 * </PRE>
 *
 * @author Viktor Szathmary
 */

public class LoggerFactory {

	private static boolean configured=false;

	/**
	 * Shorthand for getInstance(clazz.getName())
	 */	
	public static Logger getInstance( Class<?> clazz ) {
		checkConfig();
		return Logger.getLogger(clazz);
	}

	/**
	 * Retrieve a logger with named as the name parameter
	 */	
	public static Logger getInstance( String name ) {
		checkConfig();
		return Logger.getLogger(name);
	}


	private final static void checkConfig() {
		if (!configured) {
			if ( !(LogManager.getCurrentLoggers().hasMoreElements() ||
				LogManager.getRootLogger().getAllAppenders().hasMoreElements()) ) {
					// it was not automatically configured, so...
					try {
						// try XML based configuration first
						URL logURL = ClassLoader.getSystemResource("log4j.xml");
						if (logURL==null) {
							throw new NullPointerException();
						}
						DOMConfigurator.configureAndWatch( logURL.getFile() );
					} catch (Exception ex) {
						// fall back to basic
						BasicConfigurator.configure();
					}
					
			}
			configured=true;
		}
	}

	private static Logger LOGGER = LoggerFactory.getInstance( LoggerFactory.class );

	/** 
	 * Demo
	 */
	public static void main(String args[]) {
		LOGGER.debug("My own stuff");
		LOGGER.info("Others might be interested in this");
		LOGGER.warn("This stinks, but never mind");
		LOGGER.error("Very bad");
		LOGGER.error("Very bad with exception", new IllegalArgumentException());
	}
		
	
}

