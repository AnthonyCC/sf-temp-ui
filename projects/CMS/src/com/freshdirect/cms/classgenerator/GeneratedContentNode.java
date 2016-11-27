package com.freshdirect.cms.classgenerator;

import java.util.Map;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.DraftContext;

public abstract class GeneratedContentNode implements ContentNodeI {

    protected ContentKey key;
    protected DraftContext draftContext = DraftContext.MAIN;
    private boolean delete;    

    public GeneratedContentNode() {
    }
    
    @Override
    public ContentKey getKey() {
        return key;
    }
    
    public void setKey(ContentKey key) {
        this.key = key;
    }

    
    public DraftContext getDraftContext() {
        return draftContext;
    }
    
    public void setDraftContext(DraftContext draftContext) {
        this.draftContext = draftContext;
    }
    
    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean b) {
        this.delete = b;
    }
    
    /**
     * this is effectively a static method, just because the inheritance, made to instance method. User should never call it directly.   
     * It is public, because it needs to be overiden.
     * @return
     */
    public abstract Map getAttributeDefs();
    
    /**
     * this is effectively a static method, just because the inheritance, made to instance method. User should never call it directly.
     * It is public, because it needs to be overiden.
     *    
     * @return
     */
    public abstract void setAttributeDefs(Map defs);

    
    public abstract void setContentNodeGenerator(NodeGeneratorI generator);
    
    @Override
    public abstract Object getAttributeValue(String name);
    
    @Override
    public abstract boolean setAttributeValue(String name, Object value);

    public AttributeDefI getAttributeDef(String name) {
        return (AttributeDefI) getAttributeDefs().get(name);
    }

    public abstract void initAttributes();

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ContentNodeI) {
            ContentNodeI node = (ContentNodeI) obj;
            return key.equals(node.getKey());
        }
        return false;
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    public void setAttributeOrdinalHash(Map map) {
        
    }


}
