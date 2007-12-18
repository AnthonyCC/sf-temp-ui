package com.freshdirect.cms.ui.tapestry.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ExternalServiceParameter;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.Form;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeComparator;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.ui.tapestry.CmsVisit;
import com.freshdirect.cms.ui.tapestry.page.CreateContentPopup;

public abstract class RelationshipEditor extends BaseComponent implements PageDetachListener {

	/** Map of String (relationship.name) -> Set of Integer (index) */
	private Map selectedIndexes = new HashMap();

	private RelationshipI highlightRelationship = null;
	private int highlightIndex = -1;

	public void sort(IRequestCycle cycle) {
		List l = getListValue();
		Collections.sort(l, new Comparator() {
			public int compare(Object o1, Object o2) {
				ContentNodeI node1 = ((ContentKey) o1).lookupContentNode();
				ContentNodeI node2 = ((ContentKey) o2).lookupContentNode();
				return ContentNodeComparator.DEFAULT.compare(node1, node2);
			}
		});
	}

	public void clear(IRequestCycle cycle) {
		getRelationship().setValue(null);
	}

	public void delete(IRequestCycle cycle) {
		List selIndexes = getCurrSelectedIndexes();
		List relKeys = getListValue();
		int idx = 0;
		for (ListIterator i = relKeys.listIterator(); i.hasNext(); idx++) {
			i.next();
			if (selIndexes.contains(new Integer(idx))) {
				i.remove();
			}
		}
		selIndexes.clear();
	}

	public void move(IRequestCycle cycle) {
		link(cycle, true);
	}

	public void copy(IRequestCycle cycle) {
		link(cycle, false);
	}

	private void link(IRequestCycle cycle, boolean move) {
		IValidationDelegate delegate = Form.get(cycle).getDelegate();
		if (delegate.getHasErrors()) {
			return;
		}

		CmsVisit visit = (CmsVisit) getPage().getVisit();

		ContentKey sourceKey = getRelationship().getContentNode().getKey();
		ContentKey targetKey = getSelectedTarget();

		// FIXME this assumes that the current node is the root of the working set
		ContentNodeI targetNode = visit.getWorkingSet(sourceKey).getContentNode(targetKey);
		if (targetNode == null) {
			// TODO err msg
			return;
		}

		List selIndexes = getCurrSelectedIndexes();
		List relKeys = getListValue();
		int idx = 0;
		for (ListIterator i = relKeys.listIterator(); i.hasNext(); idx++) {
			ContentKey relKey = (ContentKey) i.next();
			if (selIndexes.contains(new Integer(idx))) {
				boolean added = addNavigable(targetNode, relKey);
				if (added && move) {
					i.remove();
				}
			}
		}
		selIndexes.clear();
	}

	private boolean addNavigable(ContentNodeI targetNode, ContentKey key) {
		ContentType type = key.getType();
		Collection navRels = ContentTypeUtil.getNavigableRelationshipDefs(targetNode.getDefinition());
		for (Iterator i = navRels.iterator(); i.hasNext();) {
			RelationshipDefI relDef = (RelationshipDefI) i.next();
			if (relDef.getContentTypes().contains(type)) {

				if (!EnumCardinality.MANY.equals(relDef.getCardinality())) {
					return false;
				}

				RelationshipI rel = (RelationshipI) targetNode.getAttribute(relDef.getName());

				ContentNodeUtil.addRelationshipKey(rel, key);

				System.out.println("RelationshipEditor.addNavigable() Added " + key + " -> " + targetNode + "." + relDef.getName());

				return true;
			}
		}
		return false;
	}

	public void shiftUp(IRequestCycle cycle) {
		shiftSelected(-1);
	}

	public void shiftDown(IRequestCycle cycle) {
		shiftSelected(1);
	}

	private void shiftSelected(int delta) {
		List l = getListValue();
		Object o = l.remove(getSelectedIndex());
		int idx = getSelectedIndex() + delta;
		getListValue().add(idx, o);

		highlightIndex = idx;
		highlightRelationship = getRelationship();
	}

