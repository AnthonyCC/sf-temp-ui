package com.freshdirect.transadmin.util.scrib;

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

import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.transadmin.model.EmployeeRole;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.EmployeeTeam;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.util.EnumResourceSubType;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

@SuppressWarnings({ "unchecked", "rawtypes" })
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
		//System.out.println(" ========================= PRINT PLANTREE START ==============================");
		//System.out.println(this.toString());
		//System.out.println(" ========================= PRINT PLANTREE END ==============================");
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

@SuppressWarnings({ "unchecked", "rawtypes" })
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
		if (ScheduleEmployeeInfo.DEPOT.equalsIgnoreCase(key) && s.getZone() != null) {
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
		if (ScheduleEmployeeInfo.DEPOT.equalsIgnoreCase(key) && !TreeDataUtil.isTrailerRole(s.getEmpRoles())) {
			if (s.getSchedule().getDepotFacility() != null) {
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

@SuppressWarnings({ "unchecked", "rawtypes" })
class RegionNode extends PlanTreeNode  {
	public RegionNode(PlanTree tree) {
		super(tree);
		// TODO Auto-generated constructor stub
	}

	Map<Date, TimeNode> times = new HashMap<Date, TimeNode>();

	public void prepare(Scrib s) {
		Date key = s.getDispatchGroup();
		TimeNode value = (TimeNode) times.get(key);
		if (value == null) {
			value = new TimeNode(this.getTree());
			times.put(key, value);
		}
		value.prepare(s);

	}

	public void prepare(ScheduleEmployeeDetails s) {
		Date key = s.getSchedule().getDispatchGroupTime();
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

@SuppressWarnings({ "unchecked", "rawtypes" })
class DepotNode extends PlanTreeNode  {

	public DepotNode(PlanTree tree) {
		super(tree);
	}

	Map<String, DepotOriginFacilityNode> originFacilitys = new HashMap<String, DepotOriginFacilityNode>();
	Map<String, DepotDestFacilityNode> destFacilitys = new HashMap<String, DepotDestFacilityNode>();

	public void prepare(Scrib s) {
		String key = null; 
		if(s.getDestinationFacility() != null &&
			EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(s.getDestinationFacility().getTrnFacilityType().getName())){
			key = s.getDestinationFacility().getFacilityId();
			DepotDestFacilityNode value = (DepotDestFacilityNode) destFacilitys.get(key);
			if (value == null) {
				value = new DepotDestFacilityNode(this.getTree());
				destFacilitys.put(key, value);
			}
			value.prepare(s);
		} else {
			key = s.getOriginFacility().getFacilityId();
			DepotOriginFacilityNode value = (DepotOriginFacilityNode) originFacilitys.get(key);
			if (value == null) {
				value = new DepotOriginFacilityNode(this.getTree());
				originFacilitys.put(key, value);
			}
			value.prepare(s);
		}
	}

	public void prepare(ScheduleEmployeeDetails s) {
		String key = (s.getSchedule() != null && s.getSchedule().getDepotFacility()!= null) ? s.getSchedule().getDepotFacility().getFacilityId() : null;
		if(TreeDataUtil.matchResource(s, ScheduleEmployeeInfo.RUNNER) == null) {				
				DepotDestFacilityNode value = (DepotDestFacilityNode) destFacilitys.get(key);
				if (value == null) {
					value = new DepotDestFacilityNode(this.getTree());
					destFacilitys.put(key, value);
				}
				value.prepare(s);
		} else {				
				DepotOriginFacilityNode value = (DepotOriginFacilityNode) originFacilitys.get(key);
				if (value == null) {
					value = new DepotOriginFacilityNode(this.getTree());
					originFacilitys.put(key, value);
				}
				value.prepare(s);
		}
	}

	public Collection getPlan() {
		Collection result = new ArrayList();
		for (Iterator i = destFacilitys.values().iterator(); i.hasNext();) {
			DepotDestFacilityNode value = (DepotDestFacilityNode) i.next();
			result.addAll(value.getPlan());
		}
		for (Iterator i = originFacilitys.values().iterator(); i.hasNext();) {
			DepotOriginFacilityNode value = (DepotOriginFacilityNode) i.next();
			result.addAll(value.getPlan());
		}
		return result;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("\n\t\t");
		try {
			for (Map.Entry<String, DepotDestFacilityNode> entry : destFacilitys.entrySet()) {
				String key = entry.getKey();
				DepotDestFacilityNode value = entry.getValue();
				strBuf.append("\n\t\t").append(key).append(" -> ").append(value.toString());
			}
			
			for (Map.Entry<String, DepotOriginFacilityNode> entry : originFacilitys.entrySet()) {
				String key = entry.getKey();
				DepotOriginFacilityNode value = entry.getValue();
				strBuf.append("\n\t\t").append(key).append(" -> ").append(value.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
}

@SuppressWarnings({ "unchecked", "rawtypes" })
class DepotOriginFacilityNode  extends PlanTreeNode {
	
	public DepotOriginFacilityNode(PlanTree tree) {
		super(tree);
	}

	Map<Date, DepotOriginTimeNode> times = new HashMap<Date, DepotOriginTimeNode>();

	public void prepare(Scrib s) {
		Date key = s.getDispatchGroup();
		DepotOriginTimeNode value = (DepotOriginTimeNode) times.get(key);
		if (value == null) {
			value = new DepotOriginTimeNode(this.getTree(), this);
			times.put(key, value);
		}
		value.prepare(s);
	}

	public void prepare(ScheduleEmployeeDetails s) {
			Date key = s.getSchedule().getDispatchGroupTime();
			DepotOriginTimeNode value = (DepotOriginTimeNode) times.get(key);
			if (value == null) {
				value = new DepotOriginTimeNode(this.getTree(), this);
				times.put(key, value);
			}
			value.prepare(s);
	}

	public Collection getPlan() {
		Collection result = new ArrayList();
		for (Iterator i = times.values().iterator(); i.hasNext();) {
			DepotOriginTimeNode value = (DepotOriginTimeNode) i.next();
			result.addAll(value.getPlan());
		}
		return result;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();	
		try {
			for (Map.Entry<Date, DepotOriginTimeNode> entry : times.entrySet()) {
				Date key = entry.getKey();
				DepotOriginTimeNode value = entry.getValue();
				strBuf.append("\n\t\t\t").append(TransStringUtil.getTime(key)).append(" -> ").append(value.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
}

@SuppressWarnings({ "unchecked", "rawtypes" })
class DepotDestFacilityNode  extends PlanTreeNode {
	
	public DepotDestFacilityNode(PlanTree tree) {
		super(tree);
	}

	Map<Date, DepotDestTimeNode> times = new HashMap<Date, DepotDestTimeNode>();

	public void prepare(Scrib s) {
		Date key = s.getDispatchGroup();
		DepotDestTimeNode value = (DepotDestTimeNode) times.get(key);
		if (value == null) {
			value = new DepotDestTimeNode(this.getTree(), this);
			times.put(key, value);
		}
		value.prepare(s);
	}

	public void prepare(ScheduleEmployeeDetails s) {
			Date key = s.getSchedule().getDispatchGroupTime();
			DepotDestTimeNode value = (DepotDestTimeNode) times.get(key);
			if (value == null) {
				value = new DepotDestTimeNode(this.getTree(), this);
				times.put(key, value);
			}
			value.prepare(s);
	}

	public Collection getPlan() {
		Collection result = new ArrayList();
		for (Iterator i = times.values().iterator(); i.hasNext();) {
			DepotDestTimeNode value = (DepotDestTimeNode) i.next();
			result.addAll(value.getPlan());
		}
		return result;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();	
		try {
			for (Map.Entry<Date, DepotDestTimeNode> entry : times.entrySet()) {
				Date key = entry.getKey();
				DepotDestTimeNode value = entry.getValue();
				strBuf.append("\n\t\t\t").append(TransStringUtil.getTime(key)).append(" -> ").append(value.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
}

@SuppressWarnings({ "unchecked", "rawtypes" })
class TimeNode extends PlanTreeNode  {
	public TimeNode(PlanTree tree) {
		super(tree);
	}

	List<TruckNode> trucks = new ArrayList<TruckNode>();
	List<ScheduleEmployeeDetails> employees = new ArrayList<ScheduleEmployeeDetails>();
	List<Plan> plans = new ArrayList<Plan>();
	Set<ScheduleEmployeeDetails> resourceTeams = null;
	

	public Collection getPlan() {
		Collections.sort(trucks, new TruckComparator());
		resourceTeams  = TreeDataUtil.teamUp(this.getTree(), employees);		
		assemble(resourceTeams);
		return plans;
	}

	public void prepare(Scrib s) {
		int n = s.getTruckCnt();
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
		//Retrieve Trailer resources
		List<ScheduleEmployeeDetails> trailerResources = TreeDataUtil.getTrailerResources(resources);

		for (Iterator i = trucks.iterator(); i.hasNext();) {
			Scrib s = ((TruckNode) i.next()).s;

			Plan p = new Plan();
			plans.add(p);
			p.setPlanDate(s.getScribDate());
			p.setOriginFacility(s.getOriginFacility());
			p.setDestinationFacility(s.getDestinationFacility());
			p.setEquipmentTypeS(s.getEquipmentTypeS());
			if(p.getDestinationFacility() != null && p.getDestinationFacility().getTrnFacilityType() != null &&
					!EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(p.getDestinationFacility().getTrnFacilityType().getName())){
				p.setZone(s.getZone());
			}
			p.setRegion(s.getRegion());
			p.setStartTime(s.getStartTime());
			p.setEndTime(s.getEndTime());
			p.setDispatchGroup(s.getDispatchGroup());
			p.setCutOffTime(s.getCutOffTime());
			p.setSupervisorId(s.getSupervisorCode());
			p.setMaxReturnTime(s.getMaxReturnTime());
			p.setSequence(rank++);

			Set zoneTypeResources = null;
			ScheduleEmployeeDetails _currResource = null;
			PlanResource _tmpPlanResource = null;

			if(s.getZone() != null) {
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
							zoneTypeResources = s.getZone().getTrnZoneType().getZonetypeResources();
							for (Iterator j = zoneTypeResources.iterator(); j.hasNext();) {
								ZonetypeResource r = (ZonetypeResource) j.next();
								// Driver
								if (ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r.getId().getRole())) {
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
								if (ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r.getId().getRole())) {
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
							}
						}
				}
			} else {
				if(trailerResources.size() > 0) {
					_currResource = (ScheduleEmployeeDetails)trailerResources.toArray()[0];
					if(_currResource.getMembers() != null && _currResource.getMembers().size() > 0) {
						
						for(ScheduleEmployeeDetails _members : _currResource.getMembers()) {
							_tmpPlanResource = TreeDataUtil.matchResource(_members);
							if(_tmpPlanResource != null) {
								p.getPlanResources().add(_tmpPlanResource);							
							} 
						}
						trailerResources.remove(_currResource);					
					} else {
								// Driver
								int driverCount = 0;
								for (Iterator k = trailerResources.iterator(); k.hasNext();) {
									ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
									if(se.getMembers() == null || se.getMembers().size() == 0) {
											if (driverCount >= TransportationAdminProperties.getDriverReqForTrailer())
												break;

											_tmpPlanResource = TreeDataUtil.matchResource(se, ScheduleEmployeeInfo.DRIVER);
											if(_tmpPlanResource != null) {
												p.getPlanResources().add(_tmpPlanResource);
												k.remove();
												driverCount++;
											}
									}
								}				
								// Helper
								int helperCount = 0;
								for (Iterator k = trailerResources.iterator(); k.hasNext();) {
									ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
									if(se.getMembers() == null || se.getMembers().size() == 0) {
											if (helperCount >= TransportationAdminProperties.getHelperReqForTrailer())
												break;
											
											_tmpPlanResource = TreeDataUtil.matchResource(se, ScheduleEmployeeInfo.HELPER);
											if(_tmpPlanResource != null) {
												p.getPlanResources().add(_tmpPlanResource);
												k.remove();
												helperCount++;
											}
									}
								}								
								// Runner							
								int runnerCount = 0;
								for (Iterator k = trailerResources.iterator(); k.hasNext();) {
									ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
									if(se.getMembers() == null || se.getMembers().size() == 0) {
										if (runnerCount >= TransportationAdminProperties.getRunnerReqForTrailer())
												break;
											
										_tmpPlanResource = TreeDataUtil.matchResource(se, ScheduleEmployeeInfo.RUNNER);
										if(_tmpPlanResource != null) {
											p.getPlanResources().add(_tmpPlanResource);
											k.remove();
											runnerCount++;
										}
									}
								}
						}
					}
			}
			p.setOpen(TreeDataUtil.isOpen(p, zoneTypeResources));
		}

		// Create Team Bullpen
		if (resources != null && resources.size() > 0) {
			for (Iterator i = resources.iterator(); i.hasNext();) {
				ScheduleEmployeeDetails _currResource = (ScheduleEmployeeDetails) i.next();
				if(_currResource.getMembers() != null && _currResource.getMembers().size() > 0) {
					Plan p = new Plan();
					plans.add(p);
					p.setPlanDate(_currResource.getDate());
					p.setOriginFacility(_currResource.getSchedule().getRegion().getOriginFacility());
					p.setRegion(_currResource.getSchedule().getRegion());
					p.setStartTime(_currResource.getSchedule().getDispatchGroupTime());
					p.setEndTime(_currResource.getSchedule().getDispatchGroupTime());
					p.setDispatchGroup(_currResource.getSchedule().getDispatchGroupTime());
					p.setIsBullpen("Y");

					for(ScheduleEmployeeDetails ss : _currResource.getMembers()) {						
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
					i.remove();					
				} 
			}
		}

		// Bullpens for non-team employees
		while(resources != null && resources.size() > 0){
			ScheduleEmployeeDetails s = (ScheduleEmployeeDetails) resources.toArray()[0];
			Plan p = new Plan();
			
			p.setPlanDate(s.getDate());
			p.setOriginFacility(s.getSchedule().getRegion().getOriginFacility());
			p.setRegion(s.getSchedule().getRegion());
			
			p.setStartTime(s.getSchedule().getDispatchGroupTime());
			p.setEndTime(s.getSchedule().getDispatchGroupTime());
			p.setDispatchGroup(s.getSchedule().getDispatchGroupTime());			

			p.setIsBullpen("Y");
			
			int driverMax = TransportationAdminProperties.getDriverMaxForBullpen();
			int helperMax = TransportationAdminProperties.getHelperMaxForBullpen();
			int driverCount = 0;
			int helperCount = 0;
			
			for (Iterator k = resources.iterator(); k.hasNext();) {
				ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
				
				Collection c = se.getEmpRoles();
				if (!TreeDataUtil.isRole(ScheduleEmployeeInfo.RUNNER, c)) {
					PlanResource planResource = new PlanResource();
					EmployeeRoleType type = new EmployeeRoleType();
					for (Iterator si = se.getEmpRoles().iterator(); si.hasNext();) {
						type.setCode(((EmployeeRole) (si.next())).getId().getRole());						
						break;
					}
					ResourceId resource = new ResourceId();
					resource.setResourceId(se.info.getEmployeeId());
					planResource.setEmployeeRoleType(type);
					planResource.setId(resource);
					
					if(TreeDataUtil.isRole(ScheduleEmployeeInfo.DRIVER, c) && driverCount < driverMax){
						driverCount++;
						p.getPlanResources().add(planResource);
						k.remove();
					}else if(TreeDataUtil.isRole(ScheduleEmployeeInfo.HELPER, c)  && helperCount < helperMax){
						helperCount++;
						p.getPlanResources().add(planResource);
						k.remove();
					}
				} else {
					k.remove();
				}
			}
			if(p.getPlanResources().size() > 0) {
				plans.add(p);
			}
		}

		while(trailerResources != null && trailerResources.size() > 0){
			ScheduleEmployeeDetails s = (ScheduleEmployeeDetails) trailerResources.toArray()[0];
			Plan p = new Plan();

			p.setPlanDate(s.getDate());
			p.setOriginFacility(s.getSchedule().getRegion().getOriginFacility());
			p.setRegion(s.getSchedule().getRegion());
			p.setStartTime(s.getSchedule().getDispatchGroupTime());
			p.setEndTime(s.getSchedule().getDispatchGroupTime());
			p.setDispatchGroup(s.getSchedule().getDispatchGroupTime());	

			p.setIsBullpen("Y");

			int driverMax = TransportationAdminProperties.getDriverMaxForBullpen();
			int helperMax = TransportationAdminProperties.getHelperMaxForBullpen();
			int driverCount = 0;
			int helperCount = 0;
			
			for (Iterator k = trailerResources.iterator(); k.hasNext();) {
				ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
				
				Collection c = se.getEmpRoles();
				if (!TreeDataUtil.isRole(ScheduleEmployeeInfo.RUNNER, c)) {
					PlanResource planResource = new PlanResource();
					EmployeeRoleType type = new EmployeeRoleType();
					for (Iterator si = se.getEmpRoles().iterator(); si.hasNext();) {
						type.setCode(((EmployeeRole) (si.next())).getId().getRole());						
						break;
					}
					ResourceId resource = new ResourceId();
					resource.setResourceId(se.info.getEmployeeId());
					planResource.setEmployeeRoleType(type);
					planResource.setId(resource);
					
					if(TreeDataUtil.isRole(ScheduleEmployeeInfo.DRIVER, c) && driverCount < driverMax){
						driverCount++;
						p.getPlanResources().add(planResource);
						k.remove();
					} else if(TreeDataUtil.isRole(ScheduleEmployeeInfo.HELPER, c)  && helperCount < helperMax){
						helperCount++;
						p.getPlanResources().add(planResource);
						k.remove();
					}
				} else {
					k.remove();
				}
			}
			if(p.getPlanResources().size() > 0) {
				plans.add(p);
			}
		}
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("TRUCKS == ").append(trucks);
		strBuf.append("EMPLOYEES == ").append(resourceTeams);
		return strBuf.toString();
	}
}

@SuppressWarnings({ "unchecked", "rawtypes" })
class DepotOriginTimeNode extends PlanTreeNode  {

	List<TruckNode> trucks = new ArrayList<TruckNode>();
	List<ScheduleEmployeeDetails> employees = new ArrayList<ScheduleEmployeeDetails>();
	List<Plan> plans = new ArrayList<Plan>();
	Set<ScheduleEmployeeDetails> resourceTeams = null;
	DepotOriginFacilityNode parent;

	public DepotOriginTimeNode(PlanTree tree, DepotOriginFacilityNode parent) {
		super(tree);
		this.parent = parent;
	}
	
	public Collection getPlan() {
		Collections.sort(trucks, new TruckComparator());
		resourceTeams = TreeDataUtil.teamUp(this.getTree(), employees);
		assemble(resourceTeams);
		return plans;
	}

	public void prepare(Scrib s) {
		int n = s.getNoOfResources();
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
			p.setOriginFacility(s.getOriginFacility());
			p.setDestinationFacility(s.getDestinationFacility());
			p.setEquipmentTypeS(s.getEquipmentTypeS());
			p.setZone(s.getZone());
			p.setRegion(s.getRegion());
			p.setStartTime(s.getStartTime());
			p.setEndTime(s.getEndTime());
			p.setDispatchGroup(s.getDispatchGroup());
			p.setCutOffTime(s.getCutOffTime());
			p.setSupervisorId(s.getSupervisorCode());
			p.setMaxReturnTime(s.getMaxReturnTime());
			p.setSequence(rank++);

			ScheduleEmployeeDetails _currResource = null;
			PlanResource _tmpPlanResource = null;
			
			if(s.getZone() != null){
				if(resources.size() > 0) {
					_currResource = (ScheduleEmployeeDetails) resources.toArray()[0];
					if(_currResource.getMembers() != null && _currResource.getMembers().size() > 0) {						
						for(ScheduleEmployeeDetails _members : _currResource.getMembers()) {
							_tmpPlanResource = TreeDataUtil.matchResource(_members);
							if(_tmpPlanResource != null) {
								p.getPlanResources().add(_tmpPlanResource);							
							} 
						}
						resources.remove(_currResource);
					} else {										
							int count = 0;
							for (Iterator k = resources.iterator(); k.hasNext();) {
								ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
								if(se.getMembers() == null || se.getMembers().size() == 0) {
									if (count >= TransportationAdminProperties.getRunnerReqForHandTruck())
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
				
		// Bullpen for team employees
		if (resources != null && resources.size() > 0) {
			for (Iterator i = resources.iterator(); i.hasNext();) {
				ScheduleEmployeeDetails _currResource = (ScheduleEmployeeDetails) i.next();
				if(_currResource.getMembers() != null && _currResource.getMembers().size() > 0) {
					Plan p = new Plan();
					p.setPlanDate(_currResource.getDate());
					p.setOriginFacility(_currResource.getSchedule().getRegion().getOriginFacility());
					p.setRegion(_currResource.getSchedule().getRegion());
					p.setStartTime(_currResource.getSchedule().getDispatchGroupTime());
					p.setEndTime(_currResource.getSchedule().getDispatchGroupTime());
					p.setDispatchGroup(_currResource.getSchedule().getDispatchGroupTime());
					p.setIsBullpen("Y");
					for(ScheduleEmployeeDetails ss : _currResource.getMembers()) {						
						Collection c = ss.getEmpRoles();
						if (TreeDataUtil.isRole(ScheduleEmployeeInfo.RUNNER, c)) {
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
					i.remove();

					if(p.getPlanResources().size() > 0) {
						plans.add(p);
					}
				}
			}
		}

		// Bullpen for non-team employees
		while(resources != null && resources.size() > 0){			
			ScheduleEmployeeDetails s = (ScheduleEmployeeDetails) resources.toArray()[0];
			Plan p = new Plan();
			
			p.setPlanDate(s.getDate());
			p.setOriginFacility(s.getSchedule().getRegion().getOriginFacility());
			p.setRegion(s.getSchedule().getRegion());
			p.setStartTime(s.getSchedule().getDispatchGroupTime());
			p.setEndTime(s.getSchedule().getDispatchGroupTime());
			p.setDispatchGroup(s.getSchedule().getDispatchGroupTime());	
			p.setIsBullpen("Y");
			
			int runnerCount = 0;
			
			for (Iterator k = resources.iterator(); k.hasNext();) {
				ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
					Collection c = se.getEmpRoles();				
					PlanResource planResource = new PlanResource();
					EmployeeRoleType type = new EmployeeRoleType();
					for (Iterator si = se.getEmpRoles().iterator(); si.hasNext();) {
						type.setCode(((EmployeeRole) (si.next())).getId().getRole());						
						break;
					}
					ResourceId resource = new ResourceId();
					resource.setResourceId(se.info.getEmployeeId());
					planResource.setEmployeeRoleType(type);
					planResource.setId(resource);
											
					if(runnerCount < TransportationAdminProperties.getRunnerMaxForBullpen()){
						runnerCount++;
						p.getPlanResources().add(planResource);
						k.remove();
					}
					
			}
			if(p.getPlanResources().size() > 0) {
				plans.add(p);
			}
		}		
	}
	public String toString() {
		StringBuffer strBuf = new StringBuffer();		
		strBuf.append("TRUCKS == ").append(trucks);
		strBuf.append("EMPLOYEES == ").append(resourceTeams);
		return strBuf.toString();
	}
}

@SuppressWarnings({ "unchecked", "rawtypes" })
class DepotDestTimeNode extends PlanTreeNode  {

	List<TruckNode> trucks = new ArrayList<TruckNode>();
	List<ScheduleEmployeeDetails> employees = new ArrayList<ScheduleEmployeeDetails>();
	List<Plan> plans = new ArrayList<Plan>();
	Set<ScheduleEmployeeDetails> resourceTeams = null;
	DepotDestFacilityNode parent;
	
	public DepotDestTimeNode(PlanTree tree, DepotDestFacilityNode parent) {
		super(tree);
		this.parent = parent;
	}
		
	public Collection getPlan() {
		Collections.sort(trucks, new TruckComparator());
		resourceTeams = TreeDataUtil.teamUp(this.getTree(), employees);
		assemble(resourceTeams);
		return plans;
	}
	
	public void prepare(Scrib s) {
		int n = s.getNoOfResources();
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
			p.setOriginFacility(s.getOriginFacility());
			p.setDestinationFacility(s.getDestinationFacility());
			p.setEquipmentTypeS(s.getEquipmentTypeS());
			p.setZone(s.getZone());
			p.setRegion(s.getRegion());
			p.setStartTime(s.getStartTime());
			p.setEndTime(s.getEndTime());
			p.setDispatchGroup(s.getDispatchGroup());
			p.setCutOffTime(s.getCutOffTime());
			p.setSupervisorId(s.getSupervisorCode());
			p.setMaxReturnTime(s.getMaxReturnTime());
			p.setSequence(rank++);
	
			Set zoneTypeResources = null;
			ScheduleEmployeeDetails _currResource = null;
			PlanResource _tmpPlanResource = null;
			
			if(s.getZone() != null){
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
						zoneTypeResources = s.getZone().getTrnZoneType().getZonetypeResources();
							for (Iterator j = zoneTypeResources.iterator(); j.hasNext();) {
								ZonetypeResource r = (ZonetypeResource) j.next();
								// Driver
								if (ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r.getId().getRole())) {
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
								if (ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r.getId().getRole())) {
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
							}
						
					}				
				}
			} 
		}
				
		// Bullpen for team employees
		if (resources != null && resources.size() > 0) {
			for (Iterator i = resources.iterator(); i.hasNext();) {
				ScheduleEmployeeDetails _currResource = (ScheduleEmployeeDetails) i.next();
				if(_currResource.getMembers() != null && _currResource.getMembers().size() > 0) {
					Plan p = new Plan();					
					p.setPlanDate(_currResource.getDate());
					p.setOriginFacility(_currResource.getSchedule().getRegion().getOriginFacility());
					p.setRegion(_currResource.getSchedule().getRegion());
					p.setStartTime(_currResource.getSchedule().getDispatchGroupTime());
					p.setEndTime(_currResource.getSchedule().getDispatchGroupTime());
					p.setDispatchGroup(_currResource.getSchedule().getDispatchGroupTime());
					p.setIsBullpen("Y");
					for(ScheduleEmployeeDetails ss : _currResource.getMembers()) {						
						Collection c = ss.getEmpRoles();
						if (!TreeDataUtil.isRole(ScheduleEmployeeInfo.RUNNER, c)) {
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
					i.remove();	

					if(p.getPlanResources().size() > 0) {
						plans.add(p);
					}
				} 
			}
		}

		// Bullpens for non-team employees
		while(resources != null && resources.size() > 0){			
			ScheduleEmployeeDetails s = (ScheduleEmployeeDetails) resources.toArray()[0];
			Plan p = new Plan();
						
			p.setPlanDate(s.getDate());
			p.setOriginFacility(s.getSchedule().getRegion().getOriginFacility());
			p.setRegion(s.getSchedule().getRegion());
			p.setStartTime(s.getSchedule().getDispatchGroupTime());
			p.setEndTime(s.getSchedule().getDispatchGroupTime());
			p.setDispatchGroup(s.getSchedule().getDispatchGroupTime());	

			p.setIsBullpen("Y");
			
			int driverCount = 0, helperCount = 0;
			
			for (Iterator k = resources.iterator(); k.hasNext();) {
				ScheduleEmployeeDetails se = (ScheduleEmployeeDetails) k.next();
					Collection c = se.getEmpRoles();				
					PlanResource planResource = new PlanResource();
					EmployeeRoleType type = new EmployeeRoleType();
					for (Iterator si = se.getEmpRoles().iterator(); si.hasNext();) {
						type.setCode(((EmployeeRole) (si.next())).getId().getRole());						
						break;
					}
					ResourceId resource = new ResourceId();
					resource.setResourceId(se.info.getEmployeeId());
					planResource.setEmployeeRoleType(type);
					planResource.setId(resource);
					
					if(TreeDataUtil.isRole(ScheduleEmployeeInfo.DRIVER, c) && driverCount < TransportationAdminProperties.getDriverMaxForBullpen()){
						driverCount++;
						p.getPlanResources().add(planResource);
						k.remove();
					} else if(TreeDataUtil.isRole(ScheduleEmployeeInfo.HELPER, c)  && helperCount < TransportationAdminProperties.getHelperMaxForBullpen()){
						helperCount++;
						p.getPlanResources().add(planResource);
						k.remove();
					}
			}

			if(p.getPlanResources().size() > 0) {
				plans.add(p);
			}
		}

	}

	public String toString() {
		StringBuffer strBuf = new StringBuffer();	
		strBuf.append("TRUCKS == ").append(trucks);
		strBuf.append("EMPLOYEES == ").append(resourceTeams);
		return strBuf.toString();
	}
}

class TruckNode extends PlanTreeNode {
	public TruckNode(PlanTree tree) {
		super(tree);
	}

	Scrib s;

	public void prepare(Scrib s) {
		this.s = s;
	}
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(s.getTruckCnt());		
		return strBuf.toString();
	}
}

@SuppressWarnings({ "rawtypes" })
class TruckComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		if (o1 instanceof Scrib && o2 instanceof Scrib) {
			Scrib s1 = (Scrib) o1;
			Scrib s2 = (Scrib) o2;
			int z1 = 0;
			int z2 = 0;
			if (s1.getZone() != null && s1.getZone().getPriority() != null)
				z1 = s1.getZone().getPriority().intValue();
			if (s2.getZone() != null && s2.getZone().getPriority() != null)
				z2 = s2.getZone().getPriority().intValue();
			if (z2 - z1 != 0)
				return z2 - z1;
			return s1.getStartTime().compareTo(s2.getStartTime());
		}
		return 0;
	}

}

@SuppressWarnings({ "rawtypes" })
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

@SuppressWarnings({ "rawtypes" })
class EmployeeScheduleComparator implements Comparator {

	public int compare(Object obj, Object obj1) {
		if (obj instanceof ScheduleEmployeeDetails
				&& obj1 instanceof ScheduleEmployeeDetails) {
			ScheduleEmployeeDetails s1 = (ScheduleEmployeeDetails) obj;
			ScheduleEmployeeDetails s2 = (ScheduleEmployeeDetails) obj1;
			Date d1 = null;
			Date d2 = null;
			if (s1.getSchedule() != null)
				d1 = s1.getSchedule().getDispatchGroupTime();
			if (s2.getSchedule() != null)
				d2 = s2.getSchedule().getDispatchGroupTime();			
			return d1.compareTo(d2);
		}
		return 0;
	}

}

@SuppressWarnings({ "unchecked", "rawtypes" })
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
			if (resources != null){
				for (Iterator j = resources.iterator(); j.hasNext();) {
					ZonetypeResource r = (ZonetypeResource) j.next();

					if (r != null && r.getId() != null) {
						// Driver
						if (ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r.getId().getRole()))
							driverMin = r.getRequiredNo().intValue();
						// Helper
						if (ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r.getId().getRole())) 
							helperMin = r.getRequiredNo().intValue();
						// Runner
						if (ScheduleEmployeeInfo.RUNNER.equalsIgnoreCase(r.getId().getRole())) 
							runnerMin = r.getRequiredNo().intValue();
						}
					}
			} else {
				 driverMin = TransportationAdminProperties.getDriverReqForTrailer();
				 helperMin = TransportationAdminProperties.getHelperReqForTrailer();
				 runnerMin = TransportationAdminProperties.getRunnerReqForTrailer();
			}
			
			for (Iterator<PlanResource> j = p.getPlanResources().iterator(); j.hasNext();) {
				PlanResource r = j.next();
				if (r != null && r.getEmployeeRoleType() != null) {
					// Driver
					if (ScheduleEmployeeInfo.DRIVER.equalsIgnoreCase(r.getEmployeeRoleType().getCode())) {
						drivers++;
					}
					// Helper
					if (ScheduleEmployeeInfo.HELPER.equalsIgnoreCase(r.getEmployeeRoleType().getCode())) {
						helpers++;
					}
					// Runner
					if (ScheduleEmployeeInfo.RUNNER.equalsIgnoreCase(r.getEmployeeRoleType().getCode())) {
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
	
	public static boolean isTrailerRole(Collection c) {
		boolean result = false;
		for (Iterator k = c.iterator(); k.hasNext();) {
			EmployeeRole e = (EmployeeRole) k.next();
			if (EnumResourceSubType.TRAILER_DRIVER.getName().equalsIgnoreCase(e.getEmployeeSubRoleType().getCode())
					|| EnumResourceSubType.TRAILER_HELPER.getName().equalsIgnoreCase(e.getEmployeeSubRoleType().getCode())
					|| EnumResourceSubType.TRAILER_RUNNER.getName().equalsIgnoreCase(e.getEmployeeSubRoleType().getCode())) {
				return true;
			}
		}
		return result;
	}
	
	public static List<ScheduleEmployeeDetails> getTrailerResources(List<ScheduleEmployeeDetails> resources){
		List<ScheduleEmployeeDetails> trailerResources = new ArrayList<ScheduleEmployeeDetails>();
		if(resources.size() > 0) {
			for (Iterator<ScheduleEmployeeDetails> k = resources.iterator(); k.hasNext();) {
				ScheduleEmployeeDetails se = k.next();
				if(TreeDataUtil.isTrailerRole(se.getEmpRoles())){
					trailerResources.add(se);
					k.remove();
				}
			}
		}
		Collections.sort(trailerResources, new HireDateComparator());
		return trailerResources;
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