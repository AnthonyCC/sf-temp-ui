package com.freshdirect.referral.extole;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.jcraft.jsch.SftpProgressMonitor;

public class ExtoleSftpDownloadProgressMonitor implements SftpProgressMonitor {

	private static final Logger LOGGER = LoggerFactory
			.getInstance(ExtoleSftpDownloadProgressMonitor.class);

	private double count;
	private double max;
	private String src;
	private int percent;
	private int lastDisplayedPercent;

	ExtoleSftpDownloadProgressMonitor() {
		count = 0;
		max = 0;
		percent = 0;
		lastDisplayedPercent = 0;
	}

	public void init(int op, String src, String dest, long max) {
		this.max = max;
		this.src = src;
		count = 0;
		percent = 0;
		lastDisplayedPercent = 0;
		status();
	}

	public boolean count(long count) {
		this.count += count;
		percent = (int) ((this.count / max) * 100.0);
		status();
		return true;
	}

	public void end() {
		percent = (int) ((count / max) * 100.0);
		status();
	}

	private void status() {
		if (lastDisplayedPercent <= percent - 10) {
			LOGGER.info("Download Status " + " : " + percent + "% " + ((long) count) + "/" + ((long) max));
			lastDisplayedPercent = percent;
		}
	}
}
