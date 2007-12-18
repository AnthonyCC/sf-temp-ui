/**
 * @author ekracoff
 * Created on Mar 14, 2005*/

package com.freshdirect.cms.publish;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

/**
 * Enumeration of content publishing status codes.
 */
public class EnumPublishStatus extends Enum {

	/** Publish is running. */
	public static EnumPublishStatus PROGRESS = new EnumPublishStatus("PROGRESS", "Publish in process");

	/** Publish was completed successfully. */
	public static EnumPublishStatus COMPLETE = new EnumPublishStatus("COMPLETE", "Publish completed");

	/** Publish was terminated with an error. */
	public static EnumPublishStatus FAILED = new EnumPublishStatus("FAILED", "Publish failed");

	private final String description;

	protected EnumPublishStatus(String name, String label) {
		super(name);
		this.description = label;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumPublishStatus getEnum(String name) {
		return (EnumPublishStatus) getEnum(EnumPublishStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumPublishStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumPublishStatus.class);
	}

}
