package com.freshdirect.fdstore.myfd.blog;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyFdPost implements Serializable {
	private static final long serialVersionUID = -3164510601369737759L;
	
	private static final String WP_PRINT = "?print=true";
	
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
		
	
	public String getTitle() {
		return title;
	}	

	public String getCategory() {
		return category;
	}
	
	public String getCategoryDescription() {
		return categoryDescription;
	}
	
	public ArrayList<String> getTagLinks() {
		return tagLinks;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getAuthor() {
		return author;
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
