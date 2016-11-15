package com.freshdirect.cms._import.tasks;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.listeners.Media;
import com.freshdirect.cms.listeners.MediaDao;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * Imports content of Media.xml.gz file
 * 
 * @author segabor
 */
public class ImportMediaDataTask extends CMSTaskBase {
    private static final Logger LOGGER = Logger.getLogger(ImportMediaDataTask.class);

    public ImportMediaDataTask(String basePath) {
        super(basePath);
    }


    @Override
    public void run() {
        final File base = getStoreData();

        
        // locate Media.xml.gz file
        
        File fdFolder = new File(base, EnumEStoreId.FD.getContentId());
        if (fdFolder == null || !fdFolder.isDirectory()) {
            throw new RuntimeException("FreshDirect folder not found under storedata");
        }
        
        File mediaXml = new File(fdFolder, "Media.xml.gz");
        if (mediaXml == null || !mediaXml.isFile()) {
            throw new RuntimeException("Media.xml.gz not found");
        }

        
        // initialize Media content service
        final ContentServiceI storeManager = loadContentsFromXML("classpath:/com/freshdirect/cms/resource/MediaDef.xml", mediaXml); 

        // flush media table
        infraDao.flushMediaTable();
        
        final MediaDao dao = new MediaDao();

        final Collection<ContentKey> allKeys = storeManager.getContentKeys(DraftContext.MAIN);

        Connection connection = null;
        try {
            Collection<Media> mcoll = new ArrayList<Media>();
            
            int n = 1;
            int max = allKeys.size() / BATCH_SIZE;
            
            LOGGER.info("Start writing out " + max + " batches");
            
            for (final ContentKey contentKey : allKeys) {
                ContentNodeI mediaNode = storeManager.getContentNode(contentKey, DraftContext.MAIN);
                
                final String id = contentKey.getId();
                final PrimaryKey pk = new PrimaryKey(id);
                final String uri = (String) mediaNode.getAttributeValue("path");
                final ContentType t = contentKey.getType();
                final Date stamp = new Date();
                
                Media m = null;
                if (FDContentTypes.HTML.equals(contentKey.getType())) {
                    final String mimeType = getMimeType(uri);
                    
                    m = new Media(pk, uri, t, null, null, mimeType, stamp);
                } else if (FDContentTypes.IMAGE.equals(contentKey.getType())) {
                    String mimeType = getMimeType(uri);
                    Integer width = (Integer)mediaNode.getAttributeValue("width");
                    Integer height = (Integer)mediaNode.getAttributeValue("height");

                    m = new Media(pk, uri, t, width, height, mimeType, stamp);
                } else if (FDContentTypes.MEDIAFOLDER.equals(contentKey.getType())) {
                    m = new Media(pk, uri, t, null, null, null, stamp);
                }
                
                if (m == null) {
                    continue;
                }

                mcoll.add(m);

                if (mcoll.size() >= BATCH_SIZE) {
                    LOGGER.info("Writing out batch " + n + "/" + max + " ("+mcoll.size()+")");

                    connection = outDataSource.getConnection();
                    connection.setAutoCommit(false);
                    connection.setReadOnly(true);
                    
                    dao.insertBatch(connection, mcoll);
                    connection.commit();

                    mcoll.clear();

                    n++;
                }
            }

            
            // write out the rest
            if (! mcoll.isEmpty()) {
                LOGGER.info("Writing out the last batch ("+mcoll.size()+")");

                connection = outDataSource.getConnection();
                connection.setAutoCommit(false);
                connection.setReadOnly(true);
                
                dao.insertBatch(connection, mcoll);
                connection.commit();

                mcoll.clear();
            }

            LOGGER.info("Media import ended");

        } catch (SQLException e) {
            LOGGER.error("Exception raised during media insert", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close DB connection", e);
                }
            }
        }

        
    }

    private String getMimeType(String path) {
        final int ix = path.lastIndexOf(".");
        final String ext = path.substring(ix+1, path.length()).toLowerCase();

        if ("ds_store".equals(ext)) {
            // special case, it covers OSX's .DS_Store file
            return "application/octet-stream; charset=binary";
        } else if ("jpg".equals(ext) || "jpeg".equals(ext)) {
            return "image/jpeg";
        } else if ("gif".equals(ext)) {
            return "image/gif";
        } else if ("bmp".equals(ext)) {
            return "image/gif";
        } else if ("png".equals(ext)) {
            return "image/png";
        } else if ("mp4".equals(ext)) {
            return "video/mp4";
        } else if ("txt".equals(ext)) {
            return "text/plain; charset=us-ascii";
        }

        // default ??
        return "text/html";
    }
}
