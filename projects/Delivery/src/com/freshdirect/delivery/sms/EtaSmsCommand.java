package com.freshdirect.delivery.sms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.sms.FDSmsGateway;
import com.freshdirect.sms.model.st.STSmsResponse;
import com.freshdirect.sms.service.SmsServiceException;

public class EtaSmsCommand implements Serializable, Runnable {
	
	List<SmsAlertETAInfo> etaInfoList;
	List<STSmsResponse> stSmsResponses = new ArrayList<STSmsResponse>();
	
	//Constants
	private static final String ETA_MESSAGE_TEXT_1 = "Hello! Your FreshDirect order is getting a few finishing touches, and will be delivered between ";
	private static final String ETA_MESSAGE_TEXT_2 = ". Questions? www.freshdirect.com/help";
	private static final String ETA_MIN_DEFAULT="00";
	
	
	public List<SmsAlertETAInfo> getEtaInfoList() {
		return etaInfoList;
	}
	public void setEtaInfoList(List<SmsAlertETAInfo> etaInfoList) {
		this.etaInfoList = etaInfoList;
	}
	
	
	public List<STSmsResponse> getStSmsResponses() {
		return stSmsResponses;
	}
	public void setStSmsResponses(List<STSmsResponse> stSmsResponses) {
		this.stSmsResponses = stSmsResponses;
	}
	
	private String getTime(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		String hour="";
		String mins="";
		if(calendar.get(Calendar.HOUR) == 12 || calendar.get(Calendar.HOUR) == 0 || calendar.get(Calendar.HOUR) == 24){
			hour = "12";
		}else{
			hour = String.valueOf(calendar.get(Calendar.HOUR));
		}
		mins=String.valueOf(calendar.get(Calendar.MINUTE)).equals("0") ? ETA_MIN_DEFAULT:String.valueOf(calendar.get(Calendar.MINUTE));
		return (hour+":"+mins + "  " + calendar.getDisplayName(
				Calendar.AM_PM, Calendar.SHORT, Locale.US));
	}
	
	public void execute()  {
		try {
			if (etaInfoList!=null) {
				for (int i = 0; i < etaInfoList.size(); i++) {

					if (etaInfoList.get(i).isETA() != null) {
						Date currentDate = new Date();
						if (etaInfoList.get(i).isETA()
								&& etaInfoList.get(i).getEtaStartTime() != null
								&& etaInfoList.get(i).getEtaEndTime() != null
								&& etaInfoList.get(i).getEtaEndTime().after(currentDate)) {
							STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(etaInfoList.get(i).getMobileNumber(),ETA_MESSAGE_TEXT_1
													+ getTime(etaInfoList.get(i).getEtaStartTime())+ " and "
													+ getTime(etaInfoList.get(i).getEtaEndTime())
													+ ETA_MESSAGE_TEXT_2, EnumEStoreId.FD.name());
							
							
							smsResponseModel.setDate(new Date());
							smsResponseModel.setOrderId(etaInfoList.get(i).getOrderId());
							smsResponseModel.setCustomerId(etaInfoList.get(i).getCustomerId());
							stSmsResponses.add(smsResponseModel);
							
						} else if (!etaInfoList.get(i).isETA()
								&& etaInfoList.get(i).getWindowStartTime() != null
								&& etaInfoList.get(i).getWindowEndTime() != null
								&& etaInfoList.get(i).getWindowEndTime().after(currentDate)) {
							STSmsResponse smsResponseModel = FDSmsGateway.sendSMS(etaInfoList.get(i).getMobileNumber(),ETA_MESSAGE_TEXT_1
													+ getTime(etaInfoList.get(i).getWindowStartTime())
													+ " and "
													+ getTime(etaInfoList.get(i).getWindowEndTime())
													+ ETA_MESSAGE_TEXT_2, EnumEStoreId.FD.name());
							
							smsResponseModel.setDate(new Date());
							smsResponseModel.setOrderId(etaInfoList.get(i).getOrderId());
							smsResponseModel.setCustomerId(etaInfoList.get(i).getCustomerId());
							stSmsResponses.add(smsResponseModel);
							
						}
					}
				}
			}
		}catch (SmsServiceException e) {
			
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		this.execute();
	}

}
