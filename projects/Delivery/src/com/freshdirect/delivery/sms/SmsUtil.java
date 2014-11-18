package com.freshdirect.delivery.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Category;



import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sms.model.st.STSmsResponse;

public class SmsUtil {
	private static final Category LOGGER = LoggerFactory.getInstance(SmsUtil.class);
	
	
	public static List<STSmsResponse> getEtaSmsUpdateList(List<SmsAlertETAInfo> etaInfoList){
		List<STSmsResponse> etaSmsUpdateList = new ArrayList<STSmsResponse>();
		
		int numOfThreads = DlvProperties.getSmsETANumOfThreads();
		ExecutorService es = Executors.newFixedThreadPool(numOfThreads);
		List<Future<EtaSmsCommand>> tasks = new ArrayList<Future<EtaSmsCommand>>();
		List<List<SmsAlertETAInfo>> partitions = SmsUtil.chopped(etaInfoList, numOfThreads);
		
		for(List<SmsAlertETAInfo> etaInfoListPart : partitions){
			EtaSmsCommand tmpCommand = new EtaSmsCommand();
			tmpCommand.setEtaInfoList(etaInfoListPart);
			Future<EtaSmsCommand> future = es.submit(tmpCommand, tmpCommand);
			tasks.add(future);
		}
		try{
			
			es.shutdown();
			/*while(!es.isTerminated()){
				
			}*/
			for(Future<EtaSmsCommand> future : tasks){
				EtaSmsCommand response = future.get();
				etaSmsUpdateList.addAll(response.getStSmsResponses());
				
			}
		}catch (ExecutionException e) {
	    	e.printStackTrace();
		      //throw new RoutingServiceException();
		    } catch (InterruptedException ie) {
		    	ie.printStackTrace();
		      //throw new RoutingServiceException();
		    }
		
		return etaSmsUpdateList;
	}
	
	static <T> List<List<T>> chopped(List<T> list, final int L) {
		int chunksize = (int) (Math.ceil(new Double(list.size())/L));
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += chunksize) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + chunksize)))
	        );
	    }
	    return parts;
	}

}
