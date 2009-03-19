package com.freshdirect.webapp.taglib.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SearchSnapshot {

    public class SortByMovesComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            SearchData s1 = (SearchData) o1;
            SearchData s2 = (SearchData) o2;
            int d1 = s1.getDelta();
            int d2 = s2.getDelta();
            if (d1==d2) {
                return s1.id.compareTo(s2.id);
            } else {
                return (d1>d2 ? -1 : 1);
            }
        }

    }

    public static class SearchData {
        String resultFullName;
        String id;
        int[]  position = new int[2];

        public SearchData(String id) {
            this.id = id;
            position[0] = -1;
            position[1] = -1;
        }
        
        public String getResultFullName() {
            return resultFullName;
        }

        void setResultFullName(String resultFullName) {
            this.resultFullName = resultFullName;
        }

        public int getPositionA() {
            return position[0];
        }

        public void setPosition(int listNum, int positionA) {
            this.position[listNum] = positionA;
        }

        public int getPositionB() {
            return position[1];
        }

        public String getId() {
        	return id;
        }
 
        public String toString() {
            return "("+id+':'+resultFullName+')'+position[0]+':'+position[1];
        }
        
        public int getDelta() {
            return Math.abs(position[0] - position[1]);
        }

    }

    static class AggregateSearchResult {
        String term;
        double correlation;
        int maxMove;
        List[] results       = new List[] { new ArrayList(), new ArrayList() };
        /**
         * Map<String,SearchData>
         */
        Map    searchDataMap = new HashMap();

        
        
        
        public AggregateSearchResult(String term) {
            this.term = term;
        }

        SearchData getSearchData(String id) {
            SearchData sd = (SearchData) searchDataMap.get(id);
            if (sd == null) {
                sd = new SearchData(id);
                searchDataMap.put(id, sd);
            }
            return sd;
        }
        
        void calculateCorrelation() {
            List all = new ArrayList(results[0].size() + results[1].size());
            all.addAll(results[0]);
            // add SearchData record which are not present on the first list
            for (int i = 0; i < results[1].size(); i++) {
                SearchData s = (SearchData) results[1].get(i);
                if (s.position[0] < 0) {
                    all.add(s);
                }
            }
            int max = all.size();
            long sum = 0;
            for (int i=0;i<max;i++) {
                SearchData a = (SearchData) all.get(i);
                int a0 = a.position[0];
                if (a0<0) { a0 = max; }
                int a1 = a.position[1];
                if (a1<0) { a1 = max; }
                
                maxMove = Math.max(maxMove, Math.abs(a1-a0));
                int d2 = (a1-a0)*(a1-a0);
                sum += d2;
            }
            if (max>0) { 
                if (max>1) {
                    correlation = 1 - ((double) (6 * sum) / ((double) (max * ((max * max) - 1))));
                } else {
                    correlation = 1;
                }
            } else {
                correlation = -1;
            }
            //System.out.println("correlation : "+correlation);
        }

        void addBoth(SearchData sd) {
            results[0].add(sd);
            results[1].add(sd);
        }
        
    }

    /**
     * Map<String,AggregateSearchResult>
     */
    Map  searchResults = new HashMap();
    List searchTerms   = new ArrayList();

    /**
     * Set<String>
     * 
     * @return
     */
    public List getSearchTerms() {
        return searchTerms;
    }

    public double getCorrelationForTerm(String search) {
        AggregateSearchResult asr = (AggregateSearchResult) searchResults.get(search);
        if (asr != null) {
            return asr.correlation;
        }
        return 0;
    }

    /**
     * List<SearchData>
     * 
     * @param search
     * @return
     */
    public List getResultList(String search, int listNum) {
        return getResultList(search, listNum, false);
    }
    /**
     * List<SearchData>
     * 
     * @param search
     * @return
     */
    public List getResultList(String search, int listNum, boolean sortByMoves) {
        AggregateSearchResult asr = (AggregateSearchResult) searchResults.get(search);
        if (asr != null) {
            if (sortByMoves) {
                Collections.sort(asr.results[listNum], new SortByMovesComparator() );
            }
            return asr.results[listNum];
        }
        return null;
    }
    
    /**
     * Return a list of SearchData-s from both of the list.
     * 
     * @param search
     * @param sortByMoves
     * @return List<SearchData>
     */
    public List getAggregateResultList(String search, boolean sortByMoves) {
        AggregateSearchResult asr = (AggregateSearchResult) searchResults.get(search);
        if (asr != null) {
            final int A = 1;
            final int B = 0;
            List result = new ArrayList(asr.results[A].size());
            result.addAll(asr.results[A]);
            int offset = 0;
            for (int i=0;i<asr.results[B].size();i++) {
                SearchData object = (SearchData) asr.results[B].get(i);
                if (object.position[A]==-1) {
                    // missing from the first list
                    result.add(Math.max(0, Math.min(object.position[B] + offset,result.size()-1) ), object);
                    offset++;
                }
            }
            if (sortByMoves) {
                Collections.sort(result, new SortByMovesComparator());
            }
            return result;
        }
        return null;
    }
    
    
    public void init(InputStream input, InputStream inputB, String sortBy) throws ParserConfigurationException, SAXException, IOException {
        searchResults.clear();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser parser = factory.newSAXParser();
        parseXml(input, parser, 0);
        input.close();

        parseXml(inputB, parser, 1);
        inputB.close();
        calculateCorrelation();
        
        sort(sortBy);
    }

    void sort(String sortBy) {
        if (sortBy==null) {
            // no need to sort
            return;
        }
        if ("popularity".equals(sortBy)) {
            // already sorted by popularity
            return;
        }
        Comparator comp = new NameComparator();
        if ("correlation".equals(sortBy)) {
            comp = new CorrelationComparator();
        }
        if ("moves".equals(sortBy)) {
            comp = new GreatesMovesComparator();
        }
        TreeSet set = new TreeSet(comp);
        set.addAll(searchResults.values());
        searchTerms = new ArrayList(set.size());
        for (Iterator iter = set.iterator();iter.hasNext();) {
            searchTerms.add(((AggregateSearchResult)iter.next()).term);
        }
    }
    
    static class NameComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            AggregateSearchResult a1 = (AggregateSearchResult) o1;
            AggregateSearchResult a2 = (AggregateSearchResult) o2;
            return a1.term.compareTo(a2.term);
        }
    }
    
    static class CorrelationComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            AggregateSearchResult a1 = (AggregateSearchResult) o1;
            AggregateSearchResult a2 = (AggregateSearchResult) o2;
            int comp = Double.compare(a1.correlation,a2.correlation);
            if (comp==0) {
                comp = a1.term.compareTo(a2.term);
            }
            return comp;
        }
    }
    
    static class GreatesMovesComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            AggregateSearchResult a1 = (AggregateSearchResult) o1;
            AggregateSearchResult a2 = (AggregateSearchResult) o2;
            if (a1.maxMove>a2.maxMove) {
                return -1;
            } else {
                if (a2.maxMove>a1.maxMove) {
                    return 1;
                } 
                return a1.term.compareTo(a2.term);
            }
        }
    }
    

    AggregateSearchResult getAggregateSearchResult(String term) {
        AggregateSearchResult asr = (AggregateSearchResult) searchResults.get(term);
        if (asr==null) {
            asr = new AggregateSearchResult(term);
            searchResults.put(term, asr);
            searchTerms.add(term);
        }
        return asr;
    }
    
    private void parseXml(InputStream input, SAXParser parser, final int mode) throws SAXException, IOException {
        parser.parse(input, new DefaultHandler() {
            AggregateSearchResult asr         = null;
            SearchData            data        = null;

            public void startElement(String uri, String localName,
                    String name, Attributes attributes) {
                if ("term".equals(name)) {
                    asr = getAggregateSearchResult(attributes.getValue("name"));
                } else if ("result".equals(name)) {
                    data = asr.getSearchData(attributes.getValue("id"));
                    data.setPosition(mode, Integer.parseInt(attributes.getValue("index")));
                }
            }

            public void endElement(String uri, String localName, String name) throws SAXException {
                if ("term".equals(name)) {
                    asr = null;
                } else if ("result".equals(name)) {
                    asr.results[mode].add(data);
                    data = null;
                }
            }

            public void characters(char[] ch, int start, int length) throws SAXException {
                if (data != null) {
                    String str = new String(ch, start, length);
                    str = str.trim();
                    data.setResultFullName(str);
                }
            }
        });
    }
    
    public void calculateCorrelation() {
        for (Iterator iter = searchResults.values().iterator();iter.hasNext();) {
            AggregateSearchResult a = (AggregateSearchResult) iter.next();
            a.calculateCorrelation();
        }
    }
    
    
    
    public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        AggregateSearchResult as = new AggregateSearchResult ("x");
        SearchData data = as.getSearchData("aaaa");
        data.position[0] = 2;
        data.position[1] = 0;
        as.addBoth(data);

        data = as.getSearchData("bbbb");
        data.position[0] = 1;
        data.position[1] = 1;
        as.addBoth(data);

        data = as.getSearchData("cccc");
        data.position[0] = 0;
        data.position[1] = 2;
        as.addBoth(data);
        
        as.calculateCorrelation();
        System.out.println("CORRELATION:"+as.correlation);
        
        SearchSnapshot ss = new SearchSnapshot();
        ss.init(new FileInputStream("../FDWebSite/docroot/test/search/snapshots/dev_1.xml"), new FileInputStream("../FDWebSite/docroot/test/search/snapshots/dev_name.xml"), null);
    }
    
}
