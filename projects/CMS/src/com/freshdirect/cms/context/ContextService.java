package com.freshdirect.cms.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentNodeSource;
import com.freshdirect.cms.application.DraftContext;

/**
 * Provides services to contextualize content nodes of an
 * underlying content node source {@link com.freshdirect.cms.application.ContentNodeSource}.
 * 
 * @see com.freshdirect.cms.context.ContextualContentNodeI
 */
public class ContextService {

	private static ContextService instance;

	private final ContentNodeSource source;

	public ContextService(ContentNodeSource contentService) {
		this.source = contentService;
	}

	public static ContextService getInstance() {
		return instance;
	}

	public static void setInstance(ContextService serviceInstance) {
		instance = serviceInstance;
	}

	/**
	 * Determine all valid parent contexts for a content key.
	 * 
	 * @param key content key, never null
	 * @return Collection of {@link Context}, never null
	 */
	public Collection<Context> getAllContextsOf( ContentKey key, DraftContext draftContext ) {
		List<Context> contexts = new ArrayList<Context>();
		Set<ContentKey> parentKeys = source.getParentKeys( key, draftContext );
		if ( parentKeys.isEmpty() ) {
			contexts.add( new Context( null, key ) );
		} else {
			for ( ContentKey parentKey : parentKeys ) {
				Collection<Context> parentContexts = getAllContextsOf( parentKey, draftContext );
				for ( Context ctx : parentContexts ) {
					contexts.add( new Context( ctx, key ) );
				}
			}
		}
		return contexts;
	}

	/**
	 * @return a content node in a hierarchical context that exhibits inheritance behavior
	 * 
	 * @throws IllegalArgumentException if the context path is not valid
	 */
	public ContextualContentNodeI getContextualizedContentNode(String path, DraftContext draftContext) throws IllegalArgumentException {
		ContextualContentNode lastNode = null;
		StringTokenizer stoke = new StringTokenizer(path, "/");
		while (stoke.hasMoreTokens()) {
			String token = stoke.nextToken();
			ContentKey key = ContentKey.getContentKey(token);
			//
			// this next chunk just attempts to validate paths as it builds them
			//
			if (lastNode != null) {
				if (!lastNode.getChildKeys().contains(key)) {
					throw new IllegalArgumentException("Invalid Path:"
						+ path
						+ " '"
						+ key
						+ "' is not a child of '"
						+ lastNode.getKey()
						+ "'");
				}
			}
			//
			// end validation chunk
			//
			ContentNodeI node = source.getContentNode(key, draftContext);
			if (node == null) {
				return null;
			}
			lastNode = new ContextualContentNode(lastNode, node);
		}
		return lastNode;
	}

	// convenience method
	public ContextualContentNodeI getContextualizedContentNode(Context ctx, DraftContext draftContext) throws IllegalArgumentException {
		return getContextualizedContentNode(ctx.getPath(), draftContext);
	}
}
