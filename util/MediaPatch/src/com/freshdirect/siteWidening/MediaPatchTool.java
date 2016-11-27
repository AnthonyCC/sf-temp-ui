package com.freshdirect.siteWidening;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MediaPatchTool {
	public static class Dimension {
		private int width;
		private int height;
		boolean updated;

		public Dimension(int width, int height) {
			super();
			this.width = width;
			this.height = height;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + height;
			result = prime * result + width;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Dimension other = (Dimension) obj;
			if (height != other.height)
				return false;
			if (width != other.width)
				return false;
			return true;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public void setUpdated(boolean updated) {
			this.updated = updated;
		}

		public boolean isUpdated() {
			return updated;
		}
	}

	private static Map<String, Dimension> findImages(File file, String prefix) throws IOException {
		file = file.getCanonicalFile();
		System.out.println(file.getPath());
		System.out.println(prefix);
		Map<String, Dimension> images = new HashMap<String, Dimension>();
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				String name = f.getName();
				String filePath = prefix + "/" + name;
				if (f.isDirectory() && !f.isHidden()) {
					images.putAll(findImages(f, filePath));
				} else if (f.isFile()) {
					if (name.endsWith(".gif") || name.endsWith(".jpg") || name.endsWith(".png")) {
						try {
							BufferedImage image = ImageIO.read(f);
							Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
							images.put(filePath, dimension);
						} catch (IOException e) {
						}
					}
				}
			}
		}
		return images;
	}

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		if (args.length > 0) {
			System.out.println("=== collecting image files ===");
			Map<String, Dimension> images = findImages(new File("."), "/media");
			System.out.println("=== found image files (" + images.size() + ") ===");
			for (Entry<String, Dimension> image : images.entrySet())
				System.out.println(image.getKey() + "=" + image.getValue().getWidth() + "x" + image.getValue().getHeight());
			MediaPatchTool tool = new MediaPatchTool();
			String location = args[0];
			System.out.println("=== processing media file (" + location + ") ===");
			tool.processMediaResource(location, images);
		} else
			throw new IllegalArgumentException("no command line arguments given");
	}

	private void processMediaResource(String location, Map<String, Dimension> images) throws IOException,
			ParserConfigurationException, SAXException, TransformerException {
		InputStream storeDataStream = new FileInputStream(location);
		boolean gzip = false;
		if (location.endsWith(".gz")) {
			storeDataStream = new GZIPInputStream(storeDataStream);
			gzip = true;
		}

		InputSource dataInputSource = new InputSource(storeDataStream);
		dataInputSource.setEncoding("UTF-8");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);

		DocumentBuilder builder = factory.newDocumentBuilder();
		System.out.println("=== +++ parsing file (" + location + ") +++ ===");
		Document document = builder.parse(dataInputSource);
		System.out.println("=== +++ updating Dom +++ ===");
		updateDom(document, images);
		Set<String> notUpdated = new HashSet<String>();
		for (Entry<String, Dimension> image : images.entrySet())
			if (!image.getValue().isUpdated())
				notUpdated.add(image.getKey());
		for (String path : notUpdated)
			System.err.println("warning: " + path + " is not in the Media file therefore dimensions cannot be updated");
		System.out.println("=== +++ writing working File +++ ===");
		File file = new File(location + "_new");
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		OutputStream outputStream = new FileOutputStream(file);
		if (gzip)
			outputStream = new GZIPOutputStream(outputStream);
		StreamResult result = new StreamResult(outputStream);
		DOMSource source = new DOMSource(document);
		transformer.transform(source, result);
		outputStream.close();
		System.out.println("=== +++ written working File (" + file.getAbsolutePath() + ") +++ ===");
	}

	private void updateDom(Document document, Map<String, Dimension> images) {
		NodeList contentNodes = document.getElementsByTagName("Content");
		if (contentNodes.getLength() > 0) {
			Node contentNode = contentNodes.item(0);
			NodeList childNodes = contentNode.getChildNodes();
			int c = 0;
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (childNode.getNodeName().equals("Image")) {
					NodeList imageAttrNodes = childNode.getChildNodes();
					Node path = findChildNodeByTagName(imageAttrNodes, "path");
					Node width = findChildNodeByTagName(imageAttrNodes, "width");
					Node height = findChildNodeByTagName(imageAttrNodes, "height");
					if (path != null && width != null && height != null) {
						String pathValue = path.getTextContent();
						if (images.containsKey(pathValue)) {
							System.out.println("updating file " + pathValue);
							Dimension dim = images.get(pathValue);
							if (width.getTextContent().equals(Integer.toString(dim.getWidth()))
									&& height.getTextContent().equals(Integer.toString(dim.getHeight())))
								System.err.println("warning: " + pathValue + " is being updated but no change in values");
							width.setTextContent(Integer.toString(dim.getWidth()));
							height.setTextContent(Integer.toString(dim.getHeight()));
							if (dim.isUpdated()) {
								System.err.println("warning: " + pathValue + " is already updated, maybe duplicate?");
							} else {
								dim.setUpdated(true);
								c++;
							}
						}
					} else {
						System.err.println("malformed Image node, either path, width or height is missing");
					}
				}
			}
			System.out.println("=== +++ updated " + c + " images +++ ===");
		} else
			throw new IllegalStateException("Illegal XML format: missing Content node");
	}

	private Node findChildNodeByTagName(NodeList childNodes, String tagName) {
		for (int i = 0; i < childNodes.getLength(); i++)
			if (childNodes.item(i).getNodeName().equals(tagName))
				return childNodes.item(i);
		return null;
	}
}
