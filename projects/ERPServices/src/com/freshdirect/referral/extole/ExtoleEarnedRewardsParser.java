package com.freshdirect.referral.extole;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.referral.extole.model.FDRafCreditModel;

public class ExtoleEarnedRewardsParser {

	private static final Logger LOGGER = LoggerFactory
			.getInstance(ExtoleEarnedRewardsParser.class);

	//2015-12-09 15:11:03
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss"; 
	
	public static List<FDRafCreditModel> parseCsvFile(String fileName)
			throws FileNotFoundException, ParseException {
		List<FDRafCreditModel> fdEarnedRewardsList = null;
		LOGGER.info("Starting parsing of " + fileName + " referral rewards file ");
		
		// Mapping columns from csv file to the database columns
		Map<String, String> columnMapping = new HashMap<String, String>();
		columnMapping.put("Advocate First Name", "advocateFirstName");
		columnMapping.put("Advocate Last Name", "advocateLastName");
		columnMapping.put("Advocate Email", "advocateEmail");
		columnMapping.put("Advocate Partner User Id", "advocatePartnerUid");
		columnMapping.put("Friend First Name", "friendFirstName");
		columnMapping.put("Friend Last Name", "friendLastName");
		columnMapping.put("Friend Email", "friendEmail");
		columnMapping.put("Friend Partner User Id", "friendPartnerUid");
		columnMapping.put("Reward Type", "rewardType");
		columnMapping.put("Reward Date", "rewardDateString");
		columnMapping.put("Reward Set Name", "rewardSetName");
		columnMapping.put("Reward Set Id", "rewardSetId");
		columnMapping.put("Reward Value", "rewardValueString");
		columnMapping.put("Reward Detail", "rewardDetail");
		columnMapping.put("Campaign Id","campaignId");
		columnMapping.put("Campaign Name","campaignName");

		/* 	// using opencsv 2.3 jar
		 * HeaderColumnNameTranslateMappingStrategy<FDRafCredit> strategy = new
		 * HeaderColumnNameTranslateMappingStrategy<FDRafCredit>();
		 * strategy.setType(FDRafCredit.class);
		 * strategy.setColumnMapping(columnMapping);
		 * 
		 * CSVReader csvReader = new CSVReader(new FileReader(fileName));
		 * CsvToBean<FDRafCredit> csvToBean = new CsvToBean<FDRafCredit>();
		 */

		HeaderColumnNameTranslateMappingStrategy strategy = new HeaderColumnNameTranslateMappingStrategy();
		strategy.setType(FDRafCreditModel.class);
		strategy.setColumnMapping(columnMapping);

		Reader reader = new FileReader(fileName);
		CsvToBean csvToBean = new CsvToBean();
		fdEarnedRewardsList = csvToBean.parse(strategy, reader);
		
		List<FDRafCreditModel> parsedRewardList=processData(fdEarnedRewardsList);
		
		LOGGER.info(" Finished parsing of " + fileName + " referral rewards file ");
		return parsedRewardList;
	}

	/**
	 * Post processing of Data to convert String values in particular format
	 * 
	 * @param rewards
	 * @return list of FDEarnedRewards
	 * @throws ParseException
	 */
	
	private static List<FDRafCreditModel> processData(List<FDRafCreditModel> rewards)
			throws ParseException {
		
		Iterator<FDRafCreditModel> iterator = rewards.iterator();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		while (iterator.hasNext()) {
			FDRafCreditModel reward = iterator.next();
			LOGGER.info(reward.getRewardDateString());

			try {
				reward.setRewardDate(sdf.parse(reward.getRewardDateString()));
			} catch (ParseException e) {
				LOGGER.error("Could not parse the reward date : " + reward.getRewardDateString() + " for " + reward.getAdvocateEmail(), e);
			}

			reward.setStatus(EnumRafTransactionStatus.PENDING);
			reward.setCreationTime(Calendar.getInstance().getTime());
			reward.setModifiedTime(Calendar.getInstance().getTime());
			try {
				reward.setRewardValue(Double.parseDouble(reward.getRewardValueString()));
			} catch (NumberFormatException nfe) {
				LOGGER.warn("Could not set the reward value : " + reward.getRewardValueString() + " for " + reward.getAdvocateEmail(), nfe);
				// The reward value cannot be null. It should be a whole number. If it is null, we are setting it to zero
				reward.setRewardValue(0);
			}		
		}
		return rewards;
	}
}
