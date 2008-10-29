package com.freshdirect.transadmin.datamanager;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.datamanager.model.RouteNoGenerationModel;
import com.freshdirect.transadmin.datamanager.util.CutOffComparator;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnRouteNumber;
import com.freshdirect.transadmin.model.TrnRouteNumberId;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class RouteDataManager {
	
	protected IRouteFileManager fileManager = new RouteFileManager();
	
	protected static final String ROW_IDENTIFIER = "row";
	
	protected static final String ROW_BEAN_IDENTIFIER = "rowBean";
	       
	public RoutingResult process(byte[] inputInfo1,byte[] inputInfo2, byte[] inputInfo3, String userName, Map paramMap, DomainManagerI domainManagerService) throws IOException {		
		return null;
	}
		
	protected RoutingResult initResult(String outputFileName1, String outputFileName2, String outputFileName3) throws IOException  {
		
		RoutingResult result = new RoutingResult();
		
	    if(outputFileName1 != null) {
			File outputFile1 = File.createTempFile(outputFileName1, "."+TransportationAdminProperties.getFilenameSuffix());
		    result.setOutputFile1(outputFile1.getAbsolutePath());
	    }
	    
	    if(outputFileName2 != null) {
		    File outputFile2 = File.createTempFile(outputFileName2, "."+TransportationAdminProperties.getFilenameSuffix());	    	    
		    result.setOutputFile2(outputFile2.getAbsolutePath());
	    }
	    
	    if(outputFileName3 != null) {
	    	File outputFile3 = File.createTempFile(outputFileName3, "."+TransportationAdminProperties.getErrorFilenameSuffix());
	    	result.setOutputFile3(outputFile3.getAbsolutePath());
	    }
	    return result;
	}	
	
//	TrnRouteNumberId
	protected RouteGenerationResult generateRouteNumber(List routeData, String cutOff, DomainManagerI domainManagerService ) {
				
		Map routeNoGenMapping = new HashMap();
		
		if(routeData != null) {
			Iterator iterator = routeData.iterator();
			OrderRouteInfoModel tmpModel = null;
			
			Map areaMapping = getAreaMapping(domainManagerService.getAreas());
						
			TrnRouteNumberId routeId = null;
			RouteNoGenerationModel routeNoGenModel = null;
			CutOffComparator comparator = getCutOffComparator(domainManagerService.getCutOffs());
			String areaCode = null;
			String areaPrefix = null;
			String areaDeliveryModel = null;
			String currentRoute = null;
			
			while(iterator.hasNext()) {		
				
				tmpModel = (OrderRouteInfoModel)iterator.next();
				areaCode = TransStringUtil.splitStringForCode(tmpModel.getRouteId());
				tmpModel.setDeliveryArea(areaCode);
				areaPrefix = (String)((TrnArea)areaMapping.get(areaCode)).getPrefix();
				areaDeliveryModel = (String)((TrnArea)areaMapping.get(areaCode)).getDeliveryModel();
				if(areaPrefix == null) {
					areaPrefix = "0";
				}
				routeId = getRouteNumberId(tmpModel.getDeliveryDate(), cutOff, areaCode);
				if(routeNoGenMapping.containsKey(routeId)) {
					routeNoGenModel = (RouteNoGenerationModel)routeNoGenMapping.get(routeId);
				} else {
					//Key pointer to generating route no
					routeNoGenModel = fillRouteNumberGroup(routeId, routeNoGenMapping, comparator, domainManagerService);
					/*routeNoGenModel = new RouteNoGenerationModel();
					routeNoGenMapping.put(routeId, routeNoGenModel);
					
					fillRelatedCutOff(routeNoGenModel, routeId, comparator
										, new ArrayList(domainManagerService
														.getRouteNumberGroup(getRouteDate(routeId.getRouteDate())
																, null, routeId.getAreaCode())));*/
				}
				currentRoute = (String)routeNoGenModel.getRoute(tmpModel.getRouteId().trim());
				if(currentRoute == null) {
					currentRoute = ""+routeNoGenModel.incrementCurrentSequenceNo();
					routeNoGenModel.putRoute(tmpModel.getRouteId().trim(), currentRoute);
				}
				tmpModel.setRouteId(areaPrefix+areaCode+TransStringUtil.formatRouteNumber(currentRoute));
				tmpModel.setDeliveryModel(areaDeliveryModel);
				routeNoGenModel.addOrder(tmpModel);
			}
		}
		return getRouteGenerationResult(routeNoGenMapping);
	}
	
	// Collect and fille routeNumber under investigation and related routeNumber for date and cutoff
	protected RouteNoGenerationModel fillRouteNumberGroup(TrnRouteNumberId routeId, Map routeNoGenMapping, 
											CutOffComparator comparator, DomainManagerI domainManagerService) {
		
		Collection routeNoForDateCutOff = domainManagerService.getRouteNumberGroup(getRouteDate(routeId.getRouteDate())
																, routeId.getCutOffId(), null);
		RouteNoGenerationModel routeNoGenModel = null;
		if(routeNoForDateCutOff != null) {
			Iterator iterator = routeNoForDateCutOff.iterator();
			TrnRouteNumber tmpModel = null;
			
			while(iterator.hasNext()) {				
				tmpModel = (TrnRouteNumber)iterator.next();
				if(!tmpModel.getRouteNumberId().equals(routeId)
						&& !routeNoGenMapping.containsKey(routeId)) {
					routeNoGenModel = new RouteNoGenerationModel();
					routeNoGenMapping.put(tmpModel.getRouteNumberId(), routeNoGenModel);
					
					fillRelatedCutOff(routeNoGenModel, tmpModel.getRouteNumberId(), comparator
										, new ArrayList(domainManagerService
														.getRouteNumberGroup(getRouteDate(tmpModel.getRouteNumberId().getRouteDate())
																, null, tmpModel.getRouteNumberId().getAreaCode())));
				}
			}
		}
		
		routeNoGenModel = new RouteNoGenerationModel();
		routeNoGenMapping.put(routeId, routeNoGenModel);
		
		fillRelatedCutOff(routeNoGenModel, routeId, comparator
							, new ArrayList(domainManagerService
											.getRouteNumberGroup(getRouteDate(routeId.getRouteDate())
													, null, routeId.getAreaCode())));
		return routeNoGenModel;
	}
	
	protected RouteGenerationResult getRouteGenerationResult(Map routeNoGenMapping) {
		
		RouteGenerationResult result = new RouteGenerationResult();
		Set routeKeys = routeNoGenMapping.keySet();
		
		List routeInfos = new ArrayList();		
		List routeNoSaveInfos = new ArrayList();
		result.setRouteInfos(routeInfos);
		result.setRouteNoSaveInfos(routeNoSaveInfos);
		
		if(routeKeys != null) {
			
			Iterator iterator = routeKeys.iterator();
			TrnRouteNumberId routeId = null;
			RouteNoGenerationModel routeNoGenModel = null;
			TrnRouteNumber routeNo = null;
			List successors = null;
			
			while(iterator.hasNext()) {
				
				routeId = (TrnRouteNumberId)iterator.next();
				routeNoGenModel = (RouteNoGenerationModel)routeNoGenMapping.get(routeId);
				successors = routeNoGenModel.getSuccessors();
				if(routeNoGenModel.getOrders() != null) {
					routeInfos.addAll(routeNoGenModel.getOrders());
				}
				
				routeNo = new TrnRouteNumber(routeId);
				routeNoSaveInfos.add(routeNo);
				routeNo.setCurrentVal(new BigDecimal(routeNoGenModel.getCurrentSequenceNo()));
				
				int difference = routeNoGenModel.getCurrentSequenceNo() - routeNoGenModel.getPreviousSequenceNo();
				if(successors != null) {
					
					Iterator predIterator = successors.iterator();
					while(predIterator.hasNext()) {
						routeNo = (TrnRouteNumber)predIterator.next();
						routeNo.setCurrentVal(new BigDecimal(routeNo.getCurrentVal().intValue()+difference));
						routeNoSaveInfos.add(routeNo);
					}
				}
			}
		}
		
		return result;
	}
	
	protected void fillRelatedCutOff(RouteNoGenerationModel model, TrnRouteNumberId routeId
										, CutOffComparator comparator, List routeNoLst) {		
		Map referenceData = comparator.getReferenceData();
		if(routeNoLst != null && referenceData != null) {
			Collections.sort(routeNoLst, comparator);
			
			Iterator iterator = routeNoLst.iterator();
			TrnRouteNumber tmpModel = null;
			TrnRouteNumber predecessorModel = null;
			TrnRouteNumber currentModel = null;
			TrnCutOff cutOff = null;
			
			int cutOffSequence = ((TrnCutOff)referenceData.get(routeId.getCutOffId())).getSequenceNo().intValue();
			List orphanLst = new ArrayList();
			while(iterator.hasNext()) {
				
				tmpModel = (TrnRouteNumber)iterator.next();
				cutOff = (TrnCutOff)referenceData.get(tmpModel.getRouteNumberId().getCutOffId());
				if(!tmpModel.getRouteNumberId().equals(routeId)) {
					if(cutOff != null) {
						if(cutOff.getSequenceNo().intValue() < cutOffSequence) {
							model.addPredecessor(tmpModel);
						} else {
							model.addSuccessor(tmpModel);
						}
					} else {
						orphanLst.add(tmpModel);						
					}
				} else {
					currentModel = tmpModel;
				}
			}
			model.addSuccessors(orphanLst);
			if(model.getPredecessors() != null && model.getPredecessors().size() > 0) {
				predecessorModel = (TrnRouteNumber)model.getPredecessors().get(model.getPredecessors().size()-1);
				model.setCurrentSequenceNo(predecessorModel.getCurrentVal().intValue());
			}
			if(currentModel != null) {
				model.setPreviousSequenceNo(currentModel.getCurrentVal().intValue());
			} else {
				model.setPreviousSequenceNo(model.getCurrentSequenceNo());
			}
		}
	}
	
	protected TrnRouteNumberId getRouteNumberId(Date orderDate, String cutOff, String area) {
		return new TrnRouteNumberId(orderDate, cutOff, area);
	}
	
	protected CutOffComparator getCutOffComparator(Collection cutOffLst) {
		Map cutOffMapping = new HashMap();
		if(cutOffLst != null) {
			Iterator iterator = cutOffLst.iterator();
			TrnCutOff tmpModel = null;
			
			while(iterator.hasNext()) {
				tmpModel = (TrnCutOff)iterator.next();
				cutOffMapping.put(tmpModel.getCutOffId(), tmpModel);
			}
		}
		return new CutOffComparator(cutOffMapping);
	}
	
	protected Map getAreaMapping(Collection areaLst) {
		Map areaMapping = new HashMap();
		if(areaLst != null) {
			Iterator iterator = areaLst.iterator();
			TrnArea tmpModel = null;
			
			while(iterator.hasNext()) {
				tmpModel = (TrnArea)iterator.next();
				areaMapping.put(tmpModel.getCode(), tmpModel);
			}
		}
		return areaMapping;
	}
	
	private String getRouteDate(Date routeDate) {
		try {
			return TransStringUtil.getServerDate(routeDate);
		} catch(ParseException exp) {
			return TransStringUtil.getCurrentDate();
		}
	}

}
