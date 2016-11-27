package com.freshdirect.referral.extole;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * @author bpillutla
 * 
 */
public class ExtoleSftpService {

	private static final Logger LOGGER = LoggerFactory
			.getInstance(ExtoleSftpService.class);

	private final static JSch jsch = new JSch();

	private static final String SFTP_PRIVATE_KEY = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_SFTP_PRIVATE_KEY);//"C:/Extole Keys/extole_sftp.key";
	private static final String SFTP_HOST = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_SFTP_HOST);
	private static final String SFTP_USERNAME = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_SFTP_USERNAME);
	private static final String SFTP_REMOTE_WORKING_DIR = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_SFTP_FILE_DOWNLOADER_REMOTE_WORKDIR);
	private static final String SFTP_LOCAL_WORKING_DIR = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_SFTP_FILE_DOWNLOADER_LOCAL_WORKDIR);
	private static final String EXTOLE_BASE_FILE_NAME = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_BASE_FILE_NAME);

	public static String getLocalWorkingDir() {
		return SFTP_LOCAL_WORKING_DIR;
	}

	/**
	 * Download the given file from Extole into opt/fdlog/referralCredits  
	 * folder and return the destination of the downloaded file
	 * 
	 * @param fileName
	 * @return file destination
	 */
	public static String downloadFile(String fileName) throws ExtoleServiceException {

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		Properties config = new Properties();
		String source = null;
		String destination = null;

		try {

			jsch.addIdentity(SFTP_PRIVATE_KEY);
			session = jsch.getSession(SFTP_USERNAME, SFTP_HOST);
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			LOGGER.info(" Extole SFTP: Session created on host " + SFTP_HOST);
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			LOGGER.info(" Extole SFTP Channel connected ");
			LOGGER.info(" Downloading " + fileName + " Rewards file " + " from " + SFTP_REMOTE_WORKING_DIR);
			try {
				source = SFTP_REMOTE_WORKING_DIR + "/" + fileName;
				destination = SFTP_LOCAL_WORKING_DIR + "/" + fileName;

				/*
				 * source = SFTP_REMOTE_WORKING_DIR + "/" + buildFileName();
				 * destination = SFTP_LOCAL_WORKING_DIR + "/" + buildFileName();
				 */
				channelSftp.get(source, destination, new ExtoleSftpDownloadProgressMonitor());
				LOGGER.info(" Successfully downloaded : " + fileName + " Rewards File ");
			} catch (SftpException e) {
				LOGGER.error("Error whille file download ",e);
				throw new ExtoleServiceException(e);
			}

		} catch (JSchException e) {
			LOGGER.error("Error while connecting to Extole ",e);
			throw new ExtoleServiceException(e);
		} finally {
			if (null != channelSftp && channelSftp.isConnected()) {
				LOGGER.info(" Extole SFTP: disconnecting sftp channel");
				channelSftp.disconnect();
			}
			if (null != channel) {
				LOGGER.info(" Extole SFTP: disconnecting channel");
				channel.disconnect();
			}
			if (null != session && session.isConnected()) {
				LOGGER.info("Extole SFTP: disconnecting session");
				session.disconnect();
			}
		}
		return destination;
	}

	/**
	 * append todays date to the base file name
	 * 
	 * @param baseFileName
	 * @return fileName
	 */
	public static String buildFileName() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = Calendar.getInstance().getTime();
		String fileName = EXTOLE_BASE_FILE_NAME + df.format(date) + ".csv";
		return fileName;
	}

	/**
	 * Check if the fileName exists in local working directory
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExists(String fileName) {
		File resultFile = new File(SFTP_LOCAL_WORKING_DIR, fileName);
		if (resultFile.exists()) {
			LOGGER.info(" The file " + fileName + " already exists in " + SFTP_LOCAL_WORKING_DIR);
			return true;
		} else
			LOGGER.info(" The file " + fileName + " does not exists in " + SFTP_LOCAL_WORKING_DIR);
		return false;
	}
}