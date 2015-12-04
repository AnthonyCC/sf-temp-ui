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

public class ExtoleSftpService {

	private static final Logger LOGGER = LoggerFactory
			.getInstance(ExtoleSftpService.class);

	private final static JSch jsch = new JSch();

	private static final String SFTP_PRIVATE_KEY = FDStoreProperties.get(FDStoreProperties.PROP_EXTOLE_SFTP_PRIVATE_KEY);//"C:/Extole Keys/extole_sftp.key";
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

	public static String downloadFile() {

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
			LOGGER.info("SFTP: Session created on host " + SFTP_HOST);

			channel = session.openChannel("sftp");
			channel.connect();

			channelSftp = (ChannelSftp) channel;
			LOGGER.info(channelSftp.isConnected());
			LOGGER.info("SFTP: SFTP Channel connected..");

			LOGGER.info("Downloading Rewards file");

			try {

				source = SFTP_REMOTE_WORKING_DIR + "/"
						+ buildFileName(EXTOLE_BASE_FILE_NAME);
				destination = SFTP_LOCAL_WORKING_DIR + "/"
						+ buildFileName(EXTOLE_BASE_FILE_NAME);
				channelSftp.get(source, destination,
						new ExtoleSftpDownloadProgressMonitor());

				LOGGER.info("Successfully downloaded the file");
			} catch (SftpException e) {
				e.printStackTrace();
			}

		} catch (JSchException e) {
			e.printStackTrace();
		} finally {

			if (null != channelSftp && channelSftp.isConnected()) {
				LOGGER.debug("SFTP: disconnecting sftp channel");
				channelSftp.disconnect();
			}
			if (null != channel) {
				LOGGER.debug("SFTP: disconnecting channel");
				channel.disconnect();
			}

			if (null != session && session.isConnected()) {
				LOGGER.debug("SFTP: disconnecting session");
				session.disconnect();
			}

		}
		return destination;
	}

	/**
	 * append todays date to the base file name
	 * 
	 * @param baseFileName
	 * @return
	 */
	private static String buildFileName(String baseFileName) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = Calendar.getInstance().getTime();
		String fileName = baseFileName + df.format(date) + ".csv";

		return fileName;
	}

	public static boolean isFileExists() {
		String fileName = buildFileName(EXTOLE_BASE_FILE_NAME);
		File resultFile = new File(SFTP_LOCAL_WORKING_DIR, fileName);

		if (resultFile.exists()) {
			LOGGER.info("The file" + fileName + " already exists");
			return true;
		} else
			LOGGER.info("The file" + fileName + " does not exist in");
		return false;

	}

}