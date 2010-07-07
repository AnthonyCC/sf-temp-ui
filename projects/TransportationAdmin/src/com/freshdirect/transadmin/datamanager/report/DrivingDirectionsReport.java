package com.freshdirect.transadmin.datamanager.report;

import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.extremecomponents.table.view.ExportViewUtils;

import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.IDrivingDirectionArc;
import com.freshdirect.routing.model.IPathDirection;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.transadmin.datamanager.RouteFileManager;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DrivingDirectionsReport {
	
	public final static String FONT = "Arial";
    public final static String HEADER_BACKGROUND_COLOR = "white";
    public final static String HEADER_TITLE = "Route Directions for Route ";
    public final static String HEADER_COLOR = "red";
    
    public final static String SECTION_COLOR = "blue";

    private StringBuffer xlsfo = new StringBuffer();
    
    public DrivingDirectionsReport() {
    }
    
    public void generateError(OutputStream out, String message ) 
							throws ReportGenerationException {
    	xlsfo.append(startRoot());
    	xlsfo.append(regionBefore(message));
    	xlsfo.append(endRoot());
    	new RouteFileManager().generatePdfReportFile(out, xlsfo.toString());
    }
    public void generateDrivingDirectionsReport(OutputStream out, Map reportData ) 
											throws ReportGenerationException {
    	xlsfo.append(startRoot());
    	    	
    	if(reportData != null && reportData.keySet() != null) {			
			Iterator iterator = reportData.keySet().iterator();
			IRouteModel _route = null;
			IDrivingDirection _directions = null;
			Object[] _stops = null;
			
			IRoutingStopModel _nextStop = null;
			String _baseRouteId = null;
			
			while(iterator.hasNext()) {
				_baseRouteId = (String)iterator.next();
				_route = (IRouteModel)(reportData.get(_baseRouteId));
				if(_route != null) {
					_directions = _route.getDrivingDirection();
					_stops = _route.getStops().toArray();
						
					xlsfo.append(regionBefore(_baseRouteId, _route.getRouteStartTime()));
					if(_directions != null && _directions.getPathDirections() != null) {					
				        
				        List paths = _directions.getPathDirections();
				        xlsfo.append("<fo:block color=\""+SECTION_COLOR+"\">").append("Depart ")
				        					.append(TransStringUtil.formatTime(_route.getRouteStartTime()))
				        					.append(" - DPT/FD").append("</fo:block>");	
				        
				        for(int intCount=0; intCount < paths.size(); intCount++) {
				        	List arcs = ((IPathDirection)paths.get(intCount)).getDirectionsArc();
				        		        		
				        	if(arcs != null) {
				        		
								xlsfo.append(columnDefinitions());
						        xlsfo.append(header());
						        xlsfo.append(" <fo:table-body> ");
								
						        body(arcs);					        
								
								xlsfo.append(" </fo:table-body> ");
						        xlsfo.append(afterHeader());
						        
				        	} 
				        	if(_stops.length > intCount+1) {
			        			_nextStop = (IRoutingStopModel)_stops[intCount+1];
			        			if(_nextStop != null) {
				        			if(!_nextStop.isDepot()) {
					        			xlsfo.append("<fo:block color=\""+SECTION_COLOR+"\">").append("Arrive")
					        			.append(": ").append(TransStringUtil.formatTime(_nextStop.getStopArrivalTime()))
					        			.append("   Stop ").append(intCount+1)			        			
					        			.append(" - [ ")
						        		.append(_nextStop.getLocation().getBuilding().getStreetAddress1())
						        						.append(", ").append(_nextStop.getLocation().getBuilding().getCity())
						        						.append(", ").append(_nextStop.getLocation().getBuilding().getState())
						        						.append(", ").append(_nextStop.getLocation().getBuilding().getZipCode())
						        						.append(" ]");
					        			xlsfo.append("</fo:block>");
					        			
					        			/*xlsfo.append("<fo:block font-size=\"7pt\" color=\""+SECTION_COLOR+"\">")
						        						.append("Time Window: ")
						        						.append(TransStringUtil.formatTimeRange(_nextStop.getTimeWindowStart()
						        																, _nextStop.getTimeWindowStop()));
					        			xlsfo.append("</fo:block>");*/
						        	}
					        		
			        			}
			        		}
				        	
				        }
				        xlsfo.append("<fo:block color=\""+SECTION_COLOR+"\">").append("Arrive DPT/FD").append("</fo:block>");
					}
					xlsfo.append(regionAfter(_route.getRouteId()));
				} else {
					 xlsfo.append("<fo:block color=\""+SECTION_COLOR+"\">")
					 				.append("Driving Directions not available for route ")
					 					.append(_baseRouteId)
					 					.append(". Please contact the routing team for more information.")
					 							.append("</fo:block>");
				}
			}
    	}
    	xlsfo.append(endRoot());
    	//System.out.println(xlsfo.toString());
    	new RouteFileManager().generatePdfReportFile(out, xlsfo.toString());
    }
    

    public void body(List arcs) {
    	for(int j=0;j < arcs.size();j++) {
    		IDrivingDirectionArc _arc = (IDrivingDirectionArc)arcs.get(j)
;	    	xlsfo.append(" <fo:table-row> ");
	    	
	    	if(arcs != null) {
	    		//xlsfo.append(" <fo:table-cell border=\"solid silver .5px\" display-align=\"center\" padding=\"3pt\"> ");
	    		xlsfo.append(" <fo:table-cell display-align=\"center\" padding-left=\"5pt\" padding-top=\"1pt\"> ");
	            xlsfo.append(" <fo:block>" + ExportViewUtils.parsePDF(_arc.getInstruction()) + "</fo:block> ");
	            xlsfo.append(" </fo:table-cell> ");
	            
	            xlsfo.append(" <fo:table-cell display-align=\"center\" padding-top=\"1pt\"> ");
	            xlsfo.append(" <fo:block>" + ExportViewUtils.parsePDF(""
	            					+TransStringUtil.formatTwoDigitNumber(_arc.getDistance())) + "</fo:block> ");
	            xlsfo.append(" </fo:table-cell> ");
	            
	            xlsfo.append(" <fo:table-cell display-align=\"center\" padding-top=\"1pt\"> ");
	            xlsfo.append(" <fo:block>" + ExportViewUtils.parsePDF(""+TransStringUtil.calcHMS(_arc.getTime())) + "</fo:block> ");
	            xlsfo.append(" </fo:table-cell> ");
	    	}
	        
	
	        xlsfo.append(" </fo:table-row> ");
    	}
    }

    
    public String startRoot() {
        StringBuffer sb = new StringBuffer();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        sb.append("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">");

        sb.append(" <fo:layout-master-set> ");
        sb.append(" <fo:simple-page-master master-name=\"simple\" ");
        sb.append(" page-height=\"8.3in\" ");
        sb.append(" page-width=\"11.7in\" ");
        sb.append(" margin-top=\".25in\" ");
        sb.append(" margin-bottom=\".1in\" ");
        sb.append(" margin-left=\".5in\" ");
        sb.append(" margin-right=\".25in\"> ");
        //sb.append(" <fo:simple-page-master master-name=\"A4-portrait\" page-height=\"29.7cm\" page-width=\"21.0cm\" margin=\"2cm\"> ");

        sb.append(" <fo:region-body margin-top=\".25in\" margin-bottom=\".25in\"/> ");
        sb.append(" <fo:region-before extent=\".25in\"/> ");
        sb.append(" <fo:region-after extent=\".1in\"/> ");
        sb.append(" </fo:simple-page-master> ");
        sb.append(" </fo:layout-master-set> ");

        sb.append(" <fo:page-sequence master-reference=\"simple\" initial-page-number=\"1\"> ");
        
        sb.append(" <fo:flow flow-name=\"xsl-region-body\"> ");
        
        return sb.toString();
    }

    public String regionBefore(String routeId, Date routeStartTime) {
        
    	StringBuffer sb = new StringBuffer();
        
        String title = HEADER_TITLE + routeId;

        sb.append(" <fo:block break-before=\"page\" background-color=\"" 
        						+ HEADER_BACKGROUND_COLOR + "\" color=\"" + HEADER_COLOR 
        							+ "\" font-size=\"17pt\" >" + title + "</fo:block> ");
        
        if(routeStartTime != null) {
	        sb.append(" <fo:block color=\"" + HEADER_COLOR 
						+ "\" font-size=\"11pt\" space-after.optimum=\"9pt\" >" + "Route Start Time: "
								+TransStringUtil.formatTime(routeStartTime) + "</fo:block> ");
        }

        
        return sb.toString();
    }
    
    public String regionBefore(String message) {
        
    	StringBuffer sb = new StringBuffer();
       
    	sb.append(" <fo:block break-before=\"page\" background-color=\"" 
        						+ HEADER_BACKGROUND_COLOR + "\" color=\"" + HEADER_COLOR 
        							+ "\" font-size=\"17pt\" >" + message + "</fo:block> ");
      
        
        return sb.toString();
    }

    public String regionAfter(String routeId) {
        
    	StringBuffer sb = new StringBuffer();
        return sb.toString();
    }

    public String columnDefinitions() {
        StringBuffer sb = new StringBuffer();
        sb.append("<fo:block space-before.optimum=\"5pt\" space-after.optimum=\"4pt\"></fo:block>");
        sb.append(" <fo:table table-layout=\"fixed\" font-size=\"11pt\"> ");

        sb.append(" <fo:table-column column-number=\"" + 1 + "\" column-width=\"" + 8.5 + "in\"/> ");
        sb.append(" <fo:table-column column-number=\"" + 2 + "\" column-width=\"" + 2.0 + "in\"/> ");
        //sb.append(" <fo:table-column column-number=\"" + 3 + "\" column-width=\"" + 1.5 + "in\"/> ");

        return sb.toString();
    }

    public String header() {
    	
        StringBuffer sb = new StringBuffer();
        
        sb.append(" <fo:table-header background-color=\"" + HEADER_BACKGROUND_COLOR + "\" color=\"" + HEADER_COLOR + "\"> ");

        sb.append(" <fo:table-row> ");

        //sb.append(" <fo:table-cell border-bottom=\"solid black .5px\" text-align=\"center\" display-align=\"center\" padding=\"3pt\"> ");
        sb.append(" <fo:table-cell border-bottom=\"solid black .5px\" > ");
        sb.append(" <fo:block>" + "Directions" + "</fo:block> ");
        sb.append(" </fo:table-cell> ");
        
        sb.append(" <fo:table-cell border-bottom=\"solid black .5px\" > ");
        sb.append(" <fo:block>" + "Distance" + "</fo:block> ");
        sb.append(" </fo:table-cell> ");
        
        /*sb.append(" <fo:table-cell border-bottom=\"solid black .5px\" > ");
        sb.append(" <fo:block>" + "Time" + "</fo:block> ");
        sb.append(" </fo:table-cell> ");*/

        sb.append(" </fo:table-row> ");

        sb.append(" </fo:table-header> ");

        return sb.toString();
    }
    
    public String afterHeader() {
    	
    	StringBuffer sb = new StringBuffer();
        sb.append(" </fo:table> ");        
        return sb.toString();
    }

    public String endRoot() {
    	
        StringBuffer sb = new StringBuffer();    
        sb.append(" </fo:flow> ");
        sb.append(" </fo:page-sequence> ");
        sb.append(" </fo:root> ");

        return sb.toString();
    }

    protected String getFont() {
        return FONT == null ? "" : " font-family=\"" + FONT + "\"";
    }

    protected String getHeadFont() {
        return FONT == null ? "" : FONT + ",";
    }

}
