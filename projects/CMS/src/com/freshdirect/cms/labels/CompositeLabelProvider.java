package com.freshdirect.cms.labels;

import java.util.List;

import com.freshdirect.cms.ContentNodeI;

/**
 * Chains multiple label providers and returns the first available value.
 * If no label was provided, it returns the node ID as label.
 */
public class CompositeLabelProvider implements ILabelProvider {

	private final ILabelProvider[] labelProviders;

	public CompositeLabelProvider(List labelProviders) {
		this((ILabelProvider[]) labelProviders.toArray(new ILabelProvider[labelProviders.size()]));
	}

	public CompositeLabelProvider(ILabelProvider[] labelProviders) {
		this.labelProviders = labelProviders;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.labels.ILabelProvider#getLabel(com.freshdirect.cms.ContentNodeI)
	 */
	public String getLabel(ContentNodeI node) {
		for (int i = 0; i < labelProviders.length; i++) {
			String l = labelProviders[i].getLabel(node);
			if (l != null) {
				return l;
			}
		}
		return node.getKey().getId();
	}

}
