/*
 * Created on Nov 9, 2004
 */
package com.freshdirect.cms.context;

import java.io.IOException;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;

/**
 * Representation of a hierarchical context-path.
 * 
 * @see com.freshdirect.cms.context.ContextualContentNodeI
 */
public class Context {

    private final Context parentContext;
    private final ContentKey cKey;

    /**
     * @param parentContext
     *            parent context, may be null
     * @param cKey
     *            content key of this context, never null
     */
    public Context(Context parentContext, ContentKey cKey) {
        if (cKey == null) {
            throw new IllegalArgumentException("The ContentKey for a Context must not be null");
        }
        this.cKey = cKey;
        this.parentContext = parentContext;
    }

    /**
     * @return true if this is the root of the context hierarchy
     */
    public boolean isRoot() {
        return this.parentContext == null;
    }

    /**
     * @return content key of this context, never null
     */
    public ContentKey getContentKey() {
        return this.cKey;
    }

    /**
     * @return parent context or null if root
     */
    public Context getParentContext() {
        return this.parentContext;
    }

    public Context getRootContext() {
        return this.parentContext == null ? this : this.parentContext.getRootContext();
    }

    /**
     * @return slash-separated path, never null. Slash is not good for separator character, media type nodes will have an invalid path!
     */
    @Deprecated
    public String getPath() {
        return getPath("", '/');
    }

    private String getPath(String path, char separator) {
        path = separator + this.cKey.getEncoded() + path;
        if (this.isRoot()) {
            return path;
        }
        return this.parentContext.getPath(path, separator);
    }

    public String getPath(char separator) {
        return getPath("", separator);
    }

    /**
     * Get a human readable label for this context.
     * Depends on {@link ContentServiceI} to look up parent nodes for their labels.
     * 
     * @return human readable path
     */
    public String getLabel(ContentServiceI contentService, DraftContext draftContext) {
        return getLabelPath("", contentService, draftContext);
    }

    private String getLabelPath(String path, ContentServiceI contentService, DraftContext draftContext) {
        String p = contentService.getContentNode(cKey, draftContext).getLabel();
        if (!"".equals(path)) {
            p += " > " + path;
        }
        if (this.isRoot()) {
            return p;
        }
        return this.parentContext.getLabelPath(p, contentService, draftContext);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();

        buf.append("Ctx(");
        if (isRoot()) {
            buf.append("ROOT:");
            appendContentKey(buf, getContentKey());
        } else {
            appendContentKey(buf, getContentKey());
            buf.append("->");
            appendContentKey(buf, getParentContext().getContentKey());
        }
        buf.append(")");

        return buf.toString();
    }

    private void appendContentKey(Appendable buf, ContentKey key) {
        try {
            buf.append(key.getType().toString());
            buf.append(":");
            buf.append(key.getId());
        } catch (IOException e) {
        }
    }
}
