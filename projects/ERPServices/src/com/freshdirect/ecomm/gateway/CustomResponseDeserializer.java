package com.freshdirect.ecomm.gateway;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.erp.EnumBatchStatus;
import com.freshdirect.erp.model.BatchHistoryModel;
import com.freshdirect.erp.model.BatchModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class CustomResponseDeserializer implements JsonDeserializer<Response<Collection<BatchModel>>> {

	@Override
	public Response< Collection<BatchModel>> deserialize(JsonElement json, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		  if (json == null)
	            return null;
		  	String dataCode = "OK";
	        JsonObject jo = json.getAsJsonObject();

            String timestamp = jo.get("timestamp").getAsString();
             String message = jo.get("message").getAsString();
              dataCode = jo.get("responseCode").getAsString();
             
             JsonArray data = jo.get("data").getAsJsonArray();
             
             Collection<BatchModel> batches = new ArrayList<BatchModel>();
             
             for(int i=0;i<data.size();i++){
            	 
            	 JsonElement element = data.get(i);
            	 JsonObject joo = element.getAsJsonObject();
            	 int batchnum= joo.get("batchNumber").getAsInt();
           	 
            	 BatchModel bm = new BatchModel();
            	 bm.setBatchNumber(batchnum);
            	 bm.setDescription(" ");
            	 
            	 JsonArray historydata = joo.get("history").getAsJsonArray();
            	 JsonElement historydataElement = historydata.get(0);
            	 JsonObject johL = historydataElement.getAsJsonObject();
            	 BatchHistoryModel bhm = new BatchHistoryModel();
            	 String statusDate= johL.get("statusDate").getAsString();
            	 Date date = new Date(Long.parseLong(statusDate));
            	 JsonObject status = johL.get("status").getAsJsonObject();
            	 String code= status.get("code").getAsString();
            	 bhm.setStatus(EnumBatchStatus.getStatus(code));
            	 bhm.setStatusDate(new Timestamp(date.getTime()));
            	 bm.updateStatus(bhm);
            	 batches.add(bm);
             }

            Date date = new Date(Long.parseLong(timestamp));
            Response<Collection<BatchModel>> response = new Response( dataCode,  message,  date, "");
            response.setData(batches);
	        return response;
	}
	}