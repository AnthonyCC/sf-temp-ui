/*
 * Created on Mar 21, 2005
 */
package com.freshdirect.cms.ui.tapestry.page;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.conf.FDRegistry;

/**
 * @author vszathmary
 */
public class Admin extends BasePage {

	public void rebuildIndex(IRequestCycle cycle) {

		ContentSearchServiceI searchService = (ContentSearchServiceI) FDRegistry.getInstance().getService(
			ContentSearchServiceI.class);

		rebuildIndex(searchService);
	}

	private void rebuildIndex(ContentSearchServiceI searchService) {
		Set keys = new HashSet();
		CmsManager instance = CmsManager.getInstance();
		for (Iterator i = searchService.getIndexedTypes().iterator(); i.hasNext();) {
			ContentType type = (ContentType) i.next();
			keys.addAll(instance.getContentKeysByType(type));
		}
		
		Map nodes = instance.getContentNodes(keys);

		searchService.setDictionary(SynonymDictionary.createFromCms());
		
		searchService.index(nodes.values());
		searchService.optimize();
		ContentSearch.getInstance().refreshRelevencyScores();
	}

	public void validateEditors(IRequestCycle cycle) {
		Set editorKeys = CmsManager.getInstance().getContentKeysByType(ContentType.get("CmsEditor"));
		Map nodes = CmsManager.getInstance().getContentNodes(editorKeys);
		ContentValidationDelegate delegate = new ContentValidationDelegate();
		ContentValidatorI validator = new CmsFormValidator();
		for (Iterator i = nodes.values().iterator(); i.hasNext();) {
			ContentNodeI e = (ContentNodeI) i.next();
			validator.validate(delegate, CmsManager.getInstance(), e, null);
		}
		System.err.println(delegate);
	}

	private static class CmsFormValidator implements ContentValidatorI {

		public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request) {
			ContentType type = ContentType.get(node.getAttribute("contentType").getValue().toString());
			ContentTypeDefI def = service.getTypeService().getContentTypeDefinition(type);

			Set editorFields = collectFieldNames(delegate, service, node);
			Set attributes = def.getAttributeNames();

			Set extra = new HashSet(editorFields);
			extra.removeAll(attributes);
			if (!extra.isEmpty()) {
				delegate.record(node.getKey(), "Extraneous fields: " + extra);
			}

			Set missing = new HashSet(attributes);
			missing.removeAll(editorFields);
			if (!missing.isEmpty()) {
				delegate.record(node.getKey(), "Missing fields: " + missing);
			}
		}

		private Set collectFieldNames(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI editor) {
			Set names = new HashSet();

			Set pageKeys = ContentNodeUtil.getChildKeys(editor);

			for (Iterator i = service.getContentNodes(pageKeys).values().iterator(); i.hasNext();) {
				ContentNodeI page = (ContentNodeI) i.next();

				Set sectionKeys = ContentNodeUtil.getChildKeys(page);

				for (Iterator j = service.getContentNodes(sectionKeys).values().iterator(); j.hasNext();) {
					ContentNodeI section = (ContentNodeI) j.next();

					Set fieldKeys = ContentNodeUtil.getChildKeys(section);
					for (Iterator k = service.getContentNodes(fieldKeys).values().iterator(); k.hasNext();) {
						ContentNodeI field = (ContentNodeI) k.next();

						String fieldName = (String) field.getAttribute("attribute").getValue();
						if (!names.add(fieldName)) {
							delegate.record(editor.getKey(), "Duplicate field definition: " + fieldName);
						}
					}

				}
			}

			return names;
		}

	}

}