	public boolean isHighlighted() {
		return highlightRelationship == getRelationship() && highlightIndex == getCurrIndex();
	}

	private List getListValue() {
		return (List) getRelationship().getValue();
	}

	public void pageDetached(PageEvent event) {
		highlightRelationship = null;
		highlightIndex = -1;
		selectedIndexes = new HashMap();
	}

	public boolean isDisabled() {
		return isInherited() || getRelationship().getDefinition().isReadOnly();
	}

	public boolean isInherited() {
		return getRelationship().getValue() == null && getRelationship().getDefinition().isInheritable();
	}

	public boolean isShowCreate() {
		// don't allow recursive create
		if (getPage() instanceof CreateContentPopup) {
			return false;
		}

		RelationshipDefI def = (RelationshipDefI) getRelationship().getDefinition();
		return def.isNavigable();
	}

	public boolean isShowMove() {
		RelationshipDefI def = (RelationshipDefI) getRelationship().getDefinition();
		return !isDisabled()
			&& def.isNavigable()
			&& getRelationship().getValue() != null
			&& EnumCardinality.MANY.equals(def.getCardinality())
			&& !((List) getRelationshipValue()).isEmpty();
	}

	public boolean isShowCopy() {
		RelationshipDefI def = (RelationshipDefI) getRelationship().getDefinition();
		return def.isNavigable()
			&& getRelationship().getValue() != null
			&& EnumCardinality.MANY.equals(def.getCardinality())
			&& !((List) getRelationshipValue()).isEmpty();
	}

	public boolean isShowDelete() {
		RelationshipDefI def = (RelationshipDefI) getRelationship().getDefinition();
		return !isDisabled()
			&& getRelationship().getValue() != null
			&& EnumCardinality.MANY.equals(def.getCardinality())
			&& !((List) getRelationshipValue()).isEmpty();
	}

	public boolean isShowSort() {
		RelationshipDefI def = (RelationshipDefI) getRelationship().getDefinition();
		return !isDisabled()
			&& getRelationship().getValue() != null
			&& EnumCardinality.MANY.equals(def.getCardinality())
			&& ((List) getRelationshipValue()).size() > 1;
	}

	public Object getRelationshipValue() {
		Object o = getRelationship().getValue();
		if (isInherited() && getContextNode() != null) {
			AttributeI ctxAttr = getContextNode().getAttribute(getRelationship().getName());
			o = ctxAttr.getValue();
		}
		return o;
	}

	private List getCurrSelectedIndexes() {
		List m = (List) selectedIndexes.get(getRelationship().getName());
		if (m == null) {
			m = new ArrayList();
			selectedIndexes.put(getRelationship().getName(), m);
		}
		return m;
	}

	public boolean isCurrIndexSelected() {
		return getCurrSelectedIndexes().contains(new Integer(getCurrIndex()));
	}

	public void setCurrIndexSelected(boolean selected) {
		List m = getCurrSelectedIndexes();
		Integer idx = new Integer(getCurrIndex());
		if (selected) {
			m.add(idx);
		} else {
			m.remove(idx);
		}
	}

	public String getTargetPopupUrl(boolean move) {
		IRequestCycle cycle = getPage().getRequestCycle();
		IEngineService service = cycle.getEngine().getService(Tapestry.EXTERNAL_SERVICE);

		String title = move ? "Move items to..." : "Copy items to...";
		String id = (move ? "moveSubmit_" : "copySubmit_") + getRelationshipId();

		ExternalServiceParameter params = new ExternalServiceParameter("SelectTargetPopup", new Object[] { title, getRelationshipId(), id});
		return service.getLink(false, params).getURL();
	}

	public String getRelationshipId() {
		return getRelationship().getContentNode().getKey().getEncoded() + ":" + getRelationship().getName();
	}

	public abstract RelationshipI getRelationship();

	public abstract ContextualContentNodeI getContextNode();

	public abstract int getSelectedIndex();

	public abstract ContentKey getSelectedTarget();

	public abstract int getCurrIndex();

	//public abstract ContentKey getCurrKey();

}