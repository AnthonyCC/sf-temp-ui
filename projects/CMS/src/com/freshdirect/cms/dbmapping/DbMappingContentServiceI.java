package com.freshdirect.cms.dbmapping;

import javax.sql.DataSource;

import com.freshdirect.cms.application.ContentServiceI;

/**
 * Service interface for {@link com.freshdirect.cms.dbmapping.DbMappingContentService}.
 */
public interface DbMappingContentServiceI extends ContentServiceI {

	/**
	 * @return JDBC data source used by the service, never null
	 */
	public DataSource getDataSource();

}
