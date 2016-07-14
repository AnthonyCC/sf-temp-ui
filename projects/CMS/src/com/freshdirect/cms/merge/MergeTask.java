package com.freshdirect.cms.merge;

/**
 * A step in the merge process.
 */
public interface MergeTask {

    /**
     * Perform a merge/validation task depend on the context of a given MergeResult.
     * 
     * @param merge contextual info about the merge/validation
     */
    void execute(MergeResult merge);

}
