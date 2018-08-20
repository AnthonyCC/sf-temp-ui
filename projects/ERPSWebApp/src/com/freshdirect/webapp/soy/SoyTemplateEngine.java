package com.freshdirect.webapp.soy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyDataException;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import com.google.template.soy.tofu.SoyTofuException;


public class SoyTemplateEngine {

	/**
	 * Base folder for soy templates
	 */
	public static final String SOY_BASE = "/WEB-INF/shared/soy/";
	
	/**
	 * Base namespace for soy server rendered templates
	 */
	private static final String SOY_SERVER_TEMPLATE_BASE = "serverRendered/";
	
	/**
	 * The usual logger...
	 */
	private static final Logger LOGGER = LoggerFactory.getInstance( SoyTemplateEngine.class );
	
	/**
	 * Soy specific properties - package dependencies
	 * 
	 * java.util.Properties is thread-safe, no external synchronization is required.
	 */
	private static Properties props = new Properties();
		
	/**
	 * Cache for compiled templates. ConcurrentHashMap is used to avoid using synchronized blocks on each access.
	 * 
	 * Key: 'package name' a.k.a. namespace
	 * Value: compiled template as an AbstractTofu 
	 */
	private Map<String,AbstractSoyTemplate> tofuCache = new ConcurrentHashMap<String, AbstractSoyTemplate>();

	/**
	 * Cache for compiled javascript sources. ConcurrentHashMap is used to avoid using synchronized blocks on each access.
	 * 
	 * To be used only in debug mode. TODO: currently it is used for both...
	 * 
	 * Key: 'package name' a.k.a. namespace
	 * Value: compiled javascript sources (List of Strings) 
	 */
	private Map<String,List<String>> jsCache = new ConcurrentHashMap<String, List<String>>();
	
	
	
	/**
	 * Singleton instance
	 */
	private static SoyTemplateEngine instance = null;
	
	/**
	 * Singleton getInstance method 
	 */
	public static final synchronized SoyTemplateEngine getInstance() {
		if ( instance == null )
			instance = new SoyTemplateEngine();
		
		return instance;
	}
	
	/**
	 * Private empty constructor
	 */
	private SoyTemplateEngine() {
	}
	
	
	
