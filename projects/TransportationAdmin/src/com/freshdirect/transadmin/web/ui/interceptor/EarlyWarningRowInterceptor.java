package com.freshdirect.transadmin.web.ui.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;

import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.EarlyWarningCommand;

public class EarlyWarningRowInterceptor extends FDRowInterceptor {
	
	public void addRowAttributes(TableModel tableModel, Row row) {
    } 

    public void modifyRowAttributes(TableModel model, Row row) {
    	
    	EarlyWarningCommand rowEntity = (EarlyWarningCommand) model.getCurrentRowBean(); 
    	
		/*if (getDouble(rowEntity.getPercentageAllocated()) > 89.0) {
			row.setStyleClass("earlyWarningRow"); // Red
		} else {  */  		
    		Map<String, Integer> waveCodeCnts = new HashMap<String, Integer>();    	
    		if(rowEntity.getTimeslotDetails() != null) {
    			for(EarlyWarningCommand _tsCommand : rowEntity.getTimeslotDetails()) {
    				if(!waveCodeCnts.containsKey(_tsCommand.getWaveCode())) {
    					waveCodeCnts.put(_tsCommand.getWaveCode(), 0);
    				}
    				waveCodeCnts.put(_tsCommand.getWaveCode(), 
    						getDouble(_tsCommand.getPercentageAllocated()) > 89.0 
    							? waveCodeCnts.get(_tsCommand.getWaveCode()).intValue() + 1 
    									: waveCodeCnts.get(_tsCommand.getWaveCode()).intValue());    				    				
    				
    			}
    		}   
    		    		
    		// make a switch variable so as not to change the original value
    		int switchVar = waveCodeCnts.size();

    		//combine range >= 2 to single case in switch
    		if(2 <= waveCodeCnts.size())
    			switchVar = 2;
    		
    		switch (switchVar) { 
    		    case 1:
    		    	for (Map.Entry<String, Integer> _cutOffEntry : waveCodeCnts.entrySet()) {
    		    		if((_cutOffEntry.getValue().doubleValue() / rowEntity.getTimeslotDetails().size()) * 100.0 >= 75.0) {
    		    			row.setStyleClass("earlyWarningRow"); // Red
    		    			break;
    		    		} else if ((_cutOffEntry.getValue().doubleValue() / rowEntity.getTimeslotDetails().size()) * 100.0 >= 50.0) {
    		    			row.setStyleClass("earlyWarningOrangeRow"); // Orange
    		    			break;
    		    		} else {
    		    			if(TransStringUtil.isEmpty(rowEntity.getName())) {
    		        			row.setStyleClass("earlyWarningTotalRow");
    		        		} else {
    		        			super.modifyRowAttributes(model, row);
    		        		}
    		    		}
    		    	}
    		        break; 
    		    case 2:
    		        // CutOffs >= 2
    		    	for (Map.Entry<String, Integer> _cutOffEntry : waveCodeCnts.entrySet()) {
    		    		if((_cutOffEntry.getValue().doubleValue() / rowEntity.getTimeslotDetails().size()) * 100.0 >= 50.0) {
    		    			row.setStyleClass("earlyWarningYellowRow"); // Yellow
    		    			break;
    		    		} else {
    		    			if(TransStringUtil.isEmpty(rowEntity.getName())) {
    		        			row.setStyleClass("earlyWarningTotalRow");
    		        		} else {
    		        			super.modifyRowAttributes(model, row);
    		        		}
    		    		}
    		    	}
    		        break;
    		    default:
    		    	if(TransStringUtil.isEmpty(rowEntity.getName())) {
    	    			row.setStyleClass("earlyWarningTotalRow");
    	    		} else {
    	    			super.modifyRowAttributes(model, row);
    	    		}
    		        break;
    		}    		
    	//}
    }
    
    private double getDouble(String strVal) {
    	try {
    		return Double.parseDouble(strVal.substring(0, strVal.length()-1));
    	} catch(NumberFormatException ex) {
    		return 0;
    	}
    }

}
