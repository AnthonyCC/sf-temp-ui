package com.freshdirect.fdstore.semPixel;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.fdstore.FDAbstractCache;
import com.freshdirect.fdstore.FDStoreProperties;

public class FDSemPixelCache extends FDAbstractCache {
	private static Category LOGGER = LoggerFactory.getInstance( FDSemPixelCache.class );
	private static FDSemPixelCache instance;
	
	private FDSemPixelCache() {
		super(DateUtil.MINUTE * FDStoreProperties.getSemPixelRefreshPeriod());
	}

	public synchronized static FDSemPixelCache getInstance(){
		if(instance == null){
			instance = new FDSemPixelCache();
		}
		return instance;
	}
	
	protected Map<String, SemPixelModel> loadData(Date since){
		LOGGER.info("REFRESHING SemPixel Cache");
		Map<String, SemPixelModel> data = new HashMap<String, SemPixelModel>(); 
		//load pixel models
		String semConfigs = FDStoreProperties.getSemConfigs();
		String[] sem_step1 = new String[0];
		String[] sem_step2 = new String[0];
		
		SemPixelModel curSemPixel = null;
		
		sem_step1 = semConfigs.split(";");
		for (int n = 0; n < sem_step1.length; n++) {
			curSemPixel = new SemPixelModel();
			
			sem_step2 = sem_step1[n].split(":");
			
			for (int o = 0; o < sem_step2.length; o++) {
				StringTokenizer sem_st = new StringTokenizer(sem_step2[o], "=");
				while(sem_st.hasMoreTokens()) {

					String key = sem_st.nextToken();
					String value = sem_st.nextToken();
					
					curSemPixel.setParam(key, value);
					
					if ("enabled".equalsIgnoreCase(key)) {
						curSemPixel.setEnabled(Boolean.parseBoolean(value));
					}
					if ("name".equalsIgnoreCase(key)) {
						curSemPixel.setName(value);
					}
					if ("refs".equals(key)) {
						String[] sem_curRefs = value.split(",");
						for (int i = 0; i < sem_curRefs.length; i++) {
							curSemPixel.addValidReferer(sem_curRefs[i]);
						}
					}
					if ("zips".equals(key)) {
						String[] sem_curZips = value.split(",");
						for (int i = 0; i < sem_curZips.length; i++) {
							curSemPixel.addValidZipCode(sem_curZips[i]);
						}
					}
					if ("media".equals(key)) {
						curSemPixel.setMediaPath(value);
					}
				}
			}

			//add pixel only if a name exists for it
			String pName = curSemPixel.getName();
			if (pName != null) {
				data.put(pName, curSemPixel);
			}
		}
		
	
		LOGGER.info("REFRESHED SemPixel Cache: " + data.size());
		return data;
	}
	
	protected Date getModifiedDate(Object item) {
		if(!(item instanceof SemPixelModel)){
			return null;
		}
		return ((SemPixelModel)item).getLastModifiedDate();
	}
	
	public SemPixelModel getSemPixel(String name) {
		SemPixelModel model = (SemPixelModel) this.getCachedItem(name);
		if(model == null){
			model = new SemPixelModel();
			model.setName(name);
		}
		return model;
	}
	
	public Map getCachedSemPixels() {
		return this.getCache();
	}
	
	public void addSemPixelToCache(String pixelKey, SemPixelModel pixel) {
		if ( pixel == null || "".equals(pixelKey.trim()) ) { return; }
		if ( pixelKey == null || "".equals(pixelKey.trim()) ) { return; }
		pixel.setName(pixelKey);
		pixel.setParam("enabled", Boolean.toString(pixel.isEnabled()));
		pixel.setParam("zips", pixel.getValidZipCodes().toString());
		pixel.setParam("refs", pixel.getValidReferers().toString());
		pixel.setParam("name", pixel.getName());
		pixel.setParam("media", pixel.getMediaPath());
		this.getCache().put(pixelKey, pixel);
	}
	
	public void removeSemPixelFromCache(String pixelKey) {
		this.getCache().remove(pixelKey);
	}

}