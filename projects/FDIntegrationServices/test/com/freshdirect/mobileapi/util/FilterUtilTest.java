package com.freshdirect.mobileapi.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FilterUtilTest {

    @Test
    public void testFilter(){
        List<String> names = new ArrayList<String>();
        names.add("John");
        names.add("Tom");
        names.add("Felipe");
        
        FilterUtil<String> filterUtil = new FilterUtil<String>(names);
        filterUtil.addFilter(new ThreeLettersNameFilterCondition());
        
        List<String> filteredNames = filterUtil.filter();
        Assert.assertEquals(1, filteredNames.size());
        
        filterUtil.removeAllConditions();
        filterUtil.addFilter(new FelipeNameFilterCondition());
        for(String name : filterUtil.filter()){
            Assert.assertEquals(true, "Felipe".equals(name));
        }
    }
    /**
     * Filters any name that has three letters length
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
     */
    class FelipeNameFilterCondition implements Filter<String>{

        @Override
        public boolean isFiltered(String object) {
            return "Felipe".equals(object);
        }
        
    }
}
