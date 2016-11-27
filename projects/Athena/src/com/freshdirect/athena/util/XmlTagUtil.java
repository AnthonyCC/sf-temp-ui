package com.freshdirect.athena.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.athena.data.Data;
import com.freshdirect.athena.data.Row;
import com.freshdirect.athena.data.Variable;

public class XmlTagUtil {

	public static Data addLastRefresh(Data result)
	{
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		Variable lastRefresh = new Variable("lastRefresh");
		List<Row> rows = new ArrayList<Row>();
		Row row = new Row();
		row.addColumn(sdf.format(new Date()));
		rows.add(row);
		lastRefresh.setRow(rows);
		result.addVariable(lastRefresh);
		return result;
	}
}
