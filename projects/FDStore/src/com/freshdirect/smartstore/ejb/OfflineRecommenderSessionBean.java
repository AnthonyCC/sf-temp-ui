package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.fdstore.util.ProductDisplayUtil;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;

public class OfflineRecommenderSessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = 8606750945494173083L;

	private static final Logger LOGGER = LoggerFactory
			.getInstance(OfflineRecommenderSessionBean.class);

	private static FDCustomerManagerHome customerManagerHome = null;

	private static void lookupCustomerManagerHome() throws FDResourceException {
		if (customerManagerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			customerManagerHome = (FDCustomerManagerHome) ctx
					.lookup(FDStoreProperties.getFDCustomerManagerHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}

	private static void invalidateCustomerManagerHome() {
		customerManagerHome = null;
	}

	private FDUserI getUserByEmail(String email) throws FDResourceException {
		lookupCustomerManagerHome();
		try {
			FDCustomerManagerSB sb = customerManagerHome.create();
			FDUser user = sb.recognizeByEmail(email);
			return user;
		} catch (CreateException ce) {
			invalidateCustomerManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateCustomerManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		} catch (FDAuthenticationException ae) {
			invalidateCustomerManagerHome();
			throw new FDResourceException(ae, "Unrecognized user");
		}
	}

	private SessionInput createSessionInput(FDUserI user,
			ContentNodeModel currentNode) {
		SessionInput input = new SessionInput(user);
		if (currentNode != null) {
			input.setCurrentNode(currentNode);
			if (currentNode instanceof YmalSource)
				input.setYmalSource((YmalSource) currentNode);
		}
		input.setNoShuffle(true);
		input.setIncludeCartItems(true);
		return input;
	}

	private static final String DELETE_OFFLINE_RECOMMENDATION = "DELETE FROM CUST.SS_OFFLINE_RECOMMENDATION"
			+ " WHERE USER_ID = ? AND SITE_FEATURE_ID = ?";
	private static final String INSERT_OFFLINE_RECOMMENDATION = "INSERT INTO CUST.SS_OFFLINE_RECOMMENDATION"
			+ " (LAST_MODIFIED, USER_ID, SITE_FEATURE_ID, PRODUCT_ID, POSITION, NAME, LINK, IMAGE_PATH, IMAGE_WIDTH, IMAGE_HEIGHT, RATING, PRICE, WAS_PRICE, TIERED_PRICE, ABOUT_PRICE, BURST)"
			+ " VALUES (SYSDATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private int saveRecommendations(FDUserI user, EnumSiteFeature siteFeature,
			List<ProductModel> products) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement ps = conn
					.prepareStatement(DELETE_OFFLINE_RECOMMENDATION);
			ps.setString(1, user.getUserId());
			ps.setString(2, siteFeature.getName());
			ps.executeUpdate();
			ps.close();
			ps = null;

			int rowCount = 0;
			int n = Math.min(5, products.size());

			for (int i = 0; i < n; i++) {
				ps = conn.prepareStatement(INSERT_OFFLINE_RECOMMENDATION);
				ps.setString(1, user.getUserId());
				ps.setString(2, siteFeature.getName());
				ProductModel product = products.get(i);
				ps.setString(3, product.getContentName());
				ps.setInt(4, i + 1);

				// name
				String fullName = product.getFullName();
				ps.setString(5, fullName != null
						&& !"".equalsIgnoreCase(fullName) ? fullName
						: "(this product)");

				// link
				ps.setString(6, ProductDisplayUtil.getProductURI(product));

				// image
				Image productImage = product.getProdImage();
				ps.setString(7, productImage.getPath());
				ps.setInt(8, productImage.getWidth());
				ps.setInt(9, productImage.getHeight());

				// rating
				try {
					String rating = ProductDisplayUtil.getProductRatingCode(
							user, product);
					if (rating == null)
						ps.setNull(10, Types.VARCHAR);
					else
						ps.setString(10, rating);
				} catch (FDResourceException e) {
					ps.setNull(10, Types.VARCHAR);
				} catch (FDSkuNotFoundException e) {
					ps.setNull(10, Types.VARCHAR);
				}

				// price, pricing
				ps.setString(11, product.getPriceFormatted(0));
				ps.setString(12, product.getWasPriceFormatted(0));
				ps.setString(13, product.getTieredPrice(0));
				ps.setString(14, product.getAboutPriceFormatted(0));

				// burst
				String burst = ProductDisplayUtil.getProductBurstCode(user,
						siteFeature, product);
				if (burst == null)
					ps.setNull(15, Types.VARCHAR);
				else
					ps.setString(15, burst);
				rowCount += ps.executeUpdate();
				ps.close();
				ps = null;
			}

			return rowCount;
		} catch (SQLException e) {
			LOGGER.error("saving recommendations failed; exception=" + e);
			try {
				LOGGER.error("Connection URL: " + conn.getMetaData().getURL()
						+ "/ User: " + conn.getMetaData().getUserName());
			} catch (SQLException e1) {
			}
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw new EJBException(e);
			}
		}
	}

	public List<ProductModel> recommend(EnumSiteFeature siteFeature,
			String customerEmail, ContentNodeModel currentNode)
			throws RemoteException, FDResourceException {
		FDUserI user = getUserByEmail(customerEmail);
		SessionInput input = createSessionInput(user, currentNode);
		input.setMaxRecommendations(5);
		Recommendations recs = FDStoreRecommender.getInstance()
				.getRecommendations(siteFeature, user, input, null);
		saveRecommendations(user, siteFeature, recs.getProducts());
		return recs.getProducts();
	}
}
