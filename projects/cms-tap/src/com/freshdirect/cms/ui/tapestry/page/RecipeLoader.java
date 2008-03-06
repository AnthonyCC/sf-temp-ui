package com.freshdirect.cms.ui.tapestry.page;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.fdstore.recipes.RecipeBulkLoader;
import com.freshdirect.cms.ui.tapestry.CmsVisit;
import com.freshdirect.framework.util.StringUtil;

public abstract class RecipeLoader extends BasePage {

	public void processFile(IRequestCycle cycle) throws InvalidContentKeyException {
		IUploadFile file = getFile();

		if (getUploadType() == null) {
			getDelegate().setFormComponent(null);
			getDelegate().record("upload type not specified", null);
			return;
		}
		
		if (file.getFileName().length() <= 0 || getDelegate().getHasErrors()) {
			getDelegate().setFormComponent(null);
			getDelegate().record("no file uploaded", null);
			return;
		}
				
		String	fileName = StringUtil.parseFilename(file.getFileName(),true);

		try {
			// FIXME: forcing the encoding to ISO-8859-1 is a workaround!
			ContentKey k = load(new InputStreamReader(file.getStream(), "ISO-8859-1"),
									   fileName,
									   getUploadType().intValue());
			
			setLoadedKey(k);
		} catch (IOException e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	public IValidationDelegate getDelegate() {
		return (IValidationDelegate) getBeans().getBean("delegate");
	}
	
	public abstract IUploadFile getFile();

	public abstract Integer getUploadType();
	
	public abstract void setLoadedKey(ContentKey key);
	
	/**
	 * Process an input stream with the bulk loader, and insert the contents into a
	 * storage.
	 * 
	 * @param reader the reader that contains the contents to load.
	 * @param recipeId the id of the recipe to process.
	 * @param type specify what to process: a ConfiguredProductGroup
	 *        or a Recipe
	 * @throws InvalidContentKeyException 
	 * @see RecipeBulkLoader#CONFIGURED_PRODUCT_GROUP
	 * @see RecipeBulkLoader#RECIPE
	 */
	private ContentKey load(Reader reader,
			                String recipeId,
			                int    type) throws IOException, InvalidContentKeyException {
		
		List			 	list;
		RecipeBulkLoader 	loader = new RecipeBulkLoader(CmsManager.getInstance(),
				                                reader,
				                                recipeId,
				                                type);
		
		// parse the input
		list = loader.parse();
		
		// process the input
		list = loader.process();
		
		// create a new request with all nodes, and add it to the content service
		CmsVisit     visit   = (CmsVisit) getVisit();
		CmsRequest   request = new CmsRequest(visit.getUser());
		
		for (Iterator it = list.iterator(); it.hasNext();) {
			ContentNodeI node = (ContentNodeI) it.next();
			
			request.addNode(node);
		}
		
		CmsManager.getInstance().handle(request);
		
		return ((ContentNodeI) list.get(list.size()-1)).getKey();
	}
	

}
