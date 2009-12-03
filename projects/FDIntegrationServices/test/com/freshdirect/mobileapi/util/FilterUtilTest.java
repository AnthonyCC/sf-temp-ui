package com.freshdirect.mobileapi.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;


public class FilterUtilTest extends TestCase{
    public static final Logger logger = Logger.getLogger(FilterUtilTest.class);
    
    public void testFilter(){
        List<String> names = new ArrayList<String>();
        names.add("John");
        names.add("Tom");
        names.add("Felipe");
        
        FilterUtil<String> filterUtil = new FilterUtil<String>(names);
        filterUtil.addFilter(new ThreeLettersNameFilterCondition());
        
        List<String> filteredNames = filterUtil.filter();
        assertEquals(1, filteredNames.size());
        
        filterUtil.removeAllConditions();
        filterUtil.addFilter(new FelipeNameFilterCondition());
        for(String name : filterUtil.filter()){
            assertEquals(true, "Felipe".equals(name));
        }
    }
    /**
     * Filters any name that has three letters length
     * @author fgarcia
     *
     */
    class ThreeLettersNameFilterCondition implements Filter<String>{

        @Override
        public boolean isFiltered(String object) {
            boolean result = false;
            if ( object.length() == 3){
                result = true;
            }
            return result;
        }
        
    }
    
    /**
     * Filters any name that
     * @author fgarcia
     *
     */
    class FelipeNameFilterCondition implements Filter<String>{

        @Override
        public boolean isFiltered(String object) {
            return "Felipe".equals(object);
        }
        
    }
}
