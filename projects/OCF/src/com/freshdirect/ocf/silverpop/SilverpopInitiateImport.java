package com.freshdirect.ocf.silverpop;

import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author jangela on Oct 28, 2004
 * 
 * Invoke Initiate Import via Silverpop API.
 * Required parameters (4): Username Password ColumnFileName.xml ListFileName.csv
 * 1. Login to silverpop, obtain session ID
 * 2. Write XML, posts to silverpop API, initiate import of uploaded mapping.xml and list.csv files
 * 3. Monitor status. Program ends when status is COMPLETE or returns ERROR.
 * 
 */
public class SilverpopInitiateImport extends SilverpopBase {

	private static String initiateImport(String type, String mapFile, String sourceFile, String listName, String sessionId)
		throws IOException {
		Document initiateDoc = DocumentHelper.createDocument();
		Element initiate = initiateDoc.addElement("Envelope").addElement("Body").addElement("ImportList");
		initiate.addElement("IMPORT_TYPE").addText(type); //0 create new | 3 add & update
		initiate.addElement("MAP_FILE").addText(mapFile);
		initiate.addElement("SOURCE_FILE").addText(sourceFile);
		initiate.addElement("LIST_NAME").addText(listName);

		Document doc = postXml(SERVICE.getUrl(), initiateDoc, sessionId);
		boolean success = Boolean.valueOf(doc.valueOf("/Envelope/Body/RESULT/SUCCESS")).booleanValue();
		if (success) {
			String jobId = doc.valueOf("/Envelope/Body/RESULT/JOB_ID");
			return jobId;
		} else {
			String faultString = doc.valueOf("/Envelope/Body/Fault/FaultString");
			throw new IOException("Silverpop initiate import failed "
				+ mapFile
				+ ", "
				+ sourceFile
				+ ", "
				+ listName
				+ ": "
				+ faultString);
		}
	}

	public static void main(String args[]) throws Exception {
		if (args.length != 4) {
			System.out.println("Usage initiateImport: username password mapFileName.xml listFileName.csv");
			System.exit(1);
		}
		String username = args[0];
		String password = args[1];

		String mapFileName = args[2];
		String listFileName = args[3];

		execute(username, password, mapFileName, listFileName);

	}

	public static void execute(String username, String password, String mapFileName, String listFileName) throws IOException {
		String sessionId = login(username, password);

		if (sessionId != null) {
			String jobId = initiateImport("3", mapFileName, listFileName, "Update File", sessionId);

			// check if loaded 10 s intervals 30 mins max
			boolean ok = false;
			for (int i = 0; i < 180; i++) {
				ok = getJobStatus(sessionId, jobId);
				if (ok) {
					break;
				} else {
					try {
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						return;
					}
				}
			}

			logout(sessionId);
			if (!ok) {
				throw new IOException("JobId " + jobId + " did not complete");
			}
		}
	}

	public static void execute(String mapFileName, String listFileName) throws IOException {
		execute(SERVICE.getUsername(), SERVICE.getPassword(), mapFileName, listFileName);
	}

}