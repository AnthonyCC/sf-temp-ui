package com.freshdirect.cms.application;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlReaderUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmsImport implements Runnable {
    final static Logger LOG = LoggerFactory.getInstance(CmsImport.class);

    String location;
    InputStream input;
    ContentServiceI service;
    CmsResponseI response;

    String userName = "cmsimport";

    Exception err;

    ExecutorService executor;
    
    AtomicInteger readNodes = new AtomicInteger(0);
    AtomicInteger savedNodes = new AtomicInteger(0);
    

    public CmsImport(String location, InputStream input, ContentServiceI service) {
        super();
        LOG.info("CmsImport initated " + location + " input:" + input);
        this.location = location;
        this.input = input;
        this.service = service;
        this.executor = Executors.newFixedThreadPool(2);
    }

    public CmsImport(String location, InputStream input) {
        this(location, input, CmsManager.getInstance());
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            LOG.info("run");

            Map<ContentKey, ContentNodeI> nodes = XmlReaderUtil.loadNodes(service, new FlexContentHandler() {

                @Override
                protected ContentNodeI createNode(ContentKey key) {
                    ContentNodeI node = getContentService().getContentNode(key);
                    if (node == null) {
                        return getContentService().createPrototypeContentNode(key);
                    } else {
                        return node.copy();
                    }
                }

                @Override
                protected void nodeCreated(ContentNodeI node) {
                    boolean hasValue = false;
                    for (Iterator<String> names = node.getDefinition().getAttributeNames().iterator(); names.hasNext() && !hasValue; ) {
                        String name = names.next();
                        if (node.getAttributeValue(name) != null) {
                            hasValue = true;
                        }
                    }
                    if (hasValue) {
                        super.nodeCreated(node);
                        final Map<ContentKey, ContentNodeI> nodes = getContentNodes();
                        if (nodes.size() >= 1000) {
                            enqueSave(nodes);
                        }
                    }
                }


            }, XmlReaderUtil.decompressStream(location, input));

            /*            CmsRequest req = new CmsRequest(new CmsUser(userName, true, false), false);
            req.setNodes(nodes);
            response = service.handle(req);*/
            if (nodes.size() > 0) {
                enqueSave(nodes);
            }
            LOG.info("processed");
            executor.shutdown();
            
        } catch (Exception e) {
            LOG.error("error during loading :" + e.getMessage(), e);
            this.err = e;
        } finally {
            waitForFinish();
        }
    }

    /**
     * @throws InterruptedException
     */
    protected void waitForFinish() {
        
        try {
            while(!executor.awaitTermination(2000, TimeUnit.MILLISECONDS)) {
                int saved = savedNodes.get();
                int read = readNodes.get();
                LOG.info("waiting for tasks: " + getStatus(saved, read));
            }
        } catch (InterruptedException e) {
            LOG.info("Interrupted exception:"+e,e);
        }
    }
    
    String getStatus(int saved, int read) {
        return "(already read "+read+", saved:"+saved+", "+(read>0 ? ((double) saved*100 / read) : "NaN")+ "% )";
    }

    /**
     * @param nodes
     */
    protected void enqueSave(final Map<ContentKey, ContentNodeI> nodes) {
        final CmsRequest rq = new CmsRequest(new CmsUser(userName, true, false), true);
        rq.setNodes(new HashMap<ContentKey, ContentNodeI>(nodes));
        int current = readNodes.addAndGet(nodes.size());
        LOG.info("enqueue save " + nodes.size() +" nodes, "+getStatus(savedNodes.get(), current));
        nodes.clear();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long time = System.currentTimeMillis();
                    service.handle(rq);
                    int saved = savedNodes.addAndGet(rq.getNodes().size());
                    int read = readNodes.get();
                    LOG.info("saved nodes in "+(System.currentTimeMillis() - time)/1000+" s "+getStatus(saved, read));
                } catch (Throwable e) {
                    LOG.error("Error during saving nodes:" + e.getMessage(), e);
                }
            }
        });
    }
    
    public Exception getException() {
        return err;
    }

    public CmsResponseI getResponse() {
        return response;
    }

}
