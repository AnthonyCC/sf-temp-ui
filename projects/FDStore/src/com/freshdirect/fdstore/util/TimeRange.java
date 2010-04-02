package com.freshdirect.fdstore.util;

import java.io.Serializable;
import java.util.Calendar;

import com.freshdirect.framework.util.DateUtil;

public class TimeRange implements Serializable, Comparable<TimeRange>{
	private int duration;
	private int daysRangeFrom;
	private int daysRangeTo;
	private int recencyType;
	private int fromValue;
	private int toValue;
	private int sequence;
	//private final String description;
	public final static int NULL = 0;
	public final static int DAY = 1;
	public final static int WEEK = 2;
	public final static int MONTH = 3;
	
	public final static int OLDER_THAN = 1;
	public final static int NEWER_THAN = 2;
	
	public TimeRange(int sequence, int fromValue, int toValue, int duration, int recencyType){
		this.sequence = sequence;
		this.duration = duration;
		this.recencyType = recencyType;
		this.fromValue = fromValue;
		this.toValue = toValue;
		
		Calendar cur = Calendar.getInstance();
		
		Calendar c1 = Calendar.getInstance();
		if(fromValue != 0){
			if(this.duration == DAY){
				c1.add(Calendar.DATE, -(this.fromValue));
			} else if(this.duration == WEEK){
				c1.add(Calendar.DATE, -(this.fromValue * 7));
			} else if(this.duration == MONTH){
				c1.add(Calendar.MONTH, -(this.fromValue));
			}
			this.daysRangeFrom = DateUtil.getDiffInDays(cur.getTime(), c1.getTime());
		}
		
		
		Calendar c2 = Calendar.getInstance();			
		if(toValue != 0){
			if(this.duration == DAY){
				c2.add(Calendar.DATE, -(this.toValue));
			} else if(this.duration == WEEK){
				c2.add(Calendar.DATE, -(this.toValue * 7));
			} else if(this.duration == MONTH){
				c2.add(Calendar.MONTH, -(this.toValue));
			}
			this.daysRangeTo = DateUtil.getDiffInDays(cur.getTime(), c2.getTime());
		}
		

		//this.description = description;
	}
	
	
	
	public int getDaysRangeFrom() {
		return this.daysRangeFrom;
	}

	public int getDaysRangeTo() {
		return this.daysRangeTo;
	}
	
	
	public int getFromValue() {
		return this.fromValue;
	}
	
	public int getToValue() {
		return this.toValue;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public int getRecencyType() {
		return this.recencyType;
	}
	
	public int getSequence() {
		return this.sequence;
	}
	
	public String getDescription() {
		StringBuffer buf = new StringBuffer();
		if(this.duration == DAY){
			if(this.fromValue > 0 && this.toValue == 0 && this.recencyType == NEWER_THAN){
				buf.append("Added in the last ").append(ConvertNumberToText.convert(this.fromValue));
				buf.append((this.fromValue > 1) ? " days" : " day");
			} else if(this.fromValue > 0 && this.toValue == 0 && this.recencyType == OLDER_THAN){
				buf.append("Added ").append(ConvertNumberToText.convert(this.fromValue));
				buf.append((this.fromValue > 1) ? " days ago" : " day ago");
			} else {
				buf.append("Added ").append(ConvertNumberToText.convert(this.fromValue)).append(" to ").append(ConvertNumberToText.convert(this.toValue));
				buf.append((this.toValue > 1) ? " days ago" : " day ago");
			}
		}
		if(this.duration == WEEK){
			if(this.fromValue > 0 && this.toValue == 0 && this.recencyType == NEWER_THAN){
				buf.append("Added in the last ").append(ConvertNumberToText.convert(this.fromValue));
				buf.append((this.fromValue > 1) ? " weeks" : " week");
			} else if(this.fromValue > 0 && this.toValue == 0 && this.recencyType == OLDER_THAN){
				buf.append("Added ").append(ConvertNumberToText.convert(this.fromValue));
				buf.append((this.fromValue > 1) ? " weeks ago" : " week ago");
			} else {
				buf.append("Added ").append(ConvertNumberToText.convert(this.fromValue)).append(" to ").append(ConvertNumberToText.convert(this.toValue));
				buf.append((this.toValue > 1) ? " weeks ago" : " week ago");
			}
		}
		if(this.duration == MONTH){
			if(this.fromValue > 0 && this.toValue == 0 && this.recencyType == NEWER_THAN){
				buf.append("Added in the last ").append(ConvertNumberToText.convert(this.fromValue));
				buf.append((this.fromValue > 1) ? " months" : " month");
			}	if(this.fromValue > 0 && this.toValue == 0 && this.recencyType == OLDER_THAN){
				buf.append("Added ").append(ConvertNumberToText.convert(this.fromValue));
				buf.append((this.fromValue > 1) ? " months ago" : " month ago");
			} else {
				buf.append("Added ").append(ConvertNumberToText.convert(this.fromValue));
				buf.append((this.fromValue > 1) ? " months ago" : " month ago");
			}
		}
		//return this.description;
		return buf.toString();
	}
	
	public String toString() {
		return "TimeRange [fromValue "+this.fromValue
				+" toValue "+this.toValue
				+" fromDays "+this.daysRangeFrom
				+" toDays "+this.daysRangeTo
				+" duration "
				+this.duration+"]"; 
	}
	
	public int compareTo(TimeRange o1){
		return new Integer(this.getSequence()).compareTo(new Integer(o1.getSequence()));
	}
}

