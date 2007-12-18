package com.freshdirect.erp.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.erp.model.ATPFailureInfo;

public class ATPFailureDAOTestCase extends DbTestCaseSupport {

	public ATPFailureDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "ERPS";
	}

	protected String[] getAffectedTables() {
		return new String[] { "ERPS.ATP_FAILURE" };
	}

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	public void testCreate() throws Exception {
		// setup
		List atpFailureInfos = new ArrayList();

		atpFailureInfos.add(new ATPFailureInfo(DF.parse("2004-01-01"), "000000000100200300", 5.5, "EA", 1.5, ""));
		atpFailureInfos.add(new ATPFailureInfo(DF.parse("2004-01-01"), "000000000100200301", 10, "A05", 0, ""));

		// execute
		new ATPFailureDAO().create(conn, atpFailureInfos);

		// verify
		this.assertPartialDataSet("ATPFailureDAO-one.xml", "ERPS.ATP_FAILURE");
	}

}
