package com.freshdirect.fdstore.survey;

import java.util.*;

import com.freshdirect.framework.util.*;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.survey.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 * Caching proxy for FDFactory.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDSurveyCachedFactory {

	private static Category LOGGER = LoggerFactory.getInstance( FDSurveyCachedFactory.class );

	
	
	private final static LazyTimedCache surveyDefCache =
		new LazyTimedCache(FDStoreProperties.getSurveyDefCacheSize(), FDStoreProperties.getRefreshSecsSurveyDef() * 1000);

	
	
	private final static Thread piRefreshThread = new LazyTimedCache.RefreshThread(surveyDefCache, "FDSurvey Refresh", 3*60*1000) {
		protected void refresh(List expiredKeys) {
			try {
				LOGGER.debug("FDSurvey Refresh reloading "+expiredKeys.size()+" survey definitions");
				Collection pis = FDSurveyFactory.getInstance().getSurveys( (String[])expiredKeys.toArray(new String[0]) );

				// cache these
				FDSurvey tempi;
				for (Iterator i=pis.iterator(); i.hasNext(); ) {
					tempi = (FDSurvey)i.next();
					this.cache.put(tempi.getName(), tempi);
				}

			} catch (Exception ex) {
				LOGGER.warn("Error occured in FDSurvey Refresh", ex);
			}
		}
	};

	
	static {
		piRefreshThread.start();
		
	}

	

	/**
	 * Get current survey information object for surveyId.
	 *
	 * @param surveyId surveyId
	 *
	 * @return FDSurvey object
	 * @throws FDResourceException 
 	 * 
	 */
	public static FDSurvey getSurvey(EnumSurveyType surveyType) throws FDResourceException {
		Object cached = surveyDefCache.get(surveyType.getName());
		FDSurvey survey;
		if (cached==null) {
			FDSurveyFactory _instance=FDSurveyFactory.getInstance();
			survey = _instance.getSurvey(surveyType.getName());
			if(survey!=null)
				surveyDefCache.put(surveyType.getName(), survey);
			
		}else {
			survey = (FDSurvey) cached;
		}
		return survey;
	}

	
	
}

