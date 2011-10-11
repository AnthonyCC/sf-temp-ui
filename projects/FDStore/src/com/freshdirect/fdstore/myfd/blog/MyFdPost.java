package com.freshdirect.fdstore.myfd.blog;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyFdPost implements Serializable {
	private static final long serialVersionUID = -3164510601369737759L;
	
	private static final String WP_PRINT = "?print=true";

	private static final int MAX_CHAR = 0xff;
	private static final Map<Character, String> CHAR_REPLACE_MAP = new HashMap<Character, String>();

	static {
		CHAR_REPLACE_MAP.put('\u20AC', "EUR");
		CHAR_REPLACE_MAP.put('\u201A', ",");
		CHAR_REPLACE_MAP.put('\u0192', "f");
		CHAR_REPLACE_MAP.put('\u201E', "\"");
		CHAR_REPLACE_MAP.put('\u2026', "...");
		CHAR_REPLACE_MAP.put('\u2020', "+");
		CHAR_REPLACE_MAP.put('\u2021', "++");
		CHAR_REPLACE_MAP.put('\u02C6', "^");
		CHAR_REPLACE_MAP.put('\u2030', "%o");
		CHAR_REPLACE_MAP.put('\u0160', "S");
		CHAR_REPLACE_MAP.put('\u2039', "<");
		CHAR_REPLACE_MAP.put('\u0152', "OE");
		CHAR_REPLACE_MAP.put('\u017D', "Z");
		CHAR_REPLACE_MAP.put('\u2018', "'");
		CHAR_REPLACE_MAP.put('\u2019', "'");
		CHAR_REPLACE_MAP.put('\u201C', "\"");
		CHAR_REPLACE_MAP.put('\u201D', "\"");
		CHAR_REPLACE_MAP.put('\u2022', "*");
		CHAR_REPLACE_MAP.put('\u2013', "--");
		CHAR_REPLACE_MAP.put('\u2014', "---");
		CHAR_REPLACE_MAP.put('\u02DC', "-");
		CHAR_REPLACE_MAP.put('\u2122', "(TM)");
		CHAR_REPLACE_MAP.put('\u0161', "S");
		CHAR_REPLACE_MAP.put('\u203A', ">");
		CHAR_REPLACE_MAP.put('\u0153', "oe");
		CHAR_REPLACE_MAP.put('\u017E', "z");
		CHAR_REPLACE_MAP.put('\u0178', "Y");
	}
	
	private String title;
	
	private String category;
	private String categoryDescription;
	private String content;
	private String author;
	private String authorLink;
	private String postDayLink;
	private String postLink;
	private String printLink;
	private String emailLink;

	private String categoryLink;
	private ArrayList<String> tagLinks;

	private String media;

	public MyFdPost(Element postElement) throws RuntimeException {
		this.title = postElement.getElementsByTagName("title").item(0).getTextContent();
		try {
			this.category = postElement.getElementsByTagName("fd_category").item(0).getTextContent();			
		} catch (NullPointerException e) {
			this.category = "";
		}
		
		try {
			this.categoryDescription = postElement.getElementsByTagName("fd_category_desc").item(0).getTextContent();			
		} catch (NullPointerException e) {
			this.categoryDescription = this.category;
		}
		
		this.content = "<div>" + postElement.getElementsByTagName("fd_content").item(0).getTextContent() + "</div>";

		parseContent();
		
		this.tagLinks = new ArrayList<String>();
		
		this.author = postElement.getElementsByTagName("dc:creator").item(0).getTextContent();
		
		this.authorLink = postElement.getElementsByTagName("fd_author_link").item(0).getTextContent();		
		this.postDayLink = postElement.getElementsByTagName("fd_post_day_link").item(0).getTextContent();
		this.categoryLink = postElement.getElementsByTagName("fd_category_link").item(0).getTextContent();
		this.postLink = postElement.getElementsByTagName("link").item(0).getTextContent();
		
		this.printLink = postLink + WP_PRINT;
		
		NodeList tagLinkNodes = postElement.getElementsByTagName("fd_tag_link");
		
		for (int i = 0; i < tagLinkNodes.getLength(); ++i) {
			tagLinks.add(tagLinkNodes.item(i).getTextContent());
		}
		
		try {
			this.emailLink = "mailto:?subject=" + URLEncoder.encode(this.title, "ISO-8859-1") + "&body=" + URLEncoder.encode(this.postLink, "ISO-8859-1");
			this.emailLink = this.emailLink.replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			// we know this cannot happen
			throw new RuntimeException(e);
		}
	}
	
	private void parseContent() throws RuntimeException {		
		
		Document document = DOMUtils.stringToNode(content);
		
		NodeList divs = document.getElementsByTagName("div");
		Node imageNode = null;
		
		if (divs.getLength() > 0) {
			for (int i = 0; i < divs.getLength(); ++i) {
				NamedNodeMap attributes = divs.item(i).getAttributes();
				if (attributes !=null &&
					attributes.getNamedItem("class") != null &&
					attributes.getNamedItem("class").getNodeValue() != null &&
					attributes.getNamedItem("class").getNodeValue().contains("wp-caption")) {
					imageNode = divs.item(i);
					break;
				}
			}
		}					
		
		if (imageNode != null) {
			imageNode.getParentNode().removeChild(imageNode);				
			processImageNode((Element) imageNode, document);
			content = DOMUtils.nodeToString(document);
		}
		
		NodeList embed = document.getElementsByTagName("object");
		if (embed.getLength() > 0) {
			Node n = embed.item(0);
			n.getParentNode().removeChild(n);
			media = DOMUtils.nodeToString(n);
			content = DOMUtils.nodeToString(document);
		}		
	}
	
	private void processImageNode(Element imageNode, Document document) {					
		NodeList hrefs = imageNode.getElementsByTagName("a");
		String mediaHref = null;
		if (hrefs.getLength() > 0) {
			NamedNodeMap attributes = hrefs.item(0).getAttributes();
			if (attributes != null &&
				attributes.getNamedItem("href") != null &&
				attributes.getNamedItem("href").getNodeValue() != null &&
				!attributes.getNamedItem("href").getNodeValue().equals("")) {
				
				mediaHref = attributes.getNamedItem("href").getNodeValue();
			}			
		}		
		if (mediaHref == null) {
			media = DOMUtils.nodeToString(imageNode);
			return;
		}
		
		NodeList ps = imageNode.getElementsByTagName("p");		
		if (ps.getLength() > 0) {
			Element caption = (Element) ps.item(0);
			Element link = document.createElement("a");
			link.setAttribute("href", mediaHref);

			// move content
			link.setNodeValue(caption.getNodeValue());
			caption.setNodeValue("");
			List<Node> children = new ArrayList<Node>();
			NodeList childNodes = caption.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++)
				children.add(childNodes.item(i));
			for (Node child : children)
				link.appendChild(caption.removeChild(child));
			
			caption.appendChild(link);					
		}
		media = DOMUtils.nodeToString(imageNode);
	}
		
	private static String convertIllegalChars(String origString){
		StringBuilder newString = new StringBuilder();

		for (char origChar : origString.toCharArray()){
			String newChars = CHAR_REPLACE_MAP.get(origChar);
			
			if (newChars == null ){
				if (origChar <= MAX_CHAR) {
					newString.append(origChar);
				}

			} else {
				newString.append(newChars);	
			}
		}
		
		return newString.toString();
	}
	
	public String getTitle() {
		return convertIllegalChars(title);
	}	

	public String getCategory() {
		return convertIllegalChars(category);
	}
	
	public String getCategoryDescription() {
		return convertIllegalChars(categoryDescription);
	}
	
	public ArrayList<String> getTagLinks() {
		return tagLinks;
	}
	
	public String getContent() {
		return convertIllegalChars(content);
	}
	
	public String getAuthor() {
		return convertIllegalChars(author);
	}
	
	public String getAuthorLink() {
		return authorLink;
	}
	
	public String getPostDayLink() {
		return postDayLink;
	}
	
	public String getCategoryLink() {
		return categoryLink;
	}
	
	public String getPostLink() {
		return postLink;
	}
	
	public String getPrintLink() {
		return printLink;
	}

	public String getEmailLink() {
		return emailLink;
	}

	public String getMedia() {
		return media;
	}
}
