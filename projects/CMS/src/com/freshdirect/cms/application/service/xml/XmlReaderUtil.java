package com.freshdirect.cms.application.service.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;

public class XmlReaderUtil {

    /**
     * 
     * @param service
     * @param nodeHandler
     * @param stream
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Map<ContentKey, ContentNodeI> loadNodes(ContentServiceI service, CmsNodeHandler nodeHandler, InputStream stream)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        SAXParser parser = factory.newSAXParser();

        nodeHandler.setContentService(service);

        InputSource dataInputSource = new InputSource(stream);
        // dataInputSource.setEncoding("ISO-8859-1");
        dataInputSource.setEncoding("UTF-8");
        parser.parse(dataInputSource, nodeHandler);

        return nodeHandler.getContentNodes();
    }

    /**
     * 
     * @param service
     * @param stream
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Map<ContentKey, ContentNodeI> loadNodes(ContentServiceI service, InputStream stream) throws ParserConfigurationException, SAXException,
            IOException {
        return loadNodes(service, new FlexContentHandler(), stream);
    }

    /**
     * return a stream for the raw data, with detecting zip and gzip compression
     * based on the filename.
     * 
     * @param location
     * @param storeDataStream
     * @return
     * @throws IOException
     */
    public static InputStream decompressStream(String location, InputStream storeDataStream) throws IOException {
        if (location.toLowerCase().endsWith(".zip")) {
            storeDataStream = new ZipInputStream(storeDataStream);
            ((ZipInputStream) storeDataStream).getNextEntry();
        } else if (location.toLowerCase().endsWith(".gz")) {
            storeDataStream = new GZIPInputStream(storeDataStream);
        }
        return storeDataStream;
    }

}
