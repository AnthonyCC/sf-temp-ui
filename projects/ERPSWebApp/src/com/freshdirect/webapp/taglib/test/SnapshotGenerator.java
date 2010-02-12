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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.FilteredSearchResults;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.CSVUtils;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.scoring.ScoringAlgorithm;
import com.freshdirect.smartstore.service.SearchScoringRegistry;

public class SnapshotGenerator {

    private static Logger LOG = Logger.getLogger(SnapshotGenerator.class);
    
    private static boolean running = false;
    
    private static StringBuilder status = new StringBuilder();

    public static boolean isRunning() {
        return running;
    }
    
    public static String getLog() {
        return status.toString();
    }

    /**
     * Start a separate thread which generates search results xml into a temporary directory.
     * 
     * @param input
     * @param label
     * @return
     */
    public static boolean startGenerating(final InputStream input, final String label, final FDUserI fdUser) {
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
                        generate(input, label, fos,
                        		fdUser != null && fdUser.getIdentity() != null ? fdUser.getIdentity().getErpCustomerPK() : null,
                        		fdUser != null ? fdUser.getPricingContext() : PricingContext.DEFAULT);
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
    public static boolean generate(InputStream input, String label, OutputStream output, String customerId, PricingContext pricingContext) throws IOException {
        synchronized (SnapshotGenerator.class) {
            if (running) {
                return false;
            }
            running = true;
        }
        try {
            PrintWriter p = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));
            p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            
            final ScoringAlgorithm scoring = (customerId!=null) ? 
                    SearchScoringRegistry.getInstance().getUserScoringAlgorithm() :
                        SearchScoringRegistry.getInstance().getGlobalScoringAlgorithm();
            final String factors = scoring.toString();
            
            p.println("<report name=\"" + label + "\" " + 
                    (customerId != null ? "userId=\"" + customerId + "\" " : "") +
                    "factors=\"" + factors + "\">");
            ContentSearch s = ContentSearch.getInstance();
            
            
            int termNum = 0;
            status.setLength(0);
            
            ScoreProvider provider = ScoreProvider.getInstance();
            @SuppressWarnings("unchecked")
            List<List<String>> rows = CSVUtils.parse(input, false, false);
            for (Iterator<List<String>> iter = rows.iterator(); iter.hasNext();) {
                List<String> row = iter.next();
                for (Iterator<String> riter = row.iterator(); riter.hasNext();) {
                    String term = riter.next().trim();
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

                        FilteredSearchResults fres = new FilteredSearchResults(term, res, customerId, pricingContext);
                        //fres.setScoreOracle(new FilteredSearchResults.HierarchicalGrouppingScoreOracle(CategoryNodeTree.createTree(fres.getProducts())));
                        CategoryNodeTree nodeTree = CategoryNodeTree.createTree(fres.getProducts(), true);
                        fres.setNodeTree(nodeTree);
                        fres.setScoreOracle(new FilteredSearchResults.HierarchicalScoreOracle(nodeTree));
                        fres.sortProductsBy(SearchSortType.BY_RELEVANCY, false);


                        status.append(" ("+fres.getProducts().size()+")\n");
                        
                        
                        for (int i=0;i<fres.getProducts().size();i++) {
                            ContentNodeModel model = (ContentNodeModel) fres.getProducts().get(i);
                            String fullname = StringUtils.replace(model.getFullName(), "&", "&amp;");
                            double[] scores = scoring.getScoreOf(customerId, pricingContext, provider, model);
                            StringBuilder sb = new StringBuilder();
                            for (double x : scores) {
                                sb.append(x).append(',');
                            }
                            p.println(" <result index=\"" + i + "\" id=\"" + model.getContentKey().getId() + "\" factorValues=\"" + sb + "\">" + fullname
                                    + "</result>");
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
