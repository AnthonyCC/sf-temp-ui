package com.freshdirect.mail.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class SilverpopXmlResponseParser {

	// the main is for debugging only
	public static void main(String[] args) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new FileReader("C:/Users/dheller/Documents/sulverpub/warning1.xml"));
			// File inputFile = new
			// File("C:/Users/dheller/Documents/sulverpub/warning1.xml");

			String line = "";

			while ((line = bufferedReader.readLine()) != null) {

				stringBuffer.append(line);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			SilverpopXmlResponseParser errorParser = new SilverpopXmlResponseParser();
			String response = errorParser.processXmlErrors(stringBuffer.toString().trim());
			System.out.println(" the error response was " + response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String processXmlErrors(String xmlAsString) {
		System.out.println(" processXmlErrors: xml as string \n\r" + xmlAsString);
		StringBuffer responseBuffer = new StringBuffer();
		try {
			// File inputFile = new
			// File("C:/Users/dheller/Documents/sulverpub/warning1.xml");

			InputStream is = new ByteArrayInputStream(xmlAsString.getBytes());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("XTMAILING_RESPONSE");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					responseBuffer
							.append(checkForError(getNodeContents(eElement, "NUMBER_ERRORS"), "0", "NUMBER_ERRORS"));

					responseBuffer.append(checkForError(getNodeContents(eElement, "STATUS"), "0", "STATUS"));

					responseBuffer.append(checkForError(getNodeContents(eElement, "ERROR_CODE"), "0", "ERROR_CODE"));

					responseBuffer.append(checkForError(getNodeContents(eElement, "ERROR_STRING"), "", "ERROR_STRING"));

					NodeList recipientNode = eElement.getElementsByTagName("RECIPIENT_DETAIL");
					Node node = recipientNode.item(0);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element recipientElement = (Element) node;

						responseBuffer.append(
								checkForError(getNodeContents(recipientElement, "SEND_STATUS"), "0", "SEND_STATUS"));

						responseBuffer.append(
								checkForError(getNodeContents(recipientElement, "ERROR_CODE"), "0", "ERROR_CODE"));

						responseBuffer.append(
								checkForError(getNodeContents(recipientElement, "ERROR_STRING"), "", "ERROR_STRING"));

					}
				} // outer if node element
			} // for loop
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBuffer.toString();
	}// method

	private String getNodeContents(Element eElement, String target) {
		String contents = eElement.getElementsByTagName(target).item(0).getTextContent();
		return contents;

	}

	/**
	 * 
	 * @param questionableString
	 * @param targetStr
	 * @param label
	 * @return
	 */
	private String checkForError(String questionableString, String targetStr, String label) {
		String errorString = "";
		System.out.println(String.format("questionable string: %s, target :%s ", questionableString, targetStr));
		if (!questionableString.trim().equalsIgnoreCase(targetStr)) {
			errorString = (label + " =" + questionableString + ", ");
		}

		return errorString;

	}

}
