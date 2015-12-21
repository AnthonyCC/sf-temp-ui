/**
 * 
 */
package com.freshdirect.fdstore.ewallet.impl;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.mastercard.mcwallet.sdk.MasterPassServiceRuntimeException;

/**
 * @author Aniwesh 
 *
 */
public class MasterPassApplicationHelper {
	/**
	 * Method to indent and format XML strings to be displayed.
	 * 
	 * @param input
	 * @param indent
	 * 
	 * @return Formatted XML string
	 */
	private static String prettyFormat(String input, String indent) {
	    try {
	    	//
	    	if (input == null || input.equals("")) { 
	    		return input;
	    	}
	    	input = input.replace(">  <", "><");
	    	if (input.contains("<html>") ) {
	    		return input;
	    	}
	        Source xmlInput = new StreamSource(new StringReader(input));
	        StringWriter stringWriter = new StringWriter();
	        StreamResult xmlOutput = new StreamResult(stringWriter);
	        
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",indent);
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.transform(xmlInput, xmlOutput);
	        
	        return xmlOutput.getWriter().toString();
	    } catch (Exception e) {
	    	throw new MasterPassServiceRuntimeException(e);
	    }
	}
	
	public static String prettyFormat(String input) {
	    return prettyFormat(input,"4");
	}
	
