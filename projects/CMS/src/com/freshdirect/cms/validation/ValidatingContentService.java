package com.freshdirect.cms.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.MaskContentService;
import com.freshdirect.cms.application.service.ProxyContentService;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.node.ContentNodeUtil;

/**
 * {@link com.freshdirect.cms.application.service.ProxyContentService}that
 * overrides {@link #handle(CmsRequestI)}and runs the request thru validators
 * before calling the wrapped service. If validation fails, a
 * {@link com.freshdirect.cms.validation.ContentValidationException}is thrown
 * and the request is aborted.
 * <p>
 * Uses a {@link com.freshdirect.cms.application.service.MaskContentService}to
 * apply the changes in-memory before validation.
 */
public class ValidatingContentService extends ProxyContentService {

	private final List validators;

	public ValidatingContentService(ContentServiceI service, List validators) {
		super(service);
		this.validators = validators;
	}

	/**
	 * @throws ContentValidationException
	 */
	public CmsResponseI handle(CmsRequestI request) {

		MaskContentService masked = new MaskContentService(getProxiedService(),
				new SimpleContentService(getTypeService()));
		masked.handle(request);

		Set keys = getKeys(request.getNodes());
		Collection originalNodes = getProxiedService().getContentNodes(keys)
				.values();
		keys.addAll(getChildKeys(request.getNodes()));
		keys.addAll(getChildKeys(originalNodes));

		Collection nodes = masked.getContentNodes(keys).values();

		ContentValidationDelegate delegate = new ContentValidationDelegate();
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			for (Iterator v = validators.iterator(); v.hasNext();) {
				ContentValidatorI validator = (ContentValidatorI) v.next();
				validator.validate(delegate, masked, node, request);
			}
		}

		if (!delegate.isEmpty()) {
			throw new ContentValidationException(delegate);
		}

		return super.handle(request);
	}

	private Set getKeys(Collection nodes) {
		Set s = new HashSet(nodes.size());
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			s.add(node.getKey());
		}
		return s;
	}

	private Set getChildKeys(Collection nodes) {
		Set s = new HashSet();
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			Set childKeys = ContentNodeUtil.getChildKeys(node);
			s.addAll(childKeys);
		}
		return s;
	}

}