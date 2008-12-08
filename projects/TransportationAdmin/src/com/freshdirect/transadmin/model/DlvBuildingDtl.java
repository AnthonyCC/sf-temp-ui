package com.freshdirect.transadmin.model;
import java.math.BigDecimal;
import java.util.Date;

import com.freshdirect.framework.util.TimeOfDay;

public class DlvBuildingDtl implements java.io.Serializable, TrnBaseEntityI  {
	
	
	private DlvBuilding building;
	private String dlvBuildingDtlId;	
	private String addrType;
	private String companyName;
	
	private String includeMon;
	private String includeTue;
	private String includeWed;
	private String includeThu;
	private String includeFri;
	private String includeSat;
	private String includeSun;
	
	private TimeOfDay hoursOpenMon;
	private TimeOfDay hoursOpenTue;
	private TimeOfDay hoursOpenWed;
	private TimeOfDay hoursOpenThu;
	private TimeOfDay hoursOpenFri;
	private TimeOfDay hoursOpenSat;
	private TimeOfDay hoursOpenSun;
	
	private TimeOfDay hoursCloseMon;
	private TimeOfDay hoursCloseTue;
	private TimeOfDay hoursCloseWed;
	private TimeOfDay hoursCloseThu;
	private TimeOfDay hoursCloseFri;
	private TimeOfDay hoursCloseSat;
	private TimeOfDay hoursCloseSun;
	
	private String commentMon;
	private String commentTue;
	private String commentWed;
	private String commentThu;
	private String commentFri;
	private String commentSat;
	private String commentSun;

	private String svcIncludeMon;
	private String svcIncludeTue;
	private String svcIncludeWed;
	private String svcIncludeThu;
	private String svcIncludeFri;
	private String svcIncludeSat;
	private String svcIncludeSun;
	
	private TimeOfDay svcHoursOpenMon;
	private TimeOfDay svcHoursOpenTue;
	private TimeOfDay svcHoursOpenWed;
	private TimeOfDay svcHoursOpenThu;
	private TimeOfDay svcHoursOpenFri;
	private TimeOfDay svcHoursOpenSat;
	private TimeOfDay svcHoursOpenSun;
	
	private TimeOfDay svcHoursCloseMon;
	private TimeOfDay svcHoursCloseTue;
	private TimeOfDay svcHoursCloseWed;
	private TimeOfDay svcHoursCloseThu;
	private TimeOfDay svcHoursCloseFri;
	private TimeOfDay svcHoursCloseSat;
	private TimeOfDay svcHoursCloseSun;
	
	private String svcCommentMon;
	private String svcCommentTue;
	private String svcCommentWed;
	private String svcCommentThu;
	private String svcCommentFri;
	private String svcCommentSat;
	private String svcCommentSun;
	
	
	private String svcValidate;
	private String svcScrubbedStreet;
	private String svcAddrLine2;
	private String svcCity;
	private String svcState;
	private String svcZip;
	
	private String doorman;
	private String walkup;
	private String elevator;
	private String svcEnt;
	private String house;
	
	private String handTruckAllowed;
	private String aptDlvAllowed;
	private Integer walkUpFloors;
	
	private String difficultReason;
	private String difficultToDeliver;
	private Integer extraTimeNeeded;

	private String isNew;
	
	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	
	public String getDifficultToDeliver() {
		return difficultToDeliver;
	}

	public void setDifficultToDeliver(String difficultToDeliver) {
		this.difficultToDeliver = difficultToDeliver;
	}

	public String getDoorman() {
		return doorman;
	}

	public void setDoorman(String doorman) {
		this.doorman = doorman;
	}

	public String getIncludeMon() {
		return includeMon;
	}

	public void setIncludeMon(String includeMon) {
		this.includeMon = includeMon;
	}


	public void setHoursOpenMon(TimeOfDay hoursOpenMon) {
		this.hoursOpenMon = hoursOpenMon;
	}

	public TimeOfDay getHoursOpenMon() {
		return hoursOpenMon;
	}
			
	public String getSvcIncludeMon() {
		return svcIncludeMon;
	}

	public void setSvcIncludeMon(String svcIncludeMon) {
		this.svcIncludeMon = svcIncludeMon;
	}

	public String getSvcIncludeTue() {
		return svcIncludeTue;
	}

	public void setSvcIncludeTue(String svcIncludeTue) {
		this.svcIncludeTue = svcIncludeTue;
	}

	public TimeOfDay getSvcHoursOpenMon() {
		return svcHoursOpenMon;
	}

	public String getDifficultReason() {
		return difficultReason;
	}
	public void setDifficultReason(String difficultReason) {
		this.difficultReason = difficultReason;
	}		
		
