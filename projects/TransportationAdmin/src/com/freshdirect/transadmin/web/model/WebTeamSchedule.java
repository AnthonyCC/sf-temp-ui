package com.freshdirect.transadmin.web.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;

public class WebTeamSchedule implements java.io.Serializable, Comparable<WebTeamSchedule>  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ScheduleEmployeeInfo leadMasterSchedule;
	
	private List<ScheduleEmployeeInfo> memberMasterSchedule = new ArrayList<ScheduleEmployeeInfo>();
	
	private ScheduleEmployeeInfo leadSchedule;
	
	private List<ScheduleEmployeeInfo> memberSchedule = new ArrayList<ScheduleEmployeeInfo>();
			
	private WebEmployeeInfo lead;
	
	private List<WebEmployeeInfo> members = new ArrayList<WebEmployeeInfo>();
		
	public WebEmployeeInfo getLead() {
		return lead;
	}

	public void setLead(WebEmployeeInfo lead) {
		this.lead = lead;
	}

	public List<WebEmployeeInfo> getMembers() {
		return members;
	}

	public void setMembers(List<WebEmployeeInfo> members) {
		this.members = members;
	}

	public ScheduleEmployeeInfo getLeadMasterSchedule() {
		return leadMasterSchedule;
	}

	public void setLeadMasterSchedule(ScheduleEmployeeInfo leadMasterSchedule) {
		this.leadMasterSchedule = leadMasterSchedule;
	}

	public List<ScheduleEmployeeInfo> getMemberMasterSchedule() {
		return memberMasterSchedule;
	}

	public void setMemberMasterSchedule(
			List<ScheduleEmployeeInfo> memberMasterSchedule) {
		this.memberMasterSchedule = memberMasterSchedule;
	}

	public ScheduleEmployeeInfo getLeadSchedule() {
		return leadSchedule;
	}

	public void setLeadSchedule(ScheduleEmployeeInfo leadSchedule) {
		this.leadSchedule = leadSchedule;
	}

	public List<ScheduleEmployeeInfo> getMemberSchedule() {
		return memberSchedule;
	}

	public void setMemberSchedule(List<ScheduleEmployeeInfo> memberSchedule) {
		this.memberSchedule = memberSchedule;
	}

	public Boolean getIsMasterMatching() {
		
		List<ScheduleEmployeeInfo> schedule = new ArrayList<ScheduleEmployeeInfo>();
		schedule.add(this.getLeadMasterSchedule());
		schedule.addAll(this.getMemberMasterSchedule());
		return isSameSchedule(schedule);
	}

	public Boolean getIsCurrentMatching() {
		
		List<ScheduleEmployeeInfo> schedule = new ArrayList<ScheduleEmployeeInfo>();
		schedule.add(this.getLeadSchedule());
		schedule.addAll(this.getMemberSchedule());
		return isSameSchedule(schedule);
	}
	
	private boolean isSameSchedule(List<ScheduleEmployeeInfo> schedule) {
		if(schedule != null) {
			int size = schedule.size();
			for(int intCount = 0; intCount < size; intCount++) {
				for(int intCountChild = intCount+1; intCountChild < size; intCountChild++) {
					if(schedule.get(intCount) == null 
							|| schedule.get(intCountChild) == null 
								|| !schedule.get(intCount).isSameSchedule(schedule.get(intCountChild))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public int compareTo(WebTeamSchedule o) {
		// TODO Auto-generated method stub
		return (lead != null && o.getLead() != null ?  lead.compareTo(o.getLead()) : 0);
	}
	
}
