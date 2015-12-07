package com.freshdirect.referral.extole;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPSessionBeanSupport;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;

public class FDExtoleManagerSessionBean extends ERPSessionBeanSupport {

	private static final long serialVersionUID = -1898026540138884474L;
	private static final Logger LOGGER = LoggerFactory
			.getInstance(FDExtoleManagerSessionBean.class);
	private static final String SUCCESS = "success";
	private static final String FAILURE = "BAD REQUEST";

	public List<ExtoleConversionRequest> getExtoleCreateConversionRequest()
			throws FDResourceException {
		Connection conn = null;
		List<ExtoleConversionRequest> conversionReq = null;
		try {
			conn = getConnection();
			conversionReq = FDExtoleManagerDAO
					.getExtoleCreateConversionTransactions(conn);
		} catch (SQLException e) {
			LOGGER.info("Exception in getExtoleCreateConversionRequest(): " + e);
			this.getSessionContext().setRollbackOnly();
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
		return conversionReq;

	}

	public List<ExtoleConversionRequest> getExtoleApproveConversionRequest()
			throws FDResourceException {
		Connection conn = null;
		List<ExtoleConversionRequest> approveReq = null;
		try {
			conn = getConnection();
			approveReq = FDExtoleManagerDAO
					.getExtoleApproveConversionTransactions(conn);
		} catch (SQLException e) {
			LOGGER.info("Exception in getExtoleApproveConversionRequest(): "+ e);
			this.getSessionContext().setRollbackOnly();
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
		return approveReq;

	}

	public void updateConversionRequest(ExtoleResponse convResponse)
			throws FDResourceException {
		Connection conn = null;

		try {
			conn = getConnection();
			FDExtoleManagerDAO.updateRafExtoleTransactions(conn, convResponse);

		} catch (SQLException e) {
			LOGGER.info("Exception in updateConversionRequest(): " + e);
			this.getSessionContext().setRollbackOnly();
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}

	}

	/**
	 * Save the parsed extole reward records in database
	 * 
	 * @param rewards
	 * @throws FDResourceException
	 */
	public void saveExtoleRewardsFile(List<FDRafCreditModel> rewards)
			throws FDResourceException {
		Connection conn = null;

		try {
			conn = getConnection();
			FDExtoleManagerDAO.saveExtoleRewards(conn, rewards);

		} catch (SQLException e) {
			LOGGER.info("Exception in saveExtoleRewardsFile(): " + e);
			this.getSessionContext().setRollbackOnly();
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}

	}

	public void createConversion() throws ExtoleServiceException, IOException,
			FDResourceException {
		ExtoleResponse response = null;

		// Get all the create conversion req
		List<ExtoleConversionRequest> conversionRequests = getExtoleCreateConversionRequest();

		// call the extole rest api for create conversion request
		ExtoleService extoleService = new ExtoleServiceImpl();
		for (ExtoleConversionRequest request : conversionRequests) {
			try {
				response = extoleService.createConversion(request);
				response.setRafTransId(request.getRafTransId());
				if (SUCCESS.equalsIgnoreCase(response.getStatus())) {
					response.setStatus(EnumRafTransactionStatus.SUCCESS
							.getValue());
				}
				if (FAILURE.equalsIgnoreCase(response.getCode())) {
					response.setStatus(EnumRafTransactionStatus.FAILURE
							.getValue());
				}
				/*
				 * if(response.getStatus().equalsIgnoreCase("success")){
				 * response
				 * .setStatus(EnumRafTransactionStatus.SUCCESS.getValue()); }
				 * else if(response.getStatus().equalsIgnoreCase("failure")){
				 * response
				 * .setStatus(EnumRafTransactionStatus.FAILURE.getValue()); }
				 */

				updateConversionRequest(response);
			} catch (Exception e) {
				LOGGER.error("Exception in create conversion request for clickId :"
						+ request.getClickId()
						+ "Error message : "
						+ e.getMessage());
				throw new ExtoleServiceException(
						"Exception in create conversion request for clickId :"
								+ request.getClickId() + "Error message : "
								+ e.getMessage(), e);

			}
		}

	}

	public void approveConversion() throws ExtoleServiceException, IOException,
			FDResourceException {
		ExtoleResponse response = null;

		// Get all the approve conversion requests
		List<ExtoleConversionRequest> approveRequests = getExtoleApproveConversionRequest();

		ExtoleService extoleService = new ExtoleServiceImpl();
		for (ExtoleConversionRequest request : approveRequests) {
			try {
				response = extoleService.approveConversion(request);
				response.setRafTransId(request.getRafTransId());
				if (response.getStatus().equalsIgnoreCase("success")) {
					response.setStatus(EnumRafTransactionStatus.SUCCESS
							.getValue());
				} else if (response.getStatus().equalsIgnoreCase("failure")) {
					response.setStatus(EnumRafTransactionStatus.FAILURE
							.getValue());
				}

				updateConversionRequest(response);
			} catch (Exception e) {
				LOGGER.error("Exception in approve conversion request for clickId :"
						+ request.getClickId()
						+ "Error message : "
						+ e.getMessage());
			}

		}

	}

	public void downloadAndSaveRewards() throws FDResourceException,
			FileNotFoundException, ExtoleServiceException, ParseException {
		String fileName = null;

		// 1. Download the extole result file from extole server
		if (!ExtoleSftpService.isFileExists()) {
			fileName = ExtoleSftpService.downloadFile();
		}

		// 2.Parse this downloaded file and save into RAF_CREDIT table
		List<FDRafCreditModel> fdEarnedRewardsList = ExtoleEarnedRewardsParser
				.parseCsvFile(fileName);

		// 3. Save the parsed file records into the database
		saveExtoleRewardsFile(fdEarnedRewardsList);
	}
}