	/**
	 * Converts a MerchantTransactions to a String containing all the data in the class in XML format
	 * 
	 * @param merchantTransactions
	 * 
	 * @return Marshaled string containing the data stored in merchantTransactions in an XML format
	 * 
	 * @throws JAXBException
	 */
	public static String printXML(Object xmlClass) {

			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(xmlClass.getClass());
				StringWriter st = new StringWriter();
				jaxbContext.createMarshaller().marshal(xmlClass, st);
				String xml = st.toString();
				return xml;
				
			} catch (JAXBException e) {
				throw new MasterPassServiceRuntimeException(e);
			}
	}
	
	/**
	 * Method to escape any HTML tags for displaying the XML in a web page.
	 * This method is for displaying the data only.
	 * 
	 * @param t
	 * 
	 * @return String with the escaped XML
	 */
	public static String xmlEscapeText(String t) {

		if(t!= null){
		StringBuilder sb = new StringBuilder();
		   for(int i = 0; i < t.length(); i++){
		      char c = t.charAt(i);
		      switch(c){
		      case '<': sb.append("&lt;"); break;
		      case '>': sb.append("&gt;"); break;
		      case '\"': sb.append("&quot;"); break;
		      case '&': sb.append("&amp;"); break;
		      case '\'': sb.append("&apos;"); break;
		      default:
//		         if(c>0x7e) {
//		            sb.append("&#"+((int)c)+";");
//		         }else
		            sb.append(c);
		      }
		   }
		   return sb.toString();
		   }
		return "";
	}
	
	
	  static Map<String, Integer> e2i = new HashMap<String, Integer>();

	  static Map<Integer, String> i2e = new HashMap<Integer, String>();
	  // html entity list
	  private static Object[][] entities = { { "quot", new Integer(34) }, // " - double-quote
	      { "copy", new Integer(169) }, // © - copyright
	      { "reg", new Integer(174) }, // ® - registered trademark
	      { "Agrave", new Integer(192) }, // À - uppercase A, grave accent
	      { "Aacute", new Integer(193) }, // Á - uppercase A, acute accent
	      { "Acirc", new Integer(194) }, // Â - uppercase A, circumflex accent
	      { "Atilde", new Integer(195) }, // Ã - uppercase A, tilde
	      { "Auml", new Integer(196) }, // Ä - uppercase A, umlaut
	      { "Aring", new Integer(197) }, // Å - uppercase A, ring
	      { "AElig", new Integer(198) }, // Æ - uppercase AE
	      { "Ccedil", new Integer(199) }, // Ç - uppercase C, cedilla
	      { "Egrave", new Integer(200) }, // È - uppercase E, grave accent
	      { "Eacute", new Integer(201) }, // É - uppercase E, acute accent
	      { "Ecirc", new Integer(202) }, // Ê - uppercase E, circumflex accent
	      { "Euml", new Integer(203) }, // Ë - uppercase E, umlaut
	      { "Igrave", new Integer(204) }, // Ì - uppercase I, grave accent
	      { "Iacute", new Integer(205) }, // Í - uppercase I, acute accent
	      { "Icirc", new Integer(206) }, // Î - uppercase I, circumflex accent
	      { "Iuml", new Integer(207) }, // Ï - uppercase I, umlaut
	      { "ETH", new Integer(208) }, // Ð - uppercase Eth, Icelandic
	      { "Ntilde", new Integer(209) }, // Ñ - uppercase N, tilde
	      { "Ograve", new Integer(210) }, // Ò - uppercase O, grave accent
	      { "Oacute", new Integer(211) }, // Ó - uppercase O, acute accent
	      { "Ocirc", new Integer(212) }, // Ô - uppercase O, circumflex accent
	      { "Otilde", new Integer(213) }, // Õ - uppercase O, tilde
	      { "Ouml", new Integer(214) }, // Ö - uppercase O, umlaut
	      { "Oslash", new Integer(216) }, // Ø - uppercase O, slash
	      { "Ugrave", new Integer(217) }, // Ù - uppercase U, grave accent
	      { "Uacute", new Integer(218) }, // Ú - uppercase U, acute accent
	      { "Ucirc", new Integer(219) }, // Û - uppercase U, circumflex accent
	      { "Uuml", new Integer(220) }, // Ü - uppercase U, umlaut
	      { "Yacute", new Integer(221) }, // Ý - uppercase Y, acute accent
	      { "THORN", new Integer(222) }, // Þ - uppercase THORN, Icelandic
	      { "szlig", new Integer(223) }, // ß - lowercase sharps, German
	      { "agrave", new Integer(224) }, // à - lowercase a, grave accent
	      { "aacute", new Integer(225) }, // á - lowercase a, acute accent
	      { "acirc", new Integer(226) }, // â - lowercase a, circumflex accent
	      { "atilde", new Integer(227) }, // ã - lowercase a, tilde
	      { "auml", new Integer(228) }, // ä - lowercase a, umlaut
	      { "aring", new Integer(229) }, // å - lowercase a, ring
	      { "aelig", new Integer(230) }, // æ - lowercase ae
	      { "ccedil", new Integer(231) }, // ç - lowercase c, cedilla
	      { "egrave", new Integer(232) }, // è - lowercase e, grave accent
	      { "eacute", new Integer(233) }, // é - lowercase e, acute accent
	      { "ecirc", new Integer(234) }, // ê - lowercase e, circumflex accent
	      { "euml", new Integer(235) }, // ë - lowercase e, umlaut
	      { "igrave", new Integer(236) }, // ì - lowercase i, grave accent
	      { "iacute", new Integer(237) }, // í - lowercase i, acute accent
	      { "icirc", new Integer(238) }, // î - lowercase i, circumflex accent
	      { "iuml", new Integer(239) }, // ï - lowercase i, umlaut
	      { "igrave", new Integer(236) }, // ì - lowercase i, grave accent
	      { "iacute", new Integer(237) }, // í - lowercase i, acute accent
	      { "icirc", new Integer(238) }, // î - lowercase i, circumflex accent
	      { "iuml", new Integer(239) }, // ï - lowercase i, umlaut
	      { "eth", new Integer(240) }, // ð - lowercase eth, Icelandic
	      { "ntilde", new Integer(241) }, // ñ - lowercase n, tilde
	      { "ograve", new Integer(242) }, // ò - lowercase o, grave accent
	      { "oacute", new Integer(243) }, // ó - lowercase o, acute accent
	      { "ocirc", new Integer(244) }, // ô - lowercase o, circumflex accent
	      { "otilde", new Integer(245) }, // õ - lowercase o, tilde
	      { "ouml", new Integer(246) }, // ö - lowercase o, umlaut
	      { "oslash", new Integer(248) }, // ø - lowercase o, slash
	      { "ugrave", new Integer(249) }, // ù - lowercase u, grave accent
	      { "uacute", new Integer(250) }, // ú - lowercase u, acute accent
	      { "ucirc", new Integer(251) }, // û - lowercase u, circumflex accent
	      { "uuml", new Integer(252) }, // ü - lowercase u, umlaut
	      { "yacute", new Integer(253) }, // ý - lowercase y, acute accent
	      { "thorn", new Integer(254) }, // þ - lowercase thorn, Icelandic
	      { "yuml", new Integer(255) }, // ÿ - lowercase y, umlaut
	      { "euro", new Integer(8364) },// Euro symbol
	      { "amp", new Integer(38) },// & - ampersand
	  };
	
	  static {
	    for (int i = 0; i < entities.length; i++)
	        e2i.put((String) entities[i][0], (Integer) entities[i][1]);
	    for (int i = 0; i < entities.length; i++)
	        i2e.put((Integer) entities[i][1], (String) entities[i][0]);
	  }
	public static String replaceSpecialCharsWithBlanks(String s1) {
	    StringBuffer buf = new StringBuffer();

	    int i;
	    boolean amp = false;
	    for (i = 0; i < s1.length(); ++i)
	    {

	      char ch = ' ';
	      if (amp) {
	    	  ch = '&';
	    	  i--;
	      } else {
		      ch = s1.charAt(i);
		      amp = false;
	      }
	      if (ch == '&')
	      {
	        int semi = s1.indexOf(';', i + 1);
	        if (semi == -1)
	        {
	          buf.append(ch);
	          continue;
	        }
	        String entity = s1.substring(i + 1, semi);
	        Integer iso;
	        if (entity.charAt(0) == '#')
	        {
	          iso = new Integer(entity.substring(1));
	        }
	        else
	        {
	          iso = e2i.get(entity);
	        }
	        if (iso == null)
	        {
	          buf.append("&" + entity + ";");
	          amp = false;
	        }
	        else if (iso == 38) {
	        	amp = true;
	        }
	        else
	        {
	          //Just for info that empty char is introduced.
	          buf.append("");
	          amp = false;
	        }
	        i = semi;
	      }
	      else
	      {
	        buf.append(ch);
	      }
	    }

	    return buf.toString();
	}
	
	/**
	 * Method to format the error messages that will be displayed in the test app.
	 * This method is for displaying only.
	 * 
	 * @param errorMessage
	 * 
	 * @return
	 */
	public static String formatErrorMessage(String errorMessage) {
		if (errorMessage.contains("<Errors>")) {
			return xmlEscapeText(prettyFormat(errorMessage));
		}
		else {
			return errorMessage;
		}
	}
	/**
	 * This method is used only to make the shipping image URL dynamic when changing the app URL and context.
	 * This method is not intented to be used in a production application or environment.
	 * 
	 * @param  shoppingCartRequest
	 * @param  data
	 * 
	 * @return String with the replaced image URLs
	 */
//	public static String xmlReplaceImageUrl(String shoppingCartRequest,MasterpassData data) {
//		return shoppingCartRequest.replaceAll("http://ech-0a9d8167.corp.mastercard.test:8080/SampleApp",data.getAppBaseUrl() + data.getContextPath() );
//	}
	
	public static String xmlReplaceImageUrl(String shoppingCartRequest,MasterpassData data) {
		return shoppingCartRequest.replaceAll("http://ech-0a9d8167.corp.mastercard.test:8080/SampleApp",data.getAppBaseUrl() + data.getContextPath() );
	}
}
