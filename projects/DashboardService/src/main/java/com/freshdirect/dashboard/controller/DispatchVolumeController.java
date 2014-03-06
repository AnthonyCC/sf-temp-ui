package com.freshdirect.dashboard.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.freshdirect.dashboard.data.response.Message;
import com.freshdirect.dashboard.exception.ErrorCodeDispatchVolumeEnum;
import com.freshdirect.dashboard.exception.ServiceException;
import com.freshdirect.dashboard.model.PlantDispatchData;
import com.freshdirect.dashboard.service.IPlantDispatchService;
import com.freshdirect.dashboard.util.DateUtil;

@Controller
@RequestMapping("/plantdispatch")
public class DispatchVolumeController extends BaseController {
	
	@Autowired
	private IPlantDispatchService plantDispatchService;
	
	/**
     * @return the list of PlantDispatchData
     * @throws ServiceException if the requested date is null.
     */
	@RequestMapping(value="/ordervolume", method=RequestMethod.GET)	
	public @ResponseBody Message getOrderVolume(WebRequest req,
			HttpServletResponse resp) {
		
		Message responseMessage = new Message();
		List<PlantDispatchData> dispatchData = null;		
		try {
			String deliveryDate = req.getParameter("deliveryDate");
			if(deliveryDate == null || ("".equals(deliveryDate))) {
				deliveryDate = DateUtil.getNextDate();			
			}
			dispatchData = plantDispatchService.getDispatchVolume(DateUtil.getDate(deliveryDate));
		} catch (ParseException pe) {
			throw new ServiceException(ErrorCodeDispatchVolumeEnum.INVALID_PARAMS, 
					"Delivery date is invalid");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ErrorCodeDispatchVolumeEnum.UNKNOWN_ERROR, 
					"Failed to load Dispatch volume data");
		}
		
		responseMessage.setData(dispatchData);
		responseMessage.setTotalRecords(dispatchData != null ? dispatchData.size() : 0);
		return responseMessage;
	}

}

