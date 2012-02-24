package com.freshdirect.cms.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
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

	private final List<ContentValidatorI> validators;

	public ValidatingContentService(ContentServiceI service, List<ContentValidatorI> validators) {
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

		Set<ContentKey> keys = getKeys(request.getNodes());
		final Map<ContentKey, ContentNodeI> originalNodes = getProxiedService().getContentNodes(keys);
                keys.addAll(getChildKeys(request.getNodes()));
		keys.addAll(getChildKeys(originalNodes.values()));

		Collection<ContentNodeI> nodes = masked.getContentNodes(keys).values();

		ContentValidationDelegate delegate = new ContentValidationDelegate();
                for (ContentNodeI node : nodes) {
                    ContentNodeI oldNode = originalNodes.get(node.getKey());
                    for (ContentValidatorI validator : validators) {
                        validator.validate(delegate, masked, node, request, null);
                    }
		}

		if (!delegate.isEmpty()) {
			throw new ContentValidationException(delegate);
		}

		return super.handle(request);
	}

	private Set<ContentKey> getKeys(Collection<ContentNodeI> nodes) {
		Set<ContentKey> s = new HashSet<ContentKey>(nodes.size());
		for (ContentNodeI node : nodes) {
			s.add(node.getKey());
		}
		return s;
	}

	private Set<ContentKey> getChildKeys(Collection<ContentNodeI> nodes) {
		Set<ContentKey>  s = new HashSet<ContentKey>();
                for (ContentNodeI node : nodes) {
			Set<ContentKey> childKeys = ContentNodeUtil.getChildKeys(node);
			s.addAll(childKeys);
		}
		return s;
	}

}