package com.freshdirect.transadmin.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class KronosEmployeeInfo {

	
	Integer personid;
	String personnum;
	String personfullname;
	String firstnm;
	String middleinitialnm;
	String lastnm;
	String shortnm;
	Timestamp companyhiredtm;
	int ftstdhrsqty;
	String employmentstatus;
	Date employmentstatusdt;
	String badgenum;
	Timestamp badgeeffectivedtm;
	Timestamp badgeexpirationdtm;
	String homelaboracctname;
	String homelaborlevelnm1;
	String homelaborlevelnm2;
	String homelaborlevelnm3;
	String homelaborlevelnm4;
	String homelaborlevelnm5;
	String homelaborlevelnm6;
	String homelaborlevelnm7;
	String homelaboracctdsc;
	String homelaborleveldsc1;
	String homelaborleveldsc2;
	String homelaborleveldsc3;
	String homelaborleveldsc4;
	String homelaborleveldsc5;
	String homelaborleveldsc6;
	String homelaborleveldsc7;
	Timestamp haeffectivedtm;
	Timestamp haexpirationdtm;
	Timestamp mgrsignoffthrudtm;
	int pendingsignoffsw;
	Timestamp payrollockthrudtm;
	String wageprflname;
	String grpschedname;
	String timezonename;
	String dcmdevgrpname;
	String supervisornum;
	String supervisorfullname;
	int supervisorid;
	Date currpayperiodstart;
	Date currpayperiodend;
	Date prevpayperiodstart;
	Date prevpayperiodend;
	Date nextpayperiodstart;
	Date nextpayperiodend;
	int laboracctid;
	int devicegroupid;
	int terminalruleid;
	int scheduleversion;
	Date lasttotaltime;
	Date lasttotalchngtime;
	int payruleid;
	int employeeid;
	int employmentstatid;
	String orgpathtxt;
	String orgpathdsctxt;
	Date seniorityrankdate;
	String workertypenm;

	

	public KronosEmployeeInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public KronosEmployeeInfo(Integer personid,
	String personnum,
	String personfullname,
	String firstnm,
	String middleinitialnm,
	String lastnm,
	String shortnm,
	Timestamp companyhiredtm,
	int ftstdhrsqty,
	String employmentstatus,
	Date employmentstatusdt,
	String badgenum,
	Timestamp badgeeffectivedtm,
	Timestamp badgeexpirationdtm,
	String homelaboracctname,
	String homelaborlevelnm1,
	String homelaborlevelnm2,
	String homelaborlevelnm3,
	String homelaborlevelnm4,
	String homelaborlevelnm5,
	String homelaborlevelnm6,
	String homelaborlevelnm7,
	String homelaboracctdsc,
	String homelaborleveldsc1,
	String homelaborleveldsc2,
	String homelaborleveldsc3,
	String homelaborleveldsc4,
	String homelaborleveldsc5,
	String homelaborleveldsc6,
	String homelaborleveldsc7,
	Timestamp haeffectivedtm,
	Timestamp haexpirationdtm,
	Timestamp mgrsignoffthrudtm,
	int pendingsignoffsw,
	Timestamp payrollockthrudtm,
	String wageprflname,
	String grpschedname,
	String timezonename,
	String dcmdevgrpname,
	String supervisornum,
	String supervisorfullname,
	int supervisorid,
	Date currpayperiodstart,
	Date currpayperiodend,
	Date prevpayperiodstart,
	Date prevpayperiodend,
	Date nextpayperiodstart,
	Date nextpayperiodend,
	int laboracctid,
	int devicegroupid,
	int terminalruleid,
	int scheduleversion,
	Date lasttotaltime,
	Date lasttotalchngtime,
	int payruleid,
	int employeeid,
	int employmentstatid,
	String orgpathtxt,
	String orgpathdsctxt,
	Date seniorityrankdate,
	String workertypenm) {
		super();
		this.personid = personid;
		this.personnum = personnum;
		this.personfullname = personfullname;
		this.firstnm = firstnm;
		this.middleinitialnm = middleinitialnm;
		this.lastnm = lastnm;
		this.shortnm = shortnm;
		this.companyhiredtm = companyhiredtm;
		this.ftstdhrsqty = ftstdhrsqty;
		this.employmentstatus = employmentstatus;
		this.employmentstatusdt = employmentstatusdt;
		this.badgenum = badgenum;
		this.badgeeffectivedtm = badgeeffectivedtm;
		this.badgeexpirationdtm = badgeexpirationdtm;
		this.homelaboracctname = homelaboracctname;
		this.homelaborlevelnm1 = homelaborlevelnm1;
		this.homelaborlevelnm2 = homelaborlevelnm2;
		this.homelaborlevelnm3 = homelaborlevelnm3;
		this.homelaborlevelnm4 = homelaborlevelnm4;
		this.homelaborlevelnm5 = homelaborlevelnm5;
		this.homelaborlevelnm6 = homelaborlevelnm6;
		this.homelaborlevelnm7 = homelaborlevelnm7;
		this.homelaboracctdsc = homelaboracctdsc;
		this.homelaborleveldsc1 = homelaborleveldsc1;
		this.homelaborleveldsc2 = homelaborleveldsc2;
		this.homelaborleveldsc3 = homelaborleveldsc3;
		this.homelaborleveldsc4 = homelaborleveldsc4;
		this.homelaborleveldsc5 = homelaborleveldsc5;
		this.homelaborleveldsc6 = homelaborleveldsc6;
		this.homelaborleveldsc7 = homelaborleveldsc7;
		this.haeffectivedtm = haeffectivedtm;
		this.haexpirationdtm = haexpirationdtm;
		this.mgrsignoffthrudtm = mgrsignoffthrudtm;
		this.pendingsignoffsw = pendingsignoffsw;
		this.payrollockthrudtm = payrollockthrudtm;
		this.wageprflname = wageprflname;
		this.grpschedname = grpschedname;
		this.timezonename = timezonename;
		this.dcmdevgrpname = dcmdevgrpname;
		this.supervisornum = supervisornum;
		this.supervisorfullname = supervisorfullname;
		this.supervisorid = supervisorid;
		this.currpayperiodstart = currpayperiodstart;
		this.currpayperiodend = currpayperiodend;
		this.prevpayperiodstart = prevpayperiodstart;
		this.prevpayperiodend = prevpayperiodend;
		this.nextpayperiodstart = nextpayperiodstart;
		this.nextpayperiodend = nextpayperiodend;
		this.laboracctid = laboracctid;
		this.devicegroupid = devicegroupid;
		this.terminalruleid = terminalruleid;
		this.scheduleversion = scheduleversion;
		this.lasttotaltime = lasttotaltime;
		this.lasttotalchngtime = lasttotalchngtime;
		this.payruleid = payruleid;
		this.employeeid = employeeid;
		this.employmentstatid = employmentstatid;
		this.orgpathtxt = orgpathtxt;
		this.orgpathdsctxt = orgpathdsctxt;
		this.seniorityrankdate = seniorityrankdate;
		this.workertypenm = workertypenm;

		
	}

	public KronosEmployeeInfo(Integer personid) {
		this.personid = personid;
	}

	@Override
	public String toString() {
		return " KronosEmployee[" + getPersonid() + "]";
	}
	
	public static KronosEmployeeInfo build(ResultSet rs) throws SQLException {
		
			return new KronosEmployeeInfo(rs.getInt("PERSONID"), rs.getString("PERSONNUM"), rs.getString("PERSONFULLNAME"), rs.getString("FIRSTNM"),
			rs.getString("MIDDLEINITIALNM"), rs.getString("LASTNM"), rs.getString("SHORTNM"), rs.getTimestamp("COMPANYHIREDTM"),
			rs.getInt("FTSTDHRSQTY"), rs.getString("EMPLOYMENTSTATUS"), rs.getDate("EMPLOYMENTSTATUSDT"), rs.getString("BADGENUM"),
			rs.getTimestamp("BADGEEFFECTIVEDTM"), rs.getTimestamp("BADGEEXPIRATIONDTM"), rs.getString("HOMELABORACCTNAME"), 
			rs.getString("HOMELABORLEVELNM1"), rs.getString("HOMELABORLEVELNM2"), rs.getString("HOMELABORLEVELNM3"), 
			rs.getString("HOMELABORLEVELNM4"), rs.getString("HOMELABORLEVELNM5"), rs.getString("HOMELABORLEVELNM6"), 
			rs.getString("HOMELABORLEVELNM7"), rs.getString("HOMELABORACCTDSC"), rs.getString("HOMELABORLEVELDSC1"),
			rs.getString("HOMELABORLEVELDSC2"), rs.getString("HOMELABORLEVELDSC3"), rs.getString("HOMELABORLEVELDSC4"), 
			rs.getString("HOMELABORLEVELDSC5"), rs.getString("HOMELABORLEVELDSC6"), rs.getString("HOMELABORLEVELDSC7"), 
			rs.getTimestamp("HAEFFECTIVEDTM"), rs.getTimestamp("HAEXPIRATIONDTM"), rs.getTimestamp("MGRSIGNOFFTHRUDTM"),
			rs.getInt("PENDINGSIGNOFFSW"), rs.getTimestamp("PAYROLLOCKTHRUDTM"), rs.getString("WAGEPRFLNAME"), 
			rs.getString("GRPSCHEDNAME"), rs.getString("TIMEZONENAME"), rs.getString("DCMDEVGRPNAME"), rs.getString("SUPERVISORNUM"), 
			rs.getString("SUPERVISORFULLNAME"), rs.getInt("SUPERVISORID"), rs.getDate("CURRPAYPERIODSTART"), rs.getDate("CURRPAYPERIODEND"), 
			rs.getDate("PREVPAYPERIODSTART"), rs.getDate("PREVPAYPERIODEND"), rs.getDate("NEXTPAYPERIODSTART"), rs.getDate("NEXTPAYPERIODEND"),
			rs.getInt("LABORACCTID"), rs.getInt("DEVICEGROUPID"), rs.getInt("TERMINALRULEID"), rs.getInt("SCHEDULEVERSION"),
			rs.getDate("LASTTOTALTIME"), rs.getDate("LASTTOTALCHNGTIME"), rs.getInt("PAYRULEID"), rs.getInt("EMPLOYEEID"),
			rs.getInt("EMPLOYMENTSTATID"), rs.getString("ORGPATHTXT"), rs.getString("ORGPATHDSCTXT"), rs.getDate("SENIORITYRANKDATE"),
			rs.getString("WORKERTYPENM"));     		    					
			
		
	}

	public Integer getPersonid() {
		return personid;
	}

	public void setPersonid(Integer personid) {
		this.personid = personid;
	}

	public String getPersonnum() {
		return personnum;
	}

	public void setPersonnum(String personnum) {
		this.personnum = personnum;
	}

	public String getPersonfullname() {
		return personfullname;
	}

	public void setPersonfullname(String personfullname) {
		this.personfullname = personfullname;
	}

	public String getFirstnm() {
		return firstnm;
	}

	public void setFirstnm(String firstnm) {
		this.firstnm = firstnm;
	}

	public String getMiddleinitialnm() {
		return middleinitialnm;
	}

	public void setMiddleinitialnm(String middleinitialnm) {
		this.middleinitialnm = middleinitialnm;
	}

	public String getLastnm() {
		return lastnm;
	}

	public void setLastnm(String lastnm) {
		this.lastnm = lastnm;
	}

	public String getShortnm() {
		return shortnm;
	}

	public void setShortnm(String shortnm) {
		this.shortnm = shortnm;
	}

	public Timestamp getCompanyhiredtm() {
		return companyhiredtm;
	}

	public void setCompanyhiredtm(Timestamp companyhiredtm) {
		this.companyhiredtm = companyhiredtm;
	}

	public int getFtstdhrsqty() {
		return ftstdhrsqty;
	}

	public void setFtstdhrsqty(int ftstdhrsqty) {
		this.ftstdhrsqty = ftstdhrsqty;
	}

	public String getEmploymentstatus() {
		return employmentstatus;
	}

	public void setEmploymentstatus(String employmentstatus) {
		this.employmentstatus = employmentstatus;
	}

	public Date getEmploymentstatusdt() {
		return employmentstatusdt;
	}

	public void setEmploymentstatusdt(Date employmentstatusdt) {
		this.employmentstatusdt = employmentstatusdt;
	}

	public String getBadgenum() {
		return badgenum;
	}

	public void setBadgenum(String badgenum) {
		this.badgenum = badgenum;
	}

	public Timestamp getBadgeeffectivedtm() {
		return badgeeffectivedtm;
	}

	public void setBadgeeffectivedtm(Timestamp badgeeffectivedtm) {
		this.badgeeffectivedtm = badgeeffectivedtm;
	}

	public Timestamp getBadgeexpirationdtm() {
		return badgeexpirationdtm;
	}

	public void setBadgeexpirationdtm(Timestamp badgeexpirationdtm) {
		this.badgeexpirationdtm = badgeexpirationdtm;
	}

	public String getHomelaboracctname() {
		return homelaboracctname;
	}

	public void setHomelaboracctname(String homelaboracctname) {
		this.homelaboracctname = homelaboracctname;
	}

	public String getHomelaborlevelnm1() {
		return homelaborlevelnm1;
	}

	public void setHomelaborlevelnm1(String homelaborlevelnm1) {
		this.homelaborlevelnm1 = homelaborlevelnm1;
	}

	public String getHomelaborlevelnm2() {
		return homelaborlevelnm2;
	}

	public void setHomelaborlevelnm2(String homelaborlevelnm2) {
		this.homelaborlevelnm2 = homelaborlevelnm2;
	}

	public String getHomelaborlevelnm3() {
		return homelaborlevelnm3;
	}

	public void setHomelaborlevelnm3(String homelaborlevelnm3) {
		this.homelaborlevelnm3 = homelaborlevelnm3;
	}

	public String getHomelaborlevelnm4() {
		return homelaborlevelnm4;
	}

	public void setHomelaborlevelnm4(String homelaborlevelnm4) {
		this.homelaborlevelnm4 = homelaborlevelnm4;
	}

	public String getHomelaborlevelnm5() {
		return homelaborlevelnm5;
	}

	public void setHomelaborlevelnm5(String homelaborlevelnm5) {
		this.homelaborlevelnm5 = homelaborlevelnm5;
	}

	public String getHomelaborlevelnm6() {
		return homelaborlevelnm6;
	}

	public void setHomelaborlevelnm6(String homelaborlevelnm6) {
		this.homelaborlevelnm6 = homelaborlevelnm6;
	}

	public String getHomelaborlevelnm7() {
		return homelaborlevelnm7;
	}

	public void setHomelaborlevelnm7(String homelaborlevelnm7) {
		this.homelaborlevelnm7 = homelaborlevelnm7;
	}

	public String getHomelaboracctdsc() {
		return homelaboracctdsc;
	}

	public void setHomelaboracctdsc(String homelaboracctdsc) {
		this.homelaboracctdsc = homelaboracctdsc;
	}

	public String getHomelaborleveldsc1() {
		return homelaborleveldsc1;
	}

	public void setHomelaborleveldsc1(String homelaborleveldsc1) {
		this.homelaborleveldsc1 = homelaborleveldsc1;
	}

	public String getHomelaborleveldsc2() {
		return homelaborleveldsc2;
	}

	public void setHomelaborleveldsc2(String homelaborleveldsc2) {
		this.homelaborleveldsc2 = homelaborleveldsc2;
	}

	public String getHomelaborleveldsc3() {
		return homelaborleveldsc3;
	}

	public void setHomelaborleveldsc3(String homelaborleveldsc3) {
		this.homelaborleveldsc3 = homelaborleveldsc3;
	}

	public String getHomelaborleveldsc4() {
		return homelaborleveldsc4;
	}

	public void setHomelaborleveldsc4(String homelaborleveldsc4) {
		this.homelaborleveldsc4 = homelaborleveldsc4;
	}

	public String getHomelaborleveldsc5() {
		return homelaborleveldsc5;
	}

	public void setHomelaborleveldsc5(String homelaborleveldsc5) {
		this.homelaborleveldsc5 = homelaborleveldsc5;
	}

	public String getHomelaborleveldsc6() {
		return homelaborleveldsc6;
	}

	public void setHomelaborleveldsc6(String homelaborleveldsc6) {
		this.homelaborleveldsc6 = homelaborleveldsc6;
	}

	public String getHomelaborleveldsc7() {
		return homelaborleveldsc7;
	}

	public void setHomelaborleveldsc7(String homelaborleveldsc7) {
		this.homelaborleveldsc7 = homelaborleveldsc7;
	}

	public Timestamp getHaeffectivedtm() {
		return haeffectivedtm;
	}

	public void setHaeffectivedtm(Timestamp haeffectivedtm) {
		this.haeffectivedtm = haeffectivedtm;
	}

	public Timestamp getHaexpirationdtm() {
		return haexpirationdtm;
	}

	public void setHaexpirationdtm(Timestamp haexpirationdtm) {
		this.haexpirationdtm = haexpirationdtm;
	}

	public Timestamp getMgrsignoffthrudtm() {
		return mgrsignoffthrudtm;
	}

	public void setMgrsignoffthrudtm(Timestamp mgrsignoffthrudtm) {
		this.mgrsignoffthrudtm = mgrsignoffthrudtm;
	}

	public int getPendingsignoffsw() {
		return pendingsignoffsw;
	}

	public void setPendingsignoffsw(int pendingsignoffsw) {
		this.pendingsignoffsw = pendingsignoffsw;
	}

	public Timestamp getPayrollockthrudtm() {
		return payrollockthrudtm;
	}

	public void setPayrollockthrudtm(Timestamp payrollockthrudtm) {
		this.payrollockthrudtm = payrollockthrudtm;
	}

	public String getWageprflname() {
		return wageprflname;
	}

	public void setWageprflname(String wageprflname) {
		this.wageprflname = wageprflname;
	}

	public String getGrpschedname() {
		return grpschedname;
	}

	public void setGrpschedname(String grpschedname) {
		this.grpschedname = grpschedname;
	}

	public String getTimezonename() {
		return timezonename;
	}

	public void setTimezonename(String timezonename) {
		this.timezonename = timezonename;
	}

	public String getDcmdevgrpname() {
		return dcmdevgrpname;
	}

	public void setDcmdevgrpname(String dcmdevgrpname) {
		this.dcmdevgrpname = dcmdevgrpname;
	}

	public String getSupervisornum() {
		return supervisornum;
	}

	public void setSupervisornum(String supervisornum) {
		this.supervisornum = supervisornum;
	}

	public String getSupervisorfullname() {
		return supervisorfullname;
	}

	public void setSupervisorfullname(String supervisorfullname) {
		this.supervisorfullname = supervisorfullname;
	}

	public int getSupervisorid() {
		return supervisorid;
	}

	public void setSupervisorid(int supervisorid) {
		this.supervisorid = supervisorid;
	}

	public Date getCurrpayperiodstart() {
		return currpayperiodstart;
	}

	public void setCurrpayperiodstart(Date currpayperiodstart) {
		this.currpayperiodstart = currpayperiodstart;
	}

	public Date getCurrpayperiodend() {
		return currpayperiodend;
	}

	public void setCurrpayperiodend(Date currpayperiodend) {
		this.currpayperiodend = currpayperiodend;
	}

	public Date getPrevpayperiodstart() {
		return prevpayperiodstart;
	}

	public void setPrevpayperiodstart(Date prevpayperiodstart) {
		this.prevpayperiodstart = prevpayperiodstart;
	}

	public Date getPrevpayperiodend() {
		return prevpayperiodend;
	}

	public void setPrevpayperiodend(Date prevpayperiodend) {
		this.prevpayperiodend = prevpayperiodend;
	}

	public Date getNextpayperiodstart() {
		return nextpayperiodstart;
	}

	public void setNextpayperiodstart(Date nextpayperiodstart) {
		this.nextpayperiodstart = nextpayperiodstart;
	}

	public Date getNextpayperiodend() {
		return nextpayperiodend;
	}

	public void setNextpayperiodend(Date nextpayperiodend) {
		this.nextpayperiodend = nextpayperiodend;
	}

	public int getLaboracctid() {
		return laboracctid;
	}

	public void setLaboracctid(int laboracctid) {
		this.laboracctid = laboracctid;
	}

	public int getDevicegroupid() {
		return devicegroupid;
	}

	public void setDevicegroupid(int devicegroupid) {
		this.devicegroupid = devicegroupid;
	}

	public int getTerminalruleid() {
		return terminalruleid;
	}

	public void setTerminalruleid(int terminalruleid) {
		this.terminalruleid = terminalruleid;
	}

	public int getScheduleversion() {
		return scheduleversion;
	}

	public void setScheduleversion(int scheduleversion) {
		this.scheduleversion = scheduleversion;
	}

	public Date getLasttotaltime() {
		return lasttotaltime;
	}

	public void setLasttotaltime(Date lasttotaltime) {
		this.lasttotaltime = lasttotaltime;
	}

	public Date getLasttotalchngtime() {
		return lasttotalchngtime;
	}

	public void setLasttotalchngtime(Date lasttotalchngtime) {
		this.lasttotalchngtime = lasttotalchngtime;
	}

	public int getPayruleid() {
		return payruleid;
	}

	public void setPayruleid(int payruleid) {
		this.payruleid = payruleid;
	}

	public int getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(int employeeid) {
		this.employeeid = employeeid;
	}

	public int getEmploymentstatid() {
		return employmentstatid;
	}

	public void setEmploymentstatid(int employmentstatid) {
		this.employmentstatid = employmentstatid;
	}

	public String getOrgpathtxt() {
		return orgpathtxt;
	}

	public void setOrgpathtxt(String orgpathtxt) {
		this.orgpathtxt = orgpathtxt;
	}

	public String getOrgpathdsctxt() {
		return orgpathdsctxt;
	}

	public void setOrgpathdsctxt(String orgpathdsctxt) {
		this.orgpathdsctxt = orgpathdsctxt;
	}

	public Date getSeniorityrankdate() {
		return seniorityrankdate;
	}

	public void setSeniorityrankdate(Date seniorityrankdate) {
		this.seniorityrankdate = seniorityrankdate;
	}

	public String getWorkertypenm() {
		return workertypenm;
	}

	public void setWorkertypenm(String workertypenm) {
		this.workertypenm = workertypenm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((personid == null) ? 0 : personid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KronosEmployeeInfo other = (KronosEmployeeInfo) obj;
		if (personid == null) {
			if (other.personid != null)
				return false;
		} else if (!personid.equals(other.personid))
			return false;
		return true;
	}
}
