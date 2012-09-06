package com.freshdirect.athena.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.athena.AthenaServlet;
import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.config.Parameter;
import com.freshdirect.athena.data.Data;
import com.freshdirect.athena.data.Row;
import com.freshdirect.athena.data.Variable;
import com.freshdirect.athena.util.TypeUtil;
import com.freshdirect.athena.util.XmlTagUtil;
import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;

public class JCOCall implements ICall {
	
	private static final Logger LOGGER = Logger.getLogger(JCOCall.class);
	
	IRepository repository = null;
	
	public JCOCall(IRepository repository) {
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
						
		IFunctionTemplate ftemplate = repository.getFunctionTemplate(call);
		JCO.Function function = new JCO.Function(ftemplate);
		
		JCO.Client sapClient = null;
		try {
			
			sapClient = JCO.getClient(api.getDatasource());
							
			if (sapClient == null) {
				//throw new Exception("No available connections in JCO pool");
			}
			setParameters(function, params);
			
			sapClient.execute(function);

		} catch (JCO.AbapException ex) {
			//throw new Exception("BAPI call failed in SAP (" + ex + ")");
			ex.printStackTrace();
		} catch (JCO.Exception ex) {
			//throw new Exception("BAPI call communication failure (" + ex + ")");
			ex.printStackTrace();
		} finally {
			if (sapClient != null) {
				JCO.releaseClient(sapClient);
			}
		}
		
		JCO.Table baseTable = function.getTableParameterList().getTable(0);		
		if (baseTable.getNumRows() > 0) {
			List<Row> rows = new ArrayList<Row>();
			variable.setRow(rows);
			baseTable.firstRow();
		      do {
		    	  Row record = new Row();
				  rows.add(record);
		    	  for(int i = 0; i<baseTable.getNumColumns(); i++) {
		    		  JCO.Field iField = baseTable.getField(i);
		    		  record.addColumn(iField.getValue());
		    	  }
		      } while(baseTable.nextRow());
		}
		
		result = XmlTagUtil.addLastRefresh(result);
		return result;
	}
	
	private void setParameters(JCO.Function function, List<Parameter> params) {
		if(params != null) {
			for(Parameter param : params) {
				switch (param.getParameterType()) {
				case INTEGER:
					function.getImportParameterList().setValue(
							TypeUtil.getInt(param.getParameterValue()),
							param.getParameterName());
					break;
				case DOUBLE:
					function.getImportParameterList().setValue(
							TypeUtil.getDouble(param.getParameterValue()),
							param.getParameterName());
					break;
				case FLOAT:
					function.getImportParameterList().setValue(
							TypeUtil.getFloat(param.getParameterValue()),
							param.getParameterName());
					break;
				case TIMESTAMP:
					function.getImportParameterList().setValue(
							TypeUtil.getTimestamp(param.getParameterValue()),
							param.getParameterName());
					break;
				case DATE:
					function.getImportParameterList().setValue(
							TypeUtil.getDate(param.getValue()),
							param.getParameterName());
					break;
				case STRING:
					function.getImportParameterList().setValue(
							param.getParameterValue(),
							param.getParameterName());
					break;
				default:
					function.getImportParameterList().setValue(
							param.getParameterValue(),
							param.getParameterName());
					break;
				}
		}
		}
	}
}
