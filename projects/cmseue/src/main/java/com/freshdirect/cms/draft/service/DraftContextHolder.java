package com.freshdirect.cms.draft.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.draft.domain.DraftContext;

@Service
public class DraftContextHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DraftContextHolder.class);

    private final ThreadLocal<DraftContext> currentDraftContext;

    public DraftContextHolder() {
        currentDraftContext = new ThreadLocal<DraftContext>() {

            @Override
            protected DraftContext initialValue() {
                LOGGER.debug("initializing draft context with main draft.");
                return DraftContext.MAIN;
            }
        };
    }

    public DraftContext getDraftContext() {
        return currentDraftContext.get();
    }

    public void setDraftContext(DraftContext draftContext) {
        LOGGER.debug("setting draftContext to " + draftContext);
        currentDraftContext.set(draftContext);
    }
}
