package com.freshdirect.webapp.taglib.fdstore.display;

import java.util.Random;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.JspMethods;


/**
 * Tag to generate unique ID that can be used in JavaScript code too.
 * 
 * @author segabor
 *
 */
public class GetContentNodeWebIdTag extends AbstractGetterTag<String> {
	private static final long serialVersionUID = 189469086197243864L;
	private static Random rnd = new Random();

	// OPTIONAL
	private String prefix = null;
	
	// MANDATORY
	private ContentNodeModel product;
	
	// OPTIONAL: Generate JavaScript safe identifier if true
	private boolean clientSafe = false;
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	
	public void setProduct(ContentNodeModel product) {
		this.product = product;
	}


	/**
	 * Generate JavaScript safe ID
	 * (ie. can be used as JS variable name)
	 * 
	 * @param clientSafe
	 */
	public void setClientSafe(boolean clientSafe) {
		this.clientSafe = clientSafe;
	}


	@Override
	protected String getResult() throws Exception {
		return GetContentNodeWebIdTag.getWebId(prefix, product, clientSafe);
	}



	public static String getWebId(String prefix, ContentNodeModel node, boolean javaScriptSafe) {
		StringBuilder key = new StringBuilder();
		
		key.append( prefix != null ? prefix : "CTNT" );
		key.append( "_" );
		key.append( Integer.toHexString(node.getContentName().hashCode()) );
		/// key.append( "_" );
		key.append( Long.toHexString(rnd.nextLong()) );
		
		final String uid = key.toString();

		return javaScriptSafe ? JspMethods.safeJavaScriptVariable(uid) : uid;
	}



	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return String.class.getCanonicalName();
		}
	}
}
