package com.freshdirect.fdstore.survey.ejb;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyConstants;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.fdstore.survey.SurveyKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;





public class FDSurveySessionBean extends SessionBeanSupport {

	private static final Logger LOGGER = LoggerFactory
			.getInstance(FDSurveySessionBean.class);
	
	public FDSurveySessionBean() {
		super();
	}

	/**
	 * Template method that returns the cache key to use for caching resources.
	 * 
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.fdstore.survey.ejb.FDSurveyHome";
	}


	
	public FDSurvey getSurvey(SurveyKey key) {
		Connection conn = null;
		try {
			conn = getConnection();
			return FDSurveyDAO.loadSurvey(conn, key);
		} catch (SQLException e) {
			LOGGER.warn("SQLException while loading survey : "+key, e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while loading survey : "+key, exp);
			throw new EJBException(exp);
		} finally {
		    close(conn);
		}
	}

	public FDSurveyResponse getCustomerProfile(FDIdentity identity, EnumServiceType serviceType) {
		Connection conn = null;
		try {
		
			conn = getConnection();
			List<String> surveys= FDSurveyDAO.getCustomerProfileSurveys(conn, identity, serviceType);
			if(surveys==null||surveys.size()==0 )
				return null;
			else if(surveys.size()==1) {
				return FDSurveyDAO.getResponse(conn, identity, new SurveyKey(surveys.get(0), serviceType));
			} else {
				FDSurveyResponse surveyResponse=FDSurveyDAO.getResponse(conn, identity, new SurveyKey(surveys.get(0), serviceType));
				FDSurveyResponse additionalResponse=null;
				for(int i=1;i<surveys.size();i++) {
					additionalResponse=FDSurveyDAO.getResponse(conn, identity, new SurveyKey(surveys.get(i), serviceType));
					if(additionalResponse!=null) {
						if(surveyResponse==null) {
							surveyResponse=additionalResponse;
						}
						Map additionalAnswers=additionalResponse.getAnswers();
						Iterator it=additionalAnswers.keySet().iterator();
						String key="";
						while(it.hasNext()) {
							key=it.next().toString();
							if(surveyResponse.getAnswer(key)==null) {
								surveyResponse.addAnswer(key, additionalResponse.getAnswer(key));
							}
						}
					}
				}
				return surveyResponse;
			}
		} catch (SQLException e) {
			LOGGER.warn("SQLException while loading survey response : "+FDSurveyConstants.CUSTOMER_PROFILE_SURVEY, e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while loading survey response : "+FDSurveyConstants.CUSTOMER_PROFILE_SURVEY, exp);
			throw new EJBException(exp);
		} finally {
                    close(conn);
		}
	}

	public FDSurveyResponse getSurveyResponse(FDIdentity identity, SurveyKey key) {
		Connection conn = null;
		try {
			conn = getConnection();
			return FDSurveyDAO.getResponse(conn, identity, key);
		} catch (SQLException e) {
			LOGGER.warn("SQLException while loading survey response: "+key, e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while loading survey response: "+key, exp);
			throw new EJBException(exp);
		} finally {
                    close(conn);
		}
	}
	   
	   
}
