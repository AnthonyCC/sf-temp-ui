package com.freshdirect.webapp.soy;

import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.javasrc.SoyTemplateRuntimes;
import com.google.template.soy.tofu.SoyTofu;

/**
 *	Common class for using two Google Closure (soy) backends interchangeably:
 *	
 *  * the experimental Java Source backend for debug mode (com.google.template.soy.javasrc),
 *  * and the Java Object backend (a.k.a. Tofu) for production mode (com.google.template.soy.tofu).
 *  
 *   Construct one of these with either a SoyTofu or a SoyTemplateRuntimes bundle, 
 *   and you get one common render method.
 *   
 * @author treer
 */
public class AbstractSoyTemplate {

	private SoyTemplateRuntimes runtimes;
	private SoyTofu tofu;
	
	public AbstractSoyTemplate( SoyTemplateRuntimes runtimes ) {
		this.runtimes = runtimes;
	}
	
	public AbstractSoyTemplate( SoyTofu tofu ) {
		this.tofu = tofu;
	}
	
	public String render( String template, SoyMapData data ) {
		if ( tofu != null ) {
			return tofu.newRenderer( template ).setData( data ).render();
		} else if ( runtimes != null ) {
			return runtimes.newRenderer( template ).setData( data ).render();
		} else {
			throw new IllegalStateException( "Empty AbstractTofu, aborting." );
		}
	}
}
