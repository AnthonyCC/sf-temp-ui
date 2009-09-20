package com.freshdirect.transadmin.web.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import com.freshdirect.transadmin.model.TimeslotLog;
import com.freshdirect.transadmin.model.TimeslotLogDtl;
import com.freshdirect.transadmin.util.TransStringUtil;

public class FDTimeslotLogDtlCell extends FDBaseCell  {
	
	private static final String FULL_INDICATOR = "x";
	
	public String getExportDisplay(TableModel model, Column column) {
		      
		return formatTimeslotLog((TimeslotLog)model.getCurrentRowBean(), false);       
    }

    public String getHtmlDisplay(TableModel model, Column column) {    	
        ColumnBuilder columnBuilder = new ColumnBuilder(column);
        HtmlBuilder html = columnBuilder.getHtmlBuilder();
       
        columnBuilder.tdStart();
        
        columnBuilder.getHtmlBuilder().append(formatTimeslotLog((TimeslotLog)model.getCurrentRowBean(), true));
             
        columnBuilder.tdEnd();
        return columnBuilder.toString();
    }
    
    private String formatTimeslotLog(TimeslotLog log, boolean isHtml) {
		
		StringBuffer strBuf = new StringBuffer();
		
		if(log != null && log.getTimeslotLogDtls() != null) {
			
			try {
				Iterator _itr = log.getTimeslotLogDtls().iterator();
				Date _tmpDate = null;
				TimeslotLogDtl _dtl = null;
				Map<Date, List<TimeslotLogDtl>> dtlMapping = new TreeMap<Date, List<TimeslotLogDtl>>();
				
				while(_itr.hasNext()) {
					_dtl = (TimeslotLogDtl) _itr.next();
					if(!dtlMapping.containsKey(_dtl.getId().getBaseDate())) {
						dtlMapping.put(_dtl.getId().getBaseDate(), new ArrayList<TimeslotLogDtl>());
					}	
					dtlMapping.get(_dtl.getId().getBaseDate()).add(_dtl);
				}
				
				Iterator _keyItr = dtlMapping.keySet().iterator();
				List<TimeslotLogDtl> _tmpLst = null;
				while(_keyItr.hasNext()) {
					_tmpDate = (Date)_keyItr.next();
					if(isHtml) {
						strBuf.append("<b>"+TransStringUtil.getServerDate(_tmpDate)+"</b>").append("=");
					} else {
						strBuf.append(TransStringUtil.getServerDate(_tmpDate)).append("#");
					}
					
					_tmpLst = dtlMapping.get(_tmpDate);
					Iterator<TimeslotLogDtl> _dtlItr = _tmpLst.iterator();
					while(_dtlItr.hasNext()) {
						_dtl = _dtlItr.next();
						if("1".equalsIgnoreCase(_dtl.getId().getIsEmpty())) {
							if(isHtml) {
								strBuf.append("<img src='./images/reddot.gif' border='0' alt='").append(FULL_INDICATOR).append("' title=''>");
							} else {
								strBuf.append(FULL_INDICATOR);
							}
						}						
						strBuf.append(TransStringUtil.formatTimeRange(_dtl.getId().getStartTime(), _dtl.getId().getEndTime()));
						if(_dtlItr.hasNext()) {
							strBuf.append(", ");
						}
					}
					
					if(isHtml) {
						strBuf.append("<br/>");
					} else {
						strBuf.append(" ");
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return strBuf.toString();
	}
}