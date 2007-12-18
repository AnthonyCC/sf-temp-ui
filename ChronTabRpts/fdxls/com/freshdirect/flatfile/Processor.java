package com.freshdirect.flatfile;

import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.record.*;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.hssf.usermodel.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.*;
import java.io.*;

import com.freshdirect.xls.datasource.*;

/**
 * This example shows how to use the event API for reading a file.
 */
public class Processor {
	private static HashMap CONTROL_CHARS = new HashMap();
	static {
		CONTROL_CHARS.put("SPACE", " ");
		CONTROL_CHARS.put("TAB", "\t");
		CONTROL_CHARS.put("CR", "\n");
		CONTROL_CHARS.put("CRLF", "\n\r");	
	}
    protected void perform(List operations, FileOutputStream fout) throws Exception {
    	PrintWriter pw = new PrintWriter(fout);

    	Node node = (Node) operations.get(0);

        Node dataSource = node.selectSingleNode("dataSource");
        String delimiter = DataSourceHolder.getChildNodeString(node, "delimiter");
        if(CONTROL_CHARS.get(delimiter) != null) {
        	delimiter = (String) CONTROL_CHARS.get(delimiter);
        }
        String dataSourceName = DataSourceHolder.getChildNodeString(dataSource, "name");
        List params = dataSource.selectNodes("param");

        DataSource ds = DataSourceHolder.getDataSource(dataSourceName);

        ArrayList a = new ArrayList();
        for (Iterator paramIter = params.iterator(); paramIter.hasNext(); ) {
          Node n = (Node) paramIter.next();
          a.add(n.getText().trim());
        }

        String[] paramString = new String[a.size()];
        a.toArray(paramString);

        List data = ds.getMatrixData( paramString );

        for(int i = 0; i < data.size(); i++) {
            List dataRow = (List) data.get(i);
            StringBuffer line = new StringBuffer();
            for(int j = 0; j < dataRow.size(); j++) {
            	if(j > 0)
            		line.append(delimiter);
            	line.append(dataRow.get(j).toString());
            }
            pw.println(line.toString());
        }
        pw.close();
    }

    public void process(FileInputStream config, FileOutputStream fout) throws Exception {
      int avail = config.available();
      byte[] byteArray = new byte[avail];
      config.read(byteArray);
      String configString = new String(byteArray);

      Document configDocument = DocumentHelper.parseText(configString);

      List connections = configDocument.selectNodes("/xlsReporting/sources/source");
      DataSourceHolder.init(connections);

      List operations = configDocument.selectNodes("/xlsReporting/populate/sheet");

      perform(operations, fout);
    }

    /**
     * Read an excel template and spit out new Excel file
     *
     * @param arg0 - Config file to be used for processing
     * @param arg1 - Output processed file
     * @throws IOException  When there is an error processing the file.
     */
    public static void main(String[] args) throws Exception
    {
        // create a new file input stream with the input file specified
        // at the command line
        FileInputStream config = new FileInputStream(args[0]);
        FileOutputStream fout = new FileOutputStream(args[1]);

        Processor processor = new Processor();

        processor.process(config, fout);
    }
}
