package com.freshdirect.crm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import com.freshdirect.customer.EnumVSStatus;


public class VoiceShotResponseParser {	
	
	private static final long serialVersionUID = 1L;
	Document xmlDoc;
	int successfulCalls;
	int unsuccessfulCalls;
	int humanAnsweredCalls;
	int answerMachineCalls;
	int totalCalls;
	Hashtable<String,String> phonenumbers = new Hashtable<String,String>();
	
	public int getSuccessfulCalls() {
		return successfulCalls;
	}

	public void setSuccessfulCalls(int successfulCalls) {
		this.successfulCalls = successfulCalls;
	}

	public int getUnsuccessfulCalls() {
		return unsuccessfulCalls;
	}

	public void setUnsuccessfulCalls(int unsuccessfulCalls) {
		this.unsuccessfulCalls = unsuccessfulCalls;
	}

	public int getHumanAnsweredCalls() {
		return humanAnsweredCalls;
	}

	public void setHumanAnsweredCalls(int humanAnsweredCalls) {
		this.humanAnsweredCalls = humanAnsweredCalls;
	}

	public int getAnswerMachineCalls() {
		return answerMachineCalls;
	}

	public void setAnswerMachineCalls(int answerMachineCalls) {
		this.answerMachineCalls = answerMachineCalls;
	}

	public void setTotalCalls(int totalCalls) {
		this.totalCalls = totalCalls;
	}

	public int getTotalCalls() {
		return totalCalls;
	}

	public Hashtable<String, String> getPhonenumbers() {
		return phonenumbers;
	}

	public void setPhonenumbers(Hashtable<String, String> phonenumbers) {
		this.phonenumbers = phonenumbers;
	}

	public VoiceShotResponseParser(String xmlString) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();		
			org.xml.sax.InputSource inStream = new org.xml.sax.InputSource();
			inStream.setCharacterStream(new java.io.StringReader(xmlString.trim()));
			xmlDoc = docBuilder.parse (inStream);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
	}
	
	public String getErrorCode() {
		System.out.println(xmlDoc.getDocumentElement().getAttribute("errorid"));
		return xmlDoc.getDocumentElement().getAttribute("errorid");
	}
	
	public String getErrorMessage() {
		System.out.println(xmlDoc.getDocumentElement().getAttribute("comment"));
		return xmlDoc.getDocumentElement().getAttribute("comment");
	}
	
	public void populateCallData() {
		int successful = 0;
		int answer_machine = 0;
		int human = 0;
		int unsuccessful = 0;
		Hashtable<String, String> phonenumbers = new Hashtable<String,String>();
		org.w3c.dom.NodeList nodeList = xmlDoc.getElementsByTagName("phonenumber");
		this.setTotalCalls(nodeList.getLength());
		for(int index=0; index<nodeList.getLength(); index++) {
			org.w3c.dom.Node node = nodeList.item(index);
			NamedNodeMap attrs = node.getAttributes();  
			String phonenumber = "";
			int status = 0;
		    for(int i = 0 ; i<attrs.getLength() ; i++) {
		        Attr attribute = (Attr)attrs.item(i);     
		        String atr_name = attribute.getName();
		        String value = attribute.getValue();
		        if(atr_name.equalsIgnoreCase("status")) {
		        	if(value.equalsIgnoreCase("Successful")) {
		        		successful++;
		        		//status = EnumVSStatus.SUCCESS.getValue();
		        	} else {
		        		unsuccessful++;
		        		status = EnumVSStatus.UNSUCCESSFUL.getValue();
		        	}
		        } else if(atr_name.equalsIgnoreCase("lasterror")) {
		        	if(value.equalsIgnoreCase("Answering Machine")) {
		        		answer_machine++;
		        		status = EnumVSStatus.ANS_MACHINE.getValue();
		        	} else {
		        		human++;
		        		status = EnumVSStatus.LIVE_ANS.getValue();
		        	}
		        } else if(atr_name.equalsIgnoreCase("number")) {
		        	phonenumber = value;
		        }
		    }
		    phonenumbers.put(phonenumber, status+"");
		}
		this.setSuccessfulCalls(successful);
		this.setUnsuccessfulCalls(unsuccessful);
		this.setAnswerMachineCalls(answer_machine);
		this.setHumanAnsweredCalls(human);	
		this.setPhonenumbers(phonenumbers);
	}
		
}
