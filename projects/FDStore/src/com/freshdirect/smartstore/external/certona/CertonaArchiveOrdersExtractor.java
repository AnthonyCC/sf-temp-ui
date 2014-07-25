package com.freshdirect.smartstore.external.certona;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class CertonaArchiveOrdersExtractor {

	static {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) {

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setCoalescing(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(CertonaArchiveOrdersExtractor.class.getResourceAsStream("config.xml"));

			System.out.println("Parsing config.xml...");

			String target_dir = document.getElementsByTagName("target_dir").item(0).getFirstChild().getNodeValue();
			String period = document.getElementsByTagName("period").item(0).getFirstChild().getNodeValue();
			String url = document.getElementsByTagName("url").item(0).getFirstChild().getNodeValue();
			String username = document.getElementsByTagName("username").item(0).getFirstChild().getNodeValue();
			String password = document.getElementsByTagName("password").item(0).getFirstChild().getNodeValue();
			String delimiter = document.getElementsByTagName("delimiter").item(0).getFirstChild().getNodeValue();
			String script = document.getElementsByTagName("script").item(0).getFirstChild().getNodeValue();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss");
			File file = new File(target_dir + "/" + period + "_days_archive_orders_till_" + sdf.format(new Date()) + ".txt");

			if (!file.exists()) {
				file.createNewFile();
			}
 
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			System.out.println("Running query...");

			connection = DriverManager.getConnection(url, username, password);
	 		pstmt = connection.prepareStatement(script);
	 		pstmt.setInt(1, Integer.parseInt(period));
			rset = pstmt.executeQuery();

 			StringBuffer sb = new StringBuffer();
 			
 			
			System.out.println("Writing results into file...");
	 		while (rset.next()) {
	 			
	 			sb.append(rset.getString("order_date")).append(delimiter).append(rset.getString("order_id")).append(delimiter).append(rset.getString("customer_id")).append(delimiter)
	 				.append(rset.getString("email_address")).append(delimiter).append(rset.getString("product_id")).append(delimiter).append(rset.getString("quantity")).append(delimiter)
	 				.append(rset.getString("price")).append("\r\n");
				bw.write(sb.toString());
				sb.setLength(0);
	 		}
	 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
				if (rset != null) {
					rset.close();		
				}
				if (pstmt != null) {
					pstmt.close();		
				}
				if (connection != null) {
					connection.close();		
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
