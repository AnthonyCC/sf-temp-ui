package com.freshdirect.webapp.taglib.test.fdstore;

import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.test.ContentBundle;
import com.freshdirect.fdstore.content.test.ContentNodeSampler;
import com.freshdirect.fdstore.content.test.ContentNodeSamplerImpl;
import com.freshdirect.framework.util.UniqueRandomSequence;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.TruePredicate;
import org.apache.log4j.Category;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.Random;


public class ContentBundleGetterTag extends AbstractGetterTag implements SessionName {
	private static Category LOGGER = LoggerFactory.getInstance(ContentBundleGetterTag.class);
	
	private Predicate predicate = TruePredicate.getInstance();
	
	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}
	
	private List columnExtractors = Collections.EMPTY_LIST;
	
	public void setColumnExtractors(List columnExtractors) {
		this.columnExtractors = columnExtractors;
	}
	
	private String distribution = "__none";
	
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	
	private int sampleSize = 1000;
	
	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
	
	private long seed = -1;
	
	public void setSeed(long seed) {
		this.seed = seed;
	}
	
	private ContentType contentType;
	
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	private int maxContent = -1;
	
	public void setMaxContent(int maxContent) {
		this.maxContent = maxContent;
	}

	private static final long serialVersionUID = -8519796488570296360L;
	
	protected static DistributionHelper distroHelper = DistributionHelper.getInstance(); 
	

	protected List selectRandomContentKeySample(List content, Random R) {
		if (maxContent == -1 || maxContent > content.size()) return content;
		
		UniqueRandomSequence seq = UniqueRandomSequence.getInstance(maxContent, content.size(),R);
		
		List newContent = new ArrayList(maxContent);
		
		for(int i = 0; i< maxContent; ++i) newContent.add(content.get(seq.next()));
	
		return newContent;
	}
	
	protected String getQueryParam() { 	
		if (FDContentTypes.PRODUCT.equals(contentType)) return "productId";
		else if (FDContentTypes.CATEGORY.equals(contentType)) return "catId";
		else if (FDContentTypes.DEPARTMENT.equals(contentType)) return "deptId";
		else if (FDContentTypes.RECIPE.equals(contentType)) return "recipeId";
		else throw new IllegalStateException("Invalid content type");
	}
	
	protected boolean checkExtension(String ext) {
		if (".url".equals(ext)) return true;
		else if (FDContentTypes.PRODUCT.equals(contentType)) return ".sku".equals(ext) || ".pid".equals(ext);
		else if (FDContentTypes.RECIPE.equals(contentType)) return ".rid".equals(ext);
		else if (FDContentTypes.DEPARTMENT.equals(contentType)) return ".did".equals(ext);
		else if (FDContentTypes.CATEGORY.equals(contentType)) return ".cid".equals(ext);
		else return false;
	}
	
	protected void configureSampler(ContentNodeSampler sampler) throws IOException {
		
		if (!checkExtension(distribution.substring(distribution.lastIndexOf('.')))) 
			throw new IllegalArgumentException("File " + distribution + " not recognized for " + contentType);
		
		InputStream is = null; 

		Map frequencies = new HashMap();
		
		try {
			if (distribution.endsWith(".sku") && FDContentTypes.PRODUCT.equals(contentType)) {
				is = DistributionHelper.openDistribution(distribution);
				frequencies = distroHelper.getProductDistributionFromSKUs(is);
			} else if (distribution.endsWith(".url")) {
				is = DistributionHelper.openDistribution(distribution);
				frequencies = distroHelper.getContentDistributionFromQueryStrings(is, getQueryParam(), contentType, true);
			} else if (distribution.endsWith("id")) {
				is = DistributionHelper.openDistribution(distribution);
				frequencies = distroHelper.getContentDistributionFromCSVStream(is, contentType, true);
			} 
		} catch (IOException e) {
			LOGGER.warn("Could not sample distribution " + distribution, e);
			throw e;
		} finally {
			if (is != null) is.close();
		}
		
		
		sampler.setFrequenciesFromMap(frequencies);
		
	}
	
	protected Object getResult() throws IOException {
		
		ContentBundle bundle = null;
				
		Random R = new Random(seed == -1 ? System.currentTimeMillis() : seed);
		
		List matchingContent = ContentBundle.getMatchingContent(contentType,predicate);
		
		if (!"__none".equals(distribution)) {
			
			ContentNodeSampler sampler = new ContentNodeSamplerImpl(contentType,matchingContent);
			
			configureSampler(sampler);
			
			bundle = new ContentBundle(
				contentType,
				selectRandomContentKeySample(sampler.getSampleContentKeys(sampleSize,R),R));
			
		} else { // no distribution
			bundle = new ContentBundle(contentType,selectRandomContentKeySample(matchingContent,R));
		}
		
		return bundle;
		/*
		List caches = bundle.getCaches(columnExtractors);
		int total = bundle.countElems(caches);    
		return bundle.cacheIterator(caches, columnExtractors);
		*/
	}

	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.fdstore.content.test.ContentBundle";
		}
	}
}
