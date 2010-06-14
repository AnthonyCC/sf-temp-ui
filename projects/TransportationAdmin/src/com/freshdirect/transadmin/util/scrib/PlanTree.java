package com.freshdirect.transadmin.util.scrib;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeTeam;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

public class PlanTree {
	
	Map<Date, DateNode> d = new HashMap<Date, DateNode>();
	
	Map<String, String> employeeToLead;
	
	Set<String> leads = new HashSet<String>();
	
	private static final Logger logger = Logger.getLogger(PlanTree.class);

	public void prepare(Collection scribs) {
		for (Iterator i = scribs.iterator(); i.hasNext();) {
			Object next = i.next();
			Date key;
			if (next instanceof Scrib) {
				Scrib s = (Scrib) next;
				key = s.getScribDate();
				DateNode value = (DateNode) d.get(key);
				if (value == null) {
					value = new DateNode(this);
					d.put(key, value);
				}
				value.prepare(s);
			} else if (next instanceof ScheduleEmployeeDetails) {
				ScheduleEmployeeDetails s = (ScheduleEmployeeDetails) next;
				key = s.getDate();
				DateNode value = (DateNode) d.get(key);
				if (value == null) {
					value = new DateNode(this);
					d.put(key, value);
				}
				value.prepare(s);
			}

		}
	}
	
	public void prepareTeam(Collection<EmployeeTeam> teams) {
		
		if(teams != null) {
			employeeToLead = ModelUtil.getIdMappedTeam(teams);
			leads.addAll(employeeToLead.values());
		}
	}

	public Collection getPlan() {
		Collection result = new ArrayList();
		for (Iterator i = d.values().iterator(); i.hasNext();) {
			DateNode value = (DateNode) i.next();
			result.addAll(value.getPlan());
		}
		/*System.out.println(" ========================= PRINT PLANTREE START ==============================");
		System.out.println(this.toString());
		System.out.println(" ========================= PRINT PLANTREE END ==============================");*/
		return result;
	}
	
	public Map<String, String> getEmployeeToLead() {
		return employeeToLead;
	}

	public void setEmployeeToLead(Map<String, String> employeeToLead) {
		this.employeeToLead = employeeToLead;
	}

	public Set<String> getLeads() {
		return leads;
	}

