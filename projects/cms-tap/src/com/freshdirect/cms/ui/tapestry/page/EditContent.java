package com.freshdirect.cms.ui.tapestry.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.cms.ui.tapestry.CmsVisit;
import com.freshdirect.cms.ui.tapestry.ContextSelectionModel;
import com.freshdirect.cms.ui.tapestry.WorkingSet;
import com.freshdirect.cms.validation.ContentValidationException;
import com.freshdirect.cms.validation.ContentValidationMessage;
import com.freshdirect.framework.conf.FDRegistry;

public abstract class EditContent extends BasePage implements IExternalPage {

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
		ContentKey key = (ContentKey) parameters[0];
		setContentKey(key);
		getWorkingSet().clear();
		Context[] c = getAllContexts();
		setContext(c.length == 0 ? null : c[0]);
		setDirtyForm(false);
		setSuccessMsg(null);
		setChangeSet(null);
	}
	
	public void formSubmit(IRequestCycle cycle) {
		this.setDirtyForm(true);
	}

	public void saveContent(IRequestCycle cycle) {
		if (getDelegate().getHasErrors()) {
			return;
		}
		
		CmsUser user = ((CmsVisit) getVisit()).getUser();
		try {
			CmsResponseI response = getWorkingSet().flush(user);

			setSuccessMsg("Saved " + getContentKey().lookupContentNode().getLabel());
			if (response.getChangeSetId() != null) {
				ChangeLogServiceI chgService = (ChangeLogServiceI) FDRegistry.getInstance().getService(ChangeLogServiceI.class);
				ChangeSet changeSet = chgService.getChangeSet(response.getChangeSetId());
				setChangeSet(changeSet);
			}
			setContentKey(null);
			setDirtyForm(false);
			
		} catch (ContentValidationException ex) {
			IValidationDelegate delegate = getDelegate();
			delegate.setFormComponent(null);
			List validationMessages = ex.getDelegate().getValidationMessages();
			for (Iterator i = validationMessages.iterator(); i.hasNext();) {
				ContentValidationMessage msg = (ContentValidationMessage) i.next();
				delegate.record(msg.toString(), null);
			}
		}
	}

	public void cancelEdit(IRequestCycle cycle) {
		getWorkingSet().clear();
		setDirtyForm(false);
		// FIXME dirty warning sticks
		// TODO cancel messaging
	}
	
	public String getPreviewLink() {
		ContentKey key = getContentKey();
		return key == null ? null : PreviewLinkProvider.getLink(key);
	}

	private WorkingSet getWorkingSet() {
		return ((CmsVisit) getVisit()).getWorkingSet(getContentKey());
	}

	private IValidationDelegate getDelegate() {
		return (IValidationDelegate) getBeans().getBean("delegate");
	}

	public ContentNodeI getSelectedNode() {
		return ((CmsVisit) getVisit()).getWorkingSet(getContentKey()).getContentNode(getContentKey());
	}

	public IPropertySelectionModel getContextSelectionModel() {
		return new ContextSelectionModel(getAllContexts());
	}

	public ContextualContentNodeI getContextNode() {
		Context ctx = getContext();
		if (ctx == null) {
			return null;
		}
		return (ContextualContentNodeI) ContextService.getInstance().getContextualizedContentNode(ctx).getParentNode();
	}

	private Context[] allContexts;

	public Context[] getAllContexts() {
		if (allContexts == null && getContentKey() != null) {
			Collection c = ContextService.getInstance().getAllContextsOf(getContentKey());
			List l = new ArrayList(c.size());
			for (Iterator i = c.iterator(); i.hasNext();) {
				Context ctx = (Context) i.next();
				if (ctx.getParentContext() != null) {
					l.add(ctx);
				}
			}
			allContexts = (Context[]) l.toArray(new Context[l.size()]);
		}
		return allContexts;
	}

	protected void initialize() {
		allContexts = null;
	}

	public abstract void setDirtyForm(boolean dirtyForm);

	public abstract ContentKey getContentKey();

	public abstract void setContentKey(ContentKey key);

	public abstract Context getContext();

	public abstract void setContext(Context context);

	public abstract void setSuccessMsg(String msg);

	public abstract void setChangeSet(ChangeSet changeSet);

}