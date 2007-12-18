package com.freshdirect.dlvadmin.map;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.freshdirect.delivery.map.Layer;
import com.freshdirect.delivery.map.Polygon;

public class Mapper {


	public void drawMap(Writer w, List layers) throws IOException {
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		//outformat.setEncoding(aEncodingScheme);

		XMLWriter writer = new XMLWriter(w, outformat);
		writer.write( this.drawMap(layers) );
		writer.flush();
	}

	public Document drawMap(List layers) {
		Document document = DocumentHelper.createDocument();

		Map pi = new HashMap();
		pi.put( "type", "text/css" );
		pi.put( "href", "map.css" );
		document.addProcessingInstruction("xml-stylesheet", pi);

		Element root = document.addElement("svg");

		root.addAttribute("width", "1024px");
		root.addAttribute("height", "768px");
		
		root.addAttribute("viewBox", "-740000 -408800 2000 2000");
		root.addAttribute("xmlns", "http://www.w3.org/2000/svg");

		root.addElement("script").addAttribute("type", "text/ecmascript").addAttribute("xlink:href", "map.js");

		for (Iterator i=layers.iterator(); i.hasNext(); ) {
			Layer layer = (Layer)i.next();
			layer.writeLayer(root);
		}
		
		return document;
	}

	private Layer queryZips(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select zipcode, gg.column_value from zipcode z , table(z.geoloc.sdo_ordinates) gg");
		ResultSet rs = ps.executeQuery();

		Layer polys = this.transform(rs, "zip");

		rs.close();
		ps.close();
		return polys;
	}

	private Layer queryZones(Connection conn, String region) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(
			"SELECT z.NAME, gg.column_value FROM DLV.REGION_DATA rd, DLV.ZONE z, table(z.geoloc.sdo_ordinates) gg"+
			" WHERE rd.REGION_ID=?" +  
			" AND rd.START_DATE = (SELECT MAX(START_DATE) FROM DLV.REGION_DATA WHERE START_DATE <= sysdate AND REGION_ID=?)"+
			" AND z.REGION_DATA_ID = rd.ID");
		ps.setString(1, region);
		ps.setString(2, region);
		ResultSet rs = ps.executeQuery();

		Layer polys = this.transform(rs, "zone");
		
		rs.close();
		ps.close();
		return polys;
	}
	
		
	private final static int X_SCALE = 10000;
	private final static int Y_SCALE = -10000;

	private Layer transform(ResultSet rs, String klazz) throws SQLException {
		String zip = "";
		List polygons = new ArrayList(); 

		List coords = null; 
		while (rs.next()) {
			String currentZip = rs.getString(1);
			if (!zip.equals( currentZip )) {
				if (coords!=null) {
					polygons.add( new Polygon(zip, coords) );	
				}
				zip = currentZip; 
				coords = new ArrayList();
			}
			
			int[] c = new int[2];
			c[0] = (int)Math.round( rs.getDouble(2) * X_SCALE );
			rs.next();
			c[1] = (int)Math.round( rs.getDouble(2) * Y_SCALE );
			
			coords.add(c);
		}

		polygons.add( new Polygon(zip, coords) );	
		
		return new Layer(polygons, klazz);
	}

/*
  <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 20010904//EN"
  "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd">
 */
	public static void main(String[] args) throws Exception {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection c = DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV02", "DLV", "DLV");

		Mapper m = new Mapper();

		List layers = new ArrayList();

		layers.add( m.queryZips(c) );
		layers.add( m.queryZones(c, "146") );
		
		FileWriter fw = new FileWriter("docroot/map/map.svg");
		
		m.drawMap(fw, layers);
		
		c.close();

	}


}
