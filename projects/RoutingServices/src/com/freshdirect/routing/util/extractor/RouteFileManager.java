package com.freshdirect.routing.util.extractor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RouteFileManager {
	
	public List parseRouteFile(String configurationPath,InputStream in) {
		
		
		List inputList = new ArrayList();
		/*ConfigurationReader parser = new ConfigurationReader();
        try {        	
            FileFormat ff = parser.loadConfigurationFile(this.getClass().getClassLoader().getResourceAsStream(configurationPath)); 
                        
            BufferedReader bufIn = new BufferedReader(new InputStreamReader(in));
            MatchedRecord results;
            while ((results = ff.getNextRecord(bufIn)) != null) {
                if (results.getRecordName().equals("orderinfo")) {
                	inputList.add(results.getBean("iorderinfo"));
                }
            }

        } catch (FlatwormUnsetFieldValueException flatwormUnsetFieldValueError) {
            flatwormUnsetFieldValueError.printStackTrace();  
        } catch (FlatwormConfigurationValueException flatwormConfigurationValueError) {
            flatwormConfigurationValueError.printStackTrace();
        } catch (FlatwormInvalidRecordException e) {
            e.printStackTrace(); 
        } catch (FlatwormInputLineLengthException e) {
            e.printStackTrace(); 
        } catch (FlatwormConversionException e) {
            e.printStackTrace(); 
        } */       
        return inputList;
	}
	
		
	
}
