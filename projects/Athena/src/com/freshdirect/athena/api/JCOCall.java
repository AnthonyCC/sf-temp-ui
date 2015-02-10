package com.freshdirect.athena.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.config.Parameter;
import com.freshdirect.athena.data.Data;
import com.freshdirect.athena.data.Row;
import com.freshdirect.athena.data.Variable;
import com.freshdirect.athena.util.TypeUtil;
import com.freshdirect.athena.util.XmlTagUtil;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;

public class JCOCall implements ICall {
	
	private static final Logger LOGGER = Logger.getLogger(JCOCall.class);
	
	JCoRepository repository = null;
	
	public JCOCall(JCoRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Override
	public Data getData(Api api, List<Parameter> params)  {
		
		LOGGER.debug("JCOCall.getData() =>"+api.getName());
		Data result = new Data();
		Variable variable = new Variable(api.getEndpoint());
		result.addVariable(variable);
		String call = api.getCall().replaceAll("\\p{Cntrl}", ""); //Simple XML Parser adds control character to end of CDATA elements.
						
		JCoFunctionTemplate ftemplate = null; 
		JCoFunction function = null;
		
		JCoDestination jcoDestination = null;
		try {
			
			ftemplate = repository.getFunctionTemplate(call);
			function = ftemplate.getFunction();
			
			jcoDestination = JCoDestinationManager.getDestination(api.getDatasource());
							
			if (jcoDestination == null) {
				//throw new Exception("No available connections in JCO pool");
			}
			setParameters(function, params);
			
			function.execute(jcoDestination);

		} catch (AbapException ex) {
			//throw new Exception("BAPI call failed in SAP (" + ex + ")");
			ex.printStackTrace();
		} catch (JCoException ex) {
			//throw new Exception("BAPI call communication failure (" + ex + ")");
			ex.printStackTrace();
		}
		
		JCoTable baseTable = function.getTableParameterList().getTable(0);		
		if (baseTable.getNumRows() > 0) {
			List<Row> rows = new ArrayList<Row>();
			variable.setRow(rows);
			baseTable.firstRow();
		      do {
		    	  Row record = new Row();
				  rows.add(record);		    	  
				  for(int i = 0; i < baseTable.getNumColumns(); i++)
		    	  {  
		    		 JCoFieldIterator fieldIterator = baseTable.getFieldIterator();
		    		 while(fieldIterator.hasNextField()) 
		    		 {
		    			 JCoField iField = fieldIterator.nextField();
		    			 record.addColumn(iField.getValue());
		    		 }
		    	  }
		      } while(baseTable.nextRow());
		}
		
		result = XmlTagUtil.addLastRefresh(result);
		return result;
	}
	
	private void setParameters(JCoFunction function, List<Parameter> params) {
		if(params != null) {
			for(Parameter param : params) {
				switch (param.getParameterType()) {
				case INTEGER:
					function.getImportParameterList().setValue(
							param.getParameterName(),
							TypeUtil.getInt(param.getParameterValue()));
					break;
				case DOUBLE:
					function.getImportParameterList().setValue(
							param.getParameterName(),
							TypeUtil.getDouble(param.getParameterValue()));
					break;
				case FLOAT:
					function.getImportParameterList().setValue(
							param.getParameterName(),
							TypeUtil.getFloat(param.getParameterValue()));
					break;
				case TIMESTAMP:
					function.getImportParameterList().setValue(
							param.getParameterName(),
							TypeUtil.getTimestamp(param.getParameterValue()));
					break;
				case DATE:
					function.getImportParameterList().setValue(
							param.getParameterName(),
							TypeUtil.getDate(param.getValue()));
					break;
				case STRING:
					function.getImportParameterList().setValue(
							param.getParameterName(),
							param.getParameterValue());
					break;
				default:
					function.getImportParameterList().setValue(
							param.getParameterName(),
							param.getParameterValue());
					break;
				}
		}
		}
	}
}
