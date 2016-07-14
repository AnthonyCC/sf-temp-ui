package com.freshdirect.cms.merge;

import com.freshdirect.cms.CmsRuntimeException;

/**
 * Exception indicating a merge attempt.
 */
public class MergeException extends CmsRuntimeException {

    private static final long serialVersionUID = 1763095799927636213L;

    public MergeException(String message) {
        super(message);
    }

    public MergeException(Throwable throwable) {
        super(throwable);
    }

    public MergeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
