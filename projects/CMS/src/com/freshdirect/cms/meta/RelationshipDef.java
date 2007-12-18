/*
 * Created on Oct 28, 2004
 */
package com.freshdirect.cms.meta;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;

/**
 * Trivial implementation of {@link com.freshdirect.cms.RelationshipDefI}.
 */
public class RelationshipDef extends AttributeDef implements RelationshipDefI {

	private final boolean navigable;
	private final Set contentTypes = new HashSet();

	public RelationshipDef(
		String name,
		String label,
		boolean required,
		boolean inheritable,
		boolean navigable,
		boolean readOnly, 
		EnumCardinality cardinality) {
		super(EnumAttributeType.RELATIONSHIP, name, label, required, inheritable, readOnly, cardinality);
		this.navigable = navigable;
		if (navigable && inheritable) throw new IllegalArgumentException("Relationship "+ name + " cannot be both navigable and inheritable");
	}

	public boolean isNavigable() {
		return navigable;
	}

	public Set getContentTypes() {
		return this.contentTypes;
	}

	public void addContentType(ContentType cTypeRef) {
		contentTypes.add(cTypeRef);
	}

	public void removeContentType(ContentType cTypeRef) {
		contentTypes.remove(cTypeRef);
	}

}