	public void setLeads(Set<String> leads) {
		this.leads = leads;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n");
		try {
			for (Map.Entry<Date, DateNode> entry : d.entrySet()) {
				Date key = entry.getKey();
				DateNode value = entry.getValue();
				strBuf.append(TransStringUtil.getDate(key)).append(" -> ").append(value.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
}

class DateNode extends PlanTreeNode {
	public DateNode(PlanTree tree) {
		super(tree);
		// TODO Auto-generated constructor stub
	}

	Date date;
	Map<String, RegionNode> regions = new HashMap<String, RegionNode>();
	Map<String, DepotNode> depot = new HashMap<String, DepotNode>();

	public void prepare(Scrib s) {
		String key = s.getRegion().getCode();
		if (ScheduleEmployeeInfo.DEPOT.equalsIgnoreCase(key)) {
			DepotNode value = (DepotNode) depot.get(key);
			if (value == null) {
				value = new DepotNode(this.getTree());
				depot.put(key, value);
			}
			value.prepare(s);
		} else {
			RegionNode value = (RegionNode) regions.get(key);
			if (value == null) {
				value = new RegionNode(this.getTree());
				regions.put(key, value);
			}
			value.prepare(s);
		}
	}

	public void prepare(ScheduleEmployeeDetails s) {
		String key = s.getSchedule().getRegion().getCode();
		if (ScheduleEmployeeInfo.DEPOT.equalsIgnoreCase(key)) {
			if (s.getSchedule().getDepotZone() != null) {
				DepotNode value = (DepotNode) depot.get(key);
				if (value == null) {
					value = new DepotNode(this.getTree());
					depot.put(key, value);
				}
				value.prepare(s);
			} else {
				RegionNode value = (RegionNode) regions.get(key);
				if (value == null) {
					value = new RegionNode(this.getTree());
					regions.put(key, value);
				}
				value.prepare(s);
			}
		} else {
			RegionNode value = (RegionNode) regions.get(key);
			if (value == null) {
				value = new RegionNode(this.getTree());
				regions.put(key, value);
			}
			value.prepare(s);
		}
	}

	public Collection getPlan() {
		Collection result = new ArrayList();
		for (Iterator i = regions.values().iterator(); i.hasNext();) {
			RegionNode value = (RegionNode) i.next();
			result.addAll(value.getPlan());
		}
		for (Iterator i = depot.values().iterator(); i.hasNext();) {
			DepotNode value = (DepotNode) i.next();
			result.addAll(value.getPlan());
		}
		return result;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t");
		try {
			for (Map.Entry<String, RegionNode> entry : regions.entrySet()) {
				String key = entry.getKey();
				RegionNode value = entry.getValue();
				strBuf.append("\n\t").append(key).append(" -> ").append(value.toString());
			}
			for (Map.Entry<String, DepotNode> entry : depot.entrySet()) {
				String key = entry.getKey();
				DepotNode value = entry.getValue();
				strBuf.append("\n\t").append(key).append(" -> ").append(value.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
}

class RegionNode extends PlanTreeNode  {
	public RegionNode(PlanTree tree) {
		super(tree);
		// TODO Auto-generated constructor stub
	}

	Map<Date, TimeNode> times = new HashMap<Date, TimeNode>();

	public void prepare(Scrib s) {
		Date key = s.getStartTime();
		TimeNode value = (TimeNode) times.get(key);
		if (value == null) {
			value = new TimeNode(this.getTree());
			times.put(key, value);
		}
		value.prepare(s);

	}

	public void prepare(ScheduleEmployeeDetails s) {
		Date key = s.getSchedule().getTime();
		TimeNode value = (TimeNode) times.get(key);
		if (value == null) {
			value = new TimeNode(this.getTree());
			times.put(key, value);
		}
		value.prepare(s);

	}

	public Collection getPlan() {
		Collection result = new ArrayList();
		for (Iterator i = times.values().iterator(); i.hasNext();) {
			TimeNode value = (TimeNode) i.next();
			result.addAll(value.getPlan());
		}
		return result;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		//strBuf.append("\n\t\t");
		try {
			for (Map.Entry<Date, TimeNode> entry : times.entrySet()) {
				Date key = entry.getKey();
				TimeNode value = entry.getValue();
				strBuf.append("\n\t\t").append(TransStringUtil.getTime(key)).append(" -> ").append(value.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
}

class DepotNode extends PlanTreeNode  {
	public DepotNode(PlanTree tree) {
		super(tree);
		// TODO Auto-generated constructor stub
	}

	Map<String, ZoneNode> zones = new HashMap<String, ZoneNode>();

	public void prepare(Scrib s) {
		String key = s.getZone().getZoneCode();
		ZoneNode value = (ZoneNode) zones.get(key);
		if (value == null) {
			value = new ZoneNode(this.getTree());
			zones.put(key, value);
		}
		value.prepare(s);
	}

	public void prepare(ScheduleEmployeeDetails s) {
		String key = s.getSchedule().getDepotZone().getZoneCode();
		ZoneNode value = (ZoneNode) zones.get(key);
		if (value == null) {
			value = new ZoneNode(this.getTree());
			zones.put(key, value);
		}
		value.prepare(s);
	}

	public Collection getPlan() {
		Collection result = new ArrayList();
		for (Iterator i = zones.values().iterator(); i.hasNext();) {
			ZoneNode value = (ZoneNode) i.next();
			result.addAll(value.getPlan());
		}
		return result;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t\t");
		try {
			for (Map.Entry<String, ZoneNode> entry : zones.entrySet()) {
				String key = entry.getKey();
				ZoneNode value = entry.getValue();
				strBuf.append("\n\t\t").append(key).append(" -> ").append(value.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
}

class ZoneNode  extends PlanTreeNode {
	public ZoneNode(PlanTree tree) {
		super(tree);
		// TODO Auto-generated constructor stub
	}

	Map<Date, DepotTimeNode> times = new HashMap<Date, DepotTimeNode>();
	Map<Date, List<ScheduleEmployeeDetails>> runners = new HashMap<Date, List<ScheduleEmployeeDetails>>();

	public void prepare(Scrib s) {
		Date key = s.getStartTime();
		DepotTimeNode value = (DepotTimeNode) times.get(key);
		if (value == null) {
			value = new DepotTimeNode(this.getTree(), this);
			times.put(key, value);
		}
		value.prepare(s);
	}

	public void prepare(ScheduleEmployeeDetails s) {
		if (TreeDataUtil.isRole(ScheduleEmployeeInfo.RUNNER, s.getEmpRoles())) {
			Date key = s.getSchedule().getTime();
			List value = (List) runners.get(key);
			if (value == null) {
				value = new ArrayList();
				runners.put(key, value);
			}
			value.add(s);
		} else {
			Date key = s.getSchedule().getTime();
			DepotTimeNode value = (DepotTimeNode) times.get(key);
			if (value == null) {
				value = new DepotTimeNode(this.getTree(), this);
				times.put(key, value);
			}
			value.prepare(s);
		}
	}

	public Collection getPlan() {
		Collection result = new ArrayList();
		for (Iterator i = times.values().iterator(); i.hasNext();) {
			DepotTimeNode value = (DepotTimeNode) i.next();
			result.addAll(value.getPlan());
		}
		return result;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		//strBuf.append("\n\t\t\t");
		try {
			for (Map.Entry<Date, DepotTimeNode> entry : times.entrySet()) {
				Date key = entry.getKey();
				DepotTimeNode value = entry.getValue();
				strBuf.append("\n\t\t\t").append(TransStringUtil.getTime(key)).append(" -> ").append(value.toString());
			}
			for (Map.Entry<Date, List<ScheduleEmployeeDetails>> entry : runners.entrySet()) {
				Date key = entry.getKey();
				List<ScheduleEmployeeDetails> value = entry.getValue();
				strBuf.append("\n\t\t\t").append(TransStringUtil.getTime(key)).append(" -> ").append(value.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
}

class TimeNode extends PlanTreeNode  {
	public TimeNode(PlanTree tree) {
		super(tree);
		// TODO Auto-generated constructor stub
	}

	List<TruckNode> trucks = new ArrayList<TruckNode>();
	List<ScheduleEmployeeDetails> employees = new ArrayList<ScheduleEmployeeDetails>();
	List<Plan> plans = new ArrayList<Plan>();
	Set<ScheduleEmployeeDetails> resourceTeams = null;
	
	public Collection getPlan() {
		Collections.sort(trucks, new ZoneComparator());
		resourceTeams  = TreeDataUtil.teamUp(this.getTree(), employees);		
		assemble(resourceTeams);
		return plans;
	}

	public void prepare(Scrib s) {
		int n = s.getCount();
		for (int i = 0; i < n; i++) {
			TruckNode t = new TruckNode(this.getTree());
			trucks.add(t);
			t.prepare(s);
		}
	}

	public void prepare(ScheduleEmployeeDetails s) {
		employees.add(s);
	}

	public void assemble(Set<ScheduleEmployeeDetails> resourcesIn) {
		
		int rank = 1;
		List<ScheduleEmployeeDetails> resources = new ArrayList<ScheduleEmployeeDetails>();
		resources.addAll(resourcesIn);
		Collections.sort(resources, new HireDateComparator());
				
		for (Iterator i = trucks.iterator(); i.hasNext();) {
			Scrib s = ((TruckNode) i.next()).s;

			Plan p = new Plan();
			plans.add(p);
			p.setPlanDate(s.getScribDate());
			p.setZone(s.getZone());
			p.setRegion(s.getRegion());
			p.setStartTime(s.getStartTime());
			p.setFirstDeliveryTime(s.getFirstDlvTime());
			p.setSupervisorId(s.getSupervisorCode());
			p.setMaxTime(s.getMaxTime());
			p.setSequence(rank++);
			if (s.getZone().getTrnZoneType() == null) {
				continue;
			}
			Set zoneTypeResources = s.getZone().getTrnZoneType().getZonetypeResources();
			
			ScheduleEmployeeDetails _currResource = null;
			PlanResource _tmpPlanResource = null;
			if(resources.size() > 0) {
				_currResource = (ScheduleEmployeeDetails)resources.toArray()[0];
				if(_currResource.getMembers() != null && _currResource.getMembers().size() > 0) {
					
					for(ScheduleEmployeeDetails _members : _currResource.getMembers()) {
						_tmpPlanResource = TreeDataUtil.matchResource(_members);
						if(_tmpPlanResource != null) {
							p.getPlanResources().add(_tmpPlanResource);							
						} 
					}
					resources.remove(_currResource);					
				} else {
					
					for (Iterator j = zoneTypeResources.iterator(); j.hasNext();) {
						ZonetypeResource r = (ZonetypeResource) j.next();
						// Driver
						if (ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r.getId()
								.getRole())) {
							int min = r.getRequiredNo().intValue();
							int count = 0;
							for (Iterator k = resources.iterator(); k.hasNext();) {
								ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
								if(se.getMembers() == null || se.getMembers().size() == 0) {
									if (count >= min)
										break;
									
									_tmpPlanResource = TreeDataUtil.matchResource(se, ScheduleEmployeeInfo.DRIVER);
									if(_tmpPlanResource != null) {
										p.getPlanResources().add(_tmpPlanResource);
										k.remove();
										count++;
									}
								}
							}
						}

						// Helper
						if (ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r.getId()
								.getRole())) {
							int min = r.getRequiredNo().intValue();
							int count = 0;
							for (Iterator k = resources.iterator(); k.hasNext();) {
								ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
								if(se.getMembers() == null || se.getMembers().size() == 0) {
									if (count >= min)
										break;
									
									_tmpPlanResource = TreeDataUtil.matchResource(se, ScheduleEmployeeInfo.HELPER);
									if(_tmpPlanResource != null) {
										p.getPlanResources().add(_tmpPlanResource);
										k.remove();
										count++;
									}
								}
							}
						}

						// Runner
						if (ScheduleEmployeeInfo.RUNNER.equalsIgnoreCase(r.getId()
								.getRole())) {
							int min = r.getRequiredNo().intValue();
							int count = 0;
							for (Iterator k = resources.iterator(); k.hasNext();) {
								ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
								if(se.getMembers() == null || se.getMembers().size() == 0) {
									if (count >= min)
										break;
									
									_tmpPlanResource = TreeDataUtil.matchResource(se, ScheduleEmployeeInfo.RUNNER);
									if(_tmpPlanResource != null) {
										p.getPlanResources().add(_tmpPlanResource);
										k.remove();
										count++;
									}
								}
							}
						}
					}
				}
			}
			
			p.setOpen(TreeDataUtil.isOpen(p, zoneTypeResources));
		}

		// add bullpen for remaining employees

		if (resources != null && resources.size() > 0) {
			ScheduleEmployeeDetails s = (ScheduleEmployeeDetails) resources.toArray()[0];
			Plan p = new Plan();
			plans.add(p);
			p.setPlanDate(s.getDate());

			p.setRegion(s.getSchedule().getRegion());
			p.setStartTime(s.getSchedule().getTime());
			p.setFirstDeliveryTime(s.getSchedule().getTime());
			p.setIsBullpen("Y");
			for (Iterator i = resources.iterator(); i.hasNext();) {
				ScheduleEmployeeDetails ss = (ScheduleEmployeeDetails) i.next();
				PlanResource planResource = new PlanResource();
				EmployeeRoleType type = new EmployeeRoleType();
				for (Iterator si = ss.getEmpRoles().iterator(); si.hasNext();) {
					type.setCode(((EmployeeRole) (si.next())).getId().getRole());
					break;
				}
				ResourceId resource = new ResourceId();
				resource.setResourceId(ss.info.getEmployeeId());
				planResource.setEmployeeRoleType(type);
				planResource.setId(resource);
				p.getPlanResources().add(planResource);

			}
		}
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		//strBuf.append("\n\t\t\t");
		strBuf.append("TRUCKS == ").append(trucks);
		strBuf.append("EMPLOYEES == ").append(resourceTeams);
		return strBuf.toString();
	}
}

class DepotTimeNode extends PlanTreeNode  {
	List<TruckNode> trucks = new ArrayList<TruckNode>();
	List<ScheduleEmployeeDetails> employees = new ArrayList<ScheduleEmployeeDetails>();
	List<Plan> plans = new ArrayList<Plan>();
	Set<ScheduleEmployeeDetails> resourceTeams = null;
	ZoneNode parent;

	public DepotTimeNode(PlanTree tree, ZoneNode parent) {
		super(tree);
		this.parent = parent;
	}

	public Collection getPlan() {
		Collections.sort(trucks, new ZoneComparator());
		resourceTeams = TreeDataUtil.teamUp(this.getTree(), employees);
		assemble(resourceTeams);
		return plans;
	}

	public void prepare(Scrib s) {
		int n = s.getCount();
		for (int i = 0; i < n; i++) {
			TruckNode t = new TruckNode(this.getTree());
			trucks.add(t);
			t.prepare(s);
		}
	}

	public void prepare(ScheduleEmployeeDetails s) {
		employees.add(s);
	}

	public void assemble(Set<ScheduleEmployeeDetails> resourcesIn) {
		int rank = 1;
		List<ScheduleEmployeeDetails> resources = new ArrayList<ScheduleEmployeeDetails>();
		resources.addAll(resourcesIn);
		Collections.sort(resources, new HireDateComparator());
		for (Iterator i = trucks.iterator(); i.hasNext();) {
			Scrib s = ((TruckNode) i.next()).s;

			Plan p = new Plan();
			plans.add(p);
			p.setPlanDate(s.getScribDate());
			p.setZone(s.getZone());
			p.setRegion(s.getRegion());
			p.setStartTime(s.getStartTime());
			p.setFirstDeliveryTime(s.getFirstDlvTime());
			p.setSupervisorId(s.getSupervisorCode());
			p.setMaxTime(s.getMaxTime());
			p.setSequence(rank++);
			if (s.getZone().getTrnZoneType() == null)
				continue;
			Set zoneTypeResources = s.getZone().getTrnZoneType().getZonetypeResources();
			ScheduleEmployeeDetails _currResource = null;
			PlanResource _tmpPlanResource = null;
			if(resources.size() > 0) {
				_currResource = (ScheduleEmployeeDetails)resources.toArray()[0];
				if(_currResource.getMembers() != null && _currResource.getMembers().size() > 0) {
					
					for(ScheduleEmployeeDetails _members : _currResource.getMembers()) {
						_tmpPlanResource = TreeDataUtil.matchResource(_members);
						if(_tmpPlanResource != null) {
							p.getPlanResources().add(_tmpPlanResource);							
						}
					}
					resources.remove(_currResource);
				} else {
					for (Iterator j = zoneTypeResources.iterator(); j.hasNext();) {
						ZonetypeResource r = (ZonetypeResource) j.next();
						// Driver
						if (ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r.getId()
								.getRole())) {
							int min = r.getRequiredNo().intValue();
							int count = 0;
							for (Iterator k = resources.iterator(); k.hasNext();) {
								ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
								if(se.getMembers() == null || se.getMembers().size() == 0) {
									if (count >= min)
										break;
									
									_tmpPlanResource = TreeDataUtil.matchResource(se, ScheduleEmployeeInfo.DRIVER);
									if(_tmpPlanResource != null) {
										p.getPlanResources().add(_tmpPlanResource);
										k.remove();
										count++;
									}
								}
							}
						}
		
						// Helper
						if (ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r.getId()
								.getRole())) {
							int min = r.getRequiredNo().intValue();
							int count = 0;
							for (Iterator k = resources.iterator(); k.hasNext();) {
								ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
								if(se.getMembers() == null || se.getMembers().size() == 0) {
									if (count >= min)
										break;
									
									_tmpPlanResource = TreeDataUtil.matchResource(se, ScheduleEmployeeInfo.HELPER);
									if(_tmpPlanResource != null) {
										p.getPlanResources().add(_tmpPlanResource);
										k.remove();
										count++;
									}
								}
							}
						}
						p.setOpen(TreeDataUtil.isOpen(p, zoneTypeResources));
					}
					// Runner[add all the runners available]
					List runners = getRunners(s.getFirstDlvTime());
					if (runners != null) {
						for (Iterator k = runners.iterator(); k.hasNext();) {
							ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k
									.next();
							PlanResource planResource = new PlanResource();
							EmployeeRoleType type = new EmployeeRoleType();
							type.setCode(ScheduleEmployeeInfo.RUNNER);
							ResourceId resource = new ResourceId();
							resource.setResourceId(se.info.getEmployeeId());
							planResource.setEmployeeRoleType(type);
							planResource.setId(resource);
							p.getPlanResources().add(planResource);
						}
					}
				}
			}
		}
		
		// add bullpen for remaining employees
		if (resources != null && resources.size() > 0) {
			ScheduleEmployeeDetails s = (ScheduleEmployeeDetails) resources.toArray()[0];
			Plan p = new Plan();
			plans.add(p);
			p.setPlanDate(s.getDate());

			p.setRegion(s.getSchedule().getRegion());
			p.setStartTime(s.getSchedule().getTime());
			p.setFirstDeliveryTime(s.getSchedule().getTime());
			p.setIsBullpen("Y");
			for (Iterator i = resources.iterator(); i.hasNext();) {
				ScheduleEmployeeDetails ss = (ScheduleEmployeeDetails) i.next();
				Collection c = ss.getEmpRoles();
				if (!TreeDataUtil.isRole(ScheduleEmployeeInfo.RUNNER, c)) {
					PlanResource planResource = new PlanResource();
					EmployeeRoleType type = new EmployeeRoleType();
					for (Iterator si = ss.getEmpRoles().iterator(); si
							.hasNext();) {
						type.setCode(((EmployeeRole) (si.next())).getId()
								.getRole());
						break;
					}
					ResourceId resource = new ResourceId();
					resource.setResourceId(ss.info.getEmployeeId());
					planResource.setEmployeeRoleType(type);
					planResource.setId(resource);
					p.getPlanResources().add(planResource);
				}
			}
		}
	}

	

	public List getRunners(Date time) {
		Date key = null;
		Map r = this.parent.runners;
		for (Iterator i = r.keySet().iterator(); i.hasNext();) {
			Date temp = (Date) i.next();
			if (time.getTime() >= temp.getTime()) {
				if (key != null) {
					if (key.getTime() < temp.getTime())
						key = temp;
				} else {
					key = temp;
				}
			}
		}
		return (List) r.get(key);

	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		//strBuf.append("\n\t\t\t");
		strBuf.append("TRUCKS == ").append(trucks);
		strBuf.append("EMPLOYEES == ").append(resourceTeams);
		return strBuf.toString();
	}
}

class TruckNode extends PlanTreeNode {
	public TruckNode(PlanTree tree) {
		super(tree);
		// TODO Auto-generated constructor stub
	}

	Scrib s;

	public void prepare(Scrib s) {
		this.s = s;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
	//	strBuf.append("\n\t\t\t\t");
		strBuf.append(s.getCount());		
		return strBuf.toString();
	}
}

class ZoneComparator implements Comparator {

	public int compare(Object obj, Object obj1) {
		if (obj instanceof Scrib && obj1 instanceof Scrib) {
			Scrib s1 = (Scrib) obj;
			Scrib s2 = (Scrib) obj1;
			int z1 = 0;
			int z2 = 0;
			if (s1.getZone() != null && s1.getZone().getPriority() != null)
				z1 = s1.getZone().getPriority().intValue();
			if (s2.getZone() != null && s2.getZone().getPriority() != null)
				z2 = s2.getZone().getPriority().intValue();
			return z2 - z1;
		}
		return 0;
	}

}

class HireDateComparator implements Comparator {

	public int compare(Object obj, Object obj1) {
		if (obj instanceof ScheduleEmployeeDetails
				&& obj1 instanceof ScheduleEmployeeDetails) {
			ScheduleEmployeeDetails s1 = (ScheduleEmployeeDetails) obj;
			ScheduleEmployeeDetails s2 = (ScheduleEmployeeDetails) obj1;
			long z1 = 0;
			long z2 = 0;
			if (s1.getInfo() != null && s1.getInfo().getHireDate() != null)
				z1 = s1.getInfo().getHireDate().getTime();
			if (s2.getInfo() != null && s2.getInfo().getHireDate() != null)
				z2 = s2.getInfo().getHireDate().getTime();
			int result = -1;
			if (z1 > z2)
				result = 1;
			return result;
		}
		return 0;
	}

}

class TreeDataUtil {
	
	private static final Logger logger = Logger.getLogger(TreeDataUtil.class);
	
	public static String isOpen(Plan p, Set resources) {

		int driverMin = 0;
		int helperMin = 0;
		int runnerMin = 0;
		int drivers = 0;
		int helpers = 0;
		int runners = 0;

		try {
			if (resources != null)
				for (Iterator j = resources.iterator(); j.hasNext();) {
					ZonetypeResource r = (ZonetypeResource) j.next();
					// Driver
					if (r != null && r.getId() != null) {
						if (ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r
								.getId().getRole())) {
							driverMin = r.getRequiredNo().intValue();
						}
						// Helper
						if (ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r
								.getId().getRole())) {
							helperMin = r.getRequiredNo().intValue();
						}
						// Runner
						if (ScheduleEmployeeInfo.RUNNER.equalsIgnoreCase(r
								.getId().getRole())) {
							runnerMin = r.getRequiredNo().intValue();
						}
					}
				}
			for (Iterator j = p.getPlanResources().iterator(); j.hasNext();) {
				PlanResource r = (PlanResource) j.next();
				if (r != null && r.getEmployeeRoleType() != null) {
					// Driver
					if (ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r
							.getEmployeeRoleType().getCode())) {
						drivers++;
					}
					// Helper
					if (ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r
							.getEmployeeRoleType().getCode())) {
						helpers++;
					}
					// Runner
					if (ScheduleEmployeeInfo.RUNNER.equalsIgnoreCase(r
							.getEmployeeRoleType().getCode())) {
						runners++;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (drivers < driverMin || helpers < helperMin || runners < runnerMin) {
			return "Y";
		}
		return null;

	}
	
	public static boolean isRole(String role, Collection c) {
		boolean result = false;
		for (Iterator k = c.iterator(); k.hasNext();) {
			EmployeeRole e = (EmployeeRole) k.next();
			if (role.equalsIgnoreCase(e.getId().getRole())) {
				return true;
			}
		}
		return result;
	}
	
	public static Set<ScheduleEmployeeDetails> teamUp(PlanTree tree, List<ScheduleEmployeeDetails> employees) {
		
		Set<ScheduleEmployeeDetails> resources = new HashSet<ScheduleEmployeeDetails>();
		
		List<ScheduleEmployeeDetails> memberEmployees = new ArrayList<ScheduleEmployeeDetails>();
		
		Map<String, ScheduleEmployeeDetails> currentLeads = new HashMap<String, ScheduleEmployeeDetails>();
		
		if(employees != null) {			
			// Collect Leads
			Iterator<ScheduleEmployeeDetails> _rowItr = employees.iterator();
			ScheduleEmployeeDetails _row;
			while(_rowItr.hasNext()) {
				_row = _rowItr.next();
				if(tree.getLeads().contains(_row.getInfo().getEmployeeId())) {
					currentLeads.put(_row.getInfo().getEmployeeId(), _row);
					_row.addMember(_row);
					resources.add(_row);
				} else {
					memberEmployees.add(_row);
				}
			}
			_rowItr = memberEmployees.iterator();
			String leadId;
			// Match members and orphans
			while(_rowItr.hasNext()) {
				_row = _rowItr.next();
				leadId = tree.getEmployeeToLead().get(_row.getInfo().getEmployeeId());
				if(leadId != null && currentLeads.containsKey(leadId)) {
					currentLeads.get(leadId).addMember(_row);					
				} else {
					if(leadId != null && !currentLeads.containsKey(leadId)) {						
						logger.warn(employees+" -> "+"Team Mismatch Lead ID>"+leadId+" Member> "+_row.getInfo().getEmployeeId()+" LEADS> "+currentLeads.keySet());
					}
					resources.add(_row);
				}
			}
		}
		
		return resources;
	}
	
	public static PlanResource matchResource(ScheduleEmployeeDetails se) {
		
		Collection c = se.getEmpRoles();
		PlanResource planResource = null;
		if (TreeDataUtil.isRole(ScheduleEmployeeInfo.DRIVER, c)) {
			planResource = new PlanResource();
			EmployeeRoleType type = new EmployeeRoleType();
			type.setCode(ScheduleEmployeeInfo.DRIVER);
			ResourceId resource = new ResourceId();
			resource.setResourceId(se.info.getEmployeeId());
			planResource.setEmployeeRoleType(type);
			planResource.setId(resource);
			
		} else if (TreeDataUtil.isRole(ScheduleEmployeeInfo.HELPER, c)) {
			planResource = new PlanResource();
			EmployeeRoleType type = new EmployeeRoleType();
			type.setCode(ScheduleEmployeeInfo.HELPER);
			ResourceId resource = new ResourceId();
			resource.setResourceId(se.info.getEmployeeId());
			planResource.setEmployeeRoleType(type);
			planResource.setId(resource);
			
		} else if (TreeDataUtil.isRole(ScheduleEmployeeInfo.RUNNER, c)) {
			planResource = new PlanResource();
			EmployeeRoleType type = new EmployeeRoleType();
			type.setCode(ScheduleEmployeeInfo.RUNNER);
			ResourceId resource = new ResourceId();
			resource.setResourceId(se.info.getEmployeeId());
			planResource.setEmployeeRoleType(type);
			planResource.setId(resource);
		}
		
		return planResource;
	}
	
	public static PlanResource matchResource(ScheduleEmployeeDetails se, String role) {
		
		Collection c = se.getEmpRoles();
		if (TreeDataUtil.isRole(role, c)) {
			PlanResource planResource = new PlanResource();
			EmployeeRoleType type = new EmployeeRoleType();
			type.setCode(role);
			ResourceId resource = new ResourceId();
			resource.setResourceId(se.info.getEmployeeId());
			planResource.setEmployeeRoleType(type);
			planResource.setId(resource);
			return planResource;
		} 		
		return null;
	}
	
}


class PlanTreeNode {
	
	PlanTree tree;

	public PlanTreeNode(PlanTree tree) {
		super();
		this.tree = tree;
	}

	public PlanTree getTree() {
		return tree;
	}

	public void setTree(PlanTree tree) {
		this.tree = tree;
	}
}