	public String getDlvBuildingDtlId() {
		return dlvBuildingDtlId;
	}
	public void setDlvBuildingDtlId(String dlvBuildingDtlId) {
		this.dlvBuildingDtlId = dlvBuildingDtlId;
	}
	public String getDlvBuildingId() {
		if(getBuilding() == null) {
			return null;
		}
		return getBuilding().getBuildingId();
	}
	public void setDlvBuildingId(String id) {
		if("null".equals(id)) {
			//setServiceTimeType(null);
		} else {
			DlvBuilding trnBuildingType = new DlvBuilding();
			trnBuildingType.setBuildingId(id);
			setBuilding(trnBuildingType);
		}
	}	
	
	public boolean isObsoleteEntity() {
		return false;
	}
	
	public DlvBuilding getBuilding() {
		return building;
	}
	public void setBuilding(DlvBuilding building) {
		this.building = building;
	}
	public String getAddrType() {
		return addrType;
	}
	public void setAddrType(String addressType) {
		this.addrType = addressType;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSvcCity() {
		return svcCity;
	}

	public void setSvcCity(String svcCity) {
		this.svcCity = svcCity;
	}

	public String getSvcScrubbedStreet() {
		return svcScrubbedStreet;
	}

	public void setSvcScrubbedStreet(String svcScrubbedSt) {
		this.svcScrubbedStreet = svcScrubbedSt;
	}

	public String getSvcState() {
		return svcState;
	}

	public void setSvcState(String svcState) {
		this.svcState = svcState;
	}

	public String getSvcZip() {
		return svcZip;
	}

	public void setSvcZip(String svcZip) {
		this.svcZip = svcZip;
	}

	public String getSvcValidate() {
		return svcValidate;
	}

	public void setSvcValidate(String svcValidate) {
		this.svcValidate = svcValidate;
	}

	public String getSvcAddrLine2() {
		return svcAddrLine2;
	}

	public void setSvcAddrLine2(String svcAddrLine2) {
		this.svcAddrLine2 = svcAddrLine2;
	}

	
	public String getCommentMon() {
		return commentMon;
	}

	public void setCommentMon(String commentMon) {
		this.commentMon = commentMon;
	}
	
	public String getSvcCommentMon() {
		return svcCommentMon;
	}

	public void setSvcCommentMon(String svcCommentMon) {
		this.svcCommentMon = svcCommentMon;
	}

	public String getSvcCommentTue() {
		return svcCommentTue;
	}

	public void setSvcCommentTue(String svcCommentTue) {
		this.svcCommentTue = svcCommentTue;
	}

	public TimeOfDay getHoursCloseMon() {
		return hoursCloseMon;
	}

	public void setHoursCloseMon(TimeOfDay hoursCloseMon) {
		this.hoursCloseMon = hoursCloseMon;
	}

	public TimeOfDay getSvcHoursCloseMon() {
		return svcHoursCloseMon;
	}

	public void setSvcHoursCloseMon(TimeOfDay svcHoursCloseMon) {
		this.svcHoursCloseMon = svcHoursCloseMon;
	}

	public TimeOfDay getSvcHoursCloseTue() {
		return svcHoursCloseTue;
	}

	public void setSvcHoursCloseTue(TimeOfDay svcHoursCloseTue) {
		this.svcHoursCloseTue = svcHoursCloseTue;
	}

	public TimeOfDay getSvcHoursOpenTue() {
		return svcHoursOpenTue;
	}

	public void setSvcHoursOpenTue(TimeOfDay svcHoursOpenTue) {
		this.svcHoursOpenTue = svcHoursOpenTue;
	}

	public void setSvcHoursOpenMon(TimeOfDay svcHoursOpenMon) {
		this.svcHoursOpenMon = svcHoursOpenMon;
	}

	public String getElevator() {
		return elevator;
	}

	public void setElevator(String elevator) {
		this.elevator = elevator;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getSvcEnt() {
		return svcEnt;
	}

	public void setSvcEnt(String svcEnt) {
		this.svcEnt = svcEnt;
	}

	public String getWalkup() {
		return walkup;
	}

	public void setWalkup(String walkup) {
		this.walkup = walkup;
	}

	public Integer getExtraTimeNeeded() {
		return extraTimeNeeded;
	}

	public void setExtraTimeNeeded(Integer extraTimeNeeded) {
		this.extraTimeNeeded = extraTimeNeeded;
	}

	public String getAptDlvAllowed() {
		return aptDlvAllowed;
	}

	public void setAptDlvAllowed(String aptDlvAllowed) {
		this.aptDlvAllowed = aptDlvAllowed;
	}

	public String getHandTruckAllowed() {
		return handTruckAllowed;
	}

	public void setHandTruckAllowed(String handTruckAllowed) {
		this.handTruckAllowed = handTruckAllowed;
	}

	public Integer getWalkUpFloors() {
		return walkUpFloors;
	}

	public void setWalkUpFloors(Integer walkUpFloors) {
		this.walkUpFloors = walkUpFloors;
	}

	public TimeOfDay getHoursCloseTue() {
		return hoursCloseTue;
	}

	public void setHoursCloseTue(TimeOfDay hoursCloseTue) {
		this.hoursCloseTue = hoursCloseTue;
	}

	public TimeOfDay getHoursOpenTue() {
		return hoursOpenTue;
	}

	public void setHoursOpenTue(TimeOfDay hoursOpenTue) {
		this.hoursOpenTue = hoursOpenTue;
	}

	public String getIncludeTue() {
		return includeTue;
	}

	public void setIncludeTue(String includeTue) {
		this.includeTue = includeTue;
	}

	public String getCommentTue() {
		return commentTue;
	}

	public void setCommentTue(String commentTue) {
		this.commentTue = commentTue;
	}

	public String getCommentFri() {
		return commentFri;
	}

	public void setCommentFri(String commentFri) {
		this.commentFri = commentFri;
	}

	public String getCommentSat() {
		return commentSat;
	}

	public void setCommentSat(String commentSat) {
		this.commentSat = commentSat;
	}

	public String getCommentSun() {
		return commentSun;
	}

	public void setCommentSun(String commentSun) {
		this.commentSun = commentSun;
	}

	public String getCommentThu() {
		return commentThu;
	}

	public void setCommentThu(String commentThu) {
		this.commentThu = commentThu;
	}

	public String getCommentWed() {
		return commentWed;
	}

	public void setCommentWed(String commentWed) {
		this.commentWed = commentWed;
	}

	public TimeOfDay getHoursCloseFri() {
		return hoursCloseFri;
	}

	public void setHoursCloseFri(TimeOfDay hoursCloseFri) {
		this.hoursCloseFri = hoursCloseFri;
	}

	public TimeOfDay getHoursCloseSat() {
		return hoursCloseSat;
	}

	public void setHoursCloseSat(TimeOfDay hoursCloseSat) {
		this.hoursCloseSat = hoursCloseSat;
	}

	public TimeOfDay getHoursCloseSun() {
		return hoursCloseSun;
	}

	public void setHoursCloseSun(TimeOfDay hoursCloseSun) {
		this.hoursCloseSun = hoursCloseSun;
	}

	public TimeOfDay getHoursCloseThu() {
		return hoursCloseThu;
	}

	public void setHoursCloseThu(TimeOfDay hoursCloseThu) {
		this.hoursCloseThu = hoursCloseThu;
	}

	public TimeOfDay getHoursCloseWed() {
		return hoursCloseWed;
	}

	public void setHoursCloseWed(TimeOfDay hoursCloseWed) {
		this.hoursCloseWed = hoursCloseWed;
	}

	public TimeOfDay getHoursOpenFri() {
		return hoursOpenFri;
	}

	public void setHoursOpenFri(TimeOfDay hoursOpenFri) {
		this.hoursOpenFri = hoursOpenFri;
	}

	public TimeOfDay getHoursOpenSat() {
		return hoursOpenSat;
	}

	public void setHoursOpenSat(TimeOfDay hoursOpenSat) {
		this.hoursOpenSat = hoursOpenSat;
	}

	public TimeOfDay getHoursOpenSun() {
		return hoursOpenSun;
	}

	public void setHoursOpenSun(TimeOfDay hoursOpenSun) {
		this.hoursOpenSun = hoursOpenSun;
	}

	public TimeOfDay getHoursOpenThu() {
		return hoursOpenThu;
	}

	public void setHoursOpenThu(TimeOfDay hoursOpenThu) {
		this.hoursOpenThu = hoursOpenThu;
	}

	public TimeOfDay getHoursOpenWed() {
		return hoursOpenWed;
	}

	public void setHoursOpenWed(TimeOfDay hoursOpenWed) {
		this.hoursOpenWed = hoursOpenWed;
	}

	public String getIncludeFri() {
		return includeFri;
	}

	public void setIncludeFri(String includeFri) {
		this.includeFri = includeFri;
	}

	public String getIncludeSat() {
		return includeSat;
	}

	public void setIncludeSat(String includeSat) {
		this.includeSat = includeSat;
	}

	public String getIncludeSun() {
		return includeSun;
	}

	public void setIncludeSun(String includeSun) {
		this.includeSun = includeSun;
	}

	public String getIncludeThu() {
		return includeThu;
	}

	public void setIncludeThu(String includeThu) {
		this.includeThu = includeThu;
	}

	public String getIncludeWed() {
		return includeWed;
	}

	public void setIncludeWed(String includeWed) {
		this.includeWed = includeWed;
	}

	public String getSvcCommentFri() {
		return svcCommentFri;
	}

	public void setSvcCommentFri(String svcCommentFri) {
		this.svcCommentFri = svcCommentFri;
	}

	public String getSvcCommentSat() {
		return svcCommentSat;
	}

	public void setSvcCommentSat(String svcCommentSat) {
		this.svcCommentSat = svcCommentSat;
	}

	public String getSvcCommentSun() {
		return svcCommentSun;
	}

	public void setSvcCommentSun(String svcCommentSun) {
		this.svcCommentSun = svcCommentSun;
	}

	public String getSvcCommentThu() {
		return svcCommentThu;
	}

	public void setSvcCommentThu(String svcCommentThu) {
		this.svcCommentThu = svcCommentThu;
	}

	public String getSvcCommentWed() {
		return svcCommentWed;
	}

	public void setSvcCommentWed(String svcCommentWed) {
		this.svcCommentWed = svcCommentWed;
	}

	public TimeOfDay getSvcHoursCloseFri() {
		return svcHoursCloseFri;
	}

	public void setSvcHoursCloseFri(TimeOfDay svcHoursCloseFri) {
		this.svcHoursCloseFri = svcHoursCloseFri;
	}

	public TimeOfDay getSvcHoursCloseSat() {
		return svcHoursCloseSat;
	}

	public void setSvcHoursCloseSat(TimeOfDay svcHoursCloseSat) {
		this.svcHoursCloseSat = svcHoursCloseSat;
	}

	public TimeOfDay getSvcHoursCloseSun() {
		return svcHoursCloseSun;
	}

	public void setSvcHoursCloseSun(TimeOfDay svcHoursCloseSun) {
		this.svcHoursCloseSun = svcHoursCloseSun;
	}

	public TimeOfDay getSvcHoursCloseThu() {
		return svcHoursCloseThu;
	}

	public void setSvcHoursCloseThu(TimeOfDay svcHoursCloseThu) {
		this.svcHoursCloseThu = svcHoursCloseThu;
	}

	public TimeOfDay getSvcHoursCloseWed() {
		return svcHoursCloseWed;
	}

	public void setSvcHoursCloseWed(TimeOfDay svcHoursCloseWed) {
		this.svcHoursCloseWed = svcHoursCloseWed;
	}

	public TimeOfDay getSvcHoursOpenFri() {
		return svcHoursOpenFri;
	}

	public void setSvcHoursOpenFri(TimeOfDay svcHoursOpenFri) {
		this.svcHoursOpenFri = svcHoursOpenFri;
	}

	public TimeOfDay getSvcHoursOpenSat() {
		return svcHoursOpenSat;
	}

	public void setSvcHoursOpenSat(TimeOfDay svcHoursOpenSat) {
		this.svcHoursOpenSat = svcHoursOpenSat;
	}

	public TimeOfDay getSvcHoursOpenSun() {
		return svcHoursOpenSun;
	}

	public void setSvcHoursOpenSun(TimeOfDay svcHoursOpenSun) {
		this.svcHoursOpenSun = svcHoursOpenSun;
	}

	public TimeOfDay getSvcHoursOpenThu() {
		return svcHoursOpenThu;
	}

	public void setSvcHoursOpenThu(TimeOfDay svcHoursOpenThu) {
		this.svcHoursOpenThu = svcHoursOpenThu;
	}

	public TimeOfDay getSvcHoursOpenWed() {
		return svcHoursOpenWed;
	}

	public void setSvcHoursOpenWed(TimeOfDay svcHoursOpenWed) {
		this.svcHoursOpenWed = svcHoursOpenWed;
	}

	public String getSvcIncludeFri() {
		return svcIncludeFri;
	}

	public void setSvcIncludeFri(String svcIncludeFri) {
		this.svcIncludeFri = svcIncludeFri;
	}

	public String getSvcIncludeSat() {
		return svcIncludeSat;
	}

	public void setSvcIncludeSat(String svcIncludeSat) {
		this.svcIncludeSat = svcIncludeSat;
	}

	public String getSvcIncludeSun() {
		return svcIncludeSun;
	}

	public void setSvcIncludeSun(String svcIncludeSun) {
		this.svcIncludeSun = svcIncludeSun;
	}

	public String getSvcIncludeThu() {
		return svcIncludeThu;
	}

	public void setSvcIncludeThu(String svcIncludeThu) {
		this.svcIncludeThu = svcIncludeThu;
	}

	public String getSvcIncludeWed() {
		return svcIncludeWed;
	}

	public void setSvcIncludeWed(String svcIncludeWed) {
		this.svcIncludeWed = svcIncludeWed;
	}


		
	


}
