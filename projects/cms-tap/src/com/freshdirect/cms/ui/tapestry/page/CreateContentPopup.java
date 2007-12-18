/*
 * Created on Feb 9, 2005
 */
package com.freshdirect.cms.ui.tapestry.page;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Category;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.ui.tapestry.CmsVisit;
import com.freshdirect.cms.ui.tapestry.WorkingSet;
import com.freshdirect.cms.validation.ContentValidationException;
import com.freshdirect.cms.validation.ContentValidationMessage;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author vszathmary
 */
public abstract class CreateContentPopup extends BasePopupPage implements IExternalPage {

	private final static Pattern NAME_PATTERN = Pattern.compile("([a-zA-Z]|\\d|_|-)+");
	private final static Category LOGGER = LoggerFactory.getInstance(CreateContentPopup.class);
	
	public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
		setOpenerPageName((String) parameters[0]);
		setParentKey((ContentKey) parameters[1]);
		setAttributeName((String) parameters[2]);
		setContentType(ContentType.get((String) parameters[3]));
		
		String id = CmsManager.getInstance().getTypeService().generateUniqueId(getContentType());
		if (id != null) {
			setContentId(id);
			setContentKey(null);
		} else {
			setContentId(null);
			setContentKey(null);
		}
	}

	public Context getContext() {
		Collection allContexts = ContextService.getInstance().getAllContextsOf(getParentKey());
		if (allContexts.isEmpty()) {
			return null;
		}
		return (Context) allContexts.iterator().next();
	}

	public ContextualContentNodeI getContextNode() {
		Context ctx = getContext();
		if (ctx == null) {
			return null;
		}
		ContextualContentNodeI ctxNode = ContextService.getInstance().getContextualizedContentNode(ctx.getPath());
		return ctxNode;
	}

	public void performCreate(IRequestCycle cycle) {
		IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
		if (delegate.getHasErrors()) {
			return;
		}

		if(getContentId() == null) {
			delegate.record("Content ID is required", null);
			return;
		}
		
		Matcher matcher = NAME_PATTERN.matcher(getContentId());
		if(!matcher.matches()) {
			LOGGER.info("requested content id \'"+getContentId()+"\' does not match pattern "+NAME_PATTERN.pattern());
			delegate.record(" \""+getContentId() + "\" must contain only letters, numbers, underscore and '-'", null);
			return;
		}

		ContentKey key = new ContentKey(getContentType(), getContentId());

		ContentNodeI node = CmsManager.getInstance().getContentNode(key);
		if (node != null) {
			delegate.record(key.getEncoded() + " already exists", null);
			setDuplicateKey(key);
			return;
		}

		setContentKey(key);
		((CmsVisit) getVisit()).getWorkingSet(key).createContentNode(key);
	}

	public void performSave(IRequestCycle cycle) {
		IValidationDelegate delegate = getDelegate();
		if (delegate.getHasErrors()) {
			return;
		}
		
		CmsVisit visit = (CmsVisit) getVisit();
		WorkingSet parentSet = visit.getWorkingSet(getParentKey());
		WorkingSet currentSet = visit.getWorkingSet(getContentKey());
		try {
			currentSet.flush(visit.getUser());
		} catch (ContentValidationException ex) {
			delegate.setFormComponent(null);
			List validationMessages = ex.getDelegate().getValidationMessages();
			for (Iterator i = validationMessages.iterator(); i.hasNext();) {
				ContentValidationMessage msg = (ContentValidationMessage) i.next();
				delegate.record(msg.toString(), null);
			}
			return;
		}
				
		// establish parent->child relationship
		ContentNodeI parentNode = parentSet.getContentNode(getParentKey());
		RelationshipI rel = (RelationshipI) parentNode.getAttribute(getAttributeName());
		ContentNodeUtil.addRelationshipKey(rel, getContentKey());

		// parentSet.merge(currentSet);
		
		// save parent workset
		IPage page = cycle.getPage(getOpenerPageName());
		EditContent editor = (EditContent) page;
		editor.saveContent(cycle);
		
		setClosing(true);
	}

	private IValidationDelegate getDelegate() {
		return (IValidationDelegate) getBeans().getBean("delegate");
	}
	
	public abstract String getAttributeName();

	public abstract void setAttributeName(String attributeName);

	public abstract ContentType getContentType();

	public abstract void setContentType(ContentType contentType);

	public abstract String getContentId();

	public abstract void setContentId(String contentId);

	public abstract ContentKey getParentKey();

	public abstract void setParentKey(ContentKey key);

	public abstract void setContentKey(ContentKey key);

	public abstract ContentKey getContentKey();

	public abstract void setDuplicateKey(ContentKey duplicate);

}