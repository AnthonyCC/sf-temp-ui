package com.freshdirect.mobileapi.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.Html;

public class ProductUtil {
    private static final Logger LOG = Logger.getLogger(ProductUtil.class);

    public static final String MEDIA_PATH = FDStoreProperties.getMediaPath();

    public static URL resolve(String childPath) throws IOException {
        return resolve(MEDIA_PATH, childPath);
    }

    public static URL resolve(String rootPath, String childPath) throws IOException {
        URL url = new URL(rootPath);
        if (childPath.startsWith("/")) {
            childPath = childPath.substring(1, childPath.length());
        }
        url = new URL(url, childPath);

        if (!url.toString().startsWith(rootPath)) {
            throw new IOException("Child path not under root");
        }

        return url;
    }

    public static String readContent(URL url) throws IOException {
        LOG.debug("Reading content from: " + url.toString());
        Writer out = new StringWriter();
        InputStream in = null;
        in = url.openStream();

        if (in == null) {
            throw new FileNotFoundException();
        }

        byte[] buf = new byte[4096];
        int i;
        while ((i = in.read(buf)) != -1) {
            out.write(new String(buf, 0, i));
        }
        in.close();
        return out.toString();
    }

    public static String readHtml(Html htmlContent){
        String result = "";
        if (htmlContent != null) {
            try {
                result = readContent(ProductUtil.resolve(htmlContent.getPath()));
            } catch (IOException e) {
                LOG.warn("HTML Content filr " + htmlContent.getPath() + " not found");
            }
        }
        return result;
    }

}
