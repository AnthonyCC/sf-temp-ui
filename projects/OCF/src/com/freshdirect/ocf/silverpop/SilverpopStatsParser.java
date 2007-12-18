/**
 * @author ekracoff
 * Created on May 26, 2005*/

package com.freshdirect.ocf.silverpop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.freshdirect.ocf.core.EmailStatLine;

public class SilverpopStatsParser {
	private String delimiter;
	private List fields = new ArrayList();
	private List headers = new ArrayList();
	private List statistics = new ArrayList();

	private static final String MAILING_ID = "MAILING_ID";
	private static final String EMAIL = "EMAIL";
	private static final String OPEN_COUNT = "OPEN_COUNT";
	private static final String CLICKSTREAM_COUNT = "CLICKSTREAM_COUNT";
	private static final String CLICKTHRU_COUNT = "CLICKTHRU_COUNT";
	private static final String CONVERSION_COUNT = "CONVERSION_COUNT";
	private static final String ATTACHMENT_COUNT = "ATTACHMENT_COUNT";
	private static final String FORWARD_COUNT = "FORWARD_COUNT";
	private static final String MEDIA_COUNT = "MEDIA_COUNT";
	private static final String BOUNCE_COUNT = "BOUNCE_COUNT";
	private static final String OPTOUT_COUNT = "OPTOUT_COUNT";
	private static final String OPTIN_COUNT = "OPTIN_COUNT";
	private static final String ABUSE_COUNT = "ABUSE_COUNT";
	private static final String CHANGE_ADDRESS_COUNT = "CHANGE_ADDRESS_COUNT";
	private static final String BLOCKED_COUNT = "BLOCKED_COUNT";
	private static final String RESTRICED_COUNT = "RESTRICED_COUNT";
	private static final String OTHER_REPLY_COUNT = "OTHER_REPLY_COUNT";
	private static final String SUPPRESSION_COUNT = "SUPPRESSION_COUNT";

	public SilverpopStatsParser() {
		delimiter = ",";
		this.fields.add(MAILING_ID);
		this.fields.add(EMAIL);
		this.fields.add(OPEN_COUNT);
		this.fields.add(CLICKSTREAM_COUNT);
		this.fields.add(CLICKTHRU_COUNT);
		this.fields.add(CONVERSION_COUNT);
		this.fields.add(ATTACHMENT_COUNT);
		this.fields.add(FORWARD_COUNT);
		this.fields.add(MEDIA_COUNT);
		this.fields.add(BOUNCE_COUNT);
		this.fields.add(OPTOUT_COUNT);
		this.fields.add(OPTIN_COUNT);
		this.fields.add(ABUSE_COUNT);
		this.fields.add(CHANGE_ADDRESS_COUNT);
		this.fields.add(BLOCKED_COUNT);
		this.fields.add(RESTRICED_COUNT);
		this.fields.add(OTHER_REPLY_COUNT);
		this.fields.add(SUPPRESSION_COUNT);
	}

	public List parseFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = new String();
		int lineNum = 0;

		while (null != (line = reader.readLine())) {
			if (lineNum == 0) {
				lineNum++;
				continue;
			}

			Map tokens = parseLine(line);
			makeObjects(tokens);
			lineNum++;
		}

		return this.statistics;
	}

	private Map parseLine(String line) {
		Map tokenMap = new HashMap();
		StringTokenizer strTok = new StringTokenizer(line, delimiter);

		String[] tokens = line.split(delimiter, -2);

		for (int i = 0, size = this.fields.size(); i < size; i++) {
			String f = (String) this.fields.get(i);
			String v = tokens[i].trim();
			tokenMap.put(f, v);
		}

		return tokenMap;
	}

	private void makeObjects(Map tokens) {
		EmailStatLine stat = new EmailStatLine();

		stat.setMailingId(getString(tokens, MAILING_ID));
		stat.setCustomerId(getString(tokens, EMAIL));
		stat.setOpenCount(getInt(tokens, OPEN_COUNT));
		stat.setClickstreamCount(getInt(tokens, CLICKSTREAM_COUNT));
		stat.setClickthruCount(getInt(tokens, CLICKTHRU_COUNT));
		stat.setConversionCount(getInt(tokens, CONVERSION_COUNT));
		stat.setAttachmentCount(getInt(tokens, ATTACHMENT_COUNT));
		stat.setForwardCount(getInt(tokens, FORWARD_COUNT));
		stat.setMediaCount(getInt(tokens, MEDIA_COUNT));
		stat.setBounceCount(getInt(tokens, BOUNCE_COUNT));
		stat.setOptoutCount(getInt(tokens, OPTOUT_COUNT));
		stat.setOptinCount(getInt(tokens, OPTIN_COUNT));
		stat.setAbuseCount(getInt(tokens, ABUSE_COUNT));
		stat.setChangeAddressCount(getInt(tokens, CHANGE_ADDRESS_COUNT));
		stat.setBlockedCount(getInt(tokens, BLOCKED_COUNT));
		stat.setRestrictedCount(getInt(tokens, RESTRICED_COUNT));
		stat.setSuppressionCount(getInt(tokens, SUPPRESSION_COUNT));

		statistics.add(stat);
	}

	private String getString(Map tokens, String field) {
		return (String) tokens.get(field);
	}

	private int getInt(Map tokens, String field) {
		return new Integer((String) tokens.get(field)).intValue();
	}

	/** returns a list of EmailStatLine objects **/
	public List getStatistics() {
		return statistics;
	}

	public void setStatistics(List statistics) {
		this.statistics = statistics;
	}
}