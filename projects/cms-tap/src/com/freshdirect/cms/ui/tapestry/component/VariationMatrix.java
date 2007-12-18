package com.freshdirect.cms.ui.tapestry.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ui.tapestry.CmsVisit;
import com.freshdirect.cms.ui.tapestry.ContentKeySelectionModel;
import com.freshdirect.cms.ui.tapestry.WorkingSet;

public abstract class VariationMatrix extends BaseComponent {

	private final static String ATTR_MATRIX = "VARIATION_MATRIX";
	private final static String ATTR_DOMAIN_VALUES = "domainValues";

	/**
	 * @return List of Domain ContentKeys for the current product
	 */
	public List getDomainKeys() {
		List keys = (List) getAttribute().getContentNode().getAttribute(ATTR_MATRIX).getValue();
		return keys == null ? Collections.EMPTY_LIST : keys;
	}

	public void setCurrValue(ContentKey value) {
		Set domainValues = new HashSet(getCurrDomainValues());
		List currValues = (List) getCurrNode().getAttribute(ATTR_MATRIX).getValue();
		if (currValues == null) {
			currValues = new ArrayList();
			getCurrNode().getAttribute(ATTR_MATRIX).setValue(currValues);
		}
		for (ListIterator i = currValues.listIterator(); i.hasNext();) {
			ContentKey key = (ContentKey) i.next();
			if (domainValues.contains(key)) {
				if (value == null) {
					i.remove();
				} else {
					i.set(value);
				}
				return;
			}
		}
		// wasn't set previously
		if (value != null) {
			currValues.add(value);
		}
	}

	/**
	 * @return DomainValue ContentKey for current sku in current domain
	 */
	public ContentKey getCurrValue() {
		List valueList = getCurrDomainValues();
		List currValues = (List) getCurrNode().getAttribute(ATTR_MATRIX).getValue();
		if (currValues == null || currValues.isEmpty()) {
			return null;
		}
		Set foundValues = new HashSet(valueList);
		foundValues.retainAll(currValues);
		return foundValues.isEmpty() ? null : (ContentKey) foundValues.iterator().next();
	}

	/**
	 * @return current Sku content node
	 */
	public ContentNodeI getCurrNode() {
		CmsVisit visit = (CmsVisit) getPage().getVisit();
		WorkingSet workingSet = visit.getWorkingSet(getAttribute().getContentNode().getKey());
		return workingSet.getContentNode(getCurrKey());
	}

	/**
	 * @return List DomainValue ContentKeys in the current Domain
	 */
	private List getCurrDomainValues() {
		List l = (List) getCurrDomain().getAttribute(ATTR_DOMAIN_VALUES).getValue();
		return l == null ? Collections.EMPTY_LIST : l;
	}

	public IPropertySelectionModel getCurrValueSelectionModel() {
		List currDomainValues = getCurrDomainValues();
		List l = new ArrayList(currDomainValues.size() + 1);
		l.add(null);
		l.addAll(currDomainValues);
		return new ContentKeySelectionModel((ContentKey[]) l.toArray(new ContentKey[l.size()]));
	}

	public ContentNodeI getCurrDomain() {
		ContentKey k = getCurrDomainKey();
		return k == null ? null : k.lookupContentNode();
	}

	public abstract ContentKey getCurrKey();

	public abstract AttributeI getAttribute();

	public abstract ContentKey getCurrDomainKey();

}