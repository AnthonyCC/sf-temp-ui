package com.freshdirect.xls;

import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.record.*;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.hssf.usermodel.*;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.freshdirect.xls.datasource.DataSource;
import com.freshdirect.xls.datasource.DataSourceHolder;

import java.util.*;
import java.io.*;

/**
 * This example shows how to use the event API for reading a file.
 */
public class Processor {
    protected void perform(List operations, FileInputStream fin, FileOutputStream fout) throws Exception {
      POIFSFileSystem poifs = new POIFSFileSystem(fin);
        
      HSSFWorkbook wb = new HSSFWorkbook(poifs);

      for (Iterator iter = operations.iterator(); iter.hasNext(); ) {
        Node node = (Node) iter.next();

        String sheetName = DataSourceHolder.getChildNodeString(node, "name");

        String offsetRowString = DataSourceHolder.getChildNodeString(node, "beginOffsetRow");
        String offsetColumnString = DataSourceHolder.getChildNodeString(node, "beginOffsetColumn");

        int offsetRow = Integer.parseInt(offsetRowString);
        int offsetColumn = Integer.parseInt(offsetColumnString);

        Node dataSource = node.selectSingleNode("dataSource");
        String dataSourceName = DataSourceHolder.getChildNodeString(dataSource, "name");
        List params = dataSource.selectNodes("param");

        DataSource ds = DataSourceHolder.getDataSource(dataSourceName);

        ArrayList a = new ArrayList();
        System.out.println("Iterating for: " + sheetName);
        for (Iterator paramIter = params.iterator(); paramIter.hasNext(); ) {
          Node n = (Node) paramIter.next();
          a.add(n.getText().trim());
        }

        String[] paramString = new String[a.size()];
        a.toArray(paramString);

        List data = ds.getMatrixData( paramString );

        HSSFSheet sheet = wb.getSheet(sheetName);

        for(int i = 0; i < data.size(); i++) {
          int curRowNum = offsetRow + i;
          HSSFRow row = sheet.getRow(curRowNum);
          if(row == null)
            row = sheet.createRow(curRowNum);

          List dataRow = (List) data.get(i);
          for(int j = 0; j < dataRow.size(); j++) {
            int curColumnNum = offsetColumn + j;
            HSSFCell cell = row.getCell((short) curColumnNum);
            Object cellData = dataRow.get(j);
            if(cellData == null) continue;

            int cellType;
            if(cell == null) {
              cell = row.createCell((short) curColumnNum);
            }
            cellType = cell.getCellType();

            if(cellType == HSSFCell.CELL_TYPE_BLANK) {
              if( (cellData instanceof Double) || (cellData instanceof Date) )
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
              else
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            }

            cellType = cell.getCellType();
            boolean done = false;
            if(cellType == HSSFCell.CELL_TYPE_NUMERIC) {
              if(cellData instanceof Double) {
                cell.setCellValue( ((Double) cellData).doubleValue());
                done = true;
              }
              else if(cellData instanceof Date) {
                cell.setCellValue((Date) cellData);
                done = true;
              }
            }

            if(!done) {
              //Force
              row.removeCell(cell);
              //System.out.println("Unrecognized or String type for column: " + curColumnNum + " data: " + cellData.toString());
              cell = row.createCell((short) curColumnNum);
              cell.setCellValue(cellData.toString());
            }
          }
        }
      }
      wb.write(fout);
    }

    public void process(FileInputStream fin, FileInputStream config, FileOutputStream fout) throws Exception {
      int avail = config.available();
      byte[] byteArray = new byte[avail];
      config.read(byteArray);
      String configString = new String(byteArray);

      Document configDocument = DocumentHelper.parseText(configString);

      List connections = configDocument.selectNodes("/xlsReporting/sources/source");
      DataSourceHolder.init(connections);

      List operations = configDocument.selectNodes("/xlsReporting/populate/sheet");

      perform(operations, fin, fout);
    }

    /**
     * Read an excel template and spit out new Excel file
     *
     * @param arg0 - Input XLS Template
     * @param arg1 - Config file to be used for processing
     * @param arg2 - Output processed XLS
     * @throws IOException  When there is an error processing the file.
     */
    public static void main(String[] args) throws Exception
    {
        // create a new file input stream with the input file specified
        // at the command line
        FileInputStream fin = new FileInputStream(args[0]);
        FileInputStream config = new FileInputStream(args[1]);
        FileOutputStream fout = new FileOutputStream(args[2]);

        Processor processor = new Processor();

        processor.process(fin, config, fout);
    }
}
