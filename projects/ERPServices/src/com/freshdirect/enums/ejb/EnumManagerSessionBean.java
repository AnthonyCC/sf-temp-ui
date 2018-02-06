package com.freshdirect.enums.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.enums.EnumDAOI;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *@deprecated Please use the EnumManagerController and EnumManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public class EnumManagerSessionBean extends SessionBeanSupport {

	private final static Category LOGGER = LoggerFactory.getInstance(EnumManagerSessionBean.class);

	public List loadEnum(String daoClassName) {

		EnumDAOI dao;
		try {
			dao = (EnumDAOI) Class.forName(daoClassName).newInstance();
		} catch (Exception e) {
			throw new EJBException(e);
		}

		Connection conn = null;
		try {
			conn = this.getConnection();
			return dao.loadAll(conn);
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new EJBException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignore) {
					LOGGER.warn(ignore);
				}
			}
		}
	}

}
