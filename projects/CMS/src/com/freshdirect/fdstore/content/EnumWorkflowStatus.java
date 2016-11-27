package com.freshdirect.fdstore.content;

/**
 *  The workflow status of YMAL sets.
 *
 *  @see YmalSet
 */
public class EnumWorkflowStatus {

	/** The item is still being worked on, only shown internally. */
	public final static String PENDING_APPROVAL = "PENDING_APPROVAL";

	/** The item is active, shown on the storefront as well. */
	public final static String ACTIVE = "ACTIVE";

	/** The item reached the end of its life cycle, only shown internally. */
	public final static String COMPLETED = "COMPLETED";

}
