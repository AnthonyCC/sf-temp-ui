package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**@author ekracoff on Jul 2, 2004*/
public class ExcelReportServlet extends AbstractExcelReportServlet {
	
	private static Category LOGGER = LoggerFactory.getInstance(ExcelReportServlet.class);

	protected String getWorksheetName() {
		return "Nutrition Report";
	}

	protected List getColumnNames() {
		List cols = new ArrayList();
		cols.add("SKUCODE");
		cols.add("MATERIAL_NUMBER");
		cols.add("DESCRIPTION");
		cols.add("STATUS");
		cols.add("SOURCE");
		cols.add("HAS_NUTRITION");
		cols.add("IS_HIDDEN");
		cols.add("HAS_CLAIMS");
		cols.add("HAS_ALLERGENS");
		cols.add("HAS_ORGANIC");
		cols.add("KOSHER_SYMBOL");
		cols.add("KOSHER_TYPE");
		cols.add("NOTES");
		cols.add("HEATING");
		cols.add("INGREDIENTS");
		
		return cols;
	}

	protected void initialize(HttpServletRequest request) {}
	
	protected List getReport() {
		List nutritionReport = new ArrayList();
		try {
			
			nutritionReport = ErpFactory.getInstance().generateNutritionReport();
		} catch (FDResourceException e) {
			e.printStackTrace();
		}
		
		return nutritionReport;
	}

}
