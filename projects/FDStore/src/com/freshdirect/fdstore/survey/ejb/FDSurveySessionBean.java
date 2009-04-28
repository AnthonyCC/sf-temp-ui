package com.freshdirect.fdstore.survey.ejb;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.EnumSurveyType;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyConstants;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;





public class FDSurveySessionBean extends SessionBeanSupport {

	private static final Category LOGGER = LoggerFactory
			.getInstance(FDSurveySessionBean.class);
	
	private final static ServiceLocator LOCATOR = new ServiceLocator();

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


	
	public FDSurvey getSurvey(String surveyName) {
		Connection conn = null;
		try {
			conn = getConnection();
			return FDSurveyDAO.loadSurvey(conn, surveyName);
		} catch (SQLException e) {
			LOGGER.warn("SQLException while loading survey : "+surveyName, e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while loading survey : "+surveyName, exp);
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup of getSurvey()", e);
			}
		}
	}

	public FDSurveyResponse getCustomerProfile(FDIdentity identity) {
		Connection conn = null;
		try {
		
			conn = getConnection();
			List surveys= FDSurveyDAO.getCustomerProfileSurveys(conn, identity);
			if(surveys==null||surveys.size()==0 )
				return null;
			else if(surveys.size()==1) {
				return FDSurveyDAO.getResponse(conn, identity, EnumSurveyType.getEnum(surveys.get(0).toString()));
			} else {
				FDSurveyResponse surveyResponse=FDSurveyDAO.getResponse(conn, identity, EnumSurveyType.getEnum(surveys.get(0).toString()));
				FDSurveyResponse additionalResponse=null;
				for(int i=1;i<surveys.size();i++) {
					additionalResponse=FDSurveyDAO.getResponse(conn, identity, EnumSurveyType.getEnum(surveys.get(i).toString()));
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
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup of getCustomerProfile()", e);
			}
		}
	}

	public FDSurveyResponse getSurveyResponse(FDIdentity identity, String survey) {
		Connection conn = null;
		try {
			conn = getConnection();
			return FDSurveyDAO.getResponse(conn, identity, EnumSurveyType.getEnum(survey));
		} catch (SQLException e) {
			LOGGER.warn("SQLException while loading survey response: "+survey, e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while loading survey response: "+survey, exp);
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup of getSurveyResponse()", e);
			}
		}
	}
	   
	   
}
