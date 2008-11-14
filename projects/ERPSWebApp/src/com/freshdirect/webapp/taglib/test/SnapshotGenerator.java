package com.freshdirect.webapp.taglib.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.FilteredSearchResults;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.CSVUtils;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SmartSearchTag;

public class SnapshotGenerator {

    private static Logger LOG = Logger.getLogger(SnapshotGenerator.class);
    
    private static boolean running = false;
    
    private static StringBuffer status = new StringBuffer();

    public static boolean isRunning() {
        return running;
    }
    
    public static String getLog() {
        return status.toString();
    }

    public static boolean startGenerating(InputStream input, String label, HttpSession session) {
        FDUserI sessionUser = FDSessionUser.getFDSessionUser(session);
        String userId = sessionUser!=null ? sessionUser.getUserId() : null;
        return startGenerating(input, label, userId);
    }

    /**
     * Start a separate thread which generates search results xml into a temporary directory.
     * 
     * @param input
     * @param label
     * @return
     */
    public static boolean startGenerating(final InputStream input, final String label, final String customerId) {
        File directory = new File(FDStoreProperties.getTemporaryDirectory(),"search-snapshots");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File path = new File(directory, label.replace(':', ' ').replace('.', '_').replace(File.separatorChar, '_'));
        try {
            final FileOutputStream fos = new FileOutputStream(path);
            Thread t = new Thread("snapshot-generating-"+path.getName()) {
                public void run() {
                    try {
                        generate(input, label, fos, customerId);
                    } catch (IOException e) {
                        LOG.error("Error : "+e.getMessage(),e);
                    }
                }
            };
            t.setDaemon(true);
            t.start();
            return true;
        } catch (FileNotFoundException fe) {
            LOG.error("Error : "+fe.getMessage(),fe);
            return false;
        }
    }
    
    public static String[] getSnapshotNames() {
        File directory = new File(FDStoreProperties.getTemporaryDirectory(),"search-snapshots");
        String[] names = directory.list();
        if (names!=null) {
            Arrays.sort(names);
        }
        return names;
    }
    
    
    public static InputStream getSnapshotStream(String name) throws FileNotFoundException {
        return new FileInputStream(new File(new File(FDStoreProperties.getTemporaryDirectory(),"search-snapshots"), name));
    }
    
    /**
     * Generate search results, it's input is a CSV file, output is an XML file.
     * 
     * @param input
     * @param label
     * @param output
     * @return
     * @throws IOException
     */
    public static boolean generate(InputStream input, String label, OutputStream output, String customerId) throws IOException {
        synchronized (SnapshotGenerator.class) {
            if (running) {
                return false;
            }
            running = true;
        }
        try {
            PrintWriter p = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));
            p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            p.println("<report name=\""+label+"\">");
            ContentSearch s = ContentSearch.getInstance();
            
            
            int termNum = 0;
            status.setLength(0);
            
            List rows = CSVUtils.parse(input, false, false);
            for (Iterator iter = rows.iterator(); iter.hasNext();) {
                List row = (List) iter.next();
                for (Iterator riter = row.iterator(); riter.hasNext();) {
                    String term = ((String) riter.next()).trim();
                    if (term.length()>0) {
                        try {
                            Integer.parseInt(term);
                            continue;
                        } catch (Exception e) {
                            
                        }
                        LOG.info("search for "+term);
                        termNum++;
                        
                        p.println("<term name=\""+term+"\">");
                        
                        status.append(term);
                        
                        SearchResults res = s.search(term);

                        FilteredSearchResults fres = new FilteredSearchResults(term, res, customerId);
                        //fres.setScoreOracle(new FilteredSearchResults.HierarchicalGrouppingScoreOracle(CategoryNodeTree.createTree(fres.getProducts())));
                        CategoryNodeTree nodeTree = CategoryNodeTree.createTree(fres.getProducts());
                        fres.setNodeTree(nodeTree);
                        fres.setScoreOracle(new FilteredSearchResults.HierarchicalScoreOracle(nodeTree));
                        fres.sortProductsBy(new Integer(FilteredSearchResults.BY_RELEVANCY), false);


                        status.append(" ("+fres.getProducts().size()+")\n");
                        
                        
                        for (int i=0;i<fres.getProducts().size();i++) {
                            ContentNodeModel model = (ContentNodeModel) fres.getProducts().get(i);
                            String fullname = StringUtils.replace(model.getFullName(), "&", "&amp;");
                            
                            p.println(" <result index=\""+i+"\" id=\""+model.getContentKey().getId()+"\" >"+fullname +"</result>");
                        }
                        
                        p.println("</term>");
                        p.flush();
                    }
                }
                
            }
            p.println("</report>");
            p.flush();
            p.close();
            
            
            
            return true;
        } finally {
            synchronized (SnapshotGenerator.class) {
                running = false;
            }
        }
    }

}
