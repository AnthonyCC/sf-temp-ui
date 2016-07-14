package com.freshdirect.cms.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;

/**
 * Simple implementation of {@link com.freshdirect.cms.application.CmsRequestI}.
 * 
 * @TODO we don't really need interface/implementation separation here
 */
public class CmsRequest implements CmsRequestI {

	private final UserI user;
	private final Map<ContentKey, ContentNodeI> nodes = new HashMap<ContentKey, ContentNodeI>();
	private final Source source;
	private final DraftContext draftContext;
	private final RunMode runMode;
	
    public CmsRequest(UserI user) {
        this(user, Source.ELSE);
    }

    public CmsRequest(UserI user, Source source) {
        this(user, source, DraftContext.MAIN);
    }

    public CmsRequest(UserI user, Source source, DraftContext draftContext) {
        this(user, source, draftContext, RunMode.NORMAL);
    }

    public CmsRequest(UserI user, Source source, DraftContext draftContext, RunMode runMode) {
        this.user = user;
        this.source = source;
        this.draftContext = draftContext != null ? draftContext : DraftContext.MAIN;
        this.runMode = runMode;
    }

    @Override
	public UserI getUser() {
		return user;
	}

    @Override
	public void addNode(ContentNodeI node) {
		nodes.put(node.getKey(), node);
	}

    @Override
	public Collection<ContentNodeI> getNodes() {
		return nodes.values();
	}

    @Override
    public Source getSource() {
        return this.source;
    }

    @Override
	public String toString() {
		return "CmsRequest[" + user + ", " + nodes + ", "+ draftContext.getDraftName() +"]";
	}

    @Override
    public DraftContext getDraftContext() {
        return this.draftContext;
    }

    @Override
    public RunMode getRunMode() {
        return runMode;
    }
    
    public boolean isDryMode(){
        return RunMode.DRY == runMode;
    }
}