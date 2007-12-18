package com.freshdirect.mktAdmin.model;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.fdstore.FDStoreProperties;

public class CompetitorSearchCriteria implements Serializable {

	private int startIndex=0;
	private static final int FETCH_SIZE=FDStoreProperties.getReferralPrgPaginationSize(); 
	private Date dateCreated=null;
	private String competitionType=null;
	private String zipCode=null;
	private String sortedByColumn=null;
	private int totalRcdSize;
	
	public CompetitorSearchCriteria()
	{		
	}
	
	public CompetitorSearchCriteria(int startIndex)
	{
		this.startIndex=startIndex;
	}
	
	public CompetitorSearchCriteria(int startIndex,Date dateCreated)
	{
		this(startIndex);
		this.dateCreated=dateCreated;
	}

	
	public int getEndIndex() {
		if(totalRcdSize!=0 && (startIndex+FETCH_SIZE)>totalRcdSize){
			return totalRcdSize;
		}
		return startIndex+FETCH_SIZE;
	}

	
	public int getPaginationRcdCapacity()
	{
		return FETCH_SIZE;
	}
	
	public int getPreviousIndex()
	{
		if(this.startIndex==0){
			return this.startIndex; 
		}else{
			return this.startIndex-FETCH_SIZE;
		}
	}
	
	public String getCompetitionType() {
		return competitionType;
	}
	public void setCompetitionType(String competitionType) {
		this.competitionType = competitionType;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	

	public String getSortedByColumn() {
		return sortedByColumn;
	}

	public void setSortedByColumn(String sortedByColumn) {
		this.sortedByColumn = sortedByColumn;
	}
	
}