	/**
	 * Render a soy template with the given data.
	 * 
	 * Will load and compile the template if needed.
	 * 
	 * @param servletCtx
	 * @param template
	 * @param data
	 * @return
	 * @throws IOException and some soy specific runtime exceptions
	 */
	public String render( ServletContext servletCtx, String template, SoyMapData data ) throws IOException {
		//LOGGER.info( "Rendering template: " + template );
		
		if ( data == null ) {
			LOGGER.warn( "Missing data. Skipping rendering of null." );
			throw new SoyDataException( "Failed to render template with null data for " + template );
		}
		
		String packageName = packagePathOf( template );
		AbstractSoyTemplate tofu; 
		
		//----------------
		if ( FDStoreProperties.isSoyDebugMode() ) {
			// Always compile in debug mode
			load( servletCtx, packageName );
			tofu = tofuCache.get( packageName );
			
		} else {
			// Use cache in production mode
			tofu = tofuCache.get( packageName );			
			if ( tofu == null ) {
				load( servletCtx, packageName );
				tofu = tofuCache.get( packageName );
			}			
		}
		
		//----------------
		
		if ( tofu == null ) {
			LOGGER.error( "Failed to load template tofu for " + template );
			throw new SoyTofuException( "Failed to load template tofu for " + template );
		}
		
		String result = tofu.render( template, data ); 
		//LOGGER.info( "Successfully rendered template: " + template );
		
		return result;
	}	
	
	
	/**
	 * Provides the javascript source bundle for a given package
	 * 
	 * @param servletCtx
	 * @param packageName
	 * @return
	 */
	public List<String> getJsSrc( ServletContext servletCtx, String packageName ) throws IOException {
		//LOGGER.info( "Getting javascript sources for package: " + packageName );

		List<String> jsSrc; 
		
		if ( FDStoreProperties.isSoyDebugMode() ) {
			// Always compile in debug mode
			load( servletCtx, packageName );
			jsSrc = jsCache.get( packageName );
			
		} else {
			// Use cache in production mode
			jsSrc = jsCache.get( packageName );			
			if ( jsSrc == null ) {
				load( servletCtx, packageName );
				jsSrc = jsCache.get( packageName );
			}
		}
		
		
		//LOGGER.info( "Successfully got javascript sources for package: " + packageName );
		
		return jsSrc;
	}

	
	
	
	/**
	 * Internal method to load and compile a soy template package.
	 * 
	 * @param servletCtx
	 * @param packageName
	 * @return
	 * @throws IOException
	 */
	private final void load( ServletContext servletCtx, String packageName ) throws IOException {
		
		if ( props.isEmpty() ) {
			loadProperties( servletCtx );
		}
			
		//LOGGER.info( "Loading package " + packageName );		
		
		SoyFileSet sfsWithDeps = buildSoyFileSet( servletCtx, packageName, true );	
		SoyFileSet sfsNoDeps = buildSoyFileSet( servletCtx, packageName, false );	
		
		//LOGGER.info( "Loaded package " + packageName );
		
		try {
			AbstractSoyTemplate tofu;
			if ( FDStoreProperties.isSoyDebugMode() ) {
				
//				// Compile to 'Tofu' - using the 'experimental java src backend' 
//				// DO NOT USE the experimental backend, there are important features missing, e.g. deltemplates, variants
//				
//				tofu = new AbstractSoyTemplate( sfs.compileToRuntimes( "x", new SoyJavaSrcOptions(), null ) );
//				tofuCache.put( packageName, tofu );
//				LOGGER.info( "Package " + packageName + " compiled successfully to SoyTemplateRuntimes" );

				// Compile to 'Tofu' - using the 'classic tofu' backend
				tofu = new AbstractSoyTemplate( sfsWithDeps.compileToTofu() );
				tofuCache.put( packageName, tofu );
				LOGGER.info( "Package " + packageName + " compiled successfully to SoyTemplateRuntimes" );
				
				// Compile to JS - use default compiler options, and no message bundle
				List<String> jsSrc = sfsNoDeps.compileToJsSrc( new SoyJsSrcOptions(), null );
				jsCache.put( packageName, jsSrc );
				LOGGER.info( "Package " + packageName + " compiled successfully to javascript source" );
			} else {
				
				// Compile to 'production Tofu'
				tofu = new AbstractSoyTemplate( sfsWithDeps.compileToTofu() );
				tofuCache.put( packageName, tofu );
				LOGGER.info( "Package " + packageName + " compiled successfully to SoyTofu" );
				
				// TODO: using internal js compile in production mode too - need to analyze 
				// Compile to JS - use default compiler options, and no message bundle
				List<String> jsSrc = sfsNoDeps.compileToJsSrc( new SoyJsSrcOptions(), null );
				jsCache.put( packageName, jsSrc );
				LOGGER.info( "Package " + packageName + " compiled successfully to javascript source" );
			}
		} catch (RuntimeException e) {
			// Some error occurred while compiling
			LOGGER.error( "Error while compiling template", e );
			
			// remove previous if any
			tofuCache.remove( packageName );
			jsCache.remove( packageName );
			throw e;
		}
	}

	/**
	 * Internal method to load the properties
	 * 
	 * @param servletCtx
	 * @throws IOException
	 */
	private final void loadProperties( ServletContext servletCtx ) throws IOException {		
		//LOGGER.info( "Loading properties" );
		
		InputStream propsStream = servletCtx.getResourceAsStream( SOY_BASE + "soy.properties" );			
		props.clear();
		props.load( propsStream );
		propsStream.close();
					
		//LOGGER.info( "Loaded properties" );
	}

	/**
	 * Helper method to get the package path from a fully qualified template name.
	 * 
	 * e.g. "package.subpackage.template" becomes "package/subpackage"
	 * 
	 * @param template
	 * @return
	 */
	public static final String packagePathOf( String template ) {
		return template.substring( 0, template.lastIndexOf( '.' ) ).replace( '.', '/' );
	}

