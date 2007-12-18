/*
 * Created on Mar 24, 2005
 */
package com.freshdirect.cms.ui.tapestry.component;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.tacos.ajax.components.tree.ITreeManager;
import net.sf.tacos.ajax.components.tree.TreeManager;
import net.sf.tacos.model.ITreeContentProvider;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

import com.freshdirect.cms.ContentNodeComparator;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.cms.ui.tapestry.CmsUIConfiguration;
import com.freshdirect.cms.ui.tapestry.tree.ContentKeyProvider;
import com.freshdirect.cms.ui.tapestry.tree.ContentTreeProvider;

/**
 * @author vszathmary
 */
public abstract class ContentTree extends BaseComponent implements PageDetachListener {

	private final static int MAX_HITS = 120;

	private ITreeContentProvider contentProvider;

	public ITreeContentProvider getContentProvider() {
		if (contentProvider == null) {
			contentProvider = createContentProvider();
		}
		return contentProvider;
	}

	private ITreeContentProvider createContentProvider() {
		if (!isSearchMode()) {
			return new ContentTreeProvider(CmsUIConfiguration.getInstance().getTreeRootKeys());
		}
		Collection hits = CmsManager.getInstance().search(getSearchQuery(), MAX_HITS);
		Set keys = new HashSet(hits.size());
		for (Iterator i = hits.iterator(); i.hasNext();) {
			SearchHit hit = (SearchHit) i.next();
			keys.add(hit.getContentKey());
		}
		return new ContentTreeProvider(keys);
	}

	public void pageDetached(PageEvent event) {
		contentProvider = null;
	}

	public Comparator getSorter() {
		if (!isSearchMode()) {
			return ContentNodeComparator.DEFAULT;
		}
		// FIXME unsorted -> score
		//return null;
		return ContentNodeComparator.DEFAULT;
	}

	public boolean isSearchMode() {
		return getSearchQuery() != null && !"".equals(getSearchQuery().trim());
	}

	public void clear(IRequestCycle cycle) {
		setSearchQuery(null);
	}

	public void search(IRequestCycle cycle) {
	}

	public void collapseAll(IRequestCycle cycle) {
		getTreeManager().collapseAll();
	}

	public ITreeManager getTreeManager() {
		return new TreeManager(getTreeState(), getContentProvider(), ContentKeyProvider.INSTANCE);
	}

	public abstract Set getTreeState();

	public abstract String getSearchQuery();

	public abstract void setSearchQuery(String searchQuery);

}