	/**
	 * Helper method to remove leading and trailing '/' characters.
	 * 
	 * e.g. "/package/subpackage/" becomes "package/subpackage"
	 * 
	 * @param packageName
	 * @return
	 */
	public static final String cleanPackageName( String packageName ) {
		if ( packageName.startsWith( "/" ) )
			packageName = packageName.substring( 1 );
		if ( packageName.endsWith( "/" ) )
			packageName = packageName.substring( 0, packageName.length()-1 );
		return packageName;
	}
	
	/**
	 * Method to build a complete soy-fileset for a given package
	 * @param resolveDependencies 
	 * @return
	 */
	private static final SoyFileSet buildSoyFileSet( ServletContext servletCtx, String pkgStr, boolean resolveDependencies ) throws IOException {
		
		//LOGGER.info( "Building fileset for package " + pkgStr );
		Set<String> soyFiles = new HashSet<String>();
		collectSoyFiles( servletCtx, soyFiles, pkgStr, resolveDependencies );
		
		SoyFileSet.Builder sfsb = new SoyFileSet.Builder();
		for ( String path : soyFiles ) {
			if ( FDStoreProperties.isSoyDebugMode() ) {
				// Add as volatile files
				path = servletCtx.getRealPath( path );
				File soyFile = new File( path );
				sfsb.addVolatile( soyFile );
				LOGGER.info( "Loaded " + path + ", as volatile input file.");
			} else {
				// Add as resource urls
				URL soyUrl = servletCtx.getResource( path ); 
				sfsb.add( soyUrl );
				LOGGER.info( "Loaded " + soyUrl + ", as resource.");
			}
		}
		SoyFileSet sfs = sfsb.build();
		//LOGGER.info( "Fileset built for package " + pkgStr );
		
		return sfs;
	}

	
	/**
	 * Helper method to collect soy files recursively.
	 * 
	 * @param servletCtx
	 * @param resolveDependencies 
	 * @param soyFiles
	 * @param path
	 */
	private static final void collectSoyFiles( ServletContext servletCtx, Set<String> result, String pkgStr, boolean resolveDependencies ) {
		
		Set<String> soyPaths = new HashSet<String>();
		soyPaths.add( pkgStr );
		
		if ( resolveDependencies ) {
			String dependencies = (String)props.get( pkgStr );
			// server rendered templates by default have dependency to the base namespace
			if (dependencies == null && pkgStr.startsWith(SOY_SERVER_TEMPLATE_BASE)) {
				dependencies = pkgStr.replaceFirst(SOY_SERVER_TEMPLATE_BASE, "");
				String subDep = (String)props.get( dependencies );
				if (subDep != null) {
					dependencies += " ," + subDep;
				}
			}
			if ( dependencies != null ) {
				StringTokenizer tok = new StringTokenizer( dependencies, " ," );
				while ( tok.hasMoreTokens() ) {
					soyPaths.add( tok.nextToken() );
				}
			}
		}
		
		for ( String path : soyPaths ) {
			@SuppressWarnings( "unchecked" )
			Set<String> files = servletCtx.getResourcePaths( SOY_BASE + path );
			if ( files != null ) {
				for ( String file : files ) {
					if ( file.contains( "/." ) ) {
						// skip hidden files/folders
						//LOGGER.info( "Skipping " + file );
						continue;
					}
					if ( file.endsWith( ".soy" ) ) {
						// add soy files
						//LOGGER.info( "Adding " + file );
						result.add( file );
					} else if ( file.endsWith( "/" ) ) {
						// add subfolders
						//LOGGER.info( "Adding folder " + file );
						file = file.substring( SOY_BASE.length(), file.length()-1 );
						collectSoyFiles( servletCtx, result, file, resolveDependencies );
					}
				}
			}
		}
		
	}
	
    /**
     * Converts any bean that is serializable to JSON to a java Map. To be used for server side Soy template rendering. default date formatter: MM/dd/yyyy
     * 
     * @param bean
     * @return
     */
    public static Map<String, Object> convertToMap(Object bean) {
        return convertToMap(bean, new SimpleDateFormat("MM/dd/yyyy"));
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> convertToMap(Object bean, DateFormat dateFormat) {
        ObjectMapper m = new ObjectMapper();
        m.setDateFormat(dateFormat);
        Map<String,Object> map = m.convertValue( bean, Map.class );
        return map;
    }

}
